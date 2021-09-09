package it.cs.unicam.pa2021.logo.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Rappresenta un grafo non orientato tramite matrice di adiacenza.
 * Gli oggetti GraphNode<L,D>, cioè i nodi, sono memorizzati in una lista di liste di nodi che
 * associa ad ogni nodo l' indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l' insieme dei nodi.
 *
 * @param <D> il tipo parametrico per l' oggetto da contenere nel nodo
 */
public final class UndirectedGraph<D> implements Graph<D> {

    List<List<GraphNode<Integer, D>>> matrix;
    private List<List<Integer>> cycles;
    Map<Integer, D> nodes;
    private int edges;
    private int cycleNumber;
    private List<Integer> parents;
    private List<Integer> markedNodes;
    private List<Integer> colors;

    /**
     * Crea un grafo non orientato.
     */
    public UndirectedGraph() {
        initialize();
    }

    public UndirectedGraph(Graph<D> g) {
        this.matrix = new ArrayList<>();
        this.matrix.addAll(g.getMatrix());
        this.cycles = new ArrayList<>();
        this.cycles.addAll(g.getCycles());
        this.nodes = new HashMap<>();
        this.nodes.putAll(g.getNodes());
        this.edges = g.getEdges();
        this.cycleNumber = g.getCycleNumber();
        this.parents = new ArrayList<>();
        this.parents.addAll(g.getParents());
        this.markedNodes = new ArrayList<>();
        this.markedNodes.addAll(g.getMarkedNodes());
        this.colors = new ArrayList<>();
        this.colors.addAll(g.getColors());
    }

    @Override
    public void cycleDFS(GraphNode<Integer, D> u, GraphNode<Integer, D> p) {
        if (this.colors.get(u.getLabel()) == GraphNode.COLOR_BLACK)
            return;
        if (this.colors.get(u.getLabel()) == GraphNode.COLOR_GREY) {
            this.cycleNumber++;
            Integer cur = p.getLabel();
            this.markedNodes.set(cur, this.cycleNumber);
            while (!cur.equals(u.getLabel())) {
                cur = this.parents.get(cur);
                this.markedNodes.set(cur, this.cycleNumber);
            }
            return;
        }
        this.parents.set(u.getLabel(), p.getLabel());
        this.colors.set(u.getLabel(), GraphNode.COLOR_GREY);
        for (GraphNode<Integer, D> v : this.matrix.get(u.getLabel())) {
            if (Objects.equals(v.getLabel(), this.parents.get(u.getLabel())))
                continue;
            this.cycleDFS(v, u);
        }
        this.colors.set(u.getLabel(), GraphNode.COLOR_BLACK);
    }

    @Override
    public void addEdge(GraphNode<Integer, D> u, GraphNode<Integer, D> v) {
        matrix.get(u.getLabel()).add(v);
        matrix.get(v.getLabel()).add(u);
        nodes.put(u.getLabel(), u.getData());
        nodes.put(v.getLabel(), v.getData());
        this.edges++;
    }

    @Override
    public List<D> getCycle(GraphNode<Integer, D> u, GraphNode<Integer, D> p) {
        this.cycleNumber = 0;
        for (int i = 0; i < 100000; i++)
            this.cycles.set(i, new ArrayList<>());
        for (int i = 0; i < 100000; i++) {
            this.parents.set(i, 0);
            this.markedNodes.set(i, 0);
            this.colors.set(i, GraphNode.COLOR_WHITE);
        }
        this.cycleDFS(u, p);
        for (int i = 1; i <= this.edges; i++) {
            if (this.markedNodes.get(i) != 0)
                this.cycles.get(this.markedNodes.get(i)).add(i);
        }
        List<D> cycle = new ArrayList<>();
        for (int x : this.cycles.get(this.cycleNumber)) {
            cycle.add(nodes.get(x));
            this.nodes.remove(x);
            this.matrix.get(x).clear();
            for (List<GraphNode<Integer, D>> graphNodes : this.matrix)
                graphNodes.removeIf(cycle::contains);
        }
        this.edges -= cycle.size();
        return cycle;
    }

    @Override
    public void clear() {
        initialize();
    }

    private void initialize() {
        this.matrix = new ArrayList<>();
        this.cycles = new ArrayList<>();
        this.nodes = new HashMap<>();
        this.edges = 0;
        this.cycleNumber = 0;
        this.parents = new ArrayList<>();
        this.markedNodes = new ArrayList<>();
        this.colors = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            this.matrix.add(i, new ArrayList<>());
            this.cycles.add(i, new ArrayList<>());
        }
        for (int i = 0; i < 100000; i++) {
            this.parents.add(0);
            this.markedNodes.add(0);
            this.colors.add(GraphNode.COLOR_WHITE);
        }
    }

    @Override
    public List<List<GraphNode<Integer, D>>> getMatrix() {
        return matrix;
    }

    @Override
    public List<List<Integer>> getCycles() {
        return cycles;
    }

    @Override
    public Map<Integer, D> getNodes() {
        return nodes;
    }

    @Override
    public int getEdges() {
        return edges;
    }


    @Override
    public int getCycleNumber() {
        return cycleNumber;
    }


    @Override
    public List<Integer> getParents() {
        return parents;
    }

    @Override
    public List<Integer> getMarkedNodes() {
        return markedNodes;
    }

    @Override
    public List<Integer> getColors() {
        return colors;
    }
}