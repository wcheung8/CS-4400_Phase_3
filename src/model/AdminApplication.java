package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminApplication {
	private final StringProperty name = new SimpleStringProperty();
	private final StringProperty project = new SimpleStringProperty();
	private final StringProperty major = new SimpleStringProperty();
	private final StringProperty year = new SimpleStringProperty();
	private final StringProperty status = new SimpleStringProperty();

	public AdminApplication(String project, String major, String year, String status) {
		this.project.set(project);
		this.major.set(major);
		this.year.set(year);
		this.status.set(status);
	}
	
	public StringProperty getProject() {
		return project;
	}

	public StringProperty getMajor() {
		return major;
	}

	public StringProperty getYear() {
		return year;
	}

	public StringProperty getStatus() {
		return status;
	}


}