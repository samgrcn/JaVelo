package ch.epfl.javelo.data;


import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record GraphSectors(ByteBuffer buffer) {

    private static final int OFFSET_BYTE_NODE_NUMBER = 4;
    private static final int OFFSET_NEXT_SECTOR = 6;
    private static final int SECTOR_NUMBER = 128;


    public record Sector(int startNodeId, int endNodeId) {
    }

    public List<Sector> sectorsInArea(PointCh center, double distance) {


        double xMin = SwissBounds.MAX_E - (center.e() - distance);
        double xMax = SwissBounds.MAX_E - (center.e() + distance);
        double yMin = SwissBounds.MAX_N - (center.n() - distance);
        double yMax = SwissBounds.MAX_N - (center.n() + distance);

        double sectorWidth = SwissBounds.MAX_E / SECTOR_NUMBER;
        double sectorHeight = SwissBounds.MAX_N / SECTOR_NUMBER;

        ArrayList<Sector> listOfSectorsInSquare = new ArrayList<>();

        for (int y = 0; y < SECTOR_NUMBER; y++) {
            for (int x = 0; x < SECTOR_NUMBER; x++) {
                if(x * sectorWidth >= xMin && (x - 1) * sectorHeight <= xMax) {
                    if(y * sectorHeight >= yMin && (y - 1) * sectorHeight <= yMax) {
                        int selectedNode = x * OFFSET_NEXT_SECTOR;
                        listOfSectorsInSquare.add(new Sector(
                                buffer.get(buffer.getInt(selectedNode)),
                                buffer.get(buffer.getShort(selectedNode + OFFSET_BYTE_NODE_NUMBER))));
                    }
                }
            }
        }

        return listOfSectorsInSquare;
    }




}
