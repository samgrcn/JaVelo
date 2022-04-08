package ch.epfl.javelo;

/**
 * Converts numbers between the Q28.4 representation and other representations.
 *
 * @author Quentin Chappuis (339517)
 */
public final class Q28_4 {
    private Q28_4(){}

    /**
     * Returns the value Q28.4 corresponding to the given integer.
     *
     * @param i the integer
     * @return the Q28.4 value
     */
    public static int ofInt(int i) {
        return i << 4;
    }

    /**
     * Returns the value of type double equal to the given value Q28.4.
     *
     * @param q28_4 the Q28.4 value
     * @return the double corresponding
     */
    public static double asDouble(int q28_4) {
        return Math.scalb((double) q28_4, -4);
    }

    /**
     * Returns the float value corresponding to the given Q28.4 value.
     *
     * @param q28_4 the Q28.4 value
     * @return the float corresponding
     */
    public static float asFloat(int q28_4) {
        return Math.scalb((float) q28_4, -4);
    }
}
