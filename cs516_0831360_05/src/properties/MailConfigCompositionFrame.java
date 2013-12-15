/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailConfigCompositionFrame.java
 */
package properties;

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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import java_beans.MailConfig;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import app.JEEP_GUI;

/**
 * Frame that allows a user to input their mail configuration for the
 * application
 * 
 * @author Anthony-Virgil Bermejo
 * @version 3.1
 * 
 */
public class MailConfigCompositionFrame extends JFrame {

	// class variables
	private MailConfigModel mailConfigModel = null;
	private MailConfig mailConfig = null;
	private JPanel panel = null;
	private JTextField usernameInput, emailInput, pop3URLInput, pop3PortInput,
			pop3usernameInput, smtpURLInput, smtpPortInput, mySQLurlInput,
			mySQLportInput, mySQLusernameInput = null;
	private JPasswordField pop3PasswordInput, mySQLPasswordInput = null;
	private JRadioButton isGmailRadioButton, notGmailRadioButton = null;
	private JRadioButton isSMTPAuthRadioButton, notSMTPAuthRadioButton = null;
	private JRadioButton englishRadioButton, frenchRadioButton = null;
	private ButtonGroup radioButtonGroup, radioButtonGroup2,
			radioButtonGroup3 = null;
	private JButton saveButton, cancelButton = null;
	private CustomFocusTraversalPolicy newPolicy;
	private ResourceBundle messages;

