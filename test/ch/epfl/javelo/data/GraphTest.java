package ch.epfl.javelo.data;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

public class GraphTest {

    @Test
    void nodeClosestToWorks() throws IOException {
        Path basePath = Path.of("lausanne");
        Graph myGraph = Graph.loadFrom(basePath);
        int actual = myGraph.nodeOutEdgeId(10, 0);
        int expected = myGraph.nodeOutDegree(10);
        System.out.println(actual);
        System.out.println(expected);
    }
}