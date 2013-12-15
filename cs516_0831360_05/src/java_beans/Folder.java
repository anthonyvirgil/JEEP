/**
 * Anthony-Virgil Bermejo
 * 0831360	
 * Folder.java
 */
package java_beans;

import java.io.Serializable;

/**
 * Java Bean for a folder in the J.E.E.P application
 * 
 * @author Anthony-Virgil Bermejo
 * @version 1.0
 */
public class Folder implements Serializable {

	private static final long serialVersionUID = 6550165490725187003L;
	private int id;
	private String name;

	/**
	 * Default constructor setting instance variables to their defaults.
	 */
	public Folder() {
		this.id = 0;
		this.name = "";
	}

	/**
	 * One parameter Constructor
	 * 
	 * @param name
	 *            Name of the folder
	 */
	public Folder(String name) {
		super();
		this.name = name;
	}

	/**
	 * Constructor setting all instance variables to what the user defines.
	 * 
	 * @param name
	 *            Name of the folder
	 */
	public Folder(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return ID of the folder
	 */
	public int getID() {
		return id;
	}

	/**
	 * @param id
	 *            the ID to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * @return the serialversionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Overridden toString() method that returns the name of the folder
	 */
	@Override
	public String toString() {
		return name;
	}
}
