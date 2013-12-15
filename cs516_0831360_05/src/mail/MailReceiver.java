/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailReceiver.java
 */
package mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JOptionPane;

import app.JEEP_GUI;

import com.mysql.jdbc.Messages;
import com.sun.mail.pop3.POP3SSLStore;

import databases.DBManager;

import java_beans.Mail;
import java_beans.MailConfig;

/**
 * Receives mail from a POP3 server
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.3
 */
public class MailReceiver {

	// instance variables
	private MailConfig mailConfig = null;
	private ArrayList<Mail> mailList = null;
	private DBManager dbm = null;
	private ResourceBundle messages = null;
	private boolean junit = false;
	private static final boolean DEBUG = false;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * MailReceiver constructor
	 * 
	 * @param mailConfig
	 *            Mail configuration of the user
	 * @param dbm
	 *            Database manager that communicates with the database
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailReceiver(MailConfig mailConfig, DBManager dbm,
			ResourceBundle messages) {
		this.mailConfig = mailConfig;
		this.dbm = dbm;
		this.messages = messages;
		mailList = new ArrayList<Mail>();
		this.junit = false;
	}

	/**
	 * Receives mail into an ArrayList of Mail object
	 * 
	 * @return ArrayList of mail messages that were received from POP3 server
	 * @throws MessagingException
	 * @throws Exception
	 */
	public ArrayList<Mail> getMail() throws MessagingException, Exception {
		boolean retVal = true;

		// receive mail from server
		retVal = mailReceive();

		// if failure to receive mail, clear list and display error
		if (!retVal) {
			JOptionPane.showMessageDialog(null,
					messages.getString("mailRetrieveError"),
					messages.getString("mailRetrieveErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
			mailList.clear();
		}

		return mailList;
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
	 * Retrieve mail from POP3 server
	 * 
	 * @return Success or failure of receiving mail
	 */
	private boolean mailReceive() throws MessagingException, Exception {
		boolean retVal = true;

		Store store = null;
		Folder folder = null;
		Session session = null;

		// create new Properties object
		Properties pop3Props = new Properties();

		try {
			// mail configuration is a Gmail account
			if (mailConfig.isGmail()) {
				// store configuration for receiving mail in properties
				pop3Props.setProperty("mail.pop3.host",
						mailConfig.getPop3_URL());
				pop3Props.setProperty("mail.user",
						mailConfig.getPop3_username());
				pop3Props.setProperty("mail.passwd",
						mailConfig.getPop3_password());
				pop3Props.setProperty("mail.pop3.port",
						String.valueOf(mailConfig.getPop3_portnum()));
				String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				pop3Props.setProperty("mail.pop3.socketFactory.class",
						SSL_FACTORY);
				pop3Props.setProperty("mail.pop3.socketFactory.port",
						String.valueOf(mailConfig.getPop3_portnum()));
				pop3Props.setProperty("mail.pop3.ssl", "true");
				pop3Props.setProperty("mail.pop3.socketFactory.fallback",
						"false");

				// create url to pop3 server with mail config values
				URLName url = new URLName("pop3://"
						+ pop3Props.getProperty("mail.user") + ":"
						+ pop3Props.getProperty("mail.passwd") + "@"
						+ pop3Props.getProperty("mail.pop3.host") + ":"
						+ pop3Props.getProperty("mail.pop3.port"));

				// create mail session object
				session = Session.getInstance(pop3Props);

				// get hold of a POP3 message store and connect
				store = new POP3SSLStore(session, url);

			}
			// a pop3 config
			else {
				// create a mail session
				session = Session.getDefaultInstance(pop3Props);

				// get hold of a POP3 message store and connect
				store = session.getStore("pop3");
			}

			// set to true if debugging
			if (DEBUG)
				session.setDebug(true);

			// connect to server
			store.connect(mailConfig.getPop3_URL(),
					mailConfig.getPop3_username(),
					mailConfig.getPop3_password());

			// get the default folder
			folder = store.getDefaultFolder();
			if (folder == null)
				throw new Exception(messages.getString("noDefaultFolder"));

			// get the inbox from the default folder
			folder = folder.getFolder("INBOX");
			if (folder == null)
				throw new Exception(messages.getString("noPOP3Inbox"));

			// open the folder for read/write, can delete messages
			folder.open(Folder.READ_WRITE);

			// get all the messages
			Message[] msgs = folder.getMessages();

			// process the messages into beans
			retVal = process(msgs);

		} catch (MessagingException e) {
			if (junit)
				throw e;
			else {
				// log error
				logger.log(Level.SEVERE,
						"Error with mail message while receiving.");
			}
			retVal = false;
		} catch (Exception e) {
			if (junit)
				throw e;
			else {
				// log error
				logger.log(Level.SEVERE,
						"Unexpected error occurred receiving messages.");
			}
			retVal = false;
		} finally {
			try {
				if (folder != null)
					folder.close(true);
				if (store != null)
					store.close();
			} catch (Exception ex2) {
				if (junit)
					throw ex2;
				else {
					// log error
					logger.log(Level.WARNING,
							"Error closing folder on POP3 server.");
				}
			}
		}
		return retVal;
	}

	/**
	 * Process array of messages into Mail beans
	 * 
	 * @param messageArray
	 *            Array of messages received
	 * @return Success or failure
	 */
	private boolean process(Message[] messageArray) {

		boolean retVal = true;
		Mail mail = null;

		// loop through array of messages, creating a Mail bean for each message
		for (int i = 0; i < messageArray.length; i++) {
			try {
				mail = new Mail();

				// get & set sender
				String sender = null;
				try {
					sender = ((InternetAddress) messageArray[i].getFrom()[0])
							.getAddress();
					mail.setSender(sender);

				} catch (AddressException e) {
					// if there is no sender found, set empty to sender field
					sender = "";
					mail.setSender(sender);
				}

				// get & set to recipients
				InternetAddress[] toList = (InternetAddress[]) messageArray[i]
						.getRecipients(Message.RecipientType.TO);
				ArrayList<String> toRecipientList = new ArrayList<String>();

				if (toList != null) {
					for (InternetAddress address : toList)
						toRecipientList.add(address.getAddress());
				}
				mail.setToRecipientList(toRecipientList);

				// get & set cc recipients
				InternetAddress[] ccList = (InternetAddress[]) messageArray[i]
						.getRecipients(Message.RecipientType.CC);
				ArrayList<String> ccRecipientList = new ArrayList<String>();

				if (ccList != null) {
					for (InternetAddress address : ccList)
						ccRecipientList.add(address.getAddress());
				}
				mail.setCcRecipientList(ccRecipientList);

				// get & set subject
				String subject = messageArray[i].getSubject();
				mail.setSubject(subject);

				// get & set date
				Date date = messageArray[i].getSentDate();
				mail.setDate(date);

				// get & set message
				Part messagePart = messageArray[i];
				String msgText = getMessageText(messagePart);
				mail.setMessage(msgText);

				// set folder to inbox folder
				mail.setFolder(JEEP_GUI.INBOX_FOLDER);

				// add mail object to database
				dbm.addMail(mail);

				// delete message from server when folder is closed
				// valid only if folder opened in read/write mode
				messageArray[i].setFlag(Flags.Flag.DELETED, true);

			} catch (MessagingException e) {
				// log error
				logger.log(Level.SEVERE,
						"Error reading message while receiving.");
				retVal = false;
			} catch (Exception ex) {
				// log error
				logger.log(Level.SEVERE,
						"Error while processing message from POP3 server");
				retVal = false;
			}
		}

		return retVal;
	}

	/**
	 * This method examines a the message text and identifies all parts and
	 * recursively all sub parts. It is looking for the text/plain section that
	 * every email message will have.
	 * 
	 * @param part
	 *            Part of the message
	 * @return String of the actual message that is text/plain
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getMessageText(Part part) throws MessagingException,
			IOException {

		if (part.isMimeType("text/*")) {
			String content = (String) part.getContent();
			return content;
		}

		if (part.isMimeType("multipart/alternative")) {
			// prefer plain text over html text
			Multipart multiPart = (Multipart) part.getContent();
			String text = null;
			for (int i = 0; i < multiPart.getCount(); i++) {
				Part bodyPart = multiPart.getBodyPart(i);
				if (bodyPart.isMimeType("text/html")) {
					if (text == null)
						text = getMessageText(bodyPart);
					continue;
				} else if (bodyPart.isMimeType("text/plain")) {
					String bodyPartMessageText = getMessageText(bodyPart);
					if (bodyPartMessageText != null)
						return bodyPartMessageText;
				} else {
					return getMessageText(bodyPart);
				}
			}
			return text;
		} else if (part.isMimeType("multipart/*")) {
			Multipart multiPart = (Multipart) part.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				String bodyPartMessageText = getMessageText(multiPart
						.getBodyPart(i));
				if (bodyPartMessageText != null)
					return bodyPartMessageText;
			}
		}
		return null;
	}
}
