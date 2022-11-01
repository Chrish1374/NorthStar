package com.trn.ns.test.viewer.SR4D;


import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SRDataTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private PolyLineAnnotation poly;
	private CircleAnnotation circle;
	private HelperClass helper;
	private ViewerLayout layout;

	String filePath = Configurations.TEST_PROPERTIES.get("MR_CARDIAC_2_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);	
	String secondResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("MR_CARDIAC_2_filepath"));
		
	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath"));
	
	private PointAnnotation point;
	private static int seriesLevelID;
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private RegisterUserPage register;
	private String username_1 ="UserA";
	private ContentSelector contentSelector;
	private MeasurementWithUnit lineWithUnit;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		
		DatabaseMethods db = new DatabaseMethods(driver);
		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);

	}

	@Test(groups ={"Chrome","Edge","IE11","DE708","Sanity"})
	public void test01_DE708_TC3357_verifySRData() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Import SR data set");

		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName, 2);

		ContentSelector contentSelector = new ContentSelector(driver);
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getViewboxNotification(1)),"Checkpoint[1/4] : Verifying the banner presence","if CS id product then banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getViewboxNotificationMessage(1)),"Checkpoint[2/4] : Verifying the overlay presence in vb1","verified");
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.WARNING_TITLE, ViewerPageConstants.WARNING_SR_DATA_MSG),"Checkpoint[3/6] : Verifying the warning message","verified");

		contentSelector.selectResultFromSeriesTab(2, secondResultDescription);
		
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getViewboxNotification(2)),"Checkpoint[3/4] : Verifying the banner presence post select the result "+secondResultDescription+" using content selector","verified");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getViewboxNotificationMessage(2)),"Checkpoint[4/4] : Verifying the overlay presence in vb2","Warning message should be displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getViewboxNotificationMessage(2)),ViewerPageConstants.WARNING_SR_DATA_MSG,"Checkpoint[6/6] : Verifying the warning message","verified");

		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE708","DE1928","Sanity"})
	public void test02_DE708_TC3357_DE1928_TC7805_verifyEditingOfCADSRData() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit CAD SR finding. <br>"+
		"Verify AR tool bar is displayed on the initial load for the series with findings like machine GSPS.");
		String description="Mammography CAD SR";
		DatabaseMethods db=new DatabaseMethods(driver);
		db.setSOPClassUID(description, true);
		
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
			
		poly=new PolyLineAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		
		contentSelector.selectResultFromSeriesTab(1, resultDescription);
		viewerpage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[1/7]", "Verified that AR toolbar visible when GSPS is auto selected.");
		
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(resultDescription), "Checkpoint[2/7]", "Verify Result is loaded on first viewbox");
		
		int polylineX=poly.getValueOfXCoordinate(poly.getLinesOfPolyLine(1, 1).get(1));
		int polylineY= poly.getValueOfYCoordinate(poly.getLinesOfPolyLine(1, 1).get(1));
	
		poly.openFindingTableOnBinarySelector(1);
		poly.selectFindingFromTable(1);
		viewerpage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[3/7]", "Verify Polyline annotation is current active accepted GSPS");
		
		poly.moveFreePolyLine(1, 1, 50, 50);
		poly.click(poly.getViewPort(1));
		poly.mouseHover(poly.getViewPort(1));
		poly.mouseHover(poly.getGSPSHoverContainer(1));
		poly.assertTrue(poly.verifyPendingGSPSToolbarMenu(), "Checkpoint[4/7]", "Verified that AR toolbar visible after mouse hover when finding is not selected.");
		
		
		poly.assertNotEquals(poly.getValueOfXCoordinate(poly.getLinesOfPolyLine(1, 1).get(1)), polylineX, "Checkpoint[5/7]", "verifying post movement the x co-ordinate is changed");
		poly.assertNotEquals(poly.getValueOfYCoordinate(poly.getLinesOfPolyLine(1, 1).get(1)), polylineY, "Checkpoint[6/7]", "verifying post movement the y co-ordinate is changed");
		
       int []coordinates = new int[] {30,40,50,60};
    	
    	List<WebElement> handles = poly.getLinesOfPolyLine(1, 1);
		
		poly.editPolyLine(handles.get(2), coordinates,handles.get(9));
		
	    poly.assertTrue(poly.getLinesOfPolyLine(1, 1).size()< handles.size(), "Checkpoint[7/7] Verify that poly line editing is successful", "verified");
		

		
	}
	
