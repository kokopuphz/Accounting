package tsutsumi.accounts.rcp.dialogs.ireport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.reference.Type;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.dialogs.AddNewDialog;
import tsutsumi.accounts.rcp.dialogs.DatePickerDialog;
import tsutsumi.accounts.rcp.dialogs.itransactions.ITransactions;

public class FilterPart extends Composite {
	private Combo accountCombo;
	private Combo categoryCombo;
	private Combo methodCombo;
	private Combo typeCombo;
	private DateTime startDate;
	private DateTime endDate;
	private IReport iReport;
	private ITransactions iTransations;
	
	private Button creditButton;
	private Button debitButton;
	
	public FilterPart(IReport iReport) {
		super(iReport.getShell(), SWT.NONE);
		this.iReport = iReport;
    	GridLayout thisLayout = new GridLayout();
    	thisLayout.marginHeight = 0;
    	thisLayout.marginWidth = 0;
    	thisLayout.horizontalSpacing = 0;
    	thisLayout.verticalSpacing = 0;
    	this.setLayout(thisLayout);
		createPartControl(iReport.getShell());
	}
	
	public FilterPart(ITransactions iTransations) {
		super(iTransations.getShell(), SWT.NONE);
		this.iTransations = iTransations;
    	GridLayout thisLayout = new GridLayout();
    	thisLayout.marginHeight = 0;
    	thisLayout.marginWidth = 0;
    	thisLayout.horizontalSpacing = 0;
    	thisLayout.verticalSpacing = 0;
    	this.setLayout(thisLayout);
		createPartControl(iTransations.getShell());
	}
	
	
	
	public Account getAccount() {
		Account[] accounts = (Account[])accountCombo.getData();
		int currSelect = accountCombo.getSelectionIndex();
		if (currSelect == 0)
			return null;
		return accounts[currSelect-1];
	}
	
	public Method getMethod() {
		Method[] accounts = (Method[])methodCombo.getData();
		int currSelect = methodCombo.getSelectionIndex();
		if (currSelect == 0)
			return null;
		return accounts[currSelect-1];
	}
	
	public Category getCategory() {
		Category[] accounts = (Category[])categoryCombo.getData();
		int currSelect = categoryCombo.getSelectionIndex();
		if (currSelect == 0)
			return null;
		return accounts[currSelect-1];
	}
	
	public Type getType() {
		Type[] accounts = (Type[])typeCombo.getData();
		int currSelect = typeCombo.getSelectionIndex();
		if (currSelect == 0)
			return null;
		return accounts[currSelect-1];
	}
	
	public String getStartDateString() {
		return startDate.getYear() + "/" + (startDate.getMonth() + 1) + "/" + startDate.getDay();
	}

	public String getEndDateString() {
		return endDate.getYear() + "/" + (endDate.getMonth() + 1) + "/" + endDate.getDay();
	}

	
	public void createPartControl(Composite parent) {
		Composite darkBorderContainer = new Composite(parent, SWT.NONE);
		GridLayout darkBorderLayout = new GridLayout();
		darkBorderLayout.numColumns = 1;
		darkBorderLayout.marginHeight = 1;
		darkBorderLayout.marginWidth = 1;
		darkBorderLayout.horizontalSpacing = 0;
		darkBorderLayout.verticalSpacing = 0;
		darkBorderContainer.setLayout(darkBorderLayout);
		darkBorderContainer.setBackground(Colors.BORDER);
		GridData borderData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		darkBorderContainer.setLayoutData(borderData);

		Composite lightBorderContainer = new Composite(darkBorderContainer, SWT.NONE);
		darkBorderLayout = new GridLayout();
		darkBorderLayout.numColumns = 1;
		darkBorderLayout.marginHeight = 0;
		darkBorderLayout.marginWidth = 0;
		darkBorderLayout.horizontalSpacing = 0;
		darkBorderLayout.verticalSpacing = 0;
		lightBorderContainer.setLayout(darkBorderLayout);
		borderData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		lightBorderContainer.setLayoutData(borderData);
		
		Composite borderContainer = new Composite(lightBorderContainer, SWT.NONE);
		GridLayout borderLayout = new GridLayout();
		borderLayout.numColumns = 1;
		borderLayout.marginHeight = 0;
		borderLayout.marginWidth = 0;
		borderLayout.horizontalSpacing = 0;
		borderLayout.verticalSpacing = 0;
		borderContainer.setLayout(borderLayout);
		borderData = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		borderContainer.setLayoutData(borderData);
		
		Composite dateFilterContainer = new Composite(borderContainer, SWT.NONE);
		GridLayout dateFilterLayout = new GridLayout();
		dateFilterLayout.numColumns = 8;
		dateFilterContainer.setLayout(dateFilterLayout);
		GridData filterData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		dateFilterContainer.setLayoutData(filterData);
		
		
		SelectionListener filterListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (e.widget instanceof Button) {
					if (iReport != null) {
						iReport.setAccount(getAccount());
						iReport.setMethod(getMethod());
						iReport.setCategory(getCategory());
						iReport.setType(getType());
						iReport.setStartDate(getStartDateString());
						iReport.setEndDate(getEndDateString());
						iReport.refresh();
	//					SummaryView.getDefault().updateAllAmount();
					} else {
						iTransations.setAccount(getAccount()==null?0:getAccount().getId());
						iTransations.setMethod(getMethod()==null?0:getMethod().getId());
						iTransations.setCategory(getCategory()==null?0:getCategory().getId());
						iTransations.setType(getType()==null?0:getType().getId());
						iTransations.setStartDate(getStartDateString());
						iTransations.setEndDate(getEndDateString());
						iTransations.setCreditShown(creditButton.getSelection());
						iTransations.setDebitShown(debitButton.getSelection());
						iTransations.refresh();
					}
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
//				SummaryView.getDefault().updateAllAmount();
			}
		};
		
