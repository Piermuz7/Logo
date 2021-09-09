package it.cs.unicam.pa2021.logo.model;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


/**
 * Rappresenta l' esecuzione di un' istruzione LOGO nel piano.
 *
 * @param <C> il tipo parametrico per le coordinate del punto nel piano.
 */
@FunctionalInterface
public interface Instruction<C> {

    /**
     * Metodo statico che implementa l' istruzione FORWARD.
     * Sposta il cursore in avanti verso la sua direzione di una certa distanza passata in args[0].
     * Se il cursore supera l' altezza del piano in avanti, si ferma al bordo.
     *
     * @param plane il piano contenente il cursore da spostare.
     * @param args  in particolare args[0], la distanza di cui spostarsi dalla sua posizione.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione FORWARD.
     * @throws IllegalArgumentException se la distanza specificata &egrave; minore di 0.
     */
    static Plane<Point<Double>> forward(Plane<Point<Double>> plane, Object... args) {
        return move('+', plane, args[0]);
    }

    /**
     * Metodo statico che implementa l' istruzione BACKWARD.
     * Sposta il cursore indietro verso la sua direzione di una certa distanza passata in args[0].
     * Se il cursore supera l' altezza del piano all' indietro, si ferma al bordo.
     *
     * @param plane il piano contenente il cursore da spostare.
     * @param args  in particolare args[0], la distanza di cui spostarsi dalla sua posizione.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione BACKWARD.
     * @throws IllegalArgumentException se la distanza specificata &egrave; minore di 0.
     */
    static Plane<Point<Double>> backward(Plane<Point<Double>> plane, Object... args) {
        return move('-', plane, args[0]);
    }

    /**
     * Metodo statico che implementa l' istruzione LEFT.
     * Ruota il cursore in senso antiorario di tanti gradi quanti specificati.
     *
     * @param plane il piano contente il cursore da ruotare.
     * @param args  in particolare args[0], i gradi di cui ruotare in senso antiorario rispetto la sua direzione attuale.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione LEFT.
     * @throws IllegalArgumentException se l' angolo da impostare &egrave; out of range.
     */
    static Plane<Point<Double>> left(Plane<Point<Double>> plane, Object... args) {
        return direct('+', plane, args[0]);
    }

    /**
     * Metodo statico che implementa l' istruzione RIGHT.
     * Ruota il cursore in senso orario di tanti gradi quanti specificati.
     *
     * @param plane il piano contente il cursore da ruotare.
     * @param args  in particolare args[0], i gradi di cui ruotare in senso orario rispetto la sua direzione attuale.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione RIGHT.
     * @throws IllegalArgumentException se l' angolo da impostare &egrave; out of range.
     */
    static Plane<Point<Double>> right(Plane<Point<Double>> plane, Object... args) {
        return direct('-', plane, args[0]);
    }

