package ch.epfl.javelo.gui;


import ch.epfl.javelo.Math2;
import ch.epfl.javelo.projection.PointWebMercator;
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
    private MapViewParameters parameters;
    private boolean redrawNeeded;

    private static final int TILE_WIDTH_AND_HEIGHT = 256;


    public BaseMapManager(TileManager tileManager, WaypointsManager waypointsManager, ObjectProperty<MapViewParameters> parameters) {


        this.canvas = new Canvas();
        this.pane = new Pane();

        this.waypointsManager = waypointsManager;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.tileManager = tileManager;
        this.parameters = parameters.get();

        canvas.setHeight(300);
        canvas.setWidth(600);

        pane.getChildren().add(canvas);

        pane.setPrefSize(600, 300);

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.setOnScroll(e -> {
            double zoomDelta = e.getDeltaY();

            zoomDelta = zoomDelta / Math.abs(zoomDelta);

            int newZoomLevel = this.parameters.zoomAt() + (int) zoomDelta;
            newZoomLevel = Math2.clamp(8, newZoomLevel, 19);

            if (newZoomLevel != this.parameters.zoomAt()) {

                double x = this.parameters.topLeft().getX() + e.getX();
                double y = this.parameters.topLeft().getY() + e.getY();
                double zoomX = Math.scalb(x, (int) zoomDelta);
                double zoomY = Math.scalb(y, (int) zoomDelta);
                this.parameters = new MapViewParameters(newZoomLevel, zoomX - e.getX(), zoomY - e.getY());
            }
        });

        pane.setOnMouseDragged(drag -> {


        });

        pane.setOnMouseClicked(click -> {

            if (click.isStillSincePress()) {
                this.waypointsManager.addWaypoint(
                        this.parameters.topLeft().getX() + click.getX(),
                        this.parameters.topLeft().getY() + click.getY());
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

    //private Point2D getPoint() {
   //     return point.get();
   // }

  //  private void setPoint(Point2D point) {
  //      this.point.set(point);
  //  }
  //  double oldX = getPoint().getX();
  //  double oldY = getPoint().getY();

 //   setPoint(new Point2D(drag.getX(), drag.getY()));
  //          System.out.println(this.parameters.toString());
  //          this.parameters.set(new MapViewParameters(this.parameters.get().zoomAt(),
  //  topLeftX + (oldX - getPoint().getX()),
  //  topLeftY + (oldY - getPoint().getY())));
  //          System.out.println(this.parameters.toString());



    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;

        double xMax = this.parameters.topLeft().getX() + canvas.getWidth();
        double yMax = this.parameters.topLeft().getY() + canvas.getHeight();

        double xMinTile = this.parameters.topLeft().getX() / TILE_WIDTH_AND_HEIGHT;
        double xMaxTile = xMax / TILE_WIDTH_AND_HEIGHT;
        double yMinTile = this.parameters.topLeft().getY() / TILE_WIDTH_AND_HEIGHT;
        double yMaxTile = yMax / TILE_WIDTH_AND_HEIGHT;

        for (int y = (int) yMinTile; y <= yMaxTile; y++) {
            for (int x = (int) xMinTile; x <= xMaxTile; x++) {
                TileManager.TileId tileId = new TileManager.TileId(this.parameters.zoomAt(), x, y);
                try {
                    graphicsContext.drawImage(
                            tileManager.imageForTileAt(tileId),
                            x * TILE_WIDTH_AND_HEIGHT - this.parameters.topLeft().getX(),
                            y * TILE_WIDTH_AND_HEIGHT - this.parameters.topLeft().getY());
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

