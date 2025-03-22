package geometries;

import primitives.Vector;
import primitives.Point;

/**
 * Abstract class representing a geometric shape.
 * This class provides a method to get the normal vector of the geometry at a specific point.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public abstract class Geometry {
    /**
     * Gets the normal vector of the geometry at a specific point.
     *
     * @param point The point at which to calculate the normal.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point point);
}
