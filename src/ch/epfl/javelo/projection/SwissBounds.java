package ch.epfl.javelo.projection;

/**
 * Contains the bounds of Switzerland.
 *
 * @author Quentin Chappuis (339517)
 */
public final class SwissBounds {
    public static final double MIN_E = 2485000.0;
    public static final double MAX_E = 2834000.0;
    public static final double MIN_N = 1075000.0;
    public static final double MAX_N = 1296000.0;
    public static final double WIDTH = MAX_E - MIN_E;
    public static final double HEIGHT = MAX_N - MIN_N;

    private SwissBounds() {};

    /**
     * Returns true if and only if the given E and N coordinates are within Switzerland.
     * @param e East coordinate
     * @param n North coordinate
     * @return true if contained or false if not contained in Switzerland
     */
    public static boolean containsEN(double e, double n) {
      return e >= MIN_E && e <= MAX_E && n >= MIN_N && n <= MAX_N;
    };
}
