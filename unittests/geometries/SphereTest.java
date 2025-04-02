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
        Vector v = new Vector(1, 0, 0);
        assertEquals(
                v,
                sphere.getNormal(new Point(2, 0, 0)),
                "Wrong normal vector");
        assertEquals(
                1,
                v.length(),
                "Normal vector length should be 1");
    }


}