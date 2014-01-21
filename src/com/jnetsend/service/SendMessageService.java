package com.jnetsend.service;

import javax.swing.JOptionPane;


public class SendMessageService implements Runnable {
	private String fromName;
	private String toContact;
	private String toName;
	private String msg;
	private String[] toContacts;

	public SendMessageService(String fromName, String toContact, String toName, String msg,
			Object[] toContacts) {
		this.fromName = fromName;
		this.toContact = toContact;
		this.toName = toName;
		this.msg = msg;
		this.toContacts = new String[toContacts.length];
		for (int i = 0; i < toContacts.length; i++) {
			this.toContacts[i] = (String) toContacts[i];
		}
	}

	public void run() {
		try {
			String contacts = "";
			if (toContacts.length == 1) {
				contacts = toContacts[0];
			} else {
				for (int i = 0; i < toContacts.length; i++) {
					if (i != toContacts.length - 1)
						contacts += toContacts[i] + ", ";
					else
						contacts = contacts.substring(0, contacts.length() - 2)
								+ " and " + toContacts[i];
				}
			}
			String header = "\n-------------------------------------------------------------------- JNetSend ------------------------------------------------------------------------\n";
			String footer = "\n---------------------------------------------------------------------------------------------------------------------------------------------------------\n";
			String[] cmd = { "net", "send", toContact, header, msg, footer,
					("\nFrom: " + fromName + "\n\nTo: " + contacts) };
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			// System.out.println(process.exitValue());
			if (process.exitValue() == 2)
				throw new SendMessageException();
			// tfMsg.selectAll();
		} catch (SendMessageException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to send message to " + this.toName
							+ " (" + this.toContact + ").",
					"Message send error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to execute 'NET SEND' command.", "net send Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Error sending message.", "Process exception",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}