	/**
	 * MailConfigCompositionFrame constructor
	 * 
	 * @param mailConfigModel
	 *            Model associated with MailConfiguration UIs
	 * @param mailConfig
	 *            Mail configuration of the user
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailConfigCompositionFrame(MailConfigModel mailConfigModel,
			MailConfig mailConfig, ResourceBundle messages) {
		this.mailConfigModel = mailConfigModel;
		this.mailConfig = mailConfig;
		this.messages = messages;

		// initialize the view
		initialize();

		// set title of frame
		setTitle(messages.getString("mailConfigTitle"));
		add(panel, BorderLayout.CENTER);
		pack();
		centerScreen();
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Initializes the view with necessary components and functionality
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		MyActionListener buttonListener = new MyActionListener();
		panel = new JPanel();
		panel.setLayout(gridBagLayout);

		// regular expressions for ports and email
		String portNumRegEx = "^0*(?:6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{1,3}|[0-9])$";
		String emailRegEx = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

		// add label for username
		JLabel jLabel = new JLabel();
		jLabel.setText(messages.getString("name"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 0, 1, 1, GridBagConstraints.EAST));

		// add text field for username
		usernameInput = new JTextField();
		usernameInput.setColumns(30);
		usernameInput.setText(mailConfig.getUsername());
		panel.add(usernameInput,
				getConstraints(1, 0, 3, 1, GridBagConstraints.CENTER));

		// add label for email address
		jLabel = new JLabel();
		jLabel.setText(messages.getString("emailAddress"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 1, 1, 1, GridBagConstraints.EAST));

		// add text field for email
		emailInput = new JFormattedTextField(new regex.RegexFormatter(
				emailRegEx));
		emailInput.setColumns(30);
		emailInput.setText(mailConfig.getEmail());
		panel.add(emailInput,
				getConstraints(1, 1, 3, 1, GridBagConstraints.CENTER));

		// add label for pop3 server URL
		jLabel = new JLabel();
		jLabel.setText(messages.getString("pop3ServerURL"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 2, 1, 1, GridBagConstraints.EAST));

		// add text field for pop3 server url
		pop3URLInput = new JTextField();
		pop3URLInput.setColumns(30);
		pop3URLInput.setText(mailConfig.getPop3_URL());
		panel.add(pop3URLInput,
				getConstraints(1, 2, 3, 1, GridBagConstraints.CENTER));

		// add label for pop3 port number
		jLabel = new JLabel();
		jLabel.setText(messages.getString("pop3PortNumber"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 3, 1, 1, GridBagConstraints.EAST));

		// add text field for pop3 port number
		pop3PortInput = new JFormattedTextField(new regex.RegexFormatter(
				portNumRegEx));
		pop3PortInput.setColumns(30);
		pop3PortInput.setText(String.valueOf(mailConfig.getPop3_portnum()));
		panel.add(pop3PortInput,
				getConstraints(1, 3, 3, 1, GridBagConstraints.CENTER));

		// add label for pop3 username
		jLabel = new JLabel();
		jLabel.setText(messages.getString("pop3username"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 4, 1, 1, GridBagConstraints.EAST));

		// add text field for pop3 username
		pop3usernameInput = new JTextField();
		pop3usernameInput.setColumns(30);
		pop3usernameInput.setText(mailConfig.getPop3_username());
		panel.add(pop3usernameInput,
				getConstraints(1, 4, 3, 1, GridBagConstraints.CENTER));

		// add label for pop3 password
		jLabel = new JLabel();
		jLabel.setText(messages.getString("pop3password"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 5, 1, 1, GridBagConstraints.EAST));

		// add text field for pop3 password
		pop3PasswordInput = new JPasswordField();
		pop3PasswordInput.setColumns(30);
		pop3PasswordInput.setText(mailConfig.getPop3_password());
		panel.add(pop3PasswordInput,
				getConstraints(1, 5, 3, 1, GridBagConstraints.CENTER));

		// add label for SMTP server url
		jLabel = new JLabel();
		jLabel.setText(messages.getString("SMTPserverURL"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 6, 1, 1, GridBagConstraints.EAST));

		// add text field for smtp server URL
		smtpURLInput = new JTextField();
		smtpURLInput.setColumns(30);
		smtpURLInput.setText(mailConfig.getSmtp_URL());
		panel.add(smtpURLInput,
				getConstraints(1, 6, 3, 1, GridBagConstraints.CENTER));

		// add label for SMTP port number
		jLabel = new JLabel();
		jLabel.setText(messages.getString("SMTPportNumber"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 7, 1, 1, GridBagConstraints.EAST));

		// add text field for smtp port number
		smtpPortInput = new JFormattedTextField(new regex.RegexFormatter(
				portNumRegEx));
		smtpPortInput.setColumns(30);
		smtpPortInput.setText(String.valueOf(mailConfig.getSmtp_portnum()));
		panel.add(smtpPortInput,
				getConstraints(1, 7, 3, 1, GridBagConstraints.CENTER));

		// add label for SMTP authentication
		jLabel = new JLabel();
		jLabel.setText(messages.getString("SMTPauth"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 8, 1, 1, GridBagConstraints.EAST));

		// add radiobutton for yes
		String yesSMTPRadioText = messages.getString("yes");
		isSMTPAuthRadioButton = new JRadioButton(yesSMTPRadioText);
		isSMTPAuthRadioButton.setActionCommand(yesSMTPRadioText);
		panel.add(isSMTPAuthRadioButton,
				getConstraints(1, 8, 1, 1, GridBagConstraints.CENTER));

		// add radiobutton for no
		String noSMTPRadioText = messages.getString("no");
		notSMTPAuthRadioButton = new JRadioButton(noSMTPRadioText);
		notSMTPAuthRadioButton.setActionCommand(noSMTPRadioText);
		panel.add(notSMTPAuthRadioButton,
				getConstraints(2, 8, 1, 1, GridBagConstraints.CENTER));

		// add radio buttons to a button group
		radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(isSMTPAuthRadioButton);
		radioButtonGroup.add(notSMTPAuthRadioButton);

		// set selected radio button to that of mailConfig
		if (mailConfig.isSMTPAuth())
			isSMTPAuthRadioButton.setSelected(true);
		else
			notSMTPAuthRadioButton.setSelected(true);

		// add label for gmail account
		jLabel = new JLabel();
		jLabel.setText(messages.getString("gmailAccount"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 9, 1, 1, GridBagConstraints.EAST));

		// add radiobutton for yes
		String yesGmailRadioText = messages.getString("yes");
		isGmailRadioButton = new JRadioButton(yesGmailRadioText);
		isGmailRadioButton.setActionCommand(yesGmailRadioText);
		panel.add(isGmailRadioButton,
				getConstraints(1, 9, 1, 1, GridBagConstraints.CENTER));

		// add radiobutton for no
		String noGmailRadioText = messages.getString("no");
		notGmailRadioButton = new JRadioButton(noGmailRadioText);
		notGmailRadioButton.setActionCommand(noGmailRadioText);
		panel.add(notGmailRadioButton,
				getConstraints(2, 9, 1, 1, GridBagConstraints.CENTER));

		// add radio buttons to a button group
		radioButtonGroup2 = new ButtonGroup();
		radioButtonGroup2.add(isGmailRadioButton);
		radioButtonGroup2.add(notGmailRadioButton);

		// set selected radio button to that of mailConfig
		if (mailConfig.isGmail())
			isGmailRadioButton.setSelected(true);
		else
			notGmailRadioButton.setSelected(true);

		// add label for mySQL url
		jLabel = new JLabel();
		jLabel.setText(messages.getString("mySQLurl"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 10, 1, 1, GridBagConstraints.EAST));

		// add text field for MySQL url
		mySQLurlInput = new JTextField();
		mySQLurlInput.setColumns(30);
		mySQLurlInput.setText(mailConfig.getMySQL_URL());
		panel.add(mySQLurlInput,
				getConstraints(1, 10, 3, 1, GridBagConstraints.CENTER));

		// add label for mySQL port number
		jLabel = new JLabel();
		jLabel.setText(messages.getString("mySQLportNumber"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 11, 1, 1, GridBagConstraints.EAST));

		// add text field for mySQL port
		mySQLportInput = new JFormattedTextField(new regex.RegexFormatter(
				portNumRegEx));
		mySQLportInput.setColumns(30);
		mySQLportInput.setText(String.valueOf(mailConfig.getMySQL_portnum()));
		panel.add(mySQLportInput,
				getConstraints(1, 11, 3, 1, GridBagConstraints.CENTER));

		// add label for mySQL usernameS
		jLabel = new JLabel();
		jLabel.setText(messages.getString("mySQLusername"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 12, 1, 1, GridBagConstraints.EAST));

		// add text field for mySQL username
		mySQLusernameInput = new JTextField();
		mySQLusernameInput.setColumns(30);
		mySQLusernameInput.setText(mailConfig.getMySQL_username());
		panel.add(mySQLusernameInput,
				getConstraints(1, 12, 3, 1, GridBagConstraints.CENTER));

		// add label for mySQL password
		jLabel = new JLabel();
		jLabel.setText(messages.getString("mySQLpassword"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 13, 1, 1, GridBagConstraints.EAST));

		// add text field for mySQL password
		mySQLPasswordInput = new JPasswordField();
		mySQLPasswordInput.setColumns(30);
		mySQLPasswordInput.setText(mailConfig.getMySQL_password());
		panel.add(mySQLPasswordInput,
				getConstraints(1, 13, 3, 1, GridBagConstraints.CENTER));

		// add label for language
		jLabel = new JLabel();
		jLabel.setText(messages.getString("language"));
		jLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(jLabel, getConstraints(0, 14, 1, 1, GridBagConstraints.EAST));

		// add radiobutton for english language
		String englishText = messages.getString("english");
		englishRadioButton = new JRadioButton(englishText);
		englishRadioButton.setActionCommand(englishText);
		panel.add(englishRadioButton,
				getConstraints(1, 14, 1, 1, GridBagConstraints.CENTER));

		// add radiobutton for french language
		String frenchText = messages.getString("french");
		frenchRadioButton = new JRadioButton(frenchText);
		frenchRadioButton.setActionCommand(frenchText);
		panel.add(frenchRadioButton,
				getConstraints(2, 14, 1, 1, GridBagConstraints.CENTER));

		// add radio buttons to a button group
		radioButtonGroup3 = new ButtonGroup();
		radioButtonGroup3.add(englishRadioButton);
		radioButtonGroup3.add(frenchRadioButton);

		// set selected radio button to that of mailConfig
		if (mailConfig.getLocale().getLanguage().equals("en"))
			englishRadioButton.setSelected(true);
		else if (mailConfig.getLocale().getLanguage().equals("fr"))
			frenchRadioButton.setSelected(true);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridBagLayout());

		// create button for saving configuration
		saveButton = new JButton();
		saveButton.setText(messages.getString("save"));
		saveButton.addActionListener(buttonListener);
		panel2.add(saveButton,
				getConstraints(0, 0, 1, 1, GridBagConstraints.EAST));

		// create button for cancelling configuration
		cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancel"));
		cancelButton.addActionListener(buttonListener);
		panel2.add(cancelButton,
				getConstraints(1, 0, 1, 1, GridBagConstraints.WEST));

		panel.add(panel2,
				getConstraints(0, 15, 4, 1, GridBagConstraints.CENTER));

		// add components for focus traversal policy
		Vector<Component> order = new Vector<Component>(20);
		order.add(usernameInput);
		order.add(emailInput);
		order.add(pop3URLInput);
		order.add(pop3PortInput);
		order.add(pop3usernameInput);
		order.add(pop3PasswordInput);
		order.add(smtpURLInput);
		order.add(smtpPortInput);
		order.add(isSMTPAuthRadioButton);
		order.add(notSMTPAuthRadioButton);
		order.add(isGmailRadioButton);
		order.add(notGmailRadioButton);
		order.add(mySQLurlInput);
		order.add(mySQLportInput);
		order.add(mySQLusernameInput);
		order.add(mySQLPasswordInput);
		order.add(englishRadioButton);
		order.add(frenchRadioButton);
		order.add(saveButton);
		order.add(cancelButton);

		// set focus traversal policy with custom created policy
		newPolicy = new CustomFocusTraversalPolicy(order);
		this.setFocusTraversalPolicy(newPolicy);

	}

	/**
	 * Inner action listener class for buttons
	 * 
	 * @author Anthony-Virgil Bermejo
	 */
	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// get handle to all values in text boxes
			String username = usernameInput.getText();
			String email = emailInput.getText();
			String pop3URL = pop3URLInput.getText();
			String pop3port = pop3PortInput.getText();
			String pop3username = pop3usernameInput.getText();
			String pop3password = new String(pop3PasswordInput.getPassword());
			String smtpURL = smtpURLInput.getText();
			String smtpPort = smtpPortInput.getText();
			String mySQLurl = mySQLurlInput.getText();
			String mySQLport = mySQLportInput.getText();
			String mySQLpassword = new String(mySQLPasswordInput.getPassword());
			String mySQLusername = mySQLusernameInput.getText();

