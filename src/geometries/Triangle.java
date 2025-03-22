package geometries;

import primitives.Point;

/**
 * Class representing a triangle in 3D space.
 * Inherits from Polygon.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Triangle extends Polygon {

    /**
     * Constructor for Triangle, which passes its three points to the Polygon constructor.
     *
     * @param p0 The first vertex of the triangle.
     * @param p1 The second vertex of the triangle.
     * @param p2 The third vertex of the triangle.
     */
    public Triangle(Point p0, Point p1, Point p2) {
        super(p0, p1, p2); // Call Polygon constructor
    }
}
