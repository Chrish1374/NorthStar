//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.sql.SQLException;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.openqa.selenium.By;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.ErrorOrLogoutPage;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.RegisterUserPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class LoginTest extends TestBase{
//
//	String protocolName;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ExtentTest extentTest;
//	private RegisterUserPage registerUserPage;
//	private static final String New_USERNAME = "newuser";
//	private static final String New_PASSWORD = "Abc@2018#";
//	private static final String Enc_Password = "8OPHhdnbNNTQtsdS1BJ9zgHA9Q68GTPB";
//	private static final String Enc_SaltValue = "JktJeHBzva7u10gG/j+kNfhm7hkDHPr+";
//	private static final String New_Enc_Password = "vIYosBdaUD7+eFyRgJmLxql5aJrs+K3l";
//	private static final String New_Enc_SaltValue = "YsH58d+dtrr/TEWZTfvMhVk8Rb/iBSd8";
//
//	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//
//	}
//	//Obsolete because DE1871
//	// TC360 : Verify that user is unable to login when user provides the credential case-insensitive
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test03_DE100_TC360_verifyUsernamePasswordAreCaseSensitive() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is unable to login when user provides the credential case-insensitive");
//		loginPage = new LoginPage(driver);
//
//
//		//		Launch the North Star URL to  navigate to the Login page , Enter a valid user name and  password in uppercase
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Enter both username and password in uppercase [SCAN/SCAN]");
//		loginPage.login("SCAN", "SCAN");		
//		loginPage.waitForTimePeriod("Medium");
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the user is on login page", "User is on login page " +loginPage.getCurrentPageURL());
//		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
//		//		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),loginPage.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
//
//		//		Launch the North Star URL to  navigate to the Login page , Enter username in lowercase and password in uppercase 
//		loginPage.refreshWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Enter username in lowercase and password in uppercase [scan/SCAN]");
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), "SCAN");		
//		loginPage.waitForTimePeriod("Medium");
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the user is on login page", "User is on login page " +loginPage.getCurrentPageURL());
//		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
//		//		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),loginPage.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
//
//
//		//		Launch the North Star URL to  navigate to the Login page , Enter username in uppercase and password in lowercase 
//		loginPage.refreshWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Enter username in lowercase and password in uppercase [SCAN/scan]");
//		loginPage.login("SCAN", Configurations.TEST_PROPERTIES.get("nsPassword"));		
//		loginPage.waitForTimePeriod("Medium");
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the user is on login page", "User is on login page " +loginPage.getCurrentPageURL());
//		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
//		//		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),loginPage.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
//
//
//		//		Launch the North Star URL to  navigate to the Login page , Enter both username and password in lowercase 
//
//		loginPage.refreshWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Enter both username and password in lowercase [scan/scan]");
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));		
//		loginPage.waitForTimePeriod("Medium");
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset")|| loginPage.getCurrentPageURL().contains("patient"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//
//
//	}
//
//	//Obsolete because DE1871
//	// TC361 : 	Verify that credentials are case sensitive and user is only allowed to login successfully
//	// Assumption that User, scan1/scan1 is available in DB
//	// Before running this test case add variable @ runtime -Djava.library.path=.\src\main\resources\com\jdbcdll
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
//	public void test04_DE100_TC361_verifyUsernamePasswordAreCaseSensitive() throws InterruptedException, SQLException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that credentials are case sensitive and user is only allowed to login successfully");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
////		db.addUserInDB("Automation","SCAN1","cF9VourEHgQ489fzTESlJvD9DltwsATH","bm6M7jRZsaylFecH4JAdIPwHQeijOC9+","1900-01-01 00:00:00.000","Automation","","test","support@terarecon.com","1","","2017-05-01 16:22:53.380","dark","0");
//
//		loginPage = new LoginPage(driver);
//
//
//		//			Provide the user name as 'SCAN1' and password as 'scan1' and click on the login button , User should not be able to logged into application 
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Enter username in lowercase [scan1/scan1]");
//		loginPage.login("scan1", "scan1");		
//		loginPage.waitForTimePeriod("Medium");
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the user is on login page", "User is on login page " +loginPage.getCurrentPageURL());
//		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
//		//		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),loginPage.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
//
//
//		//			Provide the user name as 'SCAN1' and password as 'SCAN1' and click on the login button , User should not be able to logged into application 
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Enter password in uppercase [scan1/SCAN1]");
//		loginPage.login("SCAN1", "SCAN1");		
//		loginPage.waitForTimePeriod("Medium");
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the user is on login page", "User is on login page " +loginPage.getCurrentPageURL());
//		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
//		//		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),loginPage.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
//
//		//			
//		//			Provide the user name as 'SCAN1' and password as 'scan1' and click on the login button , User should be able to logged into application and should be able to access all the pages 
//
//		loginPage.refreshWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Enter both username and password in correct case [SCAN1/scan]");
//		loginPage.login("SCAN1", "scan1");		
//		loginPage.waitForTimePeriod("Medium");
//
//		System.out.println(loginPage.getCurrentPageURL());
//
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("patient"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//
//	}
//
//	// TC2081 : Beta Flag - Version Information
//	@Test(groups ={"firefox","Chrome","Edge","IE11"},enabled = false)
//	public void test07_US612_TC2081_verifyVersionInfoOnAboutPage() 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Beta Flag - Version Information");
//		loginPage = new LoginPage(driver);
//		loginPage.viewAboutPage();
//		loginPage.switchToNewWindow(2);
//		loginPage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify detailed version and build numbers appear at the top of the about page");
//		String actualBuildVersion = StringUtils.substringBetween(loginPage.getTextForPage(), "(", ")");		
//		loginPage.assertEquals(actualBuildVersion, Configurations.TEST_PROPERTIES.get("build"), "Verify build number is displayed in the about page", "Build number is displayed in the about page");
//	}
//}
//
