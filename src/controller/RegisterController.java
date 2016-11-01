package controller;

import javafx.fxml.FXML;

public class RegisterController extends Controller {

    @FXML
    public void handleCreatePressed() {

    }

    @FXML
    public void handleCancelPressed() {
        showScreen("../view/LoginScreen.fxml", "Login");
    }
}