package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

public class WaypointsManager {

    private Pane pane = new Pane();
    private ObservableList<Waypoint> waypoints;

    public WaypointsManager(Graph graph, ObjectProperty<MapViewParameters> parameters, ObservableList<Waypoint> waypoints, Consumer<String> errorConsumer) {
        Pane pane = new Pane();
        this.waypoints = waypoints;
        SVGPath outsideBorder = new SVGPath();
        outsideBorder.setContent("M-8-20C-5-14-2-7 0 0 2-7 5-14 8-20 20-40-20-40-8-20");
        SVGPath insideBorder = new SVGPath();
        insideBorder.setContent("M0-23A1 1 0 000-29 1 1 0 000-23");
        Group pins = new Group();
        pane.getChildren().setAll(pins);


    }

    public void addWaypoint(double x, double y) {

    }

    public Node pane() {
        return pane;
    }
}
