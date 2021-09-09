package it.cs.unicam.pa2021.logo.model;


import java.util.List;

/**
 * Rappresenta una generica area chiusa.
 *
 * @param <L> il tipo parametrico per le linee che individuano l' area chiusa.
 */
public interface ClosedArea<L> {

    /**
     * Restituisce una lista di linee che individuano l' area chiusa.
     *
     * @return una lista di linee che individuano l' area chiusa.
     */
    List<L> getArea();

    /**
     * Restituisce il colore RGB dell' area chiusa.
     *
     * @return il colore RGB dell' area chiusa.
     */
    RGBColor getColor();


    /**
     * Restituisce la rappresentazione dell' area sotto forma di stringa.
     *
     * @return rappresentazione dell' area sotto forma di stringa.
     */
    @Override
    String toString();

    /**
     * Compara l' oggetto specificato con questa area chiusa per verificarne l' uguaglianza.
     *
     * @param o l' oggetto da comparare con questa area chiusa per l' uguaglianza.
     * @return true se l' oggetto specificato &egrave; uguale a questa area chiusa, false altrimenti.
     */
    @Override
    boolean equals(Object o);

    /**
     * Restituisce il valore hash code per questa area chiusa.
     *
     * @return il valore hash code per questa area chiusa.
     */
    @Override
    int hashCode();

}
