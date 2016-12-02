package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Activity;
import java.sql.*;
import java.util.ArrayList;

public class AddProjectController extends Controller {

    // driver details
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

    @FXML
    private TextField projectNameField;
    @FXML
    private TextField advisorField;
    @FXML
    private TextField advisorEmailField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField estNumField;
    @FXML
    private ComboBox<String> designationField;
    @FXML
    private ComboBox<String> majorField;
    @FXML
    private ComboBox<String> yearField;
    @FXML
    private ComboBox<String> departmentField;
    @FXML
    private ListView<String> categoryField;

    Connection conn = null;
    Statement stmt = null;

    // selected categories list
    ArrayList<String> selected = new ArrayList<String>();

    // view for all activities
    ObservableList<Activity> activities = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        descriptionField.setWrapText(true);

        // set category to be multiselect
        categoryField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // populate fields
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
            stmt = conn.createStatement();
            String sql;

            // populate designation
            sql = "SELECT designationName " + "FROM DESIGNATION;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                designationField.getItems().add(rs.getString("designationName"));
            }

            // populate majors
            sql = "SELECT majorName " + "FROM MAJOR;";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                majorField.getItems().add(rs.getString("majorName"));
            }

            // populate category
            sql = "SELECT categoryName " + "FROM CATEGORY;";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categoryField.getItems().add(rs.getString("categoryName"));
            }

            // populate departments
            departmentField.getItems().add("No requirement");
            sql = "SELECT departmentName " + "FROM DEPARTMENT;";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                departmentField.getItems().add(rs.getString("departmentName"));
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
    public void handleSubmitPressed() {

        // check if all fields (besides department) needed for submit are empty

        if (projectNameField.getText().isEmpty() || descriptionField.getText().isEmpty()
                || advisorField.getText().isEmpty() || advisorEmailField.getText().isEmpty()
                || advisorField.getText().isEmpty() || estNumField.getText().isEmpty()
                || categoryField.getSelectionModel().isEmpty() || designationField.getSelectionModel().isEmpty()) {
            alert("Error!", "All required fields not filled.");
        } else {
            // check estNum field for integer value
            try {
                Integer.parseInt(estNumField.getText());
            } catch (NumberFormatException er) {
                alert("Error!", "Invalid Number for Estimated Students.");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            // insert into database
            try {
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
                stmt = conn.createStatement();
                String sql;

                // insert into project table
                sql = "INSERT INTO PROJECT VALUES ('" + projectNameField.getText() + "', '" + descriptionField.getText()
                        + "', '" + estNumField.getText() + "', '" + advisorField.getText() + "', '"
                        + advisorEmailField.getText() + "', '" + designationField.getValue() + "');";
                stmt.executeUpdate(sql);

                // insert into project_category table
                ObservableList<String> categories = categoryField.getSelectionModel().getSelectedItems();
                for (String category : categories) {
                    sql = "INSERT INTO PROJECT_CATEGORY VALUES ('" + projectNameField.getText() + "', '" + category
                            + "');";
                    stmt.executeUpdate(sql);
                }

                // insert into the major_requirement table
                if (!(majorField.getSelectionModel().isEmpty())) {
                    sql = "INSERT INTO MAJOR_REQUIREMENT VALUES ('" + projectNameField.getText() + "', '"
                            + majorField.getValue() + "');";
                    stmt.executeUpdate(sql);
                }
                // insert into the year_requirement table
                if (!(yearField.getSelectionModel().isEmpty())) {
                    sql = "INSERT INTO YEAR_REQUIREMENT VALUES ('" + projectNameField.getText() + "', '"
                            + yearField.getValue() + "');";
                    stmt.executeUpdate(sql);
                }
                // insert into the department_requirement table if departmentField is not empty
                if (!(departmentField.getSelectionModel().isEmpty())) {
                    sql = "INSERT INTO DEPARTMENT_REQUIREMENT VALUES ('" + projectNameField.getText() + "', '"
                            + departmentField.getValue() + "');";
                    stmt.executeUpdate(sql);
                }

                alert("Success!", "Your project has been successfully added.");
                
                stmt.close();
                conn.close();

            } catch (SQLException se) {
                if (se.getMessage().contains("Duplicate")) {
                    alert("Course already exists!", "Cannot insert duplicate course number/course name.");
                } else {
                    se.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleBackPressed() {
        showScreen("../view/AdminMainScreen.fxml", "Main Screen");
    }
}