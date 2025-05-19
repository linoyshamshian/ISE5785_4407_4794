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
    private final Vector direction;

    /**
     * Constructor for SpotLight.
     * @param intensity The light's base color/intensity
     * @param position The position of the spotlight
     * @param direction The direction the spotlight is pointing
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Overrides the attenuation factor setter (kC) for builder-style chaining.
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
     * @param kL Linear attenuation factor
     * @return This SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKL(double kL) {
        super.setKL(kL);
        return this;
    }

    /**
     * Overrides the attenuation factor setter (kQ) for builder-style chaining.
     * @param kQ Quadratic attenuation factor
     * @return This SpotLight instance (for chaining)
     */
    @Override
    public SpotLight setKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }

    /**
     * Calculates the intensity of the spotlight at a given point.
     * It scales the PointLight's intensity by the cosine of the angle between
     * the spotlight's direction and the direction to the point.
     * If the angle is greater than 90°, the point is not lit (dot product is negative).
     *
     * @param p The point to calculate light intensity at
     * @return The scaled color intensity at the point
     */
    @Override
    public Color getIntensity(Point p) {
        Color oldColor = super.getIntensity(p);
        return oldColor.scale(Math.max(0d, direction.dotProduct(getL(p))));
    }
}
