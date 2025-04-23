package primitives;

/**
 * The Vector class represents a mathematical vector in 3D space.
 * A vector is defined by its x, y, and z components, and provides several methods to perform vector operations.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Vector extends Point {
    public static final Vector AXIS_X = new Vector(1, 0, 0);
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * Constructor receiving three double values
     *
     * @param x first coordinate
     * @param y second coordinate
     * @param z third coordinate
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }

    /**
     * Constructor receiving a Double3 object
     *
     * @param xyz coordinates as a Double3 object
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }

    /**
     * Adds another vector to this vector
     *
     * @param v vector to add
     * @return new vector that is the sum of this vector and v
     */
    public Vector add(Vector v) {
        return new Vector(xyz.add(v.xyz));
    }

    /**
     * Scales this vector by a scalar value
     *
     * @param scalar the scalar to multiply by
     * @return new scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    /**
     * Computes the dot product of this vector with another vector
     *
     * @param v vector to compute dot product with
     * @return dot product result
     */
    public double dotProduct(Vector v) {
        return xyz.d1() * v.xyz.d1() + xyz.d2() * v.xyz.d2() + xyz.d3() * v.xyz.d3();
    }

    /**
     * Computes the cross product of this vector with another vector
     *
     * @param v vector to compute cross product with
     * @return new vector that is the cross product
     */
    public Vector crossProduct(Vector v) {
        return new Vector(
                xyz.d2() * v.xyz.d3() - xyz.d3() * v.xyz.d2(),
                xyz.d3() * v.xyz.d1() - xyz.d1() * v.xyz.d3(),
                xyz.d1() * v.xyz.d2() - xyz.d2() * v.xyz.d1()
        );
    }

    /**
     * Computes the squared length of the vector
     *
     * @return squared length of the vector
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    /**
     * Computes the length of the vector
     *
     * @return length of the vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this vector
     *
     * @return new normalized vector
     */
    public Vector normalize() {
        return new Vector(xyz.reduce(length()));
    }

    /**
     * Checks equality of this vector with another object
     *
     * @param obj the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector) && super.equals(obj);
    }

    /**
     * Returns a string representation of the vector
     *
     * @return string describing the vector
     */
    @Override
    public String toString() {
        return "->" + super.toString();
    }
}
