package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fxapp.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(Main.stage());
			alert.setTitle("Invalid Login");
			alert.setHeaderText("Invalid Login");
			alert.setContentText("Username or password not filled.");
			alert.show();
		} else if (!checkPassword.equals(password)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(Main.stage());
			alert.setTitle("Passwords must match!");
			alert.setHeaderText("Passwords must match!");
			alert.setContentText("Password check did not match.");
			alert.show();
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();
				String sql;

				sql = "INSERT INTO USER (username, password, isAdmin, gtemail) VALUES ('" + username + "','" + password + "', 0,'" + email + "');";
				int rs = stmt.executeUpdate(sql);

				System.out.println(rs);

				stmt.close();
				conn.close();

				showScreen("../view/LoginScreen.fxml", "Login");

			} catch (SQLException se) {
				if (se.getMessage().contains("Duplicate")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.initOwner(Main.stage());
					alert.setTitle("User already exists!");
					alert.setHeaderText("User already exists!");
					alert.setContentText("Cannot insert duplicate user.");
					alert.show();
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