package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiRouteTest {
    @Test
    void indexOfSegmentAtTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 1000, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute, singleRoute));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute1));
        assertEquals(5, multiRoute.indexOfSegmentAt(5500));
    }
}
