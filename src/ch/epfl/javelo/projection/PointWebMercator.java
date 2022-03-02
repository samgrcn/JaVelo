package ch.epfl.javelo.projection;

/**
 * Represents a point in the Web Mercator system.
 *
 * @author Quentin Chappuis (339517)
 */
public record PointWebMercator(double x, double y) {
    public PointWebMercator {
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the point whose coordinates are x and y at the zoom level zoomLevel.
     * @param zoomLevel the zoom level
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the Web Mercator point
     */
    public static PointWebMercator of(int zoomLevel, double x, double y) {
        x = Math.scalb(x, 8 + zoomLevel);
        y = Math.scalb(y, 8 + zoomLevel);
        return new PointWebMercator(x, y);
    }

    /**
     * Returns the Web Mercator point corresponding to the given Swiss coordinate system point.
     * @param pointCh Swiss coordinate point
     * @return the Web Mercator point
     */
    public static PointWebMercator ofPointCh(PointCh pointCh) {
        double lat = pointCh.lat();
        double lon = pointCh.lon();
        return new PointWebMercator(WebMercator.x(lon), WebMercator.y(lat));
    }

    /**
     * Returns the x-coordinate at the given zoom level.
     * @param zoomLevel the zoom level
     * @return x-coordinate
     */
    public double xAtZoomLevel(int zoomLevel) {
        return Math.scalb(x, 8 + zoomLevel);
    }

    /**
     * Returns the y-coordinate at the given zoom level.
     * @param zoomLevel the zoom level
     * @return the y-coordinate
     */
    public double yAtZoomLevel(int zoomLevel) {
        return Math.scalb(y, 8 + zoomLevel);
    }

    /**
     * Returns the longitude of the point, in radians.
     * @return the longitude
     */
    public double lon() {
        return WebMercator.lon(x);
    }

    /**
     * Returns the latitude of the point, in radians.
     * @return the latitude
     */
    public double lat() {
        return WebMercator.lat(y);
    }

    /**
     * Returns the Swiss coordinate point at the same position as the receiver (this) or
     * null if this point is not within the boundaries of Switzerland defined by SwissBounds.
     * @return the Swiss coordinate point or null
     */
    public PointCh toPointCh() {
        double lon = lon();
        double lat = lat();
        double e = Ch1903.e(lon, lat);
        double n = Ch1903.n(lon, lat);

        if (SwissBounds.containsEN(e, n)) return new PointCh(e, n);
        else return null;
    }
}
