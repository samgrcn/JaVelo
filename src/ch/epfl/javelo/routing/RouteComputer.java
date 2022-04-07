package ch.epfl.javelo.routing;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;

import java.util.*;

public class RouteComputer {

    private final Graph graph;
    private final CostFunction costFunction;


    public RouteComputer(Graph graph, CostFunction costFunction) {
        this.graph = graph;
        this.costFunction = costFunction;
    }

    record WeightedNode(int nodeId, float distance)
            implements Comparable<WeightedNode> {
        @Override
        public int compareTo(WeightedNode that) {
            return Float.compare(this.distance, that.distance);
        }
    }


    private void explorationFiller(int n, int endNodeId, float[] distance, int[] predecessor, PriorityQueue<WeightedNode> exploration) {

        int numberOfEdges = graph.nodeOutDegree(n);
        for (int i = 0; i < numberOfEdges; i++) {
            int edgeThId = graph.nodeOutEdgeId(n, i);
            double costFactor = costFunction.costFactor(n, edgeThId);
            int n2 = graph.edgeTargetNodeId(edgeThId);
            float d = ((distance[n] + (float) (graph.edgeLength(edgeThId) * costFactor)));
            if (d < distance[n2]) {
                distance[n2] = d;
                predecessor[n2] = n;
                exploration.add(new WeightedNode(n2, (float) (distance[n2] + graph.nodePoint(n2).distanceTo(graph.nodePoint(endNodeId)))));

            }
        }
    }


    public Route bestRouteBetween(int startNodeId, int endNodeId) {


        Preconditions.checkArgument(startNodeId != endNodeId);

        int n;

        float[] distance = new float[graph.nodeCount()];
        int[] predecessor = new int[graph.nodeCount()];

        List<Edge> routeEdgeList = new ArrayList<>();
        PriorityQueue<WeightedNode> exploration = new PriorityQueue<>();

        for (int i = 0; i < graph.nodeCount(); i++) {
            distance[i] = Float.POSITIVE_INFINITY;
        }
        distance[startNodeId] = 0;
        exploration.add(new WeightedNode(startNodeId, distance[startNodeId]));


        while (!exploration.isEmpty()) {

            do {
                if (exploration.isEmpty()) {
                    return null;
                }
                n = exploration.remove().nodeId;

            } while (distance[n] == Float.NEGATIVE_INFINITY);


            if (n == endNodeId) {

                int currentNodeId = endNodeId;
                int previousNodeId = predecessor[endNodeId];
                int edgeId;
                while (currentNodeId != startNodeId) {
                    for (int i = 0; i < graph.nodeOutDegree(previousNodeId); i++) {
                        edgeId = graph.nodeOutEdgeId(previousNodeId, i);
                        if (graph.edgeTargetNodeId(edgeId) == currentNodeId) {
                            routeEdgeList.add(Edge.of(graph, edgeId, previousNodeId, currentNodeId));
                            break;
                        }
                    }

                    currentNodeId = previousNodeId;
                    previousNodeId = predecessor[currentNodeId];

                }
                Collections.reverse(routeEdgeList);
                return new SingleRoute(routeEdgeList);
            }

            explorationFiller(n, endNodeId, distance, predecessor, exploration);

            distance[n] = Float.NEGATIVE_INFINITY;
        }
        return null;
    }
}
