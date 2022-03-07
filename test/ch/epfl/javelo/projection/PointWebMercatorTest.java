package ch.epfl.javelo.projection;
import ch.epfl.javelo.projection.PointWebMercator;
import ch.epfl.javelo.projection.WebMercator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointWebMercatorTest {

    @Test
    void testXAtZoomLevel() {
        PointWebMercator newPoint = new PointWebMercator(0.518275, 0.353664);
        double expectedValue = 69561722;
        assertEquals(expectedValue, newPoint.xAtZoomLevel(19), 100000);

        PointWebMercator newPoint2 = new PointWebMercator(0, 0);
        double expectedValue2 = 0;
        assertEquals(expectedValue2, newPoint2.xAtZoomLevel(19), 100000);
    }

    @Test
    void testYAtZoomLevel() {
        PointWebMercator newPoint = new PointWebMercator(0.518275, 0.353664);
        double expectedValue = 47468099;
        assertEquals(expectedValue, newPoint.yAtZoomLevel(19), 100000);
    }

    @Test
    void testxAtZoomLevel2() {
        PointWebMercator newPoint2 = new PointWebMercator(0, 0);
        double expectedValue2 = 0;
        assertEquals(expectedValue2, newPoint2.xAtZoomLevel(19), 100000);
    }

    @Test
    void testYAtZoomLevel2() {
        PointWebMercator newPoint2 = new PointWebMercator(0, 0);
        double expectedValue2 = 0;
        assertEquals(expectedValue2, newPoint2.yAtZoomLevel(19), 100000);
    }
}
