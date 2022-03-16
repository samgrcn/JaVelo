package ch.epfl.javelo.data;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Preconditions;

import java.util.DoubleSummaryStatistics;
import java.util.function.DoubleUnaryOperator;

public class ElevationProfile {

    private final double length;
    private final float[] elevationSamples;
    private final DoubleUnaryOperator function;

    public ElevationProfile(double length, float[] elevationSamples) {
        this.function = Functions.sampled(elevationSamples, length);
        this.length = length;
        this.elevationSamples = elevationSamples;
    }



    private double maxValues(float[] elevationSamples) {
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        for (float i: elevationSamples) {
            statistics.accept(i);
        }
        return statistics.getMax();
    }

    private double minValues(float[] elevationSamples) {
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        for (float i: elevationSamples) {
            statistics.accept(i);
        }
        return statistics.getMin();
    }

    public double length() { return length; }

    public double minElevation() { return minValues(elevationSamples); }

    public double maxElevation() { return maxValues(elevationSamples); }

    public double totalAscent() {
        float totalAscent = 0;
        for (int i = 0; i < elevationSamples.length - 1; i++) {
            if (elevationSamples[i] < elevationSamples[i + 1]) {
                totalAscent = elevationSamples[i + 1] - elevationSamples[i];

            }
        }
        return totalAscent;
    }

    public double totalDescent() {
        float totalDescent = 0;
        for (int i = 0; i < elevationSamples.length - 1; i++) {
            if (elevationSamples[i] > elevationSamples[i + 1]) {
                    totalDescent = elevationSamples[i] - elevationSamples[i + 1];
            }
        }
        return totalDescent ;
    }

    public double elevationAt(double position) { return function.applyAsDouble(position); }



}
