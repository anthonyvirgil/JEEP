/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * MailTable.java
 */
package mail;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * Represents the table object that will be created to hold mail data
 * 
 * @author Anthony-Virgil Bermejo
 * @version 2.0
 * 
 */
public class MailTable extends JTable {
	private String[] columnToolTips;
	private ResourceBundle messages;

	/**
	 * MailTable constructor
	 * 
	 * @param tableModel
	 *            Model of the mail table
	 * @param messages
	 *            ResourceBundle used for internationalization
	 */
	public MailTable(MailTableModel tableModel, ResourceBundle messages) {
		super(tableModel);
		this.messages = messages;

		// create array of tool tips that will be associated with each column
		columnToolTips = new String[] { messages.getString("idToolTip"),
				messages.getString("senderToolTip"),
				messages.getString("recipientToolTip"),
				messages.getString("BCCrecipientsToolTip"),
				messages.getString("CCrecipientsToolTip"),
				messages.getString("subjectToolTip"),
				messages.getString("messageToolTip"),
				messages.getString("dateToolTip"),
				messages.getString("folderToolTip") };
	}

	/**
	 * Create custom table header that displays a tool tip for columns
	 * 
	 * @return Header for the table
	 */
	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new JTableHeader(columnModel) {
			/**
			 * Retrieves the tool tip associated at the column where the mouse
			 * is hovering over
			 */
			@Override
			public String getToolTipText(MouseEvent e) {
				// retrieve point where mouse is located
				java.awt.Point p = e.getPoint();
				int index = columnModel.getColumnIndexAtX(p.x);

				// return the tool tip that is associated with the column being
				// pointed at
				int realIndex = columnModel.getColumn(index).getModelIndex();
				return columnToolTips[realIndex];
			}
		};
	}
}
