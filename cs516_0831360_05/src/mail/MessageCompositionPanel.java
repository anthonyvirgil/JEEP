/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MessageCompositionPanel.java
 */
package mail;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Vector;

import java_beans.Contact;
import java_beans.Mail;
import java_beans.MailConfig;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import com.mysql.jdbc.Messages;

import app.JEEP_GUI;

import contacts.ContactModel;
import databases.DBManager;

/**
 * View that contains components and functionality needed to compose a new mail
 * message
 * 
 * @author Anthony-Virgil Bermejo
 * @version 3.1
 * 
 */
public class MessageCompositionPanel extends JPanel implements Observer {

	// class variables
	private JTextField toInput, bccInput, ccInput, subjectInput;
	private JEditorPane messageInput;
	private JLabel subjectLabel;
	private CardLayout cardLayout;
	private Mail email;
	private ArrayList<String> toContactList;
	private ArrayList<String> bccContactList;
	private ArrayList<String> ccContactList;
	private JTextComponent currentTextField;
	private String lastCommand;
	private MailConfig mailConfig;
	private MessageCompositionModel messageCompositionModel;
	private DBManager dbm;
	private CustomFocusTraversalPolicy newPolicy;
	private ResourceBundle messages;

	/**
	 * MessageCompositionPanel constructor
	 * 
	 * @param messageCompositionModel
	 *            Model of the MVC pattern associated with this view
	 * @param dbm
	 *            Database manager that communicates with the database
	 * @param mailConfig
	 *            Mail configuration of user
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MessageCompositionPanel(
			MessageCompositionModel messageCompositionModel, DBManager dbm,
			MailConfig mailConfig, ResourceBundle messages) {
		this.mailConfig = mailConfig;
		this.dbm = dbm;
		this.messages = messages;
		this.messageCompositionModel = messageCompositionModel;
		toContactList = new ArrayList<String>();
		bccContactList = new ArrayList<String>();
		ccContactList = new ArrayList<String>();
		initialize();

		// determine what the last action was
		lastCommand = "Empty";
	}

	/**
	 * Sets the card layout to its class variable
	 * 
	 * @param cardLayout
	 *            CardLayout to be set
	 */
	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	/**
	 * Sends a newly composed email to the Outbox folder
	 */
	public void sendNewEmail() {
		email = new Mail(mailConfig.getEmail(), toInput.getText(),
				bccInput.getText(), ccInput.getText(), subjectInput.getText(),
				messageInput.getText(), new Date(), JEEP_GUI.OUTBOX_FOLDER);

		// check if there is at least a sender
		if (email.getToRecipientList().size() != 0) {

			// insert mail into database
			dbm.addMail(email);

			// if compose mail, cancel email and display blank panel
			if (lastCommand.equals("ComposeMail"))
				cardLayout.show(MessageCompositionPanel.this.getParent(),
						JEEP_GUI.BLANK_PANEL);
			else
				// if reply or forward, send and display previous mail
				cardLayout.show(MessageCompositionPanel.this.getParent(),
						JEEP_GUI.DISPLAY_MAIL);

			// send mail to model
			messageCompositionModel.retrieveMail(email);

		} else
			// display error message if no sender
			JOptionPane.showMessageDialog(null,
					messages.getString("composeMailError"),
					messages.getString("sendErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Initializes this view with necessary components
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);

		// add titled border with the name of the panel
		TitledBorder title;
		title = BorderFactory.createTitledBorder(messages
				.getString("messageComposition"));

		TextFieldListener textListener = new TextFieldListener();

		// create label for recipient
		JLabel jLabel = new JLabel();
		jLabel.setText(messages.getString("ToLabel"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 0, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create text field for recipient
		toInput = new JTextField();
		toInput.setColumns(70);
		toInput.addFocusListener(textListener);
		add(toInput, getConstraints(1, 0, 3, 1, GridBagConstraints.WEST, 1, 0));

		// create label for cc
		jLabel = new JLabel();
		jLabel.setText(messages.getString("CClabel"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 1, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create text field for cc
		ccInput = new JTextField();
		ccInput.setColumns(70);
		ccInput.addFocusListener(textListener);
		add(ccInput, getConstraints(1, 1, 3, 1, GridBagConstraints.WEST, 1, 0));

		// create label for bcc
		jLabel = new JLabel();
		jLabel.setText(messages.getString("BCClabel"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(jLabel, getConstraints(0, 2, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create text field for bcc
		bccInput = new JTextField();
		bccInput.setColumns(70);
		bccInput.addFocusListener(textListener);
		add(bccInput, getConstraints(1, 2, 3, 1, GridBagConstraints.WEST, 1, 0));

		// create label for subject
		subjectLabel = new JLabel();
		subjectLabel.setText(messages.getString("subjectLabel"));
		subjectLabel.setFont(new Font("Arial", Font.BOLD, 12));
		add(subjectLabel,
				getConstraints(0, 3, 1, 1, GridBagConstraints.EAST, 0, 0));

		// create text field for subject
		subjectInput = new JTextField();
		subjectInput.setColumns(70);
		add(subjectInput,
				getConstraints(1, 3, 3, 1, GridBagConstraints.WEST, 1, 0));

		// create JEditorPane for message
		messageInput = new JEditorPane();
		messageInput.setEditable(true);
		messageInput.setContentType("text/html");
		messageInput.setFont(new Font("Arial", Font.PLAIN, 12));
		messageInput.setCaretPosition(0);
		JScrollPane scrollPane = new JScrollPane(messageInput);
		scrollPane.setPreferredSize(new Dimension(850, 150));
		add(scrollPane,
				getConstraints(0, 4, 6, 1, GridBagConstraints.CENTER, 1, 1));

		// set titled border and size
		this.setBorder(title);
		this.setPreferredSize(new Dimension(1000, 400));

		// add components for focus traversal policy
		Vector<Component> order = new Vector<Component>(5);
		order.add(toInput);
		order.add(bccInput);
		order.add(ccInput);
		order.add(subjectInput);
		order.add(messageInput);

		// set focus traversal policy to custom-made policy
		newPolicy = new CustomFocusTraversalPolicy(order);
		this.setFocusTraversalPolicy(newPolicy);
	}

	/**
	 * Sets grid bag constraints to a component
	 * 
	 * @param gridx
	 *            X coordinate of component
	 * @param gridy
	 *            Y coordinate of component
	 * @param gridwidth
	 *            Width of component
	 * @param gridheight
	 *            Height of component
	 * @param anchor
	 *            Where the component will be placed in area
	 * @param weightx
	 *            How much space will be distributed among columns
	 * @param weighty
	 *            How much space will be distributed among rows
	 * @return GridBagConstraints object for component
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
	 * Clears the text fields of the composition panelS
	 */
	private void clearTextFields() {
		toInput.setText("");
		bccInput.setText("");
		ccInput.setText("");
		subjectInput.setText("");
		messageInput.setText("");
	}

	/**
	 * Private class that will set currentTextField variable to which textfield
	 * last had focus
	 * 
	 * @author Anthony-Virgil Bermejo
	 * 
	 */
	private class TextFieldListener implements FocusListener {

		// set textField variable when focus is gained
		@Override
		public void focusGained(FocusEvent e) {
			currentTextField = (JTextField) e.getComponent();
		}

		@Override
		public void focusLost(FocusEvent e) {
			// do nothing
		}
	}

	/**
	 * Update method that is called when model notifies its observers
	 */
	@Override
	public void update(Observable o, Object arg) {
		// if empty text fields, create new lists of recipients
		if (toInput.getText().equals(""))
			toContactList = new ArrayList<String>();
		if (bccInput.getText().equals(""))
			bccContactList = new ArrayList<String>();
		if (ccInput.getText().equals(""))
			ccContactList = new ArrayList<String>();

		if (o instanceof MessageCompositionModel) {
			MessageCompositionModel localModel = (MessageCompositionModel) o;
			email = localModel.getMail();

			// reset all fields
			clearTextFields();

			// set last action to what model calls
			lastCommand = (String) arg;

			if (lastCommand.equals("Reply")) { // user wants to reply
				if (!email.getSender().equals("")) {
					// set text to the sender and add to list of TO recipients
					toInput.setText(email.getSender());
					toContactList.add(email.getSender());
				}

				// set subject and message input fields
				subjectInput.setText("Re: " + email.getSubject());
				messageInput.setText("<br><br>------ "
						+ messages.getString("replyMessage")
						+ " ------<br><br>" + messages.getString("from") + " "
						+ email.getSender() + "<br>"
						+ messages.getString("subject") + " "
						+ email.getSubject() + "<br><br>" + email.getMessage());

			} else if (lastCommand.equals("Reply All")) {
				// user wants to reply to all recipients

				StringBuilder sb = new StringBuilder();

				if (!email.getSender().equals("")) {
					// add contact to To recipient list and append to a string
					// of To recipients
					toContactList.add(email.getSender());
					sb.append(email.getSender());
				}

				if (!email.getToRecipientListString().equals("")) {
					// get list of TO recipients
					ArrayList<String> toRecipientList = email
							.getToRecipientList();

					// add all emails from list to those who will receive new
					// mail
					for (String emailAddress : toRecipientList) {
						// if email is user's, do not add
						if (!emailAddress.equals(mailConfig.getEmail())) {
							// add email to list
							toContactList.add(emailAddress);

							// append to the string of to recipients
							sb.append(", ");
							sb.append(emailAddress);
						}
					}
				}

				if (!email.getCcRecipientListString().equals("")) {
					// get list of CC recipients
					ArrayList<String> ccRecipientList = email
							.getCcRecipientList();

					// add all emails from list to those who will receive new
					// mail
					for (String emailAddress : ccRecipientList) {
						// if email is the user's, do not add
						if (!emailAddress.equals(mailConfig.getEmail())) {
							// add email to list
							toContactList.add(emailAddress);

							// append to the string of to recipients
							sb.append(", ");
							sb.append(emailAddress);
						}
					}
				}

				// set To input textfield to the string of new recipients
				toInput.setText(sb.toString());

				// set text of subject and message input fields
				subjectInput.setText("Re: " + email.getSubject());
				messageInput.setText("<br><br>------ "
						+ messages.getString("replyMessage")
						+ " ------<br><br>" + messages.getString("from") + " "
						+ email.getSender() + "<br>"
						+ messages.getString("subject") + " "
						+ email.getSubject() + "<br><br>" + email.getMessage());
			} else if (lastCommand.equals("Forward")) {
				// user wants to forward message

				// set text of subject and message input fields
				subjectInput.setText("Fwd: " + email.getSubject());
				messageInput.setText("<br><br>------ "
						+ messages.getString("forwardedMessage")
						+ " ------<br><br>" + messages.getString("from") + " "
						+ email.getSender() + "<br>"
						+ messages.getString("subject") + " "
						+ email.getSubject() + "<br><br>" + email.getMessage());

			}

			// set mail in model to null (new email)
			localModel.retrieveMail(null);
			messageInput.setCaretPosition(0);

			// display composition panel to create a mail
			cardLayout.show(this.getParent(), JEEP_GUI.COMPOSE_MAIL);
		}

		if (o instanceof ContactModel) {
			ContactModel localModel = (ContactModel) o;
			Contact contact = localModel.getContact();
			ArrayList<String> currentList;

			if (contact != null) {
				// retrieve email of contact
				String emailAddress = contact.getEmailAddress();

				// get a handle to the list that holds a specific recipient
				if (toInput == currentTextField)
					currentList = toContactList;
				else if (bccInput == currentTextField)
					currentList = bccContactList;
				else
					currentList = ccContactList;

				// add email to current list of recipients
				currentList.add(emailAddress);

				if (currentTextField != null) {
					// if size of contact list is greater than 1
					if (currentList.size() > 1) {
						// add a comma along with textField
						currentTextField.setText(currentTextField.getText()
								+ ", " + emailAddress);
					} else
						currentTextField.setText(emailAddress);
				}
			}
		}
	}

	/**
	 * Custom FocusTraversalPolicy that determines where the focus will go when
	 * tab is pressed
	 * 
	 * @author Kenneth Fogel
	 */
	class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
		Vector<Component> order;

		/**
		 * Constructor Copies the contents of the vector parameter to the class
		 * vector
		 * 
		 * @param order
		 */
		public CustomFocusTraversalPolicy(Vector<Component> order) {
			this.order = new Vector<Component>(order.size());
			this.order.addAll(order);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.FocusTraversalPolicy#getComponentAfter(java.awt.Container,
		 * java.awt.Component)
		 */
		public Component getComponentAfter(Container focusCycleRoot,
				Component aComponent) {
			int idx = (order.indexOf(aComponent) + 1) % order.size();
			return order.get(idx);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.FocusTraversalPolicy#getComponentBefore(java.awt.Container,
		 * java.awt.Component)
		 */
		public Component getComponentBefore(Container focusCycleRoot,
				Component aComponent) {
			int idx = order.indexOf(aComponent) - 1;
			if (idx < 0) {
				idx = order.size() - 1;
			}
			return order.get(idx);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.FocusTraversalPolicy#getDefaultComponent(java.awt.Container)
		 */
		public Component getDefaultComponent(Container focusCycleRoot) {
			return order.get(0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.FocusTraversalPolicy#getLastComponent(java.awt.Container)
		 */
		public Component getLastComponent(Container focusCycleRoot) {
			return order.lastElement();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.FocusTraversalPolicy#getFirstComponent(java.awt.Container)
		 */
		public Component getFirstComponent(Container focusCycleRoot) {
			return order.get(0);
		}
	}
}
