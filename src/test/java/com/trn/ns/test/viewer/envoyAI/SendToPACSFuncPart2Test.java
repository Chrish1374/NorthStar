package com.trn.ns.test.viewer.envoyAI;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
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
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSFuncPart2Test extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	
	private ViewerPage viewerPage;
	private RegisterUserPage register;
	private ViewerARToolbox viewerARToolbox;
	
	//patient detail with multiple series data
	String filePath2 = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String patientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
	String patientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeriesPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String filePath6 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneageStudyPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);

	String filePath7 = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);

	String filePath8 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath8);

	String filePath9 = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String aidocPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath9);
	
	String filePath10 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath10);

	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;

	String username_1 = "userA";
	String username_2 = "test123456789scan123456789";
	String username2 = "userB";
	String username3 = "userC";
	static String patient_id="";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;
	private PointAnnotation point;
	private MeasurementWithUnit lineWithUnit;
	private ViewerSendToPACS sd;
	private OutputPanel panel;
	private HelperClass helper;
	



	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	// US957 Update A/R states and send findings to PACS	


	
	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test07_US957_TC4495_verifySendTOPACSOnMultipleResultsData() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS when user edits data which has multiple machine results");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerPageUsingSearch(patientName3, 1,1);
		
		viewerPage.waitForViewerpageToLoad();
		sd=new ViewerSendToPACS(driver);

		panel = new OutputPanel(driver);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -70, 40, 50);
		circle.drawCircle(1, 30, 70, 40, 50);
		panel.rejectResult(1);

		panel.enableFiltersInOutputPanel(true,true,true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Verifying total number of findings in output panel");
		panel.assertEquals(panel.thumbnailList.size(),5,"Checkpoint[1/4]", "Verifying 5 findings are present: 2 from machine result-2, 1 from machine result-1 , 1 user drawn circle in accepted state, 1 user drawn circle in rejected state");
		panel.openAndCloseOutputPanel(false);
		
		sd.openSendToPACSMenu(true,true, true, true);

		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify notification UI after sending to pacs");
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS,  "5 findings are sent to PACS ( 1 as accepted, 1 as rejected, 3 as pending )"),"Checkpoint[2.1/4]","Verify notification UI after sending to pacs");	
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[2.2/4]","Verify Error notification UI after sending to pacs");	
		panel.waitForElementInVisibility(sd.notificationDiv);
		
	    panel.openAndCloseOutputPanel(false);
		cs = new ContentSelector(driver);
		List<String> latestResults = cs.getResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify clone is generated after sending to pacs");
		viewerPage.assertEquals(latestResults.get(0),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+2,"Checkpoint[3/4]","New clone should be created - 'GSPS_<user name>_2'");

		panel.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		sd = new ViewerSendToPACS(driver);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc server for sent findings");
		patient_id =sd.verifyOrthancFindings(patientName3, username, currentSelectResult, 3,acceptedFindings , rejectedFindings, pendingFindings);
	}

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test08_US957_TC4499_verifySendTOPACSOnMultipleResultsData() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Single Series - When User A creates multiple clone copy from one base series");
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9Patient,  1, 1);
		
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -70, 40, 50);
		sd=new ViewerSendToPACS(driver);
		panel = new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/11]", "Verify clone is generated after drawing annotation");
		viewerPage.assertEquals(cs.getAllResults().get(0),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+1,"Checkpoint[1/]","New clone should be created - 'GSPS_<user name>_1'");

		helper.browserBackAndReloadViewer(liver9Patient,  1, 1);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 30, 70, 40, 50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/11]", "Verify clone is generated after drawing annotation");
		viewerPage.assertEquals(cs.getAllResults().get(1),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+2,"Checkpoint[2/]","New clone should be created - 'GSPS_<user name>_2'");

		helper.browserBackAndReloadViewer(liver9Patient,  1, 1);

		panel.acceptResult(1);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/11]", "Verify clone is generated after drawing annotation");
		viewerPage.assertEquals(cs.getAllResults().get(2),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+3,"Checkpoint[3/]","New clone should be created - 'GSPS_<user name>_3'");

		panel.enableFiltersInOutputPanel(true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying total number of findings in output panel");
		panel.assertEquals(panel.thumbnailList.size(),3,"Checkpoint[4/11]", "Verifying 3 findings of clone-3 are present. One circle in pending state, one circle in rejected state and one ellipse in accepted state ");
		panel.openAndCloseOutputPanel(false);

		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify notification UI after sending to pacs");
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS,  "3 findings are sent to PACS ( 2 as accepted, 1 as pending )"),"Checkpoint[5.1/1]","Verify notification UI after sending to pacs");	
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[5.2/11]","Verify Error notification UI after sending to pacs");	
		panel.waitForElementInVisibility(sd.notificationDiv);
		
		panel.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		sd = new ViewerSendToPACS(driver);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/11]", "Verifying the orthanc server for sent findings");
		patient_id = sd.verifyOrthancFindings(liver9Patient, username, currentSelectResult, 2,acceptedFindings , rejectedFindings, pendingFindings);

		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+2);
		cs.openAndCloseSeriesTab(false);

		sd.openAndCloseOutputPanel(true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForElementVisibility(viewerPage.notificationDiv);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/11]", "Verify notification UI");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("2 findings are sent to PACS"),"Verify notification UI","Verified");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 100, 150);

		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForElementVisibility(viewerPage.notificationDiv);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/11]", "Verify notification UI");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("3 findings are sent to PACS"),"Verify notification UI","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[9/11]", "Verifying total number of findings in output panel");
		panel.assertEquals(panel.thumbnailList.size(),3,"Checkpoint[1/4]", "Verifying 3 findings of clone-4 are present. Two circles and one elipse in accepted state");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[10/11]", "Verify clone is generated after drawing annotation");
		viewerPage.assertEquals(cs.getAllResults().get(3),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+4,"Checkpoint[1/]","New clone should be created - 'GSPS_<user name>_4'");

		int numberOfClones = cs.getAllResults().size();

		panel.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[11/11]", "Verifying no clone is generated");
		viewerPage.assertEquals(cs.getAllResults().size(),numberOfClones,"Verify no new clone is created after changing the state of annotation","Verified");
	}	

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test09_US957_TC4505_verifySendTOPACSWithNOFindings() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Single Series - When User A creates multiple clone copy from one base series");
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(ah4Patient,  1, 1);

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, true, true);
		sd = new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Verifying total number of findings in output panel");
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/3]", "Verify output panel has no GSPS findings");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/3]", "Verify notification UI is not getting displayed as there is no finding to be sent to PACS");
		panel.openAndCloseOutputPanel(true);
		viewerPage.assertFalse(viewerPage.getClickabilityOfElementLocatedBy(sd.bySendToPacs),"Checkpoint[2/3]","Verify Send to PACS button is not clickable");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/3]", "Patient is not created in Orthanc");
		sd.verifyNoOfPatientsInOrthanc(0);
	}


	//	Install the latest development build for this feature.
	//	Create two username and pwd (A,B,C)
	//	Import any data which has 2 machine GSPS results and does not have any GSPS annotation already drawn for eg "DX_2results_Updated".
	//	Ensure the Orthanc server is up and running. Upload the Dicom files from the data set in Orthanc server.
	//	Login to NS application
	//	Make sure the 'Send to PACS' button options turned on - Accepted, Rejected, Pending


	@Test(groups = { "Chrome", "IE11", "Edge", "US957", "Positive"})
	public void test10_US957_TC4507_VerifyWhenBothMachineAndCloneCopyChainAvailable() throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS -  When original Machine result is loaded in the viewer but there is clone copy chain available as well (in a multi user edit scenario)");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName3 + "in viewer");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName3,  1, 1);
		
		cs = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);		
		panel = new OutputPanel(driver);
		TextAnnotation text = new TextAnnotation(driver);
		sd=new ViewerSendToPACS(driver);

		List<String> results = cs.getAllResults();		
		cs.selectResultFromSeriesTab(1, results.get(0));

		text.openGSPSRadialMenu(text.getLineOfTextAnnotations(1).get(0));	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving all the results and findings from result 1 and result 2 respectively");
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		results = cs.getAllResults();

		cs.selectResultFromSeriesTab(1, results.get(1));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb2 = findingsListVb2.keySet().stream().collect(Collectors.toList());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating users UserA, UserB and UserC");
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username2, username2, username2);
		register.createNewUser("UserC", "test", LoginPageConstants.SUPPORT_EMAIL,username3, username3, username3);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging using user A");
		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient which has 2 machine results "+patientName3);
		viewerPage = helper.loadViewerPageUsingSearch(patientName3,  1, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the second machine results");
		cs.selectResultFromSeriesTab(1, results.get(1));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Editing the second machine result by creating the ellipse");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "selecting the copy of annotation created by user A");
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging user using userB");
		loginPage.login(username2,username2);		

		viewerPage = helper.loadViewerPageUsingSearch(patientName3,  1, 1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the userA copy in viewer");
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username_1+"_1");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Editing the User A copy ");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);
		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging user using userC");
		loginPage.login(username3,username3);		
		
		helper.loadViewerPageUsingSearch(patientName3,  1, 1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading different series");
		cs.selectSeriesFromSeriesTab(1,cs.getSeriesDescriptionOverlayText(1));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "User C creates New fresh annotations on some different series.");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 200, -100, 40, -50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Select series containing  Original machine result2 from content selector-> Result tab  into viewbox-1");
		cs.selectResultFromSeriesTab(1, results.get(1));
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open output panel");
		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the ouput panel");
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/16]", "verifying the finding list");

	
		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[5/16]", "Verifying the machine results");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the count for WiaResult table and from content selector");
		int rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		results = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configuring the Send to Pacs and choosing the option Leave as is");		
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);

		viewerPage.assertFalse(message.isEmpty(), "Checkpoint[11/16]", "Verifying the message is displayed with findings sent to EnvoyAI");
		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[12/16]","Verify in content selector (under results tab) new unified user generated clone copy is created and associated with the correct series.");	
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows+1,"Checkpoint[13/16]","Verifying that wiaResult table also has new entry");

		circle = new CircleAnnotation(driver);
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username3+"_2"),"Checkpoint[14/16]", "UI should be updated with the new unified clone copy. ");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[15/16]", "Verifying the Orthanc for patient entry");		
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[16/16]", "Open each PR series in the Orthanc and verify all the GSPS objects are mapped correctly.");		
		patient_id =sd.verifyOrthancFindings(patientName3, username3, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username3+"_2", 2,acceptedFindings,rejectedFindings,pendingFindings);




	}

	//	Install the latest development build for this feature.
	//	Create two username (UserName =scan Pwd = Scan)
	//	Import any data which has multiple source series for ex. liver 9
	//	Ensure the Orthanc server is up and running. Upload the Dicom files from the data set in Orthanc server.
	//	Login to NS application
	//	Make sure the 'Send to PACS' button options turned on - Accepted, Rejected, Pending

	@Test(groups ={"Chrome","Edge","IE11","US957","DE1941","positive"})	
	public void test11_US957_TC4508_DE1941_TC7878_VerifyingUnifiedCopyForDifferentSeriesResult() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Data with multiple series and making sure unified clone copy is created attached with correct series");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9Patient,  1, 1);
		panel = new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+liver9Patient+" on viewer");
		cs = new ContentSelector(driver);
		sd = new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Create annotations on couple of series which is loaded in the viewer (For example if data is loaded in 2x2 created user defined annotations on data loaded in Viewport1 and Viewport2)");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 200, -90,80,90);
		circle.closingConflictMsg();
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(2, 200, -90,80,90);				
		circle.closingConflictMsg();

		
		String seriesDesc1 = cs.getSeriesDescriptionOverlayText(1);
		String seriesDesc2 = cs.getSeriesDescriptionOverlayText(2);

		viewerPage.assertEquals(cs.getAllResults().size(),2,"Checkpoint[1/15]","Two clones should be generated in content selector");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the findinds from both the viewbox");
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameForClone1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		ellipse.openGSPSRadialMenu(ellipse.getAllEllipses(2).get(0));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(2);
		List<String> findingsNameForClone2 = findingsListVb2.keySet().stream().collect(Collectors.toList());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open Output Panel and make a note of the number of findings and state  (In this case, all findings will be in 'Accepted' state)");
		
		panel.enableFiltersInOutputPanel(true,true,true);