		Label typeLabel = new Label(dateFilterContainer, SWT.NONE);
		typeLabel.setText("Type:");
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        typeCombo = new Combo(dateFilterContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Type[] type = AbstractReferenceData.getTypes().toArray(new Type[AbstractReferenceData.getTypes().size()]);
        typeCombo.setData(type);
        typeCombo.add("ALL");
        for (Type s : type) {
        	typeCombo.add(s.getName());
        }
        typeCombo.select(0);
        typeCombo.setLayoutData(gridData);
        typeCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				updateCategoryCombo();
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
        });

		
		Label startDtLabel = new Label(dateFilterContainer, SWT.NONE);
		startDtLabel.setText(" Start:");
//		startDtLabel.setBackground(header);

		Image image = new Image(Display.getCurrent(), AddNewDialog.class.getResourceAsStream("cal.gif"));
		startDate = new DateTime (dateFilterContainer, SWT.DATE | SWT.MEDIUM| SWT.BORDER);
		startDate.setMonth(0);
		startDate.setDay(1);
		startDate.setYear(1900);
		startDate.addSelectionListener(filterListener);
		
		final DatePickerDialog dtPick1 = new DatePickerDialog(dateFilterContainer.getShell(), startDate);
		Label imageLabel = new Label(dateFilterContainer, SWT.NONE);
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

		Label endDtLabel = new Label(dateFilterContainer, SWT.NONE);
		endDtLabel.setText(" End:");
//		endDtLabel.setBackground(header);

	    endDate = new DateTime (dateFilterContainer, SWT.DATE | SWT.MEDIUM| SWT.BORDER);
	    endDate.addSelectionListener(filterListener);
