package com.jnetsend.util;

import java.awt.Point;
import java.util.SortedMap;
import java.util.TreeMap;


public class ApplicationConfig implements java.io.Serializable {

	private static final long serialVersionUID = 1802192489360535178L;

	private String userName;
	private String lookAndFeel;
	private SortedMap contacts;
	private Point locationOnScreeen;

	public ApplicationConfig() {
		// init with default values
		//userName = AppConstants.DEFAULT_USER_NAME;
		lookAndFeel = Constants.DEFAULT_LOOKANDFELL;

		contacts = new TreeMap();
		
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setContacts(SortedMap contacts) {
		this.contacts = contacts;
	}

	public SortedMap getContacts() {
		return contacts;
	}

	public Object[] getContactNamesArray() {
		return contacts.keySet().toArray();
	}

	public void addContact(String name, String login) {
		contacts.put(name, login);
	}

	public void removeContact(String name) {
		contacts.remove(name);
	}

	public void setLocationOnScreeen(Point locationOnScreeen) {
		this.locationOnScreeen = locationOnScreeen;
	}

	public Point getLocationOnScreeen() {
		return locationOnScreeen;
	}
}