package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

/**
 * Manages the display of the "annotated" map,
 * i.e. the background map over which the route and waypoints are superimposed.
 *
 * @author Quentin Chappuis (339517)
 */
public final class AnnotatedMapManager {

    private final static int ZOOM_AT_START = 12;
    private final static int X_START = 543200;
    private final static int Y_START = 370650;

    private final StackPane stackPane = new StackPane();
    private final ObjectProperty<Point2D> actualMousePosition = new SimpleObjectProperty<>();

    /**
     * @param graph the road network graph
     * @param tileManager the OpenStreetMap tile manager
     * @param routeBean the route bean
     * @param errorConsumer an "error consumer" to report an error
     */
    public AnnotatedMapManager(Graph graph, TileManager tileManager, RouteBean routeBean,
                               Consumer<String> errorConsumer) {

        MapViewParameters mapViewParameters = new MapViewParameters(ZOOM_AT_START, X_START, Y_START);
        ObjectProperty<MapViewParameters> mapViewParametersP = new SimpleObjectProperty<>(mapViewParameters);

        WaypointsManager waypointsManager = new WaypointsManager(graph, mapViewParametersP,
                routeBean.waypoints(), errorConsumer);
        BaseMapManager baseMapManager = new BaseMapManager(tileManager, waypointsManager, mapViewParametersP);
        RouteManager routeManager = new RouteManager(routeBean, mapViewParametersP);

        stackPane.getChildren().addAll(baseMapManager.pane(), routeManager.pane(), waypointsManager.pane());
        stackPane.getStylesheets().add("map.css");

        stackPane.setOnMouseMoved(mouse -> {
            actualMousePosition.set(new Point2D(mouse.getSceneX(), mouse.getSceneY()));
        });

        stackPane.setOnMouseExited(mouse -> {
            actualMousePosition.set(new Point2D(Double.NaN, Double.NaN));
        });
    }

    /**
     * Returns the pane containing the annotated card.
     *
     * @return the pane
     */
    public StackPane pane() {
        return stackPane;
    }

    /**
     * Returns the property containing the position of the mouse pointer along the route.
     *
     * @return the property containing the position of the mouse pointer
     */
    public ObjectProperty<Point2D> mousePositionOnRouteProperty() {
        return actualMousePosition;
    }
}
