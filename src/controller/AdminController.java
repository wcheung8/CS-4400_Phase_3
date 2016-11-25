package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Application;
import java.sql.*;

public class AdminController extends Controller {
	
	@FXML private TableView<Application> tableView;
	@FXML private TableColumn<Application, String> project;
	@FXML private TableColumn<Application, String> major;
	@FXML private TableColumn<Application, String> year;
	@FXML private TableColumn<Application, String> status;
	
	// driver details
	 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	 static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";
	 
	 Connection conn = null;
	 Statement stmt = null;
	 
	 ObservableList<Application> applications = FXCollections.observableArrayList();
	 
	 
	// For admin main screen
	@FXML
	public void handleViewApplicationsPressed() {
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
				
				System.out.println(projectName);
				System.out.println(majorName);
				System.out.println(year);
				System.out.println(status);
				applications.add(new Application(projectName, majorName, year, status));		
			}
			
			for (Application application : applications) {
				System.out.println(application);
			}	
			tableView.setItems((ObservableList<Application>) applications);
			
			project = new TableColumn<Application, String>("project");
			major = new TableColumn<Application, String>("major");
			year = new TableColumn<Application, String>("year");
			status = new TableColumn<Application, String>("status");
			
			project.setCellValueFactory(new PropertyValueFactory<Application, String>("project"));
			major.setCellValueFactory(new PropertyValueFactory<Application, String>("major"));
			year.setCellValueFactory(new PropertyValueFactory<Application, String>("year"));
			status.setCellValueFactory(new PropertyValueFactory<Application, String>("status"));
			
			
		} catch (SQLException se) {
			 se.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		showScreen("../view/AdminAppScreen.fxml", "View Applications");
		
	}
	
	@FXML
	private void initialize() {
		
			/*
			// populate major
			sql = "SELECT majorName FROM USER INNER JOIN APPLICATION ON USER.username = APPLICATION.username;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("majorName");
				activities.add(new Activity(name, "majorName"));		
			}
			
			// populate year
			sql = "SELECT year FROM USER INNER JOIN APPLICATION ON USER.username = APPLICATION.username;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("year");
				activities.add(new Activity(name, "year"));		
			}
			
			//populate status
			sql = "SELECT status FROM USER INNER JOIN APPLICATION ON USER.username = APPLICATION.username;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("status");
				activities.add(new Activity(name, "status"));		
			}
			
			 tableView.getItems().setAll(applications); */
		
		
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
	
	// For view applications, maybe split into separate controller later
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
