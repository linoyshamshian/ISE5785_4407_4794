package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class VectorTest {


    /** Test method for {@link primitives.Vector#Vector(double, double, double)}. */
    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        // Trying to create a zero vector should throw an exception
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vector(0, 0, 0),
                "Constructor should throw an exception for a zero vector");
    }

    /** Test method for {@link primitives.Vector#add(primitives.Vector)}. */
    @Test
    void testAdd() {
        Vector v1 = new Vector(1, -2, 3);
        Vector v2 = new Vector(-1, 2, -3);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test regular vector addition
        assertEquals(new Vector(2, -4, 6), v1.add(v1), "Vector addition failed");
        // =============== Boundary Values Tests ==================
        // TC11: Adding a vector to its inverse should throw an exception (zero vector is not allowed)
        assertThrows(
                IllegalArgumentException.class,
                () -> v1.add(v2),
                "Adding inverse vectors should throw an exception");

    }

    /** Test method for {@link primitives.Vector#scale(double)}. */
    @Test
    void testScale() {
        Vector v = new Vector(1, -2, 3);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Scaling by a positive scalar
        assertEquals(new Vector(2, -4, 6), v.scale(2), "Scaling by positive number failed");
        // TC02: Scaling by a negative scalar
        assertEquals(new Vector(-2, 4, -6), v.scale(-2), "Scaling by negative number failed");
        // =============== Boundary Values Tests ==================
        // TC11: Scaling by zero should throw an exception
        assertThrows(
                IllegalArgumentException.class,
                () -> v.scale(0),
                "Scaling by zero should throw exception");
    }

    /** Test method for {@link primitives.Vector#scale(double)}. */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Dot product with parallel vectors
        assertEquals(-28, v1.dotProduct(v2), "Dot product with parallel vectors failed");
        // TC02: Dot product with perpendicular vectors should be zero
        assertEquals(0, v1.dotProduct(v3), "Dot product with perpendicular vectors failed");
    }

    /** Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}. */
    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        // =============== Boundary Values Tests ==================
        // TC11: Cross product of parallel vectors should throw an exception
        assertThrows(
                IllegalArgumentException.class,
                () -> v1.crossProduct(v2),
                "Cross product of parallel vectors should throw exception");
        // ============ Equivalence Partitions Tests ==============
        // TC01: Cross product with non-parallel vectors
        Vector v3 = new Vector(0, 3, -2);
        Vector result = v1.crossProduct(v3);
        assertEquals(new Vector(-13, 2, 3), result, "Cross product failed");

    }

    /** Test method for {@link primitives.Vector#lengthSquared()}. */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Checking squared length of a vector
        Vector v1=new Vector(1,2,3);
        assertEquals(14,v1.lengthSquared());
    }

    /** Test method for {@link primitives.Vector#length()}. */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Checking length of a vector
        Vector v = new Vector(0, 3, 4);
        assertEquals(5, v.length(), "Length calculation is incorrect");
    }

    /** Test method for {@link primitives.Vector#normalize()}. */
    @Test
    void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector normalized = v.normalize();
        // ============ Equivalence Partitions Tests ==============
        // TC01: Check length of normalized vector
        assertEquals(
                0,
                Util.alignZero(normalized.length() - 1),
                "Normalized vector should have length 1");
        // TC02: Check direction remains the same
        assertEquals(new Vector(0, 0.6, 0.8), normalized, "Normalization failed");
    }

    /** Test method for {@link primitives.Point#subtract(primitives.Point)}. */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtracting two regular vectors
        Vector v1 = new Vector(3, 5, 7);
        Vector v2 = new Vector(1, 2, 3);
        assertEquals(new Vector(2, 3, 4), v1.subtract(v2), "Vector subtraction failed");
        // TC02: Subtracting a negative vector (ensuring negative values are handled correctly)
        Vector v3 = new Vector(-1, -1, -1);
        assertEquals(
                new Vector(4, 6, 8),
                v1.subtract(v3),
                "Vector subtraction with negative result failed");
        // =============== Boundary Values Tests ==================
        // TC11: Subtracting a vector from itself should throw an exception (since it results in the zero vector)
        assertThrows(
                IllegalArgumentException.class,
                () -> v1.subtract(v1),
                "Subtracting a vector from itself should throw an exception");
    }

}