//		List<String> findingsName = panel.getAllFindingsName();
//		panel.assertEquals(findingsName.size(), (findingsNameForClone1.size()+findingsNameForClone2.size()), "Checkpoint[2/15]", "Verifying the total findings in output panel");
//
//		for(int i=0,j=0;i<findingsName.size();i++) {
//
//			if(i<findingsNameForClone1.size()) {
//
//				viewerPage.assertTrue(findingsName.contains(findingsNameForClone1.get(i)), "Checkpoint[3/15]", "Output Panel should show the findings created in Checkpoint[1]");
//			}
//			else if(j<findingsNameForClone2.size()){
//				viewerPage.assertTrue(findingsName.contains(findingsNameForClone2.get(j)), "Checkpoint[4/15]", "Output Panel should show the findings created in Checkpoint[1]");
//				j++;
//			}
//		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the count from wiaresult and results from content selector");
		db = new  DatabaseMethods(driver);
		int rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		List<String> results = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);

		//		Click 'Send to PACS'
		//		Open Orthanc and Verify the PR series are created

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click 'Send to PACS' and Open Orthanc and Verify the PR series are created");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
		sd.waitForElementInVisibility(sd.notificationDiv);
		//Getting all the findings based on state
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		//		List<String> acceptedFindings = viewerPage.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		//		acceptedFindings.replaceAll(sd.new ReplaceVal());

		sd.openAndCloseOutputPanel(false);
		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+2),"Checkpoint[5/15]","Verifying the content selector for unified copy");	
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows+1,"Checkpoint[6/15]","Verify in DB new BatchMachine ID is created WiaResultElementID table.");
	
		findingsNameForClone1.replaceAll(sd.new ReplaceVal());
		findingsNameForClone2.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/15]", "Verify the Entry presence in Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/15]", "Open each PR series in the Orthanc and verify all the GSPS objects are mapped correctly.");
		patient_id =sd.verifyOrthancFindings(liver9Patient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3", 1,ListUtils.union(findingsNameForClone1, findingsNameForClone2),Collections.EMPTY_LIST,Collections.EMPTY_LIST);

		viewerPage.assertEquals(cs.getSeriesDescriptionOverlayText(1),seriesDesc1,"Checkpoint[9/15]","Verify in Content selector the unified clone copy is mapped correctly to Series.");	
		viewerPage.assertEquals(cs.getSeriesDescriptionOverlayText(2),seriesDesc2,"Checkpoint[10/15]","Verify in Content selector the unified clone copy is mapped correctly to Series.");
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3"),"Checkpoint[11/15]", "Verify in Content selector the unified clone copy is mapped correctly to Series.");

		helper.browserBackAndReloadViewer(liver9Patient,  1, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Go back and reload the study.");	
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[13/15]", "Verifying the annotations aer displayed correctly");
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(cs.getAllResults().get(2)),"Checkpoint[12/15]","Verify in Content selector the unified clone copy is mapped correctly to Series.");
		

		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3", 2);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[15/15]", "Verifying the annotations aer displayed correctly");
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(cs.getAllResults().get(3)),"Checkpoint[14/15]","Verify in Content selector the unified clone copy is mapped correctly to Series.");
	


	}


	//	Install the latest development build for this feature.
	//	Create two username (UserName =scan Pwd = Scan)
	//	Import any data which has 1 machine GSPS results and does not have any GSPS annotation already drawn for eg "Aidoc2".
	//	Ensure the Orthanc server is up and running. Upload the Dicom files from the data set in Orthanc server.
	//	Login to NS application
	//	Make sure the 'Send to PACS' button options turned on - Accepted, Rejected, Pending

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test12_US957_TC4510_verifyEditPostUnifiedCloneCopy() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - When 'Send to PACS' button is clicked Unified clone copy is loaded and edits are made (MultiSeries)");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(aidocPatient,  1, 1);
		sd = new ViewerSendToPACS(driver);
		panel = new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+aidocPatient+" on viewer");
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the series description and all the results entries");		
		String seriesDesc = cs.getSeriesDescriptionOverlayText(1);
		List<String> results = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Editing the result");
		circle = new CircleAnnotation(driver);
		circle.moveSelectedCircle(1, 20, 30);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a linear measurement");
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -100, -10, -100, -89);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the clone is created");
		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[1/23]","Verifying the clone is created");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Capturing all the findings present in clone 1");
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameForClone1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the source series and create new annotation");
		cs.selectSeriesFromSeriesTab(1, seriesDesc);		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 200, -70, 60, 60);

		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+2),"Checkpoint[2/23]","Verifying the clone is created");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Capturing all the findings present in clone 2");
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		HashMap<String, String> findingsListForClone2 = panel.getGSPSFindingList(1);
		List<String> findingsNameForClone2 = findingsListForClone2.keySet().stream().collect(Collectors.toList());

		circle.mouseHover(circle.getViewPort(1));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open the Output Panel and verify");
		
		panel.enableFiltersInOutputPanel(true,true,true);
