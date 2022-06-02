package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.PointCh;

/**
 * Record to represent a point according to his position and his closest node.
 *
 * @param position      the position of the point
 * @param closestNodeId his closest node
 * @author Samuel Garcin (345633)
 */
public record Waypoint(PointCh position, int closestNodeId) {
}
