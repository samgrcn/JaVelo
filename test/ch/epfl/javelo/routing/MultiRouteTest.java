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
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2486000, 1075000.0), new PointCh(2487000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 1000, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2));
        assertEquals(5, multiRoute.indexOfSegmentAt(5500));
    }

    @Test
    void indexOfSegmentAtTest2() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2486000, 1075000.0), new PointCh(2487000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute7 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2491000.0, 1075000.0), new PointCh(2492000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute8 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2492000.0, 1075000.0), new PointCh(2493000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute9 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2493000.0, 1075000.0), new PointCh(2494000.0, 1075000.0), 1000, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute3 = new MultiRoute(Arrays.asList(singleRoute7, singleRoute8, singleRoute9));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2, multiRoute3));
        assertEquals(8, multiRoute.indexOfSegmentAt(8500));
        assertEquals(7, multiRoute.indexOfSegmentAt(7890));
        assertEquals(1, multiRoute.indexOfSegmentAt(1500));
        assertEquals(8, multiRoute.indexOfSegmentAt(8500));
        assertEquals(8, multiRoute.indexOfSegmentAt(10500));
        assertEquals(0, multiRoute.indexOfSegmentAt(-8500));
        assertEquals(3, multiRoute.indexOfSegmentAt(4000));
        assertEquals(8, multiRoute.indexOfSegmentAt(9000));
    }

    @Test
    void indexOfSegmentAtTest3() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();
        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2491000.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2491500.0, 1075000.0), new PointCh(2492000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);
        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);

        MultiRoute multiRoute2 = new MultiRoute(route2);

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(5,twoMultiroutes.indexOfSegmentAt(5500));
        assertEquals(0,twoMultiroutes.indexOfSegmentAt(0));
        assertEquals(0,twoMultiroutes.indexOfSegmentAt(-1000));
        assertEquals(5,twoMultiroutes.indexOfSegmentAt(6000));
        assertEquals(5,twoMultiroutes.indexOfSegmentAt(100000));

    }

    @Test
    void indexOfSegmentAtSimpleTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2486000, 1075000.0), new PointCh(2487000.0, 1075000.0), 1000, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 1000, null)));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        assertEquals(1, multiRoute.indexOfSegmentAt(1400));
        assertEquals(2, multiRoute.indexOfSegmentAt(2400));
        assertEquals(2, multiRoute.indexOfSegmentAt(3000));
        assertEquals(0, multiRoute.indexOfSegmentAt(-23));
    }

    @Test
    void lengthTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 1000, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute, singleRoute));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute1));
        assertEquals(6000, multiRoute.length());
    }

    @Test
    void lengthTest2() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();
        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);
        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(6000,twoMultiroutes.length());
    }

    @Test
    void pointAtTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null)));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485020, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, null)));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485060.0, 1075000.0), new PointCh(2485080.0, 1075000.0), 20, null)));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485080.0, 1075000.0), new PointCh(2485100.0, 1075000.0), 20, null)));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0), 20, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2));
        assertEquals(new PointCh(2485060.0, 1075000.0), multiRoute.pointAt(60));
        assertEquals(new PointCh(2485120.0, 1075000.0), multiRoute.pointAt(7890));
        assertEquals(new PointCh(2485000.0, 1075000.0), multiRoute.pointAt(-40));
        assertEquals(new PointCh(2485120.0, 1075000.0), multiRoute.pointAt(120));
    }

    @Test
    void elevationAtTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, Functions.constant(10))));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485020, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, Functions.constant(10))));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, Functions.constant(10))));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485060.0, 1075000.0), new PointCh(2485080.0, 1075000.0), 20, Functions.constant(20))));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485080.0, 1075000.0), new PointCh(2485100.0, 1075000.0), 20, Functions.constant(30))));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0), 20, Functions.constant(100))));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2));
        assertEquals(10, multiRoute.elevationAt(-60));
        assertEquals(10, multiRoute.elevationAt(0));
        assertEquals(100, multiRoute.elevationAt(120));
        assertEquals(30, multiRoute.elevationAt(84));
    }

    @Test
    void pointsTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null)));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485020, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, null)));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485060.0, 1075000.0), new PointCh(2485080.0, 1075000.0), 20, null)));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485080.0, 1075000.0), new PointCh(2485100.0, 1075000.0), 20, null)));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0), 20, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2));
        List<PointCh> expected = Arrays.asList(new PointCh(2485000.0, 1075000.0), new PointCh(2485020, 1075000.0), new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0)
        , new PointCh(2485080.0, 1075000.0), new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0));
        assertEquals(expected, multiRoute.points());
    }


    @Test
    void indexOfSegmentAt() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();
        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2491000.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2491500.0, 1075000.0), new PointCh(2492000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);
        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);

        MultiRoute multiRoute2 = new MultiRoute(route2);

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(5,twoMultiroutes.indexOfSegmentAt(5500));

    }

    @Test
    void length() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();
        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);
        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(6000,twoMultiroutes.length());
    }

    @Test
    void edges() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();

        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);

        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        List<Edge> allEdges1 = new ArrayList<>();
        allEdges1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        allEdges1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        allEdges1.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        allEdges1.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        allEdges1.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        allEdges1.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));


        List<Edge> allEdges2 = new ArrayList<>();
        allEdges2.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        allEdges2.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        allEdges2.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        allEdges2.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        allEdges2.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        allEdges2.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));

        List<Edge> allEdgesTwoRoutes = new ArrayList<>();
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        allEdgesTwoRoutes.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));

        assertEquals(allEdgesTwoRoutes,twoMultiroutes.edges());

        assertEquals(allEdges1,multiRoute1.edges());
        assertEquals(allEdges2,multiRoute2.edges());
    }

    @Test
    void points() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();

        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);

        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        List<PointCh> listOfAllPointsTwoRoutes = new ArrayList<>();

        listOfAllPointsTwoRoutes.add( new PointCh(2485000.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2485500.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2486000.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2486500.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2487000.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2487500.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2488000.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2488500.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2489000.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2489500.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2490000.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2490500.0, 1075000.0));
        listOfAllPointsTwoRoutes.add( new PointCh(2491000.0, 1075000.0));

        List<PointCh> listOfAllPoints1 = new ArrayList<>();
        listOfAllPoints1.add( new PointCh(2485000.0, 1075000.0));
        listOfAllPoints1.add( new PointCh(2485500.0, 1075000.0));
        listOfAllPoints1.add( new PointCh(2486000.0, 1075000.0));
        listOfAllPoints1.add( new PointCh(2486500.0, 1075000.0));
        listOfAllPoints1.add( new PointCh(2487000.0, 1075000.0));
        listOfAllPoints1.add( new PointCh(2487500.0, 1075000.0));
        listOfAllPoints1.add( new PointCh(2488000.0, 1075000.0));

        List<PointCh> listOfAllPoints2 = new ArrayList<>();
        listOfAllPoints2 .add( new PointCh(2488000.0, 1075000.0));
        listOfAllPoints2 .add( new PointCh(2488500.0, 1075000.0));
        listOfAllPoints2 .add( new PointCh(2489000.0, 1075000.0));
        listOfAllPoints2 .add( new PointCh(2489500.0, 1075000.0));
        listOfAllPoints2 .add( new PointCh(2490000.0, 1075000.0));
        listOfAllPoints2 .add( new PointCh(2490500.0, 1075000.0));
        listOfAllPoints2 .add( new PointCh(2491000.0, 1075000.0));


        assertEquals(listOfAllPoints1,multiRoute1.points());
        assertEquals(listOfAllPoints2,multiRoute2.points());

        assertEquals(listOfAllPointsTwoRoutes,twoMultiroutes.points());

    }

    @Test
    void pointAt() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();

        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);

        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(new PointCh(2486000.0, 1075000.0),multiRoute1.pointAt(1000)); //1

        assertEquals(new PointCh(2489500.0, 1075000.0),multiRoute2.pointAt(1500)); //2

        assertEquals(new PointCh(2487000.0, 1075000.0),twoMultiroutes.pointAt(2000)); //2
    }


    @Test
    void elevationAtTest2() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();

        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, Functions.constant(10)));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, Functions.constant(20)));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, Functions.constant(30)));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, Functions.constant(40)));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, Functions.constant(50)));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, Functions.constant(60)));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, Functions.constant(70)));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, Functions.constant(80)));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, Functions.constant(90)));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, Functions.constant(100)));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, Functions.constant(110)));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, Functions.constant(120)));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);

        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(30,multiRoute1.elevationAt(1000));

        assertEquals(100,multiRoute2.elevationAt(1500));

        assertEquals(90,twoMultiroutes.elevationAt(4000));
    }

    @Test
    void nodeClosestToTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null)));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(1, 2, new PointCh(2485020, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(2, 3, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, null)));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(3, 4, new PointCh(2485060.0, 1075000.0), new PointCh(2485080.0, 1075000.0), 20, null)));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(4, 5, new PointCh(2485080.0, 1075000.0), new PointCh(2485100.0, 1075000.0), 20, null)));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(5, 6, new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0), 20, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2));
        assertEquals(0,multiRoute.nodeClosestTo(-455));
        assertEquals(0,multiRoute.nodeClosestTo(5));
        assertEquals(0,multiRoute.nodeClosestTo(0));
        assertEquals(1,multiRoute.nodeClosestTo(29));
        assertEquals(2,multiRoute.nodeClosestTo(43.3647));
        assertEquals(3,multiRoute.nodeClosestTo(66));
        assertEquals(4,multiRoute.nodeClosestTo(80));
        assertEquals(6,multiRoute.nodeClosestTo(1200));

    }

    @Test
    void nodeClosestToTest2() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();

        edge1.add(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 500, null));
        edge1.add(new Edge(1, 2, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(2, 3, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(3, 4, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(4, 5, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(5, 6, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(6, 7, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(7, 8, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(8, 9, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(9, 10, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(10, 11, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(11, 12, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);

        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);
        assertEquals(3000,multiRoute1.length());

        MultiRoute multiRoute2 = new MultiRoute(route2);
        assertEquals(3000,multiRoute2.length());

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        assertEquals(2, multiRoute1.nodeClosestTo(1000));
        assertEquals(0,multiRoute1.nodeClosestTo(-1000));
        assertEquals(6,multiRoute1.nodeClosestTo(10000));

        assertEquals(9, multiRoute2.nodeClosestTo(1500));
        assertEquals(6 , multiRoute2.nodeClosestTo(-100));
        assertEquals(12,multiRoute2.nodeClosestTo(10000));

        assertEquals(12,twoMultiroutes.nodeClosestTo(7000));
        assertEquals(0,twoMultiroutes.nodeClosestTo(-7000));
        assertEquals(8,twoMultiroutes.nodeClosestTo(4000));
        assertEquals(12,twoMultiroutes.nodeClosestTo(10000));


    }

    @Test
    void pointClosestToTest() {
        SingleRoute singleRoute = new SingleRoute(List.of(new Edge(0, 1, new PointCh(2485000.0, 1075000.0), new PointCh(2485020.0, 1075000.0), 20, null)));
        SingleRoute singleRoute2 = new SingleRoute(List.of(new Edge(1, 2, new PointCh(2485020, 1075000.0), new PointCh(2485040.0, 1075000.0), 20, null)));
        SingleRoute singleRoute3 = new SingleRoute(List.of(new Edge(2, 3, new PointCh(2485040.0, 1075000.0), new PointCh(2485060.0, 1075000.0), 20, null)));
        SingleRoute singleRoute4 = new SingleRoute(List.of(new Edge(3, 4, new PointCh(2485060.0, 1075000.0), new PointCh(2485080.0, 1075000.0), 20, null)));
        SingleRoute singleRoute5 = new SingleRoute(List.of(new Edge(4, 5, new PointCh(2485080.0, 1075000.0), new PointCh(2485100.0, 1075000.0), 20, null)));
        SingleRoute singleRoute6 = new SingleRoute(List.of(new Edge(5, 6, new PointCh(2485100.0, 1075000.0), new PointCh(2485120.0, 1075000.0), 20, null)));
        MultiRoute multiRoute1 = new MultiRoute(Arrays.asList(singleRoute, singleRoute2, singleRoute3));
        MultiRoute multiRoute2 = new MultiRoute(Arrays.asList(singleRoute4, singleRoute5, singleRoute6));
        MultiRoute multiRoute = new MultiRoute(Arrays.asList(multiRoute1, multiRoute2));
        assertEquals(new RoutePoint(new PointCh(2485060.0, 1075000.0), 60, 1000), multiRoute.pointClosestTo(new PointCh(2485060.0, 1076000.0)));
        assertEquals(new RoutePoint(new PointCh(2485078.0, 1075000.0), 78, 4000), multiRoute.pointClosestTo(new PointCh(2485078.0, 1079000.0)));
        assertEquals(new RoutePoint(new PointCh(2485120.0, 1075000.0), 120, 0), multiRoute.pointClosestTo(new PointCh(2485120.0, 1075000.0)));
        assertEquals(new RoutePoint(new PointCh(2485120.0, 1075000.0), 120, 2000), multiRoute.pointClosestTo(new PointCh(2487120.0, 1075000.0)));
    }

    @Test
    void pointClosestToTest2() {
        List<Edge> edge1 = new ArrayList<>();
        List<Edge> edge2 = new ArrayList<>();
        List<Edge> edge3 = new ArrayList<>();
        List<Edge> edge4 = new ArrayList<>();
        List<Edge> edge5 = new ArrayList<>();
        List<Edge> edge6 = new ArrayList<>();

        edge1.add(new Edge(0, 1, new PointCh(2485100.0, 1075000.0), new PointCh(2485500.0, 1075000.0), 400, null));
        edge1.add(new Edge(0, 1, new PointCh(2485500.0, 1075000.0), new PointCh(2486000.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486000.0, 1075000.0), new PointCh(2486500.0, 1075000.0), 500, null));
        edge2.add(new Edge(0, 1, new PointCh(2486500.0, 1075000.0), new PointCh(2487000.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487000.0, 1075000.0), new PointCh(2487500.0, 1075000.0), 500, null));
        edge3.add(new Edge(0, 1, new PointCh(2487500.0, 1075000.0), new PointCh(2488000.0, 1075000.0), 500, null));

        edge4.add(new Edge(0, 1, new PointCh(2488000.0, 1075000.0), new PointCh(2488500.0, 1075000.0), 500, null));
        edge4.add(new Edge(0, 1, new PointCh(2488500.0, 1075000.0), new PointCh(2489000.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489000.0, 1075000.0), new PointCh(2489500.0, 1075000.0), 500, null));
        edge5.add(new Edge(0, 1, new PointCh(2489500.0, 1075000.0), new PointCh(2490000.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490000.0, 1075000.0), new PointCh(2490500.0, 1075000.0), 500, null));
        edge6.add(new Edge(0, 1, new PointCh(2490500.0, 1075000.0), new PointCh(2491000.0, 1075000.0), 500, null));
        SingleRoute singleRoute1 = new SingleRoute(edge1);
        SingleRoute singleRoute2 = new SingleRoute(edge2);
        SingleRoute singleRoute3 = new SingleRoute(edge3);

        SingleRoute singleRoute4 = new SingleRoute(edge4);
        SingleRoute singleRoute5 = new SingleRoute(edge5);
        SingleRoute singleRoute6 = new SingleRoute(edge6);

        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();
        route1.add(singleRoute1);
        route1.add(singleRoute2);
        route1.add(singleRoute3);
        route2.add(singleRoute4);
        route2.add(singleRoute5);
        route2.add(singleRoute6);

        MultiRoute multiRoute1 = new MultiRoute(route1);

        MultiRoute multiRoute2 = new MultiRoute(route2);

        List<Route> twoRoutes = new ArrayList<>();
        twoRoutes.add(multiRoute1);
        twoRoutes.add(multiRoute2);
        MultiRoute twoMultiroutes = new MultiRoute(twoRoutes);

        RoutePoint routePoint1 = new RoutePoint(new PointCh(2486000.0, 1075000.0),  900,10);

        RoutePoint routePoint2 = new RoutePoint(new PointCh(2489500.0, 1075000.0),1500,20);

        RoutePoint routePointTwoRoute = new RoutePoint(new PointCh(2489500.0, 1075000.0), 4400,30);

        RoutePoint routePoint1Gauche = new RoutePoint(new PointCh(2485100.0, 1075000.0), 0,100);

        RoutePoint routePoint1Droite = new RoutePoint(new PointCh(2488000.0, 1075000.0),2900,4000);

        assertEquals(routePoint1,multiRoute1.pointClosestTo(new PointCh(2486000.0, 1075010.0)));
        assertEquals(routePoint1Gauche,multiRoute1.pointClosestTo(new PointCh(2485000.0,1075000.0)));

        assertEquals(routePoint1Droite,multiRoute1.pointClosestTo(new PointCh(2492000.0,1075000.0)));

        assertEquals(routePoint2,multiRoute2.pointClosestTo(new PointCh(2489500.0, 1075020.0)));


        assertEquals(routePointTwoRoute,twoMultiroutes.pointClosestTo(new PointCh(2489500.0, 1075030.0)));




        RoutePoint routePoint1Gauche2 = new RoutePoint(new PointCh(2485100.0, 1075000.0), 0,100);
        RoutePoint routePoint1Droite2 = new RoutePoint(new PointCh(2491000.0, 1075000.0),5900,1000);
        assertEquals(routePoint1Gauche2,twoMultiroutes.pointClosestTo(new PointCh(2485000.0, 1075000.0)));
        assertEquals(routePoint1Droite2,twoMultiroutes.pointClosestTo(new PointCh(2492000.0, 1075000.0)));

    }
}
