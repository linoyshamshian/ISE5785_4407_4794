package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {
    /**
     * Test method for
     * {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // Create a cylinder with known parameters
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Cylinder cylinder = new Cylinder(axis, 2, 5);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Point on the curved surface
        Point p1 = new Point(2, 0, 2);
        assertEquals(
                new Vector(1, 0, 0),
                cylinder.getNormal(p1),
                "Wrong normal for point on curved surface");

        // TC02: Point on the bottom base
        Point p2 = new Point(1, 1, 0);
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(p2),
                "Wrong normal for point on bottom base");

        // TC03: Point on the top base
        Point p3 = new Point(-1, -1, 5);
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(p3),
                "Wrong normal for point on top base");

        // =============== Boundary Values Tests ==================
        // TC11: Point exactly on the edge between base and curved surface (bottom base)
        Point p4 = new Point(2, 0, 0);
        Vector normal1 = cylinder.getNormal(p4);
        boolean validNormal1 = normal1.equals(
                new Vector(1, 0, 0)) || normal1.equals(new Vector(0, 0, -1));
        assertTrue(validNormal1, "Wrong normal for edge case (bottom base and surface)");

        // TC12: Point exactly on the edge between base and curved surface (top base)
        Point p5 = new Point(2, 0, 5);
        Vector normal2 = cylinder.getNormal(p5);
        boolean validNormal2 = normal2.equals(
                new Vector(1, 0, 0)) || normal2.equals(new Vector(0, 0, 1));
        assertTrue(validNormal2, "Wrong normal for edge case (top base and surface)");

        // TC13: Point at the exact center of the bottom base
        Point p6 = new Point(0, 0, 0);
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(p6),
                "Wrong normal for center of bottom base");

        // TC14: Point at the exact center of the top base
        Point p7 = new Point(0, 0, 5);
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(p7),
                "Wrong normal for center of top base");
    }


    /**
     * Test method for
     * {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     */
    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        // Testing if IllegalArgumentException is thrown when height is less than or equal to zero
        assertThrows(IllegalArgumentException.class, () -> {
            new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 2, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 2, -1);
        });
    }
}