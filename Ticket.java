import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Iterator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import tool.map.Station;
import tool.map.Path;
import tool.map.Graph;
import tool.result.Route;
import tool.result.Journey;
import tool.engine.DijkstraAlgorithm;

class ComboItem {
    private Character key;
    private String value;

    public ComboItem(Character key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public Character getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

class MainMenu extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainMenu(List<Station> stations) {
        super("Mumbai Local Route System");
        setLayout(null);

        Vector<ComboItem> linesModel = new Vector<ComboItem>();
        linesModel.add(new ComboItem('w', "Western"));
        linesModel.add(new ComboItem('c', "Central"));
        linesModel.add(new ComboItem('h', "Harbour"));
        linesModel.add(new ComboItem('t', "Trans-Harbour"));
        linesModel.add(new ComboItem('a', "CST-Andheri"));

        JLabel srcLineLabel = new JLabel("Source Line:", SwingConstants.CENTER);
        JLabel destLineLabel = new JLabel("Destination Line:", SwingConstants.CENTER);
        JLabel srcLabel = new JLabel("Source Station:", SwingConstants.CENTER);
        JLabel destLabel = new JLabel("Destination Station:", SwingConstants.CENTER);
        JComboBox<ComboItem> srcLineCB = new JComboBox<ComboItem>(linesModel);
        JComboBox<ComboItem> destLineCB = new JComboBox<ComboItem>(linesModel);
        JComboBox<Station> srcStationCB = new JComboBox<Station>();
        JComboBox<Station> destStationCB = new JComboBox<Station>();
        JButton findBtn = new JButton("Get Route");

        srcLineLabel.setBounds(50, 15, 180, 20);
        destLineLabel.setBounds(270, 15, 180, 20);
        srcLineCB.setBounds(50, 40, 180, 20);
        destLineCB.setBounds(270, 40, 180, 20);

        srcLabel.setBounds(50, 80, 180, 20);
        destLabel.setBounds(270, 80, 180, 20);
        srcStationCB.setBounds(50, 105, 180, 20);
        destStationCB.setBounds(270, 105, 180, 20);

        findBtn.setBounds(175, 180, 150, 30);

        add(srcLineLabel);
        add(srcLineCB);
        add(srcLabel);
        add(srcStationCB);

        add(destLineLabel);
        add(destLineCB);
        add(destLabel);
        add(destStationCB);

        add(findBtn);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(500, 270);

        srcLineCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboItem lineItem = (ComboItem) srcLineCB.getSelectedItem();
                Character line = lineItem.getKey();
                srcStationCB.removeAllItems();
                for (Station station : stations) {
                    for (Character l : station.lines) {
                        if (l == line) {
                            srcStationCB.addItem(station);
                            break;
                        }
                    }
                }
            }
        });

        destLineCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboItem lineItem = (ComboItem) destLineCB.getSelectedItem();
                Character line = lineItem.getKey();
                destStationCB.removeAllItems();
                for (Station station : stations) {
                    for (Character l : station.lines) {
                        if (l == line) {
                            destStationCB.addItem(station);
                            break;
                        }
                    }
                }
            }
        });

        srcLineCB.setSelectedIndex(0);
        destLineCB.setSelectedIndex(0);

        findBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Station source = (Station) srcStationCB.getSelectedItem();
                Station destination = (Station) destStationCB.getSelectedItem();

                if (source.equals(destination)) {
                    JOptionPane.showMessageDialog(null, "Source and destination are same!");
                } else {
                    Journey j = Ticket.findJourney(source, destination);
                    new DisplayJourney(j);
                }
            }

        });
    }
}

class DisplayJourney extends JFrame {
    private static final long serialVersionUID = 1L;

