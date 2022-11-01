package com.trn.ns.test.basic;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.ErrorOrLogoutPage;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class NavigationForNonUILogin extends TestBase{
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;

	private ViewerTextOverlays viewerPage;
	private RegisterUserPage registerUserPage;
	private DatabaseMethods db;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private PointAnnotation point;
	

	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath);
	String studyDescription=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String bonagePatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);
	String bonagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String studyDesc=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath1);
	
	String filePath2 =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
	
	String filePath3 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	//AH4 patient Url to be accessed
	private String viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103";
	
	String filePath5 =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	
	String filePath6 = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
	String ADC_philips_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);
	String SeriesToSelect_ADC1= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath6);
	String SeriesToSelect_ADC2= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath6);

	String filePath7 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);
	
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
	String tcgaPatientId = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, TCGA_VP_A878_filepath);
	
	String ah4Us675Filepath = Configurations.TEST_PROPERTIES.get("AH.4_US675_filepath");
	String patientIdAh4_US675 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, ah4Us675Filepath);
	
	String cadFilepath = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cadFilepath);
	String cadPatientId = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, cadFilepath);
	
		
	
	String accNo="ACN01";
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;


	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
	public void test01_US677_TC2699_verifyRefreshForNonUILogin() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify refresh functionality for non UI login");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing patient page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verifying the launching of patient page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);

		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");

		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verifying the refresh on patient page");
		patientPage.refreshWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
		
		loginPage.logout();
	}
	
	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
	public void test01_a_US677_TC2699_verifyRefreshForNonUILogin() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify refresh functionality for non UI login");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		//Accessing patient page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verifying the launching of patient page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");

		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verifying the refresh on patient page");
		patientPage.refreshWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
		
		loginPage.logout();


	}
	
	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
	public void test01_c_US677_TC2699_verifyRefreshForNonUILogin() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify refresh functionality for non UI login");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		//Accessing register page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verifying the launching of register page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.REGISTER_PAGE_URL, hm);
		loginPage.navigateToURL(myURL);
		registerUserPage = new RegisterUserPage(driver);
		registerUserPage.assertTrue(registerUserPage.getCurrentPageURL().contains(URLConstants.REGISTER_PAGE_URL) , "Verify that register page should display", registerUserPage.getCurrentPageURL()+" is displaying");

		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verifying the refresh on register page");
		registerUserPage.refreshWebPage();
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.assertTrue(registerUserPage.getCurrentPageURL().contains(URLConstants.REGISTER_PAGE_URL) , "Verify that register page should display", registerUserPage.getCurrentPageURL()+" is displaying");

		loginPage.logout();
		
	}
	
	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
	public void test01_d_US677_TC2699_verifyRefreshForNonUILogin() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify refresh functionality for non UI login");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		
		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verifying the launching of Viewer page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.waitForAllImagesToLoad();
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");

		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verifying the refresh on Viewer page");
		viewerPage.refreshWebPage();
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
	public void test02_US677_TC2701_verifyReloadForNonUILogin() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify reloading for non UI login page urls");

		loginPage = new LoginPage(driver);		

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verifying that direct access of patient page" );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);		
		patientPage.assertTrue(patientPage.patientNamesList.size()>0,"verifying that user is on patient page","verified");

		//Perform reload
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verifying the reloading of patient page");
		patientPage.openNewWindow(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");

		Header header = new Header(driver);
		List<String> windowIDs = header.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		header.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);


