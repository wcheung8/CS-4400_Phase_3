package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ApplicationReport;
import java.sql.*;

public class AdminApplicationReportController extends Controller {

	@FXML
	private TableView<ApplicationReport> view;
	@FXML
	private TableColumn<ApplicationReport, String> projectCol;
	@FXML
	private TableColumn<ApplicationReport, Integer> numApplicantsCol;
	@FXML
	private TableColumn<ApplicationReport, String> acceptRateCol;
	@FXML
	private TableColumn<ApplicationReport, String> topMajorCol;
	@FXML
	private Label numAppsDescription;

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	Connection conn = null;
	Statement stmt = null;
	Statement stmt2 = null;

	ObservableList<ApplicationReport> applications = FXCollections.observableArrayList();

	public void initialize() {
		// set proper data sets for table
		projectCol.setCellValueFactory(cellData -> cellData.getValue().getProjectName());
		numApplicantsCol.setCellValueFactory(cellData -> cellData.getValue().getNumApplicants().asObject());
		acceptRateCol.setCellValueFactory(cellData -> cellData.getValue().getAcceptRate());
		topMajorCol.setCellValueFactory(cellData -> cellData.getValue().getTopMajors());

		view.setItems((ObservableList<ApplicationReport>) getData());
	}

	@FXML
	public ObservableList<ApplicationReport> getData() {

		// flush old data
		applications.clear();

		// populate data with applications
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			String sql;
			ResultSet rs;
			String totalApplications;
			String acceptedApplications;
			
			// gets number of all applications
			sql = "SELECT projectName, COUNT(*) FROM APPLICATION;";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				totalApplications = rs.getString("COUNT(*)");
			} else {
				totalApplications = "0";
			}
			
			//gets number of accepted applications
			sql = "SELECT projectName, COUNT(projectName) FROM APPLICATION WHERE status = 1;";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				acceptedApplications = rs.getString("COUNT(projectName)");
			} else {
				acceptedApplications = "0";
			}
			
			//sets description 
			numAppsDescription.setText(totalApplications
					+ " applications in total, accepted "
					+ acceptedApplications
					+ " applications");
			
			/*
			// JL Tests:
			// gets projects and # of all projects
			sql = "SELECT projectName, COUNT(*) FROM APPLICATION GROUP BY projectName;";
			
			// gets projects and # of accepted
			sql = "SELECT projectName, COUNT(projectName) FROM APPLICATION WHERE status = 1 GROUP BY projectName;";
			
			// natural join to also have columns of # projects and # accepted projects
			sql = "SELECT allProjects.projectName, allProjects.allCount, IFNULL(acceptedProjects.acceptedCount, 0) as acceptedCount FROM (SELECT projectName, COUNT(*) as allCount FROM APPLICATION GROUP BY projectName) allProjects LEFT JOIN (SELECT projectName, COUNT(projectName) as acceptedCount FROM APPLICATION WHERE status = 1 GROUP BY projectName) acceptedProjects ON allProjects.projectName = acceptedProjects.projectName;";
			*/
			
			// gets projectName, respective project counts, and acceptRate
			sql = "SELECT projectName, allCount, CONCAT(ROUND(((acceptedCount)/(allCount)*100),2),'%') AS acceptRate "
			    + "FROM (SELECT allProjects.projectName, allProjects.allCount, IFNULL(acceptedProjects.acceptedCount, 0) as acceptedCount "
			          + "FROM (SELECT projectName, COUNT(username) as allCount "
			                + "FROM APPLICATION NATURAL RIGHT JOIN PROJECT GROUP BY projectName) allProjects "
			                + "LEFT JOIN (SELECT projectName, COUNT(projectName) as acceptedCount "
			                + "FROM APPLICATION WHERE status = 1 GROUP BY projectName) acceptedProjects "
			                + "ON allProjects.projectName = acceptedProjects.projectName) joinedTable "
			                + "ORDER BY allCount DESC;";
			
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String projectName = rs.getString("projectName");
				Integer numApplications =  rs.getInt("allCount");
				String acceptRate =  String.valueOf(rs.getInt("acceptRate")) + "%";
				
				sql = "SELECT projectName, majorName, COUNT(majorName) FROM USER NATURAL JOIN APPLICATION GROUP BY majorName HAVING projectName = '" + projectName + "' ORDER BY COUNT(majorName) DESC LIMIT 3;";
				ResultSet rs2 = stmt2.executeQuery(sql);
				String topMajors = "";
				while (rs2.next()) {
					if (rs2.getString("majorName") != null) {
						topMajors = topMajors + rs2.getString("majorName") + "/";
					}
				}
				if (topMajors.length() != 0) {
					topMajors = topMajors.substring(0, topMajors.length() - 1);
				}
				rs2.close();
				
				applications.add(new ApplicationReport(projectName, numApplications, acceptRate, topMajors));
			}

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
	public void handleBackPressed() {
		showScreen("../view/AdminMainScreen.fxml", "Main Screen");
	}
}
