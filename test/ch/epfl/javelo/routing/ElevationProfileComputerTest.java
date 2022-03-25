package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElevationProfileComputerTest {

    private static final int E = 2_600_000;
    private static final int N = 1_200_000;
    Route route0;
    Route route1;
    private static final double DELTA = 1e-7;

    ElevationProfileComputerTest() {
        PointCh point0 = new PointCh(E, N);
        PointCh point1 = new PointCh(E + 3, N + 4);
        PointCh point2 = new PointCh(E + 3, N + 6);
        PointCh point3 = new PointCh(E + 4.5, N + 8);


        Edge edge0 = new Edge(0, 1, point0, point1, 5,
                Functions.constant(5));
        Edge edge1 = new Edge(0, 1, point1, point2, 2,
                Functions.constant(Double.NaN));
        Edge edge2 = new Edge(0, 1, point2, point3, 2.5,
                Functions.constant(2));
        List<Edge> edges0 = new ArrayList<>();
        edges0.add(edge0);
        edges0.add(edge1);
        edges0.add(edge2);


        //route0 = new TestRoute(edges0);

        Edge edge3 = new Edge(0, 1, point0, point1, 5,
                Functions.constant(Double.NaN));
        Edge edge4 = new Edge(0, 1, point1, point2, 2,
                Functions.constant(Double.NaN));
        Edge edge5 = new Edge(0, 1, point2, point3, 2.5,
                Functions.constant(Double.NaN));
        List<Edge> edges1 = new ArrayList<>();
        edges1.add(edge3);
        edges1.add(edge4);
        edges1.add(edge5);

        //route1 = new TestRoute(edges1);
    }

    @Test
    void elevationProfileThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            ElevationProfileComputer.elevationProfile(route0, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ElevationProfileComputer.elevationProfile(route0, -0.05);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ElevationProfileComputer.elevationProfile(route0, -14343240);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ElevationProfileComputer.elevationProfile(route0, 0);
        });
    }

    @Test
    void elevationProfileWorksWithBasicValues() {
        ElevationProfile profile0 = ElevationProfileComputer.elevationProfile(route0, 1);

        assertEquals(5, profile0.elevationAt(0), DELTA);
        assertEquals(5, profile0.elevationAt(1), DELTA);
        assertEquals(5, profile0.elevationAt(2), DELTA);
        assertEquals(5, profile0.elevationAt(3), DELTA);
        assertEquals(5, profile0.elevationAt(4), DELTA);
        assertEquals(4.7368421052631575, profile0.elevationAt(5), DELTA);
        assertEquals(3.6842105263157894, profile0.elevationAt(6), DELTA);
        assertEquals(2.6315789473684212, profile0.elevationAt(7), DELTA);
        assertEquals(2, profile0.elevationAt(8), DELTA);
        assertEquals(2, profile0.elevationAt(9), DELTA);
        assertEquals(2, profile0.elevationAt(9.5), DELTA);
    }

    @Test
    void elevationProfileWorksWithFullNaN() {
        ElevationProfile profile0 = ElevationProfileComputer.elevationProfile(route1, 1);
        assertEquals(0, profile0.elevationAt(0), DELTA);
        assertEquals(0, profile0.elevationAt(1), DELTA);
        assertEquals(0, profile0.elevationAt(2), DELTA);
        assertEquals(0, profile0.elevationAt(3), DELTA);
        assertEquals(0, profile0.elevationAt(4), DELTA);
        assertEquals(0, profile0.elevationAt(5), DELTA);
        assertEquals(0, profile0.elevationAt(6), DELTA);
        assertEquals(0, profile0.elevationAt(7), DELTA);
        assertEquals(0, profile0.elevationAt(8), DELTA);
        assertEquals(0, profile0.elevationAt(9), DELTA);
        assertEquals(0, profile0.elevationAt(9.5), DELTA);
    }


}
