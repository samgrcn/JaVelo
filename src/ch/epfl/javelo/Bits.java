package ch.epfl.javelo;

/**
 * Contains two methods for extracting a bit sequence from a 32-bit vector.
 *
 * @author Quentin Chappuis (339517)
 */
public final class Bits {

    private Bits(){}

    /**
     * Extracts from the 32-bit value vector the length bit range starting at the start index bit, which it interprets as a two's complement signed value.
     * @param value the 32-bit value
     * @param start the start index
     * @param length the length bit range
     * @throws IllegalArgumentException if the bit range is not completely included in the interval from 0 to 31 (inclusive) and
     * if the length is equal to zero
     * @return the extracted 32-bit vector
     */
    public static int extractSigned(int value, int start, int length) {
        if (start < 0 || start + length > 32 || length <= 0) {
            throw new IllegalArgumentException();
        }
        value = value << 32 - start - length;
        return value >> 32 - length;
    }

    /**
     * Extracts from the 32-bit value vector the length bit range starting at the start index bit, which it interprets as an unsigned two's complement value.
     * @param value the 32-bit value
     * @param start the start index
     * @param length the length bit range
     * @throws IllegalArgumentException if the bit range is not completely included in the interval from 0 to 31 (inclusive) and
     * if the length is equal to zero or the length equal to 32
     * @return the extracted 32-bit vector
     */
    public static int extractUnsigned(int value, int start, int length) {
        if (start < 0 || start + length > 32 || length == 32 || length <= 0) {
            throw new IllegalArgumentException();
        }
        value = value << 32 - start - length;
        return value >>> 32 - length;
    }
}
