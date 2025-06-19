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

    private final Scene scene = new Scene("Poly Art Auto-Fit");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    private static final String TEXT_FILE_PATH = "C:\\Users\\linoy\\Downloads\\full_mountain.txt";
    private static final String MOON_TEXTURE_PATH = "C:\\Users\\linoy\\Downloads\\moon.png";

    // Output image resolution
    private static final int IMAGE_RESOLUTION_WIDTH = 1000;
    private static final int IMAGE_RESOLUTION_HEIGHT = 1300;
    private static final double VIEWPORT_BASE_SIZE = 1300;


    // Base size for viewport distance (can be same as resolution for 1:1 pixel mapping)
    // This value often influences the "zoom" level and should be consistent with how the scene is perceived
//    private static final double VIEWPORT_BASE_SIZE = 1000;

    /**
     * שיעור כווץ קטן כדי להשאיר 2 % שוליים ביטחון (אפשר 1.0 אם לא צריך)
     */
    private static final double SAFETY_MARGIN = 1.0; // Set to 1.0 to attempt to fill the image without cutting content

    /**
     * Z קבוע למישור שבו מונחים המשולשים
     */
    private static final double Z_PLANE = 0.01;

    // Constants for the moon
    private static final double MOON_RADIUS = 150;
    private static final double MOON_DOT_SAMPLING_STEP = 3;
    private static final double MOON_DOT_RADIUS_FACTOR = 0.03;

    @Test
    void renderPolyArtFromText() throws IOException {
        /* ---------------------------------------------------------
         * 1. קריאת כל השורות מהקובץ
         * --------------------------------------------------------- */
        List<String> lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
        if (lines.isEmpty()) {
            System.err.println("No triangle data found – exiting");
            return;
        }

        /* ---------------------------------------------------------
         * 2. מעבר ראשון: חישוב גבולות   (min/max)
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

        // Calculate aspect ratio of the mountain data
        double originalAspectRatio = originalWidth / originalHeight;
        // Calculate aspect ratio of the target image resolution
        double targetAspectRatio = (double) IMAGE_RESOLUTION_WIDTH / IMAGE_RESOLUTION_HEIGHT;
        /* ---------------------------------------------------------
         * 3. חישוב Scale-Factor + Center Offset
         * This scaling should ensure the mountain fits the *viewport*,
         * not necessarily the image resolution directly yet.
         * --------------------------------------------------------- */
        double effectiveViewportWidth;
        double effectiveViewportHeight;
        double overallScaleFactor;

        // Determine how to fit the mountain content into the target image aspect ratio
        if (originalAspectRatio > targetAspectRatio) { // Mountain is wider than the target image
            effectiveViewportWidth = VIEWPORT_BASE_SIZE; // Fit by width
            effectiveViewportHeight = VIEWPORT_BASE_SIZE / originalAspectRatio; // Scale height proportionally
            overallScaleFactor = effectiveViewportWidth / originalWidth;
        } else { // Mountain is taller or same aspect ratio as the target image
            effectiveViewportHeight = VIEWPORT_BASE_SIZE; // Fit by height
            effectiveViewportWidth = VIEWPORT_BASE_SIZE * originalAspectRatio; // Scale width proportionally
            overallScaleFactor = effectiveViewportHeight / originalHeight;
        }

        // Apply safety margin to the overall scale factor
        overallScaleFactor *= SAFETY_MARGIN;

        // Recalculate effective viewport dimensions after applying overallScaleFactor
        effectiveViewportWidth = originalWidth * overallScaleFactor;
        effectiveViewportHeight = originalHeight * overallScaleFactor;


        // These offsets center the mountain within the *effective* viewport
        double offsetX = -((minX + maxX) / 2.0) * overallScaleFactor;
        double offsetY = -((minY + maxY) / 2.0) * overallScaleFactor;


        /* ---------------------------------------------------------
         * 4. יצירת המשולשים – מעבר שני
         * --------------------------------------------------------- */
        Material triMat = new Material().setKD(0.6).setKS(0.2).setShininess(10);

        int added = 0;
        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
            String[] p = line.split(",");
            if (p.length != 12) continue;

            List<Integer> v = Arrays.stream(p).map(String::trim)
                    .map(Integer::parseInt).collect(Collectors.toList());

            // המרת שלוש נקודות
            Point[] pts = new Point[3];
            for (int i = 0; i < 3; i++) {
                double x = v.get(i * 3);
                double y = v.get(i * 3 + 1);

                // Y inversion (כיוון תשובות)
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

        // Adjust moonCenter to place it in the top-left corner of the *actual rendered viewport*
        // The effectiveViewportWidth and effectiveViewportHeight define the bounds of the mountain content.
        // We need to place the moon relative to this content area, not the full 1000x1000 output image.
        // The camera's origin (0,0) is at the center of the viewport.
        Point moonCenter = new Point(
                -effectiveViewportWidth / 2.0 + MOON_RADIUS * 1.5, // X: left edge + moon radius + padding
                effectiveViewportHeight / 2.0 - MOON_RADIUS * 1.5, // Y: top edge - moon radius - padding
                Z_PLANE // Z_PLANE is fine, perhaps slightly more if it should hover
        );
        // You'll likely need to fine-tune the '1.5' factor for padding/offset.

        int textureWidth = moonTexture.getWidth();
        int textureHeight = moonTexture.getHeight();

        double dotRadius = MOON_RADIUS * MOON_DOT_RADIUS_FACTOR;

        for (int y = 0; y < textureHeight; y += MOON_DOT_SAMPLING_STEP) {
            double v = (double) y / textureHeight;
            double theta = v * Math.PI;

            for (int x = 0; x < textureWidth; x += MOON_DOT_SAMPLING_STEP) {
                int rgb = moonTexture.getRGB(x, y);
                java.awt.Color awtColor = new java.awt.Color(rgb);

                if (awtColor.getRed() < 20 && awtColor.getGreen() < 20 && awtColor.getBlue() < 20)
                    continue;

                double u = (double) x / textureWidth;
                double phi = u * 2 * Math.PI;

                double sinTheta = Math.sin(theta);
//                double px = moonCenter.xyz.d1() + MOON_RADIUS * sinTheta * Math.cos(phi);

                double aspectFix = effectiveViewportWidth / effectiveViewportHeight;
                double px = moonCenter.xyz.d1() + MOON_RADIUS * sinTheta * Math.cos(phi) / aspectFix;
                double py = moonCenter.xyz.d2() + MOON_RADIUS * Math.cos(theta) + 1;


//                double py = moonCenter.xyz.d2() + MOON_RADIUS * Math.cos(theta);
                double pz = moonCenter.xyz.d3() + MOON_RADIUS * sinTheta * Math.sin(phi);

                Color dotColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());

                scene.geometries.add(
                        new Sphere(new Point(px, py, pz), dotRadius)
                                .setEmission(dotColor)
                                .setMaterial(new Material().setKD(0).setKS(0).setShininess(0))
                );
            }
        }
        System.out.println("Moon added using image-based dot texture.");




        /* ---------------------------------------------------------
         * 5. רקע שחור ותאורה
         * --------------------------------------------------------- */
        scene.setBackground(new Color(0, 0, 0));
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        scene.lights.add(
                new PointLight(new Color(150, 150, 150),
                        new Point(0, 0, VIEWPORT_BASE_SIZE * 2)) // Light source Z
                        .setKl(0.0008).setKq(0.00003));

        /* ---------------------------------------------------------
         * 6. מצלמה
         * --------------------------------------------------------- */
        cameraBuilder
                .setLocation(new Point(0, 0, VIEWPORT_BASE_SIZE))            // Z location
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(VIEWPORT_BASE_SIZE)
                .setVpSize(effectiveViewportWidth, effectiveViewportHeight) // Use the calculated effective viewport size
                .setResolution(IMAGE_RESOLUTION_WIDTH, IMAGE_RESOLUTION_HEIGHT) // Output image resolution
//                .setBlackboard(new Blackboard(5))
//                .setUseAdaptiveSuperSampling(true)
//                .setAssMaxDepth(4)
//                .setAssTolerance(7.5)
                .setMultithreading(-2)
                .setDebugPrint(1.0)
                .build()
                .renderImage()
                .writeToImage("Mountains_and_Moon_NoStretch"); // New output name

        System.out.println("Finished – check 'Mountains_and_Moon_NoStretch.png'");
    }
}