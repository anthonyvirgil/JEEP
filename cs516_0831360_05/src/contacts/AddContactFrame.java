/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * AddContactFrame.java
 */
package contacts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;

import java_beans.Contact;
import java_beans.MailConfig;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import databases.DBManager;

import regex.RegexFormatter;

/**
 * Frame that allows user to add a new contact
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.2
 */
public class AddContactFrame extends JFrame {

	// class variables
	private JFormattedTextField emailInput;
	private JTextField lastNameInput;
	private JTextField firstNameInput;
	private DBManager dbm;
	private ContactModel model;
	private JPanel panel;
	private JButton addButton, cancelButton;
	private CustomFocusTraversalPolicy newPolicy;
	private ResourceBundle messages;

	/**
	 * Constructor for AddContactFrame class
	 * 
	 * @param dbm
	 *            Database manager that communicates with database
	 * @param Model
	 *            Model associated with ContactList UIs
	 * @param messages
	 *            Resource Bundle for internationalization
	 */
	public AddContactFrame(DBManager dbm, ContactModel model,
			ResourceBundle messages) {
		super();
		this.dbm = dbm;
		this.model = model;
		this.messages = messages;
		initialize();
		setTitle(messages.getString("addContactTitle"));
		add(panel, BorderLayout.CENTER);
		pack();
		centerScreen();
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Creates and adds all components for this frame's functionality
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		panel = new JPanel();

		panel.setLayout(gridBagLayout);

		// create label for "First Name"
		JLabel jLabel2 = new JLabel();
		jLabel2.setText(messages.getString("firstName"));
		jLabel2.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel2, getConstraints(0, 0, 1, 1, GridBagConstraints.EAST));

		// create label for "Last Name"
		JLabel jLabel3 = new JLabel();
		jLabel3.setText(messages.getString("lastName"));
		jLabel3.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel3, getConstraints(0, 1, 1, 1, GridBagConstraints.EAST));

		// create label for "Email"
		JLabel jLabel4 = new JLabel();
		jLabel4.setText(messages.getString("email"));
		jLabel4.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel4, getConstraints(0, 2, 1, 1, GridBagConstraints.EAST));

		// create email text field using a regular expression
		String emailRegEx = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		emailInput = new JFormattedTextField(new RegexFormatter(emailRegEx));
		emailInput.setColumns(20);
		panel.add(emailInput,
				getConstraints(1, 2, 3, 1, GridBagConstraints.CENTER));

		// create text field for first name
		firstNameInput = new JTextField();
		firstNameInput.setColumns(20);
		panel.add(firstNameInput,
				getConstraints(1, 0, 3, 1, GridBagConstraints.CENTER));

		// create text field for last name
		lastNameInput = new JTextField();
		lastNameInput.setColumns(20);
		panel.add(lastNameInput,
				getConstraints(1, 1, 3, 1, GridBagConstraints.CENTER));

		// create and add button panel to frame
		panel.add(getButtonPanel(),
				getConstraints(0, 3, 4, 1, GridBagConstraints.CENTER));

		// set size
		panel.setSize(gridBagLayout.preferredLayoutSize(this));

		// add components for focus traversal policy
		Vector<Component> order = new Vector<Component>(3);
		order.add(firstNameInput);
		order.add(lastNameInput);
		order.add(emailInput);
		order.add(addButton);
		order.add(cancelButton);

		// set focus traversal policy to a custom-made policy
		newPolicy = new CustomFocusTraversalPolicy(order);
		this.setFocusTraversalPolicy(newPolicy);
	}

	/**
	 * Returns a button panel with Add and Cancel buttons
	 * 
	 * @return Button panel for the frame
	 */
	private JPanel getButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		MyActionListener buttonListener = new MyActionListener();

		// create Add button
		addButton = new JButton();
		addButton.setText(messages.getString("add"));
		addButton.addActionListener(buttonListener);
		buttonPanel.add(addButton,
				getConstraints(0, 0, 1, 1, GridBagConstraints.CENTER));

		// create Cancel button
		cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancel"));
		cancelButton.addActionListener(buttonListener);
		buttonPanel.add(cancelButton,
				getConstraints(1, 0, 1, 1, GridBagConstraints.CENTER));

		return buttonPanel;
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
	 * @return GridBagConstraints object for component
	 */
	private GridBagConstraints getConstraints(int gridx, int gridy,
			int gridwidth, int gridheight, int anchor) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.anchor = anchor;
		return c;
	}

	/**
	 * Centers the frame in middle of screen
	 */
	private void centerScreen() {
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
	}

	/**
	 * Inner class action listener for buttons
	 */
	private class MyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (cancelButton == e.getSource())
				// close frame when cancel is pressed
				dispose();
			else if (firstNameInput.getText().trim().equals("")
					|| lastNameInput.getText().trim().equals("")
					|| emailInput.getText().trim().equals(""))
				// user didn't input all fields, display error
				JOptionPane.showMessageDialog(null,
						messages.getString("addError"),
						messages.getString("addErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			else {
				// add contact to Jlist
				addContact(new Contact(emailInput.getText().trim(),
						firstNameInput.getText().trim(), lastNameInput
								.getText().trim()));
				dispose();
			}

		}
	}

	/**
	 * Adds contact to database and tells model to fill JList with all contacts
	 * from database
	 * 
	 * @param Contact
	 *            to add to database
	 */
	private void addContact(Contact contact) {
		if (!dbm.addContact(contact))
			// display error if failed to add contact
			JOptionPane.showMessageDialog(getParent(),
					messages.getString("addContactError"),
					messages.getString("addErrorTitle"),
					JOptionPane.ERROR_MESSAGE);

		// send list of updated contacts to model
		model.receiveContacts(dbm.getAllContacts());
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
