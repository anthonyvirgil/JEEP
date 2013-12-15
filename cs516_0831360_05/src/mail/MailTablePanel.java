/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailTablePanel.java
 */
package mail;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_beans.Contact;
import java_beans.Mail;
import java_beans.MailConfig;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.mysql.jdbc.Messages;

import databases.DBManager;

import app.JEEP_GUI;

/**
 * View that includes a table containing mail data
 * 
 * @author Anthony-Virgil Bermejo
 * @version 3.2
 * 
 */
public class MailTablePanel extends JPanel {

	// class variables
	private MailTableModel mailTableModel = null;
	private DBManager dbm = null;
	private JScrollPane scrollPane = null;
	private int selectedRow = -1;
	private MessageDisplayModel messageDisplayModel = null;
	private MessageCompositionModel messageCompositionModel = null;
	private CardLayout cardLayout = null;
	private JPanel cards = null;
	private MailConfig mailConfig = null;
	private MailReceiver mailReceiver = null;
	private MailSender mailSender = null;
	private ResourceBundle messages;
	private MailTable table = null;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * MailTablePanel constructor
	 * 
	 * @param mailTableModel
	 *            Model associated with this view
	 * @param messageDisplayModel
	 *            Model associated with the panel that displays messages
	 * @param messageCompositionModel
	 *            Model associated with the panel that composes new messages
	 * @param mailConfig
	 *            Mail configuration of the user
	 * @param mailReceiver
	 *            Class responsible for receiving mail
	 * @param mailSender
	 *            Class responsible for sending mail
	 * @param dbm
	 *            Database manager that communicates with the database
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailTablePanel(MailTableModel mailTableModel,
			MessageDisplayModel messageDisplayModel,
			MessageCompositionModel messageCompositionModel,
			MailConfig mailConfig, MailReceiver mailReceiver,
			MailSender mailSender, DBManager dbm, ResourceBundle messages) {
		super();
		this.mailTableModel = mailTableModel;
		this.mailConfig = mailConfig;
		this.mailReceiver = mailReceiver;
		this.mailSender = mailSender;
		this.dbm = dbm;
		this.messages = messages;
		dbm.setMailTableModel(mailTableModel);
		this.messageDisplayModel = messageDisplayModel;
		this.messageCompositionModel = messageCompositionModel;

		// initialize this view
		initialize();
	}

	/**
	 * Sets the card layout class variable
	 * 
	 * @param cardLayout
	 *            CardLayout to be set
	 */
	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	/**
	 * Sets the card panel class variable
	 * 
	 * @param cards
	 *            Panel to be set
	 */
	public void setCardPanel(JPanel cards) {
		this.cards = cards;
	}

	/**
	 * Returns the selected row index in the table
	 * 
	 * @return Selected row index
	 */
	public int getSelectedRow() {
		return table.getSelectedRow();
	}

	/**
	 * Updates table with data from the specified folder
	 * 
	 * @param folder
	 *            Folder where the mail data that will populate table are
	 *            located
	 */
	public void changeTableToFolder(String folder) {

		// check if folder being changed is the Inbox
		if (folder.equals(JEEP_GUI.INBOX_FOLDER)) {
			try {
				// get mail from POP3 server
				mailReceiver.getMail();
			} catch (MessagingException e) {
				// catch only necessary for junit tests
			} catch (Exception e) {
				// catch only necessary for junit tests
			}
		}

		// fill table with data from database
		if (!dbm.fillTableModel(folder))
			// display error message if failure to populate table
			JOptionPane.showMessageDialog(null,
					messages.getString("fillTableModelError"),
					messages.getString("fillTableModelErrorTitle"),
					JOptionPane.ERROR_MESSAGE);

		// display blank screen from card layout
		showBlankDisplayPanel();
	}

