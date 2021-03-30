package sample;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.*;

public class drawController {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker strokeColorPicker;

    @FXML
    private ColorPicker fillColorPicker;

    private static final Stack<Image> savedImages = new Stack<>();
    private final Stack<Image> savedLines = new Stack<>();
    public static ArrayList<javafx.scene.shape.Shape> shapes = new ArrayList<>();
    private ArrayList<Shape> selectedShapes = new ArrayList<>();

    private int size = 10;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.TRANSPARENT;

    private double anchor1X;
    private double anchor1Y;

    private String drawFunction = "drawLine";
    private boolean keyPressed;


    public void draw(javafx.scene.input.MouseEvent mouseEvent) {

        if (savedImages.empty()) {
            makeSnapshot(savedImages);
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        switch (drawFunction) {
            case "drawLine":
                drawLine(gc, mouseX, mouseY, mouseEvent);
                break;
            case "drawSquare":
                drawSquare(gc, mouseX, mouseY, mouseEvent);
                break;
            case "drawCircle":
                drawCircle(gc, mouseX, mouseY, mouseEvent);
                break;
            case "drawTriangle":
                drawTriangle(gc, mouseX, mouseY, mouseEvent);
                break;
            case "select":
                select(mouseX, mouseY, mouseEvent);
                break;
        }
    }


    /* Functions for different types of drawing */

    private void select(double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        if (eventType.getName().equals("MOUSE_PRESSED")) {
            PixelReader pixelReader = savedImages.get(savedImages.size() - 1).getPixelReader();
            Color pixelColor = pixelReader.getColor((int)mouseX, (int)mouseY);
        }
    }

    public void drawLine(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        String shape = "line";
        shapeDraw(gc, mouseX, mouseY, eventType, shape);
    }

    private void drawSquare(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        String shape = "square";
        shapeDraw(gc, mouseX, mouseY, eventType, shape);
    }

    private void drawCircle(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        String shape = "circle";
        shapeDraw(gc, mouseX, mouseY, eventType, shape);
    }

    private void drawTriangle(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        String shape = "triangle";
        shapeDraw(gc, mouseX, mouseY, eventType, shape);
    }


    /* Function for general shape drawing and animation */
    private void shapeDraw(GraphicsContext gc, double mouseX, double mouseY, EventType<? extends MouseEvent> eventType, String shape) {
        double height;
        double width;
        if (!eventType.getName().equals("MOUSE_RELEASED")) {
            if (eventType.getName().equals("MOUSE_PRESSED")) {
                anchor1X = mouseX;
                anchor1Y = mouseY;
                makeSnapshot(savedLines);
            } else if (eventType.getName().equals("MOUSE_DRAGGED")) {
                if (!savedLines.empty()) {
                    Image undoImage = savedLines.get(savedLines.size() - 1);
                    canvas.getGraphicsContext2D().drawImage(undoImage, 0, 0);
                }
                width = mouseX - anchor1X;
                if (keyPressed) {
                    height = width;
                } else {
                    height = mouseY - anchor1Y;
                }
                whichShapeToBeDrawn(gc, mouseX, mouseY, shape, width, height);
            }
        } else {
            width = mouseX - anchor1X;
            if (keyPressed) {
                height = width;
            } else {
                height = mouseY - anchor1Y;
            }

            if (shape.equals("line")) {
                Line line = new Line(anchor1X, anchor1Y, mouseX, mouseY);
                line.setStroke(strokeColor);
                line.setStrokeWidth(size);
                shapes.add(line);
            } else if (shape.equals("circle")) {
                Ellipse ellipse = new Ellipse(anchor1X + width/2, anchor1Y + height/2, width/2, height/2);
                ellipse.setStroke(strokeColor);
                ellipse.setFill(fillColor);
                ellipse.setStrokeWidth(size);
                shapes.add(ellipse);
            } else if (shape.equals("square")) {
                Rectangle rectangle = new Rectangle(anchor1X, anchor1Y, width, height);
                rectangle.setStroke(strokeColor);
                rectangle.setFill(fillColor);
                rectangle.setStrokeWidth(size);
                shapes.add(rectangle);
            }
           whichShapeToBeDrawn(gc, mouseX, mouseY, shape, width, height);

            /* save snapshot */
            makeSnapshot(savedImages);
        }
    }


    /* Switch case to decide which function is calling on shapeDraw */
    private void whichShapeToBeDrawn(GraphicsContext gc, double mouseX, double mouseY, String shape, double width, double height) {
        switch (shape) {
            case "circle":
                makeCircle(gc, width, height);
                break;
            case "square":
                makeSquare(gc, width, height);
                break;
            case "line":
                makeLine(gc, mouseX, mouseY);
                break;
            case "triangle":
                makeTriangle(gc, mouseX, mouseY);
                break;
        }
    }


    /* The functions for actually drawing a shape */
    private void makeLine(GraphicsContext gc, double mouseX, double mouseY) {
        gc.setStroke(strokeColor);
        gc.setLineWidth(size);
        gc.strokeLine(anchor1X, anchor1Y, mouseX, mouseY);
    }

    private void makeSquare(GraphicsContext gc, double width, double height) {
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        gc.setLineWidth(size);
        gc.fillRect(anchor1X, anchor1Y, width, height);
        gc.strokeRect(anchor1X, anchor1Y, width, height);
    }

    private void makeCircle(GraphicsContext gc, double width, double height) {
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        gc.setLineWidth(size);
        gc.fillOval(anchor1X, anchor1Y, width, height);
        gc.strokeOval(anchor1X, anchor1Y, width, height);
    }

    private void makeTriangle(GraphicsContext gc, double mouseX, double mouseY) {
        double[] xArray = {anchor1X, mouseX, anchor1X - (anchor1X - mouseX) * 2};
        double[] yArray = {anchor1Y, mouseY, anchor1Y};
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        gc.setLineWidth(size);
        gc.fillPolygon(xArray, yArray, 3);
        gc.strokePolygon(xArray, yArray, 3);
    }


    /* Make a snapshot function */
    private void makeSnapshot(Stack<Image> savedImages) {
        Image snapshot = canvas.snapshot(null, null);
        savedImages.push(snapshot);
    }


    /* Undo function */
    public void undo(ActionEvent actionEvent) {
        if (!savedImages.empty()) {
            Image undoImage = savedImages.pop();
            canvas.getGraphicsContext2D().drawImage(undoImage, 0, 0);
        }
    }


    /* Get Color Picker value */
    public void strokeColorPicker(ActionEvent actionEvent) {
        Color colorValue = strokeColorPicker.getValue();
        strokeColor = Color.web(colorValue.toString());
    }

    public void fillColorPicker(ActionEvent actionEvent) {
        Color colorValue = fillColorPicker.getValue();
        fillColor = Color.web(colorValue.toString());
    }


    /* Buttons for changing drawing type */

    public void selectBtn(ActionEvent actionEvent) {
        drawFunction = "select";
    }

    public void drawLineBtn(ActionEvent actionEvent) {
        drawFunction = "drawLine";
    }

    public void SquareBtn(ActionEvent actionEvent) {
        drawFunction = "drawSquare";
    }

    public void CircleBtn(ActionEvent actionEvent) {
        drawFunction = "drawCircle";
    }

    public void triangleBtn(ActionEvent actionEvent) {
        drawFunction = "drawTriangle";
    }

    public void eraserBtn(ActionEvent actionEvent) {
        drawFunction = "erase";
    }


    /* Size buttons */

    public void size10(ActionEvent actionEvent) {
        size = 10;
    }

    public void size15(ActionEvent actionEvent) {
        size = 15;
    }

    public void size25(ActionEvent actionEvent) {
        size = 25;
    }

    public void size40(ActionEvent actionEvent) {
        size = 40;
    }

    public void size80(ActionEvent actionEvent) {
        size = 80;
    }


    /* Button Actions */
    public void openBtn(ActionEvent actionEvent) {
        Image image = Controller.openBtn();
        canvas.getGraphicsContext2D().drawImage(image, 0, 0);
    }

    public void saveBtn(ActionEvent actionEvent) {
        makeSnapshot(savedImages);
        Image latestImage = savedImages.lastElement();
        Controller.saveBtn(latestImage);
    }

    public void keyPressed(KeyEvent keyEvent) {
        keyPressed = keyEvent.isShiftDown();
    }

    /* Remove all saved Images */

    public static void emptySavedImages() {
        savedImages.clear();
    }

}
