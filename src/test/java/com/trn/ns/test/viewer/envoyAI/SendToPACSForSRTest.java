package com.trn.ns.test.viewer.envoyAI;

import static org.junit.Assert.assertThat;

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
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSForSRTest extends TestBase {

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	
	private PointAnnotation	point;
	private MeasurementWithUnit lineWithUnit;
	private SimpleLine line;
    private HelperClass helper;
    private ViewerSendToPACS sd;

	String filePath1 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String filepath2 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath2);

	private CircleAnnotation circle;
	private OutputPanel panel;
	static String patient_id="";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

	}

	//US1067: Send to PACS for supporting DICOM-SR results
	@Test(groups ={"Chrome","Edge","IE11","US1067","DE1280","US1076","positive","BVT"})	
	public void test01_US1067_TC5282_DE1280_TC5353_US1076_TC5476_verifySendToPACSForAcceptedSRResult() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS for accepted SR result");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);

		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
	
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyBinarySelectorToobar(1),"Checkpoint[1/6]","Verified AR tool bar is present on SR report");
		panel.selectAcceptfromGSPSRadialMenu(1);
		panel.assertTrue(panel.verifyResultsAreAccepted(1),"Checkpoint[2/6]","Verified Selected checkmark is highlighted in green color");

		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2);
		String expectedMessage =(pendingCountFromFindingMenu+1)+" findings are sent to PACS ( 1 as accepted, "+pendingCountFromFindingMenu+" as pending )";

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/6]", "Send to pacs and choose the leave as is option");
		panel.closeNotification();
		sd.openSendToPACSMenu(true, true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage), "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/6]", "Getting the total accepted and pending findings from output panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);;
		int acceptedCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();	

		//30 findings are sent to PACS ( 1 as accepted, 29 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/6]", "Verify count of accepted and Pending findings from Notification UI");
		panel.assertEquals((acceptedCount+pendingCount),(1+pendingCountFromFindingMenu),"Verify message post sending the findings to PACS","Verified");	

		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult = currentSelectResultForSR;

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/6]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, currentSelectResult,currentSelectResultForSR, 2);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1067","US1076","positive"})	
	public void test02_US1067_TC5325_US1076_TC5476_verifySendToPACSForRejectedSRResult() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS for rejected SR result");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);

		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyBinarySelectorToobar(1),"Checkpoint[1/6]","Verified AR tool bar is present on SR report");
		panel.selectRejectfromGSPSRadialMenu(1);
		panel.assertTrue(panel.verifyResultsAreRejected(1),"Checkpoint[2/6]","Verified Selected checkmark is highlighted in red color");

		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2);
		String expectedMessage =(pendingCountFromFindingMenu+1)+" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as accepted, 1 as rejected )";
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/6]", "Send to pacs and choose acccept all option");
		panel.closeNotification();
		sd.openSendToPACSMenu(true, true, true, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
	    panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),  "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/6]", "Getting the total accepted and rejected findings from output panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		

		//30 findings are sent to PACS ( 29 as accepted, 1 as rejected )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/6]", "Verify count of accepted and rejected findings from Notification UI");
		panel.assertEquals((acceptedCount+rejectedCount),(1+pendingCountFromFindingMenu),"Verify message post sending the findings to PACS","Verified");

		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult = currentSelectResultForSR+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/6]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, currentSelectResult,currentSelectResultForSR, 2);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1067","US1076","positive"})	
	public void test03_US1067_TC5328_US1076_TC5476_verifySendToPACSForPendingSRResult() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS for pending SR result");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		panel.closeNotification();
		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2)+1;

		String expectedMessage =pendingCountFromFindingMenu+" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as pending )";
			
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose the leave as is option");
		sd.openSendToPACSMenu(true, true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),  "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total pending findings from output panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount =panel.thumbnailList.size();
		
		//30 findings are sent to PACS (30 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of pending findings from Notification UI");
		panel.assertEquals(pendingCount,pendingCount,"Verify message post sending the findings to PACS by selecting leave as is option","Verified");	

		panel.click(panel.getViewPort(2));
		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult =currentSelectResultForSR;

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm,username, currentSelectResult,currentSelectResultForSR, 2);
	}

	@Test(groups ={"Chrome","Edge","IE11","US1067","US1076","DE1852","DE1941","Positive"})	
	public void test04_US1067_TC5326_US1076_TC5476_DE1852_TC7434_DE1941_TC7836_verifySendToPACSWhenAcceptAllOptionSelected() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS for pending SR result which is changed to accepted before sending to PACS. <br>"+
		"Verify that state of DICOM SR+GSPS findings getting changed on viewer, in findings menu, on vertical scroll bar and in Output panel after Accept All or Reject from SendToPacs Popup[Happy Path] <br>"+
				"Verify the state of findings is updated on Viewer when performed 'Accept all' before sending to PACS.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);

		panel.closeNotification();
		
		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2)+1;
		String expectedMessage =pendingCountFromFindingMenu+" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as accepted )";

		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/7]", "Send to pacs and choose accept all as option from Pending finding dialog");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),  "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/7]", "Getting the total pending findings from output panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.openAndCloseOutputPanel(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();

		//30 findings are sent to PACS (30 as accepted )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/7]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(acceptedCount,pendingCountFromFindingMenu,"Verify message post sending the findings to PACS by selecting Accept all option","Verified");		

		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult = currentSelectResultForSR+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		panel.mouseHover(panel.getViewPort(1));
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[4/7]", "Verified that SR report is in accepted state on viewer");
		panel.assertEquals(panel.getStateSpecificFindings(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size()+1, acceptedCount, "Checkpoint[5/7]", "Verified count of accepted finding in Finding menu.");
		
		int count=panel.getFindingsFromSliderContainer(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		
		panel.assertEquals(count+1, acceptedCount, "Checkpoint[6/7]", "Verified state of GSPS finding on scroll slider.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, currentSelectResult,currentSelectResultForSR, 2);
	   
	}

	@Test(groups ={"Chrome","Edge","IE11","US1067","US1076","positive"})	
	public void test05_US1067_TC5327_US1076_TC5476_verifySendToPACSWhenRejectAllOptionSelected() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS for pending SR result which is changed to rejected before sending to PACS");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		circle=new CircleAnnotation(driver);
		panel.closeNotification();
		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2)+1;
		String expectedMessage =pendingCountFromFindingMenu+" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as rejected )";
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose reject all as option from Pending finding dialog");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),  "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total pending findings from putput panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();

		//30 findings are sent to PACS ( 30 as rejected )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(rejectedCount,pendingCountFromFindingMenu,"Verify message post sending the findings to PACS by selecting Reject all option","Verified");		

		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult = currentSelectResultForSR+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, currentSelectResult,currentSelectResultForSR, 2);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1067","US1076","DE1280","positive"})	
	public void test06_US1067_TC5329_DE1280_TC5353_US1076_TC5476_verifySendToPACSForSRWithUserDrawnAnnotations() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Send to PACS for GSPS  + SR");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		panel.closeNotification();
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);
		lineWithUnit.selectRejectfromGSPSRadialMenu(2);

		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2,50,50);
		point.selectRejectfromGSPSRadialMenu(2);

		line = new  SimpleLine(driver);
		line.selectLineFromQuickToolbar(panel.getViewPort(2));
		line.drawLine(2,-10,10,250,10);
		lineWithUnit.selectAcceptfromGSPSRadialMenu(2);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		
		int acceptedCountFromFindingMenu=sd.getTotalAcceptedFindings(2);
		int	rejectedCountFromFindingMenu=sd.rejectedFindings.size();
		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2)+1;
		String expectedMessage =(acceptedCountFromFindingMenu+rejectedCountFromFindingMenu+pendingCountFromFindingMenu)+" findings are sent to PACS ( "+acceptedCountFromFindingMenu+" as accepted, "+rejectedCountFromFindingMenu+" as rejected, "+pendingCountFromFindingMenu+" as pending )";
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Send to pacs and choose leave as is option from Pending finding dialog");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),  "Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Getting the total accepted,rejected and pending findings from output panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount=panel.thumbnailList.size();

		//34 findings are sent to PACS (1 as accepted,2 as rejected and 31 as pending )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals((acceptedCount+rejectedCount+pendingCount),(acceptedCountFromFindingMenu+rejectedCountFromFindingMenu+pendingCountFromFindingMenu),"Verify message post sending the findings to PACS by selecting Leave as is option","Verified");		

		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult =currentSelectResultForSR+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm,username, currentSelectResult,currentSelectResultForSR, 4);

	}
		
	@Test(groups ={"Chrome","Edge","IE11","DE1625","Positive"})	
	public void test07_DE1652_TC7379_verifyCorrectEntryWhenSendToPACSDoneTwice() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that correct entry for SR data is getting logged in orthanc server when send to pacs again just by changing the state of finding");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);

		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		panel.closeNotification();
		
		int pendingCountFromFindingMenu = sd.getBadgeCountFromToolbar(2)+1;
		String expectedMessage =pendingCountFromFindingMenu+" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as accepted )";
				
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/8]", "Send to pacs and choose accept all as option from Pending finding dialog");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/8]", "Getting the total accepted findings from putput panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.openAndCloseOutputPanel(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();

		//30 findings are sent to PACS ( 30 as accepted )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/8]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(acceptedCount,pendingCountFromFindingMenu,"Verify message post sending the findings to PACS by selecting Accept all option","Verified");		
		
		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults().get(0);
		String currentSelectResult =currentSelectResultForSR+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/8]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, currentSelectResult,currentSelectResultForSR, 2);

		//clicking on Accept button on AR tool bar and changing the state to Pending for DICOM SR in 1st view box.
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectAcceptfromGSPSRadialMenu();

		//Taking finding count and iterating the loop to change the state of all accepted findings to pending.
		int count=panel.getFindingsCountFromFindingTable(2);
		panel.click(panel.getViewPort(2));

		for(int i=0;i<count;i++)
			panel.click(panel.gspsAccept);

		expectedMessage =pendingCountFromFindingMenu+" findings are sent to PACS ( "+pendingCountFromFindingMenu+" as rejected )";
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/8]", "Send to pacs and choose reject all as option from Pending finding dialog");
		panel.openAndCloseOutputPanel(true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,true,false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, expectedMessage),"Verify send to pacs notification.", "Verified");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Verify error notification after send to PACS.","Verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/8]", "Getting the total rejected findings from putput panel");
		panel.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();

		//30 findings are sent to PACS ( 30 as accepted )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/8]", "Verify count of rejected findings from Notification UI");
		panel.assertEquals(rejectedCount,pendingCountFromFindingMenu,"Verify message post sending the findings to PACS by selecting Reject all option","Verified");		
		panel.openAndCloseOutputPanel(false);
		String CurrentSelectResultForSR = cs.getSelectedResults().get(0);
		String CurrentSelectResult =currentSelectResultForSR+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1";

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/8]", "Verifying the orthanc for findings of SR");
		verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, CurrentSelectResult,CurrentSelectResultForSR, 4);
	}

	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {
		//DB Default configuration
		DatabaseMethods db = new DatabaseMethods(driver);
		db.setUserFeedback(ChestCT1p25mm,panel.getSeriesDescriptionOverlayText(1),"",OrthancAndAPIConstants.ORTHANC_SR_VALUE);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
		db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
		db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
		db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);


		if(!patient_id.isEmpty())
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();


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
