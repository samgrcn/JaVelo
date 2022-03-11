package ch.epfl.javelo.data;


import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static ch.epfl.javelo.Math2.clamp;

public record GraphSectors(ByteBuffer buffer) {

    private static final int OFFSET_TO_NODE_NUMBER = Integer.BYTES;
    private static final int OFFSET_TO_NEXT_SECTOR = OFFSET_TO_NODE_NUMBER + Short.BYTES;
    private static final int SECTOR_NUMBER = 128;


    private static final double SECTOR_WIDTH = SwissBounds.WIDTH / SECTOR_NUMBER;
    private static final double SECTOR_HEIGHT = SwissBounds.HEIGHT / SECTOR_NUMBER;

    public record Sector(int startNodeId, int endNodeId) {
    }

    public List<Sector> sectorsInArea(PointCh center, double distance) {

        assert SwissBounds.containsEN(center.e(), center.n());

        double xMin = center.e() - SwissBounds.MIN_E - distance;
        double xMax = center.e() - SwissBounds.MIN_E + distance;
        double yMin = center.n() - SwissBounds.MIN_N - distance;
        double yMax = center.n() - SwissBounds.MIN_N + distance;


        double xMinSectorInSquare = clamp(0, xMin / SECTOR_WIDTH, SECTOR_NUMBER - 1);
        double xMaxSectorInSquare = clamp(0, xMax / SECTOR_WIDTH, SECTOR_NUMBER - 1);
        double yMinSectorInSquare = clamp(0, yMin / SECTOR_HEIGHT, SECTOR_NUMBER - 1);
        double yMaxSectorInSquare = clamp(0, yMax / SECTOR_HEIGHT, SECTOR_NUMBER - 1);

        int axMinSectorInSquare = (int) xMinSectorInSquare;
        int axMaxSectorInSquare = (int) xMaxSectorInSquare;
        int ayMinSectorInSquare = (int) yMinSectorInSquare;
        int ayMaxSectorInSquare = (int) yMaxSectorInSquare;

        int firstNodeId;
        int endNodeId;
        int sectorId;

        ArrayList<Sector> listOfSectorsInSquare = new ArrayList<>();

        for(int y = ayMinSectorInSquare; y <= ayMaxSectorInSquare; y++) {
            for(int x = axMinSectorInSquare; x <= axMaxSectorInSquare; x++) {
                sectorId = y * SECTOR_NUMBER + x;
                firstNodeId = buffer.getInt(sectorId * OFFSET_TO_NEXT_SECTOR);
                endNodeId = firstNodeId + Short.toUnsignedInt(buffer.getShort(sectorId + OFFSET_TO_NODE_NUMBER));
                listOfSectorsInSquare.add(new Sector(firstNodeId, endNodeId));
            }
        }

        return listOfSectorsInSquare;
    }




}
