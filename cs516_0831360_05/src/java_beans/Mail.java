/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * Mail.java
 */
package java_beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Java Bean for an email in the J.E.E.P application
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.3
 */
public class Mail implements Serializable {

	private static final long serialVersionUID = -2161406904968447727L;
	private int id;
	private String sender;
	private ArrayList<String> toRecipientList;
	private ArrayList<String> bccRecipientList;
	private ArrayList<String> ccRecipientList;
	private String subject;
	private String message;
	private Date date;
	private String folder;

	/**
	 * Default constructor setting instance variables to their defaults.
	 */
	public Mail() {
		id = 0;
		sender = "";
		toRecipientList = new ArrayList<String>();
		bccRecipientList = new ArrayList<String>();
		ccRecipientList = new ArrayList<String>();
		subject = "";
		message = "";
		date = new Date();
		folder = "";
	}

	/**
	 * Mail constructor setting all class variables to what user defines, except
	 * ID
	 * 
	 * @param sender
	 *            Sender of the mail
	 * @param recipientList
	 *            List of recipients this mail is addressed to
	 * @param bccList
	 *            List of BCC recipients this mail is addressed to
	 * @param ccList
	 *            List of CC recipients this mail is addressed to
	 * @param subject
	 *            Subject of mail
	 * @param message
	 *            Content of mail message
	 * @param date
	 *            Date that the message was sent
	 * @param folder
	 *            Folder the mail resides in
	 */
	public Mail(String sender, String recipientList, String bccList,
			String ccList, String subject, String message, Date date,
			String folder) {
		this.sender = sender;
		this.toRecipientList = splitStringIntoArray(recipientList);
		this.bccRecipientList = splitStringIntoArray(bccList);
		this.ccRecipientList = splitStringIntoArray(ccList);
		this.subject = subject;
		this.message = message;
		this.date = date;
		this.folder = folder;
	}

	/**
	 * Mail constructor setting all class variables to what user defines, except
	 * ID
	 * 
	 * @param id
	 *            Id of the mail
	 * @param sender
	 *            Sender of the mail
	 * @param recipientList
	 *            List of recipients this mail is addressed to
	 * @param bccList
	 *            List of BCC recipients this mail is addressed to
	 * @param ccList
	 *            List of CC recipients this mail is addressed to
	 * @param subject
	 *            Subject of mail
	 * @param message
	 *            Content of mail message
	 * @param date
	 *            Date that the message was sent
	 * @param folder
	 *            Folder the mail resides in
	 */
	public Mail(int id, String sender, String recipientList, String bccList,
			String ccList, String subject, String message, Date date,
			String folder) {
		this.id = id;
		this.sender = sender;
		this.toRecipientList = splitStringIntoArray(recipientList);
		this.bccRecipientList = splitStringIntoArray(bccList);
		this.ccRecipientList = splitStringIntoArray(ccList);
		this.subject = subject;
		this.message = message;
		this.date = date;
		this.folder = folder;
	}

	/**
	 * Returns the sender of the email
	 * 
	 * @return Sender of the email
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender of the email
	 * 
	 * @param sender
	 *            Sender of the email to be set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Returns the recipient of the email
	 * 
	 * @return Recipient of the email
	 */
	public ArrayList<String> getToRecipientList() {
		return toRecipientList;
	}

	/**
	 * Sets the recipient of the email
	 * 
	 * @param recipient
	 *            Recipient of the email to be set
	 */
	public void setToRecipientList(ArrayList<String> recipientList) {
		this.toRecipientList = recipientList;
	}

	/**
	 * Returns the BCC of the email
	 * 
	 * @return BCC of the email
	 */
	public ArrayList<String> getBccRecipientList() {
		return bccRecipientList;
	}

	/**
	 * Sets the BCC of the email
	 * 
	 * @param bcc
	 *            BCC of the email to be set
	 */
	public void setBccRecipientList(ArrayList<String> bccList) {
		this.bccRecipientList = bccList;
	}