			// if save button was clicked
			if (saveButton == e.getSource()) {
				// verify that all fields were entered in
				if (!username.equals("")
						&& !email.equals("")
						&& !pop3URL.equals("")
						&& !pop3port.equals("")
						&& !pop3username.equals("")
						&& !pop3password.equals("")
						&& !smtpURL.equals("")
						&& !smtpPort.equals("")
						&& !mySQLurl.equals("")
						&& !mySQLport.equals("")
						&& !mySQLpassword.equals("")
						&& !mySQLusername.equals("")
						&& (isGmailRadioButton.isSelected() || notGmailRadioButton
								.isSelected())
						&& (isSMTPAuthRadioButton.isSelected() || notSMTPAuthRadioButton
								.isSelected())
						&& (englishRadioButton.isSelected() || frenchRadioButton
								.isSelected())) {

					// confirm with user their decision to change configuration
					int result = JOptionPane.showConfirmDialog(null,
							messages.getString("areyousure"),
							messages.getString("confirm"),
							JOptionPane.YES_NO_OPTION);

					// user chooses yes, set all values to mailConfig object
					if (result == JOptionPane.YES_OPTION) {
						mailConfig.setUsername(username);
						mailConfig.setEmail(email);
						mailConfig.setPop3_URL(pop3URL);
						mailConfig.setPop3_portnum(Integer.parseInt(pop3port));
						mailConfig.setPop3_username(pop3username);
						mailConfig.setPop3_password(pop3password);
						mailConfig.setSmtp_URL(smtpURL);
						mailConfig.setSmtp_portnum(Integer.parseInt(smtpPort));
						mailConfig.setMySQL_URL(mySQLurl);
						mailConfig
								.setMySQL_portnum(Integer.parseInt(mySQLport));
						mailConfig.setMySQL_password(mySQLpassword);
						mailConfig.setMySQL_username(mySQLusername);

						if (isGmailRadioButton.isSelected())
							mailConfig.setIsGmail(true);
						else
							mailConfig.setIsGmail(false);

						if (isSMTPAuthRadioButton.isSelected())
							mailConfig.setSMTPAuth(true);
						else
							mailConfig.setSMTPAuth(false);

						if (englishRadioButton.isSelected())
							mailConfig.setLocale(new Locale("en", "CA"));
						else
							mailConfig.setLocale(new Locale("fr", "CA"));

						// send mailConfig to model
						mailConfigModel.retrieveMailConfig(mailConfig);
					}
					// dispose frame
					dispose();
				} else
					// not all necessary fields were entered in, display message
					JOptionPane.showMessageDialog(null,
							messages.getString("mailConfigError"),
							messages.getString("mailConfigErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
			} else if (cancelButton == e.getSource()) {
				// display frame
				dispose();
			}
		}
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
	 * Standard method for centering a frame
	 */
	private void centerScreen() {
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
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
