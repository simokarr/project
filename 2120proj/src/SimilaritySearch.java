import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimilaritySearch {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java SimilaritySearch <query_image> <image_dataset_directory>");
            System.exit(1);
        }

        String queryImagePath = args[0];
        String datasetDirectory = args[1];

        ColorImage queryImage = new ColorImage(queryImagePath);
        ColorHistogram queryHistogram = new ColorHistogram(3);
        queryHistogram.setImage(queryImage);

        File datasetDir = new File(datasetDirectory);
        File[] imageFiles = datasetDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".jpeg"));

        if (imageFiles == null) {
            System.err.println("Error: No image files found in the dataset directory.");
            return;
        }

        TreeMap<Double, String> topSimilarImages = new TreeMap<>(Collections.reverseOrder());

        for (File imageFile : imageFiles) {
            ColorImage datasetImage = new ColorImage(imageFile.getAbsolutePath());
            ColorHistogram datasetHistogram = new ColorHistogram(3);
            datasetHistogram.setImage(datasetImage);

            double similarityScore = queryHistogram.compare(datasetHistogram);
            topSimilarImages.put(similarityScore, imageFile.getName());

            if (topSimilarImages.size() > 5) {
                topSimilarImages.pollLastEntry();
            }
        }

        System.out.println("The 5 most similar images are: ");
        topSimilarImages.forEach((score, name) -> System.out.println(name + " - Similarity: " + score));
    }
}
