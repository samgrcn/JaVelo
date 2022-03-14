package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.PointCh;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class Graph {

    GraphNodes nodes;
    GraphSectors sectors;
    GraphEdges edges;
    List<AttributeSet> attributeSets;

    public Graph(GraphNodes nodes, GraphSectors sectors, GraphEdges edges, List<AttributeSet> attributeSets) {
        this.nodes = nodes;
        this.sectors = sectors;
        this.edges = edges;
        this.attributeSets = attributeSets;
    }

    public static Graph loadFrom(Path basePath) throws IOException {
        Path nodesPath = basePath.resolve("nodes.bin");
        IntBuffer nodesIdBuffer;
        try (FileChannel channel = FileChannel.open(nodesPath)) {
            nodesIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asReadOnlyBuffer()
                    .asIntBuffer();
        }

        Path sectorsPath = basePath.resolve("sectors.bin");
        ByteBuffer sectorsIdBuffer;
        try (FileChannel channel = FileChannel.open(sectorsPath)) {
            sectorsIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asReadOnlyBuffer();
        }

        Path edgePath = basePath.resolve("edges.bin");
        ByteBuffer edgeIdBuffer;
        try (FileChannel channel = FileChannel.open(edgePath)) {
            edgeIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asReadOnlyBuffer();
        }

        Path profilePath = basePath.resolve("profile_ids.bin");
        IntBuffer profileIdBuffer;
        try (FileChannel channel = FileChannel.open(profilePath)) {
            profileIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asReadOnlyBuffer()
                    .asIntBuffer();
        }

        Path elevationPath = basePath.resolve("elevations.bin");
        ShortBuffer elevationIdBuffer;
        try (FileChannel channel = FileChannel.open(elevationPath)) {
            elevationIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asReadOnlyBuffer()
                    .asShortBuffer();
        }


        Path attributeSetPath = basePath.resolve("attributes.bin");
        LongBuffer attributeSetIdBuffer;
        try (FileChannel channel = FileChannel.open(attributeSetPath)) {
            attributeSetIdBuffer = channel
                    .map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
                    .asReadOnlyBuffer()
                    .asLongBuffer();
        }
        List<AttributeSet> attributeSetList = new ArrayList<>();
        for (int i = 0; i < attributeSetIdBuffer.capacity(); i++) {
            attributeSetList.add(new AttributeSet(attributeSetIdBuffer.get(i)));
        }


        new Graph(new GraphNodes(nodesIdBuffer),
                new GraphSectors(sectorsIdBuffer),
                new GraphEdges(edgeIdBuffer, profileIdBuffer, elevationIdBuffer), attributeSetList);
    }



    public int nodeCount() {
        return nodes.count();
    }

    public PointCh nodePoint(int nodeId) {
        //return nodes.buffer().get(nodeId);
    }

    public int nodeOutDegree(int nodeId) {
    }

    public int nodeOutEdgeId(int nodeId, int edgeIndex {
    }

    public int nodeClosestTo(PointCh point, double searchDistance) {
    }

    public int edgeTargetNodeId(int edgeId) {
    }

    public boolean edgeIsInverted(int edgeId) {
    }

    public AttributeSet edgeAttribute(int edgeId) {
    }

    public double edgeLength(int edgeId) {
    }

    // public double edgeElevationGain(int edgeId) {}

    // public DoubleUnaryOperator edgeProfile(int edgeId) {}


}
