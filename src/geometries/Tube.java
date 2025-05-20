package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
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

    /**
     * This method implements the {@code calculateIntersectionsHelper} method defined in the {@code Intersectable} interface.
     * It calculates the intersection points (if any) between a given ray and the surface of the infinite tube.
     * If there are no intersections or if the ray only touches the tube (tangent), the method returns {@code null}.
     * Intersection points where the ray origin lies exactly on the surface are not considered valid intersections.
     *
     * @param ray The ray for which intersection points with the tube are to be found.
     * @return A list of intersection points, or {@code null} if there are none.
     */

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector va = axis.getDirection(); // Direction vector of the tube's axis
        Point pa = axis.getHead(); // A point on the tube's axis
        Vector vr = ray.getDirection(); // Direction vector of the ray
        Point pr = ray.getHead(); // Origin of the ray

        // Calculate the vector (pr - pa)
        if (pr.equals(pa))
            return null;
        Vector deltaP = pr.subtract(pa);

        // Calculate the coefficients A, B, and C of the quadratic equation At^2 + Bt + C = 0
        double A = 1 - Math.pow(vr.dotProduct(va), 2);

        double B = 2 * (vr.dotProduct(deltaP) - vr.dotProduct(va) * va.dotProduct(deltaP));

        double C = deltaP.lengthSquared() - Math.pow(deltaP.dotProduct(va), 2) - radius * radius;

        // Solve the quadratic equation
        double discriminant = B * B - 4 * A * C;

        if (discriminant < 0 || isZero(discriminant)) {
            return null; // No real intersections (including tangent)
        }

        double t1 = (-B + Math.sqrt(discriminant)) / (2 * A);
        double t2 = (-B - Math.sqrt(discriminant)) / (2 * A);

        Point p1 = null, p2 = null;

        if (alignZero(t1) > 0) {
            p1 = pr.add(vr.scale(t1));
        }
        if (alignZero(t2) > 0) {
            p2 = pr.add(vr.scale(t2));
        }

        if (p1 != null && p2 != null) {
            // Ensure the intersections are ordered by their distance from the ray origin
            if (p2.subtract(pr).lengthSquared() < p1.subtract(pr).lengthSquared()) {
                return List.of(new Intersection(this, p2), new Intersection(this, p1));
            }
            return List.of(new Intersection(this, p1), new Intersection(this, p2));
        }

        if (p1 != null) {
            return List.of(new Intersection(this, p1));
        }

        if (p2 != null) {
            return List.of(new Intersection(this, p2));
        }

        return null;
    }
}