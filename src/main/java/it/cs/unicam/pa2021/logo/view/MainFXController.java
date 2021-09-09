package it.cs.unicam.pa2021.logo.view;


import it.cs.unicam.pa2021.logo.controller.Controller;
import it.cs.unicam.pa2021.logo.controller.DefaultController;
import it.cs.unicam.pa2021.logo.model.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * View per presentare i contenuti del model.
 */
public class MainFXController implements PlaneUpdateListener<Point<Double>> {

    private final Controller<Point<Double>> controller = new DefaultController();
    @FXML
    public MenuItem loadFile;
    @FXML
    public Pane textAreaPane;
    @FXML
    public TextArea instructionArea;
    @FXML
    public Pane bottomPane;
    @FXML
    public Pane planePane;
    @FXML
    public Button playButton;
    @FXML
    public TextField instructionTextField;
    @FXML
    public Button previousButton;
    @FXML
    public Button nextButton;
    @FXML
    public Button executeButton;

    private List<Integer> executeTextFieldInstructions; // Istruzioni eseguite nella textfield da rimuovere dalla lista di tutte le istruzioni
    private int i; // Contatore istruzione attuale

    @FXML
    public void initialize() {
        i = 0;
        setCartesianCoordinates();
        createPlanePane();
        createRectangle();
        createCursor();
        bottomPane.setVisible(false);
        previousButton.setDisable(true);
        executeButton.setDisable(true);
        instructionArea.setEditable(false);
        instructionArea.setStyle("-fx-font-weight: bold");
        instructionArea.positionCaret(instructionArea.getLength());
        controller.getPlane().addPlaneUpdateListener(this);
        executeTextFieldInstructions = new ArrayList<>();
    }

    private void createPlanePane() {
        controller.newPlane(700, 400);
        planePane.setMinHeight(400);
        planePane.setMaxHeight(400);
        planePane.setMinWidth(700);
        planePane.setMaxWidth(700);
        this.planePane.setStyle("-fx-border-color: black");
    }

    private void createRectangle() {
        Rectangle rectangle = new Rectangle(planePane.getMaxWidth(), planePane.getMaxHeight());
        rectangle.setFill(Color.rgb(controller.getPlane().getBackgroundColor().getRed(), controller.getPlane().getBackgroundColor().getGreen(), controller.getPlane().getBackgroundColor().getBlue()));
        planePane.getChildren().add(0, rectangle);
    }

    private void createCursor() {
        final URL url = getClass().getResource("/logoTurtleCursor.png");
        final Image imageCursor = new Image(url.toString());
        ImagePattern imagePattern = new ImagePattern(imageCursor);
        Circle cursor = new Circle(controller.getPlane().getHome().getX(), controller.getPlane().getHome().getY(), 20);
        cursor.setFill(imagePattern);
        planePane.getChildren().add(1, cursor);
    }

    private void setCartesianCoordinates() {
        Scale scale = new Scale();
        scale.setX(1);
        scale.setY(-1);
        scale.pivotYProperty().bind(Bindings.createDoubleBinding(() ->
                        planePane.getBoundsInLocal().getMinY() + planePane.getBoundsInLocal().getHeight() / 2,
                planePane.boundsInLocalProperty()));
        planePane.getTransforms().add(scale);
    }

    public void loadLOGOFile(ActionEvent actionEvent) throws IOException {
        this.reset(actionEvent);
        controller.getAllInstructions().clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        controller.loadInstructions(selectedFile.getAbsolutePath());
        bottomPane.setVisible(true);
    }

    public void startExecution(ActionEvent actionEvent) {
        StringBuilder text = new StringBuilder();
        while (i < this.controller.getAllInstructions().size()) {
            controller.execute(controller.getAllInstructions().get(i));
            text.append(controller.getAllInstructions().get(i)).append("\n");
            i++;
        }
        instructionArea.appendText(text.toString());
        playButton.setDisable(true);
        previousButton.setDisable(false);
        if (i == controller.getAllInstructions().size())
            nextButton.setDisable(true);
    }

    @Override
    public void fireMovedCursor(Point<Double> point) {
        Circle circle = (Circle) planePane.getChildren().get(1);
        circle.setCenterX(point.getX());
        circle.setCenterY(point.getY());
    }

    @Override
    public void fireGeneratedLine(Line<Point<Double>> line) {
        javafx.scene.shape.Line lineFX = new javafx.scene.shape.Line(line.getStartingPoint().getX(), line.getStartingPoint().getY(), line.getEndPoint().getX(), line.getEndPoint().getY());
        lineFX.setStroke(Color.rgb(line.getColor().getRed(), line.getColor().getGreen(), line.getColor().getBlue()));
        lineFX.setStrokeWidth(line.getSize());
        planePane.getChildren().add(lineFX);
        fireMovedCursor(line.getEndPoint());
    }

