package it.cs.unicam.pa2021.logo.model;

/**
 * Viene lanciata per indicare che un metodo ha cercato di eseguire un' istruzione LOGO con sintassi
 * non rispettata.
 */
public class LOGOSyntaxErrorException extends Exception {

    public LOGOSyntaxErrorException(String error) {
        super(error);
    }
}
