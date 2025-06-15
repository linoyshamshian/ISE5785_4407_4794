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
     * Transparency attenuation coefficient (𝒌𝑻)
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Reflection attenuation coefficient (𝒌𝑹)
     */
    public Double3 kR = Double3.ZERO;

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
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for the diffuse reflection coefficient (𝒌𝒅)
     *
     * @param kD the diffuse coefficient as a double
     * @return the material itself for method chaining
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for the specular reflection coefficient (𝒌𝒔)
     *
     * @param kS the specular coefficient
     * @return the material itself for method chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for the specular reflection coefficient (𝒌𝒔)
     *
     * @param kS the specular coefficient as a double
     * @return the material itself for method chaining
     */
    public Material setKS(double kS) {
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

    /**
     * Sets the transparency attenuation coefficient (𝒌𝑻) using a {@link Double3} value.
     *
     * @param kT the transparency coefficient
     * @return the current Material instance (for chaining)
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient (𝒌𝑻) using a single double value.
     *
     * @param kT the transparency coefficient as a double
     * @return the current Material instance (for chaining)
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient (𝒌𝑹) using a {@link Double3} value.
     *
     * @param kR the reflection coefficient
     * @return the current Material instance (for chaining)
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient (𝒌𝑹) using a single double value.
     *
     * @param kR the reflection coefficient as a double
     * @return the current Material instance (for chaining)
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }
}
