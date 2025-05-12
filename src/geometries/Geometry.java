package geometries;

import primitives.Color;
import primitives.Vector;
import primitives.Point;

/**
 * Abstract base class representing a geometric object in 3D space.
 * <p>
 * This class is part of a geometric hierarchy and serves as the foundation
 * for all specific geometry implementations (e.g., Sphere, Plane, Triangle).
 * It includes support for emission color and requires implementing the method
 * for obtaining the normal vector at a specific point on the geometry.
 * </p>
 * <p>
 * It also implements the {@link Intersectable} interface, requiring child classes
 * to define how rays intersect with the geometry.
 * </p>
 *
 * @author Chen Babay & Linoy Shamshian
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry, used in lighting calculations.
     * Default is black, meaning no self-emission.
     */
    protected Color emission = Color.BLACK;

    /**
     * Gets the normal vector of the geometry at a specific point.
     *
     * @param point The point at which to calculate the normal.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point point);


    /**
     * Returns the emission color of the geometry.
     * <p>
     * This color is used in the rendering process to simulate light
     * emitted directly from the surface of the geometry.
     * </p>
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }


    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the new emission color
     * @return the current {@code Geometry} object for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}