//	    endDate.setMonth(11);
//	    endDate.setDay(31);
//	    endDate.setYear(2999);
		
		final DatePickerDialog dtPick2 = new DatePickerDialog(dateFilterContainer.getShell(), endDate);
		
		Label imageLabel2 = new Label(dateFilterContainer, SWT.NONE);
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

		
		Composite typeFilterContainer = new Composite(borderContainer, SWT.NONE);
		GridLayout typeFilterLayout = new GridLayout();
		typeFilterLayout.numColumns = 8;
		typeFilterContainer.setLayout(typeFilterLayout);
		GridData typeFilterData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		typeFilterContainer.setLayoutData(typeFilterData);
		
		Label accountLabel = new Label(typeFilterContainer, SWT.NONE);
		accountLabel.setText("Account:");
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        accountCombo = new Combo(typeFilterContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Account[] acct = AbstractReferenceData.getAccounts().toArray(new Account[AbstractReferenceData.getAccounts().size()]);
        accountCombo.setData(acct);
        accountCombo.add("ALL");
        for (Account s : acct) {
        	accountCombo.add(s.getName());
        }
        accountCombo.select(0);
        accountCombo.setLayoutData(gridData);

		Label categoryLabel = new Label(typeFilterContainer, SWT.NONE);
		categoryLabel.setText(" Category:");
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        categoryCombo = new Combo(typeFilterContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Category[] cat = AbstractReferenceData.getCategories(0).toArray(new Category[AbstractReferenceData.getCategories(0).size()]);
        categoryCombo.setData(cat);
        categoryCombo.add("ALL");
        for (Category s : cat) {
        	categoryCombo.add(s.getName());
        }
        categoryCombo.select(0);
        categoryCombo.setLayoutData(gridData);

		Label methodLabel = new Label(typeFilterContainer, SWT.NONE);
		methodLabel.setText(" Payment Method:");
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        methodCombo = new Combo(typeFilterContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        ArrayList<Method> tempMethod = new ArrayList<Method>();
        tempMethod.addAll(AbstractReferenceData.getCashMethods());
        tempMethod.addAll(AbstractReferenceData.getBankMethods());
        tempMethod.addAll(AbstractReferenceData.getCreditCardMethods());
        Method[] method = tempMethod.toArray(new Method[tempMethod.size()]);
        methodCombo.setData(method);
        methodCombo.add("ALL");
        for (Method s : method) {
        	methodCombo.add(s.getName());
        }
        methodCombo.select(0);
        methodCombo.setLayoutData(gridData);

        if (iReport == null) {
			Composite buttonContainer = new Composite(borderContainer, SWT.NONE);
			GridLayout buttonContainerLayout = new GridLayout();
			buttonContainerLayout.numColumns = 2;
			buttonContainer.setLayout(buttonContainerLayout);
			GridData buttonContainerData = new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1);
			buttonContainer.setLayoutData(buttonContainerData);
	
			gridData = new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1);
			creditButton = new Button(buttonContainer, SWT.CHECK);
			creditButton.setText("Show Credits");
//			creditButton.addSelectionListener(filterListener);
			creditButton.setLayoutData(gridData);
			creditButton.setSelection(true);
	
			debitButton = new Button(buttonContainer, SWT.CHECK);
			debitButton.setText("Show Debits");
//			debitButton.addSelectionListener(filterListener);
			debitButton.setLayoutData(gridData);
			debitButton.setSelection(true);
        }
		
        Composite buttonContainer = new Composite(borderContainer, SWT.NONE);
        GridLayout buttonContainerLayout = new GridLayout();
		buttonContainerLayout.numColumns = 1;
		buttonContainer.setLayout(buttonContainerLayout);
		GridData buttonContainerData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		buttonContainer.setLayoutData(buttonContainerData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		Button filterButton = new Button(buttonContainer, SWT.PUSH);
		filterButton.setText("Apply Filter");
		filterButton.addSelectionListener(filterListener);
		filterButton.setLayoutData(gridData);
	}
	
	private void updateCategoryCombo() {
		int selected = typeCombo.getSelectionIndex();
		int typeId = 0;
		Category currentSelectedCat = null;
		
		if (selected > 0) {
			Type[] types = (Type[])typeCombo.getData();
			typeId = types[selected-1].getId();
		}
		int selectedCombo = categoryCombo.getSelectionIndex();
		Category[] cats = (Category[])categoryCombo.getData();
		if (selectedCombo > 0)
			currentSelectedCat = cats[selectedCombo-1];
		
		Category[] cat = AbstractReferenceData.getCategories(typeId).toArray(new Category[AbstractReferenceData.getCategories(typeId).size()]);
		categoryCombo.removeAll();
        categoryCombo.setData(cat);
        categoryCombo.add("ALL");
        categoryCombo.select(0);
        for (int i=0; i < cat.length; i++) {
        	categoryCombo.add(cat[i].getName());
        }
    	if (currentSelectedCat != null) {
    		setCategory(currentSelectedCat);
    	}
	}
	
	private SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
	public void setStartDate(String s) {
		Date date = null;
		try {
			date = formatter1.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		startDate.setYear(cal.get(Calendar.YEAR));
		startDate.setMonth(cal.get(Calendar.MONTH));
		startDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
	}

	public void setEndDate(String s) {
		Date date = null;
		try {
			date = formatter1.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		endDate.setYear(cal.get(Calendar.YEAR));
		endDate.setMonth(cal.get(Calendar.MONTH));
		endDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
	}
	
	public void setMethod(Method m) {
		Method[] methods = (Method[])methodCombo.getData();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getId()==(m==null?0:m.getId())) {
				methodCombo.select(i+1);
			}
		}
	}
	
	public void setCategory(Category c) {
		Category[] categories = (Category[])categoryCombo.getData();
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getId()==(c==null?0:c.getId())) {
				categoryCombo.select(i+1);
			}
		}
	}
	
	public void setAccount(Account a) {
		Account[] accounts = (Account[])accountCombo.getData();
		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i].getId()== (a==null ? 0:a.getId())) {
				accountCombo.select(i+1);
			}
		}
	}
	
	public void setType(Type t) {
		Type[] types = (Type[])typeCombo.getData();
		for (int i = 0; i < types.length; i++) {
			if (types[i].getId()==(t==null?0:t.getId())) {
				typeCombo.select(i+1);
				updateCategoryCombo();
				break;
			}
		}
	}
	
	public void setShowCredit(boolean value) {
		creditButton.setSelection(value);
	}
	
	public void setShowDebit(boolean value) {
		debitButton.setSelection(value);
	}

}
