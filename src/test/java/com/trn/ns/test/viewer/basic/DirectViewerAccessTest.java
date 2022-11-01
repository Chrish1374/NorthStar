package com.trn.ns.test.viewer.basic;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.ErrorOrLogoutPage;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DirectViewerAccessTest extends TestBase{
	private LoginPage loginPage;
	private ExtentTest extentTest;
    private HelperClass helper;
	private ViewerPage viewerPage;

	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	//AH4 patient Url to be accessed
	private String viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103";

	//Invalid URL
	private String invalidSuid_ViewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212" , 
			noSuid_ViewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/" , suid_WithSpecialChar1 = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.11366$9.632.21.486081.486212$" ,
			suid_WithSpecialChar2 = URLConstants.VIEWER_PAGE_URL+"2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103%24",
		suid_WithSpecialChar3 = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103%24";
	
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	
	private String terareconURL = "https://www.terarecon.com/";
	
	private PatientListPage patientListPage;
	
	@Test(groups ={"firefox","Chrome","Edge","IE11","US729","Sanity"})
	public void test01_US729_TC2802_verifyNonUIAccessWithValidSUID() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non UI Login : Verify that user should be redirected to viewer page when valid credentials and valid study instance UID is provided");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the launching of viewer page from non-ui login with valid Study UID");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForTimePeriod(1000);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US729","Sanity"})
	public void test02_US729_TC2803_verifyNonUIAccessWithInvalidSUID() {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non UI Login : Verify that user should be redirected to error page when valid credentials or invalid study instance UID is provided");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the display of error page when invalid Study UID is enterred");
		String myURL = loginPage.getNonUILaunchURL(invalidSuid_ViewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");
		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should not display", errorpage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US729"})
	public void test03_US729_TC2804_verifyNonUIAccessWithInvalidCredentials() {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non UI Login : Verify that user should be redirected to error page when invalid credentials or valid study instance UID is provided");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,"xyz");
		hm.put(LoginPageConstants.PASSWORD,"xyz");

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the display of error page when invalid credentials and valid Study UID is enterred");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_USER_CREDS_ERROR_MSG,"verifying that user is on errorpage","verified");		
		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should not display", errorpage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US729"})
	public void test04_US729_TC2808_verifyNonUIAccessWithNoSUID() {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non UI Login : Verify that user should be redirected to error page when valid credentials and no study instanceUiID provided");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the display of error page when blank Study UID is enterred");
		String myURL = loginPage.getNonUILaunchURL(noSuid_ViewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);	
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");		
		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should not display", errorpage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US729","US1500","Negative"})
	public void test05_US729_TC2811_US1500_TC8028_DE1974_TC8032_verifyUIAccessWithValidSUID() {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("UI Login : Verify that user should be redirected to login page and then on viewer page when valid study instance UID is provided"
				+ "<br> [Risk and Impact]: Verify that user is able to access the viewer page using study instance ID, Patient ID."
				+ "<br> User is not getting redirected to the respective page after login when direct that page url is accessed (without credentials)");

		loginPage = new LoginPage(driver);

		//Accessing viewer page url from ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying the launching of viewer page from ui login with valid Study UID");

		loginPage.navigateToURL(URLConstants.BASE_URL+viewerAH4Url);
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying user should redirect to login page first");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username, password);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying user should redirect to viewer page after inputing valid credentials");
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test06_US729_TC2812_verifyUIAccessWithInvalidSUID() {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("UI Login : Verify that user should be redirected to login page and then on error page when invalid study instance UID is provided");

		loginPage = new LoginPage(driver);

		//Accessing viewer page url from ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying the display of error page when invalid Study UID is enterred");

		loginPage.navigateToURL(URLConstants.BASE_URL+invalidSuid_ViewerAH4Url);
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying user should redirect to login page first");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username, password);

		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying user should redirect to error page after inputing valid credentials");
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");
		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should not display", errorpage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test07_US729_TC2813_verifyUIAccessWithNoSUID() 	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("UI Login : Verify that user should be redirected to login page and then on error page when no study instance UID is provided");

		loginPage = new LoginPage(driver);

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying the display of error page when blank Study UID is enterred");

		loginPage.navigateToURL(URLConstants.BASE_URL+noSuid_ViewerAH4Url);
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying user should redirect to login page first");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username, password);

		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying user should redirect to error page after inputing valid credentials");
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");		
		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should not display", errorpage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US729"})
	public void test08_US729_TC2829_verifyNavigationForSuidWithSpecialCharaters() 	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the navigation when Study instance UID having special character is provided in url");

		loginPage = new LoginPage(driver);

		//Step1 - Ui login with patient data not having $ in study UID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verifying Ui login with invalid patient data including $ in study UID");

		loginPage.navigateToURL(URLConstants.BASE_URL+suid_WithSpecialChar1);

		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verifying user should redirect to login page first");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username, password);

		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verifying user should redirect to error page after inputing valid credentials");
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");


		//Step2 - Ui login with patient data not having %24 in study UID		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verifying Ui login with invalid patient data including %24(Encoded value) in study UID");

		loginPage.navigateToURL(URLConstants.BASE_URL+suid_WithSpecialChar2);
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verifying user should redirect to login page first");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username, password);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verifying user should redirect to error page after inputing valid credentials");
		errorpage.waitForErrorPageToLoad();
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");


		//Step3 - Non Ui login with patient data not having $ in study UID
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verifying the display of error page when invalid patient data is accessed including $ in study UID");
		String myURL = loginPage.getNonUILaunchURL(suid_WithSpecialChar1, hm);
		loginPage.navigateToURL(myURL);
		errorpage.waitForErrorPageToLoad();
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verifying the display of error page when invalid patient data is accessed including %24(Encoded value) in study UID");
		String myURL1 = loginPage.getNonUILaunchURL(suid_WithSpecialChar3, hm);
		loginPage.navigateToURL(myURL1);
		errorpage.waitForErrorPageToLoad();
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");

	}	

	@Test(groups ={"firefox","Chrome","Edge","IE11","US729"})
	public void test09_US729_TC2834_verifyNavigationForInvalidBasicUrl() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the navigation for incorrect basic url");

		loginPage = new LoginPage(driver);

		//For ui - Accessing invalid viewer page url 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from ui login with invalid basic url");

		loginPage.navigateToURL(URLConstants.BASE_URL+"viewer123");
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying user should redirect to login pag");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");

		//For non ui - Accessing invalid viewer page url directly
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non ui login with invalid basic url");
		String myURL = loginPage.getNonUILaunchURL("viewer123//2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103", hm);
		loginPage.openNewWindow(myURL);
		loginPage.waitForLoginPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying user should redirect to login pag");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", loginPage.getCurrentPageURL()+" is displaying");

	}

	@Test(groups ={"Chrome","Edge","IE11","DE1707","Negative"})
	public void test10_DE1707_TC7048_verifyNonUIAccessWithBrowserBack() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify clicking on the back button on browser after logging in to NS using Non-UI login.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		
		loginPage.login(username,password);
		patientListPage = new PatientListPage(driver);
        helper=new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbio_PatientName, 3);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,username); 
		hm.put(LoginPageConstants.USERNAME,password);

		//Accessing viewer page url from non ui login
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		viewerPage.navigateToURL(myURL);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/5]", "Viewerpage is displayed when user tries to access the non ui viewer page");
		viewerPage.assertEquals(viewerPage.getPatientNameOverlayText(1),patientName , "Checkpoint[2/5]","current patient name is getting displayed");
		
		viewerPage.browserBackWebPage();
		loginPage.waitForLoginPageToLoad();
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Checkpoint[3/5]", "on browser back login page is displayed");

		loginPage.openNewWindow();
		loginPage.switchToNewWindow(1);
		
		loginPage.navigateToURL(terareconURL);
		loginPage.waitForTimePeriod(2000);
		myURL = loginPage.getNonUILaunchURL(invalidSuid_ViewerAH4Url, hm);
		viewerPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"Checkpoint[4/5]","verified invalid url message is displayed on accessing the wrong URL");
		errorpage.browserBackWebPage();
		errorpage.browserBackWebPage();
		errorpage.assertTrue(errorpage.getCurrentPageURL().contains(terareconURL) , "Checkpoint[5/5]", "Terarecon page is displayed");
		
		
		
	}
		
	@Test(groups ={"Chrome","Edge","IE11","DR2798","Negative","E2E","F1081"})
	public void test11_DR2798_TC10646_verifyLaunchedStudy() throws InterruptedException, SQLException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that User should not able to load other studies when launched with a specific studyuid");

		db = new DatabaseMethods(driver);
		String studyInstanceUID = db.getStudyInstanceUID(imbio_PatientName);
		db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID);
		
		loginPage = new LoginPage(driver);
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username,password);
				
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/4]", "Viewerpage is displayed when user tries to access the non ui viewer page");
		viewerPage.assertEquals(viewerPage.getPatientNameOverlayText(1),imbio_PatientName , "Checkpoint[2/4]","current patient name is getting displayed");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[3/4]", "Verified that patient icon is disable when viewer page is directly access using Study UID.");
		
		viewerPage.browserBackWebPage();
		loginPage.waitForLoginPageToLoad();
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Checkpoint[4/4]", "on browser back login page is displayed");

		
	}
	

}
