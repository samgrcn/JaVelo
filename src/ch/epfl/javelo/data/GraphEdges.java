package ch.epfl.javelo.data;

import ch.epfl.javelo.Bits;
import ch.epfl.javelo.Q28_4;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Represents the array of all edges of the graph JaVelo.
 *
 * @author Quentin Chappuis (339517)
 */
public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {

    private static final int OFFSET_DIRECTION_IDENTITY = 0;
    private static final int OFFSET_LENGTH = OFFSET_DIRECTION_IDENTITY + 4; // = 4
    private static final int OFFSET_ALTITUDE_DIFF = OFFSET_LENGTH + 2;    // = 6
    private static final int OFFSET_OSM_ATTRIBUTES = OFFSET_ALTITUDE_DIFF + 2; // = 8
    private static final int NUMBER_OF_EDGES = 10;
    private static final int PROFIL_SAMPLES_TYPE_2_DELTA = 2;
    private static final int PROFIL_SAMPLES_TYPE_3_DELTA = 4;

    /**
     * Returns true if and only if the given identity edge goes in the opposite direction of the OSM path from which it comes.
     *
     * @param edgeId the id of the concerned edge
     * @return true or false
     */
    public boolean isInverted(int edgeId) {
        return edgesBuffer.get(edgeId * NUMBER_OF_EDGES + OFFSET_DIRECTION_IDENTITY) < 0;
    }

    /**
     * Returns the identity of the destination node of the given identity edge.
     *
     * @param edgeId the id of the concerned edge
     * @return the identity of the node
     */
    public int targetNodeId(int edgeId) {
        int tempDirection = edgesBuffer.getInt(edgeId * NUMBER_OF_EDGES + OFFSET_DIRECTION_IDENTITY);

        return tempDirection >= 0 ? tempDirection : ~tempDirection;
    }

    /**
     * Returns the length, in metres, of the given identity edge.
     *
     * @param edgeId the id of the concerned edge
     * @return the length
     */
    public double length(int edgeId) {
        return Q28_4.asDouble(
                Short.toUnsignedInt(
                        edgesBuffer.getShort(edgeId * NUMBER_OF_EDGES + OFFSET_LENGTH)));
    }

    /**
     * Returns the positive vertical drop, in metres, of the given identity edge.
     *
     * @param edgeId the id of the concerned edge
     * @return the vertical drop
     */
    public double elevationGain(int edgeId) {
        return Q28_4.asDouble(
                Short.toUnsignedInt(
                        edgesBuffer.getShort(edgeId * NUMBER_OF_EDGES + OFFSET_ALTITUDE_DIFF)));

    }

    /**
     * Returns true if and only if the given identity edge has a profile.
     *
     * @param edgeId the id of the concerned edge
     * @return true or false
     */
    public boolean hasProfile(int edgeId) {
        return getProfile(edgeId) != 0;
    }

    /**
     * Returns the value of the profile of a given edge.
     *
     * @param edgeId the id of the concerned edge
     * @return the profile
     */
    private int getProfile(int edgeId) {
        return Bits.extractUnsigned(profileIds.get(edgeId), 30, 2);
    }

    /**
     * Manually reverses a given array.
     *
     * @param array the array to reverse
     * @return the reversed array
     */
    private float[] reverseFloatArray(float[] array) {
        for (int i = 0; i < array.length / 2; ++i) {
            float temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }

    /**
     * Fills the profile samples array for an edge of type 1.
     *
     * @param res           the results array
     * @param samplesNumber the number of samples
     * @param identity      the identity of the first elevation
     */
    private void profileSamplesType1(float[] res, int samplesNumber, int identity) {
        for (int i = 0; i < samplesNumber; ++i) {
            res[i] = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(identity + i)));
        }
    }

    /**
     * Fills the profile samples array for an edge of type 2 or 3.
     *
     * @param res           the results array
     * @param samplesNumber the number of samples
     * @param identity      the identity of the first elevation
     * @param delta         the delta (2 for type 2 and 4 for type 3)
     */
    private void profileSamplesType2And3(float[] res, int samplesNumber, int identity, int delta) {
        int iterNumber = (int) Math.ceil((samplesNumber - 1) / (double) delta) + 1;
        int indexFilled = 1;
        int length = Short.SIZE / delta;
        res[0] = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(identity)));

        for (int i = 1; i < iterNumber; ++i) {
            int hexNumber = elevations.get(identity + i);
            for (int j = Short.SIZE - length; j >= 0; j -= length) {
                if (indexFilled < samplesNumber) {
                    res[indexFilled] = Q28_4.asFloat(Bits.extractSigned(hexNumber, j, length)) + res[indexFilled - 1];
                    ++indexFilled;
                }
            }
        }
    }

    /**
     * Returns the array of profile samples of the given identity edge, which is empty if the edge does not have a profile.
     *
     * @param edgeId the id of the concerned edge
     * @return the array of profile samples
     */
    public float[] profileSamples(int edgeId) {
        int identity = Bits.extractUnsigned(profileIds.get(edgeId), 0, 30);
        int samplesNumber = 1 + (int) Math.ceil(length(edgeId) / 2);
        if (!hasProfile(edgeId)) return new float[0];

        float[] res = new float[samplesNumber];

        switch (getProfile(edgeId)) {
            case 1 -> profileSamplesType1(res, samplesNumber, identity);
            case 2 -> profileSamplesType2And3(res, samplesNumber, identity, PROFIL_SAMPLES_TYPE_2_DELTA);
            case 3 -> profileSamplesType2And3(res, samplesNumber, identity, PROFIL_SAMPLES_TYPE_3_DELTA);
        }

        return isInverted(edgeId) ? reverseFloatArray(res) : res;
    }

    /**
     * Returns the identity of the set of attributes attached to the given identity edge.
     *
     * @param edgeId the id of the concerned edge
     * @return the identity
     */
    public int attributesIndex(int edgeId) {
        return Short.toUnsignedInt(
                edgesBuffer.getShort(edgeId * NUMBER_OF_EDGES + OFFSET_OSM_ATTRIBUTES));
    }
}
