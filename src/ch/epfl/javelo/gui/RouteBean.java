package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import ch.epfl.javelo.routing.ElevationProfileComputer;
import ch.epfl.javelo.routing.Route;
import ch.epfl.javelo.routing.RouteComputer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a JavaFX bean containing properties relating to waypoints and the corresponding route.
 *
 * @author Quentin Chappuis (339517)
 */
public final class RouteBean {

    private final static double MAX_STEP_LENGTH = 5d;

    private final RouteComputer routePlanner;
    private final ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();
    private final ObjectProperty<Route> route = new SimpleObjectProperty<>();
    private final DoubleProperty highlightedPosition = new SimpleDoubleProperty();
    private final ObjectProperty<ElevationProfile> elevationProfile = new SimpleObjectProperty<>();

    public RouteBean(RouteComputer routePlanner) {
        this.routePlanner = routePlanner;

        elevationProfile.set(ElevationProfileComputer.elevationProfile(route.get(), MAX_STEP_LENGTH));
    }

    public ObservableList<Waypoint> waypointsProperty() {
        return waypoints;
    }

    public DoubleProperty highlightedPositionProperty() {
        return highlightedPosition;
    }

    public double highlightedPosition() {
        return highlightedPosition.get();
    }

    public void setHighlightedPosition(double value) {
        highlightedPosition.set(value);
    }

    public ReadOnlyObjectProperty<Route> routeProperty() {
        return route;
    }

    public Route route() {
        return route.get();
    }

    public ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty() {
        return elevationProfile;
    }

    public ElevationProfile elevationProfile() {
        return elevationProfile.get();
    }
}
