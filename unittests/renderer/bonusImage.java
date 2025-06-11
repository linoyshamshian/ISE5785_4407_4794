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
                .setMultithreading(3)
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
        // Gift Box (a complete cube with a ribbon and a bow) - Significantly Larger and Closer
        //Define common material for the gift box body
        Material giftBoxMaterial = new Material().setKD(0.2).setKS(0.4).setShininess(70);
        Color giftBoxColor = new Color(255, 20, 147); // Sparkling Pink

        // Define common material for the gift box top
        Material giftBoxTopMaterial = new Material().setKD(0.2).setKS(0.4).setShininess(100);
        Color giftBoxTopColor = new Color(255, 105, 180); // Lighter Pink for top

        // Define common material for the ribbon
        Material ribbonMaterial = new Material().setKD(0.2).setKS(0.6).setShininess(200);
        Color ribbonColor = new Color(255, 215, 0); // Gold

        // Position and size for the gift box (increased size, slightly different Z)
        double boxX = 30; // Center X
        double boxY = -150; // Bottom Y (keeping it on the "snow" level)
        double boxZ = -130; // Front Z (moved significantly closer to the camera for prominence)
        double boxSize = 25; // Side length (increased for better visibility)

        // Calculate points for the cube based on its center and size
        Point p1 = new Point(boxX - boxSize / 2, boxY, boxZ); // Front-bottom-left
        Point p2 = new Point(boxX + boxSize / 2, boxY, boxZ); // Front-bottom-right
        Point p3 = new Point(boxX + boxSize / 2, boxY + boxSize, boxZ); // Front-top-right
        Point p4 = new Point(boxX - boxSize / 2, boxY + boxSize, boxZ); // Front-top-left

        Point p5 = new Point(boxX - boxSize / 2, boxY, boxZ - boxSize); // Back-bottom-left
        Point p6 = new Point(boxX + boxSize / 2, boxY, boxZ - boxSize); // Back-bottom-right
        Point p7 = new Point(boxX + boxSize / 2, boxY + boxSize, boxZ - boxSize); // Back-top-right
        Point p8 = new Point(boxX - boxSize / 2, boxY + boxSize, boxZ - boxSize); // Back-top-left

        // Add faces of the gift box
        scene.geometries.add(
                // Front face
                new Polygon(p1, p2, p3, p4).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Back face
                new Polygon(p6, p5, p8, p7).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Left face
                new Polygon(p5, p1, p4, p8).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Right face
                new Polygon(p2, p6, p7, p3).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Top face
                new Polygon(p4, p3, p7, p8).setEmission(giftBoxTopColor).setMaterial(giftBoxTopMaterial),
                // Bottom face
                new Polygon(p5, p6, p2, p1).setEmission(giftBoxColor).setMaterial(giftBoxMaterial)
        );

        // Ribbon - Horizontal (around Y-axis, passing through the middle)
        double ribbonThickness = 2; // Thickness of the ribbon
        scene.geometries.add(
                // Horizontal ribbon section visible on the front face
                new Polygon(
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ + 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ + 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ + 0.1),
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ + 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Horizontal ribbon section visible on the back face
                new Polygon(
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize - 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize - 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize - 0.1),
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize - 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Horizontal ribbon section visible on the left face
                new Polygon(
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ),
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Horizontal ribbon section visible on the right face
                new Polygon(
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ),
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial)
        );


        // Ribbon - Vertical (around X-axis, passing through the middle)
        scene.geometries.add(
                // Vertical ribbon section visible on the top face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize + 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize + 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize + 0.1, boxZ - boxSize),
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize + 0.1, boxZ - boxSize)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Vertical ribbon section visible on the bottom face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY - 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY - 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY - 0.1, boxZ - boxSize),
                        new Point(boxX - ribbonThickness / 2, boxY - 0.1, boxZ - boxSize)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Vertical ribbon section visible on the front face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY, boxZ + 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY, boxZ + 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize, boxZ + 0.1),
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize, boxZ + 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Vertical ribbon section visible on the back face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY, boxZ - boxSize - 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY, boxZ - boxSize - 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize, boxZ - boxSize - 0.1),
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize, boxZ - boxSize - 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial)
        );


        // Bow on top - more distinct loops using cylinders for a flattened look
        double bowBaseY = boxY + boxSize  +0.5; // Top of the box
        double bowBaseZ = boxZ - boxSize / 2; // Center Z of the box
        double bowLoopRadius = 2; // Radius of the loops (thin)

        double bowSegmentLength = 10; // Length of each cylinder segment
        double bowBendFactor = 4; // How much the loops bend upwards/outwards

        // Left Loop
        // Arc segment 1 (up and left)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX - 8, bowBaseY, bowBaseZ - 2), new Vector(-0.5, 1, 0.1)), bowLoopRadius, bowSegmentLength)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
        // Arc segment 2 (more up and left)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX - 12, bowBaseY + bowBendFactor * 0.5, bowBaseZ - 2), new Vector(-0.3, 1, 0.1)), bowLoopRadius, bowSegmentLength * 0.8)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
        // Arc segment 3 (top of the loop, curving inwards)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX - 10, bowBaseY + bowBendFactor * 0.8, bowBaseZ - 2), new Vector(0.5, 0.5, 0.1)), bowLoopRadius, bowSegmentLength * 0.7)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));


        // Right Loop - mirrored from Left Loop
