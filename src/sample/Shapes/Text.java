package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Text extends Shape {
    private final String text;
    private double lineWidth = 1;

    public Text(String text) {
        this.text = text;
        setTextWidth();
        type();
    }

    private void setTextWidth() {
        javafx.scene.text.Text textBox = new javafx.scene.text.Text(getStartX(), getStartY(), this.text);
        textBox.setFont(new Font("Roboto", getSize()));
        double textWidth = textBox.getLayoutBounds().getWidth();
        setWidth(textWidth);
    }

    public Text(Shape shape, String text) {
        this.text = text;
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

    public void type() {
        super.setType("Text");
    }

    @Override
    public String getText() {
        return this.text;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        setTextWidth();
        gc.setLineDashes(0, 0, 0, 0, 0);
        gc.setFill(getFill());
        gc.setStroke(getStroke());
        gc.setFont(new Font("Roboto", getSize()));
        gc.setLineWidth(lineWidth);
        gc.strokeText(this.text, getStartX(), getStartY() + getSize(), getWidth());
        gc.fillText(this.text, getStartX(), getStartY() + getSize(), getWidth());
    }
}
