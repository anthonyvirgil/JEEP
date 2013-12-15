/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MessageCompositionModel.java
 */
package mail;

import java.util.Observable;

import java_beans.Contact;
import java_beans.Mail;

/**
 * Represents the model in the MVC pattern of Message Composition UIs
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.2
 */
public class MessageCompositionModel extends Observable {
	private Mail mail = null;

	/**
	 * MessageCompositionModel constructor
	 */
	public MessageCompositionModel() {
		super();
		mail = new Mail();
	}

	/**
	 * Receives a mail object and sets it to its class variable
	 * 
	 * @param mail
	 *            Mail to be set
	 */
	public void retrieveMail(Mail mail) {
		this.mail = mail;
	}

	/**
	 * Receives a mail object to replied to in composition panel
	 * 
	 * @param mail
	 *            Mail to be replied to
	 */
	public void replyMessage(Mail mail) {
		this.mail = mail;
		setChanged();

		// determine what kind of action will be performed
		notifyObservers("Reply");
	}

	/**
	 * Receives a mail object to replied to all recipients in composition panel
	 * 
	 * @param mail
	 *            Mail to be replied to all recipients
	 */
	public void replyAllMessage(Mail mail) {
		this.mail = mail;
		setChanged();

		// determine what kind of action will be performed
		notifyObservers("Reply All");
	}

	/**
	 * Receives a mail object to forwarded in composition panel
	 * 
	 * @param mail
	 *            Mail to be forwarded
	 */
	public void forwardMessage(Mail mail) {
		this.mail = mail;
		setChanged();

		// determine what kind of action will be performed
		notifyObservers("Forward");
	}

	/**
	 * Sends a new mail message from scratch
	 */
	public void sendEmptyMessage() {
		this.mail = new Mail();
		setChanged();

		// determine what kind of action will be performed
		notifyObservers("ComposeMail");
	}

	/**
	 * Return mail located in the model
	 * 
	 * @return Mail located in the model
	 */
	public Mail getMail() {
		return mail;
	}
}
