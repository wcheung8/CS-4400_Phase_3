package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;
import java.util.Calendar;

import fxapp.Main;

public class ApplicationController extends Controller {

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	@FXML
	Pane descriptionPane;
	@FXML
	Pane requirementsPane;
	
	@FXML
	Text designation;
	@FXML
	Label category;
	@FXML
	Label requirements;
	@FXML
	Text estNum;
	@FXML
	Text advisor;
	@FXML
	Label description;
	@FXML
	Label title;
	
	@FXML
	VBox content;
	
	Connection conn = null;
	Statement stmt = null;

	@FXML
	private void initialize() {
		title.setText(Main.selectedActivity.getName().get());
		if (Main.selectedActivity.getType().get().equals("Project")) {
			initializeProject();
		} else {
			initializeCourse();
		}
	}

	private void initializeCourse() {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			sql = "SELECT * " + "FROM COURSE WHERE courseName = '" + Main.selectedActivity.getName().get() + "';";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				designation.setText(rs.getString("designationName"));
				estNum.setText(rs.getString("estNum"));
				advisor.setText(rs.getString("instructor"));
			}

			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		content.getChildren().remove(descriptionPane);
		content.getChildren().remove(requirementsPane);

	}

	private void initializeProject() {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			sql = "SELECT * " + "FROM PROJECT WHERE projectName = '" + Main.selectedActivity.getName().get() + "';";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				designation.setText(rs.getString("designationName"));
				estNum.setText(rs.getString("estNum"));
				advisor.setText(rs.getString("advisorName") + "(" + rs.getString("advisorEmail") + ")");
				description.setText(rs.getString("description"));
			}

			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void handleApplyPressed() {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			sql = "INSERT INTO APPLICATION (username, projectName, date) " + "VALUES ('" + Main.currentUsername + "','"
					+ Main.selectedActivity.getName().get() + "', '" + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "');";
			stmt.executeUpdate(sql);

			stmt.close();
			conn.close();

			alert("Application Success!", "You have successfully applied.");

		} catch (SQLException se) {
			if (se.getMessage().contains("Duplicate")) {
				alert("You've already applied!", "Cannot apply twice to a project.");
			} else {
				alert("CHEATER!", "You can't apply for a course... ");
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