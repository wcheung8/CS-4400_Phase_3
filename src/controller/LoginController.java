package controller;

import javafx.fxml.FXML;

public class LoginController extends Controller {
	
	@FXML
	public void handleLoginPressed() {
		showScreen("../view/AdminMainScreen.fxml", "Main Screen");
	    
	    
	}
	
	@FXML
	public void handleRegisterPressed() {
	    showScreen("../view/RegisterScreen.fxml", "Register");
	}
}