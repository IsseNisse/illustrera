package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends Shape {

    public Rectangle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        calculateArea();
    }

    private void calculateArea() {
        double width = getStartX() - getEndX();
        double height = getStartY() - getEndY();

        super.setArea(width * height);
    }

    public void draw(GraphicsContext gc) {
        gc.setStroke(getStroke());
        gc.setFill(getFill());
        gc.setLineWidth(getSize());
        gc.fillRect(getStartX(), getStartY(), getEndX(), getEndY());
        gc.strokeRect(getStartX(), getStartY(), getEndX(), getEndY());
    }
}
