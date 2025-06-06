//package renderer;
//
//import geometries.Plane;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//
//import org.junit.jupiter.api.Test;
//import scene.Scene;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class TigerImage {
//
//    private final Scene scene = new Scene("Poly Art Facing Camera");
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(scene, RayTracerType.SIMPLE);
//
//    private static final String TEXT_FILE_PATH = "C:\\Users\\chenb\\PycharmProjects\\PythonProject1\\triangles_data.txt";
//
//    private static final int ORIGINAL_IMAGE_WIDTH = 800;
//    private static final int ORIGINAL_IMAGE_HEIGHT = 800;
//
//    private static final double SCALE_FACTOR = 0.15;
//    private static final double OFFSET_Y = - (ORIGINAL_IMAGE_WIDTH / 2.0) * SCALE_FACTOR;
//    private static final double OFFSET_Z = - (ORIGINAL_IMAGE_HEIGHT / 2.0) * SCALE_FACTOR;
//    private static final double X_PLANE = 0.01;
//
//    @Test
//    void renderPolyArtFromText() {
//        System.out.println("Starting renderPolyArtFromText test...");
//
//        List<String> lines = null;
//        try {
//            lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
//            System.out.println("Successfully loaded " + lines.size() + " lines from " + TEXT_FILE_PATH);
//        } catch (IOException e) {
//            System.err.println("Error reading text file: " + TEXT_FILE_PATH);
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load triangle data from text file.", e);
//        }
//
//        if (lines == null || lines.isEmpty()) {
//            System.out.println("No data found in text file. Exiting.");
//            return;
//        }
//
//        Material defaultTriangleMaterial = new Material()
//                .setKD(0.8)
//                .setKS(0.2)
//                .setShininess(10);
//
//        int trianglesAddedCount = 0;
//        int MAX_LINES_TO_READ = 400;
//
//
//        for (String line : lines) {
//            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
//                continue;
//            }
//
//            String[] parts = line.split(",");
//
//            if (parts.length != 12) {
//                System.err.println("Skipping malformed line (expected 12 parts): " + line);
//                continue;
//            }
//
//            try {
//                List<Integer> values = Arrays.stream(parts)
//                        .map(String::trim)
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//
//                // הגדר נקודות על מישור YZ עם X קבוע
//                Point p1 = new Point(X_PLANE,
//                        values.get(0) * SCALE_FACTOR + OFFSET_Y,
//                        values.get(1) * SCALE_FACTOR + OFFSET_Z);
//                Point p2 = new Point(X_PLANE,
//                        values.get(3) * SCALE_FACTOR + OFFSET_Y,
//                        values.get(4) * SCALE_FACTOR + OFFSET_Z);
//                Point p3 = new Point(X_PLANE,
//                        values.get(6) * SCALE_FACTOR + OFFSET_Y,
//                        values.get(7) * SCALE_FACTOR + OFFSET_Z);
//
//                Color triangleColor = new Color(values.get(9), values.get(10), values.get(11));
//
//                scene.geometries.add(
//                        new Triangle(p1, p2, p3)
//                                .setMaterial(defaultTriangleMaterial)
//                                .setEmission(triangleColor)
//                );
//                trianglesAddedCount++;
//
//            } catch (NumberFormatException e) {
//                System.err.println("Skipping line due to invalid number format: " + line + " - " + e.getMessage());
//            }
//        }
//
//        System.out.println("Added " + trianglesAddedCount + " triangles to the scene.");
//
//        // רקע/רצפה (לא חובה אם התמונה עומדת באוויר)
//        scene.geometries.add(
//                new Plane(new Point(0, 0, 0), new Vector(1, 0, 0))
//                        .setEmission(new Color(40, 40, 40))
//                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(5).setKR(0.2))
//        );
//
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        scene.lights.add(
//                new PointLight(new Color(300, 300, 300), new Point(-150, 0, 0))
//                        .setKl(0.0008)
//                        .setKq(0.00003)
//        );
//
//        double imageWorldWidth = ORIGINAL_IMAGE_WIDTH * SCALE_FACTOR;
//        double imageWorldHeight = ORIGINAL_IMAGE_HEIGHT * SCALE_FACTOR;
//
//        cameraBuilder
//                .setLocation(new Point(100, 0, 0)) // מצלמה רחוקה לאורך ציר X
//                .setDirection(new Vector(-1, 0, 0), new Vector(0, 0, -1)) // מסתכלת לכיוון X שלילי, אנכי כלפי מטה
//                .setVpDistance(imageWorldWidth * 0.8) // עומק נכון – מתאים לגובה ב-Y
//                .setVpSize(imageWorldHeight, imageWorldWidth) // קודם גובה ואז רוחב!
//                .setResolution(ORIGINAL_IMAGE_WIDTH, ORIGINAL_IMAGE_HEIGHT)
//                .build()
//                .renderImage()
//                .writeToImage("polyArtRenderFromText_Ydown");
//
//
//        System.out.println("Finished renderPolyArtFromText test. Check 'polyArtRenderFacingCamera.png' in your project directory.");
//    }
//}


//package renderer;
//
//import geometries.Plane;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import org.junit.jupiter.api.Test;
//import scene.Scene;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class TigerImage {
//
//    private final Scene scene = new Scene("Poly Art Facing Camera");
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(scene, RayTracerType.SIMPLE);
//
//    private static final String TEXT_FILE_PATH = "C:\\Users\\chenb\\PycharmProjects\\PythonProject1\\triangles_data.txt";
//
//    private static final int ORIGINAL_IMAGE_WIDTH = 800;
//    private static final int ORIGINAL_IMAGE_HEIGHT = 800;
//
//    // *** שינוי חשוב: הגדל את SCALE_FACTOR כדי להגדיל את גודל התמונה בעולם ***
//    // מ-0.15 ל-0.2 (או יותר, עד שתגיע לגודל הרצוי).
//    // אם תגדיל את זה יותר מדי, ייתכן שתצטרך להזיז את המצלמה עוד יותר אחורה.
//    private static final double SCALE_FACTOR = 0.2;
//
//    private static final double SCALED_WIDTH = ORIGINAL_IMAGE_WIDTH * SCALE_FACTOR;
//    private static final double SCALED_HEIGHT = ORIGINAL_IMAGE_HEIGHT * SCALE_FACTOR;
//
//    // חישובים אלה אמורים למרכז את התמונה בדיוק סביב (0,0) במרחב ה-XY
//    private static final double OFFSET_X = -SCALED_WIDTH / 2.0;
//    private static final double OFFSET_Y = -SCALED_HEIGHT / 2.0;
//
//    private static final double Z_PLANE = 0.01; // המישור שבו נמצאת התמונה
//
//    @Test
//    void renderPolyArtFromText() {
//        System.out.println("Starting renderPolyArtFromText test...");
//
//        List<String> lines = null;
//        try {
//            lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
//            System.out.println("Successfully loaded " + lines.size() + " lines from " + TEXT_FILE_PATH);
//        } catch (IOException e) {
//            System.err.println("Error reading text file: " + TEXT_FILE_PATH);
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load triangle data from text file.", e);
//        }
//
//        if (lines == null || lines.isEmpty()) {
//            System.out.println("No data found in text file. Exiting.");
//            return;
//        }
//
//        Material defaultTriangleMaterial = new Material()
//                .setKD(0.6) // *** שינוי: הקטנת החזרת האור הדיפוזית (פחות בהיר) ***
//                .setKS(0.2)
//                .setShininess(10);
//
//        int trianglesAddedCount = 0;
//
//        for (String line : lines) {
//            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
//                continue;
//            }
//
//            String[] parts = line.split(",");
//
//            if (parts.length != 12) {
//                System.err.println("Skipping malformed line (expected 12 parts): " + line);
//                continue;
//            }
//
//            try {
//                List<Integer> values = Arrays.stream(parts)
//                        .map(String::trim)
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//
//                // היפוך קואורדינטת ה-Y המקורית מהקובץ
//                double original_image_y1 = values.get(1);
//                double original_image_y2 = values.get(4);
//                double original_image_y3 = values.get(7);
//
//                double inverted_image_y1 = ORIGINAL_IMAGE_HEIGHT - original_image_y1;
//                double inverted_image_y2 = ORIGINAL_IMAGE_HEIGHT - original_image_y2;
//                double inverted_image_y3 = ORIGINAL_IMAGE_HEIGHT - original_image_y3;
//
//                // יצירת נקודות תלת-ממדיות במישור XY (כאשר Z קבוע)
//                Point p1 = new Point(values.get(0) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y1 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//                Point p2 = new Point(values.get(3) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y2 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//                Point p3 = new Point(values.get(6) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y3 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//
//                Color triangleColor = new Color(values.get(9), values.get(10), values.get(11));
//
//                scene.geometries.add(
//                        new Triangle(p1, p2, p3)
//                                .setMaterial(defaultTriangleMaterial)
//                                .setEmission(triangleColor)
//                );
//                trianglesAddedCount++;
//
//            } catch (NumberFormatException e) {
//                System.err.println("Skipping line due to invalid number format: " + line + " - " + e.getMessage());
//            }
//        }
//
//        System.out.println("Added " + trianglesAddedCount + " triangles to the scene.");
//
//        // רקע ואור
//        scene.geometries.add(
//                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)) // מישור XY ב-Z=0
//                        .setEmission(new Color(40, 40, 40))
//                        .setMaterial(new Material()
//                                .setKD(0.1)
//                                .setKS(0.1)
//                                .setShininess(5)
//                                .setKR(0.2))
//        );
//
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        scene.lights.add(
//                // *** שינוי: הורדת עוצמת האור של PointLight ***
//                // מ-300,300,300 ל-150,150,150. ניתן להוריד עוד ל-100,100,100 אם עדיין בהיר מדי.
//                new PointLight(new Color(150, 150, 150), new Point(0, 0, SCALED_HEIGHT * 2))
//                        .setKl(0.0008)
//                        .setKq(0.00003)
//        );
//
//        // הגדרת מצלמה
//        cameraBuilder
//                // מיקום המצלמה: במרכז ה-X וה-Y של התמונה המרונדרת (שמוקמה כבר במרכז העולם), וב-Z גבוה מספיק.
//                // *** שינוי: התאם את מרחק המצלמה בהתאם ל-SCALE_FACTOR החדש. ***
//                // אם הגדלת את SCALE_FACTOR, המצלמה צריכה לזוז עוד יותר אחורה.
//                // ננסה SCALED_HEIGHT * 4.0
//                .setLocation(new Point(0, 0, SCALED_HEIGHT * 4.0))
//
//                // *** תיקון: החזרת נקודת היעד ל-setDirection ***
//                // אם setDirection שלך מקבלת נקודת יעד ווקטור כיוון, זה הפורמט הנכון.
//                // אם היא מקבלת רק וקטורי כיוון, תצטרך לוודא שנקודת המיקום של המצלמה מספיקה.
//                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//                // אם המתודה setDirection אינה מקבלת 3 פרמטרים (Point, Vector, Vector),
//                // אלא רק 2 פרמטרים (Vector, Vector) או (Point, Vector),
//                // אז תצטרך להתאים אותה חזרה למה שעבד עבורך,
//                // לדוגמה: .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//                // או: .setDirection(new Point(0, 0, Z_PLANE), new Vector(0, 0, -1))
//                // אבל השינוי הנוכחי (הוספת נקודת יעד) הוא הגורם הסביר ביותר לתיקון המיקום.
//
//                // מרחק מישור התצוגה (VpDistance) צריך להיות מספיק גדול כדי לכלול את כל התמונה
//                // *** שינוי: התאם את VpDistance ל-SCALE_FACTOR החדש ולמיקום המצלמה. ***
//                // אם הגדלנו את גודל האובייקט והרחקנו את המצלמה, נצטרך להתאים גם את VpDistance.
//                // ננסה SCALED_WIDTH * 1.5. ייתכן שיהיה צורך בניסוי וטעייה.
//                .setVpDistance(SCALED_WIDTH * 1.5)
//
//                // גודל מישור התצוגה (VpSize) צריך להיות בדיוק בגודל התמונה בעולם.
//                // זה אמור להיות נכון:
//                .setVpSize(SCALED_WIDTH, SCALED_HEIGHT)
//
//                .setResolution(ORIGINAL_IMAGE_WIDTH, ORIGINAL_IMAGE_HEIGHT) // רזולוציית התמונה
//                .build()
//                .renderImage()
//                .writeToImage("polyArtRenderFromText_Final_Fixed6"); // שינוי שם פלט
//
//        System.out.println("Finished renderPolyArtFromText test. Check 'polyArtRenderFromText_Final_Fixed.png' in your project directory.");
//    }
//}







//package renderer;
//
//import geometries.Plane;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import org.junit.jupiter.api.Test;
//import scene.Scene;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class TigerImage {
//
//    private final Scene scene = new Scene("Poly Art Facing Camera");
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(scene, RayTracerType.SIMPLE);
//
//    private static final String TEXT_FILE_PATH = "C:\\Users\\chenb\\PycharmProjects\\PythonProject1\\triangles_data.txt";
//
//    private static final int ORIGINAL_IMAGE_WIDTH = 800;
//    private static final int ORIGINAL_IMAGE_HEIGHT = 800;
//
//    // *** שינוי חשוב: הגדל את SCALE_FACTOR באופן משמעותי יותר ***
//    // המטרה היא לגרום לתמונה הפיזיקלית להיות גדולה מספיק בעולם התלת-ממדי
//    // כך שהיא תמלא את כל ה-View Plane.
//    // ננסה 0.9 או אפילו 1.0. אם זה עדיין חתוך, נלך על 1.0.
//    private static final double SCALE_FACTOR = 0.95; // ננסה ערך גבוה, כמעט 1:1 עם הפיקסלים המקוריים
//
//    private static final double SCALED_WIDTH = ORIGINAL_IMAGE_WIDTH * SCALE_FACTOR;
//    private static final double SCALED_HEIGHT = ORIGINAL_IMAGE_HEIGHT * SCALE_FACTOR;
//
//    // חישובים אלה אמורים למרכז את התמונה בדיוק סביב (0,0) במרחב ה-XY
//    private static final double OFFSET_X = -SCALED_WIDTH / 2.0;
//    private static final double OFFSET_Y = -SCALED_HEIGHT / 2.0;
//
//    private static final double Z_PLANE = 0.01; // המישור שבו נמצאת התמונה
//
//    @Test
//    void renderPolyArtFromText() {
//        System.out.println("Starting renderPolyArtFromText test...");
//
//        List<String> lines = null;
//        try {
//            lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
//            System.out.println("Successfully loaded " + lines.size() + " lines from " + TEXT_FILE_PATH);
//        } catch (IOException e) {
//            System.err.println("Error reading text file: " + TEXT_FILE_PATH);
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load triangle data from text file.", e);
//        }
//
//        if (lines == null || lines.isEmpty()) {
//            System.out.println("No data found in text file. Exiting.");
//            return;
//        }
//
//        Material defaultTriangleMaterial = new Material()
//                .setKD(0.6)
//                .setKS(0.2)
//                .setShininess(10);
//
//        int trianglesAddedCount = 0;
//
//        for (String line : lines) {
//            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
//                continue;
//            }
//
//            String[] parts = line.split(",");
//
//            if (parts.length != 12) {
//                System.err.println("Skipping malformed line (expected 12 parts): " + line);
//                continue;
//            }
//
//            try {
//                List<Integer> values = Arrays.stream(parts)
//                        .map(String::trim)
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//
//                // היפוך קואורדינטת ה-Y המקורית מהקובץ (כי Y בדרך כלל גדל למטה בתמונות, ורוצים שיגדל למעלה בעולם התלת-ממדי)
//                double original_image_y1 = values.get(1);
//                double original_image_y2 = values.get(4);
//                double original_image_y3 = values.get(7);
//
//                double inverted_image_y1 = ORIGINAL_IMAGE_HEIGHT - original_image_y1;
//                double inverted_image_y2 = ORIGINAL_IMAGE_HEIGHT - original_image_y2;
//                double inverted_image_y3 = ORIGINAL_IMAGE_HEIGHT - original_image_y3;
//
//                // יצירת נקודות תלת-ממדיות במישור XY (כאשר Z קבוע)
//                Point p1 = new Point(values.get(0) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y1 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//                Point p2 = new Point(values.get(3) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y2 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//                Point p3 = new Point(values.get(6) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y3 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//
//                Color triangleColor = new Color(values.get(9), values.get(10), values.get(11));
//
//                scene.geometries.add(
//                        new Triangle(p1, p2, p3)
//                                .setMaterial(defaultTriangleMaterial)
//                                .setEmission(triangleColor)
//                );
//                trianglesAddedCount++;
//
//            } catch (NumberFormatException e) {
//                System.err.println("Skipping line due to invalid number format: " + line + " - " + e.getMessage());
//            }
//        }
//
//        System.out.println("Added " + trianglesAddedCount + " triangles to the scene.");
//
//        // *** קריטי: הסר את הוספת מישור הרקע האפור שגרם לרקע הבהיר ***
//        // הקוד הבא הוסר לחלוטין:
//        /*
//        scene.geometries.add(
//                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)) // מישור XY ב-Z=0
//                        .setEmission(new Color(40, 40, 40))
//                        .setMaterial(new Material()
//                                .setKD(0.1)
//                                .setKS(0.1)
//                                .setShininess(5)
//                                .setKR(0.2))
//        );
//        */
//        // אם תרצה רקע בצבע אחר (לא שחור), תוכל להוסיף:
//        // scene.setBackground(new Color(0, 0, 0)); // לדוגמה, רקע שחור מפורש
//
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        scene.lights.add(
//                new PointLight(new Color(150, 150, 150), new Point(0, 0, SCALED_HEIGHT * 2))
//                        .setKl(0.0008)
//                        .setKq(0.00003)
//        );
//
//        // הגדרת מצלמה
//        cameraBuilder
//                // מיקום המצלמה: במרכז ה-X וה-Y, וב-Z גבוה מספיק.
//                // אם SCALE_FACTOR גדול יותר, המצלמה צריכה לזוז עוד יותר אחורה (Z גדול יותר).
//                // נחשב את Z כך שהאובייקט ימלא את המסך בדיוק.
//                // נגדיר את Z של המצלמה לערך קרוב מאוד ל-SCALED_HEIGHT (גובה הנמר בעולם).
//                // אם SCALED_HEIGHT הוא 800 * 0.95 = 760, אז נשים את המצלמה ב-Z של 760.
//                // נתחיל עם 1.0 כפול SCALED_HEIGHT, ונכוונן משם.
//                .setLocation(new Point(0, 0, SCALED_HEIGHT * 1.0)) // שינוי ל-Z המצלמה
//
//                // כיוון המצלמה הוא לכיוון הנמר (כלפי מטה בציר Z)
//                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//
//                // מרחק מישור התצוגה (VpDistance)
//                // עלינו לוודא שה-VpDistance מאפשר לאובייקט למלא את מישור התצוגה.
//                // אם VpSize הוא בגודל האובייקט, אז VpDistance צריך להיות זהה ל-Z של המצלמה
//                // (בהנחה שהאובייקט ב-Z=0).
//                .setVpDistance(SCALED_HEIGHT * 1.0) // שינוי ל-VpDistance - זהה ל-Z של המצלמה
//
//                // גודל מישור התצוגה (VpSize) צריך להיות בדיוק בגודל התמונה בעולם.
//                // אלו המימדים של הנמר בפועל בעולם התלת-ממדי לאחר הכפלה ב-SCALE_FACTOR.
//                .setVpSize(SCALED_WIDTH, SCALED_HEIGHT)
//
//                .setResolution(ORIGINAL_IMAGE_WIDTH, ORIGINAL_IMAGE_HEIGHT) // רזולוציית התמונה בפועל
//                .build()
//                .renderImage()
//                .writeToImage("polyArtRenderFromText_FullImage_NoCut_FinalAttempt5"); // שינוי שם פלט
//
//        System.out.println("Finished renderPolyArtFromText test. Check 'polyArtRenderFromText_FullImage_NoCut_FinalAttempt.png' in your project directory.");
//    }
//}

//תמונה קטנה

//package renderer;
//
//import geometries.Plane; // ייתכן שנצטרך את זה לזיהוי, אבל לא נוסיף Plane לסצנה.
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import org.junit.jupiter.api.Test;
//import scene.Scene;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class TigerImage {
//
//    private final Scene scene = new Scene("Poly Art Facing Camera");
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(scene, RayTracerType.SIMPLE);
//
//    private static final String TEXT_FILE_PATH = "C:\\Users\\chenb\\PycharmProjects\\PythonProject1\\triangles_data.txt";
//
//    private static final int ORIGINAL_IMAGE_WIDTH = 800;
//    private static final int ORIGINAL_IMAGE_HEIGHT = 800;
//
//    // *** שינוי חשוב: הגדל את SCALE_FACTOR באופן משמעותי ***
//    // זהו הגורם העיקרי שקובע כמה גדול הנמר יהיה ב"עולם" התלת-ממדי.
//    // אם הוא קטן מדי, הוא יופיע עם שוליים שחורים. אם הוא גדול מדי, הוא ייחתך.
//    // נתחיל מ-0.5, ונכוונן לפי הצורך. ערכים כמו 0.7-1.0 יכולים להיות רלוונטיים.
//    private static final double SCALE_FACTOR = 0.5; // התחל עם ערך ניטרלי יותר
//
//    private static final double SCALED_WIDTH = ORIGINAL_IMAGE_WIDTH * SCALE_FACTOR;
//    private static final double SCALED_HEIGHT = ORIGINAL_IMAGE_HEIGHT * SCALE_FACTOR;
//
//    // חישובים אלה אמורים למרכז את התמונה בדיוק סביב (0,0) במרחב ה-XY
//    private static final double OFFSET_X = -SCALED_WIDTH / 2.0;
//    private static final double OFFSET_Y = -SCALED_HEIGHT / 2.0;
//
//    private static final double Z_PLANE = 0.01; // המישור שבו נמצאת התמונה
//
//    @Test
//    void renderPolyArtFromText() {
//        System.out.println("Starting renderPolyArtFromText test...");
//
//        List<String> lines = null;
//        try {
//            lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
//            System.out.println("Successfully loaded " + lines.size() + " lines from " + TEXT_FILE_PATH);
//        } catch (IOException e) {
//            System.err.println("Error reading text file: " + TEXT_FILE_PATH);
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load triangle data from text file.", e);
//        }
//
//        if (lines == null || lines.isEmpty()) {
//            System.out.println("No data found in text file. Exiting.");
//            return;
//        }
//
//        Material defaultTriangleMaterial = new Material()
//                .setKD(0.6)
//                .setKS(0.2)
//                .setShininess(10);
//
//        int trianglesAddedCount = 0;
//
//        for (String line : lines) {
//            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
//                continue;
//            }
//
//            String[] parts = line.split(",");
//
//            if (parts.length != 12) {
//                System.err.println("Skipping malformed line (expected 12 parts): " + line);
//                continue;
//            }
//
//            try {
//                List<Integer> values = Arrays.stream(parts)
//                        .map(String::trim)
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//
//                // היפוך קואורדינטת ה-Y המקורית מהקובץ
//                double original_image_y1 = values.get(1);
//                double original_image_y2 = values.get(4);
//                double original_image_y3 = values.get(7);
//
//                double inverted_image_y1 = ORIGINAL_IMAGE_HEIGHT - original_image_y1;
//                double inverted_image_y2 = ORIGINAL_IMAGE_HEIGHT - original_image_y2;
//                double inverted_image_y3 = ORIGINAL_IMAGE_HEIGHT - original_image_y3;
//
//                // יצירת נקודות תלת-ממדיות במישור XY (כאשר Z קבוע)
//                Point p1 = new Point(values.get(0) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y1 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//                Point p2 = new Point(values.get(3) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y2 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//                Point p3 = new Point(values.get(6) * SCALE_FACTOR + OFFSET_X,
//                        inverted_image_y3 * SCALE_FACTOR + OFFSET_Y,
//                        Z_PLANE);
//
//                Color triangleColor = new Color(values.get(9), values.get(10), values.get(11));
//
//                scene.geometries.add(
//                        new Triangle(p1, p2, p3)
//                                .setMaterial(defaultTriangleMaterial)
//                                .setEmission(triangleColor)
//                );
//                trianglesAddedCount++;
//
//            } catch (NumberFormatException e) {
//                System.err.println("Skipping line due to invalid number format: " + line + " - " + e.getMessage());
//            }
//        }
//
//        System.out.println("Added " + trianglesAddedCount + " triangles to the scene.");
//
//        // *** קריטי: הסר את הוספת מישור הרקע האפור ***
//        // וודא ששורות אלו *אינן* קיימות בקוד שלך:
//        /*
//        scene.geometries.add(
//                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)) // מישור XY ב-Z=0
//                        .setEmission(new Color(40, 40, 40))
//                        .setMaterial(new Material()
//                                .setKD(0.1)
//                                .setKS(0.1)
//                                .setShininess(5)
//                                .setKR(0.2))
//        );
//        */
//        // אם אתה רוצה רקע בצבע שחור, אין צורך להוסיף דבר.
//        // אם אתה רוצה צבע רקע אחר שאינו חלק מהאובייקט, השתמש ב-scene.setBackground(new Color(R, G, B));
//
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        scene.lights.add(
//                new PointLight(new Color(150, 150, 150), new Point(0, 0, SCALED_HEIGHT * 2))
//                        .setKl(0.0008)
//                        .setKq(0.00003)
//        );
//
//        // הגדרת מצלמה
//        cameraBuilder
//                // מיקום המצלמה: במרכז ה-X וה-Y, וב-Z גבוה מספיק.
//                // המטרה היא שהמצלמה תהיה רחוקה מספיק כדי לכלול את כל הנמר בגודלו החדש.
//                // נשתמש ב-SCALED_HEIGHT כבסיס.
//                // ערך התחלתי סביר הוא SCALED_HEIGHT * 1.0 (או מעט יותר/פחות)
//                .setLocation(new Point(0, 0, SCALED_HEIGHT * 1.0))
//
//                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//
//                // מרחק מישור התצוגה (VpDistance)
//                // זהו המרחק מהמצלמה למישור בו התמונה מרונדרת.
//                // אם setVpSize שווה לגודל ה"פיזי" של האובייקט, אז VpDistance צריך להיות זהה ל-Z של המצלמה
//                // כדי שהאובייקט ימלא את מישור התצוגה.
//                .setVpDistance(SCALED_HEIGHT * 1.0) // זהה ל-Z של המצלמה
//
//                // גודל מישור התצוגה (VpSize)
//                // זה חייב להיות זהה לגודל ה"פיזי" של הנמר בעולם התלת-ממדי.
//                // זה מוגדר על ידי SCALED_WIDTH ו-SCALED_HEIGHT, שהם ORIGINAL_IMAGE_WIDTH/HEIGHT * SCALE_FACTOR
//                .setVpSize(SCALED_WIDTH, SCALED_HEIGHT)
//
//                .setResolution(ORIGINAL_IMAGE_WIDTH, ORIGINAL_IMAGE_HEIGHT) // רזולוציית התמונה בפועל
//                .build()
//                .renderImage()
//                .writeToImage("polyArtRenderFromText_FullImage_Attempt_Final"); // שם קובץ פלט
//
//        System.out.println("Finished renderPolyArtFromText test. Check 'polyArtRenderFromText_FullImage_Attempt_Final.png' in your project directory.");
//    }
//}

//תמונה חתוכה  מימין ומלמטה
//package renderer;
//
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import org.junit.jupiter.api.Test;
//import scene.Scene;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class TigerImage {
//
//    private final Scene scene = new Scene("Poly Art Full Frame");
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(scene, RayTracerType.SIMPLE);
//
//    private static final String TEXT_FILE_PATH = "C:\\Users\\chenb\\PycharmProjects\\PythonProject1\\triangles_data.txt";
//
//    private static final int ORIGINAL_IMAGE_WIDTH = 800;
//    private static final int ORIGINAL_IMAGE_HEIGHT = 800;
//
//    // Scale factor מותאם כך שהתמונה תמלא את כל המסך
//    private static final double SCALE_FACTOR = 1.0;
//
//    private static final double SCALED_WIDTH = ORIGINAL_IMAGE_WIDTH * SCALE_FACTOR;
//    private static final double SCALED_HEIGHT = ORIGINAL_IMAGE_HEIGHT * SCALE_FACTOR;
//
//    private static final double OFFSET_X = -SCALED_WIDTH / 2.0;
//    private static final double OFFSET_Y = -SCALED_HEIGHT / 2.0;
//
//    private static final double Z_PLANE = 0.01;
//
//    @Test
//    void renderPolyArtFromText() {
//        System.out.println("Starting renderPolyArtFromText test...");
//
//        List<String> lines;
//        try {
//            lines = Files.readAllLines(Paths.get(TEXT_FILE_PATH));
//            System.out.println("Successfully loaded " + lines.size() + " lines from " + TEXT_FILE_PATH);
//        } catch (IOException e) {
//            System.err.println("Error reading text file: " + TEXT_FILE_PATH);
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load triangle data from text file.", e);
//        }
//
//        if (lines == null || lines.isEmpty()) {
//            System.out.println("No data found in text file. Exiting.");
//            return;
//        }
//
//        Material defaultTriangleMaterial = new Material()
//                .setKD(0.6)
//                .setKS(0.2)
//                .setShininess(10);
//
//        int trianglesAddedCount = 0;
//
//        for (String line : lines) {
//            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
//                continue;
//            }
//
//            String[] parts = line.split(",");
//            if (parts.length != 12) {
//                System.err.println("Skipping malformed line (expected 12 parts): " + line);
//                continue;
//            }
//
//            try {
//                List<Integer> values = Arrays.stream(parts)
//                        .map(String::trim)
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//
//                double y1 = ORIGINAL_IMAGE_HEIGHT - values.get(1);
//                double y2 = ORIGINAL_IMAGE_HEIGHT - values.get(4);
//                double y3 = ORIGINAL_IMAGE_HEIGHT - values.get(7);
//
//                Point p1 = new Point(values.get(0) * SCALE_FACTOR + OFFSET_X, y1 * SCALE_FACTOR + OFFSET_Y, Z_PLANE);
//                Point p2 = new Point(values.get(3) * SCALE_FACTOR + OFFSET_X, y2 * SCALE_FACTOR + OFFSET_Y, Z_PLANE);
//                Point p3 = new Point(values.get(6) * SCALE_FACTOR + OFFSET_X, y3 * SCALE_FACTOR + OFFSET_Y, Z_PLANE);
//
//                Color triangleColor = new Color(values.get(9), values.get(10), values.get(11));
//
//                scene.geometries.add(
//                        new Triangle(p1, p2, p3)
//                                .setMaterial(defaultTriangleMaterial)
//                                .setEmission(triangleColor)
//                );
//                trianglesAddedCount++;
//
//            } catch (NumberFormatException e) {
//                System.err.println("Skipping line due to invalid number format: " + line + " - " + e.getMessage());
//            }
//        }
//
//        System.out.println("Added " + trianglesAddedCount + " triangles to the scene.");
//
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        scene.lights.add(
//                new PointLight(new Color(150, 150, 150), new Point(0, 0, SCALED_HEIGHT * 2))
//                        .setKl(0.0008)
//                        .setKq(0.00003)
//        );
//
//        cameraBuilder
//                .setLocation(new Point(0, 0, SCALED_HEIGHT * 1.0))
//                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setVpDistance(SCALED_HEIGHT * 1.0)
//                .setVpSize(SCALED_WIDTH, SCALED_HEIGHT)
//                .setResolution(ORIGINAL_IMAGE_WIDTH, ORIGINAL_IMAGE_HEIGHT)
//                .build()
//                .renderImage()
//                .writeToImage("polyArtRender_FillScreen_Final");
//
//        System.out.println("Finished rendering. Output saved as 'polyArtRender_FillScreen_Final.png'");
//    }
//}


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
                .build()
                .renderImage()
                .writeToImage("polyArtRender_Full_NoBorder");

        System.out.println("Finished – check 'polyArtRender_Full_NoBorder.png'");
    }
}
