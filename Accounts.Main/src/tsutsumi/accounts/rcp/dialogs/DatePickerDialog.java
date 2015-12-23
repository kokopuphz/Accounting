package tsutsumi.accounts.rcp.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DatePickerDialog {
	private Shell dateDialog;
    
    public DatePickerDialog(Shell parent, final DateTime date) {
		dateDialog = new Shell (parent.getShell(), SWT.NO_TRIM|SWT.MODELESS);
		dateDialog.setLayout (new GridLayout (2, false));
		final DateTime calendar = new DateTime (dateDialog, SWT.CALENDAR);
		dateDialog.addShellListener(new ShellListener() {
			public void shellActivated(ShellEvent e) {
				calendar.setDate(date.getYear(), date.getMonth(), date.getDay());
			}
			public void shellClosed(ShellEvent e) {
				dateDialog.setVisible(false);
				e.doit=false;
			}
			public void shellDeactivated(ShellEvent e) {
				dateDialog.setVisible(false);
			}
			public void shellDeiconified(ShellEvent e) {}
			public void shellIconified(ShellEvent e) {}
		});
		GridData gridDateData = new GridData();
	    gridDateData.horizontalSpan = 2;
		calendar.setLayoutData(gridDateData);
		calendar.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {}
			public void widgetDefaultSelected(SelectionEvent e) {
				date.setDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
				dateDialog.setVisible(false);
			}
		});

		Button cancel = new Button (dateDialog, SWT.PUSH);
		cancel.setText ("Cancel");
		cancel.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
		cancel.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				dateDialog.setVisible(false);
			}
		});
		
		Button ok = new Button (dateDialog, SWT.PUSH);
		ok.setText ("Select");
		ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				date.setDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
				dateDialog.setVisible(false);
			}
		});
		dateDialog.setDefaultButton (ok);
		dateDialog.pack ();
		dateDialog.setVisible(false);
    }
    
    public void setVisible(boolean visible) {
    	if (visible) {
    		dateDialog.open();
    	} else {
    		dateDialog.setVisible(visible);
    	}
    }
    
    public void setLocation() {
		Point p = dateDialog.getSize();
		Point mouseLoc = Display.getCurrent().getCursorLocation();
		dateDialog.setBounds(mouseLoc.x, mouseLoc.y, p.x, p.y);
    }
    
}