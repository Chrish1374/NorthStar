package com.trn.ns.test.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import com.trn.ns.drivers.Browser;
import com.trn.ns.drivers.DriverFactory;
import com.trn.ns.drivers.IDriver;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;



public class TestBase {

	protected static final Logger LOGGER = Logg.createLogger();
	public static String parameterEnvironment;
	public static String executionType;
	public static String extractedFolderPath;
	public static String errorAppImages;

	protected WebDriver driver;
	protected String protocolName;
	private LoginPage loginPage;
	protected DatabaseMethods db;
	private int rowNumGraphicObject;
	private int rowNumGraphicLevel;
	private int batchMachineRecords;
	private static int dbCondition=0;

	@BeforeClass(alwaysRun=true)
	public void beforeClass(ITestContext context) {

		parameterEnvironment = context.getSuite().getXmlSuite().getAllParameters().get("environment");		
		executionType = Configurations.TEST_PROPERTIES.get("executionType");
		protocolName = this.getClass().getSimpleName();		
		try{
			if ((Configurations.TEST_PROPERTIES.get("launchBrowser")).toUpperCase().equals("CLASS")) {
				Browser browser = new Browser(Configurations.TEST_PROPERTIES.get("Browser"), Configurations.TEST_PROPERTIES.get("browserVersion"),
						Platform.WINDOWS, Configurations.TEST_PROPERTIES.get("lang"));
				DriverFactory factory = new DriverFactory();
				IDriver idriver = factory.getDriver(executionType);
				driver = idriver.getDriver(browser);
				context.setAttribute(context.getCurrentXmlTest().getName(), driver);
				driver = getWebDriverInstance(context);
				loginPage = new LoginPage(driver);

			}
		}catch (Exception e) {
			LOGGER.error("Error occured in beforeClass method ", e);
			ExtentManager.customExtentReportLog(Configurations.FAIL, ExtentManager.getTestInstance(), "Execution failed in Configuration <br>"+e.toString(), "");
			loginPage.closeBrowser(context);
		}
	}

	@AfterClass(alwaysRun=true)
	public void afterClass(ITestContext context) {

		try{
			if ((Configurations.TEST_PROPERTIES.get("launchBrowser")).toUpperCase().equals("CLASS")) {
				WebDriver webdriver = getWebDriverInstance(context);
				if (webdriver != null) {
					LOGGER.info(Utilities.getCurrentThreadId() + "Closing the instance:" + webdriver.toString());
					webdriver.close();
					webdriver.quit();
					context.removeAttribute(context.getCurrentXmlTest().getName());
					killingChromeDriver();


				}
			} else
				LOGGER.debug(Utilities.getCurrentThreadId()
						+ "Nothing to close in afterClass of the base class. Driver instance is null:");


		}catch (Exception e) {
			LOGGER.error("Exception in afterClass()", e);
		}
	}

