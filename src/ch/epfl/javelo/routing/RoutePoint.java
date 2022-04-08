package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;

/**
 * Represents the point on a route closest to a given reference point, which is in the vicinity of the route.
 *
 * @author Quentin Chappuis (339517)
 */
public record RoutePoint(PointCh point, double position, double distanceToReference) {

    public final static RoutePoint NONE = new RoutePoint(null, Double.NaN, Double.POSITIVE_INFINITY);

    /**
     * Retourne un point identique au récepteur (this) mais dont la position est décalée de la différence donnée, qui peut être positive ou négative.
     *
     * @param positionDifference the position difference that should be applied
     * @return a new route point
     */
    public RoutePoint withPositionShiftedBy(double positionDifference) {
        return new RoutePoint(this.point(), this.position() + positionDifference, this.distanceToReference());
    }

    /**
     * Returns this if its distance to the reference is less than or equal to that, and that otherwise.
     *
     * @param that another route point
     * @return this or that
     */
    public RoutePoint min(RoutePoint that) {
        return distanceToReference() <= that.distanceToReference() ? this : that;
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
        return distanceToReference() <= thatDistanceToReference ? this : new RoutePoint(thatPoint, thatPosition, thatDistanceToReference);
    }
}
