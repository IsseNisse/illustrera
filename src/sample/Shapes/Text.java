package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Text extends Shape {
    private final String text;

    public Text(String text) {
        this.text = text;
        type();
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

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineDashes(0, 0, 0, 0, 0);
        gc.setFill(getFill());
        gc.setStroke(getStroke());
        gc.setFont(new Font("Roboto", getSize()));
        gc.strokeText(this.text, getStartX(), getStartY() + getSize(), getWidth());
        gc.fillText(this.text, getStartX(), getStartY() + getSize(), getWidth());
    }
}
