package com.trn.ns.test.viewer.SR4D;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class OutputPanelForSRTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private HelperClass helper;
	private ViewerSliderAndFindingMenu findingMenu;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;
	private ViewerSendToPACS viewerSendToPacs;

	String filePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,filePath1);

	String filePath2 =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");

	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	private OutputPanel panel;
	private PointAnnotation point;
	private MeasurementWithUnit lineWithUnit;
	private CircleAnnotation circle;
	private ContentSelector contenSelector;
	private PolyLineAnnotation poly;
	private int seriesLevelID;




	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username ,password);

		DatabaseMethods db = new DatabaseMethods(driver);
		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1066","DE1747"})
	public void test01_US1066_TC5333_TC5335_TC5336_TC5352_DE1747_TC7340_verifySRReportInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify accepted SR report is displayed under Accepted tab in output panel"
				+ "<br> Verify rejected SR report is displayed under Rejected tab in output panel"
				+ "<br> Verify pending SR report is displayed under Pending tab in output panel"
				+ "<br> Verify description beside thumbnail containing SR"
				+ "<br> Verify SR icon in output panel on browser resize"
				+ "<br> Verify spinning wheel is displayed till SR icon gets loaded in thumbnail");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(ChestCT1p25mm);

		String tooltipHeader = patientListPage.getText(patientListPage.getAllMachineNameFromTooltip(1).get(0));

		patientListPage.mouseHover(patientListPage.studyDescriptionHeader);
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		viewerPage.closeNotification();

		String resultDescription = viewerPage.getSeriesDescriptionOverlayText(1);
		verifySRInOutputPanel(false, false, true,1,resultDescription,tooltipHeader,"test01_1");

		viewerPage.click(viewerPage.getViewPort(2));
		findingMenu.acceptResult(1);
		verifySRInOutputPanel(true, false, false,2,resultDescription,tooltipHeader,"test01_1");		

		viewerPage.click(viewerPage.getViewPort(2));
		findingMenu.rejectResult(1);
		verifySRInOutputPanel(false, true, false,3,resultDescription,tooltipHeader,"test01_1");

		viewerPage.click(viewerPage.getViewPort(2));
		findingMenu.rejectResult(1);
		verifySRInOutputPanel(false, false, true,4,resultDescription,tooltipHeader,"test01_1");

		Dimension dimension = new Dimension(800, 600);
		String parentWindow = panel.getCurrentWindowID();
		panel.setWindowSize(parentWindow,dimension);
		verifySRInOutputPanel(false, false, true,1,resultDescription,tooltipHeader,"test01_2");
		panel.maximizeWindow();
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1066"})
	public void test02_US1066_TC5337_verifyGSPSWithSRReportInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify SR + GSPS in output panel");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(ChestCT1p25mm);

		String tooltipHeader = patientListPage.getText(patientListPage.getAllMachineNameFromTooltip(1).get(0));
		patientListPage.mouseHover(patientListPage.studyDescriptionHeader);
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);
		viewerPage.closeNotification();

		String resultDescription = viewerPage.getSeriesDescriptionOverlayText(1);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.acceptResultOnBinaryToolbar(1);

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);
		lineWithUnit.selectRejectfromGSPSRadialMenu(2);

		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2,50,50);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		circle.closingConflictMsg();	

		List<String> rejectedFindings = findingMenu.getFindingsName(2,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = findingMenu.getFindingsName(2,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = findingMenu.getFindingsName(2,ViewerPageConstants.PENDING_FINDING_COLOR);

		verifySRInOutputPanel(true, false, false,2,resultDescription,tooltipHeader,"test01_1");

		viewerPage.mouseHover(viewerPage.getViewPort(2));
		panel=new OutputPanel(driver) ;	
		panel.enableFiltersInOutputPanel(true, false, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), acceptedFindings.size()+1, "Checkpoint[5/7]", "verifying the Accepted finding name");

		panel.enableFiltersInOutputPanel(false, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(),rejectedFindings.size(), "Checkpoint[6/7]", "verifying the Rejected finding name");

		panel.enableFiltersInOutputPanel(false, false,true);
		viewerPage.assertEquals(panel.thumbnailList.size(),pendingFindings.size(), "Checkpoint[7/7]", "verifying the Pending finding name");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1068","BVT","US2284","US2328","F1153","E2E","F1125"})
	public void test03_US1068_TC5383_US2284_TC9756_US2328_TC10470_verifyJumpToSRReportWhenReportIsnotLoaded() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  SR series is getting loaded on viewer when clicked performed on thumbnail when SR series is not loaded."
				+ "<br> Verify Jump to finding from Output Panel thumbnail."
				+ "<br> Verify the warning banner displayed when a finding is selected from Output Panel from a series that is not loaded on viewer.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		viewerPage.closeNotification();
		layout=new ViewerLayout(driver);

		String seriesDesc = viewerPage.getSeriesDescriptionOverlayText(1);		

		contenSelector = new ContentSelector(driver);
		contenSelector.selectSeriesFromSeriesTab(1, viewerPage.getSeriesDescriptionOverlayText(2));
		contenSelector.openAndCloseSeriesTab(false);

		String changedSeriesDesc= viewerPage.getSeriesDescriptionOverlayText(1);
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[1/10]", "Unloading the sr report and verifying the series name changed");

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[2/10]", "Verifying the jump to icon functionality");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[3/10]", "verifying the report is loaded in first viewbox post click on jump to icon");

		viewerPage.doubleClickOnViewbox(2);
		viewerPage.closeNotification();

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[4/10]", "verifying the jump to icon when report is not loaded using double click");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), seriesDesc, "Checkpoint[5/10]", "verifying the report is loaded in first viewbox post jump to icon click");

		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[6/10]", "Verifying the series name using content selector post layout change");
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), seriesDesc, "Checkpoint[7/10]", "Verifying the series name using content selector post layout change");

		layout.selectLayout(layout.oneByOneLayoutIcon);

		contenSelector.selectSeriesFromSeriesTab(1, changedSeriesDesc);		
		contenSelector.openAndCloseSeriesTab(false);
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[8/10]", "Verifying the series name using content selector post layout change");

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[9/10]", "Verifying the jump to icon click functionality");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[10.1/10]", "verifying the report is loaded in first viewbox on click of jump to icon");
		panel.assertFalse(panel.verifyPresenceOfNotification(), "Checkpoint[10.2/10]", "Verifying no warning notification displayed in case of Non-DICOM/SR not loaded");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1068"})
	public void test04_US1068_TC5384_verifyJumpToSRReportWhenReportIsLoaded() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to click on the JumpToIcon in thumbnail of SR when SR series is opened in viewer.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		viewerPage.closeNotification();
		viewerPage.click(viewerPage.getViewPort(2));

		String resultDesc = viewerPage.getSeriesDescriptionOverlayText(1);	
		String seriesDesc= viewerPage.getSeriesDescriptionOverlayText(2);
		int currentLayout = viewerPage.getNumberOfCanvasForLayout();

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		//panel.clickOnJumpIcon(1);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), resultDesc, "Checkpoint[1/4]", "Verifying nothing has changed on viewbox 1");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), seriesDesc, "Checkpoint[2/4]", "Verifying nothing has changed on viewbox 2");
		viewerPage.assertEquals( viewerPage.getNumberOfCanvasForLayout(), currentLayout, "Checkpoint[3/4]", "Verifing the layouts");
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1068"})
	public void test05_US1068_TC5386_verifyJumpToSRReportOnLayoutChange() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Implement Jump to icon functionanlity when non-dicom series is opened from output panel and layout is in 1*2  or in 2*1.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		viewerPage.closeNotification();
		String seriesDesc = viewerPage.getSeriesDescriptionOverlayText(1);		

		contenSelector = new ContentSelector(driver);
		contenSelector.selectSeriesFromSeriesTab(1, viewerPage.getSeriesDescriptionOverlayText(2));

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		String changedSeriesDesc= viewerPage.getSeriesDescriptionOverlayText(1);		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[1/7]", "Verifying the unloading the SR report");

		layout=new ViewerLayout(driver);
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[2/7]", "Verifying the series desc has changed");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[3/7]", "Verifying the result is loaded post jumptoicon click");

		layout.selectLayout(layout.oneByTwoLayoutIcon);

		contenSelector.selectSeriesFromSeriesTab(1, changedSeriesDesc);
		contenSelector.selectSeriesFromSeriesTab(2, changedSeriesDesc);

		viewerPage.mouseHover(viewerPage.getViewPort(2));
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[4/7]", "Verifying the reult description is changed post layout changed to  1x2 ");
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[5/7]", "Verifying the series is not changed in viewbox 2 post jumptoicon click");
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[6/7]", "Verifying the result is loaded post jumptoicon click");		

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1068","BVT"})
	public void test06_US1068_TC5387_verifyJumpToSRReportIsAcceptedAndRejected() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to open the SR series from Output panel when SR is in Accepted and in Rejected state.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.acceptResultOnBinaryToolbar(1);
		viewerPage.closeNotification();
		String seriesDesc = viewerPage.getSeriesDescriptionOverlayText(1);		

		contenSelector = new ContentSelector(driver);
		contenSelector.selectSeriesFromSeriesTab(1, viewerPage.getSeriesDescriptionOverlayText(2));
		contenSelector.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting the SR report");
		viewerPage.click(viewerPage.getViewPort(1));

		String changedSeriesDesc= viewerPage.getSeriesDescriptionOverlayText(1);		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[1/8]", "Verifying the series name after unloading the sr report");

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[2/8]", "Verifying the series is unloaded");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[3/8]", "Verifying the SR loading post Jump to icon");
		viewerPage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[4/8]", "Verifying the sr repoort is accepted");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting the SR report");		
		findingMenu.rejectResult(1);

		seriesDesc = viewerPage.getSeriesDescriptionOverlayText(1);		

		contenSelector = new ContentSelector(driver);
		contenSelector.selectSeriesFromSeriesTab(1, viewerPage.getSeriesDescriptionOverlayText(2));
		contenSelector.openAndCloseSeriesTab(false);

		changedSeriesDesc= viewerPage.getSeriesDescriptionOverlayText(1);		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[5/8]", "Verifying the series name after unloading the sr report");

		panel.enableFiltersInOutputPanel(false, true, false);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[6/8]", "Verifying the series is unloaded");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[7/8]", "Verifying the SR loading post Jump to icon");
		viewerPage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[8/8]", "Verifying the sr repoort is rejected");
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1068"})
	public void test07_US1068_TC5388_verifyLogsWhenReportNotLoaded() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', 'UnloadSerie's, 'LoadSeriesStart', 'LoadSeriesEnd' when series is not loaded in the view box.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		viewerPage.closeNotification();

		String seriesDesc = viewerPage.getSeriesDescriptionOverlayText(1);		

		contenSelector = new ContentSelector(driver);
		contenSelector.selectSeriesFromSeriesTab(1, viewerPage.getSeriesDescriptionOverlayText(2));

		String changedSeriesDesc= viewerPage.getSeriesDescriptionOverlayText(1);
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(2), changedSeriesDesc, "Checkpoint[1/12]", "Verifying the series after changing the SR report");

		DatabaseMethodsADB dba = new DatabaseMethodsADB(driver);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[2/12]", "Verifying series loaded using CS is unloaded");		
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[3/12]", "verifying SR report is loaded post click on jump to icon");

		List<String> unloadSeriesPayload = dba.getPayload(ActionLogConstant.UNLOAD_SERIES);
		List<String> startSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START);
		List<String> endSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END);
		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);

		viewerPage.assertEquals(unloadSeriesPayload.size(), 1, "Checkpoint[4/12]", "Verifying unload series payload");
		viewerPage.assertEquals(startSeriesPayload.size(), 1, "Checkpoint[5/12]", "verifying the start series payload");	
		viewerPage.assertEquals(endSeriesPayload.size(), 1, "Checkpoint[6/12]", "Verifying the end series payload");
		viewerPage.assertEquals(thumbnailPayload.size(), 2, "Checkpoint[7/12]", "Verifying thumbnail payload");

		String mimetype = dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
		viewerPage.assertEquals(mimetype,ActionLogConstant.APPLICATION_SR,"Checkpoint[8/12]","Verifying the MIME type");
		mimetype = dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
		viewerPage.assertEquals(mimetype,ActionLogConstant.APPLICATION_SR,"Checkpoint[9/12]","Verifying the MIME type");

		String seriesLoadType =dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[10/12]","Verifying the from output panel flag");
		seriesLoadType =dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[11/12]","verifying the output panel flag");

		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[12/12]","Verifying the isSeries loaded flag");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1068"})
	public void test08_US1068_TC5389_verifyLogsWhenReportIsLoaded() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', when series is loaded in any of the view box.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		viewerPage.closeNotification();

		String changedSeriesDesc= viewerPage.getSeriesDescriptionOverlayText(1);

		viewerPage.click(viewerPage.getViewPort(2));

		DatabaseMethodsADB dba = new DatabaseMethodsADB(driver);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.scrollIntoView(panel.srThumbnail);
		panel.clickOnJumpIcon(panel.thumbnailList.size());

		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[1/3]", "Verifying the report is loaded on click of jump to icon");		

		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);

		viewerPage.assertEquals(thumbnailPayload.size(), 1, "Checkpoint[2/3]", "Verifying the thumbnail payload size");
		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[3/3]","verifying the isSeriesLoaded flag");

	}

	private void verifySRInOutputPanel(boolean accept, boolean reject, boolean pending,int checkpoint, String resultDesc,String tooltipHeader, String imageName) throws TimeoutException, InterruptedException {

		panel=new OutputPanel(driver) ;	

		panel.enableFiltersInOutputPanel(accept, reject, pending);

		panel.waitForElementInVisibility(panel.spinningWheel);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:["+checkpoint+"/4]","");
		panel.scrollIntoView(panel.srThumbnail);
		panel.assertTrue(panel.isElementPresent(panel.srThumbnail),"Checkpoint:["+checkpoint+".a/4]","Verifying that SR thumbnail is displayed");		
		panel.openAndCloseOutputPanel(false);

	}

	@AfterMethod
	public void restoreSRReportState() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);


	}

	//	US1086 - Display Mammography CAD SR Results in the Output panel

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1086","DR2280"})
	public void test09_US1086_TC5483_DR2280_TC9204_verifyOutputPanelForCADSR() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify output panel for CAD SR (accepted + rejected + pending).<br>"+
				"[Risk and Impact] US1742- TC8747 ");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		point= new PointAnnotation(driver);
		poly = new PolyLineAnnotation(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		panel=new OutputPanel(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		List<String> results = contentSelector.getAllResults();		

		preset=new ViewBoxToolPanel (driver);
		preset.changeZoomNumber(1,200);
		contentSelector.assertFalse(contentSelector.isElementPresent(viewerPage.getLossyOverlay(1)), "Checkpoint[1/4]", "Verified that Lossy is not present on viewer when zoom % is more than 100.");
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result in first viewbox");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		contentSelector.closingConflictMsg();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the first result in second viewbox");
		contentSelector.selectResultFromSeriesTab(2, results.get(0),1);
		contentSelector.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting the two findings and rejecting one finding from one viewbox");
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(1).get(0));
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);	
		poly.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the count from finding menu");
		List<String> rejectedFindings = findingMenu.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = findingMenu.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		//		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(2, 1).get(0),true);	
		List<String> pendingFindings = findingMenu.getFindingsName(2,ViewerPageConstants.PENDING_FINDING_COLOR); 

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the accepted findings in output panel");
		panel.enableFiltersInOutputPanel(true,false,false);
		viewerPage.assertEquals(panel.thumbnailList.size(),acceptedFindings.size(), "Checkpoint[2/4]", "verifying finding name in output panel");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the rejected findings in output panel");
		panel.enableFiltersInOutputPanel(false, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(),rejectedFindings.size(), "Checkpoint[3/4]", "verifying finding name in output panel");

		viewerPage.click(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the pending findings in output panel");
		panel.enableFiltersInOutputPanel(false,false,true);
		viewerPage.assertEquals(panel.thumbnailList.size(),pendingFindings.size(), "Checkpoint[4/4]", "verifying finding name in output panel");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1086"})
	public void test10_US1086_TC5526_TC5544_TC5575_verifyJumpToFunctionalityForCADSR() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify jumpto functionality for CAD SR (accepted + rejected + pending)"
				+ "<br> Verify UserActionLog for JumpToThumbnail"
				+ "<br> Verify clone when findings are edited");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		poly = new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		List<String> results = contentSelector.getAllResults();		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result in first viewbox");
		contentSelector.selectResultFromSeriesTab(1, results.get(results.size()-1),4);
		contentSelector.closingConflictMsg();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the first result in second viewbox");
		contentSelector.selectResultFromSeriesTab(2, results.get(0),1);
		contentSelector.openAndCloseSeriesTab(false);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting the two findings and rejecting one finding from one viewbox");
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(1).get(0));
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);	
		poly.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retrieving the count from finding menu");
		//		List<String> rejectedFindings = viewerPage.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		//		List<String> acceptedFindings = viewerPage.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);

		//		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(2, 1).get(0),true);	
		//		List<String> pendingFindings = viewerPage.getFindingsName(2,ViewerPageConstants.PENDING_FINDING_COLOR);		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the click on jumpto icon functionality for accepted findings");

		DatabaseMethodsADB dba = new DatabaseMethodsADB(driver);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		panel=new OutputPanel(driver) ;	
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnThumbnail(1);

		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);

		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[1/15]","verifying on click of jumpto icon CADSR findings are focused");
		//		poly.assertTrue(panel.verifyFindingIsHighlighted(acceptedFindings.get(0)),"checkpoint[2/15]","Verifying that the finding is also highlighted in finding menu");

		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[3/15]","Verifying the isSeries loaded flag");
		viewerPage.assertTrue(dba.getKeyValue(thumbnailPayload.get(0),ActionLogConstant.JUMPED_VIEWBOX_INFO, ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_SERIES_INFO).contains(ActionLogConstant.APPLICATION_SR),"Checkpoint[4/15]","Verifying the isSeries loaded flag");
		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0),ActionLogConstant.JUMPED_VIEWBOX_INFO, ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_TYPE),ActionLogConstant.CAD_SR_RESULT_TYPE,"Checkpoint[5/15]","Verifying the isSeries loaded flag");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the click on jumpto icon functionality for rejected findings");
		panel.enableFiltersInOutputPanel(false, true, false);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		panel.clickOnThumbnail(1);

		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 2),"Checkpoint[6/15]","verifying on click of jumpto icon CADSR findings are focused");
		//		poly.assertTrue(panel.verifyFindingIsHighlighted(rejectedFindings.get(0)),"checkpoint[7/15]","Verifying that the finding is also highlighted in finding menu");

		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[8/15]","Verifying the isSeries loaded flag");
		viewerPage.assertTrue(dba.getKeyValue(thumbnailPayload.get(0),ActionLogConstant.JUMPED_VIEWBOX_INFO, ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_SERIES_INFO).contains(ActionLogConstant.APPLICATION_SR),"Checkpoint[9/15]","Verifying the isSeries loaded flag");
		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0),ActionLogConstant.JUMPED_VIEWBOX_INFO, ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_TYPE),ActionLogConstant.CAD_SR_RESULT_TYPE,"Checkpoint[10/15]","Verifying the isSeries loaded flag");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the click on jumpto icon functionality for pending findings");
		panel.enableFiltersInOutputPanel(false, false, true);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		panel.clickOnThumbnail(1);

		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(2,1),"Checkpoint[11/15]","verifying on click of jumpto icon CADSR findings are focused");
		//		poly.assertTrue(panel.verifyFindingIsHighlighted(pendingFindings.get(0)),"checkpoint[12/15]","Verifying that the finding is also highlighted in finding menu");

		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[13/15]","Verifying the isSeries loaded flag");
		viewerPage.assertTrue(dba.getKeyValue(thumbnailPayload.get(0),ActionLogConstant.JUMPED_VIEWBOX_INFO, ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_SERIES_INFO).contains(ActionLogConstant.APPLICATION_SR),"Checkpoint[14/15]","Verifying the isSeries loaded flag");
		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0),ActionLogConstant.JUMPED_VIEWBOX_INFO, ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_TYPE),ActionLogConstant.CAD_SR_RESULT_TYPE,"Checkpoint[15/15]","Verifying the isSeries loaded flag");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1086", "Negative"})
	public void test11_US1086_TC5528_VerifyWarningMessageForThumbnail()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify jumpto functionality (yellow ribbon error message) when data is not present in the viewer");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		String series = viewerPage.getSeriesDescriptionOverlayText(1);

		// click on output panel
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false,false,true);

		//click on first thumbnail image from output panel
		panel.clickOnThumbnail(1);

		// verify warning message for thumbnail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+series+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1086"})
	public void test12_US1086_TC5537_verifyAddingCommentsInOP() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comment added under CAD SR findings are displayed in output pan");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		poly = new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);

		String comment1 ="Test1";
		String comment2 = "Test2";

		ContentSelector contentSelector = new ContentSelector(driver);		
		List<String> results = contentSelector.getAllResults();		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result in first viewbox");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		contentSelector.openAndCloseSeriesTab(false);

		poly.addResultComment(poly.getLinesOfPolyLine(1, 1).get(0), comment1);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[1/5]","verifying on click of jumpto icon CADSR findings are focused");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the comment is displayed in output panel");
		panel=new OutputPanel(driver) ;	
		panel.enableFiltersInOutputPanel(true, false, false);

		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""),comment1,"Checkpoint[2/5]","Verifying the comment1 is displayed in output panel");

		poly.addMultiLineResultComment(poly.getLinesOfPolyLine(1, 1).get(0), comment2);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[3/5]","verifying on click of jumpto icon CADSR findings are focused");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the comment is displayed in output panel");
		panel.enableFiltersInOutputPanel(true, false, false);		

		panel.assertEquals(panel.getMultiLineCommenEntriesInOP(1).get(1),comment1,"Checkpoint[4/5]","Verifying the comment1 is displayed in output panel");
		panel.assertEquals(panel.getMultiLineCommenEntriesInOP(1).get(0),comment2,"Checkpoint[5/5]","Verifying the comment2 is displayed in output panel");


	}

	@Test(groups ={"Chrome","Edge","IE11","Positive","US1086"})
	public void test13_US1086_TC5539_TC5540_verifyCADSRResultReplacedByOriginals() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify output panel for CAD SR when clone copy is replaced by original machine result in viewer"
				+ "<br> Verify output panel for CAD SR when both original copy + clone copy is present in viewer");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		poly=new PolyLineAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		List<String> results = contentSelector.getAllResults();		
		viewerPage.assertEquals(results.size(), 4, "Checkpoint[1/11]","Verify list of result seen in content selector");

		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		contentSelector.openAndCloseSeriesTab(false);
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[2/11]","Verifying there are 3 GSPS objects present");		
		contentSelector.assertTrue(contentSelector.verifyPresenceOfEyeIcon(results.get(0)), "Checkpoint[3/11]","Verifying the last result is selected");


		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(false, false, true);		
		panel.assertEquals(panel.thumbnailList.size(),4, "Checkpoint[4/11]","verifying the outputpanel for findings");			
		panel.openAndCloseOutputPanel(false);


		point = new PointAnnotation(driver);		
		point.movePoint(1, 1, 40, 60);		

		poly = new PolyLineAnnotation(driver);
		poly.moveFreePolyLine(1, 1, 70, -30);						

		poly.waitForTimePeriod(3000);

		contentSelector.assertTrue(contentSelector.getAllResults().size()>results.size(), "Checkpoint[6/11]","verifying there are clones present post editing");		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout to 1x1 and selecting the original results from content selector");
		viewerPage.doubleClick(viewerPage.getViewPort(1));
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		contentSelector.openAndCloseSeriesTab(false);

		panel.enableFiltersInOutputPanel(false, false, true);		
		panel.assertEquals(panel.thumbnailList.size(),4, "Checkpoint[7/11]","verifying the outputpanel for findings");		
		panel.openAndCloseOutputPanel(false);

		contentSelector.assertTrue(contentSelector.verifyPresenceOfEyeIcon(results.get(0)), "Checkpoint[9/11]","verifying the original result is selected in content selector");


		//		Verify output panel for CAD SR when both original copy + clone copy is present in viewer
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		contentSelector.selectResultFromSeriesTab(2,results.get(0)+"_"+username+"_1",4);
		contentSelector.openAndCloseSeriesTab(false);

		panel.enableFiltersInOutputPanel(true, false, true);		
		panel.assertEquals(panel.thumbnailList.size(),4, "Checkpoint[10/11]","verifying the outputpanel for findings when original and clone are loaded in viewer");	
		panel.openAndCloseOutputPanel(false);





	}

	//@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1408"})
	public void test14_DE1408_TC5714_verifyOPWhenSRLoadedInOneByOne() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify output panel when SR report loaded in 1*1 layout");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		ContentSelector contentSelector = new ContentSelector(driver);	

		String resultDesc1 = viewerPage.getSeriesDescriptionOverlayText(1);	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ChestCT1p25mm+" in viewer" );
		viewerPage.closeNotification();
		viewerPage.doubleClick(viewerPage.getViewPort(2));
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 1, "Checkpoint[1/5]", "Verify layout loaded layout is 1*1");

		contentSelector.selectResultFromSeriesTab(2, resultDesc1, 1);
		contentSelector.openAndCloseSeriesTab(false);

		panel = new OutputPanel(driver);
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		panel.enableFiltersInOutputPanel(false, false, true);

		viewerPage.assertTrue(viewerPage.isElementPresent(panel.outputPanelSection),"Checkpoint[2/5]","Verify Output panel should get open when SR loaded in 1*1");
		//		viewerPage.assertTrue(viewerPage.checkForTextPresenceInElement(panel.acceptedButton, "Accepted"),"Checkpoint[3/5]","Verify for presence of Accepted button");
		//		viewerPage.assertTrue(viewerPage.checkForTextPresenceInElement(panel.rejectedButton, "Rejected"),"Checkpoint[4/5]","Verify for presence of Rejected button");
		//		viewerPage.assertTrue(viewerPage.checkForTextPresenceInElement(panel.pendingButton, "Pending"),"Checkpoint[5/5]","Verify for presence of Pending button");	

	}

	//DE1345: Console error seen on ThumbnailMouseOver and ThumbnailMouseOut on SR data

	@Test(groups ={"Chrome","Edge","IE11","DE1345","Positive"})
	public void test14_DE1345_TC5520_TC5664_verifyConsoleOnMouseHoverForSRAndNonDicom() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no console error on hovering over SR icon in thumbnail"
				+ "<br> Console error seen on ThumbnailMouseOver and ThumbnailMouseOut on NonDicom data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);

		viewerPage.closeNotification();
		panel=new OutputPanel(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Load patient data on viewer for " +ChestCT1p25mm);


		panel.enableFiltersInOutputPanel(false, false, true);

		//verify console for SR icon on mousehover and mouseout for SR
		panel.mouseHoverOnJumpIcon(1);
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[1/8]", "Verify no console error seen when mouse hover on thumbnail for SR ");

		//check same functionality for Non-dicom
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Load patient data on viewer for " +imbio_PatientName);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);

		panel.enableFiltersInOutputPanel(false, false, true);

		//verify console for SC data on mousehover and mouseout
		panel.mouseHover(panel.thumbnailList.get(0));
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[3/8]", "Verify no console error seen when mouse hover on thumbnail for SC data");

		//verify console for SC data on mousehover and mouseout for PDF
		panel.mouseHover(panel.thumbnailList.get(1));
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[5/8]", "Verify no console error seen when mouse hover on thumbnail for PDF");

		//check same functionality for Non-dicom jpeg 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Load patient data on viewer for " +IBL_JohnDoe_PatientName);
		helper.browserBackAndReloadViewer(IBL_JohnDoe_PatientName, 1, 4);

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.mouseHover(panel.thumbnailList.get(0));
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[7/8]", "Verify no console error seen when mouse hover on thumbnail for Jpeg data");


	}

	//DE1348 :Opening output panel causes an 'Aw, Snap' error 
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1348"})
	public void test15_DE1348_TC5541_verifyNoErrorWhileOpeningOutputPanelFrequently() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no memory error or system should crash on opening output panel frequently");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest," ","Loading patient "+IHEMammoTestPatientName+ "on viewer page");
		panel=new OutputPanel(driver);
		int layout=viewerPage.getNumberOfCanvasForLayout();

		for(int i=0;i<=5;i++)
		{
			panel.enableFiltersInOutputPanel(true, true, true);
			panel.openAndCloseOutputPanel(false);
		}

		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[1/2]", "Verified that no console error while opening output panel frequently");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),layout, "Checkpoint[2/2]", "Verfied that system not crash and application is running");
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1348"})
	public void test16_DE1348_TC5615_verifyNoErrorOnRenderingSeriesFromContentSelector() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no memory error or system should crash on rendering series from content selector");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(IHEMammoTestPatientName, 2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest," ","Loading patient "+IHEMammoTestPatientName+ "on viewer page");
		contenSelector=new ContentSelector(driver);
		panel=new OutputPanel(driver);

		int layout=viewerPage.getNumberOfCanvasForLayout();
		String seriesName=viewerPage.getSeriesDescriptionOverlayText(1);

		for(int i=0,j=1;i<6;i++,j++)
		{
			if(j==4)
				j=1;
			contenSelector.selectResultFromSeriesTab(1, contenSelector.getAllResults().get(0),j);
			contenSelector.selectSeriesFromSeriesTab(1,seriesName);
		}

		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[1/2]", "Verified that no console error while rendering series/result from content selector frequently");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),layout, "Checkpoint[2/2]", "Verfied that system not crash and application is running");
	}

	//de1867: [Hardening] Dicom view box is not getting highlighted when a finding is selected from Output panel and finding panel.
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1867"})
	public void test17_DE1867_TC7498_verifyViewboxBorderWhenFindingSelectedFromOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify viewbox is getting highlight when a finding is selected from Output panel.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ChestCT1p25mm, 2);
		viewerPage.closeNotification();

		panel=new OutputPanel(driver);

		circle=new CircleAnnotation(driver);

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(2);
		panel.assertTrue(panel.isElementPresent(panel.outputPanelSection), "Checkpoint[1/3]","Verified that Output panel closed after click on jump icon");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(2, 1), "Checkpoint[2/3]", "Verified that circle annotation is current pending active GSPS.");
		panel.assertTrue(panel.verifyBorderForActiveViewbox(2,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[3/3]", "Verified that viewbox-2 border is active.");

	}
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DR2100"})
	public void test18_DR2100_TC8494_verifyCommentAddedAfterViewerReload() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the comment added from Output Panel is mapped to the correct finding on viewer and is retained after the reload.");
		helper=new HelperClass(driver);
		helper.loadViewerPageUsingSearch(IHEMammoTestPatientName, 1, 1);

		panel=new OutputPanel(driver) ;
		poly = new PolyLineAnnotation(driver);
		String comment = "testing";
		ContentSelector contentSelector = new ContentSelector(driver);		
		List<String> results = contentSelector.getAllResults();		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result in first viewbox");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		contentSelector.closingConflictMsg();

		panel.enableFiltersInOutputPanel(true,true,true);	
		panel.addCommentFromOutputPanel(2, comment);
		panel.openAndCloseOutputPanel(false);

		panel.assertEquals(poly.getText(poly.getTextCommentsOnAllPolyLines(1).get(0)),comment, "Checkpoint[1/5]", "Verified added comment on viewer page.");	
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, comment), "Checkpoint[2/5]", "Verified added comment in finding menu.");

		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);

		contentSelector.selectResultFromSeriesTab(1, resultDescription+"_"+username+"_1", 4);
		contentSelector.closingConflictMsg();
		panel.assertEquals(poly.getText(poly.getTextCommentsOnAllPolyLines(1).get(0)), comment,"Checkpoint[3/5]", "Verifying the comment for finding in viewer is displayed after reload of viewer.");

		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1,comment), "Checkpoint[4/5]", "Verifying the comment for finding in finding menu is displayed after reload of viewer.");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), comment, "Checkpoint[5/5]", "Verifying the comment for finding in Output panel is displayed after reload of viewer.");

	}
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DR2716","F1125"})
	public void test19_DR2716_TC10696_verifyFilterMenuOnClickOfCommentIcon() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that filter menu is getting closed when user clicks on 'Comment' icon");

		patientListPage = new PatientListPage(driver);
		
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 1);
	
		panel=new OutputPanel(driver);
		viewerSendToPacs=new ViewerSendToPACS(driver);
	
		panel.click(panel.warningPopUp);
		panel.enableFiltersInOutputPanel(true, false, true);
		
		panel.mouseHover(panel.findingTileContainers.get(3));
		panel.click(panel.commentIconOnTiles.get(3));		
		
		panel.assertTrue(panel.isElementPresent(panel.editableText),"Checkpoint[1/3]","Verifying that comment editbox in output panel is open");
		panel.assertFalse(panel.isElementPresent(panel.acceptedButton),"Checkpoint[2/3]","Verifying that filter menu is closed");

		panel.click(panel.filterFindingsIcon);
		panel.click(viewerSendToPacs.sendToPacs);
		panel.assertFalse(panel.isElementPresent(panel.acceptedButton),"Checkpoint[3/3]","Verifying that filter menu is closed");

	}

	@AfterMethod
	public void restoringCADSR() throws SQLException {

		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);
		db.deleteCloneFromSeriesLevelForCAD(resultDescription+"_"+username );

	}

}



