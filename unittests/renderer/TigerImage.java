package renderer;

import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import scene.Scene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TigerImage {

    private final Scene scene = new Scene("Poly Art Auto-Fit");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    private static final String TEXT_FILE_PATH =
            "C:\\Users\\chenb\\PycharmProjects\\PythonProject1\\triangles_data.txt";

    /** גודל מישור-התצוגה (Viewport) – 800×800 פיקסלים */
    private static final int VIEWPORT_SIZE = 800;      // גם רוחב וגם גובה
    private static final int IMAGE_RESOLUTION = 800;   // רזולוציית פלט

    /** שיעור כווץ קטן כדי להשאיר 2 % שוליים ביטחון (אפשר 1.0 אם לא צריך) */
    private static final double SAFETY_MARGIN = 0.98;

    /** Z קבוע למישור שבו מונחים המשולשים */
    private static final double Z_PLANE = 0.01;

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
            if (p.length != 12) continue; // שורה לא תקינה

            double[] xs = { Double.parseDouble(p[0].trim()),
                    Double.parseDouble(p[3].trim()),
                    Double.parseDouble(p[6].trim()) };

            double[] ys = { Double.parseDouble(p[1].trim()),
                    Double.parseDouble(p[4].trim()),
                    Double.parseDouble(p[7].trim()) };

            for (double x : xs) {
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
            }
            for (double y : ys) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
        }

        double originalWidth  = maxX - minX;
        double originalHeight = maxY - minY;

        /* ---------------------------------------------------------
         * 3. חישוב Scale-Factor + Center Offset
         * --------------------------------------------------------- */
        double scale = SAFETY_MARGIN *
                Math.min(VIEWPORT_SIZE / originalWidth,
                        VIEWPORT_SIZE / originalHeight);

        double offsetX = -((minX + maxX) / 2.0) * scale;               // מרכז בציר X
        double offsetY = -((minY + maxY) / 2.0) * scale;               // מרכז בציר Y
        //      מיישרים את Y כלפי מעלה:
        double invertFactor = VIEWPORT_SIZE / originalHeight;          // יחס להפיכת Y
        // (אפשר גם פשוט ORIGINAL_IMAGE_HEIGHT אם הקובץ 0-800, אבל זו גישה כללית יותר)

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
                double x = v.get(i * 3    );
                double y = v.get(i * 3 + 1);

                // Y inversion (כיוון תשובות)
                y = maxY - (y - minY);        // הופכים בתוך תחום המקור

                pts[i] = new Point(
                        x * scale + offsetX,
                        y * scale + offsetY,
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
         * 5. רקע שחור ותאורה
         * --------------------------------------------------------- */
        scene.setBackground(new Color(0, 0, 0));                       // רקע מוחלט שחור
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        scene.lights.add(
                new PointLight(new Color(150, 150, 150),
                        new Point(0, 0, VIEWPORT_SIZE * 2))
                        .setKl(0.0008).setKq(0.00003));

        /* ---------------------------------------------------------
         * 6. מצלמה
         * --------------------------------------------------------- */
        cameraBuilder
                .setLocation(new Point(0, 0, VIEWPORT_SIZE))            // Z = 800
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(VIEWPORT_SIZE)                          // 800
                .setVpSize(VIEWPORT_SIZE, VIEWPORT_SIZE)               // 800×800
                .setResolution(IMAGE_RESOLUTION, IMAGE_RESOLUTION)
                .setBlackboard(new Blackboard(5))
                .build()
                .renderImage()
                .writeToImage("polyArtRender_Full_NoBorder_with_anti");

        System.out.println("Finished – check 'polyArtRender_Full_NoBorder.png'");
    }
}


