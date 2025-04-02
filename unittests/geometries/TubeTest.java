package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;

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

}