package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

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
        assertTrue(
                validNormal1,
                "Wrong normal for edge case (bottom base and surface)");

        // TC12: Point exactly on the edge between base and curved surface (top base)
        Point p5 = new Point(2, 0, 5);
        Vector normal2 = cylinder.getNormal(p5);
        boolean validNormal2 = normal2.equals(
                new Vector(1, 0, 0)) || normal2.equals(new Vector(0, 0, 1));
        assertTrue(
                validNormal2,
                "Wrong normal for edge case (top base and surface)");

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

    @Test
    void testFindIntersections() {
        // Cylinder on axis (0,0,z) with radius 1 and height 5
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(1, 1, 0), new Vector(0, 0, 1)),
                1,
                5);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects side and enters/exits within height range
        Ray ray1 = new Ray(new Point(3, 1, 2.5), new Vector(-1, 0, 0));
        List<Point> intersections1 = cylinder.findIntersections(ray1);
        assertNotNull(
                intersections1,
                "Expected 2 intersections");
        assertEquals(
                2,
                intersections1.size(),
                "Expected 2 intersection points");

        // TC02: Ray intersects both bases (starts below and goes through)
        Ray ray2 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
        List<Point> intersections2 = cylinder.findIntersections(ray2);
        assertNotNull(
                intersections2,
                "Expected 2 intersections with bases");
        assertEquals(
                2,
                intersections2.size(),
                "Expected 2 intersection points");


        // TC03: Ray intersects top base only (starts above and goes down)
        Ray ray3 = new Ray(new Point(1, 1, 4), new Vector(0, 0, 1));
        List<Point> intersections3 = cylinder.findIntersections(ray3);
        assertNotNull(
                intersections3,
                "Expected 1 intersection on top base");
        assertEquals(
                1,
                intersections3.size(),
                "Expected 1 intersection");
        assertEquals(
                new Point(1, 1, 5),
                intersections3.get(0),
                "Wrong point on top base");

        // =============== Boundary Values Tests ==================

        // TC11: Ray tangent to cylinder side – no intersection (grazes side)
        Ray ray4 = new Ray(new Point(0, 0, 2.5), new Vector(0, 1, 0));
        assertNull(
                cylinder.findIntersections(ray4),
                "Expected no intersection (tangent)");

        // TC12: Ray starts on side surface and goes outward – should be no intersection
        Ray ray5 = new Ray(new Point(2, 1, 2.5), new Vector(1, 0, 0));
        assertNull(
                cylinder.findIntersections(ray5),
                "Expected no intersection from surface outward");

        // TC13: Ray starts inside the cylinder and exits through side
        Ray ray6 = new Ray(new Point(1.5, 1, 2.5), new Vector(1, 0, 0));
        List<Point> intersections6 = cylinder.findIntersections(ray6);
        assertNotNull(
                intersections6,
                "Expected 1 intersection when exiting");
        assertEquals(
                1,
                intersections6.size(),
                "Expected 1 intersection");

        // TC14: Ray goes through cylinder axis (from left to right)
        Ray ray7 = new Ray(new Point(0, 1, 2.5), new Vector(1, 0, 0));
        List<Point> intersections7 = cylinder.findIntersections(ray7);
        assertNotNull(
                intersections7,
                "Expected 2 intersections through center");
        assertEquals(
                1,
                intersections7.size(),
                "Expected 2 intersection points");

        // TC15: Ray parallel to axis but outside cylinder – no intersection
        Ray ray8 = new Ray(new Point(3, 1, -1), new Vector(0, 0, 1));
        assertNull(
                cylinder.findIntersections(ray8),
                "Expected no intersection – parallel and outside");

        // TC16: Ray inside radius, parallel to axis – should intersect both bases
        Ray ray9 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
        List<Point> intersections9 = cylinder.findIntersections(ray9);
        assertNotNull(
                intersections9,
                "Expected 2 intersections with bases");
        assertEquals(
                2,
                intersections9.size(),
                "Expected 2 intersection points");

        // TC17: Ray starts exactly on top base and goes up – should not intersect
        Ray ray10 = new Ray(new Point(1, 1, 5), new Vector(0, 0, 1));
        assertNull(
                cylinder.findIntersections(ray10),
                "Expected no intersection – going up from top base");
    }

}