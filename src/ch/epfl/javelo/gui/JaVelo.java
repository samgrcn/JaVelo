package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.routing.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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

public final class JaVelo extends Application {


    public JaVelo() {
    }

    public static void main(String[] args) {
        launch(args);
    }

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

        AnnotatedMapManager stackMap = new AnnotatedMapManager(graph, tileManager, routeBean, errorManager::displayError);


        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Fichier");
        MenuItem menuItem = new MenuItem("Exporter GPX");
        menuBar.getMenus().add(menu);
        menu.getItems().add(menuItem);
        menuBar.setUseSystemMenuBar(true);

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
                splitPane.getItems().add(profileManager.pane());
            }
            if (oldValue != null && newValue == null) {
                splitPane.getItems().remove(profileManager.pane());
            }
        });


        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        show(primaryStage, borderPane);
    }

    private void show(Stage primaryStage, Pane pane) {
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }



}
