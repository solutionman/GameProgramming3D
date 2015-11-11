package com.mime.minefront;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Launcher extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;
	
	private int width = 240;
	private int height = 320;
	private int button_width = 80;
	private int button_height = 40;

	public Launcher() {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}*/    
		// this code make button looks similar to buttons in system.  
		//Commented it because it appears very slowly
		setTitle("Minefront Launcher");
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		drawButtons();
		
	}
	
	private void drawButtons() {
		play = new JButton("Play!");
		rplay = new Rectangle((width/2) - (button_width/2), 90, button_width, button_height);
		play.setBounds(rplay);
		window.add(play);
		
		options = new JButton("Options");
		roptions = new Rectangle((width/2) - (button_width/2), 140, button_width, button_height);
		options.setBounds(roptions);
		window.add(options);
		
		help = new JButton("Help");
		rhelp = new Rectangle((width/2) - (button_width/2), 190, button_width, button_height);
		help.setBounds(rhelp);
		window.add(help);
		
		quit = new JButton("Quit");
		rquit = new Rectangle((width/2) - (button_width/2), 240, button_width, button_height);
		quit.setBounds(rquit);
		window.add(quit);
	}
	
}




