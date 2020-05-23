package tool.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tool.map.Station;
import tool.map.Path;
import tool.map.Graph;

public class DijkstraAlgorithm {

    private final List<Station> nodes;
    private final List<Path> edges;
    private Set<Station> settledNodes;
    private Set<Station> unSettledNodes;
    private Map<Station, Station> predecessors;
    private Map<Station, Integer> distance;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Station>(graph.getStations());
        this.edges = new ArrayList<Path>(graph.getPaths());
    }

    public void execute(Station source) {
        settledNodes = new HashSet<Station>();
        unSettledNodes = new HashSet<Station>();
        distance = new HashMap<Station, Integer>();
        predecessors = new HashMap<Station, Station>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Station node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Station node) {
        List<Station> adjacentNodes = getNeighbors(node);
        for (Station target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(Station node, Station target) {
        for (Path edge : edges) {
            if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Station> getNeighbors(Station node) {
        List<Station> neighbors = new ArrayList<Station>();
        for (Path edge : edges) {
            if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private Station getMinimum(Set<Station> vertexes) {
        Station minimum = null;
        for (Station vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Station vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Station destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and NULL
     * if no path exists
     */
    public LinkedList<Station> getPath(Station target) {
        LinkedList<Station> path = new LinkedList<Station>();
        Station step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
