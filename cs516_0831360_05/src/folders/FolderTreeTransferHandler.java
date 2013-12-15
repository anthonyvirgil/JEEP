/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * FolderTreeTransferHandler.java
 */
package folders;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java_beans.Mail;

import javax.activation.ActivationDataFlavor;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import mail.MailTablePanel;

import databases.DBManager;

public class FolderTreeTransferHandler extends TransferHandler {

	protected DefaultTreeModel tree;
	private MailTablePanel mailTablePanel;
	private DBManager dbm;
	private final DataFlavor localObjectFlavor = new ActivationDataFlavor(
			Mail.class, DataFlavor.javaJVMLocalObjectMimeType, "Selected Mail");
	// create object for logging errors
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * FolderTreeTransferHandler constructor
	 * 
	 * @param tree
	 *            JTree that contains all the folder nodes
	 * @param mailTablePanel
	 *            Panel that holds all mail data in a JTable
	 * @param dbm
	 *            Database manager that connects with the database with SQL
	 *            statements
	 */
	public FolderTreeTransferHandler(JTree tree, MailTablePanel mailTablePanel,
			DBManager dbm) {
		super();
		this.tree = (DefaultTreeModel) tree.getModel();
		this.mailTablePanel = mailTablePanel;
		this.dbm = dbm;
	}

	/**
	 * Determine what kind of action can be supported
	 */
	@Override
	public int getSourceActions(JComponent component) {
		// no type of action is supported on folders
		return TransferHandler.NONE;
	}

	/**
	 * Verify that the data dropped is the DataFlavor (Type) expected
	 * 
	 * @param supp
	 * @return True, if type is expected. False, if not.
	 */
	@Override
	public boolean canImport(TransferSupport supp) {
		// setup so we can always see what it is we are dropping onto
		supp.setShowDropLocation(true);
		if (supp.isDataFlavorSupported(localObjectFlavor)) {
			return true;
		}
		// something prevented this import from going forward
		return false;
	}

	/**
	 * Extract the data from table when dropping and update folder according to
	 * where drop occurred
	 * 
	 * @param supp
	 * @return
	 */
	@Override
	public boolean importData(TransferSupport supp) {
		if (this.canImport(supp)) {
			try {
				// fetch the data to transfer
				Transferable t = supp.getTransferable();
				Mail selectedMail = (Mail) t.getTransferData(localObjectFlavor);

				// fetch the drop location
				TreePath loc = ((javax.swing.JTree.DropLocation) supp
						.getDropLocation()).getPath();

				// set folder of mail to the folder it will be dragged in
				selectedMail.setFolder(loc.getLastPathComponent().toString()
						.trim());

				// remove selected row from table
				mailTablePanel.removeRow();

				// update mail data in database
				dbm.updateMail(selectedMail);

				// change display panel to a blank panel
				mailTablePanel.showBlankDisplayPanel();

				return true;
			} catch (UnsupportedFlavorException e) {
				// log error
				logger.log(Level.WARNING, "Error loading data flavor.");
			} catch (IOException e) {
				// log error
				logger.log(Level.WARNING, "Error importing data.");
			}
		}
		return false;
	}
}
