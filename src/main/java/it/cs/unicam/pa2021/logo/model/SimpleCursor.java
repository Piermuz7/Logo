package it.cs.unicam.pa2021.logo.model;

import java.util.Objects;

/**
 * Semplice implementazione di un cursore.
 * Questa classe viene parametrizzata attraverso l' interfaccia it.cs.unicam.pa2021.logo.model.Point,
 * quest' ultima parametrizzata con Double. Inoltre ha come secondo parametro la classe SimpleDirection.
 * Tali parametrizzazioni permettono di utilizzare un cursore avente come coordinate una coppia di punti
 * in virgola mobile, e come propria direzione, una direzione espressa in gradi nel range [0,360].
 */
public class SimpleCursor implements Cursor<Point<Double>, SimpleDirection> {

    private final Plane<Point<Double>> plane;
    private Point<Double> position;
    private SimpleDirection direction;
    private RGBColor lineColor;
    private RGBColor areaColor;
    private boolean plot;
    private boolean pen;
    private int penSize;

    /**
     * Crea un cursore nel piano specificato, con le caratteristiche di default dell' ambiente LOGO.
     *
     * @param plane il piano in cui si muover&agrave; il cursore.
     * @throws NullPointerException se il piano specificato &egrave; null.
     */
    public SimpleCursor(Plane<Point<Double>> plane) {
        this(plane, plane.getHome(), Directional.defaultSimpleDirection(), new RGBColor(0, 0, 0), new RGBColor(255, 255, 255));
    }

    /**
     * Crea un cursore nel piano specificato, con le caratteristiche specificate.
     *
     * @param plane     il piano in cui si muover&agrave; il cursore.
     * @param position  dove sar&agrave; posizionato all' inizio il cursore nel piano.
     * @param direction in quale direzione punter&agrave; all' inizio il cursore nel piano.
     * @param lineColor il colore della linea.
     * @param areaColor il colore di riempimento dell' area.
     * @throws NullPointerException     se almeno uno dei parametri specificati &egrave; null.
     * @throws IllegalArgumentException se la posizione del cursore non &egrave; valida all' interno del piano specificato.
     */
    public SimpleCursor(Plane<Point<Double>> plane, Point<Double> position, SimpleDirection direction, RGBColor lineColor, RGBColor areaColor) {
        if (plane == null)
            throw new NullPointerException("Null plane specified!");
        if (position == null)
            throw new NullPointerException("Null position specified!");
        if (direction == null)
            throw new NullPointerException("Null direction specified!");
        if (lineColor == null)
            throw new NullPointerException("Null line color specified!");
        if (areaColor == null)
            throw new NullPointerException("Null fill area color specified!");
        this.plane = plane;
        if (!this.plane.bePartOfPlane(position))
            throw new IllegalArgumentException("Invalid cursor position in this plane!");
        this.position = position;
        this.direction = direction;
        this.lineColor = lineColor;
        this.areaColor = areaColor;
        this.plot = false;
        this.pen = true;
        this.penSize = 1;
    }

    /**
     * Crea un cursore, nel piano specificato, con le stesse caratteristiche del cursore specificato.
     *
     * @param plane  il piano che dovr&agrave; contenere il cursore specificato.
     * @param cursor il cursore da creare con le stesse caratteristiche del cursore specificato.
     */
    public SimpleCursor(Plane<Point<Double>> plane, Cursor<Point<Double>, SimpleDirection> cursor) {
        this.plane = plane;
        this.position = Point.cartesianPoint(cursor.getPosition().getX(), cursor.getPosition().getY());
        this.direction = Directional.simpleDirection(cursor.getDirection().getDirectionWay());
        this.lineColor = new RGBColor(cursor.getLineColor().getRed(), cursor.getLineColor().getGreen(), cursor.getLineColor().getBlue());
        this.areaColor = new RGBColor(cursor.getAreaColor().getRed(), cursor.getAreaColor().getGreen(), cursor.getAreaColor().getBlue());
        this.plot = cursor.isPlot();
        this.pen = cursor.isPen();
        this.penSize = cursor.getPenSize();
    }

    @Override
    public Point<Double> getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point<Double> position) {
        this.position = position;
    }

    @Override
    public SimpleDirection getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(SimpleDirection direction) {
        this.direction = direction;
    }

    @Override
    public RGBColor getLineColor() {
        return this.lineColor;
    }

    @Override
    public void setLineColor(RGBColor color) {
        this.lineColor = color;
    }

    @Override
    public RGBColor getAreaColor() {
        return this.areaColor;
    }

    @Override
    public void setAreaColor(RGBColor color) {
        this.areaColor = color;
    }

    @Override
    public boolean isPlot() {
        return this.plot;
    }

    @Override
    public Plane<Point<Double>> getPlane() {
        return this.plane;
    }

    @Override
    public boolean isPen() {
        return this.pen;
    }

    @Override
    public void setPlot(boolean plot) {
        this.plot = plot;
    }

    @Override
    public void penUp() {
        this.pen = false;
    }

    @Override
    public void penDown() {
        this.pen = true;
    }

    @Override
    public int getPenSize() {
        return this.penSize;
    }

    @Override
    public void setPenSize(int size) {
        if (size < 1)
            throw new IllegalArgumentException("Invalid pen size!");
        this.penSize = size;
    }

    @Override
    public String toString() {
        return "SimpleCursor{" +
                "\nposition=" + position +
                "\ndirection=" + direction +
                "\nlineColor=" + lineColor +
                "\nareaColor=" + areaColor +
                "\nplot=" + plot +
                "\npen=" + pen +
                "\npenSize=" + penSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCursor that = (SimpleCursor) o;
        return plot == that.plot && pen == that.pen && penSize == that.penSize && Objects.equals(plane, that.plane) && Objects.equals(position, that.position) && Objects.equals(direction, that.direction) && Objects.equals(lineColor, that.lineColor) && Objects.equals(areaColor, that.areaColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plane, position, direction, lineColor, areaColor, plot, pen, penSize);
    }
}
