package ch.epfl.javelo.gui;

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

/**
 * Manages the display of error messages
 *
 * @author Samuel Garcin (345633)
 */
public final class ErrorManager {

    public static final double MAX_FADE = 0.8;
    public static final double MIN_FADE = 0;
    public static final double DURATION_FIRST_FADE = 200;
    public static final double DURATION_SECOND_FADE = 500;
    public static final double DURATION_PAUSE = 2000;

    private final VBox vBox = new VBox();
    private final Text text = new Text();

    private final ObjectProperty<SequentialTransition> oldTransition = new SimpleObjectProperty<>();

    /**
     * Constructor of ErrorManager, setting up java fx, so it displays correctly.
     */
    public ErrorManager() {
        vBox.getStylesheets().add("error.css");
        vBox.setMouseTransparent(true);
        vBox.getChildren().add(text);
    }

    /**
     * Makes the error message appear temporarily on the screen, accompanied by a sound indicating the error
     * @param errorMessage the message you want to display in the error
     */
    public void displayError(String errorMessage) {
        if(oldTransition.get() != null) {
            oldTransition.get().stop();
        }
        Toolkit.getDefaultToolkit().beep();
        text.setText(errorMessage);

        SequentialTransition transition = setupTransitions();
        transition.play();
        oldTransition.set(transition);
    }

    /**
     * @return the pane of the error message.
     */
    public Pane pane() {
        return vBox;
    }

    /**
     * Handles the transitions with the correct values
     * @return the transition made one fade, then a pause, then another fade where the error message disappears.
     */
    private SequentialTransition setupTransitions() {
        FadeTransition firstFade = new FadeTransition();
        FadeTransition secondFade = new FadeTransition();
        PauseTransition pause = new PauseTransition();
        SequentialTransition sequential = new SequentialTransition(vBox, firstFade, pause, secondFade);

        firstFade.setNode(vBox);
        firstFade.setFromValue(MIN_FADE);
        firstFade.setToValue(MAX_FADE);
        firstFade.setDuration(Duration.millis(DURATION_FIRST_FADE));

        secondFade.setNode(vBox);
        secondFade.setFromValue(MAX_FADE);
        secondFade.setToValue(MIN_FADE);
        secondFade.setDuration(Duration.millis(DURATION_SECOND_FADE));

        pause.setDuration(Duration.millis(DURATION_PAUSE));

        return sequential;
    }
}
