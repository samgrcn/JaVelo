package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private final static int SEARCH_DISTANCE = 500;
    private final static int NO_NODE = -1;
    private final static int PREF_WIDTH = 600;
    private final static int PREF_HEIGHT = 300;
    private final Pane pane = new Pane();
    private final ObservableList<Waypoint> waypoints;
    private final ObjectProperty<MapViewParameters> parameters;
    private final Graph graph;
    private final Consumer<String> errorConsumer;
    private final List<Group> pinsList = new ArrayList<>();


    public WaypointsManager(Graph graph, ObjectProperty<MapViewParameters> parameters, ObservableList<Waypoint> waypoints, Consumer<String> errorConsumer) {

        this.waypoints = waypoints;
        this.parameters = parameters;
        this.graph = graph;

        this.errorConsumer = errorConsumer;

        pane.setPrefSize(PREF_WIDTH,PREF_HEIGHT);
        pane.setPickOnBounds(false);

        listIterator();
        pane.getChildren().setAll(pinsList);

        waypoints.addListener((Observable w) -> update());

        parameters.addListener((change, oldV, newV) -> update());

    }


    private void WaypointCreator(String status, double x, double y, int indexInList) {

        ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();
        ObjectProperty<Point2D> pointer = new SimpleObjectProperty<>();

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


        pins.setOnMousePressed(press -> {
            mousePosition.set(new Point2D(press.getSceneX(), press.getSceneY()));
            pointer.set(new Point2D(pins.getLayoutX(), pins.getLayoutY()));
        });

        pins.setOnMouseDragged(drag -> {

            double differenceX = mousePosition.get().getX() - pointer.get().getX();
            double differenceY = mousePosition.get().getY() - pointer.get().getY();
            pins.setLayoutX(drag.getSceneX() - differenceX);
            pins.setLayoutY(drag.getSceneY() - differenceY);

            pins.setOnMouseReleased(release -> {

                try {
                    this.parameters.get().pointAt(release.getX(), release.getY()).toPointCh();
                } catch (NullPointerException ignored) {
                    errorConsumer.accept("Aucune route à proximité !");
                    pins.setLayoutX(pointer.get().getX());
                    pins.setLayoutY(pointer.get().getX());
                }

                double newX = release.getSceneX() - differenceX;
                double newY = release.getSceneY() - differenceY;

                PointCh newPoint = this.parameters.get().pointAt(newX, newY).toPointCh();
                int closestNode = graph.nodeClosestTo(newPoint, SEARCH_DISTANCE);

                if (closestNode == -1) {
                    errorConsumer.accept("Aucune route à proximité !");
                    pins.setLayoutX(pointer.get().getX());
                    pins.setLayoutY(pointer.get().getY());
                    return;
                }

                pointer.set(new Point2D(newX, newY));
                Waypoint newWaypoint = new Waypoint(newPoint, closestNode);
                waypoints.set(indexInList, newWaypoint);

            });
        });

        pins.setOnMouseClicked(click -> {
            if (click.isStillSincePress()) {
                remove(indexInList);
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
        waypoints.remove(indexInList);
    }


    public void addWaypoint(double x, double y) {
        try {
            this.parameters.get().pointAt(x, y).toPointCh();
        } catch (NullPointerException ignored) {
            errorConsumer.accept("Aucune route à proximité !");
            return;
        }

        PointCh point = this.parameters.get().pointAt(x, y).toPointCh();
        int closestNode = graph.nodeClosestTo(point, 500);

        if (closestNode == NO_NODE) {
            errorConsumer.accept("Aucune route à proximité !");
            return;
        }

        waypoints.add(new Waypoint(point, closestNode));
    }


    public Pane pane() {
        return pane;
    }
}