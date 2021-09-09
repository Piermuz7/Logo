package it.cs.unicam.pa2021.logo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Permette di gestire i cambiamenti delle propriet&agrave; del piano.
 *
 * @param <C> il tipo parametrico per le coordinate del punto nel piano.
 */
public class PlaneUpdateSupport<C> {

    List<PlaneUpdateListener<C>> listeners;

    /**
     * Crea un gestore dei cambiamenti delle propriet&agrave; del piano.
     */
    public PlaneUpdateSupport() {
        this.listeners = new ArrayList<>();
    }

    /**
     * Aggiunge il listener specificato.
     * Se il listener specificato &egrave; null, non viene aggiunto.
     *
     * @param listener il listener da aggiungere.
     */
    public synchronized void addListener(PlaneUpdateListener<C> listener) {
        if (listener == null) return;
        this.listeners.add(listener);
    }

    /**
     * Rimuove il listener specificato.
     * Se il listener specificato &egrave; null, non viene rimosso nessun listener.
     *
     * @param listener il listener da rimuovere.
     */
    public synchronized void removeListener(PlaneUpdateListener<C> listener) {
        if (listener == null) return;
        this.listeners.remove(listener);
    }

    /**
     * Notifica tutti i listener che il cursore si &egrave; spostato nel punto specificato.
     *
     * @param point il punto in cui il cursore si &egrave; spostato.
     */
    public synchronized void fireMovedCursor(C point) {
        listeners.forEach(p -> p.fireMovedCursor(point));
    }

    /**
     * Notifica tutti i listener che &egrave; stata generata la linea specificata.
     *
     * @param line la linea generata.
     */
    public synchronized void fireGeneratedLine(Line<C> line) {
        listeners.forEach(l -> l.fireGeneratedLine(line));
    }

    /**
     * Notifica tutti i listener che &egrave; stata generata l' area specificata.
     *
     * @param area l' area generata.
     */
    public synchronized void fireGeneratedArea(ClosedArea<Line<C>> area) {
        listeners.forEach(a -> a.fireGeneratedArea(area));
    }

    /**
     * Notifica tutti i listener che &egrave; stato cambiato il colore del piano.
     *
     * @param color il nuovo colore del piano.
     */
    public synchronized void fireScreenColorChanged(RGBColor color) {
        listeners.forEach(s -> s.fireScreenColor(color));
    }

    /**
     * Notifica che &eagrve; stato cancellato tutto ci&ograve; che &egrave; stato
     * disegnato sullo schermo.
     */
    public synchronized void fireScreenCleaned(){
        listeners.forEach(PlaneUpdateListener::fireScreenCleaned);
    }
}
