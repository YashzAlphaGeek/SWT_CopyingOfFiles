/*
 * Copyright (c) Robert Bosch GmbH. All rights reserved.
 */
package com.yash.files;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

/**
 * @author YAP1COB
 */
public class FileApplication {

  private FilenameFilter textFilefilter;

  
  /**
 * @param folderPathList 
 * @param string
 */
private static Set<String> getFolderList(String sourcePath, Set<String> folderPathList) {
	File directoryPath = new File(sourcePath);
	folderPathList.add(sourcePath);
	File[] listFiles = directoryPath.listFiles();
	for(File file: listFiles)
	{
		if(file.isDirectory())
		{
		getFolderList(file.getAbsolutePath(),folderPathList);
		folderPathList.add(file.getAbsolutePath());
		}
	}
	return folderPathList;
}


/**
   * @param sourceFolderLoc - Source Location
   * @param targetFolderLoc - Target Location
   * @param selectedDate - Selected Date
   * @param selectedfileType - Selected File Type
   */
  public FileApplication(String sourceFolderLoc, String targetFolderLoc, String selectedDate, String selectedfileType) {
    File destPath = new File(targetFolderLoc);
	Set<String> folderPathList= new HashSet<>();
    filteringOfFolder(selectedfileType);
    String deleteFilesList[] = destPath.list(textFilefilter);
    deleteExistingFileInFolder(targetFolderLoc, deleteFilesList);
    filteringOfFolder(selectedfileType);
    getFolderList(sourceFolderLoc,folderPathList);
    int folderCount = folderPathList.size();
    int folderCounter=1;
    Set<String> filePathWithNameList= new HashSet<String>();
    for(String path : folderPathList)
    {
    File folderPath= new File(path);
    String filesList[] = folderPath.list(textFilefilter);
    performCopyOperation(folderPath, destPath, filesList, selectedDate,folderCount,folderCounter,filePathWithNameList);
    folderCounter++;
    }
    successMessage(destPath, filePathWithNameList);
  }


  /**
   * @param selectedfileType
   */
  private void filteringOfFolder(String selectedfileType) {
    if (selectedfileType.equals("PDF")) {
      textFilefilter = filteringOfFiles(".pdf");
    }
    if (selectedfileType.equals("TXT")) {
      textFilefilter = filteringOfFiles(".txt");
    }
  }


  /**
   * @param sourcePath
   * @param destPath
   * @param filesList
   * @param selectedDate
 * @param folderCount 
 * @param folderCounter 
 * @param filePathWithNameList 
   */
  private static void performCopyOperation(File sourcePath, File destPath, String[] filesList, String selectedDate, int folderCount, int folderCounter, Set<String> filePathWithNameList) {
    for (String fileName : filesList) {
      try {
        File file = new File(sourcePath + "\\" + fileName);
        if (checkFileDate(file, selectedDate)) {
          filePathWithNameList.add(sourcePath + "\\" + fileName);
          FileUtils.copyFileToDirectory(new File(sourcePath + "\\" + fileName), destPath);
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

  }


/**
 * @param destPath
 * @param filePathWithNameList
 */
private static void successMessage(File destPath, Set<String> filePathWithNameList) {
      JOptionPane.showMessageDialog(null, filePathWithNameList.size() +" files copied successfully to " + destPath, "Alert", JOptionPane.WARNING_MESSAGE);
      createLogFile(filePathWithNameList);
}

  /**
 * Log File creation
 * @param filePathWithNameList 
 */
private static void createLogFile(Set<String> filePathWithNameList) {
	try {
		File tempFolder= new File("C:\\temp");
		File tempFile=new File("C:\\temp\\FileApplication.txt");
		if(!tempFolder.exists() && tempFile.exists())
		{
			tempFolder.mkdir();
			tempFile.delete();
		}
		FileWriter myWriter = new FileWriter("C:\\temp\\FileApplication.txt");
		String listOfFiles = String.join("; \n", filePathWithNameList);
		myWriter.write(listOfFiles);
		myWriter.append("\nSuccessfully wrote to the file.");
		myWriter.close();
		} 
	catch (IOException e) {
		System.out.println("An error occurred.");
		e.printStackTrace();
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
      String dateCreated = df.format(attr.lastModifiedTime().toMillis());
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
   * @param deleteFilesList
   * @param string
   */
  private static void deleteExistingFileInFolder(String destFilePath, String[] deleteFilesList) {
    for (String fileName : deleteFilesList) {
      File file = new File(destFilePath + "\\" + fileName);
      if (file.exists()) {
        file.delete();
      }
    }
  }


}
