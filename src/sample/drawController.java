package sample;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
//import javafx.scene.shape.*;
import sample.Shapes.*;

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
    public static ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Shape> selectedShapes = new ArrayList<>();

    private int size = 10;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.TRANSPARENT;

    private double anchor1X;
    private double anchor1Y;
    private double mouseXStart = 0;
    private double mouseYStart = 0;

    private String drawFunction = "drawLine";
    private boolean keyPressed;

    private Shape selectedShape = null;


    public void draw(javafx.scene.input.MouseEvent mouseEvent) {

        if (savedImages.empty()) {
            makeSnapshot(savedImages);
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        drawAllShapes(gc);

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
//            case "drawTriangle":
//                drawTriangle(mouseX, mouseY, mouseEvent);
//                break;
            case "select":
                select(mouseX, mouseY, mouseEvent, gc);
                break;
        }
    }


    /* Functions for different types of drawing */

    public void drawLine(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        Line line = new Line();
        drawAllShapes(gc);
        shapeDraw(gc, mouseX, mouseY, eventType, line);
    }

    private void drawSquare(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        Rectangle rec = new Rectangle();
        shapeDraw(gc, mouseX, mouseY, eventType, rec);
    }

    private void drawCircle(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        Circle circle = new Circle();
        shapeDraw(gc, mouseX, mouseY, eventType, circle);
    }

//    private void drawTriangle(double mouseX, double mouseY, MouseEvent mouseEvent) {
//        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
//        String shape = "triangle";
//        shapeDraw(mouseX, mouseY, eventType, shape);
//    }

    private void select(double mouseX, double mouseY, MouseEvent mouseEvent, GraphicsContext gc) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        int shapeIndex;
        double xDifference;
        double yDifference;
        if (!eventType.getName().equals("MOUSE_RELEASED")) {
            if (eventType.getName().equals("MOUSE_PRESSED")) {
                for (Shape shape : shapes) {
                    if (mouseX >= shape.getStartX() && mouseX <= (shape.getEndX() + shape.getStartX())) {
                        if (mouseY >= shape.getStartY() && mouseY <= (shape.getEndY() + shape.getStartY())) {
                            if (!selectedShapes.isEmpty()) {
                                selectedShapes.clear();
                            }
                            selectedShapes.add(shape);
                            mouseXStart = mouseX;
                            mouseYStart = mouseY;
                        } else {
                            System.out.println("Nope");
                        }
                    } else {
                        System.out.println("Nope");
                    }
                }

                if (selectedShapes.size() == 1) {
                    selectedShape = selectedShapes.get(0);
                } else if (selectedShapes.size() > 1){
                    selectedShape = selectedShapes.get(selectedShapes.size() - 1);
                }

                System.out.println(selectedShape);

            }
        } else if (!selectedShapes.isEmpty()){

            shapeIndex = shapes.indexOf(selectedShape);
            double xRelease;
            double yRelease;

            xRelease = mouseX;
            yRelease = mouseY;

            xDifference = xRelease - mouseXStart;
            yDifference = yRelease - mouseYStart;
            Shape shape = shapes.get(shapeIndex);
            shape.setStartX(shape.getStartX() + xDifference);
            shape.setStartY(shape.getStartY() + yDifference);
            drawAllShapes(gc);

            selectedShapes.clear();
        }
    }


    /* Function for general shape drawing and animation */
    private void shapeDraw(GraphicsContext gc, double mouseX, double mouseY, EventType<? extends MouseEvent> eventType, Shape shape) {
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
                    if (shape.getType().equals("Line")) {
                        width = mouseX;
                        height = mouseY;
                    }
                }

                editAndDrawShape(gc, shape, height, width);
            }
        } else {
            width = mouseX - anchor1X;
            if (keyPressed) {
                height = width;
            } else {
                height = mouseY - anchor1Y;
                if (shape.getType().equals("Line")) {
                    width = mouseX;
                    height = mouseY;
                }
            }

            editAndDrawShape(gc, shape, height, width);
            shapes.add(shape);

            /* save snapshot */
            makeSnapshot(savedImages);
        }
    }

    private void editAndDrawShape(GraphicsContext gc, Shape shape, double height, double width) {
        shape.setStartX(anchor1X);
        shape.setStartY(anchor1Y);
        shape.setEndX(width);
        shape.setEndY(height);
        shape.setFill(fillColor);
        shape.setStroke(strokeColor);
        shape.setSize(size);
        shape.draw(gc);
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

    public void drawAllShapes(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }

}
