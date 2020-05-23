package tool.map;

import tool.map.Station;

public class Path {
    private static int count = 0;
    private final int id;
    private final Station source;
    private final Station destination;
    private final int weight;

    public Path(Station source, Station destination, int weight) {
        this.id = count;
        count++;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public Station getDestination() {
        return destination;
    }

    public Station getSource() {
        return source;
    }

    public int getWeight() {
        return weight;
    }
}
