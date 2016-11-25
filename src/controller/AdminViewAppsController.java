package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Activity;
import model.Application;
import java.sql.*;

public class AdminViewAppsController extends Controller {
	
	@FXML 
	private TableView<Application> tableView;
	@FXML 
	private TableColumn<Application, String> projectCol;
	@FXML 
	private TableColumn<Application, String> majorCol;
	@FXML 
	private TableColumn<Application, String> yearCol;
	@FXML 
	private TableColumn<Application, String> statusCol;
	
	// driver details
	 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	 static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";
	 
	 Connection conn = null;
	 Statement stmt = null;
	 
	 ObservableList<Application> applications = FXCollections.observableArrayList();
	 
	 public void initialize() {
		 projectCol.setCellValueFactory(new PropertyValueFactory<Application, String>("project"));
		 majorCol.setCellValueFactory(new PropertyValueFactory<Application, String>("major"));
		 yearCol.setCellValueFactory(new PropertyValueFactory<Application, String>("year"));
		 statusCol.setCellValueFactory(new PropertyValueFactory<Application, String>("status"));
		 tableView.getItems().setAll((ObservableList<Application>) getData());
	 }

	@FXML
	public ObservableList<Application> getData() {
		// initialize();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;
			
			sql = "SELECT projectName, majorName, year, status FROM USER INNER JOIN APPLICATION ON USER.username = APPLICATION.username";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String projectName = rs.getString("projectName");
				String majorName = rs.getString("majorName");
				if (majorName == null) {
					majorName = "null";
				}
				String year = rs.getString("year");
				if (year == null) {
					year = "null";
				}
				String status = rs.getString("status");
				if (status == null) {
					status = "null";
				}
				applications.add(new Application(projectName, majorName, year, status));		
			}
			// System.out.println(tableView != null);
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException se) {
			 se.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		} 
		return applications;
		
	}
	
	
	@FXML
	public void handleViewPopularProjectPressed() {
		showScreen("../view/AdminPopularProjectReport.fxml", "Popular Projects");
	}
	
	@FXML
	public void handleViewApplicationReportPressed() {
		showScreen("../view/AdminApplicationReport.fxml", "Application Report");
	}
	
	@FXML
	public void handleAddProjectPressed() {
		showScreen("../view/AdminAddProject.fxml", 800, "Add Project");
	}
	
	@FXML
	public void handleAddCoursePressed() {
		showScreen("../view/AdminAddCourse.fxml", "Add Course");
	}
	
	@FXML
	public void handleBackPressed() {
		showScreen("../view/AdminMainScreen.fxml", "Main Screen");
	}
	
	@FXML
	public void handleAcceptPressed() {
		
	}
	
	@FXML
	public void handleRejectPressed() {
		
	}
	
	@FXML
	public void handleAddCategory() {
		
	}
	
	
	
	@FXML
    public void handleCancelPressed() {
        showScreen("../view/LoginScreen.fxml", "Login");
    }
}
