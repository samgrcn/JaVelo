package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.routing.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.nio.file.Path;

public class ElevationProfileManagerTest {

    public ElevationProfileManagerTest() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        CityBikeCF costFunction = new CityBikeCF(graph);
        RouteComputer routeComputer =
                new RouteComputer(graph, costFunction);

        Route route = routeComputer
                .bestRouteBetween(159049, 117669);
        ElevationProfile profile = ElevationProfileComputer
                .elevationProfile(route, 5);

        ObjectProperty<ElevationProfile> profileProperty =
                new SimpleObjectProperty<>(profile);
        DoubleProperty highlightProperty =
                new SimpleDoubleProperty(1500);

        ElevationProfileManager profileManager =
                new ElevationProfileManager(profileProperty,
                        highlightProperty);

    }
}
