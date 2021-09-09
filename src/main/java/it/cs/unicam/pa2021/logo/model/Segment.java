package it.cs.unicam.pa2021.logo.model;

import java.util.Objects;

/**
 * Semplice implementazione di un segmento, ovvero di una parte di retta compresa tra due suoi punti.
 *
 * @param <C> il tipo parametrico per le coordinate del punto.
 */
public class Segment<C> implements Line<C> {

    private final C startingPoint;
    private final C endPoint;
    private final RGBColor color;
    private final int size;

    /**
     * Crea una linea.
     *
     * @param startingPoint l' estremo iniziale.
     * @param endPoint      l' estremo finale.
     */
    public Segment(C startingPoint, C endPoint, RGBColor color, int size) {
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.color = color;
        this.size = size;
    }

    @Override
    public C getStartingPoint() {
        return this.startingPoint;
    }

    @Override
    public C getEndPoint() {
        return this.endPoint;
    }

    @Override
    public RGBColor getColor() {
        return color;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        return "L{ " + this.startingPoint + " , " + this.endPoint + " color = " + this.color + " } ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment<?> segment = (Segment<?>) o;
        return size == segment.size
                && (Objects.equals(startingPoint, segment.startingPoint)
                && Objects.equals(endPoint, segment.endPoint)
                || (Objects.equals(startingPoint, segment.endPoint)
                && Objects.equals(endPoint, segment.startingPoint))
                && Objects.equals(color, segment.color));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingPoint, endPoint, color, size);
    }
}
