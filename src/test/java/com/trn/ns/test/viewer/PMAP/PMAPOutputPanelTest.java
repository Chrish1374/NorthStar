package com.trn.ns.test.viewer.PMAP;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

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
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PMAPOutputPanelTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private OutputPanel panel;
	private ContentSelector cs;
	static String patient_id="";
	private CircleAnnotation circle;
	private MeasurementWithUnit line;
	private ViewerSendToPACS sd;
	private HelperClass helper;
	private PMAP pmap;
	private ViewerLayout layout;

	String filePath1 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);

	String filePath2 =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 =Configurations.TEST_PROPERTIES.get("S2008-3CTP_Filepath");
	String s2008PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filepath4 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath4);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");



	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();	

	}

	@Test(groups ={"Chrome","Edge","IE11","US1047","US1757","DR2280","Positive","BVT","E2E","F884"})
	public void test01_US1047_TC6264_TC6265_TC6267_US1757_TC8611_DR2280_TC9201_TC9204_verifyPMAPFindingInOutputPanel() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP findings is displayed in the output panel <br>"+
				"Verify image  displayed in the output panel when PMAP findings are available <br>"+
				"Verify when cine works on PMAP findings series displayed in the OutputPanel <br>"+
				"Re-Execute TC6265- Verify image  displayed in the output panel when PMAP findings are available. <br>"+
				"[Risk and Impact] US1047 - TC6264 <br>"+
				"[Risk and Impact] US1742- TC8747");

	
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientID, 1);

		panel=new OutputPanel(driver);
		panel.closeNotification();
		panel.assertFalse(panel.isElementPresent(panel.getLossyOverlay(1)), "Checkpoint[1/7]", "Verified that Lossy is not present on viewer when zoom % is more than 100.");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.compareElementImage(protocolName,panel.thumbnailList.get(0), "Checkpoint[2/7]: Verified thumbnail image for the PMAP result", "test09_PMAP_Display");
		panel.scrollToImage(1, 1);
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.compareElementImage(protocolName,panel.thumbnailList.get(0), "Checkpoint[3/7]:Verified middle slice of the image for PMAP result in Output panel.", "test09_PMAP_Display");

		String newImagePath= Configurations.TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ Configurations.TEST_PROPERTIES.get("Browser") + "/" + protocolName;

		//verify thumbnail image after start of cine
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test09_cineplayImage_expected.png");
		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test09_cineplayImage_actual.png");

		String expectedImagePath = newImagePath+"/goldImages/test09_cineplayImage_expected.png";
		String actualImagePath = newImagePath+"/goldImages/test09_cineplayImage_actual.png";
		String diffImagePath = newImagePath+"/goldImages/test09_cineplayImage_diff.png";

		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertFalse(cpStatus, "Checkpoint[4/7] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[5/7] Verify presence of jump icon while cine is working", "verifying the jump to icon is not displayed when cine is working");

		//verify thumbnail image after cine stopped
		panel.stopCineOnThumbnail(1);		
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test09_cineplay_stopped_expected.png");
		panel.waitForTimePeriod(2000);
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test09_cineplay_stopped_actual.png");	

		expectedImagePath = newImagePath+"/goldImages/test09_cineplay_stopped_expected.png";
		actualImagePath = newImagePath+"/goldImages/test09_cineplay_stopped_actual.png";
		diffImagePath = newImagePath+"/goldImages/test09_cineplay_stopped_diff.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[6/7] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[7/7]", "verifying the jump to icon is displayed when cine is stopped");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1047","Positive"})
	public void test02_US1047_TC6268_TC6271_TC6273_verifyJumpToFunctionalityForPAMPResult() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to click on the JumpToIcon in thumbnail of PMAP series in the outputpanel when PMAP series is opened in viewer.<br>"
				+ "Verify that  PMAP result is getting loaded on viewer when clicked performed on thumbnail when PMAP result is not loaded. <br>"
				+ "Verify the SendToPacs for PMAP findings  when data is loaded and no modification made.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientID, 1);


		panel=new OutputPanel(driver);
		panel.closeNotification();	
		pmap=new PMAP(driver);
		sd=new ViewerSendToPACS(driver);

		String series = panel.getSeriesDescriptionOverlayText(1);
		cs=new ContentSelector(driver);
		String seriesToSelect=panel.getSeriesDescriptionOverlayText(1);
		String result=cs.getAllResults().get(0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		panel.enableFiltersInOutputPanel(false, false, true);
		//verify jump to icon functionality
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump to functionality for PMAP when PMAP result is already loaded on viewer." );
		panel.clickOnJumpIcon(1);
		panel.assertFalse(panel.isElementPresent(panel.outputPanelMinimizeIcon), "Checkpoint[1/7]", "Verified that Output panel closed after click on Jump icon");
		panel.assertTrue(panel.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[2/7]", "Verified that PMAP result series is loaded on viewer after click on jump");

		//verify jump functionality when PMAP result not loaded
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP source series is loaded properly after selecting from content selector" );
		cs.selectSeriesFromSeriesTab(1, seriesToSelect);
		panel.assertFalse(panel.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[3/7]", "Verified that PMAP source series is loaded on viewer from content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump to functionality for PMAP when PMAP result is not already loaded on viewer." );
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(1);
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+series+"\" " + ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[5/5]", "Verified warning message when thumbnail corresponding slice is not open in active view ");	
		panel.openAndCloseOutputPanel(false);
		panel.assertFalse(panel.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[5/7]", "Verified that PMAP result series is loaded on viewer after click on jump");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verified send to PACS functionality for PMAP when no modification is done" );
		sd.openSendToPACSMenu(true,false, false, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "One finding is sent to PACS ( 1 as pending )"), "Checkpoint[6.1/7]", "Verified that PMAP result sent to PACS without any modication");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[6.2/7]","Verified error in sending result notification.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verified send to PACS functionality when PMAP result is in accepted state." );
		cs.selectResultFromSeriesTab(1, result);
		panel.mouseHover(panel.acceptRejectToolbar);
		panel.selectAcceptfromGSPSRadialMenu();

		sd.openSendToPACSMenu(true,true, false, true);
		panel.click(sd.sendToPacs);
		panel.waitForElementVisibility(panel.notificationUI);
		String message1=panel.getText(panel.notificationUI);
		panel.assertEquals(message1, "One finding is sent to PACS ( 1 as accepted )", "Checkpoint[7/7]", "Verified that PMAP result sent to PACS after changing the state of PMAP to accepted");

	}	

	@Test(groups ={"Chrome","Edge","IE11","DE1631","Positive","BVT"})
	public void test03_DE1631_TC6821_TC6823_VerifyStateOfPMAPAfterPerformingSendTOPacs() throws InterruptedException, SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of Pmap series changes in viewer on performing SendToPacs and Accept All or Reject All findings <br>"+
				"Verify finding state of PMAP data persists in DB when user accepts or rejects the finding and performs 'Send to PACS'");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientID, 1);


		panel = new OutputPanel(driver);
		db=new DatabaseMethods(driver);
		panel.waitForViewerpageToLoad(1);		
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);
		panel.closeNotification();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		String resultToSelect=cs.getAllResults().get(0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result state in viewer and in Database");
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/5]", "Verified that PMAP result is in pending state on viewer page.");
		panel.assertTrue(db.getUserFeedbackUsingPatientID(pmapPatientID, resultToSelect).isEmpty(), "Checkpoint[2/5]","Verified that PMAP result is in pending state in User feedback table.");

		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true, false, false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "One finding is sent to PACS ( 1 as accepted )"), "Checkpoint[3.1/5]", "Verified that PMAP result sent to PACS without any modication");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[3.2/5]","Verified error in sending result notification.");

		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		panel.assertEquals(acceptedCount, 1, "Checkpoint[4/5]", "Verified that notification UI after selecting Accept All from Pending finding dialog for PMAP result");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result state in viewer and in Database after send to PACS for Pending findings.");
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[5.1/5]", "Verified that PMAP result is in Accepted state on viewer");
		panel.assertEquals(db.getUserFeedbackUsingPatientID(pmapPatientID, resultToSelect),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT, "Checkpoint[5.2/5]","Verified that PMAP result is in Accepted state in user feedback table");

	}

	@Test(groups ={"Chrome","Edge","IE11","DE1631","Positive","BVT"})
	public void test04_DE1631_TC6822_TC6824_VerifyStateOfPMAPAfterAcceptAllFromOutputPanel() throws InterruptedException, SQLException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of PMAP series in the Viewer on accepting or rejecting the finding from the Output Panel <br>"+
				"Verify finding state of PMAP data persists in DB when user accepts or rejects the finding through Output Panel");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientID, 1);


		panel = new OutputPanel(driver);
		circle=new CircleAnnotation(driver);
		panel.waitForViewerpageToLoad(1);				
		cs = new ContentSelector(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		String resultToSelect=cs.getAllResults().get(0);

		panel.closeNotification();
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		String seriesToSelect=panel.getSeriesDescriptionOverlayText(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result state in Database after drawing circle annotation on source series");
		cs.selectSeriesFromSeriesTab(3, seriesToSelect);
		circle.selectCircleFromQuickToolbar(3);
		circle.drawCircle(3, 5, 5,-80,-80);
		circle.selectAcceptfromGSPSRadialMenu();

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[1/4]", "Verified finding count from Output panel after drawing circle annotation");
		panel.assertTrue(db.getUserFeedbackUsingPatientID(pmapPatientID, resultToSelect).isEmpty(), "Checkpoint[2/4]","Verified that PMAP result is in pending state in user feedback table");

//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result state in viewer and in Database after selecting Reject All from machine filter");
//		panel.click(panel.rejectAllForMachine.get(0));
//		panel.openAndCloseOutputPanel(false);
//		panel.click(panel.getViewPort(1));
//		panel.mouseHover(panel.getGSPSHoverContainer(1));
//		panel.assertTrue(panel.verifyRejectGSPSRadialMenu(), "Checkpoint[3/4]", "Verified that PMAP result is in Rejected on viewer page");
//		panel.assertEquals(db.getUserFeedbackUsingPatientID(pmapPatientID, resultToSelect),NSDBDatabaseConstants.LOGENTRYSTATUSREJECT, "Checkpoint[4/4]","Verified that PMAP result is in Rejected state in user feedback table");

	}

//	@Test(groups ={"Chrome","Edge","IE11","DR2001","DR2178","Positive"})
//	public void test05_DR2001_TC8230_TC8232_DR2178_TC8772_verifyPMAPThumbnailAndCreatorInformation() throws InterruptedException, ParseException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that pmap thumbnail is visible in Output panel. <br>"+
//				"[Risk and Impact]: Verify that PMAP is reloaded properly and created date, creator information is displayed correctly in output panel.<br>"+
//				"PMAP - Creation date in output panel is updated after a reload.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientUsingID(pmapPatientID);
//		patientPage.clickOntheFirstStudy();
//
//		panel=new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();		
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
//		SimpleDateFormat dateFormat=new SimpleDateFormat(ViewerPageConstants.STANDARDDATEFORMAT);
//
//		cs = new ContentSelector(driver);
//
//		cs.openAndCloseSeriesTab(true);
//		String csMachineName=cs.getText(cs.allMachineName.get(0));
//		//String csMachineDate=panel.getText(cs.machineGroupLableheader);
//
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.compareElementImage(protocolName,panel.thumbnailList.get(0), "Checkpoint[1/5]: Verified thumbnail image for the PMAP result", "test09_PMAP_Display");
//
//		//panel.assertEquals(panel.getText(panel.createdByUserList.get(0)), ViewerPageConstants.CREATED_BY_TEXT+" "+csMachineName,"Checkpoint[2/5]", "Verified the created by information");
//
//
//	//	Date date = dateFormat.parse(csMachineDate);
//		SimpleDateFormat opDateFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		//panel.assertEquals(panel.getText(panel.createdOnDateList.get(0)), ViewerPageConstants.CREATED_ON_TEXT+" "+opDateFormat.format(date), "Checkpoint[3/5]", "Verified the created on information");
//
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer("", pmapPatientID, 1, 1);
//
//		panel.enableFiltersInOutputPanel(false, false, true);
//
////		panel.assertEquals(panel.getText(panel.createdByUserList.get(0)), ViewerPageConstants.CREATED_BY_TEXT+" "+csMachineName,"Checkpoint[4/5]", "Verified the created by information");
//	//	panel.assertEquals(panel.getText(panel.createdOnDateList.get(0)), ViewerPageConstants.CREATED_ON_TEXT+" "+opDateFormat.format(date), "Checkpoint[5/5]", "Verified the created on information");
//
//	}

	@Test(groups ={"Chrome","Edge","IE11","DR2189","Positive"})
	public void test06_DR2189_TC8766_verifyPMAPFindingInOutputPanel() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP is displayed on Output Panel thumbnail for Covid-003 patient.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(covidPatientName, 1);


		panel=new OutputPanel(driver);
		panel.closeNotification();		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+covidPatientName+" in viewer and verify the thumbnail" );
		panel.enableFiltersInOutputPanel(false, false, true);
		for(int i=0;i<panel.thumbnailList.size();i++)
			panel.compareElementImage(protocolName,panel.thumbnailList.get(i), "Checkpoint[1/4]: Verified thumbnail image for the PMAP result", "test06_0"+i);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "re-loading the Patient "+covidPatientName+" in viewer and verify the thumbnail" );
		helper.browserBackAndReloadViewer(covidPatientName, 1, 1);

		panel.enableFiltersInOutputPanel(false, false, true);
		for(int i=0;i<panel.thumbnailList.size();i++)
			panel.compareElementImage(protocolName,panel.thumbnailList.get(i), "Checkpoint[2/4]: Verified thumbnail image for the PMAP result", "test06_0"+i);
		panel.openAndCloseOutputPanel(false);

		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting the first result from CS in viewer and verify the thumbnail" );
		cs.selectResultFromSeriesTab(1, results.get(0), 1);
		panel.enableFiltersInOutputPanel(false, false, true);
		for(int i=0;i<panel.thumbnailList.size();i++)
			panel.compareElementImage(protocolName,panel.thumbnailList.get(i), "Checkpoint[3/4]: Verified thumbnail image for the PMAP result", "test06_0"+i);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting the second result from CS in viewer and verify the thumbnail" );
		helper.browserBackAndReloadViewer(covidPatientName, 1, 1);
		cs.selectResultFromSeriesTab(1, results.get(0), 2);
		panel.enableFiltersInOutputPanel(false, false, true);
		for(int i=0;i<panel.thumbnailList.size();i++)
			panel.compareElementImage(protocolName,panel.thumbnailList.get(i), "Checkpoint[4/4]: Verified thumbnail image for the PMAP result", "test06_0"+i);
		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups ={"Chrome","Edge","IE11","DR2204","Positive"})
	public void test07_DR2204_TC8766_verifyPMAPFindingInOutputPanel() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP and user drawn GSPS are displayed in Output Panel.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(covidPatientName, 1);


		panel=new OutputPanel(driver);
		panel.closeNotification();		

		cs = new ContentSelector(driver);
		pmap=new PMAP(driver);
		List<String> resultDesc = cs.getAllResults();

		panel.enableFiltersInOutputPanel(false, false, true);
		int thumbnails = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);

		panel.assertEquals(thumbnails, 2, "Checkpoint[1/14]", "verifying the results are displayed in OP");

		line = new MeasurementWithUnit(driver);
		line.selectDistanceFromQuickToolbar(1);
		line.drawLine(1, 100, -100, 100, 100);
		String seriesDesc = line.getSeriesDescriptionOverlayText(1);

		helper.browserBackAndReloadViewer(covidPatientName, 1, 1);
		panel.closeNotification();
		line.assertTrue(line.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[2/14]", "Line should be created and visble on reload");
		line.assertEquals(line.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[3/14]", "Viewer should be loaded with the same series.");
		line.assertFalse(line.isElementPresent(pmap.lutbar), "Checkpoint[4/14]", "Lut bar should not be present");

		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertEquals(panel.thumbnailList.size(),(thumbnails+1), "Checkpoint[5/14]", "verifying the one more thumbnail is displayed in OP");
		thumbnails = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		cs.selectSeriesFromSeriesTab(1, seriesDesc);

		circle = new  CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,100, -100, 100, 100);

		helper.browserBackAndReloadViewer(covidPatientName, 1, 1);
		panel.closeNotification();
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertEquals(panel.thumbnailList.size(),(thumbnails+1), "Checkpoint[6/14]", "verifying the circle is also displayed in OP");
		thumbnails = panel.thumbnailList.size();
		for(int i=thumbnails;i>1;i--) {
			panel.clickOnThumbnail(i);
			panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+seriesDesc+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[7/14]", "Verified warning message when thumbnail corresponding series is not open in active view ");		


		}
		panel.clickOnThumbnail(1);
		panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[8/14]", "OP is closed");
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Checkpoint[9/14]", "Verifying the Circle is now active and focused");

		cs.selectResultFromSeriesTab(1, resultDesc.get(0),2);
		cs.assertTrue(panel.isElementPresent(pmap.lutbar), "Checkpoint[10/14]", "Lut bar should be present");

		panel.mouseHover(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));

		panel.selectAcceptfromGSPSRadialMenu();
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),(thumbnails-1), "Checkpoint[11/14]", "verifying the PMAP result is moved from pending to accepted");

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[12/14]", "verifying there is only one finding in OP under pending tab");

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(thumbnails-1);
		panel.assertTrue(panel.isElementPresent(pmap.lutbar), "Checkpoint[13/14]", "Lut bar should be present post jump to icon click");

		helper.browserBackAndReloadViewer(covidPatientName, 1, 1);
		panel.closeNotification();
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),(thumbnails-1), "Checkpoint[14/14]", "verifying the findings are same on reload in OP");
		panel.openAndCloseOutputPanel(false);



	}

	@Test(groups ={"Chrome","Edge","IE11","DR2204","Positive"})
	public void test08_DE2204_TC8830_VerifySendToPACSAndGSPS() throws InterruptedException, SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the SendToPacs for PMAP finding and GSPS.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientUsingID(pmapPatientID);

		String studyDesc = patientPage.getText(patientPage.studyDescriptionList.get(0));
		String modality = patientPage.getText(patientPage.modalityListOnStudyPage.get(0));

		patientPage.clickOntheFirstStudy();

		panel = new OutputPanel(driver);
		db=new DatabaseMethods(driver);
		panel.waitForViewerpageToLoad(1);		
		cs=new ContentSelector(driver);
		sd=new ViewerSendToPACS(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		String resultToSelect=cs.getAllResults().get(0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result state in viewer and in Database");
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/8]", "Verified that PMAP result is in pending state on viewer page.");
		panel.assertTrue(db.getUserFeedbackUsingPatientID(pmapPatientID, resultToSelect).isEmpty(), "Checkpoint[2/8]","Verified that PMAP result is in pending state in User feedback table.");

		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectAcceptfromGSPSRadialMenu();

		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
		panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "One finding is sent to PACS ( 1 as accepted )"), "Checkpoint[3.1/7]", "Verified that PMAP result sent to PACS without any modication");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[3.2/7]","Verified error in sending result notification.");

		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		panel.assertEquals(acceptedCount, 1,"Checkpoint[3.3/8]", "Verified that notification UI after selecting Accept All from Pending finding dialog for PMAP result");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result state in viewer and in Database after send to PACS for Pending findings.");
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[4/8]", "Verified that PMAP result is in Accepted state on viewer");
		panel.assertEquals(db.getUserFeedbackUsingPatientID(pmapPatientID, resultToSelect),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT, "Checkpoint[5/8]","Verified that PMAP result is in Accepted state in user feedback table");

		sd = new ViewerSendToPACS(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the orthanc server for pending findings");
		sd.assertTrue(sd.verifyOrthancEntry("",pmapPatientID, studyDesc,  1,resultToSelect,modality,true, false, false),"Checkpoint[6/8]","Verifying the orthanc entry after accepting the pmap");

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -100, 100, 100);

		String clone=cs.getAllResults().get(0);
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getViewPort(1));

		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectRejectfromGSPSRadialMenu();

		panel.enableFiltersInOutputPanel(true, false, false);
		acceptedCount =panel.thumbnailList.size();
		panel.enableFiltersInOutputPanel(false, true, false);
		int rejectedCount =panel.thumbnailList.size();
		String message=(acceptedCount+rejectedCount)+" findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected )";

		panel.openAndCloseOutputPanel(false);
        sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
        panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS,message), "Checkpoint[7.1/8]", "Verified that notification UI after selecting Accept All from Pending finding dialog for PMAP result");
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"Checkpoint[7.2/8]","Verified error in sending result notification.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the orthanc server for pending findings");
		sd.assertTrue(sd.verifyOrthancEntries(clone+"_Accepted",resultToSelect+"_Accepted",resultToSelect+"_Rejected"),"Checkpoint[8/8]","Verifying the orthanc entry after accepting the pmap");



	}

	@Test(groups ={"Chrome","Edge","IE11","DR2386","Positive"})
	public void test09_DR2386_TC9408_verifyPMAPInOutputPanel() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PMAP is getting displayed properly in Output panel thumbnail.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(covidPatientName, 1);

		panel=new OutputPanel(driver);
		cs = new ContentSelector(driver);
		List<String> resultDesc = cs.getAllResults();
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/";
		String actualImagePath = newImagePath+"/actualImages/";
		String diffImagePath = newImagePath+"/actualImages/";
		
		panel.enableFiltersInOutputPanel(false, false, true);
		int thumbnails = panel.thumbnailList.size();
		for(int i=0;i<thumbnails;i++)
			panel.takeElementScreenShot(panel.thumbnailList.get(i), expectedImagePath+resultDesc.get(i)+"_"+i+".png");
		panel.openAndCloseOutputPanel(false);

		for(int j=0;j<resultDesc.size();j++) {
			cs.selectResultFromSeriesTab(1, resultDesc.get(j),j+1);
			panel.enableFiltersInOutputPanel(false, false, true);
			thumbnails = panel.thumbnailList.size();
			panel.assertEquals(thumbnails, resultDesc.size(), "Checkpoint[1/4]", "verifying the results are displayed in OP");
			
			for(int i=1;i<=thumbnails;i++) {
				panel.assertTrue(panel.verifyThumbnailInOutputPanel(i), "Checkpoint[2/4]", "Verifying the thumbnail is getting loaded");
				panel.takeElementScreenShot(panel.thumbnailList.get(i-1), actualImagePath+resultDesc.get(i-1)+"_"+(i-1)+".png");
				boolean cpStatus =  panel.compareimages(expectedImagePath+resultDesc.get(i-1)+"_"+(i-1)+".png", actualImagePath+resultDesc.get(i-1)+"_"+(i-1)+".png", diffImagePath+"diff_"+resultDesc.get(i-1)+"_"+(i-1)+".png");
				panel.assertTrue(cpStatus, "Checkpoint[3/4] ","The actual and Expected image should be same after loading the OP");
				panel.playCineOnThumbnail(i);
				panel.waitForTimePeriod(1000);
				panel.mouseHover(panel.thumbnailList.get(i-1));
				panel.takeElementScreenShot(panel.thumbnailList.get(i-1), actualImagePath+resultDesc.get(i-1)+"_"+(i-1)+".png");
				cpStatus =  panel.compareimages(expectedImagePath+resultDesc.get(i-1)+"_"+(i-1)+".png", actualImagePath+resultDesc.get(i-1)+"_"+(i-1)+".png", diffImagePath+"diff_"+resultDesc.get(i-1)+"_"+(i-1)+".png");
				panel.assertFalse(cpStatus, "Checkpoint[4/4] ","The actual and Expected image should not be same after playing cine");
			
				
			}
			
			panel.openAndCloseOutputPanel(false);
		}
	}



	@AfterMethod(alwaysRun=true)
	public void deleteOrthancEntry() {

		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		for(int i =0;i<RESTUtil.getJsonPath(response).getList("").size();i++) {
			patient_id = RESTUtil.getJsonPath(response).getList("").get(i).toString();
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
		}

	}	

}
