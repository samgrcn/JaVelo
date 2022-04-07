package ch.epfl.javelo.data;


import ch.epfl.javelo.Bits;

import java.nio.IntBuffer;

import ch.epfl.javelo.Q28_4;

/**
 * Allows representation of every node in an array, each represented by 3 integers.
 *
 * @author Samuel Garcin (345633)
 */

public record GraphNodes(IntBuffer buffer) {


    private static final int OFFSET_E = 0; //position of the E coordinates in the 3 integers of a node
    private static final int OFFSET_N = OFFSET_E + 1; //position of the N coordinates, = 1
    private static final int OFFSET_OUT_EDGES = OFFSET_N + 1; //position of the edge integer, = 2
    private static final int NUMBER_PER_NODE = OFFSET_OUT_EDGES + 1; //number of integers in a node, = 3

    private static final int NUMBER_OF_EDGES = 4; //amount of most significant bits, giving the number of outgoing edges
    private static final int NUMBER_OF_ID_BITS = Integer.SIZE - NUMBER_OF_EDGES; //number of bits dedicated to the id in the third integer, = 28

    /**
     * Counts the total amount of nodes.
     *
     * @return the amount of nodes in Switzerland
     */

    public int count() {
        return buffer.capacity() / NUMBER_PER_NODE;
    }

    /**
     * Among the 3 integers of a node, takes the first one, which represents the E coordinates.
     *
     * @param nodeId the node we want to know the E coordinates.
     * @return the integer of his E coordinates for the given node, in Q28_4 representation.
     */

    public double nodeE(int nodeId) {
        if (buffer.capacity() == 0) {
            return 0;
        } else {
            return Q28_4.asDouble(buffer.get(nodeId * NUMBER_PER_NODE + OFFSET_E));
        }
    }


    /**
     * Among the 3 integers of a node, takes the second one, which represents the N coordinates.
     *
     * @param nodeId the node we want to know the N coordinates.
     * @return the integer of his N coordinates for the given node, in Q28_4 representation.
     */

    public double nodeN(int nodeId) {
        if (buffer.capacity() == 0) {
            return 0;
        } else {
            return Q28_4.asDouble(buffer.get(nodeId * NUMBER_PER_NODE + OFFSET_N));
        }
    }


    /**
     * Among the 3 integers of a node, takes the third one and returns the amount of outgoing edges in this node.
     *
     * @param nodeId the node we want to know the outgoing edges.
     * @return the amount of outgoing edges of the given node.
     */

    public int outDegree(int nodeId) {
        return Bits.extractUnsigned(buffer.get(nodeId * NUMBER_PER_NODE + OFFSET_OUT_EDGES), NUMBER_OF_ID_BITS, NUMBER_OF_EDGES);
    }


    /**
     * Among the 3 integers of a node, takes the third and returns the id of the desired edge
     *
     * @param nodeId    the node we want to know the outgoing edges.
     * @param edgeIndex desired edge, from 0 to 15
     * @return the id of the desired edge.
     */

    public int edgeId(int nodeId, int edgeIndex) {
        assert 0 <= edgeIndex && edgeIndex < outDegree(nodeId);
        return Bits.extractUnsigned(buffer.get(nodeId * NUMBER_PER_NODE + OFFSET_OUT_EDGES), 0, NUMBER_OF_ID_BITS) + edgeIndex;
    }
}