//	US1084 - Display Mammography CAD SR Results in the Viewer
		
	@Test(groups ={"Chrome","Edge","IE11","US1084","Sanity"})
	public void test03_US1084_TC5103_verifyCADSRAndItsFindings() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that CAD SR should be present on referenced images");
			
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint[1/11]: Verifying the CAD SR is rendering on viewer properly","test03_01");
//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2), "Checkpoint[2/11]: Verifying the CAD SR is rendering on viewer properly","test03_02");
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		
		List<String> results = contentSelector.getAllResults();		
		viewerpage.assertEquals(results.size(), 4, "Checkpoint[3/11]","Verify list of result seen in content selector");
		
		contentSelector.selectResultFromSeriesTab(1, results.get(0));		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),1,"Checkpoint[4/11]","Verifying there is 1 GSPS objects present");
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[5/11]", "Verifying the poly is present on first result");
		
		contentSelector.selectResultFromSeriesTab(1, results.get(results.size()-1),4);
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[6/11]","Verifying there are 3 GSPS objects present");
		
		poly.assertEquals(poly.getAllPolylines(1).size(),2,"Checkpoint[7/11]","Verifying there are 2 polyline on last result");
		point.assertEquals(point.getAllPoints(1).size(), 1, "Checkpoint[8/11]", "verifying there is one point on last result");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[9/11]","verifying that first polyline is pending inactive");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[10/11]","verifying that second poly is active polyline");
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[11/11]","verifying that point is currently inactive");
		
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1084","Positive","US1085"})
	public void test04_US1084_TC5104_TC5106_TC5202_US1085_TC5108_verifyCADSRResultEditingAndContentSelector() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit CAD SR finding"
				+ "<br> Verify that user should be able to see CAD SR results in \"Results\" tab of content selector"
				+ "<br> Verify that CAD SR report should not be display in content selector"
				+ "<br> Verify that finding should get accepted when user edit any pending CAD SR finding");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);	

		poly=new PolyLineAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		List<String> results = contentSelector.getAllResults();		
		viewerpage.assertEquals(results.size(), 4, "Checkpoint[1/8]","Verify list of result seen in content selector");
	
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[2/8]","Verifying there are 3 GSPS objects present");		
		contentSelector.assertTrue(contentSelector.verifyPresenceOfEyeIcon(results.get(0)), "Checkpoint[3/8]","verifying there are clones present post editing");
		
		point = new PointAnnotation(driver);
		point.mouseHover(point.getViewPort(1));
		point.movePoint(1, 1, 40, 60);		
		poly.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/8]","verifying that point is currently accepted and focused after movement");		
		
		poly = new PolyLineAnnotation(driver);
		poly.moveFreePolyLine(1, 1, 70, -30);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[5/8]","verifying that second poly is active polyline post movement");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[6/8]","Verifying there are 3 GSPS objects present post editing of objects");		
		contentSelector.assertTrue(contentSelector.getAllResults().size()>results.size(), "Checkpoint[7/8]","verifying there are clones present post editing");
		
		contentSelector.assertTrue(contentSelector.verifyPresenceOfEyeIcon(results.get(0)+"_"+username+"_1"), "Checkpoint[8/8]","verifying there are clones present post editing");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1084","Negative"})
	public void test05_US1084_TC5205_TC5203_verifyCADSRResultOnViewer() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that CAD SR report should not be display in viewer"
				+ "<br> Verify user action logs for unload and load series using content selector");
	
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		
		DatabaseMethodsADB db = new DatabaseMethodsADB(driver);
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
	
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);		
	
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		String layout1 = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);
		
		int totalViewboxes = viewerpage.getNumberOfCanvasForLayout();
		
		viewerpage.assertEquals(totalViewboxes, 2, "Checkpoint[1/9]", "Verify that CAD SR report (html) should not be display in viewer");
		
		//Verify LoadSeriesStart and LoadSeriesEnd logs (when user access the viewer page) contains loadSeriesActionID and loadStudyActionID
		viewerpage.assertEquals(db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START).size(), totalViewboxes, "Checkpoint[2/9]", "Verifying the Load series start count is equals to number of viewboxes");
		viewerpage.assertEquals(db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END).size(), totalViewboxes, "Checkpoint[3/9]", "Verifying the Load series end count is equals to number of viewboxes");		

		String seriesUID ="";	
		
		for(int i=1;i<=totalViewboxes;i++) {			

			seriesUID = db.getSeriesInstanceUID(IHEMammoTestPatientName,viewerpage.getSeriesDescriptionOverlayText(i));
			List<String> startSeriesPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);			
			List<String> endSeriesPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);			

			db.assertTrue(db.verifySeriesStartAndEnd(startSeriesPayload, endSeriesPayload, studyActionID, "true", "false", "false", ActionLogConstant.APPLICATION_DICOM, layout1), "Checkpoint[4/9", "Verifying the start and end series payload details");
			db.assertTrue(db.getKeyValue(startSeriesPayload.get(0),ActionLogConstant.SERIES_INFO, ActionLogConstant.MODALITY_DB).contains(ActionLogConstant.SR_MODALITY), "Checkpoint[5/9]", "Verifying the modality for SR");
			
		}

		ContentSelector contentSelector = new ContentSelector(driver);
		List<String> results = contentSelector .getAllResults();		
		
		// Loading the CAD SR using content Selector
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		seriesUID = db.getSeriesInstanceUID(IHEMammoTestPatientName,viewerpage.getSeriesDescriptionOverlayText(1));

		ContentSelector cs = new ContentSelector(driver);
		cs.selectResultFromSeriesTab(1, results.get(0));

		List<String> unloadSeriesPayload = db.getPayload(ActionLogConstant.UNLOAD_SERIES,seriesUID);
		db.assertTrue(db.verifyUnloadSeries(unloadSeriesPayload, seriesUID, ActionLogConstant.APPLICATION_DICOM, ActionLogConstant.ONE_BY_ONE_VIEWBOXNO), "Checkpoint[6/9]", "Verifying the unload series when series is selected using content selector");

		seriesUID = db.getSeriesInstanceUID(IHEMammoTestPatientName,viewerpage.getSeriesDescriptionOverlayText(2));
		startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);

		db.assertTrue(db.verifyResulltStartAndEnd(startPayload, endPayload, studyActionID, "false", "false", "true", ActionLogConstant.APPLICATION_SR, layout1), "Checkpoint[7/9]", "Verifying that payload for new series selected using content selector for start and load payload");
		db.assertEquals(db.getKeyValue(startPayload.get(0),ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_SERIES_INFO, ActionLogConstant.MODALITY_DB),ActionLogConstant.SR_MODALITY, "Checkpoint[8/9]", "Verifying modality for SR");
				
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.assertEquals(layout.getNumberOfCanvasForLayout(), 9, "Checkpoint[9/9]", "Verify that CAD SR report(html) should not be display in viewer post layout change as well");

	}
		
