import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that contains methods to solve the nearest point problem using
 * both approaches -- Naive and neighbor preserving hash functions.
 */
public class NearestPoints {

    // The points of the problem.
    private ArrayList<Float> points;

    // Hash table for the neighbor preserving hash functions.
    private HashTable hashTable;

    /**
     * Loads points from the given file.
     */
    public NearestPoints(String dataFile) {
        Scanner input = null;

        try {
            input = new Scanner(new FileInputStream(dataFile));
            points = new ArrayList<Float>();
            while (input.hasNextFloat()) {
                points.add(input.nextFloat());
            }

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
     * Constructor to set the points.
     */
    public NearestPoints(ArrayList<Float> pointSet) {
        points = pointSet;
    }

    /**
     * Returns an array list of points (from the set S) that are close to p.
     * This method must implement the naive approach.
     */
    public ArrayList<Float> naiveNearestPoints(float p) {
        ArrayList<Float> results = new ArrayList<Float>();

        // For each point the list,
        for (Float point : points) {

            // If it is close to p, add it to the result list.
            if (Math.abs(p - point) <= 1) {
                results.add(point);
            }
        }
        return results;
    }

    /**
     * Builds the data structure that enables to quickly answer nearest point queries.
     */
    public void buildDataStructure() {

        // Build a hash table of size m > 1.5n (where n is number if points in S).
        hashTable = new HashTable((int) Math.ceil(points.size() * 1.5));

        // For each p in S, add the tuple <g(p), p> at h(g(p)), where h is the hash
        // function used to create the hash table.
        for (Float p : points) {
            hashTable.add(new Tuple((int) Math.floor(p), p));
        }
    }

    /**
     * Returns an array list of points (from the S) that are close to p. This method
     * must use the data structure that was built.
     */
    public ArrayList<Float> npHashNearestPoints(float p) {
        ArrayList<Float> results = new ArrayList<Float>();

        // Compute the hash value of p.
        int k = (int) Math.floor(p);

        // For each hash value differ by at most 1 (k-1, k, k+1)
        for (int i = Math.max(k - 1, 0); i <= k + 1; i++) {

            // Find all the close points in the tuples of k-1, k and k+1 and
            // add them to the result list.
            for (Tuple tuple : hashTable.search(i)) {
                if (Math.abs(tuple.getValue() - p) <= 1) {
                    results.add(tuple.getValue());
                }
            }
        }
        return results;
    }

    /**
     * For every point p in S, compute the list of all points from S that are close
     * to p by calling the method NaiveNearestPoints(p). Write the results to a file
     * named NaiveSolution.txt.
     */
    public void allNearestPointsNaive() {
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileOutputStream("NaiveSolution.txt"));
            for (Float point : points) {
                output.println(point + ": " + naiveNearestPoints(point));
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);

        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * For every point p in S, compute the list of all points from S that are close to
     * p by calling the method NPHashNearestPoints(p). Write the results to a file named
     * HashSolution.txt.
     */
    public void allNearestPointsHash() {
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileOutputStream("HashSolution.txt"));
            for (Float point : points) {
                output.println(point + ": " + npHashNearestPoints(point));
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);

        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    public static void main(String[] args) {

        NearestPoints nearestPoints = new NearestPoints("points.txt");
        nearestPoints.buildDataStructure();

        long start1 = System.currentTimeMillis();
        nearestPoints.allNearestPointsNaive();
        long end1 = System.currentTimeMillis();
        System.out.println("allNearestPointsNaive(): " + (end1 - start1) / 1000.0 + " s");

        long start2 = System.currentTimeMillis();
        nearestPoints.allNearestPointsHash();
        long end2 = System.currentTimeMillis();
        System.out.println("allNearestPointsHash(): " + (end2 - start2) / 1000.0 + " s");
    }
}
