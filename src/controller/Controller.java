package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import fxapp.Main;

/**
 * Controller superclass. Can either change the current dialog or display a dialog in a new screen.
 * 
 * @author Wesley Cheung
 */
public abstract class Controller {

    /**
     * Displays a dialog in the same window.
     * 
     * @param path the relative path to the FXML to be loaded
     * @return the controller associated with the dialog
     */
    protected Controller showScreen(String path) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(path));

            Parent root = loader.load();
            Stage mainStage = Main.stage();
            mainStage.setScene(new Scene(root));
            mainStage.getIcons()
                    .add(new Image("https://upload.wikimedia.org/wikipedia/en/8/8f/GeorgiaTechYellowJackets.png"));
            mainStage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Displays a dialog in the same window.
     * 
     * @param path the relative path to the FXML to be loaded
     * @param title the title of the new window
     * @return the controller associated with the dialog
     */
    protected Controller showScreen(String path, String title) {
        Controller controller = showScreen(path);
        Main.stage().setTitle(title);
        return controller;
    }

    /**
     * Displays a dialog in the same window.
     * 
     * @param path the relative path to the FXML to be loaded
     * @param height the height of the new window
     * @param title the title of the new window
     * @return the controller associated with the dialog
     */
    protected Controller showScreen(String path, int height, String title) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(path));

            Parent root = loader.load();
            Stage mainStage = Main.stage();
            Main.stage().setTitle(title);
            mainStage.setScene(new Scene(root, 800, height));
            mainStage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Alert alert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(Main.stage());
        alert.setTitle(header);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
        return alert;
    }
}