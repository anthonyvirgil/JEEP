/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MessageDisplayPanel.java
 */
package mail;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_beans.Folder;
import java_beans.Mail;

import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import com.mysql.jdbc.Messages;

import databases.DBManager;

import app.JEEP_GUI;

/**
 * View that will display a mail message and its contents
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.2
 */
public class MessageDisplayPanel extends JPanel implements Observer {

	// class variables
	private Mail email = null;
	private JLabel senderLabel = null;
	private JLabel recipientLabel = null;
	private JLabel ccLabel = null;
	private JLabel bccLabel = null;
	private JLabel subjectLabel = null;
	private JLabel dateLabel = null;
	private JEditorPane messageDisplay = null;
	private CardLayout cardLayout = null;
	private ResourceBundle messages = null;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * MessageDisplayPanel constructor
	 * 
	 * @param dbm
	 *            Database manager that communicates with the database
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MessageDisplayPanel(DBManager dbm, ResourceBundle messages) {
		super();
		this.messages = messages;
		initialize();
	}

	/**
	 * Sets card layout for this class to display various panels
	 * 
	 * @param cardLayout
	 *            CardLayout to be set
	 */
	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);

		// create titled border with name of the panel
		TitledBorder title;
		title = BorderFactory.createTitledBorder(messages
				.getString("messageDisplay"));

		// create label for "From"
		JLabel jLabel = new JLabel();
		jLabel.setText(messages.getString("from"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 0, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create label for "Sender"
		senderLabel = new JLabel();
		senderLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		add(senderLabel,
				getConstraints(1, 0, 1, 1, GridBagConstraints.WEST, 0, 0));

		// create label for "To"
		jLabel = new JLabel();
		jLabel.setText(messages.getString("ToLabel"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 1, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create label for "Recipient(s)"
		recipientLabel = new JLabel();
		recipientLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		add(recipientLabel,
				getConstraints(1, 1, 1, 1, GridBagConstraints.WEST, 0, 0));

		// create label for "CC"
		jLabel = new JLabel();
		jLabel.setText(messages.getString("CClabel"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 2, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create label for "CC Recipient(s)"
		ccLabel = new JLabel();
		ccLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		add(ccLabel, getConstraints(1, 2, 1, 1, GridBagConstraints.WEST, 0, 0));

		// create label for "CC"
		jLabel = new JLabel();
		jLabel.setText(messages.getString("BCClabel"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 3, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create label for "BCC Recipient(s)"
		bccLabel = new JLabel();
		bccLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		add(bccLabel, getConstraints(1, 3, 1, 1, GridBagConstraints.WEST, 0, 0));

		// create label for "Subject"
		jLabel = new JLabel();
		jLabel.setText(messages.getString("subject"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 4, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create label for "Recipient(s)"
		subjectLabel = new JLabel();
		subjectLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		add(subjectLabel,
				getConstraints(1, 4, 1, 1, GridBagConstraints.WEST, 0, 0));

		// create label for "Date"
		jLabel = new JLabel();
		jLabel.setText(messages.getString("date"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 5, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create label for "Recipient(s)"
		dateLabel = new JLabel();
		dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		add(dateLabel,
				getConstraints(1, 5, 1, 1, GridBagConstraints.WEST, 0, 0));

		// add JEditorPane for message
		messageDisplay = new JEditorPane();
		messageDisplay.setEditable(false);
		messageDisplay.setFont(new Font("Arial", Font.PLAIN, 12));
		messageDisplay.setContentType("text/html");
		messageDisplay.setCaretPosition(0);
		JScrollPane scrollPane = new JScrollPane(messageDisplay);
		scrollPane.setPreferredSize(new Dimension(850, 150));
		add(scrollPane,
				getConstraints(0, 6, 6, 1, GridBagConstraints.CENTER, 1, 1));

		// set border and size of panel
		this.setBorder(title);
		this.setPreferredSize(new Dimension(1000, 400));
	}

	/**
	 * A method for setting grid bag constraints
	 * 
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param anchor
	 * @return
	 */
	private GridBagConstraints getConstraints(int gridx, int gridy,
			int gridwidth, int gridheight, int anchor, double weightx,
			double weighty) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.anchor = anchor;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = weightx;
		c.weighty = weighty;
		return c;
	}

	/**
	 * Update method that will be called when model notifies observers of a
	 * change
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof MessageDisplayModel) {
			MessageDisplayModel localMessageModel = (MessageDisplayModel) o;

			// get email from model
			email = localMessageModel.getMail();

			// set labels to the email's contents
			senderLabel.setText(email.getSender());
			recipientLabel.setText(email.getToRecipientListString());
			ccLabel.setText(email.getCcRecipientListString());
			bccLabel.setText(email.getBccRecipientListString());
			subjectLabel.setText(email.getSubject());
			dateLabel.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(email.getDate().getTime()));
			messageDisplay.setText(email.getMessage());
			messageDisplay.setCaretPosition(0);

			// display the panel
			cardLayout.show(this.getParent(), JEEP_GUI.DISPLAY_MAIL);
		}
	}

	/**
	 * Prints the message in the JEditorPane
	 */
	public boolean printMessage() {
		boolean retVal = true;
		if (email != null) {
			// Set header of the printed message with subject and sender
			MessageFormat header = new MessageFormat(email.getSubject());
			MessageFormat footer = new MessageFormat(email.getSender());
			try {
				// print email in message display
				messageDisplay.print(header, footer);
			} catch (PrinterException e) {
				// log error and display message
				logger.log(Level.WARNING, "Error while printing message.");
				JOptionPane.showMessageDialog(null,
						messages.getString("printError"),
						messages.getString("printErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
				retVal = false;
			}
		} else {
			// display error message, no email selected
			JOptionPane.showMessageDialog(null,
					messages.getString("printNoEmailError"),
					messages.getString("printErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
			retVal = false;
		}
		return retVal;
	}
}
