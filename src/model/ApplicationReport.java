package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApplicationReport {
	private final StringProperty project = new SimpleStringProperty();
	private final IntegerProperty numApplicants = new SimpleIntegerProperty();
	private final StringProperty acceptRate = new SimpleStringProperty();
	private final StringProperty topMajors = new SimpleStringProperty();

	public ApplicationReport(String project, int numApplicants, String acceptRate, String topMajors) {
		this.project.set(project);
		this.numApplicants.set(numApplicants);
		this.acceptRate.set(acceptRate);
		this.topMajors.set(topMajors);
	}

	public StringProperty getProjectName() {
		return project;
	}
	public IntegerProperty getNumApplicants() {
		return numApplicants;
	}

	public StringProperty getAcceptRate() {
		return acceptRate;
	}

	public StringProperty getTopMajors() {
		return topMajors;
	}
}