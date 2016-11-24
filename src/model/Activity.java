package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Activity {
	private final StringProperty name = new SimpleStringProperty();
	private final StringProperty type = new SimpleStringProperty();

	public Activity(String name, String type) {
		this.name.set(name);
		this.type.set(type);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
		;
	}

	public StringProperty getType() {
		return type;
	}

	public void setType(String type) {
		this.type.set(type);
		;
	}

}
