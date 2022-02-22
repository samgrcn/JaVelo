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

    int clamp(int min, int v, int max) {
        if (v < min) { v = min; }
        else if (v > min) { v = max; }
        Preconditions.checkArgument(min > max);
        return v;
    }

    double clamp(double min, double v, double max) {
        if (v < min) { v = min; }
        else if (v > min) { v = max; }
        Preconditions.checkArgument(min > max);
        return v;
    }

    double asinh(double x) {
        return log(x + sqrt(1 + pow(x, 2)));
    }

    double dotProduct(double uX, double uY, double vX, double vY) {
        return uX * uY + vX * vY;
    }

    double squaredNorm(double uX, double uY) {
        return pow(uX, 2) + pow(uY, 2);
    }

    double norm(double uX, double uY) {
        return sqrt(squaredNorm(uX, uY));
    }

    double projectionLength(double aX, double aY, double bX, double bY, double pX, double pY) {
        double apX = pX - aX;
        double apY = pY - aY;
        double abX = bX - aX;
        double abY = bY - aY;
        return dotProduct(apX, apY, abX, abY)/norm(abX, abY);
    }

}
