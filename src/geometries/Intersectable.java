package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


/**
 * Abstract base class for all intersectable geometries.
 * Uses NVI design pattern to allow caching or filtering logic in derived classes.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public abstract class Intersectable {

    /**
     * Passive data structure (PDS) to represent a single intersection point with its geometry.
     */
    public static class Intersection {

        /**
         * The geometry object that was intersected
         */
        public final Geometry geometry;

        /**
         * The point at which the ray intersects the geometry
         */
        public final Point point;

        /**
         * The material of the intersected geometry (initialized in the constructor if geometry is not null)
         */
        public final Material material;

        /**
         * The ray direction vector at the point of intersection
         */
        public Vector v;

        /**
         * The normal vector to the geometry at the point of intersection
         */
        public Vector n;

        /**
         * Dot product of the ray direction and the normal vector
         */
        public Double nv;

        /**
         * The light source affecting this point
         */
        public LightSource light;

        /**
         * Vector from the light source to the intersection point
         */
        public Vector l;

        /**
         * Dot product of the light direction and the normal vector
         */
        public Double nl;

        /**
         * Constructor for Intersection object.
         *
         * @param geometry the intersected geometry
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry != null ? geometry.getMaterial() : null;
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

    /**
     * Protected abstract helper method to calculate intersections.
     * Subclasses must implement this method to provide specific intersection logic.
     *
     * @param ray the ray to intersect with the geometry
     * @return list of Intersection objects or null if none
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);


    /**
     * Public method to calculate intersections using the internal helper.
     * Ensures consistent behavior for all geometries.
     *
     * @param ray the ray to intersect with
     * @return list of Intersection objects, or null if there are no intersections
     */
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
