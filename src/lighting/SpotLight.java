package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing spot_light in a scene.
 *
 * @author Chen Babay & Linoy Shamshian
 */

public class SpotLight extends PointLight {

    /**
     * Direction the spotlight is pointing (normalized)
     */
    private final Vector direction;

    /**
     * Controls the narrowness of the spotlight beam.
     * Higher values result in a narrower, more focused beam.
     * Default is 1 (no narrowing).
     */
    private int narrowBeam = 1;

    /**
     * Constructor for SpotLight.
     *
     * @param intensity The light's base color/intensity
     * @param position  The position of the spotlight
     * @param direction The direction the spotlight is pointing
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Overrides the attenuation factor setter (kC) for builder-style chaining.
     *
     * @param kC Constant attenuation factor
     * @return This SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKC(double kC) {
        super.setKC(kC);
        return this;
    }

    /**
     * Overrides the attenuation factor setter (kL) for builder-style chaining.
     *
     * @param kL Linear attenuation factor
     * @return This SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * Overrides the attenuation factor setter (kQ) for builder-style chaining.
     *
     * @param kQ Quadratic attenuation factor
     * @return This SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * Calculates the intensity of the spotlight at a given point.
     * The spotlight intensity is based on the PointLight intensity,
     * scaled by the cosine of the angle between the spotlight's direction
     * and the vector pointing from the light source to the point.
     * A narrow beam effect is applied using the 'narrowBeam' parameter:
     * - If narrowBeam == 1, the spotlight behaves like a regular spotLight.
     * - If narrowBeam > 1, the beam becomes narrower: points not aligned with the
     * spotlight direction receive significantly less intensity.
     *
     * @param p The point at which to calculate the light intensity
     * @return The scaled color intensity at the point
     */
    @Override
    public Color getIntensity(Point p) {
        Color oldColor = super.getIntensity(p);
        if (narrowBeam == 1.0)
            return oldColor.scale(Math.max(0d, direction.dotProduct(getL(p))));
        return oldColor.scale(Math.pow(Math.max(0d, direction.dotProduct(getL(p))), narrowBeam));
    }

}
