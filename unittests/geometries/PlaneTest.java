package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.Point;
import primitives.Vector;
import primitives.Util;

class PlaneTest {

    /**
     * Test method for {@link geometries.Plane#getNormal()}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP: Create a plane from three non-collinear points
        Plane plane = new Plane(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));

        // Expected normal: (0, 0, 1) or (0, 0, -1), depending on implementation
        Vector normal = plane.getNormal();
        Vector v1 = new Vector(1, 0, 0);
        Vector v2 = new Vector(0, 1, 0);

        // TC01: Check that the normal is perpendicular to v1
        assertEquals(0, Util.alignZero(normal.dotProduct(v1)), "Normal is not perpendicular to v1");
        // TC02: Check that the normal is perpendicular to v2
        assertEquals(0, Util.alignZero(normal.dotProduct(v2)), "Normal is not perpendicular to v2");
        // TC03: Ensure the normal is a unit vector
        assertEquals(0, Util.alignZero(normal.length() - 1), "Normal is not a unit vector");
    }

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Vector)}.
     */
    @Test
    void testConstructorWithPointAndNormal() {
        // =============== Boundary Values Tests ==================
        // BVA: Define a plane with a point and a normal
        Point p = new Point(1, 2, 3);
        Vector normal = new Vector(0, 0, 1);
        Plane plane = new Plane(p, normal);

        // TC01: Retrieve the normal and ensure it is the same (or opposite) as given normal
        assertEquals(
                1,
                Util.alignZero(normal.normalize().dotProduct(plane.getNormal())),
                "Normal is not correct");
    }


    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorWithThreePoints() {
        // Define three valid points that are not collinear
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(2, 0, 0);
        Point p3 = new Point(0, 1, 0);

        // Create the plane with these three points
        Plane plane = new Plane(p1, p2, p3);

        // Get the normal of the plane
        Vector normal = plane.getNormal();

        // Check that the normal is perpendicular to the vector from p1 to p2 (should be perpendicular)
        Vector p1ToP2 = p2.subtract(p1);
        Vector p1ToP3 = p3.subtract(p1);

        // TC01: Check that the normal is perpendicular to p1->p2
        assertEquals(
                0,
                Util.alignZero(p1ToP2.dotProduct(normal)),
                "Normal is not perpendicular to vector p1->p2");
        // TC02: Check that the normal is perpendicular to p1->p3
        assertEquals(
                0,
                Util.alignZero(p1ToP3.dotProduct(normal)),
                "Normal is not perpendicular to vector p1->p3");

        // TC03: Ensure the normal is a unit vector
        assertEquals(1, normal.length(), "Normal is not unit length");

        // =============== Boundary Values Tests ==================

        // TC11: First and second points coincide (should throw an exception)
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(1, 1, 1),
                        new Point(1, 1, 1),
                        new Point(2, 2, 2)));
        // TC12: First and third points coincide (should throw an exception)
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(1, 1, 1),
                        new Point(2, 2, 2),
                        new Point(1, 1, 1)));
        // TC13: Second and third points coincide (should throw an exception)
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(2, 2, 2),
                        new Point(3, 3, 3),
                        new Point(3, 3, 3)));
        // TC14: All three points coincide (should throw an exception)
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(1, 1, 1),
                        new Point(1, 1, 1),
                        new Point(1, 1, 1)));
        // TC15: All points are collinear (should throw an exception)
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(0, 0, 0),
                        new Point(1, 1, 1),
                        new Point(2, 2, 2)));
    }

}