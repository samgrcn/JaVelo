package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

public class ElevationProfileComputerTest {


    @Test
    void ElevationProfileTest() {
        float[] array = new float[] {Float.NaN, Float.NaN, 4, 2, 4, Float.NaN, 9};
        SingleRoute route = new SingleRoute(new Edge(0, 1, new PointCh(0, 0), new PointCh(1, 1), 5, Functions.constant(10)));
        ElevationProfileComputer.elevationProfile(, array);


    }
}
