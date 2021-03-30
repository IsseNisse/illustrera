package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;

public class Line extends Shape {
    public Line(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        calculateArea();
    }

    private void calculateArea() {
        double width = getSize();
        int a = (int) (getEndX() - getStartX());
        int b = (int) (getEndY() - getStartY());
        int height = a^2 + b^2;

        super.setArea(width*height);
    }

    public void draw(GraphicsContext gc) {
        gc.setStroke(getStroke());
        gc.setLineWidth(getSize());
        gc.strokeLine(getStartX(), getStartY(), getEndX(), getEndY());
    }
}