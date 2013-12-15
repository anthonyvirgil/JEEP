/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * ContactListPanel.java
 */
package contacts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import java_beans.Contact;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import databases.DBManager;

/**
 * Contact list panel that contains a list of all user's contacts
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.1
 */
public class ContactListPanel extends JPanel implements Observer {

	// class variables
	private ContactModel contactModel;
	private ArrayList<Contact> contactList;
	private DefaultListModel<Contact> model;
	private DBManager dbm;
	private JList<Contact> list;
	private JScrollPane scrollPane;
	private ResourceBundle messages;

	/**
	 * Constructor for ContactListPanel
	 * 
	 * @param contactModel
	 *            Contact model associated with UI
	 * @param dbm
	 *            Database manager that communicates with database
	 * @param messages
	 *            ResourceBundle object used for internationalization
	 */
	public ContactListPanel(ContactModel contactModel, DBManager dbm,
			ResourceBundle messages) {
		super();
		this.contactModel = contactModel;
		this.dbm = dbm;
		this.messages = messages;
		contactList = dbm.getAllContacts();
		contactModel.receiveContacts(contactList);
		initialize();
		addPopupMenu();
	}

	/**
	 * Display a frame to allow user to add a new contact
	 */
	public void createContact() {
		new AddContactFrame(dbm, contactModel, messages);
	}

	/**
	 * Displays a frame to allow a user to edit an existing contact
	 */
	public void editContact() {
		if (list.getSelectedIndex() != -1)
			// display frame for editing
			new EditContactFrame(dbm, contactList,
					(Contact) list.getSelectedValue(), contactModel, messages);
	}

	/**
	 * Displays a frame to allow a user to delete an existing contact
	 */
	public void deleteContact() {
		if (list.getSelectedIndex() != -1)
			// display frame for editing
			new EditContactFrame(dbm, contactList,
					(Contact) list.getSelectedValue(), contactModel, messages);
	}

	/**
	 * Creates all objects needed for the panel
	 */
	private void initialize() {
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);

		// create titled border for contacts
		TitledBorder title;
		title = BorderFactory
				.createTitledBorder(messages.getString("contacts"));

		// create new JList of contacts and features
		list = new JList<Contact>();
		updateJList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new ActionJList());

		scrollPane = new JScrollPane(list);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setBorder(title);
		this.setPreferredSize(new Dimension(150, 400));
	}

	/**
	 * Updates whole JList when a change is made with a contact
	 */
	private void updateJList() {
		model = new DefaultListModel<Contact>();

		for (Contact c : contactList) {
			model.addElement(c);
		}
		list.setModel(model);
	}

	/**
	 * Adds a popup menu for Contact List UI
	 */
	private void addPopupMenu() {
		JMenuItem menuItem;

		// Create the popup menu.
		JPopupMenu popup = new JPopupMenu();

		// Add Contact menu item
		menuItem = new JMenuItem(messages.getString("addContact"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// create a new contact
				createContact();
			}
		});
		popup.add(menuItem);

		// Edit Contact menu item
		menuItem = new JMenuItem(messages.getString("editContact"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// edit contact
				editContact();
			}
		});
		popup.add(menuItem);

		popup.addSeparator();

		// Delete contact menu item
		menuItem = new JMenuItem(messages.getString("deleteContact"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// delete contact
				deleteContact();
			}
		});
		popup.add(menuItem);

		// Add listener to the component so the popup menu can appear
		MouseListener popupListener = new PopupListener(popup);
		list.addMouseListener(popupListener);
	}

	/**
	 * Inner class for a mouse listener object
	 * 
	 * @author Anthony-Virgil Bermejo
	 */
	private class ActionJList extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// user double clicks on a contact, send email to focused text box
			if (e.getClickCount() == 2) {
				int index = list.locationToIndex(e.getPoint());
				ListModel<Contact> dlm = list.getModel();
				if (dlm.getElementAt(index) != null)
					contactModel.addContact(dlm.getElementAt(index));
				list.ensureIndexIsVisible(index);
			}
		}
	}

	/**
	 * Mouse listener that displays the popup menu
	 */
	private class PopupListener extends MouseAdapter {
		JPopupMenu popup;

		public PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * Update method that is called when a model notifies observers of a change
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof ContactModel) {
			ContactModel localContactModel = (ContactModel) o;
			contactList = localContactModel.getContactList();

			// update JList with new list of contacts
			updateJList();
		}

	}
}
