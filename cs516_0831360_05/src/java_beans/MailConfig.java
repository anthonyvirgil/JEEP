/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailConfig.java
 */
package java_beans;

import java.io.Serializable;
import java.util.Locale;

/**
 * Java Bean for a user's mail configuration of the J.E.E.P application
 * 
 * @author Anthony-Virgil Bermejo
 * @version 3.1
 */
public class MailConfig implements Serializable {

	// instance variables
	private static final long serialVersionUID = -3547560537553261025L;
	private String username;
	private String email;
	private String pop3_URL;
	private String smtp_URL;
	private String pop3_username;
	private String pop3_password;
	private int pop3_portnum;
	private int smtp_portnum;
	private boolean isGmail;
	private String mySQL_URL;
	private int mySQL_portnum;
	private String mySQL_username;
	private String mySQL_password;
	private boolean isSMTPAuth;
	private Locale locale;

	/**
	 * Default constructor setting instance variables to their defaults.
	 */
	public MailConfig() {
		super();
		this.username = "";
		this.email = "";
		this.pop3_URL = "";
		this.smtp_URL = "";
		this.pop3_username = "";
		this.pop3_password = "";
		this.pop3_portnum = 0;
		this.smtp_portnum = 0;
		this.isGmail = false;
		this.mySQL_URL = "";
		this.mySQL_portnum = 0;
		this.mySQL_username = "";
		this.mySQL_password = "";
		this.isSMTPAuth = false;
		this.locale = Locale.getDefault();
	}

	/**
	 * Constructor setting all instance variables to what the user defines.
	 * 
	 * @param username
	 *            The username defined by the user
	 * @param email
	 *            The email of the user
	 * @param pop3_URL
	 *            The URL of the POP3 server
	 * @param smtp_URL
	 *            The URL of the SMTP server
	 * @param pop3_username
	 *            The username of the POP3 server
	 * @param pop3_password
	 *            The password of the POP3 server
	 * @param pop3_portnum
	 *            The port number of the POP3 server
	 * @param smtp_portnum
	 *            The port number of the SMTP server
	 * @param isGmail
	 *            If this is a Gmail account or not
	 * @param mySQL_URL
	 *            The URL of the MySQL database
	 * @param mySQL_portnum
	 *            The port number of the MySQL database
	 * @param mySQL_username
	 *            The username of the MySQL database
	 * @param mySQL_password
	 *            The password of the MySQL database
	 * @param isSMTPAuth
	 *            If authentication is required to log in to SMTP server
	 * @param locale
	 *            Locale chosen by the user
	 */
	public MailConfig(String username, String email, String pop3_URL,
			String smtp_URL, String pop3_username, String pop3_password,
			int pop3_portnum, int smtp_portnum, boolean isGmail,
			String mySQL_URL, int mySQL_portnum, String mySQL_username,
			String mySQL_password, boolean isSMTPAuth, Locale locale) {
		super();
		this.username = username;
		this.email = email;
		this.pop3_URL = pop3_URL;
		this.smtp_URL = smtp_URL;
		this.pop3_username = pop3_username;
		this.pop3_password = pop3_password;
		this.pop3_portnum = pop3_portnum;
		this.smtp_portnum = smtp_portnum;
		this.isGmail = isGmail;
		this.mySQL_URL = mySQL_URL;
		this.mySQL_portnum = mySQL_portnum;
		this.mySQL_username = mySQL_username;
		this.mySQL_password = mySQL_password;
		this.isSMTPAuth = isSMTPAuth;
		this.setLocale(locale);
	}

	/**
	 * Returns the username of the user
	 * 
	 * @return Username of the user
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the user
	 * 
	 * @param username
	 *            Username of the user to be set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the email of the user
	 * 
	 * @return Email of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the user
	 * 
	 * @param email
	 *            Email of the user to be set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the URL of the POP3 server
	 * 
	 * @return URL of the POP3 server
	 */
	public String getPop3_URL() {
		return pop3_URL;
	}

	/**
	 * Sets the URL of the POP3 server
	 * 
	 * @param pop3_URL
	 *            URL of the POP3 server to be set
	 */
	public void setPop3_URL(String pop3_URL) {
		this.pop3_URL = pop3_URL;
	}

	/**
	 * Returns the URL of the SMTP server
	 * 
	 * @return URL of the SMTP server
	 */
	public String getSmtp_URL() {
		return smtp_URL;
	}

	/**
	 * Sets the URL of the SMTP server
	 * 
	 * @param smtp_URL
	 *            URL of the SMTP server to be set
	 */
	public void setSmtp_URL(String smtp_URL) {
		this.smtp_URL = smtp_URL;
	}

