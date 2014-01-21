package com.jnetsend.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.jnetsend.util.Constants;

public class ContactsFileFilter extends FileFilter {

	public boolean accept(File f) {

		boolean accepted = false;
		if (f != null) {
			String ext = this.getExtension(f);
			if ((ext != null && ext.equals(Constants.DEFAULT_CONTACTS_FILE_EXTENSION))
					|| f.isDirectory()) {
				accepted = true;
			}
		}
		return accepted;
	}

	public String getDescription() {
		return "Contacts list file (*.txt)";
	}

	private String getExtension(File f) {
		if (f != null) {
			String fileName = f.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) {
				return fileName.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}
}
