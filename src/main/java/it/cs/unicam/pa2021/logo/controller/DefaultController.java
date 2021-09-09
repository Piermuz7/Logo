package it.cs.unicam.pa2021.logo.controller;

import it.cs.unicam.pa2021.logo.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Deque;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

/**
 * Implementazione del controller dell' interfaccia it.cs.unicam.pa2021.logo.Controller.
 * Questo controller viene parametrizzato con Point, parametrizzata a sua volta con Double,
 * per fornire un controller che gestisce punti aventi coordinate in virgola mobile.
 */
public class DefaultController implements Controller<Point<Double>> {

    private static final Logger logger = Logger.getLogger("it.cs.unicam.pa2021.logo.DefaultController");
    private static final Formatter formatter = new SimpleFormatter();
    private static final ConsoleHandler consoleHandler = new ConsoleHandler();
    private Plane<Point<Double>> currentPlane;
    private Deque<Plane<Point<Double>>> previousPlane;
    private Deque<Plane<Point<Double>>> nextPlane;
    private Deque<String> configurationInstructions;
    private List<String> allInstructions;

    @Override
    public void newPlane(double length, double height) {
        this.currentPlane = new DefaultPlane(length, height);
        this.previousPlane = new LinkedList<>();
        this.nextPlane = new LinkedList<>();
        this.configurationInstructions = new LinkedList<>();
        this.allInstructions = new ArrayList<>();
        logger.setLevel(Level.INFO);
        consoleHandler.setFormatter(formatter);
        logger.addHandler(consoleHandler);
    }

    @Override
    public synchronized Plane<Point<Double>> getPlane() {
        return this.currentPlane;
    }

    @Override
    public synchronized boolean hasPrevious() {
        return !this.previousPlane.isEmpty();
    }

    @Override
    public synchronized boolean hasNext() {
        return !this.nextPlane.isEmpty();
    }

    @Override
    public synchronized Plane<Point<Double>> previous() {
        if (this.hasPrevious()) {
            this.nextPlane.addFirst(this.currentPlane);
            this.currentPlane = this.previousPlane.removeFirst();
            this.configurationInstructions.removeLast();
            return this.currentPlane;
        }
        logger.info("No previous plane configuration");
        return null;
    }

    @Override
    public synchronized Plane<Point<Double>> next() {
        if (this.hasNext()) {
            this.previousPlane.addFirst(this.currentPlane);
            this.currentPlane = this.nextPlane.removeFirst();
            this.configurationInstructions.addLast(this.allInstructions.get(configurationInstructions.size()));
            return this.currentPlane;
        }
        logger.info("No next plane configuration");
        return null;
    }

