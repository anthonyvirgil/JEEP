/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * DBManager.java
 */
package databases;

import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_beans.Contact;
import java_beans.Folder;
import java_beans.Mail;
import java_beans.MailConfig;

import javax.swing.JOptionPane;

import mail.MailTableModel;

/**
 * Communicates with database server by supplying various CRUD methods
 * 
 * @author Anthony-Virgil Bermejo
 * @version 3.1
 */
public class DBManager {
	// class variables
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private MailTableModel mailTableModel = null;
	private MailConfig mailConfig = null;
	private static final boolean DEBUG = false;
	private Logger logger = Logger.getLogger(getClass().getName());
	private ResourceBundle messages;

	/**
	 * DBManager constructor
	 * 
	 * @param mailConfig
	 *            User's mail configuration
	 * @param messages
	 *            ResourceBundle needed for internationalization
	 */
	public DBManager(MailConfig mailConfig, ResourceBundle messages) {
		super();
		this.mailConfig = mailConfig;
		this.messages = messages;
	}

	/**
	 * Sets mail table model to this class' instance variable
	 * 
	 * @param mailTableModel
	 *            MailTableModel to be set
	 */
	public void setMailTableModel(MailTableModel mailTableModel) {
		this.mailTableModel = mailTableModel;
	}

	/**
	 * Opens connection the the database
	 */
	private boolean openConnection() {
		boolean retVal = true;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// connect to database using user's info from mail configuration
			String url = "jdbc:mysql://" + mailConfig.getMySQL_URL() + ":"
					+ mailConfig.getMySQL_portnum() + "/"
					+ mailConfig.getMySQL_username();
			String user = mailConfig.getMySQL_username();
			String password = mailConfig.getMySQL_password();
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException cnfex) {
			// log error
			logger.log(Level.SEVERE, "Failed to load JDBC/ODBC driver.");
			retVal = false;
		} catch (SQLException sqlex) {
			// log error
			logger.log(Level.SEVERE, "Unable to connect to the database.");
			retVal = false;
		} catch (Exception e) {
			// log error
			logger.log(Level.SEVERE, "Unexpected database error.");
			retVal = false;
		}

