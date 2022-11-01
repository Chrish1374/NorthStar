package com.trn.ns.test.viewer.envoyAI;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSFuncTest extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	
	private ViewerPage viewerPage;
	private RegisterUserPage register;
	private OutputPanel panel;


	//patient detail with multiple series data

	String filePath2 = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String patientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
	String patientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath7 = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);

	String filePath8 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath8);

	String filePath10 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath10);


	private CircleAnnotation circle;

	String username_1 = "userA";
	String username_2 = "test123456789scan123456789";
	String username2 = "userB";
	String username3 = "userC";
	static String patient_id="";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;
	private HelperClass gsps;
	private ViewerSendToPACS sd;
	private static int seriesLevelID;
	private static String textAnnText="look here";


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

		DatabaseMethods db = new DatabaseMethods(driver);
		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);
	}

	@Test(groups ={"Chrome","Edge","IE11","US836","US1062","US1076","US1159","positive"})	
	public void test01_US836_TC3659_TC3673_TC3675_US1062_TC5075_US1076_TC5464_US1159_TC5793_verifySendToPACSForNewFindingsandForModifiedFinding() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send configured type of findings to PACS- New GSPS Result"
				+ "<BR> Send configured type of findings to PACS- Modify GSPS results with different use"
				+ "<BR> Verification of 64 truncate value in PR series description"
				+ "<BR> Verify existing Send To PACs functionality for GSPS");

		patientPage = new PatientListPage(driver);
		//Creating Users
		usersCreation();		

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
		patientPage.waitForPatientPageToLoad();
		sd = new ViewerSendToPACS(driver);
		panel=new OutputPanel(driver);

		// Creating annotations per users
		gsps = new HelperClass(driver);
		gsps.annotationCreationAndVerification(username, password, patientName3, 1, 0, 0, 0, 2, "Checkpoint[1/7]");			
		gsps.annotationCreationAndVerification(username_1, username_1, patientName3, 1, 0, 2, 0, 0, "Checkpoint[2/7]");
		gsps.annotationCreationAndVerification(username_2, username_2, patientName3, 1, 0, 0, 2, 0, "Checkpoint[3/7]");

		//Rejecting the results
		viewerPage = new ViewerPage(driver);
		panel.selectNextfromGSPSRadialMenu();

		for(int i =0; i<4 ;i++)
			panel.rejectResult(1);


		//Getting all the findings based on state
		PointAnnotation point = new PointAnnotation(driver);
		panel.openGSPSRadialMenu(point.getAllPoints(1).get(0));
		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		//Getting all the findings counts based on state
		int acceptedCount = acceptedFindings.size();
		int rejectedCount = rejectedFindings.size();
		int pendingCount = pendingFindings.size();
				
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());
			
		String message=(acceptedCount+rejectedCount+pendingCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )";
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the PACS to send all types of findings");
		//Configuring the send to PACS to true for all the options
		sd.openSendToPACSMenu(true,true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the leave as is option after sending it to PACS");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
	
		//9 findings are sent to PACS ( 2 as accepted, 4 as rejected, 3 as pending )
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the result from content selector");
		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getSelectedResults().get(0);
		TextAnnotation textAnn = new TextAnnotation(driver);
		textAnnText = textAnn.getTextFromTextAnnotation(1, 1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/7]", "Verifying the orthanc server for findings");
		patient_id=sd.verifyOrthancFindings(patientName3, username_2, currentSelectResult, 3,acceptedFindings , rejectedFindings, pendingFindings,textAnnText);

		//		Login with 'scan' user and verify all visible annotations
		//		Modified two 'Rejected' to 'Accepted' annotation, don't click on send to PACS.

		Header header = new Header(driver);		
		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with scan");
		loginPage.login(username, password);

		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientName3);
		
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Updating the rejected to accepted results");
		List<String> findings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);		
		for(int i=0;i<findings.size();i++) {
			panel.selectFindingFromTable(findings.get(i));
			panel.acceptResult(1);
			findings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		}
		header.logout();

		//		Login with 'test' user and verify all visible annotations
		//		Modified one 'Accepted' to 'Rejected' annotation,  In configsettings UI enabled Rejected and disabled accepted , pending  and click on send to PACS.
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with "+username_1);
		loginPage.login(username_1, username_1);

		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientName3);
		
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Updating the accepted to rejected results");
		findings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);		
		for(int i=0;i<1;i++) {
			panel.selectFindingFromTable(findings.get(i));
			panel.rejectResult(1);
		}

		circle = new CircleAnnotation(driver);
		panel.openGSPSRadialMenu(circle.getAllCircles(1).get(0));

		pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		pendingFindings.replaceAll(sd.new ReplaceVal());
		
		message=pendingFindings.size()+" findings are sent to PACS ( "+pendingFindings.size()+" as pending )";

		RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configuring the PACS to send only pending result");
		sd.openSendToPACSMenu(true,false, false, true);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the findings to PACS and choosing the leave as is");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		//9 findings are sent to PACS ( 2 as accepted, 4 as rejected, 3 as pending )
		cs = new ContentSelector(driver);
		currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc server for pending findings");
		sd.verifyOrthancFindings(patientName3, username_1, currentSelectResult, 1,Collections.EMPTY_LIST, Collections.EMPTY_LIST, pendingFindings,textAnnText);

	}

	@Test(groups ={"Chrome","Edge","IE11","US836","US1076","positive","BVT"})	
	public void test02_US836_TC3671_US1076_TC5464_verifySendToPACSForEditedFindings() throws InterruptedException{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send configured type of findings to PACS- Modify GSPS results");

		// creating annotations of all 3 state 
		patientPage = new PatientListPage(driver);
		gsps = new HelperClass(driver);
		sd = new ViewerSendToPACS(driver);
		gsps.annotationCreationAndVerification(username, password, patientName3, 1, 0, 0, 0, 2,"Checkpoint[1/3]");

		// Having annotation of all state
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Having annotations of all types");
		viewerPage = new ViewerPage(driver);
		panel=new OutputPanel(driver);
		panel.rejectResult(1);
		panel.acceptResult(1);
		
		circle = new CircleAnnotation(driver);
		panel.openGSPSRadialMenu(circle.getAllCircles(1).get(0));

		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);;
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		int acceptedCount = acceptedFindings.size();
		int rejectedCount = rejectedFindings.size();
		int pendingCount = pendingFindings.size();
		
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		String message=(acceptedCount+rejectedCount+pendingCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )";
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the PACS to send all types of findings");
		sd.openSendToPACSMenu(true,true, true, true);
		
		//9 findings are sent to PACS ( 2 as accepted, 4 as rejected, 3 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/2]", "Sending to PACS and choosing leave as is option");
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
	    panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/2]", "Verifying the orthanc for findings");
		patient_id =sd.verifyOrthancFindings(patientName3, username, currentSelectResult, 3,acceptedFindings , rejectedFindings, pendingFindings,textAnnText);
	}

	@Test(groups ={"Chrome","Edge","IE11","US836","US1076","positive"})	
	public void test03_US836_TC3672_US1076_TC5464_verifySendToPACSForAcceptedAndPendingEditedFindings() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send configured type of findings to PACS- Modify GSPS results and UI config settings");

		patientPage = new PatientListPage(driver);
		usersCreation();		

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
		patientPage.waitForPatientPageToLoad();
		gsps = new HelperClass(driver);
		viewerPage = new ViewerPage(driver);
		sd = new ViewerSendToPACS(driver);
		panel=new OutputPanel(driver);

		gsps.annotationCreationAndVerification(username, password, patientName3, 1, 0, 0, 0,2,"Checkpoint[1]");
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting the results");
		panel.rejectResult(1);
		gsps.annotationCreationAndVerification(username_1, username_1, patientName3, 1, 0, 2, 0,0,"Checkpoint[2]");
		gsps.annotationCreationAndVerification(username_2, username_2, patientName3, 1, 0, 0, 2,0,"Checkpoint[3]");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting the results");

		panel.selectNextfromGSPSRadialMenu();

		for(int i =0; i<4 ;i++)
			panel.rejectResult(1);
		
		PointAnnotation point = new PointAnnotation(driver);
		panel.openGSPSRadialMenu(point.getAllPoints(1).get(0));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the total accepted and pending findings");
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		int acceptedCount = acceptedFindings.size();
		int pendingCount = pendingFindings.size();
		
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		String message=(acceptedCount+pendingCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+pendingCount+" as pending )";
		
		//9 findings are sent to PACS ( 2 as accepted, 4 as rejected, 3 as pending )	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the PACS to send the accepted and pending results only");
		sd.openSendToPACSMenu(true,true, false, true);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the orthanc server for sent findings");
		patient_id =sd.verifyOrthancFindings(patientName3, username_2, currentSelectResult, 2,acceptedFindings , Collections.EMPTY_LIST, pendingFindings,textAnnText);


	}

	@Test(groups ={"Chrome","Edge","IE11","US836","US1076","positive","BVT"})	
	public void test04_US836_TC3839_US1076_TC5464_verifySendToPACSForNonMachineData() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send configured type of findings to PACS- DE947 verification");

		patientPage = new PatientListPage(driver);

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
		patientPage.waitForPatientPageToLoad();		
		gsps = new HelperClass(driver);
		sd = new ViewerSendToPACS(driver);
		panel=new OutputPanel(driver);
		gsps.annotationCreationAndVerification(username, password, liver9Patient, 1, 1, 1, 0, 1, "Checkpoint[1/3]");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting and making the findings to pending for non machine data");
		viewerPage = new ViewerPage(driver);
		panel.rejectResult(1);		
		panel.acceptResult(1);

		PointAnnotation point = new PointAnnotation(driver);
		panel.openGSPSRadialMenu(point.getAllPoints(1).get(0));


		List<String> rejectedFindings = panel.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);;
		List<String> acceptedFindings = panel.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		int acceptedCount = acceptedFindings.size();
		int rejectedCount = rejectedFindings.size();
		int pendingCount = pendingFindings.size();
		sd = new ViewerSendToPACS(driver);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());

		String message=(acceptedCount+rejectedCount+pendingCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )";
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured the pacs to send all types of findings");
		sd.openSendToPACSMenu(true,true, true, true);
		
		//9 findings are sent to PACS ( 2 as accepted, 4 as rejected, 3 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Send to PACS and choosing leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message),"Checkpoint[2.1/3]", "Verify send to pacs notification.");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[2.2/3]","Verify error notification after send to PACS.");

		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/3]", "Verifying the orthanc server for sent findings");
		patient_id =sd.verifyOrthancFindings(liver9Patient, username, currentSelectResult, 3,acceptedFindings , rejectedFindings, pendingFindings);


	}

	@Test(groups ={"Chrome","Edge","IE11","US836","US1076","positive"})	
	public void test05_US836_TC3832_US1076_TC5464_verifySendToPACSForPICCLINEData() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Send configured type of findings to PACS- New GSPS Result-Verification with one GSPS from one user");

		patientPage = new PatientListPage(driver);
		gsps = new HelperClass(driver);
		gsps.annotationCreationAndVerification(username, password, patientName2, 2, 1, 1, 0, 1, "Checkpoint[1/3]");

		viewerPage = new ViewerPage(driver);
		sd = new ViewerSendToPACS(driver);
		panel=new OutputPanel(driver);
		panel.rejectResult(2);		
		panel.acceptResult(2);

		PointAnnotation point = new PointAnnotation(driver);
		panel.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting all the findings");	
		List<String> rejectedFindings = panel.getFindingsName(2,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = panel.getFindingsName(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = panel.getFindingsName(2,ViewerPageConstants.PENDING_FINDING_COLOR);

		int acceptedCount = acceptedFindings.size();
		int rejectedCount = rejectedFindings.size();
		int pendingCount = pendingFindings.size();
	
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());
		String message=(acceptedCount+rejectedCount+pendingCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )";
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured to send all types for PICCLINE");
		sd.openSendToPACSMenu(true,true, true, true);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Send to PACS and choose leave as is option");
		//9 findings are sent to PACS ( 2 as accepted, 4 as rejected, 3 as pending )
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, message),"Checkpoint[2.1/3]", "Verify send to pacs notification.");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[2.2/3]","Verify error notification after send to PACS.");

		// adding the pending count as there is non -dicom present for first viewbox
			
		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getSelectedResults().get(0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/3]", "Verifying the orthanc server for sent findings");
		patient_id =sd.verifyOrthancFindings(patientName2, username, currentSelectResult, 3,acceptedFindings , rejectedFindings, pendingFindings);



	}	

	@Test(groups ={"Chrome","Edge","IE11","DE1451","Negative"})	
	public void test06_DE1451_TC7347_verifySendToPACSForTwoPatients() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify \"Send to PACS\" for multiple studies within a short time duration should not cause Northstar to split the output series into multiple segments");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbioPatient);		
		String imbioStudy = patientPage.getStudyDescription(0);
		
		patientPage.clickOntheFirstStudy();
		sd=new ViewerSendToPACS(driver);
		panel=new OutputPanel(driver);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting all the results");
		panel.acceptResult(1);
		panel.acceptResult(2);

		String seriesDescription = viewerPage.getSeriesDescriptionOverlayText(1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured to send all types of data");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		viewerPage.click(viewerPage.getViewPort(1));
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the results to PACS");
		viewerPage.click(sd.sendToPacs);
		viewerPage.waitForElementVisibility(viewerPage.notificationUI);
		
		viewerPage.waitForTimePeriod(10000);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Opening the parallel tab and loading the SR data in another viewer");
		viewerPage.openNewWindow();
		viewerPage.switchToNewWindow(2);
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();

		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();

		patientPage.clickOnPatientRow(ChestCT1p25mm);		
		String chestStudy = patientPage.getStudyDescription(0);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad(2);
		viewerPage.click(viewerPage.getViewPort(2));
		viewerPage.click(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting the result");
		viewerPage.mouseHover(panel.getGSPSHoverContainer(1));
		panel.acceptResult(1);

		viewerPage.closeNotification();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configured to send all types of data");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		viewerPage.click(viewerPage.getViewPort(1));
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		viewerPage.waitForElementVisibility(viewerPage.notificationDiv);

		viewerPage.closeNotification();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the clones from content selector");
		cs = new ContentSelector(driver);
		List<String> cloneSeries = cs.getSelectedResults();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Waiting for data to come");
		viewerPage.waitForTimePeriod(360000);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the orthanc server for sent findings");
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		// verifying there are two patients
		List<String> patients = RESTUtil.getJsonPath(response).getList("");
		viewerPage.assertEquals(patients.size(), 2, "Checkpoint[1/17]", "Verifying there are two patients sent");
		
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patients.get(0));		
		viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG),imbioPatient,"Checkpoint[2/17]","Verifying the patient name is"+imbioPatient);
			
		// verifying the study for patient 1
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patients.get(0));				
		String studiesForPatient = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.STUDY_ORTHANC_TAG);		
		List<String> studiesListForPatient = Arrays.asList(studiesForPatient.replace("[", "").replace("]", "").split(","));
		
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.STUDY_ORTHANC_URL,studiesListForPatient.get(0));		
		viewerPage.assertEquals(studiesListForPatient.size(),1,"Checkpoint[3/17]","verifying the total number of study");
		viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_STUDY_DESCRIPTION_TAG), imbioStudy, "Checkpoint[4/17]", "Verifying the study description is same displayed on study page");
	
		// verifying the series		
		String series = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.SERIES_ORTHANC_TAG);		
		List<String> seriesList = Arrays.asList(series.replace("[", "").replace("]", "").split(","));
		
		viewerPage.assertEquals(seriesList.size(),2,"Checkpoint[5/17]","verifying the series");
		
		seriesDescription = seriesDescription+"_Accepted";		
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesList.get(0));
		viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), seriesDescription, "Checkpoint[6/17]", "verifying the series description as on viewer");
		
		String instances = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.INSTANCES_ORTHANC_TAG);
		List<String> instancesList = Arrays.asList(instances.replace("[", "").replace("]", "").split(","));
		
		viewerPage.assertTrue(instancesList.size()>40,"Checkpoint[7/17]","verifying the instances are 227");
		
		for(int i=0 ;i<instancesList.size();i++) {
			
			
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesList.get(0)+OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_TAG_URL);		
			viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_PATIENT_NAME_TAG),imbioPatient,"Checkpoint[8.1/17]","verifying the patient name is same as on viewer");
			viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_STUDY_DESC_TAG),imbioStudy,"Checkpoint[8.2/17]","verifying the study description is same as on study page");
			viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_SERIES_DESC_TAG),seriesDescription,"Checkpoint[8.3/17]","verifying the series description is same as on viewer");
			
			
			
		}
		
	
		
		// Verifying the second patient 2 
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patients.get(1));				
		viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG),ChestCT1p25mm,"Checkpoint[9/17]","Verifying the second patient name");

		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patients.get(1));				
		studiesForPatient = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.STUDY_ORTHANC_TAG);		
		studiesListForPatient = Arrays.asList(studiesForPatient.replace("[", "").replace("]", "").split(","));
		
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.STUDY_ORTHANC_URL,studiesListForPatient.get(0));		
		viewerPage.assertEquals(studiesListForPatient.size(),1,"Checkpoint[10/17]","verifying the study");
		viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_STUDY_DESCRIPTION_TAG), chestStudy, "Checkpoint[11/17]", "Verifying the sudy description");
	
		// verifying the series		
		series = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.SERIES_ORTHANC_TAG);		
		seriesList = Arrays.asList(series.replace("[", "").replace("]", "").split(","));		
		viewerPage.assertEquals(seriesList.size(),2,"Checkpoint[12/17]","verifying the study");
		
		for(int j =0,k=seriesList.size()-1 ;j<seriesList.size();j++,k--) {
			
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesList.get(j));
			
			viewerPage.assertTrue(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG).contains(cloneSeries.get(k)), "Checkpoint[13/17]", "Verifying the series description");
			
			 instances = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.INSTANCES_ORTHANC_TAG);
			 instancesList = Arrays.asList(instances.replace("[", "").replace("]", "").split(","));
			
			viewerPage.assertEquals(instancesList.size(),1,"Checkpoint[14/17]","verifying the instances");
			
			for(int i=0 ;i<instancesList.size();i++) {
				
				
				response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesList.get(0)+"/simplified-tags");		
				viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_PATIENT_NAME_TAG),ChestCT1p25mm,"Checkpoint[15/17]","Verifying the instance belong to same patient - patient name");
				viewerPage.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_STUDY_DESC_TAG),chestStudy,"Checkpoint[16/17]","Verifying the instance belong to  same patient - study description");
				viewerPage.assertTrue(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_SERIES_DESC_TAG).contains(cloneSeries.get(k)),"Checkpoint[17/17]","Verifying the instance belong to  same patient - series description");
				
				
				
			}}
		
		
		
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

//		if(RESTUtil.getJsonPath(response).getList("").size()>0) {
		for(int i =0;i<RESTUtil.getJsonPath(response).getList("").size();i++) {
			patient_id = RESTUtil.getJsonPath(response).getList("").get(i).toString();
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
		}

		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);


	}

	private void usersCreation() throws InterruptedException {

		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		

		register.createNewUser(username_1, username_1, LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("abc", "abc", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

	}









} 
























