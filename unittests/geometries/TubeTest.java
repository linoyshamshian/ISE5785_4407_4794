package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {
    /**
     * Test method for
     * {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(axis, 2);
        // ============ Equivalence Partitions Tests ==============
        //TC01: A point on the tube's surface
        Point p1 = new Point(2, 0, 3);
        assertEquals(
                new Vector(1, 0, 0),
                tube.getNormal(p1),
                "Wrong normal for point on tube surface");

        // =============== Boundary Values Tests ==================
        //  TC11: A point directly aligned with the tube axis origin
        Point p2 = new Point(2, 0, 0);
        assertEquals(
                new Vector(1, 0, 0),
                tube.getNormal(p2),
                "Wrong normal for boundary case");
    }

    /**
     * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Tube tube = new Tube(new Ray(new Point(1, 1, 1), new Vector(0, 0, 1)), 1);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects tube in two points
        Ray ray1 = new Ray(new Point(3, 1, 2), new Vector(-1, 0, 0));
        List<Point> result1 = tube.findIntersections(ray1);
        assertNotNull(
                result1,
                "Expected two intersection points");
        assertEquals(
                2,
                result1.size(),
                "Wrong number of intersection points");

        // TC02: Ray is outside and doesn't intersect
        Ray ray2 = new Ray(new Point(5, 5, 5), new Vector(1, 0, 0));
        assertNull(
                tube.findIntersections(ray2),
                "Ray outside should not intersect the tube");

        // =============== Boundary Values Tests ==================

        // TC11: Ray is tangent to the tube – no intersection
        Ray ray3 = new Ray(new Point(2, 2, 1), new Vector(0, 0, 1));
        assertNull(
                tube.findIntersections(ray3),
                "Tangent ray should not intersect the tube");

        // TC12: Ray starts exactly on the tube surface and goes outward – should return null
        Ray ray4 = new Ray(new Point(2, 1, 1), new Vector(1, 0, 0));
        assertNull(
                tube.findIntersections(ray4),
                "Ray starting on surface should not count as intersection");

        // TC13: One point in front, one at t=0 (start on surface) – only the one in front should be counted
        Ray ray5 = new Ray(new Point(2, 1, 1), new Vector(-1, 0, 0)); // ראש הקרן בדיוק על המעטפת
        List<Point> result5 = tube.findIntersections(ray5);
        assertNotNull(
                result5,
                "Expected only one intersection point");
        assertEquals(
                1,
                result5.size(),
                "Only one point should count (excluding t=0)");

        // TC14: Both intersections behind ray start (negative t values) – no intersection
        Ray ray6 = new Ray(new Point(0, 1, 1), new Vector(-1, 0, 0));
        assertNull(
                tube.findIntersections(ray6),
                "Both intersections behind start – should return null");

        // TC15: One intersection in front, one behind – should return only the one in front
        Ray ray7 = new Ray(new Point(1.5, 1, 1), new Vector(-1, 0, 0));
        List<Point> result7 = tube.findIntersections(ray7);
        assertNotNull(
                result7,
                "Expected one intersection point");
        assertEquals(
                1,
                result7.size(),
                "Only one intersection in front of ray");
    }

}