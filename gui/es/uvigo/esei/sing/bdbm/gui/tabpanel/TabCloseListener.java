package es.uvigo.esei.sing.bdbm.gui.tabpanel;

import java.util.EventListener;

public interface TabCloseListener extends EventListener {
	public void tabClosing(TabCloseEvent event);
	public void tabClosed(TabCloseEvent event);
}
