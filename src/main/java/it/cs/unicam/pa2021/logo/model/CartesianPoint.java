package it.cs.unicam.pa2021.logo.model;

import java.util.Objects;

/**
 * Implementazione del punto cartesiano avente una coppia di coordinate in virgola mobile.
 */
public class CartesianPoint implements Point<Double> {

    private final Double x;
    private final Double y;

    /**
     * Crea un punto cartesiano nel piano.
     *
     * @param x l' ascissa da impostare al punto.
     * @param y l' ordinata da impostare al punto.
     */
    CartesianPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Double getX() {
        return this.x;
    }

    @Override
    public Double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "P(" + getX() + "," + getY() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartesianPoint that = (CartesianPoint) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


}
