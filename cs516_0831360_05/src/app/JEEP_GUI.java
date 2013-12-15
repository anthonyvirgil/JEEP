/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * JEEP_GUI.java
 */
package app;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java_beans.MailConfig;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import mail.MailReceiver;
import mail.MailSender;
import mail.MailTableModel;
import mail.MailTablePanel;
import mail.MessageCompositionModel;
import mail.MessageCompositionPanel;
import mail.MessageDisplayModel;
import mail.MessageDisplayPanel;

import contacts.ContactListPanel;
import contacts.ContactModel;
import databases.DBManager;
import folders.FolderModel;
import folders.FolderTreePanel;

import properties.MailConfigCompositionFrame;
import properties.MailConfigController;
import properties.MailConfigModel;

/**
 * Main application class of the JEEP application. Holds all panels of the user
 * interfaces as well as all classes needed for functionality.
 * 
 * Additional features not in specs: - Reply All function
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.5
 */
public class JEEP_GUI extends JFrame {

	// class variables
	private ResourceBundle messages = null;
	private DBManager dbm;
	private ContactModel contactModel;
	private ContactListPanel contactListPanel;
	private FolderModel folderModel;
	private FolderTreePanel folderTreePanel;
	private MailConfigModel mailConfigModel;
	private MailTablePanel mailTablePanel;
	private MailTableModel mailTableModel;
	private MessageDisplayModel messageDisplayModel;
	private MessageDisplayPanel messageDisplayPanel;
	private MailConfigController mailConfigController;
	private MessageCompositionPanel messageCompositionPanel;
	private MessageCompositionModel messageCompositionModel;
	private CardLayout cardLayout;
	private JPanel cards;
	private MailConfig mailConfig;
	private MailSender mailSender;
	private MailReceiver mailReceiver;
	private HelpBroker helpBroker;
	private HelpSet helpSet;
	private Locale currentLocale;
	private JPanel holderPanel;

	// public constants used throughout all classes
	public static final String COMPOSE_MAIL = "Compose Mail";
	public static final String DISPLAY_MAIL = "Display Mail";
	public static final String BLANK_PANEL = "Blank Panel";
	public static final String INBOX_FOLDER = "Inbox";
	public static final String SENT_FOLDER = "Sent";
	public static final String OUTBOX_FOLDER = "Outbox";
	public static final String TRASH_FOLDER = "Trash";

