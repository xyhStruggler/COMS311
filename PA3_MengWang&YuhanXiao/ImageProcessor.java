import java.awt.*;
import java.util.Scanner;

/**
 *
 *  @author Meng Wang
 *  @author Yuhan Xiao
 */
public class ImageProcessor {

    private final String imageFile;

    /**
     * Constructs an image processor.
     *
     * @param imageFile the name of the image that will be manipulated.
     */
    public ImageProcessor(String imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Computes the distance between two pixels.
     *
     * @param c1 the color of pixel-1.
     * @param c2 the color of pixel-2.
     * @return the distance between the two pixels.
     */
    private static int getDist(Color c1, Color c2) {
        // Dist(p, q) = (r1 − r2)^2 + (g1 − g2)^2 + (b1 - b2)^2
        return (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
                + (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen())
                + (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
    }

    /**
     * Creates an importance matrix of the input picture.
     *
     * @param pixels pixels of the picture.
     * @param width  width of the picture.
     * @param height height of the picture.
     * @return the importance matrix created.
     */
    private static int[][] createImportanceMatrix(Color[][] pixels, int width, int height) {
        int[][] I = new int[height][width];
        int YImportance, XImportance;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) {
                    // if i = 0, then Y Importance(M[i, j]) = Dist(M[H − 1, j], M[i + 1, j]).
                    YImportance = getDist(pixels[height - 1][j], pixels[i + 1][j]);

                } else if (i == height - 1) {
                    //if i = H − 1, then Y Importance(M[i, j]) = Dist(M[i − 1, j], M[0, j]).
                    YImportance = getDist(pixels[i - 1][j], pixels[0][j]);

                } else {
                    // if 0 < i < H, then Y Importance(M[i, j]) = Dist(M[i − 1, j], M[i + 1, j]).
                    YImportance = getDist(pixels[i - 1][j], pixels[i + 1][j]);
                }

                if (j == 0) {
                    // if j = 0, then X Importance(M[i, j]) = Dist(M[i, W − 1], M[i, j + 1]).
                    XImportance = getDist(pixels[i][width - 1], pixels[i][j + 1]);

                } else if (j == width - 1) {
                    // if j = W − 1, then X Importance(M[i, j]) = Dist(M[i, 0], M[i, j − 1]).
                    XImportance = getDist(pixels[i][0], pixels[i][j - 1]);

                } else {
                    // if 0 < j < W, then XImportance(M[i, j]) = Dist(M[i, j − 1], M[i, j + 1]).
                    XImportance = getDist(pixels[i][j - 1], pixels[i][j + 1]);
                }

                // Importance(M[i, j]) = XImportance(M[i, j]) + Y Importance(M[i, j])
                I[i][j] = XImportance + YImportance;
            }
        }
        return I;
    }

    /**
     * Reduces the width of the input picture once.
     *
     * @param pixels pixels of the picture.
     * @param width  width of the picture.
     * @param height height of the picture.
     */
    private static void reduceOnce(Color[][] pixels, int width, int height) {

        // Create an importance matrix.
        int[][] I = createImportanceMatrix(pixels, width, height);

        // Compute Min-Cost vertical cut of I.
        int[] minCostCut = DynamicProgramming.minCostVC(I);

        // For every i, remove the pixel M[i,yi] from the image.
        for (int i = 0; i < height; i++) {
            for (int j = minCostCut[i * 2 + 1]; j < width - 1; j++) {
                pixels[i][j] = pixels[i][j + 1];
            }
        }
    }

    /**
     * Reduces the width of the picture.
     *
     * @param x the width scale.
     * @return a new Picture whose width is x * W.
     */
    public Picture reduceWidth(double x) {
        // Load pixels from the image file.
        Picture picture = new Picture(imageFile);
        int height = picture.height();
        int width = picture.width();
        Color[][] pixels = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = picture.get(j, i);
            }
        }

        int reduceTimes = width - (int) Math.ceil(width * x);
        for (int i = 1; i <= reduceTimes; i++, width--) {

            // Output the processing progress.
            System.out.print(".");
            if (i % 50 == 0) {
                System.out.printf(" %.2f%%\n", (100. * i / reduceTimes));
            }

            // Reduce the width of the picture.
            reduceOnce(pixels, width, height);
        }
        System.out.println("reducing done");

        // Create a new picture after reducing.
        Picture newPicture = new Picture(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newPicture.set(j, i, pixels[i][j]);
            }
        }
        return newPicture;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Image file: ");
        String imageFile = input.nextLine();
        ImageProcessor processor = new ImageProcessor(imageFile);
        Picture picture = processor.reduceWidth(0.5);
        picture.show();
    }
}