//		List<WebElement> allFindings = panel.findingsList;
//		List<String> findingsName = panel.getAllFindingsName();
//		panel.openAndCloseOutputPanel(false);
//		panel.assertEquals(findingsName.size(), (findingsNameForClone1.size()+findingsNameForClone2.size()), "Checkpoint[3/23]", "Verifying the total findings available in output panel");
//
//		for(int i=0,j=0;i<findingsName.size();i++) {
//
//			if(i<findingsNameForClone1.size()) {
//
//				viewerPage.assertTrue(findingsName.contains(findingsNameForClone1.get(i)), "Checkpoint[4/23]", "Verifying the finding name in output panel from clone1");
//			}
//			else if(j<findingsNameForClone2.size()){
//				viewerPage.assertTrue(findingsName.contains(findingsNameForClone2.get(j)), "Checkpoint[5/23]", "Verifying the finding name in output panel from clone2");
//				j++;
//			}
//		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Capturing all the values from DB and CS");
		db = new  DatabaseMethods(driver);
		int rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		results = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured output panel and performing send to pacs");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);

		//Getting all the findings based on state
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[6/23]","Verifying unified entry in CS");	
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows+1,"Checkpoint[7/23]","Verifying the unified entry in DB");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/23]", "Verifying the entry presence in Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[9/23]", "Verifying the Object mapped in Orthanc");
		patient_id =sd.verifyOrthancFindings(aidocPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3", 2,acceptedFindings,rejectedFindings,pendingFindings);

		viewerPage.assertEquals(cs.getSeriesDescriptionOverlayText(1),seriesDesc,"Checkpoint[10/23]","Verifying the series name is correctly mapped");		
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3"),"Checkpoint[11/23]", "Verifying the unified entry is loaded and highlighted");

		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -150, 90);		
		circle.moveSelectedCircle(1, 40, 50);
		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[12/23]","Verifying the clone post editing the unified clone copy");

		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