//         Arc segment 1 (up and right)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX + 8, bowBaseY, bowBaseZ - 2), new Vector(0.5, 1, 0.1)), bowLoopRadius, bowSegmentLength)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
//         Arc segment 2 (more up and right)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX + 12, bowBaseY + bowBendFactor * 0.5, bowBaseZ - 2), new Vector(0.3, 1, 0.1)), bowLoopRadius, bowSegmentLength * 0.8)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
        // Arc segment 3 (top of the loop, curving inwards)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX + 10, bowBaseY + bowBendFactor * 0.8, bowBaseZ - 2), new Vector(-0.5, 0.5, 0.1)), bowLoopRadius, bowSegmentLength * 0.7)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));


        // --- Tree Geometry (using Polygons for foliage) ---
        // Trunk (Cylinder - assuming Cylinder class exists)
        double trunkX = -180; // Position X of the tree
        double trunkY = -150; // Base Y of the tree (on the snow)
        double trunkZ = -300; // Z position (behind the snowman)
        double trunkRadius = 10;
        double trunkHeight = 170;

        scene.geometries.add(
                new Cylinder(new Ray(new Point(trunkX, trunkY, trunkZ), new Vector(0, 1, 0)), trunkRadius, trunkHeight)
                        .setEmission(new Color(101, 67, 33)) // Brown color
                        .setMaterial(new Material().setKD(0.5).setKS(0.1).setShininess(2))
        );

        // Foliage (Pyramid made of Triangles)
        double pyramidBaseY = trunkY + trunkHeight; // Base of the pyramid is top of the trunk
        double pyramidHeight = 80; // Height of the pyramid
        double pyramidBaseSide = 80; // Length of one side of the square base

    // Define the base points of the square pyramid
        Point pBase1 = new Point(trunkX - pyramidBaseSide / 2, pyramidBaseY, trunkZ - pyramidBaseSide / 2); // Front-Left
        Point pBase2 = new Point(trunkX + pyramidBaseSide / 2, pyramidBaseY, trunkZ - pyramidBaseSide / 2); // Front-Right
        Point pBase3 = new Point(trunkX + pyramidBaseSide / 2, pyramidBaseY, trunkZ + pyramidBaseSide / 2); // Back-Right
        Point pBase4 = new Point(trunkX - pyramidBaseSide / 2, pyramidBaseY, trunkZ + pyramidBaseSide / 2); // Back-Left

    // Define the apex (top point) of the pyramid
        Point pyramidApex = new Point(trunkX, pyramidBaseY + pyramidHeight, trunkZ);

        Material foliageMaterial = new Material().setKD(0.6).setKS(0.2).setShininess(10);
        Color foliageColor = new Color(34, 139, 34); // Forest Green

        scene.geometries.add(
                new Polygon(pBase1, pBase2, pBase3, pBase4)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),

                new Triangle(pBase1, pBase2, pyramidApex) // Front face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pBase2, pBase3, pyramidApex) // Right face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pBase3, pBase4, pyramidApex) // Back face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pBase4, pBase1, pyramidApex) // Left face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial)
        );

    // Second pyramid layer
        double topPyramidBaseY = pyramidBaseY + pyramidHeight * 0.7;
        double topPyramidHeight = pyramidHeight * 0.4;
        double topPyramidBaseSide = pyramidBaseSide * 0.5;

        Point pTopBase1 = new Point(trunkX - topPyramidBaseSide / 2, topPyramidBaseY, trunkZ - topPyramidBaseSide / 2);
        Point pTopBase2 = new Point(trunkX + topPyramidBaseSide / 2, topPyramidBaseY, trunkZ - topPyramidBaseSide / 2);
        Point pTopBase3 = new Point(trunkX + topPyramidBaseSide / 2, topPyramidBaseY, trunkZ + topPyramidBaseSide / 2);
        Point pTopBase4 = new Point(trunkX - topPyramidBaseSide / 2, topPyramidBaseY, trunkZ + topPyramidBaseSide / 2);

        Point topPyramidApex = new Point(trunkX, topPyramidBaseY + topPyramidHeight, trunkZ);

        scene.geometries.add(
                new Triangle(pTopBase1, pTopBase2, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pTopBase2, pTopBase3, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pTopBase3, pTopBase4, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pTopBase4, pTopBase1, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial)
        );

        // Third pyramid layer (smallest one)
        double thirdPyramidBaseY = topPyramidBaseY + topPyramidHeight * 0.8;
        double thirdPyramidHeight = topPyramidHeight * 0.5;
        double thirdPyramidBaseSide = topPyramidBaseSide * 0.5;

        Point pThirdBase1 = new Point(trunkX - thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ - thirdPyramidBaseSide / 2);
        Point pThirdBase2 = new Point(trunkX + thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ - thirdPyramidBaseSide / 2);
        Point pThirdBase3 = new Point(trunkX + thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ + thirdPyramidBaseSide / 2);
        Point pThirdBase4 = new Point(trunkX - thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ + thirdPyramidBaseSide / 2);

        Point thirdPyramidApex = new Point(trunkX, thirdPyramidBaseY + thirdPyramidHeight, trunkZ);

        scene.geometries.add(
                new Triangle(pThirdBase1, pThirdBase2, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pThirdBase2, pThirdBase3, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pThirdBase3, pThirdBase4, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pThirdBase4, pThirdBase1, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial)
        );


        // --- Lighting ---

        // Main Light Source
        scene.lights.add(
                new SpotLight(new Color(700, 700, 500),
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
                .writeToImage("snowman_refined_v5_without_anti"); // Changed output file name
    }
}




