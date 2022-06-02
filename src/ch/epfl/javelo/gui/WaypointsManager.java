package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
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
    private final DoubleProperty differenceX = new SimpleDoubleProperty();
    private final DoubleProperty differenceY = new SimpleDoubleProperty();


    /**
     * Creates the waypoints manager and its listeners.
     *
     * @param graph         the graph of the area
     * @param parameters    the map parameters
     * @param waypoints     the list of waypoints
     * @param errorConsumer the error consumer
     */
    public WaypointsManager(Graph graph, ObjectProperty<MapViewParameters> parameters, ObservableList<Waypoint> waypoints, Consumer<String> errorConsumer) {

        this.waypoints = waypoints;
        this.parameters = parameters;
        this.graph = graph;

        this.errorConsumer = errorConsumer;

        pane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        pane.setPickOnBounds(false);

        listIterator();
        pane.getChildren().setAll(pinsList);

        waypoints.addListener((Observable w) -> update());
        parameters.addListener((change, oldV, newV) -> update());

    }

    public void addWaypoint(double x, double y) {
        PointCh point;
        int closestNode;
        try {
            point = this.parameters.get().pointAt(x, y).toPointCh();
            closestNode = graph.nodeClosestTo(point, SEARCH_DISTANCE);
        } catch (NullPointerException ignored) {
            errorConsumer.accept("Aucune route à proximité !");
            return;
        }
        if (closestNode == NO_NODE) {
            errorConsumer.accept("Aucune route à proximité !");
            return;
        }
        waypoints.add(new Waypoint(point, closestNode));
    }

    /**
     * @return the pane containing the waypoints.
     */
    public Pane pane() {
        return pane;
    }

    /**
     * Private method that handles one point. It sets up its javaFX and add its listeners including the addition
     * drag and removal.
     *
     * @param status      String for the style class: "first" if it's the first point of the route,
     *                    "last" if it's the last and else "middle"
     * @param x           x coordinates
     * @param y           y coordinates
     * @param indexInList the index of the pin in the list of all pins
     */
    private void waypointCreator(String status, double x, double y, int indexInList) {

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
            differenceX.set(mousePosition.get().getX() - pointer.get().getX());
            differenceY.set(mousePosition.get().getY() - pointer.get().getY());
        });

        pins.setOnMouseDragged(drag -> {
            pins.setLayoutX(drag.getSceneX() - differenceX.get());
            pins.setLayoutY(drag.getSceneY() - differenceY.get());
        });

        pins.setOnMouseReleased(release -> {
            if (!release.isStillSincePress()) {
                try {
                    this.parameters.get().pointAt(release.getX(), release.getY()).toPointCh();
                } catch (NullPointerException ignored) {
                    errorConsumer.accept("Aucune route à proximité !");
                    pins.setLayoutX(pointer.get().getX());
                    pins.setLayoutY(pointer.get().getY());
                }

                double newX = release.getSceneX() - differenceX.get();
                double newY = release.getSceneY() - differenceY.get();

                PointCh newPoint = this.parameters.get().pointAt(newX, newY).toPointCh();
                int closestNode = graph.nodeClosestTo(newPoint, SEARCH_DISTANCE);

                if (closestNode == NO_NODE) {
                    errorConsumer.accept("Aucune route à proximité !");
                    pins.setLayoutX(pointer.get().getX());
                    pins.setLayoutY(pointer.get().getY());
                    return;
                }

                pointer.set(new Point2D(newX, newY));
                Waypoint newWaypoint = new Waypoint(newPoint, closestNode);
                waypoints.set(indexInList, newWaypoint);
            }
        });

        pins.setOnMouseClicked(click -> {
            if (click.isStillSincePress()) {
                remove(indexInList);
            }
        });
    }

    /**
     * Scans the list and manages each waypoint in the list of every waypoint
     */
    private void listIterator() {

        if (waypoints.size() == 0) return;

        PointCh position = waypoints.get(0).position();
        PointWebMercator pointWeb = PointWebMercator.ofPointCh(position);
        double x = parameters.get().viewX(pointWeb);
        double y = parameters.get().viewY(pointWeb);

        for (int i = 0; i < waypoints.size(); i++) {
            if (i == 0) {
                waypointCreator("first", x, y, i);
                if (waypoints.size() == 1) {
                    break;
                }
            } else if (i == waypoints.size() - 1) {
                waypointCreator("last", x, y, i);
                break;
            } else {
                waypointCreator("middle", x, y, i);
            }
            position = waypoints.get(i + 1).position();
            pointWeb = PointWebMercator.ofPointCh(position);
            x = parameters.get().viewX(pointWeb);
            y = parameters.get().viewY(pointWeb);
        }
    }

    /**
     * Updates so the only existing points are the one in the list of waypoints.
     */
    private void update() {
        pane.getChildren().clear();
        pinsList.clear();
        listIterator();
        pane.getChildren().setAll(pinsList);
    }

    /**
     * Removes a waypoint from the list.
     *
     * @param indexInList the index of the waypoint in the list
     */
    private void remove(int indexInList) {
        waypoints.remove(indexInList);
    }

}