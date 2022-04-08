package ch.epfl.javelo;

/**
 * Provides a method that checks the arguments.
 *
 * @author Samuel Garcin (345633)
 */
public final class Preconditions {
    private Preconditions() {}

    /**
     * Throws the IllegalArgumentException if its argument is false, and does nothing otherwise.
     *
     * @param shouldBeTrue the argument
     * @throws IllegalArgumentException if the passed argument is false
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
