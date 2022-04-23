package ch.epfl.javelo.gui;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.geometry.Point2D;

/**
 * Represents the parameters of the background map presented in the graphical interface.
 *
 * @author Quentin Chappuis (339517)
 */
public record MapViewParameters(int zoomAt, double x, double y) {

    public MapViewParameters {
        Preconditions.checkArgument(zoomAt >= 0);
    }

    public Point2D topLeft() {
        return new Point2D(x, y);
    }

    public MapViewParameters withMinXY(double x, double y) {
        return new MapViewParameters(zoomAt, x, y);
    }

    public PointWebMercator pointAt(double x, double y) {
        return PointWebMercator.of(zoomAt, x, y);
    }

    public double viewX(PointWebMercator point) {
        return point.x();
    }

    public double viewY(PointWebMercator point) {
        return point.y();
    }
}
