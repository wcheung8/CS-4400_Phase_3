package controller;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A controller that opens a dialog in a new window.
 * @author Jonathan Chen
 */
public abstract class DialogController extends Controller {

    protected Stage dialogStage;

    /**
     * @return the stage of the dialog.
     */
    public Stage stage() {
        dialogStage.getIcons().add(new Image("file:water.png"));
        return dialogStage;
    }

    protected void setDialogStage(Stage stage) {
        dialogStage = stage;
    }
}