package ch.epfl.javelo;
import java.util.function.DoubleUnaryOperator;

/**
 * Implements some functions.
 *
 * @author Samuel Garcin (345633)
 */

public final class Functions {

    private Functions() {}

    public static DoubleUnaryOperator constant(double y) {
        return new Constant(y);
    }

    public static DoubleUnaryOperator sampled(float[] samples, double xMax) {
        return new Sampled(samples, xMax); }

    private record Constant(double y)
            implements DoubleUnaryOperator {

        @Override
        public double applyAsDouble(double x) {
            return this.y;
        }
    }
    private record Sampled(float[] samples, double xMax) implements DoubleUnaryOperator {

        public Sampled {
            if(samples.length < 2 || xMax <= 0) {
                throw new IllegalArgumentException();
            }
        }

        private int bound(double x) {
            double intervalLength = xMax/(samples.length - 1);
            return (int)(x/intervalLength);
        }

        @Override
        public double applyAsDouble(double x) {
            if(x > xMax) { return xMax; }
            return Math2.interpolate(samples[bound(x)], samples[bound(x) + 1], x - bound(x)) ;
        }
    }
}
