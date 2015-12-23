package tsutsumi.accounts.rcp.contributions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;


public class StatusBarDbControl extends WorkbenchWindowControlContribution {
	private static StatusBarDbControl current;
	private static String textInfo = "WELCOME";
	private static CLabel messageLabel;
	public static void refresh() {
		if (Display.getDefault().getThread() == Thread.currentThread()) {
			current.getParent().update(true);
		} else {
			 Display.getDefault().syncExec(new Runnable() {
				 public void run() {
					 current.getParent().update(true);
				 }
			 });
		}
	}
	
	public StatusBarDbControl() {
		current = this;
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
        FormLayout layout = new FormLayout();
        layout.spacing=5;
        comp.setLayout(layout);
        
        FormData messForm = new FormData();
        messForm.left = new FormAttachment(0);
        messForm.bottom = new FormAttachment(100);
        messForm.width = 250;

		messageLabel = new CLabel(comp, SWT.NONE);
		messageLabel.setLayoutData(messForm);
		messageLabel.setText(textInfo);
		return comp;
	}
	

	public static void setText(String s) {
		textInfo = s;
		setText();
	}

	private static void setText() {
		if (messageLabel != null && !messageLabel.isDisposed()) {
			final String text = textInfo;
			if (Display.getDefault().getThread() == Thread.currentThread()) {
				if (!Display.getDefault().isDisposed()) {
					messageLabel.setText(text);
				}
			} else {
				 Display.getDefault().syncExec(new Runnable() {
					 public void run() {
						 if (!messageLabel.isDisposed()) {
							 if (!Display.getDefault().isDisposed()) {
								messageLabel.setText(text);
							 }
						 }
					 }
				 });
			}
		}
	}

	
	public boolean isDynamic() {
		return true;
	}

	public boolean isDirty() {
		return true;
	}

}