//	US1085 - Accept/Reject navigation support for Mammography CAD SR
//	US1087 - Accept / Reject individual findings in a Mammography CAD SR Result
	
	@Test(groups ={"Chrome","Edge","IE11","US1085","Positive","US1087"})
	public void test06_US1085_TC5104_US1087_TC5417_VerifyCADSRFindingsAreNotDeleted() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should not be able to delete CAD SR machine findings"
				+ "<br> Verify that CAD SR finding should be rejected when user delete it");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);			
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
        List<String> results = contentSelector.getAllResults();		
		
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[1/13]","verifying that second polyline is active polyline");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[2/13]","verifying that first polyline is inactive pending polyline");		
		
		poly = new PolyLineAnnotation(driver);
		poly.deleteSelectedPolyline();
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[3/13]","verifying that first polyline is inactive rejected polyline post deletion");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[4/13]","verifying that second polyline is active polyline");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[5/13]","Verifying there are 3 GSPS objects present post deletion of findings");		
				
		poly.selectDeletefromGSPSRadialMenu();
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[6/13]","verifying that first polyline is inactive rejected polyline post deletion");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 2),"Checkpoint[7/13]","verifying that second polyline is inactive rejected polyline post deletion");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[8/13]","verifying that point is currently pending and focused after deletion");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[9/13]","Verifying there are 3 GSPS objects present post deletion of findings");		
		
		point.selectDeletefromGSPSRadialMenu();
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 2),"Checkpoint[10/13]","verifying that first polyline is active rejected polyline post deletion");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[11/13]","verifying that second polyline is inactive rejected polyline post deletion");		
		poly.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1, 1),"Checkpoint[12/13]","verifying that point is currently rejected");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[13/13]","Verifying there are 3 GSPS objects present post deletion of findings");		

	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1085","Positive","US1087","US1411","BVT"})
	public void test07_US1085_TC5107_TC5109_TC5110_TC5212_US1087_TC5412_TC5413_TC5414_TC5416_TC5443_US1411_TC7760_VerifyUserIsAbleToAcceptReject() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able accept / reject CAD SR findings"
				+ "<br> Verify that finding should get accepted when user edit any rejected CAD SR finding"
				+ "<br> Verify total number of pending CAD SR finding should get display in A / R bar finding drop down icon"
				+ "<br> Verify that next finding should get highlighted when user accepted any CAD SR finding"
				+ "<br> Verify that user can accept CAD SR finding"
				+ "<br> Verify that user can reject CAD SR finding"
				+ "<br> Verify that pending CAD SR finding should be accepted when user edit it"
				+ "<br> Verify that rejected CAD SR finding should be accepted when user edit it"
				+ "<br> Verify state icon color in finding menu dropdown when user accept / reject CAD SR finding.<br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);			
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
		List<String> results = contentSelector.getAllResults();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/14]", "Loading the fourth result and validating upon load one is highlighted and there are in total three objects");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"verifying that point is currently accepted and focused","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/14]", "Retrieving the count from finding menu");
		int count = poly.getBadgeCountFromToolbar(1);
		List<String> rejectedFindings = poly.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = poly.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = poly.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/14]", "validating the count from finding menu");
		poly.assertEquals(count,3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(pendingFindings.size(),count,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(acceptedFindings.isEmpty(),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(rejectedFindings.isEmpty(),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
				
//		Verify that user can accept CAD SR finding
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/14]", "Accepting first polyline and validating that focus is moved to another object");
		poly = new PolyLineAnnotation(driver);
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 2),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"verifying that point is currently accepted and focused ","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
			
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/14]", "Validating the badge count and accept reject and pending findings count from menu");
		poly.assertEquals(poly.getBadgeCountFromToolbar(1),2,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),poly.getBadgeCountFromToolbar(1),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(poly.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
	
//		Verify that user can reject CAD SR finding
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/14]", "rejecting second polyline and validating that focus is moved to another object");
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"verifying that point is currently accepted and focused","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/14]", "Validating the badge count and accept reject and pending findings count from menu");
		poly.assertEquals(poly.getBadgeCountFromToolbar(1),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),poly.getBadgeCountFromToolbar(1),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/14]", "Rejecting once again should lead the second polyline to pending state");
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"verifying that point is currently accepted and focused","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[9/14]", "Validating the badge count and accept reject and pending findings count from menu");
		poly.assertEquals(poly.getBadgeCountFromToolbar(1),2,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(),poly.getBadgeCountFromToolbar(1),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertEquals(poly.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(poly.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[10/14]", "Rejecting the first poly line");
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 2),"verifying that second poly is active polyline","verified");		
	
//		Verify that rejected CAD SR finding should be accepted when user edit it
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[11/14]", "Validating after movement/edit its state should turn it to accepted");
		poly.moveFreePolyLine(1, 2, 30, 50);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"verifying that second poly is active polyline","verified");		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[12/14]", "Changing the first polyline state to pending");
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 2),"verifying that second poly is active polyline","verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[13/14]", "Validating after movement/edit its state should turn it to accepted");
		poly.moveFreePolyLine(1, 2, 30, 50);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"verifying that second poly is active polyline","verified");		
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[14/14]", "Selecting the first result, validating that upon editing object state should change it to accepted from pending");
		contentSelector.selectResultFromSeriesTab(1, results.get(0));
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		

		poly.moveFreePolyLine(1, 1, 30, 50);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verifying that second poly is active polyline","verified");		
	
		
		
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1085","Positive","US1087"})
	public void test08_US1085_TC5206_TC5207_US1087_TC5444_VerifyFindingsSelectionFromMenuAndCommentsToFindings() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify CAD SR finding should get display when user clicks on finding name from finding menu"
				+ "<br> Verify that user can add comments on any findings of CAD SR"
				+ "<br> Verify that pending / rejected CAD SR finding should be accepted when user adds comment");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
        List<String> results = contentSelector.getAllResults();		
		
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[1/21]","verifying on finding is highlighted when user selects the result from content selector");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[2/21]","verifying that second poly is inactive polyline as one finding is highlighted");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[3/21]","verifying that point is currently pending inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[4/21]","Verifying there are 3 GSPS objects present");		
				
		List<String> pendingFindings = poly.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);
		
		poly.selectFindingFromTable(pendingFindings.get(0));
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[5/21]","verifying that second poly is active polyline after selecting the first finding from finding menu");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[6/21]","verifying that second poly is inactive polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[7/21]","verifying that point is currently inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[8/21]","Verifying there are 3 GSPS objects present after selecting the findings from menu");		
		
		poly.selectFindingFromTable(pendingFindings.get(1));
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[9/21]","verifying that first poly is active after selecting from menu");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[10/21]","verifying that second polyline is inactive polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[11/21]","verifying that point is currently inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[12/21]","Verifying there are 3 GSPS objects present after selecting the findings from menus");		
		
		poly.selectFindingFromTable(pendingFindings.get(2));
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[13/21]","verifying that first polyline is inactive polyline");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 2),"Checkpoint[14/21]","verifying that second polyline is inactive polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[15/21]","verifying that point is currently active as selecting the point from menu");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[16/21]","Verifying there are 3 GSPS objects present after selecting the findings from menu");		
		
		String poyLineComment = "PolyLine Comment";
		poly.addResultComment(poly.getLinesOfPolyLine(1, 1).get(0),poyLineComment,true);
		viewerpage.assertEquals(viewerpage.getText(poly.resultComment.get(0)), poyLineComment, "Checkpoint[17/21]", "The entered result comment is "+poyLineComment);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[18/21]","verifying that second polyline is active accepted polyline post movement");		
		
		poly.hoverOnAddTextButtonForFinding(poly.getLinesOfPolyLine(1,2).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[19/21]", "Verify a infobar appear on hovering add text button, if a comment already exist for finding");
		viewerpage.assertTrue(viewerpage.isElementPresent(poly.commentInfo),"Checkpoint[20/21]","The info bar is present on viewbox");
		viewerpage.assertEquals(viewerpage.getText(poly.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Checkpoint[21.1/21]", "Verified the text of info bar");		
	
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);
		poly.addResultComment(poly.getLinesOfPolyLine(1, 2).get(0),poyLineComment,true);
		viewerpage.assertEquals(viewerpage.getText(poly.resultComment.get(1)), poyLineComment, "Checkpoint[21.2/21]", "The entered result comment is "+poyLineComment);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[21.3/21]","verifying that second polyline is active accepted polyline post movement");		
	
		
	
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1085","Positive"})
	public void test09_US1085_TC5210_TC5211_VerifyFindingsNavigationKeyboard() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that next finding should get highlighted / selected when user press  > arrow key from keyboard on CAD SR data"
				+ "<br> Verify that prev finding should get highlighted / selected when user press <  arrow key from keyboard on CAD SR data");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
       List<String> results = contentSelector.getAllResults();		
		
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[1/28]","verifying that second poly is active polyline");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[2/28]","verifying that second poly is active polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[3/28]","verifying that point is currently pending inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[4/28]","Verifying there are 3 GSPS objects present post navigations");		
				
	
		poly.navigateGSPSForwardUsingKeyboard();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[5/28]","Verifying that first polyline is highlighted after performing the GSPS forward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[6/28]","verifying that second poly is inactive polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[7/28]","verifying that point is currently pending inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[8/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		poly.navigateGSPSForwardUsingKeyboard();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 2),"Checkpoint[9/28]","Verifying that second polyline is pending and inactive");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[10/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[11/28]","Verifying that point is highlighted after performing the GSPS forward navigation");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[12/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		poly.navigateGSPSForwardUsingKeyboard();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[13/28]","Verifying that second polyline is highlighted after performing the GSPS forward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[14/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[15/28]","Verifying that point is inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[16/28]","Verifying there are 3 GSPS objects present post navigations");	
		

		poly.navigateGSPSBackUsingKeyboard();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 2),"Checkpoint[17/28]","Verifying that second polyline is pending and inactive");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[18/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[19/28]","Verifying that point is highlighted after performing the GSPS forward navigation");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[20/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		poly.navigateGSPSBackUsingKeyboard();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[21/28]","Verifying that first polyline is highlighted after performing the GSPS backward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[22/28]","Verifying that second polyline is pending and inactivet");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[23/28]","verifying that point is currently accepted and focused after movement");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[24/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		
		poly.navigateGSPSBackUsingKeyboard();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[25/28]","Verifying that first polyline is highlighted after performing the GSPS backward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[26/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[27/28]","verifying that point is currently pending and inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[28/28]","Verifying there are 3 GSPS objects present post navigations");	

		
	
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US1085","Positive"})
	public void test10_US1085_TC5208_TC5209_VerifyFindingsNavigationGSPSMenu() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that previous finding should get highlighted / selected when user clicks on \"PREV\" (<) arrow from A / R bar in CAD SR data"
				+ "<br> Verify that next finding should get highlighted / selected when user clicks on \"NEXT\" (>) arrow from A / R bar in CAD SR data");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
       List<String> results = contentSelector.getAllResults();		
		
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);				
			
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[1/28]","verifying that second poly is active polyline");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[2/28]","verifying that second poly is active polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[3/28]","verifying that point is currently pending inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[4/28]","Verifying there are 3 GSPS objects present post navigations");		
				
	
		poly.selectNextfromGSPSRadialMenu();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[5/28]","Verifying that first polyline is highlighted after performing the GSPS forward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[6/28]","verifying that second poly is inactive polyline");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[7/28]","verifying that point is currently pending inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[8/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		poly.selectNextfromGSPSRadialMenu();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 2),"Checkpoint[9/28]","Verifying that second polyline is pending and inactive");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[10/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[11/28]","Verifying that point is highlighted after performing the GSPS forward navigation");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[12/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		poly.selectNextfromGSPSRadialMenu();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[13/28]","Verifying that second polyline is highlighted after performing the GSPS forward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[14/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[15/28]","Verifying that point is inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[16/28]","Verifying there are 3 GSPS objects present post navigations");	
		

		poly.selectPreviousfromGSPSRadialMenu();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 2),"Checkpoint[17/28]","Verifying that second polyline is pending and inactive");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[18/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[19/28]","Verifying that point is highlighted after performing the GSPS forward navigation");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[20/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		poly.selectPreviousfromGSPSRadialMenu();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[21/28]","Verifying that first polyline is highlighted after performing the GSPS backward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[22/28]","Verifying that second polyline is pending and inactivet");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[23/28]","verifying that point is currently accepted and focused after movement");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[24/28]","Verifying there are 3 GSPS objects present post navigations");		
	
		
		poly.selectPreviousfromGSPSRadialMenu();
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[25/28]","Verifying that first polyline is highlighted after performing the GSPS backward navigation");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[26/28]","Verifying that first polyline is pending and inactive");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint[27/28]","verifying that point is currently pending and inactive");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Checkpoint[28/28]","Verifying there are 3 GSPS objects present post navigations");	

		
	
	}
			
	@Test(groups ={"Chrome","Edge","IE11","US1085","Positive","US1087"})
	public void test11_US1087_TC5418_VerifyCloneOnAcceptReject() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new clone copy should get created when user go back to study page and reload the viewer after accepting any CAD SR finding"
				+ "<br> Verify that new clone copy should get created when user logout from application load the viewer after accepting any CAD SR finding"
				+ "<br> Verify that new clone copy should get created when second user edits first user's clone copy for CAD SR finding");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
		List<String> results = contentSelector.getAllResults();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/8]", "Accepting first polyline and validating that focus is moved to another object");
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 2),"verifying that first poly is active polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"verifying that point is currently accepted and focused ","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
			
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/8]", "Verifying that clone is generated in Content selector");
//		contentSelector.assertEquals(contentSelector.getAllResults(1).size(), (results.size()+4),  "Verfiying the clones are generated", "verified");
		
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 2);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/8]", "Verify that new clone copy entry should be added into content selector");
		List<String> clonedEntries = contentSelector.getAllResults();
		
		contentSelector.assertEquals(clonedEntries.size(), (results.size()+4),  "Verfiying the clones are stayed on reload", "verified");
		for(int i =0,j=1 ;i<contentSelector.getAllResults().size();i++,j++)
		{
			contentSelector.assertFalse(contentSelector.verifyPresenceOfEyeIcon(clonedEntries.get(i), j), "Verifying that none of result is loaded on viewer", "verified");
			if(j==4)
				j=1;
		}
		
		Header header = new Header(driver);
		header.logout();
		
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad(2);	
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/8]", "Verifying the clones are displayed post logout");
		contentSelector.assertEquals(clonedEntries.size(), (results.size()+4),  "Verfiying the clones are stayed on reload", "verified");
		for(int i =0,j=1 ;i<contentSelector.getAllResults().size();i++,j++)
		{
			contentSelector.assertFalse(contentSelector.verifyPresenceOfEyeIcon(clonedEntries.get(i), j), "Verifying that none of result is loaded on viewer", "verified");
			if(j==4)
				j=1;
		}
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating users UserA");
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1 , username_1, username_1);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging using user A");
		header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);
		
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad(2);	
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/8]", "Verifying the clones are displayed for new user");
		contentSelector.assertEquals(clonedEntries.size(), (results.size()+4),  "Verfiying the clones are stayed on load", "verified");
		for(int i =0,j=1 ;i<contentSelector.getAllResults().size();i++,j++)
		{
			contentSelector.assertFalse(contentSelector.verifyPresenceOfEyeIcon(clonedEntries.get(i), j), "Verifying that none of result is loaded on viewer", "verified");
			if(j==4)
				j=1;
		}
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/8]", "Trying to edit result from user 1");
		contentSelector.selectResultFromSeriesTab(1, clonedEntries.get(clonedEntries.size()-1));		
		point.selectRejectfromGSPSRadialMenu();		
