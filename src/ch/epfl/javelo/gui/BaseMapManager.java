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


        canvas.setHeight(300);
        canvas.setWidth(600);
        pane.getChildren().add(canvas);

        pane.setPrefSize(600, 300);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });

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
        double yMin = parameters.get().topLeft().getY();
        double yMax = yMin + canvas.getHeight();

        double xMinTile = xMin / TILE_WIDTH_AND_HEIGHT;
        double xMaxTile = xMax / TILE_WIDTH_AND_HEIGHT;
        double yMinTile = yMin / TILE_WIDTH_AND_HEIGHT;
        double yMaxTile = yMax / TILE_WIDTH_AND_HEIGHT;

        for (int y = (int) yMinTile; y <= yMaxTile; y++) {
            for (int x = (int) xMinTile; x <= xMaxTile; x++) {
                TileManager.TileId tileId = new TileManager.TileId(parameters.get().zoomAt(), x, y);
                try {
                    graphicsContext.drawImage(
                            tileManager.imageForTileAt(tileId),
                            x*TILE_WIDTH_AND_HEIGHT - xMin ,
                            y*TILE_WIDTH_AND_HEIGHT - yMin);
                } catch (IOException ignored) {}

            }
        }
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

}
