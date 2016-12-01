package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Project;
import java.sql.*;

public class AdminViewPopularProjectController extends Controller {

	@FXML
	private TableView<Project> view;
	@FXML
	private TableColumn<Project, String> projectCol;
	@FXML
	private TableColumn<Project, Integer> numApplicantsCol;

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	Connection conn = null;
	Statement stmt = null;

	ObservableList<Project> projects = FXCollections.observableArrayList();

	public void initialize() {
		// set proper data sets for table
		projectCol.setCellValueFactory(cellData -> cellData.getValue().getProjectName());
		numApplicantsCol.setCellValueFactory(cellData -> cellData.getValue().getNumApplicants().asObject());
		view.setItems((ObservableList<Project>) getData());
	}

	@FXML
	public ObservableList<Project> getData() {

		// flush old data
		projects.clear();

		// populate data with applications
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			// get project data
			sql = "SELECT projectName, COUNT(username) as count FROM APPLICATION NATURAL RIGHT JOIN PROJECT GROUP BY projectName ORDER BY COUNT(username) DESC, projectName LIMIT 10;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String projectName = rs.getString("projectName");
				Integer numApplications = rs.getInt("count");
				projects.add(new Project(projectName, numApplications));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projects;

	}

	@FXML
	public void handleBackPressed() {
		showScreen("../view/AdminMainScreen.fxml", "Main Screen");
	}
}
