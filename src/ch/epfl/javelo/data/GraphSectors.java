package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static ch.epfl.javelo.Math2.clamp;

/**
 * Allows representation of sectors, dividing Switzerland in 128x128 rectangles.
 *
 * @author Samuel Garcin (345633)
 */

public record GraphSectors(ByteBuffer buffer) {

    private static final int OFFSET_TO_NODE_NUMBER = Integer.BYTES; //number of bytes in an integer, = 4
    private static final int OFFSET_TO_NEXT_SECTOR = OFFSET_TO_NODE_NUMBER + Short.BYTES; //number of bytes per sector, = 6
    private static final int SECTOR_NUMBER = 128; //number of sectors


    private static final double SECTOR_WIDTH = SwissBounds.WIDTH / SECTOR_NUMBER; //width of a sector
    private static final double SECTOR_HEIGHT = SwissBounds.HEIGHT / SECTOR_NUMBER; //height of a sector

    /**
     * Small record to create a list of Sectors later on.
     *
     * @param startNodeId the first node of a sector.
     * @param endNodeId   and its last.
     */
    public record Sector(int startNodeId, int endNodeId) {
    }


    /**
     * Gives the list of all sectors intersecting (having at least a point) with the square centered at the given point and of side equal
     * to twice the given distance.
     *
     * @param center   the center of the square (must be in Switzerland)
     * @param distance half the length of a side
     * @return the list of all sectors in switzerland having at least one point in the square.
     * @throws IllegalArgumentException if the center is outside Switzerland
     */
    public List<Sector> sectorsInArea(PointCh center, double distance) {


        double xMinSquare = center.e() - SwissBounds.MIN_E - distance; //minimum coordinates of the square on the x-Axis
        double xMaxSquare = center.e() - SwissBounds.MIN_E + distance; //maximum coordinates of the square on the x-Axis
        double yMinSquare = center.n() - SwissBounds.MIN_N - distance; //minimum coordinates of the square on the y-Axis
        double yMaxSquare = center.n() - SwissBounds.MIN_N + distance; //maximum coordinates of the square on the y-Axis

        double xMinSector = clamp(0, xMinSquare / SECTOR_WIDTH, SECTOR_NUMBER - 1); //index of the first sector in the square on the x-Axis
        double xMaxSector = clamp(0, xMaxSquare / SECTOR_WIDTH, SECTOR_NUMBER - 1); //index of the last sector in the square on the x-Axis
        double yMinSector = clamp(0, yMinSquare / SECTOR_HEIGHT, SECTOR_NUMBER - 1); //index of the first sector in the square on the y-Axis
        double yMaxSector = clamp(0, yMaxSquare / SECTOR_HEIGHT, SECTOR_NUMBER - 1); //index of the last sector in the square on the y-Axis

        //casting in integer
        int xMinSectorInSwiss = (int) xMinSector;
        int xMaxSectorInSwiss = (int) xMaxSector;
        int yMinSectorInSwiss = (int) yMinSector;
        int yMaxSectorInSwiss = (int) yMaxSector;

        int firstNodeId;
        int endNodeId;
        int sectorId;

        ArrayList<Sector> listOfSectorsInSquare = new ArrayList<>();

        //for each sector in the range of the square, we add to the list a Sector with the first 4 bytes (= an integer)
        //corresponding to the id of the first node, then we add the last 2 bytes (the number of nodes in the sector)
        //to the first id to get the last id of the node in the sector.

        for (int y = yMinSectorInSwiss; y <= yMaxSectorInSwiss; y++) {
            for (int x = xMinSectorInSwiss; x <= xMaxSectorInSwiss; x++) {
                sectorId = y * SECTOR_NUMBER + x;
                firstNodeId = buffer.getInt(sectorId * OFFSET_TO_NEXT_SECTOR);
                endNodeId = firstNodeId +
                        Short.toUnsignedInt(
                                buffer.getShort(sectorId * OFFSET_TO_NEXT_SECTOR + OFFSET_TO_NODE_NUMBER));
                listOfSectorsInSquare.add(new Sector(firstNodeId, endNodeId));
            }
        }

        return listOfSectorsInSquare;
    }
}
