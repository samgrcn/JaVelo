package ch.epfl.javelo.data;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.projection.PointCh;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class Graph {

    private final GraphNodes nodes;
    private final GraphSectors sectors;
    private final GraphEdges edges;
    private final List<AttributeSet> attributeSets;


    public Graph(GraphNodes nodes, GraphSectors sectors, GraphEdges edges, List<AttributeSet> attributeSets) {
        this.nodes = nodes;
        this.sectors = sectors;
        this.edges = edges;
        this.attributeSets = List.copyOf(attributeSets);
    }

    private static ByteBuffer pathBuffer(Path path) throws IOException {
        ByteBuffer byteBuffer;
        try (FileChannel channel = FileChannel.open(path)) {
            byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        }
        return byteBuffer;
    }

    public static Graph loadFrom(Path basePath) throws IOException {
        IntBuffer nodesIdBuffer = pathBuffer(basePath.resolve("nodes.bin")).asIntBuffer();
        ByteBuffer sectorsIdBuffer = pathBuffer(basePath.resolve("sectors.bin"));
        ByteBuffer edgeIdBuffer = pathBuffer(basePath.resolve("edges.bin"));
        IntBuffer profileIdBuffer = pathBuffer(basePath.resolve("profile_ids.bin")).asIntBuffer();
        ShortBuffer elevationIdBuffer = pathBuffer(basePath.resolve("elevations.bin")).asShortBuffer();
        LongBuffer attributeSetIdBuffer = pathBuffer(basePath.resolve("attributes.bin")).asLongBuffer();

        List<AttributeSet> attributeSetList = new ArrayList<>();
        for (int i = 0; i < attributeSetIdBuffer.capacity(); i++) {
            attributeSetList.add(new AttributeSet(attributeSetIdBuffer.get(i)));
        }

        return new Graph(new GraphNodes(nodesIdBuffer),
                new GraphSectors(sectorsIdBuffer),
                new GraphEdges(edgeIdBuffer, profileIdBuffer, elevationIdBuffer), attributeSetList);
    }



    public int nodeCount() {
        return nodes.count();
    }

    public PointCh nodePoint(int nodeId) {
        return new PointCh(nodes.nodeE(nodeId), nodes.nodeN(nodeId));
    }

    public int nodeOutDegree(int nodeId) {
        return nodes.outDegree(nodeId);
    }

    public int nodeOutEdgeId(int nodeId, int edgeIndex) {
        return nodes.edgeId(nodeId, edgeIndex);
    }

    public int nodeClosestTo(PointCh point, double searchDistance) {
        List<GraphSectors.Sector> sectorsInArea = sectors.sectorsInArea(point, searchDistance);
        int index = -1;
        double min = Double.POSITIVE_INFINITY;
        double distance;
        for (GraphSectors.Sector sectorsInSquare : sectorsInArea) {
            for (int i = sectorsInSquare.startNodeId(); i < sectorsInSquare.endNodeId(); i++) {
                distance = point.squaredDistanceTo(nodePoint(i));
                if (distance < min) {
                    min = distance;
                    if(distance < searchDistance) {
                        index = i;
                    }
                }
            }
        }

        return index;
    }


    public int edgeTargetNodeId(int edgeId) {
        return edges.targetNodeId(edgeId);
    }

    public boolean edgeIsInverted(int edgeId) {
        return edges.isInverted(edgeId);
    }

    public AttributeSet edgeAttributes(int edgeId) { return attributeSets.get(edges.attributesIndex(edgeId)); }

    public double edgeLength(int edgeId) {
        return edges.length(edgeId);
    }

    public double edgeElevationGain(int edgeId) {
        return edges.elevationGain(edgeId);
    }

    public DoubleUnaryOperator edgeProfile(int edgeId) {
        if(!edges.hasProfile(edgeId)) { return Functions.constant(Double.NaN); }
        else {
            return Functions.sampled(edges.profileSamples(edgeId), edgeLength(edgeId));
        }
    }


}