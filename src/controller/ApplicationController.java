package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;

import fxapp.Main;

public class ApplicationController extends Controller {

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	@FXML
	Text designation;
	@FXML
	Text category;
	@FXML
	Text requirements;
	@FXML
	Text estNum;
	@FXML
	Text advisor;
	@FXML
	Label description;
	@FXML
	Label title;

	@FXML
	private void initialize() {
		title.setText(Main.selectedActivity.getName().get());
	}
	
	@FXML
	private void handleApplyPressed() {
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			sql = "SELECT isAdmin FROM USER";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				if (rs.getInt("isAdmin") == 1) {
					showScreen("../view/AdminMainScreen.fxml", "Main Screen");
				} else {
					showScreen("../view/MainScreen.fxml", "Main Screen");
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.initOwner(Main.stage());
				alert.setTitle("User Not Found");
				alert.setHeaderText("User Not Found");
				alert.setContentText("Username or password incorrect.");
				alert.show();
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void handleBackPressed() {
		showScreen("../view/MainScreen.fxml", "Main Screen");
	}
}