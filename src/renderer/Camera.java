package renderer;

import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

import static primitives.Color.colorDistance;
import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a camera in 3D space, defined by its position and orientation.
 * It is used for ray tracing by constructing rays through a view plane.
 *
 * @author Chen Babay & Linoy Shamshian
 */

public class Camera implements Cloneable {
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double distance = 0.0;
    private double width = 0.0;
    private double height = 0.0;
    //View plane center point to save CPU time - it's always the same
    private Point viewPlanePC;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int nX = 1;
    private int nY = 1;
    private Blackboard blackboard;
    private boolean useAdaptiveSuperSampling = false;
    private int assMaxDepth = 3;
    private double assTolerance = 10.0;
    /**
     * Amount of threads to use fore rendering image by the camera
     */
    private int threadsCount = 0;
    /**
     * Amount of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /**
     * Default constructor - private for use by Builder only.
     */
    private Camera() {
    }


    /**
     * Returns a new instance of the Camera builder.
     *
     * @return a {@link Builder} for building Camera objects.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through the center of a given pixel on the view plane.
     * <p>
     * The method receives four parameters:
     * <ul>
     * <li>{@code nX} – the number of pixels along the X-axis (the width resolution)</li>
     * <li>{@code nY} – the number of pixels along the Y-axis (the height resolution)</li>
     * <li>{@code j} – the pixel index along the X-axis (horizontal index, column)</li>
     * <li>{@code i} – the pixel index along the Y-axis (vertical index, row)</li>
     * </ul>
     * <p>
     * The coordinate system is geometric and centered at the middle of the view plane,
     * where the X-axis represents the width (horizontal direction) and the Y-axis represents the height (vertical direction).
     * In contrast to matrix access order (which uses [i][j] → row, column),
     * the parameters here are ordered to follow the geometric coordinate convention: width (X) first, then height (Y).
     *
     * @param nX number of columns (resolution in width)
     * @param nY number of rows (resolution in height)
     * @param j  pixel index in X-axis (column)
     * @param i  pixel index in Y-axis (row)
     * @return the constructed {@link Ray} through the center of the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double rX = width / nX;
        double rY = height / nY;
        double xJ = (j - (nX - 1) / 2d) * rX;
        double yI = -(i - (nY - 1) / 2d) * rY;

        Point pIJ = viewPlanePC;

        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }
        // If the ray is exactly at the camera origin, return the forward direction directly
        Vector direction;
        if (pIJ.equals(p0)) {
            direction = vTo;
        } else {
            direction = pIJ.subtract(p0).normalize();
        }
        return new Ray(p0, direction);
    }


    /**
     * Computes the 3D center of a pixel on the view plane.
     */
    private Point getPixelCenter(int nX, int nY, int j, int i) {
        double rX = width / nX;
        double rY = height / nY;
        double xJ = (j - (nX - 1) / 2d) * rX;
        double yI = -(i - (nY - 1) / 2d) * rY;
        Point pIJ = viewPlanePC;
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));
        return pIJ;
    }


    /**
     * Constructs a list of rays for super-sampling through a given pixel on the view plane,
     * using the configured blackboard.
     */
    public List<Ray> constructBeamRays(int nX, int nY, int j, int i) {
        List<Ray> rays = new ArrayList<>();
        // Compute pixel center and pixel axes (scaled to pixel size)
        Point pixelCenter = getPixelCenter(nX, nY, j, i);
        Vector pixelRight = vRight.scale(width / nX);
        Vector pixelUp = vUp.scale(height / nY);

        // Get all 3D sample points in the pixel
        List<Point> samplePoints = blackboard.generateSamples3D(pixelCenter, pixelRight, pixelUp);

        // For each sample point, create a ray from the camera origin (p0) through the sample
        for (Point sample : samplePoints) {
            rays.add(new Ray(p0, sample.subtract(p0).normalize()));
        }
        return rays;
    }

    /**
     * Casts a single ray through a pixel, gets its color and writes it to the image.
     *
     * @param j column index of the pixel
     * @param i row index of the pixel
     */
    private void castRay(int j, int i) {
        Color color;
        if (useAdaptiveSuperSampling) {
            Point pixelCenter = getPixelCenter(nX, nY, j, i);
            Vector pixelRight = vRight.scale(width / nX);
            Vector pixelUp = vUp.scale(height / nY);
            color = adaptiveSuperSample(pixelCenter, pixelRight, pixelUp, 0, assMaxDepth, assTolerance);
        } else if (blackboard.isEnabled()) {
            List<Ray> rays = constructBeamRays(nX, nY, j, i);
            color = Color.BLACK;
            for (Ray ray : rays) {
                color = color.add(rayTracer.traceRay(ray));
            }
            color = color.scale(1.0 / rays.size());
        } else {
            Ray ray = constructRay(nX, nY, j, i);
            color = rayTracer.traceRay(ray);
        }
        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }

    /**
     * This function renders image's pixel color map from the scene
     * included in the ray tracer object
     *
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }

    /**
     * Draws a grid over the image by first filling the background
     * and then drawing vertical and horizontal grid lines at a given interval.
     *
     * @param interval number of pixels between grid lines
     * @param color    the color of the grid lines
     * @return this camera instance
     */
    public Camera printGrid(int interval, Color color) {
        // Fill background with the existing image colors
        // Draw horizontal grid lines
        for (int i = 0; i < nY; i += interval) {
            for (int j = 0; j < nX; j++) {
                imageWriter.writePixel(j, i, color);
            }
        }

        // Draw vertical grid lines
        for (int j = 0; j < nX; j += interval) {
            for (int i = 0; i < nY; i++) {
                imageWriter.writePixel(j, i, color);
            }
        }
        return this;
    }

    /**
     * Writes the image to disk.
     *
     * @param filename name of the image file (without extension)
     * @return this camera instance
     */
    public Camera writeToImage(String filename) {
        imageWriter.writeToImage(filename);
        return this;
    }


    /**
     * Clones the current Camera object.
     * Performs a shallow copy; override and adjust if deep copy is needed for mutable fields.
     *
     * @return a cloned {@code Camera} object
     * @throws AssertionError if cloning is not supported, though this should not occur since the class is Cloneable
     */
    @Override
    public Camera clone() {
        try {
            Camera clone = (Camera) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Adaptive Super Sampling for a single pixel.
     * Recursively subdivides the pixel and samples only where needed.
     *
     * @param center    The 3D center of the pixel/sub-pixel
     * @param right     The right vector (scaled to sub-pixel width)
     * @param up        The up vector (scaled to sub-pixel height)
     * @param depth     Current recursion depth
     * @param maxDepth  Max recursion depth (settable from test)
     * @param tolerance Color difference threshold (settable from test)
     * @return The averaged color for the pixel/sub-pixel
     */
    private Color adaptiveSuperSample(Point center, Vector right, Vector up, int depth, int maxDepth, double tolerance) {
        // Sample 4 corners and the center
        Point[] points = {
                center.add(right.scale(-0.5)).add(up.scale(-0.5)), // bottom-left
                center.add(right.scale(0.5)).add(up.scale(-0.5)),  // bottom-right
                center.add(right.scale(-0.5)).add(up.scale(0.5)),  // top-left
                center.add(right.scale(0.5)).add(up.scale(0.5))    // top-right
        };

        Color[] colors = new Color[4];
        for (int i = 0; i < 4; i++) {
            Ray ray = new Ray(p0, points[i].subtract(p0));
            colors[i] = rayTracer.traceRay(ray);
        }

        // If all corner colors are "similar" (within tolerance) or we've reached the maximum recursion depth,
        // return the average color for this region.
        if (isUniform(colors, tolerance) || depth >= maxDepth) {
            return average(colors);
        }

        // Otherwise, subdivide the region into 4 sub-quadrants and recursively sample each.
        Color[] subColors = new Color[4];
        Vector halfRight = right.scale(0.5);
        Vector halfUp = up.scale(0.5);
        // Centers of 4 sub-pixels (quarter pixels)
        Point[] subCenters = {
                center.add(right.scale(-0.25)).add(up.scale(-0.25)), // bottom-left
                center.add(right.scale(0.25)).add(up.scale(-0.25)),  // bottom-right
                center.add(right.scale(-0.25)).add(up.scale(0.25)),  // top-left
                center.add(right.scale(0.25)).add(up.scale(0.25))    // top-right
        };
        for (int i = 0; i < 4; i++) {
            subColors[i] = adaptiveSuperSample(subCenters[i], halfRight, halfUp, depth + 1, maxDepth, tolerance);
        }
        return average(subColors);
    }

    /**
     * Checks if all colors in the array are within the given tolerance.
     * <p>
     * Tolerance meaning:
     * - High tolerance = less sensitive, fewer recursions, faster rendering, but less sharp edges.
     * - Low tolerance  = more sensitive, more recursions, slower rendering, but higher edge quality.
     *
     * @param colors    Array of Color objects to compare.
     * @param tolerance Maximum allowed color distance for colors to be considered "similar".
     * @return true if all color pairs are within the tolerance, false otherwise.
     */
    private boolean isUniform(Color[] colors, double tolerance) {
        for (int i = 0; i < colors.length; i++)
            for (int j = i + 1; j < colors.length; j++)
                if (colorDistance(colors[i], colors[j]) > tolerance)
                    return false;
        return true;
    }

    /**
     * Returns the average color from an array of colors.
     *
     * @param colors Array of Color objects.
     * @return The average color.
     */
    private Color average(Color[] colors) {
        Color sum = Color.BLACK;
        for (Color c : colors) sum = sum.add(c);
        return sum.scale(1.0 / colors.length);
    }

    /**
     * Render image using multi-threading by parallel streaming
     *
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Render image without multi-threading
     *
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     *
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {
        }
        return this;
    }

    /**
     * Builder class for {@link Camera}. Follows the Builder design pattern.
     * Allows step-by-step construction of a Camera object with flexible configuration.
     */
    public static class Builder { // this is the way for internal class
        private final Camera camera = new Camera();

        /**
         * Default constructor for the {@link Builder} class.
         * Initializes a new builder instance for constructing a {@link Camera} object.
         */
        public Builder() {
        }

        /**
         * Sets the location (eye point) of the camera in space.
         *
         * @param location a {@link Point} representing the camera's position
         * @return the current Builder (for method chaining)
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Sets the orientation of the camera using 'to' and 'up' vectors.
         * The two vectors must be orthogonal.
         *
         * @param vTo the direction vector the camera is facing
         * @param vUp the up vector of the camera
         * @return the current Builder (for method chaining)
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            vTo = vTo.normalize();
            vUp = vUp.normalize();
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal (dot product must be zero)");
            }
            camera.vTo = vTo;
            camera.vUp = vUp;
            camera.vRight = vTo.crossProduct(vUp).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and an up vector.
         * The method computes the vTo and vRight vectors automatically.
         *
         * @param cameraTarget a point that the camera should face
         * @param vUp          the up direction vector
         * @return the current Builder (for method chaining)
         */
        public Builder setDirection(Point cameraTarget, Vector vUp) {
            camera.vTo = cameraTarget.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the direction using a target point, assuming the default up vector (0, 0, 1).
         *
         * @param cameraTarget a point that the camera should face
         * @return the current Builder (for method chaining)
         */
        public Builder setDirection(Point cameraTarget) {
            Vector defaultUp = new Vector(0, 1, 0);
            // Delegate to the previous method
            return setDirection(cameraTarget, defaultUp);
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  view plane width
         * @param height view plane height
         * @return the current Builder (for method chaining)
         * @throws IllegalArgumentException if dimensions are non-positive
         */
        public Builder setVpSize(double width, double height) {
            if (Util.alignZero(width) <= 0 || Util.alignZero(height) <= 0)
                throw new IllegalArgumentException("View plane width and height must be greater than zero");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance between the camera and the view plane.
         *
         * @param distance view plane distance
         * @return the current Builder (for method chaining)
         * @throws IllegalArgumentException if distance is non-positive
         */
        public Builder setVpDistance(double distance) {
            if (Util.alignZero(distance) <= 0)
                throw new IllegalArgumentException("View plane distance must be greater than zero");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution (number of pixels in X and Y).
         *
         * @param nX number of pixels along the X axis
         * @param nY number of pixels along the Y axis
         * @return this builder
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0)
                throw new IllegalArgumentException("Resolution values must be positive");

            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene to trace
         * @param type  the type of the ray tracer
         * @return this builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            } else {
                camera.rayTracer = null;
            }
            return this;
        }

        /**
         * Set the blackboard
         *
         * @param blackboard the blackboard
         * @return the camera builder
         */
        public Builder setBlackboard(Blackboard blackboard) {
            camera.blackboard = blackboard;
            return this;
        }

        /**
         * Enables or disables adaptive super sampling for this camera.
         *
         * @param useAdaptiveSuperSampling true to enable adaptive super sampling, false to disable
         * @return this builder instance (for method chaining)
         */
        public Builder setUseAdaptiveSuperSampling(boolean useAdaptiveSuperSampling) {
            camera.useAdaptiveSuperSampling = useAdaptiveSuperSampling;
            return this;
        }

        /**
         * Sets the maximum recursion depth for adaptive super sampling.
         *
         * @param assMaxDepth the maximum recursion depth (must be >= 0)
         * @return this builder instance (for method chaining)
         * @throws IllegalArgumentException if assMaxDepth is negative
         */
        public Builder setAssMaxDepth(int assMaxDepth) {
            if (assMaxDepth < 0)
                throw new IllegalArgumentException("Maximum recursion depth must be non-negative");
            camera.assMaxDepth = assMaxDepth;
            return this;
        }

        /**
         * Sets the color difference tolerance for adaptive super sampling.
         *
         * @param assTolerance the color difference threshold (must be >= 0)
         * @return this builder instance (for method chaining)
         * @throws IllegalArgumentException if assTolerance is negative
         */
        public Builder setAssTolerance(double assTolerance) {
            if (assTolerance < 0)
                throw new IllegalArgumentException("Tolerance must be non-negative");
            camera.assTolerance = assTolerance;
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         *
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }


        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         *
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }


        /**
         * Finalizes the building of the {@link Camera} object.
         * Verifies that all required components are set and valid.
         *
         * @return a cloned and fully initialized {@code Camera} object
         * @throws MissingResourceException if required fields are missing
         * @throws IllegalArgumentException if camera vectors are invalid
         */
        public Camera build() {
            final String GENERAL_DESCRIPTION = "Missing render data";
            final String CLASS_NAME = "Camera";


            if (camera.p0 == null)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CLASS_NAME, "p0");
            if (camera.vUp == null)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CLASS_NAME, "vUp");
            if (camera.vTo == null)
                throw new MissingResourceException(GENERAL_DESCRIPTION, CLASS_NAME, "vTo");
            if (Util.alignZero(camera.width) <= 0)
                throw new IllegalArgumentException("Width must be positive");
            if (Util.alignZero(camera.height) <= 0)
                throw new IllegalArgumentException("Height must be positive");
            if (Util.alignZero(camera.distance) <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            if (!Util.isZero(camera.vTo.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("Resolution must be positive");
            if (camera.blackboard == null)
                camera.blackboard = new Blackboard();
            if (camera.assMaxDepth < 0)
                throw new IllegalArgumentException("ASS maxDepth must be non-negative");
            if (camera.assTolerance < 0)
                throw new IllegalArgumentException("ASS tolerance must be non-negative");

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null); // default to empty scene
            }
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.viewPlanePC = camera.p0.add(camera.vTo.scale(camera.distance));
            return (Camera) camera.clone(); // Cloneable – get a full shadow copy
        }
    }
}