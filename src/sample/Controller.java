package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import sample.Shapes.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;

public class Controller {

    public static Image openBtn() throws IOException {
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
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        } else {
            if (getFileExtension(file).equals(".svg")) {
                Image openImage = new Image(file.toURI().toString());
                drawController.emptySavedImages();
                return openImage;
            } else if (getFileExtension(file).equals(".ilu")) {
                FileInputStream fileIn = null;
                ObjectInputStream in = null;
                try {
                    fileIn = new FileInputStream(file);
                    in = new ObjectInputStream(fileIn);
                    drawController.shapes = (ArrayList<Shape>) in.readObject();
                    in.close();
                    fileIn.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void saveBtn(Image image) {
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
                ArrayList<Shape> shapes = drawController.shapes;
                String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).replaceFirst("(?:\\*)", "");
                if (extension.equals(".svg")) {
                    writeSVG(file, shapes, extension);
                    System.out.println("SVG");
                } else if (extension.equals(".ilu")) {
                    writeILU(file, shapes);
                    System.out.println("ILU");
                }
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not save file, try again");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK");
                    }
                });
            }
        }
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
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

    private static void writeSVG(File file, ArrayList<Shape> shapes, String extension) throws IOException {
        FileWriter svgWriter = new FileWriter(file + extension);
        svgWriter.append("<svg height=\"1040\" width=\"1820\">\n");
        for (Shape shape : shapes) {
            if (shape.getType().equals("Line")) {
                Line line = (Line)shape;
                String strokeColor = getStrokeColor(shape);
                svgWriter.append("<line x1=\"").append(String.valueOf(line.getStartX())).append("\" y1=\"").append(String.valueOf(line.getStartY())).append("\" x2=\"").append(String.valueOf(line.getWidth())).append("\" y2=\"").append(String.valueOf(line.getHeight())).append("\" style=\"stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(shape.getSize())).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";").append("\" />\n");
            } else if (shape.getType().equals("Circle")) {
                Circle circle = (Circle)shape;
                String strokeColor = getStrokeColor(shape);
                String fillColor = getFillColor(shape);
                System.out.println(shape.getCenterX() + " " + shape.getCenterY());
                svgWriter.append("<ellipse cx=\"").append(String.valueOf(circle.getCenterX())).append("\" cy=\"").append(String.valueOf(circle.getCenterY())).append("\" rx=\"").append(String.valueOf(circle.getXRadius())).append("\" ry=\"").append(String.valueOf(circle.getYRadius())).append("\" style=\"fill:#").append(fillColor).append(";stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(shape.getSize())).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";").append("\" />\n");
            } else if (shape.getType().equals("Rectangle")) {
                Rectangle rectangle = (Rectangle)shape;
                String strokeColor = getStrokeColor(shape);
                String fillColor = getFillColor(shape);
                svgWriter.append("<rect x=\"").append(String.valueOf(rectangle.getStartX())).append("\" y=\"").append(String.valueOf(rectangle.getStartY())).append("\" width=\"").append(String.valueOf(rectangle.getWidth())).append("\" height=\"").append(String.valueOf(rectangle.getHeight())).append("\" style=\"fill:#").append(fillColor).append(";stroke:#").append(strokeColor).append(";stroke-width:").append(String.valueOf(shape.getSize())).append(";fill-opacity:").append(String.valueOf(shape.getFillOpacity())).append(";stroke-opacity:").append(String.valueOf(shape.getStrokeOpacity())).append(";").append("\" />\n");
            }
        }
        svgWriter.append("</svg>");
        svgWriter.close();
    }

    private static String getStrokeColor(Shape shape) {
        String color = shape.getStroke().toString();
        color = color.replaceFirst("(?:0)", "");
        color = color.replaceFirst("(?:x)", "");
        for (int i = 0; i < 2; i++) {
            color = color.substring(0, color.length() - 1);
        }
        return color;
    }

    private static String getFillColor(Shape shape) {
        String color = shape.getFill().toString();
        color = color.replaceFirst("(?:0)", "");
        color = color.replaceFirst("(?:x)", "");
        for (int i = 0; i < 2; i++) {
            color = color.substring(0, color.length() - 1);
        }
        return color;
    }
}
