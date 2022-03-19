package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.WebMercator;

import java.util.function.DoubleUnaryOperator;

/**
 * Represents an edge of a route.
 *
 * @author Quentin Chappuis (339517)
 */
public record Edge(int fromNodeId, int toNodeId, PointCh fromPoint, PointCh toPoint, double length, DoubleUnaryOperator profile) {

    /**
     * Returns an instance of Edge whose attributes fromNodeId and toNodeId are those given, the others being those of the identity edgeId in the graph Graph.
     * @param graph the graph associated
     * @param edgeId the edge identity
     * @param fromNodeId the first node id
     * @param toNodeId the last node id
     * @return an instance of Edge
     */
    public static Edge of(Graph graph, int edgeId, int fromNodeId, int toNodeId) {
        return new Edge(fromNodeId, toNodeId, graph.nodePoint(fromNodeId), graph.nodePoint(toNodeId), graph.edgeLength(edgeId), graph.edgeProfile(edgeId));
    }

    /**
     * Returns the position along the edge, in metres, that is closest to the given point.
     * @param point the given point
     * @return the closest position
     */
    public double positionClosestTo(PointCh point) {
        return Math2.projectionLength(fromPoint.e(), fromPoint.n(), toPoint.e(), toPoint.n(), point.e(), point.n());
    }

    /**
     * Returns the point at the given position on the edge, expressed in metres.
     * @param position the given position
     * @return the point at the given position
     */
    public PointCh pointAt(double position) {
        double ratio = position / length;
        double e = Math2.interpolate(fromPoint.e(), toPoint.e(), ratio);
        double n = Math2.interpolate(fromPoint.n(), toPoint.n(), ratio);
        return new PointCh(e, n);
    }

    /**
     * Returns the elevation, in metres, at the given position on the edge.
     * @param position the given position
     * @return the elevation
     */
    public double elevationAt(double position) {
        return profile.applyAsDouble(position);
    }
}
