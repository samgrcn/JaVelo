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

        double xMin = clamp(0, (center.e() - SwissBounds.MIN_E) - distance, SwissBounds.WIDTH - SECTOR_WIDTH);
        double xMax = clamp(0, (center.e() - SwissBounds.MIN_E) + distance, SwissBounds.WIDTH - SECTOR_WIDTH);

        double yMin = clamp(0, (center.n() - SwissBounds.MIN_N) - distance, SwissBounds.HEIGHT - SECTOR_HEIGHT);
        double yMax = clamp(0, (center.n() - SwissBounds.MIN_N) + distance, SwissBounds.HEIGHT - SECTOR_HEIGHT);

        int xMinSectorInSquare = (int) (xMin / SECTOR_WIDTH);
        int xMaxSectorInSquare = (int) (xMax / SECTOR_WIDTH);
        int yMinSectorInSquare = (int) (yMin / SECTOR_HEIGHT);
        int yMaxSectorInSquare = (int) (yMax / SECTOR_HEIGHT);

        int firstNodeId;
        int lastNodeId;
        int sectorId;

        ArrayList<Sector> listOfSectorsInSquare = new ArrayList<>();

        for(int y = yMinSectorInSquare; y <= yMaxSectorInSquare; y++) {
            for(int x = xMinSectorInSquare; x <= xMaxSectorInSquare; x++) {
                sectorId = y * SECTOR_NUMBER + x;
                firstNodeId = buffer.get(buffer.getInt(sectorId * OFFSET_TO_NEXT_SECTOR ));
                lastNodeId = buffer.get(buffer.getShort( sectorId * OFFSET_TO_NEXT_SECTOR + OFFSET_TO_NODE_NUMBER));
                listOfSectorsInSquare.add(new Sector(firstNodeId, lastNodeId));
            }
        }

        return listOfSectorsInSquare;
    }




}
