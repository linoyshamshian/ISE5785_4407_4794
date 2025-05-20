package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * This class represents a Cylinder, a 3D geometric object defined by its radius and height.
 * The class extends Tube, inheriting the radius and axis properties, and adds a height property.
 *
 * @author Chen Babay & Linoy Shamshian
 */


public class Cylinder extends Tube {
    private final double height;


    /**
     * Constructor for Cylinder class. Initializes the radius, axis, and height of the cylinder.
     *
     * @param axis   The axis of the cylinder (represented by a Ray).
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);  // Call the constructor of the superclass Tube
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        this.height = height;
    }

    /**
     * This method is an implementation of the getNormal method from Geometry interface.
     * It returns the normal vector to the surface of the cylinder at a specific point.
     * In this case, it is an abstract method, so we return null for now.
     *
     * @param point The point on the surface of the cylinder.
     * @return The normal vector at the point (null for now).
     */
    @Override
    public Vector getNormal(Point point) {
        // The cylinder's axis
        Point p0 = axis.getHead(); // The base center (start of the axis)
        Vector v = axis.getDirection(); // The cylinder's direction (normalized)
        // If the point is exactly at the bottom base center
        if (point.equals(p0)) {
            return v.scale(-1);
        }
        //  If the point is exactly at the top base center
        if (point.equals(p0.add(v.scale(height)))) {
            return v;
        }
        // Compute the projection of the point onto the cylinder's axis
        double t = v.dotProduct(point.subtract(p0));
        // If the point is on the bottom base (including edge cases at the junction with the mantle)
        if (isZero(t)) {
            return v.scale(-1);
        }

        // If the point is on the top base (including edge cases at the junction with the mantle)
        if (t == height) {
            return v;
        }
        // If the point is on the cylindrical mantle
        return super.getNormal(point);
    }

    /**
     * This method implements the {@code calculateIntersectionsHelper} method defined in the {@code Intersectable} interface.
     * It calculates the intersection points (if any) between a given ray and the surface of the finite cylinder
     * (including its two bases).
     *
     * @param ray The ray for which intersection points with the cylinder are to be found.
     * @return A list of intersection points, or {@code null} if there are none.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = super.calculateIntersectionsHelper(ray); // Side intersections from Tube
        List<Intersection> results = new ArrayList<>();

        Point p0 = axis.getHead();                    // Bottom base center of the cylinder
        Vector vAxis = axis.getDirection(); // Cylinder axis direction

        // Filter side intersections to keep only those within cylinder height
        if (intersections != null) {
            for (Intersection intersection : intersections) {
                Point p = intersection.point;
                double t = vAxis.dotProduct(p.subtract(p0));
                if (alignZero(t) >= 0 && alignZero(t) <= height) {
                    results.add(intersection);
                }
            }
        }

        // Intersect with bottom base
        List<Intersection> bottomHits = intersectBase(p0, vAxis.scale(-1), ray);
        if (bottomHits != null) results.addAll(bottomHits);

        // Intersect with top base
        Point topCenter = p0.add(vAxis.scale(height)); // Top base center
        List<Intersection> topHits = intersectBase(topCenter, vAxis, ray);
        if (topHits != null) results.addAll(topHits);

        if (results.isEmpty()) return null;

        // Sort the intersection points by their distance from the ray origin
        results.sort(Comparator.comparingDouble(p -> ray.getDirection().dotProduct(p.point.subtract(ray.getHead()))));

        return results;
    }

    /**
     * Finds the intersections between a ray and a circular base of a cylinder or a cone.
     * <p>
     * The method first checks intersection with the plane in which the base lies.
     * Then, it filters out any intersection points that fall outside the circular base
     * by verifying the distance from the center of the base is less than or equal to the radius.
     *
     * @param center The center point of the base circle
     * @param normal The normal vector of the base plane
     * @param ray    The ray to intersect with the base
     * @return A list of intersections with the base (null if none are within the radius)
     */
    private List<Intersection> intersectBase(Point center, Vector normal, Ray ray) {
        Plane basePlane = new Plane(center, normal);
        List<Intersection> baseIntersections = basePlane.calculateIntersectionsHelper(ray);
        if (baseIntersections == null) return null;

        List<Intersection> result = new ArrayList<>();
        for (Intersection intersection : baseIntersections) {
            Point p = intersection.point;
            if (p.equals(center)) {
                result.add(new Intersection(this, p)); // Center point – avoid zero vector creation
            } else {
                Vector v = p.subtract(center);
                if (alignZero(v.lengthSquared() - radius * radius) <= 0) {
                    result.add(new Intersection(this, p));
                }
            }
        }
        return result.isEmpty() ? null : result;
    }


}
