import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

public class SimilaritySearch {

    private static final int K = 5; // Number of most similar images to find

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java SimilaritySearch <query_image> <image_dataset_directory>");
            System.exit(1);
        }

        String queryImagePath = args[0];
        String datasetDirectory = args[1];

        try {
            // Load a specific image
            loadSpecificImage(queryImagePath);

            // Load and process the query image
            ColorImage queryImage = new ColorImage(queryImagePath);
            queryImage.reduceColor(3); // Assuming 3-bit color depth
            ColorHistogram queryHistogram = new ColorHistogram(3);
            queryHistogram.setImage(queryImage);

            // Load the pre-computed histograms from the dataset directory
            File dir = new File(datasetDirectory);
            File[] histogramFiles = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
            if (histogramFiles == null) {
                System.err.println("Error: No histogram files found in the dataset directory.");
                return;
            }

            // Find the K most similar images
            PriorityQueue<ImageSimilarity> pq = new PriorityQueue<>(K, (a, b) -> Double.compare(b.similarity, a.similarity));
            for (File file : histogramFiles) {
                ColorHistogram hist = new ColorHistogram(file.getAbsolutePath());
                double similarity = queryHistogram.compare(hist);
                pq.offer(new ImageSimilarity(file.getName(), similarity));

                if (pq.size() > K) {
                    pq.poll();
                }
            }

            // Print the names of the K most similar images
            System.out.println("The " + K + " most similar images to " + queryImagePath + " are:");
            ImageSimilarity[] similarImages = pq.toArray(new ImageSimilarity[0]);
            Arrays.sort(similarImages, (a, b) -> Double.compare(b.similarity, a.similarity));
            for (ImageSimilarity image : similarImages) {
                System.out.println(image.filename + " (Similarity: " + image.similarity + ")");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void loadSpecificImage(String imagePath) {
        try {
            ColorImage image = new ColorImage(imagePath);
            System.out.println("Specific image loaded successfully!");
            System.out.println("Width: " + image.getWidth() + " pixels");
            System.out.println("Height: " + image.getHeight() + " pixels");
            System.out.println("Depth: " + image.getDepth() + " bits per pixel");
        } catch (IOException e) {
            System.err.println("Error loading specific image: " + e.getMessage());
        }
    }

    // Helper class to store image filename and its similarity to the query image
    private static class ImageSimilarity {
        String filename;
        double similarity;

        ImageSimilarity(String filename, double similarity) {
            this.filename = filename;
            this.similarity = similarity;
        }
    }
}
