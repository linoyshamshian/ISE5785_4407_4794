package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
}