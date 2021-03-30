package sample.Shapes;

import javafx.scene.paint.Color;

import java.util.Objects;

public class Shape {
    private Color fill = Color.TRANSPARENT;
    private Color stroke = Color.BLACK;
    private double size = 10;
    private double area;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String type;

    public Shape(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Color getFill() {
        return fill;
    }

    public void setFill(Color fill) {
        this.fill = fill;
    }

    public Color getStroke() {
        return stroke;
    }

    public void setStroke(Color stroke) {
        this.stroke = stroke;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.area, area) == 0 && Double.compare(shape.startX, startX) == 0 && Double.compare(shape.startY, startY) == 0 && Double.compare(shape.endX, endX) == 0 && Double.compare(shape.endY, endY) == 0 && Objects.equals(fill, shape.fill) && Objects.equals(stroke, shape.stroke);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fill, stroke, area, startX, startY, endX, endY);
    }
}
