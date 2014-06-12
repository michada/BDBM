package es.uvigo.esei.sing.bdbm.gui.tabpanel;

import java.awt.AWTEvent;

public class TabCloseEvent extends AWTEvent {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	public static final int TAB_CLOSING = AWTEvent.RESERVED_ID_MAX + 1;
	public static final int TAB_CLOSED = AWTEvent.RESERVED_ID_MAX + 2;
	
	private final int tabIndex;
	private boolean cancelled;
	
	public TabCloseEvent(Object source, int id, int tabIndex) {
		super(source, id);
		this.tabIndex = tabIndex;
		this.cancelled = false;
	}
	
	/**
	 * @return the index
	 */
	public int getTabIndex() {
		return this.tabIndex;
	}
	
	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void cancel() {
		this.cancelled = true;
	}
}
