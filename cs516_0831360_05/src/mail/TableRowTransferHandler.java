/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * TableRowTransferHandler.java
 */
package mail;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java_beans.Mail;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.mysql.jdbc.Messages;

/**
 * TransferHandler for dragging the select row of mail from table
 */
public class TableRowTransferHandler extends TransferHandler {

	// the DataFlavor or type of object we will drag
	private final DataFlavor localObjectFlavor = new ActivationDataFlavor(
			Mail.class, DataFlavor.javaJVMLocalObjectMimeType, "Selected mail");
	private JTable table = null;
	private MailTableModel mailTableModel = null;

	/**
	 * TableRowTransferHandler constructor
	 * 
	 * @param table
	 *            Table that holds the mail data
	 * @param mailTableModel
	 *            Model associated with the table of mail data
	 */
	public TableRowTransferHandler(JTable table, MailTableModel mailTableModel) {
		this.table = table;
		this.mailTableModel = mailTableModel;
	}

	/**
	 * Define the DataHandler for the drag operation
	 */
	@Override
	protected Transferable createTransferable(JComponent c) {
		assert (c == table);
		return new DataHandler(mailTableModel.getMail(table.getSelectedRow()),
				localObjectFlavor.getMimeType());
	}

	/**
	 * Determine what kind of action can be supported
	 */
	@Override
	public int getSourceActions(JComponent c) {
		// only Move is supported
		return TransferHandler.MOVE;
	}

}
