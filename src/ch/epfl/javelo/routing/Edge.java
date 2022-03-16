package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.WebMercator;

import java.util.function.DoubleUnaryOperator;

public record Edge(int fromNodeId, int toNodeId, PointCh fromPoint, PointCh toPoint, double length, DoubleUnaryOperator profile) {

    public static Edge of(Graph graph, int edgeId, int fromNodeId, int toNodeId) {
        return new Edge(fromNodeId, toNodeId, graph.nodePoint(fromNodeId), graph.nodePoint(toNodeId), graph.edgeLength(edgeId),graph.edgeProfile(edgeId));
    }

    public double positionClosestTo(PointCh point) {
        return Math2.projectionLength(WebMercator.x(fromPoint.lon()), WebMercator.y(fromPoint.lat()), WebMercator.x(toPoint.lon()), WebMercator.y(toPoint.lat()), WebMercator.x(point.lon()), WebMercator.y(point.lat()));
    }

    public PointCh pointAt(double position) {
        double ratio = position / length;
        double e = Math.abs(toPoint.e() - fromPoint.e()) * ratio + Math.min(toPoint.e(), fromPoint.e());
        double n = Math.abs(toPoint.n() - fromPoint.n()) * ratio + Math.min(toPoint.n(), fromPoint.n());
        return new PointCh(e, n);
    }

    public double elevationAt(double position) {
        Functions.sampled(profile, ).applyAsDouble()
    }
}
