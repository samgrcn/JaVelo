package ch.epfl.javelo;

import java.util.function.DoubleUnaryOperator;

import static ch.epfl.javelo.Functions.constant;
import static ch.epfl.javelo.Functions.sampled;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class FunctionsTest {

    static void constantWorksForEachDouble() {
        DoubleUnaryOperator actualValue = constant(5);
        System.out.println(actualValue);
    }

    static void sampledWorksforEachDouble(double x) {
        float[] samples = {0, 2, 4, 6, 10};
        double actualValue = sampled(samples, 10).applyAsDouble(x);
        System.out.println(actualValue);
    }

    public static void main(String[] args) {
        sampledWorksforEachDouble(3.5);
    }


}


