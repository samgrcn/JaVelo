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
        float[] samples = {10, -10, 10, 5, 2, 1};
        double actualValue = sampled(samples, 10).applyAsDouble(x);
        //double expectedValue = ;
        System.out.println(actualValue);
    }

    public static void main(String[] args) {
        sampledWorksforEachDouble(10);
    }


}


