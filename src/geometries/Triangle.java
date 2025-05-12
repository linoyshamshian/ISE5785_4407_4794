package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class representing a triangle in 3D space.
 * Inherits from Polygon.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Triangle extends Polygon {

    /**
     * Constructor for Triangle, which passes its three points to the Polygon constructor.
     *
     * @param p0 The first vertex of the triangle.
     * @param p1 The second vertex of the triangle.
     * @param p2 The third vertex of the triangle.
     */
    public Triangle(Point p0, Point p1, Point p2) {
        super(p0, p1, p2); // Call Polygon constructor
    }

    /**
     * Finds the intersection point(s) between a ray and the triangle.
     * The method first checks for intersection with the plane of the triangle,
     * and then determines if the intersection point is within the triangle itself
     * using the edge-cross-product method.
     *
     * @param ray The ray to check intersection with.
     * @return A list with a single intersection point if the ray intersects the triangle, otherwise {@code null}.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        var planeIntersections = plane.findIntersections(ray);
        // Check if the ray intersects the plane of the triangle
        if (planeIntersections == null)
            return null;

        // Retrieve the vertices of the triangle
        Point p0 = vertices.get(0);
        Point p1 = vertices.get(1);
        Point p2 = vertices.get(2);
        // Retrieve the direction vector and head point of the ray
        Vector rayDirection = ray.getDirection();
        Point rayPoint = ray.getHead();

        if (p0.equals(rayPoint) || p1.equals(rayPoint) || p2.equals(rayPoint))
            return null; // The ray's head is one of the triangle's vertices

        // Calculate vectors representing edges of the triangle
        Vector v1 = p0.subtract(rayPoint);
        Vector v2 = p1.subtract(rayPoint);
        // Calculate normal vectors to the triangle's edges
        Vector n1 = v1.crossProduct(v2).normalize();
        // Calculate dot products between the normal vectors and the ray direction
        double d1 = alignZero(n1.dotProduct(rayDirection));
        // Check if the ray does not intersect the triangle.
        if (d1 == 0)
            return null;

        Vector v3 = p2.subtract(rayPoint);
        Vector n2 = v2.crossProduct(v3).normalize();
        double d2 = alignZero(n2.dotProduct(rayDirection));
        // Check if the ray does not intersect the triangle
        if (d1 * d2 <= 0)
            return null;

        Vector n3 = v3.crossProduct(v1).normalize();
        double d3 = alignZero(n3.dotProduct(rayDirection));
        // Check if the ray does not intersect the triangle
        if (d1 * d3 <= 0)
            return null;

        return List.of(new Intersection(this,planeIntersections.getFirst()));
    }

    /**
     * Alternative intersection method using the pyramid method (based on normals between edges and ray origin).
     * This checks whether the intersection point lies inside the triangle by comparing signs of dot products.
     *
     * @param ray The ray to check intersection with.
     * @return A list with a single intersection point if inside the triangle, otherwise {@code null}.
     */
    public List<Point> findIntersectionsPyramid(Ray ray) {
        // 1. Intersect ray with triangle's plane.
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) {
            return null; // No intersection with the plane, so no intersection with the triangle.
        }
        Point p = planeIntersections.getFirst(); // Intersection point with the plane.

        // 2. Check if the intersection point is inside the triangle using the pyramid method.
        Point p0 = ray.getHead();
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);

        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        Vector v3 = p3.subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        Vector v = ray.getDirection();

        double s1 = v.dotProduct(n1);
        double s2 = v.dotProduct(n2);
        double s3 = v.dotProduct(n3);


        if (isZero(s1) || isZero(s2) || isZero(s3)) return null;

        // if all the signs are the same
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            // The intersection point is inside the triangle.
            return List.of(p);
        }

        return null;
    }
}
