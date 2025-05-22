package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
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
     * Maximum recursion level for global effects like reflection and refraction.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * Minimum attenuation coefficient to continue recursion.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Initial attenuation coefficient (no attenuation).
     */
    private static final Double3 INITIAL_K = Double3.ONE;

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
            return Color.BLACK;
        }

        return calcColor(intersection, ray,MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * Calculates the color at the intersection point, including local lighting and global effects.
     *
     * @param intersection the intersection information
     * @param level        current recursion depth
     * @param k            cumulative attenuation coefficient
     * @return the final color at the intersection point
     */

    private Color calcColor(Intersection intersection, Ray ray, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection);
        return level == 1 ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }



    /**
     * Traces a ray and determines the color seen along that ray.
     *
     * @param ray The ray to trace through the scene.
     * @return The color visible along the ray.
     */
    @Override
    public Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        if (closestIntersection == null) {
            return scene.background;
        }
        if (!preprocessIntersection(closestIntersection, ray.getDirection())) {
            return Color.BLACK;
        }

        return calcColor(closestIntersection, ray);
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
        intersection.l = lightSource.getL(intersection.point).normalize();
        // Light direction
        intersection.nl = intersection.n.dotProduct(intersection.l); // dot(n, l)
        // Only use light source if signs of dot products match
        return !isZero(intersection.nl) && !isZero(intersection.nv) && alignZero(intersection.nl * intersection.nv) > 0.0;
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
            Vector l=lightSource.getL(intersection.point);
//            if(intersection.nl* intersection.nv>0)
//            {
//                Double3 ktr=
//            }
            if (setLightSource(intersection, lightSource) && unshaded(intersection)) {

                Color iL = lightSource.getIntensity(intersection.point); // Light intensity
                // Add the scaled light contribution to the total color
                color = color.add(iL.scale(calcDiffusive(intersection)
                        .add(calcSpecular(intersection))));
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

    /**
     * Determines whether a point on a surface is unshaded (i.e., fully illuminated by a light source).
     * This method checks if any geometry blocks the light from reaching the point by casting a shadow ray
     * toward the light source. If the ray intersects any geometry before reaching the light, the point is considered shaded.
     *
     * @param intersection An {@link Intersection} object containing details about the intersection point,
     *                     its normal vector, light direction, and dot product (nl).
     * @return {@code true} if the point is unshaded (no blocking geometry), {@code false} if the point is in shadow.
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.n);

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
            if (disPoints<lightDistance) {
                Double3 transparency = intersect.material.kT; // assuming red channel is used
                if (transparency.lowerThan(MIN_CALC_COLOR_K) ) {
                    return false;
                }
            }
        }

        // No object blocks the light → the point is unshaded
        return true;
    }

    /**
     * Constructs a reflected ray based on the surface normal and incoming direction.
     * This is typically used to calculate reflection rays from shiny (mirror-like) surfaces.
     *
     * @param intersection the intersection point containing the point and the surface normal
     * @param ray          the incoming ray hitting the surface
     * @return the reflected ray constructed from the intersection point and direction
     */
    private Ray constructReflectedRay(Intersection intersection, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = intersection.n;
        double vn = v.dotProduct(n);
        return new Ray(intersection.point, v.subtract(n.scale(2 * vn)), n);
    }

    /**
     * Constructs a refracted ray (transmitted through a transparent material).
     * Assumes no bending of the ray (i.e., ignores Snell’s law and keeps direction unchanged).
     *
     * @param intersection the intersection point containing the point and the surface normal
     * @param ray          the incoming ray hitting the surface
     * @return the refracted ray starting from the intersection point in the same direction
     */
    private Ray constructRefractedRay(Intersection intersection, Ray ray) {
        return new Ray(intersection.point, ray.getDirection(), intersection.n);
    }

    /**
     * Calculates the global effect (either reflection or refraction) for a single ray.
     *
     * @param ray   the reflected or refracted ray
     * @param level the recursion level
     * @param kx    the material coefficient (reflection/refraction)
     * @param k     the cumulative transparency coefficient
     * @return the resulting color from tracing the global ray
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.background;
        }
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }
        return calcColor(intersection,ray, level - 1, kkx).scale(kx);
    }

    /**
     * Calculates global effects (reflection and refraction) for a given intersection.
     *
     * @param intersection the intersection at which global effects are to be calculated
     * @param ray          the incoming ray hitting the surface
     * @param level        the current recursion depth
     * @param k            the current accumulated transparency coefficient
     * @return color resulting from global effects (reflected and refracted rays)
     */
    private Color calcGlobalEffects(Intersection intersection , Ray ray, int level, Double3 k) {
        Material material = intersection.geometry.getMaterial();
        return calcGlobalEffect(constructReflectedRay(intersection, ray),  level,material.kR, k)
                .add(calcGlobalEffect(constructRefractedRay(intersection, ray), level,material.kT,  k));
    }

    /**
     * Finds the closest intersection of the given ray with the scene geometries.
     *
     * @param ray the ray to check for intersections
     * @return the closest intersection, or null if no intersection exists
     */
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }


}
