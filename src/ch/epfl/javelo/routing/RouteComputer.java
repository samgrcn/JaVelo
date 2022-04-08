package ch.epfl.javelo.routing;

import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;

import java.util.*;

/**
 * RouteComputer builds a route planner for the given graph and cost function.
 *
 * @author Samuel Garcin (345633)
 */

public class RouteComputer {


    private final Graph graph;
    private final CostFunction costFunction;


    public RouteComputer(Graph graph, CostFunction costFunction) {
        this.graph = graph;
        this.costFunction = costFunction;
    }

    /**
     * Record used to store in the list exploration a WeightedNode, including an id of the node and its distance.
     */

    record WeightedNode(int nodeId, float distance)
            implements Comparable<WeightedNode> {

        @Override
        public int compareTo(WeightedNode that) {
            return Float.compare(this.distance, that.distance);
        }
    }

    /**
     * Private method adding a WeightedNode of the closest node to the node n to the exploration list.
     *
     * @param n           the node n
     * @param endNodeId   endNodeId
     * @param distance    array of distance
     * @param predecessor array of predecessor (id of the node before the index in the array, which is also its closest)
     * @param exploration list of WeightedNode
     */


    private void explorationFiller(int n, int endNodeId, float[] distance, int[] predecessor, PriorityQueue<WeightedNode> exploration) {

        for (int i = 0; i < graph.nodeOutDegree(n); i++) {
            int edgeId = graph.nodeOutEdgeId(n, i);
            double costFactor = costFunction.costFactor(n, edgeId);
            int n2 = graph.edgeTargetNodeId(edgeId);
            float d = ((distance[n] + (float) (graph.edgeLength(edgeId) * costFactor)));
            if (d < distance[n2]) {
                distance[n2] = d;
                predecessor[n2] = n;
                exploration.add(
                        new WeightedNode(
                                n2,
                                (float) (distance[n2] + graph.nodePoint(n2).distanceTo(graph.nodePoint(endNodeId)))));

            }
        }
    }

    /**
     * Private method which creates a list of Edge connecting the nodes of the array predecessor (a node and its closest).
     *
     * @param startNodeId   first node of the route
     * @param endNodeId     last node of the route
     * @param predecessor   array of predecessor (id of the node before the index in the array, which is also its closest)
     * @param routeEdgeList the list of Edge we want to return
     */

    private void endNodeReached(int startNodeId, int endNodeId, int[] predecessor, List<Edge> routeEdgeList) {
        int currentNodeId = endNodeId;
        int previousNodeId = predecessor[endNodeId];
        int edgeId;
        while (currentNodeId != startNodeId) {
            for (int i = 0; i < graph.nodeOutDegree(previousNodeId); i++) {
                edgeId = graph.nodeOutEdgeId(previousNodeId, i);
                if (graph.edgeTargetNodeId(edgeId) == currentNodeId) {
                    routeEdgeList.add(
                            Edge.of(graph, edgeId, previousNodeId, currentNodeId));
                    break;
                }
            }

            currentNodeId = previousNodeId;
            previousNodeId = predecessor[currentNodeId];

        }
        Collections.reverse(routeEdgeList);
    }

    /**
     * Gives the minimum total cost route from the startNodeId to the endNodeId in the
     * graph passed to the constructor.
     *
     * @param startNodeId first node of the route
     * @param endNodeId   last node of the route
     * @return the minimum total cost route from the startNodeId to the endNodeId in the graph passed to the constructor,
     * or null if no route exists
     * @throws IllegalArgumentException if the start and end nodes are identical
     */

    public Route bestRouteBetween(int startNodeId, int endNodeId) {

        Preconditions.checkArgument(startNodeId != endNodeId);

        int n;
        int nodeNumber = graph.nodeCount();

        float[] distance = new float[nodeNumber];
        int[] predecessor = new int[nodeNumber];

        List<Edge> routeEdgeList = new ArrayList<>();
        PriorityQueue<WeightedNode> exploration = new PriorityQueue<>();

        //we set the distance of each element at positive infinity
        for (int i = 0; i < nodeNumber; i++) {
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
                endNodeReached(startNodeId, endNodeId, predecessor, routeEdgeList);
                return new SingleRoute(routeEdgeList);
            }

            explorationFiller(n, endNodeId, distance, predecessor, exploration);

            distance[n] = Float.NEGATIVE_INFINITY;
        }
        return null; //if there's no more node to explore, and it has not reached the last node.
    }
}