/**
 * Custom test scene demonstrating a snowman with various geometric shapes,
 * materials, and lighting, adapted to the existing test framework.
 */
class RefinedCustomSceneTests1_anti {

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
        // Gift Box (a complete cube with a ribbon and a bow) - Significantly Larger and Closer
        //Define common material for the gift box body
        Material giftBoxMaterial = new Material().setKD(0.2).setKS(0.4).setShininess(70);
        Color giftBoxColor = new Color(255, 20, 147); // Sparkling Pink

        // Define common material for the gift box top
        Material giftBoxTopMaterial = new Material().setKD(0.2).setKS(0.4).setShininess(100);
        Color giftBoxTopColor = new Color(255, 105, 180); // Lighter Pink for top

        // Define common material for the ribbon
        Material ribbonMaterial = new Material().setKD(0.2).setKS(0.6).setShininess(200);
        Color ribbonColor = new Color(255, 215, 0); // Gold

        // Position and size for the gift box (increased size, slightly different Z)
        double boxX = 30; // Center X
        double boxY = -150; // Bottom Y (keeping it on the "snow" level)
        double boxZ = -130; // Front Z (moved significantly closer to the camera for prominence)
        double boxSize = 25; // Side length (increased for better visibility)

        // Calculate points for the cube based on its center and size
        Point p1 = new Point(boxX - boxSize / 2, boxY, boxZ); // Front-bottom-left
        Point p2 = new Point(boxX + boxSize / 2, boxY, boxZ); // Front-bottom-right
        Point p3 = new Point(boxX + boxSize / 2, boxY + boxSize, boxZ); // Front-top-right
        Point p4 = new Point(boxX - boxSize / 2, boxY + boxSize, boxZ); // Front-top-left

