package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
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

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getHead();
        Vector v = ray.getDirection();

        if (P0.equals(center)) {
            return List.of(P0.add(v.scale(radius)));
        }

        Vector u;
        try {
            u = center.subtract(P0);
        } catch (IllegalArgumentException e) {
            return null;
        }

        double tm = u.dotProduct(v);
        double dSquared = u.lengthSquared() - tm * tm;
        double rSquared = radius * radius;

        if (dSquared > rSquared) return null;

        double thSquared = rSquared - dSquared;
        if (thSquared <= 0) return null;

        double th = Math.sqrt(thSquared);
        double t1 = tm - th;
        double t2 = tm + th;

        if (isZero(P0.distance(center) - radius)) {
            Vector toCenter = center.subtract(P0);
            if (v.dotProduct(toCenter) <= 0) {
                return null;
            }
            if (t2 > 0) {
                return List.of(ray.getPoint(t2));
            } else {
                return null;
            }
        }

        List<Point> intersections = null;

        if (t1 > 0) {
            intersections = new ArrayList<>();
            intersections.add(ray.getPoint(t1));
        }

        if (t2 > 0) {
            if (intersections == null)
                intersections = new ArrayList<>();
            intersections.add(ray.getPoint(t2));
        }

        if (intersections == null || intersections.size() < 2)
            return intersections;

        // מיון לפי מרחק מראש הקרן
        intersections.sort((p1, p2) -> {
            double d1 = P0.distanceSquared(p1);
            double d2 = P0.distanceSquared(p2);
            return Double.compare(d1, d2);
        });

        return intersections;
    }
}
