package ch.epfl.javelo;

public final class Preconditions {
    private Preconditions() {}

    /**
     * Lève l'exception IllegalArgumentException si son argument est faux, et ne fait rien sinon.
     * @param shouldBeTrue
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