    @Override
    public void fireGeneratedArea(ClosedArea<Line<Point<Double>>> area) {
        int p = 0;
        double[] points = new double[area.getArea().size() * 4];
        for (Line<Point<Double>> l : area.getArea()) {
            points[p] = l.getStartingPoint().getX();
            p++;
            points[p] = l.getStartingPoint().getY();
            p++;
            points[p] = l.getEndPoint().getX();
            p++;
            points[p] = l.getEndPoint().getY();
            p++;
        }
        Polygon polygon = new Polygon(points);
        polygon.setFill(Color.rgb(area.getColor().getRed(), area.getColor().getGreen(), area.getColor().getBlue()));
        planePane.getChildren().add(polygon);
    }

    @Override
    public void fireScreenColor(RGBColor color) {
        Rectangle rectangle = new Rectangle(planePane.getWidth(), planePane.getHeight());
        rectangle.setFill(Color.rgb(color.getRed(), color.getGreen(), color.getBlue()));
        planePane.getChildren().set(0, rectangle);
    }

    @Override
    public void fireScreenCleaned() {
        planePane.getChildren().remove(2, planePane.getChildren().size());
        controller.clearNext();
    }

    public void previousPlane(ActionEvent actionEvent) {
        if (controller.hasPrevious()) {
            i--;
            nextButton.setDisable(false);
            playButton.setDisable(false);
            configurationPlane(controller.previous());
            appendConfigurationInstructions();
            if (!controller.hasPrevious())
                previousButton.setDisable(true);
        }
    }

    public void nextPlane(ActionEvent actionEvent) {
        previousButton.setDisable(false);
        if (controller.hasNext())
            configurationPlane(controller.next());
        else if (i < this.controller.getAllInstructions().size())
            controller.execute(controller.getAllInstructions().get(i));
        i++;
        appendConfigurationInstructions();
        if (i == controller.getAllInstructions().size()) {
            nextButton.setDisable(true);
            playButton.setDisable(true);
        }
    }

    private void appendConfigurationInstructions() {
        StringBuilder text = new StringBuilder();
        if (!controller.getConfigurationInstructions().isEmpty())
            for (String s : controller.getConfigurationInstructions())
                text.append(s).append("\n");
        instructionArea.setText(text.toString());
        instructionArea.positionCaret(instructionArea.getLength());
    }

    private void configurationPlane(Plane<Point<Double>> plane) {
        planePane.getChildren().remove(2, planePane.getChildren().size());
        fireMovedCursor(plane.getCursorPosition());
        fireScreenColor(plane.getBackgroundColor());
        for (Line<Point<Double>> l : plane.getLines())
            fireGeneratedLine(l);
        for (ClosedArea<Line<Point<Double>>> a : plane.getClosedAreas())
            fireGeneratedArea(a);
    }

    public void reset(ActionEvent actionEvent) {
        i = 0;
        planePane.getChildren().remove(2, planePane.getChildren().size());
        controller.getPlane().removePlaneUpdateListener(this);
        controller.clear();
        fireMovedCursor(controller.getPlane().getHome());
        fireScreenColor(controller.getPlane().getBackgroundColor());
        instructionArea.setText("");
        for (Integer executeTextFieldInstruction : executeTextFieldInstructions)
            this.controller.getAllInstructions().set(executeTextFieldInstruction, "");
        this.executeTextFieldInstructions.clear();
        playButton.setDisable(false);
        previousButton.setDisable(true);
        nextButton.setDisable(false);
        controller.getPlane().addPlaneUpdateListener(this);
    }

    public void exit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/logoIcon.jpg").toString()));
        alert.setTitle("Confirmation exit");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to close this window?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            Platform.exit();
    }

    public void executeAction(ActionEvent actionEvent) {
        this.executeTextFieldInstructions.add(i);
        this.controller.getAllInstructions().add(i, this.instructionTextField.getText());
        this.nextPlane(actionEvent);
        this.instructionTextField.setText("");
        this.executeButton.setDisable(true);
    }

    public void filledTextField(ActionEvent actionEvent) {
        if (!this.instructionTextField.getText().isEmpty())
            this.executeButton.setDisable(false);
    }

    public void aboutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setHeight(500);
        stage.setWidth(500);
        stage.getIcons().add(new Image(getClass().getResource("/logoIcon.jpg").toString()));
        alert.setTitle("How to do");
        alert.setHeaderText("");
        alert.setContentText("First of all, you must load a file.txt that contains " +
                "logo instructions in the menu bar: File -> Load file." +
                "\nAs soon as the file is upload you can execute an instruction with previous," +
                " next and play buttons." +
                "\nPlay button let you see the final result after all instructions." +
                "\nIf you want execute an instruction out of file, you can digit the instruction in the " +
                "text-field near the execute button." +
                "\nWhen you finished to write your instruction, " +
                "you must press Enter to enable the execute button.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            alert.close();
    }
}
