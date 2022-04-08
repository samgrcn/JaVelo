package ch.epfl.javelo;

import java.util.function.DoubleUnaryOperator;

/**
 * Implements some functions as interpolate and constant.
 *
 * @author Samuel Garcin (345633)
 */
public final class Functions {

    private Functions() {
    }

    /**
     * Public method to call the function applyAsDouble though a new instance of Constant.
     *
     * @param y y
     * @return a new instance of Constant
     */
    public static DoubleUnaryOperator constant(double y) {
        return new Constant(y);
    }

    /**
     * Record Constant implementing DoubleUnaryOperator, used for functions and taking double y as a parameter
     *
     * @param y y
     */
    private record Constant(double y)
            implements DoubleUnaryOperator {

        /**
         * Overrides applyAsDouble from DoubleUnaryOperator. Return the parameter y from the record Constant,
         * ignoring the parameter x
         *
         * @param x x
         * @return y from record Constant (this.y)
         */
        @Override
        public double applyAsDouble(double x) {
            return this.y;
        }
    }

    /**
     * Public method to call the function applyAsDouble though a new instance of Constant.
     *
     * @param xMax    the maximum x value of the big interval containing all values of samples [].
     * @param samples [] an array of y values spaced by a same interval.
     * @return a new instance of Sampled.
     */
    public static DoubleUnaryOperator sampled(float[] samples, double xMax) {
        return new Sampled(samples, xMax);
    }

    /**
     * Record Sampled implementing DoubleUnaryOperator, used for functions and taking an array of float samples
     * and xMax as a parameter.
     *
     * @param samples [] an array of y values spaced by a same interval.
     * @param xMax    the maximum x value of the big interval containing all values of samples [].
     */
    private record Sampled(float[] samples, double xMax) implements DoubleUnaryOperator {

        /**
         * Constructor checking whether there are at least 2 values in samples, and if xMax is strictly positive.
         *
         * @param samples [] an array of y values spaced by a same interval.
         * @param xMax    the maximum x value of the big interval containing all values of samples [].
         * @throws IllegalArgumentException if it's not the case.
         */
        public Sampled {
            Preconditions.checkArgument(xMax > 0);
            Preconditions.checkArgument(samples.length >= 2);
        }

        /**
         * Private method used for applyAsDouble later, for a better understanding.
         * This method finds the lower boundary of the interval in which x is.
         *
         * @param x x
         * @return The value of x divided by the length of an interval, rounded down ; so the lower boundary of
         * the interval in which x is.
         */
        // We create intervalLength
        // which is the length in the X-axis between each point with a corresponding value on the Y-axis of samples.
        // Each point in samples has a value on the X-axis, spaced by intervalLength. There are (the number of elements
        // in samples - 1) intervals, and we want to find in which interval x is.
        private int bound(double x) {
            double intervalLength = xMax / (samples.length - 1);
            return (int) (x / intervalLength);
        }

        /**
         * Given a double x, some values in samples, and an xMax, find the y value of that double x by interpolating.
         *
         * @param x x
         * @return the corresponding y of the x value
         */
        @Override
        public double applyAsDouble(double x) {

            if (x >= xMax) {                          //If the value of x is greater than xMax, then its y value
                return samples[samples.length - 1];   //equals the last element in samples, if it is
            } else if (x < 0) {                       //negative, then it equals the first element of samples.
                return samples[0];
            }

            double intervalLength = xMax / (samples.length - 1);

            return Math2.interpolate(samples[bound(x)], samples[bound(x) + 1],
                    (x - intervalLength * bound(x)) / intervalLength);

        }
    }
}



