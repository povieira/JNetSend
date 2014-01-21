package com.jnetsend.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.jnetsend.service.NetSendService;
import com.jnetsend.service.SendMessageService;
import com.jnetsend.util.ApplicationConfig;
import com.jnetsend.util.Constants;
import com.jnetsend.util.Utils;

public class JNetSendFrame extends JFrame {

	private ApplicationConfig config;

	private Point frameLocation;
	private HashMap lastMessages;

	private JList contactsList;
	private JLabel lContact, lMsg;
	private JTextArea taMsg;

	private JScrollPane scrollPaneMsg, scrollPaneList;
	private JButton sendButton, showLastMsgButton, serviceButton,
			addContactButton, editContactButton, removeContactButton;
	private JPanel mainPanel, buttonsPanel, contactsPanel, messagePanel;
	private JSplitPane splitPane;

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuFileSetName, menuFileImport, menuFileExport,
			menuFileExit;
	private JMenu menuLAF;
	private JMenuItem menuLAFMetal, menuLAFMotif, menuLAFWindows,
			menuLAFDefaults;
	private JMenu menuHelp;
	private JMenuItem menuHelpAbout;

	private static String serviceStatus = "(Inactive)";

	// constructor
	public JNetSendFrame() {

		config = Utils.loadConfiguration();

		if (config.getUserName() == null) {
			updateUserName();
		}

		lastMessages = new HashMap();

		// Menu actions
		MenusListener menuListener = new MenusListener();

		// Buttons actions
		ButtonsListener buttonListener = new ButtonsListener();

		// Contacts list actions
		ManageContactsListener contactListener = new ManageContactsListener();

		// Menus configuration
		menuFileSetName = new JMenuItem();
		menuFileSetName.setText("Change nickname");
		menuFileSetName.setMnemonic('C');
		menuFileSetName.addActionListener(menuListener);
		menuFileImport = new JMenuItem();
		menuFileImport.setText("Import contacts");
		menuFileImport.setMnemonic('I');
		menuFileImport.addActionListener(menuListener);
		menuFileImport.setToolTipText("Imports contact list from file.");
		menuFileExport = new JMenuItem();
		menuFileExport.setText("Export contacts");
		menuFileExport.setMnemonic('E');
		menuFileExport.addActionListener(menuListener);
		menuFileExport.setToolTipText("Exports contacts to file.");
		menuFileExit = new JMenuItem();
		menuFileExit.setText("Exit");
		menuFileExit.setMnemonic('x');
		menuFileExit.addActionListener(new ExitHandler());
		menuFile = new JMenu();
		menuFile.setText("File");
		menuFile.setMnemonic('F');
		menuFile.add(menuFileSetName);
		menuFile.add(menuFileImport);
		menuFile.add(menuFileExport);
		menuFile.addSeparator();
		menuFile.add(menuFileExit);

		menuLAFMetal = new JMenuItem();
		menuLAFMetal.setText("Metal");
		menuLAFMetal.setMnemonic('M');
		menuLAFMetal.addActionListener(menuListener);
		menuLAFMotif = new JMenuItem();
		menuLAFMotif.setText("Motif");
		menuLAFMotif.setMnemonic('o');
		menuLAFMotif.addActionListener(menuListener);
		menuLAFWindows = new JMenuItem();
		menuLAFWindows.setText("Windows");
		menuLAFWindows.setMnemonic('W');
		menuLAFWindows.addActionListener(menuListener);
		menuLAFDefaults = new JMenuItem();
		menuLAFDefaults.setText("Restore defaults");
		menuLAFDefaults.setMnemonic('R');
		menuLAFDefaults.setToolTipText("Restore default JNetSend look");
		menuLAFDefaults.addActionListener(menuListener);
		menuLAF = new JMenu();
		menuLAF.setText("Look & Feel");
		menuLAF.add(menuLAFMetal);
		menuLAF.add(menuLAFMotif);
		menuLAF.add(menuLAFWindows);
		menuLAF.addSeparator();
		menuLAF.add(menuLAFDefaults);
		menuLAF.setMnemonic('L');

		menuHelpAbout = new JMenuItem();
		menuHelpAbout.setText("About");
		menuHelpAbout.setMnemonic('a');
		menuHelpAbout.addActionListener(menuListener);
		menuHelp = new JMenu();
		menuHelp.setText("Help");
		menuHelp.setMnemonic('h');
		// menuAbout.addMenuListener(menuListener);
		menuHelp.add(menuHelpAbout);

		menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuLAF);
		menuBar.add(menuHelp);

