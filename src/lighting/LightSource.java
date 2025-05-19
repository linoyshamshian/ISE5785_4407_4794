package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in a scene.
 *
 * @author Chen Babay & Linoy Shamshian
 */

public interface LightSource {
    /**
     * Calculates the intensity of the light at the given point.
     * @param p point to calculate light intensity at
     * @return light intensity as Color
     */
    Color getIntensity(Point p);

    /**
     * Returns normalized direction vector from light to point p.
     * @param p point in space
     * @return unit vector in direction from light to point
     */
    Vector getL(Point p);

    /**
     * Returns distance from light source to point p.
     * @param p point in space
     * @return distance to point
     */
    double getDistance(Point p);
}