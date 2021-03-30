package sample.Shapes;

public class Rectangle extends Shape {

    public Rectangle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    public double getArea() {
        double startX = getStartX();
        double startY = getStartY();
        double endX = getEndX();
        double endY = getEndY();

        double width = startX - endX;
        double height = startY - endY;

        super.setArea(width * height);
        return startX;
    }
}
