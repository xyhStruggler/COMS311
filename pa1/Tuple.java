/**
 * Represents tuples of form <key,value>, where key is of type int
 * and value is of type double.
 */
public final class Tuple {

    // The key of the tuple.
    private final int key;

    // The value of the tuple.
    private final float value;

    /**
     * Creates Tuple object with keyP as key and valueP as value.
     */
    public Tuple(int keyP, float valueP) {
        key = keyP;
        value = valueP;
    }

    /**
     * Returns key.
     */
    public int getKey() {
        return key;
    }

    /**
     * Returns value.
     */
    public float getValue() {
        return value;
    }

    /**
     * Returns true if this tuple equals t; otherwise returns false.
     */
    public boolean equals(Tuple t) {
        if (t == null) {
            return false;
        }
        if (t == this) {
            return true;
        }
        return t.getKey() == getKey() && Float.compare(t.getValue(), getValue()) == 0;
    }

    /**
     * Returns a string represents the tuple.
     */
    @Override
    public String toString() {
        return String.format("(%d, %.3f)", key, value);
    }
}
