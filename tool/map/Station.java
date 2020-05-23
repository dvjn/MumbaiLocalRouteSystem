package tool.map;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Station {

    public static int count = 0;
    final public int id;
    final public String name;
    public HashMap<Station, Integer> connections = new HashMap<Station, Integer>(2);
    public Character[] lines = new Character[5];

    public Station(String name, Character[] lines) {
        this.id = count;
        count++;
        this.name = name;
        this.lines = lines;
    }

    public void addConnection(int time, Station station) {
        connections.put(station, time);
    }

    public void print() {
        System.out.println(this.id + ". " + this.name);
        System.out.print("\tConnected To: ");
        Iterator<Map.Entry<Station, Integer>> it = connections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Station, Integer> pair = (Map.Entry<Station, Integer>) it.next();
            System.out.print("\"" + ((Station) pair.getKey()).name + "\" ");
            it.remove();
        }
        System.out.println();
    }

    public boolean equals(Station station) {
        return this.id == station.id;
    }

    public String toString() {
        return this.name;
    }
}
