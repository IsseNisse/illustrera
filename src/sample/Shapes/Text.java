package sample.Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Text extends Shape {
    private String text;

    public Text(String text) {
        this.text = text;
        type();
    }

    public void type() {
        super.setType("Text");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getFill());
        gc.setStroke(getStroke());
        gc.setFont(new Font("Roboto", 20));
        gc.strokeText(this.text, getStartX(), getStartY(), getWidth());
        gc.fillText(this.text, getStartX(), getStartY(), getWidth());
    }
}
