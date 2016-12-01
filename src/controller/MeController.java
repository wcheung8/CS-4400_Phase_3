package controller;

import javafx.fxml.FXML;

public class MeController extends Controller {
	@FXML
	void handleProfilePressed() {
		showScreen("../view/ProfileScreen.fxml", "Profile");
	}
	
	@FXML
	void handleBackPressed() {
		showScreen("../view/MainScreen.fxml", "Main Screen");
	}
	
	@FXML
	void handleMyApplicationPressed() {
		showScreen("../view/MyApplication.fxml", "My Application");
	}
}