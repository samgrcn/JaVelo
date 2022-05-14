package ch.epfl.javelo.gui;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;

public final class ErrorManager {

    private final VBox vBox = new VBox();
    private final Text text = new Text();

    private final ObjectProperty<SequentialTransition> oldTransition = new SimpleObjectProperty<>();

    public ErrorManager() {
        vBox.getStylesheets().add("error.css");
        vBox.setMouseTransparent(true);
        vBox.getChildren().add(text);
    }

    public void displayError(String errorMessage) {
        if (oldTransition != null) {
            oldTransition.get().stop();
        }
        Toolkit.getDefaultToolkit().beep();
        text.setText(errorMessage);

        SequentialTransition transition = setupTransitions();
        transition.play();
        oldTransition.set(transition);
    }

    public Pane pane() {
        return vBox;
    }

    private SequentialTransition setupTransitions() {
        FadeTransition firstFade = new FadeTransition();
        FadeTransition secondFade = new FadeTransition();
        PauseTransition pause = new PauseTransition();
        SequentialTransition sequential = new SequentialTransition(vBox, firstFade, pause, secondFade);

        firstFade.setNode(vBox);
        firstFade.setFromValue(0);
        firstFade.setToValue(0.8);
        firstFade.setDuration(Duration.millis(200));

        secondFade.setNode(vBox);
        secondFade.setFromValue(0.8);
        secondFade.setToValue(0);
        secondFade.setDuration(Duration.millis(500));

        pause.setDuration(Duration.millis(2000));

        return sequential;
    }
}
