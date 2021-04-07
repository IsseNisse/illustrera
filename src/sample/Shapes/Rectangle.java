package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends Shape {

    public Rectangle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        calculateArea();
        type();
    }

    public Rectangle() {
        super();
        calculateArea();
        type();
    }

    @Override
    public void calculateArea() {
        double width = getWidth() - getStartX();
        double height = getHeight() - getStartY();
        super.setArea(width * height);
    }

    private void type() {
        super.setType("Rectangle");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getStroke());
        gc.setFill(getFill());
        gc.setLineWidth(getSize());
        gc.fillRect(getStartX(), getStartY(), getWidth(), getHeight());
        gc.strokeRect(getStartX(), getStartY(), getWidth(), getHeight());
    }
}
