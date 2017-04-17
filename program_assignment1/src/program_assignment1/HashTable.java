package program_assignment1;



import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a hash table holding data items whose type is tuple.
 */
public class HashTable {

    // Hash function.
    private HashFunction hashFunction;

    // Linked-lists of the hash table.
    private ArrayList<LinkedList<Tuple>> table;

    // Number of tuples.
    private int numElements;

    /**
     * Creates a hash table of size p where each cell initially is NULL.
     */
    public HashTable(int size) {

        // Create a hash function.
        hashFunction = new HashFunction(size);

        // Create an empty table of size p
        size = hashFunction.getP();
        table = new ArrayList<LinkedList<Tuple>>();
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<Tuple>());
        }
        numElements = 0;
    }

    /**
     * Returns the maximum load of the hash table.
     */
    public int maxLoad() {
        int maxLoad = 0;
        for (LinkedList<Tuple> list : table) {
            maxLoad = Math.max(maxLoad, list.size());
        }
        return maxLoad;
    }

    /**
     * Returns the average load of the hash table.
     */
    public int averageLoad() {
        int totalLoads = 0;
        for (LinkedList<Tuple> list : table) {
            totalLoads += list.size();
        }
        return totalLoads / table.size();
    }

    /**
     * Returns the current size of the hash table.
     */
    public int size() {
        return table.size();
    }

    /**
     * Returns the number of Tuples that are currently stored in the
     * hash table.
     */
    public int numElements() {
        return numElements;
    }

    /**
     * Returns the load factor which is numElements()/size()
     */
    public float loadFactor() {
        return 1f * numElements() / size();
    }

    /**
     * Adds the tuple to the hash table.
     */
    public void add(Tuple t) {
        int index = hashFunction.hash(t.getKey());
        table.get(index).add(t);
        numElements++;

        // When the load factors becomes bigger than 0.7, then it (approximately)
        // doubles the size of the hash table and rehashes all the elements (tuples)
        // to the new hash table.
        if (loadFactor() > 0.7) {
            resize();
        }
    }

    /**
     * Doubles the size of the table.
     */
    private void resize() {

        // Create a new hash function and a new table with doubled size.
        int size = size() * 2;
        hashFunction = new HashFunction(size);
        size = hashFunction.getP();
        ArrayList<LinkedList<Tuple>> oldTable = table;
        table = new ArrayList<LinkedList<Tuple>>();
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<Tuple>());
        }
        numElements = 0;

        // Add the tuples to the new table.
        for (LinkedList<Tuple> list : oldTable) {
            for (Tuple tuple : list) {
                add(tuple);
            }
        }
    }

    /**
     * Returns an array list of Tuples (in the hash table) whose key equals k.
     * If no such Tuples exist, returns an empty list.
     */
    public ArrayList<Tuple> search(int k) {

        // Compute the index of the linked list.
        int index = hashFunction.hash(k);

        // Find the tuples.
        ArrayList<Tuple> result = new ArrayList<Tuple>();
        for (Tuple tuple : table.get(index)) {
            if (tuple.getKey() == k) {
                result.add(tuple);
            }
        }
        return result;
    }

    /**
     * Removes the Tuple t from the hash table.
     */
    public void remove(Tuple t) {

        // Compute the index of the linked list.
        int index = hashFunction.hash(t.getKey());

        // Remove the tuple.
        if (table.get(index).remove(t)) {
            numElements--;
        }
    }

    /**
     * Returns a string represents the hash table.
     */
    @Override
    public String toString() {
        String s = "";
        int index = 1;
        for (LinkedList<Tuple> list : table) {
            s += "(" + (index++) + "): " + list + "\n";
        }
        s += "loadFactor: " + loadFactor() + "\n";
        return s;
    }
}
