/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * CreateTablesUnitTest.java
 */
package junitTests;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.ResourceBundle;

import java_beans.MailConfig;

import mail.MailTableModel;

import org.junit.Before;
import org.junit.Test;

import databases.DBManager;

/**
 * Junit testing for initial database table set up
 * 
 * @author Anthony
 * @version 1.1
 */
public class CreateTablesUnitTest {

	private DBManager dbm;
	private MailTableModel model;

	/**
	 * Set up method that runs before every JUnit test
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"M0831360@waldo.dawsoncollege.qc.ca",
				"waldo.dawsoncollege.qc.ca", "waldo.dawsoncollege.qc.ca",
				"M0831360@CompSci", "32D29QOY3Q", 110, 25, false,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				true, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());

		model = new MailTableModel(messages);
		dbm = new DBManager(mailConfig, messages);

		/*
		 * if tables don't exists create tables and insert values if needed
		 * (folders)
		 */
		if (!dbm.checkTablesExist()) {
			dbm.createContactsTable();
			dbm.createFoldersTable();
			dbm.createMailTable();
		}
	}

	/**
	 * Tests that all 3 tables exist in the database
	 */
	@Test
	public void tablesExistTest() {
		// tables exists in database, will return true
		assertTrue(dbm.checkTablesExist());
	}

	/**
	 * Tests that no tables exists in the database
	 */
	@Test
	public void tablesDoNotExistTest() {
		// drop all tables
		dbm.dropTable("contacts");
		dbm.dropTable("folders");
		dbm.dropTable("mail");

		// tables don't exists, will return false
		assertFalse(dbm.checkTablesExist());
	}

	/**
	 * Tests that when there are no tables in database, they will be created
	 */
	@Test
	public void createTablesTest() {

		// drop all tables
		dbm.dropTable("contacts");
		dbm.dropTable("folders");
		dbm.dropTable("mail");

		/*
		 * if tables don't exists create tables and insert values if needed
		 * (folders)
		 */
		if (!dbm.checkTablesExist()) {
			dbm.createContactsTable();
			dbm.createFoldersTable();
			dbm.createMailTable();
		}

		// returns true because tables have been recreated
		assertTrue(dbm.checkTablesExist());
	}
}
