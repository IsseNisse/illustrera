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

    public Circle(Shape shape) {
        setFill(shape.getFill());
        setStroke(shape.getStroke());
        setSize(shape.getSize());
        setArea(shape.getArea());
        setStartX(shape.getStartX());
        setStartY(shape.getStartY());
        setWidth(shape.getWidth());
        setHeight(shape.getHeight());
        setCenterX(shape.getCenterX());
        setCenterY(shape.getCenterY());
        setType(shape.getType());
    }

    @Override
    public void calculateArea() {
        double radius1 = (getWidth() - getStartX())/2;
        double radius2 = (getHeight() - getStartY())/2;

        double area = Math.PI * radius1 * radius2;
        super.setArea(area);
    }

    @Override
    public double getCenterX() {
        return getStartX() + (getWidth()/2);
    }

    public double getXRadius() {
        return getWidth()/2;
    }

    public double getYRadius() {
        return getHeight()/2;
    }

    @Override
    public double getCenterY() {
        return getStartY() + (getHeight()/2);
    }

    private void type() {
        super.setType("Circle");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getStroke());
        gc.setFill(getFill());
        gc.setLineWidth(getSize());
        gc.fillOval(getStartX(), getStartY(), getWidth(), getHeight());
        gc.strokeOval(getStartX(), getStartY(), getWidth(), getHeight());
    }
}
