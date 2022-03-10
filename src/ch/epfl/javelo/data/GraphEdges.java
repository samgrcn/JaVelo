package ch.epfl.javelo.data;

import ch.epfl.javelo.Bits;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Q28_4;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {

    private static final int OFFSET_DIRECTION_IDENTITY = 0;
    private static final int OFFSET_LENGTH = OFFSET_DIRECTION_IDENTITY + 4;
    private static final int OFFSET_ALTITUDE_DIFF = OFFSET_LENGTH + 2;
    private static final int OFFSET_OSM_ATTRIBUTES = OFFSET_ALTITUDE_DIFF + 2;
    private static final int EDGES_INTS = OFFSET_OSM_ATTRIBUTES + 1;

    public boolean isInverted(int edgeId) {
        return edgesBuffer.get(edgeId + OFFSET_DIRECTION_IDENTITY) < 0;
    }

    public int targetNodeId(int edgeId) {
        int tempDirection = edgesBuffer.getInt(edgeId + OFFSET_DIRECTION_IDENTITY);
        if (tempDirection >= 0) return tempDirection;
        return ~tempDirection;
    }

    public double length(int edgeId) {
        return Q28_4.asDouble(edgesBuffer.getShort(edgeId + OFFSET_LENGTH));
    }

    public double elevationGain(int edgeId) {
        return Q28_4.asDouble(edgesBuffer.getShort(edgeId + OFFSET_ALTITUDE_DIFF));
    }

    public boolean hasProfile(int edgeId) {
        int profile = Bits.extractUnsigned(profileIds.get(edgeId), 30, 2);
        return profile == 3 || profile == 4;
    }

    public float[] profileSamples(int edgeId) {
        int identity = Bits.extractUnsigned(profileIds.get(edgeId), 0, 30);
        int samplesNumber = 1 + (int)Math.ceil(length(edgeId) / 2);
        int iterNumber = (int)Math.ceil((samplesNumber - 1) / 4.0) + 1;

        float[] res = new float[samplesNumber];
        for (int i = 0; i < iterNumber; ++i) {
            if (i == 0) res[i] = elevations.get(identity - 1 + i);
            else res[i] = elevations.get(identity + i);
        }
        return res;
    }

    public int attributesIndex(int edgeId) {
        return 0;
    }
}
