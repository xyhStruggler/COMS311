package program_assignment1;


import java.util.Random;

/**
 * Represents a random hash function that can be used in a hash table.
 */
public class HashFunction {

    // Instance variables.
    private int a;
    private int b;
    private int p;

    /**
     * Constructs a HashFunction.
     */
    public HashFunction(int range) {

        // Picks the first (positive) prime integer whose value is at least range,
        // and sets the value of p to that prime integer.
        setP(range);

        // Then picks two random integers x and y from {0,1, ..., p-1} and sets a
        // as x and b as y.
        Random rand = new Random();
        setA(rand.nextInt(p));
        setB(rand.nextInt(p));
    }

    /**
     * Returns the value of the hash function on x.
     */
    public int hash(int x) {
        return (int) (((long) a * x + b) % p);
    }

    /**
     * Returns a.
     */
    public int getA() {
        return a;
    }

    /**
     * Returns b.
     */
    public int getB() {
        return b;
    }

    /**
     * Returns p.
     */
    public int getP() {
        return p;
    }

    /**
     * Changes the value of a.
     */
    public void setA(int x) {
        a = x;
    }

    /**
     * Changes the value of b.
     */
    public void setB(int y) {
        b = y;
    }

    /**
     * Returns true if x is a prime or false otherwise.
     */
    private static boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        for (int i = 2; i * i <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Changes the value of p.
     */
    public void setP(int x) {
        // Pick the first (positive) prime whose value is at least x and sets
        // the value of p to that integer.
        p = Math.max(0, x);
        while (!isPrime(p)) {
            p++;
        }
    }
}
