package renderer;

import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TigerImage {

    private final Scene scene = new Scene("Poly Art Auto-Fit");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    private static final String TEXT_FILE_PATH = "python_code/triangles_data.txt";

    // Viewport size (width and height in pixels)
    private static final int VIEWPORT_SIZE = 1000;
    // Output image resolution (width and height in pixels)
    private static final int IMAGE_RESOLUTION = 1000;

    // Safety margin (e.g., 0.98 means 2% margin from the edges)
    private static final double SAFETY_MARGIN = 0.98;

    // Z coordinate for the plane where the triangles are placed
    private static final double Z_PLANE = 0.01;

    @Test
    void renderPolyArtFromText() throws IOException {
        /* ---------------------------------------------------------
         * 1. Read all lines from the triangles data file
         * --------------------------------------------------------- */
        List<String> lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
        if (lines.isEmpty()) {
            System.err.println("No triangle data found – exiting");
            return;
        }

        /* ---------------------------------------------------------
         * 2. First pass: Find the min/max bounds of all triangles
         * --------------------------------------------------------- */
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        // Iterate over all lines to find the bounding box of the triangles
        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;// Skip empty or comment lines
            String[] p = line.split(",");
            if (p.length != 12) continue; // Skip invalid lines

            // Extract X and Y coordinates of the triangle's vertices
            double[] xs = {Double.parseDouble(p[0].trim()),
                    Double.parseDouble(p[3].trim()),
                    Double.parseDouble(p[6].trim())};

            double[] ys = {Double.parseDouble(p[1].trim()),
                    Double.parseDouble(p[4].trim()),
                    Double.parseDouble(p[7].trim())};

            // Update min/max for X and Y
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

        /* ---------------------------------------------------------
         * 3. Calculate scale factor and center offset for fitting triangles to viewport
         * --------------------------------------------------------- */
        // Calculate the scale so the triangles fit within the viewport, considering the safety margin
        double scale = SAFETY_MARGIN *
                Math.min(VIEWPORT_SIZE / originalWidth,
                        VIEWPORT_SIZE / originalHeight);

        // Calculate offsets to center the triangles in the viewport
        double offsetX = -((minX + maxX) / 2.0) * scale; // Center X
        double offsetY = -((minY + maxY) / 2.0) * scale; // Center Y

        /* ---------------------------------------------------------
         * 4. Second pass: Create and add triangles to the scene
         * --------------------------------------------------------- */
        Material triMat = new Material().setKD(0.6).setKS(0.2).setShininess(10);

        int added = 0;
        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
            String[] p = line.split(",");
            if (p.length != 12) continue;

            // Parse all 12 values (3 vertices × 2 coordinates + 3 color components)
            List<Integer> v = Arrays.stream(p).map(String::trim)
                    .map(Integer::parseInt).collect(Collectors.toList());

            // Convert the three vertices to Point objects, applying scale and offset
            Point[] pts = new Point[3];
            for (int i = 0; i < 3; i++) {
                double x = v.get(i * 3);
                double y = v.get(i * 3 + 1);

                // Invert Y axis to match image coordinate system (Y=0 is top)
                y = maxY - (y - minY);

                pts[i] = new Point(
                        x * scale + offsetX, // Apply scale and offset to X
                        y * scale + offsetY, // Apply scale and offset to Y
                        Z_PLANE // Place all triangles on the same Z plane
                );
            }

            // Create color from the last three values (RGB)
            Color col = new Color(v.get(9), v.get(10), v.get(11));
            // Add the triangle to the scene with the given material and color
            scene.geometries.add(new Triangle(pts[0], pts[1], pts[2])
                    .setMaterial(triMat)
                    .setEmission(col));
            added++;
        }
        System.out.printf("Added %,d triangles%n", added);

        /* ---------------------------------------------------------
         * 5. Set black background and add lighting
         * --------------------------------------------------------- */
        scene.setBackground(new Color(0, 0, 0)); // Absolute black background
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // Add a point light above the scene for shading and highlights
        scene.lights.add(
                new PointLight(new Color(150, 150, 150),
                        new Point(0, 0, VIEWPORT_SIZE * 2))
                        .setKl(0.0008).setKq(0.00003));

        /* ---------------------------------------------------------
         * 6. Camera setup and image rendering
         * --------------------------------------------------------- */
        cameraBuilder
                .setLocation(new Point(0, 0, VIEWPORT_SIZE)) // Camera placed above the scene
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))// Looking down the -Z axis, Y is up
                .setVpDistance(VIEWPORT_SIZE)
                .setVpSize(VIEWPORT_SIZE, VIEWPORT_SIZE)
                .setResolution(IMAGE_RESOLUTION, IMAGE_RESOLUTION)
                .setBlackboard(new Blackboard(17))
//                .setUseAdaptiveSuperSampling(true)
//                .setAssMaxDepth(4)
//                .setAssTolerance(7)
                .setMultithreading(-2)
                .setDebugPrint(1.0)
                .build()
                .renderImage()
                .writeToImage("Tiger_AA");

        System.out.println("Finished – check 'polyArtRender.png'");
    }
}
