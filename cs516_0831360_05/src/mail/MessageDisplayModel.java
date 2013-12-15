/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MessageDisplayModel.java
 */
package mail;

import java.util.Observable;

import java_beans.Mail;

/**
 * Model of the MVC patter that is associated with Message Display UIs
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.1
 */
public class MessageDisplayModel extends Observable {

	private Mail mail = null;

	/**
	 * MessageDisplayModel constructor
	 */
	public MessageDisplayModel() {
		super();
		mail = new Mail();
	}

	/**
	 * Receives a mail object and sets it to its class variable
	 * 
	 * @param mail
	 *            Mail to be set
	 */
	public void receiveMail(Mail mail) {
		this.mail = mail;
		setChanged();
		notifyObservers();
	}

	/**
	 * Return mail from this model
	 * 
	 * @return Mail data in the model
	 */
	public Mail getMail() {
		return mail;
	}
}
