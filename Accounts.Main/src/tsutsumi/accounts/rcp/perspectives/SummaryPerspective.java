package tsutsumi.accounts.rcp.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class SummaryPerspective implements IPerspectiveFactory {
	public static final String ID = "Summary.perspective";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addPerspectiveShortcut(ID);
	}

}
