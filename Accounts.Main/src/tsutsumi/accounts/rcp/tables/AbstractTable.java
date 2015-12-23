package tsutsumi.accounts.rcp.tables;

import org.eclipse.swt.widgets.Label;

public interface AbstractTable {
	public void processDoubleClick(Label e);
	public boolean hasLabel(Label e);
}

