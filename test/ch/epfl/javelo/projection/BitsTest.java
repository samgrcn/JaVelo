package ch.epfl.javelo.projection;
import ch.epfl.javelo.Bits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitsTest {

    @Test
    void testExtractSigned() {
        assertEquals(0b11111111111111111111111111111010, Bits.extractSigned(0b11001010111111101011101010111110, 8, 4));
    }

    @Test
    void testExtractUnsigned2() {
        assertEquals(8, Bits.extractUnsigned(0b10000000000000000000000000000000,28,4));
    }

    @Test
    void testExtractUnsigned() {
        assertEquals(0b00000000000000000000000000001010, Bits.extractUnsigned(0b11001010111111101011101010111110, 8, 4));
    }
}
