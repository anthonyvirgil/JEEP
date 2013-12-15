/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailConfigController.java
 */
package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Messages;

import app.JEEP_GUI;

import java_beans.MailConfig;

/**
 * Responsible for loading from configuration properties file and creating a
 * properties file if none exists
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.2
 */
public class MailConfigController implements Observer {

	// class variables
	private String propFileName = null;
	private Properties properties = null;
	private MailConfig mailConfig = null;
	private JEEP_GUI gui = null;
	private ResourceBundle messages = null;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * MailConfigController constructor
	 * 
	 * @param gui
	 *            Frame that contains all panels and objects for the JEEP
	 *            Application
	 * @param propFileName
	 *            File name of the properties file
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailConfigController(JEEP_GUI gui, String propFileName,
			ResourceBundle messages) {
		this.propFileName = propFileName;
		this.messages = messages;
		this.gui = gui;
		mailConfig = new MailConfig();
		properties = new Properties();
	}

	/**
	 * Loads the properties from the Configuration.ini file and sets the values
	 * to a mailConfig object
	 * 
	 * @return True, if success. False, if failure.
	 */
	public boolean loadProperties() {
		FileInputStream propFileStream = null;
		File propFile = new File(propFileName);
		boolean retVal = true;

		// check that file exists
		if (propFile.exists()) {
			try {
				// load properties file
				propFileStream = new FileInputStream(propFile);
				properties.load(propFileStream);
				propFileStream.close();

				// set values of mailConfig object with those from the
				// properties object
				mailConfig.setUsername(properties.getProperty("username"));
				mailConfig.setEmail(properties.getProperty("email"));
				mailConfig.setPop3_URL(properties.getProperty("pop3url"));
				mailConfig.setSmtp_URL(properties.getProperty("smtpurl"));
				mailConfig.setPop3_username(properties
						.getProperty("pop3username"));
				mailConfig.setPop3_password(properties
						.getProperty("pop3password"));
				mailConfig.setPop3_portnum(Integer.parseInt(properties
						.getProperty("pop3portnum")));
				mailConfig.setSmtp_portnum(Integer.parseInt(properties
						.getProperty("smtpportnum")));

				mailConfig.setMySQL_URL(properties.getProperty("mySQL_URL"));
				mailConfig.setMySQL_portnum(Integer.parseInt(properties
						.getProperty("mySQL_portnum")));
				mailConfig.setMySQL_username(properties
						.getProperty("mySQL_username"));
				mailConfig.setMySQL_password(properties
						.getProperty("mySQL_password"));

				mailConfig.setIsGmail(Boolean.parseBoolean(properties
						.getProperty("isGmail")));
				mailConfig.setSMTPAuth(Boolean.parseBoolean(properties
						.getProperty("isSMTPAuth")));
				mailConfig.setLocale(new Locale(properties
						.getProperty("language"), properties
						.getProperty("country")));

				// initialize the main gui
				gui.initialize();

			} catch (FileNotFoundException e) {
				// log error
				logger.log(Level.SEVERE,
						"Mail configuration file not found while loading.");
				retVal = false;
			} catch (IOException e) {
				// log error
				logger.log(Level.SEVERE,
						"Error reading mail configuration file while loading.");
				retVal = false;
			}
		} else
			retVal = false;

		return retVal;
	}

	/**
	 * Writes to a properties file the values of the user's mail configuration
	 * 
	 * @return True, if a success. False, if failure.
	 */
	public boolean writeProperties() {
		boolean retVal = true;

		// set properties of properties object from the values mailConfig object
		properties.setProperty("username", mailConfig.getUsername());
		properties.setProperty("email", mailConfig.getEmail());
		properties.setProperty("pop3url", mailConfig.getPop3_URL());
		properties.setProperty("smtpurl", mailConfig.getSmtp_URL());
		properties.setProperty("pop3username", mailConfig.getPop3_username());
		properties.setProperty("pop3password", mailConfig.getPop3_password());
		properties.setProperty("pop3portnum",
				String.valueOf(mailConfig.getPop3_portnum()));
		properties.setProperty("smtpportnum",
				String.valueOf(mailConfig.getSmtp_portnum()));
		properties.setProperty("mySQL_URL", mailConfig.getMySQL_URL());
		properties.setProperty("mySQL_portnum",
				String.valueOf(mailConfig.getMySQL_portnum()));
		properties
				.setProperty("mySQL_username", mailConfig.getMySQL_username());
		properties
				.setProperty("mySQL_password", mailConfig.getMySQL_password());

		if (mailConfig.isGmail())
			properties.setProperty("isGmail", "true");
		else
			properties.setProperty("isGmail", "false");

		if (mailConfig.isSMTPAuth())
			properties.setProperty("isSMTPAuth", "true");
		else
			properties.setProperty("isSMTPAuth", "false");

		properties
				.setProperty("language", mailConfig.getLocale().getLanguage());
		properties.setProperty("country", mailConfig.getLocale().getCountry());

		// write the properties to a file
		FileOutputStream propFileStream = null;
		File propFile = new File(propFileName);
		try {
			propFileStream = new FileOutputStream(propFile);
			properties.store(propFileStream, "-- SMTP Properties --");
			propFileStream.close();

			// reset main GUI
			gui.dispose();
			gui.initialize();

		} catch (FileNotFoundException e) {
			// log error
			logger.log(Level.SEVERE,
					"Mail configuration file not found while writing.");
			retVal = false;
		} catch (IOException e) {
			// log error
			logger.log(Level.SEVERE,
					"Error reading mail configuration file while writing.");
			retVal = false;
		}
		return retVal;
	}

	/**
	 * Return mail configuration of user from controller
	 * 
	 * @return User's mail configuration
	 */
	public MailConfig getMailConfig() {
		return mailConfig;
	}

	/**
	 * Display the values in the properties file
	 */
	public void displayProperties() {
		properties.list(System.out);
	}

	/**
	 * Update method that is called when associated model notifies observers
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof MailConfigModel) {
			MailConfigModel localMailConfigModel = (MailConfigModel) o;
			// get mailConfig object from model
			mailConfig = localMailConfigModel.getMailConfig();
			if (!writeProperties())
				// if failure to write properties, display error
				JOptionPane.showMessageDialog(null,
						messages.getString("configWriteError"),
						messages.getString("configErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
		}
	}
}