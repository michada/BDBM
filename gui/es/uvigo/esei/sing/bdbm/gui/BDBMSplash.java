package es.uvigo.esei.sing.bdbm.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class BDBMSplash extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static ImageIcon IMAGE_SPLASH = new ImageIcon(BDBMSplash.class.getResource("images/splash.png"));
	private final static ImageIcon IMAGE_LOADING = new ImageIcon(BDBMSplash.class.getResource("images/loader.gif"));
	
	public BDBMSplash() {
		super();
		
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JLayeredPane contentPane = new JLayeredPane();
		contentPane.setPreferredSize(
			new Dimension(IMAGE_SPLASH.getIconWidth(), IMAGE_SPLASH.getIconHeight())
		);
		
		final JLabel lblSplash = new JLabel(IMAGE_SPLASH);
		lblSplash.setBounds(0, 0, IMAGE_SPLASH.getIconWidth(), IMAGE_SPLASH.getIconHeight());
		
		final JLabel lblLoadingIcon = new JLabel("Loading database...", IMAGE_LOADING, JLabel.LEADING);
		lblLoadingIcon.setForeground(Color.WHITE);
		lblLoadingIcon.setFont(lblLoadingIcon.getFont().deriveFont(Font.BOLD));
		
		lblLoadingIcon.setBounds(
			4, IMAGE_SPLASH.getIconHeight() - IMAGE_LOADING.getIconHeight() - 4, 
			IMAGE_SPLASH.getIconWidth(), IMAGE_LOADING.getIconHeight()
		);
		contentPane.add(lblSplash, 0, 0);
		contentPane.add(lblLoadingIcon, 1, 1);
		
		this.setContentPane(contentPane);
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
}
