package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    /** A point used in some tests */
    private final Point p001 = new Point(0, 0, 1);
    /** A point used in some tests */
    private final Point p100 = new Point(1, 0, 0);
    /** A vector used in some tests */
    private final Vector v001 = new Vector(0, 0, 1);

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(p01, v110)),
                "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(
                result1,
                "Can't be empty list");
        assertEquals(
                2,
                result1.size(),
                "Wrong number of points");
        assertEquals(
                exp,
                result1,
                "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        final Point gp3 = new Point(1.86602540378444, 0.5, 0);
        final var result2 = sphere.findIntersections(new Ray(new Point(0.8, 0.6, 0), v310));
        assertNotNull(
                result2,
                "Should return one intersection");
        assertEquals(
                1,
                result2.size(),
                "Wrong number of points");
        assertEquals(
                gp2,
                result2.get(0),
                "Wrong intersection point");
        // TC04: Ray starts after the sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(new Point(3, 1, 0), v310)),
                "Ray starts after sphere");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        final Point gp4 = new Point(1.8, 0.56, -0.2);
        final var result3 = sphere.findIntersections(new Ray(new Point(0.02359,-0.04563,-0.2), v310));
        assertNotNull(
                result3,
                "Should return one intersection");
        assertEquals(
                1,
                result3.size(),
                "Wrong number of points");
        assertEquals(
                gp4,
                result3.get(0),
                "Wrong intersection point");
        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(
                sphere.findIntersections(new Ray(gp4, v310)),
                "Ray starts at sphere and goes outside");
        // **** Group 2: Ray's line goes through the center
        Vector v0minus20=new Vector(0 ,-2, 0);
        Point gp1minus10=new Point(1, -1, 0);
        // TC21: Ray starts before the sphere (2 points)
        final var exp2 = List.of(gp1minus10, new Point(1,1,0));
        final var result4 = sphere.findIntersections(new Ray(new Point(1, 2, 0), v0minus20));
        assertNotNull(
                result4,
                "Should return two intersections");
        assertEquals(
                2,
                result4.size(),
                "Wrong number of points");
        assertEquals(
                exp2,
                result4,
                "Ray crosses sphere");
        // TC22: Ray starts at sphere and goes inside (1 point)
        final var result5 = sphere.findIntersections(new Ray(new Point(1, 1, 0), v0minus20));
        assertNotNull(
                result5,
                "Should return one intersection");
        assertEquals(
                1,
                result5.size(),
                "Wrong number of points");
        assertEquals(
                gp1minus10,
                result5.get(0),
                "Wrong intersection point");
        // TC23: Ray starts inside (after the center) (1 point)
        final var result6 = sphere.findIntersections(new Ray(new Point(1, -0.5, 0), v0minus20));
        assertNotNull(
                result6,
                "Should return one intersection");
        assertEquals(
                1,
                result6.size(),
                "Wrong number of points");
        assertEquals(
                gp1minus10,
                result6.get(0),
                "Wrong intersection point");
        // TC24: Ray starts at the center (1 point)
        final var result7 = sphere.findIntersections(new Ray(new Point(1, 0, 0),v0minus20));
        assertNotNull(
                result7,
                "Should return one intersection");
        assertEquals(
                1,
                result7.size(),
                "Wrong number of points");
        assertEquals(
                gp1minus10,
                result7.get(0),
                "Wrong intersection point");
        // TC25: Ray starts at sphere and goes outside (0 points)
        assertNull(
                sphere.findIntersections(new Ray(gp1minus10,v0minus20)),
                "Ray starts at sphere and goes outside");
        // TC26: Ray starts after sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(new Point(1, -2, 0), v0minus20)),
                "Ray starts after sphere");
        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        Vector v200=new Vector(2,0,0);
        Point gp001=new Point(0, 0, 1);
        Point gp101=new Point(1, 0, 1);
        Point gp200=new Point(2, 0, 0);
        // TC31: Ray starts before the tangent point
        assertNull(
                sphere.findIntersections(new Ray(gp001,v200)),
                "Ray starts before the tangent point");
        // TC32: Ray starts at the tangent point
        assertNull(
                sphere.findIntersections(new Ray(gp101,v200)),
                "Ray starts at the tangent point");
        // TC33: Ray starts after the tangent point
        assertNull(
                sphere.findIntersections(new Ray(gp200,v200)),
                "Ray starts after the tangent point");
        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        assertNull(
                sphere.findIntersections(new Ray(new Point(1,0,2),v200)),
                "Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line");
        // TC42: Ray's starts inside, ray is orthogonal to ray start to sphere's center line
        final var result8 = sphere.findIntersections(new Ray(new Point(1, 0, 0.5),v200));
        assertNotNull(
                result8,
                "Should return one intersection");
        assertEquals(
                1,
                result8.size(),
                "Wrong number of points");
        assertEquals(
                new Point(1.867,0,0.5),
                result8.get(0),
                "Wrong intersection point");
    }
}