	/**
	 * Deletes mail from the table and/or database depending on which folder it
	 * resides in. Mail deleted from trash folder will be deleted forever. If
	 * mail resides in any other folder, send to Trash folder
	 * 
	 * @param mail
	 *            Mail to be deleted
	 */
	public void deleteMail(Mail mail) {

		// check if mail is located in Trash folder
		if (mail.getFolder().equals(JEEP_GUI.TRASH_FOLDER)) {
			// ask for delete confirmation
			int result = JOptionPane.showConfirmDialog(
					null,
					messages.getString("deleteMailConfirm") + "\n"
							+ messages.getString("areyousure"),
					messages.getString("confirmDeleteTitle"),
					JOptionPane.YES_NO_OPTION);

			// user clicks yes
			if (result == JOptionPane.YES_OPTION) {
				// delete the mail from the database and table
				mailTableModel.deleteRow(table.getSelectedRow());
				dbm.deleteMail(mail);

				// display blank screen from card layout
				showBlankDisplayPanel();
			}
		} else { // send mail to DELETED folder
			// remove row table
			mailTableModel.deleteRow(table.getSelectedRow());

			// update mail to move it to Trash folder
			mail.setFolder(JEEP_GUI.TRASH_FOLDER);
			dbm.updateMail(mail);

			// display blank screen from card layout
			showBlankDisplayPanel();
		}
	}

