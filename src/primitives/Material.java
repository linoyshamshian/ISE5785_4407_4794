package primitives;

/**
 * Class representing the material properties of a surface.
 *
 * @author Chen Babay & Linoy Shamshian
 */
public class Material {

    /**
     * Ambient reflection coefficient (𝒌𝑨)
     */
    public Double3 kA = Double3.ONE;

    /**
     * Diffuse reflection coefficient (𝒌𝒅)
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient (𝒌𝒔)
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Shininess factor (𝒔𝒏)
     */
    public int nSh = 0;

    /**
     * Default constructor
     */
    public Material() {
    }

    /**
     * Sets the ambient reflection coefficient (𝒌𝑨) using a {@link Double3} value.
     * Supports method chaining.
     *
     * @param kA the ambient reflection coefficient as a Double3
     * @return the current Material instance (for chaining)
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient (𝒌𝑨) using a single double value.
     * Converts it to a {@link Double3}. Supports method chaining.
     *
     * @param kA the ambient reflection coefficient as a double
     * @return the current Material instance (for chaining)
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Setter for the diffuse reflection coefficient (𝒌𝒅)
     *
     * @param kD the diffuse coefficient
     * @return the material itself for method chaining
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for the diffuse reflection coefficient (𝒌𝒅)
     *
     * @param kD the diffuse coefficient as a double
     * @return the material itself for method chaining
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for the specular reflection coefficient (𝒌𝒔)
     *
     * @param kS the specular coefficient
     * @return the material itself for method chaining
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for the specular reflection coefficient (𝒌𝒔)
     *
     * @param kS the specular coefficient as a double
     * @return the material itself for method chaining
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for the shininess factor (𝒔𝒏)
     *
     * @param nSh shininess value
     * @return the material itself for method chaining
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }
}
