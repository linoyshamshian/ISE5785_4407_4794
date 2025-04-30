package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;

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
//    private ImageWriter imageWriter;

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

        Vector v = pIJ.subtract(p0).normalize();
        return new Ray(p0, v);
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
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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

//        /**
//         * Sets the resolution of the view plane.
//         * currently a placeholder for future implementation.
//         *
//         * @param nX number of pixels in X direction
//         * @param nY number of pixels in Y direction
//         * @return the current Builder
//         */
//        public Builder setResolution(int nX, int nY) {
//            return this;
//        }

//        public Builder setImageWriter(ImageWriter imageWriter) {
//            camera.imageWriter = imageWriter;
//            return this;
//        }
//        public Builder setRayTracer(RayTracerBase tracer) {
//            camera.rayTracer = tracer;
//            return this;
//        }

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

//            if (camera.imageWriter == null)
//                throw new MissingResourceException(GENERAL_DESCRIPTION, CLASS_NAME, "imageWriter");
//            if (camera.rayTracer == null)
//                throw new MissingResourceException(GENERAL_DESCRIPTION, CLASS_NAME, "rayTracer");
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
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.viewPlanePC = camera.p0.add(camera.vTo.scale(camera.distance));
            return (Camera) camera.clone(); // Cloneable – get a full shadow copy
        }
    } // end of Builder class
} // end of Camera class