	/**
	 * Returns the username of the POP3 server
	 * 
	 * @return Username of the POP3 server
	 */
	public String getPop3_username() {
		return pop3_username;
	}

	/**
	 * Sets the username of the POP3 server
	 * 
	 * @param pop3_username
	 *            Username of the POP3 server to be set
	 */
	public void setPop3_username(String pop3_username) {
		this.pop3_username = pop3_username;
	}

	/**
	 * Returns the password of the POP3 server
	 * 
	 * @return Password of the POP3 server
	 */
	public String getPop3_password() {
		return pop3_password;
	}

	/**
	 * Sets the password of the POP3 server
	 * 
	 * @param pop3_password
	 *            Password of the POP3 server to be set
	 */
	public void setPop3_password(String pop3_password) {
		this.pop3_password = pop3_password;
	}

	/**
	 * Returns the port number of the POP3 server
	 * 
	 * @return Port number of the POP3 server
	 */
	public int getPop3_portnum() {
		return pop3_portnum;
	}

	/**
	 * Sets the port number of the POP3 server
	 * 
	 * @param pop3_portnum
	 *            Port number of the POP3 server to be set
	 */
	public void setPop3_portnum(int pop3_portnum) {
		this.pop3_portnum = pop3_portnum;
	}

	/**
	 * Returns the port number of the SMTP server
	 * 
	 * @return Port number of the SMTP server
	 */
	public int getSmtp_portnum() {
		return smtp_portnum;
	}

	/**
	 * Sets the port number of the SMTP server
	 * 
	 * @param smtp_portnum
	 *            Port number of the SMTP server to be set
	 */
	public void setSmtp_portnum(int smtp_portnum) {
		this.smtp_portnum = smtp_portnum;
	}

	/**
	 * Returns if the email account is a Gmail account
	 * 
	 * @return True, if it a Gmail account. False, if not.
	 */
	public boolean isGmail() {
		return isGmail;
	}

	/**
	 * Sets if the email account is a Gmail account
	 * 
	 * @param isGmail
	 *            Boolean if the account is a Gmail account
	 */
	public void setIsGmail(boolean isGmail) {
		this.isGmail = isGmail;
	}

	/**
	 * Returns the URL of the MySQL database
	 * 
	 * @return URL of the MySQL database
	 */
	public String getMySQL_URL() {
		return mySQL_URL;
	}

	/**
	 * Sets the URL of MySQL database
	 * 
	 * @param mySQL_URL
	 *            URL of the MySQL database to be set
	 */
	public void setMySQL_URL(String mySQL_URL) {
		this.mySQL_URL = mySQL_URL;
	}

	/**
	 * Returns the port number of the MySQL database
	 * 
	 * @return Port number of the MySQL database
	 */
	public int getMySQL_portnum() {
		return mySQL_portnum;
	}

	/**
	 * Sets the port number of the MySQL database
	 * 
	 * @param mySQL_portnum
	 *            Port number of the MySQL database to be set
	 */
	public void setMySQL_portnum(int mySQL_portnum) {
		this.mySQL_portnum = mySQL_portnum;
	}

	/**
	 * Returns the username of the MySQL database
	 * 
	 * @return Username of the MySQL database
	 */
	public String getMySQL_username() {
		return mySQL_username;
	}

	/**
	 * Sets the username of the MySQL database
	 * 
	 * @param mySQL_username
	 *            Username of the MySQL database to be set
	 */
	public void setMySQL_username(String mySQL_username) {
		this.mySQL_username = mySQL_username;
	}

	/**
	 * Returns the password of the MySQL database
	 * 
	 * @return Password of the MySQL database
	 */
	public String getMySQL_password() {
		return mySQL_password;
	}

	/**
	 * Sets the password of the MySQL database
	 * 
	 * @param mySQL_password
	 *            Password of the MySQL database to be set
	 */
	public void setMySQL_password(String mySQL_password) {
		this.mySQL_password = mySQL_password;
	}

	/**
	 * Returns the locale of the configuration
	 * 
	 * @return Locale of the configuration
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets the locale of the mail config
	 * 
	 * @param locale
	 *            Locale to be set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Returns whether or not the mail configuration requires SMTP
	 * authentication
	 * 
	 * @return the isSMTPAuth
	 */
	public boolean isSMTPAuth() {
		return isSMTPAuth;
	}

	/**
	 * Sets if the mail configuration requires SMTP authentication
	 * 
	 * @param isSMTPAuth
	 *            the isSMTPAuth to set
	 */
	public void setSMTPAuth(boolean isSMTPAuth) {
		this.isSMTPAuth = isSMTPAuth;
	}

	/**
	 * Returns the serial UID of the object
	 * 
	 * @return The serial UID of the object
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
