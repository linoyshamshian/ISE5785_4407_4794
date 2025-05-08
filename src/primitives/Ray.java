package primitives;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a Ray (half-line) in 3D geometry.
 * A ray is defined by a point (its origin) and a direction vector.
 * The direction vector must be a unit vector.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Ray {

    private final Point head;
    private final Vector direction;

    /**
     * Constructor for Ray.
     *
     * @param head      The origin point of the ray.
     * @param direction The direction vector of the ray, not necessarily normalized.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Getter for the origin point (head) of the ray.
     *
     * @return The origin point of the ray.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Getter for the direction vector of the ray.
     *
     * @return The direction vector of the ray.
     */
    public Vector getDirection() {
        return direction;
    }


    /**
     * Compares this Ray to another object for equality.
     * Two Rays are considered equal if they have the same head point and the same direction vector.
     *
     * @param obj the object to compare with this Ray
     * @return {@code true} if the specified object is equal to this Ray, {@code false} otherwise
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Ray) && head.equals(((Ray) obj).head) && direction.equals(((Ray) obj).direction);
    }

    /**
     * Returns the hash code for this ray.
     *
     * @return The hash code for this ray.
     */
    @Override
    public int hashCode() {
        return head.hashCode() * 31 + direction.hashCode();
    }

    /**
     * Returns a string representation of the ray, including the origin and direction.
     *
     * @return A string representation of the ray.
     */
    @Override
    public String toString() {
        return "Ray[Origin: " + head + ", Direction: " + direction + "]";
    }

    /**
     * Returns a point on the ray at a given distance from the ray origin.
     *
     * @param t Distance from the origin
     * @return The point at distance t on the ray
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;  // Return the origin point if t == 0
        }
        return head.add(direction.scale(t));  // Otherwise, return the calculated point
    }

    /**
     * Returns the point from the list that is closest to the ray's origin (p0)
     * and lies in the direction of the ray.
     * If the list is null or empty, returns null.
     *
     * @param points the list of points to search
     * @return the closest point in the direction of the ray, or null if none
     */
    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty()) return null;

        Point closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Point point : points) {
            // Skip if the point is exactly at the ray's origin
            if (point.equals(head)) continue;
            Vector toPoint = point.subtract(head);
            // Check if point is in the direction of the ray
            if (direction.dotProduct(toPoint) > 0) {
                double distance = toPoint.lengthSquared(); // faster than distance()
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = point;
                }
            }
        }

        return closest;
    }

}
