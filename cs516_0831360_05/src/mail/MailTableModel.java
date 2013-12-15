/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailTableModel.java
 */
package mail;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_beans.Mail;
import javax.swing.table.AbstractTableModel;

/**
 * Model that is associated with the table containing mail data
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.0
 * 
 */
public class MailTableModel extends AbstractTableModel {

	// instance variables
	private Vector<String> columnNames;
	private Vector<Mail> mailData;
	private ResourceBundle messages;
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * MailTableModel constructor
	 * 
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailTableModel(ResourceBundle messages) {
		super();
		columnNames = new Vector<String>();
		mailData = new Vector<Mail>();
		this.messages = messages;
		logger.info("MailTableModel Instantiated");
	}

	/**
	 * Loads data of mail retrieved from database into the table through the
	 * vector of mail.
	 * 
	 * @param resultSet
	 *            Result set of data retrieved from the database
	 * @return Success or failure
	 */
	public boolean loadData(ResultSet resultSet) {
		boolean retVal = true;

		// recreate vector of mail
		mailData = new Vector<Mail>();

		try {
			// iterate through result set
			resultSet.beforeFirst();
			while (resultSet.next()) {
				// add mail data to vector creating a new mail object using
				// values
				mailData.addElement(new Mail(resultSet.getInt(1), resultSet
						.getString(2), resultSet.getString(3), resultSet
						.getString(4), resultSet.getString(5), resultSet
						.getString(6), resultSet.getString(7), resultSet
						.getTimestamp(8), resultSet.getString(9)));
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error loading data into table.", e);
			retVal = false;
		}

		// load column names of table
		loadColumnNames();

		return retVal;
	}

	/**
	 * Gets mail data at a specified row index
	 * 
	 * @param row
	 *            Row index in table where mail data is located
	 * @return Mail at the specified row index
	 */
	public Mail getMail(int row) {
		return mailData.elementAt(row);
	}

	/**
	 * Returns number of rows in table
	 * 
	 * @return Number of rows in table
	 */
	@Override
	public int getRowCount() {
		return mailData.size();
	}

	/**
	 * Returns column name at a specified column index
	 * 
	 * @return Column name
	 */
	@Override
	public String getColumnName(int col) {
		return (String) columnNames.elementAt(col);
	}

	/**
	 * Returns number of columns in the table
	 * 
	 * @return Number of columns
	 */
	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	/**
	 * Returns the object at the specified row index and column index
	 * 
	 * @return Object at the specified row index and column index
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return mailData.elementAt(rowIndex).getID();
		case 1:
			return mailData.elementAt(rowIndex).getSender();
		case 2:
			return mailData.elementAt(rowIndex).getToRecipientList();
		case 3:
			return mailData.elementAt(rowIndex).getBccRecipientList();
		case 4:
			return mailData.elementAt(rowIndex).getCcRecipientList();
		case 5:
			return mailData.elementAt(rowIndex).getSubject();
		case 6:
			return mailData.elementAt(rowIndex).getMessage();
		case 7:
			return mailData.elementAt(rowIndex).getDate();
		case 8:
			return mailData.elementAt(rowIndex).getFolder();
		}
		return null;
	}

	/**
	 * Removes the row at the specified row index
	 * 
	 * @param selectedRow
	 *            Index of row to be removed
	 */
	public void deleteRow(int selectedRow) {
		mailData.remove(selectedRow);
		this.fireTableDataChanged();
	}

	/**
	 * Loads the names that will be display on the header of each column
	 */
	private void loadColumnNames() {

		// add the names of each column in the table
		columnNames.addElement(messages.getString("mailID"));
		columnNames.addElement(messages.getString("sender"));
		columnNames.addElement(messages.getString("to"));
		columnNames.addElement(messages.getString("bcc"));
		columnNames.addElement(messages.getString("cc"));
		columnNames.addElement(messages.getString("subject"));
		columnNames.addElement(messages.getString("message"));
		columnNames.addElement(messages.getString("date"));
		columnNames.addElement(messages.getString("folder"));
	}
}
