package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;

/**
 * Represents the point on a route closest to a given reference point, which is in the vicinity of the route.
 *
 * @author Quentin Chappuis (339517)
 */
public record RoutePoint(PointCh point, double position, double distanceToReference) {

    /**
     * Represents a non-existent point.
     */
    public final static RoutePoint NONE = new RoutePoint(null, Double.NaN, Double.POSITIVE_INFINITY);

    /**
     * Returns a point identical to the receiver (this) but whose position is offset by the given difference,
     * which can be positive or negative
     *
     * @param positionDifference the position difference that should be applied
     * @return a new route point
     */
    public RoutePoint withPositionShiftedBy(double positionDifference) {
        if (positionDifference == 0) return this;
        return new RoutePoint(point, position + positionDifference, distanceToReference);
    }

    /**
     * Returns this if its distance to the reference is less than or equal to that, and that otherwise.
     *
     * @param that another route point
     * @return this or that
     */
    public RoutePoint min(RoutePoint that) {
        return distanceToReference <= that.distanceToReference() ? this : that;
    }

    /**
     * Returns this if its distance to the reference is less than or equal to thatDistanceToReference,
     * and a new instance of RoutePoint whose attributes are the arguments passed to min otherwise.
     *
     * @param thatPoint another route point
     * @param thatPosition new position
     * @param thatDistanceToReference the distance to compare
     * @return this or a new route point
     */
    public RoutePoint min(PointCh thatPoint, double thatPosition, double thatDistanceToReference) {
        return distanceToReference <= thatDistanceToReference ? this : new RoutePoint(thatPoint, thatPosition, thatDistanceToReference);
    }
}
