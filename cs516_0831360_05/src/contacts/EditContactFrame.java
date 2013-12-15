/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * EditContactFrame.java
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
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;

import java_beans.Contact;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import contacts.AddContactFrame.CustomFocusTraversalPolicy;

import databases.DBManager;

import regex.RegexFormatter;

/**
 * Frame that allows user to edit an existing contact
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.1
 */
public class EditContactFrame extends JFrame {

	// class variables
	private JFormattedTextField emailInput;
	private JTextField lastNameInput;
	private JTextField firstNameInput;
	private DBManager dbm;
	private ContactModel model;
	private ArrayList<Contact> contactList;
	private Contact contact;
	private JPanel panel;
	private JButton editButton, deleteButton, cancelButton;
	private CustomFocusTraversalPolicy newPolicy;
	private ResourceBundle messages;

	/**
	 * EditContactFrame constructor
	 * 
	 * @param dbm
	 *            Database manager that communicates with database
	 * @param contactList
	 *            List of contacts in the application
	 * @param contact
	 *            Contact to be edited
	 * @param model
	 *            Contact model
	 * @param messages
	 *            ResourceBundle needed for internationalization
	 */
	public EditContactFrame(DBManager dbm, ArrayList<Contact> contactList,
			Contact contact, ContactModel model, ResourceBundle messages) {
		super();
		this.dbm = dbm;
		this.contact = contact;
		this.contactList = contactList;
		this.model = model;
		this.messages = messages;
		initialize();
		setTitle(messages.getString("editContactTitle"));
		add(panel, BorderLayout.CENTER);
		pack();
		centerScreen();
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Creates and adds necessary components to the frame
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
		emailInput.setText(contact.getEmailAddress());
		panel.add(emailInput,
				getConstraints(1, 2, 3, 1, GridBagConstraints.CENTER));

		// create text field for a name
		firstNameInput = new JTextField();
		firstNameInput.setColumns(20);
		firstNameInput.setText(contact.getFirstName());
		panel.add(firstNameInput,
				getConstraints(1, 0, 3, 1, GridBagConstraints.CENTER));

		// create text field for a name
		lastNameInput = new JTextField();
		lastNameInput.setColumns(20);
		lastNameInput.setText(contact.getLastName());
		panel.add(lastNameInput,
				getConstraints(1, 1, 3, 1, GridBagConstraints.CENTER));

		// add button panel with delete, edit and cancel
		panel.add(getButtonPanel(),
				getConstraints(0, 3, 4, 1, GridBagConstraints.CENTER));

		panel.setSize(gridBagLayout.preferredLayoutSize(this));

		// add components for focus traversal policy
		Vector<Component> order = new Vector<Component>(6);
		order.add(firstNameInput);
		order.add(lastNameInput);
		order.add(emailInput);
		order.add(editButton);
		order.add(deleteButton);
		order.add(cancelButton);

		// set focus traversal policy to custom-made policy
		newPolicy = new CustomFocusTraversalPolicy(order);
		this.setFocusTraversalPolicy(newPolicy);
	}

	/**
	 * Creates a button panel with Edit, Delete and Cancel buttons
	 * 
	 * @return Button panel
	 */
	private JPanel getButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		ButtonListener buttonListener = new ButtonListener();

		// create button for adding/editing a contact
		editButton = new JButton();
		editButton.setText(messages.getString("edit"));
		editButton.addActionListener(buttonListener);
		buttonPanel.add(editButton,
				getConstraints(0, 0, 1, 1, GridBagConstraints.CENTER));

		// create button for delete a contact
		deleteButton = new JButton();
		deleteButton.setText(messages.getString("delete"));
		deleteButton.addActionListener(buttonListener);
		buttonPanel.add(deleteButton,
				getConstraints(1, 0, 1, 1, GridBagConstraints.CENTER));

		// create button for delete a contact
		cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancel"));
		cancelButton.addActionListener(buttonListener);
		buttonPanel.add(cancelButton,
				getConstraints(2, 0, 1, 1, GridBagConstraints.CENTER));

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
	 * Centers the frame in the screen
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
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (cancelButton == e.getSource())
				// dispose frame when cancel is clicked
				dispose();
			else if (deleteButton == e.getSource()) {
				// display confirm dialog for deleting
				int result = JOptionPane.showConfirmDialog(
						null,
						messages.getString("deleteConfirm") + " "
								+ contact.getFirstName() + " "
								+ contact.getLastName() + "?",
						messages.getString("deleteConfirmTitle"),
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					// delete contact and dispose frame
					deleteContact(contact);
					dispose();
				}
			}
			// if neither of above buttons, user wants to edit
			else if (firstNameInput.getText().trim().equals("")
					|| lastNameInput.getText().trim().equals("")
					|| emailInput.getText().trim().equals(""))
				// not all fields are filled in, display message
				JOptionPane.showMessageDialog(null,
						messages.getString("editContactFrameError"),
						messages.getString("editContactErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			else if (editButton == e.getSource()) {
				// edit the contact's fields and dispose frame
				contact.setEmailAddress(emailInput.getText().trim());
				contact.setFirstName(firstNameInput.getText().trim());
				contact.setLastName(lastNameInput.getText().trim());
				editContact(contact);
				dispose();
			}
		}
	}

	/**
	 * Deletes a contact from the list of contacts
	 * 
	 * @param contact
	 *            Contact to be deleted
	 */
	private void deleteContact(Contact contact) {
		for (int i = 0; i < contactList.size(); i++) {
			if (contactList.get(i).getID() == contact.getID()) {
				if (!dbm.deleteContact(contact))
					// display error if failed to delete contact
					JOptionPane.showMessageDialog(getParent(),
							messages.getString("contactDeleteError"),
							messages.getString("deleteErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
			}
		}
		// send list of updated contacts to model
		model.receiveContacts(dbm.getAllContacts());
	}

	/**
	 * Edits a contact from the list of contacts
	 * 
	 * @param contact
	 *            Contact to be edited
	 */
	private void editContact(Contact contact) {
		for (int i = 0; i < contactList.size(); i++) {
			if (contactList.get(i).getID() == contact.getID()) {
				if (!dbm.editContact(contact))
					// display error if failed to edit contact
					JOptionPane.showMessageDialog(getParent(),
							messages.getString("contactEditError"),
							messages.getString("editErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
			}
		}
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
