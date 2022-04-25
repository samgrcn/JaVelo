package ch.epfl.javelo.gui;


import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static ch.epfl.javelo.Math2.clamp;

public final class BaseMapManager {

    private WaypointsManager waypointsManager;
    private Pane pane;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private TileManager tileManager;
    private ObjectProperty<MapViewParameters> parameters;
    private boolean redrawNeeded;

    private static final int TILE_WIDTH_AND_HEIGHT = 256;


    public BaseMapManager(TileManager tileManager, WaypointsManager waypointsManager, ObjectProperty<MapViewParameters> parameters) {


        this.canvas = new Canvas();
        this.pane = new Pane();

        this.waypointsManager = waypointsManager;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileManager = tileManager;
        this.parameters = parameters;

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });

        redrawIfNeeded();

        redrawOnNextPulse();
    }

    public Node pane() {
        return pane;
    }

    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;


        double xMin = parameters.get().topLeft().getX();
        double xMax = xMin + canvas.getWidth();
        double yMax = parameters.get().topLeft().getY();
        double yMin = yMax - canvas.getHeight();

        double xMinTile = xMin / TILE_WIDTH_AND_HEIGHT;
        double xMaxTile = xMax / TILE_WIDTH_AND_HEIGHT;
        double yMinTile = yMin / TILE_WIDTH_AND_HEIGHT;
        double yMaxTile = yMax / TILE_WIDTH_AND_HEIGHT;

        double xMinSector = clamp(0, xMinTile, canvas.getWidth() / TILE_WIDTH_AND_HEIGHT);
        double xMaxSector = clamp(0, xMaxTile, canvas.getWidth() / TILE_WIDTH_AND_HEIGHT);
        double yMinSector = clamp(0, yMinTile, canvas.getHeight() / TILE_WIDTH_AND_HEIGHT);
        double yMaxSector = clamp(0, yMaxTile, canvas.getHeight() / TILE_WIDTH_AND_HEIGHT);


        for (int y = (int) yMinSector; y <= yMaxSector; y++) {
            for (int x = (int) xMinSector; x <= xMaxSector; x++) {
                TileManager.TileId tileId = new TileManager.TileId(parameters.get().zoomAt(), x * 256, y * 256);
                try {
                    graphicsContext.drawImage(tileManager.imageForTileAt(tileId), tileId.x(), tileId.y());
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
