package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Custom test scene demonstrating a snowman with various geometric shapes,
 * materials, and lighting, adapted to the existing test framework.
 */
class RefinedCustomSceneTests1 {

    /** Scene for the tests */
    private final Scene scene = new Scene("Refined Custom Scene");
    /** Camera builder for the tests */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE); // Use SimpleRayTracer

    /**
     * Produce a custom picture with various effects, refined for clarity and softness.
     * (This is your original test method, kept for context.)
     */
    @Test
    void myRefinedCustomScene() {

        // --- Geometry ---

        // 1. Reflective Floor (large plane) - Lighter grey, subtle reflection
        scene.geometries.add(
                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1)) // Plane at Z=0, normal pointing up
                        .setEmission(new Color(40, 40, 40)) // Lighter grey base
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.1)
                                .setShininess(5)
                                .setKR(0.2)) // Slightly less reflection
        );

        // 2. Transparent Sphere (glass-like) - High KT, very low KR
        scene.geometries.add(
                new Sphere(new Point(-40, 40, 50), 30d) // Positioned to the left-front
                        .setEmission(new Color(15, 15, 20)) // Slight blue tint, slightly softer
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.2)
                                .setShininess(40)
                                .setKT(0.7) // Slightly less transparent for better visibility
                                .setKR(0.01))
        );

        // 3. Opaque Triangle - Demonstrating Diffuse and Specular highlights
        // Replacing the pyramid with a single, clearly visible triangle
        Point t1 = new Point(50, -50, 20);   // Base point 1
        Point t2 = new Point(80, -20, 20);   // Base point 2
        Point t3 = new Point(65, -35, 100);  // Apex of the triangle (higher)

        Material triangleMaterial = new Material()
                .setKD(0.6) // Strong diffuse
                .setKS(0.4) // Moderate specular
                .setShininess(25); // Softer specular highlight
        Color triangleColor = new Color(120, 70, 40); // Milder orange-brown

        scene.geometries.add(
                new Triangle(t1, t2, t3)
                        .setMaterial(triangleMaterial)
                        .setEmission(triangleColor)
        );


        // 4. Mirror sphere - High KR, very low KT
        scene.geometries.add(
                new Sphere(new Point(0, -60, 40), 25d) // Positioned in front-center
                        .setEmission(new Color(10, 10, 10)) // Slightly brighter emission for better visibility
                        .setMaterial(new Material()
                                .setKD(0.01)
                                .setKS(0.01)
                                .setShininess(100)
                                .setKR(0.9) // Slightly less pure reflection to pick up more ambient light
                                .setKT(0.0))
        );


        // 5. Back Wall (Plane) - To replace the black background
        scene.geometries.add(
                new Plane(new Point(0, 0, -500), new Vector(0, 0, 1)) // Z=-500, much further back
                        .setEmission(new Color(180, 180, 190)) // Light grey-blue for a clean background
                        .setMaterial(new Material().setKD(0.5).setKS(0.0).setShininess(1)) // Matte material
        );

        // --- Lighting ---

        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20))); // Slightly brighter ambient light

        // Point Light - Main light source, softer
        scene.lights.add(
                new PointLight(new Color(300, 300, 300), new Point(-80, -80, 150)) // Milder white light
                        .setKl(0.0008) // Increased linear attenuation
                        .setKq(0.00003) // Increased quadratic attenuation for very soft falloff
        );

        // Spot Light on the triangle - Softer and less focused
        Point spotLightPos = new Point(70, 70, 120);
        // Pointing towards the center of the triangle face
        Vector directionToTriangle = new Point(65, -35, 50).subtract(spotLightPos).normalize();
        scene.lights.add(
                new SpotLight(new Color(250, 250, 250), spotLightPos, directionToTriangle) // Milder white light
                        .setKl(0.0008)
                        .setKq(0.00003) // Increased attenuation for very soft falloff
        );

        // --- Camera setup & rendering ---

        cameraBuilder
                .setLocation(new Point(0, -200, 100))
                .setDirection(new Point(0, 0, 40), new Vector(0, 1, 0))
                .setVpDistance(150)
                .setVpSize(200, 200)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("myRefinedCustomSceneRender_v3"); // New output file name
    }


    /**
     * Renders a snowman scene, integrating it into the existing test framework.
     */
    @Test
    void renderSnowmanScene() {
        // Ensure the scene is clean for this test.
        scene.lights.clear(); // Clear existing lights
        scene.setBackground(new Color(135, 206, 235)); // Sky blue background

        // Reduced ambient light to allow black objects to appear black
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // --- Geometry for Snowman ---

        // Snow floor (CHANGED TO LIGHT GREY/BLUE FOR CONTRAST)
        scene.geometries.add(
                new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
                        // Changed emission from white to a light grey-blue
                        .setEmission(new Color(200, 210, 220)) // Light blue-grey for the snow surface
                        .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20))
        );

        // Snowman body - ADJUSTED Z TO BE FURTHER BACK TO ALLOW FOR FOREGROUND OBJECTS
        scene.geometries.add(
                new Sphere(new Point(0, -100, -200), 45d)  // Bottom sphere - Z changed from -150 to -200, RADIUS REDUCED
                        .setEmission(new Color(white))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),

                new Sphere(new Point(0, -30, -200), 35d)   // Middle sphere - Z changed from -150 to -200
                        .setEmission(new Color(white))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),

                new Sphere(new Point(0, 10, -200), 20d)    // Top sphere (head) - Z changed from -150 to -200
                        .setEmission(new Color(white))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))
        );

        // Eyes - ADJUSTED Z FORWARD, AND MADE SMALLER
        scene.geometries.add(
                new Sphere(new Point(-5, 13, -170), 2d) // Z from -125 to -170, radius 2.5 to 2
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKD(0.01).setKS(0.01).setShininess(1)),

                new Sphere(new Point(5, 13, -170), 2d)  // Z from -125 to -170, radius 2.5 to 2
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKD(0.01).setKS(0.01).setShininess(1))
        );

        // Carrot nose - ADJUSTED Z FORWARD, AND MADE SMALLER
        scene.geometries.add(
                new Sphere(new Point(0, 10, -165), 3d).setEmission(new Color(255, 140, 0)) // Z from -120 to -165, radius 3.5 to 3
        );

        // --- Mouth (BACK TO TRIANGLE, ADJUSTED FOR VISIBILITY) ---
        // These points are chosen to make it wide and clearly visible,
        // and positioned further forward than the rest of the face features.
        Point mouthP1 = new Point(-10, 0, -155);   // Left base point, significantly more forward
        Point mouthP2 = new Point(5, 0, -155);    // Right base point, significantly more forward
        Point mouthP3 = new Point(0, -3, -155);    // Apex of the triangle (pointing downwards for a smiley/neutral look)

        scene.geometries.add(
                new Triangle(mouthP1, mouthP2, mouthP3)
                        .setEmission(new Color(255, 0, 0)) // Pure Red
        );


        // --- Buttons ---
        Material buttonMaterial = new Material().setKD(0.01).setKS(0.01).setShininess(1);

        // 3 buttons on the middle sphere
        scene.geometries.add(
                new Sphere(new Point(0, -10, -165), 2.5d) // Middle sphere button 1
                        .setEmission(Color.BLACK)
                        .setMaterial(buttonMaterial),
                new Sphere(new Point(0, -25, -165), 2.5d) // Middle sphere button 2
                        .setEmission(Color.BLACK)
                        .setMaterial(buttonMaterial),
                new Sphere(new Point(0, -40, -165), 2.5d) // Middle sphere button 3
                        .setEmission(Color.BLACK)
                        .setMaterial(buttonMaterial)
        );

        // 4 buttons on the bottom sphere
        scene.geometries.add(
                new Sphere(new Point(0, -68, -150), 3d)
                        .setEmission(Color.BLACK), // Absolute black emission

                new Sphere(new Point(0, -86, -150), 3d)
                        .setEmission(Color.BLACK), // Absolute black emission

                new Sphere(new Point(0, -104, -150), 3d)
                        .setEmission(Color.BLACK), // Absolute black emission

                new Sphere(new Point(0, -122, -150), 3d)
                        .setEmission(Color.BLACK) // Absolute black emission
        );


        // Wooden arms (as cylinders) - ADJUSTED Z FURTHER BACK
        scene.geometries.add(
                new Cylinder(new Ray(new Point(-30, -30, -200), new Vector(-0.8, 0.5, 0)), 2d, 35d) // Z from -150 to -200
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(30)),

                new Cylinder(new Ray(new Point(30, -30, -200), new Vector(0.8, 0.5, 0)), 2d, 35d) // Z from -150 to -200
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(30))
        );

        // Hat (base + top part) - ADJUSTED Z FORWARD
        scene.geometries.add(
                new Cylinder(new Ray(new Point(0, 29, -195), new Vector(0, 1, 0)), 12d, 3d) // Z from -145 to -195
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKD(0.01).setKS(0.01).setShininess(1)),

                new Cylinder(new Ray(new Point(0, 32, -195), new Vector(0, 1, 0)), 8d, 15d) // Z from -145 to -195
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKD(0.01).setKS(0.01).setShininess(1))
        );

        // פתיתי שלג פזורים באוויר
        for (int i = -100; i <= 100; i += 30) {
            for (int j = -100; j <= 100; j += 30) {
                scene.geometries.add(
                        new Sphere(new Point(i, j, -100 + Math.random() * 150), 1)
                                .setEmission(new Color(255, 255, 255))
                                .setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10))
                );
            }
        }
        // פנים קדמי של המתנה
        scene.geometries.add(
                new Polygon(
                        new Point(20, -150, -180), new Point(40, -150, -180),
                        new Point(40, -130, -180), new Point(20, -130, -180)
                ).setEmission(new Color(255, 20, 147)) // ורוד מנצנץ
                        .setMaterial(new Material().setKD(0.4).setKS(0.8).setShininess(100)),

                // למעלה
                new Polygon(
                        new Point(20, -130, -180), new Point(40, -130, -180),
                        new Point(40, -130, -160), new Point(20, -130, -160)
                ).setEmission(new Color(255, 105, 180)) // ורוד-בהיר
                        .setMaterial(new Material().setKD(0.3).setKS(0.9).setShininess(150)),

                // סרט אופקי על הפאה הקדמית
                new Polygon(
                        new Point(28, -150, -179.9), new Point(32, -150, -179.9),
                        new Point(32, -130, -179.9), new Point(28, -130, -179.9)
                ).setEmission(new Color(255, 215, 0)) // זהב
                        .setMaterial(new Material().setKD(0.2).setKS(1.0).setShininess(200)),

                // סרט אנכי על הפאה העליונה
                new Polygon(
                        new Point(29, -130, -180), new Point(31, -130, -180),
                        new Point(31, -130, -160), new Point(29, -130, -160)
                ).setEmission(new Color(255, 215, 0)) // זהב
                        .setMaterial(new Material().setKD(0.2).setKS(1.0).setShininess(200)),

                // קישוט כפתור נוצץ באמצע
                new Sphere(new Point(30, -130, -170), 1.5)
                        .setEmission(new Color(255, 255, 255)) // לבן נוצץ
                        .setMaterial(new Material().setKD(0.1).setKS(1.0).setShininess(300))
        );



        // --- Lighting ---

        // Main Light Source
        scene.lights.add(
                new SpotLight(new Color(700, 700, 600),
                        new Point(-70, 70, 100),
                        new Vector(0, -1, -0.5))
                        .setKl(0.00005).setKq(0.000001)
        );

        // Fill Light
        scene.lights.add(
                new PointLight(new Color(300, 300, 350),
                        new Point(50, -50, 50))
                        .setKl(0.0001).setKq(0.000005)
        );

        // --- Camera setup & rendering ---
        cameraBuilder
                .setLocation(new Point(-30, -50, 50)) // Camera location
                .setDirection(new Point(0, -20, -180), new Vector(0, 1, 0)) // Target snowman's head, adjusted Z
                .setVpDistance(200) // Increased View Plane Distance for wider view, from 100 to 200
                .setVpSize(300, 300) // Increased View Plane Size for wider view, from 150,150 to 300,300
                .setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("snowman_refined_v5"); // Changed output file name
    }
}