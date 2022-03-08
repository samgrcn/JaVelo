package ch.epfl.javelo.data;


import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record GraphSectors(ByteBuffer buffer) {



    public record Sector(int startNodeId, int endNodeId) {
    }

    public List<Sector> sectorInArea(PointCh center, double distance) {

        final int OFFSET_BYTE_NODE_NUMBER = 4;
        final int OFFSET_NEXT_SECTOR = 6;

        double xMin = SwissBounds.MAX_E - (center.e() - distance);
        double xMax = SwissBounds.MAX_E - (center.e() + distance);
        double yMin = SwissBounds.MAX_N - (center.n() - distance);
        double yMax = SwissBounds.MAX_N - (center.n() + distance);

        double sectorLengthX = SwissBounds.MAX_E / 128;
        double sectorWidthY  = SwissBounds.MAX_N / 128;

        double sectorInSquareX = (int)(xMin / sectorLengthX);
        double sectorInSquareY = (int)(yMin / sectorWidthY);

        List<Sector> listOfSectorsInSquare = new ArrayList<>();

        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 128; x++) {
                if(x * sectorLengthX >= xMin) {
                    int selectedNode = x * OFFSET_NEXT_SECTOR;
                    listOfSectorsInSquare.add(new Sector(
                                    buffer.get(buffer.getInt(selectedNode)),
                                    buffer.get(buffer.getInt(selectedNode + OFFSET_BYTE_NODE_NUMBER))));
                }
            }
        }

        return listOfSectorsInSquare;
    }




}