    DisplayJourney(Journey journey) {
        super("Journey");

        int baseh = 150;
        int hperroute = 150;

        List<JPanel> routePanels = new ArrayList<JPanel>();
        List<JLabel> routeSources = new ArrayList<JLabel>();
        List<JLabel> routeDestinations = new ArrayList<JLabel>();
        List<JLabel> routeTimes = new ArrayList<JLabel>();
        List<JLabel> routeLines = new ArrayList<JLabel>();
        List<JLabel> routeChanges = new ArrayList<JLabel>();

        setLayout(null);

        setSize(400, baseh + (hperroute * journey.routes.size()));
        Dictionary<Character, String> lines = new Hashtable<Character, String>();
        lines.put('w', "Western");
        lines.put('c', "Central");
        lines.put('h', "Harbour");
        lines.put('t', "Trans-Harbour");
        lines.put('a', "CST-Andheri");

        JLabel srcLabel = new JLabel("Source Station: " + journey.source.name, SwingConstants.LEFT);
        JLabel destLabel = new JLabel("Destination Station: " + journey.destination.name, SwingConstants.LEFT);

        JLabel timeLabel = new JLabel("Time Taken: " + (int) journey.time + " minutes", SwingConstants.LEFT);

        JLabel trainsLabel = new JLabel("Route", SwingConstants.CENTER);
        trainsLabel.setFont(new Font("Helvetica", Font.BOLD, 16));

        srcLabel.setBounds(30, 20, 340, 20);
        destLabel.setBounds(30, 40, 340, 20);
        timeLabel.setBounds(70, 70, 300, 20);
        trainsLabel.setBounds(20, 110, 360, 20);

        add(srcLabel);
        add(destLabel);

        add(timeLabel);
        add(trainsLabel);

        int i = 0;
        for (Route route : journey.routes) {
            routePanels.add(new JPanel());
            routePanels.get(i).setLayout(null);
            routeSources.add(new JLabel("From: " + route.source));
            routeSources.get(i).setBounds(50, 10, 300, 20);
            routeDestinations.add(new JLabel("To: " + route.destination));
            routeDestinations.get(i).setBounds(50, 30, 300, 20);
            routeTimes.add(new JLabel("Time: " + (int) route.time + " minutes"));
            routeTimes.get(i).setBounds(70, 60, 260, 20);
            routeLines.add(new JLabel("Line: " + lines.get(route.line)));
            routeLines.get(i).setBounds(70, 80, 260, 20);

            routePanels.get(i).add(routeSources.get(i));
            routePanels.get(i).add(routeDestinations.get(i));
            routePanels.get(i).add(routeTimes.get(i));
            routePanels.get(i).add(routeLines.get(i));

            if (i != journey.routes.size() - 1) {
                routeChanges.add(new JLabel("5 minutes for changing trains.", SwingConstants.CENTER));
                routeChanges.get(i).setBounds(90, baseh + hperroute * i + 110, 220, 20);
                add(routeChanges.get(i));
            }

            routePanels.get(i).setBounds(30, baseh + hperroute * i - 10, 340, hperroute - 40);
            routePanels.get(i).setBackground(Color.WHITE);

            add(routePanels.get(i));
            i++;
        }

        setVisible(true);
        setResizable(false);
    }
}

class Ticket {
    static List<Station> stations = new ArrayList<Station>();
    static List<Path> paths = new ArrayList<Path>();

    public static void main(String[] args) {
        loadStations();
        loadPaths();

        new MainMenu(stations);
    }

    public static Journey findJourney(Station source, Station destination) {
        Graph graph = new Graph(stations, paths);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

        dijkstra.execute(source);

        LinkedList<Station> path = dijkstra.getPath(destination);

        Journey j = new Journey();

        if (path == null) {
            // Display could not find path error
        } else {
            int i = 0;
            Character currLines[] = new Character[5];
            Character lastLines[] = new Character[5];
            Station lastStation = null;
            int time = 0;

            for (Station station : path) {
                if (i == 0) {
                    lastLines = station.lines;
                    j.addRoute(new Route(station, ' '));
                    lastStation = station;
                    i++;
                    continue;
                }

                Set<Character> s1 = new HashSet<Character>(Arrays.asList(lastLines));
                Set<Character> s2 = new HashSet<Character>(Arrays.asList(station.lines));
                s1.retainAll(s2);
                currLines = s1.toArray(new Character[s1.size()]);

                if (currLines.length == 0) {
                    // j.lastRoute().time -= 0.3;
                    j.lastRoute().line = lastLines[0];

                    j.addRoute(new Route(lastStation, ' '));
                    currLines = station.lines;
                }

                Iterator<Map.Entry<Station, Integer>> it = lastStation.connections.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Station, Integer> pair = (Map.Entry<Station, Integer>) it.next();
                    if (((Station) pair.getKey()) == station) {
                        time = (int) pair.getValue();
                        break;
                    }
                    it.remove();
                }

                j.lastRoute().addStation(station, ((double) time));
                time = 0;

                lastLines = currLines;

                if (i == path.size() - 1) {
                    // j.lastRoute().time -= 0.3;
                    if (lastLines.length != 0)
                        j.lastRoute().line = lastLines[0];
                }

                lastStation = station;
                i++;
            }

            j.reCalculateParameters();

            System.out.println("Journey Time:" + j.time);
            System.out.println("Journey Source:" + j.source.name);
            System.out.println("Journey Destination:" + j.destination.name);
            for (int a = 0; a < j.routes.size(); a++)
                j.routes.get(a).printRoute();
        }

