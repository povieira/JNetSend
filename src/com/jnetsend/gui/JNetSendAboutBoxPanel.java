package com.jnetsend.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jnetsend.util.Constants;


public class JNetSendAboutBoxPanel extends JPanel {
	Border border = BorderFactory.createEtchedBorder();
	GridBagLayout mainLayout = new GridBagLayout();
	JLabel copyrightLabel = new JLabel();
	JLabel authorLabel = new JLabel();
	JLabel titleLabel = new JLabel();

	public JNetSendAboutBoxPanel() {
		this.setLayout(mainLayout);
		this.setBorder(border);
		titleLabel.setText(Constants.APP_NAME);
		copyrightLabel.setText("Copyright (c) 2002 Pablo Vieira");
		titleLabel.setFont(Constants.TEXT_FONT);
		authorLabel.setFont(Constants.TEXT_FONT);
		authorLabel.setText("Pablo Vieira");
		copyrightLabel.setFont(Constants.TEXT_FONT);
		this.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						15, 0, 15), 0, 0));
		this.add(authorLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						15, 0, 15), 0, 0));
		this.add(copyrightLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						15, 0, 15), 0, 0));
	}
}