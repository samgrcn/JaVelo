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

        double xMinSquare = center.e() - SwissBounds.MIN_E - distance;
        double xMaxSquare = center.e() - SwissBounds.MIN_E + distance;
        double yMinSquare = center.n() - SwissBounds.MIN_N - distance;
        double yMaxSquare = center.n() - SwissBounds.MIN_N + distance;

        double xMinSector = clamp(0, xMinSquare / SECTOR_WIDTH, SECTOR_NUMBER - 1);
        double xMaxSector = clamp(0, xMaxSquare / SECTOR_WIDTH, SECTOR_NUMBER - 1);
        double yMinSector = clamp(0, yMinSquare / SECTOR_HEIGHT, SECTOR_NUMBER - 1);
        double yMaxSector = clamp(0, yMaxSquare / SECTOR_HEIGHT, SECTOR_NUMBER - 1);

        int xMinSectorInSwiss = (int) xMinSector;
        int xMaxSectorInSwiss = (int) xMaxSector;
        int yMinSectorInSwiss = (int) yMinSector;
        int yMaxSectorInSwiss = (int) yMaxSector;

        int firstNodeId;
        int endNodeId;
        int sectorId;

        ArrayList<Sector> listOfSectorsInSquare = new ArrayList<>();

        for(int y = yMinSectorInSwiss; y <= yMaxSectorInSwiss; y++) {
            for(int x = xMinSectorInSwiss; x <= xMaxSectorInSwiss; x++) {
                sectorId = y * SECTOR_NUMBER + x;
                firstNodeId = buffer.getInt(sectorId * OFFSET_TO_NEXT_SECTOR);
                endNodeId = firstNodeId + Short.toUnsignedInt(buffer.getShort(sectorId + OFFSET_TO_NODE_NUMBER));
                listOfSectorsInSquare.add(new Sector(firstNodeId, endNodeId));
            }
        }

        return listOfSectorsInSquare;
    }




}
