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
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;


public final class ElevationProfileManager {

    private final Insets distanceFromBorder = new Insets(10, 10, 20, 40);
    private final BorderPane borderPane = new BorderPane();
    private final Pane centerPane = new Pane();
    private final VBox vBox = new VBox();
    private final Line line = new Line();
    private final Path gridNode = new Path();
    private final Polygon polygon = new Polygon();
    private final ReadOnlyObjectProperty<ElevationProfile> elevationProfile;
    private final ReadOnlyDoubleProperty highlightedPosition;
    private final DoubleProperty position = new SimpleDoubleProperty(Double.NaN);
    private final ObjectProperty<Transform> screenToWorld = new SimpleObjectProperty<>();
    private final ObjectProperty<Transform> worldToScreen = new SimpleObjectProperty<>();
    private final ObjectProperty<Rectangle2D> rectangle = new SimpleObjectProperty<>();

    private final static int[] POS_STEPS = { 1000, 2000, 5000, 10_000, 25_000, 50_000, 100_000 };
    private final static int[] ELE_STEPS = { 5, 10, 20, 25, 50, 100, 200, 250, 500, 1_000 };
    private final static int HORIZONTAL_LINES_MIN = 25;
    private final static int VERTICAL_LINES_MIN = 50;


    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfile, ReadOnlyDoubleProperty highlightedPosition) {
        this.elevationProfile = elevationProfile;
        this.highlightedPosition = highlightedPosition;

        transformManager();
        updateRectangle();
        gridManager();

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
            gridManager();
        });

        centerPane.heightProperty().addListener((p, oldS, newS) -> {
            updateRectangle();
            gridManager();
        });

        elevationProfile.addListener(e -> {
            updateRectangle();
            gridManager();
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
        centerPane.getChildren().add(gridNode);

        borderPane.setCenter(centerPane);
        borderPane.setBottom(vBox);
    }

    private void gridManager() {
        gridNode.getElements().clear();

        double minEle = elevationProfile.get().minElevation();
        double maxEle = elevationProfile.get().maxElevation();
        System.out.println(minEle + " " + maxEle);

        int posDiff = 100_000;
        for (int el : POS_STEPS) {
            if (worldToScreen.get().deltaTransform(el, 0).getX() >= VERTICAL_LINES_MIN) {
                posDiff = el;
                break;
            }
        }
        int eleDiff = 1_000;
        for (int el : ELE_STEPS) {
            System.out.println(screenToWorld.get().deltaTransform(0, -el));
            if (worldToScreen.get().deltaTransform(0, -el).getY() >= HORIZONTAL_LINES_MIN) {
                eleDiff = el;
                break;
            }
        }

        int firstStepEle = (int) Math.round(minEle/eleDiff) * eleDiff;
        int lastStepEle = (int) Math.round(maxEle/eleDiff) * eleDiff;
        int numberOfHorizontalLines = (lastStepEle - firstStepEle) / eleDiff;
        int numberOfVerticalLines = (int) (elevationProfile.get().length() / posDiff);

        for (int i = 1; i <= 6; ++i) {
            double y = rectangle.get().getMinY() + i * 50;
            gridNode.getElements().add(new MoveTo(rectangle.get().getMinX(), worldToScreen.get().deltaTransform(0, -y).getY()));
            gridNode.getElements().add(new LineTo(rectangle.get().getMaxX(), worldToScreen.get().deltaTransform(0, -y).getY()));
        }
        for (int i = 1; i <= 10; ++i) {
            double x = rectangle.get().getMinX() + i * posDiff;
            gridNode.getElements().add(new MoveTo(worldToScreen.get().deltaTransform(x, 0).getX(), rectangle.get().getMinY()));
            gridNode.getElements().add(new LineTo(worldToScreen.get().deltaTransform(x, 0).getX(), rectangle.get().getMaxY()));
        }

        System.out.println(posDiff + " " + eleDiff);
        System.out.println(firstStepEle + " " + lastStepEle + " " + numberOfHorizontalLines + " " + numberOfVerticalLines);
    }

    public Pane pane() {
        return borderPane;
    }
}
