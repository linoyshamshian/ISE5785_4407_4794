package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

/**
 * Class representing a point light source.
 *
 * @author Chen Babay & Linoy Shamshian
 */

public class PointLight extends Light implements LightSource {
    // The 3D position of the point light source
    private final Point position;
    // Constant attenuation factor
    private double kC = 1.0;
    // Linear attenuation factor
    private double kL = 0.0;
    // Quadratic attenuation factor
    private double kQ = 0.0;

    /**
     * Constructor for PointLight.
     * @param intensity The base intensity (color) of the light
     * @param position The location of the light source in 3D space
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor (kC).
     * @param kC The constant attenuation coefficient
     * @return This PointLight object (for chaining)
     */
    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL).
     * @param kL The linear attenuation coefficient
     * @return This PointLight object (for chaining)
     */
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ).
     * @param kQ The quadratic attenuation coefficient
     * @return This PointLight object (for chaining)
     */
    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Calculates the light intensity at a given point.
     * Uses the attenuation formula: I / (kC + kL*d + kQ*d^2)
     * where d is the distance from the light source to the point.
     *
     * @param p The point where light intensity is evaluated
     * @return The attenuated light intensity at the point
     */
    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        double factor = kC + kL * d + kQ * d * d;
        // If the factor is effectively zero, return maximum intensity to avoid division by zero
        if(Util.isZero(factor))
            return intensity.scale(Double.POSITIVE_INFINITY);
        return intensity.scale(1.0/factor);
    }

    /**
     * Returns the direction vector from the light to a given point.
     * The vector is normalized (unit vector).
     *
     * @param p The target point
     * @return Normalized direction vector from light to point
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    /**
     * Returns the distance from the light source to the given point.
     *
     * @param p The target point
     * @return Euclidean distance from light to point
     */
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
