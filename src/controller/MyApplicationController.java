package controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.UserApplication;
import java.sql.*;
import fxapp.Main;

public class MyApplicationController extends Controller {

	@FXML
	private TableView<UserApplication> view;
	@FXML
	private TableColumn<UserApplication, String> dateCol;
	@FXML
	private TableColumn<UserApplication, String> projectCol;
	@FXML
	private TableColumn<UserApplication, String> statusCol;

	// driver details
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

	Connection conn = null;
	Statement stmt = null;
	
	ObservableList<UserApplication> applications = FXCollections.observableArrayList();
	
	public void initialize() {
		// set proper data sets for table
		dateCol.setCellValueFactory(cellData -> cellData.getValue().getDate());
		projectCol.setCellValueFactory(cellData -> cellData.getValue().getProject());
		statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatus());
		view.setItems((ObservableList<UserApplication>) getData());
	}

	@FXML
	public ObservableList<UserApplication> getData() {

		// flush old data
		applications.clear();

		// populate data with applications
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
			stmt = conn.createStatement();
			String sql;

			// get all application data
			sql = "SELECT date, projectName, status FROM APPLICATION WHERE username='" + Main.currentUsername + "';";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String date = rs.getString("date");
				String projectName = rs.getString("projectName");
				String status = rs.getString("status");

				if (status == null) {
					status = "Pending";
				} else if (status.equals("1")) {
					status = "Accepted";
				} else {
					status = "Rejected";
				}

				applications.add(new UserApplication(date, projectName, status));
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
		showScreen("../view/Me.fxml", "Me");
	}
}
