package primitives;

/**
 * The Point class represents a point in 3D space.
 * The class provides methods for performing operations like addition, subtraction, distance calculation, and more.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Point {
    public static final Point ZERO = new Point(Double3.ZERO);
    final Double3 xyz;

    /**
     * Creates a point with the given x, y, and z coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param z The z-coordinate of the point.
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Creates a point from a Double3 object containing the coordinates.
     *
     * @param xyz A Double3 object that contains the coordinates of the point.
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Subtracts another point from this point and returns the result as a vector.
     *
     * @param p1 The point to subtract.
     * @return A Vector representing the difference between the points.
     */
    public Vector subtract(Point p1) {
        return new Vector(xyz.subtract(p1.xyz));
    }

    /**
     * Adds a vector to this point and returns the resulting point.
     *
     * @param v1 The vector to add to the point.
     * @return A new Point representing the sum of the point and the vector.
     */
    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param p1 The point to calculate the distance to.
     * @return The squared distance between the two points.
     */
    public double distanceSquared(Point p1) {
        Double3 sub = xyz.subtract(p1.xyz);
        return sub.d1() * sub.d1() + sub.d2() * sub.d2() + sub.d3() * sub.d3();
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param p1 The point to calculate the distance to.
     * @return The distance between the two points.
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    /**
     * Compares this point to another object for equality.
     * Two points are considered equal if their coordinates are identical.
     *
     * @param obj The object to compare to.
     * @return True if the object is a Point and has the same coordinates, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Point) && xyz.equals(((Point) obj).xyz);
    }

    /**
     * Returns a string representation of this point.
     *
     * @return A string representing the point in the format "Point(x, y, z)".
     */
    @Override
    public String toString() {
        return "Point(" + xyz.d1() + ", " + xyz.d2() + ", " + xyz.d3() + ")";
    }
}
