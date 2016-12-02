package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Activity;

import fxapp.Main;

import java.sql.*;
import java.util.ArrayList;

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
	private TableView<Activity> view;
	@FXML
	private TableColumn<Activity, String> activityName;
	@FXML
	private TableColumn<Activity, String> activityType;

	Connection conn = null;
	Statement stmt = null;

	// selected categories list
	ArrayList<String> selected = new ArrayList<String>();

	// view for all activities
	ObservableList<Activity> activities = FXCollections.observableArrayList();

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
			sql = "SELECT designationName " 
				+ "FROM DESIGNATION;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				designationField.getItems().add(rs.getString("designationName"));
			}

			// populate majors
			sql = "SELECT majorName " 
				+ "FROM MAJOR;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				majorField.getItems().add(rs.getString("majorName"));
			}

			// populate category
			sql = "SELECT categoryName " 
				+ "FROM CATEGORY;";
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

		// set the table view to contain the list of courses from the model
		view.setItems((ObservableList<Activity>) activities);

		// Initialize the course table with the two columns.
		activityName.setCellValueFactory(cellData -> cellData.getValue().getName());
		activityType.setCellValueFactory(cellData -> cellData.getValue().getType());

		// Listen for selection changes and show the activities list when changed.
        view.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue,
                        newValue) -> openApplicationDialog(newValue));
		
	}

	private void openApplicationDialog(Activity selected) {

		Main.selectedActivity = selected;
		showScreen("../view/Application.fxml", "Application");
		
	}

	@FXML
	void applyFilter() {

		activities.clear();

		// check empty fields
		String titleFilter = "%";
		String designationFilter = "";
		String majorFilter = "";
		String departmentFilter = "";
		String yearFilter = "";
		String courseCategoryFilter = "";
		String projectCategoryFilter = "";
		ObservableList<String> category = categoryField.getSelectionModel().getSelectedItems();
		int tableChoice = 1;
		boolean noRequirements = true;

		//set table choice
        if (project.isSelected()) {
            tableChoice = 0;
        } else if (course.isSelected()) {
            tableChoice = 2;
        }
		
		//check empty title
		if (!titleFilter.isEmpty()) {
			titleFilter = titleField.getText();
		}
		
		//check empty designation
		if (designationField.getSelectionModel().getSelectedItem() != null) {
			designationFilter = "AND designationName = '"+ designationField.getSelectionModel().getSelectedItem() + "' ";
		}
		
		//check empty major
		if (majorField.getSelectionModel().getSelectedItem() != null) {
		    noRequirements = false;
			majorFilter = "AND (projectName in (SELECT projectName "
										    + "FROM MAJOR_REQUIREMENT " 
										    + "WHERE MAJOR_REQUIREMENT.majorName = '" + majorField.getSelectionModel().getSelectedItem() + "') ";
			departmentFilter = "OR projectName in (SELECT projectName "
                                                + "FROM DEPARTMENT_REQUIREMENT " 
                                                + "WHERE DEPARTMENT_REQUIREMENT.departmentName in (SELECT departmentName "
                                                                                                + "FROM MAJOR "
                                                                                                + "WHERE majorName = '" + majorField.getSelectionModel().getSelectedItem() + "') )) ";
		}
		
		//check empty year
		if (yearField.getSelectionModel().getSelectedItem() != null) {
		    noRequirements = false;
		    yearFilter = "AND projectName in (SELECT projectName " 
										   + "FROM YEAR_REQUIREMENT " 
										   + "WHERE YEAR_REQUIREMENT.year = '"+ yearField.getSelectionModel().getSelectedItem() + "') ";
		}
		
		//check empty category
		if (category != null) {
			for(String s : category) {
				courseCategoryFilter += "AND courseName in (SELECT courseName " 
														 + "FROM COURSE_CATEGORY " 
														 + "WHERE COURSE_CATEGORY.categoryName = '"+ s + "') ";
				projectCategoryFilter += "AND projectName in (SELECT projectName " 
														 + "FROM PROJECT_CATEGORY " 
														 + "WHERE PROJECT_CATEGORY.categoryName = '"+ s + "') ";
			}
		}
		
		

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;
			ResultSet rs = null;

			// add projects
			if (tableChoice <= 1) {
				sql = "SELECT projectName " 
					+ "FROM PROJECT " 
					+ "WHERE projectName LIKE '%" + titleFilter + "%' "
					+ designationFilter
					+ majorFilter
					+ departmentFilter
					+ yearFilter
					+ projectCategoryFilter;

				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String name = rs.getString("projectName");
					activities.add(new Activity(name, "Project"));
				}
			}

			// add courses
			if (tableChoice >= 1 && noRequirements) {
				sql = "SELECT courseName " 
					+ "FROM COURSE " 
					+ "WHERE courseName LIKE '%" + titleFilter + "%' "
					+ designationFilter
					+ courseCategoryFilter + ";";
				
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String name = rs.getString("courseName");
					activities.add(new Activity(name, "Course"));
				}
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

		activities.clear();

		initialize();
	}
	
	@FXML
	void handleMePressed() {
		showScreen("../view/Me.fxml", "Me");
	}
	
	@FXML
	public void handleLogoutPressed() {
		showScreen("../view/LoginScreen.fxml", "Login");
	}
}