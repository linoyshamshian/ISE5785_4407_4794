package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

/**
 * Test rendering a basic image
 *
 * @author Dan Zilberstein
 */
class LightsTests {
    /**
     * Default constructor to satisfy JavaDoc generator
     */
    LightsTests() { /* to satisfy JavaDoc generator */ }

    /**
     * First scene for some of tests
     */
    private final Scene scene1 = new Scene("Test scene");
    /**
     * Second scene for some of tests
     */
    private final Scene scene2 = new Scene("Test scene")
            .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

    /**
     * First camera builder for some of tests
     */
    private final Camera.Builder camera1 = Camera.getBuilder()                                          //
            .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
            .setLocation(new Point(0, 0, 1000))                                                                              //
            .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
            .setVpSize(150, 150).setVpDistance(1000);

    /**
     * Second camera builder for some of tests
     */
    private final Camera.Builder camera2 = Camera.getBuilder()                                          //
            .setRayTracer(scene2, RayTracerType.SIMPLE)                                                                      //
            .setLocation(new Point(0, 0, 1000))                                                                              //
            .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
            .setVpSize(200, 200).setVpDistance(1000);

    /**
     * Shininess value for most of the geometries in the tests
     */
    private static final int SHININESS = 301;
    /**
     * Diffusion attenuation factor for some of the geometries in the tests
     */
    private static final double KD = 0.5;
    /**
     * Diffusion attenuation factor for some of the geometries in the tests
     */
    private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

    /**
     * Specular attenuation factor for some of the geometries in the tests
     */
    private static final double KS = 0.5;
    /**
     * Specular attenuation factor for some of the geometries in the tests
     */
    private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

