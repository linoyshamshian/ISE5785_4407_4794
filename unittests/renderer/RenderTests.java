package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class RenderTests {
    /**
     * Default constructor to satisfy JavaDoc generator
     */
    public RenderTests() { /* to satisfy JavaDoc generator */ }

    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder() //
            .setLocation(Point.ZERO).setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
            .setVpDistance(100) //
            .setVpSize(500, 500);

    /**
     * Produce a scene with basic 3D model and render it into a png image with a
     * gridnew Color(75, 127, 90)
     */
    @Test
    public void renderTwoColorTest() {
        Scene scene = new Scene("Two color").setBackground(new Color(255, 191, 191))
                .setAmbientLight(new AmbientLight(Color.BLACK));
        scene.geometries //
                .add(// center
                        new Sphere(new Point(0, 0, -100), 50d),
                        // up left
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
                        // down left
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
                        // down right
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .printGrid(100, new Color(white)) //
                .writeToImage("Two color render test");
    }

    // For stage 6

    /**
     * Produce a scene with basic 3D model - including individual lights of the
     * bodies and render it into a png image with a grid
     */
    @Test
    void renderMultiColorTest() {
        Scene scene = new Scene("Multi color").setAmbientLight(new AmbientLight(new Color(51, 51, 51)));
        scene.geometries //
                .add(// center
                        new Sphere(new Point(0, 0, -100), 50),
                        // up left
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)) //
                                .setEmission(new Color(GREEN)),
                        // down left
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)) //
                                .setEmission(new Color(RED)),
                        // down right
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)) //
                                .setEmission(new Color(BLUE)));

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .printGrid(100, new Color(WHITE)) //
                .writeToImage("color render test");
    }


    /**
     * Test method for rendering a scene using ambient reflection only.
     * <p>
     * The scene contains a sphere and three triangles. Each geometry is given a
     * different ambient reflection coefficient (kA) to demonstrate how ambient light
     * interacts with different materials.
     * <p>
     * The ambient light is set to full white, and there is no emission color on the
     * geometries. The output image will reflect only the ambient component.
     */
    @Test
    void renderAmbientReflectionTest() {
        Scene scene = new Scene("Ambient Reflection Test")
                .setAmbientLight(new AmbientLight(new Color(white)));

        scene.geometries
                .add(// center
                        new Sphere(new Point(0, 0, -100), 50)
                                .setMaterial(new Material().setKA(new Double3(0.4))),

                        // up left
                        new Triangle(new Point(-100, 0, -100),
                                new Point(0, 100, -100),
                                new Point(-100, 100, -100))
                                .setMaterial(new Material().setKA(new Double3(0, 0.8, 0))),

                        // down left
                        new Triangle(new Point(-100, 0, -100),
                                new Point(0, -100, -100),
                                new Point(-100, -100, -100))
                                .setMaterial(new Material().setKA(new Double3(0.8, 0, 0))),

                        // down right
                        new Triangle(new Point(100, 0, -100),
                                new Point(0, -100, -100),
                                new Point(100, -100, -100))
                                .setMaterial(new Material().setKA(new Double3(0, 0, 0.8)))
                );

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .printGrid(100, new Color(WHITE)) //
                .writeToImage("color render test");
    }

}
