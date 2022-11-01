package com.trn.ns.test.basic;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.ErrorOrLogoutPage;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.utilities.GSONParser;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MFASupportTest extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private RegisterUserPage register;
	private Header hd; 
	private ViewerPage viewerPage;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String secretKey = "test";
	String anotherSecretKey = "abc";
	String secretKeyUpdated ="secret";
	String newEncPassword = "SKJtcVzYzAkTHMZmHnTvZt0rnBUCA5fHH65o%2FVfG6nOu%2FU%2Ble5skvPFStgrMJftYKcDjNgFLnj2%2F7Js2Q0HD%2BoZRTC2yg19F5um1PwJXsKFfdRIuCcNUfcOOta1hWbu8JU6nCbLI5OQE2%2FCfAzZZ0%2FXlUkS3pd4WGIZov1HuO4o%3D%3A%3A%3AYbK79BxYWFCpsk4C5iuLpw%3D%3D";
	String newPassword = "newusertwo";
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/";
	String invalidOTP="123";
	private ViewerLayout layout;
	
	@BeforeMethod
	public void testDataCreation() throws InterruptedException, SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
			
			loginPage.login(username, password);
			patientPage = new PatientListPage(driver);
			patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
			register = new RegisterUserPage(driver);

			for(int i = 0 ;i<2;i++)
				register.createNewUser(LoginPageConstants.FIRST_NAME, LoginPageConstants.LAST_NAME, LoginPageConstants.SUPPORT_EMAIL,username+i, username+i, username+i);
			
		
			hd = new Header(driver);
			if(hd.isElementPresent(hd.userNameField))
				hd.logout();
	

	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Positive"})
	public void test01_US1743_TC8393_verifyMFAColumnInDB() throws SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFASecretKey column is present in dbo.UserAccount table.");

		db = new DatabaseMethods(driver);		 
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.USER_ACCOUNT_TABLE, NSDBDatabaseConstants.MFA_COLUMN_NAME),"Checkpoint[1/2]","Verifying the MFA column is present in table");
		db.assertTrue(db.getMFAColumnValue(username).isEmpty(),"Checkpoint[2/2]","verifying the value is default empty");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","US1835","DR2235","Positive","E2E","F930"})
	public void test02_US1743_TC8395_TC8397_TC8444_TC8531_TC8529_US1835_TC8620_DR2235_TC9102_US1744_TC8374_verifyMFAPopupPresence() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFA authetication pop up is displayed for only that user for whom MFASecretKey is ON in dbo.UserAccount table."
				+ "<br> Verify that MFASecretKey is getting enabled through Postman PUT API for specific user."
				+ "<br> Verify that PUT request API is successfully creating a new entry or updating a already existing entry for MFASecretKey in dbo.Useraccount table for the user."
				+ "<br> Verify the UserActionnLog when MfaSecretKey is not enabled in user does UI login"
				+ "<br> Verify the UserActionnLog when MfaSecretKey is enabled in user does UI login with correct MFA token.<br>"+
				"[Risk and Impact]: Verify that all the other MFA pop up error messages are displayed correctly.<br>"+
				"Verify MFA popup is appearing on logging with correct user name and password , also verify its location and UI.");

		loginPage = new LoginPage(driver);
		
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+0);
		body.put(NSDBDatabaseConstants.MFA_COLUMN_NAME,secretKey);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"2");

		
		DatabaseMethodsADB userActionLog = new DatabaseMethodsADB(driver);
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);

		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/9]","Verified status code 200 is received after updating mfasecret key");

		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		//TC8620
		loginPage.compareElementImage(protocolName, loginPage.mfaPopup, "Checkpoint[2/9]", "test02_2_MFA_PopUp");
		loginPage.mouseHover(loginPage.continueButton);
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage),LoginPageConstants.USERNAME_REQUIREDWARNINGMESSAGE.replaceAll(LoginPageConstants.USERNAMELABEL, "").trim(),"Checkpoint[3/9]","Verified error message on mousehover on continue button without entering token.");
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[4/9]", "verifying the mfa popup is displayed");
		loginPage.enterOTP(loginPage.getOTP(secretKey));

		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[5/9]", "verifying after entering the otp patient page is displayed");

		List<String> startPayload = userActionLog.getPayload(ActionLogConstant.USER_ACTION_LOGIN_END);
		String mfaEnabled = userActionLog.getKeyValue(startPayload.get(0),ActionLogConstant.IS_MFA_ENABLED);
		userActionLog.assertEquals(mfaEnabled,NSGenericConstants.BOOLEAN_TRUE, "Checkpoint[6/9]", "verifying the action log for isMFA enaled = true ");
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		hd = new Header(driver);
		hd.logout();

		loginPage.login(username+1, username+1);
		loginPage.assertFalse(loginPage.isElementPresent(loginPage.mfaPopup), "Checkpoint[7/9]", "verifying that no mfa popup is displayed");
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[8/9]", "Verifying the patient page is displayed when mfa is disabled after login");
		startPayload = userActionLog.getPayload(ActionLogConstant.USER_ACTION_LOGIN_END);
		mfaEnabled = userActionLog.getKeyValue(startPayload.get(0),ActionLogConstant.IS_MFA_ENABLED);
		userActionLog.assertEquals(mfaEnabled,NSGenericConstants.BOOLEAN_FALSE, "Checkpoint[9/9]", "verifying the user action log is containing false when mfa is disabled");

		hd = new Header(driver);
		hd.logout();
	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Positive","DR2209","E2E"})
	public void test03_US1743_TC8395_TC8397_TC8444_DR2209_TC8825_verifyOTPValidaitySpecificToSecretKey() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFA authentication key from winauth is user specific."
				+ "<br> Verify that MFASecretKey is getting enabled through Postman PUT API for specific user." 
				+ "<br> Verify that PUT request API is successfully creating a new entry or updating a already existing entry for MFASecretKey in dbo.Useraccount table for the user."
				+ "<br> Verify the grammatical syntax for MFA validation error message.");

		loginPage = new LoginPage(driver);

		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+1);
		body.put(NSDBDatabaseConstants.MFA_COLUMN_NAME,anotherSecretKey);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"2");

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/9]","Verified status code 200 is received after updating mfasecret key");

		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[2/9]", "verifying the mfa is displayed for user1");		
		loginPage.enterOTP(loginPage.getOTP(anotherSecretKey));		
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[3/9]", "verifying that error is displayed when otp for user2 is entered");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage), LoginPageConstants.MFA_VALIDATION_ERROR, "Checkpoint[4/9]", "verifying the error message");
		loginPage.enterOTP(loginPage.getOTP(secretKey));
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[5/9]", "verifying after entering the correct secret key otp user is landed on patient page");
		hd = new Header(driver);
		hd.logout();

		loginPage.login(username+1, username+1);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[6/9]", "verifying the mfa popup is displayed for user2");
		loginPage.enterOTP(loginPage.getOTP(secretKey));
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[7/9]", "verifying the error message is displayed when otp from another secret key is entered");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage), LoginPageConstants.MFA_VALIDATION_ERROR, "Checkpoint[8/9]", "verifying the error message");
		loginPage.enterOTP(loginPage.getOTP(anotherSecretKey));
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[9/9]", "verifying after entering the correct secret key otp user is landed on patient page");
		
	
	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Positive","DR2209","E2E"})
	public void test04_US1743_TC8399_TC8401_DR2209_TC8825_verifyMFATokenExpiry() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Winauth MFA token validity has time constrained of 30 sec."
				+ "<br> Verify that user is able to configure Winauth 2FA and able to get the 6 digit OTP."
				+ "<br> Verify the grammatical syntax for MFA validation error message.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[1/5]", "verifying the mfa popup is displayed");
		String otp = loginPage.getOTP(secretKey);

		loginPage.assertEquals(otp.length(), 6, "Checkpoint[2/5]", "verifying the otp length");
		loginPage.waitForTimePeriod(30100);
		loginPage.enterOTP(otp);

		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[3/5]", "verifying the error message if otp is entered after 30 sec");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage),  LoginPageConstants.MFA_VALIDATION_ERROR, "Checkpoint[4/5]", "verifying the error message text");

		loginPage.enterOTP(loginPage.getOTP(secretKey));

		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[5/5]", "verifying the user is landed after enter of correct otp");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Positive","E2E"})
	public void test05_US1743_TC8438_verifyMFATokenReusablewithIn30Sec() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that winauth token used can be reused to login again if user tries within 30 sec.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[1/3]", "verifying the mfa popup presence");
		String otp = loginPage.getOTP(secretKey);
		loginPage.enterOTP(otp);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[2/3]", "verifying user is on patient page");
		hd = new Header(driver);
		hd.logout();

		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.enterOTP(otp);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[3/3]", "verifying that otp is reused after entering the otp with in 30 sec");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Positive","E2E"})
	public void test06_US1743_TC8495_TC8530_verifyMFAPopupForNonUI() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFA popup is appearing when user tries to access Northstar application using Non UI login with valid credentials."
				+ "<br> Verify the UserActionnLog when MfaSecretKey is enabled in user does NONUI login with correct MFA token.");

		DatabaseMethodsADB userActionLog = new DatabaseMethodsADB(driver);
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		viewerAH4Url= viewerAH4Url+db.getStudyInstanceUID(patientName);
		
		db.updateSecretKey(username+0, secretKeyUpdated);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username+0); 
		hm.put(LoginPageConstants.USERNAME,username+0);

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[1/4]", "verifying the mfa popup displayed");
		loginPage.enterOTP(loginPage.getOTP(secretKeyUpdated));

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		layout = new ViewerLayout(driver);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[2/4]", "viewer page is displayed");
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/4]", viewerPage.getCurrentPageURL()+" is displaying");

		List<String> startPayload = userActionLog.getPayload(ActionLogConstant.USER_ACTION_LOGIN_END);
		String mfaEnabled = userActionLog.getKeyValue(startPayload.get(0),ActionLogConstant.IS_MFA_ENABLED);
		userActionLog.assertEquals(mfaEnabled,NSGenericConstants.BOOLEAN_TRUE, "Checkpoint[4/4]", "verifying that is mfa enabled tracked in logs");
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative"})
	public void test07_US1743_TC8496_verifyNoMFAPopupForNonUINoCredsPassed() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFA popup is not appearing when user tries to access Northstar application using Non UI login with invalid credentials.");

		loginPage = new LoginPage(driver);

		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username+0); 
		hm.put(LoginPageConstants.USERNAME,username);

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		loginPage.assertFalse(loginPage.isElementPresent(loginPage.mfaPopup), "Checkpoint[1/2]", "verifying no mfa popup displayed when non ui is accessed w/o valid credentials");
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_USER_CREDS_ERROR_MSG,"Checkpoint[2/2]","verifying that user is on errorpage");		


	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative"})
	public void test08_US1743_TC8497_verifyMFAPopupForNonUIWhenNoCreds() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFA popup is appearing after login page when user tries to access Northstar application using Non UI login without credentials.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		viewerAH4Url= viewerAH4Url+db.getStudyInstanceUID(patientName);
		db.updateSecretKey(username+0, anotherSecretKey);

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		loginPage.navigateToURL(URLConstants.BASE_URL+viewerAH4Url);
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying user should redirect to login page first");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Checkpoint[1/4]", loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username+0, username+0);

		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[2/4]", "verifying after login MFA popup is displayed");
		loginPage.enterOTP(loginPage.getOTP(anotherSecretKey));

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		layout = new ViewerLayout(driver);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[3/4]", "verrifying viewer is loaded");
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[4/4]", viewerPage.getCurrentPageURL()+" is displaying");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative","E2E"})
	public void test09_US1743_TC8499_verifyPasswordChange() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PUT request API is successfully updating password only for already existing user in dbo.Useraccount table.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+0);
		body.put(LoginPageConstants.PASSWORD,newEncPassword);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"1");

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);

		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,
				OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);

		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/7]","Verified status code 200 is received after updating mfasecret key");

		loginPage.login(username+0, username+0);
		loginPage.waitForElementVisibility(loginPage.loginFailedErrorMsg);
		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Checkpoint[2/7]", "Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Checkpoint[3/7]", "Correct error message is displayed");

		loginPage.login(username+0, newPassword);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[4/7]", "verifying the mfa popup displayed after login using updated password");		
		loginPage.enterOTP(loginPage.getOTP(anotherSecretKey));		
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[5/7]", "verifying the error message is displayed");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage), LoginPageConstants.MFA_VALIDATION_ERROR, "Checkpoint[6/7]", "verifying the validation message");
		loginPage.enterOTP(loginPage.getOTP(secretKey));
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[7/7]", "verifying user is landed on patient page after password change");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative","DR2209","E2E"})
	public void test10_US1743_TC8500_DR2209_TC8825_verifySecretKeyChange() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PUT request API is successfully updating password and MFASecretKey for already existing user in dbo.Useraccount table for the user."
				+ "<br> Verify the grammatical syntax for MFA validation error message.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+1);
		body.put(LoginPageConstants.PASSWORD,newEncPassword);
		body.put(NSDBDatabaseConstants.MFA_COLUMN_NAME,secretKey);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"3");

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);

		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,
				OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);

		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/6]","Verified status code 200 is received after updating mfasecret key");

		loginPage.login(username+1, username+1);
		loginPage.waitForElementVisibility(loginPage.loginFailedErrorMsg);
		loginPage.assertTrue(loginPage.loginFailedErrorMsg.isDisplayed(), "Checkpoint[2/6]", "Verifying the error message - Error message \""+loginPage.loginFailedErrorMsg.getText()+"\"is displayed ");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Checkpoint[3/6]", "Verifying error message text - Correct error message is displayed");

		loginPage.login(username+1, newPassword);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.enterOTP(loginPage.getOTP(anotherSecretKey));
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[4/6]", "verifying the error messsage is displayed when user tries to enter using otp generated for old secret key");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage), LoginPageConstants.MFA_VALIDATION_ERROR, "Checkpoint[5/6]", "verifying the error message");

		loginPage.enterOTP(loginPage.getOTP(secretKey));		
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[6/6]", "verifying user landed on patient page after entering the otp for changed secret key");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative"})
	public void test11_US1743_TC8508_verifyErrorMessageWhenIncorrectToken() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PUT api request is failing when invalid token is entered for PUT api request for that user and entry is not getting created in MFASecret key column under dbo.userAccount table.");

		loginPage = new LoginPage(driver);
		
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey);
		
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+1);
		body.put(NSDBDatabaseConstants.MFA_COLUMN_NAME,secretKeyUpdated);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"2");

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		keyVal.put(OrthancAndAPIConstants.HEADER_KEY, "abcdef");

		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,
				OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);

		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.UNAUTHORIZED_API_CODE,"Checkpoint[1/3]","Verified status code 401 is received after entering the incorrect token");

		loginPage.assertEquals(GSONParser.createHashMapFromJsonString(response.get(1).toString()).get(OrthancAndAPIConstants.ERROR_TAG).toString(),OrthancAndAPIConstants.TOKEN_EXPIRED,"Checkpoint[2/3]","verify token expiry message");
		loginPage.assertEquals(GSONParser.createHashMapFromJsonString(response.get(1).toString()).get(OrthancAndAPIConstants.ERROR_LEVEL).toString(),WordUtils.capitalizeFully(OrthancAndAPIConstants.ERROR_TAG),"Checkpoint[3/3]","verifying the error level");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative","E2E"})
	public void test12_US1743_TC8517_verifyNoMFAPopupOnRemovalOfSecretKey() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that MFA pop not appearing when entry for the user is manually deleted from dbo.UserAccount table in MfaSecretKey column.");

		db = new DatabaseMethods(driver);
		loginPage = new LoginPage(driver);
		db.updateSecretKey(username+0, secretKey);
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+0);
		body.put(LoginPageConstants.PASSWORD,newEncPassword);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"1");

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/4]","Verified status code 200 is received after updating password.");
		
		body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+1);
		body.put(LoginPageConstants.PASSWORD,newEncPassword);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"1");

		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/4]","Verified status code 200 is received after updating password.");
		
		for(int i =0;i<2;i++)
			db.updateSecretKey(username+i,"");

		for(int i =0;i<2;i++) {
			loginPage = new LoginPage(driver);
			loginPage.login(username+i, newPassword);
			loginPage.assertFalse(loginPage.isElementPresent(loginPage.mfaPopup), "Checkpoint[3/4]", "verifying mfa popup is displayed when secret key is changed from db");
			patientPage = new PatientListPage(driver);
			patientPage.waitForPatientPageToLoad();
			patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[4/4]", "verifying user is on patient page");
			hd = new Header(driver);
			hd.logout();
		}

	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","Negative","DR2209","DR2235","E2E"})
	public void test13_US1743_TC8519_DR2009_TC8825_DR2235_TC9102_verifyErrorWhenIncorrectOTPEntered() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();

		extentTest.setDescription("Verify that MFA popup is appearing when user tries to access Northstar application using Non UI login with valid credentials , but enter invalid OTP. <br>"+
		        "Verify look and feel of the MFA pop up <br>"
		+ " Verify the grammatical syntax for MFA validation error message.<br>"+
		  "[Risk and Impact]: Verify that all the other MFA pop up error messages are displayed correctly.");

		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKeyUpdated);
		viewerAH4Url= viewerAH4Url+db.getStudyInstanceUID(patientName);
		db.waitForTimePeriod(2000);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username+0); 
		hm.put(LoginPageConstants.USERNAME,username+0);

		//Accessing viewer page url from non ui login
		
		//TC8620: Enter invalid MFA token (6 digit number) and press Continue button.
		//Verify error message is displayed
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage = new LoginPage(driver);
		loginPage.navigateToURL(myURL);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.enterOTP("123456");		
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[1/8]", "verifying the error message");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage),LoginPageConstants.MFA_VALIDATION_ERROR, "Checkpoint[2/8]", "verifying the validation error message when incorrect toekn entered");
		
		loginPage.enterOTP(loginPage.getOTP(secretKeyUpdated));		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		layout = new ViewerLayout(driver);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[7/8]", "verifying the viewerpage is diplayed");
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[8/8]", viewerPage.getCurrentPageURL()+" is displaying");


	}
	
	//DR2235: [Hardening V1.0.3] On MFA pop up, error tooltip not getting displayed on hover on Continue button when it is clicked
	@Test(groups ={"Chrome","Edge","IE11","Negative","DR2235","US1744","F888"})
	public void test14_DR2235_TC9089_US1744_TC8377_TC8378_verifyErrorWhenEnteredOTPIsLessThanSixDigit() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();

		extentTest.setDescription("Verify that error tooltip is displayed on hover on continue button, for the MFA pop up for UI and NonUI login scenario.<br>"+
		"Verify that user is able to successfully access Patient list page after completing MFA token validation.<br>"+
		"Verify that user is navigating back to Login page on clicking anywhere on MFA pop up screen page.");

		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKeyUpdated);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username+0); 
		hm.put(LoginPageConstants.USERNAME,username+0);

		//Accessing viewer page url from non ui login
		
		//TC8620: Enter  MFA token less than 6 digit number and press Continue button.
		//Verify error message is displayed
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage = new LoginPage(driver);
		loginPage.navigateToURL(myURL);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.enterOTP(invalidOTP);		
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[1/6]", "verifying the error message for Non UI login scenario.");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage),LoginPageConstants.MFA_TOKEN_LESS_THAN_6_DIGIT, "Checkpoint[2/6]", "verifying the validation error message when entered token is less than 6 digit.s");
		
		loginPage.navigateToBaseURL();
		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.enterOTP(invalidOTP);		
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.mfaErrorMessage), "Checkpoint[3/6]", "verifying the error message for UI login scenario.");
		loginPage.assertEquals(loginPage.getText(loginPage.errorMessage),LoginPageConstants.MFA_TOKEN_LESS_THAN_6_DIGIT, "Checkpoint[4/6]", "verifying the validation error message when entered token is less than 6 digit.s");

		loginPage.navigateToBaseURL();
		loginPage.login(username+0, username+0);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(),"Checkpoint[5/6]","Verified that MFA pop up is visible.");
		loginPage.mouseHover(loginPage.brainImage);
		loginPage.click(25, 250);
		loginPage.assertFalse(loginPage.verifyMFAPopup(),"Checkpoint[6/6]","Verified that MFA pop up closed after click outside the pop up.");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1743","F888","US1744","Negative"})
	public void test15_US1743_TC8524_US1744_TC8377_verifyNoErrorInUpdateSecret() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PUT API not showing error when scan1 admin user authorization token is entered in Scan2  PUT api.<br>"+
		"Verify that user is able to successfully access Patient list page after completing MFA token validation.");
		
		db = new DatabaseMethods(driver);
		db.convertUserIntoAdmin(username+0);
		db.waitForTimePeriod(2000);

		loginPage = new LoginPage(driver);
		LinkedHashMap<String, String> body=new LinkedHashMap<String,String>();  
		body.put(LoginPageConstants.USERNAME, username+1);
		body.put(NSDBDatabaseConstants.MFA_COLUMN_NAME,secretKeyUpdated);
		body.put(OrthancAndAPIConstants.UPDATE_FIELD_TYPE,"2");

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username+0,username+0,OrthancAndAPIConstants.HEADER_KEY);

		List<Object> response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.API_BASE_URL,
				OrthancAndAPIConstants.UPDATE_USER_URL,keyVal,body);

		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/3]","Verified status code 200 is received after updating mfasecret key");

		loginPage.login(username+1, username+1);
		loginPage.waitForElementsVisibility(loginPage.mfaPopup);
		loginPage.assertTrue(loginPage.verifyMFAPopup(), "Checkpoint[2/3]", "verifying the mfa popup is displayed when secret key is changed when another admin token is passed during change");		
		loginPage.enterOTP(loginPage.getOTP(secretKeyUpdated));
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.size()>=1, "Checkpoint[3/3]", "verifying user is on patient page");


	}
	
	//US1744:[UI/UX] MFA token validation popup
	@Test(groups ={"Chrome","Edge","IE11","US1744","Negative","F888"})
	public void test16_US1744_TC8375_verifyNoErrorInUpdateSecret() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify MFA popup is not appearing on logging with incorrect user name or password.");
		
		db = new DatabaseMethods(driver);
		db.updateSecretKey(username+0, secretKey); 
		
		//TC8620: Enter  MFA token less than 6 digit number and press Continue button.
		//Verify error message is displayed
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		loginPage = new LoginPage(driver);
		loginPage.login(username+0, password);
		loginPage.assertFalse(loginPage.verifyMFAPopup(),"Checkpoint[1/3]","Verified that MFA pop up is not visible when incorrect username is provided.");
		
		loginPage.login(username, newPassword);
		loginPage.assertFalse(loginPage.verifyMFAPopup(),"Checkpoint[2/3]","Verified that MFA pop up is not visible when incorrect password is provided.");
		
		loginPage.login(username+2, newPassword);
		loginPage.assertFalse(loginPage.verifyMFAPopup(),"Checkpoint[3/3]","Verified that MFA pop up is not visible when incorrect username and password is provided.");
		
		
	}



}
