package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;

/**
 * Converts between WGS 84 and Web Mercator coordinates.
 *
 * @author Quentin Chappuis (339517)
 */
public final class WebMercator {
    private WebMercator() {
    }


    /**
     * Returns the x-coordinate of the projection of a point at longitude lon, given in radians.
     *
     * @param lon longitude
     * @return the x-coordinate
     */
    public static double x(double lon) {
        return (lon + Math.PI) / (2 * Math.PI);
    }

    /**
     * Returns the y-coordinate of the projection of a point at latitude lat, given in radians.
     *
     * @param lat latitude
     * @return the y-coordinate
     */
    public static double y(double lat) {
        return (Math.PI - Math2.asinh(Math.tan(lat))) / (2 * Math.PI);
    }

    /**
     * Returns the longitude, in radians, of a point whose projection is at the given x-coordinate.
     *
     * @param x x-coordinate
     * @return the longitude
     */
    public static double lon(double x) {
        return 2 * Math.PI * x - Math.PI;
    }

    /**
     * Returns the latitude, in radians, of a point whose projection is at the given y-coordinate.
     *
     * @param y y-coordinate
     * @return the latitude
     */
    public static double lat(double y) {
        return Math.atan(Math.sinh(Math.PI - 2 * Math.PI * y));
    }
}
