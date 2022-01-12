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
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.yash.constants.Constants;

/**
 * @author YAP1COB
 */
public class FileApplication {

	private FilenameFilter textFilefilter;
	private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

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
		JFrame frame = new JFrame();
		if(!selectedfileType.equals(Constants.ALL_FORMAT))
		{
			filteringOfFolder(selectedfileType);
			String deleteFilesList[] = destPath.list(textFilefilter);
			String message="Do you want to delete all "+ selectedfileType+" files in ("+targetFolderLoc+")";
			Set<String> filePathWithNameList= new HashSet<String>();
			if(confirmationForDeletion(targetFolderLoc, frame,message)==JOptionPane.YES_OPTION)
			{
				frame.dispose();
				deleteExistingFileInFolder(targetFolderLoc, deleteFilesList);
				copyingOfSpecificFiles(sourceFolderLoc, selectedDate, selectedfileType, destPath, folderPathList,
						filePathWithNameList);
			}
			else
			{
				frame.dispose();
				copyingOfSpecificFiles(sourceFolderLoc, selectedDate, selectedfileType, destPath, folderPathList,
						filePathWithNameList);
			}


		}
		else
		{
			int allFileCopyConfirmation = JOptionPane.showConfirmDialog (frame, "Do you want to copy all files from ("+sourceFolderLoc+")"+ "to ("+targetFolderLoc+")","Warning",JOptionPane.YES_NO_OPTION);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.toFront();
			Set<String> filePathWithNameList= new HashSet<String>();
			Set<String> foldersPathList =new HashSet<String>();
			if(allFileCopyConfirmation == JOptionPane.YES_OPTION){
				String message="Do you want to delete all existing files in ("+targetFolderLoc+")";
				if(confirmationForDeletion(targetFolderLoc, frame, message)==JOptionPane.YES_OPTION)
				{
					frame.dispose();
					File targetFile= new File(targetFolderLoc);
					deleteExistingFileInFolder(targetFolderLoc, targetFile.list());
					getFolderList(sourceFolderLoc,foldersPathList);
					performCopyOperation(sourceFolderLoc,targetFolderLoc,selectedDate,filePathWithNameList,foldersPathList);
					successMessage(destPath, filePathWithNameList);
				}
				else
				{
					frame.dispose();
					getFolderList(sourceFolderLoc,foldersPathList);
					performCopyOperation(sourceFolderLoc,targetFolderLoc,selectedDate,filePathWithNameList,foldersPathList);
					successMessage(destPath, filePathWithNameList);
				}
			}
		}
	}


	/**
	 * @param sourceFolderLoc
	 * @param selectedDate
	 * @param selectedfileType
	 * @param destPath
	 * @param folderPathList
	 * @param filePathWithNameList
	 */
	private void copyingOfSpecificFiles(String sourceFolderLoc, String selectedDate, String selectedfileType,
			File destPath, Set<String> folderPathList, Set<String> filePathWithNameList) {
		filteringOfFolder(selectedfileType);
		getFolderList(sourceFolderLoc,folderPathList);
		int folderCount = folderPathList.size();
		int folderCounter=1;
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
	 * @param targetFolderLoc
	 * @param frame
	 */
	private int confirmationForDeletion(String targetFolderLoc, JFrame frame,String message) {
		int allFileDelConfirmation = JOptionPane.showConfirmDialog (frame, message,"Warning",JOptionPane.YES_NO_OPTION);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.toFront();
		return allFileDelConfirmation;
	}


	/**
	 * @param sourceFolderLoc
	 * @param targetFolderLoc
	 * @param selectedDate 
	 * @param filePathWithNameList 
	 * @param foldersPathList 
	 */
	private void performCopyOperation(String sourceFolderLoc, String targetFolderLoc, String selectedDate, Set<String> filePathWithNameList, Set<String> foldersPathList) {
		try {
			for(String foldersPath:foldersPathList)
			{
				File folderPath= new File(foldersPath);
				File[] listFiles = folderPath.listFiles();
				for(File files:listFiles)
				{
					if(checkFileDate(files, selectedDate))
					{
						if(files.isFile())
							FileUtils.copyFileToDirectory(files, new File(targetFolderLoc));
						filePathWithNameList.add(files.getAbsoluteFile().getAbsolutePath());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param selectedfileType
	 */
	private void filteringOfFolder(String selectedfileType) {
		if (selectedfileType.equals(Constants.PDF_FORMAT)) {
			textFilefilter = filteringOfFiles(Constants.PDF_EXTENSION_FORMAT);
		}
		if (selectedfileType.equals(Constants.TXT_FORMAT)) {
			textFilefilter = filteringOfFiles(Constants.TXT_EXTENSION_FORMAT);
		}
		if (selectedfileType.equals(Constants.XLSX_FORMAT)) {
			textFilefilter = filteringOfFiles(Constants.XLSX_EXTENSION_FORMAT);
		}
		if(selectedfileType.equals(Constants.XLS_FORMAT))
		{
			textFilefilter = filteringOfFiles(Constants.XLS_EXTENSION_FORMAT);
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
