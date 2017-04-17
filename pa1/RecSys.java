import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class operates on a mapped ratings matrix.
 */
public class RecSys {

    // Number of users.
    private int numUsers;

    // Number of movies.
    private int numMovies;

    // List of points.
    private ArrayList<Float> pointsList;

    // Rating matrix.
    private ArrayList<ArrayList<Float>> ratesList;

    // NearestPoints algorithm.
    private NearestPoints nearestPoints;

    /**
     * Loads the matrix from the input file.
     */
    public RecSys(String mrMatrix) {
        Scanner input = null;

        try {
            input = new Scanner(new FileInputStream(mrMatrix));

            // Return the numbers and matrix.
            numUsers = input.nextInt();
            numMovies = input.nextInt();
            pointsList = new ArrayList<Float>();
            ratesList = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < numUsers; i++) {
                float points = input.nextFloat();
                pointsList.add(points);
                ArrayList<Float> rates = new ArrayList<Float>();
                for (int j = 0; j < numMovies; j++) {
                    rates.add(input.nextFloat());
                }
                ratesList.add(rates);
            }

            // Build data structure for the hash approach.
            nearestPoints = new NearestPoints(pointsList);
            nearestPoints.buildDataStructure();

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);

        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * Returns the average value of the given list.
     */
    private static float getAverage(ArrayList<Float> values) {
        float sum = 0;
        for (float value : values) {
            sum += value;
        }
        return values.size() > 0 ? sum / values.size() : 0;
    }

    /**
     * Predicts the rating of user u to movie m.
     */
    public float ratingOf(int u, int m) {

        // Convert to zero-based index.
        u--;
        m--;

        // If the user u has rated movie m, then it returns that rating.
        if (ratesList.get(u).get(m) != 0) {
            return ratesList.get(u).get(m);
        }

        // Find the nearest users..
        ArrayList<Float> nearest = nearestPoints.naiveNearestPoints(pointsList.get(u));

        // Add the ratings of the nearest users to movie m to the possible list.
        ArrayList<Float> possibleRatings = new ArrayList<Float>();
        for (int i = 0; i < numUsers; i++) {
            if (nearest.contains(pointsList.get(i)) && ratesList.get(i).get(m) != 0) {
                possibleRatings.add(ratesList.get(i).get(m));
            }
        }

        // Return the average rating.
        return getAverage(possibleRatings);
    }

    public static void main(String[] args) {
        RecSys sys = new RecSys("mrMatrix.txt");
        System.out.println(sys.ratingOf(1, 1));
        System.out.println(sys.ratingOf(1, 2));
        System.out.println(sys.ratingOf(4, 3));
    }
}
