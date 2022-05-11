package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;


public final class ElevationProfileManager {

    private final Insets distanceFromBorder = new Insets(10, 10, 20, 40);
    private final BorderPane borderPane = new BorderPane();
    private final Pane pane = new Pane();
    private final VBox vBox = new VBox();
    private Line line;
    private Polygon polygon;
    private final ReadOnlyObjectProperty<ElevationProfile> elevationProfile;
    ReadOnlyDoubleProperty position = new SimpleDoubleProperty();
    ObjectProperty<Transform> screenToWorld = new SimpleObjectProperty<>();
    ObjectProperty<Transform> worldToScreen = new SimpleObjectProperty<>();
    ObjectProperty<Rectangle2D> rectangle = new SimpleObjectProperty<>();

    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfile, ReadOnlyDoubleProperty position) {
        pane.setPrefSize(600, 300);
        this.elevationProfile = elevationProfile;
        scaleManager();

        pane.widthProperty().addListener(e -> {
            updateRectangle();
        });

        pane.heightProperty().addListener(e -> {
            updateRectangle();
        });

        borderPane.setCenter(pane);
        borderPane.setBottom(vBox);


        Bindings.createDoubleBinding(() -> { return  });
        line.layoutXProperty().bind();

        ;
    }

    public ReadOnlyDoubleProperty mousePositionOnProfileProperty() {
        return null;
    }

    private void scaleManager() {

        updateRectangle();
        Affine scaleToWorldAffine = new Affine();

        Point2D topLeft = new Point2D(rectangle.get().getMinX(), rectangle.get().getMaxY());

        scaleToWorldAffine.prependTranslation(-topLeft.getX(), -topLeft.getY());
        scaleToWorldAffine.prependScale(
                elevationProfile.get().length() / rectangle.get().getWidth(),
                -(elevationProfile.get().maxElevation() - elevationProfile.get().minElevation()) / rectangle.get().getHeight());
        scaleToWorldAffine.prependTranslation(topLeft.getX(), topLeft.getY());

        screenToWorld.set(scaleToWorldAffine);

        System.out.println(screenToWorld.get().transform(new Point2D(40, 10)));

        try {
            worldToScreen.set(scaleToWorldAffine.createInverse());
        } catch (NonInvertibleTransformException e) {
            throw new Error(e); //if scaleToWorldAffine equals to 0
        }

    }

    private void updateRectangle() {
        rectangle.set(new Rectangle2D(
                0 + distanceFromBorder.getLeft(),
                0 + distanceFromBorder.getBottom(),
                Math.max(0, pane.getMaxWidth() - (distanceFromBorder.getRight() + distanceFromBorder.getLeft())),
                Math.max(0, pane.getMaxHeight() - (distanceFromBorder.getTop() + distanceFromBorder.getBottom()))));
    }

    private void polygonCreator() {
        Point2D worldPoint;
        Point2D screenPoint;
        List<Point2D> listOfPoints = new ArrayList<>();
        for (int x = (int) rectangle.get().getMinX(); x < rectangle.get().getMaxX(); x++) {
            worldPoint = screenToWorld.get().transform(x, 0);
            screenPoint = worldToScreen.get().transform(x, elevationProfile.get().elevationAt(worldPoint.getX()));
            listOfPoints.add(screenPoint);
        }
        polygon.getPoints().setAll(listOfPoints);


    }

    public Pane pane() {
        return borderPane;
    }
}
