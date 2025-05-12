package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Geometries class represents a collection (composite) of intersectable geometry objects.
 * It implements the Intersectable interface and allows finding intersections of a ray
 * with any of the geometries in the collection.
 * <p>
 * This class is useful for grouping multiple geometry objects (like spheres, planes, etc.)
 * and treating them as a single object when performing intersection calculations.
 *
 * @author Chen Babay & Linoy Shamshian
 */

public class Geometries extends Intersectable {
    List<Intersectable> geometries = new LinkedList<Intersectable>();

    /**
     * Default constructor. Initializes an empty collection of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructor that accepts one or more geometry objects and adds them to the collection.
     *
     * @param geometries One or more Intersectable objects to be added to the collection.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the internal list.
     *
     * @param geometries The geometries to add.
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds all intersection points between a given ray and all geometries in the collection.
     *
     * @param ray The ray for which to find intersections with the geometries.
     * @return A list of intersection points, or {@code null} if there are no intersections.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;

        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calculateIntersections(ray);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(geometryIntersections);
            }
        }
        return intersections;
    }
}
