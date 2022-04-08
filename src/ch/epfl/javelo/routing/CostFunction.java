package ch.epfl.javelo.routing;

/**
 * @author Samuel Garcin (345633)
 * A very small interface that CityBikeCF implements
 */

public interface CostFunction {

    /**
     * Gives the factor by which the length of the identity edgeId, starting from the identity nodeId, must be multiplied,
     * this factor must be greater than or equal to 1.
     * @param nodeId Identity of the first node
     * @param edgeId Identity of the last node
     * @return the factor by which the length of the identity edgeId must be multiplied.
     */

    double costFactor(int nodeId, int edgeId);
}
