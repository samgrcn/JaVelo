package ch.epfl.javelo.gui;


import ch.epfl.javelo.Math2;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.io.IOException;

public final class BaseMapManager {

    private final WaypointsManager waypointsManager;
    private final Pane pane;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final TileManager tileManager;
    private final ObjectProperty<MapViewParameters> parameters;
    private final double topLeftX;
    private final double topLeftY;
    private boolean redrawNeeded;
    private ObjectProperty<Point2D> point;

    private static final int TILE_WIDTH_AND_HEIGHT = 256;


    public BaseMapManager(TileManager tileManager, WaypointsManager waypointsManager, ObjectProperty<MapViewParameters> parameters) {


        this.canvas = new Canvas();
        this.pane = new Pane();

        this.waypointsManager = waypointsManager;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileManager = tileManager;
        this.parameters = parameters;
        this.topLeftX = parameters.get().topLeft().getX();
        this.topLeftY = parameters.get().topLeft().getY();


        canvas.setHeight(300);
        canvas.setWidth(600);
        pane.getChildren().add(canvas);

        pane.setPrefSize(600, 300);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.setOnScroll(e -> {
            double zoomDelta = e.getDeltaY();

           zoomDelta = clampAtOne(zoomDelta);

            int newZoomLevel = this.parameters.get().zoomAt() + (int) zoomDelta;
            newZoomLevel = Math2.clamp(8, newZoomLevel, 19);

            if(newZoomLevel != this.parameters.get().zoomAt()) {
                double x = topLeftX + e.getX();
                double y = topLeftY + e.getY();
                double zoomX = Math.scalb(x, (int) zoomDelta);
                double zoomY = Math.scalb(y, (int) zoomDelta);

                this.parameters.set(new MapViewParameters(newZoomLevel, zoomX - e.getX(), zoomY - e.getY()));

                redrawOnNextPulse();
            }
        });

        pane.setOnMouseDragged(drag -> {
            double oldX = getPoint().getX();
            double oldY = getPoint().getY();

            setPoint(new Point2D(drag.getX(), drag.getY()));

            this.parameters.set(new MapViewParameters(this.parameters.get().zoomAt(),
                    topLeftX + (oldX - getPoint().getX()),
                    topLeftY + (oldY - getPoint().getY())));
            redrawOnNextPulse();
        });

        pane.setOnMouseClicked(click -> {
            setPoint(new Point2D(click.getX(), click.getY()));
            if(click.isStillSincePress()) {
                this.waypointsManager.addWaypoint(click.getX(), click.getY());
                redrawOnNextPulse();
            }
        });


        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
        redrawOnNextPulse();
    }

    public Node pane() {
        return pane;
    }

    private Point2D getPoint() {
        return point.get();
    }

    private void setPoint(Point2D point) {
        this.point.set(point);
    }

    private double clampAtOne(double zoomDelta) {
        if(zoomDelta < 0) { zoomDelta = -1; }
        else if(zoomDelta > 0) { zoomDelta = 1; }
        return zoomDelta;
    }

    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;

        double xMax = topLeftX + canvas.getWidth();
        double yMax = topLeftY + canvas.getHeight();

        double xMinTile = topLeftX / TILE_WIDTH_AND_HEIGHT;
        double xMaxTile = xMax / TILE_WIDTH_AND_HEIGHT;
        double yMinTile = topLeftY / TILE_WIDTH_AND_HEIGHT;
        double yMaxTile = yMax / TILE_WIDTH_AND_HEIGHT;

        for (int y = (int) yMinTile; y <= yMaxTile; y++) {
            for (int x = (int) xMinTile; x <= xMaxTile; x++) {
                TileManager.TileId tileId = new TileManager.TileId(parameters.get().zoomAt(), x, y);
                try {
                    graphicsContext.drawImage(
                            tileManager.imageForTileAt(tileId),
                            x * TILE_WIDTH_AND_HEIGHT - topLeftX,
                            y * TILE_WIDTH_AND_HEIGHT - topLeftY);
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

