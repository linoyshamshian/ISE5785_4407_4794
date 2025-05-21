package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
     * Computes the final color at the intersection point including local lighting effects.
     *
     * @param intersection the intersection data
     * @param ray          the viewing ray
     * @return the resulting color at the intersection
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK; // No light contribution
        }
        return scene.ambientLight.getIntensity().add(calcColorLocalEffects(intersection));
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
        return calcColor(ray.findClosestIntersection(intersections), ray);
    }

    /**
     * Initializes intersection data with the ray direction and normal at the intersection point.
     *
     * @param intersection the intersection object to update
     * @param rayDirection the direction of the intersecting ray
     * @return true if the dot product of normal and view direction is non-zero, false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection.normalize(); // View direction
        intersection.n = intersection.geometry.getNormal(intersection.point); // Surface normal
        intersection.nv = alignZero(intersection.n.dotProduct(intersection.v)); // dot(n, v)
        return !isZero(intersection.nv); // If dot product is zero, there is no contribution
    }

    /**
     * Sets light-related vectors in the intersection object.
     *
     * @param intersection the intersection object to update
     * @param lightSource  the light source for calculating lighting effects
     * @return true if light contributes to the shading (dot products are valid), false otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.light = lightSource;
        intersection.l = lightSource.getL(intersection.point); // Light direction
        intersection.nl = alignZero(intersection.n.dotProduct(intersection.l)); // dot(n, l)

        // Only use light source if signs of dot products match
        return !isZero(intersection.nl) && !isZero(intersection.nv) && intersection.nl * intersection.nv > 0;
    }

    /**
     * Calculates local lighting effects (diffuse and specular) for the given intersection.
     *
     * @param intersection the intersection with initialized geometry and normal data
     * @return the color resulting from local lighting at the point
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission(); // Start with emission color
        for (LightSource lightSource : scene.lights) {
            if (setLightSource(intersection, lightSource) && unshaded(intersection)) {

                Color iL = lightSource.getIntensity(intersection.point); // Light intensity
                // Add the scaled light contribution to the total color
                color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
            }
        }

        return color;
    }

    /**
     * Calculates the diffuse lighting component using Lambert's cosine law.
     *
     * @param intersection the intersection containing light and surface data
     * @return the diffuse reflection coefficient as a Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        double absNL = Math.abs(intersection.nl); // |dot(n, l)|
        return intersection.material.kD.scale(absNL);
    }

    /**
     * Calculates the specular lighting component using the Phong reflection model.
     *
     * @param intersection the intersection containing light, normal, and view vectors
     * @return the specular reflection coefficient as a Double3
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector l = intersection.l;
        Vector n = intersection.n;
        Vector v = intersection.v;

        // Compute reflection vector: r = l - 2*(n•l)*n
        Vector r = l.subtract(n.scale(2 * intersection.nl)).normalize();
        double vr = alignZero(v.scale(-1).dotProduct(r)); // dot(v, r)

        if (vr <= 0) return Double3.ZERO;

        return intersection.material.kS.scale(Math.pow(vr, intersection.material.nSh));
    }

    private static final double DELTA = 0.1;

    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Vector delta = intersection.n.scale(intersection.nl < 0 ? DELTA : -DELTA);
        Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(shadowRay);

        // If there are no intersections, the point is unshaded
        if (null == intersections || intersections.isEmpty()) {
            return true;
        }

        // Calculate the distance from the point to the light source
        double lightDistance = intersection.light.getDistance(intersection.point);

        // Check if there is any intersection point that blocks the light before it reaches the point
        for (Intersection intersect : intersections) {
            double disPoints = intersect.point.distance(intersection.point);
            if (disPoints < lightDistance - DELTA) {
                // Light is blocked by another object → the point is in shadow
                return false;
            }
        }

        // No object blocks the light → the point is unshaded
        return true;
    }

}
