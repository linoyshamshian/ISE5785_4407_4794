//package renderer;
//
//import static java.awt.Color.*;
//
//import org.junit.jupiter.api.Test;
//
//import geometries.*;
//import lighting.*;
//import primitives.*;
//import scene.Scene;
//
///**
// * An elaborate custom test scene demonstrating various rendering effects
// * with a diverse set of geometries, materials, and lighting.
// * This scene aims for visual complexity and artistic composition,
// * avoiding random placements and showcasing all implemented features.
// */
//class ElaborateCustomSceneTests {
//
//    /** Scene for the tests */
//    private final Scene scene = new Scene("Elaborate Custom Scene");
//    /** Camera builder for the tests */
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(scene, RayTracerType.SIMPLE); // Using SimpleRayTracer for this example
//
//    /**
//     * Produces an elaborate custom picture with diverse objects and effects.
//     */
//    @Test
//    void myElaborateCustomScene() {
//
//        // --- Geometry ---
//
//        // 1. Grand Reflective Floor (Plane) - Central element, subtle reflection
//        scene.geometries.add(
//                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1))
//                        .setEmission(new Color(30, 30, 35)) // Darker, richer grey
//                        .setMaterial(new Material()
//                                .setKD(0.05)
//                                .setKS(0.05)
//                                .setShininess(10)
//                                .setKR(0.3)) // More noticeable reflection than previous
//        );
//
//        // 2. Main Transparent Sphere (Glass Orb) - Prominent, high transparency
//        scene.geometries.add(
//                new Sphere(new Point(-50, 60, 40), 35d) // Larger, positioned to the left-front
//                        .setEmission(new Color(10, 10, 25)) // Deeper blue tint for glass
//                        .setMaterial(new Material()
//                                .setKD(0.05)
//                                .setKS(0.1)
//                                .setShininess(50)
//                                .setKT(0.85) // Very transparent
//                                .setKR(0.05))
//        );
//
//        // 3. Opaque Diffuse Triangle (Deep Red) - Clearly visible, strong diffuse
//        Point t1 = new Point(60, -70, 30);
//        Point t2 = new Point(90, -40, 30);
//        Point t3 = new Point(75, -55, 120); // Taller apex for a dramatic look
//
//        Material triangleMaterial = new Material()
//                .setKD(0.8) // Very strong diffuse
//                .setKS(0.2) // Subtle specular
//                .setShininess(15);
//        Color triangleColor = new Color(150, 30, 30); // Deep red
//
//        scene.geometries.add(
//                new Triangle(t1, t2, t3)
//                        .setMaterial(triangleMaterial)
//                        .setEmission(triangleColor)
//        );
//
//        // 4. Mirror Sphere (Polished Chrome) - Highly reflective, near perfect mirror
//        scene.geometries.add(
//                new Sphere(new Point(20, -80, 25), 20d) // Smaller, positioned closer to center
//                        .setEmission(new Color(5, 5, 5)) // Minimal emission for true reflection
//                        .setMaterial(new Material()
//                                .setKD(0.005)
//                                .setKS(0.005)
//                                .setShininess(150)
//                                .setKR(0.95) // Almost pure reflection
//                                .setKT(0.0))
//        );
//
//        // 5. Back Wall (Plane) - Horizon line, subtle texture
//        scene.geometries.add(
//                new Plane(new Point(0, 0, -600), new Vector(0, 0, 1)) // Far background
//                        .setEmission(new Color(150, 150, 160)) // Muted blue-grey
//                        .setMaterial(new Material().setKD(0.6).setKS(0.05).setShininess(5)) // Slight sheen
//        );
//
//        // 6. Large Opaque Cube (Polygon based) - Using six planes/polygons for a cube
//        // This demonstrates the Polygon class for a more complex shape
//        Point p0 = new Point(-100, -30, 10);
//        Point p1 = new Point(-100, -30, 60);
//        Point p2 = new Point(-100, 20, 60);
//        Point p3 = new Point(-100, 20, 10);
//
//        Point p4 = new Point(-50, -30, 10);
//        Point p5 = new Point(-50, -30, 60);
//        Point p6 = new Point(-50, 20, 60);
//        Point p7 = new Point(-50, 20, 10);
//
//        Material cubeMaterial = new Material().setKD(0.5).setKS(0.3).setShininess(20);
//        Color cubeColor = new Color(80, 50, 100); // Muted purple
//
//        // Front Face
//        scene.geometries.add(new Polygon(p0, p1, p2, p3)
//                .setMaterial(cubeMaterial).setEmission(cubeColor));
//        // Back Face
//        scene.geometries.add(new Polygon(p7, p6, p5, p4)
//                .setMaterial(cubeMaterial).setEmission(cubeColor));
//        // Left Face
//        scene.geometries.add(new Polygon(p3, p2, p6, p7)
//                .setMaterial(cubeMaterial).setEmission(cubeColor));
//        // Right Face
//        scene.geometries.add(new Polygon(p4, p5, p1, p0)
//                .setMaterial(cubeMaterial).setEmission(cubeColor));
//        // Top Face
//        scene.geometries.add(new Polygon(p1, p5, p6, p2)
//                .setMaterial(cubeMaterial).setEmission(cubeColor));
//        // Bottom Face (Partially hidden by floor)
//        scene.geometries.add(new Polygon(p0, p4, p7, p3)
//                .setMaterial(cubeMaterial).setEmission(cubeColor));
//
//
//        // 7. Golden Tube (Cylinder-like object, assuming Cylinder class based on Tube)
//        // Positioned upright, slightly to the right
//        scene.geometries.add(
//                new Tube(new Ray(new Point(90, 0, 0), new Vector(0, 0, 1)), 15d)
//                        .setEmission(new Color(200, 150, 50)) // Golden color
//                        .setMaterial(new Material()
//                                .setKD(0.4)
//                                .setKS(0.6)
//                                .setShininess(60)
//                                .setKR(0.1)) // Slight reflection
//        );
//
//        // 8. Small Reflective Sphere (Blue accent)
//        scene.geometries.add(
//                new Sphere(new Point(-20, -30, 15), 10d)
//                        .setEmission(new Color(10, 10, 50)) // Deep blue
//                        .setMaterial(new Material()
//                                .setKD(0.02)
//                                .setKS(0.8)
//                                .setShininess(80)
//                                .setKR(0.6)) // Moderate reflection
//        );
//
//        // 9. Green Opaque Sphere
//        scene.geometries.add(
//                new Sphere(new Point(40, 20, 20), 12d)
//                        .setEmission(new Color(30, 80, 30)) // Forest green
//                        .setMaterial(new Material()
//                                .setKD(0.7)
//                                .setKS(0.1)
//                                .setShininess(10))
//        );
//
//        // 10. Small Transparent Cube (using Polygons for a smaller, transparent cube)
//        // Positioned next to the main transparent sphere
//        Point sp0 = new Point(-70, 70, 5);
//        Point sp1 = new Point(-70, 70, 25);
//        Point sp2 = new Point(-70, 90, 25);
//        Point sp3 = new Point(-70, 90, 5);
//
//        Point sp4 = new Point(-50, 70, 5);
//        Point sp5 = new Point(-50, 70, 25);
//        Point sp6 = new Point(-50, 90, 25);
//        Point sp7 = new Point(-50, 90, 5);
//
//        Material smallCubeMaterial = new Material()
//                .setKD(0.1).setKS(0.1).setShininess(30)
//                .setKT(0.6).setKR(0.05); // Semi-transparent
//        Color smallCubeColor = new Color(5, 5, 10); // Very subtle blue tint
//
//        scene.geometries.add(new Polygon(sp0, sp1, sp2, sp3)
//                .setMaterial(smallCubeMaterial).setEmission(smallCubeColor));
//        scene.geometries.add(new Polygon(sp7, sp6, sp5, sp4)
//                .setMaterial(smallCubeMaterial).setEmission(smallCubeColor));
//        scene.geometries.add(new Polygon(sp3, sp2, sp6, sp7)
//                .setMaterial(smallCubeMaterial).setEmission(smallCubeColor));
//        scene.geometries.add(new Polygon(sp4, sp5, sp1, sp0)
//                .setMaterial(smallCubeMaterial).setEmission(smallCubeColor));
//        scene.geometries.add(new Polygon(sp1, sp5, sp6, sp2)
//                .setMaterial(smallCubeMaterial).setEmission(smallCubeColor));
//        scene.geometries.add(new Polygon(sp0, sp4, sp7, sp3)
//                .setMaterial(smallCubeMaterial).setEmission(smallCubeColor));
//
//        // --- Lighting ---
//
//        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30))); // Brighter ambient light for overall scene illumination
//
//        // Main Point Light - Warm white, soft falloff
//        scene.lights.add(
//                new PointLight(new Color(400, 400, 350), new Point(-100, -100, 200)) // Warmer light
//                        .setKl(0.0005)
//                        .setKq(0.00001)
//        );
//
//        // Spot Light 1 - Focused on the main transparent sphere, subtle blue tint
//        Point spotLightPos1 = new Point(-60, 50, 100);
//        Vector directionToTransparentSphere = new Point(-50, 60, 40).subtract(spotLightPos1).normalize();
//        scene.lights.add(
//                new SpotLight(new Color(150, 150, 200), spotLightPos1, directionToTransparentSphere) // Cool blue light
//                        .setKl(0.0007)
//                        .setKq(0.00002)
//        );
//
//        // Spot Light 2 - Highlighting the Mirror Sphere and Triangle from above, soft yellow
//        Point spotLightPos2 = new Point(0, -50, 150);
//        Vector directionToCenter = new Point(0, -60, 40).subtract(spotLightPos2).normalize(); // Pointing towards the mirror sphere
//        scene.lights.add(
//                new SpotLight(new Color(300, 300, 200), spotLightPos2, directionToCenter) // Warm yellow light
//                        .setKl(0.0006)
//                        .setKq(0.000015)
//        );
//
//        // --- Camera setup & rendering ---
//
//        cameraBuilder
//                .setLocation(new Point(0, -250, 120)) // Pulled back further, slightly higher
//                .setDirection(new Point(0, 0, 40), new Vector(0, 1, 0)) // Looking towards the scene center
//                .setVpDistance(180) // Increased view plane distance
//                .setVpSize(250, 250) // Larger view plane
//                .setResolution(1000, 1000) // Higher resolution for better detail
//                .build()
//                .renderImage()
//                .writeToImage("myElaborateCustomScene"); // Output file name
//    }
//}
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
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255))); // Reduced ambient light

        // --- Geometry for Snowman ---

        // Snow floor (white plane)
        scene.geometries.add(
                new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(245, 245, 245)) // Off-white for snow
                        .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20))
        );

        // Snowman body
        scene.geometries.add(
                new Sphere(new Point(0, -100, -150), 50d)  // Bottom sphere
                        .setEmission(new Color(white))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),

                new Sphere(new Point(0, -30, -150), 35d)   // Middle sphere
                        .setEmission(new Color(white))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)),

                new Sphere(new Point(0, 10, -150), 20d)    // Top sphere (head)
                        .setEmission(new Color(white))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))
        );

        // Eyes
        scene.geometries.add(
                new Sphere(new Point(-5, 13, -130), 2d).setEmission(Color.BLACK),
                new Sphere(new Point(5, 13, -130), 2d).setEmission(Color.BLACK)
        );

        // Carrot nose
        scene.geometries.add(
                new Sphere(new Point(0, 10, -125), 3d).setEmission(new Color(255, 140, 0))
        );

        // Buttons
        scene.geometries.add(
                new Sphere(new Point(0, -10, -120), 2d).setEmission(Color.BLACK),
                new Sphere(new Point(0, -25, -120), 2d).setEmission(Color.BLACK),
                new Sphere(new Point(0, -40, -120), 2d).setEmission(Color.BLACK)
        );

        // Wooden arms (as cylinders)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(-30, -30, -150), new Vector(-0.8, 0.5, 0)), 2d, 35d)
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(30)),

                new Cylinder(new Ray(new Point(30, -30, -150), new Vector(0.8, 0.5, 0)), 2d, 35d)
                        .setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(30))
        );

        // Hat (base + top part)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(0, 29, -150), new Vector(0, 1, 0)), 12d, 3d)
                        .setEmission(Color.BLACK),

                new Cylinder(new Ray(new Point(0, 32, -150), new Vector(0, 1, 0)), 8d, 15d)
                        .setEmission(Color.BLACK)
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
                .setLocation(new Point(0, -50, 50)) // Closer and lower camera
                .setDirection(new Point(0, -20, -150), new Vector(0, 1, 0)) // Looking at the snowman
                .setVpDistance(100) // Closer view plane
                .setVpSize(150, 150) // Smaller view plane for a tighter shot
                .setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("snowman_refined_v2");
    }
}