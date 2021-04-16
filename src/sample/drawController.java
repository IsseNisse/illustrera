package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.Window;
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

    @FXML
    private TextField textField;

    @FXML
    private GridPane gridPane;

    private final Stack<Image> savedLines = new Stack<>();
    public ArrayList<Shape> shapes = new ArrayList<>();
    public ArrayList<ArrayList<Shape>> latestCreatedShapesList = new ArrayList<>();
    private final ArrayList<Shape> selectedShapes = new ArrayList<>();

    private double size = 10;
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
        if (mouseEvent.getButton().toString().equals("PRIMARY")) {
            Line line = new Line();
            drawAllShapes(gc);
            shapeDraw(gc, mouseX, mouseY, eventType, line);
        }
    }

    private void drawSquare(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        if (mouseEvent.getButton().toString().equals("PRIMARY")) {
            Rectangle rec = new Rectangle();
            shapeDraw(gc, mouseX, mouseY, eventType, rec);
        }
    }

    private void drawCircle(GraphicsContext gc, double mouseX, double mouseY, MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        if (mouseEvent.getButton().toString().equals("PRIMARY")) {
            Circle circle = new Circle();
            shapeDraw(gc, mouseX, mouseY, eventType, circle);
        }
    }

//    private void drawTriangle(double mouseX, double mouseY, MouseEvent mouseEvent) {
//        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
//        String shape = "triangle";
//        shapeDraw(mouseX, mouseY, eventType, shape);
//    }

    private void select(double mouseX, double mouseY, MouseEvent mouseEvent, GraphicsContext gc) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        if (mouseEvent.getButton().toString().equals("PRIMARY")) {
            if (!eventType.getName().equals("MOUSE_RELEASED")) {
                if (eventType.getName().equals("MOUSE_PRESSED")) {
                    for (Shape shape : shapes) {
                        if (shape.getType().equals("Line")) {
                            lineClickDetection(mouseX, mouseY, shape);
                        } else {
                            shapeClickDetection(mouseX, mouseY, shape);
                        }
                    }

                    if (selectedShapes.size() == 1) {
                        selectedShape = selectedShapes.get(0);
                    } else if (selectedShapes.size() > 1){
                        selectedShape = selectedShapes.get(selectedShapes.size() - 1);
                    }

                }
            } else if (!selectedShapes.isEmpty()){

                selectedShape = changeShapePos(mouseX, mouseY);
                drawAllShapes(gc);
                selectedShape.drawSelection(gc);

                selectedShapes.clear();
            }
        } else if (mouseEvent.getButton().toString().equals("SECONDARY")) {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem moveToBack = new MenuItem("Move To back");
            MenuItem moveBack = new MenuItem("Move Back");
            MenuItem moveToFront = new MenuItem("Move To Front");
            MenuItem moveForward = new MenuItem("Move To Forward");
            contextMenu.getItems().addAll(moveToBack, moveBack, moveToFront, moveForward);
            TextField textField = new TextField();
            textField.setContextMenu(contextMenu);
            contextMenu.show(canvas, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            moveToBack.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    shapes.remove(selectedShape);
                    shapes.add(0, selectedShape);
                    drawAllShapes(gc);
                }
            });
            moveBack.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int selectedShapeIndex = shapes.indexOf(selectedShape);
                    Shape movedShape = shapes.get(selectedShapeIndex - 1);
                    shapes.set(selectedShapeIndex - 1, selectedShape);
                    shapes.set(selectedShapeIndex, movedShape);
                    drawAllShapes(gc);
                }
            });
            moveToFront.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    shapes.remove(selectedShape);
                    shapes.add(shapes.size(), selectedShape);
                    drawAllShapes(gc);
                }
            });
            moveForward.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int selectedShapeIndex = shapes.indexOf(selectedShape);
                    Shape movedShape = shapes.get(selectedShapeIndex + 1);
                    shapes.set(selectedShapeIndex + 1, selectedShape);
                    shapes.set(selectedShapeIndex, movedShape);
                    drawAllShapes(gc);
                }
            });
        }
    }

    private void lineClickDetection(double mouseX, double mouseY, Shape shape) {
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
    }

    private void shapeClickDetection(double mouseX, double mouseY, Shape shape) {
        if (mouseX >= shape.getStartX() && mouseX <= (shape.getWidth() + shape.getStartX())) {
            if (mouseY >= shape.getStartY() && mouseY <= (shape.getHeight() + shape.getStartY())) {
                if (!selectedShapes.isEmpty()) {
                    selectedShapes.clear();
                }
                selectedShapes.add(shape);
                mouseXStart = mouseX;
                mouseYStart = mouseY;
            }
        }
    }

    private Shape changeShapePos(double mouseX, double mouseY) {
        double yDifference;
        double xDifference;
        int shapeIndex = shapes.indexOf(selectedShape);
        double xRelease;
        double yRelease;

        xRelease = mouseX;
        yRelease = mouseY;

        ArrayList<Shape> latestShapes = new ArrayList<>(shapes);
        latestCreatedShapesList.add(latestShapes);
        xDifference = xRelease - mouseXStart;
        yDifference = yRelease - mouseYStart;
        Shape shape = shapes.remove(shapeIndex);
        Shape newPos = new Shape();
        switch (shape.getType()) {
            case "Line":
                newPos = new Line(shape);
                break;
            case "Rectangle":
                newPos = new Rectangle(shape);
                break;
            case "Circle":
                newPos = new Circle(shape);
                break;
        }
        if (shape.getType().equals("Line")) {
            newPos.setStartX(shape.getStartX() + xDifference);
            newPos.setStartY(shape.getStartY() + yDifference);
            newPos.setWidth(shape.getWidth() + xDifference);
            newPos.setHeight(shape.getHeight() + yDifference);
            shapes.add(shapeIndex, newPos);
        } else {
            newPos.setStartX(shape.getStartX() + xDifference);
            newPos.setStartY(shape.getStartY() + yDifference);
            shapes.add(shapeIndex, newPos);
        }
        System.out.println(newPos.getType());
        drawAllShapes(canvas.getGraphicsContext2D());

        return newPos;
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
            ArrayList<Shape> latestShapes = new ArrayList<>(shapes);
            latestCreatedShapesList.add(latestShapes);
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
            shapes = latestCreatedShapesList.remove(latestCreatedShapesList.size() - 1);
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

//    public void triangleBtn(ActionEvent actionEvent) {
//        drawFunction = "drawTriangle";
//    }

    /* Size buttons */

    public void size10(ActionEvent actionEvent) {
        size = 10;
        textField.setText(size + "px");
    }

    public void size15(ActionEvent actionEvent) {
        size = 15;
        textField.setText(size + "px");
    }

    public void size25(ActionEvent actionEvent) {
        size = 25;
        textField.setText(size + "px");
    }

    public void size40(ActionEvent actionEvent) {
        size = 40;
        textField.setText(size + "px");
    }

    public void size80(ActionEvent actionEvent) {
        size = 80;
        textField.setText(size + "px");
    }

    public void sizeCustom(ActionEvent actionEvent) {
        String inputText = textField.getText();
        int index = inputText.length();
        if (inputText.contains("px")) {
            index = inputText.indexOf("p");
        }
        String sizeString = inputText.substring(0, index);
        size = Integer.parseInt(sizeString);
        if (size > 899) {
            size = 899;
        }
        textField.setText(size + "px");
    }

    /* Button Actions */
    public void openBtn(ActionEvent actionEvent) {
        try {
            shapes = Controller.openBtn();
            drawAllShapes(canvas.getGraphicsContext2D());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBtn(ActionEvent actionEvent) {
        Controller.saveBtn(shapes);
    }

    public void keyPressed(KeyEvent keyEvent) {
        keyPressed = keyEvent.isShiftDown();
    }

    public void drawAllShapes(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }
}
