package sample;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileWriter;
import java.io.IOException;

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
            createSVGPath(startX, startY, mouseX, mouseY, "line");
        }
    }

    private void createSVGPath(double startX, double startY, double mouseX, double mouseY, String type) {
        if (type.equals("line")) {
            try {
                FileWriter fileWriter = new FileWriter("filename.xml");
                fileWriter.write(" <svg height=\"210\" width=\"500\">\n" +
                        "  <line x1=\"" + startX +"\" y1=\"0\" x2=\"200\" y2=\"200\" style=\"stroke:rgb(255,0,0);stroke-width:2\" />\n" +
                        "</svg> ");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
