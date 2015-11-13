package com.mime.minefront.gui;

import java.awt.Dimension;

public class Options extends Launcher {
	private static final long serialVersionUID = 1L;
	
	private int width = 550;
	private int height = 450;
	
	public Options() {
		super(1);
		setTitle("Options - Minefront Launcher");
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
	}
	
}


