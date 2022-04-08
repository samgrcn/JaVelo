package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;

/**
 * Represents a point in the Swiss coordinate system.
 *
 * @author Samuel Garcin (345633)
 */

public record PointCh(double e, double n) {

    /**
     * @throws IllegalArgumentException if the given coordinates are not contained in Switzerland
     */
    public PointCh {
        Preconditions.checkArgument(SwissBounds.containsEN(e, n));
    }

    /**
     * Returns the square of the distance in metres from the receiver (this) to the argument that.
     *
     * @param that argument
     * @return the square of the distance
     */
    public double squaredDistanceTo(PointCh that) {
        double vectorX = that.e - this.e;
        double vectorY = that.n - this.n;

        return Math2.squaredNorm(vectorX, vectorY);
    }

    /**
     * Returns the distance in metres from the receiver (this) to the argument that.
     *
     * @param that argument
     * @return the distance
     */
    public double distanceTo(PointCh that) {
        double vectorX = that.e - this.e;
        double vectorY = that.n - this.n;

        return Math2.norm(vectorX, vectorY);
    }

    /**
     * Returns the longitude of the point, in the WGS84 system, in radians.
     *
     * @return the longitude
     */
    public double lon() {
        return Ch1903.lon(this.e, this.n);
    }

    /**
     * Returns the latitude of the point, in the WGS84 system, in radians.
     *
     * @return the latitude
     */
    public double lat() {
        return Ch1903.lat(this.e, this.n);
    }
}

