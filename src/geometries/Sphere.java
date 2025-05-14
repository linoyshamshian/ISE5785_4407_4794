package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a Sphere, a 3D geometry with a center point and a radius.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Sphere extends RadialGeometry {

    private final Point center; // Center of the sphere

    /**
     * Constructor for Sphere.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     * @throws IllegalArgumentException if the radius is negative or zero.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal of the sphere at a given point.
     *
     * @param point The point on the sphere.
     * @return The normal vector, or null for the sphere.
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Finds intersection points between a ray and the sphere.
     * <p>
     * Calculates the intersection points using geometric relations between
     * the ray and the sphere. Returns up to two points in front of the ray,
     * or null if there are no valid intersections.
     *
     * @param ray The ray to intersect with the sphere.
     * @return A list of intersection points, or null if none.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Ray starts at the center → one intersection in direction of the ray
        if (p0.equals(center)) {
            return List.of(new Intersection(this, ray.getPoint(radius)));
        }

        Vector u = center.subtract(p0);
        double tm = v.dotProduct(u);
        double dSquared = u.lengthSquared() - tm * tm;
        double rSquared = radius * radius;

        // No intersection: ray misses the sphere or just touches (tangent)
        if (dSquared >= rSquared) return null;

        double th = Math.sqrt(rSquared - dSquared);

        // Tangent case (touches only one point) – doesn't count as intersection
        if (isZero(th)) return null;

        double t1 = tm - th;
        double t2 = tm + th;

        // Only create list if we actually have intersections
        if (t1 > 0 && t2 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
        } else if (t1 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t1)));
        } else if (t2 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }

        return null;
    }


}
