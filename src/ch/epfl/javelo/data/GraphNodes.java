package ch.epfl.javelo.data;


import ch.epfl.javelo.Bits;

import java.nio.IntBuffer;

import static ch.epfl.javelo.Q28_4.asDouble;

public record GraphNodes(IntBuffer buffer) {


    private static final int OFFSET_E = 0;
    private static final int OFFSET_N = OFFSET_E + 1;
    private static final int OFFSET_OUT_EDGES = OFFSET_N + 1;
    private static final int NUMBER_PER_NODE = 3;
    private static final int NUMBER_OF_EDGES = 4;
    private static final int FIRST_NODE_ID = 28;


    public int count() {
        return buffer.capacity() / NUMBER_PER_NODE ;
    }

    public double nodeE(int nodeId) {
        return asDouble(buffer.get(nodeId + OFFSET_E));
    }

    public double nodeN(int nodeId) {
        return asDouble(buffer.get(nodeId + OFFSET_N));
    }

    public int outDegree(int nodeId) {
        return Bits.extractSigned(nodeId + OFFSET_OUT_EDGES, FIRST_NODE_ID, NUMBER_OF_EDGES);
    }

    public int edgeId(int nodeId, int edgeIndex) {
        return Bits.extractSigned(nodeId + OFFSET_OUT_EDGES, 0, FIRST_NODE_ID) + edgeIndex;
    }
}
