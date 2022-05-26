package ch.epfl.javelo.gui;


import ch.epfl.javelo.Math2;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * BaseMapManager manages the display and interaction with the background map.
 *
 * @author Samuel Garcin (345633)
 */
public final class BaseMapManager {

    private final WaypointsManager waypointsManager;
    private final Pane pane;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final TileManager tileManager;
    private final ObjectProperty<MapViewParameters> parameters;
    private boolean redrawNeeded;
    private final ObjectProperty<Point2D> point2d = new SimpleObjectProperty<>();

    private static final int MIN_ZOOM = 8;
    private static final int MAX_ZOOM = 19;
    private static final int TILE_WIDTH_AND_HEIGHT = 256;
    private static final int PREF_WIDTH = 600;
    private static final int PREF_HEIGHT = 300;
    private static final int MIN_SCROLL_TIME = 200;

    /**
     * Constructor for BaseMapManager
     * @param tileManager the tile manager
     * @param waypointsManager the waypoint manager
     * @param parameters the map view parameters (in a property)
     */
    public BaseMapManager(TileManager tileManager, WaypointsManager waypointsManager, ObjectProperty<MapViewParameters> parameters) {


        this.canvas = new Canvas();
        this.pane = new Pane();

        this.waypointsManager = waypointsManager;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileManager = tileManager;
        this.parameters = parameters;

        setupPaneAndCanvas();
        paneHandlersAndListeners();
    }

    /**
     * Private method that redraws only the tiles required in the range of the pane.
     * @throws IOException ignored exception in case of an IOException
     */
    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;
        MapViewParameters parameters = this.parameters.get();
        Point2D topLeft = parameters.topLeft();

        double xMax = topLeft.getX() + canvas.getWidth();
        double yMax = topLeft.getY() + canvas.getHeight();

        double xMinTile = topLeft.getX() / TILE_WIDTH_AND_HEIGHT;
        double xMaxTile = xMax / TILE_WIDTH_AND_HEIGHT;
        double yMinTile = topLeft.getY() / TILE_WIDTH_AND_HEIGHT;
        double yMaxTile = yMax / TILE_WIDTH_AND_HEIGHT;

        for (int y = (int) yMinTile; y <= yMaxTile; y++) {
            for (int x = (int) xMinTile; x <= xMaxTile; x++) {
                TileManager.TileId tileId = new TileManager.TileId(parameters.zoomAt(), x, y);
                try {
                    graphicsContext.drawImage(
                            tileManager.imageForTileAt(tileId),
                            x * TILE_WIDTH_AND_HEIGHT - topLeft.getX(),
                            y * TILE_WIDTH_AND_HEIGHT - topLeft.getY());
                } catch (IOException ignored) {
                }

            }
        }
    }

    /**
     * Request a next pulse
     */
    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

    /**
     * Setups the pane and canvas correctly
     */

    private void setupPaneAndCanvas() {
        canvas.setHeight(PREF_HEIGHT);
        canvas.setWidth(PREF_WIDTH);

        pane.getChildren().add(canvas);
        pane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.setPickOnBounds(false);
    }

    /**
     * Handles every listener and action, including scrolling and dragging the map.
     */
    private void paneHandlersAndListeners() {

        SimpleLongProperty minScrollTime = new SimpleLongProperty();
        pane.setOnScroll(e -> {
            if (e.getDeltaY() == 0d) return;
            long currentTime = System.currentTimeMillis();
            if (currentTime < minScrollTime.get()) return;
            minScrollTime.set(currentTime + MIN_SCROLL_TIME);
            int zoomDelta = (int) Math.signum(e.getDeltaY());

            int newZoomLevel = parameters.get().zoomAt() + zoomDelta;
            newZoomLevel = Math2.clamp(MIN_ZOOM, newZoomLevel, MAX_ZOOM);
            if (newZoomLevel != parameters.get().zoomAt()) {

                double x = parameters.get().topLeft().getX() + e.getX();
                double y = parameters.get().topLeft().getY() + e.getY();
                double zoomX = Math.scalb(x, zoomDelta);
                double zoomY = Math.scalb(y, zoomDelta);
                this.parameters.set(new MapViewParameters(newZoomLevel, zoomX - e.getX(), zoomY - e.getY()));
            }
        });

        pane.setOnMousePressed(press -> {
            point2d.set(new Point2D(press.getX(), press.getY()));
        });


        pane.setOnMouseDragged(drag -> {
            double oldX = point2d.get().getX();
            double oldY = point2d.get().getY();
            point2d.set(new Point2D(drag.getX(), drag.getY()));
            parameters.set(new MapViewParameters(parameters.get().zoomAt(),
                    parameters.get().topLeft().getX() + (oldX - point2d.get().getX()),
                    parameters.get().topLeft().getY() + (oldY - point2d.get().getY())));
        });


        pane.setOnMouseClicked(click -> {
            if (click.isStillSincePress()) {
                waypointsManager.addWaypoint(click.getX(), click.getY());
            }
        });

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
            newS.addPreLayoutPulseListener(this::redrawOnNextPulse);
        });
    }

    /**
     * @return the pane of the base map.
     */
    public Pane pane() {
        return pane;
    }

}

