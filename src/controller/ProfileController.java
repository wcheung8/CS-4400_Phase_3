package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.sql.*;

import fxapp.Main;

public class ProfileController extends Controller {

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	@FXML
	Text departmentField;
	@FXML
	ComboBox<String> yearField;
	@FXML
	ComboBox<String> majorField;

	Connection conn = null;
	Statement stmt = null;
	String sql = null;

	String year = "";
	String major = "";
	String department = "";

	@FXML
	private void initialize() {

		// populate year (hardcoded)
		yearField.getItems().addAll("FR", "SO", "JR", "SR");

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();

			// populate majors
			sql = "SELECT majorName " + "FROM MAJOR;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				majorField.getItems().add(rs.getString("majorName"));
			}

			// load user profile
			sql = "SELECT majorName, year " + "FROM USER " + "WHERE username = '" + Main.currentUsername + "';";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				major = rs.getString("majorName");
				year = rs.getString("year");
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateDepartment();

		// set text to match profile
		yearField.getSelectionModel().select(year);
		majorField.getSelectionModel().select(major);
		departmentField.setText(department);

	}

	// updates department based on selection
	private void updateDepartment() {
		if (major == null || major.equals("")) {
			department = "";
			return;
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();

			// load user profile
			sql = "SELECT departmentName " + "FROM MAJOR " + "WHERE majorName = '" + major + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				department = rs.getString("departmentName");
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

	// updates database on user input
	@FXML
	private void update() {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();

			String updatedMajor = "null";
			String updatedyear = "null";
			if (!majorField.getSelectionModel().isEmpty()) {
				updatedMajor = "'" + majorField.getSelectionModel().getSelectedItem() + "'";
				major = majorField.getSelectionModel().getSelectedItem();
			}

			if (!yearField.getSelectionModel().isEmpty()) {
				updatedyear = "'" + yearField.getSelectionModel().getSelectedItem() + "'";
				year = yearField.getSelectionModel().getSelectedItem();
			}

			sql = "UPDATE USER SET majorName=" + updatedMajor + ", year=" + updatedyear + " WHERE username='" + Main.currentUsername + "';";

			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateDepartment();
		departmentField.setText(department);

	}

	@FXML
	private void handleBackPressed() {
		showScreen("../view/MainScreen.fxml", "Main Screen");
	}
}