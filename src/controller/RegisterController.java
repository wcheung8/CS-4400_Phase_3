package controller;

import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController extends Controller {
	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	@FXML
	TextField userField;

	@FXML
	TextField emailField;

	@FXML
	PasswordField passwordField;

	@FXML
	PasswordField checkPasswordField;

	@FXML
	public void handleCreatePressed() {

		Connection conn = null;
		Statement stmt = null;

		String username = userField.getText();
		String email = emailField.getText();
		String password = passwordField.getText();
		String checkPassword = checkPasswordField.getText();

		if (username.isEmpty() || password.isEmpty() || email.isEmpty() || checkPassword.isEmpty()) {
			alert("Invalid Login", "Username or password not filled.");
		} else if (!checkPassword.equals(password)) {
			alert("Passwords must match!", "Password check did not match.");
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();
				String sql;

				sql = "INSERT INTO USER (username, password, isAdmin, gtemail) VALUES ('" + username + "','" + password + "', 0,'" + email + "');";
				stmt.executeUpdate(sql);

				stmt.close();
				conn.close();

				showScreen("../view/LoginScreen.fxml", "Login");

			} catch (SQLException se) {
				if (se.getMessage().contains("Duplicate")) {
					alert("Username/email already exists!", "Cannot insert duplicate username/email.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	public void handleCancelPressed() {
		showScreen("../view/LoginScreen.fxml", "Login");
	}
}