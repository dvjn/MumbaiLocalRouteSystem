package tool.result;

import java.util.ArrayList;
import java.util.List;

import tool.map.Station;

public class Route {

    public Station source;
    public Station destination;
    public final List<Station> path = new ArrayList<Station>();
    public double time = 0.0;
    public char line;

    public Route(Station start, char line) {
        path.add(start);
        this.source = start;
        this.destination = start;
        this.line = line;
    }

  public Route addStation(Station station, double time) {
    if (this.path.size() < 2)
      this.time += time;
    else
      this.time += time + 0.3;

    path.add(station);
    this.destination = station;
    return this;
  }

public void printRoute() {
    System.out.println("\nRoute:");
    System.out.println("\tFrom: " + this.source.name);
    System.out.println("\tTo: " + this.destination.name);
    System.out.println("\tTime: " + this.time);
    System.out.println("\tLine: " + this.line);
    System.out.println();
