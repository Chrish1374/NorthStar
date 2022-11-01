package com.trn.ns.test.basic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.LoginPageConstants;

import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PasswordPolicyPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.utilities.GSONParser;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class EncryptedPasswordTest extends TestBase{
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	//private SinglePatientStudyPage patientStudyPage;
		
	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath);
	
	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);
		
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String hostname = Configurations.TEST_PROPERTIES.get("nsHostName");
	String url ="http://"+hostname+"/api/login/authenticate?"+LoginPageConstants.USERNAME+"="+username+"&"+LoginPageConstants.PASSWORD+"=";
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	String username_1 = "user_1";
	private ViewerPage viewerpage;
	

	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test01_US1409_TC7990_verifyPasswordIsEncrypted() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in client side when User logins -Login Screen");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		db=new DatabaseMethods(driver);

		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);


		patientPage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/4]","Verifying the url is present in network tab");
		patientPage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/4]","Verifying password "+password+" is not present in the request hence they are encrypted");	
		Header header = new Header(driver);

		List<String> passwords = new ArrayList<String>();
		String authenticateURL ="";
		for(int i =0 ;i<2 ;i++) {
			header.logout();
			loginPage.waitForLoginPageToLoad();
			loginPage.login(username, password);

			authenticateURL= loginPage.getAuthenticateURLFromNetworkTab();
			passwords.add(loginPage.decodeQueryString(authenticateURL).get(LoginPageConstants.PASSWORD));
			patientPage.waitForPatientPageToLoad();
			patientPage.assertNotEquals(passwords.get(i), password, "Checkpoint[3/4]", "Verifying that over successive login and logout the password sent is not scan which is encrypted password");
		}

		patientPage.assertNotEquals(passwords.get(0), passwords.get(1), "Checkpoint[4/4]", "Verifying that encrypted password is not same in differnt logins -means every login has different encrypted password");





	}

	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test02_US1409_TC7991_verifyPasswordIsEncryptedOnRegisterPage() 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in client side when Registering  NS User");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		db=new DatabaseMethods(driver);

		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);

		RegisterUserPage register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		String authenticateURL ="";
		authenticateURL= register.getUsersURLFromNetworkTab();
		HashMap<String, Object> values = GSONParser.createHashMapFromJsonString(authenticateURL);
		patientPage.assertNotEquals(values.get(LoginPageConstants.PASSWORD).toString(), username_1, "Checkpoint[1/2]", "Verifying while creating the user , password is shared in in encrypted form data post in Request");
		patientPage.assertNotEquals(values.get("confirmpassword").toString(), username_1, "Checkpoint[2/2]", "Verifying while creating the user , confirmed password is shared in encrypted form data post in Request");
		
		

	}

	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test03_US1409_TC7992_verifyPasswordIsEncryptedOnPasswordPolicyPage() 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in client side when user tries to access the password policy page.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		db=new DatabaseMethods(driver);

		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);

		PasswordPolicyPage passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/2]","Verifying that on access of password policy page, in authenticated request password is encrypted");
		passwordPolicyPage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/2]","Verifying that on access of password policy page, in authenticated request password is not same as password used for login ");	
		

	}

	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test04_US1409_TC7993_verifyPasswordIsEncryptedOnGettingToken() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in server side when user tries to get the authentication token");
		
		db.truncateLogTable();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
		RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		
		db.assertEquals(db.getFullMessageFromLogContainsString(OrthancAndAPIConstants.PASSWORD_IS_ENCRYPTED+"%"),OrthancAndAPIConstants.PASSWORD_IS_ENCRYPTED,"Checkpoint[1/1]","Verifying the message 'password is encrypted' in Log table when token is generated");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test05_US1409_TC7994_verifyPwdEncryptedOnPatientPageAccess() 	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in client side when user tries to access study list page (Direct URL access)");
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.navigateToURL(myURL);
		
		patientPage = new PatientListPage(driver);
		
		patientPage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/2]","Verifying the URL is present in the Authenticate request when accessed the NON UI Patient page with Password");
		patientPage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/2]","Verifying the password is not present in URL hence encrypted");	
		

	}

	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test07_US1409_TC7996_verifyPasswordIsEncryptedOnDirectViewerPageAccess() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted for non-UI logon to NorthStar and opening Viewer using URL Launch ('StudyUid' as query parameter)");
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login");
	    String myURL =loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+db.getStudyInstanceUID(patientName), hm);
	
		loginPage.navigateToURL(myURL);		
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		
		viewerpage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/2]","Verifying the URL is present in the Authenticate request when accessed the NON UI viewer page with Password");
		viewerpage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/2]","Verifying the password is not present in URL hence encrypted");	
		

	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test08_US1409_TC7998_verifyPwdEncryptedOnViewerPageAccessUsingAccessionNo() throws SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in the client side for non UI login into Northstar using URL launch and accession number.");
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,db.getAccessionNumber(patientNameTCGA));
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login");
	    String myURL =loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);		
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		
		viewerpage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/2]","Verifying the URL is present in the Authenticate request when accessed the NON UI URL launch with accession number with Password");
		viewerpage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/2]","Verifying the password is not present in URL hence encrypted");	
		

	}
	
	@AfterMethod
	public void deleteUsers() throws SQLException {		
		db = new DatabaseMethods(driver);
		db.deleteAllUsers(Configurations.TEST_PROPERTIES.get("nsUserName"));
	}




}