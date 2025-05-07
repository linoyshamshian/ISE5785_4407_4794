package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing algorithms.
 * Provides a common interface and holds the scene data.
 */
public abstract class RayTracerBase {
    /**
     * The scene to be rendered.
     */
    protected final Scene scene;

    /**
     * Constructor for RayTracerBase.
     *
     * @param scene the scene to trace rays in
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray and returns the color it encounters in the scene.
     *
     * @param ray the ray to trace
     * @return the color resulting from the ray tracing
     */
    public abstract Color traceRay(Ray ray);
}