//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verifying that direct access of single patient page" );
//		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.openNewWindow(myURL);
//		SinglePatientStudyPage patientStudyPage = new SinglePatientStudyPage(driver);
//		patientPage.assertTrue(patientPage.studyRows.size()>0,"verifying that user is on single patient page","verified");		
//
//		//Perform reload
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verifying the reload on patient's study page");
//		patientPage.openNewWindow(URLConstants.BASE_URL+patientAH4Url);
//		patientPage.waitForSingleStudyToLoad();
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient's study page should display", patientPage.getCurrentPageURL()+" is displaying");
//
//		windowIDs = header.getAllOpenedWindowsIDs();
//		windowIDs.remove(currentWindowID);
//		header.closeWindow(windowIDs.get(0));
//		header.closeWindow(windowIDs.get(1));
//		loginPage.switchToWindow(currentWindowID);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verifying that direct access of Study list page" );
//		myURL = loginPage.getNonUILaunchURL(URLConstants.STUDY_LIST_URL, hm);
//		loginPage.openNewWindow(myURL);
//		StudyListPage studyPage = new StudyListPage(driver);
//		studyPage.assertTrue(studyPage.patientNameList.size()>0,"verifying that user is on study list page","verified");			
//
//		//Perform reload
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verifying the reload on study list page");
//		studyPage.openNewWindow(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL);
//		studyPage.waitForStudyListToLoad();
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Verify that study list page should display", studyPage.getCurrentPageURL()+" is displaying");
//
//		windowIDs = header.getAllOpenedWindowsIDs();
//		windowIDs.remove(currentWindowID);
//		header.closeWindow(windowIDs.get(0));
//		header.closeWindow(windowIDs.get(1));
//		loginPage.switchToWindow(currentWindowID);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verifying that direct access of viewer page" );
		myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.openNewWindow(myURL);		
		ViewerPage viewerPage = new ViewerTextOverlays(driver);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewbox(1)),"verifying that user is on viewer page","verified");

		//Perform reload
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verifying the relaod on Viewer page");
		viewerPage.openNewWindow(URLConstants.BASE_URL+viewerAH4Url);
		viewerPage.waitForViewerpageToLoad();
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");

		windowIDs = header.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		header.closeWindow(windowIDs.get(0));
		header.closeWindow(windowIDs.get(1));
		loginPage.switchToWindow(currentWindowID);
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
	public void test03_US677_TC2763_verifyReloadAndBackForNonUILogin() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that login page should not display after performing reload and back from browser - For non-ui login");

		loginPage = new LoginPage(driver);		

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));

		String currentWindowID = loginPage.getCurrentWindowID();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verifying that direct access of patient page" );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.waitForTimePeriod(2000);	
		patientPage.assertTrue(patientPage.patientNamesList.size()>0,"verifying that user is on patient page","verified");
		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verifying the refresh and back on patient page");
		patientPage.refreshWebPage();
		patientPage.waitForPatientPageToLoad();
		//Browser back
		patientPage.browserBackWebPage();
		patientPage.waitForURLToChange();
		patientPage.assertFalse(patientPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should not display", patientPage.getCurrentPageURL()+" is displaying");

		Header header = new Header(driver);
		List<String> windowIDs = header.getAllOpenedWindowsIDs();

//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verifying that direct access of single patient page" );
//		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.openNewWindow(myURL);
//		SinglePatientStudyPage patientStudyPage = new SinglePatientStudyPage(driver);
//		patientPage.assertTrue(patientPage.studyRows.size()>0,"verifying that user is on single patient page","verified");		
//		//Perform refresh
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verifying the refresh and back on patient's study page");
//		patientPage.refreshWebPage();
//		patientPage.waitForSingleStudyToLoad();
//		//Browser back
//		patientPage.browserBackWebPage();
//		patientPage.waitForURLToChange();
//		patientPage.assertFalse(patientPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should not display", patientPage.getCurrentPageURL()+" is displaying");
//
//		windowIDs = header.getAllOpenedWindowsIDs();
//		windowIDs.remove(currentWindowID);
//		header.closeWindow(windowIDs.get(0));
//		loginPage.switchToWindow(currentWindowID);
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verifying that direct access of Study list page" );
//		myURL = loginPage.getNonUILaunchURL(URLConstants.STUDY_LIST_URL, hm);
//		loginPage.openNewWindow(myURL);
//		StudyListPage studyPage = new StudyListPage(driver);
//		studyPage.assertTrue(studyPage.patientNameList.size()>0,"verifying that user is on study list page","verified");			
//		//Perform refresh
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verifying the refresh and back on study list page");
//		studyPage.refreshWebPage();
//		studyPage.waitForStudyListToLoad();
//		//Browser back
//		studyPage.browserBackWebPage();
//		studyPage.waitForStudyListToLoad();
//		studyPage.assertFalse(studyPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should not display", studyPage.getCurrentPageURL()+" is displaying");
//
//		windowIDs = header.getAllOpenedWindowsIDs();
//		windowIDs.remove(currentWindowID);
//		header.closeWindow(windowIDs.get(0));
//		loginPage.switchToWindow(currentWindowID);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verifying that direct access of viewer page" );
		myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.openNewWindow(myURL);		
		ViewerPage viewerPage = new ViewerTextOverlays(driver);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewbox(1)),"verifying that user is on viewer page","verified");
		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verifying the refresh and back on Viewer page");
		viewerPage.refreshWebPage();
		viewerPage.waitForViewerpageToLoad();
		//Browser back
		viewerPage.browserBackWebPage();
		viewerPage.waitForURLToChange();
		viewerPage.assertFalse(viewerPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page should not display", viewerPage.getCurrentPageURL()+" is displaying");

		windowIDs = header.getAllOpenedWindowsIDs();
		windowIDs.remove(currentWindowID);
		header.closeWindow(windowIDs.get(0));
		loginPage.switchToWindow(currentWindowID);
	}

	//DE1673: URL Parameters are not consistent across NS APIs
	@Test(groups ={"Chrome","Edge","IE11","DE1673","DR1992","Positive"})
	public void test04_DE1673_TC6753_DR1992_TC8119_verifyNonUILoginWithURLLaunchAndAccessionNo() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login into Northstar using URL launch and accession number which returns single study.<br>"+
		"[Risk and Impact]: Verify the multiple valid and invalid scenarios with batch, study and accession number.");

		loginPage = new LoginPage(driver);
		viewerPage = new ViewerTextOverlays(driver);
		db=new DatabaseMethods(driver);
		String accessionNo="2362654294358";
		db.updateAccessionNoInStudyTable(patientName,accessionNo);
		
		//update accession no for AH.4 data for only one study
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accessionNo);

		//Accessing patient page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using accession number");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/2]","Verified that viewer page loaded by using accession number and " +viewerPage.getCurrentPageURL()+" is displaying");
		 
		//Perform browser back
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify page URL after browser back on viewer page should navigate to default home page of the browser");
		viewerPage.browserBackWebPage();
		viewerPage.assertFalse(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[2/2]","Verified page URL after browser back on viewer page");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1673","Positive"})
	public void test05_DE1673_TC6755_verifyNonUILoginOfMultipleStudiesOfPatientHavingSameAccessionNo() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login into Northstar with accession number that returns multiple studies of the same Patient.");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		db.addStudiesInDB(patientName, studyDescription);
		db.updateAccessionNoInStudyTable(patientName,accNo);
		
		//update accession no for AH.4 data for multiple study
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo);

		//Accessing patient page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify the launching of patient page from non-ui login for multiple studies of same patient");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/5]", patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.studyRows.size()>1, "Checkpoint[2/5]", "Verified that multiple studies are displying on studylist page");
		
		//navigate to viewer page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify the navigation from study list to viewer page and viewer page to study list back");
		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/5]", patientPage.getCurrentPageURL()+" is displaying");
		
		//browser back navigate to study list page
		viewerPage.browserBackWebPage();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "[Checkpoint[4/5]", patientPage.getCurrentPageURL()+" is displaying");
		
		//browser back navigate to google starting page
		patientPage.browserBackWebPage();
		patientPage.assertFalse(loginPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[5/5]", patientPage.getCurrentPageURL()+" is displaying");
		
	}
	
	//stusy page is discarded
	//@Test(groups ={"Chrome","Edge","IE11","DE1673","Positive"})
	public void test06_DE1673_TC6757_verifyNonUILoginOfStudiesFromMultiplePatientHavingSameAccessionNo() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login into Northstar with accession number that returns studies from multiple patients.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		
		db=new DatabaseMethods(driver);
		db.updateAccessionNoInStudyTable(patientName,accNo);
		db.updateAccessionNoInStudyTable(bonagePatientName,accNo);
		
		//update accession no for AH.4 data for only one study
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo);

		//Accessing patient page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study list page when multiple patient having same accession number.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToChangedURL(myURL);
		patientPage.waitForStudyToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Checkpoint[1/10]", patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.studyRows.size()>1, "Checkpoint[2/10]", "Verified that multiple study loaded on study list page");
		
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForRespectedViewboxToLoad(1);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Checkpoint[3/10]", "Verified that viewer page loaded for the first study");

		patientPage.browserBackWebPage();
		patientPage.waitForTimePeriod(3000);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Checkpoint[4/10]", patientPage.getCurrentPageURL()+" is displaying");
		
		//verify with terarecon.com site
		String otherURL="http://terarecon.com/";
		loginPage.navigateToURL(otherURL);
		loginPage.waitForTimePeriod(2000);
		patientPage.assertEquals(patientPage.getCurrentPageURL(),otherURL, "Checkpoint[5/10]", patientPage.getCurrentPageURL()+" is displaying");
		
		//verify on URL login on other that northstar application site
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Checkpoint[6/10]", patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.studyRows.size()>1, "Checkpoint[7/10]","Verified that multiple study loaded on study list page");
		
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForRespectedViewboxToLoad(1);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Checkpoint[8/10]", "Verified that viewer page loaded for the first study");
		
		patientPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Checkpoint[9/10]", patientPage.getCurrentPageURL()+" is displaying");

		patientPage.browserBackWebPage();
		patientPage.waitForTimePeriod(2000);
		patientPage.assertEquals(patientPage.getCurrentPageURL(),otherURL, "Checkpoint[10/10]", patientPage.getCurrentPageURL()+" is displaying");
				
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1673","Negative"})
	public void test07_DE1673_TC6758_verifyNonUILoginWithInvalidAccessionNo() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the non UI login into Northstar with the invalid accession number.");

		loginPage = new LoginPage(driver);
		viewerPage = new ViewerTextOverlays(driver);
		db=new DatabaseMethods(driver);
		
        //verify when accession number is not present in DB
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo);

		//Accessing patient page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify error message for non-ui login with invalid accession number");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
		loginPage.assertEquals(errorpage.getText(errorpage.message),OrthancAndAPIConstants.INVALID_ACCESSION_NO_ERROR_MESSAGE,"Checkpoint[1/4]", loginPage.getCurrentPageURL()+" is displaying");

		//verify error message when accession number is not present in URL but present in db
		db.updateAccessionNoInStudyTable(patientName,accNo);
		hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message from non-ui login when accession number present in db but not present in URL");
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertEquals(errorpage.getText(errorpage.message),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_PARAMETERS_NOT_PROVIDED , "Checkpoint[2/4]", loginPage.getCurrentPageURL()+" is displaying");

		//verify error message when mismatch with accession number and patient ID
		hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,bonagePatientID); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying error message from non-ui login when there is mismatch in accession number and patientID");
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertEquals(errorpage.getText(errorpage.message),OrthancAndAPIConstants.INVALID_ACCESSION_NO_ERROR_MESSAGE , "Checkpoint[3/4]", loginPage.getCurrentPageURL()+" is displaying");
	  
		//Enter the URL with no accession number but with patientID"
		hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,bonagePatientID); 
