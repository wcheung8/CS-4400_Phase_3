package controller;

import javafx.fxml.FXML;

public class AdminController extends Controller {
	
	@FXML
	public void handleViewApplicationsPressed() {
		// showScreen("../view/MainScreen.fxml", "Main Screen");
	}
	@FXML
	public void handleViewPopularProjectPressed() {
		
	}
	@FXML
	public void handleViewApplicationReportPressed() {
		
	}
	@FXML
	public void handleAddProjectPressed() {
		
	}
	@FXML
	public void handleAddCoursePressed() {
		
	}
	
	@FXML
    public void handleCancelPressed() {
        showScreen("../view/LoginScreen.fxml", "Login");
    }
}
