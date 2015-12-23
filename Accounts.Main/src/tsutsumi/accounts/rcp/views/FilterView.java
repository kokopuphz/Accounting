package tsutsumi.accounts.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.rcp.dialogs.AddNewDialog;
import tsutsumi.accounts.rcp.dialogs.DatePickerDialog;

public class FilterView extends AbstractViewPart {
	public final static String ID = "Accounts.Main.SummaryView";
	private static FilterView defaultView;
	private DateTime startDate;
	private DateTime endDate;

	public FilterView() {
		super();
		this.minHeight = 30;
		this.maxHeight = 30;
	}
	public void createPartControl(Composite parent) {
//		Color header = new Color (Display.getCurrent() , 238, 203, 173);
//		Color header = new Color (Display.getCurrent(), 250, 235, 215);
		defaultView = this;
		Composite filterContainer = new Composite(parent, SWT.NONE);
//		filterContainer.setBackground(header);
		GridLayout filterLayout = new GridLayout();
		filterLayout.numColumns = 8;
		filterContainer.setLayout(filterLayout);
		GridData filterData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		filterContainer.setLayoutData(filterData);
		
		SelectionListener filterListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (e.widget instanceof Button) {
					SummaryView.getDefault().updateAllAmount();
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				SummaryView.getDefault().updateAllAmount();
			}
		};
		
		Listener changeListener = new Listener() {
			public void handleEvent(Event event) {
				SummaryView.getDefault().updateAllAmount();
			}
		};
		
		Label startDtLabel = new Label(filterContainer, SWT.NONE);
		startDtLabel.setText("Start:");
//		startDtLabel.setBackground(header);

		Image image = new Image(Display.getCurrent(), AddNewDialog.class.getResourceAsStream("cal.gif"));
		startDate = new DateTime (filterContainer, SWT.DATE | SWT.MEDIUM| SWT.BORDER);
		startDate.setMonth(0);
		startDate.setDay(1);
		startDate.setYear(1900);
		startDate.addSelectionListener(filterListener);
		startDate.addListener(SWT.KeyUp, changeListener);
		
		final DatePickerDialog dtPick1 = new DatePickerDialog(filterContainer.getShell(), startDate);
		Label imageLabel = new Label(filterContainer, SWT.NONE);
		imageLabel.setImage(image);
	    imageLabel.addMouseListener(new MouseListener() {
	    	private boolean mouseOver = false;;
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				mouseOver = true;
			}
			public void mouseUp(MouseEvent e) {
				if (mouseOver) {
					dtPick1.setLocation();
					dtPick1.setVisible(true);
				}
				mouseOver = false;
			}
	    });

		Label endDtLabel = new Label(filterContainer, SWT.NONE);
		endDtLabel.setText("  End:");
//		endDtLabel.setBackground(header);

	    endDate = new DateTime (filterContainer, SWT.DATE | SWT.MEDIUM| SWT.BORDER);
	    endDate.addSelectionListener(filterListener);
	    endDate.addListener(SWT.KeyUp, changeListener);
//	    endDate.setMonth(11);
//	    endDate.setDay(31);
//	    endDate.setYear(2999);
		
		final DatePickerDialog dtPick2 = new DatePickerDialog(filterContainer.getShell(), endDate);
		
		Label imageLabel2 = new Label(filterContainer, SWT.NONE);
		imageLabel2.setImage(image);
		imageLabel2.addMouseListener(new MouseListener() {
	    	private boolean mouseOver = false;;
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				mouseOver = true;
			}
			public void mouseUp(MouseEvent e) {
				if (mouseOver) {
					dtPick2.setLocation();
					dtPick2.setVisible(true);
				}
				mouseOver = false;
			}
	    });
		
		Label dummyLabel = new Label(filterContainer, SWT.NONE);
		dummyLabel.setText(" ");
//		dummyLabel.setBackground(header);
		
		Button filterButton = new Button(filterContainer, SWT.PUSH);
		filterButton.setText("Apply Filter");
		filterButton.addSelectionListener(filterListener);
	}
	
	public void dispose() {
		defaultView = null;
	}
	
	public static FilterView getDefault() {
		return defaultView;
	}
	
	public String getStartDateString() {
		if (startDate == null) {
			return "1900/01/01";
		}
		return startDate.getYear() + "/" + (startDate.getMonth() + 1) + "/" + startDate.getDay();
	}

	public String getEndDateString() {
		if (endDate == null) {
			return Utils.getToday();
		}
		return endDate.getYear() + "/" + (endDate.getMonth() + 1) + "/" + endDate.getDay();
	}

	
	
	
}