	// constant for debugging
	private static final boolean DEBUG = false;

	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());
	static {
		final InputStream inputStream = JEEP_GUI.class
				.getResourceAsStream("/logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(inputStream);

		} catch (final IOException e) {
			Logger.getAnonymousLogger().severe(
					"Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}

	/**
	 * JEEP_GUI Constructor
	 */
	public JEEP_GUI() {

		// set default locale of application, create resource bundle
		currentLocale = Locale.getDefault();
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);

		// create classes needed to load/write properties
		mailConfigController = new MailConfigController(this,
				"Configuration.ini", messages);
		mailConfigModel = new MailConfigModel();
		mailConfigModel.addObserver(mailConfigController);

		// if loading fails, display frame for user to input their configuration
		if (!mailConfigController.loadProperties()) {
			new MailConfigCompositionFrame(mailConfigModel, new MailConfig(),
					messages);
		}
	}

	/**
	 * Initializes the entire frame with all user interface classes.
	 */
	public void initialize() {
		// create panel to hold all other panels
		holderPanel = new JPanel(new GridBagLayout());

		// get a handle to user's mail configuration
		mailConfig = mailConfigController.getMailConfig();

		// display configuration if debugging
		if (DEBUG)
			mailConfigController.displayProperties();

		// determine locale of application, create resource bundle
		currentLocale = mailConfig.getLocale();
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);

		// mail configuration is non-existent
		if (mailConfig == null) {
			// log error and display message to user then exit program
			logger.log(Level.SEVERE, "Error loading the mail properties");
			JOptionPane.showMessageDialog(this,
					messages.getString("propsError"),
					messages.getString("propsErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		// create DBManager to communicate to database
		dbm = new DBManager(mailConfig, messages);

		// if tables do not exist, create and populate with necessary data
		if (!dbm.checkTablesExist()) {
			dbm.createContactsTable();
			dbm.createFoldersTable();
			dbm.createMailTable();
		}

		// create an object to send mail
		mailSender = new MailSender(mailConfig, messages);

		// create object to receive mail
		mailReceiver = new MailReceiver(mailConfig, dbm, messages);

		// create UI classes associated with displaying/creating mail
		messageDisplayModel = new MessageDisplayModel();
		messageDisplayPanel = new MessageDisplayPanel(dbm, messages);
		messageDisplayModel.addObserver(messageDisplayPanel);
		messageCompositionModel = new MessageCompositionModel();
		messageCompositionPanel = new MessageCompositionPanel(
				messageCompositionModel, dbm, mailConfig, messages);
		messageCompositionModel.addObserver(messageCompositionPanel);

		// create card layout for switching between message panels
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		cards.add(messageDisplayPanel, DISPLAY_MAIL);
		cards.add(messageCompositionPanel, COMPOSE_MAIL);
		cards.add(new JPanel(), BLANK_PANEL);
		cardLayout.show(cards, BLANK_PANEL); // display blank panel
		messageDisplayPanel.setCardLayout(cardLayout);
		messageCompositionPanel.setCardLayout(cardLayout);
		holderPanel.add(
				cards,
				getConstraints(1, 2, 1, 1, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, 0.75, 1));

		// create mail table panel with its model
		mailTableModel = new MailTableModel(messages);
		mailTablePanel = new MailTablePanel(mailTableModel,
				messageDisplayModel, messageCompositionModel, mailConfig,
				mailReceiver, mailSender, dbm, messages);
		mailTablePanel.setCardLayout(cardLayout);
		mailTablePanel.setCardPanel(cards);
		holderPanel.add(
				mailTablePanel,
				getConstraints(1, 1, 1, 1, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, 0.75, 1));

		// create instances of classes associated with contacts
		contactModel = new ContactModel();
		contactListPanel = new ContactListPanel(contactModel, dbm, messages);
		contactModel.addObserver(contactListPanel);
		contactModel.addObserver(messageCompositionPanel);
		holderPanel.add(
				contactListPanel,
				getConstraints(0, 2, 1, 1, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, 0.25, 1));

		// create instances of classes associated with folders
		folderModel = new FolderModel();
		folderTreePanel = new FolderTreePanel(folderModel, dbm, mailTablePanel,
				messages);
		folderModel.addObserver(folderTreePanel);
		holderPanel.add(
				folderTreePanel,
				getConstraints(0, 1, 1, 1, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, 0.25, 1));

		// create classes for Help menu
		makeHelp();

		// create tool bar
		holderPanel.add(
				createToolBar(),
				getConstraints(0, 0, 2, 1, GridBagConstraints.WEST,
						GridBagConstraints.NONE, 0, 0));

		// create menu bar
		setJMenuBar(createMenuBar());

		// set title
		setTitle(messages.getString("frameTitle"));

		// set preferred size of holder panel
		holderPanel.setPreferredSize(holderPanel.getPreferredSize());

		this.setContentPane(holderPanel);
		pack();
		setVisible(true);
	}

	/**
	 * Creates HelpSet and HelpBroker objects for the application's help menu
	 */
	private void makeHelp() {
		// find the HelpSet file and create the HelpSet object:
		String helpHS = "hs/main.hs";
		ClassLoader cl = JEEP_GUI.class.getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpHS);
			helpSet = new HelpSet(null, hsURL);
		} catch (Exception ee) {
			// log error and display message to user
			logger.log(Level.WARNING, "Error loading help menu", ee);
			JOptionPane.showMessageDialog(this,
					messages.getString("helpError"),
					messages.getString("helpErrorTitle"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// create a HelpBroker object:
		helpBroker = helpSet.createHelpBroker();

		// enable window level help on RootPane with F1
		JRootPane rootpane = getRootPane();
		helpBroker.enableHelpKey(rootpane, "JEEP", null);

	}

	/**
	 * Creates a menu bar with all of its functionality
	 * 
	 * @return Completed menu bar
	 */
	private JMenuBar createMenuBar() {
		// create menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;

		// File Menu
		menu = new JMenu(messages.getString("file"));
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription(
				messages.getString("file"));
		menu.setToolTipText(messages.getString("fileToolTip"));
		menuBar.add(menu);

		// "Print" menu item
		menuItem = new JMenuItem(messages.getString("print"), KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("printToolTip"));
		menuItem.setToolTipText(messages.getString("printToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// print message
				messageDisplayPanel.printMessage();
			}
		});
		menu.add(menuItem);

		menu.addSeparator();

		// "Exit" menu item
		menuItem = new JMenuItem(messages.getString("exit"), KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("exitToolTip"));
		menuItem.setToolTipText(messages.getString("exitToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// exit application
				System.exit(0);
			}
		});
		menu.add(menuItem);

		// Mail Menu
		menu = new JMenu(messages.getString("mail"));
		menu.setMnemonic(KeyEvent.VK_M);
		menu.getAccessibleContext().setAccessibleDescription(
				messages.getString("mail"));
		menu.setToolTipText(messages.getString("mailMenu"));
		menuBar.add(menu);

		// "Compose Mail" menu item
		menuItem = new JMenuItem(messages.getString("composeMail"),
				KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("composeMail"));
		menuItem.setToolTipText(messages.getString("composeMailToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// compose a new mail message
				mailTablePanel.composeNewMail();
			}

		});
		menu.add(menuItem);

		// "Reply" menu item
		menuItem = new JMenuItem(messages.getString("reply"), KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("reply"));
		menuItem.setToolTipText(messages.getString("replyToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reply to mail
				mailTablePanel.replyToMail();
			}

		});
		menu.add(menuItem);

		// "Reply All" menu item
		menuItem = new JMenuItem(messages.getString("replyAll"), KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("replyAll"));
		menuItem.setToolTipText(messages.getString("replyAllToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reply to all
				mailTablePanel.replyAllMail();
			}

		});
		menu.add(menuItem);

		// "Forward" menu item
		menuItem = new JMenuItem(messages.getString("forward"), KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("forward"));
		menuItem.setToolTipText(messages.getString("forwardToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// forward mail
				mailTablePanel.forwardMail();
			}
		});
		menu.add(menuItem);

		// "Send" menu item
		menuItem = new JMenuItem(messages.getString("send"), KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("send"));
		menuItem.setToolTipText(messages.getString("sendToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// send mail
				sendMail();
			}
		});
		menu.add(menuItem);

		menu.addSeparator();

		// "Delete" menu item
		menuItem = new JMenuItem(messages.getString("delete"), KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("delete"));
		menuItem.setToolTipText(messages.getString("deleteToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// delete mail
				deleteMail();
			}
		});
		menu.add(menuItem);

		// Contact menu
		menu = new JMenu(messages.getString("contact"));
		menu.setMnemonic(KeyEvent.VK_C);
		menu.getAccessibleContext().setAccessibleDescription(
				messages.getString("contact"));
		menu.setToolTipText(messages.getString("contactMenu"));
		menuBar.add(menu);

		// "New contact" menu item
		menuItem = new JMenuItem(messages.getString("new"), KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("new"));
		menuItem.setToolTipText(messages.getString("newContactToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contactListPanel.createContact();
			}
		});
		menu.add(menuItem);

		// "Edit contact" menu item
		menuItem = new JMenuItem(messages.getString("edit"), KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("edit"));
		menuItem.setToolTipText(messages.getString("editContactToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// edit contact
				contactListPanel.editContact();
			}
		});
		menu.add(menuItem);

		// "Delete contact" menu item
		menuItem = new JMenuItem(messages.getString("delete"), KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("delete"));
		menuItem.setToolTipText(messages.getString("deleteContactToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// delete contact
				contactListPanel.deleteContact();
			}
		});
		menu.add(menuItem);

		// Folder menu
		menu = new JMenu(messages.getString("folder"));
		menu.setMnemonic(KeyEvent.VK_D);
		menu.getAccessibleContext().setAccessibleDescription(
				messages.getString("folder"));
		menu.setToolTipText(messages.getString("folderMenu"));
		menuBar.add(menu);

		// "New Folder" menu item
		menuItem = new JMenuItem(messages.getString("new"), KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("new"));
		menuItem.setToolTipText(messages.getString("newFolderToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// create new folder
				folderTreePanel.createFolder();
			}
		});
		menu.add(menuItem);

		// "Edit folder" menu item
		menuItem = new JMenuItem(messages.getString("edit"), KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("edit"));
		menuItem.setToolTipText(messages.getString("editFolderToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// edit folder
				folderTreePanel.editFolder();
			}
		});
		menu.add(menuItem);

		// "Delete folder" menu item
		menuItem = new JMenuItem(messages.getString("delete"), KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("delete"));
		menuItem.setToolTipText(messages.getString("deleteFolderToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// delete folder
				folderTreePanel.deleteFolder();
			}
		});
		menu.add(menuItem);

		// Build the configuration menu
		menu = new JMenu(messages.getString("configuration"));
		menu.setMnemonic(KeyEvent.VK_G);
		menu.getAccessibleContext().setAccessibleDescription(
				messages.getString("configuration"));
		menu.setToolTipText(messages.getString("configurationMenu"));
		menuBar.add(menu);

		// "Edit configuration" menu item
		menuItem = new JMenuItem(messages.getString("edit"), KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("edit"));
		menuItem.setToolTipText(messages.getString("editConfigToolTip"));
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// display window for new composition
				new MailConfigCompositionFrame(mailConfigModel, mailConfig,
						messages);
			}
		});
		menu.add(menuItem);

		// "Help" menu item
		menu = new JMenu(messages.getString("help"));
		menu.setMnemonic(KeyEvent.VK_H);
		menu.getAccessibleContext().setAccessibleDescription(
				messages.getString("helpMenu"));
		menu.setToolTipText(messages.getString("helpMenu"));
		menuBar.add(menu);

		// "Help Contents" menu item
		menuItem = new JMenuItem(messages.getString("helpContents"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				messages.getString("launchHelp"));
		menuItem.setToolTipText(messages.getString("launchHelp"));
		menuItem.addActionListener(new CSH.DisplayHelpFromSource(helpBroker));
		menu.add(menuItem);

		return menuBar;
	}

	/**
	 * Creates the tool bar and adds its functionality
	 * 
	 * @return Tool bar with all of its added buttons and their listeners
	 */
	private JToolBar createToolBar() {

		// create toolbar
		JToolBar toolBar = new JToolBar();
		JButton button = null;

		// compose mail button
		button = makeToolBarButton("compose_mail",
				messages.getString("composeMail"),
				messages.getString("composeMailToolTip"),
				messages.getString("composeMail"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// create a new mail message
				mailTablePanel.composeNewMail();
			}
		});
		toolBar.add(button);

		// send mail button
		button = makeToolBarButton("send_mail", messages.getString("send"),
				messages.getString("sendToolTip"), messages.getString("send"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMail();
			}
		});
		toolBar.add(button);

		// reply mail button
		button = makeToolBarButton("reply_mail", messages.getString("reply"),
				messages.getString("replyToolTip"), messages.getString("reply"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reply to mail message
				mailTablePanel.replyToMail();
			}
		});
		toolBar.add(button);

		// reply all mail button
		button = makeToolBarButton("replyall_mail",
				messages.getString("replyAll"),
				messages.getString("replyAllToolTip"),
				messages.getString("replyAll"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reply to all recipients of mail message
				mailTablePanel.replyAllMail();
			}
		});
		toolBar.add(button);

		// forward mail button
		button = makeToolBarButton("forward_mail",
				messages.getString("forward"),
				messages.getString("forwardToolTip"),
				messages.getString("forward"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// forward mail message
				mailTablePanel.forwardMail();
			}
		});
		toolBar.add(button);

		// delete mail button
		button = makeToolBarButton("delete_mail", messages.getString("delete"),
				messages.getString("deleteToolTip"),
				messages.getString("delete"));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteMail();
			}

		});
		toolBar.add(button);

		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		return toolBar;
	}

	/**
	 * Create the buttons that will be placed in the tool bar
	 * 
	 * @param imageName
	 *            File name of image
	 * @param actionCommand
	 *            Action command of the button
	 * @param toolTipText
	 *            Text that will appear as a tool tip
	 * @param altText
	 *            Alternate text if image of button isn't found
	 * @return Completed tool bar
	 */
	private JButton makeToolBarButton(String imageName, String actionCommand,
			String toolTipText, String altText) {

		// look for the image.
		String imgLocation = "/images/" + imageName + ".png";
		URL imageURL = JEEP_GUI.class.getResource(imgLocation);

		// create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);

		if (imageURL != null) { // image found, set icon to image
			button.setIcon(new ImageIcon(imageURL, altText));
		} else { // no image found, set alternate text
			button.setText(altText);
			logger.log(Level.WARNING, "Resource not found: " + imgLocation);
		}

		return button;
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
	 * @param fill
	 *            How component will be resized
	 * @param weightx
	 *            How much space will be distributed among columns
	 * @param weighty
	 *            How much space will be distributed among rows
	 * @return GridBagConstraints object for component
	 */
	private GridBagConstraints getConstraints(int gridx, int gridy,
			int gridwidth, int gridheight, int anchor, int fill,
			double weightx, double weighty) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.fill = fill;
		c.weightx = weightx;
		c.weighty = weighty;
		c.anchor = anchor;
		return c;
	}

	/**
	 * Sends a mail message
	 */
	private void sendMail() {
		// get selected row in table
		int selectedRow = mailTablePanel.getSelectedRow();

		if (messageCompositionModel.getMail() == null)
			// send newly composed/replied/forwarded email to outbox
			messageCompositionPanel.sendNewEmail();
		else if (selectedRow != -1)
			// send mail message to recipients
			mailTablePanel.sendMail(mailTableModel.getMail(selectedRow));
		else
			// display error if no email was selected
			JOptionPane.showMessageDialog(getParent(),
					messages.getString("sendError"),
					messages.getString("sendErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	private void deleteMail() {
		// get selected row in table
		int selectedRow = mailTablePanel.getSelectedRow();

		// delete selected mail message
		if (selectedRow != -1)
			mailTablePanel.deleteMail(mailTableModel.getMail(selectedRow));
		else
			// display error if no email was selected
			JOptionPane.showMessageDialog(getParent(),
					messages.getString("deleteError"),
					messages.getString("deleteErrorTitle"),
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Schedule a job for the event-dispatching thread by creating and
	 * displaying this application's GUI.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// create and set up the window.
				JEEP_GUI gui = new JEEP_GUI();
				gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}

}