//		panel.enableFiltersInOutputPanel(true,false,false);
//		allFindings = panel.findingsList;
//		panel.assertEquals(allFindings.size(), acceptedFindings.size(), "Checkpoint[13/23]", "Verifying the accepted findings count in output panel");
//		findingsName = panel.getAllFindingsName();
//		for(int i=0;i<panel.findingsList.size();i++) {
//
//			viewerPage.assertTrue(findingsName.contains(acceptedFindings.get(i)), "Checkpoint[14/23]", "Verifying the accepted findings name in output panel");
//		}
//		
//		panel.enableFiltersInOutputPanel(false,true,false);
//		allFindings = panel.findingsList;
//		panel.assertEquals(allFindings.size(), rejectedFindings.size(), "Checkpoint[15/23]", "Verifying the accepted findings count in output panel");
//		findingsName = panel.getAllFindingsName();
//		for(int i=0;i<panel.findingsList.size();i++) {
//
//			viewerPage.assertTrue(findingsName.contains(rejectedFindings.get(i)), "Checkpoint[16/23]", "Verifying the rejected findings name in output panel");
//		}
//		
//		panel.enableFiltersInOutputPanel(false,false,true);
//		allFindings = panel.findingsList;
//		panel.assertEquals(allFindings.size(), pendingFindings.size(), "Checkpoint[17/23]", "Verifying the pending findings count in output panel");
//		findingsName = panel.getAllFindingsName();
//		for(int i=0;i<panel.findingsList.size();i++) {
//
//			viewerPage.assertTrue(findingsName.contains(pendingFindings.get(i)),"Checkpoint[18/23]", "Verifying the pending findings name in output panel");
//		}

		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		results = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured output panel and performing send to pacs");
		sd.openSendToPACSMenu();
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		sd.openAndCloseOutputPanel(false);
	
		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()),"Checkpoint[19/23]","Verifying no unified copy is generated");	
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows,"Checkpoint[20/23]","verifying no new entry in DB");

		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3"),"Checkpoint[21/23]", "verifying the current clone is highlighted");
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[22/23]", "Verifying the entry presence in Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[23/23]", "Verifying the Object mapped correctly in Orthanc");
		patient_id =sd.verifyOrthancFindings(aidocPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3", 2,acceptedFindings,rejectedFindings,pendingFindings);




	}

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test13_US957_TC4511_verifyLatestFindingsAreAvailableInOutputPanel() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel displays findings from loaded clone copy when Unified clone copy is present");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9Patient,  1, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient in viewer "+liver9Patient);
		cs = new ContentSelector(driver);
		sd = new ViewerSendToPACS(driver);
		panel = new OutputPanel(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the series description and results");
		String seriesDesc = cs.getSeriesDescriptionOverlayText(1);
		List<String> results = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Create a new annotations on the source series (draw a linear measurement)");
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -100, -10, -100, -50);
		lineWithUnit.closingConflictMsg();
		cs.waitForTimePeriod(10000);

		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[1/14]","Annotation is created a no new clone copy is created.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the clone copy");
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Edit the  annotation/ add new annotation (edit the linear measurement and draw a circle) from clone copy created in Step #2");
		lineWithUnit.moveLinearMeasurement(1, 1, 10, 10);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 200, -70, 60, 60);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the measurement text");
		String measurement1 = lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the findings name from viewbox 1");
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		List<String> findingsNameVb1 = findingsListVb1.keySet().stream().collect(Collectors.toList());

		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[2/14]","Annotation is created a no new clone copy is created.");
		cs.openAndCloseSeriesTab(false);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify in Out-panel");
				
		panel.enableFiltersInOutputPanel(true,true,false);
		List<WebElement> allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), findingsNameVb1.size(), "Checkpoint[3/14]", "Verifying the total findings in output panel");

//		for(int i=0,j=findingsNameVb1.size()-1;i<panel.findingsList.size();i++,j--) {
//
//			viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(i)).contains(findingsNameVb1.get(j)), "Checkpoint[4/14]", "Output Panel should display the annotations (line and linear measurement).");
//		}

		panel.openAndCloseOutputPanel(false);

		results = cs.getAllResults();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Reload the source Series and create a new Annotations (this time Point)");
		cs.selectSeriesFromSeriesTab(1, seriesDesc);
		point = new PointAnnotation(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured output panel and performing send to pacs");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, 90);

		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[5/14]","Annotation is drawn and new clone copy is created");		

		//		point.openGSPSRadialMenu(point.getAllPoints(1).get(0));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Capturing the DB and results value before Send to pacs");
		db = new  DatabaseMethods(driver);
		int rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);
		results = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured and performing send to pacs");
		sd.openSendToPACSMenu(true,true, true, true);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		String msg = sd.getText(sd.notificationMessage.get(0));
		viewerPage.assertFalse(msg.isEmpty(),"Checkpoint[6/14]","UI Notification is displayed and new unified clone copy is created. UI is currently loaded with this clone copy. ");	

		//Getting all the findings based on state
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		//		point.openGSPSRadialMenu(point.getLinesOfPoint(1, 1).get(0));
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		viewerPage.assertEquals(cs.getAllResults().size(),(results.size()+1),"Checkpoint[7/14]","UI Notification is displayed and new unified clone copy is created. UI is currently loaded with this clone copy. ");	
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows+1,"Checkpoint[8/14]","Verify in WiaResultsElements table in DB new entry with BatchMachine ID is created.");
		
		cs.openAndCloseSeriesTab(false);
		panel.enableFiltersInOutputPanel(true,true,false);

		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 3, "Checkpoint[9/14]", "Verifying the total findings in output panel");
		panel.openAndCloseOutputPanel(false);

		viewerPage.click(viewerPage.getViewPort(2));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Edit the currently loaded clone copy and add a new annotation (this time Point)");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -150, 90);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, 30, 40);

		//		String measurement2 = lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		findingsNameVb1 = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "From content selector load the clone copy created from Step #3  (From Input #1)");
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		cs.openAndCloseSeriesTab(true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify the Output Panel");
		panel.enableFiltersInOutputPanel(true,true,false);
		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 4, "Checkpoint[10/14]", "Verifying the total findings in output panel");

