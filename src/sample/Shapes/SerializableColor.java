package sample.Shapes;

import java.io.Serializable;
import javafx.scene.paint.Color;

public class SerializableColor implements Serializable {
    private final double red;
    private final double green;
    private final double blue;
    private final double alpha;

    public SerializableColor(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getOpacity();
    }

    public SerializableColor(double red, double green, double blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color getFXColor() {
        return new Color(red, green, blue, alpha);
    }

    public double getAlpha() {
        return this.alpha;
    }
}