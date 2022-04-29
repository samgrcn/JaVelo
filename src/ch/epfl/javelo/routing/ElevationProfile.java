package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.function.DoubleUnaryOperator;

/**
 * ElevationProfile represents the long profile of either a simple or multiple route.
 *
 * @author Samuel Garcin (345633)
 */
public final class ElevationProfile {

    private final double length;
    private final float[] elevationSamples;
    private final DoubleUnaryOperator function;

    /**
     * Constructs the long profile of a route of length (in meters) and whose elevation samples,
     * uniformly distributed along the route, are contained in elevationSamples.
     *
     * @param length           length of the route (in meters)
     * @param elevationSamples elevations of the profile
     * @throws IllegalArgumentException if the length is negative or null,
     *                                  or if the sample array contains less than 2 elements
     */
    public ElevationProfile(double length, float[] elevationSamples) {
        this.function = Functions.sampled(elevationSamples, length);
        this.length = length;
        this.elevationSamples = Arrays.copyOf(elevationSamples, elevationSamples.length);
    }

    /**
     * Private method to give the statistics of the array using DoubleSummaryStatistics, in order to either get
     * the minimum or the maximum of the array..
     *
     * @param elevationSamples given array
     * @return the statistics of the array
     */
    private DoubleSummaryStatistics statisticsValues(float[] elevationSamples) {
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();

        for (float i : elevationSamples) {
            statistics.accept(i);
        }
        return statistics;
    }

    /**
     * Gives the length of the profile (in meters).
     *
     * @return length of the profile (in meters)
     */
    public double length() {
        return length;
    }

    /**
     * Gives  the minimum altitude of the profile (in meters).
     *
     * @return the minimum altitude of the profile (in meters)
     */
    public double minElevation() { return statisticsValues((elevationSamples)).getMin(); }

    /**
     * Gives the maximum altitude of the profile (in meters).
     *
     * @return returns the maximum altitude of the profile (in meters)
     */
    public double maxElevation() { return statisticsValues((elevationSamples)).getMax(); }

    /**
     * Gives the total positive vertical drop of the profile (in meters).
     *
     * @return the total positive vertical drop of the profile (in meters)
     */
    public double totalAscent() {
        double totalAscent = 0;

        for (int i = 0; i < elevationSamples.length - 1; i++) {
            if (elevationSamples[i] < elevationSamples[i + 1]) {
                totalAscent += elevationSamples[i + 1] - elevationSamples[i];

            }
        }
        return totalAscent;
    }

    /**
     * Gives the total negative elevation of the profile (in meters).
     *
     * @return the total negative elevation of the profile (in meters)
     */
    public double totalDescent() {
        double totalDescent = 0;

        for (int i = 0; i < elevationSamples.length - 1; i++) {
            if (elevationSamples[i] > elevationSamples[i + 1]) {
                totalDescent += elevationSamples[i] - elevationSamples[i + 1];
            }
        }
        return totalDescent;
    }

    /**
     * Gives the altitude of the profile at the given position.
     *
     * @param position desired position to get the altitude.
     * @return the altitude of the profile at the position
     */
    public double elevationAt(double position) {
        return function.applyAsDouble(position);
    }
}
