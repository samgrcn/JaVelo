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
        int N = exploration.get(0);
        while (!exploration.isEmpty()) {
            for (int i = 0; i < exploration.size(); i++) {
                if(distance[N] < distance[exploration.get(i)]) {
                    N = exploration.get(i);
                }
            }
            exploration.remove(N);
            if(N == endNodeId) {
                break;
            }
            for
        }
    }




}
