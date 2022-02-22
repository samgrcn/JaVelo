package ch.epfl.javelo.projection;

public final class SwissBounds {
    public static final double MIN_E = 2485000.0;
    public static final double MAX_E = 2834000.0;
    public static final double MIN_N = 1075000.0;
    public static final double MAX_N = 1296000.0;
    public static final double WIDTH = MAX_E - MIN_E;
    public static final double HEIGHT = MAX_N - MIN_N;

    private SwissBounds() {};

    public static boolean containsEN(double e, double n) {
      return e >= MIN_E && e <= MAX_E && n >= MIN_N && n <= MAX_N;
    };
}
