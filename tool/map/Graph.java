package tool.map;

import java.util.List;

import tool.map.Station;
import tool.map.Path;

public class Graph {
    private final List<Station> stations;
    private final List<Path> paths;

    public Graph(List<Station> stations, List<Path> paths) {
        this.stations = stations;
        this.paths = paths;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void addStation(Station station) {
        this.stations.add(station);
    }

    public List<Path> getPaths() {
        return paths;
    }
}
