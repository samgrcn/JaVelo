package ch.epfl.javelo.gui;


import ch.epfl.javelo.Math2;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;

import static ch.epfl.javelo.Math2.clamp;

public final class BaseMapManager {

    private final WaypointsManager waypointsManager;
    private final Pane pane;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final TileManager tileManager;
    private final ObjectProperty<MapViewParameters> parameters;
    private boolean redrawNeeded;
    private final ObjectProperty<Point2D> point2d = new SimpleObjectProperty<>();
    private PointCh mouseClick;

    private static final int TILE_WIDTH_AND_HEIGHT = 256;


    public BaseMapManager(TileManager tileManager, WaypointsManager waypointsManager, ObjectProperty<MapViewParameters> parameters) {


        this.canvas = new Canvas();
        this.pane = new Pane();

        this.waypointsManager = waypointsManager;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileManager = tileManager;
        this.parameters = parameters;

        canvas.setHeight(300);
        canvas.setWidth(600);

        pane.getChildren().add(canvas);

        pane.setPrefSize(600, 300);

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.setPickOnBounds(false);

        pane.setOnScroll(e -> {
            double zoomDelta = e.getDeltaY();

            zoomDelta = zoomDelta / Math.abs(zoomDelta);

            int newZoomLevel = this.parameters.get().zoomAt() + (int) zoomDelta;
            newZoomLevel = Math2.clamp(8, newZoomLevel, 19);

            if (newZoomLevel != this.parameters.get().zoomAt()) {

                double x = this.parameters.get().topLeft().getX() + e.getX();
                double y = this.parameters.get().topLeft().getY() + e.getY();
                double zoomX = Math.scalb(x, (int) zoomDelta);
                double zoomY = Math.scalb(y, (int) zoomDelta);
                this.parameters.set( new MapViewParameters(newZoomLevel, zoomX - e.getX(), zoomY - e.getY()));
            }
        });

        pane.setOnMousePressed(press -> {
            point2d.set(new Point2D(press.getX(), press.getY()));
        });


        pane.setOnMouseDragged(drag -> {
            double oldX = point2d.get().getX();
            double oldY = point2d.get().getY();
            point2d.set(new Point2D(drag.getX(), drag.getY()));
            this.parameters.set(new MapViewParameters(this.parameters.get().zoomAt(),
                    this.parameters.get().topLeft().getX() + (oldX - point2d.get().getX()),
                    this.parameters.get().topLeft().getY() + (oldY - point2d.get().getY())));


        });

        pane.setOnMouseClicked(click -> {
            if (click.isStillSincePress()) {
                mouseClick = this.parameters.get().pointAt(click.getX(), click.getY()).toPointCh();
                this.waypointsManager.addWaypoint(mouseClick.e(), mouseClick.n());
                redrawOnNextPulse();
            }
        });

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
            newS.addPreLayoutPulseListener(this::redrawOnNextPulse);
        });

    }

    public Node pane() {
        return pane;
    }




    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;

        double xMax = this.parameters.get().topLeft().getX() + canvas.getWidth();
        double yMax = this.parameters.get().topLeft().getY() + canvas.getHeight();

        double xMinTile = this.parameters.get().topLeft().getX() / TILE_WIDTH_AND_HEIGHT;
        double xMaxTile = xMax / TILE_WIDTH_AND_HEIGHT;
        double yMinTile = this.parameters.get().topLeft().getY() / TILE_WIDTH_AND_HEIGHT;
        double yMaxTile = yMax / TILE_WIDTH_AND_HEIGHT;

        for (int y = (int) yMinTile; y <= yMaxTile; y++) {
            for (int x = (int) xMinTile; x <= xMaxTile; x++) {
                TileManager.TileId tileId = new TileManager.TileId(this.parameters.get().zoomAt(), x, y);
                try {
                    graphicsContext.drawImage(
                            tileManager.imageForTileAt(tileId),
                            x * TILE_WIDTH_AND_HEIGHT - this.parameters.get().topLeft().getX(),
                            y * TILE_WIDTH_AND_HEIGHT - this.parameters.get().topLeft().getY());
                } catch (IOException ignored) {
                }

            }
        }
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

}

