package ch.epfl.javelo.projection;

/**
 * Permet de convertir entre les coordonnées WGS 84 et les coordonnées Web Mercator.
 *
 * @author Quentin Chappuis (339517)
 */
public final class WebMercator {
    private WebMercator(){};

    private static double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    public static double x(double lon) {
        return 1 / (2 * Math.PI) * (lon + Math.PI);
    }

    public static double y(double lat) {
        return 1 / (2 * Math.PI) * (Math.PI - asinh(Math.tan(lat)));
    }

    public static double lon(double x) {
        return 2 * Math.PI * x - Math.PI;
    }

    public static double lat(double y) {
        return Math.atan(Math.sinh(Math.PI - 2 * Math.PI * y));
    }
}
