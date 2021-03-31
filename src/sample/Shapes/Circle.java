package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;

public class Circle extends Shape {
    public Circle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        calculateArea();
        type();
    }

    public Circle() {
        super();
        calculateArea();
        type();
    }

    public void calculateArea() {
        double radius1 = (getEndX() - getStartX())/2;
        double radius2 = (getEndY() - getStartY())/2;

        double area = Math.PI * radius1 * radius2;
        super.setArea(area);
    }

    private void type() {
        super.setType("Circle");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getStroke());
        gc.setFill(getFill());
        gc.setLineWidth(getSize());
        gc.fillOval(getStartX(), getStartY(), getEndX(), getEndY());
        gc.strokeOval(getStartX(), getStartY(), getEndX(), getEndY());
    }
}
