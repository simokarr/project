/*
CSI 2120
Project - Part 1

Group:
Samih Karroum - 300188957
Fuad Thabet - 300255031
*/


import java.io.*;
import java.util.StringTokenizer;

public class ColorHistogram {

    private double[] histogram; // Array to store the histogram data
    private ColorImage image; // The current image the histogram is based on
    private int d; // Number of bits per color channel

    // Constructor for creating a histogram with a specified bit depth
    public ColorHistogram(int d) {
        this.d = d;
        int binCount = (int) Math.pow(2, d * 3); // 2^(3 * d) bins for RGB color histogram
        histogram = new double[binCount];
    }

    // Constructor to create a histogram from a text file
    public ColorHistogram(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Read the first line
            String[] values = line.split(" "); // Split the line into tokens

            // The first value is the number of bins, which we can ignore for now
            // The rest of the values are the histogram data
            histogram = new double[values.length - 1];
            for (int i = 1; i < values.length; i++) {
                histogram[i - 1] = Double.parseDouble(values[i]);
            }
        }
    }

    // Sets a new image and computes its histogram
    public void setImage(ColorImage image) {
        this.image = image;
        computeHistogram();
    }

    // Computes the histogram from the image
    private void computeHistogram() {
        int width = image.getWidth();
        int height = image.getHeight();
        int binCount = (int) Math.pow(2, d * 3);

        // Reset histogram
        for (int i = 0; i < binCount; i++) {
            histogram[i] = 0;
        }

        // Calculate histogram
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int[] pixel = image.getPixel(i, j);

                // Reduce the color values
                int rPrime = pixel[0] >> (8 - d);
                int gPrime = pixel[1] >> (8 - d);
                int bPrime = pixel[2] >> (8 - d);

                // Calculate the index of the histogram bin corresponding to color [R', G', B']
                int binIndex = (rPrime << (2 * d)) + (gPrime << d) + bPrime;
                histogram[binIndex]++;
            }
        }

        // Normalize histogram
        for (int i = 0; i < binCount; i++) {
            histogram[i] /= (width * height);
        }
    }

    // Returns the histogram as a double array
    public double[] getHistogram() {
        return histogram;
    }

    // Compares this histogram with another, returning the intersection
    public double compare(ColorHistogram other) {
        double intersection = 0.0;
        for (int i = 0; i < histogram.length; i++) {
            intersection += Math.min(histogram[i], other.histogram[i]);
        }

        return intersection;
    }
}
