package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {

    /**
     * Test method for
     * {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        Point p1 = new Point(3, 4, 5);
        Point p2 = new Point(1, 1, 1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtracting two different points should return the correct vector
        assertEquals(
                new Vector(2, 3, 4),
                p1.subtract(p2),
                "Subtracting two points failed");
        // =============== Boundary Values Tests ==================
        // TC11: Subtracting a point from itself should throw an exception (zero vector is not allowed)
        assertThrows(
                IllegalArgumentException.class,
                () -> p1.subtract(p1),
                "Subtracting a point from itself should throw exception");
    }

    /**
     * Test method for
     * {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(2, 3, 4);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Adding a vector to a point should return a new point
        assertEquals(
                new Point(3, 5, 7),
                p.add(v),
                "Adding a vector to a point failed");
    }

    /**
     * Test method for
     * {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 8);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Calculating the squared distance between two different points
        assertEquals(
                50,
                p1.distanceSquared(p2),
                "Distance squared calculation is incorrect");
        // =============== Boundary Values Tests ==================
        // TC11: Squared distance from a point to itself should be 0
        assertEquals(
                0,
                p1.distanceSquared(p1),
                "Distance squared from a point to itself should be 0");
    }

    /**
     * Test method for
     * {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(0, 3, 4);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Calculating the distance between two different points
        assertEquals(
                5,
                p1.distance(p2),
                "Distance calculation is incorrect");
        // =============== Boundary Values Tests ==================
        // TC11: Distance from a point to itself should be 0
        assertEquals(
                0,
                p1.distance(p1),
                "Distance from a point to itself should be 0");
    }
}