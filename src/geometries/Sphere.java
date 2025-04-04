package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        return null;
    }
}
