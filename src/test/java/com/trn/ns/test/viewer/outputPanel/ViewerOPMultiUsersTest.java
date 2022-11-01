package com.trn.ns.test.viewer.outputPanel;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerOPMultiUsersTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private RegisterUserPage register;
	private OutputPanel panel;
	private HelperClass helper;
	private ViewerSliderAndFindingMenu findingMenu;

	String flag = "false";
	DatabaseMethods db= new DatabaseMethods(driver);

	// Get Patient Name

	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName1 = DataReader.getPatientDetails(
			PatientXMLConstants.PATIENT_NAME, AH4_Filepath);

	String liver9_Filepath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liverpatientName = DataReader.getPatientDetails(
			PatientXMLConstants.PATIENT_NAME, liver9_Filepath);

	String gspsData_filepath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POLYLINE_filepath");
	String patientName2 = DataReader.getPatientDetails(
			PatientXMLConstants.PATIENT_NAME, gspsData_filepath);

	String dx_filepath = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
	String patientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, dx_filepath);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String JohnDoe = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, JohnDoe);



	String username_1 = "user_1";
	String username_2 = "user_2";
	String username_3 = "user_3";
	String ANNOTATION_TXT_1="ABC";
	String circleComment="Circle comment";
	String webGLConsoleError ="Failed to execute 'shaderSource' on 'WebGLRenderingContext': parameter 1 is not of type 'WebGLShader'";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector contentSelector;

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientListPage = new PatientListPage(driver);

	}




	//US891 Find and display all GSPS findings data for the study without duplication in the output panel

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive","BVT" })
	public void test01_US891_TC3748_verifyAcceptedFindingsInOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be displayed in outpanel.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
		
		helper = new  HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liverpatientName, 1);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		OutputPanel panel = new OutputPanel(driver);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//navigate to fifth slice on viewbox1 and draw a circle annotation
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -50, 40, -50);


		HashMap<String, String> findings = circle.getGSPSFindingList(1);
		viewerPage.assertEquals(findings.size(),3, "Checkpoint[1/3]", "verifying the total findings");

		findings.forEach((k, v) -> viewerPage.assertEquals(v,username,"Checkpoint[2/3]","verify the created by")); 

		circle.closingConflictMsg();
		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), findings.size(), "Checkpoint[3/3]", "verifying the total findings");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive","BVT" })
	public void test02_US891_TC3749_verifyUserEditedFindingsInOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be displayed in console logs when user-B edits user-A's copy");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(liverpatientName, 1);

		ellipse = new EllipseAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

		Header header = new Header(driver);		
		header.logout();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with User A");
		loginPage.login(username_1, username_1);
		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(liverpatientName, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Draw any annotation on any series");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(2, 100, -50, 40, -50);

		HashMap<String, String> findings = ellipse.getGSPSFindingList(2);
		ellipse.assertEquals(findings.size(),1, "Checkpoint[1/9]", "verifying the total findings");
		findings.forEach((k, v) -> ellipse.assertEquals(v,username_1,"Checkpoint[2/9]","verify the created by")); 

		ellipse.closingConflictMsg();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying in output panel");
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/9]", "Annotations created by only user A should get displayed in Output panel");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logout from the application");
		header.logout();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with User B");
		loginPage.login(username_2, username_2);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "User B loading the patient "+patientName1);
		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(liverpatientName, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Editing the copy created by User A");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		findings = ellipse.getGSPSFindingList(1);
		panel.assertEquals(findings.size(),2, "Checkpoint[4/9]", "verifying the total findings");
		findings.forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[5/9]","verify the created by")); 

		panel.enableFiltersInOutputPanel(true, true, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), findings.size(), "Checkpoint[6/9]", "Annotations edited and created by User B should get displayed in output panel.");
		panel.openAndCloseOutputPanel(false);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logout from the application");
		header.logout();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with User A");
		loginPage.login(username_1, username_1);
		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(liverpatientName, 1);

		findings = ellipse.getGSPSFindingList(1);
		panel.assertEquals(findings.size(),2, "Checkpoint[7/9]", "verifying the total findings");
		findings.forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[8/9]","verify the created by")); 

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Validating the annotations edited by User B");
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), findings.size(), "Checkpoint[9/9]", "Annotations edited and created by User  B should get displayed in output panel.");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test03_US891_TC3750_verifyMachineGSPSOnOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be displayed in outpanel when only 1 Machine result present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName2 + "in viewer with one machine result");
		

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName2, 1);

		findingMenu = new ViewerSliderAndFindingMenu(driver);

		HashMap<String, String> findings = findingMenu.getGSPSFindingList(1);
		findingMenu.assertEquals(findings.size(),1, "Checkpoint[1/4]", "verifying the total findings");
		findings.forEach((k, v) -> findingMenu.assertEquals(v,"jamie","Checkpoint[2/4]","verify the created by field is displayed correctly"));

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		int lines = poly.getLinesOfPolyLine(1, 1).size();

		OutputPanel panel = new OutputPanel(driver);				
		panel.enableFiltersInOutputPanel(false, false, true);
		findingMenu.assertEquals(panel.thumbnailList.size(), findings.size(), "Checkpoint[3/4]", "Original machine results annotations should get displayed in output panel on \"Pending filter\".");
		panel.assertEquals(panel.getCountOfLinesInPolylineInThumbnail(1),lines,"Checkpoint[4/4]", "Verifying the annotation is displayed in thumbnail");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test04_US891_TC3751_verifyUserEditMachineFinding() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Find the list of the GSPS findings to be displayed in output panel, when user edits machine result.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName3, 1);

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		OutputPanel panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);

		List<String> results = contentSelector.getAllResults();
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving all the results from GSPS finding menu and content selector");
		HashMap<String, String> findingsListVb1 = findingMenu.getGSPSFindingList(1);

		results = contentSelector.getAllResults();
		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = findingMenu.getGSPSFindingList(1);

		findingMenu.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "logging using user A");
		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "loading the patient " +patientName3);
		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the machine result 2");
		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the annotation on result 2");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading user A copy");
		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Validating the result in output panel");
		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User A in "Accepted filter"and Original machine result1 in "pending filter" should get displayed in output panel.
		findingMenu.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/10]", "1 accepted finding");


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the machine results under pending tab");
		panel.enableFiltersInOutputPanel(true, true, true);
		findingMenu.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[2/10]", "verifying the machine results are same in output panel in GSPS finding menu");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,username_1,"Checkpoint[3/10]","verify the created by"));

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,findingsListVb2.get(k),"Checkpoint[4/10]","verify the findings after selecting the second result"));

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,findingsListVb1.get(k),"Checkpoint[5/10]","verify the findings after selecting the first result"));

		contentSelector.openAndCloseSeriesTab(false);

		header.logout();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging using user scan");
		loginPage.login(username,password);		

		patientListPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName3, 1);
		panel.waitForTimePeriod(5000);
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User A in "Accepted filter"and Original machine result1 in "pending filter" should get displayed in output panel.
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/10]", "verifying the finding created by user A is displayed");
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[7/10]", "verifying the machine results");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,username_1,"Checkpoint[8/10]","verify the created by field"));

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,findingsListVb2.get(k),"Checkpoint[9/10]","verifying the findings after selection of second result"));

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,findingsListVb1.get(k),"Checkpoint[10/10]","verify the findings after selecting the first result"));



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive","Sanity","BVT" })
	public void test05_US891_TC3752_verifyUserEditMachineResultsFromAnotherUser() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Find the list of the GSPS findings to be displayed in output panel, when user edits machine result And user B edits user A copy.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName3, 1);

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		OutputPanel panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);

		List<String> results = contentSelector.getAllResults();		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving all the results and findings");
		HashMap<String, String> findingsListVb1 = findingMenu.getGSPSFindingList(1);
		results = contentSelector.getAllResults();

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = findingMenu.getGSPSFindingList(1);

		findingMenu.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging using user A");
		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient which has 2 machine results");
		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the second machine results");
		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Editing the second machine result");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "selecting the copy of annotation created by user A");
		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the output panel");
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		findingMenu.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/10]", "verifying the annotation created by UserA");
		panel.openAndCloseOutputPanel(false);

		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,username_1,"Checkpoint[2/10]","verify the created by"));



		header.logout();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging user using userB");
		loginPage.login(username_2,username_2);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the userA copy in viewer");
		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Editing the User A copy ");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);

		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the ouput panel");
		findingMenu.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[3/10]", "verifying the finding list");

		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,username_2,"Checkpoint[4/10]","verify the created by"));

		panel.enableFiltersInOutputPanel(true, true, true);
		findingMenu.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()+1), "Checkpoint[5/10]", "Verifying the machine results");

		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,username_2,"Checkpoint[6/10]","verify the created by"));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		findingMenu.getGSPSFindingList(1).forEach((k, v) -> findingMenu.assertEquals(v,username_1,"Checkpoint[7/7]","verify the created by"));


		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" ,"Sanity"})
	public void test06_US891_TC3753_verifyUserEditNonMachineResultsFromAnotherUser() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be displayed in outpanel when Machine result Loading, Machine result edit by and A and B and User C new annotations");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);

		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		OutputPanel panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);

		List<String> results = contentSelector.getAllResults();		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	

		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);

		List<String> source = contentSelector.getAllSeries();
		results = contentSelector.getAllResults();

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/15]", "Verifying the finding count when User B edits the original result");
		panel.openAndCloseOutputPanel(false);

		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/15]","verify the created by"));

		header.logout();
		loginPage.login(username_2,username_2);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);

		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[3/15]", "verifying the finding count when User B and Original machine result1 should get displayed in output panel");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[4/15]","verify the created by"));

		panel.enableFiltersInOutputPanel(true, true, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()+1), "Checkpoint[5/15]", "Verifying the total findings available under pending tab");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[6/15]","verifying the findings after selecting result 1"));

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb2.get(k),"Checkpoint[7/15]","verifying the findings after selecting result 2"));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[8/15]","verify the created by"));

		contentSelector.openAndCloseSeriesTab(false);

		header.logout();
		loginPage.login(username_3,username_3);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		panel.enableFiltersInOutputPanel(true, true, true);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()+2), "Checkpoint[9/15]", "Verifying the findings list when user 3 draws the annotations");

		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[10/15]", "Verifying the pending finding list");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[11/15]","verifying the findings after selecting the result 1"));

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb2.get(k),"Checkpoint[12/15]","verifying the findings after selecting result 2"));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[13/15]","verify the created by "+username_1));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_2+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[14/15]","verify the created by "+username_2));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_3+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[15/15]","verify the created by "+username_3));




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test07_US891_TC3754_TC3758_verifyUserEditNonMachineResultsFromAnotherUser() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings to be displayed in outpanel when Machine result Loading, Machine result edit by A and B and User C result loaded"
				+ "<br> 	Verify the list of findigs to be displayed When User B edits the User A copy and User C copy is implicitlly loade and different machine result are also available.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);

		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		OutputPanel panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);

		List<String> results = contentSelector.getAllResults();		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	

		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> source = contentSelector.getAllSeries();
		results = contentSelector.getAllResults();

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);
		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/16]", "verifying the findings size ");
		panel.openAndCloseOutputPanel(false);

		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/16]","verify the created by"));

		header.logout();
		loginPage.login(username_2,username_2);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);


		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[3/16]", "verfying the findings size when users draw its findings and user " +username_2+" loads the user "+username_1+" copy using content selector");

		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[4/16]","verify the created by"));

		panel.enableFiltersInOutputPanel(true, true, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()+1), "Checkpoint[5/16]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[6/16]","verify findings after selecting result 1"));

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb2.get(k),"Checkpoint[7/16]","verify findings after selecting result 2"));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[8/16]","verify the created by "+username_1));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_2+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[9/16]","verify the created by "+username_2));


		header.logout();
		loginPage.login(username_3,username_3);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);


		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_3+"_1");

		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[10/16]", "Verifying the total accepted findings list");

		panel.enableFiltersInOutputPanel(false, false, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[11/16]", "Verifying the total pending findings list");

		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[12/16]","verify findings after selecting result 1"));

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb2.get(k),"Checkpoint[13/16]","verify findings after selecting result 2"));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[14/16]","verify findings after selecting result created by "+username_1));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_2+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[15/16]","verify findings after selecting result created by "+username_2));

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_3+"_1");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[16/16]","verify findings after selecting result created by "+username_3));




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test08_US891_TC3755_verifyUserAccessAnotherUserCopyToBeDisplayedInOutputPanel() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be displayed in outpanel when User C creats the annotations and another user just opens user c copy");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName1, 1);

		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		OutputPanel panel = new OutputPanel(driver);

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_3, username_3);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName1, 1);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/4]", "Verifying the total findings list");
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[2/4]","verify the created by"));

		panel.openAndCloseOutputPanel(false);
		header.logout();
		loginPage.login(username_1,username_1);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName1, 1);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_3+"_1");

		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/4]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);
		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[4/4]","verify the created by"));


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test09_US891_TC3756_verifyUserEditSingleMachineResults() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of the GSPS findings to be displayed in output panel when only single machine result is present and User A edits.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName2, 1);

		panel = new OutputPanel(driver);
		contentSelector = new ContentSelector(driver);
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> results = contentSelector.getAllResults();

		ellipse = new EllipseAnnotation(driver);		

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		//contentSelector.selectPREntryForGivenSeries(1, results.get(0));
		contentSelector.selectResultFromSeriesTab(1, results.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username_1+"_1");

		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/3]", "Verifying the total findings list");

		panel.enableFiltersInOutputPanel( false, false, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), findingsListVb1.size(), "Checkpoint[2/3]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);

		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[3/3]","verify the created by"));


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test10_US891_TC3757_verifyOutputPanelForDifferentMachineResultsLoadedFromContentSelector() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be displayed in outpanel when there are different machine result loaded default or from Content selector.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper  = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);

		panel = new OutputPanel(driver);
		contentSelector = new ContentSelector(driver);
		List<String> results = contentSelector.getAllResults();		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));

		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		results = contentSelector.getAllResults();

		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		patientListPage.clickOnPatientRow(patientName3);

		patientListPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();

		contentSelector.selectResultFromSeriesTab(1, results.get(0));

		panel.enableFiltersInOutputPanel(false, false, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[1/4]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);


		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[2/4]","verify the created by"));


		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		panel.enableFiltersInOutputPanel(false, false, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[3/4]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);


		panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb2.get(k),"Checkpoint[4/4]","verify the created by"));


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test11_US891_TC3759_verifyOutputPanelWhenMachineResultsAndGSPSFindingsAreAbsent() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verfiy the list of annotations findings when there are No already present GSPS annoataions , neither Machine result present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		helper  = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName1, 1);

		
		OutputPanel panel = new OutputPanel(driver);
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		patientListPage.clickOnPatientRow(patientName1);

		patientListPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();

		panel.enableFiltersInOutputPanel(true, true, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), 0, "Checkpoint[1/1]", "Verifying the total findings list");

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test12_US891_TC3760_TC3761_verifyOutputPanelWhenMachinesResultsAreLoadedAndLatestVersionIsPresent() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings to be displayed when one machine result is implicitly loaded and has latest version of copy chain as well and another machine result and User C copy."
				+ "<br> 	Verify the list of findings to be displayed when one machine result user A copy is implicitly loaded and has latest version of copy in chain as well and another machine result and User C copy.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);

		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);
		List<String> results = contentSelector.getAllResults();		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));

		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	

		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);

		List<String> source = contentSelector.getAllSeries();
		results = contentSelector.getAllResults();

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/26]", "Verifying the total findings list");

		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/26]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[3/26]","verifying the findings name created");
		panel.openAndCloseOutputPanel(false);

		header.logout();
		loginPage.login(username_2,username_2);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);


		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[4/26]", "Verifying the total findings list");
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[5/26]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey("Ellipse_"+(panel.thumbnailList.size()-1)),"Checkpoint[6/26]","verifying the finding name is correctly displayed");

		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[7/26]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);

		header.logout();
		loginPage.login(username_3,username_3);		
		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);

		//		contentSelector.selectSeriesFromSeriesTab(1, results.get(0));
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[8/26]", "Verifying the total accepted findings list");
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[9/26]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[10/26]","verifying the finding name for accepted findings");


		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[11/26]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);

		results = contentSelector.getAllResults();		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));		
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[12/26]","verifying the pending findings"));

		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[13/26]", "Verifying the total findings list");

		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[14/26]", "Verifying the total findings list");

		contentSelector.selectResultFromSeriesTab(1, results.get(1));		
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,findingsListVb2.get(k),"Checkpoint[15/26]","verifying the findings after selecting the result 2"));

		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[16/26]", "Verifying the total accepted findings list");


		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[17/26]", "Verifying the total pending findings list");


		contentSelector.selectResultFromSeriesTab(1, results.get(2));		
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[18/26]","verify the created by"));

		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[19/26]", "Verifying the total findings list");


		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[20/26]", "Verifying the total findings list");


		contentSelector.selectResultFromSeriesTab(1, results.get(3));		
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[21/26]","verify the created by"));

		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[22/26]", "Verifying the total findings list");


		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[23/26]", "Verifying the total findings list");



		contentSelector.selectResultFromSeriesTab(1, results.get(4));		
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[24/26]","verify the created by"));

		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[25/26]", "Verifying the total findings list");


		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[26/26]", "Verifying the total findings list");



	}



	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test14_US891_TC3762_verifyOPWhenUserACopyIsLoadedAndUserCCopyIsPresent() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings annoatations to be displayed, when single Machine result chain User A copy is laoded in chain and User C copy is also present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);

		viewerPage = helper.loadViewerDirectly(patientName2, 1);

		contentSelector = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);

		List<String> source = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		ellipse = new EllipseAnnotation(driver);		

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username_1+"_1");

		panel.enableFiltersInOutputPanel(true, false, false);

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/7]", "Verifying the total findings list");

		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/9]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[2/7]","verify the created by");


		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), findingsListVb1.size(), "Checkpoint[3/7]", "Verifying the total findings list");
		panel.openAndCloseOutputPanel(false);

		header.logout();
		loginPage.login(username_3,username_3);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);


		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username_1+"_1");

		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[4/7]", "Verifying the total findings list");
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[5/7]","verify the created by"));

		}

		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()), "Checkpoint[6/7]", "Verifying the total findings list");
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[7/7]","verify the created by"));
		}



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test15_US891_TC3763_TC3764_verifyOPWhenUserACopyIsLoadedAndMachineResultPresent() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings annoatations to be displayed, when single Machine result chain User A copy is laoded in chain and Machine result(Loading) present"
				+ "<br> Verify the list of findings annoatations to be displayed, when single Machine result(loading) is loaded and User A copy is in chain and Machine result present");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);
		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		panel = new OutputPanel(driver);

		List<String> results = contentSelector.getAllResults();
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb2 = findingsListVb2.keySet().stream().collect(Collectors.toList());

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/9]", "Verifying the total findings list");
		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/9]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[3/9]","verifying the finding name");

		panel.enableFiltersInOutputPanel(false, false, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[4/9]", "Verifying the total findings list");

		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[5/9]","verify the created by"));

		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[6/9]","verifying the finding name");

		for(int i=0;i<findingsNameVb1.size();i++) {

			panel.clickOnJumpIcon(i+1);
			viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+viewerPage.getSeriesDescriptionOverlayText(1)+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[7.1/9]", "Verified warning message when thumbnail corresponding slice is not open in active view ");
			panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[7.2/9]","verify the created by"));
		}


		for(int i=findingsNameVb2.size(),j=0;i>1;i--,j++) {

			panel.clickOnJumpIcon(i);
			panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[8/9]","verify the created by"));
			viewerPage.assertEquals(panel.getSelectedFindingName(),findingsNameVb2.get(j), "Checkpoint[9/9]", "machine result1 is same not changed");
		}

		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive"})
	public void test16_US891_TC3765_verifyOPWhenMachineResultIsLoadedAndUserCCopyIsPresent() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings annoatations to be displayed, when single Machine result is imlpliclty loaded in chain copy User C annotations copy is also present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName2, 1);

		panel = new OutputPanel(driver);
		contentSelector = new ContentSelector(driver);
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		List<String> source = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		ellipse = new EllipseAnnotation(driver);		


		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_3, username_3);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));		

		panel.enableFiltersInOutputPanel(true, false, false);

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/6]", "Verifying the total findings list");

		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[2/6]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(findingsNameVb1.get(0)),"Checkpoint[3/6]","verifying the findings name is same after adding the annotation");

		for(int i=0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+viewerPage.getSeriesDescriptionOverlayText(1)+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[4/6]", "Verified info message when thumbnail corresponding slice is not open in active view ");
			viewerPage.closeNotification();
		}

		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()), "Checkpoint[5/6]", "Verifying the total findings list	");

		for(int i=0;i<findingsNameVb1.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.getGSPSFindingList(1).forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[6/6]","verifying the findings name is correct"));
		}

		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test17_US891_TC3766_verifyOPWhenUserCIsLoadedAndMachineResultsArePresent() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings annoatations to be displayed, When User C copy is implicitly loaded and Machine result are present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName2, 1);
		contentSelector = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);


		List<String> source = contentSelector.getAllSeries();

		ellipse = new EllipseAnnotation(driver);		

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_3, username_3);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_3+"_1");
		panel.enableFiltersInOutputPanel(true, false, false);

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/5]", "Verifying the total findings list");
		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[2/5]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[3/5]","verifying the findings name");


		panel.enableFiltersInOutputPanel(false, false,true);
		viewerPage.assertEquals(panel.thumbnailList.size(), findingsListVb1.size(), "Checkpoint[4/5]", "Verifying the total findings list");
		for(int i=0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+viewerPage.getSeriesDescriptionOverlayText(1)+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[5/5]", "Verified warning message when thumbnail corresponding slice is not open in active view ");
			viewerPage.closeNotification();
		}
		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test18_US891_TC3768_verifyOPWhenTwoVersionsAreLoadedOfSameCopy() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings to be displayed in output panel when user implicitly load the 2 version of same copy.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName2, 1);
		panel = new OutputPanel(driver);

		contentSelector = new ContentSelector(driver);
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		List<String> results = contentSelector.getAllResults();

		ellipse = new EllipseAnnotation(driver);		

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		viewerPage = helper.loadViewerDirectly(patientName2, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		contentSelector.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username_1+"_1");

		panel.enableFiltersInOutputPanel(true, false, false);

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/13]", "Verifying the total findings list");

		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(2);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/13]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[3/13]","verifying the correct finding name is displayed in finding menu");

		for(int i=0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[4/13]", "verifying that ellipse is getting highlighted");
			updatedFindings = panel.getGSPSFindingList(2);
			updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[5/13]","verify the created by"));
			panel.assertTrue(updatedFindings.containsKey(findingsNameVb1.get(i)),"Checkpoint[6/13]","verifying the findings");


		}


		panel.enableFiltersInOutputPanel(false, false, true);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		viewerPage.assertEquals(panel.thumbnailList.size(), findingsListVb1.size(), "Checkpoint[7/13]", "Verifying the total findings list");
		for(int i=0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(2, 1), "Checkpoint[8/13]", "verifying the polyline from second viewbox is highlighted meaning correct clone is selected");
			panel.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1), "Checkpoint[9/13]", "Verifying that original result is not getting highlighted");
			updatedFindings = panel.getGSPSFindingList(2);
			updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[10/13]","verify the created by"));
			panel.assertTrue(updatedFindings.containsKey(findingsNameVb1.get(i)),"Checkpoint[11/13]","verifying the findings name");


		}
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[12/13]","verifying the findings"));
		panel.assertEquals(updatedFindings, findingsListVb1,"Checkpoint[13/13]","verifying the findings names");

		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive","Sanity"})
	public void test19_US891_TC3769_verifyOPWhen2VerionsAreLoadedOutOf3() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings to be displayed in output panel when user has 3 version of copy and 2 are implicitly loaded.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName2, 1);;

		contentSelector = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		List<String> results = contentSelector.getAllResults();

		ellipse = new EllipseAnnotation(driver);		

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		header.logout();
		loginPage.login(username_2, username_2);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName2, 1);

		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		contentSelector.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username_1+"_1");
		panel.enableFiltersInOutputPanel(true, false, false);

		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/13]", "Verifying the total findings list");

		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(2);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[2/13]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[3/13]","verifythe created finding name");

		for(int i=0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[4/13]", "verifying the ellipse is getting highlighted");
			updatedFindings = panel.getGSPSFindingList(2);
			updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[5/13]","verify the created by"));
			panel.assertTrue(updatedFindings.containsKey(findingsNameVb1.get(i)),"Checkpoint[6/13]","verifying the other findings are also present");


		}


		panel.enableFiltersInOutputPanel(false, false, true);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		viewerPage.assertEquals(panel.thumbnailList.size(), findingsListVb1.size(), "Checkpoint[7/13]", "Verifying the total findings list");
		for(int i=0;i<panel.thumbnailList.size();i++) {

			panel.clickOnJumpIcon(i+1);
			panel.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(2, 1), "Checkpoint[8/13]", "verifying the correct findings is displayed from correct clone");
			panel.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1), "Checkpoint[9/13]", "verifying original finding is not highlighted meaning updated result is displayed in OP");
			updatedFindings = panel.getGSPSFindingList(2);
			updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[10/13]","verify the created by"));
			panel.assertTrue(updatedFindings.containsKey(findingsNameVb1.get(i)),"Checkpoint[11/13]","verifying the original results are also displayed");


		}
		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,findingsListVb1.get(k),"Checkpoint[12/13]","verifying the findings"));
		panel.assertEquals(updatedFindings, findingsListVb1,"Checkpoint[13/13]","verifying the original results are also part of OP with updated user");

		panel.openAndCloseOutputPanel(false);



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" ,"Sanity"})
	public void test20_US891_TC3770_verifyOPWhenVarietyOfCopiesArePresent() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findings to be displayed in output panel when user implicitly load the 2 version of same copy and different machine result and User C copy is also present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);		

		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);

		List<String> results = contentSelector.getAllResults();
		List<String> source = contentSelector.getAllSeries();

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb2 = findingsListVb2.keySet().stream().collect(Collectors.toList());

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		header.logout();
		loginPage.login(username_2,username_2);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);


		header.logout();
		loginPage.login(username_3,username_3);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);

		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[1/18]","verify the created by"));
		updatedFindings.forEach((k, v) -> panel.assertEquals(k,ViewerPageConstants.ELLIPSE_FINDING_NAME,"Checkpoint[2/18]","verifying the correct finding name is created"));


		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);

		contentSelector.selectResultFromSeriesTab(1,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_3+"_1");
		contentSelector.selectResultFromSeriesTab(2,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		panel.closingConflictMsg();

		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[3/18]", "Verifying the total findings list");

		panel.clickOnJumpIcon(1);
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/18]", "verifying the correct finding is highlighted");
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(2, 1), "Checkpoint[5/18]", "verifying other findings");

		updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_3,"Checkpoint[6/18]","verify the created by"));
		updatedFindings.forEach((k, v) -> panel.assertEquals(k,ViewerPageConstants.ELLIPSE_FINDING_NAME,"Checkpoint[7/18]","verifying the finding name"));

		panel.click(panel.getViewPort(2));
		panel.clickOnJumpIcon(2);
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[8/18]", "verifying the correct finding from correct clone is highlighted");
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[9/18]", "verifying other finding is not highlighted");

		updatedFindings = panel.getGSPSFindingList(2);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[10/18]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[11/18]","verifying the finding name");

		panel.enableFiltersInOutputPanel(false, false, true);

		circle = new CircleAnnotation(driver);

		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[12/18]", "Verifying the total findings list");

		for(int i=0;i<findingsListVb1.size();i++) {

			panel.clickOnJumpIcon(i+1);
			viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+viewerPage.getSeriesDescriptionOverlayText(1)+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[13/18]", "Verified warning message when thumbnail corresponding slice is not open in active view ");
			viewerPage.closeNotification();
			viewerPage.assertTrue(panel.isElementPresent(panel.getLineOfTextAnnotationsInOutPutPanel(1)), "Checkpoint[14/18]", "verifying the text annotation is displayed under pending finding");

		}

		for(int i=findingsNameVb2.size() ,j=0;i>1;i--,j++) {

			panel.clickOnJumpIcon(i);
			panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(2, i), "Checkpoint[15/18]", "verifying the pending finding");
			panel.assertEquals(panel.getSelectedFindingName(), findingsNameVb2.get(j), "Checkpoint[16/18]", "verifying the finding name");
			updatedFindings = panel.getGSPSFindingList(2);
			updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_1,"Checkpoint[17/18]","verify the created by"));
			panel.assertTrue(updatedFindings.containsKey(findingsNameVb2.get(j)),"Checkpoint[18/18]","verifying the original findings are also displayed");
		}		

		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US891", "Positive" })
	public void test21_US891_TC3771_TC3811_TC3812_verifyOPWhenUserBeditsAndUserCLoads() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the list of findigs to be diaplyed When User B edits the User A copy and User C copy is present and User B copy is implicitlly loade and different machine result are also available."
				+ "<br> Verify finding name displayed in finding menu and output panel is same."
				+ "<br> Verify 'From username' is added in the finding menu.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName3, 1);
		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);

		List<String> results = contentSelector.getAllResults();
		List<String> source = contentSelector.getAllSeries();

		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		contentSelector.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb2 = findingsListVb2.keySet().stream().collect(Collectors.toList());

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username_3, username_3, username_3);

		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);
		contentSelector.selectResultFromSeriesTab(1, results.get(1));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, -50, 40, -50);

		header.logout();
		loginPage.login(username_2,username_2);		

		patientListPage.waitForPatientPageToLoad();		
		patientListPage.clickOnPatientRow(patientName3);

		patientListPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -150, 40, -50);

		header.logout();
		loginPage.login(username_3,username_3);		

		patientListPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(patientName3, 1);

		contentSelector.selectSeriesFromSeriesTab(1, source.get(0));

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);

		contentSelector.selectResultFromSeriesTab(1,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_2+"_1");

		findingsNameVb1.add(ViewerPageConstants.ELLIPSE_FINDING_NAME);
		panel.enableFiltersInOutputPanel(true, false, false);

		//		1. Annotations drawn/edited by User B and Original machine result1 should get displayed in output panel.
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[1/12]", "Verifying the total findings list");


		HashMap<String, String> updatedFindings = panel.getGSPSFindingList(1);
		updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[2/12]","verify the created by"));
		panel.assertTrue(updatedFindings.containsKey(ViewerPageConstants.ELLIPSE_FINDING_NAME),"Checkpoint[3/12]","verifying the finding name");

		panel.clickOnJumpIcon(1);
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+viewerPage.getSeriesDescriptionOverlayText(1)+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[4/12]", "Verified warning message when thumbnail corresponding slice is not open in active view ");
		viewerPage.closeNotification();

		for(int i =panel.thumbnailList.size();i>1;i--) {

			panel.clickOnJumpIcon(i);
			panel.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[5/12]", "verifying the corrcte finding is highlighted");

		}

		panel.enableFiltersInOutputPanel(false, false, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[6/12]", "Verifying the total findings list");

		circle = new CircleAnnotation(driver);
		for(int i=0;i<findingsListVb1.size();i++) {

			panel.clickOnJumpIcon(i+1);
			viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+viewerPage.getSeriesDescriptionOverlayText(1)+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[7/12]", "Verified warning message when thumbnail corresponding slice is not open in active view ");
			viewerPage.closeNotification();
			viewerPage.assertTrue(panel.isElementPresent(panel.getLineOfTextAnnotationsInOutPutPanel(1)), "Checkpoint[8/12]", "verifying the text annotation is displayed ");

		}

		for(int i=panel.thumbnailList.size(),j=0,pending=findingsNameVb2.size()-1;i>1;i--,j++,pending--) {

			panel.clickOnJumpIcon(i);
			panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[9/12]", "verifying that original finding is also displayed with updated user");
			panel.assertEquals(panel.getSelectedFindingName(), findingsNameVb2.get(pending), "Checkpoint[10/12]", "verifying the selected finding name is correct");
			updatedFindings = panel.getGSPSFindingList(1);
			updatedFindings.forEach((k, v) -> panel.assertEquals(v,username_2,"Checkpoint[11/12]","verify the created by"));
			panel.assertTrue(updatedFindings.containsKey(findingsNameVb2.get(j)),"Checkpoint[12/12]","verifying the original findings are also displayed");
		}		

		panel.openAndCloseOutputPanel(false);


	}

	//	DE964: Unable to close output panel after getting 'Cannot read property 'toString' of undefined' error when previous result is rendered in viewbox

	@Test(groups = { "Chrome", "IE11", "Edge", "DE964", "Positive","DR2210"})
	public void test22_DE964_TC4334_DR2210_TC8840_verifyNoConsoleErrorOnImmediateAccessOfOP() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that there should not be any error in console when user open output panel immediately after drawing multiple linear measurement"
				+ "<br> Verify no webGL related console error is observed and thumbnail images are getting rendered properly");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");

		helper = new HelperClass(driver);
		viewerPage =  helper.loadViewerDirectly(patientName1, 1);
		
		panel = new OutputPanel(driver);
		
		
		
		lineWithUnit = new MeasurementWithUnit(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Draw 6-7 linear measurement and open output panel immediately 	");

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1, 50, 50, 150, 0);
		lineWithUnit.drawLineQuickVersion(1, 80, 80, 120, 0);
		lineWithUnit.drawLineQuickVersion(1,-4,-20,134,0);
		lineWithUnit.drawLineQuickVersion(1,-100,-100,0,100);
		lineWithUnit.drawLineQuickVersion(1,-150,-150,200,0);
		lineWithUnit.drawLineQuickVersion(1,-200,-200,0,180);

		int totalLines= lineWithUnit.getAllLinearMeasurements(1).size();

		panel.enableFiltersInOutputPanel(true, false, false);

		panel.assertFalse(panel.isConsoleErrorPresent("Cannot read property 'toString' of undefined"),"Checkpoint[1/6]","Verify that Output panel should get opened and should not be any console error.");

		panel.assertEquals(panel.thumbnailList.size(), totalLines, "Checkpoint[2/6]", "Verifying the total findings list");
		for(int i =0 ,j=totalLines;i<panel.thumbnailList.size();i++,j--) {
			panel.scrollIntoView(panel.thumbnailList.get(i));
			//			panel.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Configurations.TEST_PROPERTIES.get("nsUserName"), "Checkpoint[3/6]", "Verifying the findings created by user");
			//			panel.assertTrue(panel.getText(panel.findingsNameList.get(i)).contains("LinearMeasurement_"+j), "Checkpoint[4/6]", "value of linear measurement should not get display under \"unformattedText\" section in output panel thumbnail details.");
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[5/6]", "Verifying the thumbnail");
		}

		panel.openAndCloseOutputPanel(false);		
		panel.assertFalse(panel.isConsoleErrorPresent(webGLConsoleError),"Checkpoint[6/6]","Verify that Output panel should get opened and should not be any console error.");


	}

	//US804 Scrollbar component
	@Test(groups = { "Chrome", "IE11", "Edge", "US804", "Positive" })
	public void test23_US804_TC4448_VerifyScrollbarComponentInOutputPanel()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new UI of scrollbar component on Output panel");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);	
		PointAnnotation point=new PointAnnotation(driver);
		panel=new OutputPanel(driver);
		lineWithUnit=new MeasurementWithUnit(driver);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		lineWithUnit.drawLine(1, 50, 50, 150, 150);
		lineWithUnit.drawLine(1, 78, -20, 100, 100);
		lineWithUnit.drawLine(1, -120, -150, 0, 150);
		lineWithUnit.drawLine(1,-4,-20,134,0);
		lineWithUnit.drawLine(1,66,-50,0,180);
		lineWithUnit.drawLine(1,42,41,134,134);
		lineWithUnit.drawLine(1,-150,-20,200,0);
		lineWithUnit.drawLine(1, -20, -50, 120, 150);
		lineWithUnit.drawLine(1, -10, -30, 90, 80);
		lineWithUnit.drawLine(1, 25, -70, 50, 0);

		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);

		//Draw a Circle on View Box1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -100,-100);

		//Change state of Existing findings to Rejected
		panel.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		panel.selectRejectfromGSPSRadialMenu();
		panel.selectRejectfromGSPSRadialMenu();
		panel.selectRejectfromGSPSRadialMenu();

		//Open Output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify scroll bar component in output panel");
		panel.enableFiltersInOutputPanel(true, true, true);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.verticalScrollBarComponent.get(viewerPage.verticalScrollBarComponent.size()-1)), "Verify scroll component present in output panel", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify UI of regular scrollbar component in Output panel");
		viewerPage.assertTrue(viewerPage.verifyPropertyOfRegularVerticalScrollBar(viewerPage.verticalScrollBarComponent.get(viewerPage.verticalScrollBarComponent.size()-1), viewerPage.verticalScrollBarSlider.get(viewerPage.verticalScrollBarSlider.size()-1)),"Verify width and background color of regular vertical scrollbar","Verified");

		viewerPage.mouseHover(viewerPage.verticalScrollBarComponent.get(viewerPage.verticalScrollBarComponent.size()-1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in Output panel");
		viewerPage.assertTrue(viewerPage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerPage.verticalScrollBarComponent.get(viewerPage.verticalScrollBarComponent.size()-1), viewerPage.verticalScrollBarSlider.get(viewerPage.verticalScrollBarSlider.size()-1)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify tree structure in groups and findings underneath");
		panel.scrollIntoView(panel.thumbnailList.get(panel.thumbnailList.size()-1));
		panel.click(panel.thumbnailList.get(panel.thumbnailList.size()-1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 2), "Verify Findings is in Active pending state", "Verified");

	}

	@Test(groups ={"Chrome","Edge","IE11", "DR2494", "Positive"})
	public void test24_DR2494_TC9888_verifyOutputPanelContentInMaximizedState()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that output panel tab is not blank when user shuffles between the tabs in minimized and maximized state.");

		
		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(GSPS_PatientName, 1);
		
		contentSelector=new ContentSelector(driver);
		panel=new OutputPanel(driver);
	

		contentSelector.openAndCloseSeriesTab(true);
		panel.click(panel.opTabOpened);
		
		panel.assertTrue(panel.isElementPresent(panel.syncAllFindingsIcon), "Checkpoint[1/5]", "Verified that sync all finding is present inside the maximized output panel.");
	    
		panel.assertTrue(panel.isElementPresent(panel.filterFindingsIcon), "Checkpoint[2/5]", "Verified that filter finding is present inside the maximized output panel.");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify that no console error is displayed after clicking on the output panel tab inside right panel" );
		panel.assertFalse(panel.isConsoleErrorPresent(), "Verify no console error when user is inside maximized output panel tab", "Verified");
   
		panel.click(panel.seriesTabOpened);
		
		contentSelector.assertTrue(contentSelector.isElementPresent(contentSelector.seriesPanel), "Checkpoint[4/5]", "Verified that series panel contents are displayed");
		   
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify that no console error is displayed after clicking on the series tab inside right panel" );
		panel.assertFalse(panel.isConsoleErrorPresent(), "Verify no console error when user is inside maximized series tab", "Verified");
		   
	
	}


}