/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailSendUnitTest.java
 */
package junitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;

import java_beans.Mail;
import java_beans.MailConfig;

import mail.MailSender;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit testing for sending mail through Gmail
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.1
 */
public class MailSendUnitTest {
	private MailSender mailSender;

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tests sending mail with correct information to one TO recipient
	 * 
	 * @throws AddressException
	 */
	@Test
	public void waldoMailSendTest() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvbermejo@gmail.com", "", "", "Test", "Hello",
				new Date(), "Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with correct information to multiple TO recipients
	 */
	@Test
	public void waldoMailSendTest2() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvirgil.bermejo@gmail.com, anthonyvbermejo@gmail.com",
				"", "", "Test2", "Hello", new Date(), "Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with correct information to TO recipient and a BCC
	 * recipient
	 */
	@Test
	public void waldoMailSendTest3() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvbermejo@gmail.com", "anthonyvirgil.bermejo@gmail.com",
				"", "Test3", "Hello", new Date(), "Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with correct information to TO recipient and multiple
	 * BCC recipients
	 */
	@Test
	public void waldoMailSendTest4() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvbermejo@gmail.com",
				"anthonyvirgil.bermejo@gmail.com, z3phyr01@hotmail.com", "",
				"Test4", "Hello", new Date(), "Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with correct information to TO recipient and a CC
	 * recipient
	 */
	@Test
	public void waldoMailSendTest5() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvbermejo@gmail.com", "",
				"anthonyvirgil.bermejo@gmail.com", "Test5", "Hello",
				new Date(), "Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with correct information to TO recipient and multiple
	 * CC recipients
	 */
	@Test
	public void waldoMailSendTest6() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvbermejo@gmail.com", "",
				"anthonyvirgil.bermejo@gmail.com, z3phyr01@hotmail.com",
				"Test6", "Hello", new Date(), "Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with correct information to TO recipient, a BCC
	 * recipient and CC recipient
	 */
	@Test
	public void waldoMailSendTest7() throws MessagingException, Exception {
		// create mailconfig object with all correct information
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvbermejo@gmail.com", "anthonyvirgil.bermejo@gmail.com",
				"z3phyr01@hotmail.com", "Test7", "Hello", new Date(),
				"Ready to Send");

		// send message, returns true if sending is successful
		assertTrue(mailSender.sendMail(mail));
	}

	/**
	 * Tests sending mail with invalid information (invalid email)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailSendInvalidTest() throws MessagingException, Exception {
		// create mailConfig object with an invalid email
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"FAKE EMAIL", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvirgil.bermejo@gmail.com", "", "", "Test", "Hello",
				new Date(), "Ready to Send");

		// send message, returns false because sending will be a failure
		try {
			mailSender.sendMail(mail);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests sending mail with invalid information (incorrect SMTP address)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailSendInvalidTest2() throws MessagingException,
			Exception {
		// create mailConfig object with an incorrect SMTP address
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com",
				"INCORRECT SMTP ADDRESS", "anthonyvbermejo@gmail.com",
				"n1ntendo", 995, 465, true, "waldo.dawsoncollege.qc.ca", 3306,
				"D0831360", "facenthi", false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvirgil.bermejo@gmail.com", "", "", "Test", "Hello",
				new Date(), "Ready to Send");

		// send message, returns false because sending will be a failure
		try {
			mailSender.sendMail(mail);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Tests sending mail with invalid information (wrong SMTP port)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailSendInvalidTest3() throws MessagingException,
			Exception {
		// create mailConfig object with a wrong port number
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 12345, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvirgil.bermejo@gmail.com", "", "", "Test", "Hello",
				new Date(), "Ready to Send");

		// send message, returns false because sending will be a failure
		try {
			mailSender.sendMail(mail);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests sending mail with invalid information (incorrect to recipient)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailSendInvalidTest4() throws MessagingException,
			Exception {
		// create mailConfig object with correct info
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(), "FAKE EMAIL", "", "",
				"Test", "Hello", new Date(), "Ready to Send");

		// send message, returns false because sending will be a failure
		try {
			mailSender.sendMail(mail);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests sending mail with invalid information (incorrect bcc recipient)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailSendInvalidTest5() throws MessagingException,
			Exception {
		// create mailConfig object with correct info
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvirgil.bermejo@gmail.com", "FAKE BCC RECIPIENT", "",
				"Test", "Hello", new Date(), "Ready to Send");

		// send message, returns false because sending will be a failure
		try {
			mailSender.sendMail(mail);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests sending mail with invalid information (incorrect cc recipient)
	 */
	@Test(expected = MessagingException.class)
	public void waldoMailSendInvalidTest6() throws MessagingException,
			Exception {
		// create mailConfig object with a wrong port number
		MailConfig mailConfig = new MailConfig("Anthony Bermejo",
				"anthonyvbermejo@gmail.com", "pop.gmail.com", "smtp.gmail.com",
				"anthonyvbermejo@gmail.com", "n1ntendo", 995, 465, true,
				"waldo.dawsoncollege.qc.ca", 3306, "D0831360", "facenthi",
				false, Locale.getDefault());
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",
				Locale.getDefault());
		mailSender = new MailSender(mailConfig, messages);
		mailSender.setJUnit(true);

		// create a mail message
		Mail mail = new Mail(1, mailConfig.getEmail(),
				"anthonyvirgil.bermejo@gmail.com", "", "FAKE CC RECIPIENT",
				"Test", "Hello", new Date(), "Ready to Send");

		// send message, returns false because sending will be a failure
		try {
			mailSender.sendMail(mail);
		} catch (MessagingException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
}
