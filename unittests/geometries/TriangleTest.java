package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for
     * {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testTriangleGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Triangle, check normal vector (length = 1)
        Point p0 = new Point(0, 0, 0);
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Triangle triangle = new Triangle(p0, p1, p2);
        Vector normal = triangle.getNormal(p0);

        // Check that the normal vector has length 1
        assertEquals(
                1,
                normal.length(),
                DELTA,
                "Normal vector length should be 1");

        // Check that the normal is orthogonal to the vector (0, 0, 1)
        double dotProduct = normal.dotProduct(new Vector(0, 0, 1));
        assertEquals(
                1,
                Math.abs(dotProduct),
                DELTA,
                "Normal direction is incorrect");

    }

    /**
     * Test method for
     * {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testTriangleConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Valid triangle created with non-collinear points
        Point p0 = new Point(0, 0, 0);
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);

        // Creating a valid triangle
        Triangle triangle = new Triangle(p0, p1, p2);
        assertNotNull(
                triangle,
                "Triangle creation failed with valid points");

        // TC02: Invalid triangle with collinear points (should throw exception)
        Point p3 = new Point(0, 0, 0);
        Point p4 = new Point(1, 1, 1);
        Point p5 = new Point(2, 2, 2);

        // Creating a triangle with collinear points should throw an exception
        assertThrows(
                IllegalArgumentException.class,
                () -> new Triangle(p3, p4, p5),
                "Exception should be thrown for collinear points");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(
                new Point(1, 0, 1),
                new Point(2, 0, 3),
                new Point(3, 0, 1)
        );

        // ======== Equivalence Partitions Tests ========

        // TC01: Intersection point is inside the triangle
        Ray rayInside = new Ray(
                new Point(2, 1, 2),
                new Vector(0, -1, 0)
        );
        final var result1 = triangle.findIntersections(rayInside);
        assertEquals(
                1,
                result1.size(),
                "Wrong number of points");

        assertEquals(
                List.of(new Point(2, 0, 2)),
                triangle.findIntersections(rayInside),
                "Ray intersects inside the triangle"
        );

        // TC02: Intersection point is outside the triangle, opposite to one edge
        Ray rayOutsideEdge = new Ray(
                new Point(0.5, 1, 2),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersections(rayOutsideEdge),
                "Ray outside opposite to one edge should not intersect"
        );

        // TC03: Intersection point is outside the triangle, opposite to one vertex
        Ray rayOutsideVertex = new Ray(
                new Point(2, 1, 4),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersections(rayOutsideVertex),
                "Ray outside opposite to one vertex should not intersect"
        );

        // ======== Boundary Values Tests ========

        // TC04: Intersection point is on an edge of the triangle

        Ray rayOnEdge = new Ray(
                new Point(1.5, 1, 2),
                new Vector(0, -1, 0)
        );

        assertNull(
                triangle.findIntersections(rayOnEdge),
                "Intersection exactly on vertex should return null"
        );
        // TC05: Intersection point is exactly on a vertex of the triangle
        Ray rayOnVertex = new Ray(
                new Point(1, 1, 1),
                new Vector(0, -1, 0)
        );

        assertNull(
                triangle.findIntersections(rayOnVertex)
                , "Intersection exactly on vertex should throw an exception due to zero vector creation");

        // TC06: Intersection point is on the extension of an edge
        Ray rayOnEdgeExtension = new Ray(
                new Point(0, 1, 1),
                new Vector(0, -1, 0)
        );

        assertNull(
                triangle.findIntersections(rayOnEdgeExtension)
                , "Intersection exactly on vertex should throw an exception due to zero vector creation");

    }

    /**
     * Test method for {@link geometries.Triangle#findIntersectionsPyramid(primitives.Ray)}.
     */
    @Test
    void testFindIntersectionsPyramid() {
        Triangle triangle = new Triangle(
                new Point(1, 0, 1),
                new Point(2, 0, 3),
                new Point(3, 0, 1)
        );

        // ======== Equivalence Partitions Tests ========

        // TC01: Intersection point is inside the triangle
        Ray rayInside = new Ray(
                new Point(2, 1, 2),
                new Vector(0, -1, 0)
        );
        final var result1 = triangle.findIntersectionsPyramid(rayInside);
        assertEquals(
                1,
                result1.size(),
                "Wrong number of points");

        assertEquals(
                List.of(new Point(2, 0, 2)),
                triangle.findIntersectionsPyramid(rayInside),
                "Ray intersects inside the triangle"
        );

        // TC02: Intersection point is outside the triangle, opposite to one edge
        Ray rayOutsideEdge = new Ray(
                new Point(0.5, 1, 2),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersectionsPyramid(rayOutsideEdge),
                "Ray outside opposite to one edge should not intersect"
        );

        // TC03: Intersection point is outside the triangle, opposite to one vertex
        Ray rayOutsideVertex = new Ray(
                new Point(2, 1, 4),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersectionsPyramid(rayOutsideVertex),
                "Ray outside opposite to one vertex should not intersect"
        );

        // ======== Boundary Values Tests ========

        // TC04: Intersection point is on an edge of the triangle

        Ray rayOnEdge = new Ray(
                new Point(1.5, 1, 2),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersectionsPyramid(rayOnEdge),
                "Ray intersecting exactly on edge should return null"
        );
        // TC05: Intersection point is exactly on a vertex of the triangle
        Ray rayOnVertex = new Ray(
                new Point(1, 1, 1),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersectionsPyramid(rayOnVertex),
                "Ray intersecting exactly on edge should return null"
        );
        // TC06: Intersection point is on the extension of an edge
        Ray rayOnEdgeExtension = new Ray(
                new Point(0, 1, 1),
                new Vector(0, -1, 0)
        );
        assertNull(
                triangle.findIntersectionsPyramid(rayOnEdgeExtension),
                "Ray intersecting exactly on edge should return null"
        );
    }
}