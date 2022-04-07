package ch.epfl.javelo.routing;

/**
 * @author Samuel Garcin (345633)
 * A very small interface that CityBikeCF implements
 */

public interface CostFunction {

    double costFactor(int nodeId, int edgeId);
}
