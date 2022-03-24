package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a simple route, i.e. from a starting point to an ending point, with no intermediate points.
 *
 * @author Quentin Chappuis (339517)
 */
public final class SingleRoute implements Route {
    private final List<Edge> edges;
    private final double[] route;

    public SingleRoute(List<Edge> edges) {
        Preconditions.checkArgument(!edges.isEmpty());
        this.edges = edges;
        route = new double[edges.size()];
        route[0] = 0;
        for (int i = 1; i < edges.size(); i++) {
            route[i] = edges.get(i - 1).length() + route[i - 1];
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
        double length = 0.0;
        for (Edge edge : edges) length += edge.length();
        return length;
    }

    /**
     * Returns all the edges of the route.
     *
     * @return all the edges
     */
    @Override
    public List<Edge> edges() {
        return new ArrayList<>(edges);
    }

    /**
     * Returns all the points located at the ends of the edges of the route.
     *
     * @return all the points
     */
    @Override
    public List<PointCh> points() {
        List<PointCh> res = new ArrayList<PointCh>();
        res.add(edges.get(0).fromPoint());
        for (Edge edge : edges) res.add(edge.toPoint());
        return res;
    }

    /**
     *
     *
     * @param position the given position
     * @return the index
     */
    private int dichotomousSearch(double position) {
        int res = Arrays.binarySearch(route, position);
        return res < 0 ? -(res + 2) : res;
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
        int index = dichotomousSearch(position);
        for (int i = 0; i < index; i++) position -= edges.get(i).length();
        return edges.get(index).pointAt(position);
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
        int index = dichotomousSearch(position);
        for (int i = 0; i < index; i++) position -= edges.get(i).length();
        return edges.get(index).elevationAt(position);
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
        int index = dichotomousSearch(position);
        for (int i = 0; i < index; ++i) position -= edges.get(i).length();
        if (position < edges.get(index).length()/2) return edges.get(index).fromNodeId();
        return edges.get(index).toNodeId();
    }

    /**
     * Returns the point on the route that is closest to the given reference point.
     *
     * @param point the given point
     * @return the closest point
     */
    @Override
    public RoutePoint pointClosestTo(PointCh point) {
        List<PointCh> points = new ArrayList<PointCh>();
        for (Edge edge : edges) {
            double position = edge.positionClosestTo(point);
            points.add(edge.pointAt(position));
        }
        double distanceToReference = points.get(0).distanceTo(point);
        int index = 0;
        for (int i = 1; i < points.size(); ++i) {
            if (points.get(i).distanceTo(point) < distanceToReference) {
                distanceToReference = points.get(i).distanceTo(point);
                index = i;
            }
        }
        return new RoutePoint(points.get(index), edges.get(index).fromPoint().distanceTo(points.get(index)), distanceToReference);
    }
}
