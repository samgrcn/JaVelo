package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;

public record PointCh(double e, double n){

    public record Complex(double e, double n) {
        public Complex {
            if (!SwissBounds.containsEN(e, n))
                throw new IllegalArgumentException();
        }

        double squaredDistanceTo(PointCh that) {
            double vectorX = that.e - this.e;
            double vectorY = that.n - this.n;
            return Math2.squaredNorm(vectorX, vectorY);
        }

        double distanceTo(PointCh that) {
            double vectorX = that.e - this.e;
            double vectorY = that.n - this.n;
            return Math2.norm(vectorX, vectorY);
        }

        double lon() { return Ch1903.lon(this.e, this.n); }

        double lat() { return Ch1903.lat(this.e, this.n); }

    }

}

