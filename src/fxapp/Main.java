package fxapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Activity;

/**
 * Entry point into the application.
 */
public class Main extends Application {

    private static Stage mainStage;
    public static String currentUsername;
    public static Activity selectedActivity;

    /**
     * @return the single instance of the main stage.
     */
    public static Stage stage() {
        return mainStage;
    }

    public static final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("../view/LoginScreen.fxml"));
        stage.getIcons().add(new Image("icon.png"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
