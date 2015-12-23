package aiu.jp.nps.admi.tools.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import aiu.jp.nps.admi.tools.rcp.views.DatabaseView;

public class DatabaseSortHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IViewReference viewReference = page.findViewReference(DatabaseView.ID);
		if (viewReference != null) {
	        final IViewPart view = viewReference.getView(true);
	        if (view instanceof DatabaseView) {
//	            final DatabaseView dbView = (DatabaseView) view;
//	            NpsRecordHandler.getDefault().sort(dbView.getSortColumn(),dbView.getSortDir());
	        }
		}
		return null;
	}

}
