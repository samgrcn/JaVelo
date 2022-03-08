package ch.epfl.javelo.data;


import ch.epfl.javelo.projection.PointCh;

import java.nio.ByteBuffer;
import java.util.List;

public record GraphSectors(ByteBuffer buffer) {



    public record Sector(int startNodeId, int endNodeId) {
    }

    public List<Sector> sectorInArea(PointCh center, double distance) {
        double xMin = center.e() - distance;
        double xMax = center.e() + distance;
        double yMin = center.n() - distance;
        double yMax = center.n() + distance;




        return null;
    }




}
