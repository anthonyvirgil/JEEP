/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * ContactModel.java
 */
package contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import java_beans.Contact;

/**
 * Represents the model in the MVC pattern for ContactList UIs
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.3
 */
public class ContactModel extends Observable {

	private ArrayList<Contact> contactList;
	private Contact contact;

	/**
	 * ContactModel constructor
	 */
	public ContactModel() {
		super();
		contactList = new ArrayList<Contact>();
	}

	/**
	 * Receives an ArrayList of contacts and sorts them by name
	 * 
	 * @param contactList
	 *            List of contacts
	 */
	public void receiveContacts(ArrayList<Contact> contactList) {
		this.contactList = contactList;
		Collections.sort(contactList);

		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the list of contacts contained in the model
	 * 
	 * @return List of contacts
	 */
	public ArrayList<Contact> getContactList() {
		return contactList;
	}

	/**
	 * Sets a contact to the model's contact class variable
	 * 
	 * @param contact
	 *            Contact to be set
	 */
	public void addContact(Contact contact) {
		this.contact = contact;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the contact contained in the model
	 * 
	 * @return Contact
	 */
	public Contact getContact() {
		return contact;
	}
}
