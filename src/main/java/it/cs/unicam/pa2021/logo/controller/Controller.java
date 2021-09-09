package it.cs.unicam.pa2021.logo.controller;

import it.cs.unicam.pa2021.logo.model.LOGOSyntaxErrorException;
import it.cs.unicam.pa2021.logo.model.Plane;

import java.io.IOException;
import java.util.Deque;
import java.util.List;

/**
 * Definisce il controller dell' ambiente d' esecuzione per il linguaggio LOGO.
 *
 * @param <C> il tipo parametrico delle coordinate del punto nel piano.
 */
public interface Controller<C> {

    /**
     * Costruisce un nuovo piano con le dimensioni specificate.
     *
     * @param length l' altezza del piano.
     * @param height la lunghezza del piano.
     */
    void newPlane(double length, double height);

    /**
     * Restituisce la configurazione del piano corrente.
     *
     * @return la configurazione del piano corrente.
     */
    Plane<C> getPlane();

    /**
     * Permette di verificare di tornare nella configurazione del piano precedente a quella attuale.
     *
     * @return true se &egrave; possibile tornare alla configurazione precedente a quella attuale, false altrimenti.
     */
    boolean hasPrevious();

    /**
     * Permette di verificare di andare nella configurazione del piano successiva a quella attuale.
     *
     * @return true se &egrave; possibile andare alla configurazione successiva a quella attuale, false altrimenti.
     */
    boolean hasNext();

    /**
     * Restituisce la configurazione del piano precedente a quella attuale.
     *
     * @return la configurazione del piano precedente a quella attuale.
     */
    Plane<C> previous();

    /**
     * Restituisce la configurazione del piano successiva a quella attuale.
     *
     * @return la configurazione del piano successiva a quella attuale.
     */
    Plane<C> next();

    /**
     * Restituisce la lista delle istruzioni LOGO contenute nel file, il cui percorso
     * &egrave; passato come parametro.
     * Viene lanciata una LOGOSyntaxErrorException se viene caricato un numero come istruzione.
     *
     * @param filePath il percorso assoluto o relativo del file da leggere.
     * @throws IOException se c'&egrave; un errore di I/O nell' apertura del file.
     */
    void loadInstructions(String filePath) throws IOException;

    /**
     * Restituisce la lista delle istruzioni caricate da file.
     *
     * @return la lista delle istruzioni caricate.
     */
    List<String> getAllInstructions();

    /**
     * Restituisce la coda di tutte le istruzioni per una determinata configurazione.
     *
     * @return la coda di tutte le istruzioni per una determinata configurazione.
     */
    Deque<String> getConfigurationInstructions();

    /**
     * Crea un file con lo stesso nome del file usato per caricare le istruzioni, ma che
     * inizia con "Output".
     * In questo file vengono memorizzate tutte le linee presenti nel piano e tutte le area chiuse,
     * ognuna di queste su una riga del file, comprese tutte le loro caratteristiche.
     *
     * @param filepath il percorso assoluto o relativo del file utilizzato per le istruzioni.
     * @return true se il file non esisteva ed &egrave; stato creato correttamente, false altrimenti.
     * @throws IOException se ci sono errori di I/O con il file.
     */
    boolean createLOGOFile(String filepath) throws IOException;

    /**
     * Esegue un' istruzione LOGO nel piano.
     * Viene lanciata una LOGOSyntaxErrorException se ci sono errori di sintassi nell' istruzione da eseguire.
     *
     * @param instruction l' istruzione da eseguire.
     */
    void execute(String instruction);

    /**
     * Esegue una lista di istruzioni LOGO nel piano.
     * Viene lanciata una LOGOSyntaxErrorException se ci sono errori di sintassi nell' istruzione da eseguire.
     *
     * @param instructions la lista di istruzioni da eseguire.
     */
    void executeAll(List<String> instructions);

    /**
     * Cancella tutte le configurazioni precedenti e successive del piano, con le rispettive
     * istruzioni per quelle configurazioni.
     */
    void clear();

    /**
     * Cancella tutte le configurazioni successive a questo piano.
     */
    void clearNext();

}
