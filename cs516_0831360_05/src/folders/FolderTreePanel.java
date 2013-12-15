/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * FolderTreePanel.java
 */
package folders;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_beans.Contact;
import java_beans.Folder;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import app.JEEP_GUI;

import mail.MailTablePanel;

import databases.DBManager;

/**
 * Panel that holds a JTree of several folders in the JEEP application
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.2
 * 
 */
public class FolderTreePanel extends JPanel implements TreeSelectionListener,
		Observer {

	private DBManager dbm = null;
	private JTree tree = null;
	private JScrollPane treeView = null;
	private ArrayList<Folder> folderList = null;
	private DefaultTreeModel model = null;
	private DefaultMutableTreeNode top = null;
	private FolderModel folderModel = null;
	private MailTablePanel mailTablePanel = null;
	private ResourceBundle messages;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * FolderTreePanel constructor
	 * 
	 * @param folderModel
	 *            Model of the MVC pattern for this view
	 * @param dbm
	 *            Database manager that includes CRUD methods for folders table
	 * @param mailTable
	 *            Table UI that contains the mail data
	 * @param messages
	 *            ResourceBundle that will be used for internationalization
	 */
	public FolderTreePanel(FolderModel folderModel, DBManager dbm,
			MailTablePanel mailTable, ResourceBundle messages) {
		super();
		this.folderModel = folderModel;
		this.dbm = dbm;
		this.mailTablePanel = mailTable;
		this.messages = messages;
		this.folderList = dbm.getFolders();
		folderModel.receiveFolders(folderList);
		initialize();
	}

	/**
	 * Creates a folder and adds it to the folders table in the database.
	 * Prompts the user for a folder name.
	 */
	public void createFolder() {
		// prompt user to enter a new folder
		String folderName = JOptionPane.showInputDialog(null,
				messages.getString("addFolderPrompt"),
				messages.getString("addFolderPromptTitle"),
				JOptionPane.QUESTION_MESSAGE);
		if (folderName != null) {
			if (!folderName.equals("")) {
				// if folder name is valid
				if (verifyFolderName(folderName)) {

					// create Folder and add it to database
					Folder newFolder = new Folder(folderName);
					addFolder(newFolder);

				} else
					// folder name is one of default folders
					JOptionPane.showMessageDialog(null,
							messages.getString("addFolderError"),
							messages.getString("addFolderErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Edits an existing folder in the application. Prompts the user for a new
	 * folder name.
	 */
	public void editFolder() {
		// show edit folder input dialog if a folder was selected
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node != null) {
			Folder folder = (Folder) node.getUserObject();

			if (!verifyFolderName(folder.getName())) {
				// name is one of default folders, display error
				JOptionPane.showMessageDialog(null,
						messages.getString("editImportantFolderError"),
						messages.getString("editFolderErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			} else {
				// show input dialog, prompting user for new folder name
				String newFolderName = JOptionPane.showInputDialog(
						null,
						messages.getString("editFolderPrompt") + " "
								+ folder.getName(),
						messages.getString("editFolderTitle"),
						JOptionPane.QUESTION_MESSAGE);
				if (newFolderName != null) {
					if (!newFolderName.equals("")) {

						// if name is valid, update folder name in table
						folder.setName(newFolderName);
						editFolder(folder);

					} else
						// display error if empty field
						JOptionPane.showMessageDialog(null,
								messages.getString("folderEmptyError"),
								messages.getString("editFolderErrorTitle"),
								JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * Deletes an existing folder in the application.
	 */
	public void deleteFolder() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node != null) {
			Folder folder = (Folder) node.getUserObject();
			String folderName = folder.getName();
			// check if folder is one of the default folders
			if (verifyFolderName(folderName)) {
				// show dialog confirming action
				int result = JOptionPane.showConfirmDialog(null,
						messages.getString("deleteFolderConfirm") + " "
								+ folderName + " ?",
						messages.getString("deleteFolderTitle"),
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					// remove folder from database
					removeFolder(folder);
				}
			} else
				// folder is a custom folder
				JOptionPane.showMessageDialog(null,
						messages.getString("deleteImportantFolderError"),
						messages.getString("deleteFolderErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Initializes the view, creating components and adding them to the view
	 */
	private void initialize() {
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);

		// create border with title of panel
		TitledBorder title;
		title = BorderFactory.createTitledBorder(messages.getString("folders"));
		this.setBorder(title);

		// populate JTree with folders in database
		createTree();
		add(treeView, BorderLayout.CENTER);

		this.setPreferredSize(new Dimension(150, 200));
	}

	/**
	 * Creates a JTree for the folder nodes
	 */
	private void createTree() {

		// create the top node
		top = new DefaultMutableTreeNode(messages.getString("JEEPMail"));

		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		// allow a drop only
		tree.setDropMode(DropMode.ON);

		// The TransferHandler needs to communicate with the DBManager and the
		// DBTableModel
		tree.setTransferHandler(new FolderTreeTransferHandler(tree,
				mailTablePanel, dbm));

		// update JTree with values from the database
		updateJTree();

		// make the popup menu
		makeTreePopupMenu();

		// listen for when the selection changes.
		tree.addTreeSelectionListener(this);

		// hide the root node
		tree.setRootVisible(false);

		// add icons to each node
		ImageIcon leafIcon = createImageIcon("/images/folder.png");
		if (leafIcon != null) {
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(leafIcon);
			tree.setCellRenderer(renderer);
		}

		// create the scroll pane and add the tree to it.
		treeView = new JScrollPane(tree);

	}

	/**
	 * Updates JTree when there is a change in the folders table
	 */
	private void updateJTree() {
		model = new DefaultTreeModel(top);
		top.removeAllChildren();

		for (Folder f : folderList) {
			DefaultMutableTreeNode dmtn = createNode(f);
			top.add(dmtn);
		}
		tree.setModel(model);
	}

	/**
	 * Creates a node in the JTree
	 * 
	 * @param folder
	 *            Folder to be added to the tree
	 * @return Node that will be added
	 */
	private DefaultMutableTreeNode createNode(Object folder) {
		DefaultMutableTreeNode folderNode = null;
		folderNode = new DefaultMutableTreeNode(folder);
		return folderNode;
	}

	/**
	 * Validates what folder name is being created/deleted/edited. Checks that
	 * it is not one of the default folders
	 * 
	 * @param folderName
	 *            Name of folder to be edited
	 * @return True, if folder name is valid. False, if folder name is one of
	 *         default folders
	 */
	private boolean verifyFolderName(String folderName) {
		return !(folderName.equals(JEEP_GUI.INBOX_FOLDER)
				|| folderName.equals(JEEP_GUI.SENT_FOLDER)
				|| folderName.equals(JEEP_GUI.OUTBOX_FOLDER) || folderName
					.equals(JEEP_GUI.TRASH_FOLDER));
	}

	/**
	 * Event handler for when a user selects a node
	 */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node != null) {
			// populate mail table with emails from clicked folder node
			Folder aFolder = (Folder) node.getUserObject();
			if (aFolder != null)
				mailTablePanel.changeTableToFolder(aFolder.getName());
		}
	}

	/**
	 * Create the popup menu and attach it to the tree
	 */
	private void makeTreePopupMenu() {
		JMenuItem menuItem;

		// Create the popup menu.
		JPopupMenu popup = new JPopupMenu();

		// add folder menu item
		menuItem = new JMenuItem(messages.getString("addFolderPromptTitle"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// creates a new folder
				createFolder();
			}
		}

		);
		popup.add(menuItem);

		// edit folder menu item
		menuItem = new JMenuItem(messages.getString("editFolderTitle"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// edit the selected folder
				editFolder();
			}
		});
		popup.add(menuItem);

		popup.addSeparator();

		// delete folder menu item
		menuItem = new JMenuItem(messages.getString("deleteFolderTitle"));
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// delete selected folder
				deleteFolder();
			}
		});
		popup.add(menuItem);

		// add mouse listener to the text area so the popup menu can come up.
		MouseListener popupListener = new PopupListener(popup);

		// add listener to the tree
		tree.addMouseListener(popupListener);
	}

	/**
	 * Adds folder to the database
	 * 
	 * @param folder
	 *            Folder to be added to database
	 */
	private void addFolder(Folder folder) {
		if (!dbm.addFolder(folder))
			// display error if failed to add
			JOptionPane.showMessageDialog(null,
					messages.getString("addFolderDBError"),
					messages.getString("addFolderErrorTitle"),
					JOptionPane.ERROR_MESSAGE);

		folderModel.receiveFolders(dbm.getFolders());
	}

	/**
	 * Deletes a folder from the database
	 * 
	 * @param folder
	 *            Folder to be deleted
	 */
	private void removeFolder(Folder folder) {
		// iterate through list of folder and find that which will be deleted
		for (int i = 0; i < folderList.size(); i++) {
			if (folderList.get(i).getID() == folder.getID())
				if (!dbm.deleteFolder(folder))
					// display error if failed to delete
					JOptionPane.showMessageDialog(null,
							messages.getString("deleteFolderDBError"),
							messages.getString("deleteFolderErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
		}
		// send list of updated folders to model
		folderModel.receiveFolders(dbm.getFolders());
	}

	/**
	 * Updates a folder in the database
	 * 
	 * @param folder
	 *            Folder to be updated in database
	 */
	private void editFolder(Folder folder) {
		// iterate through list of folder and find which will be edited
		for (int i = 0; i < folderList.size(); i++) {
			if (folderList.get(i).getID() == folder.getID()) {
				if (!dbm.editFolder(folder))
					// display error if failed to edit
					JOptionPane.showMessageDialog(null,
							messages.getString("editFolderDBError"),
							messages.getString("editFolderErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
			}
		}
		// send list of updated folders to model
		folderModel.receiveFolders(dbm.getFolders());
	}

	/**
	 * Mouse listener that displays the popup menu
	 */
	private class PopupListener extends MouseAdapter {

		JPopupMenu popup;

		public PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
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
	 * Method that creates an image icon for the folder nodes
	 * 
	 * @param path
	 *            Path where the image is located
	 * @return ImageIcon for the folder
	 */
	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FolderTreePanel.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			// log error
			logger.log(Level.WARNING,
					"Error loading icon for folders. Unable to find file: "
							+ path);
			return null;
		}
	}

	/**
	 * Update method that is called when associated model calls
	 * notifyObservers()
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof FolderModel) {
			FolderModel localFolderModel = (FolderModel) o;
			folderList = localFolderModel.getFolderList();

			// update tree with updated list of folders
			updateJTree();
		}
	}
}
