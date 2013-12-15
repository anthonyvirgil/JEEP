/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailReceiveUnitTest.java
 */
package junitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.AuthenticationFailedException;

import java_beans.Mail;
import java_beans.MailConfig;

import mail.MailReceiver;

import org.junit.Before;
import org.junit.Test;

import databases.DBManager;

/**
 * JUnit testing for receiving mail from Gmail
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.0
 */
public class MailReceiveUnitTest {
	private MailReceiver mailReceiver;
	private ArrayList<Mail> mailList;

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tests receiving mail with correct information
	 */
	@Test
	public void waldoMailReceiveTest() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		DBManager dbm = new DBManager(mailConfig, messages);
		mailReceiver = new MailReceiver(mailConfig, dbm, messages);
		mailReceiver.setJUnit(true);
		mailList = new ArrayList<Mail>();

		// receive mail into a list of mails, no exceptions are thrown
		mailList = mailReceiver.getMail();
	}

	/**
	 * Tests receiving mail with invalid information (invalid pop3 username)
	 */
	@Test(expected = AuthenticationFailedException.class)
	public void waldoMailReceiveInvalidTest() throws MessagingException,
			Exception {
		// create mailConfig object with an invalid pop3 username
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"INVALID USERNAME", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		DBManager dbm = new DBManager(mailConfig, messages);
		mailReceiver = new MailReceiver(mailConfig, dbm, messages);
		mailReceiver.setJUnit(true);
		mailList = new ArrayList<Mail>();

		// receive mail into a list of mails, no exceptions are thrown
		mailList = mailReceiver.getMail();
	}

	/**
	 * Tests receiving mail with invalid information (invalid pop3 password)
	 */
	@Test(expected = AuthenticationFailedException.class)
	public void waldoMailReceiveInvalidTest2() throws MessagingException,
			Exception {
		// create mailConfig object with an invalid pop3 password
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "WRONG PASSWORD", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());

		DBManager dbm = new DBManager(mailConfig, messages);
		mailReceiver = new MailReceiver(mailConfig, dbm, messages);
		mailReceiver.setJUnit(true);
		mailList = new ArrayList<Mail>();

		// receive mail into a list of mails, no exceptions are thrown
		mailList = mailReceiver.getMail();
	}

	/**
	 * Tests sending mail with invalid information (incorrect pop3 url)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailReceiveInvalidTest3() throws MessagingException,
			Exception {
		// create mailConfig object with an invalid pop3 url
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "INVALID POP3 URL",
				"smtp.gmail.com", "anthonyvbermejo@gmail.com", "n1ntendo", 995,
				465, true, "waldo.dawsoncollege.qc.ca", 3306, "D0831360",
				"facenthi", false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());

		DBManager dbm = new DBManager(mailConfig, messages);
		mailReceiver = new MailReceiver(mailConfig, dbm, messages);
		mailReceiver.setJUnit(true);
		mailList = new ArrayList<Mail>();

		// receive mail into a list of mails, no exceptions are thrown
		mailList = mailReceiver.getMail();
	}

	/**
	 * Tests sending mail with invalid information (wrong pop3 port)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailReceiveInvalidTest4() throws MessagingException,
			Exception {
		// create mailconfig object with invalid pop3 port number
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 9000, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		DBManager dbm = new DBManager(mailConfig, messages);
		mailReceiver = new MailReceiver(mailConfig, dbm, messages);
		mailReceiver.setJUnit(true);
		mailList = new ArrayList<Mail>();

		mailList = mailReceiver.getMail();
	}
}