    /**
     * Material for some of the geometries in the tests
     */
    private final Material material = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);
    /**
     * Light color for tests with triangles
     */
    private final Color trianglesLightColor = new Color(800, 500, 250);
    /**
     * Light color for tests with sphere
     */
    private final Color sphereLightColor = new Color(800, 500, 0);
    /**
     * Color of the sphere
     */
    private final Color sphereColor = new Color(BLUE).reduce(2);

    /**
     * Center of the sphere
     */
    private final Point sphereCenter = new Point(0, 0, -50);
    /**
     * Radius of the sphere
     */
    private static final double SPHERE_RADIUS = 50d;

    /**
     * The triangles' vertices for the tests with triangles
     */
    private final Point[] vertices =
            {
                    // the shared left-bottom:
                    new Point(-110, -110, -150),
                    // the shared right-top:
                    new Point(95, 100, -150),
                    // the right-bottom
                    new Point(110, -110, -150),
                    // the left-top
                    new Point(-75, 78, 100)
            };
    /**
     * Position of the light in tests with sphere
     */
    private final Point sphereLightPosition = new Point(-50, -50, 25);
    /**
     * Light direction (directional and spot) in tests with sphere
     */
    private final Vector sphereLightDirection = new Vector(1, 1, -0.5);
    /**
     * Position of the light in tests with triangles
     */
    private final Point trianglesLightPosition = new Point(30, 10, -100);
    /**
     * Light direction (directional and spot) in tests with triangles
     */
    private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

    /**
     * The sphere in appropriate tests
     */
    private final Geometry sphere = new Sphere(sphereCenter, SPHERE_RADIUS)
            .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));
    /**
     * The first triangle in appropriate tests
     */
    private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
            .setMaterial(material);
    /**
     * The first triangle in appropriate tests
     */
    private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
            .setMaterial(material);

    /**
     * Produce a picture of a sphere lighted by a directional light
     */
    @Test
    void sphereDirectional() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightSphereDirectional");
    }

    /**
     * Produce a picture of a sphere lighted by a point light
     */
    @Test
    void spherePoint() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition) //
                .setKl(0.001).setKq(0.0002));

        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightSpherePoint");
    }

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    void sphereSpot() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection) //
                .setKl(0.001).setKq(0.0001));

        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightSphereSpot");
    }

    /**
     * Produce a picture of two triangles lighted by a directional light
     */
    @Test
    void trianglesDirectional() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

        camera2.setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTrianglesDirectional");
    }

    /**
     * Test for a pair of triangles illuminated by a narrow beam spotlight.
     * <p>
     * This test adds two triangles to the scene and illuminates them using a spotlight with a focused narrow beam.
     * The spotlight originates from a point above and to the side of the triangles and is directed at an angle.
     * The narrow beam (setNarrowBeam = 20) concentrates the light, resulting in a sharp, focused lighting effect
     * with a clear highlight and minimal spread, which tests the enhanced spotlight functionality.
     * The final rendered image is saved to "trianglesWithNarrowSpotLight".
     */
    @Test
    void trianglesSpotLightNarrowBeam() {
        scene2.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(300)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(300))
        );

        scene2.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(60, 60, 100), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006)
                        .setNarrowBeam(20)
        );

        camera2.setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("trianglesWithNarrowSpotLight");
    }


    /**
     * Produce a picture of two triangles lighted by a point light
     */
    @Test
    void trianglesPoint() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition) //
                .setKl(0.001).setKq(0.0002));

        camera2.setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTrianglesPoint");
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight
     */
    @Test
    void trianglesSpot() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
                .setKl(0.001).setKq(0.0001));

        camera2.setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTrianglesSpot");
    }

    /**
     * Produce a picture of a sphere lighted by a narrow spotlight
     */
    @Test
    void sphereSpotSharp() {
        scene1.geometries.add(sphere);
        scene1.lights
                .add(new SpotLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5)) //
                        .setKl(0.001).setKq(0.00004).setNarrowBeam(10));

        camera1.setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightSphereSpotSharp");
    }

    /**
     * Test for a sphere illuminated by a narrow beam spotlight.
     * <p>
     * The test creates a red sphere and adds a spotlight with a narrow beam (focused beam using setNarrowBeam).
     * The light originates from a point and is directed toward the sphere with a limited spread,
     * which creates a sharper and more focused lighting effect compared to a regular spotlight.
     * The result is rendered to an image file named "sphereWithNarrowSpotLight".
     */
    @Test
    void sphereSpotLightNarrowBeam() {
        scene1.geometries.add(
                new Sphere(new Point(0, 0, -50), 50d)
                        .setEmission(new Color(RED))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(300))
        );

        scene1.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(100, 100, 100), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006)
                        .setNarrowBeam(10)
        );

        camera1.setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("sphereWithNarrowSpotLight");
    }


    /**
     * Produce a picture of two triangles lighted by a narrow spotlight
     */
    @Test
    void trianglesSpotSharp() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
                .setKl(0.001).setKq(0.00004).setNarrowBeam(10));

        camera2.setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTrianglesSpotSharp");
    }


    /**
     * Test rendering a sphere with multiple types of lights:
     * DirectionalLight, PointLight, and SpotLight.
     * The test adds a sphere to the scene and applies three different light sources
     * with different colors, directions, and attenuation factors.
     * The rendered image is saved with the name "sphereMultipleLights".
     */
    @Test
    void sphereMultipleLights() {
        scene1.geometries.add(sphere);

        scene1.lights.add(
                new DirectionalLight(
                        new Color(300, 150, 0),
                        new Vector(-1, -0.5, -0.5)));
        scene1.lights.add(
                new PointLight(
                        new Color(200, 250, 100),
                        new Point(60, -40, 20)).setKl(0.001).setKq(0.0002));
        scene1.lights.add(
                new SpotLight(new Color(500, 300, 0),
                        new Point(-80, 60, 100), new Vector(1, -1, -2))
                        .setKl(0.001).setKq(0.0001));

        camera1
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("sphereMultipleLights");
    }

    /**
     * Test rendering two triangles with multiple types of lights:
     * DirectionalLight, PointLight, and SpotLight.
     * The test adds two triangles to the scene and applies three different light sources
     * with different colors, directions, and attenuation factors.
     * The rendered image is saved with the name "trianglesMultipleLights".
     */
    @Test
    void trianglesMultipleLights() {
        scene2.geometries.add(triangle1, triangle2);

        scene2.lights.add(
                new DirectionalLight(
                        new Color(400, 200, 100),
                        new Vector(-1, -1, -1)));
        scene2.lights.add(
                new PointLight(
                        new Color(300, 300, 150),
                        new Point(40, 40, -120)).setKl(0.001).setKq(0.0001));
        scene2.lights.add(
                new SpotLight(
                        new Color(500, 250, 250),
                        new Point(-60, 30, 50), new Vector(2, -2, -1))
                        .setKl(0.0005).setKq(0.00005));

        camera2
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("trianglesMultipleLights");
    }

    /**
     * Test rendering a cylinder illuminated by a combination of directional, point, and spot lights.
     * The scene tests how different light sources interact with a non-trivial geometry (cylinder) and
     * how the lighting effects such as shading and specular highlights are handled.
     */
    @Test
    void cylinderDirectional() {
        Geometry cylinder = new Cylinder(
                new Ray(new Point(0, -20, -50), new Vector(7, 1, -8)),
                50, 60
        )
                .setEmission(new Color(java.awt.Color.pink).reduce(1))
                .setMaterial(new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS));

        scene1.geometries.add(cylinder);
        scene1.lights.add(
                new DirectionalLight(
                        new Color(300, 150, 0),
                        new Vector(-1, -0.5, -0.5)));
        scene1.lights.add(
                new PointLight(
                        new Color(200, 250, 100),
                        new Point(60, -40, 20)).setKl(0.001).setKq(0.0002));
        scene1.lights.add(
                new SpotLight(new Color(500, 300, 0),
                        new Point(-80, 60, 100), new Vector(1, -1, -2))
                        .setKl(0.001).setKq(0.0001));
        camera1.setResolution(500, 500)
                .setBlackboard(new Blackboard(9))
                .build()
                .renderImage()
                .writeToImage("lightCylinderDirectional_with_anti");
    }


    /**
     * The tube used in the tests
     */
    private final Geometry tube = new Tube(new Ray(new Point(0, 0, -100), new Vector(0, 1, 0)), 30)
            .setEmission(new Color(RED)) //
            .setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));

    /**
     * Produce a picture of a tube lighted by a directional light
     */
    @Test
    void tubeDirectional() {
        scene1.geometries.add(tube);
        scene1.lights.add(new DirectionalLight(new Color(800, 500, 250), new Vector(1, 1, -1)));

        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTubeDirectional");
    }

    /**
     * Produce a picture of a tube lighted by a point light
     */
    @Test
    void tubePoint() {
        scene1.geometries.add(tube);
        scene1.lights.add(new PointLight(new Color(800, 500, 250), new Point(-50, -50, 25)) //
                .setKl(0.001).setKq(0.0002));

        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTubePoint");
    }

    /**
     * Produce a picture of a tube lighted by a spotlight
     */
    @Test
    void tubeSpot() {
        scene1.geometries.add(tube);
        scene1.lights.add(new SpotLight(new Color(800, 500, 250), new Point(-50, -50, 25), new Vector(1, 1, -0.5)) //
                .setKl(0.001).setKq(0.0001));

        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("lightTubeSpot");
    }


}
