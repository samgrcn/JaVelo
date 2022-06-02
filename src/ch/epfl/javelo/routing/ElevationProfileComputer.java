package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;

import java.util.Arrays;


/**
 * ElevationProfileComputer contains the code to allow to calculate the long profile of a given route
 *
 * @author Samuel Garcin (345633)
 */
public final class ElevationProfileComputer {

    private ElevationProfileComputer() {
    }

    /**
     * Private method used to get the first non NaN element of an array
     *
     * @param elevationSamples the array
     * @param from             the starting index
     * @param forward          true if it goes forward and false if we want to explore backward, in which case from isn't useful
     *                         since we start from the end
     * @return the index of the first element that is not NaN, going forward or backward depending on the boolean forward.
     */
    private static int toNextExistingFloat(float[] elevationSamples, int from, boolean forward) {
        int samplesNumber = elevationSamples.length;
        int existingFloat = 0;

        //we explore the list forward (starting from the left)
        if (forward) {
            for (int i = from; i < samplesNumber; i++) {
                if (!Float.isNaN(elevationSamples[i])) {
                    existingFloat = i;
                    break;
                }
            }
        }
        //we explore the list backward (starting from the right)
        else {
            for (int i = samplesNumber - 1; i > 0; i--) {
                if (!Float.isNaN(elevationSamples[i])) {
                    existingFloat = i;
                    break;
                }
            }
        }

        return existingFloat;
    }

    /**
     * Private method used to check the special cases (if the array is full of NaN, if there are NaN at the beginning
     * or at the end of the array) and fills the array accordingly.
     *
     * @param elevationSamples the array we want to check
     */
    private static void checkSpecialCases(float[] elevationSamples) {

        int samplesNumber = elevationSamples.length;

        if (Float.isNaN(elevationSamples[0])) {
            int i = toNextExistingFloat(elevationSamples, 0, true);
            int j = toNextExistingFloat(elevationSamples, 1, true);

            //if the list is full of NaN
            if (i == 0) {
                Arrays.fill(elevationSamples, 0, samplesNumber, 0);
            }
            if (j == 0) {
                Arrays.fill(elevationSamples, 1, samplesNumber, elevationSamples[1]);
            }
            //only if some of the first values are NaN, but not the entire array
            else {
                Arrays.fill(elevationSamples,
                        0,
                        i - 1,
                        elevationSamples[i]);
            }
        }

        //only if some of the last values are NaN, but not the entire array
        if (Float.isNaN(elevationSamples[samplesNumber - 1])) {
            int i = toNextExistingFloat(elevationSamples, samplesNumber, false);

            Arrays.fill(elevationSamples,
                    i + 1,
                    samplesNumber,
                    elevationSamples[i]);
        }
    }

    /**
     * Private method used to fill an array, replacing the NaNs by existing values by interpolation.
     *
     * @param elevationSamples the array we want to fill
     */
    private static void fillingArray(float[] elevationSamples) {

        int samplesNumber = elevationSamples.length;

        //for each element, we check whether it is a NaN, and we fill the array accordingly
        for (int i = 0; i < samplesNumber; i++) {
            if (Float.isNaN(elevationSamples[i])) {
                int lowerBound = i - 1;
                int higherBound = toNextExistingFloat(elevationSamples, i, true);
                elevationSamples[i] = (float) Math2.interpolate(
                        elevationSamples[lowerBound],
                        elevationSamples[higherBound], (double) (i - lowerBound) / (higherBound - lowerBound));
            }
        }
    }

    /**
     * Build the long profile of the route in parameter, ensuring that the spacing between the
     * samples of the profile is at most maxStepLength meters
     *
     * @param route         the route
     * @param maxStepLength maximum space between each sample
     * @return the long profile of the route.
     * @throws IllegalArgumentException if this spacing is not strictly positive.
     */
    public static ElevationProfile elevationProfile(Route route, double maxStepLength) {

        Preconditions.checkArgument(maxStepLength > 0);
        if (route != null) {
            int samplesNumber = (int) Math.ceil(route.length() / maxStepLength) + 1;
            float[] elevationSamples = new float[samplesNumber];

            for (int i = 0; i < samplesNumber; i++) {
                double position = i * route.length() / (samplesNumber - 1);
                elevationSamples[i] = (float) route.elevationAt(position);
            }

            checkSpecialCases(elevationSamples);
            fillingArray(elevationSamples);

            return new ElevationProfile(route.length(), elevationSamples);
        }
        return null;
    }
}