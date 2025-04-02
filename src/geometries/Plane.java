package geometries;

import primitives.Point;
import primitives.Vector;

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
}
