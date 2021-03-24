package sample;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    public static Image openBtn() {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("sample.fxml"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
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
            Image openImage = new Image(file.toURI().toString());
            drawController.emptySavedImages();
            return openImage;
        }
        return null;
    }

    public static void saveBtn(Image image) {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("sample.fxml"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showSaveDialog(loader.getRoot());
        if (file != null) {
            try {
                ArrayList<Shape> shapes = drawController.shapes;
                FileWriter svgWriter = new FileWriter(file + ".svg");
                svgWriter.append("<svg height=\"1040\" width=\"1820\">");
                for (Shape shape : shapes) {
                    if (shape.getTypeSelector().equals("Line")) {
                        Line line = (Line)shape;
                        String color = line.getStroke().toString();
                        color = color.replaceFirst("(?:0)", "");
                        color = color.replaceFirst("(?:x)", "");
                        for (int i = 0; i < 2; i++) {
                            color = color.substring(0, color.length() - 1);
                        }
                        svgWriter.append("<line x1=\"" + line.getStartX() + "\" y1=\"" + line.getStartY() + "\" x2=\"" + line.getEndX() + "\" y2=\"" + line.getEndY() + "\" style=\"stroke:#" + color + ";stroke-width:10\" />\n");
                    }
                }
                svgWriter.append("</svg>");
                svgWriter.close();
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
}