		// Contacts list initialization (JLabel, JList and JScrollPane)
		contactsList = new JList();
		contactsList.setListData(config.getContactNamesArray());
		contactsList.setFont(Constants.TEXT_SMALL_FONT);
		contactsList.setFixedCellWidth(100);
		contactsList
				.setToolTipText("Double click to edit the selected contact.");
		contactsList.addMouseListener(contactListener);
		contactsList.addKeyListener(contactListener);

		lContact = new JLabel("Contacts: ");
		lContact.setLabelFor(contactsList);
		lContact.setHorizontalAlignment(JLabel.CENTER);
		lContact.setFont(Constants.TEXT_FONT);

		scrollPaneList = new JScrollPane(contactsList);

		// Buttons configuration
		sendButton = new JButton("Send");
		sendButton.setMnemonic('S');
		sendButton.addActionListener(buttonListener);
		sendButton.setFont(Constants.TEXT_SMALL_FONT);
		sendButton.setToolTipText("Send message to selected contacts.");

		showLastMsgButton = new JButton("Last message");
		showLastMsgButton.setMnemonic('L');
		showLastMsgButton.addActionListener(buttonListener);
		showLastMsgButton.setFont(Constants.TEXT_SMALL_FONT);
		showLastMsgButton
				.setToolTipText("Shows the last massage sent to selected contact.");

		serviceButton = new JButton("Activate");
		serviceButton.setMnemonic('A');
		serviceButton.setForeground(Color.BLUE);
		serviceButton.setToolTipText("Starts NET SEND service.");
		serviceButton.setFont(Constants.TEXT_SMALL_FONT);
		serviceButton.addActionListener(buttonListener);

		addContactButton = new JButton("Add");
		addContactButton.setMnemonic('A');
		addContactButton.addActionListener(buttonListener);
		addContactButton.setFont(Constants.TEXT_SMALL_FONT);
		addContactButton.setToolTipText("Add contact.");

		editContactButton = new JButton("Edit");
		editContactButton.setMnemonic('E');
		editContactButton.addActionListener(buttonListener);
		editContactButton.setFont(Constants.TEXT_SMALL_FONT);
		editContactButton.setToolTipText("Edit contact.");

		removeContactButton = new JButton("Delete");
		removeContactButton.setMnemonic('D');
		removeContactButton.addActionListener(buttonListener);
		removeContactButton.setFont(Constants.TEXT_SMALL_FONT);
		removeContactButton.setToolTipText("Delete contact.");

		// Message input initialization (JTextArea, JLabel and JScrollPane)
		// taMsg = new JTextArea(4,30);
		taMsg = new JTextArea();
		taMsg.setEditable(true);
		taMsg.setLineWrap(true);
		taMsg.setWrapStyleWord(true);
		taMsg.setFont(Constants.TEXT_FONT);
		taMsg.setFocusable(true);

		lMsg = new JLabel("Message: ");
		lMsg.setLabelFor(taMsg);
		lMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lMsg.setFont(Constants.TEXT_FONT);

		scrollPaneMsg = new JScrollPane(taMsg);
		scrollPaneMsg.setFocusable(true);

		// panels

		contactsPanel = new JPanel();
		contactsPanel.setLayout(new BorderLayout());
		contactsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		contactsPanel.add(lContact, BorderLayout.NORTH);
		contactsPanel.add(scrollPaneList, BorderLayout.CENTER);

