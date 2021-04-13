package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shape {
    public Line(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        calculateArea();
        type();
    }

    public Line() {
        super();
        calculateArea();
        type();

    }

    @Override
    public void calculateArea() {
        double width = getSize();
        int a = (int) (getWidth() - getStartX());
        int b = (int) (getHeight() - getStartY());
        int height = a^2 + b^2;

        super.setArea(width*Math.sqrt(height));
    }

    @Override
    public double getHypotenuse() {
        int a = (int) (getWidth() - getStartX());
        int b = (int) (getHeight() - getStartY());
        double height = Math.pow(a, 2) + Math.pow(b, 2);
        return Math.sqrt(height);
    }

    private void type() {
        super.setType("Line");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getStroke());
        gc.setLineWidth(getSize());
        gc.strokeLine(getStartX(), getStartY(), getWidth(), getHeight());
    }

    @Override
    public void drawSelection(GraphicsContext gc) {
        final double[] dashPattern = {4, 4, 4, 4, 4};
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.setLineDashes(dashPattern);
        gc.strokeRect(getStartX() - getSize()/2 - 20, getStartY() - getSize()/2 - 20, getWidth() - getStartX() + getSize() + 35, getHeight() - getStartY() + getSize() + 35);
    }
}
