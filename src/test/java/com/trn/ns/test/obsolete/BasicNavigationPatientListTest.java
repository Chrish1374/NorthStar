//import org.testng.annotations.Test;
//
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////package com.trn.ns.test.patientList;
////
////import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
////
////import java.util.LinkedHashMap;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////
////import com.relevantcodes.extentreports.ExtentTest;
////import com.trn.ns.dataProviders.DataProviderArguments;
////import com.trn.ns.dataProviders.ExcelDataProvider;
////import com.trn.ns.page.constants.LoginPageConstants;
////import com.trn.ns.page.constants.PatientPageConstants;
////import com.trn.ns.page.constants.PatientXMLConstants;
////import com.trn.ns.page.constants.URLConstants;
////import com.trn.ns.page.factory.LoginPage;
////
////import com.trn.ns.page.factory.PatientListPage;
////
////import com.trn.ns.page.factory.ViewerPage;
////import com.trn.ns.test.base.TestBase;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.DataReader;
////import com.trn.ns.utilities.ExtentManager;
////
////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
////public class BasicNavigationPatientListTest extends TestBase {
////	private PatientListPage patientPage;
////	private LoginPage loginPage;
////	
////	private ExtentTest extentTest;
////	private ViewerPage viewerPage;
////	private String actualUrl, myURL;
////	private String ah4Filepath, aidocFilepath, patientNameAH4, patientNameAidoc, patientIDAH4, studyUIDAH4, patientIDAidoc, studyUIDAidoc, viewerAH4Url, viewerAidocUrl;
////	private LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
////	private LinkedHashMap<String, String> hmBlank = new LinkedHashMap<String, String>();
////
////
////	public BasicNavigationPatientListTest() {
////		ah4Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
////		aidocFilepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath"); 
////		patientNameAH4 = DataReader.getPatientDetails(
////				PatientXMLConstants.PATIENT_NAME, ah4Filepath);
////		patientIDAH4 = DataReader.getPatientDetails(
////				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, ah4Filepath);
////		studyUIDAH4 = DataReader.getPatientDetails(
////				PatientXMLConstants.STUDY_UID_TEXTOVERLAY, ah4Filepath);
////		patientNameAidoc = DataReader.getPatientDetails(
////				PatientXMLConstants.PATIENT_NAME, aidocFilepath);
////		patientIDAidoc = DataReader.getPatientDetails(
////				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, aidocFilepath);
////		studyUIDAidoc = DataReader.getPatientDetails(
////				PatientXMLConstants.STUDY_UID_TEXTOVERLAY, aidocFilepath);
//////		patientAH4Url = URLConstants.SINGLE_PATIENT_LIST_URL + "/" + patientIDAH4;
////		viewerAH4Url = URLConstants.VIEWER_PAGE_URL + "/" + studyUIDAH4;
//////		patientAidocUrl = URLConstants.SINGLE_PATIENT_LIST_URL + "/" + patientIDAidoc;
////		viewerAidocUrl = URLConstants.VIEWER_PAGE_URL + "/" + studyUIDAidoc;
////		hm.put(LoginPageConstants.PASSWORD, TEST_PROPERTIES.get("nsPassword"));
////		hm.put(LoginPageConstants.USERNAME, TEST_PROPERTIES.get("nsUserName"));
////
////	}
////
////	@Test(groups = { "Chrome", "Edge", "IE11", "DE943", "Positive"})
////	public void test03_TC3803_NonUILoginToAccessStudylistWithoutCredential()
////			throws InterruptedException {
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify URL launch of patient/study list without username and password should redirect to login page.");
////
////		//Step 1
////		loginPage = new LoginPage(driver);
////
////		//AIdoc - With username and password 
////		myURL = loginPage.getNonUILaunchURL(patientAidocUrl, hm);
////
////
////		//AIdoc study page displayed
////		loginPage.navigateToURL(myURL);
////		
////		actualUrl = studyPage.getCurrentPageURL();
////		//Verifying login page URL contains "Login"
////		loginPage.assertTrue(actualUrl.contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verify User is redirected to patient page", "verified");
////
////		//Step 2
////		//AH.4 without username and password
////		
////		myURL = studyPage.getNonUILaunchURL(patientAH4Url, hmBlank);
////		studyPage.navigateToURL(myURL);
////
////		//Fetching login page URL
////		actualUrl = loginPage.getCurrentPageURL();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3803_2][1/3]", "Verify User is redirected to login page");
////
////		//Verifying login page URL contains "Login"
////		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify User is redirected to login page", "verified");
////
////		//Step 3
////		//Refresh = loggout
////		loginPage.refreshWebPage();
////
////		//Patient list page without username and password
////		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hmBlank);
////		studyPage.navigateToURL(myURL);
////		studyPage.waitForURLToChange();
////		actualUrl = loginPage.getCurrentPageURL();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3803_3][2/3]", "Verify User is redirected to login page");
////		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify User is redirected to login page", "verified");
////
////		//Step 4
////		myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hmBlank);
////		studyPage.navigateToURL(myURL);
////		actualUrl = loginPage.getCurrentPageURL();
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3803_4][3/3]", "Verify User is redirected to login page");
////		loginPage.waitForLoginPageToLoad();
////		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify User is redirected to login page", "verified");
////
////	}
////}
//
//@Test(groups = { "Chrome", "Edge", "IE11", "DE943", "Positive"})
//	public void test05_TC3827_VerifyAccessToAllPagesWithCredentials()
//			throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify With credentials for each request: User has access to all pages(when used Non-UI login to access studylist)");
//
//		loginPage = new LoginPage(driver);
//		myURL = loginPage.getNonUILaunchURL(patientAidocUrl, hm);
//		loginPage.navigateToURL(myURL);
//		
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3827_1][1/4]", "Verify AIDOC data set opens with the study list page details");
//		studyPage.assertTrue(studyPage.getText(studyPage.PatientName).contains(patientNameAidoc), "Verify AIDOC data set opens with the study list page details", "verified");
//		myURL = studyPage.getNonUILaunchURL(patientAH4Url, hm);
//		studyPage.navigateToURL(myURL);
//		studyPage.waitForURLToChange();
//		studyPage.refreshWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3827_2][2/4]", "Verify User is redirected to single patient study list of AH4");
//		studyPage.assertTrue(studyPage.getText(studyPage.PatientName).contains(patientNameAH4), "Verify User is redirected to single patient study list of AH4", "verified");
//		myURL = loginPage.getNonUILaunchURL(patientAidocUrl, hm);
//		loginPage.navigateToURL(myURL);
//		loginPage.waitForURLToChange();
//		loginPage.refreshWebPage();
//		studyPage.assertTrue(studyPage.getText(studyPage.PatientName).contains(patientNameAidoc), "Verify AIDOC data set opens with the study list page details", "verified");
//		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
//		loginPage.navigateToURL(myURL);
//		studyPage.waitForURLToChange();
//		studyPage.refreshWebPage();
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3827_3][3/4]", "Verify User is redirected to patient list page");
//		patientPage.assertEquals(patientPage.getText(patientPage.columnHeaders.get(0)), PatientPageConstants.PATIENTID_TEXT, "Verify User is redirected to patient list page", "verified");
//		myURL = patientPage.getNonUILaunchURL(patientAidocUrl, hm);
//		patientPage.navigateToURL(myURL);
//		myURL = patientPage.getNonUILaunchURL(viewerAH4Url, hm);
//		patientPage.navigateToURL(myURL);
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		actualUrl = viewerPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3827_4][4/4]", "Verifying User is redirected viewer page of AH4");
//		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying User is redirected viewer page of AH4", "verified");
//
//	}
