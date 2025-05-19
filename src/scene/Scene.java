package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * A Plain Data Structure (PDS) representing a 3D scene for rendering.
 * Holds all the essential scene configuration including name, background,
 * ambient light and geometries.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;

    /**
     * The background color of the scene.
     * Defaults to black.
     */
    public Color background = Color.BLACK;

    /**
     * A list of light sources in the scene.
     * Initialized as an empty LinkedList by default.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * The ambient light of the scene.
     * Default is {@link lighting.AmbientLight#NONE}.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The collection of geometries in the scene.
     * Default is an empty {@link geometries.Geometries} object.
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructs a scene with the given name.
     *
     * @param sceneName the name of the scene
     */
    public Scene(String sceneName) {
        this.name = sceneName;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color
     * @return the current Scene object (for chaining)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light
     * @return the current Scene object (for chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the geometries
     * @return the current Scene object (for chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Sets the list of light sources in the scene.
     * This method follows the Builder design pattern to allow method chaining.
     *
     * @param lights a list of LightSource objects to be used in the scene
     * @return the current Scene object, for method chaining
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

}
