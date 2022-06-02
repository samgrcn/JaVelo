package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import ch.epfl.javelo.routing.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.List;

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
    private final Circle disk = new Circle();
    private final Polyline polyline = new Polyline();
    private Route route;

    /**
     * @param bean       the route bean
     * @param parameters a JavaFX property, read-only, containing the parameters of the displayed map
     */
    public RouteManager(RouteBean bean, ObjectProperty<MapViewParameters> parameters) {
        this.bean = bean;
        this.parameters = parameters;

        init();
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
     * Initialization of all components and listeners. Called in the constructor.
     */
    private void init() {
        pane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        pane.setPickOnBounds(false);

        disk.setRadius(DISK_RADIUS);
        disk.setId("highlight");

        polyline.setId("route");

        pane.getChildren().add(polyline);
        pane.getChildren().add(disk);

        bean.highlightedPositionProperty().addListener((change, oldV, newV) -> setDisk());

        parameters.addListener((change, oldV, newV) -> {
            route = bean.route();
            if (oldV.zoomAt() == newV.zoomAt()) setPolylineOnDrag(oldV, newV);
            else setPolyline();
            setDisk();
        });

        bean.routeProperty().addListener((change, oldV, newV) -> {
            route = bean.route();
            setPolyline();
            setDisk();
        });

        disk.setOnMouseClicked(click -> {
            PointCh pointCh = parameters.get().pointAt(disk.localToParent(click.getX(), click.getY()).getX(),
                    disk.localToParent(click.getX(), click.getY()).getY()).toPointCh();
            double position = route.pointClosestTo(pointCh).position();
            int closestNode = route.nodeClosestTo(position);
            Waypoint waypoint = new Waypoint(pointCh, closestNode);
            bean.waypoints().add(bean.indexOfNonEmptySegmentAt(position) + 1, waypoint);
        });
    }


    /**
     * Sets the disk over the route when necessary.
     */
    private void setDisk() {
        if (route == null || Double.isNaN(bean.highlightedPosition())) {
            disk.setVisible(false);
        } else {
            PointCh pointCh = route.pointAt(bean.highlightedPosition());
            PointWebMercator webMercatorPoint = PointWebMercator.ofPointCh(pointCh);
            double x = parameters.get().viewX(webMercatorPoint);
            double y = parameters.get().viewY(webMercatorPoint);

            disk.setLayoutX(x);
            disk.setLayoutY(y);
            disk.setVisible(true);
        }
    }

    /**
     * Completely redraw the polyline when necessary.
     */
    private void setPolyline() {
        if (route == null) {
            polyline.setVisible(false);
        } else {
            polyline.setLayoutX(0);
            polyline.setLayoutY(0);
            List<PointCh> pointsList = route.points();
            Double[] points = new Double[pointsList.size() * 2];
            int index = 0;
            for (PointCh point : pointsList) {
                points[index] = parameters.get().viewX(PointWebMercator.ofPointCh(point));
                points[index + 1] = parameters.get().viewY(PointWebMercator.ofPointCh(point));
                index += 2;
            }
            polyline.getPoints().setAll(points);
            polyline.setVisible(true);
        }
    }

    /**
     * Shifts the polyline during a drag.
     *
     * @param oldV the old MapViewParameters
     * @param newV the new MapViewParameters
     */
    private void setPolylineOnDrag(MapViewParameters oldV, MapViewParameters newV) {
        if (route == null) {
            polyline.setVisible(false);
        } else {
            double differenceX = newV.x() - oldV.x();
            double differenceY = newV.y() - oldV.y();
            polyline.setLayoutX(polyline.getLayoutX() - differenceX);
            polyline.setLayoutY(polyline.getLayoutY() - differenceY);
            polyline.setVisible(true);
        }
    }
}