//		contentSelector.assertEquals(contentSelector.getAllResults(2).size(), (clonedEntries.size()+4),  "Verfiying the clones are stayed on reload", "verified");
		
		
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/8]", "Clones presence post reload");
		contentSelector.assertEquals(contentSelector.getAllResults().size(), (clonedEntries.size()+4),  "Verfiying the clones are stayed on reload", "verified");
		clonedEntries = contentSelector.getAllResults();
		for(int i =0,j=1 ;i<contentSelector.getAllResults().size();i++,j++)
		{
			contentSelector.assertFalse(contentSelector.verifyPresenceOfEyeIcon(clonedEntries.get(i), j), "Verifying that none of result is loaded on viewer", "verified");
			if(j%4==0)
				j=1;
		}
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[8/8]", "Verifying the clones for user scan are displayed as well created by user one");
		header.logout();
		loginPage.login(username,password);
		
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad(2);	
		
		contentSelector.assertEquals(contentSelector.getAllResults().size(), (clonedEntries.size()),  "Verfiying the clones are stayed on reload", "verified");
		for(int i =0,j=1 ;i<contentSelector.getAllResults().size();i++,j++)
		{
			contentSelector.assertFalse(contentSelector.verifyPresenceOfEyeIcon(clonedEntries.get(i), j), "Verifying that none of result is loaded on viewer", "verified");
			if(j%4==0)
				j=1;
		}
		
	}
			
	@Test(groups ={"Chrome","Edge","IE11","DE1362","Positive"})
	public void test12_DE1362_TC5681_VerifyAcceptRejectForCADSecondTime() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of CAD SR finding gets updated when accepted/rejected second time.");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
        List<String> results = contentSelector.getAllResults();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Accepting first polyline and validating that focus is moved to another object");		
		
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 2),"verifying that first poly is active polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"verifying that point is currently accepted and focused ","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(poly.verifyPendingGSPSToolbarMenu(), "Verifying the pending gsps toolbar is displayed as focus is moved to other annotation", "verified");
		poly.selectPreviousfromGSPSRadialMenu();
		poly.assertTrue(poly.verifyAcceptGSPSRadialMenu(), "Verifying the accepted gsps toolbar is displayed when navigated to prev annotation", "verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Rejecting first polyline and validating that focus is moved to another object");		
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 2),"verifying that first poly is rejected polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"verifying that second poly is active polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"verifying that point is currently accepted and focused ","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(poly.verifyPendingGSPSToolbarMenu(), "Verifying the pending gsps toolbar is displayed as focus is moved to other annotation", "verified");
		poly.selectPreviousfromGSPSRadialMenu();
		poly.assertTrue(poly.verifyRejectGSPSRadialMenu(), "verifying the rejected gsps toolbar is displayed when navigated to prev annotation", "verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Selecting the first result and verifying the behavior");		
		contentSelector.selectResultFromSeriesTab(1, results.get(results.size()-1),1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "Accepting  the polyline and verifying the state is changed");		
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verifying that first poly is active polyline","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(poly.verifyAcceptGSPSRadialMenu(), "Verifying the accepted gsps toolbar is displayed", "verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "Rejecting the same polyline and verifying the state is changed");		
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"verifying that first poly is rejected polyline","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.assertTrue(poly.verifyRejectGSPSRadialMenu(), "Verifying the rejected gsps toolbar is displayed", "verified");

		
	}
		
	@Test(groups ={"Chrome","Edge","IE11","DE1362","Negative"})
	public void test13_DE1362_TC5715_VerifyStateCADOnCNTRLDEL() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify State of CAD SR finding  gets updated when pressed ctrl+del to delete all machine generated annotations");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);		
		
		poly=new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);		
		
        List<String> results = contentSelector.getAllResults();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the fourth result");
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Deleting the annotations using cntrl+del and verifying the state is changed");			
		poly.deleteAllAnnotation(1);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"verifying that first poly is rejected polyline","verified");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 2),"verifying that second poly is active rejected polyline","verified");		
		poly.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1),"verifying that point is currently rejected and focused ","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),3,"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		poly.mouseHover(poly.acceptRejectToolbar);
		List<String> findings = poly.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),findings.size(),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Accepting the polyline and verifying its state");			
		poly.selectAcceptfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 2).get(0),true);		
		poly.assertTrue(poly.verifyRejectGSPSRadialMenu(), "Verifying the pending gsps toolbar is displayed as focus is moved to other annotation", "verified");
		poly.selectPreviousfromGSPSRadialMenu();
		poly.assertTrue(poly.verifyAcceptGSPSRadialMenu(), "verifying the accepted gsps toolbar is displayed when navigated to prev annotation", "verified");
		
				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Selecting the first result and verifying the behavior");		
		contentSelector.selectResultFromSeriesTab(1, results.get(results.size()-1),1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Deleting the annotation using cntrl+del and verifying the state is changed");
		poly.deleteAllAnnotation(1);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"verifying that first poly is active polyline","verified");		
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),1,"Verifying there are 3 GSPS objects present post editing of objects","verified");	
		poly.mouseHover(poly.acceptRejectToolbar);
		findings = poly.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		poly.assertEquals(poly.getAllGSPSObjects(1).size(),findings.size(),"Verifying there are 3 GSPS objects present post editing of objects","verified");		
		
	

		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1436","Positive"})
	public void test14_DE1436_TC5808_TC5875_TC5876verifyARToolBarOnCad() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that AR tool bar gets closed when moved out of the view box(the view box where the AR tool bar is displayed) in multi view box layout.<br>"+ 
				"Verify that the finding list is not duplicated when navigated through the findings using < and > arrow from the AR tool bar.<br>" + 
				"Verify the selection of the findings when only CAD SR findings are available in the series.");

		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);	

		contentSelector=new ContentSelector(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		
		lineWithUnit=new MeasurementWithUnit(driver);

		String SecondSeriesDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT04_TEXTOVERLAY, filePath1);
		contentSelector.selectResultFromSeriesTab(2, SecondSeriesDescription, 4);

		String firstSeriesDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath1);
		contentSelector.selectResultFromSeriesTab(1, firstSeriesDescription, 1);

		// Selecting and changing the state of annotation from pending to Accept in both the view box.

		poly.mouseHover(poly.getGSPSHoverContainer(1));
		poly.selectFindingFromTable(1);
		poly.selectAcceptfromGSPSRadialMenu();

		poly.mouseHover(poly.getViewPort(2));

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, 50, 50, 100, 50);
		
		// Verifying the AR tool bar present or not in both view boxes simultaneously.
		lineWithUnit.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1), "Verifying is AR tool bar present or not", "AR tool bar is not present");
		lineWithUnit.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(2), "Verifying is AR tool bar present or not simultaneously on 2nd view box", "AR tool bar is not present");

		lineWithUnit.closingConflictMsg();
		
		lineWithUnit.selectLinearMeasurement(2, 1);
		lineWithUnit.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1), "Verifying is AR tool bar present or not", "AR tool bar is not present");
		lineWithUnit.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(2), "Verifying is AR tool bar present or not simultaneously on 2nd view box", "AR tool bar is not present");

		//Implementing TC5875 and TC5876

		int countBeforeNavigation=lineWithUnit.getFindingsCountFromFindingTable();

		for(int i=0;i<=countBeforeNavigation*2;i++)
			lineWithUnit.selectNextfromGSPSRadialMenu();
		lineWithUnit.assertEquals(lineWithUnit.getFindingsCountFromFindingTable(), countBeforeNavigation, "Verifying the findings count before and after navigation in loop twice.", "Findings count is same before and after navigation.");


	}
	
	@Test(groups = {"Chrome", "IE11", "Edge", "DE1754","Negative"})
	public void test15_DE1754_TC7302_verifyResultWhenAnnotationDrawnOnSourceSeriesForCADData() throws  InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the added annotation on source series does not add the annotations on respective CAD result series.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);	

		circle=new CircleAnnotation(driver);
		
		contentSelector=new ContentSelector(driver);
		contentSelector.selectResultFromSeriesTab(1, resultDescription, 1);
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 5, 5, -80, -80);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[1/4]", "Verified that circle annotation is current active accepted GSPS.");
		circle.mouseHover(circle.getGSPSHoverContainer(2));
		circle.openFindingTableOnBinarySelector(2);
		circle.assertEquals(circle.acceptedFindings.size(), 1, "Checkpoint[2/4]", "Verified that circle annotation visible in finding table as well.");
		circle.closingConflictMsg();
		
		//mousehover on first viewbox i.e. result
		circle.mouseHover(circle.getViewPort(1));
		circle.assertFalse(circle.isCirclePresent(1), "Checkpoint[3/4]", "Verified that circle annotation is not present on result series.");
		circle.openFindingTableOnBinarySelector(1);
		circle.assertTrue(circle.acceptedFindings.isEmpty(), "Checkpoint[4/4]", "Verified that no accepted finding visible in finding menu dropdown.");
		
		
		
		}
	
	@Test(groups ={"Chrome", "IE11", "Edge","Positive" ,"DE2143"})
	public void test16_DE2143_TC8710_verifyCADFindingOnMovement() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that CAD finding is not deselected after a second after moving it.");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);	

		ContentSelector contentSelector = new ContentSelector(driver);		

		List<String> results = contentSelector.getAllResults();		
		viewerpage.assertEquals(results.size(), 4, "Checkpoint[1/2]","Verify list of result seen in content selector");
	
		contentSelector.selectResultFromSeriesTab(1, results.get(0),4);
		
		point = new PointAnnotation(driver);
		point.mouseHover(point.getViewPort(1));
		point.movePoint(1, 1, 40, 60);		
		viewerpage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/2]","verifying that point is currently accepted and focused after movement");		
		
		}
	
	@Test(groups ={"Chrome", "IE11", "Edge","Positive" ,"DE2143"})
	public void test17_DE2143_TC8711_verifyCADFindingOnDeleteAll() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that finding remains selected after user rejects all the findings.");
	
		helper=new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(IHEMammoTestPatientName, 2);
	
		poly=new PolyLineAnnotation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		List<String> results = contentSelector.getAllResults();		
		
		contentSelector.selectResultFromSeriesTab(1, results.get(results.size()-1),4);
		
		point = new PointAnnotation(driver);
		
		point.deleteAllAnnotation(1);
		point.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[1/3]","verifying that first poly is rejected polyline");		
		point.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 2),"Checkpoint[2/3]","verifying that second poly is active rejected polyline");		
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1),"Checkpoint[3/3]","verifying that point is currently rejected and focused");		
		
	}

		
	@AfterMethod
	public void restoringCADSR() throws SQLException {

		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);
		db.deleteDrawnAnnotation(username_1);		
		db.deleteUser(username_1);
		db.deleteCloneFromSeriesLevelForCAD(resultDescription+"_"+username);
		
	}

}