	/**
	 * Sends a mail object. If mail is located in Outbox folder, send that mail
	 * to its recipients.
	 * 
	 * @param mail
	 *            Mail to be sent
	 */
	public void sendMail(Mail mail) {
		if (mail != null) {
			// only send mail if it is from Outbox folder
			if (mail.getFolder().equals(JEEP_GUI.OUTBOX_FOLDER)) {
				try {
					if (mailSender.sendMail(mail)) {
						// delete row from table
						mailTableModel.deleteRow(table.getSelectedRow());

						// change folder of mail to Sent folder
						mail.setFolder(JEEP_GUI.SENT_FOLDER);

						// update mail in database
						dbm.updateMail(mail);

						// display blank panel
						cardLayout.show(cards, JEEP_GUI.BLANK_PANEL);
					} else
						// failed to send, display error message
						JOptionPane.showMessageDialog(null,
								messages.getString("sendMessageError"),
								messages.getString("sendMessageErrorTitle"),
								JOptionPane.ERROR_MESSAGE);
				} catch (MessagingException e) {
					// catch only necessary for JUnit tests
				} catch (Exception e) {
					// catch only necessary for JUnit tests
				}
			} else
				// display error message, not appropriate folder
				JOptionPane.showMessageDialog(getParent(),
						messages.getString("sendFromOtherFolderError"),
						messages.getString("sendErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
		} else
			// display error message, none selected
			JOptionPane.showMessageDialog(getParent(),
					messages.getString("sendMailError"),
					messages.getString("sendErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Create a new mail message from scratch
	 */
	public void composeNewMail() {
		messageCompositionModel.sendEmptyMessage();
	}

	/**
	 * Create a new mail message that replies to sender
	 */
	public void replyToMail() {
		if (table.getSelectedRow() != -1) {
			// retrieve mail from selected row
			Mail mail = mailTableModel.getMail(table.getSelectedRow());

			// display message composition panel if from inbox folder
			if (mail.getFolder().equals(JEEP_GUI.INBOX_FOLDER))
				messageCompositionModel.replyMessage(mail);
			else
				// display error if replying from another folder
				JOptionPane.showMessageDialog(null,
						messages.getString("replyOtherFolderError"),
						messages.getString("replyErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
		} else
			// display error if no mail selected
			JOptionPane.showMessageDialog(null,
					messages.getString("replyMailError"),
					messages.getString("replyErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Create a new mail message replying to sender and all recipients
	 */
	public void replyAllMail() {
		if (table.getSelectedRow() != -1) {
			// retrieve mail from selected row
			Mail mail = mailTableModel.getMail(table.getSelectedRow());

			// display message composition panel if from inbox folder
			if (mail.getFolder().equals(JEEP_GUI.INBOX_FOLDER))
				messageCompositionModel.replyAllMessage(mail);
			else
				// display error if replying from another folder
				JOptionPane.showMessageDialog(null,
						messages.getString("replyAllOtherFolderError"),
						messages.getString("replyAllErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null,
					messages.getString("replyMailError"),
					messages.getString("replyAllErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	public void forwardMail() {
		if (table.getSelectedRow() != -1) {
			Mail mail = mailTableModel.getMail(table.getSelectedRow());

			if (mail.getFolder().equals(JEEP_GUI.INBOX_FOLDER))
				messageCompositionModel.forwardMessage(mail);
			else
				JOptionPane.showMessageDialog(null,
						messages.getString("forwardOtherFolderError"),
						messages.getString("forwardErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
		} else
			// display error if no mail selected
			JOptionPane.showMessageDialog(null,
					messages.getString("forwardMailError"),
					messages.getString("forwardErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Removes selected row from table
	 */
	public void removeRow() {
		mailTableModel.deleteRow(table.getSelectedRow());
	}

	/**
	 * Displays blank panel from card layout
	 */
	public void showBlankDisplayPanel() {
		// display blank panel
		cardLayout.show(cards, JEEP_GUI.BLANK_PANEL);
	}

	/**
	 * Initializes this view with its necessary components
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());

		// populate table with data
		makeTablePanel();
	}

	/**
	 * Creates and adds necessary components to this view
	 */
	private void makeTablePanel() {

		// fill table with data from specified folder when created
		if (!dbm.fillTableModel(JEEP_GUI.INBOX_FOLDER))
			// display error message if failure to populate table
			JOptionPane.showMessageDialog(null,
					messages.getString("fillTableModelError"),
					messages.getString("fillTableModelErrorTitle"),
					JOptionPane.ERROR_MESSAGE);

		// create new table using its model
		table = new MailTable(mailTableModel, messages);

		// set the viewport size
		table.setPreferredScrollableViewportSize(new Dimension(1000, 150));

		// allow table to fill the entire viewport
		table.setFillsViewportHeight(true);

		// allow single selection
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// prevent reordering on table
		table.getTableHeader().setReorderingAllowed(false);

		// prevent resizing
		table.getTableHeader().setResizingAllowed(false);

		// allow for rows to be selected
		table.setRowSelectionAllowed(true);

		// allows drag & drop with transfer handler
		table.setDragEnabled(true);
		table.setTransferHandler(new TableRowTransferHandler(table,
				mailTableModel));

		if (table.getColumnCount() != 0) {

			// remove unnecessary columns
			TableColumn column = null;
			column = table.getColumnModel().getColumn(0);
			table.removeColumn(column);
			column = table.getColumnModel().getColumn(2);
			table.removeColumn(column);
			column = table.getColumnModel().getColumn(2);
			table.removeColumn(column);
			column = table.getColumnModel().getColumn(1);
			table.removeColumn(column);
			column = table.getColumnModel().getColumn(4);
			table.removeColumn(column);
		}

		// set column header height
		table.getTableHeader()
				.setPreferredSize(
						new Dimension(table.getColumnModel()
								.getTotalColumnWidth(), 30));

		// when user presses TAB, proceeds to next row, not column
		table.getInputMap().put(KeyStroke.getKeyStroke("TAB"),
				"selectNextRowCell");

		// add listener to model
		ListSelectionModel lsm = table.getSelectionModel();
		lsm.addListSelectionListener(new RowListener());

		// create a scroll pane with the table
		scrollPane = new JScrollPane(table);
		scrollPane.setSize(new Dimension(1000, 50));
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// add the scroll pane to the panel
		this.add(scrollPane);
	}

	/**
	 * Inner class that listens for row selection events. Display mail that is
	 * selected from table in panel
	 */
	private class RowListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (!lsm.isSelectionEmpty()) {
					// display mail that is selected in panel
					selectedRow = lsm.getMinSelectionIndex();
					Mail mail = mailTableModel.getMail(selectedRow);
					messageDisplayModel.receiveMail(mail);
				}
			}
		}
	}
}
