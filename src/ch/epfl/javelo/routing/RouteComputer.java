package ch.epfl.javelo.routing;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;

import java.util.ArrayList;
import java.util.List;

public class RouteComputer {

    private final Graph graph;
    private final CostFunction costFunction;

    public RouteComputer(Graph graph, CostFunction costFunction) {
        this.graph = graph;
        this.costFunction = costFunction;
    }

    public Route bestRouteBetween(int startNodeId, int endNodeId) {
        Preconditions.checkArgument(startNodeId != endNodeId);
        double[] distance = new double[graph.nodeCount()];
        List<Integer> exploration = new ArrayList<>();
        for (int i = 0; i < graph.nodeCount(); i++) {
            distance[i] = Float.POSITIVE_INFINITY;
        }
        distance[startNodeId] = 0;
        exploration.add(startNodeId);
        int n = exploration.get(0);
        while (!exploration.isEmpty()) {
            for (Integer i : exploration) {
                if (distance[n] < distance[i]) {
                    n = i;
                }
            }
            exploration.remove(n);
            if(n == endNodeId) {
                break;
            }
            int numberOfEdges = graph.nodeOutDegree(n);
            int n2;
            for (int i = 0; i < numberOfEdges; i++) {
                for (int j = graph.nodeOutEdgeId(n, 0); j < graph.nodeOutEdgeId(n, numberOfEdges); j++) {
                    n2 = graph.edgeTargetNodeId(j);
                    double d = distance[n] + graph.edgeLength(j);
                    if(d < distance[n2]) {
                        distance[n2] = d;
                        exploration.add(n2);
                    }
                }
            }
        }
    }




}
