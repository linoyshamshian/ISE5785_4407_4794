package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * A Plain Data Structure (PDS) representing a 3D scene for rendering.
 * Holds all the essential scene configuration including name, background,
 * ambient light and geometries.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Scene {
    public String name;
    public Color background = Color.BLACK;

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

}
