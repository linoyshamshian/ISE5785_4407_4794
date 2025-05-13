package lighting;

import primitives.Color;

/**
 * Abstract base class representing a generic light source.
 *
 * @author Chen Babay & Linoy Shamshian
 */
abstract class Light {

    // The intensity (color) of the light
    protected final Color intensity;

    /**
     * Protected constructor to initialize the intensity.
     *
     * @param intensity the color intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the light intensity.
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
