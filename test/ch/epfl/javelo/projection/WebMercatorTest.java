package ch.epfl.javelo.projection;
import ch.epfl.javelo.projection.WebMercator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebMercatorTest {

    @Test
    void testX() {
        double expectedValue = 0.518275;
        assertEquals(expectedValue, WebMercator.x(0.11482655888), 0.00001);
    }

    @Test
    void testY() {
        double expectedValue = 0.353664;
        assertEquals(expectedValue, WebMercator.y(0.8119602874), 0.00001);
    }

    @Test
    void testLon() {
        double expectedValue = 0.11482655888;
        assertEquals(expectedValue, WebMercator.lon(0.518275), 0.00001);
    }

    @Test
    void testLat() {
        double expectedValue = 0.8119602874;
        assertEquals(expectedValue, WebMercator.lat(0.353664), 0.00001);
    }

}
