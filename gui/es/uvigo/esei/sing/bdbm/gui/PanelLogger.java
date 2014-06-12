package es.uvigo.esei.sing.bdbm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelLogger extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private final JTextArea taLogger;

	public PanelLogger(ExecutionObservableAppender appender) {
		super(new BorderLayout());
		
		this.taLogger = new JTextArea();
		this.taLogger.setOpaque(true);
		this.taLogger.setBackground(Color.BLACK);
		this.taLogger.setForeground(Color.WHITE);
		this.taLogger.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		this.taLogger.setEditable(false);
		
		this.add(new JScrollPane(this.taLogger), BorderLayout.CENTER);
		
		appender.addObserver(this);
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (this.taLogger != null)
			this.taLogger.setBackground(bg);
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (this.taLogger != null)
			this.taLogger.setForeground(fg);
	}
	
	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
		if (this.taLogger != null)
			this.taLogger.setOpaque(isOpaque);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof String) {
			this.taLogger.append(((String) arg) + '\n');
			this.taLogger.setCaretPosition(this.taLogger.getText().length());
		}
	}
}
