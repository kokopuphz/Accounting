package tsutsumi.accounts.rcp.handlers;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

public class GlobalClipboard {
	public static Clipboard clipboard = new Clipboard(Display.getCurrent());
	private static Object owner;
	public static void setOwner(Object o) {
		owner = o;
	}
	
	public static Object getOwner() {
		return owner;
	}
	
}
