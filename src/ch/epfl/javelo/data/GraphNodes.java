package ch.epfl.javelo.data;


import java.nio.IntBuffer;

public record GraphNodes(IntBuffer buffer) {


    private static final int OFFSET_E = 0;
    private static final int OFFSET_N = OFFSET_E + 1;
    private static final int OFFSET_OUT_EDGES = OFFSET_N + 1;
    private static final int NODE_INTS = OFFSET_OUT_EDGES + 1;


    public int count() {
        return buffer.capacity() / 3 ;
    }

    public double nodeE(int nodeId) {
        return buffer.get(nodeId + OFFSET_E);
    }

    public double nodeN(int nodeId) {
        return buffer.get(nodeId + OFFSET_N);
    }

    public int outDegree(int nodeId) {
        return buffer.get(nodeId + OFFSET_OUT_EDGES) >> 28;
    }

    int edgeId(int nodeId, int edgeIndex) {
        return nodeId;
    }
}
