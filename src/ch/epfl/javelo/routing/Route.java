package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;

import java.util.List;

/**
 * Represents a route.
 *
 * @author Quentin Chappuis (339517)
 */
public interface Route {

    /**
     * Returns the segment index at the given position (in metres).
     *
     * @param position the given position
     * @return the segment index
     */
    int indexOfSegmentAt(double position);

    /**
     * Returns the length of the route, in metres.
     *
     * @return the length
     */
    double length();

    /**
     * Returns all the edges of the route.
     *
     * @return all the edges
     */
    List<Edge> edges();

    /**
     * Returns all the points located at the ends of the edges of the route.
     *
     * @return all the points
     */
    List<PointCh> points();

    /**
     * Returns the point at the given position along the route.
     *
     * @param position the given position
     * @return the point
     */
    PointCh pointAt(double position);

    /**
     * Returns the altitude at the given position along the route.
     *
     * @param position the given position
     * @return the altitude
     */
    double elevationAt(double position);

    /**
     * Returns the identity of the node belonging to the route and being closest to the given position.
     *
     * @param position the given position
     * @return the identity of the closest node
     */
    int nodeClosestTo(double position);

    /**
     * Returns the point on the route that is closest to the given reference point.
     *
     * @param point the given point
     * @return the closest point
     */
    RoutePoint pointClosestTo(PointCh point);

}
