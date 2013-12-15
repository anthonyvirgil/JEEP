/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailSender.java
 */
package mail;

import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import com.mysql.jdbc.Messages;

import java_beans.Mail;
import java_beans.MailConfig;

/**
 * Sends mail from a Gmail account or SMTP account
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.4
 */
public class MailSender {
	// class variables
	private MailConfig mailConfig;
	private boolean junit = false;
	private ResourceBundle messages;
	private static final boolean DEBUG = false;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * MailSender constructor
	 * 
	 * @param mailConfig
	 *            Mail configuration of the user
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailSender(MailConfig mailConfig, ResourceBundle messages) {
		this.mailConfig = mailConfig;
		this.messages = messages;
		junit = false;
	}

	/**
	 * Sends a mail through either Gmail or SMTP
	 * 
	 * @param mail
	 *            Mail data to be sent
	 * @return Success or failure of sending mail
	 * @throws Exception
	 * @throws MessagingException
	 * @throws NoSuchProviderException
	 * @throws AddressException
	 */
	public boolean sendMail(Mail mail) throws MessagingException {
		boolean retVal = true;

		if (mailConfig.isGmail())
			// if user's email is a gmail account
			retVal = gmailSend(mail);
		else
			// if user's email is smtp account
			retVal = smtpSend(mail);

		// display recipients for debugging
		if (DEBUG) {
			System.out.println("TO: " + mail.getToRecipientList());
			System.out.println("CC: " + mail.getCcRecipientList());
			System.out.println("BCC: " + mail.getBccRecipientList());
		}
		return retVal;
	}

	/**
	 * Determines whether or not junit testing is being performed
	 * 
	 * @param junit
	 *            Whether or not junit testing is being performed
	 */
	public void setJUnit(boolean junit) {
		this.junit = junit;
	}

	/**
	 * Sends a message from a SMTP account
	 * 
	 * @param mail
	 *            Mail data to be sent
	 * @return Success or failure of sending mail
	 * @throws MessageException
	 */
	private boolean smtpSend(Mail mail) throws MessagingException {
		boolean retVal = true;
		Session session = null;

		try {
			// create a properties object
			Properties smtpProps = new Properties();

			// add mail configuration to the properties
			smtpProps.put("mail.transport.protocol", "smtp");
			smtpProps.put("mail.smtp.host", mailConfig.getSmtp_URL());
			smtpProps.put("mail.smtp.port", mailConfig.getSmtp_portnum());

			// check if needs authentication
			if (mailConfig.isSMTPAuth()) {
				smtpProps.put("mail.smtp.auth", "true");
				Authenticator auth = new SMTPAuthenticator();
				session = Session.getInstance(smtpProps, auth);
			} else
				session = Session.getDefaultInstance(smtpProps);

			// display the conversation between the client and server
			if (DEBUG)
				session.setDebug(true);

			// create a new message
			MimeMessage msg = new MimeMessage(session);

			// set the single sender field
			msg.setFrom(new InternetAddress(mailConfig.getEmail()));

			// set the To, CC, and BCC from their ArrayLists
			for (String emailAddress : mail.getToRecipientList())
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						emailAddress, false));

			if (mail.getCcRecipientList() != null)
				for (String emailAddress : mail.getCcRecipientList())
					msg.addRecipient(Message.RecipientType.CC,
							new InternetAddress(emailAddress, false));

			if (mail.getBccRecipientList() != null)
				for (String emailAddress : mail.getBccRecipientList())
					msg.addRecipient(Message.RecipientType.BCC,
							new InternetAddress(emailAddress, false));

			// set the subject
			msg.setSubject(mail.getSubject());

			// set the message body
			msg.setContent(mail.getMessage(), "text/html; charset=utf-8");

			// set the date of mail to when it is being sent
			msg.setSentDate(new Date());
			mail.setDate(new Date());

			if (mailConfig.isSMTPAuth()) {
				// authentication is required
				Transport transport = session.getTransport();
				transport.connect();
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
			} else
				// authentication not required
				Transport.send(msg);

		} catch (MessagingException e) {
			if (junit)
				throw e;
			else {
				// log error
				logger.log(Level.WARNING, "Error in message while sending.");
			}
			retVal = false;
		} catch (Exception e) {
			if (junit)
				throw e;
			else {
				// log error
				logger.log(Level.WARNING, "Unexpected error while sending.");
			}
			retVal = false;
		}
		return retVal;
	}

	/**
	 * Send a message from a Gmail account
	 * 
	 * @param mail
	 *            Mail data to send
	 * @return Success or failure
	 * @throws NoSuchProviderException
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws Exception
	 */
	private boolean gmailSend(Mail mail) throws MessagingException {
		boolean retVal = true;
		Transport transport = null;

		try {
			// create a properties object
			Properties smtpProps = new Properties();

			// add mail configuration to the properties
			smtpProps.put("mail.transport.protocol", "smtps");
			smtpProps.put("mail.smtps.host", mailConfig.getSmtp_URL());
			smtpProps.put("mail.smtps.auth", "true");
			smtpProps.put("mail.smtps.quitwait", "false");

			// create a mail session
			Session mailSession = Session.getInstance(smtpProps);

			// display the conversation between the client and server
			if (DEBUG)
				mailSession.setDebug(true);

			// create transport object
			transport = mailSession.getTransport();

			// create a new message
			MimeMessage msg = new MimeMessage(mailSession);

			// set the single sender field
			msg.setFrom(new InternetAddress(mailConfig.getEmail()));

			// set the To, CC, and BCC from their ArrayLists
			for (String emailAddress : mail.getToRecipientList())
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						emailAddress, false));

			if (mail.getCcRecipientList() != null)
				for (String emailAddress : mail.getCcRecipientList())
					msg.addRecipient(Message.RecipientType.CC,
							new InternetAddress(emailAddress, false));

			if (mail.getBccRecipientList() != null)
				for (String emailAddress : mail.getBccRecipientList())
					msg.addRecipient(Message.RecipientType.BCC,
							new InternetAddress(emailAddress, false));

			// set the subject line
			msg.setSubject(mail.getSubject());

			// set the message body
			msg.setContent(mail.getMessage(), "text/html; charset=utf-8");

			// set the date of mail to when it is being sent
			msg.setSentDate(new Date());
			mail.setDate(new Date());

			// connect and authenticate to the server
			transport.connect(mailConfig.getSmtp_URL(),
					mailConfig.getSmtp_portnum(),
					mailConfig.getPop3_username(),
					mailConfig.getPop3_password());

			// send the message
			transport.sendMessage(msg, msg.getAllRecipients());

			// close the connection
			transport.close();

		} catch (MessagingException e) {
			if (junit)
				throw e;
			else {
				// log error
				logger.log(Level.WARNING, "Error in message while sending.");
			}
			retVal = false;
		} catch (Exception e) {
			if (junit)
				throw e;
			else {
				// log error
				logger.log(Level.WARNING, "Unexpected error while sending.");
			}
			retVal = false;
		}
		return retVal;
	}

	/**
	 * Private class that authenticates the username and password of user's
	 * account
	 * 
	 * @author Kenneth Fogel
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = mailConfig.getPop3_username();
			String password = mailConfig.getPop3_password();
			if (DEBUG)
				System.out.println(username + "\t" + password);
			return new PasswordAuthentication(username, password);
		}
	}
}
