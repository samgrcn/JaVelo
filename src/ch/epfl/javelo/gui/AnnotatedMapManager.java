package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.util.function.Consumer;

public final class AnnotatedMapManager {

    private final Graph graph;
    private final TileManager tileManager;
    private final RouteBean routeBean;
    private final Consumer<String> errorConsumer;
    private final BaseMapManager baseMapManager;
    private final WaypointsManager waypointsManager;
    private final RouteManager routeManager;
    private final MapViewParameters mapViewParameters;
    private final ObjectProperty<MapViewParameters> mapViewParametersP;
    private final StackPane stackPane = new StackPane();
    private final ObjectProperty<Point2D> actualMousePosition = new SimpleObjectProperty<>();

    public AnnotatedMapManager(Graph graph, TileManager tileManager, RouteBean routeBean,
                               Consumer<String> errorConsumer) {
        this.graph = graph;
        this.tileManager = tileManager;
        this.routeBean = routeBean;
        this.errorConsumer = errorConsumer;
        mapViewParameters = new MapViewParameters(12, 543200, 370650);
        mapViewParametersP = new SimpleObjectProperty<>(mapViewParameters);

        waypointsManager = new WaypointsManager(graph, mapViewParametersP, routeBean.waypoints(), errorConsumer);
        baseMapManager = new BaseMapManager(tileManager, waypointsManager, mapViewParametersP);
        routeManager = new RouteManager(routeBean, mapViewParametersP);

        stackPane.getChildren().addAll(baseMapManager.pane(), routeManager.pane(), waypointsManager.pane());
        stackPane.getStylesheets().add("map.css");

        stackPane.setOnMouseMoved(mouse -> {
            actualMousePosition.set(new Point2D(mouse.getSceneX(), mouse.getSceneY()));
        });

        stackPane.setOnMouseExited(mouse -> {
            actualMousePosition.set(new Point2D(Double.NaN, Double.NaN));
        });
    }

    public StackPane pane() {
        return stackPane;
    }

    public ObjectProperty<Point2D> mousePositionOnRouteProperty() {
        return actualMousePosition;
    }


}
