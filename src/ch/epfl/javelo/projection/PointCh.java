package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;
import java.util.Objects;

import java.util.Objects;

/**
 * Represents a point in the Swiss coordinate system.
 *
 * @author Samuel Garcin (345633)
 */
public record PointCh(double e, double n){
    public PointCh {
        if (!SwissBounds.containsEN(e, n))
            throw new IllegalArgumentException();
    }

    public record Complex(double e, double n) {
        public Complex {
            if (!SwissBounds.containsEN(e, n))
                throw new IllegalArgumentException();
        }

        @Override
        public String toString() {
            return "Complex{" +
                    "e=" + e +
                    ", n=" + n +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Complex complex = (Complex) o;
            return Double.compare(complex.e, e) == 0 && Double.compare(complex.n, n) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(e, n);
        }

        /**
         * Returns the square of the distance in metres from the receiver (this) to the argument that.
         * @param that argument
         * @return the square of the distance
         */
        double squaredDistanceTo(PointCh that) {
            double vectorX = that.e - this.e;
            double vectorY = that.n - this.n;
            return Math2.squaredNorm(vectorX, vectorY);
        }

        /**
         * Returns the distance in metres from the receiver (this) to the argument that.
         * @param that argument
         * @return the distance
         */
        double distanceTo(PointCh that) {
            double vectorX = that.e - this.e;
            double vectorY = that.n - this.n;
            return Math2.norm(vectorX, vectorY);
        }

        /**
         * Returns the longitude of the point, in the WGS84 system, in radians.
         * @return the longitude
         */
        double lon() { return Ch1903.lon(this.e, this.n); }

        /**
         * Returns the latitude of the point, in the WGS84 system, in radians.
         * @return the latitude
         */
        double lat() { return Ch1903.lat(this.e, this.n); }

        @Override
        public String toString() {
            return "Complex{" +
                    "e=" + e +
                    ", n=" + n +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Complex complex = (Complex) o;
            return Double.compare(complex.e, e) == 0 && Double.compare(complex.n, n) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(e, n);
        }

    }
}

