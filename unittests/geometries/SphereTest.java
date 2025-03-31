package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SphereTest {
    /**
     * Test method for
     * {@link
     * geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Sphere sphere = new Sphere(new Point(0, 0, 0), 2);
        // TC01 :Test that the normal is correctly calculated for point (2, 0, 0)
        assertEquals(
                new Vector(1, 0, 0),
                sphere.getNormal(new Point(2, 0, 0)),
                "Wrong normal vector");
    }

    /**
     * Test method for
     * {@link geometries.Sphere#Sphere(primitives.Point, double)}.
     */
    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        // Testing if IllegalArgumentException is thrown when the center is null
        assertThrows(IllegalArgumentException.class, () -> {
            new Sphere(null, 5);
        });
    }

}