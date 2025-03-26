package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class VectorTest {

    @Test
    void testAdd() {
        Vector v1 = new Vector(1, -2, 3);
        Vector v2 = new Vector(-1, 2, -3);

        // EP: Test regular vector addition
        assertEquals(new Vector(2, -4, 6), v1.add(v1), "Vector addition failed");

        // BVA: Adding a vector to its inverse should throw an exception (zero vector is not allowed)
        assertThrows(IllegalArgumentException.class, () -> v1.add(v2), "Adding inverse vectors should throw an exception");

    }

    @Test
    void testScale() {
        Vector v = new Vector(1, -2, 3);

        // EP: Scaling by a positive scalar
        assertEquals(new Vector(2, -4, 6), v.scale(2), "Scaling by positive number failed");

        // EP: Scaling by a negative scalar
        assertEquals(new Vector(-2, 4, -6), v.scale(-2), "Scaling by negative number failed");

        // BVA: Scaling by zero should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v.scale(0), "Scaling by zero should throw exception");
    }

    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // EP: Dot product with parallel vectors
        assertEquals(-28, v1.dotProduct(v2), "Dot product with parallel vectors failed");

        // EP: Dot product with perpendicular vectors should be zero
        assertEquals(0, v1.dotProduct(v3), "Dot product with perpendicular vectors failed");
    }

    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // BVA: Cross product of parallel vectors should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2), "Cross product of parallel vectors should throw exception");

        // EP: Cross product with non-parallel vectors
        Vector v3 = new Vector(0, 3, -2);
        Vector result = v1.crossProduct(v3);
        assertEquals(new Vector(-13, 2, 3), result, "Cross product failed");

    }

    @Test
    void testLengthSquared() {
        Vector v1=new Vector(1,2,3);
        assertEquals(14,v1.lengthSquared());
    }

    @Test
    void testLength() {
        Vector v = new Vector(0, 3, 4);
        assertEquals(5, v.length(), "Length calculation is incorrect");
    }

    @Test
    void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector normalized = v.normalize();

        // EP: Check length of normalized vector
        assertEquals(0, Util.alignZero(normalized.length() - 1), "Normalized vector should have length 1");
        // EP: Check direction remains the same
        assertEquals(new Vector(0, 0.6, 0.8), normalized, "Normalization failed");
    }
    @Test
    void testSubtract() {
        // EP: Subtracting two regular vectors
        Vector v1 = new Vector(3, 5, 7);
        Vector v2 = new Vector(1, 2, 3);
        assertEquals(new Vector(2, 3, 4), v1.subtract(v2), "Vector subtraction failed");

        // BVA: Subtracting a vector from itself should throw an exception (since it results in the zero vector)
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1), "Subtracting a vector from itself should throw an exception");

        // EP: Subtracting a negative vector (ensuring negative values are handled correctly)
        Vector v3 = new Vector(-1, -1, -1);
        assertEquals(new Vector(4, 6, 8), v1.subtract(v3), "Vector subtraction with negative result failed");
    }

}