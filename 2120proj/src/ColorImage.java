import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ColorImage {
    public int width;
    public int height;
    public int depth;
    public BufferedImage image;

    public ColorImage(String filename) throws IOException {
        this.image = ImageIO.read(new File(filename));
        this.width=image.getWidth();
        this.height= image.getHeight();
        this.depth=image.getColorModel().getPixelSize();


    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getPixel(int i, int j){
        int colours=image.getRGB(i, j);
        int[] yurr = new int[3];
        yurr[0] = (colours >> 16) & 0xFF; // Extract the red component
        yurr[1] = (colours >> 8) & 0xFF;  // Extract the green component
        yurr[2] = colours & 0xFF;
        return yurr;

    }
    public void reduceColor(int d) {
        // Since d is fixed at 3 bits for this scenario
        int maxValue = (1 << d) - 1; // 7, since 2^3 - 1 = 7
        double scale = 255.0 / maxValue; // Scale factor to map original color to 3-bit color

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int originalColor = image.getRGB(i, j);
                int alpha = (originalColor >> 24) & 0xFF; // Preserve the alpha channel

                int[] colors = getPixel(i, j); // Extract the original RGB values

                // Quantize each color component to 3 bits
                colors[0] = (int)(((colors[0] / scale) + 0.5) * scale); // Red
                colors[1] = (int)(((colors[1] / scale) + 0.5) * scale); // Green
                colors[2] = (int)(((colors[2] / scale) + 0.5) * scale); // Blue

                // Reassemble the pixel with the reduced color and set it back to the image
                int newColor = (alpha << 24) | (colors[0] << 16) | (colors[1] << 8) | colors[2];
                image.setRGB(i, j, newColor);
            }
        }
    }

}
