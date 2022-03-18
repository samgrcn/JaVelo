package ch.epfl.javelo.data;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.projection.Ch1903;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.io.IOException;
import java.nio.LongBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class GraphTest {

    @Test
    void nodeClosestToWorks() throws IOException {
        Path basePath = Path.of("lausanne");
        Graph graph = Graph.loadFrom(basePath);
        double lat = Math.toRadians(46.518394);
        double lon = Math.toRadians(6.568469);
        double e= Ch1903.e(lon, lat);
        double n = Ch1903.n(lon, lat);
        int actual = graph.nodeClosestTo(new PointCh(e + 1000,n + 1000), 100000000);
        int expected = 143166;
        assertEquals(expected, actual);
    }

    @Test
    void test() throws IOException {
        Path filePath = Path.of("lausanne/nodes_osmid.bin");
        LongBuffer osmIdBuffer;
        try (FileChannel channel = FileChannel.open(filePath)) {
            osmIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asLongBuffer();
        }
        System.out.println(osmIdBuffer.get(143166));
    }

    @Test
    void attributeTest() throws IOException {
        Path basePath = Path.of("lausanne");
        Graph graph = Graph.loadFrom(basePath);
        AttributeSet actual = graph.edgeAttributes(2022);
        System.out.println(graph.edgeAttributes(graph.nodeOutEdgeId(2022,0)));
        System.out.println(actual);
    }

}