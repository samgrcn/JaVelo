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

/**
 * Builds a graph, and gives methods to interact nodes, sectors, edges, attributes in the graph.
 *
 * @author Samuel Garcin (345633)
 */

public class Graph {

    private final GraphNodes nodes;
    private final GraphSectors sectors;
    private final GraphEdges edges;
    private final List<AttributeSet> attributeSets;

    /**
     * Returns an instance of the complete Graph with the desired nodes, sectors, edges, attributeSets entered
     * in parameter.
     * @param nodes nodes
     * @param sectors sectors
     * @param edges edges
     * @param attributeSets attributeSets
     */


    public Graph(GraphNodes nodes, GraphSectors sectors, GraphEdges edges, List<AttributeSet> attributeSets) {
        this.nodes = nodes;
        this.sectors = sectors;
        this.edges = edges;
        this.attributeSets = List.copyOf(attributeSets);
    }

    /**
     * Private method to open the wanted file and map it to a buffer of bytes
     * @param path the wanted file (not only the folder)
     * @return the mapped buffer
     * @throws IOException if the file doesn't exist
     */

    private static ByteBuffer pathBuffer(Path path) throws IOException {
        ByteBuffer byteBuffer;
        try (FileChannel channel = FileChannel.open(path)) {
            byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        }
        return byteBuffer;
    }

    /**
     * Returns the JaVelo graph obtained from the files in the directory whose path is basePath,
     * or throws IOException if there is an input/output error,
     * e.g. if one of the expected files does not exist.
     * @param basePath path of the folder
     * @return a JaVelo graph obtained from the files in the directory whose path is basePath
     * @throws IOException if one of the expected file doesn't exist.
     */

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


    /**
     * Counts the number of nodes in the graph.
     * @return the total number of nodes in the graph.
     */
    public int nodeCount() {
        return nodes.count();
    }

    /**
     * Gives the position of the given identity node
     * @param nodeId identity of the desired node
     * @return the position of the node
     */
    public PointCh nodePoint(int nodeId) {
        return new PointCh(nodes.nodeE(nodeId), nodes.nodeN(nodeId));
    }

    /**
     * Gives the number of edges coming out of the given identity node.
     * @param nodeId the identity of the desired node
     * @return the number of edges coming out of the node
     */

    public int nodeOutDegree(int nodeId) {
        return nodes.outDegree(nodeId);
    }

    /**
     * Gives the identity of the edgeIndex-th edge coming out of the node of identity nodeId.
     * @param nodeId identity of the desired node
     * @param edgeIndex index of the desired edge
     * @return the identity of the desired edge of the desired node
     */

    public int nodeOutEdgeId(int nodeId, int edgeIndex) {
        return nodes.edgeId(nodeId, edgeIndex);
    }

    /**
     * Gives the identity of the closest node to the given point, at the given maximum distance (in meters),
     * or -1 if no node matches these criteria
     * @param point reference point
     * @param searchDistance distance to find the closest node (in meters)
     * @return the closest node to the point
     */

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

    /**
     * Gives the identity of the destination node of the given identity edge.
     * @param edgeId identity of the desired edge
     * @return identity of the node at the end of the edge
     */

    public int edgeTargetNodeId(int edgeId) {
        return edges.targetNodeId(edgeId);
    }

    /**
     * Returns true if and only if the given identity edge goes in the
     * opposite direction of the OSM path from which it comes.
     * @param edgeId identity of the desired edge
     * @return true if given identity edge goes in the opposite direction of the OSM path from which it comes
     */

    public boolean edgeIsInverted(int edgeId) {
        return edges.isInverted(edgeId);
    }

    /**
     * Gives the set of OSM attributes attached to the given identity edge.
     * @param edgeId identity of the desired edge
     * @return the set of OSM attributes of the desired edge
     */

    public AttributeSet edgeAttributes(int edgeId) { return attributeSets.get(edges.attributesIndex(edgeId)); }

    /**
     * Gives the length, in meters, of the given identity edge.
     * @param edgeId identity of the desired edge
     * @return length, in meters, of the given identity edge
     */

    public double edgeLength(int edgeId) {
        return edges.length(edgeId);
    }

    /**
     * Gives the total positive elevation of the given identity ridge.
     * @param edgeId identity of the desired edge
     * @return the total positive elevation of the given identity ridge
     */

    public double edgeElevationGain(int edgeId) {
        return edges.elevationGain(edgeId);
    }

    /**
     *  Gives the long profile of the given identity edge, as a function; if the edge has no profile,
     *  then this function returns Double.NaN for any argument.
     * @param edgeId identity of the desired edge
     * @return the long profile of the edge
     */

    public DoubleUnaryOperator edgeProfile(int edgeId) {
        if(!edges.hasProfile(edgeId)) { return Functions.constant(Double.NaN); }
        else {
            return Functions.sampled(edges.profileSamples(edgeId), edgeLength(edgeId));
        }
    }


}