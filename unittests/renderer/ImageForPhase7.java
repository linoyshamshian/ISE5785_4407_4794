package renderer;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Refined custom test scene to demonstrate rendering effects with soft lighting,
 * normal colors, and spread-out objects.
 * Fixes: No concurrent high KR and KT on closed objects. Uses a single Triangle instead of a pyramid.
 */
class RefinedCustomSceneTests { // Keeping the class name as it is a refinement

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Refined Custom Scene");
    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE); // Use SimpleRayTracer


    /**
     * Produce a custom picture with various effects, refined for clarity and softness.
     */
    @Test
    void myRefinedCustomScene() { // Keeping the method name

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
}