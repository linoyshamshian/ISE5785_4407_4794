package geometries;

import primitives.Ray;
import primitives.Vector;
import primitives.Point;

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
        Point topCenter = p0.add(v.scale(height));
        if (point.equals(topCenter)) {
            return v;
        }
        // Compute the projection of the point onto the cylinder's axis
        double t = v.dotProduct(point.subtract(p0));
        // If the point is on the bottom base (including edge cases at the junction with the mantle)
        if (t <= 0) {
            return v.scale(-1);
        }
        // If the point is on the top base (including edge cases at the junction with the mantle)
        if (t >= height) {
            return v;
        }
        // If the point is on the cylindrical mantle
        Point o = p0.add(v.scale(t)); // The center of the circular section where the point is located
        return point.subtract(o).normalize();
    }

}
