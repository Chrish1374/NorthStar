package com.trn.ns.test.listeners;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.PropertyManager;
import com.trn.ns.utilities.RallyUtil;
import com.trn.ns.utilities.Utilities;

public class ItestCustomListener implements ITestListener {


	private WebDriver driver;
	private ExtentTest extentRepSuite;
	private static long endTime;
	private static long startTime;
	protected static final Logger LOGGER = Logg.createLogger();


	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	@Override
	public synchronized void onStart(ITestContext iTestContext) {

		synchronized (iTestContext) {
			extentRepSuite = ExtentManager.getInstance().startTest(iTestContext.getAllTestMethods()[0].getInstance().getClass().getSimpleName());
			
			LOGGER.info("*********************************************");
			LOGGER.info("Script Name - "+iTestContext.getAllTestMethods()[0].getInstance().getClass());
			startTime = System.currentTimeMillis();
			LOGGER.info("*********************************************");

		}
	}

	//After ending all tests, below method runs.
	@Override
	public synchronized void onFinish(ITestContext iTestContext) {

		ExtentManager.getInstance().addSystemInfo("Environment", PropertyManager.Propertyfilename);
		ExtentManager.getInstance().addSystemInfo("Browser", Configurations.TEST_PROPERTIES.get("Browser"));	
//		ExtentManager.getInstance().addSystemInfo("Browser - version", );	

		if (!Configurations.TEST_PROPERTIES.get("run").equalsIgnoreCase("TEST")) {
			extentRepSuite.log(Configurations.WARNING, "Base MODE - DO NOT CONSIDER THIS RESULT!!");
		} else {
			extentRepSuite.log(Configurations.INFO, "Execution of this test protocol is completed");
		}

		ExtentManager.getInstance().endTest(extentRepSuite);		
		ExtentManager.getInstance().flush();
		
		LOGGER.info("*********************************************");
		endTime = System.currentTimeMillis();
		LOGGER.info("Execution Time = "+TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)+" seconds");
		LOGGER.info("Execution Time = "+TimeUnit.MILLISECONDS.toMinutes(endTime - startTime)+" minutes");
		LOGGER.info("*********************************************");

