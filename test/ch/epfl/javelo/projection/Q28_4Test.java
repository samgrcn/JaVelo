package ch.epfl.javelo.projection;
import ch.epfl.javelo.Q28_4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Q28_4Test {
    @Test
    void testOfInt() {
        assertEquals(2048, Q28_4.ofInt(128));
    }

    @Test
    void testAsDouble() {
        assertEquals(-6.25, Q28_4.asDouble(0b0000000000000000000000010011100));
    }

    @Test
    void testAsDouble2() {
        assertEquals(-128, Q28_4.asDouble(2048));
    }

    @Test
    void testAsFloat() {
        assertEquals(-6.25, Q28_4.asFloat(0b0000000000000000000000010011100));
    }
}
