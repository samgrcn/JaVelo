package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
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
    private ObjectProperty<MapViewParameters> parameters;
    private Graph graph;
    private Consumer<String> errorConsumer;

    public WaypointsManager(Graph graph, ObjectProperty<MapViewParameters> parameters, ObservableList<Waypoint> waypoints, Consumer<String> errorConsumer) {

        this.waypoints = waypoints;
        this.parameters = parameters;
        this.graph = graph;
        this.errorConsumer = errorConsumer;

    }

    private void WaypointCreator(String status, double x, double y) {
        SVGPath outsideBorder = new SVGPath();
        outsideBorder.setContent("M-8-20C-5-14-2-7 0 0 2-7 5-14 8-20 20-40-20-40-8-20");
        SVGPath insideBorder = new SVGPath();
        insideBorder.setContent("M0-23A1 1 0 000-29 1 1 0 000-23");
        Group pins = new Group(outsideBorder, insideBorder);
        pins.setLayoutX(x);
        pins.setLayoutY(y);
        pins.getStyleClass().add("pin");
        pins.getStyleClass().add(status);
        pane.getChildren().add(pins);
    }

    public void addWaypoint(double x, double y) {
        PointCh point = new PointCh(x, y);
        waypoints.add(new Waypoint(point, graph.nodeClosestTo(point, 500)));
        if (pane.getChildren().isEmpty()) {
            WaypointCreator("first", x, y);
        } else if ((pane.getChildren().size() == 1)) {
            WaypointCreator("middle", x, y);
        } else {
            WaypointCreator("last", x, y);
        }
    }

    public Node pane() {
        return pane;
    }
}
