package renderer;

import geometries.Sphere;
import lighting.AmbientLight;
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

public class MoonScene {

    private final Scene scene = new Scene("Moon With Texture Using Dots");
    private final Camera.Builder cameraBuilder = Camera.getBuilder().setRayTracer(scene, RayTracerType.SIMPLE);

    private static final int VIEWPORT_SIZE = 1000;
    private static final int IMAGE_RESOLUTION = 1000;
    private static final double MOON_RADIUS = 150;

    @Test
    void renderMoonWithTextureDots() {
        scene.setBackground(new Color(0, 0, 0));
        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 10)));

        // טען את התמונה של הירח
        BufferedImage moonTexture;
        try {
            moonTexture = ImageIO.read(new File("C:\\Users\\linoy\\Downloads\\moon.png"));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load moon texture image", e);
        }

        Point moonCenter = new Point(0, 0, 0);

        int width = moonTexture.getWidth();
        int height = moonTexture.getHeight();

        double samplingStep = 3; // דגימה כל 8 פיקסלים (ליעילות)
        double dotRadius = MOON_RADIUS * 0.03; // רדיוס קטן

        for (int y = 0; y < height; y += samplingStep) {
            double v = (double) y / height;
            double theta = v * Math.PI;

            for (int x = 0; x < width; x += samplingStep) {
                int rgb = moonTexture.getRGB(x, y);
                java.awt.Color awtColor = new java.awt.Color(rgb);

                // דילוג על פיקסלים כהים מאוד (רקע)
                if (awtColor.getRed() < 20 && awtColor.getGreen() < 20 && awtColor.getBlue() < 20)
                    continue;

                double u = (double) x / width;
                double phi = u * 2 * Math.PI;

                double sinTheta = Math.sin(theta);
                double px = moonCenter.xyz.d1() + MOON_RADIUS * sinTheta * Math.cos(phi);
                double py = moonCenter.xyz.d2() + MOON_RADIUS * Math.cos(theta);
                double pz = moonCenter.xyz.d3() + MOON_RADIUS * sinTheta * Math.sin(phi);

                Color dotColor = new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());

                scene.geometries.add(
                        new Sphere(new Point(px, py, pz), dotRadius)
                                .setEmission(dotColor)
                                .setMaterial(new Material().setKD(0).setKS(0).setShininess(0))
                );

            }
        }
        // מצלמה
        cameraBuilder
                .setLocation(new Point(0, 0, 1000))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(VIEWPORT_SIZE)
                .setVpSize(VIEWPORT_SIZE, VIEWPORT_SIZE)
                .setResolution(IMAGE_RESOLUTION, IMAGE_RESOLUTION)
                .setMultithreading(-2)
                .setDebugPrint(1)
                .build()
                .renderImage()
                .writeToImage("Moon_Texture");

        System.out.println("✔ Moon rendered using image-based dot texture");
    }
}