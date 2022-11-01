package com.trn.ns.test.basic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.UsersPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class NavigationBetweenAppPages extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private HelperClass helper;

	private static String patientPageUrl = "" ,viewerPageUrl="";
	private static final String INVALID_CREDENTIAL = "invalid";
	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String patientId = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath);
	String ah4StudyDescription = DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);

	private String filePath1 = Configurations.TEST_PROPERTIES.get("AAA2_filepath");
	String aAA2PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String studyDescription = DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath1);

	String username1="UserOne";
	String username2="UserTwo";
	String username3="UserThree";
	String username4="UserFour";
	String username5="UserFive";
	String username6="UserSix";
	String username7="UserSeven";
	String username8="UserEight";
	private UsersPage user;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws IOException, InterruptedException, SAXException, ParserConfigurationException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}
	//TC544 - Verify that user is able to access the Northstar pages directly

	@Test(groups ={"firefox","Chrome","Edge"},enabled=true)
	public void test01_part3_US133_TC544_part3_verifyNorthstarPagesAccess() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to access the northstar pages directly - patient Page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		viewerPageUrl = viewerpage.getCurrentPageURL();

		//opening patient list in directly 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify the launching of Patient list page");
		viewerpage.navigateToChangedURL("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.PATIENT_LIST_URL);

		//verifying patient list url 
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying the launching of Patient list page", "User is on "+ patientPage.getCurrentPageURL()+" page");


	}

	//TC547 - Verify that there is no back link present for single patient study page
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test02_US133_TC547_verifyBackNavigation() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to perform the back navigation using browser back");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		//Verify Browser back from viewer page to single patient study page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify the browser back from viewer page to single patient study page");
		viewerpage.browserBackWebPage();
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify the browser back from single study page to patient page");
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying the browser back from single study page to patient page", "User is on "+ patientPage.getCurrentPageURL()+" page");

	}

	//TC2357 - Verify that the login page should display while user try to access the page URls
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test03_US676_TC2357_verifyPageUrlDirectAccess() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the login page should display while user try to access the page URls");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPageUrl = patientPage.getCurrentPageURL();
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		
		viewerPageUrl = viewerpage.getCurrentPageURL();

		viewerpage.openNewWindow(patientPageUrl);
		List<String> allOpenWindows = viewerpage.getAllOpenedWindowsIDs();

		viewerpage.switchToWindow(allOpenWindows.get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify the launching of Patient page in new tab");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display first while accessing page url directly", 
				"Login page is displaying");
		loginPage = new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that access page should display on valid login", viewerpage.getCurrentPageURL()+" is displaying");
		allOpenWindows.clear();

		patientPage.openNewWindow(viewerPageUrl);
		allOpenWindows = viewerpage.getAllOpenedWindowsIDs();
		viewerpage.switchToWindow(allOpenWindows.get(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the launching of viewer page in new tab");
		loginPage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display first while accessing page url directly", 
				"Login page is displaying");
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that access page should display on valid login", viewerpage.getCurrentPageURL()+" is displaying");
		allOpenWindows.clear();

	}

	//TC2394 - Verify that user should redirect to the previous page URls from browser forward button with already saved credentials
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test04_US676_TC2394_verifyBrowserFrwdButtonWithValidCredentials() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should redirect to the previous page URls from browser forward button with already saved credentials");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		
		viewerPageUrl = viewerpage.getCurrentPageURL();

		viewerpage.openNewWindow(viewerPageUrl);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that login page should display on launching of viewer in new tab");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");
		loginPage=new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		viewerpage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that access page should display on inputing valid credentials");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerpage.getCurrentPageURL()+" is displaying");

		viewerpage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify that login page should display on click of browser back button from viewer");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

		viewerpage.browserForwardWebPage();
		viewerpage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify that viewer page should display on click of browser forward button from login");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page display", viewerpage.getCurrentPageURL()+" is displaying");

	}

	//TC2403 - Verify that user should used display error message while redirecting through browser forward button with invalid credentials
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test05_US676_TC2403_verifyBrowserFrwdButtonWithInValidCredentials() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should used display error message while redirecting through browser forward button with invalid credentials");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		
		viewerPageUrl = viewerpage.getCurrentPageURL();

		viewerpage.openNewWindow(viewerPageUrl);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify that login page should display on launching of viewer in new tab");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		viewerpage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify that access page should display on inputing valid credentials");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerpage.getCurrentPageURL()+" is displaying");

		//Click browser back button
		viewerpage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify that login page should display on click of browser back button from viewer");
		viewerpage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

		//Enter invalid credentials and click on browser forward button
		loginPage = new LoginPage(driver);
		loginPage.enterText(loginPage.usernameTextbox,INVALID_CREDENTIAL);
		loginPage.enterText(loginPage.passwordTextbox,INVALID_CREDENTIAL);

		viewerpage.browserForwardWebPage();
		viewerpage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify that viewer page should display on click of browser forward button from login with in-valid credentials");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page display", viewerpage.getCurrentPageURL()+" is displaying");

		//Click browser back button
		viewerpage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify that login page should display on click of browser back button from viewer");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

		//Enter invalid credentials and click on login button
		loginPage.login(INVALID_CREDENTIAL,INVALID_CREDENTIAL);
		loginPage.waitForTimePeriod("Medium");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify that on entering invalid credentials and clicking on login button, error message should display and user remains on login screen");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

	}

	//TC2432 - Verify the browser refresh functionality
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test06_US676_TC2432_verifyPageRefresh() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the browser refresh functionality");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPageUrl = patientPage.getCurrentPageURL();

		//Verify that on refresh user is redirecting to the login page
		patientPage.refreshWebPage();

		//On successful login user is redirecting to the previous page 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify the browser refresh from Patient page");
		loginPage = new LoginPage(driver);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display on refresh", "Login page is displaying");
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify that the previous page should display after valid login");
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");


		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		
		viewerpage.waitForViewerpageToLoad();
		viewerPageUrl = viewerpage.getCurrentPageURL();

		//Verify that on refresh user is redirecting to the login page
		viewerpage.refreshWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify the browser refresh from viewer page");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display on refresh", "Login page is displaying");

		//On successful login user is redirecting to the previous page 
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify that the previous page should display after valid login");
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerpage.getCurrentPageURL()+" is displaying");

	}

	//TC2453 - Verify that user should stay on login page after login attempt with invalid credentials followed by browser forward
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test07_US676_TC2453_verifyLoginWithInValidCredentialsAndFrwdButton() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should stay on login page after login attempt with invalid credentials followed by browser forward");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		viewerPageUrl = viewerpage.getCurrentPageURL();
		loginPage = new LoginPage(driver);

		viewerpage.openNewWindow(viewerPageUrl);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify that login page should display on launching of viewer in new tab");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		viewerpage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify that access page should display on inputing valid credentials");
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerpage.getCurrentPageURL()+" is displaying");

		//Click browser back button
		viewerpage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify that login page should display on click of browser back button from viewer");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

		//Enter invalid credentials and click on login button
		loginPage.login(INVALID_CREDENTIAL,INVALID_CREDENTIAL);
		loginPage.waitForTimePeriod("Medium");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify that on entering invalid credentials and clicking on login button, error message should display and user remains on login screen");
		loginPage.assertEquals(loginPage.loginFailedErrorMsg.getText(),LoginPageConstants.LOGINFAILUREERRORMESSAGE, "Verifying error message text", "Correct error message is displayed");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

		//Click on browser forward button
		viewerpage.browserForwardWebPage();

		//Verify that user stays on login page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify that on entering invalid credentials followed by forward button, user stays on login page");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should display", viewerpage.getCurrentPageURL()+" is displaying");

	}

	//US804 Scrollbar component
	//@Test(groups ={"Chrome","Edge","IE11","US804","Positive"})
	public void test08_US804_TC4453_verifyScrollbarFunctionalityWhenBrowserWindowResize() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Scrollbar functionality on Login, PatientList, StudyList, LicenceInfo, Register and User Page");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		

		db = new DatabaseMethods(driver);
		db.addStudiesInDB(aAA2PatientName, studyDescription);

		String hostname=Configurations.TEST_PROPERTIES.get("nsHostName");
		String port=Configurations.TEST_PROPERTIES.get("nsPort");
		String username=Configurations.TEST_PROPERTIES.get("nsUserName");
		String password=Configurations.TEST_PROPERTIES.get("nsUserName");

		viewerpage = new ViewerPage(driver);

		loginPage.navigateToChangedURL("http://"+hostname+":"+port+"/"+"#/"+URLConstants.REGISTER_PAGE_URL);
		RegisterUserPage registerUserPage = new RegisterUserPage(driver);
