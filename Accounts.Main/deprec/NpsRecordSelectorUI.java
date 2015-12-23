package aiu.jp.nps.admi.tools.ui;

import java.util.Vector;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import aiu.jp.nps.admi.tools.FileLoader;
import aiu.jp.nps.admi.tools.db.DatabaseController;
import aiu.jp.nps.admi.tools.db.NpsRecord;
import aiu.jp.nps.admi.tools.db.NpsRecordHandler;

public class NpsRecordSelectorUI {
	public static DatabaseController dbController;
	public static Shell shell;
	public static NpsRecordSelectorUI currentProgram;
	private Display display;
	private Table databaseTable;
	private Text policySearch;
	private Text seqNoSearch;
	private Text seqSubSearch;
	private Combo sourceSystemIdSearch;
	private Label status;
	private Label dbStatus;
	private Label dbSchema;
	private Button activeSearchButton;
	private Button doSearchButton;
	private Button uploadButton;
	private Vector<NpsRecord> npsRecords;
	private NpsRecordHandler npsSelect;
	private ProgressBar progressBar;
	private boolean needToClear = false;
	private Label progressBar2;
	public static Clipboard clipboard;

	private CTabFolder workFolder; 
//	private WorkerThread wth = new WorkerThread();
	private Object synchObject = new Object();
	
	private FileLoader fileLoader;
	
