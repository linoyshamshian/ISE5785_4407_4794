package primitives;

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

}