	/**
	 * Returns the CC of the email
	 * 
	 * @return CC of the email
	 */
	public ArrayList<String> getCcRecipientList() {
		return ccRecipientList;
	}

	/**
	 * Sets the CC of the email
	 * 
	 * @param cc
	 *            CC of the email to be set
	 */
	public void setCcRecipientList(ArrayList<String> ccList) {
		this.ccRecipientList = ccList;
	}

	/**
	 * Returns the subject of the email
	 * 
	 * @return Subject of the email
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of the email
	 * 
	 * @param subject
	 *            Subject of the email to be set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Returns the message of the email
	 * 
	 * @return Message of the email
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message of the email
	 * 
	 * @param message
	 *            Message of the email to be set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the id of message
	 */
	public int getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id of email to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * @return the date the mail was sent
	 */
	public Date getDate() {

		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the folder name
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * @param folder
	 *            the folder name to set
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Retrieves a String of all To recipients concatentated with a ", " between
	 * each recipient
	 * 
	 * @return String of all recipients
	 */
	public String getToRecipientListString() {
		StringBuilder sb = new StringBuilder();
		String recipientList = "";

		if (toRecipientList.size() > 1) {
			for (String recipient : toRecipientList) {
				sb.append(recipient);
				sb.append(", ");
			}
			String listString = sb.toString();
			recipientList = listString.substring(0,
					listString.lastIndexOf(", "));
		} else if (toRecipientList.size() == 1) {
			sb.append(toRecipientList.get(0));
			recipientList = sb.toString();
		}
		return recipientList;
	}

	/**
	 * Retrieves a String of all BCC recipients concatentated with a ", "
	 * between each recipient
	 * 
	 * @return String of all recipients
	 */
	public String getBccRecipientListString() {
		StringBuilder sb = new StringBuilder();
		String recipientList = "";

		if (bccRecipientList.size() > 1) {
			for (String recipient : bccRecipientList) {
				sb.append(recipient);
				sb.append(", ");
			}
			String listString = sb.toString();
			recipientList = listString.substring(0,
					listString.lastIndexOf(", "));
		} else if (bccRecipientList.size() == 1) {
			sb.append(bccRecipientList.get(0));
			recipientList = sb.toString();
		}

		return recipientList;
	}

	/**
	 * Retrieves a String of all CC recipients concatentated with a ", " between
	 * each recipient
	 * 
	 * @return String of all recipients
	 */
	public String getCcRecipientListString() {
		StringBuilder sb = new StringBuilder();
		String recipientList = "";

		if (ccRecipientList.size() > 1) {
			for (String recipient : ccRecipientList) {
				sb.append(recipient);
				sb.append(", ");
			}
			String listString = sb.toString();
			recipientList = listString.substring(0,
					listString.lastIndexOf(", "));
		} else if (ccRecipientList.size() == 1) {
			sb.append(ccRecipientList.get(0));
			recipientList = sb.toString();
		}

		return recipientList;
	}

	/**
	 * Splits a String of recipients into an ArrayList of email addresses
	 * 
	 * @param emailList
	 *            String of emails to be made into an ArrayList
	 * @return ArrayList of all emails in the String
	 */
	public ArrayList<String> splitStringIntoArray(String emailList) {
		ArrayList<String> list = new ArrayList<String>();

		// if recipient input is not empty, split string and fill list
		if (emailList.indexOf(", ") != -1) {
			String[] emailArray = emailList.split(", ");
			Collections.addAll(list, emailArray);
		} else if (!emailList.equals(""))
			list.add(emailList);

		return list;

	}

	/**
	 * Overridden toString() that returns a String display all values within a
	 * Mail bean
	 */
	@Override
	public String toString() {
		return "id " + id + "\n" + "sender " + sender + "\n" + "to "
				+ getToRecipientListString() + "\n" + "bcc "
				+ getBccRecipientListString() + "\n" + "cc "
				+ getCcRecipientListString() + "\n" + "subject " + subject
				+ "\n" + "message " + message + "\ndate " + date + "\nfolder "
				+ folder;
	}
}
