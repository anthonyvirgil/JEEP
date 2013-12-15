/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * FolderUnitTest.java
 */
package junitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import java_beans.Folder;
import java_beans.MailConfig;

import org.junit.Before;
import org.junit.Test;

import databases.DBManager;

/**
 * JUnit testing for folder CRUD functionality
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.6
 */
public class FolderUnitTest {

	private DBManager dbm;

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

		dbm.truncateTable("folders");
	}

	/**
	 * Tests returning a list of all folders in the database
	 */
	@Test
	public void testGetFolders() {
		assertTrue(dbm.addFolder(new Folder("Test Folder")));

		ArrayList<Folder> list = dbm.getFolders();

		// check that number of folders in table is 1
		assertEquals(1, list.size());
	}

	/**
	 * Test when adding a folder to an empty table
	 */
	@Test
	public void testAddFolderEmptyTable() {
		assertTrue(dbm.addFolder(new Folder("Test Folder")));

		ArrayList<Folder> list = dbm.getFolders();

		// check that number of folders in table is 1
		assertEquals(1, list.size());

		Folder folder = list.get(0);

		// check that details of folder are same as what was added
		assertEquals(1, folder.getID());
		assertEquals("Test Folder", folder.getName());
	}

	/**
	 * Test when adding a folder to a populated table
	 */
	@Test
	public void testAddFolderPopulatedTable() {
		// add initial folder in table
		assertTrue(dbm.addFolder(new Folder("Test Folder")));

		// add another folder into the table
		assertTrue(dbm.addFolder(new Folder("Added Folder")));

		ArrayList<Folder> list = dbm.getFolders();

		// check that number of folders in table is 2
		assertEquals(2, list.size());

		Folder folder = list.get(1);

		// check that details of folder are same as what was added
		assertEquals(2, folder.getID());
		assertEquals("Added Folder", folder.getName());
	}

	/**
	 * Tests when editing a folder in a table
	 */
	@Test
	public void testEditFolderPopulatedTable() {
		// add initial folder in table
		assertTrue(dbm.addFolder(new Folder("Test Folder")));

		// edit that folder with a different name
		dbm.editFolder(new Folder(1, "Edited Folder"));

		ArrayList<Folder> list = dbm.getFolders();
		Folder folder = list.get(0);

		assertEquals("Edited Folder", folder.getName());
	}

	/**
	 * Tests when deleting a folder in a table
	 */
	@Test
	public void testDeleteFolderPopulatedTable() {
		// add initial folder in table
		assertTrue(dbm.addFolder(new Folder("Test Folder")));

		ArrayList<Folder> list = dbm.getFolders();

		// check size of list will be only 1 folder
		assertEquals(1, list.size());

		// delete that folder from the table
		dbm.deleteFolder(new Folder(1, "Test Folder"));

		list = dbm.getFolders();

		// size is now 0
		assertEquals(0, list.size());
	}
}
