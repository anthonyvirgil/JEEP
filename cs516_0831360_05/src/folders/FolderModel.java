/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * FolderModel.java
 */
package folders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import java_beans.Folder;

/**
 * Represents the model in the MVC pattern for FolderTree UIs
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.4
 * 
 */
public class FolderModel extends Observable {
	private ArrayList<Folder> folderList;

	/**
	 * FolderModel constructor
	 */
	public FolderModel() {
		super();
		folderList = new ArrayList<Folder>();
	}

	/**
	 * Receives an array list of folders
	 * 
	 * @param folderList
	 *            List of folders
	 */
	public void receiveFolders(ArrayList<Folder> folderList) {
		this.folderList = folderList;

		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the list of folders contained in this model
	 * 
	 * @return List of folders
	 */
	public ArrayList<Folder> getFolderList() {
		return folderList;
	}
}
