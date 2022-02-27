package ch.epfl.javelo;
import java.util.function.DoubleUnaryOperator;

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

        static int lowerBound = 0;

        public Sampled {
            if(samples.length < 2 || xMax <= 0) {
                throw new IllegalArgumentException();
            }
        }

        private int bound(double x) {
            double intervalLength = xMax/samples.length;
            while (x > xMax) {
                x = x - intervalLength;
                lowerBound++;
            }
            return lowerBound;
        }

        @Override
        public double applyAsDouble(double x) {
            return Math2.interpolate(samples[bound(x)], samples[bound(x) + 1], x) ;
        }
    }
}
