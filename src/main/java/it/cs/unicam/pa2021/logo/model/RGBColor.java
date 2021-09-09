package it.cs.unicam.pa2021.logo.model;

import java.util.Objects;

/**
 * Classe usata per rappresentare i colori secondo il modello additivo RGB.
 */
public class RGBColor {
    private final int r;
    private final int g;
    private final int b;

    /**
     * Crea un colore RGB con i colori rosso, verde e blu specificati nel range tra 0 - 255.
     *
     * @param r il colore rosso.
     * @param g il colore verde.
     * @param b il colore blu.
     * @throws IllegalArgumentException se almeno uno dei 3 parametri &egrave; out of range.
     */
    public RGBColor(int r, int g, int b) {
        checkRGBRange(r, g, b);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private void checkRGBRange(int r, int g, int b) {
        boolean outOfRange = false;
        String badRGBValues = "";

        if (r < 0 || r > 255) {
            outOfRange = true;
            badRGBValues += "Red";
        }

        if (g < 0 || g > 255) {
            outOfRange = true;
            badRGBValues += "Green";
        }

        if (b < 0 || b > 255) {
            outOfRange = true;
            badRGBValues += "Blue";
        }

        if (outOfRange)
            throw new IllegalArgumentException("Parameters out of range: " + badRGBValues);

    }

    public int getRed() {
        return this.r;
    }

    public int getGreen() {
        return this.g;
    }

    public int getBlue() {
        return this.b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGBColor rgbColor = (RGBColor) o;
        return this.r == rgbColor.r && this.g == rgbColor.g && this.b == rgbColor.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.r, this.g, this.b);
    }

    @Override
    public String toString() {
        return "RGBColor{" +
                "r=" + this.r +
                ", g=" + this.g +
                ", b=" + this.b +
                '}';
    }
}
