package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Locale;

public class RouteComputerTest {

        public static void main(String[] args) throws IOException {
            Graph g = Graph.loadFrom(Path.of("ch_west"));
            CostFunction cf = new CityBikeCF(g);
            RouteComputer rc = new RouteComputer(g, cf);
            Route r = rc.bestRouteBetween(2046055, 2694240);
            KmlPrinter.write("javelo.kml", r);
            System.out.println(r.length());
            System.out.println(r.edges().size());
        }
    }

