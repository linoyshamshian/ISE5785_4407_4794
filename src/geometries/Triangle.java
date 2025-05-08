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
    public List<Point> findIntersections(Ray ray) {
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

        return planeIntersections;
    }

//    public List<Point> findIntersections(Ray ray) {
//        // First, intersect the ray with the triangle's plane.
//        List<Point> planeIntersections = plane.findIntersections(ray);
//        if (planeIntersections == null)
//            // No intersection with the plane, so no intersection with the triangle.
//            return null;
//
//        // Get the intersection point with the plane.
//        Point p = planeIntersections.get(0);
//
//
//        // Get the vertices of the triangle.
//        Point p1 = vertices.get(0);
//        Point p2 = vertices.get(1);
//        Point p3 = vertices.get(2);
//        // Retrieve the direction vector and head point of the ray
//        Vector rayDirection = ray.getDirection();
//        Point rayPoint = ray.getHead();
//
//        if (p1.equals(rayPoint) || p2.equals(rayPoint) || p3.equals(rayPoint))
//            return null; // The ray's head is one of the triangle's vertices
//        // Reject intersections that are exactly on the triangle's vertices (to avoid zero vectors)
//        if (p.equals(p1) || p.equals(p2) || p.equals(p3)) return null;
//
//        // Calculate the vectors from the vertices to the point.
//        Vector v0 = p.subtract(p1);
//        Vector v1 = p.subtract(p2);
//        Vector v2 = p.subtract(p3);
//        if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
//            return null;
//        }
//        // Calculate the cross products of the edges and the vectors to the point.
//        Vector n1 = p2.subtract(p1).crossProduct(v0);
//        Vector n2 = p3.subtract(p2).crossProduct(v1);
//        Vector n3 = p1.subtract(p3).crossProduct(v2);
//
//        // Check if all the cross products point in the same direction.
//        if (n1.dotProduct(n2) > 0 && n2.dotProduct(n3) > 0) {
//            // The intersection point is inside the triangle.
//            return List.of(p);
//        }
//        // The intersection point is outside the triangle.
//        return null;
//    }

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
        Point p = planeIntersections.get(0); // Intersection point with the plane.

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

        // אם כל הסימנים זהים
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            // The intersection point is inside the triangle.
            return List.of(p);
        }

        return null;
    }
}
