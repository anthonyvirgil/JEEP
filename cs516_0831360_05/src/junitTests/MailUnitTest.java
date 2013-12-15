/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailUnitTest.java
 */
package junitTests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import java_beans.Mail;
import java_beans.MailConfig;

import org.junit.Before;
import org.junit.Test;

import databases.DBManager;

/**
 * JUnit testing for Mail Message CRUD functionality
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.4
 */
public class MailUnitTest {

	private DBManager dbm;

	/**
	 * Set up method that runs before every Junit test. Truncates mail table's
	 * data.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// create mail config and database manager
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

		// truncate table before every test
		dbm.truncateTable("mail");
	}

	/**
	 * Tests returning a list of folders from the database
	 */
	@Test
	public void testGetAllMailFromFolder() {
		// add mail to be updated
		Mail mail = new Mail("patrick@test.com", "recipient@abc.com",
				"someBcc@bcc.com", "someCC@cc.com", "Test1", "This is a test",
				new Date(), "Inbox");

		dbm.addMail(mail);

		// check mail was added to database
		ArrayList<Mail> list = dbm.getAllMailFromFolder(mail.getFolder());
		assertEquals(1, list.size());
	}

	/**
	 * Tests updating a specific mail item in the database
	 */
	@Test
	public void updateMailTest() {
		Date date = new Date();

		// add mail to be updated
		Mail mail = new Mail("patrick@test.com", "recipient@abc.com",
				"someBcc@bcc.com", "someCC@cc.com", "Test1", "This is a test",
				new Date(), "Inbox");

		dbm.addMail(mail);

		// check mail was added to database
		ArrayList<Mail> list = dbm.getAllMailFromFolder(mail.getFolder());
		assertEquals(1, list.size());

		// change values of that mail and update
		mail = list.get(0);
		mail.setSubject("Updated subject");
		mail.setFolder("Deleted");
		mail.setMessage("UPDATED HERE");
		dbm.updateMail(mail);

		list = dbm.getAllMailFromFolder(mail.getFolder());

		// check all contents from database are same as expected
		mail = list.get(0);
		assertEquals(1, mail.getID());
		assertEquals("patrick@test.com", mail.getSender());
		assertEquals("recipient@abc.com", mail.getToRecipientListString());
		assertEquals("someBcc@bcc.com", mail.getBccRecipientListString());
		assertEquals("someCC@cc.com", mail.getCcRecipientListString());
		assertEquals("Updated subject", mail.getSubject());
		assertEquals("UPDATED HERE", mail.getMessage());
		// cannot check for timestamps because milliseconds will always be off
		// assertEquals(new Timestamp(date.getTime()), mail.getDate());
		assertEquals("Deleted", mail.getFolder());
	}

	/**
	 * Tests adding a mail object to a populated tabled
	 */
	@Test
	public void addMailPopulatedTableTest() {
		Date date = new Date();

		// inital mail already exists in table
		Mail mail1 = new Mail("patrick@test.com", "recipient@abc.com",
				"someBcc@bcc.com", "someCC@cc.com", "Test1", "This is a test",
				date, "Inbox");
		dbm.addMail(mail1);

		// adding a new mail
		Mail mail2 = new Mail("anthony@test.com", "recipient@abc.com",
				"someBcc@bcc.com", "someCC@cc.com", "Test2", "Will be deleted",
				date, "Inbox");
		dbm.addMail(mail2);

		ArrayList<Mail> list = dbm.getAllMailFromFolder(mail1.getFolder());

		// check that size of list is now 2
		assertEquals(2, list.size());

		// check all contents from database are same as expected
		Mail mail3 = list.get(1);
		assertEquals(2, mail3.getID());
		assertEquals("anthony@test.com", mail3.getSender());
		assertEquals("recipient@abc.com", mail3.getToRecipientListString());
		assertEquals("someBcc@bcc.com", mail3.getBccRecipientListString());
		assertEquals("someCC@cc.com", mail3.getCcRecipientListString());
		assertEquals("Test2", mail3.getSubject());
		assertEquals("Will be deleted", mail3.getMessage());
		// cannot check for timestamps because milliseconds will always be off
		// assertEquals(new Timestamp(date.getTime()), mail.getDate());
		assertEquals("Inbox", mail3.getFolder());
	}

	/**
	 * Tests adding a mail object to an empty table
	 */
	@Test
	public void addMailEmptyTableTest() {
		Date date = new Date();

		Mail mail = new Mail("anthony@test.com", "recipient@abc.com",
				"someBcc@bcc.com", "someCC@cc.com", "Test1", "This is a test",
				date, "Inbox");

		dbm.addMail(mail);

		ArrayList<Mail> list = dbm.getAllMailFromFolder(mail.getFolder());

		// check that size of list is now 1
		assertEquals(1, list.size());

		mail = list.get(0);

		// check all contents from database are same as expected
		assertEquals(1, mail.getID());
		assertEquals("anthony@test.com", mail.getSender());
		assertEquals("recipient@abc.com", mail.getToRecipientListString());
		assertEquals("someBcc@bcc.com", mail.getBccRecipientListString());
		assertEquals("someCC@cc.com", mail.getCcRecipientListString());
		assertEquals("Test1", mail.getSubject());
		assertEquals("This is a test", mail.getMessage());
		// cannot check for timestamps because milliseconds will always be off
		// assertEquals(new Timestamp(date.getTime()), mail.getDate());
		assertEquals("Inbox", mail.getFolder());
	}

	/**
	 * Tests deleting a specified mail object from the database
	 */
	@Test
	public void deleteMailTest() {
		Date date = new Date();

		Mail mail = new Mail("anthony@test.com", "recipient@abc.com",
				"someBcc@bcc.com", "someCC@cc.com", "Test1", "This is a test",
				date, "Inbox");

		dbm.addMail(mail);

		ArrayList<Mail> list = dbm.getAllMailFromFolder("Inbox");

		// check size of list will be only 1 mail
		assertEquals(1, list.size());

		mail = list.get(0);

		dbm.deleteMail(mail);

		list = dbm.getAllMailFromFolder("Inbox");

		// check that size of list is now 0 after deletion
		assertEquals(0, list.size());
	}
}
