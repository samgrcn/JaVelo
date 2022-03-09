package ch.epfl.javelo.data;

import org.junit.Test;

import java.nio.IntBuffer;

import static org.junit.Assert.assertEquals;

public class GraphNodesTest {

    @Test
    public void countWorksOnNonTrivialArray() {
        IntBuffer buffer = IntBuffer.wrap(new int[]{});
        GraphNodes graph = new GraphNodes(buffer);
        double expected = 0.0;
        assertEquals(expected, graph.nodeN(0),0.1);
    }


    public static void main(String[] args) {

    }
}
