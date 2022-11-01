package com.trn.ns.test.patientList;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.util.LinkedHashMap;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class BasicNavigationPatientListTest extends TestBase {
	private PatientListPage patientPage;
	private LoginPage loginPage;
	
	private ExtentTest extentTest;
	private ViewerPage viewerPage;
	private String actualUrl, myURL;
	private String ah4Filepath, aidocFilepath, patientNameAH4, patientNameAidoc, patientIDAH4, studyUIDAH4, patientIDAidoc, studyUIDAidoc, viewerAH4Url, viewerAidocUrl,patientAH4Url;
	private LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> hmBlank = new LinkedHashMap<String, String>();


	public BasicNavigationPatientListTest() {
		ah4Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
		aidocFilepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath"); 
		patientNameAH4 = DataReader.getPatientDetails(
				PatientXMLConstants.PATIENT_NAME, ah4Filepath);
		patientIDAH4 = DataReader.getPatientDetails(
				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, ah4Filepath);
		studyUIDAH4 = DataReader.getPatientDetails(
				PatientXMLConstants.STUDY_UID_TEXTOVERLAY, ah4Filepath);
		patientNameAidoc = DataReader.getPatientDetails(
				PatientXMLConstants.PATIENT_NAME, aidocFilepath);
		patientIDAidoc = DataReader.getPatientDetails(
				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, aidocFilepath);
		studyUIDAidoc = DataReader.getPatientDetails(
				PatientXMLConstants.STUDY_UID_TEXTOVERLAY, aidocFilepath);
		patientAH4Url = URLConstants.PATIENT_LIST_URL + "/" + patientIDAH4;
		viewerAH4Url = URLConstants.VIEWER_PAGE_URL + "/" + studyUIDAH4;
//		patientAidocUrl = URLConstants.SINGLE_PATIENT_LIST_URL + "/" + patientIDAidoc;
		viewerAidocUrl = URLConstants.VIEWER_PAGE_URL + "/" + studyUIDAidoc;
		hm.put(LoginPageConstants.PASSWORD, TEST_PROPERTIES.get("nsPassword"));
		hm.put(LoginPageConstants.USERNAME, TEST_PROPERTIES.get("nsUserName"));

	}

	@Test(groups = { "Chrome", "Edge", "IE11", "DE943", "Positive"})
	public void test01_TC3800_BasicNavigationFromPatientListPage()
			throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify After navigation back to patient list page, user can again navigate to study list and then to viewer page");

		loginPage = new LoginPage(driver);
		// Access patient page URL by appending patient ID
		loginPage.navigateToBaseURL();
		loginPage.login(hm.get(LoginPageConstants.USERNAME), hm.get(LoginPageConstants.PASSWORD));
		patientPage = new PatientListPage(driver);
		HelperClass helper=new HelperClass(driver);
		viewerPage=helper.loadViewerPageUsingSearch(patientNameAH4, 1, 1);
		

		// Verify patient page
		viewerPage.waitForViewerpageToLoad();
		actualUrl = viewerPage.getCurrentPageURL();

		//opening patient list in directly 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3800_1][1/1]", "Verify After navigation back to patient list page, user can again navigate to study list and then to viewer page");

		//verifying patient list url 
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying the launching of Viewer page", "User is on "+ viewerPage.getCurrentPageURL()+" page");
		viewerPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForEndOfAllAjaxes();
		actualUrl = patientPage.getCurrentPageURL();
		patientPage.assertTrue(actualUrl.contains(URLConstants.PATIENT_LIST_URL), "Verifying the launching of Patient list page", "User is on "+ patientPage.getCurrentPageURL()+" page");
		patientPage.clickOnPatientRow(patientNameAidoc);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		actualUrl = viewerPage.getCurrentPageURL();
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying the launching of Viewer page", "User is on "+ viewerPage.getCurrentPageURL()+" page");
		viewerPage.navigateToBack();
		actualUrl = patientPage.getCurrentPageURL();
		patientPage.assertTrue(actualUrl.contains(URLConstants.PATIENT_LIST_URL), "Verifying the launching of Patient list page", "User is on "+ patientPage.getCurrentPageURL()+" page");

	}

	@Test(groups = { "Chrome", "Edge", "IE11", "DE943", "Positive"})
	public void test02_TC3801_TC3802_VerifyNavigatingBackOnStudyList()
			throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Back button navigates to the initial page accessed from internet from StudyList"
				+"<br> Back button navigates to the initial page accessed from internet from Viewer Page.");

		loginPage = new LoginPage(driver);
		myURL = URLConstants.GOOGLE_URL; 
		loginPage.navigateToURL(myURL);
		actualUrl = loginPage.getCurrentPageURL();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3801_4][3/5]", "Verify Back button navigates to the initial page accessed from internet");
		loginPage.assertTrue(actualUrl.contains(URLConstants.GOOGLE_URL), "Verifying access google page from the internet", "verified");
		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl, hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		actualUrl = viewerPage.getCurrentPageURL();
		//verifying patient list url 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3802_1][4/5]", "Verify AIDOC data set opens with the viewer page");
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying AIDOC data set opens with the viewer page", "verified");
		viewerPage.navigateToBack();
		actualUrl = loginPage.getCurrentPageURL();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3802_2][5/5]", "Verify Back button navigates to the initial page accessed from internet");
		loginPage.assertTrue(actualUrl.contains(URLConstants.GOOGLE_URL), "Verifying access google page from the internet", "verified");

	}
	
	@Test(groups = { "Chrome", "Edge", "IE11", "DE943", "Positive"})
	public void test03_DE943_TC3804_VerifyAccessToSpecificStudyWithoutCredential()
			throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User has access to specific study (when used Non-UI login to access viewer page) without credential in url");

		loginPage = new LoginPage(driver);
		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl, hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		actualUrl = viewerPage.getCurrentPageURL();
		//verifying patient list url 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3804_1][1/4]", "Verify AIDOC data set opens with the viewer page");
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying AIDOC data set opens with the viewer page", "verified");
		myURL = loginPage.getNonUILaunchURL(viewerAH4Url, hmBlank);
		viewerPage.navigateToURL(myURL);
		actualUrl = loginPage.getCurrentPageURL();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3804_2][2/4]", "Verify User is redirected to login page");
		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify User is redirected to login page", "verified");
		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl, hm);
		loginPage.navigateToURL(myURL);
		viewerPage.waitForViewerpageToLoad();
		actualUrl = viewerPage.getCurrentPageURL();
		//verifying patient list url 
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying AIDOC data set opens with the viewer page", "verified");
		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hmBlank);
		viewerPage.navigateToURL(myURL);
    	actualUrl = loginPage.getCurrentPageURL();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3804_3][2/4]", "Verify User is redirected to login page");
		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify User is redirected to login page", "verified");

		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl, hm);
		loginPage.navigateToURL(myURL);
		viewerPage.waitForViewerpageToLoad();
		actualUrl = viewerPage.getCurrentPageURL();
		//verifying patient list url 
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Verifying AIDOC data set opens with the viewer page", "verified");
		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hmBlank);
		viewerPage.navigateToURL(myURL);
		actualUrl = loginPage.getCurrentPageURL();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3804_4][4/4]", "Verify User is redirected to login page");
		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify User is redirected to login page", "verified");

	}

	@Test(groups = { "Chrome", "Edge", "IE11", "DE943", "Positive"})
	public void test04_TC3828_NonUILoginToAccessViewer()
			throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify with credentials for each request: User has access to all viwer pages");

		loginPage = new LoginPage(driver);
		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl, hm);
		loginPage.navigateToURL(myURL);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3828_1][1/4]", "Verifying AIDOC data set opens with the viewer page");
		viewerPage.assertTrue(viewerPage.getPatientIDOverlayText(1).contains(patientIDAidoc), "Verifying AIDOC data set opens with the viewer page", "verified");
		myURL = viewerPage.getNonUILaunchURL(viewerAH4Url, hm);
		viewerPage.navigateToURL(myURL);
		viewerPage.waitForURLToChange();
		viewerPage.refreshWebPage();
		viewerPage.waitForTimePeriod(1000);
		viewerPage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3828_2][2/4]", "Verifying User is redirected viewer page of AH4");
		viewerPage.assertTrue(viewerPage.getPatientIDOverlayText(1).contains(patientIDAH4), "Verifying User is redirected viewer page of AH4", "verified");
		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl, hm);
		loginPage.navigateToURL(myURL);
		viewerPage.waitForURLToChange();
		viewerPage.refreshWebPage();
		viewerPage.waitForTimePeriod(1000);
		viewerPage.waitForViewerpageToLoad();
