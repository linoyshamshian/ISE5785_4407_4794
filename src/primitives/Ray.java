package primitives;

import geometries.Intersectable.Intersection;

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
     * Finds the closest point to the ray's head from a list of points.
     * <p>
     * Internally converts each point into an {@link Intersection} object and then
     * delegates to {@link #findClosestIntersection(List)} to determine the closest one.
     *
     * @param intersections List of points to check
     * @return The closest point in the direction of the ray, or {@code null} if none found
     */
    public Point findClosestPoint(List<Point> intersections) {
        return intersections == null ? null
                : findClosestIntersection(
                intersections.stream().map(p -> new Intersection(null, p))
                                .toList()).point;
    }


    /**
     * Finds the closest intersection point to the ray's head from a list of intersections,
     * considering only those that lie in the direction of the ray (i.e., dot product > 0).
     *
     * @param intersections List of {@link Intersection} objects to check
     * @return The closest {@link Intersection} in the ray's direction, or {@code null} if none are valid
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null)
            return null;
        double minDistance = Double.POSITIVE_INFINITY;
        Intersection closestIntersection = null;
        for (Intersection intersection : intersections) {
            Point point = intersection.point;
            // Skip if the point is exactly at the ray's origin
            if (point.equals(head)) continue;
            Vector toPoint = point.subtract(head);
            // Check if point is in the direction of the ray
            if (direction.dotProduct(toPoint) > 0) {
                double distance = toPoint.lengthSquared(); // faster than distance()
                if (distance < minDistance) {
                    minDistance = distance;
                    closestIntersection = intersection;
                }
            }
        }

        return closestIntersection;
    }

}
