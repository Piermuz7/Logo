package it.cs.unicam.pa2021.logo.model;


import java.util.Queue;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Rappresenta un generico piano geometrico.
 *
 * @param <C> il tipo parametrico per le coordinate del punto nel piano.
 */
public interface Plane<C> {

    /**
     * Restituisce la lunghezza del piano.
     *
     * @return la lunghezza del piano.
     */
    double getLength();


    /**
     * Restituisce l' altezza del piano.
     *
     * @return l' altezza del piano.
     */
    double getHeight();

    /**
     * Restituisce la coordinata dell'origine del piano.
     *
     * @return la coordinata dell' origine del piano.
     */
    C getOrigin();

    /**
     * Restituisce la coordinata del punto centrale del piano.
     *
     * @return la coordinata del punto centrale del piano.
     */
    C getHome();

    /**
     * Restituisce l' insieme delle linee presenti nel piano in ordine cronologico.
     *
     * @return l' insieme delle linee presenti nel piano.
     */
    Queue<Line<C>> getLines();

    /**
     * Restituisce il numero di linee presenti nel piano.
     *
     * @return il numero di linee presenti nel piano.
     */
    int getNumLines();

    /**
     * Restituisce il colore di background del piano.
     *
     * @return il colore di background del piano.
     */
    RGBColor getBackgroundColor();

    /**
     * Imposta il colore specificato come colore di background del piano.
     *
     * @param backgroundColor il colore di background da impostare al piano.
     */
    void setBackgroundColor(RGBColor backgroundColor);

    /**
     * Aggiunge la linea specificata al piano.
     *
     * @param line la linea da aggiungere al piano.
     */
    void addLine(Line<C> line);

    /**
     * Restituisce il numero di punti presenti nel piano, che appartengono a delle linee presenti nel piano.
     *
     * @return il numero di punti presenti nel piano.
     */
    int getNumPoints();

    /**
     * Restituisce il cursore che si trova nel piano.
     *
     * @return il cursore situato nel piano.
     */
    <D extends Directional<?>> Cursor<C, D> getCursor();

    /**
     * Restituisce l' insieme di tutte le aree chiuse presenti nel piano.
     *
     * @return l' insieme di tutte le aree chiuse presenti nel piano.
     */
    Queue<ClosedArea<Line<C>>> getClosedAreas();

    /**
     * Restituisce il numero di aree chiuse nel piano.
     *
     * @return il numero di aree chiuse nel piano.
     */
    int getNumClosedAreas();

    /**
     * Verifica se il punto specificato appartiene al piano.
     *
     * @param point il punto di cui verificarne l' appartenenza al piano.
     * @return true se il punto specificato appartiene al piano, false altrimenti.
     */
    boolean bePartOfPlane(C point);

    /**
     * Restituisce le coordinate dell' angolo in basso a sinistra.
     *
     * @return il punto del piano in basso a sinistra.
     */
    C getDownLeftPoint();

    /**
     * Restituisce le coordinate dell' angolo in basso a destra.
     *
     * @return il punto del piano in basso a destra.
     */
    C getDownRightPoint();

    /**
     * Restituisce le coordinate dell' angolo in alto a sinistra.
     *
     * @return il punto del piano in alto a sinistra.
     */
    C getUpLeftPoint();

    /**
     * Restituisce le coordinate dell' angolo in alto a destra.
     *
     * @return il punto del piano in alto a destra.
     */
    C getUpRightPoint();

    /**
     * Restituisce una mappa dei punti con i corrispettivi valori che li identificano.
     *
     * @param <L> il tipo parametrico per l' identificativo del punto.
     * @return la mappa dei punti con i corrispettivi identificatori.
     */
    <L> Map<C, L> getPoints();

    /**
     * Restituisce il grafo dei punti di questo piano.
     *
     * @return il grafo dei punti nel piano.
     */
    Graph<C> getGraph();

    PlaneUpdateSupport<C> getPlaneUpdateSupport();

    /**
     * Restituisce la posizione corrente del cursore nel piano.
     *
     * @return la posizione corrente del cursore nel piano.
     */
    default C getCursorPosition() {
        return getCursor().getPosition();
    }

    /**
     * Restituisce l' insieme delle linee aventi come estremo il punto passato come parametro.
     *
     * @param point il punto per cui passano determinate linee.
     * @return l' insieme delle linee che passano per quel determinato punto.
     */
    default Set<Line<C>> getLinesAt(C point) {
        Set<Line<C>> lines = new HashSet<>();
        if (bePartOfPlane(point)) {
            lines.addAll(getLines().
                    stream().
                    filter(l -> l.getStartingPoint().equals(point)
                            || l.getEndPoint().equals(point))
                    .collect(Collectors.toSet()));
        }
        return lines;
    }

    /**
     * Metodo statico che prese due linee calcola il punto di intersezione.
     *
     * @param a la prima linea.
     * @param b la seconda linea.
     * @return il punto di intersezione tra la linea a e la linea b se esiste, Optional.isEmpty() altrimenti.
     */
    static Optional<Point<Double>> intersection(Line<Point<Double>> a, Line<Point<Double>> b) {
        double x1 = a.getStartingPoint().getX(), y1 = a.getStartingPoint().getY(),
                x2 = a.getEndPoint().getX(), y2 = a.getEndPoint().getY(),
                x3 = b.getStartingPoint().getX(), y3 = b.getStartingPoint().getY(),
                x4 = b.getEndPoint().getX(), y4 = b.getEndPoint().getY();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
            return Optional.empty();
        double x = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double y = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
        x = x == -0.0 ? 0.0 : x;
        y = y == -0.0 ? 0.0 : y;
        return Optional.of(Point.cartesianPoint(Math.round(x * 100.0) / 100.0, Math.round(y * 100.0) / 100.0));
    }

    /**
     * Aggiunge il listener specificato a questo piano.
     *
     * @param listener il listener da aggiungere al piano.
     */
    void addPlaneUpdateListener(PlaneUpdateListener<Point<Double>> listener);

    /**
     * Rimuove il listener specificato da questo piano.
     *
     * @param listener il listener da rimuovere dal piano.
     */
    void removePlaneUpdateListener(PlaneUpdateListener<Point<Double>> listener);

}
