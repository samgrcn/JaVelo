package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WaypointsManager {

    private final Pane pane = new Pane();
    private final ObservableList<Waypoint> waypoints;
    private final ObjectProperty<MapViewParameters> parameters;
    private final Graph graph;
    private final Consumer<String> errorConsumer;
    private final List<Group> pinsList = new ArrayList<>();
    private final ObjectProperty<Point2D> newCoordinates = new SimpleObjectProperty<>();

    public WaypointsManager(Graph graph, ObjectProperty<MapViewParameters> parameters, ObservableList<Waypoint> waypoints, Consumer<String> errorConsumer) {

        this.waypoints = waypoints;
        this.parameters = parameters;
        this.graph = graph;
        this.errorConsumer = errorConsumer;

        pane.setPrefSize(600, 300);
        pane.setPickOnBounds(false);

        listIterator();
        pane.getChildren().setAll(pinsList);

        waypoints.addListener((Observable w) -> {
            update();
        });

        parameters.addListener(change -> {
            update();
        });

    }


    private void WaypointCreator(String status, double x, double y, int indexInList) {

        SVGPath outsideBorder = new SVGPath();
        SVGPath insideBorder = new SVGPath();

        outsideBorder.setContent("M-8-20C-5-14-2-7 0 0 2-7 5-14 8-20 20-40-20-40-8-20");
        outsideBorder.getStyleClass().add("pin_outside");
        insideBorder.setContent("M0-23A1 1 0 000-29 1 1 0 000-23");
        insideBorder.getStyleClass().add("pin_inside");

        Group pins = new Group(outsideBorder, insideBorder);

        pins.getStyleClass().add("pin");
        pins.getStyleClass().add(status);

        pins.setLayoutX(x);
        pins.setLayoutY(y);

        pinsList.add(pins);

        pins.setOnMouseDragged(drag -> {
            pins.setLayoutX(drag.getSceneX());
            pins.setLayoutY(drag.getSceneY());

            pins.setOnMouseReleased(release -> {
                PointCh newPoint = this.parameters.get().pointAt(x + release.getX(), y + release.getY()).toPointCh();
                Waypoint newWaypoint = new Waypoint(newPoint, graph.nodeClosestTo(newPoint, 500));
                waypoints.set(indexInList, newWaypoint);
            });
        });


        pins.setOnMouseClicked(click -> {
            if (click.isStillSincePress()) {
                remove(indexInList);
                update();
            }
        });
    }

    private void listIterator() {

        if (waypoints.size() == 0) return;

        PointCh position = waypoints.get(0).position();
        PointWebMercator pointWeb = PointWebMercator.ofPointCh(position);
        double x = parameters.get().viewX(pointWeb);
        double y = parameters.get().viewY(pointWeb);

        for (int i = 0; i < waypoints.size(); i++) {
            if (i == 0) {
                WaypointCreator("first", x, y, i);
                if (waypoints.size() == 1) {
                    break;
                }
            } else if (i == waypoints.size() - 1) {
                WaypointCreator("last", x, y, i);
                break;
            } else {
                WaypointCreator("middle", x, y, i);
            }
            position = waypoints.get(i + 1).position();
            pointWeb = PointWebMercator.ofPointCh(position);
            x = parameters.get().viewX(pointWeb);
            y = parameters.get().viewY(pointWeb);
        }
    }

    private void update() {
        pane.getChildren().clear();
        pinsList.clear();
        listIterator();
        pane.getChildren().setAll(pinsList);
    }

    private void remove(int indexInList) {
        pinsList.remove(indexInList);
        pane.getChildren().remove(indexInList);
        waypoints.remove(indexInList);
    }


    public void addWaypoint(double x, double y) {
        PointCh point = this.parameters.get().pointAt(x, y).toPointCh();
        waypoints.add(new Waypoint(point, graph.nodeClosestTo(point, 500)));
    }

    public Node pane() {
        return pane;
    }
}