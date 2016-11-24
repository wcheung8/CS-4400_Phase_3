package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;

import fxapp.Main;

public class MainController extends Controller {

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	@FXML
	private TextField titleField;
	@FXML
	private ComboBox<String> designationField;
	@FXML
	private ComboBox<String> majorField;
	@FXML
	private ComboBox<String> yearField;
	@FXML
	private ListView<String> categoryField;
	@FXML
	private RadioButton project;
	@FXML
	private RadioButton course;
	@FXML
	private RadioButton both;

	@FXML
	private TableView view;

	Connection conn = null;
	Statement stmt = null;

	ArrayList<String> selected = new ArrayList<String>();

	@FXML
	private void initialize() {

		// set category to be multiselect
		categoryField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// populate fields
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			// populate designation
			sql = "SELECT designationName FROM DESIGNATION;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				designationField.getItems().add(rs.getString("designationName"));
			}

			// populate majors
			sql = "SELECT majorName FROM MAJOR;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				majorField.getItems().add(rs.getString("majorName"));
			}

			// populate category
			sql = "SELECT categoryName FROM CATEGORY;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				categoryField.getItems().add(rs.getString("categoryName"));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// populate year (hardcoded)
		yearField.getItems().addAll("FR", "SO", "JR", "SR");

	}

	@FXML
	void applyFilter() {

		// check empty fields
		String title = titleField.getText();
		String designation = designationField.getSelectionModel().getSelectedItem().toString();
		String major = majorField.getSelectionModel().getSelectedItem().toString();
		String year = yearField.getSelectionModel().getSelectedItem().toString();
		ObservableList<String> category = categoryField.getSelectionModel().getSelectedItems();

		if (title.isEmpty()) {
			title = "%";
		}
		if (designation.isEmpty()) {
			designation = "%";
		}
		if (major.isEmpty()) {
			major = "%";
		}
		if (year.isEmpty()) {
			year = "%";
		}
		if (category.isEmpty()) {
			category = FXCollections.observableArrayList();
			category.add("%");
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			// populate designation
			sql = "SELECT * FROM PROJECT;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

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
	void resetFilter() {

		// clear fields
		titleField.clear();
		designationField.getItems().clear();
		majorField.getItems().clear();
		yearField.getItems().clear();
		categoryField.getItems().clear();
		project.setSelected(true);

		view.getItems().clear();

		initialize();

	}

	@FXML
	void handleProfilePressed() {
		showScreen("../view/ProfileScreen.fxml", "Profile");
	}

}