package it.cs.unicam.pa2021.logo.model;


import java.util.List;
import java.util.Objects;

/**
 * Semplice implementazione di un' area chiusa dell' interfaccia it.cs.unicam.pa2021.logo.model.ClosedArea.
 * Questa classe viene parametrizzata attraverso l' interfaccia it.cs.unicam.pa2021.logo.model.Line, la quale
 * viene parametrizzata attraverso l' interfaccia it.cs.unicam.pa2021.logo.model.Point, quest' ultima
 * parametrizzata con la classe wrapper Double.
 * Queste parametrizzazioni permettono di poter realizzare un' area chiusa di linee, le quali hanno
 * come estremi due coppie di punti con coordinate in virgola mobile.
 */
public class SimpleArea implements ClosedArea<Line<Point<Double>>> {

    private final List<Line<Point<Double>>> lines;
    private final RGBColor color;

    /**
     * Crea un' area chiusa nel piano individuata dalla lista di linee e il colore RGB specificati.
     *
     * @param lines la lista di linee che individuano l' area chiusa.
     * @param color il colore RGB da impostare all' area chiusa.
     */
    public SimpleArea(List<Line<Point<Double>>> lines, RGBColor color) {
        this.lines = lines;
        this.color = color;
    }

    @Override
    public List<Line<Point<Double>>> getArea() {
        return this.lines;
    }

    @Override
    public String toString() {
        StringBuilder lines = new StringBuilder();
        for(Line<Point<Double>> l : this.lines)
            lines.append("\n").append(l);
        return "AREA{ " +
                "lines= " + lines
                +
                "\n, color= " + color +
                " }";
    }

    @Override
    public RGBColor getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleArea that = (SimpleArea) o;
        return Objects.equals(lines, that.lines) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines, color);
    }
}
