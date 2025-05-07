package renderer;

import primitives.Color;
import primitives.Point;
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
     * Calculates the color at the given intersection point.
     * For now, returns only the ambient light of the scene.
     *
     * @param point the intersection point
     * @return the ambient light color
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }

    /**
     * Traces a ray in the scene.
     * If the ray intersects with a geometry, returns the ambient light color.
     * Otherwise, returns the background color.
     *
     * @param ray the ray to trace
     * @return the color of the intersection point or background color
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }

        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

}
