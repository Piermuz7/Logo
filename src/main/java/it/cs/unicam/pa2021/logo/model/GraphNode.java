package it.cs.unicam.pa2021.logo.model;

import java.util.Objects;

/**
 * Rappresenta un nodo contenuto in un grafo.
 *
 * @param <L> il tipo parametrico per le etichette dei nodi.
 * @param <D> il tipo parametrico per l' oggetto contenuto nel nodo.
 */
public class GraphNode<L, D> {

    /**
     * Colore bianco associato al nodo.
     */
    public static int COLOR_WHITE = 0;
    /**
     * Colore grigio associato al nodo.
     */
    public static int COLOR_GREY = 1;

    /**
     * Colore nero associato al nodo.
     */
    public static int COLOR_BLACK = 2;

    private final L label;

    private final D data;


    /**
     * Crea un nodo con l' etichetta e l' oggetto da contenere specificati.
     *
     * @param label l' etichetta del nodo.
     * @param data  l' oggetto da contenere nel nodo.
     */
    public GraphNode(L label, D data) {
        this.label = label;
        this.data = data;
    }

    /**
     * Restituisce l'etichetta associata al nodo che lo identifica univocamente nel grafo.
     *
     * @return l' etichetta del nodo.
     */
    public L getLabel() {
        return this.label;
    }

    /**
     * Restituisce l'oggetto contenuto nel nodo.
     *
     * @return l' oggetto contenuto nel nodo.
     */
    public D getData() {
        return this.data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode<?, ?> graphNode = (GraphNode<?, ?>) o;
        return Objects.equals(label, graphNode.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return "Nodo[ " + label.toString() + " ]";
    }
}
