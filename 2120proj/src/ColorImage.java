/*
CSI 2120
Project - Part 1

Group:
Samih Karroum - 300188957
Fuad Thabet - 300255031
*/


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
        int shift = 8 - d;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int originalColor = image.getRGB(i, j);
                int alpha = (originalColor >> 24) & 0xFF; // Preserve the alpha channel

                // Shift the color values right by (8 - d) bits
                int red = ((originalColor >> 16) & 0xFF) >> shift;
                int green = ((originalColor >> 8) & 0xFF) >> shift;
                int blue = (originalColor & 0xFF) >> shift;

                // Reassemble the pixel with the reduced color and set it back to the image
                int newColor = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(i, j, newColor);
            }
        }
    }

}