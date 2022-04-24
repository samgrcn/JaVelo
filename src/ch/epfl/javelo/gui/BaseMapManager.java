package ch.epfl.javelo.gui;


import ch.epfl.javelo.data.GraphSectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static ch.epfl.javelo.Math2.clamp;

public final class BaseMapManager {

    private boolean redrawNeeded;
    private Pane pane;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private TileManager tileManager;
    private ObjectProperty<MapViewParameters> parameters;

    private static final int TILE_WIDTH_AND_HEIGHT = 256;


    public BaseMapManager(TileManager tileManager, boolean redrawNeeded, ObjectProperty<MapViewParameters> parameters) {


        this.canvas = new Canvas();
        this.pane = new Pane();

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        this.redrawNeeded = redrawNeeded;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileManager = tileManager;
        this.parameters = parameters;

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });

        redrawOnNextPulse();
    }

    private Pane getPane() {
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
                TileManager.TileId tileId = new TileManager.TileId(parameters.get().zoomAt(), x, y);
                try {
                    graphicsContext.drawImage(tileManager.imageForTileAt(tileId), x, y);
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
