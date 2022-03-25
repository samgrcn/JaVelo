package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
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
    void lengthTest2(){
        List<Edge> edges = new ArrayList<>();
        List<Edge> edgesVoid = new ArrayList<>();
        List<Edge> edgesLotsVoid = new ArrayList<>();
        Edge edgeVoid = new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 0, null);
        edgesVoid.add(edgeVoid);
        edges.add(edgeVoid);
        for (int i = 0; i < 100; i++) {
            edgesLotsVoid.add(edgeVoid);
        }
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        SingleRoute singleRouteVoid = new SingleRoute(edgesVoid);
        SingleRoute singleRouteLotsVoid = new SingleRoute(edgesLotsVoid);
        assertEquals(70,singleRoute.length());
        assertEquals(0,singleRouteVoid.length());
        assertEquals(0,singleRouteLotsVoid.length());
    }

    @Test
    void pointAtTest() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        assertEquals(new PointCh(2485003.0, 1075000.0), singleRoute.pointAt(3));
        assertEquals(new PointCh(2485050.0, 1075000.0), singleRoute.pointAt(50));
        assertEquals(new PointCh(2485020.0, 1075000.0), singleRoute.pointAt(20));
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
        PointCh point3 = new PointCh(2485134.0, 1075057.0);
        assertEquals(new RoutePoint(point, 40, 0), singleRoute.pointClosestTo(point));
        assertEquals(new RoutePoint(new PointCh(2485026.0, 1075000.0), 26, 34), singleRoute.pointClosestTo(point2));
        assertEquals(new RoutePoint(new PointCh(2485060.0, 1075000.0), 60, 93.40770846134703), singleRoute.pointClosestTo(point3));
    }

    @Test
    void conditionTest(){
        List<Edge> edges = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new SingleRoute(edges) ; });
    }

    @Test
    void edgesTest(){
        List<Edge> edges = new ArrayList<>();
        List<Edge> edgesVoid = new ArrayList<>();
        List<Edge> edgesLotsVoid = new ArrayList<>();
        Edge edgeVoid = new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 0, null);
        edgesVoid.add(edgeVoid);
        edges.add(edgeVoid);
        for (int i = 0; i < 100; i++) {
            edgesLotsVoid.add(edgeVoid);
        }
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        SingleRoute singleRouteVoid = new SingleRoute(edgesVoid);
        SingleRoute singleRouteLotsVoid = new SingleRoute(edgesLotsVoid);
        assertEquals(edges,singleRoute.edges());
        assertEquals(edgesVoid,singleRouteVoid.edges());
        assertEquals(edgesLotsVoid,singleRouteLotsVoid.edges());

    }

    @Test
    void pointsTest(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        List<PointCh> listPointCh = new ArrayList<>();
        listPointCh.add(new PointCh(2485000.0, 1075000.0));
        listPointCh.add(new PointCh(2485020.0, 1075000.0));
        listPointCh.add(new PointCh(2485050.0, 1075000.0));
        listPointCh.add(new PointCh(2485090.0, 1075000.0));
        assertEquals(listPointCh,singleRoute.points());
    }


    @Test
    void pointAtTest2(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, null));
        edges.add(new Edge(0, 1, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        PointCh pointChO = new PointCh(2485000.0, 1075000.0);
        PointCh pointChExtremite = new PointCh(2485090.0, 1075000.0);
        PointCh pointChMilieurandom = new PointCh(2485064.0, 1075000.0);
        PointCh pointChMilieuToutpile = new PointCh(2485050.0, 1075000.0);
        assertEquals(pointChO,singleRoute.pointAt(0));
        assertEquals(pointChO,singleRoute.pointAt(-10));

        assertEquals(pointChExtremite,singleRoute.pointAt(90));
        assertEquals(pointChExtremite,singleRoute.pointAt(150));

        assertEquals(pointChMilieurandom,singleRoute.pointAt(64));
        assertEquals(pointChMilieuToutpile,singleRoute.pointAt(50));

    }

    @Test
    void elevationAtTest(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, Functions.constant(10)));
        edges.add(new Edge(1, 2, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, Functions.constant(20)));
        edges.add(new Edge(2, 3, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, Functions.constant(30)));
        SingleRoute singleRoute = new SingleRoute(edges);
        assertEquals(10,singleRoute.elevationAt(0));
        assertEquals(10,singleRoute.elevationAt(-100));
        assertEquals(20,singleRoute.elevationAt(30));
        assertEquals(30,singleRoute.elevationAt(90));
        assertEquals(30,singleRoute.elevationAt(150));
    }

    @Test
    void nodeClosestToTest2(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, Functions.constant(10)));
        edges.add(new Edge(1, 2, new PointCh(2485020.0, 1075000.0), new PointCh(2485050.0, 1075000.0), 30, Functions.constant(20)));
        edges.add(new Edge(2, 3, new PointCh(2485050.0, 1075000.0), new PointCh(2485090.0, 1075000.0), 40, Functions.constant(30)));
        SingleRoute singleRoute = new SingleRoute(edges);

        assertEquals(0,singleRoute.nodeClosestTo(-400));
        assertEquals(0,singleRoute.nodeClosestTo(5));
        assertEquals(0,singleRoute.nodeClosestTo(0));
        assertEquals(1,singleRoute.nodeClosestTo(17));
        assertEquals(2,singleRoute.nodeClosestTo(43.3647));
        assertEquals(3,singleRoute.nodeClosestTo(90));
        assertEquals(3,singleRoute.nodeClosestTo(1200));
    }

    @Test
    void pointClosestToTest2(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485120.0, 1075000.0), new PointCh(2485140.0, 1075000.0), 20, null));
        edges.add(new Edge(0, 1, new PointCh(2485140.0, 1075000.0), new PointCh(2485160.0, 1075000.0), 20, null));
        SingleRoute singleRoute = new SingleRoute(edges);
        PointCh point = new PointCh(2485140.0, 1075000.0);
        PointCh point2 = new PointCh(2485126.0, 1075034.0);

        PointCh pointExtremeGauche = new PointCh(2485000.0, 1075000.0);
        RoutePoint routePointExtremeGauche = new RoutePoint(new PointCh(2485100.0, 1075000.0),0,100);

        PointCh pointExtremeDroite = new PointCh(2485200.0, 1075000.0);
        RoutePoint routePointExtremeDroite = new RoutePoint(new PointCh(2485160.0, 1075000.0),60,40);


        assertEquals(new RoutePoint(point, 40, 0), singleRoute.pointClosestTo(point));
        assertEquals(new RoutePoint(new PointCh(2485126.0, 1075000.0), 26, 34), singleRoute.pointClosestTo(point2));
        assertEquals(routePointExtremeGauche,singleRoute.pointClosestTo(pointExtremeGauche));
        assertEquals(routePointExtremeDroite,singleRoute.pointClosestTo(pointExtremeDroite));

    }
}
