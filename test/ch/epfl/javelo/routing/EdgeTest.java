package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {
    @Test
    void positionClosestToTest() {
        PointCh point1 = new PointCh(2485000.0, 1075000.0);
        PointCh point2 = new PointCh(2485012.0, 1075000.00);
        Edge edge = new Edge(0, 1, point1, point2, 12, null);
        assertEquals(12, edge.positionClosestTo(point2));
        assertEquals(0, edge.positionClosestTo(point1));
    }

    @Test
    void positionClosestToTest2() {
        PointCh point1 = new PointCh(2485012.0, 1075000.00);
        PointCh point2 = new PointCh(2485000.0, 1075000.0);
        Edge edge = new Edge(0, 1, point1, point2, 12, null);
        assertEquals(12, edge.positionClosestTo(point2));
        assertEquals(0, edge.positionClosestTo(point1));
    }

    @Test
    void positionClosestToTest3() {
        PointCh point1 = new PointCh(2485000.0, 1075000.00);
        PointCh point2 = new PointCh(2485000.0, 1075000.0);
        Edge edge = new Edge(0, 1, point1, point2, 0, null);
        assertEquals(Double.NaN, edge.positionClosestTo(point2));
        assertEquals(Double.NaN, edge.positionClosestTo(point1));
    }

    @Test
    void pointAtTest() {
        PointCh point1 = new PointCh(2485000.0, 1075000.0);
        PointCh point2 = new PointCh(2485066.0, 1075000.00);
        PointCh expected = new PointCh(2485024.0, 1075000.0);
        Edge edge = new Edge(0, 1, point1, point2, 66, null);
        assertEquals(expected, edge.pointAt(24));
    }

    @Test
    void pointAtTest2() {
        PointCh point1 = new PointCh(2485066.0, 1075000.0);
        PointCh point2 = new PointCh(2485000.0, 1075000.00);
        PointCh expected = new PointCh(2485050.0, 1075000.0);
        Edge edge = new Edge(0, 1, point1, point2, 66, null);
        assertEquals(expected, edge.pointAt(16));
    }

    @Test
    void pointAtTest3() {
        PointCh point1 = new PointCh(2485000.0, 1075066.0);
        PointCh point2 = new PointCh(2485000.0, 1075000.00);
        PointCh expected = new PointCh(2485000.0, 1075050.0);
        Edge edge = new Edge(0, 1, point1, point2, 66, null);
        assertEquals(expected, edge.pointAt(16));
    }

    @Test
    void pointAtTest4() {
        PointCh point1 = new PointCh(2485000.0, 1075000.0);
        PointCh point2 = new PointCh(2485000.0, 1075066.00);
        PointCh expected = new PointCh(2485000.0, 1075024.0);
        Edge edge = new Edge(0, 1, point1, point2, 66, null);
        assertEquals(expected, edge.pointAt(24));
    }

    @Test
    void elevationAtTest() {
        ElevationProfile firstProfile = new ElevationProfile(9, new float[]{384.75f, 384.6875f, 384.5625f, 384.5f, 384.4375f, 384.375f, 384.3125f, 384.25f, 384.125f, 384.0625f});
        assertEquals(384.6875f, firstProfile.elevationAt(1));
        assertEquals(384.5f, firstProfile.elevationAt(3));
        assertEquals(384.0625f, firstProfile.elevationAt(9));
    }
}
