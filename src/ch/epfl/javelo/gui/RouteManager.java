package ch.epfl.javelo.gui;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

public final class RouteManager {

    private final Pane pane = new Pane();
    private final RouteBean bean;
    private final ObjectProperty<MapViewParameters> parameters;
    private final Consumer<String> errorConsumer;

    public RouteManager(RouteBean bean, ObjectProperty<MapViewParameters> parameters, Consumer<String> errorConsumer) {
        this.bean = bean;
        this.parameters = parameters;
        this.errorConsumer = errorConsumer;

        parameters.addListener(change -> {
            update();
        });
    }

    public Pane pane() {
        return pane;
    }

    private void update() {

    }
}
