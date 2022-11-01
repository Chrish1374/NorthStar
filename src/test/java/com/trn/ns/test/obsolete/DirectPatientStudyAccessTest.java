//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.ErrorOrLogoutPage;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class DirectPatientStudyAccessTest extends TestBase{
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//	private String filePath_Subject60 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
//	String subject60_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath_Subject60);
//
//	//AH4 patient Url to be accessed
//	private String issuerIDAH4=DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ISSUERID_TEXTOVERLAY, filePath), 
//			sUID_AH4Data =DataReader.getStudyDetails(PatientXMLConstants.STUDY_UID_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath),
//			viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/"+sUID_AH4Data,
//			invalid_SUID = "1234";
//
//
//	private String issuerIDSubject60=DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ISSUERID_TEXTOVERLAY, filePath_Subject60), 
//	sUID1_Subject60Data = DataReader.getStudyDetails(PatientXMLConstants.STUDY_UID_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath_Subject60),
//	viewerSubject60Url = URLConstants.VIEWER_PAGE_URL+"/"+sUID1_Subject60Data;
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test01_US728_TC2851_verifyChangedPatientPageUrl() {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the modified patient list page url");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//
//		//Verifying the modified patient page url
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that the Patient list url is changed");
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page url is updated", patientPage.getCurrentPageURL()+" is displaying");
//
//		//From non ui login
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing viewer page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying that the Patient list url from non ui login");
//		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
//
//		loginPage.navigateToURL(myURL);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page url is updated", patientPage.getCurrentPageURL()+" is displaying");
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test02_US728_TC2852_verifyChangedPatientsStudyPageUrl() {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the modified patient's study page url");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying user is redirected to Patient list page");
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
//		patientPage.clickOnPatientRow(patientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//
//		//Verifying the modified patients study page url
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that the Patient's study page url is changed");
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is updated", patientStudyPage.getCurrentPageURL()+" is displaying");
//		patientStudyPage.assertFalse(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_TEXT) , "Verify that patients study page should not contain single patient text", "Verified");
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(patientAH4Url) , "Verify the exact updated patient study page url", "Verified");
//
//		//From non ui login
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing viewer page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying the Patient's study page url from non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//
//		loginPage.navigateToURL(myURL);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(patientAH4Url) , "Verify the exact updated patient study page url", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test03_US728_TC2853_verifyNavigationFromPatientsPage() {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the navigation from patients page url");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying user is redirected to Patient list page");
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
//		patientPage.clickOnPatientRow(patientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying that the Patient's study page url is changed");
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is updated", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		patientStudyPage.navigateToBack();
//		patientPage.waitForPatientPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying user is redirected to Patient list page");
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
//
//		patientPage.navigateToForward();
//		patientStudyPage.waitForSingleStudyToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying user is redirected to Patient's study page");
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should display", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying navigation between patinets and viewer page");
//		patientStudyPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that patiens page should display", patientPage.getCurrentPageURL()+" is displaying");
//		patientStudyPage.navigateToURL(URLConstants.BASE_URL+viewerAH4Url);
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
//
//		viewerPage.navigateToBack();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that patiens page should display on browser back from viewer page", patientPage.getCurrentPageURL()+" is displaying");
//
//		patientPage.navigateToForward();
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display on browser forward from paients page", viewerPage.getCurrentPageURL()+" is displaying");
//
//		//from non ui login access
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing viewer page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying the navigation from non ui login");
//		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
//
//		loginPage.navigateToURL(myURL);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page url is displaying", patientPage.getCurrentPageURL()+" is displaying");
//		patientPage.clickOnPatientRow(patientName);
//
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displaying", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		patientStudyPage.navigateToBack();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display on browser back from patinet study page", patientPage.getCurrentPageURL()+" is displaying");
//
//		patientPage.navigateToForward();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should display on browser forward from patinets page", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test04_US728_TC2854_verifyNavigationFromPatientStudyPage() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the navigation from patient study page url");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying user is redirected to Patient list page");
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
//		patientPage.clickOnPatientRow(patientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying that user is on viewer page");
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page is  loaded", viewerPage.getCurrentPageURL()+" is displaying");
//
//		viewerPage.navigateToBack();
//		patientStudyPage.waitForSingleStudyToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying user is redirected to Patient study page on browser back from viewer page");
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should display", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		patientStudyPage.navigateToForward();
//		viewerPage.waitForViewerpageToLoad(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]","Verifying user is redirected to viewer page on browser forward from patient study page");
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verify that viewer page should display", viewerPage.getCurrentPageURL()+" is displaying");
//
//		//Accessing patient study and studylist page
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]","Verifying navigation between patinet study page and studylist page");
//		viewerPage.navigateToURL(URLConstants.BASE_URL+patientAH4Url);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should display", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		patientStudyPage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL);
//
//		studyPage = new StudyListPage(driver);
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL),"Verify that study list page should display", studyPage.getCurrentPageURL()+" is displaying");
//
//		studyPage.navigateToBack();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL),"Verify that patinet study page should display on browser back from study list page", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		viewerPage.navigateToForward();
//		studyPage.waitForStudyListToLoad();
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL),"Verify that study list page should display on browser forward from viewer page", studyPage.getCurrentPageURL()+" is displaying");
//
//		//from non ui login access
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing viewer page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]","Verifying the navigation from non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		String parentWindow = studyPage.getCurrentWindowID();
//		studyPage.openNewWindow(myURL);
//		List<String> childWinHandles = studyPage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		studyPage.switchToWindow(childWinHandles.get(0));
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL),"Verify that patient page should display", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		patientStudyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad(1);
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL),"Verify that viewer page is  loaded", viewerPage.getCurrentPageURL()+" is displaying");
//
//		viewerPage.navigateToBack();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL),"Verify that patients study page should display on browser back from viewer page", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		patientStudyPage.navigateToForward();
//		viewerPage.waitForViewerpageToLoad(1);
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL),"Verify that viewer page should display on browser forward from patient study page", viewerPage.getCurrentPageURL()+" is displaying");
//		patientPage.closeWindow(childWinHandles.get(0));
//		patientPage.switchToWindow(parentWindow);
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test05_US728_TC2855_verifyPatientStudyUrlAcessWithValidPatientIDAndStudyUID() {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify that user should be redirected to mention patient's study page when valid credentials , valid patient ID and valid study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//		//For patinet data having only one study
//		//Accessing patient study page url without study uid
//		LinkedHashMap<String, String> lhm=new LinkedHashMap<String,String>(); 
//		lhm.put(PatientPageConstants.PATIENT_ISSUER_ID_TEXT, issuerIDAH4);
//		lhm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		lhm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the launching of patient study page for AH.4 data with valid patient ID");
//		String myURLWithoutSUID = loginPage.getNonUILaunchURL(patientAH4Url, lhm);
//		loginPage.navigateToURL(myURLWithoutSUID);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed when access without Study UID", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//
//		//Accessing patient study page url with study uid
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(PatientPageConstants.PATIENT_ISSUER_ID_TEXT, issuerIDAH4);
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//		hm.put(PatientPageConstants.SUID_TEXT, sUID_AH4Data);
//
//		String myURLWithSUID = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURLWithSUID);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed when accessed with Study UID", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//For patinet data having multiple studies
//		//Accessing patient study page url without study uid
//		LinkedHashMap<String, String> lhm1=new LinkedHashMap<String,String>(); 
//		lhm1.put(PatientPageConstants.PATIENT_ISSUER_ID_TEXT, issuerIDSubject60);
//		lhm1.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		lhm1.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying the launching of patient study page for Subject60 patient data with valid patient ID");
//		String uRLWithoutSUID = loginPage.getNonUILaunchURL(patientSubject60Url, lhm1);
//		loginPage.navigateToURL(uRLWithoutSUID);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed when access without Study UID", patientStudyPage.getCurrentPageURL()+" is displaying");
//		patientStudyPage.assertFalse(patientStudyPage.getCurrentPageURL().contains(sUID1_Subject60Data) , "Verify that Study UID should display while accessing the patient study page url with study uid", "Verified");
//
//
//		//Accessing patient study page url with study uid
//		LinkedHashMap<String, String> hm1=new LinkedHashMap<String,String>(); 
//		hm1.put(PatientPageConstants.PATIENT_ISSUER_ID_TEXT, issuerIDSubject60);
//		hm1.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm1.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//		hm1.put(PatientPageConstants.SUID_TEXT, sUID1_Subject60Data);
//
//		String uRLWithSUID = loginPage.getNonUILaunchURL(patientSubject60Url, hm1);
//		loginPage.navigateToURL(uRLWithSUID);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed when accessed with Study UID", patientStudyPage.getCurrentPageURL()+" is displaying");
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(sUID1_Subject60Data) , "Verify that Study UID should display while accessing the patient study page url with study uid", "Verified");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test06_US728_TC2856_verifyNonUiLAccessWithChangedURL() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify that after successful non ui login if user changed the url, it will redirect to the login page");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(PatientPageConstants.PATIENT_ISSUER_ID_TEXT, issuerIDAH4);
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the launching of patient study page non-ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Access changed url and press refresh
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying that on changing url, user should redirect to the login page");
//		patientStudyPage.navigateToURL(URLConstants.BASE_URL+patientSubject60Url);
//		patientStudyPage.refreshWebPage();
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed", loginPage.getCurrentPageURL()+" is displaying");
//
//		//Verify the changing url for viewer page
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the changing of viewer page url from non-ui login access");
//		String myViewerURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
//		loginPage.navigateToURL(myViewerURL);
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page url is displayed", viewerPage.getCurrentPageURL()+" is displaying");
//
//		viewerPage.navigateToURL(URLConstants.BASE_URL+viewerSubject60Url);
//		viewerPage.refreshWebPage();
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed after changing url from viewer page", loginPage.getCurrentPageURL()+" is displaying");
//
//		//Verifying the same url in other tab
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying the same ui url access from other tab");
//		String parentWindow =loginPage.getCurrentWindowID();
//		loginPage.openNewWindow(URLConstants.BASE_URL);
//		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		loginPage.switchToWindow(childWinHandles.get(0));
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientPage = new PatientListPage(driver);
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Verify that patient page should display", patientPage.getCurrentPageURL()+" is displaying");
//
//		//access same patient page from other tab
//		patientPage.openNewWindow(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
//		childWinHandles = loginPage.getAllOpenedWindowsIDs();
//		patientPage.switchToWindow(childWinHandles.get(2));
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed on accessing patients page ui url from other tab", loginPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.closeWindow(childWinHandles.get(2));
//		loginPage.closeWindow(childWinHandles.get(1));
//		loginPage.switchToWindow(parentWindow);
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test07_US728_TC2857_verifyNonUiURLRefresh() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify that after successful non ui login if user reload the same url, it will remain on same page");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the launching of patient study page non-ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying the refresh from non-ui login");
//		patientStudyPage.refreshWebPage();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the browser back from non ui login");
//		patientStudyPage.browserBackWebPage();
//		patientStudyPage.assertFalse(patientStudyPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page shpuld not displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Refresh from different non ui pages
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the refresh from non-ui login for viewer page");
//		String myViewerURL = loginPage.getNonUILaunchURL(viewerAH4Url, hm);
//		loginPage.navigateToURL(myViewerURL);
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should displayed", viewerPage.getCurrentPageURL()+" is displaying");
//		viewerPage.refreshWebPage();
//		viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should displayed after refresh", viewerPage.getCurrentPageURL()+" is displaying");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test08_US728_TC2858_verifyNonUiURLWithInvalidPatientID() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify user should redirect to the error page when invalid patient ID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that error page should display when patinet page is accessed from non-ui login with invalid patient ID after successful non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Access the non ui login url with invalid patient ID
//		String myInvalidURL = patientStudyPage.getNonUILaunchURL(invaildPatientAH4Url, hm);
//		patientStudyPage.navigateToURL(myInvalidURL);
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//		//Accessing directly non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that error page should display when patinet page is accessed directly from non-ui login with invalid patient ID");
//
//		String parentWindow = errorpage.getCurrentWindowID();
//		errorpage.openNewWindow(myInvalidURL);
//		List<String> childWinHandles = errorpage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		errorpage.switchToWindow(childWinHandles.get(0));
//		errorpage.waitForErrorPageToLoad();
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//		errorpage.closeWindow(childWinHandles.get(0));
//		errorpage.switchToWindow(parentWindow);
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test09_US728_TC2859_verifyNonUiURLWithNoPatientID() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify user should redirect to the error page when no patient ID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that login page should display when patinet page is accessed from non-ui login with no patient ID after successful non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Access the non ui login url with invalid patient ID
//		String myInvalidURL = patientStudyPage.getNonUILaunchURL(noPatientAH4Url, hm);
//		patientStudyPage.navigateToURL(myInvalidURL);
//		//patientStudyPage.waitForSingleStudyToLoad();
//		//patientStudyPage.refreshWebPage();
//		patientStudyPage.waitForURLToChange();
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//		//Accessing directly non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that login page should display when patinet page is accessed directly from non-ui login with no patient ID");
//
//		String parentWindow = errorpage.getCurrentWindowID();
//		errorpage.openNewWindow(myInvalidURL);
//		List<String> childWinHandles = errorpage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		errorpage.switchToWindow(childWinHandles.get(0));
//		errorpage.waitForErrorPageToLoad();
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//		errorpage.closeWindow(childWinHandles.get(0));
//		errorpage.switchToWindow(parentWindow);
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test10_US728_TC2860_verifyNonUiURLWithInvalidSID() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify user should redirect to the error page when valid patient ID and invalid study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that error page should display when patinet page is accessed from non-ui login with invalid Study UID after successful non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Access the non ui login url with invalid patient ID
//		hm.put(PatientPageConstants.SUID_TEXT, invalid_SUID);
//		String myInvalidURL = patientStudyPage.getNonUILaunchURL(patientAH4Url, hm);
//		patientStudyPage.navigateToURL(myInvalidURL);
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//		//Accessing directly non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that error page should display when patinet page is accessed directly from non-ui login with invalid Study UID");
//
//		String parentWindow = errorpage.getCurrentWindowID();
//		errorpage.openNewWindow(myInvalidURL);
//		List<String> childWinHandles = errorpage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		errorpage.switchToWindow(childWinHandles.get(0));
//		errorpage.waitForErrorPageToLoad();
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_SUID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//		errorpage.closeWindow(childWinHandles.get(0));
//		errorpage.switchToWindow(parentWindow);
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","positive"})
//	public void test11_US728_TC2860_verifyNonUiURLWithNoSID() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify user should redirect to the error page when valid patient ID and invalid study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that patient study page should display when patinet page is accessed from non-ui login with blank Study UID after successful non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Access the non ui login url with blank study ID
//		hm.put(PatientPageConstants.SUID_TEXT, "");
//		String myValidURL = patientStudyPage.getNonUILaunchURL(patientAH4Url, hm);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//
//		//Accessing directly non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that patient study page should display when patinet page is accessed directly from non-ui login with blank Study UID");
//
//		String parentWindow = patientStudyPage.getCurrentWindowID();
//		patientStudyPage.openNewWindow(myValidURL);
//		List<String> childWinHandles = patientStudyPage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		patientStudyPage.switchToWindow(childWinHandles.get(0));
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page should displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//		patientStudyPage.closeWindow(childWinHandles.get(0));
//		patientStudyPage.switchToWindow(parentWindow);
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test12_US728_TC2862_verifyNonUiURLWithInvalidCombinationOfPatientIDAndStudyUID() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify user should redirect to the error page when invalid combination of patient ID and study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//
//		//Accessing patient study page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that error page should display when patient page is accessed from non-ui login with invalid combination of patient ID and Study UID after successful non ui login");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patients study page url is displayed", patientStudyPage.getCurrentPageURL()+" is displaying");
//
//		//Access the non ui login url with invalid patient ID
//		hm.put(PatientPageConstants.SUID_TEXT, sUID1_Subject60Data);
//		String myInvalidURL = patientStudyPage.getNonUILaunchURL(patientAH4Url, hm);
//		patientStudyPage.navigateToURL(myInvalidURL);
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		errorpage.assertEquals(errorpage.getText(errorpage.message),PatientPageConstants.INVALID_PATIENTID_SUID_COMBINATION_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//		//Accessing directly non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that error page should display when patient page is accessed directly from non-ui login with invalid combination of patient ID and Study UID");
//
//		String parentWindow = errorpage.getCurrentWindowID();
//		errorpage.openNewWindow(myInvalidURL);
//		List<String> childWinHandles = errorpage.getAllOpenedWindowsIDs();
//
//		childWinHandles.remove(parentWindow);
//		errorpage.switchToWindow(childWinHandles.get(0));
//		errorpage.waitForErrorPageToLoad();
//		errorpage.assertEquals(errorpage.getText(errorpage.message),PatientPageConstants.INVALID_PATIENTID_SUID_COMBINATION_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//		errorpage.closeWindow(childWinHandles.get(0));
//		errorpage.switchToWindow(parentWindow);
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test13_US728_TC2863_verifyUiURLWithInvalidPatientID(){
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("UI Login : Verify user should redirect to the error page when invalid patient ID is provided");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToURL(URLConstants.BASE_URL+invaildPatientAH4Url);
//		loginPage.waitForLoginPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that user is redirecting to login page when invalid patient ID is provided in case of ui access");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed", loginPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that user is redirecting to error page followed by login page");
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test14_US728_TC2864_verifyUiURLWithNoPatientID(){
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("UI Login : Verify user should redirect to the error page when no patient ID is provided");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToURL(URLConstants.BASE_URL+noPatientAH4Url);
//		loginPage.waitForLoginPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that user is redirecting to login page when no patient ID is provided in case of ui access");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed", loginPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that user is redirecting to error page followed by login page");
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test15_US728_TC2865_verifyUiURLWithInvalidStudyUID(){
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("UI Login : Verify user should redirect to the error page when valid patient ID and invalid study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		//Accessing ui url with study uid
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(PatientPageConstants.SUID_TEXT, invalid_SUID);
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//
//		loginPage.waitForLoginPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that user is redirecting to login page when invalid Stydy UID is provided in case of ui access");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed", loginPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that user is redirecting to error page followed by login page");
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
//		//		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test16_US728_TC2866_verifyUiURLWithNoStudyUID(){
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("UI Login : Verify user should redirect to the patient study page when valid patient ID and no study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		//Accessing ui url with blank study uid
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(PatientPageConstants.SUID_TEXT, "");
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//
//		loginPage.waitForLoginPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that user is redirecting to login page when no Study UID is provided in case of ui access");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed on accessing patients page ui url from other tab", loginPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that user is redirecting to error page followed by login page");
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
////		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US728","negative"})
//	public void test17_US728_TC2867_verifyUiURLWithInvalidStudyUID(){
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("UI Login : Verify user should redirect to the error page when invalid combination of patient ID and study instance UID is provided");
//
//		loginPage = new LoginPage(driver);
//
//		//Accessing ui url with study uid
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
//		hm.put(PatientPageConstants.SUID_TEXT, sUID1_Subject60Data);
//		String myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToURL(myURL);
//
//		loginPage.waitForLoginPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that user is redirecting to login page when invalid combination of patient ID and Study UID is provided in case of ui access");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Verify that login page url is displayed", loginPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying that user is redirecting to error page followed by login page");
//		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"verifying that user is on errorpage","verified");		
////		errorpage.assertFalse(errorpage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL) , "Verify that patient study page should not display", errorpage.getCurrentPageURL()+" is displaying");
//
//	}
//
//}
