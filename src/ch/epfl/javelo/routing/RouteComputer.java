package ch.epfl.javelo.routing;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;

import java.util.ArrayList;
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
        List<Integer> pathNode = new ArrayList<>();
        for (int i = 0; i < graph.nodeCount(); i++) {
            distance[i] = Float.POSITIVE_INFINITY;
            predecessor[i] = 0;
        }
        distance[startNodeId] = 0;
        exploration.add(new WeightedNode(startNodeId, distance[startNodeId]));
        int n;
        while (!exploration.isEmpty()) {
            n = exploration.peek().nodeId;
            exploration.remove();
            distance[n] = Float.NEGATIVE_INFINITY;
        pathNode.add(n);
            if(n == endNodeId) {
                break;
            }
            int numberOfEdges = graph.nodeOutDegree(n);
            int n2;
            for (int i = 0; i < numberOfEdges; i++) {
                for (int j = graph.nodeOutEdgeId(n, 0); j < graph.nodeOutEdgeId(n, numberOfEdges); j++) {
                    double costFactor = costFunction.costFactor(n, j);
                    n2 = graph.edgeTargetNodeId(j);
                    float d = (float)((distance[n] + graph.edgeLength(j))*(costFactor));
                    if(d < distance[n2]) {
                        distance[n2] = d;
                        predecessor[n2] = n;
                        exploration.add(new WeightedNode(n2, distance[n2]));
                    }
                }
            }
        }

        List<Edge> routeEdgeList = new ArrayList<>();
        int currentNodeId = endNodeId;
        int previousNodeId = predecessor[endNodeId];
        int edgeId;
        while(currentNodeId != startNodeId) {
            for (int i = 0; i < graph.nodeOutDegree(previousNodeId); i++) {
                edgeId = graph.nodeOutEdgeId(previousNodeId, i);
                if (graph.edgeTargetNodeId(edgeId) == currentNodeId); {
                    routeEdgeList.add(new Edge(previousNodeId, currentNodeId, graph.nodePoint(previousNodeId), graph.nodePoint(currentNodeId), graph.edgeLength(edgeId), graph.edgeProfile(edgeId)));
                }
                currentNodeId = previousNodeId;
                previousNodeId = predecessor[currentNodeId];
            }

        }
        return new SingleRoute(routeEdgeList);
    }




}
