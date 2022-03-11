package ch.epfl.javelo.data;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.junit.jupiter.api.Assertions.*;

class GraphEdgesTest {

    @Test
    void graphEdgesTest() {
        ByteBuffer edgesBuffer = ByteBuffer.allocate(10);
        // Sens : inversé. Nœud destination : 12.
        edgesBuffer.putInt(0, ~12);
        // Longueur : 0x10.b m (= 16.6875 m)
        edgesBuffer.putShort(4, (short) 0x10_b);
        // Dénivelé : 0x10.0 m (= 16.0 m)
        edgesBuffer.putShort(6, (short) 0x10_0);
        // Identité de l'ensemble d'attributs OSM : 2022
        edgesBuffer.putShort(8, (short) 2022);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 3. Index du premier échantillon : 1.
                (3 << 30) | 1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF,
                (short) 0xFFFE, (short) 0xF000
        });

        GraphEdges edges =
                new GraphEdges(edgesBuffer, profileIds, elevations);

        assertTrue(edges.isInverted(0));
        assertEquals(12, edges.targetNodeId(0));
        assertEquals(16.6875, edges.length(0));
        assertEquals(16.0, edges.elevationGain(0));
        assertTrue(edges.hasProfile(0));
        assertEquals(2022, edges.attributesIndex(0));
        float[] expectedSamples = new float[]{
                384.0625f, 384.125f, 384.25f, 384.3125f, 384.375f,
                384.4375f, 384.5f, 384.5625f, 384.6875f, 384.75f
        };
        assertArrayEquals(expectedSamples, edges.profileSamples(0));
    }

    @Test
    void graphEdgesTest2() {
        ByteBuffer edgesBuffer = ByteBuffer.allocate(10);
        // Sens : inversé. Nœud destination : 24.
        edgesBuffer.putInt(0, ~-24);
        // Longueur :
        edgesBuffer.putShort(4, (short) 0x1f_e);
        // Dénivelé :
        edgesBuffer.putShort(6, (short) 0x1f_0);
        // Identité de l'ensemble d'attributs OSM : 234
        edgesBuffer.putShort(8, (short) 234);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 2. Index du premier échantillon : 1.
                (0 << 30) | 1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF,
                (short) 0xFFFE, (short) 0xF000
        });

        GraphEdges edges =
                new GraphEdges(edgesBuffer, profileIds, elevations);

        assertFalse(edges.isInverted(0));
        assertEquals(23, edges.targetNodeId(0));
        assertEquals(31.8, edges.length(0), 0.1);
        assertEquals(31.0, edges.elevationGain(0));
        assertFalse(edges.hasProfile(0));
        assertEquals(234, edges.attributesIndex(0));
        float[] expectedSamples = new float[0];
        assertArrayEquals(expectedSamples, edges.profileSamples(0));
    }

    @Test
    void graphEdgesTest3() {
        ByteBuffer edgesBuffer = ByteBuffer.allocate(20);
        // Sens : inversé. Nœud destination : 24.
        edgesBuffer.putInt(0, ~24);
        // Longueur :
        edgesBuffer.putShort(4, (short) 0x1f_e);
        // Dénivelé :
        edgesBuffer.putShort(6, (short) 0x1f_0);
        // Identité de l'ensemble d'attributs OSM : 234
        edgesBuffer.putShort(8, (short) 234);

        // index 2
        // Sens : inversé. Nœud destination : 24.
        edgesBuffer.putInt(10, ~40);
        // Longueur :
        edgesBuffer.putShort(14, (short) 0x0_0);
        // Dénivelé :
        edgesBuffer.putShort(16, (short) 0xff_0);
        // Identité de l'ensemble d'attributs OSM : 234
        edgesBuffer.putShort(18, (short) -34);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 2. Index du premier échantillon : 1.
                (2 << 30) | 1, (0 << 30) | 1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF,
                (short) 0xFFFE, (short) 0xF000
        });

        GraphEdges edges =
                new GraphEdges(edgesBuffer, profileIds, elevations);

        assertTrue(edges.isInverted(1));
        assertEquals(40, edges.targetNodeId(1));
        assertEquals(0.0, edges.length(1), 0.1);
        assertEquals(255, edges.elevationGain(1), 0.01);
        assertFalse(edges.hasProfile(1));
        assertEquals(65502, edges.attributesIndex(1));
        float[] expectedSamples = new float[0];
        assertArrayEquals(expectedSamples, edges.profileSamples(1));
    }

    @Test
    public void check(){
        ByteBuffer edgesBuffer = ByteBuffer.allocate(10);
// Sens : inversé. Nœud destination : 12.
        edgesBuffer.putInt(0, ~12);
// Longueur : 0x10.b m (= 16.6875 m)
        edgesBuffer.putShort(4, (short) 0x10_b);
// Dénivelé : 0x10.0 m (= 16.0 m)
        edgesBuffer.putShort(6, (short) 0x10_0);
// Identité de l'ensemble d'attributs OSM : 2022
        edgesBuffer.putShort(8, (short) 2022);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 3. Index du premier échantillon : 1.
                (3 << 30) | 1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF,
                (short) 0xFFFE, (short) 0xF000
        });

        GraphEdges edges =
                new GraphEdges(edgesBuffer, profileIds, elevations);

        assertTrue(edges.isInverted(0));
        assertEquals(12, edges.targetNodeId(0));
        assertEquals(16.6875, edges.length(0));
        assertEquals(16.0, edges.elevationGain(0));
        assertEquals(2022, edges.attributesIndex(0));
        float[] expectedSamples = new float[]{
                384.0625f, 384.125f, 384.25f, 384.3125f, 384.375f,
                384.4375f, 384.5f, 384.5625f, 384.6875f, 384.75f
        };
        assertArrayEquals(expectedSamples, edges.profileSamples(0));
    }

    @Test
    public void testForCase0(){
        ByteBuffer edgesBuffer = ByteBuffer.allocate(10);
// Sens : inversé. Nœud destination : 12.
        edgesBuffer.putInt(0, ~12);
// Longueur : 0x10.b m (= 16.6875 m)
        edgesBuffer.putShort(4, (short) 0x10_b);
// Dénivelé : 0x10.0 m (= 16.0 m)
        edgesBuffer.putShort(6, (short) 0x10_0);
// Identité de l'ensemble d'attributs OSM : 2022
        edgesBuffer.putShort(8, (short) 2022);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 3. Index du premier échantillon : 1.
                1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF,
                (short) 0xFFFE, (short) 0xF000
        });

        GraphEdges edges =
                new GraphEdges(edgesBuffer, profileIds, elevations);

        float[] expectedSamples = new float[0];
        assertArrayEquals(expectedSamples, edges.profileSamples(0));
    }

    @Test
    public void testForCase2() {

        ByteBuffer edgesBuffer = ByteBuffer.allocate(10);
        edgesBuffer.putInt(0, ~12);
        edgesBuffer.putShort(4, (short) 0x10_b);
        edgesBuffer.putShort(6, (short) 0x10_0);
        edgesBuffer.putShort(8, (short) 2022);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 3. Index du premier échantillon : 1.
                (2 << 30) | 1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF,
                (short) 0xFFFE, (short) 0xF000,
                (short) 0xF000, (short) 0xABC7,
                (short) 0x984F, (short) 0xFABC,
        });

        GraphEdges edges =
                new GraphEdges(edgesBuffer, profileIds, elevations);

        float[] expectedSamples = new float[]{
                377.0625f, 382.375f, 382.375f, 383.375f, 383.375f,
                384.375f, 384.5f, 384.5625f, 384.625f, 384.75f
        };
        // [377.0625, 382.375, 382.375, 383.375, 383.375, 384.375, 384.5, 384.5625, 384.625, 384.75]

        assertArrayEquals(expectedSamples, edges.profileSamples(0));
    }
}
