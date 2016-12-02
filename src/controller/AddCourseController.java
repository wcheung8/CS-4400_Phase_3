package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import model.Activity;
import java.sql.*;
import java.util.ArrayList;

public class AddCourseController extends Controller {

    // driver details
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

    @FXML
    private TextField courseNumField;
    @FXML
    private TextField courseNameField;
    @FXML
    private TextField instructorField;
    @FXML
    private TextField estNumField;
    @FXML
    private ComboBox<String> designationField;
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

            // populate category
            sql = "SELECT categoryName " + "FROM CATEGORY;";
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
    }

    @FXML
    public void handleSubmitPressed() {

        // check if all fields needed for submit are empty

        if (courseNumField.getText().isEmpty() || courseNameField.getText().isEmpty()
                || instructorField.getText().isEmpty() || estNumField.getText().isEmpty()
                || designationField.getSelectionModel().isEmpty()
                || categoryField.getSelectionModel().getSelectedItems().isEmpty()) {
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
                ResultSet rs;

                // insert into course table
                sql = "INSERT INTO COURSE VALUES ('" + courseNameField.getText() + "', '" + courseNumField.getText()
                        + "', '" + estNumField.getText() + "', '" + instructorField.getText() + "', '"
                        + designationField.getValue() + "');";
                stmt.executeUpdate(sql);

                // insert into course_category table
                ObservableList<String> categories = categoryField.getSelectionModel().getSelectedItems();
                for (String category : categories) {
                    sql = "INSERT INTO COURSE_CATEGORY VALUES ('" + courseNameField.getText() + "', '" + category
                            + "');";
                    stmt.executeUpdate(sql);
                }

                alert("Success!", "Your course has been successfully added.");

                stmt.close();
                conn.close();

            } catch (SQLException se) {
                if (se.getMessage().contains("Duplicate")) {
                    alert("Course already exists!", "Cannot insert duplicate course number/course name.");
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