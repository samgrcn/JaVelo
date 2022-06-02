package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.*;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a JavaFX bean containing properties relating to waypoints and the corresponding route.
 *
 * @author Quentin Chappuis (339517)
 */
public final class RouteBean {

    private final static double MAX_STEP_LENGTH = 5;
    private final static double INITIAL_HIGHLIGHTED_POSITION = 1000;
    private static final int MAX_ENTRIES = 50;
    private static final float LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;

    private final RouteComputer routePlanner;
    private final ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();
    private final ObjectProperty<Route> route = new SimpleObjectProperty<>();
    private final DoubleProperty highlightedPosition = new SimpleDoubleProperty(INITIAL_HIGHLIGHTED_POSITION);
    private final ObjectProperty<ElevationProfile> elevationProfile = new SimpleObjectProperty<>();
    private final Map<Pair<Integer, Integer>, Route> bestRoute = new LinkedHashMap<>(MAX_ENTRIES, LOAD_FACTOR, ACCESS_ORDER) {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    };

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
     * Returns the waypoint list as an observable list-
     *
     * @return the waypoints list
     */
    public ObservableList<Waypoint> waypoints() {
        return waypoints;
    }

    /**
     * Returns the highlighted position property.
     *
     * @return the highlightedPosition property
     */
    public DoubleProperty highlightedPositionProperty() {
        return highlightedPosition;
    }

    /**
     * Returns the highlighted position value.
     *
     * @return the highlightedPosition
     */
    public double highlightedPosition() {
        return highlightedPosition.get();
    }

    /**
     * Returns the route property.
     *
     * @return the route property
     */
    public ReadOnlyObjectProperty<Route> routeProperty() {
        return route;
    }

    /**
     * Returns the route value.
     *
     * @return the route
     */
    public Route route() {
        return route.get();
    }

    /**
     * Returns the elevation profile property.
     *
     * @return the elevationProfile property
     */
    public ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty() {
        return elevationProfile;
    }

    /**
     * Returns the elevation profile value.
     * @return the elevationProfile
     */
    public ElevationProfile elevationProfile() {
        return elevationProfile.get();
    }

    /**
     * Takes as argument a position along the route and returns the index of the segment containing it,
     * ignoring empty segments.
     *
     * @param position the position along the route
     * @return the index of the segment containing the position
     */
    public int indexOfNonEmptySegmentAt(double position) {
        int index = route().indexOfSegmentAt(position);
        for (int i = 0; i <= index; i += 1) {
            int n1 = waypoints.get(i).closestNodeId();
            int n2 = waypoints.get(i + 1).closestNodeId();
            if (n1 == n2) index += 1;
        }
        return index;
    }


    /**
     * Called if there is no route found (bestRouteBetween() returns null or the size of waypoints is lower than 2).
     * Will set the route and elevationProfile properties to null.
     */
    private void noRoute() {
        route.set(null);
        elevationProfile.set(null);
    }

    /**
     * Called when the waypoints list is updated. Will calculate the new route with the waypoints and
     * set the route and elevationProfile properties.
     */
    private void update() {
        int fromNode;
        int toNode;
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < waypoints.size() - 1; ++i) {
            fromNode = waypoints.get(i).closestNodeId();
            toNode = waypoints.get(i + 1).closestNodeId();
            if (fromNode == toNode) continue;

            Pair<Integer, Integer> key = new Pair<>(fromNode, toNode);
            Route bestRouteFor = bestRoute.get(key);
            if (bestRouteFor != null) {
                routes.add(bestRouteFor);
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

        if (routes.size() == 0) noRoute();
        else {
            route.set(new MultiRoute(routes));
            elevationProfile.set(ElevationProfileComputer.elevationProfile(route(), MAX_STEP_LENGTH));
        }
    }

}
