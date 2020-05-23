package tool.result;

import java.util.ArrayList;

import tool.map.Station;
import tool.result.Route;

public class Journey {
    public double time = 0.0;
    public final ArrayList<Route> routes = new ArrayList<Route>();
    public Station source = null;
    public Station destination = null;

    public Journey addRoute(Route route) {
        if (this.isEmpty()) {
            this.source = route.path.get(0);
            this.time += route.time;
        } else
            this.time += route.time + 3.0;
        this.destination = route.destination;
        this.routes.add(route);

        return this;
    }

    public Route lastRoute() {
        return this.routes.get(this.routes.size() - 1);
    }

    public boolean isEmpty() {
        if (this.routes.size() == 0.0)
            return true;
        else
            return false;
    }

    public void reCalculateParameters() {
        double time = 0.0;
        for (Route route : this.routes) {
            if (time != 0.0)
                time += 5;
            time += route.time;
        }
        this.time = time;

        this.source = this.routes.get(0).source;
        this.destination = this.lastRoute().destination;
    }
}
