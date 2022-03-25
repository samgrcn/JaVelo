package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;

import java.util.Arrays;
public class ElevationProfileComputer {

    private ElevationProfileComputer() {
    }

    private static int toNextExistingFloat(float[] elevationSamples, int from) {
        int samplesNumber = elevationSamples.length;
        int existingFloat = -1;

        for (int i = from; i < samplesNumber; i++) {
            if (!Float.isNaN(elevationSamples[i])) {
                existingFloat = i;
                break;
            }
        }
        return existingFloat;
    }

    private static int toPreviousExistingFloat(float[] elevationSamples) {
        int samplesNumber = elevationSamples.length;
        int existingFloat = 0;
        for (int i = samplesNumber - 1; i > 0; i--) {
            if (!Float.isNaN(elevationSamples[i])) {
                existingFloat = i;
                break;
            }
        }
        return existingFloat;
    }

    private static void checkSpecialCases(float[] elevationSamples) {
        int samplesNumber = elevationSamples.length;
        if (Float.isNaN(elevationSamples[0])) {
            int i = toNextExistingFloat(elevationSamples, 0);
            if (i == -1) {
                Arrays.fill(elevationSamples, 0, samplesNumber, 0);
            } else {
                Arrays.fill(elevationSamples,
                        0,
                        i - 1,
                        elevationSamples[i]);
            }
        }

        if (Float.isNaN(elevationSamples[samplesNumber - 1])) {
            int i = toPreviousExistingFloat(elevationSamples);
            Arrays.fill(elevationSamples,
                    i + 1,
                    samplesNumber,
                    elevationSamples[i]);
        }
    }

    private static void fillingArray(Route route, float[] elevationSamples) {
        int samplesNumber = elevationSamples.length;
        for (int i = 0; i < samplesNumber; i++) {
            if (Float.isNaN(elevationSamples[i])) {
                int lowerBound = i - 1;
                int higherBound = toNextExistingFloat(elevationSamples, i);
                elevationSamples[i] = (float) Math2.interpolate(
                        elevationSamples[lowerBound],
                        elevationSamples[higherBound], (double)(i - lowerBound) / (higherBound - lowerBound));
            }
        }
    }


    public static ElevationProfile elevationProfile(Route route, double maxStepLength) {
        Preconditions.checkArgument(maxStepLength > 0);
        int samplesNumber = (int) Math.ceil(route.length() / maxStepLength) + 1;
        float[] elevationSamples = new float[samplesNumber];
        for (int i = 0; i < samplesNumber; i++) {
            double position = i * route.length() / (samplesNumber - 1);
            elevationSamples[i] = (float) route.elevationAt(position);
        }
        checkSpecialCases(elevationSamples);
        fillingArray(route, elevationSamples);

        return new ElevationProfile(route.length(), elevationSamples);
    }
}