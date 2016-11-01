package model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Wesley
 *
 * Represents a single user in the system
 */
public class User {
    /**
     * Properties are a way of binding data under the JavaBeans specification.
     *
     * See the article at: http://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm
     * for more details.
     */
    private final StringProperty _name = new SimpleStringProperty();
    private final StringProperty _password = new SimpleStringProperty();
    private StringProperty _email = new SimpleStringProperty("N/A");
    private StringProperty _numb = new SimpleStringProperty("N/A");
    private StringProperty _address = new SimpleStringProperty("N/A");
    
    /* **********************
     * Getters and setters for properties
     */
    public String getName() { return _name.get(); }
    public void setName(String name) { _name.set(name); }

    public String getPassword() {return _password.get(); }
    public void setPassword(String password) { _password.set(password); }
	
	public String getEmail() { return _email.get(); }
	public void setEmail(String email) { _email.set(email); }
	
	public String getPhoneNumber() { return _numb.get(); }
	public void setNumber(String numb) { _numb.set(numb); }
	
	public String getAddress() { return _address.get(); }
	public void setAddress(String address) { _address.set(address); }
	
	/**
     * Make a new user
     * @param name      the user's name
     * @param password     the user's password
     * @param AuthorizationLevel     the user's Authorization Level
     */
    public User(String name, String password) {
        _name.set(name);
        _password.set(password);
    }

    /**
     * No param constructor -- DO NOT CALL NORMALLY
     * This constructor only for GUI use in edit/new user dialog
     */
    public User() {
        _name.set("enter new name");
        _password.set("enter new password");
    }
}