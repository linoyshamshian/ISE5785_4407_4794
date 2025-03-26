package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testSubtract() {
        Point p1 = new Point(3, 4, 5);
        Point p2 = new Point(1, 1, 1);

        // EP: Subtracting two different points should return the correct vector
        assertEquals(new Vector(2, 3, 4), p1.subtract(p2), "Subtracting two points failed");

        // BVA: Subtracting a point from itself should throw an exception (zero vector is not allowed)
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "Subtracting a point from itself should throw exception");
    }

    @Test
    void testAdd() {
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(2, 3, 4);

        // EP: Adding a vector to a point should return a new point
        assertEquals(new Point(3, 5, 7), p.add(v), "Adding a vector to a point failed");
    }

    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 8);

        // EP: Calculating the squared distance between two different points
        assertEquals(50, p1.distanceSquared(p2), "Distance squared calculation is incorrect");

        // BVA: Squared distance from a point to itself should be 0
        assertEquals(0, p1.distanceSquared(p1), "Distance squared from a point to itself should be 0");
    }

    @Test
    void testDistance() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(0, 3, 4);

        // EP: Calculating the distance between two different points
        assertEquals(5, p1.distance(p2), "Distance calculation is incorrect");

        // BVA: Distance from a point to itself should be 0
        assertEquals(0, p1.distance(p1), "Distance from a point to itself should be 0");
    }
}