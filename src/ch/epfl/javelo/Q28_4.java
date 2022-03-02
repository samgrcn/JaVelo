package ch.epfl.javelo;

/**
 * Converts numbers between the Q28.4 representation and other representations.
 *
 * @author Quentin Chappuis (339517)
 */
public final class Q28_4 {
    private Q28_4(){};

    /**
     * Returns the value Q28.4 corresponding to the given integer.
     * @param i the integer
     * @return the Q28.4 value
     */
    public static int ofInt(int i) {
        return i << 4;
    }

    /**
     * Returns the value of type double equal to the given value Q28.4.
     * @param q28_4 the Q28.4 value
     * @return the double corresponding
     */
    public static double asDouble(int q28_4) {
        double res = 0.0;
        char[] numbers = Integer.toBinaryString(q28_4).toCharArray();
        for (int i = numbers.length - 1; i >= 0; i--) {
            if (i == 0 && numbers[i] == '1') {
                res -= Math.pow(2, (numbers.length - 5));
            }
            else if (numbers[i] == '1') {
                res += Math.pow(2, (numbers.length - i - 5));
            }
        }
        return res;
    }

    /**
     * Returns the float value corresponding to the given Q28.4 value.
     * @param q28_4 the Q28.4 value
     * @return the float corresponding
     */
    public static float asFloat(int q28_4) {
        float res = 0.0f;
        char[] numbers = Integer.toBinaryString(q28_4).toCharArray();
        for (int i = numbers.length - 1; i >= 0; i--) {
            if (i == 0 && numbers[i] == '1') {
                res -= Math.pow(2, (numbers.length - 5));
            }
            else if (numbers[i] == '1') {
                res += Math.pow(2, (numbers.length - i - 5));
            }
        }
        return res;
    }
}
