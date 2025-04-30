package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ImageWriter}.
 */
class ImageWriterTest {

    /**
     * Tests the basic functionality of writing a solid color image.
     * It creates an {@link ImageWriter} instance, fills it with yellow color,
     * and then writes it to a file named "yellow.png".
     */
    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter(800, 500);
        for (int i = 0; i < imageWriter.nX(); i++)
            for (int j = 0; j < imageWriter.nY(); j++)
                imageWriter.writePixel(i, j, new Color(java.awt.Color.YELLOW));
        imageWriter.writeToImage("yellow");
    }

    /**
     * Tests the functionality of writing an image with a grid.
     * It creates an {@link ImageWriter} instance with a specified resolution,
     * fills it with a background color (pink), and then draws a white grid
     * of 16x10 squares on top. Finally, it writes the image to a file
     * named "imageWithGrid.png".
     */
    @Test
    void testWriteImageWithGrid() {
        int width = 800;
        int height = 500;
        int rows = 10;
        int cols = 16;
        Color backgroundColor = new Color(java.awt.Color.PINK);
        Color gridColor = new Color(java.awt.Color.WHITE);

        ImageWriter imageWriter = new ImageWriter(width, height);

        // Step 1: Paint the background with the specified color.
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imageWriter.writePixel(i, j, backgroundColor);
            }
        }

        // Step 2: Draw vertical grid lines.
        for (int i = 0; i < cols; i++) {
            int x = i * width / cols;
            for (int j = 0; j < height; j++) {
                imageWriter.writePixel(x, j, gridColor);
            }
        }

        for (int j = 0; j < height; j++) {
            imageWriter.writePixel(width - 1, j, gridColor);
        }

        // Step 3: Draw horizontal grid lines.
        for (int j = 0; j < rows; j++) {
            int y = j * height / rows;
            for (int i = 0; i < width; i++) {
                imageWriter.writePixel(i, y, gridColor);
            }
        }

        for (int i = 0; i < width; i++) {
            imageWriter.writePixel(i, height - 1, gridColor);
        }

        imageWriter.writeToImage("imageWithGrid");
    }
}