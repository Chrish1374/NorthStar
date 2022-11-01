package com.trn.ns.test.viewer.envoyAI;

import static org.junit.Assert.assertThat;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSForCADSRTest extends TestBase {

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private HelperClass helper;
    private ViewerSendToPACS sd;
    private PolyLineAnnotation poly;;
    

	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath"));

	private OutputPanel panel;
	static String patient_id="";
	private final String poyLineComment="test";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	//US1088 :Send to PACS support for Mammography CAD SR results
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","DE1923","US2222","Positive","BVT","E2E","F1094"})	
	public void test01_US1088_DE1923_TC5542_TC7686_US2222_TC9988_verifySendToPACSForAcceptedCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send accepted CAD mammography findings to PACS. <br>"+ 
		"Verify an entry is created in Orthanc when 'Accepted' findings are sent to Pacs.<br>"+
		"Verify the new UI for different send to pacs notification messages.");
		
		patientPage = new PatientListPage(driver);
		
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(IHEMammoTestPatientName,1);
		
		panel=new OutputPanel(driver);
		cs=new ContentSelector(driver);

		sd=new ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription,1);
		panel.selectAcceptfromGSPSRadialMenu();
		
		int acceptedCountFromFindingMenu = sd.getTotalAcceptedFindings(1);
		String expectedMessage ="One finding is sent to PACS ( "+acceptedCountFromFindingMenu+" as accepted )";
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,true, false, false);

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and get notification UI message");
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
	
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),  "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
        int acceptedCountFromOP =panel.thumbnailList.size();
		
		//1 finding is sent to PACS ( 1 as accepted )
	   ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of accepted findings from Notification UI");
	   panel.assertEquals(acceptedCountFromFindingMenu,acceptedCountFromOP,"Verify message post sending the findings to PACS","Verified");	
			
	   String currentSelectResult = cs.getSelectedResults().get(0);
		System.out.println(currentSelectResult);
	   ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	   sd.verifyNoOfPatientsInOrthanc(1);
	   
	   ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for accepted findings of CAD SR");
	   verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 1);	
	}

	@Test(groups ={"Chrome","Edge","IE11","US1088","US2222","Positive","F1094"})	
	public void test02_US1088_TC5562_US2222_TC9988_verifySendToPACSForPendingCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send pending CAD mammography findings to PACS.<br>"+
		"Verify the new UI for different send to pacs notification messages.");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
	    helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);

		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription);
		cs.selectResultFromSeriesTab(2, resultDescription, 4);
		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(1)+sd.getBadgeCountFromToolbar(2);

		String expectedMessage =pendingCountFromFindingMenu +" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as pending )";
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,false, false, true);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");;

		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, false, true);
        int pendingCountFromOP =panel.thumbnailList.size();
		
		//One finding is sent to PACS ( 1 as pending )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of pending findings from Notification UI");
	    panel.assertEquals(pendingCountFromOP,pendingCountFromFindingMenu,"Verify message post sending the findings to PACS","Verified");
	    
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for pending findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 1);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","DE1923","US2222","Positive","F1094"})	
	public void test03_US1088_TC5563_DE1923_TC7704_US2222_TC9988_verifySendToPACSForRejectedCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send rejected CAD mammography findings to PACS. <br>"+
		                          "Verify an entry is created in Orthanc when 'Rejected' findings are sent to Pacs. <br>"+
				                  "Verify the new UI for different send to pacs notification messages.");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		panel=new OutputPanel(driver);
		
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription);
		panel.selectRejectfromGSPSRadialMenu();
	
		
		int rejectedCountFromFindingMenu = sd.getTotalRejectedFindings();
		String expectedMessage ="One finding is sent to PACS ( "+rejectedCountFromFindingMenu+" as rejected )";
		
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,false, true, false);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and get message from notification UI");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
        int rejectedCountFromOP =panel.thumbnailList.size();
		
		//1 finding is sent to PACS ( 1 as rejected )
	   ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of rejected findings from Notification UI");
	   panel.assertEquals(rejectedCountFromOP,rejectedCountFromFindingMenu,"Verify message post sending the findings to PACS","Verified");	
			
	   String currentSelectResult = cs.getSelectedResults().get(0);
		
	   ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	   sd.verifyNoOfPatientsInOrthanc(1);
	   
	   ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for rejected findings of CAD SR");
	   verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 1);	
	}

	@Test(groups ={"Chrome","Edge","IE11","US1088","Positive"})	
	public void test04_US1088_TC5564_verifySendToPACSForAcceptRejectAndPendingCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send rejected / accepted / pending CAD mammography findings to PACS");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);

		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		poly =new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		panel.selectFindingFromTable(1, 1);
		panel.selectAcceptfromGSPSRadialMenu();
		panel.selectNextfromGSPSRadialMenu();
		panel.selectRejectfromGSPSRadialMenu();
		
		
		int acceptedCountFromFM = sd.getTotalAcceptedFindings(1);
		int rejectedCountFromFM = sd.rejectedFindings.size();
		int pendingCountFromFM = sd.pendingFindings.size()+1;
		String expectedMessage =(acceptedCountFromFM+rejectedCountFromFM+pendingCountFromFM)+" findings are sent to PACS ( "+acceptedCountFromFM+" as accepted, "+rejectedCountFromFM+" as rejected, "+pendingCountFromFM+" as pending )";
		
		cs.openAndCloseSeriesTab(false);
        sd.openSendToPACSMenu(true,true, true, true);
	
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
	
		//4 findings are sent to PACS ( 1 as accepted, 1 as rejected ,2 as pending )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of accepted,rejected and pending findings from Notification UI");
	    panel.assertEquals((acceptedCountFromFM+rejectedCountFromFM+pendingCountFromFM),(acceptedCount+rejectedCount+pendingCount),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
			
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 3);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","US2222","Positive","F1094"})	
	public void test05_US1088_TC5565_US2222_TC9989_verifySendToPACSForAcceptedAndRejectedCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send rejected + accepted CAD mammography findings to PACS.<br>"+
		"Verify the new UI of notification messages when user sends Rejected+ Accepted results to PACS");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);
	
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		poly =new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		panel.selectFindingFromTable(1, 1);
		panel.selectAcceptfromGSPSRadialMenu();
		panel.selectNextfromGSPSRadialMenu();
		panel.selectRejectfromGSPSRadialMenu();
		
	    
		int acceptedCountFromFM = sd.getTotalAcceptedFindings(1);
		int rejectedCountFromFM = sd.rejectedFindings.size();
		String expectedMessage =(acceptedCountFromFM+rejectedCountFromFM)+" findings are sent to PACS ( "+acceptedCountFromFM+" as accepted, "+rejectedCountFromFM+" as rejected )";
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,true, true, false);
	
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and get message from notification UI");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		//2 findings are sent to PACS ( 1 as accepted, 1 as rejected )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of accepted and rejected findings from Notification UI");
	    panel.assertEquals((acceptedCount+rejectedCount),(acceptedCountFromFM+rejectedCountFromFM),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
			
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for accepted and rejected findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 2);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","Positive"})	
	public void test06_US1088_TC5564_verifySendToPACSForRejectedAndPendingCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send rejected + pending CAD mammography findings to PACS");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);
		
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		poly =new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		panel.selectFindingFromTable(1, 1);
		panel.selectRejectfromGSPSRadialMenu();
		
		int rejectedCountFromFM = sd.getTotalRejectedFindings();
		int pendingCountFromFM = sd.pendingFindings.size()+1;
		String expectedMessage =(rejectedCountFromFM+pendingCountFromFM)+" findings are sent to PACS ( "+rejectedCountFromFM+" as rejected, "+pendingCountFromFM+" as pending )";
        
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,false, true, true);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");;
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		//4 findings are sent to PACS ( 1 as rejected ,3 as pending )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of rejected and pending findings from Notification UI");
	    panel.assertEquals((rejectedCount+pendingCount),(rejectedCountFromFM+pendingCountFromFM),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
			
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for rejected and pending findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 2);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","US2222","Positive","F1094"})	
	public void test07_US1088_TC5567_US2222_US10027_verifySendToPACSForAcceptedAndPendingCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to send accepted  + pending CAD mammography findings to PACS. <br>"+
		"Verify the new UI of notification messages when user sends Pending + Accepted results to PACS");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);

		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		poly =new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		panel.selectFindingFromTable(1, 1);
		panel.selectAcceptfromGSPSRadialMenu();
		
		int acceptedCountFromFM = sd.getTotalAcceptedFindings(1);
		int pendingCountFromFM = sd.pendingFindings.size()+1;
		String expectedMessage =(acceptedCountFromFM+pendingCountFromFM)+" findings are sent to PACS ( "+acceptedCountFromFM+" as accepted, "+pendingCountFromFM+" as pending )";
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,true, false, true);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
		
		//4 findings are sent to PACS ( 1 as accepted ,3 as pending )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of accepted and pending findings from Notification UI");
	    panel.assertEquals((acceptedCount+pendingCount),(acceptedCountFromFM+pendingCountFromFM),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
			
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for accepted and pending findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 2);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","Negative"})	
	public void test08_US1088_TC5568_verifySendToPACSForEditedCADSRFinding() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user edit any CAD SR finding, accepted results should be send to pacs");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);
		
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		poly =new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		panel.selectFindingFromTable(1, 1);
		panel.addResultComment(poly.getLinesOfPolyLine(1,1).get(1),poyLineComment);
		
		int acceptedCountFromFM = sd.getTotalAcceptedFindings(1);
		int pendingCountFromFM = sd.pendingFindings.size()+1;
		String expectedMessage =(acceptedCountFromFM+pendingCountFromFM)+" findings are sent to PACS ( "+acceptedCountFromFM+" as accepted, "+pendingCountFromFM+" as pending )";
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,true, false, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
		
		//4 findings are sent to PACS ( 1 as accepted ,3 as pending )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of accepted and pending findings from Notification UI");
	    panel.assertEquals((acceptedCount+pendingCount),(acceptedCountFromFM+pendingCountFromFM),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
			
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for edited finding and pending findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 2);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1088","Negative"})	
	public void test09_US1088_TC5569_verifySendToPACSForDeletedCADResult() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user deletes any CAD SR machine finding, rejected result should be send to pacs");
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		panel=new OutputPanel(driver);
		
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		poly =new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		panel.selectFindingFromTable(1, 1);
		panel.selectDeletefromGSPSRadialMenu();
		int rejectedCountFromFM = sd.getTotalRejectedFindings();
		int pendingCountFromFM = sd.pendingFindings.size()+1;
		String expectedMessage =(rejectedCountFromFM+pendingCountFromFM)+" findings are sent to PACS ( "+rejectedCountFromFM+" as rejected, "+pendingCountFromFM+" as pending )";
        
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,false, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		//4 findings are sent to PACS ( 1 as rejected ,3 as pending )
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify count of rejected and pending findings from Notification UI");
	    panel.assertEquals((rejectedCount+pendingCount),(rejectedCountFromFM+pendingCountFromFM),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
			
	    String currentSelectResult = cs.getSelectedResults().get(0);
		
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify in Orthanc CAD SR series are created");
	    sd.verifyNoOfPatientsInOrthanc(1);
	   
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for deleted CAD SR finding and pending findings of CAD SR");
	    verifyOrthancFindingsForSRAndPR(IHEMammoTestPatientName, username, " ",currentSelectResult, 2);	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE2022","Positive"})
	public void test10_DR2022_TC8308_verifyCloneIsGenerated() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Clone is getting reflected in Content selector for Mammo CAD data after performing accept all from SendToPacs");	
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
		
		sd=new ViewerSendToPACS(driver);
		cs=new ContentSelector(driver);
		poly =new PolyLineAnnotation(driver);
		panel=new OutputPanel(driver);
	
		List<String> results = cs.getAllResults();
		int beforeResult= results.size();
				
		cs.selectResultFromSeriesTab(1, resultDescription, 1);
		cs.selectResultFromSeriesTab(2, resultDescription, 4);
		
		int pendingCountFromFM = sd.getBadgeCountFromToolbar(1)+ sd.getBadgeCountFromToolbar(2);
		String expectedMessage =(pendingCountFromFM)+" findings are sent to PACS ( "+pendingCountFromFM+" as accepted )";
		
		cs.openAndCloseSeriesTab(false);
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.assertEquals(acceptedCount,pendingCountFromFM,"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[1/4]","Verifying that polyline from viewbox-1 is accepted");		
			
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2, 1),"Checkpoint[2/4]","Verifying that first polyline from viewbox-2 is accepted");		

		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2, 2),"Checkpoint[3/4]","Verifying that second polyline from viewbox-2 is accepted");		
        

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify new clone gets generated after performing Send to pacs action");
        sd.assertNotEquals(cs.getAllResults().size(), beforeResult, "Checkpoint[4/4]", "Verified that new clone copy created after performing send to pacs");
		

	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {
		//DB Default configuration
		DatabaseMethods db = new DatabaseMethods(driver);

		//Getting patient id
				
		if(!patient_id.isEmpty())
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
			
			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
		     db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);
		    db.deleteCloneFromSeriesLevelForCAD(resultDescription+"_"+username );
	}
		
	public void verifyOrthancFindingsForSRAndPR(String patientName, String username, String resultName,String SRResultName,int numberOfEntries) {


		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		// verifying there is only one patient
		panel.assertEquals(RESTUtil.getJsonPath(response).getList("").size(),1,"","");
		patient_id = RESTUtil.getJsonPath(response).getList("").get(0).toString();

		//Getting patient details
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id);
		panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG),patientName,"Checkpoint[o.1]","Verifying the patient name");
		
		//Getting PR/SR Entires
		List<String> seriesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL)).getList("");
		panel.assertEquals(seriesID.size(),numberOfEntries,"Checkpoint[o.2]","Verifying the total PR entries [Pending/accepted/rejected]");
		
		for(int i=0;i<seriesID.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesID.get(i));
        if (RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE))
        {
			panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_PR_VALUE,"Checkpoint[o.3]","Verifying the PR entries name");
			assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(resultName+"_"+ViewerPageConstants.PENDING_TEXT),
					Matchers.is(resultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(resultName+"_"+ViewerPageConstants.REJECTED_TEXT)));
			
        }
        else
        	{panel.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_SR_VALUE,"Checkpoint[o.3]","Verifying the SR entries name");
		assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(SRResultName+"_"+ViewerPageConstants.PENDING_TEXT),
        		Matchers.is(SRResultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(SRResultName+"_"+ViewerPageConstants.REJECTED_TEXT)));	
        	}
		}
		List<String> instancesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL)).getList("");
		panel.assertEquals(instancesID.size(),numberOfEntries,"Checkpoint[o.4]","Verifying the total instances");
		
		
		

		
		

		

	}
		

		
}