		/* This method checks whether a method has been invoked 
		 * multiple times,if yes and one of result of this method is either passed or failed
		 * it will remove all  skipped test run of same method
		 */
		//		for(int i=0;i<iTestContext.getAllTestMethods().length;i++){
		//			if(iTestContext.getAllTestMethods()[i].getCurrentInvocationCount()>=2)
		//			{
		//				if (iTestContext.getFailedTests().getResults(iTestContext.getAllTestMethods()[i]).size() == 1 || iTestContext.getPassedTests().getResults(iTestContext.getAllTestMethods()[i]).size() == 1)
		//				{
		//					iTestContext.getSkippedTests().removeResult(iTestContext.getAllTestMethods()[i]);
		//				}
		//			}
		//		}
	}


	@Override
	public synchronized void onTestStart(ITestResult iTestResult) {
		ExtentManager.startTest(iTestResult.getMethod().getMethodName(),"");
		
		
		
	}

	@Override
	public synchronized void onTestSuccess(ITestResult iTestResult) {

		ExtentManager.getInstance().endTest(ExtentManager.getTestInstance());
		ExtentManager.getInstance().flush();
		extentRepSuite.appendChild(ExtentManager.getTestInstance());

		if(Configurations.TEST_PROPERTIES.get("rallyFlag").equals(Configurations.YES))
		{
			LOGGER.info(updateRally(iTestResult,TEST_PROPERTIES.get("build"),"","",""));
		}


	}

	@Override
	public synchronized void onTestFailure(ITestResult iTestResult) {


		ExtentManager.customExtentReportLog(Configurations.FAIL, ExtentManager.getTestInstance(), iTestResult.getMethod().getMethodName()+" failed", iTestResult.getThrowable().toString());
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		String errorAppImages = TEST_PROPERTIES.get("errorAppImages");
		String screenshotpath = errorAppImages + "failed-" + iTestResult.getName() +"_"+dateFormat.format(date)+ ".png";
		Object currentClass = iTestResult.getInstance();
		driver = ((TestBase) currentClass).getDriver();

		takeScreenShot(screenshotpath);		
		ExtentManager.customExtentReportLogWithScreenshot(Configurations.INFO, ExtentManager.getTestInstance(), "Gracefully handled error", "At the time of failure, the application screenshot is here" + "@" + screenshotpath);

		ExtentManager.getInstance().endTest(ExtentManager.getTestInstance());
		ExtentManager.getInstance().flush();
		extentRepSuite.appendChild(ExtentManager.getTestInstance());

		if(Configurations.TEST_PROPERTIES.get("rallyFlag").equals(Configurations.YES))
		{
			LOGGER.info(updateRally(iTestResult,TEST_PROPERTIES.get("build"),errorAppImages,"failed-" +iTestResult.getName() +"_"+dateFormat.format(date)+ ".png",iTestResult.getName()));
		}

	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {

		ExtentManager.getTestInstance().log(Configurations.SKIP, "Test Skipped");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		LOGGER.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
	}

	/**
	 * This method will take the default webdriver instance.
	 * @param give any specific Path with image name were you want to create the snapshot.
	 * @return the path of the screenshot name
	 */
	public boolean takeScreenShot(String destinationFilepath){
		boolean status= false; 
		try {
			File screenshot = null;

			//			if(driver  != null){
			screenshot= ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			LOGGER.info(Utilities.getCurrentThreadId() + "Screenshot taken successfully. Name of file :" + destinationFilepath);
			FileUtils.copyFile(screenshot, new File(destinationFilepath));
			status= true;
			//			}
			//			else 
			//				LOGGER.info("WEB_DRIVER is not initialised, can't take a screenshot");
		} catch (Exception e) {
			LOGGER.error(new StringBuilder()
			.append(Utilities.getCurrentThreadId() + "Failed to capture screenshot ----: ")
			.append(e.getMessage()),e);
		}
		return status;

	}

	private String updateRally(ITestResult iTestResult, String build,String attachmentPath,String attachmentName,String attachmentDesc){

		// update Rally

		String testCaseName= iTestResult.getMethod().getMethodName();
		String regex = "TC(\\d+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(testCaseName);
		String update ="";
		List<String> testCases = new ArrayList<String>();

		try{
			if(!iTestResult.getAttribute("TEST_CASE_ID").toString().isEmpty()||iTestResult.getAttribute("TEST_CASE_ID").toString()==null)			
				testCases.add(iTestResult.getAttribute("TEST_CASE_ID").toString());
		}catch(NullPointerException e){

			while(matcher.find()){
				testCases.add(matcher.group());

			}}

		try{
			for(int i=0 ;i< testCases.size();i++){				
				RallyUtil util = new RallyUtil(new URI(TEST_PROPERTIES.get("rallyURL")), TEST_PROPERTIES.get("rallyUsername"), TEST_PROPERTIES.get("rallyPassword"), TEST_PROPERTIES.get("workspaceRef"));
				if(iTestResult.getStatus()!=iTestResult.FAILURE)
					update = util.updateTestCase(testCases.get(i), TEST_PROPERTIES.get("rallyUsername"), Configurations.RALLY_STATUS_PASS, build, "Automated Test Cases updation", attachmentPath, attachmentName, attachmentDesc);	
				else
					update = util.updateTestCase(testCases.get(i), TEST_PROPERTIES.get("rallyUsername"), Configurations.RALLY_STATUS_FAIL, build, "Automated Test Cases updation", attachmentPath, attachmentName, attachmentDesc);
				ExtentManager.customExtentReportLog(Configurations.INFO, ExtentManager.getTestInstance(), "Test case result updation", update);
			}
		}catch(Exception e){}
		return update;

	}


}
