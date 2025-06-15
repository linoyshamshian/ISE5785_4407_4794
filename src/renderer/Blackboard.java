//package renderer;
//
///**
// * Stores configuration for Super-sampling used in the rendering engine.
// * Acts as a shared object ("blackboard") for accessing sampling settings.
// */
//public class Blackboard {
//
//    private boolean isEnabled;
//    private int gridSize; // N x N rays per pixel. Example: 3 → 3x3 = 9 rays.
//
//    // --- Optional parameters (uncomment and use if needed) ---
//    // private double focalDistance;
//    // private SuperSamplingTargetAreaType targetAreaType; // e.g., CIRCLE or SQUARE
//    // private double targetAreaSize;
//    // private boolean useJitter;
//
//    /**
//     * Default constructor.
//     * Disables super-sampling and uses a 1x1 grid (no effect).
//     */
//    public Blackboard() {
//        this.isEnabled = false;
//        this.gridSize = 0;
//    }
//
//    /**
//     * Constructor with grid size; super-sampling remains disabled.
//     *
//     * @param gridSize Number of rays per axis (e.g., 3 for 3x3).
//     */
//    public Blackboard(int gridSize) {
//        this.isEnabled = true;
//        this.gridSize = gridSize;
//    }
//
//    /**
//     * Constructor to fully configure super-sampling.
//     *
//     * @param isEnabled Enable or disable super-sampling.
//     * @param gridSize  Number of rays per axis. Must be >= 1.
//     * @throws IllegalArgumentException if gridSize < 1.
//     */
//    public Blackboard(boolean isEnabled, int gridSize) {
//        if (gridSize < 0) {
//            throw new IllegalArgumentException("Grid size must be at least 1.");
//        }
//        this.isEnabled = isEnabled;
//        this.gridSize = gridSize;
//    }
//
//    /**
//     * @return True if super-sampling is enabled, false otherwise.
//     */
//    public boolean isEnabled() {
//        return isEnabled;
//    }
//
//    /**
//     * Enables or disables super-sampling.
//     *
//     * @param enabled True to enable, false to disable.
//     * @return This object (for method chaining).
//     */
//    public Blackboard setEnabled(boolean enabled) {
//        isEnabled = enabled;
//        return this;
//    }
//
//    /**
//     * @return Grid size (e.g., 3 means 3x3 rays per pixel).
//     */
//    public int getGridSize() {
//        return gridSize;
//    }
//
//    /**
//     * Sets the grid size.
//     *
//     * @param gridSize Must be >= 1.
//     * @return This object (for method chaining).
//     * @throws IllegalArgumentException if gridSize < 1.
//     */
//    public Blackboard setGridSize(int gridSize) {
//        if (gridSize < 0) {
//            throw new IllegalArgumentException("Grid size must be at least 1.");
//        }
//        this.gridSize = gridSize;
//        return this;
//    }
//}




package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified Blackboard class for super-sampling (anti-aliasing) with jittered pattern only.
 * Pattern is fixed to JITTERED, shape is fixed to SQUARE for simplicity and speed.
 * Only gridSize is configurable via constructor.
 */
public class Blackboard {
    private int gridSize; // Number of samples per axis (N x N)
    private boolean enabled;

    public Blackboard() {
        this.enabled = false;
        this.gridSize = 1;
    }

    /**
     * Constructor with only gridSize parameter.
     * Supersampling is enabled by default.
     * Pattern is fixed to JITTERED, shape fixed to SQUARE for performance.
     * @param gridSize Number of samples per axis (e.g., 3 for 3x3)
     */
    public Blackboard(int gridSize) {
        this.enabled = true;
        this.gridSize = gridSize;
    }

    public boolean isEnabled() { return enabled; }
    public int getGridSize() { return gridSize; }

    /**
     * Generates 2D sample points in normalized pixel coordinates (centered at (0,0), range [-0.5,0.5]).
     * Pattern is fixed to JITTERED. Shape is fixed to SQUARE.
     * @return List of double arrays: [dx, dy] offsets
     */
    public List<double[]> generateSamples2D() {
        List<double[]> samples = new ArrayList<>();
        double step = 1.0 / gridSize;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double dx = (i + 0.5) * step - 0.5;
                double dy = (j + 0.5) * step - 0.5;
                // Add jittered random offset inside each cell
                dx += (Math.random() - 0.5) * step;
                dy += (Math.random() - 0.5) * step;
                samples.add(new double[]{dx, dy});
            }
        }
        return samples;
    }

    /**
     * Maps 2D sample offsets to 3D points on the view plane.
     * @param pixelCenter The 3D center of the pixel (primitives.Point)
     * @param right The right vector of the view plane (normalized, scaled to pixel width)
     * @param up The up vector of the view plane (normalized, scaled to pixel height)
     * @return List of 3D points (primitives.Point) for ray directions
     */
    public List<Point> generateSamples3D(Point pixelCenter, Vector right, Vector up) {
        List<Point> points3D = new ArrayList<>();
        for (double[] offset : generateSamples2D()) {
            // pixelCenter + dx * right + dy * up
            Point p = pixelCenter.add(right.scale(offset[0])).add(up.scale(offset[1]));
            points3D.add(p);
        }
        return points3D;
    }
}
