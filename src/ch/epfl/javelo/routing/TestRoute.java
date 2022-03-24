package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.projection.PointCh;

import java.util.Arrays;
import java.util.List;

public class TestRoute implements Route {

        private final List<Edge> edges;
        private double length;

        public TestRoute(List<Edge> edges) {
            this.edges = edges;
            for(Edge e : edges) {
                length += e.length();
            }
        }

        @Override
        public int indexOfSegmentAt(double position) {
            return 0;
        }

        @Override
        public double length() {
            return length;
        }

        @Override
        public List<Edge> edges() {
            return null;
        }

        @Override
        public List<PointCh> points() {
            return null;
        }

        @Override
        public PointCh pointAt(double position) {
            return null;
        }

        @Override
        public double elevationAt(double position) {
            position = Math2.clamp(0, position, this.length());
            int edgeIndex = edgeIndex(position);
            position -= nodesPositions()[edgeIndex]; //to become the position on the edge
            return edges.get(edgeIndex).elevationAt(position);
        }

        @Override
        public int nodeClosestTo(double position) {
            return 0;
        }

        @Override
        public RoutePoint pointClosestTo(PointCh point) {
            return null;
        }

        /**
         * Returns the index of the edge on which position is. For the points between edges, returns next edge
         * (the edge starting at position), and the last point of the itinerary is counted on the last edge
         * @param position (double) : must be clamped before given as a param (method only works for valid positions)
         * @return index of the edge on which position is
         */
        private int edgeIndex(double position) {

            int value = Arrays.binarySearch(nodesPositions(), position);
            int edgeIndex;

            if (value < 0) { //position is in one of the edges
                edgeIndex = - value - 2;
            }
            else if (value == edges.size()) { //position is the end of the route
                edgeIndex = value - 1; //For the last point, we want to look in the last Edge
            }
            else { //position is on one of the nodes of the route
                edgeIndex = value;
            }

            return edgeIndex;
        }

        /**
         * Returns a list containing all positions of the nodes contained in the Route
         */
        private double[] nodesPositions() {
            double runningLength = 0;
            double[] positions = new double[edges.size() + 1];
            positions[0] = 0.0;
            for(int i = 0; i < edges.size(); i++) {
                runningLength += edges.get(i).length();
                positions[i + 1] = runningLength;
            }
            return positions;
        }
    }
