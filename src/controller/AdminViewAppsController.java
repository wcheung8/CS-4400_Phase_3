package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AdminApplication;
import java.sql.*;

public class AdminViewAppsController extends Controller {

	@FXML
	private TableView<AdminApplication> view;
	@FXML
	private TableColumn<AdminApplication, String> projectCol;
	@FXML
	private TableColumn<AdminApplication, String> majorCol;
	@FXML
	private TableColumn<AdminApplication, String> yearCol;
	@FXML
	private TableColumn<AdminApplication, String> statusCol;

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	Connection conn = null;
	Statement stmt = null;

	ObservableList<AdminApplication> applications = FXCollections.observableArrayList();

	public void initialize() {

		// set category to be multiselect
		view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// set proper data sets for table
		projectCol.setCellValueFactory(cellData -> cellData.getValue().getProject());
		majorCol.setCellValueFactory(cellData -> cellData.getValue().getMajor());
		yearCol.setCellValueFactory(cellData -> cellData.getValue().getYear());
		statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatus());
		view.setItems((ObservableList<AdminApplication>) getData());
	}

	@FXML
	public ObservableList<AdminApplication> getData() {

		// flush old data
		applications.clear();

		// populate data with applications
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			// get all application data
			sql = "SELECT username, projectName, majorName, year, status FROM USER NATURAL JOIN APPLICATION";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String username = rs.getString("username");
				String projectName = rs.getString("projectName");
				String majorName = rs.getString("majorName");
				String year = rs.getString("year");
				String status = rs.getString("status");

				if (status == null) {
					status = "Pending";
				} else if (status.equals("1")) {
					status = "Accepted";
				} else {
					status = "Rejected";
				}

				applications.add(new AdminApplication(username, projectName, majorName, year, status));
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

	@FXML
	public void handleAcceptPressed() {
		// get all selected
		ObservableList<AdminApplication> selected = view.getSelectionModel().getSelectedItems();

		// check no selected
		if (selected == null) {
			alert("No applications selected!", "Cannot accept/decline zero applications.");
			return;
		}

		// update each selected application
		for (AdminApplication a : selected) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();

				String sql = "UPDATE APPLICATION SET status=1 "
						   + "WHERE username='" + a.getName().getValue() + 
						   "' AND projectName = '" + a.getProject().getValue() + "';";

				stmt.executeUpdate(sql);
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// refresh table
		this.getData();

	}

	@FXML
	public void handleRejectPressed() {
		// get all selected
		ObservableList<AdminApplication> selected = view.getSelectionModel().getSelectedItems();

		// check no selected
		if (selected == null) {
			alert("No applications selected!", "Cannot accept/decline zero applications.");
			return;
		}

		// update each selected application
		for (AdminApplication a : selected) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();

				String sql = "UPDATE APPLICATION SET status=0" 
						  + " WHERE username='" + a.getName().get() 
						 + "' AND projectName = '" + a.getProject().get()
						+ "';";

				stmt.executeUpdate(sql);
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// refresh table
		this.getData();
	}
}
