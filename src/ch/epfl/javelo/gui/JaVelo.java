package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.routing.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * JaVelo is the main class of the application.
 *
 * @author Samuel Garcin (345633)
 */
public final class JaVelo extends Application {

    private static final int PREF_WIDTH = 800;
    private static final int PREF_HEIGHT = 600;

    public JaVelo() {}

    /**
     * Main method.
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Builds the final GUI by combining the parts managed by the previously written classes and adding a menu.
     *
     * @param primaryStage the primary stage
     * @throws IOException in case of an input/output error
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Graph graph = Graph.loadFrom(Path.of("javelo-data"));
        Path cacheBasePath = Path.of("osm-cache");
        String tileServerHost = "tile.openstreetmap.org";

        TileManager tileManager =
                new TileManager(cacheBasePath, tileServerHost);

        CostFunction cf = new CityBikeCF(graph);
        RouteComputer routeComputer = new RouteComputer(graph, cf);
        RouteBean routeBean = new RouteBean(routeComputer);
        ErrorManager errorManager = new ErrorManager();

        AnnotatedMapManager stackMap = new AnnotatedMapManager(graph, tileManager,
                routeBean, errorManager::displayError);


        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Fichier");
        MenuItem menuItem = new MenuItem("Exporter GPX");
        menuBar.getMenus().add(menu);
        menu.getItems().add(menuItem);
        menuBar.setUseSystemMenuBar(true);

        menuItem.setDisable(true);

        menuItem.setOnAction(click -> {
            try {
                GpxGenerator.writeGpx("javelo.gpx", routeBean.route(), routeBean.elevationProfile());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        ElevationProfileManager profileManager = new ElevationProfileManager(
                routeBean.elevationProfileProperty(), routeBean.highlightedPositionProperty());
        SplitPane.setResizableWithParent(profileManager.pane(), false);

        routeBean.highlightedPositionProperty().bind(Bindings.when(
                        stackMap.mousePositionOnRouteProperty().greaterThanOrEqualTo(0))
                .then(stackMap.mousePositionOnRouteProperty())
                .otherwise(profileManager.mousePositionOnProfileProperty()));

        SplitPane splitPane = new SplitPane(stackMap.pane());
        splitPane.setOrientation(Orientation.VERTICAL);
        StackPane stackPane = new StackPane(splitPane, errorManager.pane());
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(stackPane);


        routeBean.elevationProfileProperty().addListener((e, oldValue, newValue) -> {
            if (oldValue == null && newValue != null) {
                menuItem.setDisable(false);
                splitPane.getItems().add(profileManager.pane());
            }
            if (oldValue != null && newValue == null) {
                menuItem.setDisable(true);
                splitPane.getItems().remove(profileManager.pane());
            }
        });


        primaryStage.setMinWidth(PREF_WIDTH);
        primaryStage.setMinHeight(PREF_HEIGHT);
        show(primaryStage, borderPane);
    }

    /**
     * Shows the given primary stage and pane.
     *
     * @param primaryStage the primary stage
     * @param pane the pane to show
     */
    private void show(Stage primaryStage, Pane pane) {
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
}
