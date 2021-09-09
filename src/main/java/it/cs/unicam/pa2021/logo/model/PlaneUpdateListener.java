package it.cs.unicam.pa2021.logo.model;

/**
 * Descrive la gestione dell' iterazione tra la vista e il modello.
 *
 * @param <C> il tipo parametrico per le coordinate del punto nel piano.
 */
public interface PlaneUpdateListener<C> {

    /**
     * Notifica che il cursore si &egrave; spostato nel punto specificato.
     *
     * @param point il punto in cui il cursore si &egrave; spostato.
     */
    void fireMovedCursor(C point);

    /**
     * Notifica che &egrave; stata generata la linea specificata.
     *
     * @param line la linea generata.
     */
    void fireGeneratedLine(Line<C> line);

    /**
     * Notifica che &egrave; stata generata un' area chiusa.
     *
     * @param area l' area chiusa generata.
     */
    void fireGeneratedArea(ClosedArea<Line<C>> area);

    /**
     * Notifica che &egrave; stato cambiato il colore al piano.
     *
     * @param color il nuovo colore del piano.
     */
    void fireScreenColor(RGBColor color);

    /**
     * Notifica che &eagrve; stato cancellato tutto ci&ograve; che &egrave; stato
     * disegnato sullo schermo.
     */
    void fireScreenCleaned();
}
