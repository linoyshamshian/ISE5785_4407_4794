package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * The Plane class represents a geometric plane in 3D space.
 * The plane is defined by a point and a normal vector. It also provides a method to get the normal vector at any point on the plane.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Plane extends Geometry {
    private final Vector normal;
    private final Point q;

    /**
     * Constructor for creating a Plane with three points.
     *
     * @param p0 The first point on the plane.
     * @param p1 The second point on the plane.
     * @param p2 The third point on the plane.
     */
    public Plane(Point p0, Point p1, Point p2) {
        this.q = p0;
        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        this.normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Constructor for creating a Plane with a point and a normal vector.
     *
     * @param q      The point on the plane.
     * @param normal The normal vector of the plane.
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector of the plane at the given point (q).
     *
     * @param point The point at which to calculate the normal. For a plane, this is always the same.
     * @return The normal vector.
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * Returns the normal vector of the plane.
     * This method can be used without specifying a point as the normal is constant for the entire plane.
     *
     * @return The normal vector of the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Finds the intersection point(s) between the given ray and the plane.
     * <p>
     * If the ray is parallel to the plane or starts on the plane – there is no intersection.
     * If the intersection point is behind the ray's head (t <= 0), it is not considered valid.
     * Otherwise, the method returns a list with the single intersection point.
     *
     * @param ray the ray to intersect with the plane
     * @return list with one intersection point if there is an intersection; {@code null} otherwise
     */

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Ray starting point
        Point p0 = ray.getHead();

        // Ray direction vector
        Vector v = ray.getDirection();

        // If the ray starts exactly at the reference point of the plane
        if (q.equals(p0)) {
            return null; // no intersection – edge case
        }

        // Vector from ray start to plane point (Q - P0)
        Vector qMinusP0 = q.subtract(p0);

        // Numerator of the t formula: normal • (Q - P0)
        double numerator = normal.dotProduct(qMinusP0);

        // Denominator of the t formula: normal • v
        double denominator = normal.dotProduct(v);

        // If the denominator is 0, the ray is parallel to the plane
        if (isZero(denominator)) {
            return null; // no intersection
        }

        // Compute t = numerator / denominator
        double t = numerator / denominator;

        // If t is zero or negative – intersection is behind the ray
        if (t <= 0) {
            return null; // no valid intersection
        }

        // Calculate intersection point: P = P0 + t*v
        return List.of(ray.getPoint(t));
    }



}
