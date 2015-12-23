package aiu.jp.nps.admi.tools.rcp.views.dnd;

import org.eclipse.ui.part.IDropActionDelegate;

public class NpsRecordDropPluginAdapter implements IDropActionDelegate {

	public boolean run(Object source, Object target) {
		if (source==target) {
			return false;
		} else {
			return true;
		}
	}
}
