package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Project {
	private final StringProperty name = new SimpleStringProperty();
	private final IntegerProperty numApplicants = new SimpleIntegerProperty();
	
	public Project(String name, int numApplicants) {
		this.name.set(name);
		this.numApplicants.set(numApplicants);
		// TODO Auto-generated constructor stub
	}
	
	public StringProperty getProjectName() {
		return name;
	}
	
	public IntegerProperty getNumApplicants() {
		return numApplicants;
	}

}
