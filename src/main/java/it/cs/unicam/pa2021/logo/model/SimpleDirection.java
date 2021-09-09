package it.cs.unicam.pa2021.logo.model;

/**
 * Semplice implementazione di una direzione dell' interfaccia it.cs.unicam.pa2021.logo.model.Directional.
 * Questa classe viene parametrizzata con la classe wrapper Integer, in modo tale da avere una direzione di
 * interi compresi nel range [0,360].
 * In particolare gli interi rappresentano i gradi dell' angolo, cioè la direzione.
 */
public class SimpleDirection implements Directional<Integer> {

    private int angle;

    /**
     * Crea una direzione di default con angolo di zero gradi.
     */
    public SimpleDirection() {
        this(0);
    }

    /**
     * Crea una direzione con l' angolo specificato.
     * L' angolo deve essere compreso nel range 0 - 360 gradi.
     *
     * @param angle l' angolo specificato.
     * @throws IllegalArgumentException se l' angolo da impostare &egrave; out of range.
     */
    public SimpleDirection(int angle) {
        checkAngle(angle);
        this.angle = angle;
    }

    private void checkAngle(int angle) {
        if (angle < 0 || angle > 360)
            throw new IllegalArgumentException("Direction out of range!");
    }

    @Override
    public String toString() {
        return "" + this.angle;
    }

    @Override
    public Integer getDirectionWay() {
        return this.angle;
    }

    /**
     * Imposta l' angolo con quello specificato.
     * L' angolo deve essere compreso nel range 0 - 360 gradi.
     *
     * @param direction l' angolo da impostare alla direzione.
     * @throws IllegalArgumentException se l' angolo da impostare &egrave; out of range.
     */
    @Override
    public void setDirectionWay(Integer direction) {
        checkAngle(direction);
        this.angle = direction;
    }
}