//		for(int i=0,j=findingsNameVb1.size()-1;i<panel.thumbnailList.size();i++,j--) {
//			panel.scrollIntoView(panel.findingsNameList.get(i));
//			viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(i)).contains(findingsNameVb1.get(j)), "Checkpoint[11/14]", "Verifying the findings name in output panel");
//		}
		viewerPage.assertTrue(panel.getMeasurementTextFromThumnail(1).contains(measurement1), "Checkpoint[12/14]", "Verifying the measurement in output panel");

		panel.openAndCloseOutputPanel(false);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[13/14]", "Verifying the orthanc for number of entries sent");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[14/14]", "Verifying the object mapped to correct entries in Orthanc");
		patient_id =sd.verifyOrthancFindings(liver9Patient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3", 1,acceptedFindings,rejectedFindings,pendingFindings);


	}

	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import any data set. 
	//	Login in to the application

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test14_US957_TC4629_VerifyOutputPanelFunctionality() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all measurement tool works and edits shown in Output Panel");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9Patient,  1, 1);

		panel = new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient in viewer "+liver9Patient);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open the radial tool bar draw an annotation original dicom image.");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 200, -70, 60, 60);


		List<String> latestResults = cs.getAllResults();

		cs.openAndCloseSeriesTab(false);		
		panel.enableFiltersInOutputPanel(true,true,true);
		List<WebElement> allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 1, "Checkpoint[1/7]", "Verifying the entry in output panel");
		panel.openAndCloseOutputPanel(false);

		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -70, 60, 60);

		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[2/7]","Verifying the clone is generated");
		cs.openAndCloseSeriesTab(false);
	
		panel.enableFiltersInOutputPanel(true,true,true);
		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 2,  "Checkpoint[3/7]", "Verifying the entry in output panel");
		panel.openAndCloseOutputPanel(false);

		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 200, 40);

		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[4/7]","Verifying the clone is generated");
		cs.openAndCloseSeriesTab(false);
		panel.enableFiltersInOutputPanel(true,true,true);
		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 3,  "Checkpoint[5/7]", "Verifying the entry in output panel");
		panel.openAndCloseOutputPanel(false);

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -100, -10, -100, -89);
		panel.closingConflictMsg();

		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[6/7]","Verifying the clone is generated");
		cs.openAndCloseSeriesTab(false);
	
		panel.enableFiltersInOutputPanel(true,true,true);
		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 4,  "Checkpoint[7/7]", "Verifying the entry in output panel");
		panel.openAndCloseOutputPanel(false);

	}

	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import any data set. For this testing the following data is used where there are no Machine GSPS findings - ( BoneAge or PiccLine or Liver 9)
	//	Make sure the 'Send to PACS' button desired options turned on. For ex - User can have following combinations -
	//	Accepted, Rejected Pending
	//	Rejected and Pending
	//	Accepted and Rejected
	//	Accepted and Pending

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test15_US957_TC4630_verifySendToPacsSingleSeries() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Single Series");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9Patient,  1, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient in viewer "+liver9Patient);
		sd=new ViewerSendToPACS(driver);

		cs = new ContentSelector(driver);
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Draw an annotation.");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 200, -70, 60, 60);
		circle.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click Send to PACS");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForElementInVisibility(viewerPage.notificationDiv);
		sd.openAndCloseOutputPanel(false);

		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[1/4]","Verify in content selector No new clone copy is created.");
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Now add another annotation in the same image.");
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -70, 40, 70);
		ellipse.closingConflictMsg();

		ellipse.selectRejectfromGSPSRadialMenu();
		latestResults = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click Send to PACS");
		sd.openSendToPACSMenu(true,true, true, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));

		//		viewerPage.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForElementInVisibility(viewerPage.notificationDiv);
		sd.openAndCloseOutputPanel(false);
		
		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[2/4]","Verify in content selector No new clone copy is created.");
		panel.openGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));
		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		sd = new ViewerSendToPACS(driver);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify the Entry in Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verify the object are correctly mapped in Orthanc");
		patient_id =sd.verifyOrthancFindings(liver9Patient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", 2,acceptedFindings,rejectedFindings, pendingFindings);

	}

	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import any data set. For this testing the following data is used where there are no Machine GSPS findings - ( BoneAge)
	//	Make sure the 'Send to PACS' button desired options turned on. For ex - User can have following combinations -
	//	Accepted, Rejected Pending
	//	Rejected and Pending
	//	Accepted and Rejected
	//	Accepted and Pending

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test16_US957_TC4636_verifyUserFindingsOnUserLogout() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - User findings are created and browser application closed and relaunched in browser");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(boneageStudyPatient,  1, 4);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient in viewer "+boneageStudyPatient);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Draw an annotation.");

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(4);
		circle.drawCircle(4, 100, -70, 100, 100);
		circle.closingConflictMsg();
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(4);
		ellipse.drawEllipse(4, 100, 70, 50, 70);
		
		ellipse.selectRejectfromGSPSRadialMenu();

		ellipse.closingConflictMsg();
		cs = new ContentSelector(driver);
		sd = new ViewerSendToPACS(driver);
		panel=new OutputPanel(driver);
		List<String> latestResults = cs.getAllResults();

		panel.openGSPSRadialMenu(ellipse.getAllEllipses(4).get(0));

		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(4,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(4,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(4,ViewerPageConstants.PENDING_FINDING_COLOR);
		
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		Header header = new Header(driver);		
		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with scan");
		loginPage.login(username, password);

		patientPage.waitForPatientPageToLoad();
	    helper.loadViewerPageUsingSearch(boneageStudyPatient,  1, 1);		

		sd.openSendToPACSMenu(true,true, true, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.click(sd.sendToPacs);		
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "2 findings are sent to PACS ( 1 as accepted, 1 as rejected )"), "Checkpoint[1/2]", "Verified success notification UI.");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[1.1/2]","Verifeied error notification after send to pacs");
		viewerPage.waitForElementInVisibility(viewerPage.notificationDiv);
		panel.openAndCloseOutputPanel(false);
		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[2/2]","Verify in content selector No new clone copy is created.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the Orthanc Entry");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the Orthanc Entry mapped with correct object");
		patient_id =sd.verifyOrthancFindings(boneageStudyPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", 2,acceptedFindings,rejectedFindings, pendingFindings);


	}

	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import any data set. For this testing the following data can be used where there are no Machine GSPS findings - ( BoneAge, PiCCline or AH.4 )
	//	Make sure the 'Send to PACS' button desired options turned on. with Accepted, Rejected Pending

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test17_US957_TC4637_verifySingleSeriesCloneWhenSettingsAreChanged() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Single Series Clone Copy creation when User preference setting is changed");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9Patient,  1, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient in viewer "+liver9Patient);
		
		sd = new ViewerSendToPACS(driver);
        panel=new OutputPanel(driver);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Draw an annotation (circle)");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -70, 60, 60);
		circle.closingConflictMsg();

		List<String> latestResults = cs.getAllResults();
		cs.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click Send to PACS");
		sd.openSendToPACSMenu(true,true, true, true);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));

		//		viewerPage.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		sd.waitForElementInVisibility(sd.notificationDiv);
        sd.openAndCloseOutputPanel(false);
        
		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[1]","Verify in content selector No new clone copy is created.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Draw an annotation (line) and accept the finding");
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -70, 40, 70);
		circle.closingConflictMsg();

		latestResults = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Change the  'Send to PACS' config settings to send only 'Accepted' findings and Click Send to PACS");
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,true, false, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));

		//		viewerPage.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		sd.waitForElementInVisibility(sd.notificationDiv);
		sd.openAndCloseOutputPanel(false);

		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[2]","Verify in content selector No new clone copy is created.");

		panel.openGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));
		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
	
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3]", "Verifying the Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4]", "Verifying the objects in Orthanc");
		// 2 instances
		patient_id =sd.verifyOrthancFindings(liver9Patient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", 1,acceptedFindings,rejectedFindings, pendingFindings);

	}


	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import data set with pdf result. (Imbio)
	//	Make sure Orthanc is running.
	//	Make sure the 'Send to PACS' button desired options turned on. with Accepted, Rejected Pending	

	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test18_US957_TC4638_verifySendToPacsWhenPDFIsLoadedInOneByOne() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - When PDF is loaded in 1X1 and we have findings in the other series");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbioPatient);

		
		patientPage.clickOntheFirstStudy();
	    panel = new OutputPanel(driver);
	    sd = new ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient on viewer "+imbioPatient);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(3);

		cs = new ContentSelector(driver);
		//		List<String> results = cs.getAllResults(3);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Draw an annotation (circle) in the source or result series");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -150, 100, 100);
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 150, 150, 50, 10);
		ellipse.selectRejectfromGSPSRadialMenu();
		
		List<String> latestResults = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the output panel");
		
		panel.enableFiltersInOutputPanel(true,true,true);
		List<WebElement> allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 4, "Checkpoint[1/5]", "Verifying the total findings in output panel");
		panel.openAndCloseOutputPanel(false);

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.resultApplied(1));
		panel.openGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));
		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