		if (!retVal) {
			// cannot open connection, display error
			JOptionPane.showMessageDialog(null,
					messages.getString("connectDBError"),
					messages.getString("connectDBErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
			retVal = false;
		}

		return retVal;
	}

	/**
	 * Closes the database connection
	 */
	private void closeConnection() {
		try {
			connection.close();
		} catch (SQLException sqlex) {
			// log error and display message to user
			logger.log(Level.WARNING,
					"Unable to close the connection to the database.");
			JOptionPane.showMessageDialog(null,
					messages.getString("closeConnectError"),
					messages.getString("closeConnectErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Verifies that tables exist within the database
	 * 
	 * @return True, if tables exists in database. False, if otherwise.
	 */
	public boolean checkTablesExist() {
		boolean retVal = true;

		if (openConnection()) {
			try {
				String preparedQuery = "SHOW TABLES";
				preparedStatement = connection.prepareStatement(preparedQuery);

				// Send the SQL statement
				resultSet = preparedStatement.executeQuery();

				boolean moreRecords = resultSet.next();

				// If there are results, return value will be true. Else, false
				if (moreRecords) {
					if (DEBUG) {
						do {
							System.out.println(resultSet.getString(1));
						} while (resultSet.next());
					}
				} else
					retVal = false;

			} catch (SQLException sqlex) {
				// log error
				logger.log(Level.SEVERE,
						"Error checking that MySQL tables exist.");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			// close connection
			closeConnection();
		}
		return retVal;

	}

	/**
	 * Loads data and columns into table model from the data of mail in a
	 * specified folder
	 * 
	 * @param folderName
	 *            Name of folder that we want the data from
	 * @return True, if a success. False, if failure.
	 */
	public boolean fillTableModel(String folderName) {
		boolean retVal = true;

		if (openConnection()) {
			try {
				String preparedQuery = "Select * from mail WHERE folder = ? ORDER BY mailDate DESC";
				preparedStatement = connection.prepareStatement(preparedQuery);

				// query from a specific folder
				preparedStatement.setString(1, folderName);

				// send the SQL statement
				resultSet = preparedStatement.executeQuery();

				// load data into table's model
				if (!mailTableModel.loadData(resultSet))
					retVal = false;

				mailTableModel.fireTableDataChanged();

			} catch (SQLException sqlex) {
				// log error and display message to user
				logger.log(Level.SEVERE, "Error filling table with mail from "
						+ folderName + " folder.");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// log error and display message to user
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			// close connection
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Returns all mail data from a specified folder into a list
	 * 
	 * @param folder
	 *            Name of folder the data will come from
	 * @return List of resulted mail data
	 */
	public ArrayList<Mail> getAllMailFromFolder(String folder) {
		ArrayList<Mail> rows = new ArrayList<Mail>();

		String preparedQuery = "SELECT * from mail WHERE folder = ? ORDER BY mailDate DESC";

		if (openConnection()) {
			try {
				// send the SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, folder);
				resultSet = preparedStatement.executeQuery();

				boolean moreRecords = resultSet.next();

				// if records are found, process them
				if (moreRecords) {
					do {
						// add results of query into Mail objects to list
						rows.add(new Mail(resultSet.getInt(1), resultSet
								.getString(2), resultSet.getString(3),
								resultSet.getString(4), resultSet.getString(5),
								resultSet.getString(6), resultSet.getString(7),
								resultSet.getTimestamp(8), resultSet
										.getString(9)));
					} while (resultSet.next());
				}
			} catch (SQLException sqlex) {
				// log error and display message to user
				logger.log(Level.SEVERE, "Error querying mail data from "
						+ folder + " folder.");
				JOptionPane.showMessageDialog(null,
						messages.getString("getMailFromFolderError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					// close the SQL statement
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
				}
			}

			// close the connection
			closeConnection();
		}
		return rows;
	}

	/**
	 * Deletes mail from the database server
	 * 
	 * @param mail
	 *            Mail to be deleted
	 * @return Number of rows affected by statement
	 */
	public int deleteMail(Mail mail) {
		String preparedQuery = "DELETE FROM mail WHERE id = ?";
		int deletedRowCount = 0;

		if (openConnection()) {
			try {
				// send SQL statement, setting where clause to ID of mail
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setInt(1, mail.getID());
				deletedRowCount = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error deleting mail data from database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("deleteMailError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
				}
			}
			// close connection
			closeConnection();
		}
		return deletedRowCount;
	}

	/**
	 * Inserts mail into the database server
	 * 
	 * @param mail
	 *            Mail to be added
	 * @return True, if a success. False, if failure
	 */
	public boolean addMail(Mail mail) {
		String preparedQuery = "INSERT INTO mail VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with values from mail object
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, mail.getSender());
				preparedStatement.setString(2, mail.getToRecipientListString());
				preparedStatement
						.setString(3, mail.getBccRecipientListString());
				preparedStatement.setString(4, mail.getCcRecipientListString());
				preparedStatement.setString(5, mail.getSubject());
				preparedStatement.setString(6, mail.getMessage());
				preparedStatement.setTimestamp(7, new Timestamp(mail.getDate()
						.getTime()));
				preparedStatement.setString(8, mail.getFolder());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error inserting mail data into database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("addMailError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			// close connection
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Updates a particular mail data row in the database server
	 * 
	 * @param mail
	 *            Mail to be updated
	 * @return True, if a success. False, if failure.
	 */
	public boolean updateMail(Mail mail) {
		String preparedQuery = "UPDATE mail SET sender = ?, recipient = ?, bcc = ?, cc = ?, subject = ?, message = ?, mailDate = ?, folder = ? WHERE id = ?";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with updated values from mail object
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, mail.getSender());
				preparedStatement.setString(2, mail.getToRecipientListString());
				preparedStatement
						.setString(3, mail.getBccRecipientListString());
				preparedStatement.setString(4, mail.getCcRecipientListString());
				preparedStatement.setString(5, mail.getSubject());
				preparedStatement.setString(6, mail.getMessage());
				preparedStatement.setTimestamp(7, new Timestamp(mail.getDate()
						.getTime()));
				preparedStatement.setString(8, mail.getFolder());
				preparedStatement.setInt(9, mail.getID());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error updating mail data in the database");
				JOptionPane.showMessageDialog(null,
						messages.getString("updateMailError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			// close connection
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Retrieves a list of Contacts from the contacts table in the database
	 * 
	 * @return List of contacts from the executed SQL statement
	 */
	public ArrayList<Contact> getAllContacts() {
		ArrayList<Contact> rows = new ArrayList<Contact>();

		// build query string
		String preparedQuery = "SELECT * from contacts";

		if (openConnection()) {
			try {
				// Send the SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				resultSet = preparedStatement.executeQuery();

				boolean moreRecords = resultSet.next();

				// if records are found, process them
				if (moreRecords) {
					do {
						rows.add(new Contact(resultSet.getInt(1), resultSet
								.getString(2), resultSet.getString(3),
								resultSet.getString(4)));
					} while (resultSet.next());
				}
			} catch (SQLException sqlex) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error retrieving all contacts from database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("getAllContactsError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					// close the SQL statement
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
				}
			}
			// close the connection
			closeConnection();
		}
		return rows;
	}

	/**
	 * Deletes a contact from the database server
	 * 
	 * @param contact
	 *            Contact to be deleted
	 * @return True, if a success. False, if a failure
	 */
	public boolean deleteContact(Contact contact) {
		boolean retVal = true;
		String preparedQuery = "DELETE from contacts WHERE id = ?";

		if (openConnection()) {
			try {
				// prepare statement setting where clause to ID of the contact
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setInt(1, contact.getID());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error
				logger.log(Level.SEVERE, "Error deleting contact from database");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Edits a contact data row in the database server
	 * 
	 * @param contact
	 *            Contact to be edited
	 * @return True, if a success. False, if failure
	 */
	public boolean editContact(Contact contact) {
		String preparedQuery = "UPDATE contacts SET emailAddress = ?, firstName = ?, lastName = ? WHERE id = ?";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statements setting values to that of the contact
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, contact.getEmailAddress());
				preparedStatement.setString(2, contact.getFirstName());
				preparedStatement.setString(3, contact.getLastName());
				preparedStatement.setInt(4, contact.getID());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error
				logger.log(Level.SEVERE, "Error editing contact in database.");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			// close connection
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Adds a contact to the database server
	 * 
	 * @param contact
	 *            Contact to be added
	 * @return True, if a success. False, if failure.
	 */
	public boolean addContact(Contact contact) {
		String preparedQuery = "INSERT INTO contacts (emailAddress, firstName, lastName) VALUES (?,?,?)";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with values from contact to be added
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, contact.getEmailAddress());
				preparedStatement.setString(2, contact.getFirstName());
				preparedStatement.setString(3, contact.getLastName());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error
				logger.log(Level.SEVERE, "Error adding contact to database.");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			// close connection
			closeConnection();
		}

		return retVal;
	}

	/**
	 * Retrieves all folders from the folders table in the database
	 * 
	 * @return A list of folders from the executed SQL statement
	 */
	public ArrayList<Folder> getFolders() {
		ArrayList<Folder> rows = new ArrayList<Folder>();

		// build query string
		String preparedQuery = "SELECT * from folders";

		if (openConnection()) {
			try {
				// Send the SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				resultSet = preparedStatement.executeQuery();

				boolean moreRecords = resultSet.next();

				// if records are found, process them
				if (moreRecords) {
					do {
						// add folder object with results from query to list
						rows.add(new Folder(resultSet.getInt(1), resultSet
								.getString(2)));
					} while (resultSet.next());
				}
			} catch (SQLException sqlex) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error querying all folders from database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("getFoldersDBError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					// close the SQL statement
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
				}
			}
			// close the connection
			closeConnection();
		}
		return rows;
	}

	/**
	 * Deletes a folder from the Folders table with the specified folder name
	 * 
	 * @param folder
	 *            Folder to be deleted
	 * @return Number of rows that were deleted
	 */
	public boolean deleteFolder(Folder folder) {
		String preparedQuery = "DELETE FROM folders WHERE id = ?";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with value of folder's ID in where clause
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setInt(1, folder.getID());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error
				logger.log(Level.SEVERE,
						"Error deleting folder from the database.");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Adds a new folder to the folders table of the database
	 * 
	 * @param folder
	 *            Folder that will be added
	 */
	public boolean addFolder(Folder folder) {
		String preparedQuery = "INSERT INTO folders (name) VALUES (?)";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with values of folder to be added
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, folder.getName());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error
				logger.log(Level.SEVERE, "Error adding folder to database.");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}

		return retVal;
	}

	/**
	 * Edits the name of a folder with a specified name
	 * 
	 * @param folder
	 *            Folder to be edited
	 */
	public boolean editFolder(Folder folder) {
		String preparedQuery = "UPDATE folders SET name = ? WHERE id = ?";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with values of folder to be updated
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.setString(1, folder.getName());
				preparedStatement.setInt(2, folder.getID());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error
				logger.log(Level.SEVERE, "Error editing folder in the database");
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Truncates the data within the specified table
	 * 
	 * @param tableName
	 *            Name of table from which data will be truncated
	 * @return True, if a success. False, if failure.
	 */
	public boolean truncateTable(String tableName) {
		String preparedQuery = "truncate " + tableName;
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement with SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE, "Error truncating table data in "
						+ tableName + " table.");
				JOptionPane.showMessageDialog(null,
						messages.getString("truncateTableError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Drops the specified table from the database serverS
	 * 
	 * @param tableName
	 *            Name of tabSle to be dropped
	 * @return True, if a success. False, if failure
	 */
	public boolean dropTable(String tableName) {
		String preparedQuery = "drop table " + tableName;
		String commit = "commit";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement to be sent as SQL statment
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.executeUpdate();
				preparedStatement = connection.prepareStatement(commit);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE, "Error dropping table " + tableName
						+ " from database");
				JOptionPane.showMessageDialog(null,
						messages.getString("dropTableError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Creates the mail table in the database
	 * 
	 * @return True, if a success. False, if failure
	 */
	public boolean createMailTable() {
		// build query string with table name and data types for columns
		String preparedQuery = "CREATE TABLE mail"
				+ "( id int(11) NOT NULL auto_increment,"
				+ "sender text NOT NULL," + "recipient text NOT NULL,"
				+ "bcc text NOT NULL," + "cc text NOT NULL,"
				+ "subject text NOT NULL," + "message text,"
				+ "mailDate datetime,"
				+ "folder varchar(20) NOT NULL default '',"
				+ "PRIMARY KEY  (id)" + ") ENGINE=InnoDB;";
		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement for SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error creating Mail table in database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("createMailTableError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Creates folders table in the database
	 * 
	 * @return True, if a success. Fails, if failure.
	 */
	public boolean createFoldersTable() {
		// build query string with table name and data types for columns
		String preparedQuery = "CREATE TABLE folders"
				+ "(id int(11) NOT NULL auto_increment,"
				+ "name varchar(20) NOT NULL," + "PRIMARY KEY  (id)"
				+ ") ENGINE=InnoDB";
		String insertValues = "INSERT INTO folders (name) VALUES ('Inbox'), ('Outbox'), ('Sent'), ('Trash')";

		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement for SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.executeUpdate();
				preparedStatement = connection.prepareStatement(insertValues);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				logger.log(Level.SEVERE,
						"Error creating Folders table in database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("createFoldersTableError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}

	/**
	 * Creates contacts table in the database
	 * 
	 * @return True, if a success. False, if failure.
	 */
	public boolean createContactsTable() {
		// build query string with table name and data types for columns
		String preparedQuery = "CREATE TABLE contacts"
				+ "(id int(11) NOT NULL auto_increment,"
				+ "emailAddress text NOT NULL," + "firstName text NOT NULL,"
				+ "lastName text NOT NULL," + "PRIMARY KEY  (id)"
				+ ") ENGINE=InnoDB";

		boolean retVal = true;

		if (openConnection()) {
			try {
				// prepare statement for SQL statement
				preparedStatement = connection.prepareStatement(preparedQuery);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// log error and display message to user
				logger.log(Level.SEVERE,
						"Error creating Contacts folder in database.");
				JOptionPane.showMessageDialog(null,
						messages.getString("createContactsTableError"),
						messages.getString("databaseError"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException ex) {
					// log error
					logger.log(Level.SEVERE, "Error closing SQL statement.");
					retVal = false;
				}
			}
			closeConnection();
		}
		return retVal;
	}
}