	private Label databaseBoardLabel;
	private int returnCode = 0;

//	public NpsRecordSelectorUI() {
//        //Display display = new Display();
//		this.display = Display.getDefault();
//		dbController = new DatabaseController();
//		clipboard = new Clipboard(display);
//		currentProgram = this;
//		fileLoader = new FileLoader();
//        npsSelect = new NpsRecordHandler();
//        npsRecords = new Vector<NpsRecord>();
//        shell = new Shell(this.display);
//        shell.setText("NPS Data3 XML Patch Tool by Tsutsumi v1");
//        shell.setSize(800, 500);
//        shell.setMinimumSize(450, 350);
//        shell.setImage(new Image(display, NpsRecordSelectorUI.class.getResourceAsStream("icon.gif")));
//        shell.addShellListener(new ShellListener() {
//            public void shellActivated(ShellEvent event) {}
//            public void shellClosed(ShellEvent event) {
//            	boolean allClosed = true;
//            	for (CTabItem ctab : workFolder.getItems()) {
//            		NpsDocumentUI doc = (NpsDocumentUI)ctab;
//            		if (!doc.close(false)) {
//            			allClosed = false;
//            		}
//            	}
//            	event.doit = allClosed;
//            }
//            public void shellDeactivated(ShellEvent arg0) {}
//            public void shellDeiconified(ShellEvent arg0) {}
//            public void shellIconified(ShellEvent arg0) {}
//          });
//
//        initUI();
//        center();
//        showSelectionScreen();
//	}
//	
//	public FileLoader getFileLoader() {
//		return fileLoader;
//	}
//	public void showSelectionScreen() {
//        shell.open();
//        dbConnectCommand();
//        while (!shell.isDisposed()) {
//          if (!display.readAndDispatch()) {
//            display.sleep();
//          }
//        }
//        //System.exit(0);
//	}
//	
//	public int getCode() {
//		return returnCode;
//	}
//	
//	public static void focus() {
//		shell.setActive();
//	}
//	
//    private void center() {
//        Rectangle bds = shell.getDisplay().getBounds();
//        Point p = shell.getSize();
//        int nLeft = (bds.width - p.x) / 2;
//        int nTop = (bds.height - p.y) / 2;
//        shell.setBounds(nLeft, nTop, p.x, p.y);
//    }
//
//    private void initUI() {
//        FormLayout layout = new FormLayout();
//        shell.setLayout(layout);
//
//        //listeners... need to streamline a lot of these
//        TextInputListener textListen = new TextInputListener();
////        SelectionInputListener sil = new SelectionInputListener();
////        SelectionListenerTable tableListener = new SelectionListenerTable();
////        OpenEditorMenuListener tableEditorMenuListener = new OpenEditorMenuListener();
//        
//    	//menu items
//        Menu menuBar = new Menu(shell, SWT.BAR);
//        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
//        cascadeFileMenu.setText("&File");
//        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
//        cascadeFileMenu.setMenu(fileMenu);
//
//        MenuItem menuItemA = new MenuItem (fileMenu, SWT.PUSH);
//        menuItemA.setText ("&New\tCtrl+N");
//        menuItemA.setAccelerator(SWT.MOD1 + 'N');
//        menuItemA.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//			}
//			public void widgetSelected(SelectionEvent arg0) {
//				new NpsDocumentUI(workFolder);
//				returnCode = 1;
//				//workFolder.setSelection(workFolder.getItemCount()-1);
//			}
//        });
//
//        MenuItem menuItem0 = new MenuItem (fileMenu, SWT.PUSH);
//        menuItem0.setText ("&Open File...\tCtrl+O");
//        menuItem0.setAccelerator(SWT.MOD1 + 'O');
//        menuItem0.addSelectionListener(new SelectionListener() {
//            public void widgetSelected(SelectionEvent e) {
//            	try {
//            		fileLoader.openXMLFile(workFolder);
////            			workFolder.setSelection(workFolder.getItemCount()-1);        			
//            	} catch (Exception exc) {
//            		System.out.println("problems...?");
//            	}
//            }
//            public void widgetDefaultSelected(SelectionEvent e) {
//            	
//            }
//        });
//
////      	MenuItem menuItem1 = new MenuItem (fileMenu, SWT.PUSH);
////      	menuItem1.setText ("&Open Selection In Editor...");
////      	menuItem1.addSelectionListener(tableEditorMenuListener);
////
////      	MenuItem menuItem2 = new MenuItem (fileMenu, SWT.PUSH);
////      	menuItem2.setText ("O&pen Selection In Multiple Editors...");
////      	menuItem2.addSelectionListener(tableEditorMenuListener);
//
//      	new MenuItem(fileMenu, SWT.SEPARATOR);
//      	
//        MenuItem menuItemB = new MenuItem (fileMenu, SWT.PUSH);
//        menuItemB.setText ("&Close");
//        menuItemB.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//			}
//			public void widgetSelected(SelectionEvent arg0) {
//      		  if (workFolder.getSelection()!=null) {
//      			  if (((NpsDocumentUI)workFolder.getSelection()).close(false)) {
//      				  workFolder.getSelection().dispose();
//      			  }
//      		  }
//			}
//        });
//
//        MenuItem menuItemC = new MenuItem (fileMenu, SWT.PUSH);
//        menuItemC.setText ("C&lose All");
//        menuItemC.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//			}
//			public void widgetSelected(SelectionEvent arg0) {
//				for (CTabItem tabItem : workFolder.getItems()) {
//	      			  if (((NpsDocumentUI)tabItem).close(false)) {
//	      				tabItem.dispose();
//	      			  }
//				}
//			}
//        });
//
//        new MenuItem(fileMenu, SWT.SEPARATOR);
//
//        MenuItem saveAsItem = new MenuItem (fileMenu, SWT.PUSH);
//      	saveAsItem.setText ("&Save\tCtrl+S");
//      	saveAsItem.setAccelerator(SWT.MOD1 + 'S');
//      	saveAsItem.addSelectionListener(new SelectionAdapter() {
//              public void widgetSelected(SelectionEvent e) {
//            	  try {
//            		  if (workFolder.getSelection()!=null) {
//            			  ((NpsDocumentUI)workFolder.getSelection()).saveFile(false);
//            		  }
//            	  } catch (Exception ex) {
//      				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//      				errDialog.setText("ERROR");
//      				errDialog.setMessage("Runtime Error: " + ex.getMessage());
//      				errDialog.open();
//            	  }
//              }
//      	});
//
//        MenuItem saveAsItem2 = new MenuItem (fileMenu, SWT.PUSH);
//        saveAsItem2.setText ("Save &As...");
//        saveAsItem2.addSelectionListener(new SelectionAdapter() {
//              public void widgetSelected(SelectionEvent e) {
//            	  try {
//            		  if (workFolder.getSelection()!=null) {
//            			  ((NpsDocumentUI)workFolder.getSelection()).saveFile(true);
//            		  }
//            	  } catch (Exception ex) {
//      				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//      				errDialog.setText("ERROR");
//      				errDialog.setMessage("Runtime Error: " + ex.getMessage());
//      				errDialog.open();
//            	  }
//              }
//      	});
//
//        new MenuItem(fileMenu, SWT.SEPARATOR);
//        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
//        exitItem.setText("&Exit");
//        exitItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	shell.close();
//            }
//        });
//        
//        
//        MenuItem cascadeEditMenu = new MenuItem(menuBar, SWT.CASCADE);
//        cascadeEditMenu.setText("&Edit");
//        Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
//        cascadeEditMenu.setMenu(editMenu);
//
//        MenuItem openEditItem = new MenuItem(editMenu, SWT.PUSH);
//        openEditItem.setText("&Open Selection In Editor\tCtrl+E");
//        openEditItem.setAccelerator(SWT.MOD1 + 'E');
//        openEditItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	if (workFolder.getSelection() != null) {
//            		((NpsDocumentUI)workFolder.getSelection()).openEditor(true);
//            	}
//            }
//        });
//
//        MenuItem openEditAllItem = new MenuItem(editMenu, SWT.PUSH);
//        openEditAllItem.setText("O&pen Selection In Multiple Editor");
//        openEditAllItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	if (workFolder.getSelection() != null) {
//            		((NpsDocumentUI)workFolder.getSelection()).openEditor(false);
//            	}
//            }
//        });
//
//        new MenuItem(editMenu, SWT.SEPARATOR);
//
//        MenuItem cutMenuItem = new MenuItem(editMenu, SWT.PUSH);
//        cutMenuItem.setText("Cu&t\tCtrl+X");
//        cutMenuItem.setAccelerator(SWT.MOD1 + 'X');
//        cutMenuItem.setEnabled(false);
//        cutMenuItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	if (workFolder.isFocusControl()) {
//                	if (!databaseTable.isFocusControl()) {
//	            		((NpsDocumentUI)workFolder.getSelection()).cut(clipboard);
//	            	}
//            	}
//            }
//        });
//        
//        MenuItem copyMenuItem = new MenuItem(editMenu, SWT.PUSH);
//        copyMenuItem.setText("&Copy\tCtrl+C");
//        copyMenuItem.setAccelerator(SWT.MOD1 + 'C');
//        copyMenuItem.setEnabled(false);
//        copyMenuItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	if (databaseTable.isFocusControl()) {
////            		copy(clipboard);
//            	} else {
//	            	if (workFolder.getSelection() != null) {
//	            		((NpsDocumentUI)workFolder.getSelection()).copy(clipboard);
//	            	}
//            	}
//            }
//        });
//        
//        MenuItem pasteMenuItem = new MenuItem(editMenu, SWT.PUSH);
//        pasteMenuItem.setText("&Paste\tCtrl+V");
//        pasteMenuItem.setAccelerator(SWT.MOD1 + 'V');
//        pasteMenuItem.setEnabled(false);
//        pasteMenuItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	if (workFolder.getSelection() != null) {
//            		((NpsDocumentUI)workFolder.getSelection()).paste(clipboard);
//            		//((NpsDocumentUI)workFolder.getSelection()).openEditor(false);
//            	} else if (workFolder.getItemCount()==0) {
//            		new NpsDocumentUI(workFolder);
//            		((NpsDocumentUI)workFolder.getSelection()).paste(clipboard);
//            	}
//            }
//        });
//        
//        
//        
//        
//        new MenuItem(editMenu, SWT.SEPARATOR);
//
//        MenuItem removeItem = new MenuItem(editMenu, SWT.PUSH);
//        removeItem.setText("&Delete\tDelete");
//        removeItem.setEnabled(false);
//        removeItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	if (workFolder.getSelection() != null) {
//            		((NpsDocumentUI)workFolder.getSelection()).removeSelectedRecord();
//            	}
//            }
//        });
//
//        
//  	
//        MenuItem cascadeDatabaseMenu = new MenuItem(menuBar, SWT.CASCADE);
//        cascadeDatabaseMenu.setText("&Database");
//
//        Menu databaseMenu = new Menu(shell, SWT.DROP_DOWN);
//        cascadeDatabaseMenu.setMenu(databaseMenu);
//
//        final MenuItem databaseConnect = new MenuItem(databaseMenu, SWT.PUSH);
//        databaseConnect.setText("&Connect...");
//        databaseConnect.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	//open new window and wait
//            	//waiting for Ohsumi-san to create interface, thus going to do default.
//            	dbConnectCommand();
//            }
//        });
//        
//        MenuItem databaseDisconnect = new MenuItem(databaseMenu, SWT.PUSH);
//        databaseDisconnect.setText("&Disconnect");
//        databaseDisconnect.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	try {
//            		//if (NpsRecordSelectorUI.disposeAllChild(false)) {
//            		npsSelect.disconnect();
//            		NpsRecordSelectorUI.dbController.disconnect();
////            		wth.cancelAll();
//            		//}
//            	} catch (Exception exc) {
//            		
//            	}
//            	updateDbStatus(true);
//            }
//        });
//        new MenuItem(databaseMenu, SWT.SEPARATOR);
//        
//        MenuItem selectSchema = new MenuItem(databaseMenu, SWT.PUSH);
//        selectSchema.setText("&Select Schema");
//        selectSchema.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            	new SchemaSelectorUI(shell);
//            }
//        });  
//        
//        MenuItem cascadeWindowMenu = new MenuItem(menuBar, SWT.CASCADE);
//        cascadeWindowMenu.setText("&Window");
//
//        Menu windowMenu = new Menu(shell, SWT.DROP_DOWN);
//        cascadeWindowMenu.setMenu(windowMenu);
//
//        final MenuItem prefeItem = new MenuItem(windowMenu, SWT.PUSH);
//        prefeItem.setText("&Preferences...");
//        prefeItem.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//       		 	IPreferencePage page = new PreferenceUI(); 
//       		 	page.setTitle("Automatic Update");
//       		 	//create a new PreferenceNode that will appear in the Preference window
//       		 	PreferenceNode node = new PreferenceNode("1", page);
//       		 	
//       		 	PreferenceManager pm = new PreferenceManager();
//       		 	pm.addToRoot(node); //add the node in the PreferenceManager
//       		 	
//       		 	Shell shell = new Shell(NpsRecordSelectorUI.shell, SWT.APPLICATION_MODAL);
//       		 	
//             //	instantiate the PreferenceDialog
//       		 	PreferenceDialog pd = new PreferenceDialog(shell, pm); 
//             //	this line is important, it tell's the PreferenceDialog on what store to used
//       		 	pd.setPreferenceStore(PlatformUI.getPreferenceStore());
//       		 	pd.create();
//       		 	pd.open();
//       		 	
//       		 	ServiceLocator sL = new ServiceLocator();
//       			IHandlerService handlerService = (IHandlerService)sL.getService(IHandlerService.class);
//       			try {
//       				handlerService.executeCommand("org.eclipse.ui.window.preferences", null);
//       				} catch (Exception ex) {
//       					throw new RuntimeException("command not found");
//       					// Give message
//       				}
//       		 	
//            }
//        });
//
//        
//        
//        
//
//        shell.setMenuBar(menuBar);
//        
//        
//        
//        
//        
//        
//        
//		Group searchPane = new Group(shell, SWT.NONE);
//		FormLayout navigationLayout = new FormLayout();
//		navigationLayout.marginWidth = 1;
//		navigationLayout.marginHeight = 0;
//		searchPane.setLayout(navigationLayout);
//
//        FormData navigationData = new FormData();
//        navigationData.left = new FormAttachment(0);
//        navigationData.right = new FormAttachment(0, 150);
//        navigationData.top =  new FormAttachment(0, -6);
//        searchPane.setLayoutData(navigationData);
//        //searchPane.setText("Search");
//
//        //textbox
//        Label labelSourceSystemId = new Label(searchPane, SWT.LEFT);
//        FormData labelSourceSystemIdForm = new FormData();
//        labelSourceSystemIdForm.left = new FormAttachment(0);
//        labelSourceSystemIdForm.top = new FormAttachment(0, -2);
//        labelSourceSystemId.setLayoutData(labelSourceSystemIdForm);
//        labelSourceSystemId.setText("&SSID");
//        
//        sourceSystemIdSearch = new Combo(searchPane, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
//        sourceSystemIdSearch.add("");
//        sourceSystemIdSearch.add("02");
//        sourceSystemIdSearch.add("03");
//        sourceSystemIdSearch.add("04");
//        sourceSystemIdSearch.add("05");
//        sourceSystemIdSearch.add("0A");
//        sourceSystemIdSearch.add("0G");
//        sourceSystemIdSearch.add("0U");
//        sourceSystemIdSearch.add("0W");
//        sourceSystemIdSearch.add("0X");
//        sourceSystemIdSearch.add("0Y");
//       	sourceSystemIdSearch.add("TS");	
//        
//        sourceSystemIdSearch.addModifyListener(textListen);
//
//        FormData sourceSystemData = new FormData();
//        sourceSystemData.left = new FormAttachment(0);
//        sourceSystemData.top = new FormAttachment(labelSourceSystemId,1);
//        sourceSystemIdSearch.setLayoutData(sourceSystemData);
//        sourceSystemIdSearch.addSelectionListener(sil);
//        
//        //textbox
//        Label labelPolicy = new Label(searchPane, SWT.LEFT);
//        FormData labelPolicyData = new FormData();
//        labelPolicyData.left = new FormAttachment(0);
//        labelPolicyData.top = new FormAttachment(sourceSystemIdSearch, 3);
//        labelPolicy.setLayoutData(labelPolicyData);
//        labelPolicy.setText("Jp&PolicyNo");
//
//        policySearch = new Text(searchPane, SWT.BORDER);
//        policySearch.addModifyListener(textListen);
//        FormData policyData = new FormData();
//        policyData.left = new FormAttachment(0);
//        policyData.top = new FormAttachment(labelPolicy, 1);
//        policyData.right = new FormAttachment(100);
//        policySearch.setLayoutData(policyData);
////        policySearch.addSelectionListener(sil);
//        
//        //SeqNo search box
//        Label labelSeqNo = new Label(searchPane, SWT.LEFT);
//        FormData labelSeqNoForm = new FormData();
//        labelSeqNoForm.left = new FormAttachment(0);
//        labelSeqNoForm.top = new FormAttachment(policySearch, 3);
//        labelSeqNo.setLayoutData(labelSeqNoForm);
//        labelSeqNo.setText("&SeqNo");
//
//        seqNoSearch = new Text(searchPane, SWT.BORDER);
//        seqNoSearch.addModifyListener(textListen);
//        FormData seqNoData = new FormData();
//        seqNoData.left = new FormAttachment(0);
//        seqNoData.top = new FormAttachment(labelSeqNo, 1);
//        seqNoData.right = new FormAttachment(100);
//        seqNoSearch.setLayoutData(seqNoData);
//        seqNoSearch.addSelectionListener(sil);
//        
//        //SeqSub search box
//        Label labelSeqSub = new Label(searchPane, SWT.LEFT);
//        FormData labelSeqSubForm = new FormData();
//        labelSeqSubForm.left = new FormAttachment(0);
//        labelSeqSubForm.top = new FormAttachment(seqNoSearch, 3);
//        labelSeqSub.setLayoutData(labelSeqSubForm);
//        labelSeqSub.setText("S&eqSub");
//
//        seqSubSearch = new Text(searchPane, SWT.BORDER);
//        seqSubSearch.addModifyListener(textListen);
//        FormData seqSubData = new FormData();
//        seqSubData.left = new FormAttachment(0);
//        seqSubData.right = new FormAttachment(100);
//        seqSubData.top = new FormAttachment(labelSeqSub, 1);
//        seqSubSearch.setLayoutData(seqSubData);
//        seqSubSearch.addSelectionListener(sil);
//        
//        FormData buttonData = new FormData();
//        buttonData.left = new FormAttachment(0);
//        buttonData.top =  new FormAttachment(seqSubSearch, 3);
//        buttonData.right = new FormAttachment(100);
//        doSearchButton = new Button(searchPane, SWT.PUSH);
//    	doSearchButton.setText("Search");
//        doSearchButton.addSelectionListener(sil);
//        doSearchButton.setEnabled(true);
//        doSearchButton.setLayoutData(buttonData);
//        
//        FormData activeSearchData = new FormData();
//        activeSearchButton = new Button(searchPane, SWT.CHECK);
//        activeSearchButton.setText("&Active Search");
//        activeSearchButton.setSelection(false);
//        activeSearchButton.setEnabled(true);
//        activeSearchData.left = new FormAttachment(0);
//        activeSearchData.top = new FormAttachment(doSearchButton, 3);
//        activeSearchData.right = new FormAttachment(100);
//        activeSearchButton.setLayoutData(activeSearchData);
//        activeSearchButton.addSelectionListener(sil);
//        
//		Group otherOptionPane = new Group(shell, SWT.NONE);
//		FormLayout otherOptionLayout = new FormLayout();
//		otherOptionLayout.marginWidth = 1;
//		otherOptionLayout.marginHeight = 0;
//		otherOptionPane.setLayout(otherOptionLayout);
//
//		FormData otherOptionData = new FormData();
//        otherOptionData.left = new FormAttachment(0);
//        otherOptionData.right = new FormAttachment(0, 150);
//        otherOptionData.top =  new FormAttachment(searchPane, -3);
//        otherOptionData.bottom = new FormAttachment(100, -20);
//        otherOptionPane.setLayoutData(otherOptionData);
//
//        FormData openXMLData = new FormData();
//        openXMLData.left = new FormAttachment(0);
//        openXMLData.right = new FormAttachment(100);
//        openXMLData.top =  new FormAttachment(0, -3);
//        Button openXMLButton = new Button(otherOptionPane, SWT.PUSH);
//    	openXMLButton.setText("Open File...");
//        openXMLButton.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {}
//			public void widgetSelected(SelectionEvent arg0) {
//            	try {
//            		fileLoader.openXMLFile(workFolder);
//            	} catch (Exception exc) {
//            		System.out.println("problems...?");
//            	}
//			}
//        });      
//        openXMLButton.setEnabled(true);
//        openXMLButton.setLayoutData(openXMLData);
//
//        FormData refreshData = new FormData();
//        refreshData.left = new FormAttachment(0);
//        refreshData.right = new FormAttachment(100);
//        refreshData.top =  new FormAttachment(openXMLButton, 2);
//        Button refreshButton = new Button(otherOptionPane, SWT.PUSH);
//    	refreshButton.setText("Refresh All");
//        refreshButton.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {}
//			public void widgetSelected(SelectionEvent arg0) {
//				refresh();
//			}
//        });
//        refreshButton.setEnabled(true);
//        refreshButton.setLayoutData(refreshData);
//
//        
//        FormData uploadData = new FormData();
//        uploadData.left = new FormAttachment(0);
//        uploadData.right = new FormAttachment(100);
//        uploadData.bottom = new FormAttachment(100);
//        uploadButton = new Button(otherOptionPane, SWT.PUSH);
//    	uploadButton.setText("Save Changes to DB...");
//        uploadButton.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {}
//			public void widgetSelected(SelectionEvent arg0) {
//				if (workFolder.getSelection() != null) {
//					((NpsDocumentUI)workFolder.getSelection()).uploadToDatabase();	
//				}
//			}
//        });
//        uploadButton.setEnabled(true);
//        uploadButton.setLayoutData(uploadData);
//
//        status = new Label(shell, SWT.NULL);
//        status.setText("Ready");
//        FormData labelData = new FormData();
//        labelData.left = new FormAttachment(0, 3);
//        labelData.right = new FormAttachment(100, -553);
//        labelData.top =  new FormAttachment(100, -15);
//        labelData.bottom = new FormAttachment(100, -2);
//        status.setLayoutData(labelData);
//
//		Label statusBar = new Label(shell, SWT.BORDER);
//		FormData statusBarData = new FormData();
//		statusBarData.left = new FormAttachment(0);
//		statusBarData.right = new FormAttachment(100, -550);
//		statusBarData.top =  new FormAttachment(100, -18);
//		statusBarData.bottom = new FormAttachment(100);
//		statusBar.setLayoutData(statusBarData);
//
//		
//        dbStatus = new Label(shell, SWT.NULL);
//        FormData labelData2 = new FormData();
//        labelData2.left = new FormAttachment(statusBar,5);
//        labelData2.width = 157;
//        labelData2.top =  new FormAttachment(100, -15);
//        labelData2.bottom = new FormAttachment(100, -2);
//        dbStatus.setLayoutData(labelData2);
//
//        FormData statusBarData2 = new FormData();
//        statusBarData2 = new FormData();
//        statusBarData2.left = new FormAttachment(statusBar, 2);
//        statusBarData2.width = 160;
//        statusBarData2.top =  new FormAttachment(100, -18);
//        statusBarData2.bottom = new FormAttachment(100);
//        Label statusBar2 = new Label(shell, SWT.BORDER);
//		statusBar2.setLayoutData(statusBarData2);
//
//		dbSchema = new Label(shell, SWT.NULL);
//        FormData schemaData = new FormData();
//        schemaData.left = new FormAttachment(statusBar2, 5);
//        schemaData.width = 217;
//        schemaData.top =  new FormAttachment(100, -15);
//        schemaData.bottom = new FormAttachment(100, -2);
//        dbSchema.setLayoutData(schemaData);
//
//        FormData schemaBgData = new FormData();
//        schemaBgData = new FormData();
//        schemaBgData.left = new FormAttachment(statusBar2, 2);
//        schemaBgData.width = 220;
//        schemaBgData.top =  new FormAttachment(100, -18);
//        schemaBgData.bottom = new FormAttachment(100);
//        Label schemaBgLabel = new Label(shell, SWT.BORDER);
//        schemaBgLabel.setLayoutData(schemaBgData);
//
//        //add progress bar to status bar
//        FormData progressBarForm = new FormData();
//        progressBarForm.left = new FormAttachment(schemaBgLabel, 2);
//        progressBarForm.right = new FormAttachment(100);
//        progressBarForm.top =  new FormAttachment(100, -18);
//        progressBarForm.bottom = new FormAttachment(100);
//        progressBar = new ProgressBar(shell, SWT.HORIZONTAL|SWT.INDETERMINATE);
//        progressBar.setVisible(false);
//        progressBar.setLayoutData(progressBarForm);
//        
//        //add invisible progress bar so that when it is stopped
//		progressBar2 = new Label(shell, SWT.BORDER);
//		FormData progressBarData2 = new FormData();
//		progressBarData2.left = new FormAttachment(schemaBgLabel, 2);
//		progressBarData2.right = new FormAttachment(100);
//		progressBarData2.top =  new FormAttachment(100, -18);
//		progressBarData2.bottom = new FormAttachment(100);
//		progressBar2.setLayoutData(progressBarData2);
//		
//        FormData rightSideData = new FormData();
//        rightSideData.left = new FormAttachment(searchPane, 2);
//        rightSideData.top = new FormAttachment(0);
//        rightSideData.right = new FormAttachment(100);
//        rightSideData.bottom = new FormAttachment(100, -20);
//        
//        final Composite rightSideComposite = new Composite(shell, SWT.NONE);
//		FormLayout rightSideLayout = new FormLayout();
//		rightSideLayout.marginWidth = 0;
//		rightSideLayout.marginHeight = 0;
//		rightSideComposite.setLayout(rightSideLayout);
//		rightSideComposite.setLayoutData(rightSideData);
//
//	    final Sash sash = new Sash(rightSideComposite, SWT.HORIZONTAL);
//	    FormData data = new FormData();
//	    data.top = new FormAttachment(0, 200); // Attach to top
//	    data.left = new FormAttachment(0); // Attach halfway across
//	    data.right = new FormAttachment(100); // Attach halfway across
//	    sash.setLayoutData(data);
//	    sash.addSelectionListener(new SelectionAdapter() {
//	        public void widgetSelected(SelectionEvent event) {
//	        	if (event.detail == SWT.DRAG) {
//		        	int limit = 100;
//		        	int height = sash.getParent().getBounds().height;
//		        	int maxLimit = (height - limit);
//		        	if (event.y < limit) {
//	        			event.y=limit;
//		        	} else if (event.y > maxLimit) {
//		        		event.y = maxLimit;
//		        	}
//	        	} else {
//	        		((FormData) sash.getLayoutData()).top = new FormAttachment(0, event.y);
//	        		sash.getParent().layout();
//	        	}
//	        }
//	      });
//	    
//	    rightSideComposite.addControlListener(new ControlListener() {
//			public void controlMoved(ControlEvent arg0) {
//			}
//			public void controlResized(ControlEvent arg0) {
//				int height = sash.getParent().getBounds().height;
//				if (height > 0) {
//					int yLoc = sash.getBounds().y;
//					if (yLoc > height - 100) {
//						yLoc = height - 100;
//		        		((FormData) sash.getLayoutData()).top = new FormAttachment(0, yLoc);
//		        		sash.getParent().layout();
//					}
//				}
//			}
//	    });
//
//	    
//        FormData listData = new FormData();
//        listData.left = new FormAttachment(0);
//        listData.top = new FormAttachment(0);
//        listData.right = new FormAttachment(100);
//        listData.bottom = new FormAttachment(sash, 0);
//        
//        Composite databaseComposite = new Composite(rightSideComposite, SWT.NONE);
//		FormLayout dbCompLayout = new FormLayout();
//		dbCompLayout.marginWidth = 0;
//		dbCompLayout.marginHeight = 0;
//		databaseComposite.setLayout(dbCompLayout);
//        databaseComposite.setLayoutData(listData);
//
//        databaseBoardLabel = new Label(databaseComposite, SWT.CENTER);
//        FormData workboardForm = new FormData();
//        workboardForm.left = new FormAttachment(50, -100);
//        workboardForm.top = new FormAttachment(50, -10);
//        workboardForm.width = 200;
//        databaseBoardLabel.setLayoutData(workboardForm);
//        databaseBoardLabel.setBackground(Colors.normalBgColor);
//        databaseBoardLabel.setText("Database Records");
//        FontData[] fontData = databaseBoardLabel.getFont().getFontData();
//        fontData[0].setStyle(SWT.ITALIC);
//        fontData[0].setHeight(11);
//        databaseBoardLabel.setFont(new Font(display, fontData[0]));
//        databaseBoardLabel.setEnabled(false);
//
//        FormData dbData = new FormData();
//        dbData.left = new FormAttachment(0);
//        dbData.top = new FormAttachment(0);
//        dbData.right = new FormAttachment(100);
//        dbData.bottom = new FormAttachment(100);
//        databaseTable = new Table(databaseComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
//        //activeTable = databaseTable;
//        databaseTable.setHeaderVisible(true);
//        databaseTable.setLayoutData(dbData);
//
//        
//        final TableColumn column1 = new TableColumn(databaseTable, SWT.NONE);
//        column1.setWidth(50);
//        final TableColumn column2 = new TableColumn(databaseTable, SWT.NONE);
//        column2.setWidth(50);
//        final TableColumn column3 = new TableColumn(databaseTable, SWT.NONE);
//        column3.setWidth(100);
//        final TableColumn column4 = new TableColumn(databaseTable, SWT.NONE);
//        column4.setWidth(100);
//        final TableColumn column5 = new TableColumn(databaseTable, SWT.NONE);
//        column5.setWidth(100);
//        
//        final TableColumn column6 = new TableColumn(databaseTable, SWT.NONE);
//        column6.setWidth(100);
//        final TableColumn column7 = new TableColumn(databaseTable, SWT.NONE);
//        column7.setWidth(100);
//        final TableColumn column8 = new TableColumn(databaseTable, SWT.NONE);
//        column8.setWidth(100);
//        final TableColumn column9 = new TableColumn(databaseTable, SWT.NONE);
//        column9.setWidth(100);
//        final TableColumn column10 = new TableColumn(databaseTable, SWT.NONE);
//        column10.setWidth(100);
//        final TableColumn column11 = new TableColumn(databaseTable, SWT.NONE);
//        column11.setWidth(100);
//        final TableColumn column12 = new TableColumn(databaseTable, SWT.NONE);
//        column12.setWidth(100);
//        final TableColumn column13 = new TableColumn(databaseTable, SWT.NONE);
//        column13.setWidth(100);
//        final TableColumn column14 = new TableColumn(databaseTable, SWT.NONE);
//        column14.setWidth(100);
//        final TableColumn column15 = new TableColumn(databaseTable, SWT.NONE);
//        column15.setWidth(100);
//        final TableColumn column16 = new TableColumn(databaseTable, SWT.NONE);
//        column16.setWidth(100);
//        final TableColumn column17 = new TableColumn(databaseTable, SWT.NONE);
//        column17.setWidth(100);
//        final TableColumn column18 = new TableColumn(databaseTable, SWT.NONE);
//        column18.setWidth(100);
//
//        column1.setText("SSID");
//        column2.setText("SUB_SSID");
//        column3.setText("POLICY_NO");
//        column4.setText("SEQ_NO");
//        column5.setText("SEQ_SUB");
//        column6.setText("ITEM_NO");
//        column7.setText("ENDS_EFF_DATE");
//        column8.setText("ENDS_NO");
//        column9.setText("TEISEI_NO");
//        column10.setText("DATA_KIND");
//        column11.setText("I3_FLAG");
//        column12.setText("I4_FLAG");
//        column13.setText("CREATE_DATE");
//        column14.setText("CREATE_TIME");
//        column15.setText("PROCESS_DATE");
//        column16.setText("PROCESS_TIME");
//        column17.setText("STATUS");
//        column18.setText("ITERATION_KIND");
//        
//        databaseTable.addListener(SWT.SetData, new Listener() {
//            public void handleEvent(Event e) {
//              TableItem item = (TableItem) e.item;
//              int index = databaseTable.indexOf(item);
//              NpsRecord record = null;
//              try {
//        		  if (npsRecords.size()>0 && npsRecords.size()>=index) {
//	            	  record = npsRecords.get(index);
//		              String[] datum = new String[]{
//		            		  record.getSystemId(), 
//		            		  record.getSubSystemId(), 
//		            		  record.getPolicyNo(), 
//		            		  record.getSeqNo(), 
//		            		  record.getSeqSub(),
//		            		  record.getItemNo(),
//		            		  record.getEndsEffDate(),
//		            		  record.getEndsNo(),
//		            		  record.getTeiseiNo(),
//		            		  record.getDataKind(),
//		            		  record.getI3Flag(),
//		            		  record.getI4Flag(),
//		            		  record.getCreateDate(),
//		            		  record.getCreateTime(),
//		            		  record.getProcessDate(),
//		            		  record.getProcessTime(),
//		            		  record.getStatus(),
//		            		  record.getIterationKind()
//		            		  };
//		              item.setText(datum);
//		              item.setData(record);
//            	  }
//              } catch (ArrayIndexOutOfBoundsException aoe) {
//            	  //this happens occasionally as the vector may be cleared prior to output to screen.
//            	  //System.out.println("warning, vector cleared.");
//              }
//            }
//          });
//        
//
//        Menu tableMenu = new Menu (databaseTable);
//        //tableMenu.setData(databaseTable);
//      	MenuItem menuTableItem1 = new MenuItem (tableMenu, SWT.PUSH);
//      	menuTableItem1.setText ("&Add To Current Document");
//      	menuTableItem1.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {}
//			public void widgetSelected(SelectionEvent arg0) {
//            	TableItem[] items = databaseTable.getSelection();
//            	ArrayList<NpsRecord> npsRecordArray = new ArrayList<NpsRecord>();
//            	for (TableItem item : items) {
//            		NpsRecord record = (NpsRecord)item.getData();
//            		if (record==null) {
//            			record = npsRecords.get(databaseTable.indexOf(item));
//            		}
//            		npsRecordArray.add(record.clone());
//            	}
//        		if (workFolder.getSelection()==null) {
//    				new NpsDocumentUI(workFolder);
//        		}
//        		if (((NpsDocumentUI)workFolder.getSelection()).overwriteUI(npsRecordArray)) {
//        			LoadingDialog loadingLog = new LoadingDialog(shell, "Adding records to document...");
//        			loadingLog.open();
//    	            NpsRecordLoaderThread dbth = new NpsRecordLoaderThread((NpsDocumentUI)workFolder.getSelection(), npsRecordArray, loadingLog, false);
//    	            dbth.start();
//
//    	            while (dbth.running()) {
//    	                if (!Display.getCurrent().readAndDispatch()) {
//    	                	Display.getCurrent().sleep();
//    	                }
//    	            }
//        			((NpsDocumentUI)workFolder.getSelection()).refreshSchemaChange(npsRecordArray);
//                    loadingLog.dispose();
//
//        		}
//			}
//      	});
//
//      	MenuItem menuTableItem2 = new MenuItem (tableMenu, SWT.PUSH);
//      	menuTableItem2.setText ("Add To &New Document");
//      	menuTableItem2.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {}
//			public void widgetSelected(SelectionEvent arg0) {
//            	TableItem[] items = databaseTable.getSelection();
//				NpsDocumentUI doc = new NpsDocumentUI(workFolder);
//				ArrayList<NpsRecord> npsRecordArray = new ArrayList<NpsRecord>();
//            	for (TableItem item : items) {
//            		NpsRecord record = (NpsRecord)item.getData();
//            		if (record==null) {
//            			record = npsRecords.get(databaseTable.indexOf(item));
//            		}
//            		npsRecordArray.add(record.clone());
//            	}
//    			LoadingDialog loadingLog = new LoadingDialog(shell, "Adding records to document...");
//    			loadingLog.open();
//	            NpsRecordLoaderThread dbth = new NpsRecordLoaderThread(doc, npsRecordArray, loadingLog, false);
//	            dbth.start();
//
//	            while (dbth.running()) {
//	                if (!Display.getCurrent().readAndDispatch()) {
//	                	Display.getCurrent().sleep();
//	                }
//	            }
//            	doc.refreshSchemaChange(npsRecordArray);
//                loadingLog.dispose();
//
//			}
//      	});
//
////      	new MenuItem(tableMenu, SWT.SEPARATOR);
////
////      	MenuItem saveAsXMLMenu = new MenuItem (tableMenu, SWT.PUSH);
////      	saveAsXMLMenu.setText ("&Save As Backup XML File...");
////      	saveAsXMLMenu.addSelectionListener(new SelectionAdapter() {
////      		public void widgetSelected(SelectionEvent e) {
////          	  try {
////        		  ArrayList<NpsRecord> selectedRecords = new ArrayList<NpsRecord>();
////        		  for (TableItem item : databaseTable.getSelection()) {
////        			  selectedRecords.add((NpsRecord)item.getData());
////        		  }
////        		  //FileCreatorUI.createXMLBackup(shell, selectedRecords);
//////        		  for (TableItem item : databaseTable.getSelection()) {
//////        			  setAsLoaded((NpsRecord)item.getData());
//////        		  }
////        	  } catch (Exception ex) {
////  				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
////  				errDialog.setText("ERROR");
////  				errDialog.setMessage("Runtime Error: " + ex.getMessage());
////  				errDialog.open();
////        	  }
////      		}
////      	});
////      	
////      	
////      	MenuItem menuTableItem3 = new MenuItem (tableMenu, SWT.PUSH);
////      	menuTableItem3.setText ("&Extract As Flatfile...");
////      	menuTableItem3.addSelectionListener(new SelectionAdapter() {
////      		public void widgetSelected(SelectionEvent e) {
////          	  try {
////        		  ArrayList<NpsRecord> selectedRecords = new ArrayList<NpsRecord>();
////        		  for (TableItem item : databaseTable.getSelection()) {
////        			  selectedRecords.add((NpsRecord)item.getData());
////        		  }
////        		  FileCreatorUI.createFlatFile(shell, selectedRecords);
////        	  } catch (Exception ex) {
////  				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
////  				errDialog.setText("ERROR");
////  				errDialog.setMessage("Runtime Error: " + ex.getMessage());
////  				errDialog.open();
////        	  }
////      		}
////      	});
//      	databaseTable.setMenu(tableMenu);
//        databaseTable.setSortColumn(column3);
//        databaseTable.setSortDirection(SWT.UP);
//        databaseTable.addSelectionListener(new SelectionListener(){
//        	public void widgetSelected(SelectionEvent e) {}
//            public void widgetDefaultSelected(SelectionEvent e) {
//            	TableItem[] items = databaseTable.getSelection();
//            	ArrayList<NpsRecord> npsRecordArray = new ArrayList<NpsRecord>();
//            	for (TableItem item : items) {
//            		NpsRecord record = (NpsRecord)item.getData();
//            		if (record==null) {
//            			record = npsRecords.get(databaseTable.indexOf(item));
//            		}
//
//            		npsRecordArray.add(record.clone());
//            	}
//        		if (workFolder.getSelection()==null) {
//    				new NpsDocumentUI(workFolder);
//    				//workFolder.setSelection(workFolder.getItemCount()-1);
//        		}
//        		if (((NpsDocumentUI)workFolder.getSelection()).overwriteUI(npsRecordArray)) {
//        			LoadingDialog loadingLog = new LoadingDialog(shell, "Adding records to document...");
//        			loadingLog.open();
//    	            NpsRecordLoaderThread dbth = new NpsRecordLoaderThread((NpsDocumentUI)workFolder.getSelection(), npsRecordArray, loadingLog, false);
//    	            dbth.start();
//    	            while (dbth.running()) {
//    	                if (!Display.getCurrent().readAndDispatch()) {
//    	                	Display.getCurrent().sleep();
//    	                }
//    	            }
//                	((NpsDocumentUI)workFolder.getSelection()).refreshSchemaChange(npsRecordArray);
//                    loadingLog.dispose();
//        		}
//            }
//        });
//        
//        
//        Listener dbWindowListener = new Listener() {
//			public void handleEvent(Event e) { 
//				switch (e.type) { 
//					case SWT.Activate:
//						shell.getMenuBar().getItem(1).getMenu().getItem(0).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(1).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(3).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(4).setEnabled(true);
//						shell.getMenuBar().getItem(1).getMenu().getItem(5).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(7).setEnabled(false);
//						break; 
//					case SWT.Deactivate:
//						shell.getMenuBar().getItem(1).getMenu().getItem(0).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(1).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(3).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(4).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(5).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(7).setEnabled(false);
//						break; 
//				} 
//			} 
//        };
//        databaseTable.addListener(SWT.Activate, dbWindowListener);        
//        databaseTable.addListener(SWT.Deactivate, dbWindowListener);
//
//        databaseTable.setToolTipText("Database records are shown here.");
//
//        SortListener sortListener = new SortListener();
//    	column3.addListener(SWT.Selection, sortListener);
//    	column4.addListener(SWT.Selection, sortListener);
//        column5.addListener(SWT.Selection, sortListener);
//        column1.addListener(SWT.Selection, sortListener);
//        column2.addListener(SWT.Selection, sortListener);
//        column6.addListener(SWT.Selection, sortListener);
//        column7.addListener(SWT.Selection, sortListener);
//        column8.addListener(SWT.Selection, sortListener);
//        column9.addListener(SWT.Selection, sortListener);
//        column10.addListener(SWT.Selection, sortListener);
//        column11.addListener(SWT.Selection, sortListener);
//        column12.addListener(SWT.Selection, sortListener);
//        column13.addListener(SWT.Selection, sortListener);
//        column14.addListener(SWT.Selection, sortListener);
//        column15.addListener(SWT.Selection, sortListener);
//        column16.addListener(SWT.Selection, sortListener);
//        column17.addListener(SWT.Selection, sortListener);
//        column18.addListener(SWT.Selection, sortListener);
//        
//        column3.setMoveable(true);
//        column4.setMoveable(true);
//        column5.setMoveable(true);
//        column1.setMoveable(true);
//        column2.setMoveable(true);
//        column6.setMoveable(true);
//        column7.setMoveable(true);
//        column8.setMoveable(true);
//        column9.setMoveable(true);
//        column10.setMoveable(true);
//        column11.setMoveable(true);
//        column12.setMoveable(true);
//        column13.setMoveable(true);
//        column14.setMoveable(true);
//        column15.setMoveable(true);
//		column16.setMoveable(true);
//        column17.setMoveable(true);
//        column18.setMoveable(true);
//        
//        
//        int operations = DND.DROP_COPY; 
//        DragSource source = new DragSource(databaseTable, operations);
//    	source.setTransfer (new Transfer [] {NpsRecord.getInstance ()});
//        source.addDragListener(new DragSourceListener(){
//			public void dragFinished(DragSourceEvent event) {}
//
//			public void dragSetData(DragSourceEvent event) {
//				if (databaseTable.getSelectionCount()> 0) {
//					NpsRecord[] dataArray = new NpsRecord[databaseTable.getSelectionCount()];
//					for (int i=0; i < databaseTable.getSelectionCount(); i++) {
//						dataArray[i] = (NpsRecord)databaseTable.getSelection()[i].getData();
//					}
//					event.data = dataArray;
//				}
//			}
//			public void dragStart(DragSourceEvent event) {
//				if (databaseTable.getSelectionCount()==0) {
//					event.doit=false;
//				}
//			}
//        });
//
//        
////        FormData workboardData = new FormData();
////        workboardData.left = new FormAttachment(0);
////        workboardData.top = new FormAttachment(sash, 0);
////        workboardData.right = new FormAttachment(100);
////        workboardData.bottom = new FormAttachment(100);
////        
////        Composite workboardComposite = new Composite(rightSideComposite, SWT.NONE);
////		FormLayout workboardLayout = new FormLayout();
////		workboardLayout.marginWidth = 0;
////		workboardLayout.marginHeight = 0;
////		workboardComposite.setLayout(workboardLayout);
////        workboardComposite.setLayoutData(workboardData);
////
////        workboardLabel = new Label(workboardComposite, SWT.CENTER);
////        FormData workboardFormData = new FormData();
////        workboardFormData.left = new FormAttachment(50, -100);
////        workboardFormData.top = new FormAttachment(50, -10);
////        workboardFormData.width = 200;
////        workboardLabel.setLayoutData(workboardFormData);
////        workboardLabel.setBackground(normalBgColor);
////        workboardLabel.setText("Temporary Workboard");
////        FontData[] workFontData = workboardLabel.getFont().getFontData();
////        workFontData[0].setStyle(SWT.ITALIC);
////        workFontData[0].setHeight(11);
////        workboardLabel.setFont(new Font(display, workFontData[0]));
////        workboardLabel.setEnabled(false);
//        
//        FormData localListData = new FormData();
//        localListData.left = new FormAttachment(0);
//        localListData.top = new FormAttachment(sash, 0);
//        localListData.right = new FormAttachment(100);
//        localListData.bottom = new FormAttachment(100);
//		workFolder = new CTabFolder(rightSideComposite, SWT.CLOSE | SWT.BOTTOM );
//		workFolder.setUnselectedCloseVisible(true);
//		workFolder.setSimple(false);
//		workFolder.setLayoutData(localListData);
//		workFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
//			public void close(CTabFolderEvent event) {
//				event.doit = ((NpsDocumentUI)event.item).close(false);
//			}
//		});
//
//		
//		new NpsDocumentUI(workFolder);
//		
//		workFolder.setBorderVisible(true);
//		workFolder.setMRUVisible(true);
//		//System.out.println(workFolder.getTabHeight());
//		workFolder.setTabHeight(19);
//		//workFolder.setMaximizeVisible(true);
////		workFolder.setBackground(
////				new Color[]{
////						display.getSystemColor(SWT.COLOR_DARK_BLUE), 
////						display.getSystemColor(SWT.COLOR_BLUE),
////						display.getSystemColor(SWT.COLOR_WHITE), 
////						display.getSystemColor(SWT.COLOR_WHITE)},
////                new int[] {25, 50, 100}, 
////                true);
//		
//		workFolder.setSelectionBackground(
//				new Color[]{
//						new Color (Display.getCurrent () , 255, 230, 190),
//						new Color (Display.getCurrent () , 250, 222, 184),
//						new Color (Display.getCurrent () , 245, 215, 175),
//						new Color (Display.getCurrent () , 240, 210, 170),
//						new Color (Display.getCurrent () , 240, 210, 170),
//						},
//                new int[] {
//						30, 
//						50, 
//						60, 
//						100}, 
//                true);
//		//workFolder.
////		workFolder.setSelection(0);
//		workFolder.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//				//do nothing
//			}
//			public void widgetSelected(SelectionEvent arg0) {
//				((NpsDocumentUI)workFolder.getSelection()).refreshSchemaChange();
//			}
//		});
//
//        Listener docWindowListener = new Listener() {
//			public void handleEvent(Event e) { 
//				switch (e.type) { 
//					case SWT.Activate: 
//						shell.getMenuBar().getItem(1).getMenu().getItem(0).setEnabled(true);
//						shell.getMenuBar().getItem(1).getMenu().getItem(1).setEnabled(true);
//						shell.getMenuBar().getItem(1).getMenu().getItem(3).setEnabled(true);
//						shell.getMenuBar().getItem(1).getMenu().getItem(4).setEnabled(true);
//						shell.getMenuBar().getItem(1).getMenu().getItem(5).setEnabled(true);
//						shell.getMenuBar().getItem(1).getMenu().getItem(7).setEnabled(true);
//						break; 
//					case SWT.Deactivate:
//						shell.getMenuBar().getItem(1).getMenu().getItem(0).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(1).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(3).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(4).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(5).setEnabled(false);
//						shell.getMenuBar().getItem(1).getMenu().getItem(7).setEnabled(false);
//						break; 
//				} 
//			} 
//        };
//        workFolder.addListener(SWT.Activate, docWindowListener);        
//        workFolder.addListener(SWT.Deactivate, docWindowListener);
//
//        
//        
//    	DropTarget target = new DropTarget (shell, DND.DROP_COPY | DND.DROP_DEFAULT);
//    	target.setTransfer (new Transfer [] {FileTransfer.getInstance ()});
//    	target.addDropListener (new DropTargetAdapter () {
//    		public void dragEnter (DropTargetEvent event) {
//    			if (event.detail == DND.DROP_DEFAULT) {
//    				event.detail = DND.DROP_COPY;
//    			}
//    		}
//
//    		public void dragOperationChanged (DropTargetEvent event) {
//    			if (event.detail == DND.DROP_DEFAULT) {
//    				event.detail = DND.DROP_COPY;
//    			}
//    		}
//
//    		public void drop (DropTargetEvent event) {
//    			if (event.data != null && event.data instanceof String[]) {
//					try {
//						String[] files = (String[])event.data;
//						for (String filename : files) {
//							NpsRecordSelectorUI.currentProgram.getFileLoader().openXMLFile(workFolder, filename);
//						}
//					} catch (Exception e) {
//						Display.getDefault().asyncExec(new ErrorDialog(shell, "Runtime Error: ", e));
//					}
//    			}
//    		}
//    	});
//
//		//workFolder.addListener(SWT.Deactivate, listener); 
//		
//		
////		for (int i = 0; i < 6; i++) {
////			CTabItem item = new CTabItem(workFolder, SWT.NONE);
////			
////			item.setText("Tab Item "+i);
////			Text text = new Text(workFolder, SWT.BORDER | SWT.MULTI);
////			text.setText("Content for Item "+i);
////			item.setControl(text);
////		}
//		
////		workFolder.setSize (400, 200);
//
////        localTable = new Table(workboardComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
////        localTable.setHeaderVisible(true);
////        localTable.setLayoutData(localListData);
////
////        final TableColumn localColumn1 = new TableColumn(localTable, SWT.NONE);
////        localColumn1.setWidth(50);
////        final TableColumn localColumn2 = new TableColumn(localTable, SWT.NONE);
////        localColumn2.setWidth(50);
////        final TableColumn localColumn3 = new TableColumn(localTable, SWT.NONE);
////        localColumn3.setWidth(100);
////        final TableColumn localColumn4 = new TableColumn(localTable, SWT.NONE);
////        localColumn4.setWidth(100);
////        final TableColumn localColumn5 = new TableColumn(localTable, SWT.NONE);
////        localColumn5.setWidth(100);
////        
////        final TableColumn localColumn6 = new TableColumn(localTable, SWT.NONE);
////        localColumn6.setWidth(100);
////        final TableColumn localColumn7 = new TableColumn(localTable, SWT.NONE);
////        localColumn7.setWidth(100);
////        final TableColumn localColumn8 = new TableColumn(localTable, SWT.NONE);
////        localColumn8.setWidth(100);
////        final TableColumn localColumn9 = new TableColumn(localTable, SWT.NONE);
////        localColumn9.setWidth(100);
////        final TableColumn localColumn10 = new TableColumn(localTable, SWT.NONE);
////        localColumn10.setWidth(100);
////        final TableColumn localColumn11 = new TableColumn(localTable, SWT.NONE);
////        localColumn11.setWidth(100);
////        final TableColumn localColumn12 = new TableColumn(localTable, SWT.NONE);
////        localColumn12.setWidth(100);
////        final TableColumn localColumn13 = new TableColumn(localTable, SWT.NONE);
////        localColumn13.setWidth(100);
////        final TableColumn localColumn14 = new TableColumn(localTable, SWT.NONE);
////        localColumn14.setWidth(100);
////        final TableColumn localColumn15 = new TableColumn(localTable, SWT.NONE);
////        localColumn15.setWidth(100);
////        final TableColumn localColumn16 = new TableColumn(localTable, SWT.NONE);
////        localColumn16.setWidth(100);
////        final TableColumn localColumn17 = new TableColumn(localTable, SWT.NONE);
////        localColumn17.setWidth(100);
////        final TableColumn localColumn18 = new TableColumn(localTable, SWT.NONE);
////        localColumn18.setWidth(100);
////
////        localColumn1.setText("SSID");
////        localColumn2.setText("SUB_SSID");
////        localColumn3.setText("POLICY_NO");
////        localColumn4.setText("SEQ_NO");
////        localColumn5.setText("SEQ_SUB");
////        localColumn6.setText("ITEM_NO");
////        localColumn7.setText("ENDS_EFF_DATE");
////        localColumn8.setText("ENDS_NO");
////        localColumn9.setText("TEISEI_NO");
////        localColumn10.setText("DATA_KIND");
////        localColumn11.setText("I3_FLAG");
////        localColumn12.setText("I4_FLAG");
////        localColumn13.setText("CREATE_DATE");
////        localColumn14.setText("CREATE_TIME");
////        localColumn15.setText("PROCESS_DATE");
////        localColumn16.setText("PROCESS_TIME");
////        localColumn17.setText("STATUS");
////        localColumn18.setText("ITERATION_KIND");
////        
////        localTable.addListener(SWT.SetData, new Listener() {
////            public void handleEvent(Event e) {
////              TableItem item = (TableItem) e.item;
////              int index = localTable.indexOf(item);
////              NpsRecord record = null;
////              try {
////        		  if (localSavedRecords.size()>0 && localSavedRecords.size()>=index) {
////	            	  record = localSavedRecords.get(index);
////	            	  if (loadedRecords.contains(record)) {
////	            		  if (record.hasUncommitedChanges()) {
////	            			  item.setBackground(savedChangesColor);
////	            		  } else {
////	            			  item.setBackground(normalBgColor);
////	            		  }
////	            	  } else {
////	            		  item.setBackground(changedColor);
////	            	  }
////		              String[] datum = new String[]{
////		            		  record.getSystemId(), 
////		            		  record.getSubSystemId(), 
////		            		  record.getPolicyNo(), 
////		            		  record.getSeqNo(), 
////		            		  record.getSeqSub(),
////		            		  record.getItemNo(),
////		            		  record.getEndsEffDate(),
////		            		  record.getEndsNo(),
////		            		  record.getTeiseiNo(),
////		            		  record.getDataKind(),
////		            		  record.getI3Flag(),
////		            		  record.getI4Flag(),
////		            		  record.getCreateDate(),
////		            		  record.getCreateTime(),
////		            		  record.getProcessDate(),
////		            		  record.getProcessTime(),
////		            		  record.getStatus(),
////		            		  record.getIterationKind()
////		            		  };
////		              item.setText(datum);
////		              item.setData(record);
////            	  }
////              } catch (ArrayIndexOutOfBoundsException aoe) {
////            	  //this happens occasionally as the vector may be cleared prior to output to screen.
////            	  //System.out.println("warning, vector cleared.");
////              }
////            }
////          });
////        
////
////        Menu locaTableMenu = new Menu (localTable);
////        //locaTableMenu.setData(localTable);
////      	MenuItem locaMenuTableItem1 = new MenuItem (locaTableMenu, SWT.PUSH);
////      	locaMenuTableItem1.setText ("&Open Selection In Editor");
////      	locaMenuTableItem1.addSelectionListener(tableEditorMenuListener);
////
////      	MenuItem localmenuTableItem2 = new MenuItem (locaTableMenu, SWT.PUSH);
////      	localmenuTableItem2.setText ("O&pen Selection In Multiple Editors");
////      	localmenuTableItem2.addSelectionListener(tableEditorMenuListener);
////
////      	new MenuItem(locaTableMenu, SWT.SEPARATOR);
////
////      	MenuItem localsaveAsXMLMenu = new MenuItem (locaTableMenu, SWT.PUSH);
////      	localsaveAsXMLMenu.setText ("&Save As Backup XML File...");
////      	localsaveAsXMLMenu.addSelectionListener(new SelectionAdapter() {
////      		public void widgetSelected(SelectionEvent e) {
////          	  try {
////        		  ArrayList<NpsRecord> selectedRecords = new ArrayList<NpsRecord>();
////        		  for (TableItem item : localTable.getSelection()) {
////        			  selectedRecords.add((NpsRecord)item.getData());
////        		  }
////        		  FileCreatorUI.createXMLBackup(shell, selectedRecords);
////        		  for (TableItem item : localTable.getSelection()) {
////        			  setAsLoaded((NpsRecord)item.getData());
////        		  }
////        	  } catch (Exception ex) {
////  				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
////  				errDialog.setText("ERROR");
////  				errDialog.setMessage("Runtime Error: " + ex.getMessage());
////  				errDialog.open();
////        	  }
////      		}
////      	});
////      	
////      	
////      	MenuItem localmenuTableItem3 = new MenuItem (locaTableMenu, SWT.PUSH);
////      	localmenuTableItem3.setText ("&Extract As Flatfile...");
////      	localmenuTableItem3.addSelectionListener(new SelectionAdapter() {
////      		public void widgetSelected(SelectionEvent e) {
////          	  try {
////        		  ArrayList<NpsRecord> selectedRecords = new ArrayList<NpsRecord>();
////        		  for (TableItem item : localTable.getSelection()) {
////        			  selectedRecords.add((NpsRecord)item.getData());
////        		  }
////        		  FileCreatorUI.createFlatFile(shell, selectedRecords);
////        	  } catch (Exception ex) {
////  				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
////  				errDialog.setText("ERROR");
////  				errDialog.setMessage("Runtime Error: " + ex.getMessage());
////  				errDialog.open();
////        	  }
////      		}
////      	});
////      	localTable.setMenu(locaTableMenu);
////      	localTable.setSortColumn(localColumn3);
////      	localTable.setSortDirection(SWT.UP);
////        localTable.addSelectionListener(tableListener);
////        localTable.addFocusListener(focusTableListener);
////        
////        SortListener localsortListener = new SortListener();
////    	localColumn3.addListener(SWT.Selection, localsortListener);
////    	localColumn4.addListener(SWT.Selection, localsortListener);
////        localColumn5.addListener(SWT.Selection, localsortListener);
////        localColumn1.addListener(SWT.Selection, localsortListener);
////        localColumn2.addListener(SWT.Selection, localsortListener);
////        localColumn6.addListener(SWT.Selection, localsortListener);
////        localColumn7.addListener(SWT.Selection, localsortListener);
////        localColumn8.addListener(SWT.Selection, localsortListener);
////        localColumn9.addListener(SWT.Selection, localsortListener);
////        localColumn10.addListener(SWT.Selection, localsortListener);
////        localColumn11.addListener(SWT.Selection, localsortListener);
////        localColumn12.addListener(SWT.Selection, localsortListener);
////        localColumn13.addListener(SWT.Selection, localsortListener);
////        localColumn14.addListener(SWT.Selection, localsortListener);
////        localColumn15.addListener(SWT.Selection, localsortListener);
////        localColumn16.addListener(SWT.Selection, localsortListener);
////        localColumn17.addListener(SWT.Selection, localsortListener);
////        localColumn18.addListener(SWT.Selection, localsortListener);
////        
////        localColumn3.setMoveable(true);
////        localColumn4.setMoveable(true);
////        localColumn5.setMoveable(true);
////        localColumn1.setMoveable(true);
////        localColumn2.setMoveable(true);
////        localColumn6.setMoveable(true);
////        localColumn7.setMoveable(true);
////        localColumn8.setMoveable(true);
////        localColumn9.setMoveable(true);
////        localColumn10.setMoveable(true);
////        localColumn11.setMoveable(true);
////        localColumn12.setMoveable(true);
////        localColumn13.setMoveable(true);
////        localColumn14.setMoveable(true);
////        localColumn15.setMoveable(true);
////		localColumn16.setMoveable(true);
////        localColumn17.setMoveable(true);
////        localColumn18.setMoveable(true);
////        
//        
//        updateDbStatus(true);
////    	wth.start();
////
////        RecordMonitorThread rmt = new RecordMonitorThread();
////        rmt.start();
//        
//        
//    }
//
//    public void dbConnectCommand() {
//    	String previousURL = "dummy";
//    	String newURL = "newDummy";
//    	
////    	if (previousURL==null || !previousURL.equals(newURL)) {
////    		if (!NpsRecordSelectorUI.disposeAllChild(false)) {
////    			return;
////    		}
////    	}
//    	try {
//    		String URL="jdbc:oracle:thin:@10.16.209.149:1521:IMPT1";
//    		String USER="isys66e";
//    		String PASS="emychen6";
//    		//String USER = "JODSDEV4";
//    		//String PASS = "JODSDEV4";
//    		NpsRecordSelectorUI.dbController.openConnection(URL, USER, PASS);
//    	} catch (Exception exc) {
//    		
//    	}
//    	if (previousURL==null || !previousURL.equals(newURL)) {
//    		new SchemaSelectorUI(shell);
//        	updateDbStatus(true);
//    	}
////    	} else {
////    		updateEditMenu();
////    	}
//    }
//
////    private void updateEditMenu() {
////    	if (!shell.isDisposed()) {
////	    	if (childShells.size() > 0) {
//////	    		shell.getMenuBar().getItem(0).getMenu().getItem(7).setEnabled(true);
////	    	} else {
//////	    		shell.getMenuBar().getItem(0).getMenu().getItem(7).setEnabled(false);
////	    	}
////    	}
////    	
////		//boolean multipleLayout = multipleMetaDataSelection();
////    	if (databaseTable.getSelection().length==1) {
////    		//item0 = open in single editor
////    		//item1 = open in multiple editor
////    		//item2 = bar
////    		//item3 = extract to file
//////    		databaseTable.getMenu().getItem(0).setEnabled(true);
//////    		databaseTable.getMenu().getItem(1).setEnabled(false);
//////    		databaseTable.getMenu().getItem(3).setEnabled(true);
//////    		databaseTable.getMenu().getItem(4).setEnabled(true);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(1).setEnabled(true);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(2).setEnabled(false);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(4).setEnabled(true);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(5).setEnabled(true);
////    	} else if (databaseTable.getSelection().length > 1){
//////    		if (multipleLayout) {
//////        		databaseTable.getMenu().getItem(0).setEnabled(false);
//////        		databaseTable.getMenu().getItem(1).setEnabled(true);
//////        		databaseTable.getMenu().getItem(3).setEnabled(false);
//////        		databaseTable.getMenu().getItem(4).setEnabled(false);
//////        		shell.getMenuBar().getItem(0).getMenu().getItem(1).setEnabled(false);
//////        		shell.getMenuBar().getItem(0).getMenu().getItem(2).setEnabled(true);
//////        		shell.getMenuBar().getItem(0).getMenu().getItem(4).setEnabled(false);
//////        		shell.getMenuBar().getItem(0).getMenu().getItem(5).setEnabled(false);
//////    		} else {
//////	    		databaseTable.getMenu().getItem(0).setEnabled(true);
//////	    		databaseTable.getMenu().getItem(1).setEnabled(true);
//////	    		databaseTable.getMenu().getItem(3).setEnabled(true);
//////	    		databaseTable.getMenu().getItem(4).setEnabled(true);
//////	    		shell.getMenuBar().getItem(0).getMenu().getItem(1).setEnabled(true);
//////	    		shell.getMenuBar().getItem(0).getMenu().getItem(2).setEnabled(true);
//////	    		shell.getMenuBar().getItem(0).getMenu().getItem(4).setEnabled(true);
//////	    		shell.getMenuBar().getItem(0).getMenu().getItem(5).setEnabled(true);
//////    		}
////    	} else {
//////    		databaseTable.getMenu().getItem(0).setEnabled(false);
//////    		databaseTable.getMenu().getItem(1).setEnabled(false);
//////    		databaseTable.getMenu().getItem(3).setEnabled(false);
//////    		databaseTable.getMenu().getItem(4).setEnabled(false);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(1).setEnabled(false);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(2).setEnabled(false);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(4).setEnabled(false);
//////    		shell.getMenuBar().getItem(0).getMenu().getItem(5).setEnabled(false);
////    	}
////    }
//    
////    class OpenEditorMenuListener extends SelectionAdapter {
////        public void widgetSelected(SelectionEvent e) {
////        	MenuItem menItem = ((MenuItem)e.widget);
////        	Menu menu = menItem.getParent();
////        	if (menu.getParentMenu()==null) {
////        	//if (menu.getData() instanceof Table) {
////	        	boolean sameWindow = true;
////	        	if (menItem == menu.getItem(1)) {
////	        		sameWindow = false;
////	        	}
////	        	openEditor(activeTable, sameWindow);
////        	} else {
////        		//have to determine which table is active.
////        		//dummy debug
////	        	boolean sameWindow = true;
////	        	if (menItem == menu.getItem(2)) {
////	        		sameWindow = false;
////	        	}
////        		openEditor(activeTable, sameWindow);
////        	}
////        }
////    }
//    
////    public void openEditor(Table selectedTable, boolean sameWindow) {
////      	ArrayList<NpsRecord> npsRecords = new ArrayList<NpsRecord>();
////      	for (TableItem item : selectedTable.getSelection()) {
////        	if (!sameWindow) {
////        		ArrayList<NpsRecord> npsRecordSingle = new ArrayList<NpsRecord>();
////        		npsRecordSingle.add((NpsRecord)item.getData());
////        		new NpsRecordEditorUI(display, npsRecordSingle);
////        	}
////      		npsRecords.add((NpsRecord)item.getData());
////      	}
////      	if (sameWindow) {
////      		new NpsRecordEditorUI(display, npsRecords);
////      	}
////    }
//    
////    class SelectionListenerTable extends SelectionAdapter {
////    	public void widgetSelected(SelectionEvent e) {
////    		//some items were selected on the table.
////    		//lets set the default menu values depending on what the selection is
////    		updateEditMenu();
////    	}
////    	
////        public void widgetDefaultSelected(SelectionEvent e) {
////        	TableItem[] items = databaseTable.getSelection();
////        	ArrayList<NpsRecord> npsRecordArray = new ArrayList<NpsRecord>();
////        	for (TableItem item : items) {
////        		npsRecordArray.add((NpsRecord)item.getData());
////        	}
////        	try {
////        		if (workFolder.getSelection()==null) {
////    				new NpsDocumentUI(workFolder);
////    				workFolder.setSelection(workFolder.getItemCount()-1);
////        		}
////        		((NpsDocumentUI)workFolder.getSelection()).add(npsRecordArray);
////        	} catch (Exception ex) {
////  				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
////  				errDialog.setText("ERROR");
////  				errDialog.setMessage("Runtime Error: " + ex.getMessage());
////  				errDialog.open();
////        	}
////        }
////    }
//    
////    class FocusTableListener extends FocusAdapter {
////    	public void focusGained(FocusEvent e) {
////        	Table selectedTable = (Table)e.widget;
////        	if (selectedTable == databaseTable) {
////        		activeTable = databaseTable;
////        		localTable.deselectAll();
////        	} else {
////        		activeTable = localTable;
////        		databaseTable.deselectAll();
////        	}
////    		updateEditMenu();
////    	}
////    }
//    
////    private boolean multipleMetaDataSelection() {
////		ArrayList<MetaDataEnum> foundMetaData = new ArrayList<MetaDataEnum>();
////		//ArrayList<String> foundSSID = new ArrayList<String>();
////		for (TableItem item : databaseTable.getSelection()) {
////			if (!foundMetaData.contains(((NpsRecord)item.getData()).getMetaDataEnum()) && foundMetaData.size() > 0){
////				return true;
////			} else {
////				foundMetaData.add(((NpsRecord)item.getData()).getMetaDataEnum());
////			}
////		}
////		return false;
////    }
//    
//    public void queryData() {
//    	databaseTable.deselectAll();
//    	try {
//	    	if (NpsRecordSelectorUI.dbController.getConnection()==null) {
//	    		npsRecords.clear();
//	    	} else if (NpsRecordSelectorUI.dbController.getSchemaRecord()==null) {
//	    		npsRecords.clear();
//	    	} else {
//	    		try {
////	    	    	wth.addToQueue(
////	    	    			sourceSystemIdSearch.getText(),
////	    	    			policySearch.getText(),
////	    	    			seqNoSearch.getText(),
////	    	    			seqSubSearch.getText(),
////	    	    			databaseTable.indexOf(databaseTable.getSortColumn())+1,
////	    	    			databaseTable.getSortDirection()==SWT.UP
////	    	    			);
//		    	} catch (Exception e) {
//					MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//					errDialog.setText("ERROR");
//					errDialog.setMessage("Runtime Error: " + e.getMessage());
//					errDialog.open();
//		    	}
//	    	}
//    	} catch (Exception e) {
//			MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//			errDialog.setText("ERROR");
//			errDialog.setMessage("Runtime Error: " + e.getMessage());
//			errDialog.open();
//    	}
//    }
//
////    public void updateLocalStatus() {
////		//if loadedfromfile, then as soon as connection is made, we need to evaluate the values and determine if changes are there or not
////		//if there are no changes, then discard..
////    	try {
////			if (!(NpsRecordSelectorUI.dbController.getConnection()==null || NpsRecordSelectorUI.dbController.getSchemaRecord()==null)) {
////				XmlDataHandler xmlHandler = new XmlDataHandler();
////				for (NpsRecord npsRecord : localSavedRecords) {
////					xmlHandler.updateInitialValues(npsRecord);
////				}
////			}
////			localTable.clearAll();
////			localTable.setItemCount(localSavedRecords.size());
////    	} catch (Exception e) {
////			MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
////			errDialog.setText("ERROR");
////			errDialog.setMessage("Runtime Error: " + e.getMessage());
////			errDialog.open();
////    	}
////    	updateEditMenu();
////    }
//    
//    public void updateDbStatus(boolean refreshData) {
//    	try {
//	    	if (NpsRecordSelectorUI.dbController.getConnection()==null) {
//	    		dbStatus.setText("Not connected");
//	    		shell.getMenuBar().getItem(2).getMenu().getItem(1).setEnabled(false);
//	    		shell.getMenuBar().getItem(2).getMenu().getItem(3).setEnabled(false);
//	    		uploadButton.setEnabled(false);
//	    		dbSchema.setText("");
//	    		npsRecords.clear();
//	    		needToClear=true;
//	    		databaseTable.setItemCount(npsRecords.size());
//	    	} else {
//	    		dbStatus.setText("Connected: " + NpsRecordSelectorUI.dbController.getUSER());
//	    		shell.getMenuBar().getItem(2).getMenu().getItem(1).setEnabled(true);
//	    		shell.getMenuBar().getItem(2).getMenu().getItem(3).setEnabled(true);
//	    		if (NpsRecordSelectorUI.dbController.getSchemaRecord()==null) {
//	    			dbSchema.setText("Schema: none selected");
//	    			uploadButton.setEnabled(false);
//	    		} else {
//	    			dbSchema.setText("Schema: " + NpsRecordSelectorUI.dbController.getSchemaRecord().getLabelString());
//	    			if (NpsRecordSelectorUI.dbController.isUpdateable()) {
//	    				uploadButton.setEnabled(true);
//	    			} else {
//	    				uploadButton.setEnabled(false);
//	    			}
//	    		}
//	    		if (refreshData) {
//	    			if (activeSearchButton.getSelection()) {
//	    				queryData();
//	    			} else {
//		    			npsRecords.clear();
//	    			}
//    				needToClear=true;
//    				databaseTable.clearAll();
//    				databaseTable.setItemCount(npsRecords.size());
//    				if (workFolder.getSelection()!= null) {
//    					((NpsDocumentUI)workFolder.getSelection()).refreshSchemaChange();
//    				}
//	    		}
//	    	}
//    	} catch (Exception e) {
//			MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//			errDialog.setText("ERROR");
//			errDialog.setMessage("Runtime Error: " + e.getMessage());
//			errDialog.open();
//    	}
////    	updateEditMenu();
//	}
//    
//    class TextInputListener implements ModifyListener{
//        public void modifyText(ModifyEvent event) {
//        	if (activeSearchButton.getSelection()) {
//        		runQuery();
//        	}
//        }
//    }
//
//    public void runQuery() {
//		npsRecords.clear();
//		needToClear=true;
//	    databaseTable.clearAll();
//	    queryData();
//    }
//
//    
//    public void sortRecords(Table selectionTable, TableColumn currentColumn) {
//        TableColumn sortColumn = selectionTable.getSortColumn();
//        int dir = selectionTable.getSortDirection();
//        if (currentColumn != null && sortColumn == currentColumn) {
//          dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
//        } else if (currentColumn==null) {
//        	currentColumn = sortColumn;
//        } else {
//        	selectionTable.setSortColumn(currentColumn);
//        	dir = SWT.UP;
//        }
//        // sort the data based on column and direction
//        final int index = selectionTable.indexOf(currentColumn);
//        //System.out.println(index);
//        final int direction = dir;
//		final String methodName;
//		switch (index) {
//    		case 0: 
//    			methodName = "getSystemId";
//		  		break;
//		  	case 1: 
//		  		methodName = "getSubSystemId";
//		  		break;
//		  	case 2:
//		  		methodName = "getPolicyNo";
//		  		break;
//		  	case 3:
//		  		methodName = "getSeqNo";
//		  		break;
//		  	case 4:
//		  		methodName = "getSeqSub";
//		  		break;
//		  	case 5:
//		  		methodName = "getItemNo";
//		  		break;
//		  	case 6:
//		  		methodName = "getEndsEffDate";
//		  		break;
//		  	case 7:
//		  		methodName = "getEndsNo";
//		  		break;
//		  	case 8:
//		  		methodName = "getTeiseiNo";
//		  		break;
//		  	case 9:
//		  		methodName = "getDataKind";
//		  		break;
//		  	case 10:
//		  		methodName = "getI3Flag";
//		  		break;
//		  	case 11:
//		  		methodName = "getI4Flag";
//		  		break;
//		  	case 12:
//		  		methodName = "getCreateDate";
//		  		break;
//		  	case 13:
//		  		methodName = "getCreateTime";
//		  		break;
//		  	case 14:
//		  		methodName = "getProcessDate";
//		  		break;
//		  	case 15:
//		  		methodName = "getProcessTime";
//		  		break;
//		  	case 16:
//		  		methodName = "getStatus";
//		  		break;
//		  	case 17:
//		  		methodName = "getIterationKind";
//		  		break;
//		  	default: 
//		  		methodName = "getPolicyNo";
//		}
//		//System.out.println(methodName);
//		Vector<NpsRecord> sortVector = null;
//		if (selectionTable == databaseTable) {
//			npsSelect.pause();
//			sortVector = npsRecords;
//		} else {
////			sortVector = localSavedRecords;
//		}
//        Collections.sort(sortVector, new Comparator<NpsRecord>() {
//    		
//        	public int compare(NpsRecord a, NpsRecord b) {
//        		try {
//        			Method method = a.getClass().getMethod(methodName);
//	                if (method.invoke(a).equals(method.invoke(b)))
//	                  return 0;
//	                if (direction == SWT.UP) {
//	                  return ((String)method.invoke(a)).compareTo((String)method.invoke(b));
//	                }
//	                return ((String)method.invoke(b)).compareTo((String)method.invoke(a));
//        		} catch (Exception e) {
//    				MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//    				errDialog.setText("ERROR");
//    				errDialog.setMessage("Runtime Error: " + e.getMessage());
//    				errDialog.open();
//    				return 0;
//        		}
//              }
//        });
//		if (selectionTable == databaseTable) {
//			npsSelect.resume();
//		}
//        // update data displayed in table
//		selectionTable.setSortDirection(dir);
//		selectionTable.clearAll();
//      }
//   
//    public void refresh() {
//    	if (workFolder.getSelection() != null) {
//    		((NpsDocumentUI)workFolder.getSelection()).refreshAll();
//    	}
//    	runQuery();
//    }
//    
//    class SortListener implements Listener {
//        public void handleEvent(Event e) {
//            // determine new sort column and direction
//        	Table selectionTable = ((TableColumn)e.widget).getParent();
//            TableColumn currentColumn = (TableColumn) e.widget;
//            sortRecords(selectionTable, currentColumn);
//        }
//    }
//    
////    private class WorkerThread extends Thread {
////    	private NpsRecordSearchQueue queue;
////    	public void addToQueue(String sourceSystemId, String policy, String seqNo, String seqSub, int sortColumn, boolean sortDir) {
////    		synchronized(synchObject){
////    			queue = new NpsRecordSearchQueue(sourceSystemId, policy, seqNo, seqSub, sortColumn, sortDir);
////    			npsSelect.queued();
////    			npsRecords.clear();
////    			//npsRecords.addAll(savedRecords);
////    			needToClear=true;
////    		}
////    	}
////
////    	public void cancelAll() {
////    		npsSelect.queued();
////    		npsRecords.clear();
////    		needToClear=true;
////    	}
////    	
////    	public void run() {
//////    		while (true) {
//////    			NpsRecordSearchQueue localQueue = null;
//////    			synchronized(synchObject){
//////    				localQueue = queue;
//////        			queue = null;
//////    			}
//////    			if (localQueue != null) {
//////			    	try {
//////			    		npsRecords.clear();
//////			    		//npsRecords.addAll(savedRecords);
//////			    		needToClear=true;
//////		    			npsSelect.getDataIntoVector(
//////		    					localQueue.getSystemId(),
//////		    					localQueue.getPolicyNo(), 
//////		    					localQueue.getSeqNo(),
//////		    					localQueue.getSeqSub(),
//////		    					localQueue.getSortColumn(),
//////		    					localQueue.isSortDir(),
//////		    					npsRecords);
//////			    	} catch (Exception e) {
//////			    		Display.getDefault().asyncExec(new ErrorDialog(shell, "Runtime Error: ", e));
//////			    	}
//////    			}
//////				try {
//////					Thread.sleep(100);
//////				} catch (InterruptedException e) {
//////					throw new RuntimeException(e);
//////				}
//////    		}
////        }
////     }
////    
////    private class UpdatedRecordThread extends Thread {
////    	public void run() {
////    		if (needToClear) {
////    			needToClear = false;
////    			databaseTable.clearAll();
////    		}
////    		//npsRecords.trimToSize();
////			databaseTable.setItemCount(npsRecords.size());
////			if (npsRecords.size() == 0) {
////				databaseBoardLabel.setVisible(true);
////			} else {
////				databaseBoardLabel.setVisible(false);
////			}
//////			localTable.setItemCount(localSavedRecords.size());
//////			if (localSavedRecords.size()==0) {
//////				workboardLabel.setVisible(true);
//////			} else {
//////				workboardLabel.setVisible(false);
//////			}
////			
//////			if (npsSelect.currentlySearching) {
//////	    		progressBar.setVisible(true);
//////	    		progressBar2.setVisible(false);
//////		    	status.setText("Querying data...");
//////			} else {
//////	    		progressBar.setVisible(false);
//////	    		progressBar2.setVisible(true);
//////	        	try {
//////	        		if (NpsRecordSelectorUI.dbController.getSchemaRecord()==null) {
//////	        			status.setText("Schema not selected");
//////	        		} else if (NpsRecordSelectorUI.dbController.getConnection()==null) {
//////	    	    		status.setText("Not connected to database");
//////	//		    	} else if (sourceSystemIdSearch.getText().trim().equals("") &&
//////	//		    			policySearch.getText().trim().equals("") &&
//////	//		    			seqNoSearch.getText().trim().equals("") &&
//////	//		    			seqSubSearch.getText().trim().equals("")) {
//////	//		    		status.setText("Please enter at least one search criteria");
//////			    	} else {
//////				    	status.setText("Showing: " + npsRecords.size() + " records");
//////			    	}
//////	        	} catch (Exception e) {
//////	    			MessageBox errDialog = new MessageBox(shell, SWT.ICON_ERROR);
//////	    			errDialog.setText("ERROR");
//////	    			errDialog.setMessage("Runtime Error: " + e.getMessage());
//////	    			errDialog.open();
//////	        	}
//////
//////			}
////    	}
////    }
////    
////    private class RecordMonitorThread extends Thread {
////    	public void run() {
////    		while (true) {
////				//if (npsSelect.isChanged()||needToClear||temporaryRefresh) {
////				Display.getDefault().syncExec(new UpdatedRecordThread());
////				//npsSelect.notChanged();
////				//}
////				try {
////					Thread.sleep(100);
////				} catch (InterruptedException e) {
////					throw new RuntimeException(e);
////				}
////
////    		}
////    	}
////    }
////    
////    class OpenXMLSelectionListener implements SelectionListener {
////        public void widgetSelected(SelectionEvent e) {
////        	try {
////        		fileLoader.openXMLFile(workFolder);
//////        			workFolder.setSelection(workFolder.getItemCount()-1);        			
////        	} catch (Exception exc) {
////        		System.out.println("rpoboem?");
////        	}
////        }
////        public void widgetDefaultSelected(SelectionEvent e) {
////        	
////        }
////    }  
////    
////    class SelectionInputListener implements SelectionListener{
////    	public void widgetDefaultSelected(SelectionEvent e) {
////    		runQuery();
////    	}
////    	public void widgetSelected(SelectionEvent e) {
////    		//do nothing
////    		if (e.widget==doSearchButton) {
////        		runQuery();
////    		} else if (e.widget==activeSearchButton) {
////    			doSearchButton.setEnabled(!activeSearchButton.getSelection());
////    			if (activeSearchButton.getSelection()) {
////    	    		runQuery();
////    			}
////    		}
////    	}
////    }
////    
////    public void copy(Clipboard cb) {
////    	TableItem[] items = databaseTable.getSelection();
////    	if (items.length> 0) {
////	    	//Transfer[] transferArray = new Transfer[items.length];
////	    	//Object[] transObjects = new Object[items.length];
////    		NpsRecord[] recordArray = new NpsRecord[items.length];
////	    	for (int i=0; i < items.length; i++) {
////	    		NpsRecord record = (NpsRecord)items[i].getData();
////	    		recordArray[i]=record;
////	    		//transObjects[i]=recordArray;
////	    		//transferArray[i]=NpsRecord.getInstance();
////	    	}
////	    	cb.setContents(new Object[]{recordArray}, new Transfer[] {NpsRecord.getInstance()});
////    	}
////    }
////
////
//////    class OpenEditorMenuListener extends SelectionAdapter {
//////        public void widgetSelected(SelectionEvent e) {
//////        	if (workFolder.getSelection()!=null) {
//////	        	MenuItem menItem = ((MenuItem)e.widget);
//////	        	Menu menu = menItem.getParent();
//////	        	boolean sameWindow = true;
//////	        	if (menItem == menu.getItem(2)) {
//////	        		sameWindow = false;
//////	        	}
//////	        	((NpsDocumentUI)workFolder.getSelection()).openEditor(sameWindow);
//////        	}
//////        }
//////    }
    
}


