package ch.epfl.javelo.routing;

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
    private List<Edge> edges;

    public SingleRoute(List<Edge> edges) {
        Preconditions.checkArgument(!edges.isEmpty());
        this.edges = List.copyOf(edges);
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
        return edges; // immuabilité ??!
    }

    /**
     * Returns all the points located at the ends of the edges of the route.
     *
     * @return all the points
     */
    @Override
    public List<PointCh> points() {
        List<PointCh> res = new ArrayList<PointCh>() {
        };
        for (Edge edge : edges) {
            res.add(edge.fromPoint());
            res.add(edge.toPoint());
        }
        return res;
    }

    private int dichotomousSearch(double position) {
        double[] route = new double[edges.size()];
        route[0] = edges.get(0).length();
        for (int i = 1; i < edges.size(); i++) {
            route[i] = edges.get(i).length() + route[i - 1];
        }
        int res = Arrays.binarySearch(route, position);
        if (res >= 0) return res;
        return -(res + 1);
    }

    private double checkPosition(double position) {
        if (position < 0) position = 0;
        if (position > length()) position = length();
        return position;
    }

    /**
     * Returns the point at the given position along the route.
     *
     * @param position the given position
     * @return the point
     */
    @Override
    public PointCh pointAt(double position) {
        position = checkPosition(position);
        int index = dichotomousSearch(position);
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
        position = checkPosition(position);
        int index = dichotomousSearch(position);
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
        position = checkPosition(position);
        int index = dichotomousSearch(position);
        for (int i = 0; i < index; ++i) {
            position -= edges.get(i).length();
        }
        if (position < edges.get(index).length()) return edges.get(index).fromNodeId();
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
        return new RoutePoint(point, )
    }
}
