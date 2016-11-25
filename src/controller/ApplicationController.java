package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.sql.*;
import java.util.Calendar;

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

			sql = "INSERT INTO APPLICATION (username, projectName, date) "
				+ "VALUES ('" + Main.currentUsername + "','" + Main.selectedActivity.getName().get() + "', '" + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "');";
			stmt.executeUpdate(sql);

			stmt.close();
			conn.close();
			
			alert("Application Success!","You have successfully applied.");

		} catch (SQLException se) {
			if (se.getMessage().contains("Duplicate")) {
				alert("You've already applied!","Cannot apply twice to a project.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void handleBackPressed() {
		showScreen("../view/MainScreen.fxml", "Main Screen");
	}
}