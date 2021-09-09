package it.cs.unicam.pa2021.logo.model;


/**
 * Rappresenta una generica linea.
 *
 * @param <C> il tipo parametrico delle coordinate del punto nel piano.
 */
public interface Line<C> {

    /**
     * Restituisce le coordinate dell' estremo iniziale della linea.
     *
     * @return le coordinate dell' estremo iniziale della linea.
     */
    C getStartingPoint();

    /**
     * Restituisce le coordinate dell' estremo finale della linea.
     *
     * @return le coordinate dell' estremo finale della linea.
     */
    C getEndPoint();

    /**
     * Restituisce il colore associato alla linea.
     *
     * @return il colore della linea.
     */
    RGBColor getColor();

    /**
     * Restituisce la grandezza del tratto della linea.
     *
     * @return la grandezza del tratto della linea.
     */
    int getSize();

    /**
     * Restituisce la rappresentazione della linea sotto forma di stringa.
     *
     * @return rappresentazione della linea sotto forma di stringa.
     */
    @Override
    String toString();

    /**
     * Compara l' oggetto specificato con questa linea per verificarne l' uguaglianza.
     *
     * @param o l' oggetto da comparare con questa linea per l' uguaglianza.
     * @return true se l' oggetto specificato &egrave; uguale a questa linea, false altrimenti.
     */
    @Override
    boolean equals(Object o);

    /**
     * Restituisce il valore hash code per questa linea.
     *
     * @return il valore hash code per questa linea.
     */
    @Override
    int hashCode();

}