        return j;
    }

    public static void loadStations() {
        Character[] line = { 'h', 'c', 'a' };
        Character[] line2 = { 'h', 'c' };
        Character[] line3 = { 'h' };
        Character[] line4 = { 'c' };
        Character[] line5 = { 'w', 'c' };
        Character[] line6 = { 't', 'h' };
        Character[] line12 = { 't', 'c' };
        Character[] line7 = { 'h', 'a' };
        Character[] line11 = { 'w', 'a' };
        Character[] line8 = { 'a' };
        Character[] line9 = { 'w' };
        Character[] line10 = { 't' };

        addStation("Chatrapati Shivaji Maharaj Terminus", line);
        addStation("Masjid", line);
        addStation("Sandhurst Road", line);
        addStation("Byculla", line4);
        addStation("Chinchpokali", line4);
        addStation("Currey Rd.", line4);
        addStation("Parel", line4);
        addStation("Dadar", line5);
        addStation("Matunga", line4);
        addStation("Sion", line4);
        addStation("Kurla", line2);
        addStation("Vidya Vihar", line4);
        addStation("Ghatkopar", line4);
        addStation("Vikroli", line4);
        addStation("Kanjurmarg", line4);
        addStation("Bhandup", line4);
        addStation("Nahur", line4);
        addStation("Mulund", line4);
        addStation("Thane", line12);
        addStation("Kalwa", line4);
        addStation("Mumbra", line4);
        addStation("Diva", line4);
        addStation("Kopar", line4);
        addStation("Dombivali", line4);
        addStation("Thakurli", line4);
        addStation("Kalyan", line4);
        addStation("Shahad", line4);
        addStation("Ambivli", line4);
        addStation("Titwala", line4);
        addStation("Khadavli", line4);
        addStation("Vashind", line4);
        addStation("Asangaon", line4);
        addStation("Atgaon", line4);
        addStation("Khardi", line4);
        addStation("Kasara", line4);
        addStation("Dockyard Road", line7);
        addStation("Reay Road", line7);
        addStation("Cotton Green", line7);
        addStation("Sewri", line7);
        addStation("Vadala", line7);
        addStation("GTB Nagar", line3);
        addStation("Chuna Bhatti", line3);
        addStation("Tilak Nagar", line3);
        addStation("Chembur", line3);
        addStation("Govandi", line3);
        addStation("Mankhurd", line3);
        addStation("Vashi", line6);
        addStation("Sanpada", line6);
        addStation("Jui Nagar", line6);
        addStation("Nerul", line6);
        addStation("Seawood Darave", line6);
        addStation("Belapur", line6);
        addStation("Kharghar", line6);
        addStation("Mansarovar", line6);
        addStation("Khandeshwar", line6);
        addStation("Panvel", line6);
        addStation("King's Circle", line8);
        addStation("Mahim", line11);
        addStation("Bandra", line11);
        addStation("Khar Road", line11);
        addStation("Santacruz", line11);
        addStation("Vile Parle", line11);
        addStation("Andheri", line11);
        addStation("Churchgate", line9);
        addStation("Marine Lines", line9);
        addStation("Charni Road", line9);
        addStation("Grant Road", line9);
        addStation("Mumbai Central", line9);
        addStation("Mahalaxmi", line9);
        addStation("Lower Parel", line9);
        addStation("PrabhaDevi", line9);
        addStation("Matunga Road", line9);
        addStation("Jogeshwari", line9);
        addStation("Goregaon", line9);
        addStation("Malad", line9);
        addStation("Kandivali", line9);
        addStation("Borivali", line9);
        addStation("Dahisar", line9);
        addStation("Mira Road", line9);
        addStation("Bhayandar", line9);
        addStation("Naigaon", line9);
        addStation("Vasai Road", line9);
        addStation("Nallasopara", line9);
        addStation("Virar", line9);
        addStation("Vaitarna", line9);
        addStation("Saphale", line9);
        addStation("Kelve Road", line9);
        addStation("Palghar", line9);
        addStation("Umroli", line9);
        addStation("Boisar", line9);
        addStation("Vangaon", line9);
        addStation("Dahanu Road", line9);
        addStation("Airoli", line10);
        addStation("Rabale", line10);
        addStation("Ghansoli", line10);
        addStation("Kopar Khairane", line10);
        addStation("Turbhe", line10);
        addStation("Ram Mandir", line9);
    }

    public static void loadPaths() {
        addPath(0, 1, 3);
        addPath(1, 2, 2);
        addPath(2, 3, 3);
        addPath(3, 4, 2);
        addPath(4, 5, 2);
        addPath(5, 6, 2);
        addPath(6, 7, 3);
        addPath(7, 8, 3);
        addPath(8, 9, 3);
        addPath(9, 10, 4);
        addPath(10, 11, 4);
        addPath(11, 12, 3);
        addPath(12, 13, 3);
        addPath(13, 14, 5);
        addPath(14, 15, 3);
        addPath(15, 16, 3);
        addPath(16, 17, 3);
        addPath(17, 18, 3);
        addPath(18, 19, 4);
        addPath(19, 20, 6);
        addPath(20, 21, 4);
        addPath(21, 22, 5);
        addPath(22, 23, 4);
        addPath(23, 24, 3);
        addPath(24, 25, 6);
        addPath(25, 26, 5);
        addPath(26, 27, 3);
        addPath(27, 28, 5);
        addPath(28, 29, 8);
        addPath(29, 30, 8);
        addPath(30, 31, 6);
        addPath(31, 32, 9);
        addPath(32, 33, 4);
        addPath(33, 34, 17);
        addPath(2, 35, 2);
        addPath(35, 36, 2);
        addPath(36, 37, 2);
        addPath(37, 38, 3);
        addPath(38, 39, 3);
        addPath(39, 40, 4);
        addPath(40, 41, 3);
        addPath(41, 10, 3);
        addPath(10, 42, 3);
        addPath(42, 43, 2);
        addPath(43, 44, 3);
        addPath(44, 45, 3);
        addPath(45, 46, 8);
        addPath(46, 47, 3);
        addPath(47, 48, 3);
        addPath(48, 49, 3);
        addPath(49, 50, 3);
        addPath(50, 51, 4);
        addPath(51, 52, 4);
        addPath(52, 53, 3);
        addPath(53, 54, 3);
        addPath(54, 55, 3);
        addPath(39, 56, 5);
        addPath(56, 57, 2);
        addPath(57, 58, 4);
        addPath(58, 59, 3);
        addPath(59, 60, 2);
        addPath(60, 61, 3);
        addPath(61, 62, 5);
        addPath(63, 64, 3);
        addPath(64, 65, 2);
        addPath(65, 66, 3);
        addPath(66, 67, 2);
        addPath(67, 68, 3);
        addPath(68, 69, 3);
        addPath(69, 70, 3);
        addPath(70, 7, 2);
        addPath(7, 71, 2);
        addPath(71, 57, 3);
        addPath(62, 72, 3);
        addPath(72, 97, 1);
        addPath(97, 73, 2);
        addPath(73, 74, 3);
        addPath(74, 75, 3);
        addPath(75, 76, 3);
        addPath(76, 77, 6);
        addPath(77, 78, 5);
        addPath(78, 79, 5);
        addPath(79, 80, 5);
        addPath(80, 81, 6);
        addPath(81, 82, 5);
        addPath(82, 83, 5);
        addPath(83, 84, 6);
        addPath(84, 85, 9);
        addPath(85, 86, 8);
        addPath(86, 87, 8);
        addPath(87, 88, 15);
        addPath(88, 89, 6);
        addPath(89, 90, 9);
        addPath(90, 91, 13);
        addPath(18, 92, 8);
        addPath(92, 93, 3);
        addPath(93, 94, 3);
        addPath(94, 95, 3);
        addPath(95, 96, 4);
        addPath(96, 48, 5);
        addPath(96, 47, 3);
    }

    public static void addStation(String name, Character[] line) {
        stations.add(new Station(name, line));
    }

    public static void addPath(int s, int d, int w) {
        stations.get(s).addConnection(w, stations.get(d));
        stations.get(d).addConnection(w, stations.get(s));
        paths.add(new Path(stations.get(s), stations.get(d), w));
        paths.add(new Path(stations.get(d), stations.get(s), w));
    }

}

