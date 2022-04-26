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

    /**
     * @throws IllegalArgumentException if the zoom is negative
     */
    public MapViewParameters {
        Preconditions.checkArgument(zoomAt >= 0);
    }

    /**
     * Returns the coordinates of the top-left corner as an object of type Point2D.
     *
     * @return the top-left corne point
     */
    public Point2D topLeft() {
        return new Point2D(x, y);
    }

    /**
     * Returns an instance of MapViewParameters identical to the receiver,
     * except that the coordinates of the top-left corner are those passed as arguments to the method.
     *
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     * @return the new instance
     */
    public MapViewParameters withMinXY(double x, double y) {
        return new MapViewParameters(zoomAt, x, y);
    }

    /**
     * Takes as arguments the x and y coordinates of a point,
     * expressed relative to the top-left corner of the map portion displayed on the screen,
     * and returns this point as a PointWebMercator instance.
     *
     * @param x the x-coordinate of a point
     * @param y the y-coordinate of a point
     * @return the point as a PointWebMercator instance
     */
    public PointWebMercator pointAt(double x, double y) {
        return PointWebMercator.of(zoomAt, x, y);
    }

    /**
     * Takes as argument a PointWebMercator and returns the corresponding x position.
     *
     * @param point the PointWebMercator
     * @return the corresponding x position
     */
    public double viewX(PointWebMercator point) {
        return point.x() - x;
    }

    /**
     * Takes as argument a PointWebMercator and returns the corresponding y position.
     *
     * @param point the PointWebMercator
     * @return the corresponding y position
     */
    public double viewY(PointWebMercator point) {
        return point.y() - y;
    }
}
