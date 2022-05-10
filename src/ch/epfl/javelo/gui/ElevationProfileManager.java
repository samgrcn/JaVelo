package ch.epfl.javelo.gui;

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
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;


public final class ElevationProfileManager {

    private final Pane pane = new Pane();
    private final BorderPane borderPane = new BorderPane();
    private ReadOnlyObjectProperty<ElevationProfile> elevationProfile = new SimpleObjectProperty<>();

    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfile, ReadOnlyDoubleProperty position) {
        this.elevationProfile = elevationProfile;
    }

    public ReadOnlyDoubleProperty mousePositionOnProfileProperty() {
        return null;
    }

    private void rectangleManager() {
        Insets distanceFromBorder = new Insets(10, 10, 20, 40);
        ObjectProperty<Transform> screenToWorld = new SimpleObjectProperty<>();
        ObjectProperty<Transform> worldToScreen = new SimpleObjectProperty<>();
        ObjectProperty<Rectangle2D> rectangle = new SimpleObjectProperty<>();

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

        public Node pane() {
        return pane;
    }
}
