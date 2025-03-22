package geometries;

/**
 * Abstract class representing radial geometries (such as spheres and cylinders).
 * Extends the Geometry class.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public abstract class RadialGeometry extends Geometry {
    protected final double radius; // The radius of the geometry

    /**
     * Constructor that initializes the radius.
     *
     * @param radius The radius of the geometric shape.
     * @throws IllegalArgumentException if the radius is negative or zero.
     */
    public RadialGeometry(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive and greater than zero.");
        }
        this.radius = radius;
    }
}
