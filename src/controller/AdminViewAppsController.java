package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Activity;
import model.Application;
import java.sql.*;

import fxapp.Main;

public class AdminViewAppsController extends Controller {

	@FXML
	private TableView<Application> view;
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

		// set category to be multiselect
		view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// set proper data sets for table
		projectCol.setCellValueFactory(cellData -> cellData.getValue().getProject());
		majorCol.setCellValueFactory(cellData -> cellData.getValue().getMajor());
		yearCol.setCellValueFactory(cellData -> cellData.getValue().getYear());
		statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatus());
		view.setItems((ObservableList<Application>) getData());
	}

	@FXML
	public ObservableList<Application> getData() {

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

				applications.add(new Application(username, projectName, majorName, year, status));
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
		ObservableList<Application> selected = view.getSelectionModel().getSelectedItems();

		// check no selected
		if (selected == null) {
			alert("No applications selected!", "Cannot accept/decline zero applications.");
			return;
		}

		// update each selected application
		for (Application a : applications) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();

				String sql = "UPDATE APPLICATION SET status=1 WHERE username='" + a.getName().get() + "' AND projectName = '" + a.getProject().get()
						+ "';";
				System.out.println(sql);
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
		ObservableList<Application> selected = view.getSelectionModel().getSelectedItems();

		// check no selected
		if (selected == null) {
			alert("No applications selected!", "Cannot accept/decline zero applications.");
			return;
		}

		// update each selected application
		for (Application a : applications) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
				stmt = conn.createStatement();

				String sql = "UPDATE APPLICATION SET status=0 WHERE username='" + a.getName().get() + "' AND projectName = '" + a.getProject().get()
						+ "';";
				System.out.println(sql);
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