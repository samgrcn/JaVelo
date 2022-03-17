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
     * @param graph the graphe associated
     * @param edgeId the edge identity
     * @param fromNodeId the first node id
     * @param toNodeId the last node id
     * @return an instance of Edge
     */
    public static Edge of(Graph graph, int edgeId, int fromNodeId, int toNodeId) {
        return new Edge(fromNodeId, toNodeId, graph.nodePoint(fromNodeId), graph.nodePoint(toNodeId), graph.edgeLength(edgeId),graph.edgeProfile(edgeId));
    }

    /**
     * Returns the position along the edge, in metres, that is closest to the given point.
     * @param point the given point
     * @return the closest position
     */
    public double positionClosestTo(PointCh point) {
        return Math2.projectionLength(WebMercator.x(fromPoint.lon()), WebMercator.y(fromPoint.lat()), WebMercator.x(toPoint.lon()), WebMercator.y(toPoint.lat()), WebMercator.x(point.lon()), WebMercator.y(point.lat()));
    }

    /**
     * Returns the point at the given position on the edge, expressed in metres.
     * @param position the given position
     * @return the point at the given position
     */
    public PointCh pointAt(double position) {
        double ratio = position / length;
        double e = Math.abs(toPoint.e() - fromPoint.e()) * ratio + Math.min(toPoint.e(), fromPoint.e());
        double n = Math.abs(toPoint.n() - fromPoint.n()) * ratio + Math.min(toPoint.n(), fromPoint.n());
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
