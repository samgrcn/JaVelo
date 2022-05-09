package ch.epfl.javelo.gui;

import ch.epfl.javelo.MemoryCacheHashMap;
import ch.epfl.javelo.routing.*;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a JavaFX bean containing properties relating to waypoints and the corresponding route.
 *
 * @author Quentin Chappuis (339517)
 */
public final class RouteBean {

    private final static double MAX_STEP_LENGTH = 5d;
    private final static double INITIAL_HIGHLIGHTED_POSITION = 1000d;

    private final RouteComputer routePlanner;
    private final ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();
    private final ObjectProperty<Route> route = new SimpleObjectProperty<>();
    private final DoubleProperty highlightedPosition = new SimpleDoubleProperty(INITIAL_HIGHLIGHTED_POSITION);
    private final ObjectProperty<ElevationProfile> elevationProfile = new SimpleObjectProperty<>();
    private final Map<Pair<Integer, Integer>, Route> bestRoute = new MemoryCacheHashMap<>();

    /**
     * @param routePlanner the route planner of type RouteComputer
     */
    public RouteBean(RouteComputer routePlanner) {
        this.routePlanner = routePlanner;

        waypoints.addListener((Observable w) -> {
            if (waypoints.size() < 2) {
                noRoute();
            }
            else update();
        });
    }

    /**
     *
     * @return
     */
    public ObservableList<Waypoint> waypoints() {
        return waypoints;
    }

    /**
     *
     * @return
     */
    public DoubleProperty highlightedPositionProperty() {
        return highlightedPosition;
    }

    /**
     *
     * @return
     */
    public double highlightedPosition() {
        return highlightedPosition.get();
    }

    /**
     *
     * @param value
     */
    public void setHighlightedPosition(double value) {
        highlightedPosition.set(value);
    }

    /**
     *
     * @return
     */
    public ReadOnlyObjectProperty<Route> routeProperty() {
        return route;
    }

    /**
     *
     * @return
     */
    public Route route() {
        return route.get();
    }

    /**
     *
     * @return
     */
    public ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty() {
        return elevationProfile;
    }

    /**
     *
     * @return
     */
    public ElevationProfile elevationProfile() {
        return elevationProfile.get();
    }

    /**
     *
     */
    private void noRoute() {
        route.set(null);
        elevationProfile.set(null);
    }

    /**
     *
     */
    private void update() {
        int fromNode;
        int toNode;
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < waypoints.size() - 1; ++i) {
            fromNode = waypoints.get(i).closestNodeId();
            toNode = waypoints.get(i + 1).closestNodeId();
            Pair<Integer, Integer> key = new Pair<>(fromNode, toNode);
            if (bestRoute.containsKey(key)) {
                routes.add(bestRoute.get(key));
            } else {
                Route route = routePlanner.bestRouteBetween(fromNode, toNode);
                if (route == null) {
                    noRoute();
                    return;
                }
                routes.add(route);
                bestRoute.put(key, route);
            }
        }
        route.set(new MultiRoute(routes));
        elevationProfile.set(ElevationProfileComputer.elevationProfile(route(), MAX_STEP_LENGTH));
    }
}
