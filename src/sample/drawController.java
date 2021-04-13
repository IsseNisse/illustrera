package sample;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import sample.Shapes.*;

import java.io.IOException;
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
                    if (shape.getType().equals("Line")) {
                        double tilt = Math.atan((shape.getWidth() - shape.getStartX())/(shape.getHeight() - shape.getStartY()));
                        Point2D mousePos = new Point2D(mouseX, mouseY);
                        javafx.scene.shape.Line line = new javafx.scene.shape.Line(shape.getStartX(), shape.getStartY(), shape.getWidth(), shape.getHeight());
                        line.setStrokeWidth(shape.getSize());
                        line.getTransforms().add(new Rotate(Math.toDegrees(tilt), line.getStartX(), line.getStartY()));
                        if (line.contains(mousePos)) {
                            if (!selectedShapes.isEmpty()) {
                                selectedShapes.clear();
                            }
                            selectedShapes.add(shape);
                            mouseXStart = mouseX;
                            mouseYStart = mouseY;
                        }
                    } else {
                        if (mouseX >= shape.getStartX() && mouseX <= (shape.getWidth() + shape.getStartX())) {
                            if (mouseY >= shape.getStartY() && mouseY <= (shape.getHeight() + shape.getStartY())) {
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
                }

                if (selectedShapes.size() == 1) {
                    selectedShape = selectedShapes.get(0);
                } else if (selectedShapes.size() > 1){
                    selectedShape = selectedShapes.get(selectedShapes.size() - 1);
                }

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
            if (shape.getType().equals("Line")) {
                shape.setStartX(shape.getStartX() + xDifference);
                shape.setStartY(shape.getStartY() + yDifference);
                shape.setWidth(shape.getWidth() + xDifference);
                shape.setHeight(shape.getHeight() + yDifference);
            } else {
                shape.setStartX(shape.getStartX() + xDifference);
                shape.setStartY(shape.getStartY() + yDifference);
            }
            drawAllShapes(gc);
            shape.drawSelection(gc);

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
        }
    }

    private void editAndDrawShape(GraphicsContext gc, Shape shape, double height, double width) {
        shape.setStartX(anchor1X);
        shape.setStartY(anchor1Y);
        shape.setWidth(width);
        shape.setHeight(height);
        shape.setFill(fillColor);
        shape.setStroke(strokeColor);
        shape.setSize(size);
        shape.draw(gc);
    }


    /* Undo function */
    public void undo(ActionEvent actionEvent) {
        if (!shapes.isEmpty()) {
            shapes.remove(shapes.size() - 1);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            drawAllShapes(gc);
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
        try {
            Image image = Controller.openBtn();
            if (image != null) {
                canvas.getGraphicsContext2D().drawImage(image, 0, 0);
            } else {
                drawAllShapes(canvas.getGraphicsContext2D());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBtn(ActionEvent actionEvent) {
        Controller.saveBtn();
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
