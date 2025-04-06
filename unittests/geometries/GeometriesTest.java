package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void testFindIntersections() {
        // Setup geometries: plane, sphere and triangle
        Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        Sphere sphere = new Sphere(new Point(0, 0, 3), 1);
        Triangle triangle = new Triangle(
                new Point(0, 1, 0),
                new Point(1, -1, 0),
                new Point(-1, -1, 0)
        );

        // BVA: Empty collection of geometries
        Geometries emptyGeometries = new Geometries();
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertNull(
                emptyGeometries.findIntersections(ray),
                "Expected null for empty collection"
        );

        // BVA: No shapes intersected
        Geometries geometries1 = new Geometries(plane, sphere, triangle);
        Ray rayNoHit = new Ray(new Point(0, 0, -1), new Vector(0, -5, -1));
        assertNull(
                geometries1.findIntersections(rayNoHit),
                "Expected null when no intersections found"
        );

        // BVA: Only one shape intersects
        Ray rayOneHit = new Ray(new Point(0, 0, 3), new Vector(0, 0, 1)); // Intersects the sphere only
        var resultOneHit = geometries1.findIntersections(rayOneHit);
        assertNotNull(
                resultOneHit,
                "Expected one intersection");
        assertEquals(
                1,
                resultOneHit.size(),
                "Expected only one intersection point");

        // EP: Some but not all shapes intersect
        Ray raySomeHit = new Ray(new Point(0, 0, 1.5), new Vector(0, 0, -1)); // Intersects triangle and plane
        var resultSomeHit = geometries1.findIntersections(raySomeHit);
        assertNotNull(
                resultSomeHit,
                "Expected intersections with some shapes");
        assertEquals(
                2,
                resultSomeHit.size(),
                "Expected two intersection points");

        // BVA: All shapes intersect
        Ray rayAllHit = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)); // Goes through triangle, plane and sphere
        Geometries geometriesAll = new Geometries(plane, triangle, sphere);
        var resultAllHit = geometriesAll.findIntersections(rayAllHit);
        assertNotNull(
                resultAllHit,
                "Expected intersections with all shapes");
        assertEquals(
                4,
                resultAllHit.size(),
                "Expected four intersection points (triangle + plane + 2 from sphere)");
    }
}