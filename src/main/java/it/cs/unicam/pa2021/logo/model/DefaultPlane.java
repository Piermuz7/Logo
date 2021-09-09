package it.cs.unicam.pa2021.logo.model;


import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class DefaultPlane implements Plane<Point<Double>> {

    private final double length;
    private final double height;
    private final Point<Double> home;
    private final Point<Double> origin;
    private final Cursor<Point<Double>, SimpleDirection> cursor;
    private final Queue<Line<Point<Double>>> lines;
    private final Queue<ClosedArea<Line<Point<Double>>>> closedAreas;
    private RGBColor backgroundColor;
    private final Graph<Point<Double>> graph;
    private final Map<Point<Double>, Integer> points;
    private PlaneUpdateSupport<Point<Double>> planeUpdateSupport;

    /**
     * Crea un piano geometrico con le caratteristiche di default dell' ambiente LOGO.
     *
     * @param length la base del piano.
     * @param height l' altezza del piano.
     */
    public DefaultPlane(double length, double height) {
        this(length, height, Point.cartesianPoint(length / 2, height / 2), Point.cartesianPoint(0, 0));
    }

    /**
     * Crea un piano geometrico in base a tutti i parametri passati.
     *
     * @param length la base del piano.
     * @param height l' altezza del piano.
     * @param home   la posizione centrale del piano.
     * @param origin l' origine del piano.
     * @throws NullPointerException     se la posizione home o la posizione origine &egrave; null
     * @throws IllegalArgumentException se la base del piano o l' altezza sono minori di 2 oppure
     *                                  se la posizione home e la posizione origine sono punti non appartenenti al piano.
     */
    public DefaultPlane(double length, double height, Point<Double> home, Point<Double> origin) {
        if (length < 2)
            throw new IllegalArgumentException("Invalid plane's length!");
        if (height < 2)
            throw new IllegalArgumentException("Invalid plane's height!");
        if (home == null)
            throw new NullPointerException("Null Home position!");
        if (origin == null)
            throw new NullPointerException("Null Origin position!");
        this.length = length;
        this.height = height;
        this.lines = new ArrayDeque<>();
        this.closedAreas = new ArrayDeque<>();
        if (!bePartOfPlane(home))
            throw new IllegalArgumentException("Nonexistent Home position in this plane!");
        if (!bePartOfPlane(origin))
            throw new IllegalArgumentException("Nonexistent Origin position in this plane!");
        this.home = home;
        this.origin = origin;
        this.cursor = new SimpleCursor(this);
        this.backgroundColor = new RGBColor(255, 255, 255);
        this.graph = new UndirectedGraph<>();
        this.points = new HashMap<>();
        this.planeUpdateSupport = new PlaneUpdateSupport<>();
    }

    /**
     * Crea un piano identico a quello specificato.
     *
     * @param plane il piano con le caratteristiche da creare.
     */
    public DefaultPlane(Plane<Point<Double>> plane) {
        this.length = plane.getLength();
        this.height = plane.getHeight();
        this.lines = new ArrayDeque<>();
        this.lines.addAll(plane.getLines());
        this.closedAreas = new ArrayDeque<>();
        this.closedAreas.addAll(plane.getClosedAreas());
        this.home = plane.getHome();
        this.origin = plane.getOrigin();
        this.cursor = new SimpleCursor(this, plane.getCursor());
        this.backgroundColor = new RGBColor(plane.getBackgroundColor().getRed(), plane.getBackgroundColor().getGreen(), plane.getBackgroundColor().getBlue());
        this.graph = new UndirectedGraph<>(plane.getGraph());
        this.points = new HashMap<>();
        this.points.putAll(plane.getPoints());
        this.planeUpdateSupport = plane.getPlaneUpdateSupport();
    }

    @Override
    public double getLength() {
        return this.length;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Point<Double> getOrigin() {
        return this.origin;
    }

    @Override
    public Point<Double> getHome() {
        return this.home;
    }

    @Override
    public Queue<Line<Point<Double>>> getLines() {
        return this.lines;
    }

    @Override
    public int getNumLines() {
        return this.lines.size();
    }

    @Override
    public RGBColor getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    public void setBackgroundColor(RGBColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void addLine(Line<Point<Double>> line) {
        if (line.getStartingPoint().equals(line.getEndPoint()))
            return;
        this.lines.offer(line);
        this.setIndexPoints(line);
        for (Line<Point<Double>> l : this.lines)
            if (!this.lineInClosedAreasisPresent(l))
                graph.addEdge(new GraphNode<>(this.points.get(l.getStartingPoint()), l.getStartingPoint()), new GraphNode<>(this.points.get(l.getEndPoint()), l.getEndPoint()));
        Optional<List<Point<Double>>> area = checkClosedArea(this.graph);
        List<Line<Point<Double>>> l;
        if (area.isPresent()) {
            l = new ArrayList<>();
            for (int i = 0; i < area.get().size(); i++) {
                if (i == area.get().size() - 1)
                    l.add(this.lineExistsBetween(area.get().get(0), area.get().get(i)).get());
                else
                    l.add(this.lineExistsBetween(area.get().get(i), area.get().get(i + 1)).get());
            }
            ClosedArea<Line<Point<Double>>> closedArea = new SimpleArea(l, this.cursor.getAreaColor());
            this.closedAreas.offer(closedArea);
            Logger.getGlobal().info("Generated closed area: " + closedArea);
            this.planeUpdateSupport.fireGeneratedArea(closedArea);
        }
    }

    /**
     * Metodo di comodo che si occupa di restituire un' area chiusa, ovvero un ciclo
     * nel grafo specificato.
     *
     * @param graph il grafo in cui trovare il ciclo.
     * @return un Optional descrivente un' area chiusa.
     */
    private Optional<List<Point<Double>>> checkClosedArea(Graph<Point<Double>> graph) {
        if (this.getNumLines() != 0) {
            Line<Point<Double>> line = null;
            for (Line<Point<Double>> l : this.lines) {
                if (!lineInClosedAreasisPresent(l))
                    line = l;
            }
            if (line != null) {
                List<Point<Double>> cycle = graph.getCycle(new GraphNode<>(this.points.get(line.getStartingPoint()), line.getStartingPoint()), new GraphNode<>(0, null));
                if (!cycle.isEmpty())
                    return Optional.of(cycle);
            }
        }
        return Optional.empty();
    }

    /**
     * Restituisce la linea avente come estremi point1 e point 2 se presente nel piano,
     * Optional.isEmpty() altrimenti.
     *
     * @param point1 l' estremo iniziale della linea.
     * @param point2 l' estremo finale della linea.
     * @return un Optional descrivente la linea da cercare.
     */
    public Optional<Line<Point<Double>>> lineExistsBetween(Point<Double> point1, Point<Double> point2) {
        return this.lines.stream()
                .filter((l) -> (l.getStartingPoint().equals(point1) && l.getEndPoint().equals(point2))
                        || (l.getStartingPoint().equals(point2) && l.getEndPoint().equals(point1)))
                .findAny();
    }

    private boolean lineInClosedAreasisPresent(Line<Point<Double>> line) {
        for (ClosedArea<Line<Point<Double>>> a : this.closedAreas)
            if (a.getArea().contains(line))
                return true;
        return false;
    }

    private void setIndexPoints(Line<Point<Double>> line) {
        if (!this.points.containsKey(line.getStartingPoint()))
            this.points.put(line.getStartingPoint(), points.size() + 1);
        if (!this.points.containsKey(line.getEndPoint()))
            this.points.put(line.getEndPoint(), points.size() + 1);
    }


    @Override
    public Queue<ClosedArea<Line<Point<Double>>>> getClosedAreas() {
        return this.closedAreas;
    }

    @Override
    public int getNumClosedAreas() {
        return this.closedAreas.size();
    }


    @Override
    public boolean bePartOfPlane(Point<Double> point) {
        return (point.getY() >= 0 && point.getY() < this.getHeight()
                && point.getX() >= 0 && point.getX() < this.getLength());
    }

    @Override
    public Point<Double> getDownLeftPoint() {
        return this.origin;
    }

    @Override
    public Point<Double> getDownRightPoint() {
        return Point.cartesianPoint(this.length - 1, 0);
    }

    @Override
    public Point<Double> getUpLeftPoint() {
        return Point.cartesianPoint(0, this.height - 1);
    }

    @Override
    public Point<Double> getUpRightPoint() {
        return Point.cartesianPoint(this.length - 1, this.height - 1);
    }

    @Override
    public Graph<Point<Double>> getGraph() {
        return this.graph;
    }

    @Override
    public PlaneUpdateSupport<Point<Double>> getPlaneUpdateSupport() {
        return this.planeUpdateSupport;
    }

    @Override
    public int getNumPoints() {
        return this.points.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Cursor<Point<Double>, SimpleDirection> getCursor() {
        return this.cursor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Point<Double>, Integer> getPoints() {
        return this.points;
    }


    @Override
    public String toString() {
        StringBuilder lines= new StringBuilder();
        for(Line<Point<Double>> l : this.lines)
            lines.append("\n").append(l);
        StringBuilder areas= new StringBuilder();
        for(ClosedArea<Line<Point<Double>>> a : this.closedAreas)
            areas.append("\n").append(a);
        return "DefaultPlane{" +
                " length=" + length +
                ", height=" + height +
                "\nhome=" + home +
                "\norigin=" + origin +
                "\ncursor=" + cursor +
                "\nlines=" + lines +
                "\nclosedAreas=" + areas +
                "\nbackgroundColor=" + backgroundColor +
                "\n}";
    }

    @Override
    public synchronized void addPlaneUpdateListener(PlaneUpdateListener<Point<Double>> listener) {
        this.planeUpdateSupport.addListener(listener);
    }

    @Override
    public synchronized void removePlaneUpdateListener(PlaneUpdateListener<Point<Double>> listener) {
        this.planeUpdateSupport.removeListener(listener);
    }
}
