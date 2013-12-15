/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailConfigModel.java
 */
package properties;

import java.util.Observable;

import java_beans.MailConfig;

/**
 * Represents the model in the MVC pattern Mail Configuration UIs
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.0
 */
public class MailConfigModel extends Observable {

	private MailConfig mailConfig;

	/**
	 * MailConfigModel constructor
	 */
	public MailConfigModel() {
		super();
		mailConfig = new MailConfig();
	}

	/**
	 * Receives a mailConfig object and sets it to instance variable
	 * 
	 * @param mailConfig
	 *            MailConfig object to set
	 */
	public void retrieveMailConfig(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns mailConfig object from this model
	 * 
	 * @return MailConfig object in this model
	 */
	public MailConfig getMailConfig() {
		return mailConfig;
	}
}
