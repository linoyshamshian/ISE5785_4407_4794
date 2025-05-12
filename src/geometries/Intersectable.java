package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;


/**
 * Abstract base class for all intersectable geometries.
 * Uses NVI design pattern to allow caching or filtering logic in derived classes.
 */
public abstract class Intersectable {

    /**
     * Passive data structure (PDS) to represent a single intersection point with its geometry.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        /**
         * Constructor for Intersection object.
         *
         * @param geometry the intersected geometry
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }


        /**
         * Indicates whether some other object is "equal to" this one.
         * Two Intersection objects are considered equal if they have the same geometry reference
         * and their points are equal.
         *
         * @param obj the reference object with which to compare
         * @return true if this object is the same as the obj argument; false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return geometry == other.geometry && point.equals(other.point);
        }


        /**
         * Returns a string representation of the Intersection object.
         *
         * @return a string containing the geometry and point values of the intersection
         */
        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }


    /**
     * Finds all intersection points between the ray and the geometry.
     *
     * @param ray the ray to intersect with
     * @return list of intersection points, or null if there are no intersections
     */
//    public abstract List<Point> findIntersections(Ray ray);
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }


}
