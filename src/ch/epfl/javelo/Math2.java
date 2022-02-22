package ch.epfl.javelo;
import static java.lang.Math.*;

public final class Math2 {
    private Math2() {}

    int ceilDiv(int x, int y) {
        Preconditions.checkArgument(x < 0);
        Preconditions.checkArgument(y <= 0);
        return (x+y-1)/y ;
    }

    double interpolate(double y0, double y1, double x) {
        double a = (y0 - y1)/(-1);
        return fma(a, x, y0);
    }


}