        Point p5 = new Point(boxX - boxSize / 2, boxY, boxZ - boxSize); // Back-bottom-left
        Point p6 = new Point(boxX + boxSize / 2, boxY, boxZ - boxSize); // Back-bottom-right
        Point p7 = new Point(boxX + boxSize / 2, boxY + boxSize, boxZ - boxSize); // Back-top-right
        Point p8 = new Point(boxX - boxSize / 2, boxY + boxSize, boxZ - boxSize); // Back-top-left

        // Add faces of the gift box
        scene.geometries.add(
                // Front face
                new Polygon(p1, p2, p3, p4).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Back face
                new Polygon(p6, p5, p8, p7).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Left face
                new Polygon(p5, p1, p4, p8).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Right face
                new Polygon(p2, p6, p7, p3).setEmission(giftBoxColor).setMaterial(giftBoxMaterial),
                // Top face
                new Polygon(p4, p3, p7, p8).setEmission(giftBoxTopColor).setMaterial(giftBoxTopMaterial),
                // Bottom face
                new Polygon(p5, p6, p2, p1).setEmission(giftBoxColor).setMaterial(giftBoxMaterial)
        );

        // Ribbon - Horizontal (around Y-axis, passing through the middle)
        double ribbonThickness = 2; // Thickness of the ribbon
        scene.geometries.add(
                // Horizontal ribbon section visible on the front face
                new Polygon(
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ + 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ + 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ + 0.1),
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ + 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Horizontal ribbon section visible on the back face
                new Polygon(
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize - 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize - 0.1),
                        new Point(boxX + boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize - 0.1),
                        new Point(boxX - boxSize / 2, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize - 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Horizontal ribbon section visible on the left face
                new Polygon(
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ),
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX - boxSize / 2 - 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Horizontal ribbon section visible on the right face
                new Polygon(
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ),
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 - ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ - boxSize),
                        new Point(boxX + boxSize / 2 + 0.1, boxY + boxSize / 2 + ribbonThickness / 2, boxZ)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial)
        );


        // Ribbon - Vertical (around X-axis, passing through the middle)
        scene.geometries.add(
                // Vertical ribbon section visible on the top face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize + 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize + 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize + 0.1, boxZ - boxSize),
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize + 0.1, boxZ - boxSize)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Vertical ribbon section visible on the bottom face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY - 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY - 0.1, boxZ),
                        new Point(boxX + ribbonThickness / 2, boxY - 0.1, boxZ - boxSize),
                        new Point(boxX - ribbonThickness / 2, boxY - 0.1, boxZ - boxSize)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Vertical ribbon section visible on the front face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY, boxZ + 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY, boxZ + 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize, boxZ + 0.1),
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize, boxZ + 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial),
                // Vertical ribbon section visible on the back face
                new Polygon(
                        new Point(boxX - ribbonThickness / 2, boxY, boxZ - boxSize - 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY, boxZ - boxSize - 0.1),
                        new Point(boxX + ribbonThickness / 2, boxY + boxSize, boxZ - boxSize - 0.1),
                        new Point(boxX - ribbonThickness / 2, boxY + boxSize, boxZ - boxSize - 0.1)
                ).setEmission(ribbonColor).setMaterial(ribbonMaterial)
        );


        // Bow on top - more distinct loops using cylinders for a flattened look
        double bowBaseY = boxY + boxSize  +0.5; // Top of the box
        double bowBaseZ = boxZ - boxSize / 2; // Center Z of the box
        double bowLoopRadius = 2; // Radius of the loops (thin)

        double bowSegmentLength = 10; // Length of each cylinder segment
        double bowBendFactor = 4; // How much the loops bend upwards/outwards

        // Left Loop
        // Arc segment 1 (up and left)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX - 8, bowBaseY, bowBaseZ - 2), new Vector(-0.5, 1, 0.1)), bowLoopRadius, bowSegmentLength)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
        // Arc segment 2 (more up and left)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX - 12, bowBaseY + bowBendFactor * 0.5, bowBaseZ - 2), new Vector(-0.3, 1, 0.1)), bowLoopRadius, bowSegmentLength * 0.8)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
        // Arc segment 3 (top of the loop, curving inwards)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX - 10, bowBaseY + bowBendFactor * 0.8, bowBaseZ - 2), new Vector(0.5, 0.5, 0.1)), bowLoopRadius, bowSegmentLength * 0.7)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));


        // Right Loop - mirrored from Left Loop
