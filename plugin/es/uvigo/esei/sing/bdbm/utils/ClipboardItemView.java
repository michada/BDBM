package es.uvigo.esei.sing.bdbm.utils;

/**
 * 
 * @author Miguel Reboiro-Jato
 *
 */
public interface ClipboardItemView {
	public boolean canShowClipboardItem(Object clipboardItem);
	public boolean showClipboardItem(Object clipboardItem);
}
