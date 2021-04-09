package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Shape implements java.io.Serializable {
    private SerializableColor fill = new SerializableColor(Color.TRANSPARENT);
    private SerializableColor stroke = new SerializableColor(Color.BLACK);
    private double size = 10;
    private double area;
    private double startX;
    private double startY;
    private double width;
    private double height;
    private double centerX;
    private double centerY;
    private String type;

    public Shape(double startX, double startY, double width, double height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public Shape() {
        this.startX = 0;
        this.startY = 0;
        this.width = 0;
        this.height = 0;
    }

    public Color getFill() {
        return fill.getFXColor();
    }

    public void setFill(Color fill) {
        this.fill = new SerializableColor(fill);
    }

    public Color getStroke() {
        return stroke.getFXColor();
    }

    public void setStroke(Color stroke) {
        this.stroke = new SerializableColor(stroke);
    }

    public double getFillOpacity() {
        return fill.getAlpha();
    }

    public double getStrokeOpacity() {
        return stroke.getAlpha();
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
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

    public double getHypotenuse() {
        int a = (int) (getWidth());
        int b = (int) (getHeight());
        int height = a^2 + b^2;
        return Math.sqrt(height);
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
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
        gc.setStroke(stroke.getFXColor());
    }

    public void calculateArea() {
        setArea(width * height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.size, size) == 0 && Double.compare(shape.area, area) == 0 && Double.compare(shape.startX, startX) == 0 && Double.compare(shape.startY, startY) == 0 && Double.compare(shape.width, width) == 0 && Double.compare(shape.height, height) == 0 && Double.compare(shape.centerX, centerX) == 0 && Double.compare(shape.centerY, centerY) == 0 && Double.compare(shape.width, width) == 0 && Double.compare(shape.height, height) == 0 && Objects.equals(fill, shape.fill) && Objects.equals(stroke, shape.stroke) && Objects.equals(type, shape.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fill, stroke, size, area, startX, startY, width, height, centerX, centerY, width, height, type);
    }
}
