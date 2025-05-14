package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RayTest {

    /**
     * Test method for {@link Ray#getPoint(double)}.
     * This test verifies that the {@link Ray#getPoint(double)} method works correctly
     * for positive, negative, and zero distances.
     */
    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============

        // EP: Create a ray with a specific origin and direction
        Point head = new Point(1, 2, 3);
        Vector direction = new Vector(0, 0, 1);  // z-axis direction
        Ray ray = new Ray(head, direction);

        // ============ Test Cases ==============

        // TC01: Test for positive distance (2 units in the z-direction)
        Point expectedPositive = new Point(1, 2, 5);  // Moving 2 units in the z-direction
        Point actualPositive = ray.getPoint(2);
        assertEquals(expectedPositive, actualPositive, "The point should be 2 units along the direction vector.");

        // TC02: Test for negative distance (-2 units in the z-direction)
        Point expectedNegative = new Point(1, 2, 1);  // Moving -2 units in the z-direction
        Point actualNegative = ray.getPoint(-2);
        assertEquals(expectedNegative, actualNegative, "The point should be -2 units along the direction vector.");

        // =============== Boundary Values Tests ==================
        // Test for zero distance (the origin point should be returned)
        Point actualZero = ray.getPoint(0);
        assertEquals(head, actualZero, "The point should be the origin when the distance is 0.");
    }

    /**
     * Test method for {@link Ray#findClosestPoint(List)}.
     * Checks closest point to the ray origin from a given list.
     */
    @Test
    void testFindClosestPoint() {
        Point p0 = new Point(1, 2, 3);
        Vector dir = new Vector(1, 0, 0);
        Ray ray = new Ray(p0, dir);

        // ============ Equivalence Partition (EP) ==============

        // TC01: Middle point is the closest
        Point p1 = new Point(6, 2, 3);  // distance = 5
        Point p2 = new Point(3, 2, 3);  // distance = 2 (expected)
        Point p3 = new Point(10, 2, 3); // distance = 9
        List<Point> pointsEP = List.of(p1, p2, p3);
        assertEquals(p2, ray.findClosestPoint(pointsEP), "Expected middle point to be closest");

        // ============ Boundary Value Tests (BVA) ==============

        // TC02: Null list
        assertNull(ray.findClosestPoint(null), "Expected null for null list");

        // TC03: First point is the closest
        Point p4 = new Point(2, 2, 3); // distance = 1 (expected)
        List<Point> pointsFirst = List.of(p4, p1, p3);
        assertEquals(p4, ray.findClosestPoint(pointsFirst), "Expected first point to be closest");

        // TC04: Last point is the closest
        Point p5 = new Point(1.5, 2, 3); // distance = 0.5 (expected)
        List<Point> pointsLast = List.of(p1, p3, p5);
        assertEquals(p5, ray.findClosestPoint(pointsLast), "Expected last point to be closest");
    }


}