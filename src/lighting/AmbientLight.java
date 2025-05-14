package lighting;

import primitives.Color;

/**
 * Class representing ambient light in the scene.
 * Ambient light is a global light source that affects all objects equally.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class AmbientLight extends Light {
    /**
     * Public constant for ambient light with no intensity (black).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for ambient light with given intensity.
     *
     * @param intensity the light intensity (Color)
     */
    public AmbientLight(Color intensity) {
        super(intensity); // Call to the superclass constructor
    }


}
