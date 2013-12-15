/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * ContactUnitTest.java
 */
package junitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import java_beans.Contact;
import java_beans.MailConfig;

import org.junit.Before;
import org.junit.Test;

import databases.DBManager;

/**
 * JUnit tests for Contact CRUD functionality
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.3
 */
public class ContactUnitTest {

	private DBManager dbm;

	/**
	 * Set up method that runs before every JUnit test
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"M0831360@waldo.dawsoncollege.qc.ca",
				"waldo.dawsoncollege.qc.ca", "waldo.dawsoncollege.qc.ca",
				"M0831360@CompSci", "32D29QOY3Q", 110, 25, false,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				true, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		dbm = new DBManager(mailConfig, messages);

		if (!dbm.checkTablesExist()) {
			dbm.createContactsTable();
			dbm.createFoldersTable();
			dbm.createMailTable();
		}

		// truncate table before every test
		dbm.truncateTable("contacts");
	}

	/**
	 * Tests returning a list of all contacts from the database
	 */
	@Test
	public void testGetAllContacts() {
		// add initial contact in table
		assertTrue(dbm.addContact(new Contact(
				"anthonyvirgil.bermejo@gmail.com", "Anthony", "Bermejo")));

		// get a list of all contacts in table
		ArrayList<Contact> list = dbm.getAllContacts();

		// check size of list will be only 1 contact
		assertEquals(1, list.size());
	}

	/**
	 * Test when delete a contact from populated table with that contact
	 */
	@Test
	public void testDeleteContactPopulatedTable() {

		// add initial contact in table
		assertTrue(dbm.addContact(new Contact(
				"anthonyvirgil.bermejo@gmail.com", "Anthony", "Bermejo")));

		ArrayList<Contact> list = dbm.getAllContacts();

		// check size of list will be only 1 contact
		assertEquals(1, list.size());

		// delete that contact from the table
		dbm.deleteContact(new Contact(1, "anthonyvirgil.bermejo@gmail.com",
				"Anthony", "Bermejo"));

		list = dbm.getAllContacts();

		// size is now 0
		assertEquals(0, list.size());

	}

	/**
	 * Tests when adding a single contact to an empty database
	 */
	@Test
	public void testAddContactEmptyTable() {

		// add contact to database with no contacts there already
		assertTrue(dbm.addContact(new Contact(
				"anthonyvirgil.bermejo@gmail.com", "Anthony", "Bermejo")));

		ArrayList<Contact> list = dbm.getAllContacts();

		// number of rows is 1
		assertEquals(1, list.size());

		Contact contact = list.get(0);

		// check contact details match
		assertEquals(1, contact.getID());
		assertEquals("anthonyvirgil.bermejo@gmail.com",
				contact.getEmailAddress());
		assertEquals("Anthony", contact.getFirstName());
		assertEquals("Bermejo", contact.getLastName());

	}

	/**
	 * Test when adding a contact to an already populated database
	 */
	@Test
	public void testAddContactPopulatedTable() {

		// add a contact one after another
		assertTrue(dbm.addContact(new Contact(
				"anthonyvirgil.bermejo@gmail.com", "Anthony", "Bermejo")));
		assertTrue(dbm.addContact(new Contact("jennifer.lawrence@gmail.com",
				"Jennifer", "Lawrence")));

		ArrayList<Contact> list = dbm.getAllContacts();
		assertEquals(2, list.size());

		Contact contact = list.get(1);

		// check last contact added have the same details
		assertEquals(2, contact.getID());
		assertEquals("jennifer.lawrence@gmail.com", contact.getEmailAddress());
		assertEquals("Jennifer", contact.getFirstName());
		assertEquals("Lawrence", contact.getLastName());

	}

	/**
	 * Tests when editing a contact in a table
	 */
	@Test
	public void testEditContactPopulatedTable() {

		// add initial contact in table
		assertTrue(dbm.addContact(new Contact("jennifer.lawrence@gmail.com",
				"Jennifer", "Lawrence")));

		// edit that contact with different details
		dbm.editContact(new Contact(1, "hungergames_actress@gmail.com",
				"Jennifer-Shrader", "Lawrence"));

		ArrayList<Contact> list = dbm.getAllContacts();
		Contact contact = list.get(0);

		// check that details have been changed in table
		assertEquals(1, contact.getID());
		assertEquals("hungergames_actress@gmail.com", contact.getEmailAddress());
		assertEquals("Jennifer-Shrader", contact.getFirstName());
		assertEquals("Lawrence", contact.getLastName());

	}

}
