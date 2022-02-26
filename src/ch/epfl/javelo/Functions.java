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
        public Sampled {
            if(samples.length < 2 || xMax <= 0) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public double applyAsDouble(double x) {

            return Math2.interpolate(samples[0], samples[1], x) ;
        }
    }
}
