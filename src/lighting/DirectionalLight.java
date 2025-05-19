package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a directional light source.
 *
 * @author Chen Babay & Linoy Shamshian
 */

public class DirectionalLight extends Light implements LightSource {

    /**
     * Direction the directional light is pointing (normalized)
     */
    private final Vector direction;

    /**
     * Constructor for DirectionalLight
     *
     * @param intensity The color and intensity of the light
     * @param direction The direction vector of the light (will be normalized)
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Returns the intensity of the light at a given point.
     * For directional light, the intensity is constant and independent of the point.
     *
     * @param p The point where the light intensity is calculated
     * @return The constant intensity of the light
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Returns the direction vector from the light source to the point.
     * For directional light, the direction is the same for all points.
     *
     * @param p The point to which the light direction is calculated
     * @return The normalized direction vector of the light
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }

    /**
     * Returns the distance from the light source to a given point.
     * For directional light, which has no position, the distance is considered infinite.
     *
     * @param p The point to which the distance is calculated
     * @return Double.POSITIVE_INFINITY since directional light has no position
     */
    @Override
    public double getDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }
}