//         Arc segment 1 (up and right)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX + 8, bowBaseY, bowBaseZ - 2), new Vector(0.5, 1, 0.1)), bowLoopRadius, bowSegmentLength)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
//         Arc segment 2 (more up and right)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX + 12, bowBaseY + bowBendFactor * 0.5, bowBaseZ - 2), new Vector(0.3, 1, 0.1)), bowLoopRadius, bowSegmentLength * 0.8)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));
        // Arc segment 3 (top of the loop, curving inwards)
        scene.geometries.add(
                new Cylinder(new Ray(new Point(boxX + 10, bowBaseY + bowBendFactor * 0.8, bowBaseZ - 2), new Vector(-0.5, 0.5, 0.1)), bowLoopRadius, bowSegmentLength * 0.7)
                        .setEmission(ribbonColor).setMaterial(ribbonMaterial));


        // --- Tree Geometry (using Polygons for foliage) ---
        // Trunk (Cylinder - assuming Cylinder class exists)
        double trunkX = -180; // Position X of the tree
        double trunkY = -150; // Base Y of the tree (on the snow)
        double trunkZ = -300; // Z position (behind the snowman)
        double trunkRadius = 10;
        double trunkHeight = 170;

        scene.geometries.add(
                new Cylinder(new Ray(new Point(trunkX, trunkY, trunkZ), new Vector(0, 1, 0)), trunkRadius, trunkHeight)
                        .setEmission(new Color(101, 67, 33)) // Brown color
                        .setMaterial(new Material().setKD(0.5).setKS(0.1).setShininess(2))
        );

        // Foliage (Pyramid made of Triangles)
        double pyramidBaseY = trunkY + trunkHeight; // Base of the pyramid is top of the trunk
        double pyramidHeight = 80; // Height of the pyramid
        double pyramidBaseSide = 80; // Length of one side of the square base

        // Define the base points of the square pyramid
        Point pBase1 = new Point(trunkX - pyramidBaseSide / 2, pyramidBaseY, trunkZ - pyramidBaseSide / 2); // Front-Left
        Point pBase2 = new Point(trunkX + pyramidBaseSide / 2, pyramidBaseY, trunkZ - pyramidBaseSide / 2); // Front-Right
        Point pBase3 = new Point(trunkX + pyramidBaseSide / 2, pyramidBaseY, trunkZ + pyramidBaseSide / 2); // Back-Right
        Point pBase4 = new Point(trunkX - pyramidBaseSide / 2, pyramidBaseY, trunkZ + pyramidBaseSide / 2); // Back-Left

        // Define the apex (top point) of the pyramid
        Point pyramidApex = new Point(trunkX, pyramidBaseY + pyramidHeight, trunkZ);

        Material foliageMaterial = new Material().setKD(0.6).setKS(0.2).setShininess(10);
        Color foliageColor = new Color(34, 139, 34); // Forest Green

        scene.geometries.add(
                new Polygon(pBase1, pBase2, pBase3, pBase4)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),

                new Triangle(pBase1, pBase2, pyramidApex) // Front face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pBase2, pBase3, pyramidApex) // Right face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pBase3, pBase4, pyramidApex) // Back face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pBase4, pBase1, pyramidApex) // Left face
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial)
        );

        // Second pyramid layer
        double topPyramidBaseY = pyramidBaseY + pyramidHeight * 0.7;
        double topPyramidHeight = pyramidHeight * 0.4;
        double topPyramidBaseSide = pyramidBaseSide * 0.5;

        Point pTopBase1 = new Point(trunkX - topPyramidBaseSide / 2, topPyramidBaseY, trunkZ - topPyramidBaseSide / 2);
        Point pTopBase2 = new Point(trunkX + topPyramidBaseSide / 2, topPyramidBaseY, trunkZ - topPyramidBaseSide / 2);
        Point pTopBase3 = new Point(trunkX + topPyramidBaseSide / 2, topPyramidBaseY, trunkZ + topPyramidBaseSide / 2);
        Point pTopBase4 = new Point(trunkX - topPyramidBaseSide / 2, topPyramidBaseY, trunkZ + topPyramidBaseSide / 2);

        Point topPyramidApex = new Point(trunkX, topPyramidBaseY + topPyramidHeight, trunkZ);

        scene.geometries.add(
                new Triangle(pTopBase1, pTopBase2, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pTopBase2, pTopBase3, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pTopBase3, pTopBase4, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pTopBase4, pTopBase1, topPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial)
        );

        // Third pyramid layer (smallest one)
        double thirdPyramidBaseY = topPyramidBaseY + topPyramidHeight * 0.8;
        double thirdPyramidHeight = topPyramidHeight * 0.5;
        double thirdPyramidBaseSide = topPyramidBaseSide * 0.5;

        Point pThirdBase1 = new Point(trunkX - thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ - thirdPyramidBaseSide / 2);
        Point pThirdBase2 = new Point(trunkX + thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ - thirdPyramidBaseSide / 2);
        Point pThirdBase3 = new Point(trunkX + thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ + thirdPyramidBaseSide / 2);
        Point pThirdBase4 = new Point(trunkX - thirdPyramidBaseSide / 2, thirdPyramidBaseY, trunkZ + thirdPyramidBaseSide / 2);

        Point thirdPyramidApex = new Point(trunkX, thirdPyramidBaseY + thirdPyramidHeight, trunkZ);

        scene.geometries.add(
                new Triangle(pThirdBase1, pThirdBase2, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pThirdBase2, pThirdBase3, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pThirdBase3, pThirdBase4, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial),
                new Triangle(pThirdBase4, pThirdBase1, thirdPyramidApex)
                        .setEmission(foliageColor)
                        .setMaterial(foliageMaterial)
        );


        // --- Lighting ---

        // Main Light Source
        scene.lights.add(
                new SpotLight(new Color(700, 700, 500),
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
        Blackboard ssParamsOn = new Blackboard(true, 9);
        // --- Camera setup & rendering ---
        cameraBuilder
                .setLocation(new Point(-30, -50, 50)) // Camera location
                .setDirection(new Point(0, -20, -180), new Vector(0, 1, 0)) // Target snowman's head, adjusted Z
                .setVpDistance(200) // Increased View Plane Distance for wider view, from 100 to 200
                .setVpSize(300, 300) // Increased View Plane Size for wider view, from 150,150 to 300,300
                .setResolution(600, 600)
                .setBlackboard(ssParamsOn)
                .build()
                .renderImage()
                .writeToImage("snowman_refined_v5_with_anti"); // Changed output file name
    }
}




