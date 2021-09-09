package it.cs.unicam.pa2021.logo.model;

/**
 * Interfaccia che esplicita che un oggetto pu&ograve; essere direzionato.
 * Indica la direzione da seguire per una certa classe che implementa
 * l' interfaccia it.cs.unicam.pa2021.logo.model.Directional.
 */
public interface Directional<D> {
    /**
     * Metodo statico che crea un oggetto SimpleDirection con le caratteristiche della direzione LOGO di default.
     *
     * @return la direzione di default dell' ambiente LOGO.
     */
    static SimpleDirection defaultSimpleDirection() {
        return new SimpleDirection();
    }

    /**
     * Metodo statico che crea un oggetto SimpleDirection con l' angolo specificato.
     *
     * @param angle l' angolo verso cui punta la direzione.
     * @return la direzione con l' angolo specificato.
     */
    static SimpleDirection simpleDirection(int angle) {
        return new SimpleDirection(angle);
    }

    /**
     * Restituisce la direzione corrente.
     *
     * @return la direzione verso cui l' oggetto punta.
     */
    D getDirectionWay();

    /**
     * Imposta la direzione specificata come direzione.
     * La direzione deve essere compresa nel range in questione.
     *
     * @param direction la direzione da impostare.
     * @throws IllegalArgumentException se l' angolo da impostare &egrave; out of range.
     */
    void setDirectionWay(D direction);

    /**
     * Restituisce la rappresentazione della direzione sotto forma di stringa.
     *
     * @return rappresentazione della direzione sotto forma di stringa.
     */
    @Override
    String toString();

}
