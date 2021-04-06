package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Objects;

public class Shape implements java.io.Serializable {
    private Color fill = Color.TRANSPARENT;
    private Color stroke = Color.BLACK;
    private double size = 10;
    private double area;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double centerX;
    private double centerY;
    private double width;
    private double height;
    private String type;

    public Shape(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Shape() {
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
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

    public double getCenterX() {
        centerX = width/2;
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        centerY = height/2;
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getWidth() {
        width = endX - startX;
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        height = endY - startY;
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
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


    public void draw(GraphicsContext gc) {
        gc.setStroke(stroke);
    }

    public void calculateArea() {
        setArea(width * height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.size, size) == 0 && Double.compare(shape.area, area) == 0 && Double.compare(shape.startX, startX) == 0 && Double.compare(shape.startY, startY) == 0 && Double.compare(shape.endX, endX) == 0 && Double.compare(shape.endY, endY) == 0 && Double.compare(shape.centerX, centerX) == 0 && Double.compare(shape.centerY, centerY) == 0 && Double.compare(shape.width, width) == 0 && Double.compare(shape.height, height) == 0 && Objects.equals(fill, shape.fill) && Objects.equals(stroke, shape.stroke) && Objects.equals(type, shape.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fill, stroke, size, area, startX, startY, endX, endY, centerX, centerY, width, height, type);
    }
}
