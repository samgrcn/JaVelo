package ch.epfl.javelo.projection;

import ch.epfl.javelo.Preconditions;

/**
 * Represents a point in the Web Mercator system.
 *
 * @author Quentin Chappuis (339517)
 */
public record PointWebMercator(double x, double y) {

    private static final int ZOOM_AT_LEVEL_0 = 8;

    /**
     * @throws IllegalArgumentException if x or y are not between 0 and 1
     */
    public PointWebMercator {
        Preconditions.checkArgument(x >= 0 && x <= 1 && y >= 0 && y <= 1);
    }

    /**
     * Returns the point whose coordinates are x and y at the zoom level zoomLevel.
     *
     * @param zoomLevel the zoom level
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @return the Web Mercator point
     */
    public static PointWebMercator of(int zoomLevel, double x, double y) {
        x = x / Math.pow(2, ZOOM_AT_LEVEL_0 + zoomLevel);
        y = y / Math.pow(2, ZOOM_AT_LEVEL_0 + zoomLevel);

        return new PointWebMercator(x, y);
    }

    /**
     * Returns the Web Mercator point corresponding to the given Swiss coordinate system point.
     *
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
     *
     * @param zoomLevel the zoom level
     * @return x-coordinate
     */
    public double xAtZoomLevel(int zoomLevel) {
        return Math.scalb(x, ZOOM_AT_LEVEL_0 + zoomLevel);
    }

    /**
     * Returns the y-coordinate at the given zoom level.
     *
     * @param zoomLevel the zoom level
     * @return the y-coordinate
     */
    public double yAtZoomLevel(int zoomLevel) {
        return Math.scalb(y, ZOOM_AT_LEVEL_0 + zoomLevel);
    }

    /**
     * Returns the longitude of the point, in radians.
     *
     * @return the longitude
     */
    public double lon() {
        return WebMercator.lon(x);
    }

    /**
     * Returns the latitude of the point, in radians.
     *
     * @return the latitude
     */
    public double lat() {
        return WebMercator.lat(y);
    }

    /**
     * Returns the Swiss coordinate point at the same position as the receiver (this) or
     * null if this point is not within the boundaries of Switzerland defined by SwissBounds.
     *
     * @return the Swiss coordinate point or null
     */
    public PointCh toPointCh() {
        double lon = lon();
        double lat = lat();
        double e = Ch1903.e(lon, lat);
        double n = Ch1903.n(lon, lat);
        
        return SwissBounds.containsEN(e, n) ? new PointCh(e, n) : null;
    }
}
