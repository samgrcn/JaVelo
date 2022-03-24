package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SingleRouteTest {
    @Test
    void lengthTest() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        assertEquals(90, singleRoute.length());
    }

    @Test
    void pointAt() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        assertEquals(new PointCh(2485003.0, 1075000.0), singleRoute.pointAt(3));
        assertEquals(new PointCh(2485023.0, 1075000.0), singleRoute.pointAt(23));
        assertEquals(new PointCh(2485046.0, 1075000.0), singleRoute.pointAt(46));
        assertEquals(new PointCh(2485000.0, 1075000.0), singleRoute.pointAt(0));
        assertEquals(new PointCh(2485090.0, 1075000.0), singleRoute.pointAt(90));
        assertEquals(new PointCh(2485090.0, 1075000.0), singleRoute.pointAt(98));
        assertEquals(new PointCh(2485000.0, 1075000.0), singleRoute.pointAt(-23));
    }

    @Test
    void nodeClosestToTest() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(1, 2, new PointCh(2485020.0, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, null));
        edges.add(new Edge(2, 3, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        assertEquals(1, singleRoute.nodeClosestTo(17));
        assertEquals(0, singleRoute.nodeClosestTo(7));
        assertEquals(3, singleRoute.nodeClosestTo(54));
        assertEquals(3, singleRoute.nodeClosestTo(86));
        assertEquals(0, singleRoute.nodeClosestTo(-4));
    }

    @Test
    void pointClosestToTest() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        PointCh point = new PointCh(2485040.0, 1075000.0);
        PointCh point2 = new PointCh(2485026.0, 1075034.0);
        assertEquals(new RoutePoint(point, 40, 0), singleRoute.pointClosestTo(point));
        assertEquals(new RoutePoint(new PointCh(2485026.0, 1075000.0), 26, 34), singleRoute.pointClosestTo(point2));
    }
}
