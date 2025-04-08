package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}