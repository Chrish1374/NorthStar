package com.trn.ns.test.basic;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.ErrorOrLogoutPage;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;

//import com.trn.ns.page.factory.StudyListPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LoginTest extends TestBase{


	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private static final String New_USERNAME = "newuser";
	private static final String New_PASSWORD = "Abc@2018#";
	private static final String Enc_Password = "8OPHhdnbNNTQtsdS1BJ9zgHA9Q68GTPB";
	private static final String Enc_SaltValue = "JktJeHBzva7u10gG/j+kNfhm7hkDHPr+";
	private static final String New_Enc_Password = "vIYosBdaUD7+eFyRgJmLxql5aJrs+K3l";
	private static final String New_Enc_SaltValue = "YsH58d+dtrr/TEWZTfvMhVk8Rb/iBSd8";

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	String user1 = "USER1"; String user1FirstName = "Steven"; String user1MiddleName = "Paul"; String user1LastName = "Jobs";
	String password1 = "password1"; String user1InSmall = "user1"; String userNameinMisc = "UsEr1";
	String user2 = "user2"; String user2FirstName = "Elon"; String user2MiddleName = "Reeve"; String user2LastName = "Musk";
	String password2 = "password2"; String password2InCaps = "PASSWORD2"; String password2InMisc = "PassWord2";
	String user2InCaps = "USER2";  String user2Misc = "uSeR2"; 

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();

	}

	// TC1: Basic (user name & pass) Authentication to the System

	@Test(groups ={"firefox","Chrome","Edge","IE11","US8","US1816","DE1829","Positive","F930","E2E"})
	public void test01_US8_TC1_US1816_TC8579_TC8580_TC8581_TC8583_DE1829_TC7390_verifyValidlogin() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Basic (user name & pass) Authentication to the System. <br>"+
		"Verify the location of login field on New Eureka login page. <br>"+
		"Verifying that 'Welcome to Eureka' logo is present on new Login page.<br>"+
		"Verify that Username and password labels for login fields are removed on new login page.<br>"+
		"Verify the new Login button UI.");

		loginPage = new LoginPage(driver);
		// Verifying the Username, password and button presence
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.usernameTextbox), "Checkpoint[1/10]", "username textbox is present");
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.passwordTextbox), "Checkpoint[2/10]", "password textbox is present");
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.signInButton), "Checkpoint[3/10]", "Sign in button is present");

		// verifying the text of labels -
		loginPage.assertEquals(loginPage.getAttributeValue(loginPage.usernameTextbox,LoginPageConstants.PLACEHOLDER),LoginPageConstants.ENTER_USERNAME, "Checkpoint[4/10]", "Placeholder is present inside the Username textbox.");
		loginPage.assertEquals(loginPage.getAttributeValue(loginPage.passwordTextbox,LoginPageConstants.PLACEHOLDER),LoginPageConstants.ENTER_PASSWORD , "Checkpoint[5/10]", "Placeholder is present inside the Password textbox.");
		loginPage.assertEquals(loginPage.getText(loginPage.signInButton), LoginPageConstants.SIGNINLABEL, "Checkpoint[6/10]", "Sign In button is present");
		loginPage.assertEquals(loginPage.getCssValue(loginPage.signInButton,NSGenericConstants.BACKGROUND_IMAGE),LoginPageConstants.MFA_CONTINUE_BUTTON_COLOR, "Checkpoint[7/10]","Verifying background color of Sign in button");

		// verifying login button is disabled
		loginPage.assertFalse(loginPage.isEnabled(loginPage.signInButton), "Checkpoint[8/10]", "Sign in button is disabled");
		loginPage.compareElementImage(protocolName,loginPage.loginPage ,"Checkpoint[9/10]","test01_06_Entire_Login Page");
		
		loginPage.login(username, password);
		loginPage.waitForTimePeriod("Medium");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("patient"), "Checkpoint[10/10]", "User is on page "+ loginPage.getCurrentPageURL());

	}

	// TC2 : Basic (user name & pass) Authentication to the System-Invalid credentials
	// TC438	Application or service interruption and error reporting-Login Error
	@Test(groups ={"firefox","Chrome","Edge","IE11","US8","US1816","DE1829","E2E","F930"})
	public void test02_US8_TC2_US11_TC438_US1816_TC8586_DE1829_TC7390_verifyInvalidlogin() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Basic (user name & pass) Authentication to the System-Invalid credentials : Application or service interruption and error reporting-Login Error. <br>"+
				"[Hardening] - Exception Caught message appearing when entering invalid login credentials. <b>"+
				"Verify that SIGN IN functionality on new Login page of Eureka when user tries with multiple incorrect combination.");

		loginPage = new LoginPage(driver);


		//		Launch the North Star URL to  navigate to the Login page , Enter a valid user name and invalid password.

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Enter a valid user name and invalid password.");
		loginPage.login(username, "scan12");		
		loginPage.waitForTimePeriod("Medium");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Verifying the user is on login page", "User is on login page " +loginPage.getCurrentPageURL());
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.loginFailedErrorMsg), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
		loginPage.assertEquals(loginPage.getText(loginPage.loginFailedErrorMsg),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");

		//		Enter a invalid user name and  valid password. Verify the  user is not  able to login to the system and there should be message indicating Login failed.

		loginPage.refreshWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Enter a invalid user name and  valid password.");
		loginPage.login("scan12",password);		
		loginPage.waitForTimePeriod("Medium");
		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
		loginPage.assertEquals(loginPage.getText(loginPage.loginFailedErrorMsg),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify warning message when username and password both are invalid.");
		loginPage.refreshWebPage();
		loginPage.login(user1, password1);	
		loginPage.waitForTimePeriod("Medium");
		loginPage.assertEquals(loginPage.getText(loginPage.loginFailedErrorMsg),LoginPageConstants.LOGINFAILUREERRORMESSAGE,"Verifying error message text","Verified error message when username and password both are incorrect.");
		
		//		Enter a  valid user name  and  do not enter any string in the  password field. -	Verify there is warning message indicating password is required .
		loginPage.refreshWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate the warning message for password field");
		loginPage.waitForLoginPageToLoad();
		loginPage.enterText(loginPage.usernameTextbox,username);
		loginPage.enterText(loginPage.passwordTextbox,"");
		loginPage.assertTrue(loginPage.verifyErrorMessage(loginPage.signInButton, loginPage.passwordWarningMsg, LoginPageConstants.PASS_REQUIREDWARNINGMESSAGE),"Verifying warning message text","Warning message for username field is displayed");	

		loginPage.refreshWebPage();
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate the warning message for username field");
		loginPage.enterText(loginPage.usernameTextbox,"");
		loginPage.enterText(loginPage.passwordTextbox,password);
		loginPage.assertTrue(loginPage.verifyErrorMessage(loginPage.signInButton,loginPage.usernameWarningMsg,LoginPageConstants.USERNAME_REQUIREDWARNINGMESSAGE), "Verifying warning message text", "Warning message for username field is displayed");


	}


	// TC773 : Verify that loading indicator is displayed on login with Enter Key
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test03_DE63_TC773_verifyLoginWithEnterKey() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify login using Enter Key");
		loginPage = new LoginPage(driver);
		//Launch the North Star URL to  navigate to the Login page , Enter a valid user name,password and press Enter key
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify login using Enter Key");
		loginPage.loginWithKeyPress(username,password);	
		loginPage.waitForLoadingIndicatorToDisappear();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL),"Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
	}

	// TC1878 : Remove "Forgot Password?" from Login screen
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test04_US595_TC1878_verifyForgotPasswordIsRemoved()  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US595_TC1878_Remove Forgot Password? from Login screen");
		loginPage = new LoginPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify 'Forgot Password?' link is removed from login Page");
		loginPage.assertFalse(loginPage.isElementPresent(By.linkText("Forgot Password?")), "Verifying that 'Forgot Password?' link is removed from Login Page", "'Forgot Password?' link is not visible on Login Page");
	}

	//TC2409 :  Verify the values under password field in database
	//TC2410 :  Change password and verify user authentication from login page
	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
	public void test05_US714_TC2409_TC2410_verifyEncryptedPasswordField() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the values under password field in database <br/> "
				+ "Change password and verify user authentication from login page ");

		DatabaseMethods db = new DatabaseMethods(driver);
		loginPage = new LoginPage(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying that the password field should be present in encrypted format in DB" );
		loginPage.assertNotEquals(password, db.getPasswordFromDB(username), "Verify that the password field should not contain 'scan'", "'scan' is not present in password field");

		//Adding new user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Adding new user");
		db.addUserInDB("", New_USERNAME, Enc_Password,Enc_SaltValue, "1900-01-01 00:00:00.000","","","","","","","2018-09-10 16:30:28.153","dark","0","","0","1","");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that new user should login successfully");
		loginPage.login(New_USERNAME, password);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying the Successful Login", "User is on page "+ patientPage.getCurrentPageURL());

		loginPage.refreshWebPage();
		//Update the password
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update password");
		db.updatePassword(New_USERNAME,New_Enc_Password,New_Enc_SaltValue);

		//Verify login with new credentials
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify that on entering new updated password, user should login successfully");
		loginPage.login(New_USERNAME, New_PASSWORD);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying the Successful Login", "User is on page "+ patientPage.getCurrentPageURL());

		loginPage.refreshWebPage();
		//verify login with old credentials
		loginPage.login(New_USERNAME, password);
		loginPage.waitForTimePeriod("Medium");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify that on entering old credentials, error message should display and user remains on login screen");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
	public void test06_US752_TC2698_TC2757_verifyAccessPageUsingNonUI() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify direct access the page URLs with authentication having correct credentials - Non UI login"
				+ "<br> Verify logout functionality for page access through Non UI login");

		loginPage = new LoginPage(driver);		


		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username); 
		hm.put(LoginPageConstants.USERNAME,password);

		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of patient page" );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);		
		patientPage.assertTrue(patientPage.patientNamesList.size()>0,"verifying that user is on patient page","verified");
		Header header = new Header(driver);
		header.logout();	
		ErrorOrLogoutPage logoutpage = new ErrorOrLogoutPage(driver);		
		logoutpage.assertEquals(logoutpage.getText(logoutpage.message),LoginPageConstants.SUCCESSFUL_LOGGED_OUT_MSG,"verifying that user is on log out page","verified");		

		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of viewer page" );
		myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103", hm);
		loginPage.openNewWindow(myURL);		
		ViewerPage vw = new ViewerPage(driver);
		vw.assertTrue(vw.isElementPresent(vw.getViewbox(1)),"verifying that user is on viewer page","verified");
		header.logout();
		logoutpage.assertEquals(logoutpage.getText(logoutpage.message),LoginPageConstants.SUCCESSFUL_LOGGED_OUT_MSG,"verifying that user is on log out page","verified");	
		loginPage.navigateToURL(myURL);
		vw.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGOUT_PAGE_URL),"verifying that user is not navigate to another page still on logout page","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the browser back" );
		logoutpage.navigateToBack();
		vw.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGOUT_PAGE_URL),"verifying that user is not navigate to another page still on logout page","verified");
		logoutpage.assertEquals(logoutpage.getText(logoutpage.message),LoginPageConstants.SUCCESSFUL_LOGGED_OUT_MSG,"verifying that user is on log out page","verified");	

		List<String> windowIDs = header.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		header.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
	public void test07_US752_TC2702_verifyErrorPageOnIncompleteCreds() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify direct access the page URLs with authentication having Incomplete credentials - Non UI login"
				+ "<br> Verify that login page should not display in case of browser back from Non UI login");

		loginPage = new LoginPage(driver);		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		hm.put(LoginPageConstants.PASSWORD,"");
		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of patient page with incomplete credentials" );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.openNewWindow(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INCOMPLETE_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");
		List<String> windowIDs = errorpage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		errorpage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of viewer page with incomplete credentials" );
		myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103", hm);
		loginPage.openNewWindow(myURL);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INCOMPLETE_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");
		windowIDs = errorpage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		errorpage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
	public void test08_US752_TC2703_TC2759_verifyErrorPageOnIncorrectCreds() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify direct access the page URLs with authentication having Incorrect credentials - Non UI login"
				+ "<br> Verify direct access the page URLs with non register user credentials - Non UI login");

		loginPage = new LoginPage(driver);		
		//		loginPage.waitForElementVisibility(loginPage.usernameLabel);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,"xyz");
		hm.put(LoginPageConstants.PASSWORD,"xyz");
		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of patient page with incorrect credentials" );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.openNewWindow(myURL);	
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");		
		List<String> windowIDs = errorpage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		errorpage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of viewer page with incorrect credentials" );
		myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103", hm);
		loginPage.openNewWindow(myURL);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");		
		windowIDs = errorpage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		errorpage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
	public void test09_US752_TC2704_verifyErrorPageOnIncorrectURL() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify direct access the page URLs with authentication having Incorrect Url and correct credentials - Non UI login");

		loginPage = new LoginPage(driver);		

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put("$"+LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword"));
		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of patient page having Incorrect Url and correct credentials - Non UI login" );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.openNewWindow(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INCOMPLETE_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");
		List<String> windowIDs = errorpage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		errorpage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of viewer page having Incorrect Url and correct credentials - Non UI login" );
		myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103", hm);
		loginPage.openNewWindow(myURL);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INCOMPLETE_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");
		windowIDs = errorpage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		errorpage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
	public void test10_US752_TC2758_verifyBrowserBackNavigation() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that login page should not display in case of browser back from Non UI login");

		loginPage = new LoginPage(driver);	
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username); 
		hm.put(LoginPageConstants.USERNAME,password);
		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of patient page " );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.openNewWindow(myURL);
		patientPage = new PatientListPage(driver);		
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.patientNamesList.size()>0,"verifying that user is on patient page","verified");
		patientPage.navigateToBack();
		patientPage.assertFalse(patientPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL),"verifying that login page is not displayed on browser back","verified");
		List<String> windowIDs = patientPage.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		patientPage.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying that direct access of viewer page " );
		myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103", hm);
		loginPage.openNewWindow(myURL);		
		ViewerPage vw = new ViewerPage(driver);
		vw.assertTrue(vw.isElementPresent(vw.getViewbox(1)),"verifying that user is on viewer page","verified");
		vw.navigateToBack();
		vw.assertFalse(vw.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL),"verifying that login page is not displayed on browser back","verified");

		windowIDs = vw.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		vw.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","DE1118"})
	public void test11_DE1118_TC4710_verifyNoConsoleErrorWhileLogin() {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no console error is logged when login in to the application");

		loginPage = new LoginPage(driver);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify no console error after right click on username textbox " );
		loginPage.performMouseRightClick(loginPage.usernameTextbox);
		loginPage.assertFalse(loginPage.isConsoleErrorPresent(), "Verify no console error when mouse right click on username textbox", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify no console error after right click on password textbox " );
		loginPage.performMouseRightClick(loginPage.passwordTextbox);
		loginPage.assertFalse(loginPage.isConsoleErrorPresent(), "Verify no console error when mouse right click on password textbox", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify no console error after left click on username textbox " );
		loginPage.click(loginPage.usernameTextbox);
		loginPage.assertFalse(loginPage.isConsoleErrorPresent(), "Verify no console error when mouse right click on password textbox", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify no console error after left click on password textbox " );
		loginPage.performMouseRightClick(loginPage.passwordTextbox);
		loginPage.assertFalse(loginPage.isConsoleErrorPresent(), "Verify no console error when mouse right click on password textbox", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify no console error when user login into application" );
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		loginPage.assertFalse(loginPage.isConsoleErrorPresent(), "Verify no console error when user login into application", "Verified");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1151"})
	public void test12_US1151_TC5777_verifyLoginUI() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the UI of the login screen on desktop");

		loginPage = new LoginPage(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify 'Required' message should be displayed below 'Username' and 'Password' text fields after hovering on Login button" );
		loginPage.mouseHover(loginPage.signInButton);
		loginPage.assertEquals(loginPage.usernameWarningMsg.getText(),LoginPageConstants.USERNAME_REQUIREDWARNINGMESSAGE, "Verifying warning message text-Required", "Warning message for username field is displayed");
		loginPage.assertEquals(loginPage.passwordWarningMsg.getText(),LoginPageConstants.PASS_REQUIREDWARNINGMESSAGE, "Verifying warning message text-Required", "Warning message for password field is displayed");
		loginPage.verifyEquals(loginPage.signInButton.getCssValue(NSGenericConstants.CSS_PRO_BACKGROUND),LoginPageConstants.MFA_CONTINUE_BUTTON_COLOR, "Verifying background color of login button","Verifying background color of login button");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify scrollbar is displayed after resizing the browser window");

		loginPage.resizeBrowserWindow(500, 500);
		loginPage.scrollIntoView(loginPage.iIconButton);
		loginPage.assertTrue(loginPage.iIconButton.isDisplayed(), "Verify 'i' icon is present on right bottom corner of login page after browser resize", "Verified");

	}

	@Test(groups ={"Chrome","Edge","IE11","Negative","DE1871"})
	public void test13_DE1871_TC7394_verifyUsernameIsCaseInsensitive(){

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to login when user provides the credential case-insensitive (UserName)");
		loginPage = new LoginPage(driver);

		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		loginPage.login(username, password);	

		RegisterUserPage registerUserPage = new RegisterUserPage(driver);

		registerUserPage.createNewUser(user1FirstName,user1MiddleName,user1LastName, LoginPageConstants.SUPPORT_EMAIL,user1,password1,password1);
		loginPage.logout();
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);

		loginPage.login(user1InSmall, password1);		
		patientPage = new PatientListPage(driver) ;
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/3]", "Verify login is successful  and patient page is rendered");

		loginPage.logout();
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
		loginPage.login(userNameinMisc, password1);		
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[2/3]", "Verify login is successful  and patient page is rendered");

		loginPage.logout();
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
		loginPage.login(user1, password1);	
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/3]", "Verify login is successful  and patient page is rendered");	
	}

	@Test(groups ={"Chrome","Edge","IE11","Negative","DE1871"})
	public void test14_DE1871_TC7396_verifyPasswordIsCaseSensitive() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is not able to login when user provides the credential case-insensitive (Pwd)");
		loginPage = new LoginPage(driver);

		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		loginPage.login(username, password);	

		RegisterUserPage registerUserPage = new RegisterUserPage(driver);
		registerUserPage.createNewUser(user2FirstName, user2MiddleName, user2LastName, LoginPageConstants.SUPPORT_EMAIL,user2, password2, password2);

		loginPage.logout();
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);

		loginPage.login(user2InCaps, password2InMisc);	
		loginPage.waitForTimePeriod("Medium");
		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");

		loginPage.refreshWebPage();
		loginPage.login(user2, password2InCaps);		
		loginPage.waitForTimePeriod("Medium");
		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Verifying the error message", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");

		loginPage.refreshWebPage();
		loginPage.login(user2Misc, password2);
		patientPage = new PatientListPage(driver) ;
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/3]", "Verify login is successful  and patient page is rendered");	
	}

	//US1816:Branding update for login form
	
	@Test(groups ={"Chrome","Edge","IE11","US1816","Positive","E2E","F930"})
	public void test15_US1816_TC8587_verifySignInForCorrectCredentials() throws InterruptedException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that SIGN IN functionality on new Login page of Eureka when user tries with correct credentials.");
		loginPage = new LoginPage(driver);

		loginPage.assertTrue(loginPage.verifyErrorMessage(loginPage.signInButton,loginPage.usernameWarningMsg,LoginPageConstants.USERNAME_REQUIREDWARNINGMESSAGE), "Checkpoint[1/5]", "Warning message for username field is displayed after click on sign in button.");
		loginPage.refreshWebPage();
		loginPage.assertTrue(loginPage.verifyErrorMessage(loginPage.signInButton, loginPage.passwordWarningMsg, LoginPageConstants.PASS_REQUIREDWARNINGMESSAGE),"Checkpoint[2/5]","Warning message for password field is displayed after click on sign in button.");	
		
		loginPage.refreshWebPage();
		loginPage.enterText(loginPage.usernameTextbox,username);
	    loginPage.enterText(loginPage.passwordTextbox,"");
	    loginPage.assertTrue(loginPage.verifyErrorMessage(loginPage.signInButton, loginPage.passwordWarningMsg, LoginPageConstants.PASS_REQUIREDWARNINGMESSAGE),"Checkpoint[3/5]","Warning message for password field is displayed when only username is provided");	

	    loginPage.refreshWebPage();
	    loginPage.enterText(loginPage.usernameTextbox,"");
	    loginPage.enterText(loginPage.passwordTextbox,password);
	    loginPage.assertTrue(loginPage.verifyErrorMessage(loginPage.signInButton,loginPage.usernameWarningMsg,LoginPageConstants.USERNAME_REQUIREDWARNINGMESSAGE), "Checkpoint[4/5]", "Warning message for username field is displayed when only password is provided");

	    //enter correct username and password
	    loginPage.refreshWebPage();
	    loginPage.login(username, password);
	    patientPage = new PatientListPage(driver) ;
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[5/5]", "Verify login is successful  and patient page is rendered");	
	    
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1819","Positive","F930"})
	public void test16_US1819_TC8590_TC8591_verifyLoginPageOnWindowResize() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the login UI after resizing it to multiple pixel lengths");
		loginPage = new LoginPage(driver);

		int x=loginPage.getXCoordinate(loginPage.brainImage);
		int y=loginPage.getYCoordinate(loginPage.brainImage);
		
		loginPage.assertTrue(x<loginPage.getXCoordinate(loginPage.loginContainer), "Checkpoint[1/10]","Verified that brain image frame is on the left and login form is towards the right on the login page, comparing with X cordinates");
		loginPage.assertTrue(y<loginPage.getYCoordinate(loginPage.loginContainer), "Checkpoint[2/10]","Verified that brain image frame is on the left and login form is towards the right on the login page, comparing with Y cordinates");
		
		
		loginPage.resizeBrowserWindow(780, 700);
		loginPage.assertEquals(loginPage.getXCoordinate(loginPage.brainImage),x, "Checkpoint[3/10]","Verified that after the resize, both the frames x cordinates start from 0 value");
		loginPage.assertEquals(loginPage.getYCoordinate(loginPage.brainImage),y, "Checkpoint[4/10]","Verified that after the resize, both the frames y cordinates start from 0 value");
		
		loginPage.assertTrue(x<loginPage.getXCoordinate(loginPage.loginContainer), "Checkpoint[5/10]","Verified that brain image frame is on the left and login form is towards the right on the login page");
		loginPage.assertTrue(y<loginPage.getYCoordinate(loginPage.loginContainer), "Checkpoint[6/10]","Verified that brain image frame is on the left and login form is towards the right on the login page");
		
		
		loginPage.resizeBrowserWindow(500, 500);
		
		int brainImage_height=loginPage.getHeightOfWebElement(loginPage.brainImage);
		int brainImage_width=loginPage.getWidthOfWebElement(loginPage.brainImage);
		
		int loginContainer_height=loginPage.getHeightOfWebElement(loginPage.loginContainer);
		int loginContainer_width=loginPage.getWidthOfWebElement(loginPage.loginContainer);

		loginPage.assertEquals(loginPage.getXCoordinate(loginPage.brainImage),x, "Checkpoint[7/10]","Verified that after the resize, both the frames x cordinates start from 0 value");
		loginPage.assertEquals(loginPage.getYCoordinate(loginPage.brainImage),y, "Checkpoint[8/10]","Verified that after the resize, both the frames y cordinates start from 0 value");
		
		
		loginPage.assertTrue(brainImage_height>loginContainer_height, "Checkpoint[9/10]","Verified that login frame is inside the brain image frame in terms of height");
		loginPage.assertTrue(brainImage_width>loginContainer_width,"Checkpoint[10/10]", "Verified that login frame is inside the brain image frame in terms of width");
				
	}

	@Test(groups ={"Chrome","Edge","IE11","US1819","Positive","F930","E2E"})
	public void test17_US1819_TC8661_TC8669_verifyTooltipPositionsOnBrowserResize() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Compare the tooltip image frame after resizing it to multiple pixel lengths");
		loginPage = new LoginPage(driver);

		loginPage.resizeBrowserWindow(800, 800);
		loginPage.mouseHover(loginPage.signInButton);
		loginPage.compareElementImage(protocolName, loginPage.loginFrame, "Checkpoint[1/3]: Verifying the tooltip image after resize to 800 px", "test17_01");
		
		loginPage.resizeBrowserWindow(500, 500);
		loginPage.mouseHover(loginPage.signInButton);
		
		loginPage.compareElementImage(protocolName, loginPage.loginFrame, "Checkpoint[2/3]: Verifying the tooltip image after resize to 500 px", "test17_02");
	
		loginPage.resizeBrowserWindow(200, 400);
		loginPage.mouseHover(loginPage.signInButton);
		
		loginPage.compareElementImage(protocolName, loginPage.loginFrame, "Checkpoint[3/3]: Verifying the tooltip image after resize to 200, 400 px", "test17_03");
		
				
	}

	@AfterMethod
	public void deleteUsers() throws SQLException {		
		db = new DatabaseMethods(driver);
		db.deleteAllUsers(Configurations.TEST_PROPERTIES.get("nsUserName"));
	}
}
