package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

import fxapp.Main;

public class ApplicationController extends Controller {

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	@FXML
	TextField userField;

	@FXML
	PasswordField passwordField;

	@FXML
	public void handleLoginPressed() {
		Connection conn = null;
		Statement stmt = null;

		String username = userField.getText();
		String password = passwordField.getText();

		if (username.isEmpty() || password.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(Main.stage());
			alert.setTitle("Invalid Login");
			alert.setHeaderText("Invalid Login");
			alert.setContentText("Username or password not filled.");
			alert.show();
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();
				String sql;

				sql = "SELECT * FROM USER where username = '" + username + "';";
				ResultSet rs = stmt.executeQuery(sql);

				

				if (rs.next() && password.equals(rs.getString("password"))) {
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

	}

	@FXML
	public void handleBackPressed() {
		showScreen("../view/MainScreen.fxml", "Main Screen");
	}
}