    @Override
    public synchronized void loadInstructions(String filePath) throws IOException {
        if (!Files.exists(Path.of(filePath)))
            logger.severe("Nonexistent file path");
        else {
            List<String> lst = Files.lines(Path.of(filePath))
                    .map(l -> l.split(" "))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toList());
            if (lst.isEmpty()) {
                logger.severe("Empty file!");
                return;
            }
            this.allInstructions.add(lst.get(0));
            if (isNumeric(lst.get(0))) {
                logger.log(Level.SEVERE, "Exception thrown: " + new LOGOSyntaxErrorException("Number is not an istruction!"));
            }
            int j = 0;
            boolean repeat = false;
            for (int i = 1; i < lst.size(); i++) {
                if (isNumeric(lst.get(i)))
                    this.allInstructions.set(j, this.allInstructions.get(j).concat(" " + lst.get(i)));
                else {
                    if (lst.get(i).charAt(0) == '[') {
                        repeat = true;
                        this.allInstructions.set(j, this.allInstructions.get(j).concat(" " + lst.get(i).substring(1)));
                    } else if (lst.get(i).charAt(lst.get(i).length() - 1) == ']') {
                        this.allInstructions.set(j, this.allInstructions.get(j).concat(" " + lst.get(i).substring(0, lst.get(i).length() - 1)));
                        repeat = false;
                    } else if (repeat)
                        this.allInstructions.set(j, this.allInstructions.get(j).concat(" " + lst.get(i)));
                    else {
                        this.allInstructions.add(lst.get(i));
                        j++;
                    }
                }
            }
        }
    }


    @Override
    public List<String> getAllInstructions() {
        return this.allInstructions;
    }

    @Override
    public Deque<String> getConfigurationInstructions() {
        return this.configurationInstructions;
    }

    @Override
    public boolean createLOGOFile(String filepath) throws IOException {
        StringBuilder outputFile = new StringBuilder();
        for (int i = 0; i < filepath.length(); i++) {
            outputFile.append(filepath.charAt(i));
            if (i == filepath.lastIndexOf(".txt") - 1)
                outputFile.append("Output");
        }
        File file = new File(outputFile.toString());
        boolean result = file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        for (Line<Point<Double>> l : this.getPlane().getLines())
            if (!this.currentPlane.getClosedAreas().contains(l))
                fileWriter.write(l + "\n");
        for (ClosedArea<Line<Point<Double>>> a : this.getPlane().getClosedAreas())
            fileWriter.write(a + "\n");
        fileWriter.close();
        if (result)
            logger.info("File created successfully");
        else
            logger.severe("File not created");
        return result;
    }

    private boolean isNumeric(String s) {
        if (s == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public synchronized void execute(String instruction) {
        Plane<Point<Double>> generatedPlane = new DefaultPlane(this.currentPlane);
        Instruction<Point<Double>> instr;
        String[] nameCmd = instruction.split(" ");
        StringBuilder log = new StringBuilder();
        boolean defaultCase = false;
        switch (nameCmd[0]) {
            case "FORWARD": {
                checkLOGOSyntax(nameCmd[0], 2, nameCmd.length);
                instr = Instruction::forward;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1]);
                break;
            }
            case "BACKWARD": {
                checkLOGOSyntax(nameCmd[0], 2, nameCmd.length);
                instr = Instruction::backward;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1]);
                break;
            }
            case "LEFT": {
                checkLOGOSyntax(nameCmd[0], 2, nameCmd.length);
                instr = Instruction::left;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1]);
                break;
            }
            case "RIGHT": {
                checkLOGOSyntax(nameCmd[0], 2, nameCmd.length);
                instr = Instruction::right;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1]);
                break;
            }
            case "CLEARSCREEN": {
                instr = Instruction::clearScreen;
                generatedPlane = instr.execute(this.currentPlane);
                break;
            }
            case "HOME": {
                instr = Instruction::home;
                generatedPlane = instr.execute(this.currentPlane);
                break;
            }
            case "PENUP": {
                instr = Instruction::penUp;
                generatedPlane = instr.execute(this.currentPlane);
                break;
            }
            case "PENDOWN": {
                instr = Instruction::penDown;
                generatedPlane = instr.execute(this.currentPlane);
                break;
            }
            case "SETPENCOLOR": {
                checkLOGOSyntax(nameCmd[0], 4, nameCmd.length);
                instr = Instruction::setPenColor;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1], nameCmd[2], nameCmd[3]);
                break;
            }
            case "SETFILLCOLOR": {
                checkLOGOSyntax(nameCmd[0], 4, nameCmd.length);
                instr = Instruction::setFillColor;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1], nameCmd[2], nameCmd[3]);
                break;
            }
            case "SETSCREENCOLOR": {
                checkLOGOSyntax(nameCmd[0], 4, nameCmd.length);
                instr = Instruction::setScreenColor;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1], nameCmd[2], nameCmd[3]);
                break;
            }
            case "SETPENSIZE": {
                instr = Instruction::setPenSize;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1]);
                break;
            }
            case "REPEAT": {
                List<String> lstCmd = new ArrayList<>(Arrays.asList(nameCmd).subList(2, nameCmd.length));
                instr = Instruction::repeat;
                generatedPlane = instr.execute(this.currentPlane, nameCmd[1], lstCmd);
                break;
            }
            default: {
                defaultCase = true;
                if (!isNumeric(nameCmd[0])) {
                    logger.severe("Nonexistent instruction: " + instruction);
                } else {
                    logger.severe("Number is not an instruction: " + instruction);
                }
                logger.severe("Not executed instruction: " + instruction);
                break;
            }
        }
        if (!defaultCase) {
            for (String s : nameCmd)
                log.append(s).append(" ");
            logger.setUseParentHandlers(false);
            logger.info("Executed instruction: " + log);
            System.out.println("----------------------------------------");
        }
        this.previousPlane.addFirst(this.currentPlane);
        this.currentPlane = generatedPlane;
        this.configurationInstructions.addLast(instruction);
    }

    @Override
    public void executeAll(List<String> instructions) {
        for (String s : instructions)
            execute(s);
    }

    @Override
    public void clear() {
        this.currentPlane = new DefaultPlane(this.currentPlane.getLength(), this.currentPlane.getHeight());
        this.configurationInstructions = new LinkedList<>();
        this.previousPlane = new LinkedList<>();
        this.nextPlane = new LinkedList<>();
        logger.info("All Plane configurations deleted");
    }

    @Override
    public void clearNext() {
        this.nextPlane.clear();
    }


    private void checkLOGOSyntax(String instruction, int expectedSize, int size) {
        if (expectedSize != size)
            logger.log(Level.SEVERE, "Exception thrown: " + new LOGOSyntaxErrorException("Syntax error for " + instruction + " instruction"));
    }

}
