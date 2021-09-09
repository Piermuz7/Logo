package it.cs.unicam.pa2021.logo.model;

/**
 * Rappresenta un generico punto nel piano.
 *
 * @param <N> il tipo parametrico per le coordinate numeriche del punto.
 */
public interface Point<N extends Number> {

    /**
     * Restituisce l' ascissa del punto.
     *
     * @return l' ascissa del punto.
     */
    N getX();

    /**
     * Restituisce l' ordinata del punto.
     *
     * @return l' ordinata del punto.
     */
    N getY();

    /**
     * Restituisce la rappresentazione del punto sotto forma di stringa.
     *
     * @return rappresentazione del punto sotto forma di stringa.
     */
    @Override
    String toString();

    /**
     * Verifica l' uguaglianza tra due punti.
     *
     * @param o l' oggetto da verificarne l'uguaglianza.
     * @return true se l' oggetto &egrave; uguale all' oggetto specificato, false altrimenti.
     */
    @Override
    boolean equals(Object o);

    /**
     * Calcola l'hashcode dell' oggetto.
     *
     * @return l'hashcode calcolato.
     */
    @Override
    int hashCode();

    /**
     * Metodo statico che crea un punto cartesiano in base alle coordinate specificate.
     *
     * @param x   l' ascissa del punto da creare.
     * @param y   l' ordinate del punto da creare.
     * @param <N> il tipo parametrico per le coordinate del punto.
     * @return un punto cartesiano con le coordinate specificate.
     */
    static <N extends Number> CartesianPoint cartesianPoint(N x, N y) {
        return new CartesianPoint(x.doubleValue(), y.doubleValue());
    }

}
