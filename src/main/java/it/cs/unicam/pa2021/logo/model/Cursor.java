package it.cs.unicam.pa2021.logo.model;

/**
 * Rappresenta un generico cursore.
 *
 * @param <C> il tipo parametrico delle coordinate del punto nel piano.
 * @param <D> il tipo parametrico per la direzione in cui punta il cursore nel piano.
 */
public interface Cursor<C, D extends Directional<?>> {

    /**
     * Restituisce le coordinate della posizione attuale del cursore.
     *
     * @return le coordinate della posizione attuale del cursore.
     */
    C getPosition();

    /**
     * Imposta il cursore in una nuova posizione nel piano.
     *
     * @param position la nuova posizione del cursore nel piano.
     */
    void setPosition(C position);

    /**
     * Restituisce la direzione verso cui &egrave; puntato il cursore.
     *
     * @return la direzione verso cui &egrave; puntato il cursore.
     */
    D getDirection();

    /**
     * Imposta la direzione verso cui dovr&agrave; puntare il cursore.
     *
     * @param direction la nuova direzione verso cui dovr&agrave; puntare il cursore.
     */
    void setDirection(D direction);

    /**
     * Restituisce il colore della linea prodotta del cursore come conseguenza di uno spostamento.
     *
     * @return il colore della linea prodotta dal cursore.
     */
    RGBColor getLineColor();

    /**
     * Imposta il colore della linea prodotta dal cursore come conseguenza di uno spostamento.
     *
     * @param color il colore da impostare alla linea prodotta dal cursore.
     */
    void setLineColor(RGBColor color);

    /**
     * Restituisce il colore dell' area formata da una serie di segmenti.
     *
     * @return il colore dell' area formata da una serie di segementi.
     */
    RGBColor getAreaColor();

    /**
     * Imposta il colore dell' area prodotta formata da una serie di segmenti.
     *
     * @param color il colore da impostare all' area.
     */
    void setAreaColor(RGBColor color);

    /**
     * Indica se durante uno spostamento, il cursore genera o meno un tracciato.
     *
     * @return true se &egrave; stato generato un tracciato, false altrimenti.
     */
    boolean isPlot();

    /**
     * Imposta il plot a seconda della generazione di un tracciato.
     *
     * @param plot il plot da impostare.
     */
    void setPlot(boolean plot);

    /**
     * Restituisce il piano in cui &egrave; contenuto questo cursore.
     *
     * @return il piano in cui &egrave; contenuto questo cursore.s
     */
    Plane<C> getPlane();

    /**
     * Restituisce true se la penna &egrave; attaccata al piano, false altrimenti.
     *
     * @return true se la penna &egrave; attaccata al piano, false altrimenti.
     */
    boolean isPen();

    /**
     * Imposta la penna attaccata al piano del cursore.
     */
    void penUp();

    /**
     * Imposta la penna staccata dal piano del cursore.
     */
    void penDown();

    /**
     * Restituisce la size del tratto della penna.
     *
     * @return un intero >=1 che rappresenta la size del tratto della penna.
     */
    int getPenSize();

    /**
     * Imposta la size alla penna.
     *
     * @param size la grandezza del tratto della penna.
     * @throws IllegalArgumentException se size &egrave; minore di 1.
     */
    void setPenSize(int size);

    /**
     * Restituisce la rappresentazione del cursore sotto forma di stringa.
     *
     * @return rappresentazione del cursore sotto forma di stringa.
     */
    @Override
    String toString();

    /**
     * Compara l' oggetto specificato con questo cursore per verificarne l' uguaglianza.
     *
     * @param o l' oggetto da comparare con questo cursore per l' uguaglianza.
     * @return true se l' oggetto specificato &egrave; uguale a questo cursore, false altrimenti.
     */
    @Override
    boolean equals(Object o);

    /**
     * Restituisce il valore hash code per questo cursore.
     *
     * @return il valore hash code per questo cursore.
     */
    @Override
    int hashCode();



}
