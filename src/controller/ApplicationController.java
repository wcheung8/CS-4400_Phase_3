package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;
import java.util.Calendar;

import fxapp.Main;

public class ApplicationController extends Controller {

    // driver details
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_1";

    @FXML
    Pane descriptionPane;
    @FXML
    Pane requirementsPane;

    @FXML
    Text designation;
    @FXML
    Label category;
    @FXML
    Label requirements;
    @FXML
    Text estNum;
    @FXML
    Text advisor;
    @FXML
    Label description;
    @FXML
    Label title;
    
    @FXML
    Button applyButton;

    @FXML
    VBox content;

    Connection conn = null;
    Statement stmt = null;

    @FXML
    private void initialize() {
        title.setText(Main.selectedActivity.getName().get());
        if (Main.selectedActivity.getType().get().equals("Project")) {
            initializeProject();
        } else {
            initializeCourse();
        }
    }

    private void initializeCourse() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT * " 
                + "FROM COURSE "
                + "WHERE courseName = '" + Main.selectedActivity.getName().get() + "';";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                designation.setText(rs.getString("designationName"));
                estNum.setText(rs.getString("estNum"));
                advisor.setText(rs.getString("instructor"));
            }
            
            //populate categories
            sql = "SELECT categoryName " 
                + "FROM COURSE_CATEGORY "
                + "WHERE courseName = '" + Main.selectedActivity.getName().get() + "';";
            rs = stmt.executeQuery(sql);

            String cat = "";
            
            if(rs.next()) {
                cat = rs.getString("categoryName");
            }
            while (rs.next()) {
                cat += "," + rs.getString("categoryName");
            }
            
            category.setText(cat);

            stmt.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        content.getChildren().remove(descriptionPane);
        content.getChildren().remove(requirementsPane);
        content.getChildren().remove(applyButton);

    }

    private void initializeProject() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
            stmt = conn.createStatement();
            String sql;

            //populate fields
            sql = "SELECT * " 
                + "FROM PROJECT "
                + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "';";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                designation.setText(rs.getString("designationName"));
                estNum.setText(rs.getString("estNum"));
                advisor.setText(rs.getString("advisorName") + "(" + rs.getString("advisorEmail") + ")");
                description.setText(rs.getString("description"));
            }
            
            //populate requirements
            sql = "SELECT year " 
                + "FROM YEAR_REQUIREMENT "
                + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "' UNION "
                + "SELECT majorName " 
                + "FROM MAJOR_REQUIREMENT "
                + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "' UNION "
                + "SELECT departmentName " 
                + "FROM DEPARTMENT_REQUIREMENT "
                + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "';";
            rs = stmt.executeQuery(sql);

            String req = "";
            
            if(rs.next()) {
                req = rs.getString("year");
            }
            while (rs.next()) {
                req += "," + rs.getString("year");
            }
            
            requirements.setText(req);
            
            //populate categories
            sql = "SELECT categoryName " 
                + "FROM PROJECT_CATEGORY "
                + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "';";
            rs = stmt.executeQuery(sql);

            String cat = "";
            
            if(rs.next()) {
                cat = rs.getString("categoryName");
            }
            while (rs.next()) {
                cat += "," + rs.getString("categoryName");
            }
            
            category.setText(cat);
            
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleApplyPressed() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, "cs4400_Team_1", "MONLSe9e");
            stmt = conn.createStatement();
            String sql;
            
            //check the student's major and year are filled
            sql = "SELECT * "
                + "FROM USER "
                + "WHERE username = '" + Main.currentUsername + "' "
                + "AND (majorName IS NULL OR year IS NULL); ";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                alert("Year/Major not set!", "You must declare your major and year before applying.");
            } else {
              //check requirements are met for the student
                sql = "SELECT * " 
                      + "FROM USER "
                      + "WHERE username = '" + Main.currentUsername + "' "
                      + "AND ((NOT EXISTS (SELECT * "
                                          + "FROM MAJOR_REQUIREMENT "
                                          + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "') AND "
                             + "NOT EXISTS (SELECT * "
                                          + "FROM DEPARTMENT_REQUIREMENT "
                                          + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "')) OR "
                             + "(majorName in (SELECT majorName "
                                            + "FROM MAJOR_REQUIREMENT "
                                            + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "')) OR "
                             + "(majorName in (SELECT majorName "
                                            + "FROM MAJOR "
                                            + "WHERE departmentName in (SELECT departmentName "
                                                                   + "FROM DEPARTMENT_REQUIREMENT "
                                                                   + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "')))) "
                        + "AND ((NOT EXISTS (SELECT * "
                                          + "FROM YEAR_REQUIREMENT "
                                          + "WHERE projectName = '" + Main.selectedActivity.getName().get() + "')) OR "
                             + "year in (SELECT year "
                                      + "FROM YEAR_REQUIREMENT "
                                      + "WHERE projectName = 'selectedProject')); ";
                rs = stmt.executeQuery(sql);
                
                if(rs.next()) {
                  //insert application into table
                    sql = "INSERT INTO APPLICATION (username, projectName, date) " + "VALUES ('" + Main.currentUsername + "','"
                            + Main.selectedActivity.getName().get() + "', '"
                            + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "');";
                    stmt.executeUpdate(sql);

                    stmt.close();
                    conn.close();

                    alert("Application Success!", "You have successfully applied.");
                } else {
                    alert("Requirements not met!", "You don't meet the required description");
                }
            }
            
            
            

        } catch (SQLException se) {
            if (se.getMessage().contains("Duplicate")) {
                alert("You've already applied!", "Cannot apply twice to a project.");
            } else {
                alert("CHEATER!", "You can't apply for a course... ");
                se.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleBackPressed() {
        showScreen("../view/MainScreen.fxml", "Main Screen");
    }
}