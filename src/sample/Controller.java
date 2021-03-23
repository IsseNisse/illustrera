package sample;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private Canvas canvas;

    private final Color strokeColor = Color.BLACK;
    private int size = 20;
    private double startX;
    private double startY;

    public void draw(MouseEvent mouseEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        if (!eventType.getName().equals("MOUSE_RELEASED")) {
            if (eventType.getName().equals("MOUSE_PRESSED")) {
                startX = mouseX;
                startY = mouseY;
            }
        } else {
            gc.setStroke(strokeColor);
            gc.setLineWidth(size);
            gc.strokeLine(startX, startY, mouseX, mouseY);
        }
    }
}
