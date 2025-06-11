package renderer;

/**
 * Stores configuration for Super-sampling used in the rendering engine.
 * Acts as a shared object ("blackboard") for accessing sampling settings.
 */
public class Blackboard {

    private boolean isEnabled;
    private int gridSize; // N x N rays per pixel. Example: 3 → 3x3 = 9 rays.

    // --- Optional parameters (uncomment and use if needed) ---
    // private double focalDistance;
    // private SuperSamplingTargetAreaType targetAreaType; // e.g., CIRCLE or SQUARE
    // private double targetAreaSize;
    // private boolean useJitter;

    /**
     * Default constructor.
     * Disables super-sampling and uses a 1x1 grid (no effect).
     */
    public Blackboard() {
        this.isEnabled = false;
        this.gridSize = 0;
    }

    /**
     * Constructor with grid size; super-sampling remains disabled.
     *
     * @param gridSize Number of rays per axis (e.g., 3 for 3x3).
     */
    public Blackboard(int gridSize) {
        this.isEnabled = true;
        this.gridSize = gridSize;
    }

    /**
     * Constructor to fully configure super-sampling.
     *
     * @param isEnabled Enable or disable super-sampling.
     * @param gridSize  Number of rays per axis. Must be >= 1.
     * @throws IllegalArgumentException if gridSize < 1.
     */
    public Blackboard(boolean isEnabled, int gridSize) {
        if (gridSize < 0) {
            throw new IllegalArgumentException("Grid size must be at least 1.");
        }
        this.isEnabled = isEnabled;
        this.gridSize = gridSize;
    }

    /**
     * @return True if super-sampling is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Enables or disables super-sampling.
     *
     * @param enabled True to enable, false to disable.
     * @return This object (for method chaining).
     */
    public Blackboard setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    /**
     * @return Grid size (e.g., 3 means 3x3 rays per pixel).
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Sets the grid size.
     *
     * @param gridSize Must be >= 1.
     * @return This object (for method chaining).
     * @throws IllegalArgumentException if gridSize < 1.
     */
    public Blackboard setGridSize(int gridSize) {
        if (gridSize < 0) {
            throw new IllegalArgumentException("Grid size must be at least 1.");
        }
        this.gridSize = gridSize;
        return this;
    }

    // --- Optional parameter accessors (uncomment as needed) ---
    /*
    public double getFocalDistance() {
        return focalDistance;
    }

    public Blackboard setFocalDistance(double focalDistance) {
        this.focalDistance = focalDistance;
        return this;
    }

    public SuperSamplingTargetAreaType getTargetAreaType() {
        return targetAreaType;
    }

    public Blackboard setTargetAreaType(SuperSamplingTargetAreaType targetAreaType) {
        this.targetAreaType = targetAreaType;
        return this;
    }

    public double getTargetAreaSize() {
        return targetAreaSize;
    }

    public Blackboard setTargetAreaSize(double targetAreaSize) {
        this.targetAreaSize = targetAreaSize;
        return this;
    }

    public boolean isUseJitter() {
        return useJitter;
    }

    public Blackboard setUseJitter(boolean useJitter) {
        this.useJitter = useJitter;
        return this;
    }
    */
}

// Example enum for optional use:
/*
package superSampling;

public enum SuperSamplingTargetAreaType {
    CIRCLE,
    SQUARE
}
*/
