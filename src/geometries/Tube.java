package geometries;

import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import static primitives.Util.isZero;

/**
 * The Tube class represents an infinite cylindrical tube defined by an axis and a radius.
 * It extends RadialGeometry and provides functionality for getting the normal vector at a point on the tube's surface.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Tube extends RadialGeometry {
    protected final Ray axis;
    protected static final double DELTA = 0.000001;
    /**
     * Constructor for Tube class. Initializes the axis and radius of the tube.
     *
     * @param axis   The axis of the tube (represented by a Ray).
     * @param radius The radius of the tube.
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * This method is an implementation of the getNormal method from Geometry interface.
     * It returns the normal vector to the surface of the tube at a specific point.
     * In this case, it is an abstract method, so we return null for now.
     *
     * @param point The point on the surface of the tube.
     * @return The normal vector at the point (null for now).
     */
    @Override
    public Vector getNormal(Point point) {

        // Compute the vector from the point on the tube to the head of the axis (Ray origin)
        Vector u = point.subtract(axis.getHead());
        // Project the vector onto the axis and subtract it from the original vector
        double t = axis.getDirection().dotProduct(u); // Scalar projection of v on the axis direction
        if (isZero(t))
            return u.normalize();
        Point o = axis.getHead().add(axis.getDirection().scale(t));
        return point.subtract(o).normalize(); // Subtract the projection to get the normal and normalize it
    }
}
