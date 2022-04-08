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
        int len = array.length;
        float[] newArray = new float[len];

        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[len - 1];
            --len;
        }
        return newArray;
    }

    /**
     * Returns the array of profile samples for an edge of type 1.
     *
     * @param res           the results array
     * @param samplesNumber the number of samples
     * @param identity      the identity of the first elevation
     * @return the array of profil samples in reverse order
     */
    private float[] profileSamplesType1(float[] res, int samplesNumber, int identity) {
        for (int i = 0; i < samplesNumber; ++i) {
            res[i] = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(identity + i)));
        }
        return res;
    }

    /**
     * Returns the array of profile samples for an edge of type 2.
     *
     * @param res           the results array
     * @param samplesNumber the number of samples
     * @param identity      the identity of the first elevation
     * @return the array of profil samples in reverse order
     */
    private float[] profileSamplesType2(float[] res, int samplesNumber, int identity) {
        int iterNumber = (int) Math.ceil((samplesNumber - 1) / 2.0) + 1;
        int indexFilled = 0;

        for (int i = 0; i < iterNumber; ++i) {
            if (i == 0) {
                res[i] = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(identity + i)));
                ++indexFilled;
            } else {
                int hexNumber = elevations.get(identity + i);
                for (int j = 8; j >= 0; j -= 8) {
                    if (indexFilled < samplesNumber) {
                        res[indexFilled] = Q28_4.asFloat(Bits.extractSigned(hexNumber, j, 8)) + res[indexFilled - 1];
                        ++indexFilled;
                    }
                }
            }
        }
        return res;
    }

    /**
     * Returns the array of profile samples for an edge of type 3.
     *
     * @param res           the results array
     * @param samplesNumber the number of samples
     * @param identity      the identity of the first elevation
     * @return the array of profil samples in reverse order
     */
    private float[] profileSamplesType3(float[] res, int samplesNumber, int identity) {
        int iterNumber = (int) Math.ceil((samplesNumber - 1) / 4.0) + 1;
        int indexFilled = 0;

        for (int i = 0; i < iterNumber; ++i) {
            if (i == 0) {
                res[i] = Q28_4.asFloat(Short.toUnsignedInt(elevations.get(identity + i)));
                ++indexFilled;
            } else {
                int hexNumber = elevations.get(identity + i);
                for (int j = 12; j >= 0; j -= 4) {
                    if (indexFilled < samplesNumber) {
                        res[indexFilled] = Q28_4.asFloat(Bits.extractSigned(hexNumber, j, 4)) + res[indexFilled - 1];
                        ++indexFilled;
                    }
                }
            }
        }
        return res;
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
            case 2 -> profileSamplesType2(res, samplesNumber, identity);
            case 3 -> profileSamplesType3(res, samplesNumber, identity);
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
