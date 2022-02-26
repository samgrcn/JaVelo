package ch.epfl.javelo.projection;

/**
 * Représente un point dans le système Web Mercator.
 *
 * @author Quentin Chappuis (339517)
 */
public record PointWebMercator(double x, double y) {
    public PointWebMercator {
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            throw new IllegalArgumentException();
        }
    }

    public static PointWebMercator of(int zoomLevel, double x, double y) {
        x = Math.scalb(x, 8 + zoomLevel);
        y = Math.scalb(y, 8 + zoomLevel);
        return new PointWebMercator(x, y);
    }

    public static PointWebMercator ofPointCh(PointCh pointCh) {
        double lat = pointCh.lat();
        double lon = pointCh.lon();
        return new PointWebMercator(WebMercator.x(lon), WebMercator.y(lat));
    }

    public double xAtZoomLevel(int zoomLevel) { return Math.scalb(x, 8 + zoomLevel); }

    public double yAtZoomLevel(int zoomLevel) {
        return Math.scalb(y, 8 + zoomLevel);
    }

    public double lon() {
        return WebMercator.lon(x);
    }

    public double lat() {
        return WebMercator.lat(y);
    }

    public PointCh toPointCh() {
        double lon = lon();
        double lat = lat();
        double e = Ch1903.e(lon, lat);
        double n = Ch1903.n(lon, lat);

        if (SwissBounds.containsEN(e, n)) return new PointCh(e, n);
        else return null;
    }
}
