package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import ch.epfl.javelo.routing.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.List;
import java.util.function.Consumer;

/**
 * Manages the display of the route and (part of) the interaction with it.
 *
 * @author Quentin Chappuis (339517)
 */
public final class RouteManager {

    private final static int DISK_RADIUS = 5;
    private final static int PREF_WIDTH = 600;
    private final static int PREF_HEIGHT = 300;

    private final Pane pane = new Pane();
    private final RouteBean bean;
    private final ObjectProperty<MapViewParameters> parameters;
    private final Consumer<String> errorConsumer;
    private final Circle disk = new Circle();
    private final Polyline polyline = new Polyline();
    private Route route;

    /**
     * @param bean the route bean
     * @param parameters a JavaFX property, read-only, containing the parameters of the displayed map
     * @param errorConsumer the "error consumer" for reporting an error
     */
    public RouteManager(RouteBean bean, ObjectProperty<MapViewParameters> parameters, Consumer<String> errorConsumer) {
        this.bean = bean;
        this.parameters = parameters;
        this.errorConsumer = errorConsumer;

        init();
    }

    /**
     *
     */
    private void init() {
        pane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        pane.setPickOnBounds(false);

        disk.setRadius(DISK_RADIUS);
        disk.setId("highlight");

        polyline.setId("route");

        pane.getChildren().add(polyline);
        pane.getChildren().add(disk);

        parameters.addListener(change -> {
            update();
        });

        bean.routeProperty().addListener(change -> {
            update();
        });
    }

    /**
     * Returns the JavaFX panel containing the route line and the highlighting disk.
     *
     * @return the JavaFX panel
     */
    public Pane pane() {
        return pane;
    }

    /**
     *
     */
    private void setDisk() {
        if (route == null) {
            disk.setVisible(false);
            return;
        }

        disk.setVisible(true);
        PointCh pointCh = route.pointAt(bean.highlightedPosition());
        PointWebMercator webMercatorPoint = PointWebMercator.ofPointCh(pointCh);
        double x = parameters.get().viewX(webMercatorPoint);
        double y = parameters.get().viewY(webMercatorPoint);

        disk.setLayoutX(x);
        disk.setLayoutY(y);
    }

    /**
     *
     */
    private void setPolyline() {
        if (route == null) {
            polyline.setVisible(false);
            return;
        }

        polyline.setVisible(true);
        List<PointCh> pointsList = route.points();
        Double[] points = new Double[pointsList.size() * 2];
        int index = 0;
        for (PointCh point : pointsList) {
            points[index] = parameters.get().viewX(PointWebMercator.ofPointCh(point));
            points[index + 1] = parameters.get().viewY(PointWebMercator.ofPointCh(point));
            index += 2;
        }
        polyline.getPoints().setAll(points);
    }

    /**
     *
     */
    private void update() {
        route = bean.route();
        disk.setOnMouseClicked(click -> {
            int closestNode = route.nodeClosestTo(bean.highlightedPosition());
            for (Waypoint point : bean.waypoints()) {
                if (point.closestNodeId() == closestNode) {
                    errorConsumer.accept("Un point de passage est déjà présent à cet endroit !");
                    return;
                }
            }
            Waypoint waypoint = new Waypoint(route.pointAt(bean.highlightedPosition()), closestNode);
            bean.waypoints().add(bean.waypoints().size() / 2, waypoint);
        });
        setDisk();
        setPolyline();
    }
}
