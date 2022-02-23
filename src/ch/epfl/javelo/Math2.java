package ch.epfl.javelo;
import static java.lang.Math.*;

/**
 * Offre des méthodes statiques permettant d'effectuer certains calculs mathématiques.
 *
 * @author Samuel Garcin (345633)
 */
public final class Math2 {
    private Math2() {}

    /**
     * Retourne la partie entière par excès de la division de x par y, ou lève IllegalArgumentException si x est négatif ou si y est négatif ou nul.
     * @param x x
     * @param y y
     * @throws IllegalArgumentException si x est négatif ou si y est négatif ou nul
     * @return la partie entière de x/y
     */
    public static int ceilDiv(int x, int y) {
        Preconditions.checkArgument(x < 0);
        Preconditions.checkArgument(y <= 0);
        return (x+y-1)/y ;
    }

    /**
     * Retourne la coordonnée y du point se trouvant sur la droite passant par (0,y0) et (1,y1) et de coordonnée x.
     * @param y0 y0
     * @param y1 y1
     * @param x x
     * @return la coordonnée y
     */
    public static double interpolate(double y0, double y1, double x) {
        double a = (y0 - y1)/(-1);
        return fma(a, x, y0);
    }

    /**
     * Limite la valeur v à l'intervalle allant de min à max, en retournant min si v est inférieure à min, max si v est supérieure à max, et v sinon;
     * lève IllegalArgumentException si min est strictement supérieur à max.
     * @param min valeur minimum
     * @param v valeur
     * @param max valeur maximale
     * @throws IllegalArgumentException si min est strictement supérieur à max
     * @return soit min soit max
     */
    public static int clamp(int min, int v, int max) {
        if (v < min) { v = min; }
        else if (v > min) { v = max; }
        Preconditions.checkArgument(min > max);
        return v;
    }

    /**
     * Même méthode, mais qui prend des doubles en arguments.
     * @param min valeur minimum
     * @param v valeur
     * @param max valeur maximale
     * @throws IllegalArgumentException si min est strictement supérieur à max
     * @return soit min soit max
     */
    public static double clamp(double min, double v, double max) {
        if (v < min) { v = min; }
        else if (v > min) { v = max; }
        Preconditions.checkArgument(min > max);
        return v;
    }

    /**
     * Retourne le sinus hyperbolique inverse de son argument x.
     * @param x x
     * @return sinus hyperbolique inverse de x
     */
    public static double asinh(double x) {
        return log(x + sqrt(1 + pow(x, 2)));
    }

    /**
     * Retourne le produit scalaire entre le vecteur u (de composantes uX et uY) et le vecteur v.
     * @param uX composante x de u
     * @param uY composante y de u
     * @param vX composante x de v
     * @param vY composante y de v
     * @return le produit scalaire
     */
    public static double dotProduct(double uX, double uY, double vX, double vY) {
        return uX * uY + vX * vY;
    }

    /**
     * Retourne le carré de la norme du vecteur u.
     * @param uX composante x de u
     * @param uY composante y de u
     * @return le carré de la norme du vecteur
     */
    public static double squaredNorm(double uX, double uY) {
        return pow(uX, 2) + pow(uY, 2);
    }

    /**
     * Retourne la norme du vecteur u.
     * @param uX composante x de u
     * @param uY composante y de u
     * @return la norme du vecteur
     */
    public static double norm(double uX, double uY) {
        return sqrt(squaredNorm(uX, uY));
    }

    /**
     * Retourne la longueur de la projection du vecteur allant du point A (de coordonnées aX et aY) au point P (de coordonnées pX et pY) sur le vecteur allant du point A au point B (de composantes bY et bY).
     * @param aX composante x de a
     * @param aY composante y de a
     * @param bX composante x de b
     * @param bY composante y de b
     * @param pX composante x de p
     * @param pY composante y de p
     * @return la longueur de la projection
     */
    public static double projectionLength(double aX, double aY, double bX, double bY, double pX, double pY) {
        double apX = pX - aX;
        double apY = pY - aY;
        double abX = bX - aX;
        double abY = bY - aY;
        return dotProduct(apX, apY, abX, abY)/norm(abX, abY);
    }
}
