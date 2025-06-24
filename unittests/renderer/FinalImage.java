package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FinalImage {

    private final Scene scene = new Scene("Beautiful Art Mountains By Linoy And Chen");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    private static final String TEXT_FILE_PATH = "python_code/mountains_data.txt";
    private static final String MOON_TEXTURE_PATH = "python_code/moon.png";

    // Output image resolution
    private static final int IMAGE_RESOLUTION_HEIGHT = 1300;
    private static final double VIEWPORT_BASE_SIZE = 1300;

    private static final double SAFETY_MARGIN = 1.0; // Set to 1.0 to attempt to fill the image without cutting content

    /**
     * Z-coordinate for the plane where the triangles are placed
     */
    private static final double Z_PLANE = 0.01;

    // Constants for the moon
    private static final double MOON_RADIUS = 150;
    private static final double MOON_DOT_SAMPLING_STEP = 3;
    private static final double MOON_DOT_RADIUS_FACTOR = 0.03;

    @Test
    void renderPolyArtFromText() throws IOException {
        /* ---------------------------------------------------------
         * 1. Read all lines from the file
         * --------------------------------------------------------- */
        List<String> lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
        if (lines.isEmpty()) {
            System.err.println("No triangle data found – exiting");
            return;
        }

        /* ---------------------------------------------------------
         * 2. First pass: Calculate bounds (min/max)
         * --------------------------------------------------------- */
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
            String[] p = line.split(",");
            if (p.length != 12) continue;

            double[] xs = {Double.parseDouble(p[0].trim()),
                    Double.parseDouble(p[3].trim()),
                    Double.parseDouble(p[6].trim())};

            double[] ys = {Double.parseDouble(p[1].trim()),
                    Double.parseDouble(p[4].trim()),
                    Double.parseDouble(p[7].trim())};

            for (double x : xs) {
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
            }
            for (double y : ys) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
        }

        double originalWidth = maxX - minX;
        double originalHeight = maxY - minY;

        //----In case we do not define the width----
        // Ensure originalHeight is not zero to prevent division by zero
        if (originalHeight == 0) {
            System.err.println("Error: originalHeight is zero, cannot calculate aspect ratio correctly.");
            originalHeight = 1; // Prevent division by zero
        }
        // Calculate aspect ratio of the mountain data
        double originalAspectRatio = originalWidth / originalHeight;

        // Calculate IMAGE_RESOLUTION_WIDTH, ensuring it's at least 1 pixel wide
        int IMAGE_RESOLUTION_WIDTH = (int) Math.max(1, Math.round(originalAspectRatio * IMAGE_RESOLUTION_HEIGHT));
        //---- ----

        // Calculate aspect ratio of the target image resolution
        double targetAspectRatio = (double) IMAGE_RESOLUTION_WIDTH / IMAGE_RESOLUTION_HEIGHT;
        double effectiveViewportWidth;
        double effectiveViewportHeight;
        double overallScaleFactor;

        // Determine how to fit the mountain content into the target image aspect ratio
        if (originalAspectRatio > targetAspectRatio) { // Mountain is wider than the target image
            effectiveViewportWidth = VIEWPORT_BASE_SIZE; // Fit by width
            overallScaleFactor = effectiveViewportWidth / originalWidth;
        } else { // Mountain is taller or same aspect ratio as the target image
            effectiveViewportHeight = VIEWPORT_BASE_SIZE; // Fit by height
            overallScaleFactor = effectiveViewportHeight / originalHeight;
        }
        // Apply safety margin to the overall scale factor
        overallScaleFactor *= SAFETY_MARGIN;
        // Recalculate effective viewport dimensions after applying overallScaleFactor
        effectiveViewportWidth = originalWidth * overallScaleFactor;
        effectiveViewportHeight = originalHeight * overallScaleFactor;

        // These offsets center the mountain within the *effective* viewport
        // This shifts the model so that its center (after scaling) will be at the origin (0,0) of the display or canvas.
        // The center is found by averaging the min and max coordinates, then scaling that value.
        // The negative sign moves the model so its center aligns with the center of the view.
        double offsetX = -((minX + maxX) / 2.0) * overallScaleFactor;
        double offsetY = -((minY + maxY) / 2.0) * overallScaleFactor;

        /* ---------------------------------------------------------
         * 4. Create Triangles – Second Pass
         * --------------------------------------------------------- */
        Material triMat = new Material().setKD(0.6).setKS(0.2).setShininess(10);

        int added = 0;
        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
            String[] p = line.split(",");
            if (p.length != 12) continue;

            //Convert the values to numbers
            List<Integer> v = Arrays.stream(p).map(String::trim)
                    .map(Integer::parseInt).collect(Collectors.toList());

            // Convert three points
            Point[] pts = new Point[3];
            for (int i = 0; i < 3; i++) {
                double x = v.get(i * 3);
                double y = v.get(i * 3 + 1);

                // Y inversion (coordinate system adjustment)
                // Flip Y coordinate to match image coordinate system where Y=0 is top
                y = maxY - (y - minY);

                pts[i] = new Point(
                        x * overallScaleFactor + offsetX, // Use overallScaleFactor
                        y * overallScaleFactor + offsetY, // Use overallScaleFactor
                        Z_PLANE
                );
            }

            Color col = new Color(v.get(9), v.get(10), v.get(11));
            scene.geometries.add(new Triangle(pts[0], pts[1], pts[2])
                    .setMaterial(triMat)
                    .setEmission(col));
            added++;
        }
        System.out.printf("Added %,d triangles%n", added);

        /* ---------------------------------------------------------
         * Moon Texture Generation
         * --------------------------------------------------------- */
        BufferedImage moonTexture;
        try {
            moonTexture = ImageIO.read(new File(MOON_TEXTURE_PATH));
        } catch (IOException e) {
            System.err.println("Couldn't load moon texture image: " + e.getMessage());
            throw new RuntimeException("Couldn't load moon texture image", e);
        }

        // Adjust moonCenter to place it in the top-left corner of the *actual rendered viewport
        Point moonCenter = new Point(
                -effectiveViewportWidth / 2.0 + MOON_RADIUS * 1.5, // X: left edge + moon radius + padding
                effectiveViewportHeight / 2.0 - MOON_RADIUS * 1.5, // Y: top edge - moon radius - padding
                Z_PLANE
        );

        int textureWidth = moonTexture.getWidth();
        int textureHeight = moonTexture.getHeight();

        double dotRadius = MOON_RADIUS * MOON_DOT_RADIUS_FACTOR;

        // Loop over the vertical pixels of the moon texture image with a step to sample points
        for (int y = 0; y < textureHeight; y += MOON_DOT_SAMPLING_STEP) {
            // Normalize y to [0,1] range
            double v = (double) y / textureHeight;
            // Convert normalized y to polar angle phi (0 to PI)
            double phi = v * Math.PI;

            // Loop over the horizontal pixels of the moon texture image with a step
            for (int x = 0; x < textureWidth; x += MOON_DOT_SAMPLING_STEP) {
                // Get the RGB color of the current pixel
                int rgb = moonTexture.getRGB(x, y);
                java.awt.Color awtColor = new java.awt.Color(rgb);

                // Skip very dark pixels (assumed background)
                if (awtColor.getRed() < 20 && awtColor.getGreen() < 20 && awtColor.getBlue() < 20)
                    continue;

                // Normalize x to [0,1] range
                double u = (double) x / textureWidth;
                // Convert normalized x to azimuthal angle theta (0 to 2*PI)
                double theta = u * 2 * Math.PI;

                // Calculate sine of polar angle phi
                double sinPhi = Math.sin(phi);
                // Aspect ratio correction to avoid ellipse distortion
                double aspectFix = effectiveViewportWidth / effectiveViewportHeight;
                // Calculate 3D coordinates on the sphere surface using spherical coordinates
                // Spherical coordinates formulas:
                // x = r * sin(φ) * cos(θ)
                // y = r * cos(φ)
                // z = r * sin(φ) * sin(θ)
                double px = moonCenter.xyz.d1() + MOON_RADIUS * sinPhi * Math.cos(theta) / aspectFix;
                double py = moonCenter.xyz.d2() + MOON_RADIUS * Math.cos(phi) + 1;
                double pz = moonCenter.xyz.d3() + MOON_RADIUS * sinPhi * Math.sin(theta);

                // Create color for the sphere dot
                Color dotColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());

                // Add a small sphere at the calculated position with the pixel color
                scene.geometries.add(
                        new Sphere(new Point(px, py, pz), dotRadius)
                                .setEmission(dotColor)
                                .setMaterial(new Material().setKD(0).setKS(0).setShininess(0))
                );
            }
        }
        System.out.println("Moon added using image-based dot texture.");

        /* ---------------------------------------------------------
         * 5. Black Background and Lighting
         * --------------------------------------------------------- */
        scene.setBackground(new Color(0, 0, 0));
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        scene.lights.add(
                new PointLight(new Color(150, 150, 150),
                        new Point(0, 0, VIEWPORT_BASE_SIZE * 2)) // Light source Z
                        .setKl(0.0008).setKq(0.00003));

        /* ---------------------------------------------------------
         * 6. Camera Setup
         * --------------------------------------------------------- */
        cameraBuilder
                .setLocation(new Point(0, 0, VIEWPORT_BASE_SIZE))            // Z location
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(VIEWPORT_BASE_SIZE)
                .setVpSize(effectiveViewportWidth, effectiveViewportHeight) // Use the calculated effective viewport size
                .setResolution(IMAGE_RESOLUTION_WIDTH, IMAGE_RESOLUTION_HEIGHT) // Output image resolution
                //.setBlackboard(new Blackboard(5))
//                .setUseAdaptiveSuperSampling(true)
//                .setAssMaxDepth(5)
//                .setAssTolerance(7)
                .setMultithreading(-2)
                .setDebugPrint(1.0)
                .build()
                .renderImage()
                .writeToImage("Mountains_and_Moon1");

        System.out.println("Finished – check 'Mountains_and_Moon.png'");
    }
}