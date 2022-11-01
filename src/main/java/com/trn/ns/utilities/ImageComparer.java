package com.trn.ns.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.trn.ns.utilities.PropertyManager;

public class ImageComparer {

	String goldImagesPath,actualImagesPath,diffImagesPath;

	String basePath, compareExeLocation;
	private static final Logger LOGGER = Logg.createLogger();
	private static final Properties APPLICATIONPROPERTY = PropertyManager.loadApplicationPropertyFile();

	String dateAndTime;

	public ImageComparer(){

		dateAndTime = DateAndTime.getFormattedCurrentDateAndTime(APPLICATIONPROPERTY.getProperty("dateAndTimeFormat"));
		basePath = System.getProperty("user.dir").replace("\\", "/");
		compareExeLocation = basePath + APPLICATIONPROPERTY.getProperty("compareExePath");
	}

	/**
	 * @param expectedImagePath
	 * @param actualImagePath
	 * @param diffImagePath
	 * @return
	 */
	public boolean compareImage(String expectedImagePath, String actualImagePath, String diffImagePath){

		LOGGER.info(Utilities.getCurrentThreadId() + "Expected Image : " + expectedImagePath);
		LOGGER.info(Utilities.getCurrentThreadId() + "Actual  Image : " + actualImagePath);
		String value ="";
		boolean imgStatus=false;

		ProcessBuilder builder = new ProcessBuilder(
				compareExeLocation, "-metric", "RMSE",
				expectedImagePath, actualImagePath,
				diffImagePath);

		builder.redirectErrorStream(true);
		try {
			Process p;
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if(line.contains("(")) {
				 value = line.split("\\(")[1].trim().split("\\)")[0].trim();
				 LOGGER.info(Utilities.getCurrentThreadId() + "Difference Value : " +value );
				}else
					LOGGER.info(Utilities.getCurrentThreadId() + "compare exe issue "+line);

				if (r.read() == -1) {
					break;
				}
			}
			if(!value.isEmpty()) 
			if(Float.parseFloat(value)<Float.parseFloat(APPLICATIONPROPERTY.getProperty("threshold"))){
				LOGGER.info(Utilities.getCurrentThreadId() + "Expected image: "+ expectedImagePath + " and "+" Actual image : "+ actualImagePath + " are Same");
				imgStatus =true;

			}else{
				LOGGER.info(Utilities.getCurrentThreadId() + "Expected image : "+ expectedImagePath + " and "+" Actual image: "+ actualImagePath + " are Different");
				imgStatus=false;
			}
		} catch (IOException e) {
			LOGGER.info(Utilities.getCurrentThreadId() + "Unable to start the process");
			e.printStackTrace();
		}
		return imgStatus;
	}
}
