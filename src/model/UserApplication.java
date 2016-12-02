package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserApplication {
	private final StringProperty date = new SimpleStringProperty();
	private final StringProperty project = new SimpleStringProperty();
	private final StringProperty status = new SimpleStringProperty();

	public UserApplication(String date, String project, String status) {
		this.date.set(date);
		this.project.set(project);
		this.status.set(status);
	}

	public StringProperty getDate() {
		return date;
	}

	public StringProperty getProject() {
		return project;
	}

	public StringProperty getStatus() {
		return status;
	}
}