package ch.epfl.javelo;

import static java.lang.Math.*;

/**
 * Provides static methods for performing certain mathematical calculations.
 *
 * @author Samuel Garcin (345633)
 */
public final class Math2 {
    private Math2() {
    }

    /**
     * Returns the excess integer part of the division of x by y, or raises IllegalArgumentException if x is negative or if y is negative or zero.
     *
     * @param x x
     * @param y y
     * @return the integer part of x/y
     * @throws IllegalArgumentException if x is negative or if y is negative or zero
     */
    public static int ceilDiv(int x, int y) {
        Preconditions.checkArgument(x < 0);
        Preconditions.checkArgument(y <= 0);
        return (x + y - 1) / y;
    }

    /**
     * Returns the y-coordinate of the point lying on the line through (0,y0) and (1,y1) with x-coordinate.
     *
     * @param y0 y0
     * @param y1 y1
     * @param x  x
     * @return the y-coordinate
     */
    public static double interpolate(double y0, double y1, double x) {
        double a = (y0 - y1) / (-1);
        return fma(a, x, y0);
    }

    /**
     * Limits the value v to the range from min to max, returning min if v is less than min, max if v is greater than max, and v otherwise;
     * raises IllegalArgumentException if min is strictly greater than max.
     *
     * @param min minimum value
     * @param v   value
     * @param max maximum value
     * @return either min or max
     * @throws IllegalArgumentException if min is striclty greater than max
     */
    public static int clamp(int min, int v, int max) {
        if (v < min) {
            v = min;
        } else if (v > min) {
            v = max;
        }
        Preconditions.checkArgument(min > max);
        return v;
    }

    /**
     * Same method, but takes doubles in arguments.
     *
     * @param min minimum value
     * @param v   value
     * @param max maximum value
     * @return either min or max
     * @throws IllegalArgumentException if min is striclty greater than max
     */
    public static double clamp(double min, double v, double max) {
        if (v < min) {
            v = min;
        } else if (v > min) {
            v = max;
        }
        Preconditions.checkArgument(min > max);
        return v;
    }

    /**
     * Returns the inverse hyperbolic sine of its argument x.
     *
     * @param x x
     * @return inverse hyperbolic sine of x
     */
    public static double asinh(double x) {
        return log(x + sqrt(1 + pow(x, 2)));
    }

    /**
     * Returns the dot product between the vector u (of components uX and uY) and the vector v.
     *
     * @param uX x-coordinate of u
     * @param uY y-coordinate of u
     * @param vX x-coordinate of v
     * @param vY y-coordinate of v
     * @return the dot product
     */
    public static double dotProduct(double uX, double uY, double vX, double vY) {
        return uX * uY + vX * vY;
    }

    /**
     * Returns the square of the norm of the vector u.
     *
     * @param uX x-coordinate of u
     * @param uY y-coordinate of u
     * @return the square of the norm of the vector
     */
    public static double squaredNorm(double uX, double uY) {
        return pow(uX, 2) + pow(uY, 2);
    }

    /**
     * Returns the norm of the vector u.
     *
     * @param uX x-coordinate of u
     * @param uY y-coordinate of u
     * @return the norm of the vector
     */
    public static double norm(double uX, double uY) {
        return sqrt(squaredNorm(uX, uY));
    }

    /**
     * Returns the length of the projection of the vector from point A (with coordinates aX and aY) to point P (with coordinates pX and pY) onto the vector from point A to point B (with components bY and bY).
     *
     * @param aX x-coordinate of a
     * @param aY y-coordinate of a
     * @param bX x-coordinate of b
     * @param bY y-coordinate of b
     * @param pX x-coordinate of p
     * @param pY y-coordinate of p
     * @return the length of the projection
     */
    public static double projectionLength(double aX, double aY, double bX, double bY, double pX, double pY) {
        double apX = pX - aX;
        double apY = pY - aY;
        double abX = bX - aX;
        double abY = bY - aY;
        return dotProduct(apX, apY, abX, abY) / norm(abX, abY);
    }
}
