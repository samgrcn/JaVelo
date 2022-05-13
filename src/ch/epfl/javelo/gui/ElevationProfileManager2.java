package ch.epfl.javelo.gui;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.routing.ElevationProfile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;


public final class ElevationProfileManager2 {

    private final static int[] POS_STEPS = { 1000, 2000, 5000, 10_000, 25_000, 50_000, 100_000 };
    private final static int[] ELE_STEPS = { 5, 10, 20, 25, 50, 100, 200, 250, 500, 1_000 };
    private final static int HORIZONTAL_LINES_MIN = 25;
    private final static int VERTICAL_LINES_MIN = 50;

    private final Path gridNode = new Path();
    private final Pane pane = new Pane();
    private final BorderPane borderPane = new BorderPane();
    private ReadOnlyObjectProperty<ElevationProfile> elevationProfile;
    private final Insets distanceFromBorder = new Insets(10, 10, 20, 40);
    private final ObjectProperty<Transform> screenToWorld = new SimpleObjectProperty<>();
    private final ObjectProperty<Transform> worldToScreen = new SimpleObjectProperty<>();
    private final ObjectProperty<Rectangle2D> rectangle = new SimpleObjectProperty<>();

    public ElevationProfileManager2(ReadOnlyObjectProperty<ElevationProfile> elevationProfile, ReadOnlyDoubleProperty position) {
        this.elevationProfile = elevationProfile;
    }

    public ReadOnlyDoubleProperty mousePositionOnProfileProperty() {
        return null;
    }

    private void rectangleManager() {
        rectangle.set(new Rectangle2D(
                0 + distanceFromBorder.getLeft(),
                0 + distanceFromBorder.getBottom(),
                borderPane.getWidth() - distanceFromBorder.getRight(),
                borderPane.getHeight() - distanceFromBorder.getTop()));

        Affine scaleToWorldAffine = new Affine();
        double x = 0;
        double y = 0;
        Point2D point = new Point2D(x, y);

        scaleToWorldAffine.prependTranslation(-point.getX(), -point.getY());

        double longueurX = 5;
        double longueurY = 5;

        scaleToWorldAffine.prependScale(rectangle.get().getWidth() / longueurX, -(rectangle.get().getHeight() / longueurY));

        scaleToWorldAffine.prependTranslation(x, y);


        screenToWorld.set(scaleToWorldAffine);

        Affine inverse = new Affine();

        try {
            inverse = scaleToWorldAffine.createInverse();
        } catch (NonInvertibleTransformException e) {
            throw new Error(e); //if scaleToWorldAffine equals to 0
        }
    }

    private void gridManager() {
        double minEle = elevationProfile.get().minElevation();
        double maxEle = elevationProfile.get().maxElevation();

        int posDiff = 100_000;
        for (int el : POS_STEPS) {
            System.out.println(worldToScreen.get());
            System.out.println(worldToScreen.get().deltaTransform(el, 0));
            System.out.println(worldToScreen.get().deltaTransform(0, el));
            if (worldToScreen.get().deltaTransform(el, 0).getX() >= VERTICAL_LINES_MIN) {
                posDiff = el;
                break;
            }
        }
        int eleDiff = 1_000;
        for (int el : ELE_STEPS) {
            if (worldToScreen.get().deltaTransform(el, 0).getX() >= HORIZONTAL_LINES_MIN) {
                eleDiff = el;
                break;
            }
        }

        int firstStepEle = (int) (minEle/eleDiff) * eleDiff;
        int lastStepEle = (int) (maxEle/eleDiff) * eleDiff;
        int numberOfHorizontalLines = (lastStepEle - firstStepEle) / eleDiff - 1;
        int numberOfVerticalLines = (int) (elevationProfile.get().length() / posDiff) - 1;
        System.out.println(posDiff + " " + eleDiff);
        System.out.println(firstStepEle + " " + lastStepEle + " " + numberOfHorizontalLines + " " + numberOfVerticalLines);
    }

    public Node pane() {
        return pane;
    }
}

