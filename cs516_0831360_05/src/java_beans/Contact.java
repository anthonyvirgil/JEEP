/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * Contact.java
 */
package java_beans;

import java.io.Serializable;

/**
 * Java Bean for a Contact object in the J.E.E.P application
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.0f
 */
public class Contact implements Serializable, Comparable<Contact> {

	private static final long serialVersionUID = 5036454593246835400L;
	private int id;
	private String firstName;
	private String lastName;
	private String emailAddress;

	/**
	 * Default constructor setting instance variables to their defaults.
	 */
	public Contact() {
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.emailAddress = "";
	}

	/**
	 * Constructor setting all instance variables to what the user defines.
	 * 
	 * @param firstName
	 *            First name of the contact
	 * @param lastName
	 *            Last name of contact
	 * @param emailAddress
	 *            Email address of the contact
	 */
	public Contact(String emailAddress, String firstName, String lastName) {
		super();
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Constructor setting all instance variables to what the user defines.
	 * 
	 * @param id
	 *            ID of the contact
	 * @param firstName
	 *            First name of the contact
	 * @param lastName
	 *            Last name of contact
	 * @param emailAddress
	 *            Email address of the contact
	 */
	public Contact(int id, String emailAddress, String firstName,
			String lastName) {
		super();
		this.id = id;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Returns the id of the contact
	 * 
	 * @return ID of the contact
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the id of the contact
	 * 
	 * @param id
	 *            ID of the contact to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Returns the first name of the contact
	 * 
	 * @return First name of the contact
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the contact
	 * 
	 * @param name
	 *            First name of the contact to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of the contact
	 * 
	 * @return Last name of the contact
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the contact
	 * 
	 * @param lastName
	 *            Last name of the contact to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the email address of the contact
	 * 
	 * @return Email address of the contact
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the email address of the contact
	 * 
	 * @param emailAddress
	 *            Email address of the contact to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Returns the serial UID of the object
	 * 
	 * @return The serial UID of the object
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Overridden toString method displaying the first and last name of contact
	 */
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

	/**
	 * Overridden compareTo method sorting contacts by their names
	 */
	@Override
	public int compareTo(Contact c) {
		int result;

		// compare last names
		if (this.getLastName().compareTo(c.getLastName()) < 0)
			result = -1;
		else if (this.getLastName().compareTo(c.getLastName()) > 0)
			result = 1;
		else {
			// if last names are equal, check first names
			if (this.getFirstName().compareTo(c.getFirstName()) < 0)
				result = -1;
			else if (this.getFirstName().compareTo(c.getFirstName()) > 0)
				result = 1;
			else
				result = 0;
		}
		return result;
	}
}