//		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Change the layout to show just the pdf (Double click one up)");
		viewerPage.doubleClick(viewerPage.getViewPort(2));
		viewerPage.waitForPdfToRenderInViewbox(2);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click Send to PACS");
		sd.openSendToPACSMenu(true,true, true, true);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		viewerPage.waitForTimePeriod(10000);
		
		//viewerPage.mouseHover(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the output panel");
		panel.enableFiltersInOutputPanel(true,true,true);
		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 4, "Checkpoint[2/5]", "Verifying the total findings in output panel");
		panel.openAndCloseOutputPanel(false);

		//		if(viewerPage.isElementPresent(viewerPage.banner))
		//			viewerPage.click(viewerPage.bannerCloseIcon);
		viewerPage.doubleClick(viewerPage.getViewPort(2));
		viewerPage.waitForPdfToRenderInViewbox(2);
		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size(),"Checkpoint[3/5]","Verify in content selector No new clone copy is created.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "Verify PR series is created in Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"Checkpoint[5/5]", "PR Series is created in Orthanc.");
		patient_id =sd.verifyOrthancFindings(imbioPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", 2,acceptedFindings,rejectedFindings, pendingFindings);

	}



	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import any data set which has single series and Machine results which has only secondary capture. Data used -> eg "BoneAge", PICCLine
	//	Ensure the Orthanc server is up and running. Upload the Dicom files from the data set in Orthanc server.
	//	Login to NS application
	//	Make sure the 'Send to PACS' button following options turned on - Accepted, Rejected, Pending
	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test19_US957_TC4658_verifySendToPACSWhenSeriesIsInBackground() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Single Series - When User generated GSPS results are in the background (Cloning editable series that are in output Panel and have pending findings)");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName2);

		patientPage.clickOntheFirstStudy();
		panel = new OutputPanel(driver);
		sd = new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer.");
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the source series and draw annotation (Ex. draw a circle)");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, -150, -150, 100, 100);
		circle.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Reload the clone copy created in step #2 and draw the annotation (Ex. ellipse )");
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");

		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 150, 150, 50, 10);		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", " Accept/Reject the findings (Ex. Accept the circle and Reject the ellipse)");
		ellipse.selectRejectfromGSPSRadialMenu();
		ellipse.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		ellipse.mouseHover(ellipse.getViewPort(1));
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Output Panel");
		panel.enableFiltersInOutputPanel(true,true,false);
		List<WebElement> allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 2, "Checkpoint[1/7]", "Output Panel should display the findings from Step #3");
		panel.openAndCloseOutputPanel(false);

		panel.openGSPSRadialMenu(ellipse.getAllEllipses(2).get(0));

		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(2,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(2,ViewerPageConstants.PENDING_FINDING_COLOR);

		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "From the content selector load the Machine Result");
		cs.selectResultFromSeriesTab(2, results.get(0));

		db = new  DatabaseMethods(driver);
		int rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click 'Send to PACS'. Click 'Accept All' or 'Reject All'");
		sd.openSendToPACSMenu(true,true, true, true);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));

	    sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
	    String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);
		viewerPage.assertFalse(message.isEmpty(),"Checkpoint[2/7]","UI Notification is displayed and new unified clone copy is created. UI is currently loaded with this clone copy. ");	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click the Output Panel to verify the finding states are updated.");
	
		panel.enableFiltersInOutputPanel(true,true,false);
		allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 3, "Checkpoint[3/7]", "Click the Output Panel to verify the finding states are updated.");
		panel.openAndCloseOutputPanel(false);

		if(viewerPage.isElementPresent(viewerPage.notificationDiv))
			viewerPage.closeNotification();

		viewerPage.assertEquals(cs.getAllResults(),latestResults,"Checkpoint[4/7]","Verify in content selector no new clone is created.");
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows,"Checkpoint[5/7]","Verify in NSDB WiaResultElementID table no new entry with BatchMachineID should be created");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/7]", "Verify in Orthanc PR series are created");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Finding states should be updated based on the selection made in Step #9. ");
		patient_id =sd.verifyOrthancFindings(patientName2, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", 2,acceptedFindings,rejectedFindings, Collections.EMPTY_LIST);


	}


	//	Install the latest development build for this feature.
	//	Create a username and pwd (Username = Scan and Pwd= Scan).
	//	Import any data set which has single series and Machine results with no GSPS. Data used -> eg "BoneAge", PICCLine, Imbio
	//	Ensure the Orthanc server is up and running. Upload the Dicom files from the data set in Orthanc server.
	//	Login to NS application
	//	Make sure the 'Send to PACS' button following options turned on - Accepted, Rejected, Pending


	@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
	public void test20_US957_TC4670_verifySendToPACSWhenUserResultsInBackground() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS - Single Series - When User generated GSPS results are in the background (Cloning non -editable series that are in output Panel and have pending findings)");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName2,  1, 2);
		panel = new OutputPanel(driver);
		sd = new ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientName2);
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the source series and draw annotation (Ex. draw a circle)");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, -150, -150, 100, 100);
		circle.closingConflictMsg();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Reload the clone copy created in step #2 and draw the annotation (Ex. ellipse )");
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 150, 150, 50, 10);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accept/Reject the findings (Ex. Accept the circle and Reject the ellipse)");
		ellipse.selectRejectfromGSPSRadialMenu();
		circle.closingConflictMsg();
		List<String> latestResults = cs.getAllResults();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify Output Panel");
		
		panel.enableFiltersInOutputPanel(true,true,false);
		List<WebElement> allFindings = panel.thumbnailList;
		panel.assertEquals(allFindings.size(), 2, "Checkpoint[1/6]", "Verifying the findings in output panel");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Go back to study list and reload the study.");
		helper.browserBackAndReloadViewer(patientName2,  1, 1);

		panel.openGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));

		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
	
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "From the content selector load the Machine Result");
		cs.selectResultFromSeriesTab(1, results.get(0));

		db = new  DatabaseMethods(driver);
		int rows = db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click 'Send to PACS'.Click 'Accept All' or 'Reject All'");
		sd.openSendToPACSMenu(true,true, true, true);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		viewerPage.waitForElementVisibility(viewerPage.notificationDiv);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click the Output Panel to verify the finding states are updated.");
			
		panel.enableFiltersInOutputPanel(true,true,false);

		allFindings = panel.thumbnailList;

		panel.assertEquals(allFindings.size(), 3, "Checkpoint[2/6]", "inding states should be updated based on the selection made in Step #9. ");
		panel.openAndCloseOutputPanel(false);


		viewerPage.assertEquals(cs.getAllResults().size(),latestResults.size()+1,"Checkpoint[3/6]","Verify in content selector new clone is created.");
		viewerPage.assertEquals(db.getRowsCount(NSDBDatabaseConstants.WIARESULTTABLE),rows+1,"Checkpoint[4/6]","Verify in NSDB WiaResultElement table new entry for the clone is created.");	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/6]", "Verify in Orthanc PR series are created");
		sd.verifyNoOfPatientsInOrthanc(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/6]", "PR series should be created one for each state of the findings which are sent.");
		sd.verifyOrthancFindings(patientName2, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2", 2,acceptedFindings,rejectedFindings, pendingFindings);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", " Reload the latest clone copy in viewer.");
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2");

		viewerPage.assertEquals(panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).size(),rejectedFindings.size(),"Checkpoint[6.1/6]","Data is loaded and displays the correct states of the findings.");
		viewerPage.assertEquals(panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),acceptedFindings.size(),"Checkpoint[6.2/6]","Data is loaded and displays the correct states of the findings.");
		viewerPage.assertEquals(panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),pendingFindings.size(),"Checkpoint[6.3/6]","Data is loaded and displays the correct states of the findings.");

	}


	
	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test21_US996_TC4678_verifySendToPACSWhenTrackChangeDisableForUserA() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able to send results to PACS for new temporary results (temp clone copy) even when Track my change is off");

		helper = new HelperClass(driver);		
		viewerPage = helper.loadViewerPageUsingSearch(pointMultiSeriesPatient,  1, 1);		
		
		circle = new CircleAnnotation(driver);
		point=new PointAnnotation(driver);
		panel = new OutputPanel(driver);
		sd = new ViewerSendToPACS(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);

		int resultCount=cs.getAllResults().size();

		//disable user action track nd edit annotation and check on Content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pointMultiSeriesPatient+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);

		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[1/8]", "Verify new entry in content selector after editing annotation");

		//verify send to PACS 
		sd.openSendToPACSMenu(true,true, true, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);

		panel.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		//Getting all the findings based on state
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		sd = new ViewerSendToPACS(driver);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/8]", "Verifying patient entry on Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/8]", "Verifying the objects in Orthanc");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", 1,acceptedFindings,rejectedFindings,pendingFindings);

		//Reload viewer page and Verify temporary clone copy
		helper.browserBackAndReloadViewer(pointMultiSeriesPatient,  1, 1);
		
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[4/8]", "Verify previous entry not seen in Content selector after reloading viewer page");

		//Edit annotation afte reload of viewer page
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		point.waitForTimePeriod(1000);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[5/8]", "Verify new entry in content selector after editing annotation when viewer page is reload");

		sd.openSendToPACSMenu(true,true, false, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForTimePeriod(2000);

		//verify send to PACS after reload of viewer page for the same user
		panel.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		//Getting all the findings based on state
		List<String> acceptedFindings1 = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> rejectedFindings1 = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> pendingFindings1= panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		sd = new ViewerSendToPACS(driver);
		acceptedFindings1.replaceAll(sd.new ReplaceVal());
		rejectedFindings1.replaceAll(sd.new ReplaceVal());
		pendingFindings1.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/8]", "Verifying patient entry on the Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/8]", "Verifying the objects in Orthanc");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", 2 ,acceptedFindings1,rejectedFindings1,pendingFindings1);

		loginPage.logout();
		loginPage.login(username, password);
		patientPage=new PatientListPage(driver);
		viewerPage=helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[8/8]", "Verify previous entry not seen in Content selector after re-login");
	}

	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test22_US996_TC4724_verifySendToPACSForUserACloneCopyByUserB() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  other user B should not be able to send results to PACS for new temporary results (temp clone copy) created by user A");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage=helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);

		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);
		sd = new ViewerSendToPACS(driver);
		point=new PointAnnotation(driver);

		int resultCount=cs.getAllResults().size();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pointMultiSeriesPatient+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[1/5]", "Verify new entry in content selector after editing annotation");

		//Login with User B and check send to PACS for User A created copy
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register=new RegisterUserPage(driver);
		register.createNewUser(username2, "" ,username2 , LoginPageConstants.SUPPORT_EMAIL, username2, username2, username2);
		Header header = new Header(driver);
		header.logout();
		loginPage=new LoginPage(driver);
		loginPage.login(username2, username2);
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[2/5]", "Verify previous entry not seen in Content selector after login by User B");

		//Edit annotation by User B and verify send to PACS for User B 
		viewerPage.click(viewerPage.getViewPort(2));
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		point.waitForTimePeriod(1000);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username2+"_1"), "Checkpoint[3/5]", "Verify new entry in content selector after editing annotation by User B");

		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, false, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForTimePeriod(2000);

		panel.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		//Getting all the findings based on state
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> pendingFindings= panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "Verifying the patient entry on Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "Verifying the objects in Orthanc");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username2+"_1", 1 ,acceptedFindings,rejectedFindings,pendingFindings);


	}

	@Test(groups ={"Chrome","Edge","IE11","US996","positive"})
	public void test23_US996_TC4736_verifyCloneNotSavedButDisplayInOutputPanelWhenTrackChangesDisable() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Reg : E2E : Verify that new result copy should not get created & save , but display in O/P panel, and user should be able to Send them to PACS when Track my change is off from UI");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage=helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
		
		point=new PointAnnotation(driver);
		panel= new OutputPanel(driver);
		cs=new ContentSelector(driver);
		loginPage=new LoginPage(driver);
		sd = new ViewerSendToPACS(driver);

		int resultCount=cs.getAllResults().size();

		//disable user action tracking and change the state of Annotation to Reject
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pointMultiSeriesPatient+" in viewer" );
		sd.enableSendToPACSUserActionTracking(false);

		//verify temporary  copy in content selector
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[1/10]", "Verify clone copy in content selector after rejecting the machine drawn annotation");

		//Again edit annotation by changing the state as Accepted and verify on Content selector
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		point.waitForTimePeriod(1000);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[2/10]", "Verify clone copy in content selector after accepting the machine drawn annotation");

		//verify newly done changes visible in output panel after changing the state of annotation to Accepted
		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/10]", "Verify edited annotation seen under Accepted tab in Output panel");

		//send to newly edited copy to send to PACS and validate it
		sd.openSendToPACSMenu(true,true, false, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForTimePeriod(2000);

		panel.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		//Getting all the findings based on state
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> pendingFindings= panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/10]", "Verifying the patient entry on  Orthanc server");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/10]", "Verifying the objects in Orthanc for Accepted Finding");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", 1 ,acceptedFindings,rejectedFindings,pendingFindings);

		//login with user B and check previous copy created by user A not visible for User B on Content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user");
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register=new RegisterUserPage(driver);
		register.createNewUser(username2, "" ,username2 , LoginPageConstants.SUPPORT_EMAIL, username2, username2, username2);
		Header header = new Header(driver);
		header.logout();
		loginPage.login(username2, username2);
		helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[6/10]", "Verify previous entry created by User A not visible to User B");

		//logout from User B and again Re-login with User A
		header.logout();
		loginPage.login(username, username);
		helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);

		//edit annotation and click on send to PACS for User A
		sd.enableSendToPACSUserActionTracking(false);

		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(2).get(0));
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[7/10]", "Verify clone copy  in content selector after Re-login by User A");

		//Verify send to PACS for User copy created by user A for Rejected finding not by the user B
		sd.openSendToPACSMenu(true,false, true, false);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		viewerPage.waitForTimePeriod(2000);

		panel.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		//Getting all the findings based on state
		List<String> acceptedFindings1 = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> rejectedFindings1 = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> pendingFindings1= panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		acceptedFindings1.replaceAll(sd.new ReplaceVal());
		rejectedFindings1.replaceAll(sd.new ReplaceVal());
		pendingFindings1.replaceAll(sd.new ReplaceVal());

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/10]", "Verifying the patient entry on  Orthanc");
		sd.verifyNoOfPatientsInOrthanc(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[9/10]", "Verifying the objects in Orthanc");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", 2 ,acceptedFindings1,rejectedFindings1,pendingFindings1);

		//Again Logout from Application for User A and Check Re-login
		header.logout();
		loginPage.login(username, username);
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
		viewerPage.assertEquals(cs.getAllResults().size(),resultCount, "Checkpoint[10/10]", "Verify previous entry created by User A not visible to User A after Relogin");

	}

	@Test(groups ={"Chrome","Edge","IE11","DE1852","Positive"})	
	public void test24_DE1852_TC7437_verifySendToPACSWhenAcceptAllOptionSelectedForMachineFinding() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that state of GSPS findings getting changed on viewer, in findings menu, on vertical scroll bar and in Output panel when all findings SendToPacs as Accepted.");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		
        //enable all finding option from send to PACS
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/7]", "Send to pacs and choose accept all as option from Pending finding dialog");
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/7]", "Getting the total pending findings from output panel");
		panel.openAndCloseOutputPanel(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();

		//18 findings are sent to PACS (18 as accepted )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/7]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,acceptedCount+" findings are sent to PACS ( "+acceptedCount+" as accepted )","Checkpoint[4/7]","Verified message post sending the findings to PACS by selecting Accept all option");		
		
		panel.click(panel.getViewPort(2));
		cs = new ContentSelector(driver);
		
		panel.mouseHover(panel.getViewPort(1));
		int count=panel.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size()+panel.getStateSpecificFindings(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size();
		int markerSize=panel.getFindingsFromSliderContainer(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR)+panel.getFindingsFromSliderContainer(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		panel.assertEquals(count, acceptedCount, "Checkpoint[5/7]", "Verified count of accepted finding in Finding menu.");
		panel.assertEquals(markerSize, acceptedCount, "Checkpoint[6/7]", "Verified state of GSPS finding on scroll slider.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc for findings of SR");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", 1,Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST);

	   
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1852","Positive"})	
	public void test25_DE1852_TC7436_verifySendToPACSForAllTypesOfFinding() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that state of GSPS findings getting changed on viewer, in findings menu, on vertical scroll bar and in Output panel when Accept ,Reject and pending findings sent to SendToPacs.");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageUsingSearch(pointMultiSeriesPatient, 1,1);
	
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		point=new PointAnnotation(driver);
		
		panel.selectAcceptfromGSPSRadialMenu(point.getAllPoints(1).get(0));
		panel.selectAcceptfromGSPSRadialMenu(1);
		panel.selectRejectfromGSPSRadialMenu(1);
		panel.selectRejectfromGSPSRadialMenu(1);

		//enable all finding option from send to PACS
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/7]", "Send to pacs and choose accept all as option from Pending finding dialog");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		String message=sd.getText(sd.notificationMessage.get(0));
		sd.waitForElementInVisibility(sd.notificationDiv);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/7]", "Getting the total pending findings from output panel");
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
		
		//18 findings are sent to PACS (18 as accepted )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/7]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,(acceptedCount+rejectedCount+pendingCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )","Checkpoint[4/7]","Verified message post sending the findings to PACS by selecting Accept all option");		
		
		cs = new ContentSelector(driver);
		
		panel.mouseHover(panel.getViewPort(1));
		int acceptedFinding=panel.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size();
		int rejectedFinding=panel.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size();
		int pendingFinding=panel.getStateSpecificFindings(1, ViewerPageConstants.PENDING_FINDING_COLOR).size()+panel.getStateSpecificFindings(2, ViewerPageConstants.PENDING_FINDING_COLOR).size();
		int count=acceptedFinding+rejectedFinding+pendingFinding;
		
		int acceptedMarker=panel.getFindingsFromSliderContainer(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		int rejectedMarker=panel.getFindingsFromSliderContainer(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		int pendingMarker=panel.getFindingsFromSliderContainer(1,ViewerPageConstants.PENDING_FINDING_COLOR)+panel.getFindingsFromSliderContainer(2, ViewerPageConstants.PENDING_FINDING_COLOR);
		int markerSize=acceptedMarker+rejectedMarker+pendingMarker;
				
		panel.assertEquals(count, acceptedCount+rejectedCount+pendingCount, "Checkpoint[5/7]", "Verified count of accepted finding in Finding menu.");
		panel.assertEquals(markerSize, count, "Checkpoint[6/7]", "Verified state of GSPS finding on scroll slider.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc for findings of SR");
		sd.verifyOrthancFindings(pointMultiSeriesPatient, username,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", 3,Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST);

	   
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US2354","Positive","E2E","F1094"})	
	public void test26_US2354_TC9766_verifyErrorNotificationAfterSendToPacs() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that notification 'Error in sending results to Envoy AI' changed to Error in sending results to Eureka AI");

		helper=new HelperClass(driver);
		helper.loadViewerPageUsingSearch(ah4Patient, 1,1);
		viewerARToolbox = new ViewerARToolbox(driver);
		sd = new ViewerSendToPACS(driver);

		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		circle.doubleClickOnViewbox(1);
		circle.selectCircleFromQuickToolbar(1);

		circle.drawCircle(1, -30, -70, 40, 50);
		circle.drawCircle(1, 10, 70, 40, 100);
		viewerARToolbox.selectAcceptfromGSPSRadialMenu();

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		viewerARToolbox.selectRejectfromGSPSRadialMenu();

		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);

		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);

		sd.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[1/1]","Verify error notification 'Error in sending results to Eureka AI' should appear");	

	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException{
		DatabaseMethods db = new DatabaseMethods(driver);

		db.deleteDrawnAnnotation(username_1);
		db.deleteDrawnAnnotation(username_2);
		db.deleteDrawnAnnotation(username2);
		db.deleteDrawnAnnotation(username3);

		db.deleteUser(username_1);
		db.deleteUser(username_2);
		db.deleteUser(username2);
		db.deleteUser(username3);

		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);


		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		if(RESTUtil.getJsonPath(response).getList("").size()>0) {
			patient_id = RESTUtil.getJsonPath(response).getList("").get(0).toString();
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
		}




	}



} 
























