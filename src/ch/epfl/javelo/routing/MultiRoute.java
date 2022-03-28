package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;

import java.util.ArrayList;
import java.util.List;

public class MultiRoute implements Route {
    private final List<Route> segments;
    private final double[] route;

    public MultiRoute(List<Route> segments) {
        Preconditions.checkArgument(!segments.isEmpty());
        this.segments = segments;
        route = new double[segments.size() + 1];
        route[0] = 0;
        for (int i = 1; i < segments.size() + 1; i++) {
            route[i] = segments.get(i - 1).length() + route[i - 1];
        }
    }

    /**
     * Returns the segment index at the given position (in metres).
     *
     * @param position the given position
     * @return the segment index
     */
    @Override
    public int indexOfSegmentAt(double position) {
        return 0;
    }

    /**
     * Returns the length of the route, in metres.
     *
     * @return the length
     */
    @Override
    public double length() {
        return route[route.length - 1];
    }

    /**
     * Returns all the edges of the route.
     *
     * @return all the edges
     */
    @Override
    public List<Edge> edges() {
        for (Route segment : segments) {

        }
        return null;
    }

    /**
     * Returns all the points located at the ends of the edges of the route.
     *
     * @return all the points
     */
    @Override
   public List<PointCh> points() {
   //     List<PointCh> res = new ArrayList<>();
   //     res.add(segments.get(0).fromPoint());
   //     for (Route segment : segments) res.add((Edge)segment.toPoint());
        return null;
    }

    /**
     * Returns the point at the given position along the route.
     *
     * @param position the given position
     * @return the point
     */
    @Override
    public PointCh pointAt(double position) {
        position = Math2.clamp(0, position, length());
        return null;
    }

    /**
     * Returns the altitude at the given position along the route.
     *
     * @param position the given position
     * @return the altitude
     */
    @Override
    public double elevationAt(double position) {
        position = Math2.clamp(0, position, length());
        return 0;
    }

    /**
     * Returns the identity of the node belonging to the route and being closest to the given position.
     *
     * @param position the given position
     * @return the identity of the closest node
     */
    @Override
    public int nodeClosestTo(double position) {
        position = Math2.clamp(0, position, length());
        return 0;
    }

    /**
     * Returns the point on the route that is closest to the given reference point.
     *
     * @param point the given point
     * @return the closest point
     */
    @Override
    public RoutePoint pointClosestTo(PointCh point) {
        return null;
    }
}
