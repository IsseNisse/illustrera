package sample;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

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
                ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", file);
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