/*
		registerUserPage.createNewUser(username1, username1, username1, LoginPageConstants.SUPPORT_EMAIL, username1,username1 ,username1 );
		registerUserPage.createNewUser(username2, username2, username2, LoginPageConstants.SUPPORT_EMAIL, username2,username2 ,username2 );
		registerUserPage.createNewUser(username3, username3, username3, LoginPageConstants.SUPPORT_EMAIL, username3,username3 ,username3 );
		registerUserPage.createNewUser(username4, username4, username4, LoginPageConstants.SUPPORT_EMAIL, username4,username4 ,username4 );
		registerUserPage.createNewUser(username5, username5, username5, LoginPageConstants.SUPPORT_EMAIL, username5,username5 ,username5 );
		registerUserPage.createNewUser(username6, username6, username6, LoginPageConstants.SUPPORT_EMAIL, username6,username6 ,username6 );
		registerUserPage.createNewUser(username7, username7, username7, LoginPageConstants.SUPPORT_EMAIL, username7,username7 ,username7 );
		registerUserPage.createNewUser(username8, username8, username8, LoginPageConstants.SUPPORT_EMAIL, username8,username8 ,username8 );

*/		patientPage.resizeBrowserWindow(300, 500);

		//verify scrollbar component on Login page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/18]", "Verify scroll bar component present on  Login Page when browser window resize");
		loginPage.navigateToChangedURL("http://"+hostname+":"+port+"/"+"#/"+URLConstants.LOGIN_PAGE_URL);
		loginPage.mouseHover(loginPage.signInButton);
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify scrollbar present on Login page", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/18]", "Verify UI of regular scrollbar component seen on Login page");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/18]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in Login page");
//		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertFalse(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");


		//verify scrollbar component on patientlist page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/18]", "Verify scroll bar component present on  Patient List Page when browser window resize");
		loginPage.login(username,password );
		patientPage.waitForPatientPageToLoad();
		patientPage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify scrollbar present on Patient List page", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/18]", "Verify UI of regular scrollbar component for Patient List page");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/18]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in Patient List page");
		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");


		//verify scrollbar component on LicenseInfo page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/18]", "Verify scroll bar component present on  LicenceInfo Page when browser window resize");
		loginPage.navigateToChangedURL(URLConstants.BASE_URL+URLConstants.ABOUT_PAGE_URL);
		loginPage.mouseHover(loginPage.licenceInfo);
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify scrollbar present on LicenceInfo Page", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/18]", "Verify UI of regular scrollbar component for LicenceInfo Page");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/18]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in LicenceInfo Page");
		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");


		//verify scrollbar component on Register page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/18]", "Verify scroll bar component present on  Register Page when browser window resize");
		registerUserPage.navigateToChangedURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.mouseHover(registerUserPage.middleNameTextbox);
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify scrollbar present on Register Page", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/18]", "Verify UI of regular scrollbar component for Register page");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[15/18]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in Register Page");
		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");


		//verify scrollbar component on Users page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[16/18]", "Verify scroll bar component present on User Page when browser window resize");
		viewerpage.navigateToChangedURL(URLConstants.BASE_URL+URLConstants.USER_PAGE_URL);
		user = new UsersPage(driver);
		viewerpage.mouseHover(user.usersTable);
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify scrollbar present on User Page", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[17/18]", "Verify UI of regular scrollbar component for User Page");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[18/18]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in User Page");
		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");

	}

	//US1998:Page Navigation changes to support new patient-study list page
	@Test(groups ={"IE11","Chrome","Edge","US1998","Positive","E2E","F996"})
	public void test09_US1998_TC9095_TC9096_TC9097_verifyStudyPageForSelectedPatient() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that patient list and study page are collaborated into a single view on the UI. <br>"+
		"Verify the background view of the patient study list page. <br>"+
		"Verify that user is able to see the study page for the respective patient after selecting it from the left view of the patient study list page. ");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.patientListTab), "Checkpoint[1/7]", "Verifying the patient list tab");
		
		patientPage.searchPatient(patientName, "", "","");
		patientPage.clickOnPatientRow(patientName);
		
		//patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(2)), patientName, "Checkpoint[2/7]", "Verified that first patient name visible on Patient list page");
		//patientPage.assertEquals(patientPage.getText(patientPage.patientIdsList.get(2)),patientId,"Checkpoint[3/7]","Verified that first patient ID visible on Patient list page");
		patientPage.assertEquals(patientPage.getCssValue(driver.findElement(patientPage.leftPanel), NSGenericConstants.BACKGROUND_COLOR), PatientPageConstants.BLACK_BACKGROUND_COLOR, "Checkpoint[4/7]", "Verified background color of Patient list page as black color.");
	
		patientPage.assertEquals(patientPage.getText("presence", patientPage.patientNameOnStudyPanel), patientName, "Checkpoint[5/7]", "Verified patient name on Study list panel");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDescriptionList.get(0)), ah4StudyDescription, "Checkpoint[6/7]", "Verified study description on study list pane.");
		patientPage.assertEquals(patientPage.getCssValue(driver.findElement(patientPage.rightPanel), NSGenericConstants.BACKGROUND_COLOR), PatientPageConstants.BLUE_BACKGROUND_COLOR, "Checkpoint[7/7]", "Verified background color of study list page as blue color.");
	
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException{
		
		DatabaseMethods db=new DatabaseMethods(driver);
		//Remove added studies
		db.removeAddedStudiesInDB(aAA2PatientName, studyDescription);
	

		}


}