    /**
     * Metodo statico per impostare la direzione.
     *
     * @param direction - per direzione in senso orario, + antiorario.
     * @param plane     il piano in cui direzionare il cursore.
     * @param angle     l' angolo di cui spostarsi.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione LEFT/RIGHT.
     */
    private static Plane<Point<Double>> direct(char direction, Plane<Point<Double>> plane, Object angle) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        int degrees = Integer.parseInt("" + angle);
        if (degrees < 0 || degrees > 360)
            throw new IllegalArgumentException("Angle out of range");
        int newDirection;
        if (direction == '-') {
            newDirection = (int) p.getCursor().getDirection().getDirectionWay() - degrees;
            newDirection = newDirection < 0 ? (newDirection + 360) : newDirection;
        } else {
            newDirection = (int) p.getCursor().getDirection().getDirectionWay() + degrees;
            newDirection = newDirection >= 360 ? (newDirection - 360) : newDirection;
        }
        p.getCursor().setDirection(Directional.simpleDirection(newDirection));
        return p;
    }

    /**
     * Metodo statico per muovere un cursore in avanti o indietro rispetto la sua direzione.
     *
     * @param operator + per andare avanti, - per andare indietro.
     * @param plane    il piano dove spostare il cursore.
     * @param distance la distanza di cui spostarsi dalla sua posizione.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione FORWARD/BACKWARD.
     * @throws IllegalArgumentException se la distanza specificata &egrave; minore di 0.
     */
    private static Plane<Point<Double>> move(char operator, Plane<Point<Double>> plane, Object distance) {
        int dist = Integer.parseInt("" + distance);
        if (dist < 0)
            throw new IllegalArgumentException("Negative distance!");
        Plane<Point<Double>> p = new DefaultPlane(plane);
        Point<Double> oldPosition = Point.cartesianPoint(p.getCursorPosition().getX(), p.getCursorPosition().getY());
        int degrees = (int) p.getCursor().getDirection().getDirectionWay();
        double angleCos = operator == '-' ? -(dist * Math.cos(Math.toRadians(degrees))) : dist * Math.cos(Math.toRadians(degrees));
        double angleSin = operator == '-' ? -(dist * Math.sin(Math.toRadians(degrees))) : dist * Math.sin(Math.toRadians(degrees));
        double newX = Math.round((p.getCursorPosition().getX() + angleCos) * 100.0) / 100.0;
        double newY = Math.round((p.getCursorPosition().getY() + angleSin) * 100.0) / 100.0;
        Point<Double> newPosition = Point.cartesianPoint(newX, newY);
        checkCursorAtBorder(p, newPosition, oldPosition);
        Line<Point<Double>> l = new Segment<>(oldPosition, p.getCursorPosition(), plane.getCursor().getLineColor(), plane.getCursor().getPenSize());
        if (p.getCursor().isPen()) {
            p.addLine(l);
            Logger.getGlobal().info("Generated line: " + l);
            p.getCursor().setPlot(true);
            p.getPlaneUpdateSupport().fireGeneratedLine(l);
        } else {
            p.getCursor().setPlot(false);
            Logger.getGlobal().info("Moved cursor from " + oldPosition + " to " + newPosition);
            p.getPlaneUpdateSupport().fireMovedCursor(newPosition);
        }
        return p;
    }

    /**
     * Metodo statico per posizionare il cursore al bordo del piano se questo supera i limiti.
     *
     * @param plane             il piano in questione.
     * @param newCursorPosition la nuova posizione del cursore da controllare.
     */
    private static void checkCursorAtBorder(Plane<Point<Double>> plane, Point<Double> newCursorPosition, Point<Double> oldCursorPosition) {
        Segment<Point<Double>> xDownAxis = new Segment<>(plane.getDownLeftPoint(), plane.getDownRightPoint(), plane.getCursor().getLineColor(), plane.getCursor().getPenSize()),
                xUpAxis = new Segment<>(plane.getUpLeftPoint(), plane.getUpRightPoint(), plane.getCursor().getLineColor(), plane.getCursor().getPenSize()),
                yLeftAxis = new Segment<>(plane.getUpLeftPoint(), plane.getDownLeftPoint(), plane.getCursor().getLineColor(), plane.getCursor().getPenSize()),
                yRightAxis = new Segment<>(plane.getUpRightPoint(), plane.getDownRightPoint(), plane.getCursor().getLineColor(), plane.getCursor().getPenSize());
        Line<Point<Double>> line = new Segment<>(oldCursorPosition, newCursorPosition, plane.getCursor().getLineColor(), plane.getCursor().getPenSize());
        Optional<Point<Double>> intersectionPoint = Optional.empty();
        if (newCursorPosition.getY() >= plane.getHeight())
            intersectionPoint = Plane.intersection(line, xUpAxis);
        if (newCursorPosition.getY() < 0)
            intersectionPoint = Plane.intersection(line, xDownAxis);
        if (newCursorPosition.getX() >= plane.getLength())
            intersectionPoint = Plane.intersection(line, yRightAxis);
        if (newCursorPosition.getX() < 0)
            intersectionPoint = Plane.intersection(line, yLeftAxis);
        plane.getCursor().setPosition(intersectionPoint.orElse(newCursorPosition));
        Logger.getGlobal().info("Intersection point:  " + intersectionPoint);
        Logger.getGlobal().info("Cursor position " + plane.getCursorPosition());
    }

    /**
     * Metodo statico che implementa l' istruzione CLEARSCREEN.
     * Non fa altro che cancellare tutto ci&ograve; che &egrave; stato disegnato nel piano.
     *
     * @param plane il piano in cui cancellare tutto ci&ograve; che &egrave; stato disegnato.
     * @param args  in questo caso nessun argomento, quindi args sar&agrave; un array vuoto.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione CLEARSCREEN.
     */
    static Plane<Point<Double>> clearScreen(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        p.getPlaneUpdateSupport().fireScreenCleaned();
        p.getLines().clear();
        p.getClosedAreas().clear();
        p.getPoints().clear();
        p.getGraph().clear();
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione HOME.
     * Sposta il cursore in posizione home del piano.
     *
     * @param plane il piano in cui spostare il cursore nella home.
     * @param args  in questo caso nessun argomento, quindi args sar&agrave; un array vuoto.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione HOME.
     */
    static Plane<Point<Double>> home(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        p.getCursor().setPosition(p.getHome());
        p.getPlaneUpdateSupport().fireMovedCursor(p.getCursorPosition());
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione PENUP.
     * Stacca la penna dal foglio, quindi se il cursore si sposta non viene generata nessuna linea.
     *
     * @param plane il piano da cui staccare la penna.
     * @param args  in questo caso nessun argomento, quindi args sar&agrave; un array vuoto.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione PENUP.
     */
    static Plane<Point<Double>> penUp(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        p.getCursor().penUp();
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione PENDOWN.
     * Attacca la penna al foglio, quindi se il cursore si sposta generata una linea.
     *
     * @param plane il piano a cui attaccare la penna.
     * @param args  in questo caso nessun argomento, quindi args sar&agrave; un array vuoto.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione PENDOWN.
     */
    static Plane<Point<Double>> penDown(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        p.getCursor().penDown();
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione SETPENCOLOR.
     * Imposta il colore RGB della penna in base ai parametri specificati.
     *
     * @param plane il piano in cui impostare il colore RGB della penna.
     * @param args  in particolare args[0] = R, args[1] = G, args[2] = B.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione SETPENCOLOR.
     * @throws IllegalArgumentException se R, G o B sono out of range.
     */
    static Plane<Point<Double>> setPenColor(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        int r = Integer.parseInt("" + args[0]), g = Integer.parseInt("" + args[1]), b = Integer.parseInt("" + args[2]);
        p.getCursor().setLineColor(new RGBColor(r, g, b));
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione SETFILLCOLOR.
     * Imposta il colore RGB di riempimento di un' area chiusa in base ai parametri specificati.
     *
     * @param plane il piano in cui impostare il colore RGB di riempimento dell' area chiusa.
     * @param args  in particolare args[0] = R, args[1] = G, args[2] = B.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione SETFILLCOLOR.
     * @throws IllegalArgumentException se R, G o B sono out of range.
     */
    static Plane<Point<Double>> setFillColor(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        int r = Integer.parseInt("" + args[0]), g = Integer.parseInt("" + args[1]), b = Integer.parseInt("" + args[2]);
        p.getCursor().setAreaColor(new RGBColor(r, g, b));
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione SETSCREENCOLOR.
     * Imposta il colore RGB di background del piano in base ai parametri specificati.
     *
     * @param plane il piano in cui impostare il proprio colore RGB.
     * @param args  in particolare args[0] = R, args[1] = G, args[2] = B.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione SETSCREENCOLOR.
     * @throws IllegalArgumentException se R, G o B sono out of range.
     */
    static Plane<Point<Double>> setScreenColor(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        int r = Integer.parseInt("" + args[0]), g = Integer.parseInt("" + args[1]), b = Integer.parseInt("" + args[2]);
        p.setBackgroundColor(new RGBColor(r, g, b));
        p.getPlaneUpdateSupport().fireScreenColorChanged(p.getBackgroundColor());
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione SETPENSIZE.
     * Imposta il tratto della penna in base alla size specificata.
     *
     * @param plane il piano in cui impostare il tratto della penna.
     * @param args  in particolare args[0], rappresenta la size della penna.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione SETPENSIZE.
     * @throws IllegalArgumentException se il tratto della penna specificata &egrave; minore di 1.
     */
    static Plane<Point<Double>> setPenSize(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        int size = Integer.parseInt("" + args[0]);
        p.getCursor().setPenSize(size);
        return p;
    }

    /**
     * Metodo statico che implementa l' istruzione REPEAT.
     * Ripete la sequenza di comandi [cmds] per N volte.
     * Se nella lista di istruzioni da ripetere sono presenti istruzioni non sono valide per l' ambiente LOGO,
     * verranno eseguite solo quelle valide.
     *
     * @param plane il piano in cui eseguire la sequenza di comandi per N volte.
     * @param args  in particolare args[0] = N, args[1] = List[cmds].
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione REPEAT.
     */
    static Plane<Point<Double>> repeat(Plane<Point<Double>> plane, Object... args) {
        Plane<Point<Double>> p = new DefaultPlane(plane);
        Instruction<Point<Double>> instr;
        List<String> cmds = (List<String>) args[1];
        int n = Integer.parseInt("" + args[0]);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < cmds.size(); j++)
                switch (cmds.get(j)) {
                    case "FORWARD": {
                        instr = Instruction::forward;
                        p = instr.execute(p, cmds.get(j + 1));
                        break;
                    }
                    case "BACKWARD": {
                        instr = Instruction::backward;
                        p = instr.execute(p, cmds.get(j + 1));
                        break;
                    }
                    case "LEFT": {
                        instr = Instruction::left;
                        p = instr.execute(p, cmds.get(j + 1));
                        break;
                    }
                    case "RIGHT": {
                        instr = Instruction::right;
                        p = instr.execute(p, cmds.get(j + 1));
                        break;
                    }
                    case "CLEARSCREEN": {
                        instr = Instruction::clearScreen;
                        p = instr.execute(p);
                        break;
                    }
                    case "HOME": {
                        instr = Instruction::home;
                        p = instr.execute(p);
                        break;
                    }
                    case "PENUP": {
                        instr = Instruction::penUp;
                        p = instr.execute(p);
                        break;
                    }
                    case "PENDOWN": {
                        instr = Instruction::penDown;
                        p = instr.execute(p);
                        break;
                    }
                    case "SETPENCOLOR": {
                        instr = Instruction::setPenColor;
                        p = instr.execute(p, cmds.get(j + 1), cmds.get(j + 2), cmds.get(j + 3));
                        break;
                    }
                    case "SETFILLCOLOR": {
                        instr = Instruction::setFillColor;
                        p = instr.execute(p, cmds.get(j + 1), cmds.get(j + 2), cmds.get(j + 3));
                        break;
                    }
                    case "SETSCREENCOLOR": {
                        instr = Instruction::setScreenColor;
                        p = instr.execute(p, cmds.get(j + 1), cmds.get(j + 2), cmds.get(j + 3));
                        break;
                    }
                    case "SETPENSIZE": {
                        instr = Instruction::setPenSize;
                        p = instr.execute(p, cmds.get(j + 1));
                        break;
                    }
                    case "REPEAT": {
                        instr = Instruction::repeat;
                        p = instr.execute(p, cmds.get(j));
                        break;
                    }
                }
        }
        return p;
    }

    /**
     * Esegue un' istruzione LOGO nel piano.
     *
     * @param plane il piano in cui eseguire l' istruzione.
     * @param args  la lista degli argomenti di quella specifica istruzione.
     * @return un piano contenente le modifiche dopo aver eseguito l' istruzione.
     */
    Plane<C> execute(Plane<C> plane, Object... args);

}
