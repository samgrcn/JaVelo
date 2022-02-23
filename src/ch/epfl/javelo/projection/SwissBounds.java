package ch.epfl.javelo.projection;

/**
 * Contient les limites de la Suisse.
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
     * Retourne vrai si et seulement si les coordonnées E et N données sont dans les limites de la Suisse.
     * @param e coordonnée Est
     * @param n coordonnée Nord
     * @return true si contenu ou false si pas contenu dans la Suisse
     */
    public static boolean containsEN(double e, double n) {
      return e >= MIN_E && e <= MAX_E && n >= MIN_N && n <= MAX_N;
    };
}
