package com.trn.ns.test.basic;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.ErrorOrLogoutPage;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class NavigationToErrorPages extends TestBase {
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private DatabaseMethods db;
	private ErrorOrLogoutPage errorpage;
	private PatientListPage patientPage;

	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String dummyIssuerID="test";
	String invalidPatientID="1234";
	String invalidStudyUID="1111";
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "US2030","F931","E2E"})
	public void test01_US2030_TC8911_verifyNewUIForInvalidCredentialsError() throws SQLException 
	{
		String invalidPassword = "scan1"; //Invalid password assigned
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the non-UI login into Northstar with the invalid credentials.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		
		LinkedHashMap<String, String> queryStringParameter = new LinkedHashMap<String,String>();  
		queryStringParameter.put(LoginPageConstants.USERNAME, username);
		queryStringParameter.put(LoginPageConstants.PASSWORD, invalidPassword);

		//Accessing viewer page URL from non-UI login with invalid credentials
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login with invalid credentials");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/" + db.getStudyInstanceUID(patientName), queryStringParameter);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_USER_CREDS_ERROR_MSG, "Checkpoint[2/12]", "verified display of Invalid credentials error message");
		AssertPageUIElements();
		AssertErrorMessageUIElements();
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "US2030","F931","E2E"})
	public void test02_US2030_TC8919_verifyNewUIForInvalidPatientIDError()
	{
		String invalidPatientID = "123";
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the non-UI login into Northstar with the invalid Patient ID.");

		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> queryStringParameter = new LinkedHashMap<String,String>();  
		queryStringParameter.put(LoginPageConstants.USERNAME, username);
		queryStringParameter.put(LoginPageConstants.PASSWORD, password);

		//Accessing patient page URL from non-UI login with invalid patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login with invalid Patient ID");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL + "/" + invalidPatientID , queryStringParameter);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG, "Checkpoint[2/12]", "verified display of Invalid credentials error message");
		AssertPageUIElements();
		AssertErrorMessageUIElements();
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "US2030","F931","E2E"})
	public void test03_US2030_TC8917_verifyNewUIForInvalidStudyUIDError()
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the non-UI login into Northstar with the invalid study UID.");

		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> queryStringParameter = new LinkedHashMap<String,String>();  
		queryStringParameter.put(LoginPageConstants.USERNAME, username);
		queryStringParameter.put(LoginPageConstants.PASSWORD, password);
		
		String invalidStudyUID = "2.16.840.1.113669.632.21.486081.jkj678667tugf486212.16416582522216879.1424103";

		//Accessing viewer page URL from non-UI login with invalid study UID.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login with invalid study UID");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+invalidStudyUID, queryStringParameter);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_SUID_ERROR_MSG, "Checkpoint[2/12]", "verified display of Invalid study UID error message");
		AssertPageUIElements();
		AssertErrorMessageUIElements();
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "US2030","F931","E2E"})
	public void test04_US2030_TC8877_verifyLogoutPageOnLogoutFromViewerPage() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the non UI login into Northstar and on logout display logout page");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		
		LinkedHashMap<String, String> queryStringParameter = new LinkedHashMap<String,String>();  
		queryStringParameter.put(LoginPageConstants.USERNAME, username);
		queryStringParameter.put(LoginPageConstants.PASSWORD, password);

		//Accessing viewer page URL from non-UI login with invalid credentials
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for logout page");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/" + db.getStudyInstanceUID(patientName), queryStringParameter);
		loginPage.navigateToURL(myURL);
		ViewerPage viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		loginPage.logout();
		errorpage = new ErrorOrLogoutPage(driver);
		
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(URLConstants.LOGOUT_PAGE_URL), "Checkpoint[1/8]", "verified logout page is displayed");
		errorpage.assertEquals(errorpage.getText(errorpage.logoutMsg), LoginPageConstants.SUCCESSFUL_LOGGED_OUT_MSG, "Checkpoint[2/8]", "verified display of logout error message");
		
		int x_errorMsg = errorpage.getXCoordinate(errorpage.logoutMsg);
		int x_brainImage = errorpage.getXCoordinate(loginPage.brainImage);
		errorpage.assertTrue(x_brainImage < x_errorMsg, "Checkpoint[5/8]", "Verified that brain image is on the left and error message container is towards the right on the error page");
		
		AssertPageUIElements();
	}
	
	//DR2382: Non UI Login: Error messages are not correct when query parameters are wrong
	@Test(groups ={"Chrome","Edge","IE11","Negative", "DR2382"})
	public void test05_DR2382_TC9323_TC9340_verifyErrorForNonUIWhenParameterIsCorrectButForDifferentPatient() throws SQLException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non UI Login: Verify Error messages when query parameters are wrong. <br>"+
        "Re-Execute TC9043 -  Verify Non UI Login to Eureka application when correct query parameter with correct patient id and correct study instance UID  are provided but for different patient.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		
		String studyUID_Liver9=db.getStudyInstanceUID(liver9PatientName);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  

		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, studyUID_Liver9);
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login with correct patient id and correct study instance UID  are provided but for different patient.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/4]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.ERROR_FOR_WRONG_QUERYPARAMETER, "Checkpoint[2/4]", "Verified error message when correct SUID and PID are provided but for different patient.");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login with correct patient id and incorrect issuer ID.");
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID, dummyIssuerID);
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);
		
	    myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.openNewWindow(myURL);
		errorpage.waitForErrorPageToLoad();
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[3/4]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_ISSUER_OF_PATIENTID_ERROR_MSG, "Checkpoint[4/4]", "Verified error message when invalid issuer ID is provided.");
			
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive", "DR2382"})
	public void test06_DR2382_TC9335_TC9341_verifyStudyPageWhenCorrectSUIDIsProvided() throws SQLException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9036 - Verify that user will be  redirected to the error page when invalid combination of patient ID and study instance UID is provided.<br>"+
		"Re-Execute TC9044 - Verify Non UI Login to Eureka application when correct query parameter with correct patient id and correct study instance UID  for the DP.4 patient are provided.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		String studyDescription=db.getStudyDescription(patientName);
		String studyUID_AH4=db.getStudyInstanceUID(patientName);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  

		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, "1234");
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login when invalid Study UID is provided.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/6]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_SUID_ERROR_MSG, "Checkpoint[2/6]", "erified error message when invalid study UID is provided.");
	
		//TC9341 and TC9335 checkpoint2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify study page when correct study UID is provided in URL.");
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, studyUID_AH4);
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);
		
	    myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.openNewWindow(myURL);
		PatientListPage patientPage=new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/6]", "Verified current page URL for patient study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientName, "Checkpoint[4/6]", "Verified correct patient name is displayed on Patient list page.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientInformationOnStudyPage.get(0)), patientName, "Checkpoint[5/6]", "Verified correct patient name is displayed on Patient study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDescriptionList.get(0)),studyDescription , "Checkpoint[6/6]", "Verified correct study description name is displayed on Patient study page.");
			
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "DR2382"})
	public void test07_DR2382_TC9336_verifyErrorForNonUIWhenUndefinedQueryParameterIsUsed()
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9040 - Verify Non UI Login to Eureka application when undefined query parameter is provided.");

		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  

		hm.put(LoginPageConstants.STUDY_UID, invalidStudyUID);
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login when undefined query parameter is used.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +invalidPatientID, hm);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/2]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_PARAMETER_SUID_ERROR_MSG, "Checkpoint[2/2]", "Verified error message when undefined query parameter for Study UID is used. ");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "DR2382"})
	public void test08_DR2382_TC9337_verifyErrorForNonUIWhenInvalidPatientIDAndStudyUIDIsUsed() 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9041 - Verify Non UI Login to Eureka application when correct query parameter with wrong patient id and study instance UID are provided.");

		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  

		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, invalidStudyUID);
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login when incorrect patient ID and Study UID is used.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +invalidPatientID, hm);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/2]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG, "Checkpoint[2/2]", "Verified error message when invalid patient ID and invalid Study UID is used.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative","DR2382","DR2914","F931"})
	public void test09_DR2382_TC9338_DR2914_TC11272_verifyErrorForNonUILoginWhenCorrectPatientIDAndIncorrectStudyUIDIsUsed() 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9042 -  Verify Non UI Login to Eureka application when  correct query parameter with correct patient id and wrong study instance UID  are provided. <br>"+
		"Verify the username and password field accepts all alphabets on login page after redirected from Error page.");

		loginPage = new LoginPage(driver);
	
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  

		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, invalidStudyUID);
		hm.put(LoginPageConstants.USERNAME, username);
		hm.put(LoginPageConstants.PASSWORD, password);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login when correct patient ID and wrong study UID is used.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/3]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_SUID_ERROR_MSG, "Checkpoint[2/3]", "Verified error message when valid patient ID and Invalid Study UID is provided.");
		errorpage.click(errorpage.goBackToLoginbutton);
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);
		PatientListPage patientpage=new PatientListPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/3]", "Verified that username and password field is accepting the text and user login into application again.");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Negative", "DR2382"})
	public void test10_DR2382_TC9342_verifyErrorForUILoginWhenCorrectPatientIDAndIncorrectStudyUIDIsUsed() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9045 -  Verify Non UI Login to Eureka application when correct patient id and wrong study UID are provided, without providing username and password.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  

		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, invalidStudyUID);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for UI login when correct patient ID and wrong study UID is used.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		loginPage.login(username, password);
		
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/2]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.INVALID_URL_SUID_ERROR_MSG, "Checkpoint[2/2]", "Verified error message when correct patient ID and wrong study UID is used.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive", "DR2382"})
	public void test11_DR2382_TC9343_verifyStudyPageUILoginWhenCorrectPatientIDUsed() throws SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9046 -  Verify Non UI Login to Eureka application when correct patient id without any other query parameters are provided.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		String studyDescription=db.getStudyDescription(patientName);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify user is successfully navigate to study list page when correct patient ID is provided.");
		loginPage.navigateToURL(URLConstants.BASE_URL + URLConstants.PATIENT_LIST_URL +"/" +patientID);
		loginPage.login(username, password);
		
		patientPage=new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/4]", "Verified patients page url after successful login.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientName, "Checkpoint[2/4]", "Verified patient name on patient list page.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientInformationOnStudyPage.get(0)), patientName, "Checkpoint[3/4]", "Verified patient name on study list pane.");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDescriptionList.get(0)),studyDescription , "Checkpoint[4/4]", "Verified study description on study pane.");
	
	}

	@Test(groups ={"Chrome","Edge","IE11","Negative", "DR2382"})
	public void test12_DR2382_TC9344_verifyErrorForUILoginWhenParameterIsCorrectButForDifferentPatient() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9047 - Verify Non UI Login to Eureka application when  correct query parameter with correct patient id and correct study instance UID are provided, but for a different patient without providing username and password.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		
		String studyUID_Liver9=db.getStudyInstanceUID(liver9PatientName);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, studyUID_Liver9);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for UI login with correct patient id and correct study instance UID are provided, but for a different patient without providing username and password.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		loginPage.login(username, password);
		
		errorpage = new ErrorOrLogoutPage(driver);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/2]", "Verified current page URL for error page.");
		errorpage.assertEquals(errorpage.getText(errorpage.message), LoginPageConstants.ERROR_FOR_WRONG_QUERYPARAMETER, "Checkpoint[2/2]", "Verified error message with correct patient id and correct study instance UID are provided, but for a different patient without providing username and password..");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive", "DR2382"})
	public void test13_DR2382_TC9345_verifyNonUIStudyPageUsingCorrectPatientIDAndIssuerID() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9048 -  Verify Non UI Login to Eureka application when correct patient Id, issuerOfpatientId are provided, without username and password.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		String studyDescription=db.getStudyDescription(patientName);
		String issuerID_AH4=db.getIssuerOfPatientID(patientID);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID, issuerID_AH4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify study page using correct issuer ID and Patient ID.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		loginPage.login(username, password);
		
		patientPage=new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/4]", "Verified current page URL using correct issuer ID and Patient ID.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientName, "Checkpoint[2/4]", "Verified patient name on patient List pane.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientInformationOnStudyPage.get(0)), patientName, "Checkpoint[3/4]", "Verified patient name on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDescriptionList.get(0)),studyDescription , "Checkpoint[4/4]", "Verified study description on study pane.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive", "DR2382"})
	public void test14_DR2382_TC9346_verifyIStudyPageUsingCorrectPatientIDAndIssuerIDForUILogin() throws SQLException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-Execute TC9048 -  Verify Non UI Login to Eureka application when correct patient Id, issuerOfpatientId are provided, without username and password.");

		loginPage = new LoginPage(driver);
		db = new DatabaseMethods(driver);
		String studyDescription=db.getStudyDescription(patientName);
		String issuerID_AH4=db.getIssuerOfPatientID(patientID);
		String studyUID=db.getStudyInstanceUID(patientName);
		
		LinkedHashMap<String, String> hm = new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID, issuerID_AH4);
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API, studyUID);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify study page using correct issuer ID and Patient ID.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/" +patientID, hm);
		loginPage.navigateToURL(myURL);
		loginPage.login(username, password);
		
		patientPage=new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/4]","Verified current page URL using correct issuer ID and Patient ID.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientName, "Checkpoint[2/4]", "Verified patient name on patient List pane.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientInformationOnStudyPage.get(0)), patientName, "Checkpoint[3/4]", "Verified patient name on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDescriptionList.get(0)),studyDescription , "Checkpoint[4/4]","Verified study description on study pane.");
	
	}
	
	//Assert error messages and button functionality on the error page.
	private void AssertErrorMessageUIElements() {
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL), "Checkpoint[1/12]", "verified error page is displayed");
		errorpage.assertTrue(errorpage.isElementPresent(errorpage.headerMessage), "Checkpoint[3/12]", "verified brain svg is displayed on the error page");
		errorpage.assertEquals(errorpage.getText(errorpage.headerMessage), LoginPageConstants.ERROR_HEADER_MESSAGE, "Checkpoint[4/12]", "Verified error header message displayed on the error page");
		
		errorpage.assertTrue(errorpage.isElementPresent(errorpage.goBackToLoginbutton), "Checkpoint[6/12]", "Verified go back to login button from error page");
		

		int x_errorMsg = errorpage.getXCoordinate(errorpage.message);
		int x_brainImage = errorpage.getXCoordinate(loginPage.brainImage);
		errorpage.assertTrue(x_brainImage < x_errorMsg, "Checkpoint[8/12]", "Verified that brain image is on the left and error message container is towards the right on the error page");
		
		//Go back to Login button click
		errorpage.click(errorpage.goBackToLoginbutton);
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Checkpoint[12/12]", "verified login page is displayed");
	}
	
	//Assert background image, copyright version elements and responsiveness of the page.
	private void AssertPageUIElements() {
		errorpage.assertEquals(errorpage.getText(errorpage.versionfooter), LoginPageConstants.COPYRIGHT_INFO, "Checkpoint[5/12]", "Verified copyright/version details displayed on the error page");
		errorpage.assertTrue(errorpage.getCssValue(loginPage.brainImage, NSGenericConstants.BACKGROUND_IMAGE).contains(LoginPageConstants.BRAINIMAGE_BACKGROUNDIMAGE_NAME), "Checkpoint[7/12]", "Verified brain image displayed on the error page");
		
		//Resize window 
		int x = errorpage.getXCoordinate(loginPage.brainImage);
		int y = errorpage.getYCoordinate(loginPage.brainImage);
				
		errorpage.resizeBrowserWindow(780, 450);
				
		errorpage.assertEquals(errorpage.getXCoordinate(loginPage.brainImage), x, "Checkpoint[9/12]", "Verified that after the resize, both the frames x cordinates start from 0 value");
		errorpage.assertEquals(errorpage.getYCoordinate(loginPage.brainImage), y, "Checkpoint[10/12]", "Verified that after the resize, both the frames y cordinates start from 0 value");
		errorpage.assertTrue(x < errorpage.getXCoordinate(errorpage.errorMsgContainer), "Checkpoint[11/12]", "Verified that brain image is on the left and error message container is towards the right on the error page");
	}
}
