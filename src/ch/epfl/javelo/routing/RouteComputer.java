package ch.epfl.javelo.routing;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class RouteComputer {

    private final Graph graph;
    private final CostFunction costFunction;

    public RouteComputer(Graph graph, CostFunction costFunction) {
        this.graph = graph;
        this.costFunction = costFunction;
    }

    public Route bestRouteBetween(int startNodeId, int endNodeId) {

        record WeightedNode(int nodeId, float distance)
                implements Comparable<WeightedNode> {
            @Override
            public int compareTo(WeightedNode that) {
                return Float.compare(this.distance, that.distance);
            }
        }

        Preconditions.checkArgument(startNodeId != endNodeId);
        float[] distance = new float[graph.nodeCount()];
        int[] predecessor = new int[graph.nodeCount()];
        PriorityQueue<WeightedNode> exploration = new PriorityQueue<>();
        for (int i = 0; i < graph.nodeCount(); i++) {
            distance[i] = Float.POSITIVE_INFINITY;
        }
        distance[startNodeId] = 0;
        exploration.add(new WeightedNode(startNodeId, distance[startNodeId]));
        int n;
        while (!exploration.isEmpty()) {

            n = exploration.remove().nodeId;

            if (n == endNodeId) {
                break;
            } else {
                int numberOfEdges = graph.nodeOutDegree(n);
                int n2;
                for (int i = 0; i < numberOfEdges; i++) {
                        double costFactor = costFunction.costFactor(n, graph.nodeOutEdgeId(n, i));
                        n2 = graph.edgeTargetNodeId(graph.nodeOutEdgeId(n, i));
                        float d = (float) ((distance[n] + (graph.edgeLength(graph.nodeOutEdgeId(n, i)) * costFactor)));
                        if (d < distance[n2] && distance[n] != Float.NEGATIVE_INFINITY) {
                            distance[n2] = d;
                            predecessor[n2] = n;
                            exploration.add(new WeightedNode(n2, distance[n2]));

                    }
                }
                distance[n] = Float.NEGATIVE_INFINITY;
            }
        }

        List<Edge> routeEdgeList = new ArrayList<>();
        int currentNodeId = endNodeId;
        int previousNodeId = predecessor[endNodeId];
        int edgeId;
        while(currentNodeId != startNodeId) {
            for (int i = 0; i < graph.nodeOutDegree(previousNodeId); i++) {
                edgeId = graph.nodeOutEdgeId(previousNodeId, i);
                if (graph.edgeTargetNodeId(edgeId) == currentNodeId) {
                    routeEdgeList.add(Edge.of(graph, edgeId, previousNodeId, currentNodeId));
                }
            }
            currentNodeId = previousNodeId;
            previousNodeId = predecessor[currentNodeId];

        }
        return new SingleRoute(routeEdgeList);
    }




}
