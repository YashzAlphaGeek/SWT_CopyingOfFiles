/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.yash.files;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.yash.constants.Constants;


/**
 * @author Yashwanth
 */
public class CopyingOfFiles extends Shell {

  private static CopyingOfFiles shell;
  private Text sourceFolderTxt;
  private Text destFolderTxt;
  private Button btnBrowseSource;
  private Button btnBrowseTarget;
  private Combo fileTypeCombo;
  private DateTime dateTime;
  private Button btnCopyFiles;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  /**
   * Launch the application.
   * 
   * @param args - Main Application
   */
  public static void main(String args[]) {
    try {
      Display display = Display.getDefault();
      shell = new CopyingOfFiles(display);
      Monitor primary = display.getPrimaryMonitor();
      Rectangle bounds = primary.getBounds();
      Rectangle rect = shell.getBounds();
      int x = bounds.x + ((bounds.width - rect.width) / 2);
      int y = bounds.y + ((bounds.height - rect.height) / 2);
      shell.setLocation(x, y);
      shell.open();
      shell.layout();
      while (!shell.isDisposed()) {
        if (!display.readAndDispatch()) {
          display.sleep();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the shell.
   * 
   * @param display - Display
   */
  public CopyingOfFiles(Display display) {
    super(display, SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
    setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));

    dateTime = new DateTime(this, SWT.BORDER);
    dateTime.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    dateTime.setBounds(222, 166, 230, 24);

    Label lblDate = new Label(this, SWT.NONE);
    lblDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
    lblDate.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    lblDate.setAlignment(SWT.CENTER);
    lblDate.setBounds(33, 166, 134, 30);
    lblDate.setText("Date (DD/MM/YYYY)");

    btnCopyFiles = new Button(this, SWT.BORDER);
    btnCopyFiles.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    btnCopyFiles.setBounds(242, 281, 98, 36);
    btnCopyFiles.setText("Copy Files");

    Label lblSourceFolder = new Label(this, SWT.CENTER);
    lblSourceFolder.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    lblSourceFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
    lblSourceFolder.setAlignment(SWT.CENTER);
    lblSourceFolder.setAlignment(SWT.CENTER);
    lblSourceFolder.setBounds(33, 34, 134, 30);
    lblSourceFolder.setText("Source Folder");

    sourceFolderTxt = new Text(this, SWT.BORDER);
    sourceFolderTxt.setBounds(222, 31, 230, 30);
    sourceFolderTxt.setText("E:\\gokuladevi\\Minutes");
    sourceFolderTxt.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));

    Label lblDestinationFolder = new Label(this, SWT.CENTER);
    lblDestinationFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
    lblDestinationFolder.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    lblDestinationFolder.setAlignment(SWT.CENTER);
    lblDestinationFolder.setBounds(33, 106, 134, 30);
    lblDestinationFolder.setText("Destination Folder");

    destFolderTxt = new Text(this, SWT.BORDER);
    destFolderTxt.setBounds(222, 103, 230, 30);
    destFolderTxt.setText("E:\\gokuladevi\\Mail");
    destFolderTxt.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));


    btnBrowseSource = new Button(this, SWT.BORDER);
    btnBrowseSource.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    btnBrowseSource.setBounds(502, 31, 75, 33);
    btnBrowseSource.setText("Browse");

    btnBrowseTarget = new Button(this, SWT.BORDER);
    btnBrowseTarget.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    btnBrowseTarget.setText("Browse");
    btnBrowseTarget.setBounds(502, 103, 75, 33);

    Label lblFileType = new Label(this, SWT.CENTER);
    lblFileType.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
    lblFileType.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    lblFileType.setAlignment(SWT.CENTER);
    lblFileType.setBounds(33, 228, 134, 30);
    lblFileType.setText("File Type");

    fileTypeCombo = new Combo(this, SWT.NONE);
    fileTypeCombo.setBounds(222, 228, 230, 23);
    String[] items = new String[] { Constants.PDF_FORMAT, Constants.TXT_FORMAT,Constants.XLSX_FORMAT,Constants.XLS_FORMAT,Constants.ALL_FORMAT };
    fileTypeCombo.setItems(items);
    fileTypeCombo.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
    fileTypeCombo.setText(Constants.PDF_FORMAT);
    createContents();
    addListeners();
  }

  /**
   * 
   */
  private void addListeners() {
    this.btnBrowseSource.addSelectionListener(new SelectionAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void widgetSelected(final SelectionEvent arg0) {
        String presentPath = "C://temp";
        String sourceFolder = openDirectoryDialog(shell, "Select the Source folder", presentPath);
        if (sourceFolder != null) {
          sourceFolderTxt.setText(sourceFolder);
        }
      }
    });

    this.btnBrowseTarget.addSelectionListener(new SelectionAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void widgetSelected(final SelectionEvent arg0) {
        String presentPath = "C://temp";
        String targetFolder = openDirectoryDialog(shell, "Select the Destination folder", presentPath);
        if (targetFolder != null) {
          destFolderTxt.setText(targetFolder);
        }
      }
    });


    btnCopyFiles.addSelectionListener(new SelectionAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        String sourceFolderLoc = sourceFolderTxt.getText();
        String targetFolderLoc = destFolderTxt.getText();
        String selectedDate = "";
        LocalDateTime datesTime = 
        		   LocalDateTime.of(dateTime.getYear(), dateTime.getMonth() + 1, dateTime.getDay(), dateTime.getHours(), dateTime.getMinutes(), dateTime.getSeconds());
        selectedDate = datesTime.format(formatter);
        String selectedfileType = fileTypeCombo.getText();
        if (!(sourceFolderLoc.isEmpty() && targetFolderLoc.isEmpty() && selectedDate.isEmpty() &&
            selectedfileType.isEmpty())) {
          shell.close();
          new FileApplication(sourceFolderLoc, targetFolderLoc, selectedDate, selectedfileType);
        }
      }

    });

  }

  /**
   * Create contents of the shell.
   */
  protected void createContents() {
    setText("CopyingOfFiles");
    setSize(621, 377);

  }

  /**
   * @param uiShell - Shell from Copying Files
   * @param title - Dialog title
   * @param presetPath - Present Path
   * @return Directory Path. This method open Directory dialog for Directory selection and return the Folder path in
   *         String.
   */
  public static String openDirectoryDialog(final Shell uiShell, final String title, final String presetPath) {
    Shell currentShell = new Shell(uiShell);
    DirectoryDialog dd = new DirectoryDialog(currentShell, 4096);
    dd.setFilterPath(presetPath);
    dd.setText(title);
    return dd.open();
  }

  @Override
  protected void checkSubclass() {
    // Disable the check that prevents subclassing of SWT components
  }
}
