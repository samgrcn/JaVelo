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


public final class ElevationProfileManager {

    private final Insets distanceFromBorder = new Insets(10, 10, 20, 40);
    private final BorderPane borderPane = new BorderPane();
    private final Pane centerPane = new Pane();
    private final VBox vBox = new VBox();
    private final Line line = new Line();
    private final Polygon polygon = new Polygon();
    private final ReadOnlyObjectProperty<ElevationProfile> elevationProfile;
    private final ReadOnlyDoubleProperty highlightedPosition;
    private final DoubleProperty position = new SimpleDoubleProperty(Double.NaN);
    private final ObjectProperty<Transform> screenToWorld = new SimpleObjectProperty<>();
    private final ObjectProperty<Transform> worldToScreen = new SimpleObjectProperty<>();
    private final ObjectProperty<Rectangle2D> rectangle = new SimpleObjectProperty<>();

    private final static int KM_TO_M = 1000;

    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfile, ReadOnlyDoubleProperty highlightedPosition) {
        this.elevationProfile = elevationProfile;
        this.highlightedPosition = highlightedPosition;

        transformManager();
        updateRectangle();

        setupJavaFX();
        lineBindings();
        addListeners();
        addHandlers();
    }


    public ReadOnlyDoubleProperty mousePositionOnProfileProperty() {
        return position;
    }

    private void transformManager() {
        updateRectangle();
        Affine scaleToWorldAffine = new Affine();

        Point2D topLeft = new Point2D(rectangle.get().getMinX(), rectangle.get().getMinY());


        scaleToWorldAffine.prependTranslation(-topLeft.getX(), -topLeft.getY());

        scaleToWorldAffine.prependScale(
                elevationProfile.get().length() / rectangle.get().getWidth(),
                -(elevationProfile.get().maxElevation() - elevationProfile.get().minElevation()) / rectangle.get().getHeight());

        scaleToWorldAffine.prependTranslation(0, elevationProfile.get().maxElevation());

        screenToWorld.set(scaleToWorldAffine);

        try {
            worldToScreen.set(scaleToWorldAffine.createInverse());
        } catch (NonInvertibleTransformException e) {
            throw new Error(e); //if scaleToWorldAffine equals to 0
        }
    }

    private void updateRectangle() {
        rectangle.set(new Rectangle2D(
                0 + distanceFromBorder.getLeft(),
                0 + distanceFromBorder.getTop(),
                Math.max(0, centerPane.getWidth() - (distanceFromBorder.getRight() + distanceFromBorder.getLeft())),
                Math.max(0, centerPane.getHeight() - (distanceFromBorder.getTop() + distanceFromBorder.getBottom()))));
    }

    private void polygonCreator() {
        Point2D worldPoint;
        Point2D screenPoint;

        polygon.getPoints().clear();

        polygon.getPoints().add(rectangle.get().getMinX());
        polygon.getPoints().add(rectangle.get().getMaxY());

        for (int x = (int) rectangle.get().getMinX(); x < rectangle.get().getMaxX(); x++) {
            worldPoint = screenToWorld.get().transform(x, 0);
            screenPoint = worldToScreen.get().transform(0, elevationProfile.get().elevationAt(worldPoint.getX()));
            polygon.getPoints().add((double) x);
            polygon.getPoints().add(screenPoint.getY());
        }
        polygon.getPoints().add(rectangle.get().getMaxX());
        polygon.getPoints().add(rectangle.get().getMaxY());


    }

    private void lineBindings() {
        line.layoutXProperty().bind(
                Bindings.createDoubleBinding(() -> worldToScreen.get().transform(highlightedPosition.get(), 0).getX(), highlightedPosition, worldToScreen));
        line.startYProperty().bind(Bindings.select(rectangle, "minY"));

        line.endYProperty().bind(Bindings.select(rectangle, "maxY"));
        line.visibleProperty().bind(highlightedPosition.greaterThanOrEqualTo(0));
    }

    private void addListeners() {
        centerPane.widthProperty().addListener((p, oldS, newS) -> {
            updateRectangle();
        });

        centerPane.heightProperty().addListener((p, oldS, newS) -> {
            updateRectangle();
            System.out.println(rectangle.get().getMaxY());
        });

        elevationProfile.addListener(e -> {
            updateRectangle();
        });

    }

    private void addHandlers() {
        centerPane.setOnMouseMoved(e -> {
            if (rectangle.get().contains(e.getX(), e.getY())) {
                position.set(screenToWorld.get().transform(e.getX(), 0).getX());
            } else {
                position.set(Double.NaN);
            }
        });

        centerPane.setOnMouseExited(e -> {
            position.set(Double.NaN);
        });

        rectangle.addListener(e -> {
            transformManager();
            polygonCreator();
        });
    }

    private void setupJavaFX() {
        borderPane.getStylesheets().add("elevation_profile.css");
        polygon.setId("profile");

        centerPane.getChildren().add(polygon);
        centerPane.getChildren().add(line);

        borderPane.setCenter(centerPane);
        borderPane.setBottom(vBox);
    }

    public Pane pane() {
        return borderPane;
    }
}
