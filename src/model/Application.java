package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Application {
	private final StringProperty project = new SimpleStringProperty();
	private final StringProperty major = new SimpleStringProperty();
	private final StringProperty year = new SimpleStringProperty();
	private final StringProperty status = new SimpleStringProperty();

	public Application(String project, String major, String year, String status) {
		this.project.set(project);
		this.major.set(major);
		this.year.set(year);
		this.status.set(status);
	}

	public StringProperty projectProperty() {
		return project;
	}

	public void setProject(String project) {
		this.project.set(project);
	}
	
	public StringProperty majorProperty() {
		return major;
	}

	public void setMajor(String major) {
		this.major.set(major);
	}
	
	public StringProperty yearProperty() {
		return year;
	}

	public void setYear(String year) {
		this.year.set(year);
	}
	
	public StringProperty statusProperty() {
		return status;
	}

	public void setStatus(String status) {
		this.status.set(status);
	}




}