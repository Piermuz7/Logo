package it.cs.unicam.pa2021.logo;

import it.cs.unicam.pa2021.logo.controller.Controller;
import it.cs.unicam.pa2021.logo.controller.DefaultController;
import it.cs.unicam.pa2021.logo.model.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    // Consegna media
    /*public static void main(String[] args) {
        String filepath = "";
        double length = 2, height = 2;
        int choice;
        Scanner scanner = new Scanner(System.in);
        Controller<Point<Double>> controller = new DefaultController();
        try {
            System.out.println("Insert path of file that contains logo instructions: ");
            if (scanner.hasNextLine())
                filepath = scanner.nextLine();
            System.out.println("Creating plane...");
            System.out.println("Insert plane's width: ");
            if (scanner.hasNextDouble())
                length = scanner.nextDouble();
            System.out.println("Insert plane's height: ");
            if (scanner.hasNextDouble())
                height = scanner.nextDouble();
            controller.newPlane(length, height);
            controller.loadInstructions(filepath);
            System.out.println("\nCurrent plane's configuration:\n" + controller.getPlane());
            System.out.println("Press:\n1 to execute the program step by step" +
                    "\n2 to execute the program and to see instruction after instruction" +
                    " and the final plane's configuration:");
            choice = scanner.nextInt();
            checkChoice(choice);
            if (choice == 1) {
                int conf = 0;
                ListIterator<String> it = controller.getAllInstructions().listIterator();
                String instr = "";
                do {
                    System.out.println("Press:\n0 for previous plane's configuration:" +
                            "\n1 for next plane's configuration:\n2 for exit.");
                    do {
                        conf = scanner.nextInt();
                    } while (conf < 0 || conf > 2);
                    if (conf == 0 && it.hasPrevious() && controller.hasPrevious()) {
                        System.out.println("\nPrevious plane's configuration:\n" + controller.previous());
                        it.previous();
                    } else if (conf == 1 && it.hasNext()) {
                        instr = it.next();
                        controller.execute(instr);
                    }
                    if (conf != 0)
                        System.out.println("\nCurrent plane's configuration:\n" + controller.getPlane());
                    System.out.println();
                } while (it.hasNext() && conf != 2);
            }
            if (choice == 2) {
                for (String instr : controller.getAllInstructions()) {
                    controller.execute(instr);
                    Thread.sleep(5000);
                }
                System.out.println("\nFinal plane's configuration:\n" + controller.getPlane());
            }
            controller.createLOGOFile(filepath);
            scanner.close();
        } catch (InputMismatchException | InterruptedException mismatchException) {
            mismatchException.printStackTrace();
        } catch (IOException ioException) {
            Logger.getGlobal().severe("Error opening file");
            ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            Logger.getGlobal().severe("Invalid input choice");
            illegalArgumentException.printStackTrace();
        }
    }*/

    public static void checkChoice(int choice) {
        if (choice < 1 || choice > 2)
            throw new IllegalArgumentException("Invalid choice: " + choice);
    }
}