		messagePanel = new JPanel();
		messagePanel
				.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 60, 10));
		messagePanel.add(lMsg);
		messagePanel.add(scrollPaneMsg);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new FlowLayout());
		rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
		rightPanel.add(addContactButton);
		rightPanel.add(removeContactButton);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout());
		leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 5, 0));
		leftPanel.add(sendButton);
		leftPanel.add(showLastMsgButton);
		leftPanel.add(serviceButton);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(Constants.DEFAULT_DIVIDER_LOCATION);
		splitPane.setDividerSize(25);
		splitPane.setLeftComponent(messagePanel);
		splitPane.setRightComponent(contactsPanel);

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.add(rightPanel, BorderLayout.EAST);
		buttonsPanel.add(leftPanel, BorderLayout.CENTER);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		// frame configuration
		updateTitle();

		this.setJMenuBar(menuBar);
		this.setFont(Constants.TEXT_FONT);
		this.getContentPane().add(mainPanel, "Center");
		this.setResizable(false);
		this.addWindowListener(new ExitHandler());
		this.setSize(525, 270);

		// check OS
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("win") < 0) {
			JOptionPane.showMessageDialog(JNetSendFrame.this,
					"JNetSend is not compatible with " + osName + ".",
					"O.S. issue", JOptionPane.ERROR_MESSAGE);
			serviceStatus = "(Inactive)";
			sendButton.setEnabled(false);
			serviceButton.setEnabled(false);
		} else {
			startNetService();
		}
	}

	// Inner classes - Listeners

	class ExitHandler extends WindowAdapter implements ActionListener {

		private void exit() {

			try {
				Utils.saveConfiguration(config);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(JNetSendFrame.this,
						"Error saving config file.", "File error",
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		}

		public void windowClosing(WindowEvent we) {
			exit();
		}

		public void actionPerformed(ActionEvent ae) {
			exit();
		}
	}

	class ButtonsListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JComponent jc = (JComponent) ae.getSource();
			if (jc == sendButton) {
				if (taMsg.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Type a message.",
							"Warning", JOptionPane.WARNING_MESSAGE);
					taMsg.requestFocus();
				} else if (contactsList.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Select at least one contact.", "Warning",
							JOptionPane.WARNING_MESSAGE);
					taMsg.requestFocus();
				} else {
					sendMessage(contactsList.getSelectedValues(),
							taMsg.getText());
				}
				taMsg.requestFocus();
				taMsg.selectAll();
			} else if (jc == showLastMsgButton) {
				if (contactsList.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Select at least one contact.", "Warning",
							JOptionPane.WARNING_MESSAGE);
					taMsg.requestFocus();
				} else {
					String lastMsg = (String) lastMessages.get(contactsList
							.getSelectedValue());
					if (lastMsg == null) {
						JOptionPane.showMessageDialog(null,
								"Message not sent to "
										+ contactsList.getSelectedValue()
												.toString() + " yet.",
								"Warning", JOptionPane.WARNING_MESSAGE);
					} else {
						taMsg.setText(lastMsg);
						taMsg.requestFocus();
						taMsg.selectAll();
					}
				}
			} else if (jc == serviceButton) {
				if (serviceButton.getText().trim().equals("Activate")) {
					startNetService();
					updateTitle();
				} else {
					stopNetService();
					updateTitle();
				}
			} else if (jc == addContactButton) {
				addContact();
			} else if (jc == editContactButton) {
				editContact();
			} else if (jc == removeContactButton) {
				removeContact();
			}
		}
	}

	class MenusListener implements ActionListener, MenuListener {
		public void actionPerformed(ActionEvent ae) {
			JComponent jc = (JComponent) ae.getSource();
			if (jc == menuHelpAbout) {
				JOptionPane.showMessageDialog(JNetSendFrame.this,
						new JNetSendAboutBoxPanel(), "About",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (jc == menuLAFDefaults) {
				config.setLookAndFeel("Metal");
				splitPane
						.setDividerLocation(Constants.DEFAULT_DIVIDER_LOCATION);
				JNetSendFrame.this.setLocationRelativeTo(null);
				initLookAndFeel();
			} else if (jc == menuLAFWindows) {
				config.setLookAndFeel("Windows");
				initLookAndFeel();
			} else if (jc == menuLAFMotif) {
				config.setLookAndFeel("Motif");
				initLookAndFeel();
			} else if (jc == menuLAFMetal) {
				config.setLookAndFeel("Metal");
				initLookAndFeel();
			} else if (jc == menuFileImport) {
				String fileImported = null;

				try {
					ContactsFileFilter filter = new ContactsFileFilter();
					JFileChooser fc = new JFileChooser(new File("."));
					fc.setAcceptAllFileFilterUsed(false);
					fc.setFileFilter(filter);
					int result = fc.showOpenDialog(JNetSendFrame.this);
					if (result == JFileChooser.APPROVE_OPTION) {
						fileImported = fc.getSelectedFile().getPath();
						Map mapImported = Utils.importFile(fileImported);
						if (mapImported != null) {
							config.getContacts().putAll(mapImported);
							contactsList.setListData(config
									.getContactNamesArray());
						} else {
							throw new IOException();
						}
					}
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(JNetSendFrame.this,
							"Error importing contacts file: \n\"" + fileImported + "\"",
							"File import failed", JOptionPane.ERROR_MESSAGE);
				}
			} else if (jc == menuFileExport) {
				if (config.getContacts().size() > 0) {
					JFileChooser saveFile = new JFileChooser(new File("."));
					saveFile.setSelectedFile(new File(Constants.DEFAULT_CONTACTS_FILE_NAME));
					int result = saveFile.showSaveDialog(null);
					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = saveFile.getSelectedFile();
						try {
							Utils.exportFile(config.getContacts(), selectedFile);
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(JNetSendFrame.this,
									"Unable to export contacts to file: \n\""
											+ selectedFile.getAbsolutePath() + "\"", "Export file failed",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Contacts list empty",
							"Information", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (jc == menuFileSetName) {

				updateUserName();

			}
		}

		public void menuSelected(MenuEvent e) {
			JOptionPane.showMessageDialog(JNetSendFrame.this,
					new JNetSendAboutBoxPanel(), "About",
					JOptionPane.INFORMATION_MESSAGE);

		}

		public void menuDeselected(MenuEvent e) {
		}

		public void menuCanceled(MenuEvent e) {
		}
	}

	class ManageContactsListener implements KeyListener, MouseListener {
		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
				removeContact();
			}
			if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				editContact();
			}
		}

		public void keyReleased(KeyEvent ke) {
			if (ke.getKeyCode() != KeyEvent.VK_CONTROL) {
				if (ke.getKeyCode() != KeyEvent.VK_SHIFT) {
					Object name = contactsList.getSelectedValue();
					if (name != null) {
						contactsList.setListData(config.getContactNamesArray());
						contactsList.setSelectedValue(name, true);
					}
				}
			}
		}

		public void keyTyped(KeyEvent ke) {
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getClickCount() == 2) {
				editContact();
			}
		}

		public void mouseEntered(MouseEvent me) {
		}

		public void mouseExited(MouseEvent me) {
		}

		public void mousePressed(MouseEvent me) {
		}

		public void mouseReleased(MouseEvent me) {
		}
	}

	private void startNetService() {
		try {
			NetSendService.start();
			serviceButton.setText("Deactivate");
			serviceButton.setMnemonic('t');
			serviceButton.setForeground(Color.RED);
			serviceButton.setToolTipText("Stops NET SEND service.");
			// sendButton.setEnabled(true);
			serviceStatus = "(Active)";
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Unable to start NET SEND service.", "Messenger Service",
					JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
		}
	}

	private void stopNetService() {
		try {
			NetSendService.stop();
			serviceButton.setText("  Activate  ");
			serviceButton.setMnemonic('t');
			serviceButton.setForeground(Color.BLUE);
			serviceButton.setToolTipText("Starts NET SEND service.");
			// sendButton.setEnabled(false);
			serviceStatus = "(Inactive)";
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Unable to stop NET SEND service.", "Messenger Service",
					JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
		}
	}

	private void sendMessage(Object[] to, String msg) {

		Object[] toLogin = new Object[to.length];
		String fromName = config.getUserName();

		Thread sendThread;
		for (int i = 0; i < to.length; i++) {
			toLogin[i] = config.getContacts().get(to[i].toString());
			sendThread = new Thread(new SendMessageService(fromName,
					(String) toLogin[i], (String) to[i], msg, to));
			lastMessages.put(to[i], msg);
			try {
				sendThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		taMsg.requestFocus();
		taMsg.selectAll();
	}

	private void addContact() {
		Object[] message = new Object[4];
		message[0] = new String("Name:");
		message[1] = new JTextField();
		message[2] = new String("Network username:");
		message[3] = new JTextField();
		String[] options = { "OK", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, message, "Add contact",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]);
		switch (result) {
		case 0:
			String name = ((JTextField) message[1]).getText();
			String login = ((JTextField) message[3]).getText();
			config.addContact(name, login);
			contactsList.setListData(config.getContactNamesArray());
			contactsList.setSelectedValue(name, true);
			break;
		case 1:
			// JOptionPane.showMessageDialog(null, "Canceled.");
			break;
		default:
			break;
		}
	}

	private void removeContact() {
		int result = JOptionPane.showConfirmDialog(null, "Delete contact?",
				"Delete", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		switch (result) {
		case JOptionPane.OK_OPTION:
			Object[] key = contactsList.getSelectedValues();
			for (int i = 0; i < key.length; i++) {
				config.removeContact(key[i].toString());
			}
			contactsList.setListData(config.getContactNamesArray());
			break;
		case JOptionPane.CANCEL_OPTION:
			// JOptionPane.showMessageDialog(null, "Canceled.");
			break;
		default:
			break;
		}
	}

	private void editContact() {
		if (contactsList.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(null, "Contacts list empty",
					"Information", JOptionPane.INFORMATION_MESSAGE);
			taMsg.requestFocus();
		} else {
			Object[] message = new Object[4];
			message[0] = new String("Name:");
			message[1] = new JTextField(
					(String) contactsList.getSelectedValue());
			message[2] = new String("Username:");
			message[3] = new JTextField((String) config.getContacts().get(
					contactsList.getSelectedValue()));
			String[] options = { "OK", "Cancel" };
			int result = JOptionPane.showOptionDialog(null, message, "Edit",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[0]);
			switch (result) {
			case 0:
				String name = ((JTextField) message[1]).getText();
				String login = ((JTextField) message[3]).getText();
				config.removeContact(contactsList.getSelectedValue().toString());
				config.addContact(name, login);
				contactsList.setListData(config.getContactNamesArray());
				contactsList.setSelectedValue(name, true);
				break;
			case 1:
				// JOptionPane.showMessageDialog(null, "Canceled.");
				break;
			default:
				break;
			}
		}
	}

	// launch frame method
	public void launchFrame() {
		initLookAndFeel();

		Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dFrame = this.getSize();
		int x = (dScreen.width - dFrame.width) / 2;
		int y = (dScreen.height - dFrame.height) / 2;

		frameLocation = config.getLocationOnScreeen();
		if (frameLocation == null)
			this.setLocationRelativeTo(null);
		else
			this.setLocation(frameLocation);

		// show frame
		this.setVisible(true);
		taMsg.requestFocus();
	}

	private void initLookAndFeel() {

		String lookAndFeel = config.getLookAndFeel();
		String s = null;
		if (lookAndFeel.equals("Metal"))
			s = UIManager.getCrossPlatformLookAndFeelClassName();
		else if (lookAndFeel.equals("System"))
			s = UIManager.getSystemLookAndFeelClassName();
		else if (lookAndFeel.equals("Mac"))
			s = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
		else if (lookAndFeel.equals("Windows"))
			s = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		else if (lookAndFeel.equals("Motif"))
			s = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		try {
			UIManager.setLookAndFeel(s);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException ex) {
			System.err
					.println("Couldn't find class for specified look and feel:"
							+ s);
			System.err
					.println("Did you include the L&F library in the class path?");
			System.err.println("Using the default look and feel.");
		} catch (UnsupportedLookAndFeelException ex) {
			System.err.println("Can't use the specified look and feel (" + s
					+ ") on this platform.");
			System.err.println("Using the default look and feel.");
		} catch (Exception ex) {
			System.err.println("Couldn't get specified look and feel (" + s
					+ "), for some reason.");
			System.err.println("Using the default look and feel.");
			ex.printStackTrace();
		}
	}

	private void updateTitle() {
		this.setTitle(config.getUserName() + " - " + Constants.APP_NAME + " - "
				+ serviceStatus);
	}

	private void updateUserName() {
		Object[] message = new Object[2];
		message[0] = new String("Nickname: ");
		message[1] = new JTextField(config.getUserName());
		String[] options = { "OK", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, message,
				"Change nickname", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		((JTextField) message[1]).selectAll();
		((JTextField) message[1]).requestFocus();

		if (((JTextField) message[1]).getText().trim().length() == 0) {
			if (result == 0) { // OK button
				JOptionPane.showMessageDialog(null, "Nickname can't be blank.",
						"Warning", JOptionPane.WARNING_MESSAGE);

				// try again
				updateUserName();
			} else if (config.getUserName() == null) { // Cancel button
				System.exit(-1);
			}
		} else {
			if (result == 0) { // OK button
				config.setUserName(((JTextField) message[1]).getText());
				updateTitle();
			}
		}
	}
}