package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

class PlaneTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

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
        assertEquals(
                0,
                normal.dotProduct(v1),
                DELTA,
                "Normal is not perpendicular to v1");
        // TC02: Check that the normal is perpendicular to v2
        assertEquals(
                0,
                normal.dotProduct(v2),
                DELTA,
                "Normal is not perpendicular to v2");
        // TC03: Ensure the normal is a unit vector
        assertEquals(
                1,
                normal.length(),
                DELTA,
                "Normal is not a unit vector");
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
                p1ToP2.dotProduct(normal),
                DELTA,
                "Normal is not perpendicular to vector p1->p2");
        // TC02: Check that the normal is perpendicular to p1->p3
        assertEquals(
                0,
                p1ToP3.dotProduct(normal),
                DELTA,
                "Normal is not perpendicular to vector p1->p3");

        // TC03: Ensure the normal is a unit vector
        assertEquals(
                1,
                normal.length(),
                DELTA,
                "Normal is not unit length");

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

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        Plane plane = new Plane(new Point(0, 0, 2), new Vector(0, 0, 1));

        // TC01: Ray intersects the plane
        Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(2, 2, 2));
        var result1 = plane.findIntersections(ray1);

        assertNotNull(
                result1,
                "Expected intersection point");
        assertEquals(
                1,
                result1.size(),
                "Wrong number of points");

        // TC02: Ray does not intersect the plane
        Ray ray2 = new Ray(new Point(2, 2, 3), new Vector(2, 2, 2));
        assertNull(
                plane.findIntersections(ray2),
                "Ray away from plane");

        // =============== Boundary Values Tests ==================

        // **** Group 1: Rays that are parallel to the plane
        // TC11: Ray is parallel and included in the plane
        Ray ray3 = new Ray(new Point(1, 1, 2), new Vector(1, 0, 0));
        assertNull(
                plane.findIntersections(ray3),
                "Ray lies in the plane");

        // TC12: Ray is parallel and not included in the plane
        Ray ray4 = new Ray(new Point(0, 0, 4), new Vector(1, 0, 0));
        assertNull(
                plane.findIntersections(ray4),
                "Ray parallel but not in plane");

        // **** Group 2: Rays that are orthogonal to the plane
        // TC13: Ray is orthogonal to the plane and starts before
        Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 2));
        var result5 = plane.findIntersections(ray5);
        assertNotNull(
                result5,
                "Expected one intersection point");
        assertEquals(
                1,
                result5.size(),
                "Wrong number of points");

        // TC14: Ray is orthogonal and starts in the plane
        Ray ray6 = new Ray(new Point(1, 1, 2), new Vector(0, 0, 1));
        assertNull(
                plane.findIntersections(ray6),
                "Starts in plane going out");

        // TC15: Ray is orthogonal and starts after the plane
        Ray ray7 = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        assertNull(
                plane.findIntersections(ray7),
                "Starts after the plane");

        // **** Group 3: Special cases
        // TC16: Ray starts in the plane but is not orthogonal or parallel
        Ray ray8 = new Ray(new Point(1, 1, 2), new Vector(1, 1, 1));
        assertNull(
                plane.findIntersections(ray8),
                "Starts in the plane");

        // TC17: Ray starts at reference point of the plane
        Ray ray9 = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        assertNull(
                plane.findIntersections(ray9),
                "Starts at the reference point");
    }
}