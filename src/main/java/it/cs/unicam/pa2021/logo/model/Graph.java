package it.cs.unicam.pa2021.logo.model;

import java.util.List;
import java.util.Map;

/**
 * Rappresenta un grafo rappresentato tramite matrice di adiacenza.
 * Se viene trovato un ciclo nel grafo, questo viene ritornato e rimosso dal grafo.
 *
 * @param <D> il tipo parametrico per i dati da contenere nei nodi.
 */
public interface Graph<D> {

    /**
     * Esegue la visita in profondit&agrve; di questo grafo per trovare uno o più cicli.
     *
     * @param u il nodo sorgente.
     * @param p il padre del nodo sorgente.
     */
    void cycleDFS(GraphNode<Integer, D> u, GraphNode<Integer, D> p);

    /**
     * Aggiunge un arco (u, v) a questo grafo.
     *
     * @param u il nodo u dell' arco (u, v).
     * @param v il nodo v dell' arco (u, v).
     */
    void addEdge(GraphNode<Integer, D> u, GraphNode<Integer, D> v);

    /**
     * Restituisce e rimuove i nodi che formano un ciclo in questo grafo.
     * Se il grafo non contiene cicli, viene ritornata una lista vuota.
     *
     * @param u il nodo sorgente.
     * @param p il padre del nodo sorgente.
     * @return una lista di dati contenuti nei nodi, i quali formano un ciclo presente nel grafo.
     */
    List<D> getCycle(GraphNode<Integer, D> u, GraphNode<Integer, D> p);

    /**
     * Inizializza questo grafo.
     */
    void clear();

    /**
     * Restituisce la matrice delle adiacenze di questo grafo.
     *
     * @return la matrice delle adiacenza di questo grafo.
     */
    List<List<GraphNode<Integer, D>>> getMatrix();

    /**
     * Restituisce la lista dei cicli presenti nel grafo.
     *
     * @return la lista dei cicli presenti nel grafo.
     */
    List<List<Integer>> getCycles();

    /**
     * Restituisce una mappa contenente la mappatura tra l' indice intero di un nodo e l' oggetto
     * contenuto in quel nodo.
     *
     * @return la mappa dei nodi con i propri indici nel grafo.
     */
    Map<Integer, D> getNodes();

    /**
     * Restituisce il numero di archi presenti nel grafo.
     *
     * @return il numero di archi presenti nel grafo.
     */
    int getEdges();

    /**
     * Restituisce il numero di ciclo corrente nel grafo.
     *
     * @return il numero di ciclo corrente nel grafo.
     */
    int getCycleNumber();

    /**
     * Restituisce una lista degli indici dei padri nel grafo.
     *
     * @return la lista degli indici dei padri nel grafo.
     */
    List<Integer> getParents();

    /**
     * Restituisce la lista degli indici dei nodi segnati per quel determinato ciclo.
     *
     * @return la lista degli indici dei nodi segnati per quel determinato ciclo.
     */
    List<Integer> getMarkedNodes();

    /**
     * Restituisce la lista degli indici dei nodi colorati.
     *
     * @return la lista degli indici dei nodi colorati.
     */
    List<Integer> getColors();

}