/*
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying error message from non-ui login with patient ID but no accession number");
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertEquals(errorpage.getText(errorpage.message),OrthancAndAPIConstants.INVALID_ACCESSION_NO_ERROR_MESSAGE , "Checkpoint[4/4]", loginPage.getCurrentPageURL()+" is displaying");
*/
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1673","Positive"})
	public void test08_DE1673_TC6760_verifyNonUILoginWithoutUsingURLLaunch() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non-UI login to Patients, PatientStudy, StudyList and Viewer page with out using the URL launch.");

		loginPage = new LoginPage(driver);
		viewerPage = new ViewerTextOverlays(driver);
		db=new DatabaseMethods(driver);
		
		//Patients page:
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of patient page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		loginPage.waitForTimePeriod(2000);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/8]", patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.patientListTable) , "Checkpoint[2/8]","Verified patient list table from non UI login");
		
		//Single Patient Study page
//		hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.USERNAME,username);
//		hm.put(LoginPageConstants.PASSWORD,password); 
//		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(patientID)); 
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login");
//		myURL = loginPage.getNonUILaunchURL(URLConstants.SINGLE_PATIENT_LIST_URL+"/"+patientID, hm);
//		loginPage.navigateToURL(myURL);
//		loginPage.waitForTimePeriod(2000);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Checkpoint[3/8]", patientPage.getCurrentPageURL()+" is displaying");
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyListRows), "Checkpoint[4/8]","Verified single study list page from non UI login");
//		
//		//StudyList page
//	    hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.USERNAME,username);
//		hm.put(LoginPageConstants.PASSWORD,password); 
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of study list url from non-ui login");
//	    myURL =loginPage.getNonUILaunchURL(URLConstants.STUDY_LIST_URL, hm);
//		loginPage.navigateToURL(myURL);
//		studyPage = new StudyListPage(driver);
//		studyPage.waitForTimePeriod(2000);
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Checkpoint[5/8]", studyPage.getCurrentPageURL()+" is displaying");
//		studyPage.assertTrue(studyPage.isElementPresent(studyPage.studyListRows) , "Checkpoint[6/8]", "Verified study list page from non UI login");
		
		//Viewer page
	    hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of viewer page from non-ui login");
	    myURL =loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+db.getStudyInstanceUID(patientName), hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[7/8]", viewerPage.getCurrentPageURL()+" is displaying");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.EurekaLogo) , "Checkpoint[8/8]","Verified terarecon logo on viewer page");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1673","Positive"})
	public void test09_DE1673_TC6783_verifyNonUILoginWithAccessionNoAndPatientID () throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login into Northstar with accession number and the patient id.");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);

		db.updateAccessionNoInStudyTable(patientName,accNo);
		
        //Unique Patient Id - not unique accession number
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo);
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API, patientID);

		//Unique Patient Id - returning single study for accession number:
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify launching of viewer page from non-ui login using accession number");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[1/12]", loginPage.getCurrentPageURL()+" is displaying");
        viewerPage = new ViewerTextOverlays(driver);
        viewerPage.waitForViewerpageToLoad();
        viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Checkpoint[2/12]", "Verified that layout is 2*2");
     
		//Accessing patient page url from non ui login
        db.addStudiesInDB(patientName, studyDescription);
		db.updateAccessionNoInStudyTable(patientName,accNo);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify launching of patient page from non-ui login");
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/12]", loginPage.getCurrentPageURL()+" is displaying");
		patientPage=new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.studyRows.size()>1,"Checkpoint[4/12]", "Verified that single patient study list page loaded");
		patientPage.clickOntheFirstStudy();
        viewerPage.waitForViewerpageToLoad();
        viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Checkpoint[5/12]", "Verified that viewer page loaded successfully.");
        viewerPage.browserBackWebPage();
        patientPage.waitForPatientPageToLoad();
        loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[6/12]", loginPage.getCurrentPageURL()+" is displaying");
        
		//Not Unique Patient Id - unique accession number
        db.updatePatientID(IBL_JohnDoe_PatientName, ChestCT1p25mm);
        hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,ChestCT1p25mm);
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API, ChestCT1p25mm);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of viewer page from non-ui login with unique patient ID and accession number");
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[7/12]", loginPage.getCurrentPageURL()+" is displaying");
        viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 1, "Checkpoint[8/12]", "Verified that viewer page loaded successfully.");
        
        //Not Unique Patient Id - unique accession number
        db.updateAccessionNoInStudyTable(IBL_JohnDoe_PatientName, ChestCT1p25mm);
        hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,ChestCT1p25mm);
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API, ChestCT1p25mm);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login with unique accession number and non unique patient ID");
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(2000);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[9/12]", loginPage.getCurrentPageURL()+" is displaying");
		
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOntheFirstStudy();
        viewerPage.waitForViewerpageToLoad(4);
        loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[10/12]", loginPage.getCurrentPageURL()+" is displaying");
        viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Checkpoint[11/12]", "Verified that viewer page loaded successfully");
        viewerPage.browserBackWebPage();
        loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[12/12]", loginPage.getCurrentPageURL()+" is displaying");
       
	}
	
	//DE1874: [Automation]: Text overlay not visible on viewer when user access Non-UI URL and refreshes it.
	@Test(groups ={"Chrome","Edge","IE11","DE1874","Positive"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test10_verifyTextOverlays"})
	public void test10_DE1874_TC7472_verifyTextOverlaysWhenUserAccessNonUIURL(String fileName,String viewboxNo) throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that text overlay's are visible on the viewer page, when user access non UI URL and refreshes the page.");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		
		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(PatientName);
		int viewbox=Integer.parseInt(viewboxNo);
		
		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading patient "+PatientName+" on viewer page from non UI login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUID, hm);

		loginPage.openNewWindow(myURL);
		List<String> allWindowsID = loginPage.getAllOpenedWindowsIDs();
		
		loginPage.switchToWindow(allWindowsID.get(1));
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForRespectedViewboxToLoad(viewbox);
		int viewboxCount=viewerPage.getNumberOfCanvasForLayout();
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/6]",viewerPage.getCurrentPageURL()+" is displaying on viewer");

		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying Viewer page after refresh");
		viewerPage.refreshWebPage();
		viewerPage.waitForRespectedViewboxToLoad(viewbox);
		viewerPage.waitForDefaultOverlayDisplay(viewbox);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), viewboxCount, "Verify layout after viewer page refresh.", "Verified layout after viewer page refreshed.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying text overlay on viewer after browser refresh.");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.DEFAULT_ANNOTATION), "Verify text overlay detail for default annotation", "Verified patient name text overlay for default annotation.");
		
		//change textoverlay from default to full and verify text overlay after refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay for Full annotation.");
		viewerPage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.refreshWebPage();
		viewerPage.waitForRespectedViewboxToLoad(viewbox);
		viewerPage.waitForFullOverlayDisplay(viewbox);
		viewerPage.assertTrue(viewerPage.verifyTextOverlayIsHighlighted(viewerPage.fullTextOverlay), "Verifying TextOverlay icon 'Full' on Menu", "TextOverlay  icon 'Full' is selected on Menu");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.FULL_ANNOTATION), "Verify text overlay detail for full annotation", "Verified Image matrix text overlay for full annotation.");
	
		//change textoverlay from full to minimum and verify text overlay after refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify text overlay for Minimum annotation.");
		viewerPage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerPage.refreshWebPage();
		viewerPage.waitForRespectedViewboxToLoad(viewbox);
		viewerPage.waitForMinimumOverlayDisplay(viewbox);
		viewerPage.assertTrue(viewerPage.verifyTextOverlayIsHighlighted(viewerPage.minimumTextOverlay), "Verifying TextOverlay icon 'Minimum' on Menu", "TextOverlay  icon 'Minimum' is selected on Menu");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.MINIMUM_ANNOTATION), "Verify text overlay detail for minimum  annotation", "Verified patient ID text overlay for minimum annotation.");
		
		
		//change textoverlay from minimum to default and verify text  overlay after refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay for Default annotation.");
	    viewerPage.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerPage.refreshWebPage();
		viewerPage.waitForRespectedViewboxToLoad(viewbox);
		viewerPage.waitForDefaultOverlayDisplay(viewbox);
		viewerPage.assertTrue(viewerPage.verifyTextOverlayIsHighlighted(viewerPage.defaultTextOverlay), "Verifying TextOverlay icon 'Default' on Menu", "TextOverlay  icon 'Default' is selected on Menu");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.DEFAULT_ANNOTATION), "Verify text overlay detail for default annotation", "Verified patient name text overlay for default annotation.");
		
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1874","Positive"})
	public void test11_DE1874_TC7475_verifyWWPresetAndLayoutItems() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the windowing presets and the layout items, when user loaded the viewer page using non UI URL");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		
		//Loading the patient on viewer
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(ChestCT1p25mm);;
		
		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading of "+ ChestCT1p25mm+ " patient on viewer using Non UI URL.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUID, hm);

		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad(2);
		layout = new ViewerLayout(driver);
		preset = new ViewBoxToolPanel(driver);
		
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/7]", viewerPage.getCurrentPageURL()+" is displaying");

		int  beforeWW=viewerPage.getValueOfWindowWidth(2);
		int beforeWC=viewerPage.getValueOfWindowCenter(2);
		preset.selectPresetValue(2, ViewerPageConstants.HEAD);
		viewerPage.assertNotEquals(viewerPage.getValueOfWindowWidth(2), beforeWW, "Checkpoint[2/7]", "Verified changed value of window width after selecting value from preset menu.");
		viewerPage.assertNotEquals(viewerPage.getValueOfWindowCenter(2), beforeWC, "Checkpoint[3/7]", "Verified changed value of window centre after selecting value from preset menu.");
		
		//verify layout option visible on viewer
		layout.openLayoutContainer();
		viewerPage.assertTrue(viewerPage.isElementPresent(layout.layoutContainer), "Checkpoint[4/7]", "Verified layout selector option on viewer page");
		
		//refresh webpage
		viewerPage.refreshWebPage();
		viewerPage.assertEquals(viewerPage.getValueOfWindowWidth(2), beforeWW, "Checkpoint[5/7]","Verified value of window width is same after browser refresh.");
		viewerPage.assertEquals(viewerPage.getValueOfWindowCenter(2), beforeWC, "Checkpoint[6/7]", "Verified value of window centre is same after browser refresh.");
		
		//verify layout option after refresh page
		layout.openLayoutContainer();
		viewerPage.assertTrue(viewerPage.isElementPresent(layout.layoutContainer), "Checkpoint[7/7]", "Verified layout selector option on viewer page after browser refresh.");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1874","Positive"})
	public void test12_DE1874_TC7479_verifyThumbnailsForNonUILoginPageURL() throws SQLException, InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify the output panel thumbnail images are loaded, for series not loaded in viewer page using non UI URL");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		
		//Loading the patient on viewer
		
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(ADC_philips_Patient);;
		
		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading of "+ADC_philips_Patient+" on viewer");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUID, hm);

		loginPage.navigateToURL(myURL);
		OutputPanel panel = new OutputPanel(driver);
		cs=new ContentSelector(driver);
		panel.waitForViewerpageToLoad(2);
		panel.assertTrue(panel.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/5]", panel.getCurrentPageURL()+" is displaying");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw annotation and verify jump to functionality for the GSPS.");
	    lineWithUnit= new MeasurementWithUnit(driver);
	    lineWithUnit.selectDistanceFromQuickToolbar(3);
	    lineWithUnit.drawLine(3, -70, -80, -120, 90);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(3, 1), "Checkpoint[2/5]", "Verified that annotation is highlighted on viewer after click on jump icon from Output panel.");
		panel.openAndCloseOutputPanel(false);
		
		//change series from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","New series selected from content selector on viewbox-3");
		cs.selectSeriesFromSeriesTab(3, SeriesToSelect_ADC2);
		panel.assertTrue(cs.verifyPresenceOfEyeIcon(SeriesToSelect_ADC2),"Checkpoint[3/5]","Verified new series is selected on viewbox-3");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.LINEAR_FINDING_NAME), "Checkpoint[4/5]", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));	
		panel.clickOnThumbnail(1);
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+SeriesToSelect_ADC1+"\" " + ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[5/5]", "Verified warning message when thumbnail corresponding slice is not open in active view ");	

	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1874","Positive"})
	public void test13_DE1874_TC7476_verifyKeyboardShortcutsOnViewer() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page using non UI URL.");

		loginPage = new LoginPage(driver);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		
		//Loading the patient on viewer
		
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(ADC_philips_Patient);;
		
		//Accessing viewer page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verifying the launching of Viewer page from non-ui login for "+ADC_philips_Patient);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUID, hm);

		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad(2);
		ViewerSliderAndFindingMenu slider = new ViewerSliderAndFindingMenu(driver);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/19]", viewerPage.getCurrentPageURL()+" is displaying");
		
				
        //verify cine on non UI login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify cine functionality using keyboard shortcut");
		viewerPage.playOrStopCineUsingKeyboardCKey();
		String position = viewerPage.getCurrentScrollPosition(1);
		viewerPage.waitForTimePeriod(300);
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(1),position ,"Checkpoint[2/19]","Verifying that Cine is getting played using Keyboard C key");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Checkpoint[3/19]","Verified that cine is stopped.");
		
		
		//verify slice change using up and down arrow key
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify slice change functionality using up and down arrow key.");
		viewerPage.click(viewerPage.getViewPort(2));
		position = viewerPage.getCurrentScrollPosition(2);
		viewerPage.scrollUpToSliceUsingKeyboard(2, 1);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(2),position ,"Checkpoint[4/19]","Verified slice change after pressing arrow up key.");
		position = viewerPage.getCurrentScrollPosition(2);
		viewerPage.scrollDownToSliceUsingKeyboard(2, 1);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(2),position,"Checkpoint[5/19]","Verified slice change after pressing arrow down key.");
				
		
		//verify ww and WC
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify WWWC value using shortcut key");
		String currentWidth = viewerPage.getWindowWidthValueOverlayText(1);
		String currentCenter = viewerPage.getWindowCenterValueOverlayText(1);

		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();		
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1),0, 0, 100, 50);		

		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(1),currentWidth ,"Checkpoint[6/19]","Verifying the WW/WL(width) performed using Keyboard W key");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(1),currentCenter ,"Chekpoint[7/19]","Verifying the WW/WL(center) performed using Keyboard W key");
		
		
		//Click on dot(.) from keyboard to change phase number
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify phase navigation using dot and comma keyboard shortcut.");
		viewerPage.click(viewerPage.getViewPort(4));
		int CurrentPhasePosition=viewerPage.getValueOfCurrentPhase(4);
		viewerPage.pressKey(NSGenericConstants.DOT_KEY);
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(4);
		viewerPage.assertNotEquals(ChangePhasePosition,CurrentPhasePosition,"Checkpoint[8/19]", "Verify user navigate forward on phases");
		//Click on comma(,) from keyboard to change phase number
		viewerPage.pressKey(NSGenericConstants.COMMA_KEY);
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(4),CurrentPhasePosition,"Checkpoint[9/19]", "Verify user navigate backward on phases");
		
		
		//sync on and off using keyboard shortcut
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify sync On Off  using keyboard shortcut");
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.performSyncONorOFF();
		int beforeValue1=viewerPage.getCurrentScrollPositionOfViewbox(2);
		int beforeValue2=viewerPage.getCurrentScrollPositionOfViewbox(3);
			
		slider.scrollTheSlicesUsingSlider(2, 0, 0, 0, 20);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), beforeValue1, "Checkpoint[10/19]","Verified that slice change in the second viewbox when sync mode off.");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), beforeValue2, "Checkpoint[11/19]", "Verified that slice not change in the third viewbox when sync mode off.");
		
		beforeValue1=viewerPage.getCurrentScrollPositionOfViewbox(2);
	    beforeValue2=viewerPage.getCurrentScrollPositionOfViewbox(3);
	    
	    viewerPage.performSyncONorOFF();
	    slider.scrollTheSlicesUsingSlider(2, 0, 0, 0, 20);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), beforeValue1, "Checkpoint[12/19]","Verified that slice change in the second viewbox when sync mode on.");
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), beforeValue2, "Checkpoint[13/19]","Verified that slice change in the third viewbox when sync mode on.");
				
		
		//verify page up and page down functionality and keyboard shortcut for distance annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify shortcut key for distance annotation");
		lineWithUnit=new MeasurementWithUnit(driver);
		point=new PointAnnotation(driver);
		lineWithUnit.enableOrDisableDistanceIconUsingKeyboardDKey();
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		
//		viewerPage.inputImageNumber(13, 1);
		viewerPage.scrollToImage(1, 13);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		slider.scrollUpGSPSUsingKeyboard();
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[14/19]", "Verified that linear measurement annotation is selected using page up.");
		slider.scrollDownGSPSUsingKeyboard();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[15/19]", "Verified that point annotation is selected using page down.");
		
		//Click on the G key from the keyboard and again click on the G key from the keyboard
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify keyboard shortcut G for result applied tag.");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF),"Checkpoint[16/19]", "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1");
		viewerPage.assertTrue(slider.getAllGSPSObjects(1).isEmpty(), "Checkpoint[17/19]", "Verified that GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON),"Checkpoint[18/19]", "Verify the 'Result applied' is set to toogle 'On' by clicking on text 'Result applied' in Viewbox1");
		
		
		//Refresh the page and verify 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify viewer page URL after refresh.");
		viewerPage.refreshWebPage();
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[19/19]", viewerPage.getCurrentPageURL()+" is displaying");
	
		
	}
	
	//US1500: Launch study in viewer by passing batch in the URL
	@Test(groups ={"Chrome","Edge","IE11","US1500","Negative"})
	public void test14_US1500_TC8007_verifyViewerWhenUserNamePasswordNotProvidedInURL() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that if token is not passed, user can view the viewer page only after the external log in.");

		loginPage = new LoginPage(driver);
		viewerPage = new ViewerTextOverlays(driver);
		db=new DatabaseMethods(driver);
		
		String batchUID=db.getBatchUIDFromBatchTable(liver9PatientName);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.BATCH_UID,batchUID); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of login page from non-ui login when username pasasword not provided.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Checkpoint[1/3]","Verified that login page loaded and " +loginPage.getCurrentPageURL()+" is displaying");
		loginPage.login(username, password);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[2/3]","Verified that viewer page loaded by using batch UID and " +viewerPage.getCurrentPageURL()+" is displaying");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(4, ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[3/3]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1500","US1998","DR2570","Positive","E2E","F996"})
	public void test15_US1500_TC8006_US1998_TC9098_DR2570_TC10313_verifyViewerUsingValidBatchUID() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to navigate to the patient viewer using batch id.<br>"+
		"[Risk and impact]: Verify the TC8006, TC8031, TC8119 for desktop as well as mobile.<br>"+
		"Re-execute TC8006: Verify that the user is able to navigate to the patient viewer using batch id.");

		loginPage = new LoginPage(driver);
		viewerPage = new ViewerTextOverlays(driver);
		db=new DatabaseMethods(driver);
		
		String batchUID=db.getBatchUIDFromBatchTable(liver9PatientName);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.BATCH_UID,batchUID); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login using batch UID");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/2]","Verified that viewer page loaded by using batch UID and " +viewerPage.getCurrentPageURL()+" is displaying");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(4, ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/2]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1500","Negative"})
	public void test16_US1500_TC8009_verifyErrorMessageForInvalidBatchUID() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that if incorrect batch id is provided, then user is navigated to the invalid URL page.");

		loginPage = new LoginPage(driver);
		String batchUID="877902f1-dec9-4deb-b6a3-9717a988";
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.BATCH_UID,batchUID); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of error page from non-ui login when invalid batch UID provided");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(1000);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[1/2]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_BATCH_UID_ERROR_MSG,"Checkpoint[2/2]","Verifyied error message when invalid batch UID provided in URL.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1500","DE1992","DR2570","Negative","E2E","F1081"})
	public void test17_US1500_TC8028_DR1992_TC8119_DR2570_TC10311_verifyNonUILoginUsingPatientID() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify that user is able to access the single study page using Patient ID. <br>"+
		"[Risk and Impact]: Verify the multiple valid and invalid scenarios with batch, study and accession number.<br>"+
				"Re-execute TC8119: Verify the multiple valid and invalid scenarios with batch, study and accession number.");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,bonagePatientID); 
		
		//study page using patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of study page from non-ui login using valid patient ID");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/2]","Verified that single study list page loaded by using Patient ID and " +patientPage.getCurrentPageURL()+" is displaying");	
		patientPage.assertEquals(patientPage.getStudyDescription(0), studyDesc, "Checkpoint[2/2]","Verified that single study page  loaded for the correct patient based on patient ID.");
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1500","DR1992","DR2570","Negative","E2E","F1081"})
	public void test18_US1500_TC8028_DE1992_TC8119_DR2570_TC10311_verifyNonUILoginUsingStudyUID() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify that user is able to access the viewer page using study instance ID.<br>"+
		"[Risk and Impact]: Verify the multiple valid and invalid scenarios with batch, study and accession number.<br>"+
		"Re-execute TC8119: Verify the multiple valid and invalid scenarios with batch, study and accession number.");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		
		String studyUID=db.getStudyInstanceUID(liver9PatientName);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID); 

		//viewer page using Study UID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login using study UID.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/2]","Verified that viewer page loaded by using study UID and " +viewerPage.getCurrentPageURL()+" is displaying");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(4, ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/2]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1673","DR1992","US1998","DR2570","Positive","E2E","F996","E2E","F1081"})
	public void test19_US1500_TC8031_DR1992_TC8119_US1998_TC9098_DR2570_TC10311_TC10312_verifyBatchUIDIsNonCaseSensitive() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the keys and values are not case sensitive while searching the patient.<br>"+
		"[Risk and Impact]: Verify the multiple valid and invalid scenarios with batch, study and accession number.<br>"+
		"[Risk and impact]: Verify the TC8006, TC8031, TC8119 for desktop as well as mobile.<br>"+
		"Re-execute TC8119: Verify the multiple valid and invalid scenarios with batch, study and accession number.<br>"+
		"Re-execute TC8031: Verify that the keys and values are not case sensitive while searching the patient.");

		loginPage = new LoginPage(driver);
		viewerPage = new ViewerTextOverlays(driver);
		db=new DatabaseMethods(driver);
		
		String batchUID=db.getBatchUIDFromBatchTable(liver9PatientName);

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.BATCH_UID,batchUID.toUpperCase()); 

		//change batch UID to upper case and verify launch of viewer page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login using batch UID in uppercase.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/2]","Verified that viewer page loaded by using batch UID and " +viewerPage.getCurrentPageURL()+" is displaying");
		viewerPage.assertTrue(viewerPage.verifyTextOverlayDetail(4, ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/2]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
	
	}
	
	//DR1992: User is able to login in to the application instead of seeing Error page when 'acno' is used in Non-UI login.
	@Test(groups ={"Chrome","Edge","IE11","DR1992","Negative"})
	public void test20_DR1992_TC8118_verifyErrorPagWhenParameterIsInCorrectForAccessionNumber() throws SQLException 
		{
			extentTest = ExtentManager.getTestInstance();
			extentTest.setDescription("Verify password is encrypted in the client side for non UI login into Northstar using URL launch and accession number.");
			loginPage = new LoginPage(driver);
			
			db=new DatabaseMethods(driver);
			LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
			hm.put(LoginPageConstants.PASSWORD,password); 
			hm.put(LoginPageConstants.USERNAME,username);
			
			hm.put("acno",db.getAccessionNumber(patientNameDICOMRT));
		
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of error page from non-ui login");
		    String myURL =loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		
			loginPage.navigateToURL(myURL);		
			ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
			loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[1/4]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
			errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PARAMETER_ERROR_MSG,"Checkpoint[2/4]","Verifyied error message when only accession number is provided in URL.");
		
			//verify error message when paramter enter is case sensitive
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of error page from non-ui login when parameter is not correct for accession number.");
			hm=new LinkedHashMap<String,String>();  
			hm.put(LoginPageConstants.PASSWORD,password); 
			hm.put(LoginPageConstants.USERNAME,username);
			hm.put("acNo",db.getAccessionNumber(patientNameDICOMRT));
	
		    myURL =loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		
			loginPage.navigateToURL(myURL);			
			loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[3/4]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
			errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PARAMETER_ERROR_MSG,"Checkpoint[4/4]","Verifyied error message when only accession number is provided in URL.");
		
		
		}
	
	//US1998:Page Navigation changes to support new patient-study list page
	@Test(groups ={"Chrome","Edge","IE11","US1998","Positive","E2E","F996"})
	public void test21_US1998_TC9215_verifyNavigationForMultiPatientListPage()  
		{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non UI - Verify that user is able to filter the multi patient page");
		loginPage = new LoginPage(driver);
			
		db=new DatabaseMethods(driver);
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientIdAh4_US675); 	
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the launching of multipatient page.");
		String myURL =loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		
	    loginPage.navigateToURL(myURL);			
	    patientPage=new PatientListPage(driver);
	    
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/2]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		patientPage.assertEquals(patientPage.patientRows.size(), 2, "Checkpoint[2/2]", "Verified that multipatient list visible ");
		
		}
	
	@Test(groups ={"Chrome","Edge","IE11","US1999","Positive","E2E","F994"})
	public void test22_US1999_TC9119_verifyNoEntryInSearchAndHistoryTabForNonUI() throws InterruptedException, SQLException  
		{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that no entry is created for under Search and Viewed History  tab if user visit the viewer page through NON UI login.");
		loginPage = new LoginPage(driver);
		
		db=new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE);

       // Accessing Viewer page thorugh NON UI for AH.4 patient and verifying entry is present or not
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		
		String myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
	    loginPage.logout();
	    loginPage.openNewWindow();
	    loginPage.switchToNewWindow(2);
	    
		loginPage.navigateToBaseURL();
	    loginPage.login(username, password);
	    patientPage=new PatientListPage(driver);
	    patientPage.click(patientPage.searchAndViewedHistoryTab);
	    patientPage.waitForPatientPageToLoad();
	    patientPage.assertFalse(patientPage.verifyPatientInSearchViewedHistoryTab(patientName),"Checkpoint[1/2]","Verifying that patient entry is present or not on Search and Viewed history tab");
		db=new DatabaseMethods(driver);
	    db.assertEquals(db.VerifyEntryInRecentlyViewedPatientTable(), 0, "Checkpoint[2/2]", "Verifying count in dbo.RecentlyViewedPatient table");
	    
		}
		
	//US2144: Eureka Viewer Toolbar
	@Test(groups ={"Chrome","Edge","IE11","US2144","Negative","E2E","F1081"})
	public void test22_US2144_TC9614_verifyPatientIconForNonUILogin() throws InterruptedException  
		{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 'Patients' button functionality from the viewer tool bar.");
		loginPage = new LoginPage(driver);
			
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
	
		String myURL =loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		
	    loginPage.navigateToURL(myURL);			
	    patientPage=new PatientListPage(driver);
	    patientPage.waitForTimePeriod(2000);
	  
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/4]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		patientPage.click(patientPage.patientNamesList.get(0));
		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		patientPage.assertTrue(viewerPage.isEnabled(viewerPage.patientsIcon), "Checkpoint[2/4]", "Verified that patient icon is enable on viewer toolbar.");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[3/4]", "Verified that patient icon is enable on viewer toolbar.");
		
		viewerPage.click(viewerPage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[4/4]","Verified that user is navigate back to patient list page after click on patient back icon.");
		}
	
	@Test(groups ={"Chrome","Edge","IE11","US2144","DR2459","Negative","E2E","F1081"})
	public void test23_US2144_TC9614_DR2459_TC9880_verifyPatientIconForNonUILoginUsingPatientID()  
		{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 'Patients' button functionality from the viewer tool bar.<br>"+
		"Verify that user is navigated back to the patients page on click of the patients arrow present on the viewer; when patients page is accessed through Non UI method");
		loginPage = new LoginPage(driver);
			
		String token = RESTUtil.getToken(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password);
	
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientID);
		hm.put(OrthancAndAPIConstants.TOKEN_URL,token);
		
		String myURL =loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		
	    loginPage.navigateToURL(myURL);		
	    patientPage=new PatientListPage(driver);
	    patientPage.clickOntheFirstStudy();
	    viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		patientPage.assertTrue(viewerPage.isEnabled(viewerPage.patientsIcon), "Checkpoint[1/3]", "Verified that patient icon is enable on viewer toolbar.");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[2/3]", "Verified that patient icon is enable on viewer toolbar.");
		
		viewerPage.click(viewerPage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/3]","Verified that user is navigate back to patient list page after click on patient back icon on viewer.");
		}
	
	@Test(groups ={"Chrome","Edge","IE11","US2144","DR2458","Negative","E2E","F1081"})
	public void test24_US2144_TC9614_DR2458_TC9879_verifyPatientIconForNonUILoginUsingStudyUID() throws SQLException  
		{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 'Patients' button functionality from the viewer tool bar.<br>"+
		"Verify that 'Patients' button on tool bar is  disabled when accessed viewer using Non-UI login on a new browser instance.");
		loginPage = new LoginPage(driver);
			
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(patientName);
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying the patient icon on viewer toolbar using Non UI login.");
		String myURL =loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		
	    loginPage.navigateToURL(myURL);		
	    viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[1/1]", "Verified that patient icon is disable when viewer page is directly access using Study UID.");
		
		}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2439","Positive"})
	public void test25_01_DR2439_TC9679_verifyPatientsIconUrlLaunchWay() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact Non-UI]: Verify that clicking on browser back button on viewer page does not navigate to the Login page.");
		
		db= new DatabaseMethods(driver);
		String id = db.getPatientID(patientName);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,id);
		loginPage = new LoginPage(driver);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);

		loginPage.navigateToURL(myURL);
		
		patientPage = new PatientListPage(driver);
		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		
		patientPage.assertTrue(viewerPage.isEnabled(viewerPage.patientsIcon), "Checkpoint[1/3]", "Verified that patient icon is enable on viewer toolbar.");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[2/3]", "Verified that patient icon is enable on viewer toolbar.");
		
		viewerPage.click(viewerPage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/3]","Verified that user is navigate back to patient list page after click on patient back icon on viewer.");
	
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2439","Positive"})
	public void test25_02_DR2439_TC9679_verifyPatientsIconUrlLaunchWay() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact Non-UI]: Verify that clicking on browser back button on viewer page does not navigate to the Login page.");
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientIdAh4_US675);
		loginPage = new LoginPage(driver);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);

		loginPage.navigateToURL(myURL);
		
		patientPage = new PatientListPage(driver);
		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		
		patientPage.assertTrue(viewerPage.isEnabled(viewerPage.patientsIcon), "Checkpoint[1/3]", "Verified that patient icon is enable on viewer toolbar.");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[2/3]", "Verified that patient icon is enable on viewer toolbar.");
		
		viewerPage.click(viewerPage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/3]","Verified that user is navigate back to patient list page after click on patient back icon on viewer.");
	
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2439","Positive"})
	public void test26_DR2439_TC9682_verifyPatientsIconWhenAccessedUsingToken()  
		{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[With token]: Verify that login page is not displayed on browser back from viewer to patients page.");
		loginPage = new LoginPage(driver);
			
		String token = RESTUtil.getToken(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password);
	
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.TOKEN_URL,token);
		
		String myURL =loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		
	    loginPage.navigateToURL(myURL);		
	    patientPage=new PatientListPage(driver);
	    patientPage.clickOntheFirstStudy();
	    viewerPage = new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		patientPage.assertTrue(viewerPage.isEnabled(viewerPage.patientsIcon), "Checkpoint[1/3]", "Verified that patient icon is enable on viewer toolbar.");
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[2/3]", "Verified that patient icon is enable on viewer toolbar.");
		
		viewerPage.click(viewerPage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/3]","Verified that user is navigate back to patient list page after click on patient back icon on viewer.");
		}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2570","Negative","F1081"})
	public void test27_DR2570_TC10282_verifyMatchingPatientUsingPatientID() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify only patients macthing the given parameters in non-UI login are displayed back on PatientsList page when clicked on 'Patients' button after accessing the viewer.");

		db=new DatabaseMethods(driver);
		db.updatePatientID(IHEMammoTestPatientName, patientID);
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientID); 
		
		//patient list page using patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify display of match patient record using patients icon on viewerpage.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		//scenario using patient back icon on viewer
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/8]","Verified that patient list page loaded by using Patient ID and " +patientPage.getCurrentPageURL()+" is displaying");	
		patientPage.assertEquals(patientPage.patientRows.size(), 2, "Checkpoint[2/8]","Verified that only match patient records visible for given patient ID.");
		
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		patientPage.clickOntheFirstStudy();
		viewerPage=new ViewerTextOverlays(driver);
		viewerPage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/8]","Verified that viewer page loaded for the selected patient and " +patientPage.getCurrentPageURL()+" is displaying");	
		
		viewerPage.click(viewerPage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientRows.size(), 2, "Checkpoint[4/8]","Verified that only match patient records visible for given patient ID after click on Patients Icon from viewer page.");
		
		//scenario using browser back button
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify display of match patient record using browser back.");
		patientPage.openNewWindow(myURL);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[5/8]","Verified that patient list page loaded by using Patient ID and  " +patientPage.getCurrentPageURL()+" is displaying");	
		patientPage.assertEquals(patientPage.patientRows.size(), 2, "Checkpoint[6/8]","Verified that only match patient records visible for given patient ID");
		
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[7/8]","Verified that viewer page loaded for the selected patient and  " +patientPage.getCurrentPageURL()+" is displaying");	
		
		viewerPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientRows.size(), 2, "Checkpoint[8/8]","Verified that only match patient records visible for given patient ID after click on browser back from viewer page.");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2570","Negative","F1081"})
	public void test28_DR2570_TC10282_verifyMatchingPatientUsingIssuerOfPatientID() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify only patients macthing the given parameters in non-UI login are displayed back on PatientsList page when clicked on 'Patients' button after accessing the viewer.");

		String issuerId="commonIssuerID";
		db=new DatabaseMethods(driver);
		db.updateIssuerOfPatientID(ChestCT1p25mm, issuerId);
		db.updateIssuerOfPatientID(IBL_JohnDoe_PatientName, issuerId);
		db.updateIssuerOfPatientID(imbio_PatientName, issuerId);
		
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,issuerId); 
		
		//study page using patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using valid Issuer of patient ID");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		//scenario using patient back icon on viewer
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/2]","Verified that patient list page loaded by using Issuer of Patient ID and " +patientPage.getCurrentPageURL()+" is displaying");	
		patientPage.assertEquals(patientPage.patientRows.size(), 3, "Checkpoint[2/2]","Verified that only patient matching the given parameter is displayed..");
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2570","Negative","E2E","F1081"})
	public void test29_DR2570_TC10282_verifyMatchingStudiesUsingAccessionNo() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify only patients macthing the given parameters in non-UI login are displayed back on PatientsList page when clicked on 'Patients' button after accessing the viewer.");

		db=new DatabaseMethods(driver);
		db.addStudiesInDB(patientName, studyDescription);
	    db.updateAccessionNoInStudyTable(patientName, accNo);
		
		loginPage = new LoginPage(driver);
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo); 
		
		//study page using patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of study page from non-ui login using valid accession number.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		//scenario using patient back icon on viewer
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL),"Checkpoint[1/3]","Verified that patient list page is displayed.");	
		patientPage.assertEquals(patientPage.patientRows.size(), 1, "Checkpoint[2/3]","Verified that single patient is loaded on patient list page.");
		patientPage.assertEquals(patientPage.studyRows.size(), 11, "Checkpoint[3/3]","Verified that only matching studies with accession number is displayed on study list pane.");
			
	}
	
	//DR2881: State Indicator and send to PACS icon not visible in thumbnail for Non UI login scenario.
	@Test(groups ={"Chrome","Edge","IE11","DR2881","Negative","F1125"})
	public void test29_DR2881_TC11108_verifySendtoPACSIconOnNonUILogin() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the icons are displayed on Output Panel thumbnail tool bar on Non-UI login.");
		
		loginPage = new LoginPage(driver);
		
		db=new DatabaseMethods(driver);
		String accessionNumber=db.getAccessionNumber(patientNameDICOMRT);
		
		int count=4;
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accessionNumber); 
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login for "+patientNameDICOMRT+" using accession number.");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
		ViewerPage viewerpage=new ViewerPage(driver);
	
		OutputPanel panel=new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);
		 for(int i=0;i<count;i++)
		 {
			 panel.mouseHover(panel.findingTileContainers.get(i));
			 panel.assertTrue(panel.isElementPresent(panel.sendToPacsIcons.get(i)), "Checkpoint[1."+(i+1)+"/7]", "Verified that send to pacs Icon is visible on finding-"+(i+1));
			 panel.assertTrue(panel.verifyPendingStateInThumbnail(i+1), "Checkpoint[2."+(i+1)+"/7]", "Verified that Pending state icon is visible on finding-"+(i+1));
		 }	
		 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login for "+ChestCT1p25mm+" using study UID and Issuer of patient ID."); 
		db.updateIssuerOfPatientID(ChestCT1p25mm,accNo);
		String studyUID=db.getStudyInstanceUID(ChestCT1p25mm);
			
		 hm=new LinkedHashMap<String,String>();  
		 hm.put(LoginPageConstants.USERNAME,username);
		 hm.put(LoginPageConstants.PASSWORD,password); 
		 hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,accNo); 
		 hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID); 
		 
		 myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		//scenario using patient back icon on viewer
		loginPage.navigateToURL(myURL);
		viewerpage.waitForViewerpageToLoad(2);
		viewerpage.closeNotification();
        panel.enableFiltersInOutputPanel(false, false, true);
			 for(int i=0;i<count;i++)
			 {
				 panel.mouseHover(panel.findingTileContainers.get(i));
				 panel.assertTrue(panel.isElementPresent(panel.sendToPacsIcons.get(i)), "Checkpoint[3."+(i+1)+"/7]", "Verified that send to pacs Icon is visible on finding-"+(i+1));
				 panel.assertTrue(panel.isElementPresent(panel.commentIconOnTiles.get(i)), "Checkpoint[4."+(i+1)+"/7]", "Verified that Comment icon is visible on finding-"+(i+1));
				 panel.assertTrue(panel.verifyPendingStateInThumbnail(i+1), "Checkpoint[5/7]", "Verified that Pending state icon is visible on finding-"+(i+1));
			 }	
			 
		int srThumbnail=panel.findingTileContainers.size();
			 panel.mouseHover(panel.findingTileContainers.get(srThumbnail-1));
			 panel.assertTrue(panel.isElementPresent(panel.sendToPacsIcons.get(srThumbnail-1)), "Checkpoint[6/7]", "Verified send to pacs icon for SR thumbnail.");
			 panel.assertTrue(panel.verifyPendingStateInThumbnail(srThumbnail), "Checkpoint[7/7]", "Verified state of SR thumbnail is pending.");
	}
	
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException{
		DatabaseMethods db = new DatabaseMethods(driver);

		db.updateAccessionNoInStudyTable(patientName, "");
		db.updateAccessionNoInStudyTable(bonagePatientName, "");
		db.updateAccessionNoInStudyTable(IBL_JohnDoe_PatientName, "");
		db.updatePatientID(IBL_JohnDoe_PatientName, IBL_JohnDoe_PatientName);
		db.removeAddedStudiesInDB(patientName, studyDescription);
		db.updatePatientID(IHEMammoTestPatientName, cadPatientId);
		db.updateIssuerOfPatientID(ChestCT1p25mm, "");
		db.updateIssuerOfPatientID(IBL_JohnDoe_PatientName, "");
		db.updateIssuerOfPatientID(imbio_PatientName, "");	
		
	}

}