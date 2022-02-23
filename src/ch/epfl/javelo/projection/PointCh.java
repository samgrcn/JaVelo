package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;

/**
 * Représente un point dans le système de coordonnées suisse.
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

        /**
         * Retourne le carré de la distance en mètres séparant le récepteur (this) de l'argument that.
         * @param that argument that
         * @return le carré de la distance
         */
        double squaredDistanceTo(PointCh that) {
            double vectorX = that.e - this.e;
            double vectorY = that.n - this.n;
            return Math2.squaredNorm(vectorX, vectorY);
        }

        /**
         * Retourne la distance en mètres séparant le récepteur (this) de l'argument that.
         * @param that argument that
         * @return la distance
         */
        double distanceTo(PointCh that) {
            double vectorX = that.e - this.e;
            double vectorY = that.n - this.n;
            return Math2.norm(vectorX, vectorY);
        }

        /**
         * Retourne la longitude du point, dans le système WGS84, en radians.
         * @return la longitude
         */
        double lon() { return Ch1903.lon(this.e, this.n); }

        /**
         * Retourne la latitude du point, dans le système WGS84, en radians.
         * @return la latitude
         */
        double lat() { return Ch1903.lat(this.e, this.n); }

    }
}

