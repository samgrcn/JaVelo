package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

class GraphSectorsTest {

    @Test
    void AllSectorsInArea() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10000000);
        for (int i = 0; i < 200000; i++) {
            byteBuffer.putInt(i)
                    .putShort((short) (1));
        }
        GraphSectors graphSectors=new GraphSectors(byteBuffer);
        List<GraphSectors.Sector> sectors = graphSectors.sectorsInArea(new PointCh(SwissBounds.MAX_E, SwissBounds.MAX_N),2000);
        int actual=sectors.size();
        assertEquals(2 ,actual);

    }
}