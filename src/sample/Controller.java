package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import sample.Shapes.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    public static ArrayList<Shape> openBtn() throws IOException {
        ArrayList<Shape> shapes = new ArrayList<>();
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("sample.fxml"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter iluFilter = new FileChooser.ExtensionFilter("Illustrera (*.ilu)", "*.ilu");
        FileChooser.ExtensionFilter svgFilter = new FileChooser.ExtensionFilter("SVG (*.svg)", "*.svg");
        fileChooser.getExtensionFilters().add(iluFilter);
        fileChooser.getExtensionFilters().add(svgFilter);
        File file = fileChooser.showOpenDialog(loader.getRoot());

        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No File Selected");
            alert.setHeaderText("No File Selected");
            alert.showAndWait().ifPresent(rs -> {
            });
        } else {
            if (getFileExtension(file).equals(".svg")) {
                shapes = svgToShapesConverter(file);
            } else if (getFileExtension(file).equals(".ilu")) {
                FileInputStream fileIn;
                ObjectInputStream in;
                try {
                    fileIn = new FileInputStream(file);
                    in = new ObjectInputStream(fileIn);
                    shapes = (ArrayList<Shape>) in.readObject();
                    in.close();
                    fileIn.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return shapes;
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public static ArrayList<Shape> svgToShapesConverter(File file) {
        ArrayList<Shape> svgShapes = new ArrayList<>();

        try {
            Scanner svgReader = new Scanner(file);
            while (svgReader.hasNextLine()) {
                String nextLine = svgReader.nextLine();
                if (nextLine.contains("line")) {
                    String[] searchString = {"x1", "y1", "x2", "y2"};
                    double[] values = new double[searchString.length];
                    for (int i = 0; i < searchString.length; i++) {
                        int valueIndex = nextLine.indexOf(searchString[i]);
                        int quoteIndex = nextLine.indexOf("\"", valueIndex + 4);
                        double value = Double.parseDouble(nextLine.substring(valueIndex + 4, quoteIndex));
                        values[i] = value;
                    }

                    String[] styleValues = getStyleValues(nextLine);

                    Line line = new Line(values[0], values[1], values[2], values[3]);
                    line.setStroke(Color.web(("0x" + styleValues[1]), Double.parseDouble(styleValues[4])));
                    line.setFill(Color.web(("0x" + styleValues[0]), Double.parseDouble(styleValues[3])));
                    line.setSize(Double.parseDouble(styleValues[2]));

                    svgShapes.add(line);

                } else if (nextLine.contains("rect")) {
                    String[] searchString = {"x", "y"};
                    double[] values = new double[searchString.length];
                    for (int i = 0; i < searchString.length; i++) {
                        int valueIndex = nextLine.indexOf(searchString[i]);
                        int quoteIndex = nextLine.indexOf("\"", valueIndex + 3);
                        double value = Double.parseDouble(nextLine.substring(valueIndex + 3, quoteIndex));
                        values[i] = value;
                    }

                    int widthIndex = nextLine.indexOf("width=");
                    int quoteIndex = nextLine.indexOf("\"", widthIndex + 8);
                    double width = Double.parseDouble(nextLine.substring(widthIndex + 7, quoteIndex));
                    int heightIndex = nextLine.indexOf("height=");
                    quoteIndex = nextLine.indexOf("\"", heightIndex + 9);
                    double height = Double.parseDouble(nextLine.substring(heightIndex + 8, quoteIndex));

                    String[] styleValues = getStyleValues(nextLine);

                    Rectangle rect = new Rectangle(values[0], values[1], width, height);
                    rect.setStroke(Color.web(("0x" + styleValues[1]), Double.parseDouble(styleValues[4])));
                    rect.setFill(Color.web(("0x" + styleValues[0]), Double.parseDouble(styleValues[3])));
                    rect.setSize(Double.parseDouble(styleValues[2]));

                    svgShapes.add(rect);
                } else if (nextLine.contains("ellipse")) {
                    String[] searchString = {"cx", "cy", "rx", "ry"};
                    double[] values = new double[searchString.length];
                    for (int i = 0; i < searchString.length; i++) {
                        int valueIndex = nextLine.indexOf(searchString[i]);
                        int quoteIndex = nextLine.indexOf("\"", valueIndex + 4);
                        double value = Double.parseDouble(nextLine.substring(valueIndex + 4, quoteIndex));
                        values[i] = value;
                    }

                    String[] styleValues = getStyleValues(nextLine);

                    Circle ellipse = new Circle(values[0], values[1], values[2], values[3]);
                    ellipse.setStroke(Color.web(("0x" + styleValues[1]), Double.parseDouble(styleValues[4])));
                    ellipse.setFill(Color.web(("0x" + styleValues[0]), Double.parseDouble(styleValues[3])));
                    ellipse.setSize(Double.parseDouble(styleValues[2]));

                    svgShapes.add(ellipse);
                } else if (nextLine.contains("text")) {
                    String[] searchString = {"x=", "y="};
                    double[] values = new double[searchString.length];
                    for (int i = 0; i < searchString.length; i++) {
                        int valueIndex = nextLine.indexOf(searchString[i]);
                        int quoteIndex = nextLine.indexOf("\"", valueIndex + 3);
                        double value = Double.parseDouble(nextLine.substring(valueIndex + 3, quoteIndex));
                        values[i] = value;
                    }

                    int greaterThenIndex = nextLine.indexOf(">");
                    int lessThenIndex = nextLine.indexOf("<", greaterThenIndex + 1);
                    String textValue = nextLine.substring(greaterThenIndex + 1, lessThenIndex);

                    int fontSizeIndex = nextLine.indexOf("font-size");
                    int quoteIndex = nextLine.indexOf("\"", fontSizeIndex + 11);
                    double fontSize = Double.parseDouble(nextLine.substring(fontSizeIndex + 11, quoteIndex));

                    String[] styleValues = getStyleValues(nextLine);

                    Text text = new Text(textValue);
                    text.setStartX(values[0]);
                    text.setStartY(values[1]);
                    text.setStroke(Color.web(("0x" + styleValues[1]), Double.parseDouble(styleValues[4])));
                    text.setFill(Color.web(("0x" + styleValues[0]), Double.parseDouble(styleValues[3])));
                    text.setSize(fontSize);
                    text.setHeight(fontSize);

                    svgShapes.add(text);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return svgShapes;
    }

    public static String[] getStyleValues(String nextLine) {
        String[] styleValues = new String[5];

        int fillIndex = nextLine.indexOf("fill:");
        int endCharIndex = nextLine.indexOf(";", fillIndex);
        String fillValue = nextLine.substring(fillIndex + 6, endCharIndex);
        int strokeIndex= nextLine.indexOf("stroke:");
        endCharIndex = nextLine.indexOf(";", strokeIndex);
        String strokeValue = nextLine.substring(strokeIndex + 8, endCharIndex);
        int strokeWidthIndex = nextLine.indexOf("stroke-width:");
        endCharIndex = nextLine.indexOf(";", strokeWidthIndex);
        double strokeWidth = Double.parseDouble(nextLine.substring(strokeWidthIndex + 13, endCharIndex));
        int fillOpacityIndex = nextLine.indexOf("fill-opacity:");
        endCharIndex = nextLine.indexOf(";", fillOpacityIndex);
        double fillOpacity = Double.parseDouble(nextLine.substring(fillOpacityIndex + 13, endCharIndex));
        int strokeOpacityIndex = nextLine.indexOf("stroke-opacity:");
        endCharIndex = nextLine.indexOf(";", strokeOpacityIndex);
        double strokeOpacity = Double.parseDouble(nextLine.substring(strokeOpacityIndex + 15, endCharIndex));

        styleValues[0] = fillValue;
        styleValues[1] = strokeValue;
        styleValues[2] = Double.toString(strokeWidth);
        styleValues[3] = Double.toString(fillOpacity);
        styleValues[4] = Double.toString(strokeOpacity);

        return styleValues;
    }

    public static void saveBtn(ArrayList<Shape> shapes) {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("sample.fxml"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        FileChooser.ExtensionFilter iluFilter = new FileChooser.ExtensionFilter("Illustrera (*.ilu)", "*.ilu");
        FileChooser.ExtensionFilter svgFilter = new FileChooser.ExtensionFilter("SVG (*.svg)", "*.svg");
        fileChooser.getExtensionFilters().add(iluFilter);
        fileChooser.getExtensionFilters().add(svgFilter);
        File file = fileChooser.showSaveDialog(loader.getRoot());
        if (file != null) {
            try {
                String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).replaceFirst("\\*", "");
                if (extension.equals(".svg")) {
                    writeSVG(file, shapes, extension);
                } else if (extension.equals(".ilu")) {
                    writeILU(file, shapes);
                }
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not save file, try again");
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        }
    }

    private static void writeSVG(File file, ArrayList<Shape> shapes, String extension) throws IOException {
        FileWriter svgWriter = new FileWriter(file + extension);
        svgWriter.append("<svg height=\"1040\" width=\"1820\">\n");
        for (Shape shape : shapes) {
            switch (shape.getType()) {
                case "Line": {
                    Line line = (Line) shape;
                    String strokeColor = getStrokeColor(shape);
                    String fillColor = getFillColor(shape);
                    svgWriter.append("<line x1=\"").append(String.valueOf(line.getStartX())).append("\" y1=\"").append(String.valueOf(line.getStartY())).append("\" x2=\"").append(String.valueOf(line.getWidth())).append("\" y2=\"").append(String.valueOf(line.getHeight())).append("\" style=\"fill:#").append(fillColor).append(";stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(shape.getSize())).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";").append("\" />\n");
                    break;
                }
                case "Circle": {
                    Circle circle = (Circle) shape;
                    String strokeColor = getStrokeColor(shape);
                    String fillColor = getFillColor(shape);
                    svgWriter.append("<ellipse cx=\"").append(String.valueOf(circle.getCenterX())).append("\" cy=\"").append(String.valueOf(circle.getCenterY())).append("\" rx=\"").append(String.valueOf(circle.getXRadius())).append("\" ry=\"").append(String.valueOf(circle.getYRadius())).append("\" style=\"fill:#").append(fillColor).append(";stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(shape.getSize())).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";").append("\" />\n");
                    break;
                }
                case "Rectangle": {
                    Rectangle rectangle = (Rectangle) shape;
                    String strokeColor = getStrokeColor(shape);
                    String fillColor = getFillColor(shape);
                    svgWriter.append("<rect x=\"").append(String.valueOf(rectangle.getStartX())).append("\" y=\"").append(String.valueOf(rectangle.getStartY())).append("\" width=\"").append(String.valueOf(rectangle.getWidth())).append("\" height=\"").append(String.valueOf(rectangle.getHeight())).append("\" style=\"fill:#").append(fillColor).append(";stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(shape.getSize())).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";").append("\" />\n");
                    break;
                }
                case "Text": {
                    Text text = (Text) shape;
                    String strokeColor = getStrokeColor(shape);
                    String fillColor = getFillColor(shape);
                    svgWriter.append("<text x=\"").append(String.valueOf(text.getStartX())).append("\" y=\"").append(String.valueOf(text.getStartY())).append("\" font-size=\"").append(String.valueOf(shape.getSize())).append("\" style=\"fill:#").append(fillColor).append(";stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(1)).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";\"").append(">").append(text.getText()).append("</text>\n");
                }
            }
        }
        svgWriter.append("</svg>");
        svgWriter.close();
    }

    private static void writeILU(File file, ArrayList<Shape> shapes) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file + ".ilu");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(shapes);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStrokeColor(Shape shape) {
        String color = shape.getStroke().toString();
        color = color.replaceFirst("0", "");
        color = color.replaceFirst("x", "");
        for (int i = 0; i < 2; i++) {
            color = color.substring(0, color.length() - 1);
        }
        return color;
    }

    private static String getFillColor(Shape shape) {
        String color = shape.getFill().toString();
        color = color.replaceFirst("0", "");
        color = color.replaceFirst("x", "");
        for (int i = 0; i < 2; i++) {
            color = color.substring(0, color.length() - 1);
        }
        return color;
    }
}
