package renderer;

import geometries.Intersectable.Intersection;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Simple implementation of a ray tracer.
 * Currently, this implementation does not support ray tracing
 * and will throw an exception when attempting to trace a ray.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for SimpleRayTracer.
     *
     * @param scene the scene to trace rays in
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculates the color at a given intersection point.
     * <p>
     * The resulting color is calculated as the sum of:
     * <ul>
     *     <li>The ambient light of the scene, attenuated by the material's kA (ambient coefficient)</li>
     *     <li>The emission color of the intersected geometry</li>
     * </ul>
     *
     * @param intersection The intersection for which the color is calculated.
     * @return The resulting color at the intersection point.
     */
    private Color calcColor(Intersection intersection) {
        return scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(intersection.geometry.getEmission());
    }


    /**
     * Traces a ray and determines the color seen along that ray.
     * <p>
     * If the ray intersects any geometry, the closest intersection is used to calculate the color.
     * If no intersection is found, the background color of the scene is returned.
     *
     * @param ray The ray to trace through the scene.
     * @return The color visible along the ray.
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }

        Intersection closestIntersection = ray.findClosestIntersection(intersections);
        return calcColor(closestIntersection);
    }

}
