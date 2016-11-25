package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
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

	ObservableList<Project> applications = FXCollections.observableArrayList();

	public void initialize() {

		// set category to be multiselect
		// view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// set proper data sets for table
		projectCol.setCellValueFactory(cellData -> cellData.getValue().getProjectName());
		// MAY NEED TO CHANGE TO STRING: JL
		numApplicantsCol.setCellValueFactory(cellData -> cellData.getValue().getNumApplicants().asObject());
		view.setItems((ObservableList<Project>) getData());
	}

	@FXML
	public ObservableList<Project> getData() {

		// flush old data
		applications.clear();

		// populate data with applications
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			// get project data
			sql = "SELECT projectName, COUNT(*) FROM APPLICATION GROUP BY projectName ORDER BY COUNT(*) DESC;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String projectName = rs.getString("projectName");
				System.out.println(projectName);
				Integer numApplications = ((Number) rs.getObject("COUNT(*)")).intValue();
				System.out.println(numApplications);
				applications.add(new Project(projectName, numApplications));
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
