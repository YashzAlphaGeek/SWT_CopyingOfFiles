/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.yash.files;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

/**
 * @author YAP1COB
 */
public class FileApplication {


  private FilenameFilter textFilefilter;

  /**
   * @param sourceFolderLoc - Source Location
   * @param targetFolderLoc - Target Location
   * @param selectedDate - Selected Date
   * @param selectedfileType - Selected File Type
   */
  public FileApplication(String sourceFolderLoc, String targetFolderLoc, String selectedDate, String selectedfileType) {
    File directoryPath = new File(sourceFolderLoc);
    File destPath = new File(targetFolderLoc);
    deleteExistingFileInFolder(targetFolderLoc);
    if (selectedfileType.equals("PDF")) {
      textFilefilter = filteringOfFiles(".pdf");
    }
    if (selectedfileType.equals("TXT")) {
      textFilefilter = filteringOfFiles(".txt");
    }
    String filesList[] = directoryPath.list(textFilefilter);
    performCopyOperation(directoryPath, destPath, filesList, selectedDate);
  }


  /**
   * @param directoryPath
   * @param destPath
   * @param filesList
   * @param selectedDate
   */
  private static void performCopyOperation(File directoryPath, File destPath, String[] filesList, String selectedDate) {
    boolean isSuccess = false;
    for (String fileName : filesList) {
      try {
        File file = new File("");
        if (checkFileDate(file, selectedDate)) {
          FileUtils.copyFileToDirectory(new File(directoryPath + "\\" + fileName), destPath);
          isSuccess = true;
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (isSuccess) {
      JFrame frame = new JFrame();
      JOptionPane.showMessageDialog(frame, "Copied Successfully to " + destPath, "Alert", JOptionPane.WARNING_MESSAGE);
    }

  }

  /**
   * @param file
   * @param selectedDate
   */
  private static boolean checkFileDate(File file, String selectedDate) {
    try {
      BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
      SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      String dateCreated = df.format(attr.creationTime().toMillis());
      if (dateCreated.equals(selectedDate)) {
        return true;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return false;

  }


  /**
   * @param selectedfileType
   * @return
   */
  private static FilenameFilter filteringOfFiles(String selectedfileType) {
    FilenameFilter textFilefilter = new FilenameFilter() {

      @Override
      public boolean accept(File file, String name) {
        String lowercaseName = name.toLowerCase();
        if (lowercaseName.endsWith(selectedfileType)) {
          return true;
        }
        return false;
      }
    };
    return textFilefilter;
  }

  /**
   * @param string
   */
  private static void deleteExistingFileInFolder(String destFilePath) {
    try {
      FileUtils.cleanDirectory(new File(destFilePath));
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }


}
