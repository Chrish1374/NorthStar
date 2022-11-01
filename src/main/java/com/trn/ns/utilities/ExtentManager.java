package com.trn.ns.utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class ExtentManager {

	private final static Properties applicationProperty = PropertyManager.loadApplicationPropertyFile();
	private static String log = applicationProperty.getProperty("enablextentreportLog");
	private static String addscreenshot = applicationProperty.getProperty("addcaptureScreenshot");
	private static String runMode = applicationProperty.getProperty("run");

	static ExtentReports  extent = ExtentManager.getInstance();

	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

	public synchronized static ExtentReports getInstance() {

		if (extent == null) {
			String Path = System.getProperty("user.dir").replace("\\", "/")+"/ExtentReport.html";
			extent = new ExtentReports(Path, true);
			extent.loadConfig(new File(System.getProperty("user.dir") + "/ExtentReportConfig.xml"));
		}
		return extent;
	}

	public synchronized static  ExtentTest startTest(String testName, String description){
		ExtentTest test = extent.startTest(testName, description);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		return test;
	}

	public synchronized static ExtentTest getTestInstance(){
		return extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	public synchronized static void customExtentReportLog(LogStatus status, ExtentTest _extentTest, String action, String message){
		if(log.equalsIgnoreCase("YES") && runMode.equalsIgnoreCase("Test")){
			_extentTest.log(status, action, message);				
		}
	}

	public synchronized static void customExtentReportLogWithScreenshot(LogStatus status, ExtentTest _extentTest, String action, String screenshotpath){
		String arr[] = screenshotpath.split("@");
		if(addscreenshot.equalsIgnoreCase("YES"))
			_extentTest.log(status, action, arr[0]+ _extentTest.addScreenCapture(arr[1]));				
	}

	public static synchronized void endTest() {

		extent.endTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));

	}
}