//		myURL = viewerPage.getNonUILaunchURL(patientAH4Url, hm);
//		viewerPage.navigateToURL(myURL);
//		viewerPage.waitForURLToChange();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3828_3][4/4]", "Verifying User is redirected to single patient study list of AH4");
//		studyPage.assertTrue(studyPage.getText(studyPage.PatientName).contains(patientNameAH4), "Verify User is redirected to single patient study list of AH4", "verified");
//		myURL = studyPage.getNonUILaunchURL(viewerAidocUrl, hm);
//		studyPage.navigateToURL(myURL);
//		viewerPage.waitForURLToChange();
//		viewerPage.refreshWebPage();
//		viewerPage.waitForViewerpageToLoad();
		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
		loginPage.navigateToURL(myURL);
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3828_4][4/4]", "Verifying User is redirected to patient list page");
		patientPage.assertEquals(patientPage.getText(patientPage.patientIDHeader), PatientPageConstants.PATIENTID_TEXT, "verify User is redirected to patient list page", "verified");
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1916","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test_TC7765"})
	public void test05_DE1916_TC7765_MultiplePatientNavigationFromListing(String fileName)
			throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to navigate between patient list and viewer page");
		loginPage = new LoginPage(driver);

		loginPage.navigateToBaseURL();
		loginPage.login(hm.get(LoginPageConstants.USERNAME), hm.get(LoginPageConstants.PASSWORD));
		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		HelperClass helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, 1);
		actualUrl = viewerPage.getCurrentPageURL();
		viewerPage.assertTrue(actualUrl.contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[1/2]","Verified that User is on "+ viewerPage.getCurrentPageURL()+" page");

		viewerPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		actualUrl = patientPage.getCurrentPageURL();
		patientPage.assertTrue(actualUrl.contains(URLConstants.PATIENT_LIST_URL),"Checkpoint[2/2]","Verified that User is on "+ patientPage.getCurrentPageURL()+" page");

	}

	
}