	@BeforeMethod(alwaysRun=true)
	protected void beforeMethod(ITestContext context)  {


		try{
			if ((Configurations.TEST_PROPERTIES.get("launchBrowser")).toUpperCase().equals("METHOD")) {
				Browser browser = new Browser(Configurations.TEST_PROPERTIES.get("Browser"), Configurations.TEST_PROPERTIES.get("browserVersion"),
						Platform.WINDOWS, Configurations.TEST_PROPERTIES.get("lang"));
				DriverFactory factory = new DriverFactory();
				IDriver idriver = factory.getDriver(executionType);
				driver = idriver.getDriver(browser);
				context.setAttribute(context.getCurrentXmlTest().getName(), driver);
				driver = getWebDriverInstance(context);	
			}
			driver = getWebDriverInstance(context);
			db = new DatabaseMethods(driver);
			rowNumGraphicObject = db.getLastRowNumFromGSPSGraphicObjectTable() ;
			rowNumGraphicLevel = db.getLastRowNumFromGSPSGraphicLevelTable();
			batchMachineRecords = db.getLastRowNum("Batch", "BatchID");

			// This is temporary solution to convert Multiphase data to simple data
			if(dbCondition==0) {

				db.updateImagePosition(-92.5353463888461, "2.16.840.1.113669.632.21.56092013.488297.27283780762147235414855");
				db.updateImagePosition(-198.12988281251, "2.16.840.1.113669.632.21.1263928028.642942556.422068372420455022");
				db.updateImagePosition(-198.129882812501, "2.16.840.1.113669.632.21.1263928028.642942556.410046177221859522");
				db.updateImagePosition(-92.535346388847, "2.16.840.1.113669.632.21.384241572.3312864356.398785330133711067");
				db.updateImagePosition(0.5, "2.16.840.1.113669.632.21.2426340930.51258212.7116666092216681714");
				db.updateImagePosition(1, "2.16.840.1.113669.632.21.2426340930.51258212.7117084482116171714");
				db.updateImagePosition(1.5, "2.16.840.1.113669.632.21.2426340930.51258212.7116669762116131714");
				db.updateImagePosition(-92.535346388847, "2.16.840.1.113669.632.21.3123181089.1755674976.39878533013371106");
				db.updateImagePosition(99,"2.16.840.1.113669.632.21.2426340930.51258212.7116753132136001714");
				dbCondition=1;

			}



		} catch (Exception e) {
			LOGGER.error("Exception in beforeMethod()", e);
			ExtentManager.customExtentReportLog(Configurations.FAIL,ExtentManager.getTestInstance(), "Execution failed in Configuration <br>"+e.toString(),"");
			loginPage.closeBrowser(context);

		}
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod(ITestContext context, ITestResult result) {

		try{


			db.deleteDrawnAnnotation(Configurations.TEST_PROPERTIES.get("nsUserName"));
			db.deleteDrawnAnnotationForMachineData(Configurations.TEST_PROPERTIES.get("nsUserName"));
			db.deleteDrawnAnnotationFromGSPSData(rowNumGraphicObject,rowNumGraphicLevel,batchMachineRecords);
			db.deleteDrawnAnnotationOnMachineData(Configurations.TEST_PROPERTIES.get("nsUserName"));
			db.deleteCloneFromSeriesLevelForCAD("%_"+Configurations.TEST_PROPERTIES.get("nsUserName")+"_[1-10]");
			
			db.deleteUserSetLayout();
			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);
			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
			db.deleteRTafterPerformingAnyOperation(Configurations.TEST_PROPERTIES.get("nsUserName"));
			db.deleteAllUsers(Configurations.TEST_PROPERTIES.get("nsUserName"));
			   db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,Configurations.TEST_PROPERTIES.get("nsUserName"));
			
			// Get The Batch ID and Batch machine ID from DB
			HashMap<String, Integer> ids = db.getBatchIDAndBatchMachineID();

			if(ids.size()>0) {
				// Get the StudyInstanceUId from DB
				String studyInstanceID = db.getStudyInstanceIDFromBatchTable(ids.get(NSDBDatabaseConstants.BATCH_ID));

				// update the wiacloudresult to 0 for user defined entry
				if(!db.verifyStudyIsWiaSource(studyInstanceID))
					db.updateWIAResult(studyInstanceID);

				// delete all the entries from batch, batch machines and wiaresult  tables			
				db.deleteDataFromBatchBatchMachinesAndWiaResult(ids.get(NSDBDatabaseConstants.BATCH_ID),ids.get(NSDBDatabaseConstants.BATCH_MACHCINEID));

			}

			try {

				Header hd = new Header(driver);
				if(hd.isElementPresent(hd.userNameField))
					hd.logout();
			}catch(Exception e) {
				LOGGER.info("Logout is not working");
			}

			WebDriver webdriver = getWebDriverInstance(context);
			if (webdriver != null && (Configurations.TEST_PROPERTIES.get("launchBrowser")).toUpperCase().equals("METHOD")) { {
				LOGGER.info(Utilities.getCurrentThreadId() + "Closing the instance:" + webdriver.toString());
				webdriver.close();
				webdriver.quit();
				killingChromeDriver();
			}
			context.removeAttribute(context.getCurrentXmlTest().getName());

			} else
				LOGGER.debug(Utilities.getCurrentThreadId()
						+ "Nothing to close in afterMethod of the base class. Driver instance is null:");

		}catch (Exception e) {
			LOGGER.error("Exception in aftermethod()", e);
		}
	}

	protected void logErrorMessage(Throwable ex) {
		StringWriter stw = new StringWriter();
		PrintWriter pw = new PrintWriter(stw);
		ex.printStackTrace(pw);
		LOGGER.error(stw.toString());
	}

	protected static WebDriver getWebDriverInstance(ITestContext context) {
		return (WebDriver) context.getAttribute(context.getCurrentXmlTest().getName());
	}

	public WebDriver getDriver() {
		// TODO Auto-generated method stub
		return driver;
	}

	private void killingChromeDriver() throws IOException {

		// killing the Chromedriver exe process
		List<Integer> pids = new ArrayList<Integer>();
		String out;
		Process p = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq chromedriver*\"");
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((out = input.readLine()) != null) {
			String[] items = StringUtils.split(out, " ");
			if (items.length > 1 && StringUtils.isNumeric(items[1])) {
				pids.add(NumberUtils.toInt(items[1]));
			}
		}
		for(int pid : pids){
			String cmd = "taskkill /F /PID " + pid;
			Runtime.getRuntime().exec(cmd);

		}
	}

	private void killingBrowser() throws IOException {

		Process p = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq Chrome.exe*\"");
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String out;
		List<Integer> pids = null;
		while ((out = input.readLine()) != null) {
			String[] items = StringUtils.split(out, " ");
			if (items.length > 1 && StringUtils.isNumeric(items[1])) {
				pids.add(NumberUtils.toInt(items[1]));
			}
		}
		for(int pid : pids){
			String cmd = "taskkill /F /PID " + pid;
			Runtime.getRuntime().exec(cmd);

		}
	}
}
