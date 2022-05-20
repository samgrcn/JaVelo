package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.Locale;


public final class ElevationProfileManager {

    private final static int[] POS_STEPS = {1000, 2000, 5000, 10_000, 25_000, 50_000, 100_000};
    private final static int[] ELE_STEPS = {5, 10, 20, 25, 50, 100, 200, 250, 500, 1_000};
    private final static int HORIZONTAL_LINES_MIN = 25;
    private final static int VERTICAL_LINES_MIN = 50;
    private final static int NB_OF_METERS_PER_KM = 1000;
    private final static int WIDTH_CORRECTION = 2;
    private final static Font font = Font.font("Avenir", 10);

    private final Insets distanceFromBorder = new Insets(10, 10, 20, 40);
    private final BorderPane borderPane = new BorderPane();
    private final Pane centerPane = new Pane();
    private final VBox vBox = new VBox();
    private final Line line = new Line();
    private final Path gridNode = new Path();
    private final Group tags = new Group();
    private final Polygon polygon = new Polygon();
    private final ReadOnlyObjectProperty<ElevationProfile> elevationProfile;
    private final ReadOnlyDoubleProperty highlightedPosition;
    private final DoubleProperty position = new SimpleDoubleProperty(Double.NaN);
    private final ObjectProperty<Transform> screenToWorld = new SimpleObjectProperty<>();
    private final ObjectProperty<Transform> worldToScreen = new SimpleObjectProperty<>();
    private final ObjectProperty<Rectangle2D> rectangle = new SimpleObjectProperty<>();


    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfile, ReadOnlyDoubleProperty highlightedPosition) {
        this.elevationProfile = elevationProfile;
        this.highlightedPosition = highlightedPosition;
        setupJavaFX();
        addListeners();
        addHandlers();
    }


    public ReadOnlyDoubleProperty mousePositionOnProfileProperty() {
        return position;
    }

    private void transformManager() {
        if (elevationProfile.get() != null) {

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
    }

    private void updateRectangle() {
        rectangle.set(new Rectangle2D(
                distanceFromBorder.getLeft(),
                distanceFromBorder.getTop(),
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
        if(worldToScreen.get() != null) {
            line.layoutXProperty().bind(
                    Bindings.createDoubleBinding(() -> worldToScreen.get().transform(highlightedPosition.get(), 0).getX(), highlightedPosition, worldToScreen));
            line.startYProperty().bind(Bindings.select(rectangle, "minY"));

            line.endYProperty().bind(Bindings.select(rectangle, "maxY"));
            line.visibleProperty().bind(highlightedPosition.greaterThanOrEqualTo(0));
        }
    }

    private void addListeners() {
        centerPane.widthProperty().addListener((p, oldS, newS) -> {
            if (elevationProfile.get() != null) {
                updateRectangle();
                transformManager();
                polygonCreator();
                gridManager();
            }
        });

        centerPane.heightProperty().addListener((p, oldS, newS) -> {
            if (elevationProfile.get() != null) {
                updateRectangle();
                transformManager();
                polygonCreator();
                gridManager();
            }
        });

        elevationProfile.addListener((e, oldV, newV) -> {
            if(elevationProfile.get() != null) {
                updateRectangle();
                transformManager();
                polygonCreator();
                gridManager();
                stats();
            }
            if(oldV == null && newV != null) {
                lineBindings();
            }
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

    }


    private void setupJavaFX() {
        borderPane.getStylesheets().add("elevation_profile.css");
        polygon.setId("profile");
        gridNode.setId("grid");
        vBox.setId("profile_data");

        centerPane.getChildren().addAll(gridNode, polygon, line, tags, vBox);

        borderPane.setCenter(centerPane);
        borderPane.setBottom(vBox);
    }

    private void gridManager() {
        if (elevationProfile.get() != null && worldToScreen.get() != null) {

            gridNode.getElements().clear();
            tags.getChildren().clear();

            double minEle = elevationProfile.get().minElevation();
            double maxEle = elevationProfile.get().maxElevation();

            int posDiff = 100_000;
            for (int el : POS_STEPS) {
                if (worldToScreen.get().deltaTransform(el, 0).getX() >= VERTICAL_LINES_MIN) {
                    posDiff = el;
                    break;
                }
            }
            int eleDiff = 1_000;
            for (int el : ELE_STEPS) {
                if (worldToScreen.get().deltaTransform(0, -el).getY() >= HORIZONTAL_LINES_MIN) {
                    eleDiff = el;
                    break;
                }
            }

            int firstStepEle = (int) Math.ceil(minEle / eleDiff) * eleDiff;
            int lastStepEle = (int) (Math.ceil(maxEle / eleDiff) - 1) * eleDiff;
            int numberOfHorizontalLines = (lastStepEle - firstStepEle) / eleDiff;
            int numberOfVerticalLines = (int) (elevationProfile.get().length() / posDiff);

            double delta = firstStepEle - minEle;

            for (int i = 0; i <= numberOfHorizontalLines; ++i) {
                double y = rectangle.get().getMaxY() - worldToScreen.get().deltaTransform(0, -delta).getY()
                        - worldToScreen.get().deltaTransform(0, -i * eleDiff).getY();
                gridNode.getElements().add(new MoveTo(rectangle.get().getMinX(), y));
                gridNode.getElements().add(new LineTo(rectangle.get().getMaxX(), y));
                setTagEle(firstStepEle, i, eleDiff, y);
            }
            for (int i = 0; i <= numberOfVerticalLines; ++i) {
                double x = rectangle.get().getMinX() + worldToScreen.get().deltaTransform(i * posDiff, 0).getX();
                gridNode.getElements().add(new MoveTo(x, rectangle.get().getMinY()));
                gridNode.getElements().add(new LineTo(x, rectangle.get().getMaxY()));
                setTagPos(i, posDiff, x);
            }
        }
    }

    private void setTagEle(int firstStepEle, int index, int eleDiff, double y) {
        Text tag = new Text(String.valueOf(firstStepEle + index * eleDiff));
        tag.textOriginProperty().set(VPos.CENTER);
        tag.setId("grid_label");
        tag.getStyleClass().add("vertical");
        tag.setFont(font);
        tag.setLayoutX(rectangle.get().getMinX() - tag.prefWidth(0) - WIDTH_CORRECTION);
        tag.setLayoutY(y);
        tags.getChildren().add(tag);
    }

    private void setTagPos(int index, int posDiff, double x) {
        Text tag = new Text(String.valueOf((index * posDiff) / NB_OF_METERS_PER_KM));
        tag.textOriginProperty().set(VPos.TOP);
        tag.setId("grid_label");
        tag.getStyleClass().add("horizontal");
        tag.setFont(font);
        tag.setLayoutX(x - tag.prefWidth(0) / WIDTH_CORRECTION);
        tag.setLayoutY(rectangle.get().getMaxY());
        tags.getChildren().add(tag);
    }

    private void stats() {
        if (elevationProfile.get() != null) {
            vBox.getChildren().clear();
            ElevationProfile elevationProfileAttribute = elevationProfile.get();
            String text = String.format(Locale.ROOT, "Longueur : %.1f km" +
                            "     Montée : %.0f m" +
                            "     Descente : %.0f m" +
                            "     Altitude : de %.0f m à %.0f m", elevationProfileAttribute.length() / NB_OF_METERS_PER_KM,
                    elevationProfileAttribute.totalAscent(), elevationProfileAttribute.totalDescent(),
                    elevationProfileAttribute.minElevation(), elevationProfileAttribute.maxElevation());
            Text stats = new Text(text);
            stats.setFont(font);
            vBox.getChildren().add(stats);
        }
    }

    public Pane pane() {
        return borderPane;
    }
}
