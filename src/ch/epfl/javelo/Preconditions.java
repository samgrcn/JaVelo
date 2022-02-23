package ch.epfl.javelo;

/**
 * Offre une méthode qui check les arguments.
 *
 * @author Samuel Garcin (345633)
 */
public final class Preconditions {
    private Preconditions() {}

    /**
     * Lève l'exception IllegalArgumentException si son argument est faux, et ne fait rien sinon.
     * @param shouldBeTrue l'argument
     * @throws IllegalArgumentException si l'argument passé est false
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
