package primitives;

/**
 * Class representing the material properties of a surface.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Material {
    /**
     * Diffuse reflection coefficient
     */
    public Double3 kA = Double3.ONE;

    /**
     * Default constructor
     */
    public Material() {}

    /**
     * Setter for kD using a Double3 parameter
     *
     * @param kA the diffuse reflection coefficient
     * @return the Material object itself (for chaining)
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for kA using a double parameter (converted to Double3)
     *
     * @param kA the diffuse reflection coefficient
     * @return the Material object itself (for chaining)
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }
}
