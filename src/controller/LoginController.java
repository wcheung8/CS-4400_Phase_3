package controller;

import javafx.fxml.FXML;

public class LoginController extends Controller {
	
	@FXML
	public void handleLoginPressed() {
	    
	    
	    
	}
	
	@FXML
	public void handleRegisterPressed() {
	    showScreen("../view/RegisterScreen.fxml", "Register");
	}
}