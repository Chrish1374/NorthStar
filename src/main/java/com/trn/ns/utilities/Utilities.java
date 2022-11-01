package com.trn.ns.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.trn.ns.test.configs.Configurations;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Utilities {

	private static final Logger LOGGER = Logg.createLogger();

	public static String getCurrentThreadId() {
		return "Thread:" + Thread.currentThread().getId() + "	-";
	}

	public static int randomNumberGenerator() {
		LOGGER.info(Utilities.getCurrentThreadId() + "Random number generator function");
		Random rand = new Random();
		LOGGER.info(Utilities.getCurrentThreadId() + "Generating a random number between 0 and "
				+ Configurations.TEST_PROPERTIES.get("randomMaxInteger"));
		return rand.nextInt(1000);
	}

	public static String convertToString(int value) {
		LOGGER.info(Utilities.getCurrentThreadId() + "Integer to String Conversion function");
		LOGGER.info(Utilities.getCurrentThreadId() + "Converting the Integer value " + value
				+ " to String");
		return String.valueOf(value);
	}

	public static int convertToInteger(String value) {
		LOGGER.info(Utilities.getCurrentThreadId() + "String to Integer Conversion function");
		LOGGER.info(Utilities.getCurrentThreadId() + "Coverting the String value " + value
				+ " to Integer");
		return Integer.parseInt(value);
	}

	public static String[] convertListToStringArray(List<?> list) {
		LOGGER.info(Utilities.getCurrentThreadId() + "List to String Array Conversion function");
		LOGGER.info(Utilities.getCurrentThreadId() + "Size of the list obtained=" + list.size());
		Object[] obj = list.toArray();
		String[] str = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			str[i] = (String) obj[i];
		}
		return str;
	}

	public static List<String> covert2DArrayToList(String[][] array2D) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < array2D.length; i++) {
			for (int j = 0; j < array2D[i].length; j++) {
				list.add(array2D[i][j]);
			}
		}
		return list;
	}

	/**
	 * Gets the base path where the project resides
	 * @return the base path
	 */
	public static String getBaseDirPath(){
		String path = System.getProperty("user.dir").replace("/", "\\");
		LOGGER.debug(Utilities.getCurrentThreadId() + "Base directory path : " + path );

		return path;

	}

	/**
	 * @param dirName is the name of the folder that's to be created
	 * @return boolean based on the whether the file is created or not
	 */
	public static boolean createDirIfNotExists(String dirName) {
		File dir = new File(dirName);
		boolean result = false;
		// if the directory does not exist, create it
		if (!dir.exists()) {
			LOGGER.debug("Creating Logs directory : " + dir);
			result= false;
			try{
				dir.mkdir();
				result = true;
			} catch(SecurityException se){
				LOGGER.error(Utilities.getCurrentThreadId()+"SecurityException "+se.getStackTrace(),se);
			}        
			if(result) {    
				LOGGER.debug(dirName + " dir created");  
			}
		}
		else
			LOGGER.debug(dirName + "folder already exists");
		return result;
	}

	/**
	 * @param dirName is the name of the folder structure that's to be created (if parent older is not exists it will get created automatically)
	 * @return boolean based on the whether the file is created or not
	 */
	public static boolean createDirsIfNotExists(String dirName) {
		File dir = new File(dirName);
		boolean result = false;
		// if the directory does not exist, create it
		if (!dir.exists()) {
			LOGGER.debug("Creating Logs directory : " + dir);
			result= false;
			try{
				dir.mkdirs();
				result = true;
			} catch(SecurityException se){
				LOGGER.error(Utilities.getCurrentThreadId()+"SecurityException "+se.getStackTrace(),se);
			}        
			if(result) {    
				LOGGER.debug(dirName + " dir created");  
			}
		}
		else
			LOGGER.debug(dirName + "folder already exists");
		return result;
	}
	public boolean getTextFromImage(String filepath){
		boolean status=false;
		// D:\\download.jpg
		File imageFile = new File(filepath);
		ITesseract instance = new Tesseract();
		instance.setDatapath("C:/Program Files (x86)/OCS Inventory Agent");
		try {
			String result = instance.doOCR(imageFile);
			LOGGER.info(result);

		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}

		return status;
	}

	public static boolean compareExactText(String actual, String expected) {

		Logg.createLogger().info(Utilities.getCurrentThreadId() + "Actual Value=" + actual + " Expected Value="
				+ expected);
		Logg.createLogger().info(Utilities.getCurrentThreadId() + "Result of exact comparison is "
				+ actual.equals(expected));
		return actual.equals(expected);
	}

	public static boolean comparePartialText(String actual, String expected) {
		Logg.createLogger().info(Utilities.getCurrentThreadId() + "Actual Value=" + actual + " Expected Value="
				+ expected);
		Logg.createLogger().info(Utilities.getCurrentThreadId() + "Result of partial comparison is "
				+ actual.contains(expected));
		return actual.contains(expected);
	}

	/**@author chiranjivb
	 * @param inputFile - zip file name which is to be extracted
	 * @param outputFolder - path of folder where files are to be extracted
	 * Description: This function extracts the given zip file to the specified output folder 
	 */
	public static boolean extractZip(String inputFile, String outputFolder) {

		byte[] buffer = new byte[1024];
		FileOutputStream fos = null; ZipInputStream zis = null;
		boolean success = true;

		try{
			//create output directory if it doesn't exist
			File folder = new File(outputFolder);
			if(!folder.exists()){
				folder.mkdir();
			}
			//get the zip file content
			zis = new ZipInputStream(new FileInputStream(inputFile));
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);
				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();
				fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			LOGGER.info("Extracted to " + outputFolder);

		}catch(IOException ex){
			success = false;
			ex.printStackTrace();
		} finally {
			try {
				fos.close();
				zis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	/**@author chiranjivb
	 * @param directoryPath - path of folder which is to be cleared
	 * Description: This function deletes all the files inside the given directory
	 */
	public static void clearDirectory(String directoryPath) throws IOException{
		File folder = new File(directoryPath);
		FileUtils.cleanDirectory(folder);
	}

	/**@author chiranjivb
	 * @param directoryPath - path of folder where files are located
	 * @return String[] filenames
	 * Description: This function gets the names of all files located in given directory 
	 */
	public static String[] getFileNamesFromDirectory(String directoryPath){
		File[] listOfFiles = null;
		String[] filenames = null;
		File folder = new File(directoryPath);
		//		try {
		listOfFiles = folder.listFiles();
		filenames = new String[listOfFiles.length];
		for(int i=0;i<listOfFiles.length;i++) {
			filenames[i] = listOfFiles[i].getName();
		}
		return filenames;
	}

	/**@author chiranjivb
	 * @param directoryPath - path of folder where file is located
	 * @return filename
	 * Description: This function gets the file name located in given directory 
	 */
	public static String getFileNameFromDirectory(String directoryPath) {
		File[] listOfFiles = null;
		String filename = "";
		File folder = new File(directoryPath);
		listOfFiles = folder.listFiles();
		filename = listOfFiles[0].getName();
		return filename;
	}

	/**@author chiranjivb
	 * @param inputFolder - path of folder from where files are to be copied
	 * @param outputFolder - folder to which files are  to be copied
	 * Description: This function copies files from inputFolder to outputFolder 
	 */
	public static void copyFileToDir(String inputFolder, String outputFolder) {
		File srcFolder = new File(inputFolder);
		File destFolder = new File(outputFolder);
		try {
			FileUtils.copyDirectory(srcFolder, destFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author anilt
	 * @param dirPath -directory to be used to reanme files
	 * @param testName -name to be renamed followed by _ and number
	 * Description: Function to rename files with testname.
	 */
	public static void renameFile(String dirPath, String testName) {

		String[] filenames = getFileNamesFromDirectory(dirPath);                
		for(int i=1;i<=filenames.length;i++) {
			File oldName = new File(dirPath+"/"+filenames[i-1]);
			File newName = new File(dirPath+"/"+testName+ "_" +i+".png");
			if(oldName.renameTo(newName)) {
				LOGGER.info("renamed" + newName.getName());
			} else {
				LOGGER.info("Error renaming file");
			}
		}
	}

	/**@author chiranjivb
	 * @param baseIP - base IP of application server
	 * @param url - url of application
	 * Description: This function replaces the base IP in the application url 
	 */
	public static String replaceUrl(String baseIP, String url) {
		String finalUrl = "";
		try {
			String arr1[] = url.split("//");
			String s1 = arr1[0]+"//";
			String arr2[] = arr1[1].split("/");
			finalUrl = s1 + baseIP+"/" + arr2[1] +"/" + arr2[2];
		} catch(Exception e) {
			LOGGER.error("Error in replaceUrl: " + e.toString());
		}
		return finalUrl;		
	}

	/**@author chiranjivb
	 * @param filePath - path of file to be uploaded
	 * @throws Exception
	 * Description: This function uploads the file in the given filepath in the application 
	 */
	public static void uploadFile(String filePath) throws Exception {
		StringSelection stringSelection = new StringSelection(filePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		try{
			Thread.sleep(4000);
		}catch(InterruptedException e){}
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}



	/**@author chiranjivb
	 * @param filePath - path of xml file
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException
	 * Description: This function reads patient series data from xml file  
	 */

	public static String[][] readSeriesXml(String filePath) throws ParserConfigurationException, SAXException, IOException {
		String arr[][] = null;
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("series");
		arr = new String[nList.getLength()][7];
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			LOGGER.info("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				arr[temp][0] = eElement.getAttribute("number");
				arr[temp][1] = eElement.getElementsByTagName("modality").item(0).getTextContent();
				arr[temp][2] = eElement.getElementsByTagName("seriesdatetime").item(0).getTextContent();
				arr[temp][3] = eElement.getElementsByTagName("instances").item(0).getTextContent();
				arr[temp][4] = eElement.getElementsByTagName("seriesdescription").item(0).getTextContent();
				arr[temp][5] = eElement.getElementsByTagName("bodypartexamined").item(0).getTextContent();
				arr[temp][6] = eElement.getElementsByTagName("manufacturer").item(0).getTextContent();
			}
		} //end of for loop
		return arr;
	}





	/**Author DheerajG
	 * @param f1
	 * @param f2
	 * @throws IOException 
	 */
	public static void copyFile(File f1, File f2) throws IOException{

		Files.copy(f1.toPath(), f2.toPath(),StandardCopyOption.REPLACE_EXISTING);
		LOGGER.info(Utilities.getCurrentThreadId() + "Method to copy file");
		LOGGER.info(Utilities.getCurrentThreadId() + "Copied file from:  "+f1+"  to  "+f2);
		LOGGER.info(Utilities.getCurrentThreadId() + "file copied successfully");

	}

	/**Author DheerajG
	 * @param filename
	 * @throws IOException 
	 */
	public static void copyBackupFile(String filename, String...destination) throws IOException{
		File dest = null;
		if(destination.length > 0) {
			dest = new File(Configurations.TEST_PROPERTIES.get(destination[0])+filename);
		} else {
			dest = new File(Configurations.TEST_PROPERTIES.get("destination")+filename);
		} 
		File temp = new File(Configurations.TEST_PROPERTIES.get("temp")+filename);
		Files.copy(temp.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
		LOGGER.info(Utilities.getCurrentThreadId() + "Method to copy file");
		LOGGER.info(Utilities.getCurrentThreadId() + "Copied file from:  "+temp+"  to  "+dest);
		LOGGER.info(Utilities.getCurrentThreadId() + "file copied successfully");

	}


	/**Author DheerajG
	 * @param filename
	 * @throws IOException 
	 */
	public static void copyFileFromOneFolderToOther(String filename, String sourceFileName, String...destination) throws IOException
	{
		File dest = null;
		File source = new File(Configurations.TEST_PROPERTIES.get("source")+sourceFileName);

		if(destination.length > 0) {
			dest = new File(Configurations.TEST_PROPERTIES.get(destination[0])+filename);
		} else {
			dest = new File(Configurations.TEST_PROPERTIES.get("destination")+filename);
		}
		File temp = new File(Configurations.TEST_PROPERTIES.get("temp")+filename);

		if(dest.exists())
		{
			copyFile(dest,temp);
		}
		copyFile(source,dest);
	}

	/**
	 * @author anilt
	 * @param folderPath - Path of directory
	 * @return -true if folder is not empty
	 * Description - This function checks whether given folder is not empty
	 */
	public static boolean checkIfDirectoryisEmpty(String folderPath) {
		if(FileUtils.sizeOfDirectory(new File(folderPath)) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author anilt
	 * @param folderPath - Path of directory
	 * @return -true if folder is not empty
	 * Description - This function checks whether given folder is not empty
	 * @throws InterruptedException 
	 * @throws WaitException 
	 */
	public boolean checkFileWithExtension(String folderPath, String extension)  {
		boolean fileExists = false;
		String fileName = "";
		int waitCount = 0;
		boolean requiredFileExists = false;

		while(!fileExists && waitCount<100) {
			try{
				Thread.sleep(4000);
			}catch(InterruptedException e){}
			fileExists = Utilities.checkIfDirectoryisEmpty(folderPath);
			waitCount++;
		}

		for(int i=0;i<100;i++) {
			if(fileExists) {
				fileName = Utilities.getFileNameFromDirectory(folderPath);
				if(fileName.endsWith(extension)) {
					requiredFileExists = true;
					break;
				}else if(fileName.endsWith(".jpg")) {
					requiredFileExists = true;
					break;	
				} else {
					try{
						Thread.sleep(4000);
					}catch(InterruptedException e){}
				}
			}
		}
		return requiredFileExists;
	}

	/**
	 * @author chiranjivb
	 * @return
	 * Description: This function returns a random number between 100 and 9999
	 */
	public int getRandomNumber() {
		Random randomObj = new Random();
		int randomNum = randomObj.nextInt((9999 - 100)) + 10;
		return randomNum;
	}

	/**
	 * @author chiranjivb
	 * @param processName
	 * @return success message
	 * Description: This function terminates the specified process in server
	 */
	public String killProcess(String processName) {
		String strCmdLine = null;
		String domainUser = Configurations.TEST_PROPERTIES.get("DomainUser");
		String domainUserPassword = Configurations.TEST_PROPERTIES.get("DomainUserPassword");
		String host =Configurations.TEST_PROPERTIES.get("nsHostName");
		String command = "taskkill /S " + host + " /U " + domainUser + " /P " + domainUserPassword + " /IM " + processName;
		String output = "";

		strCmdLine = String.format(command);
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(strCmdLine);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s = null;
			// read the output from the command
			LOGGER.info("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				LOGGER.info(s);
				output = output + s;
			}
			return output;
		} catch (Exception e) {
			LOGGER.error("Exception in killProcess(): " + e.toString());
			return null;
		}
	}

	/**
	 * @author chiranjivb
	 * @param srcFile - source file path
	 * @param destFile - destination file path
	 * @throws IOException
	 * Description: This function appends text from source file to destination file
	 */
	public static void appendTextToFile(String srcFile, String destFile) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(srcFile));
			bw = new BufferedWriter(new FileWriter(destFile, true));
			String aLine = null;
			bw.newLine();
			while ((aLine = br.readLine()) != null) {
				//Process each line and add output to Dest.txt file
				bw.write(aLine);
				bw.newLine();
			}
		}	finally {
			try{
				bw.close();
				br.close();
			} catch(Exception e) {
				LOGGER.error("Exception in finally: " + e.toString());
			}
		}
	}

	/**
	 * @author chiranjivb
	 * @param fileName - path for the file to be deleted
	 * Description: This function deletes the file at specified path
	 */
	public static void deleteFileFromFolder(String fileName) {
		File file = new File(fileName);
		if(file.delete()){
			LOGGER.info(file.getName() + " is deleted!");
		}else{
			LOGGER.error("Delete operation failed.");
		}
	}

	/**
	 * @author chiranjivb
	 * @param xOffset - x offset on top left
	 * @param yOffset - y offset on top left
	 * @param xDistance - distance along x axis i.e. width of new image
	 * @param yDistance - distance along y axis i.e. height of new image
	 * @param srcFile - source file name which is to be cropped
	 * @param destFile - new file name after cropping the source file
	 * Description: This function crops the given image and creates a new one with specified size
	 * @throws IOException 
	 */
	public static void cropImage(int xOffset, int yOffset, int xDistance, int yDistance, String srcFile, String destFile) throws IOException {
		BufferedImage originalImgage = ImageIO.read(new File(srcFile));
		BufferedImage subImgage = originalImgage.getSubimage(xOffset, yOffset, xDistance, yDistance);
		ImageIO.write(subImgage, "png", new File(destFile));
	}

	

	/**@author payala
	 * @param filePath - path of xml file
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException
	 * Description: This function reads textoverlay data from xml file  
	 */
	public static String[][] readTextOverlayXml(String filePath,String seriesName) throws ParserConfigurationException, SAXException, IOException {
		String arr[][] = null;
		String seriesNm = seriesName;
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("Series");
		arr = new String[nList.getLength()][29];
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			LOGGER.info("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				
			if(eElement.getAttribute("Name").equalsIgnoreCase(seriesNm)){
				arr[temp][0] = eElement.getAttribute("Name");
				arr[temp][1] = eElement.getElementsByTagName("PatientName").item(0).getTextContent();
				arr[temp][2] = eElement.getElementsByTagName("PatientID").item(0).getTextContent();
				arr[temp][3] = eElement.getElementsByTagName("PatientExternalID").item(0).getTextContent();
				arr[temp][4] = eElement.getElementsByTagName("SeriesDescription").item(0).getTextContent();
				arr[temp][5] = eElement.getElementsByTagName("PatientBirthDate").item(0).getTextContent();
				arr[temp][6] = eElement.getElementsByTagName("StudyDescription").item(0).getTextContent();
				arr[temp][7] = eElement.getElementsByTagName("ReferringPhysiciansName").item(0).getTextContent();
				arr[temp][8] = eElement.getElementsByTagName("AcquisitionDevice").item(0).getTextContent();
				
				arr[temp][9] = eElement.getElementsByTagName("PatientSex").item(0).getTextContent();
				arr[temp][10] = eElement.getElementsByTagName("StudyDateTime").item(0).getTextContent();
				arr[temp][11] = eElement.getElementsByTagName("PatientClass").item(0).getTextContent();
				arr[temp][12] = eElement.getElementsByTagName("AccessionNumber").item(0).getTextContent();
				arr[temp][13] = eElement.getElementsByTagName("ImageMatrix").item(0).getTextContent();
				arr[temp][14] = eElement.getElementsByTagName("StudyAcquisitionSite").item(0).getTextContent();
				
				arr[temp][15] = eElement.getElementsByTagName("ImagePosition").item(0).getTextContent();
				arr[temp][16] = eElement.getElementsByTagName("SliceThickness").item(0).getTextContent();
				arr[temp][17] = eElement.getElementsByTagName("Detector").item(0).getTextContent();
				arr[temp][18] = eElement.getElementsByTagName("SliceLocation").item(0).getTextContent();
				arr[temp][19] = eElement.getElementsByTagName("Kvp").item(0).getTextContent();
				arr[temp][20] = eElement.getElementsByTagName("FOV").item(0).getTextContent();
				arr[temp][21] = eElement.getElementsByTagName("ImageNumberCurrentSCrollPosition").item(0).getTextContent();
				arr[temp][22] = eElement.getElementsByTagName("ImageNumberMaxSCrollPosition").item(0).getTextContent();
				
				arr[temp][23] = eElement.getElementsByTagName("mAs").item(0).getTextContent();
				arr[temp][24] = eElement.getElementsByTagName("Target").item(0).getTextContent();
				arr[temp][25] = eElement.getElementsByTagName("ImageType").item(0).getTextContent();
				arr[temp][26] = eElement.getElementsByTagName("ImageAcquisitionDateTime").item(0).getTextContent();
				arr[temp][27] = eElement.getElementsByTagName("WindowWidth").item(0).getTextContent();
				arr[temp][28] = eElement.getElementsByTagName("WindowCenter").item(0).getTextContent();
			}
			}
		} //end of for loop
		return arr;
	}
	
	
}
