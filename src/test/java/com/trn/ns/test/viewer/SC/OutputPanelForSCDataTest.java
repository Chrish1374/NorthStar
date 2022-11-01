package com.trn.ns.test.viewer.SC;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class OutputPanelForSCDataTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	private OutputPanel panel;
	private MeasurementWithUnit lineWithUnit;
	private CircleAnnotation circle;
	private ContentSelector contentSelector;
	private EllipseAnnotation ellipse;
	private SimpleLine line;
	private PointAnnotation point ;
	private PolyLineAnnotation poly;
	private TextAnnotation textAnn;
	private PatientListPage patientPage;
	private ViewerLayout layout;
	private HelperClass helper;

	
	String comment="Sample Comment";
	String editedComment="Sample Comment edited";
	String seriesComment="Series Level comment";
	String seriesEditedComment="Series Level Edited comment";
	String username1 = "abc";
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	
	String filePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String imbio_filePath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbio_filePath);
	String pdfResult=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, imbio_filePath);
	String result=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, imbio_filePath);

	String doeLillyFilePath =Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String doeLillyPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, doeLillyFilePath);

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {

		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username ,password);

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1078","US1047","BVT"})
	public void test01_US1078_TC5600_TC5601_US1047_TC6414_verifyJumpToSCResultWhenReportIsLoaded() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  DICOM SC series is getting loaded on viewer when clicked performed on thumbnail when DICOM series is not loaded.<br>"
				+ "Verify that user is able to click on the JumpToIcon in thumbnail of SR when SR series is opened in viewer.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);
		
		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.selectAcceptfromGSPSRadialMenu();

		String resultDesc1 = panel.getSeriesDescriptionOverlayText(1);	
		String resultDesc2 = panel.getSeriesDescriptionOverlayText(2);	
		String seriesDesc= panel.getSeriesDescriptionOverlayText(3);
		int currentLayout = panel.getNumberOfCanvasForLayout();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump icon functionality when DICOM SC loaded in viewbox1" );
		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnThumbnail(1);

		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[1/5]", "Verifying nothing has changed on viewbox 1");	
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(2), resultDesc2, "Checkpoint[2/5]", "Verifying nothing has changed on viewbox 2");	
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(3), seriesDesc, "Checkpoint[3/5]", "Verifying nothing has changed on viewbox 3");
		panel.assertEquals( panel.getNumberOfCanvasForLayout(), currentLayout, "Checkpoint[4/5]", "Verifing the layouts");
		//panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[5/5]", "Verifying that output panel is closed");
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1078"})
	public void test02_US1078_TC5603_verifyJumpToScResultWhenReportIsnotLoaded() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Implement Jump to icon functionanlity when non-dicom series is opened from output panel and layout is in 1*2  or in 2*1.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);
		contentSelector=new ContentSelector(driver);

		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.selectAcceptfromGSPSRadialMenu();
		String resultDesc1 = panel.getSeriesDescriptionOverlayText(1);	
		String seriesDesc= panel.getSeriesDescriptionOverlayText(3);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump icon functionality when DICOM SC not loaded in any viewbox and Layout is 1*2 " );
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		panel.waitForAllChangesToLoad();

		contentSelector.selectSeriesFromSeriesTab(1, seriesDesc);
		contentSelector.openAndCloseSeriesTab(false);
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[1/8]", "Verifying DICOM SC not loaded in viewbox 1");	
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(2), resultDesc1, "Checkpoint[2/8]", "Verifying DICOM SC not loaded in viewbox 2");	

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnThumbnail(1);

		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[3/8]", "Verifying the jump to icon functionality");		
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[4/8]", "Verifying the SC data is loaded in first viewbox post click on jump to icon");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump icon functionality when DICOM SC not loaded in any viewbox and Layout is 2*1");
		layout.selectLayout(layout.twoByOneLayoutIcon);
		panel.waitForAllChangesToLoad();

		contentSelector.selectSeriesFromSeriesTab(1, seriesDesc);
		contentSelector.openAndCloseSeriesTab(false);
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[5/8]", "Verifying the jump to icon functionality");	
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(2), resultDesc1, "Checkpoint[6/8]", "Verifying DICOM SC not loaded in viewbox 2");	

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnThumbnail(1);

		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1), seriesDesc, "Checkpoint[7/8]", "Verifying the jump to icon functionality");		
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[8/8]", "Verifying the SC data is loaded in first viewbox post click on jump to icon");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1078"})
	public void test03_US1078_TC5604_verifyJumpToSCWhenReportIsAcceptedOrRejected() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to open the DICOM SC sereis from Output panel when DICOM SC is in Accepted and in Rejected state.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);

		String resultDesc1 = panel.getSeriesDescriptionOverlayText(1);	
		String resultDesc2 = panel.getSeriesDescriptionOverlayText(2);	
		String seriesDesc= panel.getSeriesDescriptionOverlayText(3);
		int currentLayout = panel.getNumberOfCanvasForLayout();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump icon functionality when DICOM SC is in accepted state " );

		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectAcceptfromGSPSRadialMenu();

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnThumbnail(1);

		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[1/12]", "Verifying nothing has changed on viewbox 1");	
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(2), resultDesc2, "Checkpoint[2/12]", "Verifying nothing has changed on viewbox 2");	
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(3), seriesDesc, "Checkpoint[3/12]", "Verifying nothing has changed on viewbox 3");
		panel.assertEquals( panel.getNumberOfCanvasForLayout(), currentLayout, "Checkpoint[4/12]", "Verifing the layouts");
		//panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[5/12]", "Verifying that output panel is closed");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[6/12]", "Verifying that output panel is closed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify jump icon functionality when DICOM SC is in rejected state " );
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectRejectfromGSPSRadialMenu();

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.clickOnThumbnail(1);

		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc1, "Checkpoint[7/12]", "Verifying nothing has changed on viewbox 1");	
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(2), resultDesc2, "Checkpoint[6/12]", "Verifying nothing has changed on viewbox 2");	
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(3), seriesDesc, "Checkpoint[9/12]", "Verifying nothing has changed on viewbox 3");
		panel.assertEquals( panel.getNumberOfCanvasForLayout(), currentLayout, "Checkpoint[10/12]", "Verifing the layouts");
	//	panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[11/12]", "Verifying that output panel is closed");
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[12/12]", "Verifying that output panel is closed");	
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1078"})
	public void test04_US1078_TC5605_verifyLogsWhenSCReportNotLoaded() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', 'UnloadSeries, 'LoadSeriesStart', 'LoadSeriesEnd' when DICOM SC series is not loaded in the view box.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);
		contentSelector = new ContentSelector(driver);

		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.selectAcceptfromGSPSRadialMenu();

		String resultDesc = panel.getSeriesDescriptionOverlayText(1);	
		String seriesDesc= panel.getSeriesDescriptionOverlayText(3);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		contentSelector.selectSeriesFromSeriesTab(1, seriesDesc);
		String changedSeriesDesc= panel.getSeriesDescriptionOverlayText(1);
		panel.assertTrue(contentSelector.verifyPresenceOfEyeIcon(seriesDesc), "Checkpoint[1/13]", "Verified that SC report is not loaded in first viwbox");
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc, "Checkpoint[2/13]", "Verifying the series after changing the SC report");

		DatabaseMethodsADB dba = new DatabaseMethodsADB(driver);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
	
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(1);

		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1), changedSeriesDesc, "Checkpoint[3/13]", "Verifying series loaded using CS is unloaded");		
		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1), resultDesc, "Checkpoint[4/13]", "verifying SC report is loaded post click on jump to icon");

		List<String> unloadSeriesPayload = dba.getPayload(ActionLogConstant.UNLOAD_SERIES);
		List<String> startSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START);
		List<String> endSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END);
		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);

		panel.assertEquals(unloadSeriesPayload.size(), 1, "Checkpoint[5/13]", "Verifying unload series payload");
		panel.assertEquals(startSeriesPayload.size(), 1, "Checkpoint[6/13]", "verifying the start series payload");	
		panel.assertEquals(endSeriesPayload.size(), 1, "Checkpoint[7/13]", "Verifying the end series payload");
		panel.assertEquals(thumbnailPayload.size(), 2, "Checkpoint[8/13]", "Verifying thumbnail payload");

		String mimetype = dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
		panel.assertEquals(mimetype,ActionLogConstant.APPLICATION_DICOM,"Checkpoint[9/13]","Verifying the MIME type in start series payload");
		mimetype = dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
		panel.assertEquals(mimetype,ActionLogConstant.APPLICATION_DICOM,"Checkpoint[10/13]","Verifying the MIME type in end series payload");

		String seriesLoadType =dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
		panel.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[11/13]","Verifying the from output panel flag in end series payload");
		seriesLoadType =dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
		panel.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[12/13]","verifying the output panel flag in start series payload");

		panel.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[13/13]","Verifying the isSeries loaded flag in thumbnail payload");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1078"})
	public void test05_US1078_TC5606_verifyLogsWhenSCReportIsLoaded() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', when series is loaded in any of the view box.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);

		panel.mouseHover(panel.getGSPSHoverContainer(2));
		panel.selectAcceptfromGSPSRadialMenu();

		String resultDescSC = panel.getSeriesDescriptionOverlayText(1);

		DatabaseMethodsADB dba = new DatabaseMethodsADB(driver);
		dba.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnThumbnail(1);

		panel.assertEquals(panel.getSeriesDescriptionOverlayText(1).trim(), resultDescSC.trim(), "Checkpoint[1/3]", "Verifying the SC report is loaded on click of jump to icon");		

		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);

		panel.assertEquals(thumbnailPayload.size(), 1, "Checkpoint[2/3]", "Verifying the thumbnail payload size");
		panel.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[3/3]","verifying the isSeriesLoaded flag in thumbnail payload");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1078"})
	public void test06_US1078_TC5612_verifyJumpToSCWhenGSPSAnnotationPresentOnSC() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', when series is loaded in any of the view box.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		
		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		lineWithUnit.selectRejectfromGSPSRadialMenu(1);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		panel.toggleOnOrOffResultUsingKeyboardGKey();

		panel.assertTrue(panel.isElementPresent(panel.resultApplied(1)), "Checkpoint[1/7]","Verified presence of Result applied text on secondary capture");
		panel.assertTrue(panel.getAllGSPSObjects(1).isEmpty(), "Checkpoint[2/7]", "Verified that there is not GSPS object present on Secondary capture data");
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[3/7]", "Result Applied is de-highlighted when SC report loaded in viewbox1");

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnThumbnail(1);

		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[4/7]", "Verified that circle annotation is accepted active GSPS on SC report");
		panel.assertFalse(panel.getAllGSPSObjects(1).isEmpty(), "Checkpoint[5/7]", "Verified that there is GSPS object present on Secondary capture data");
		panel.assertTrue(panel.isElementPresent(panel.resultApplied(1)), "Checkpoint[6/7]","Verified presence of Result applied text on secondary capture");
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Checkpoint[7/7]", "Result Applied is highlighted when SC report contaning GSPS annotation loaded in viewbox1");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1078"})
	public void test07_US1078_TC5614_verifyJumpToSCWhenSCWithGSPSNotLoadedInViewbox() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the JumpTo functionality on DICOM SC data when GSPS annotation drawn on DICOM SC view box not loaded in any viewbox");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);
		contentSelector=new ContentSelector(driver);

		String resultDesc = panel.getSeriesDescriptionOverlayText(1);	
		String seriesDesc= panel.getSeriesDescriptionOverlayText(3);
		String userCreatedResult=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX +username+"_1";

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		lineWithUnit.selectRejectfromGSPSRadialMenu(1);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		contentSelector.selectSeriesFromSeriesTab(1, seriesDesc);
		panel.assertFalse(contentSelector.verifyPresenceOfEyeIcon(userCreatedResult), "Checkpoint[1/6]", "Verified that viewbox-1 loaded with another series on which annotation are not present");
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(1),resultDesc, "Checkpoint[2/6]", "Verified that SC report not loaded in viewbox-1");
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(2),resultDesc, "Checkpoint[3/6]", "Verified that SC report not loaded in viewbox-2");
		panel.assertNotEquals(panel.getSeriesDescriptionOverlayText(3), resultDesc, "Checkpoint[4/6]", "Verified that SC report not loaded in viewbox-3");

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnThumbnail(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/6]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+resultDesc+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[6/6]", "Verified warning message when thumbnail corresponding slice is not open in active view for SC data");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1049"})
	public void test08_US1049_TC5574_verifyCineFunctionalityAndJumpToIconPresenceForSC() throws InterruptedException, ParseException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse actions on Secondary Capture  in Output Panel");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);
		panel = new OutputPanel(driver);

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.compareElementImage(protocolName, panel.thumbnailList.get(1), "Checkpoint[1/2]: Cine should start from the middle slice of the image stack if the DefaultSlicePosition value sets to 0.5.", "test08_01");

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/goldImages/test08_cineplayImage.png");

		boolean jumpToIconPresence = panel.playCineOnThumbnail(2);

		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/actualImages/test08_cineplayImage.png");		
		String expectedImagePath = newImagePath+"/goldImages/test08_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/test08_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_test08_cineplayImage.png";

		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");

		panel.stopCineOnThumbnail(2);		
		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/goldImages/test08_cineplayImageStopped.png");

		panel.waitForTimePeriod(2000);

		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/actualImages/test08_cineplayImageStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test08_cineplayImageStopped.png";
		actualImagePath = newImagePath+"/actualImages/test08_cineplayImageStopped.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_test08_cineplayImageStopped.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(1)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");



	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1049"})
	public void test09_US1049_TC5573_verifyCineFunctionalityAndJumpToIconPresence() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when cine works on SC images that spans multiple slices on series");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);
		panel = new OutputPanel(driver);

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.compareElementImage(protocolName, panel.thumbnailList.get(1), "Verifying the image is loaded from 30%", "test09_01");

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/goldImages/test09_cineplayImage.png");

		boolean jumpToIconPresence = panel.playCineOnThumbnail(2);

		panel.takeElementScreenShot(panel.thumbnailList.get(2-1), newImagePath+"/actualImages/test09_cineplayImage.png");		
		String expectedImagePath = newImagePath+"/goldImages/test09_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/test09_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_test09_cineplayImage.png";

		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");

		panel.mouseHover(panel.thumbnailList.get(2-1));		
		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/goldImages/test09_cineplayImageStopped.png");

		panel.waitForTimePeriod(2000);

		panel.takeElementScreenShot(panel.thumbnailList.get(1), newImagePath+"/actualImages/test09_cineplayImageStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test09_cineplayImageStopped.png";
		actualImagePath = newImagePath+"/actualImages/test09_cineplayImageStopped.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_test09_cineplayImageStopped.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(1)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");



	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1049"})
	public void test10_US1049_TC5574_verifyCineWhenDefaultLoadingIsAt30() throws InterruptedException, SQLException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse actions on Secondary Capture  in Output Panel");

		String percValue=".3";
		db = new DatabaseMethods(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the default slice position value to :"+percValue, "");
		db.updateDefaultSlicePosition(percValue);
		db.assertEquals(db.getDefaultSlicePosition(),percValue,"verifying the default slice position percentage is :"+percValue,"verified");
		db.resetIISPostDBChanges();

		Header header = new Header(driver);
		header.logout();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(2);

		panel.enableFiltersInOutputPanel(false, false, true);	
		int number =2;
		panel.compareElementImage(protocolName, panel.thumbnailList.get(number-1), "Verifying the image is loaded from 30%", "test10_01");
			

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/goldImages/test10_cineplayImage.png");

		boolean jumpToIconPresence = panel.playCineOnThumbnail(number);

		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/actualImages/test10_cineplayImage.png");		
		String expectedImagePath = newImagePath+"/goldImages/test10_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/test10_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_test10_cineplayImage.png";

		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");

		panel.mouseHover(panel.thumbnailList.get(number-1));		
		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/goldImages/test10_cineplayImageStopped.png");

		panel.waitForTimePeriod(2000);

		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/actualImages/test10_cineplayImageStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test10_cineplayImageStopped.png";
		actualImagePath = newImagePath+"/actualImages/test10_cineplayImageStopped.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_test10_cineplayImageStopped.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(number-1)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");



	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1049"})
	public void test11_US1049_TC5583_verifyCineFunctionalityForMultiPhase() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when cine works on SC MultiPhase images");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(doeLillyPatientName, 1);

		panel=new OutputPanel(driver) ;
	
		int number = 4;
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.compareElementImage(protocolName, panel.thumbnailList.get(number-1), "Verifying the image is loaded", "test11_01");

		//		panel.enableFiltersInOutputPanel(false, false, true);		

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/goldImages/test11_cineplayImage.png");

		boolean jumpToIconPresence = panel.playCineOnThumbnail(number);

		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/actualImages/test11_cineplayImage.png");		
		String expectedImagePath = newImagePath+"/goldImages/test11_cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/test11_cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_test11_cineplayImage.png";

		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");

		panel.mouseHover(panel.thumbnailList.get(number-1));		
		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/goldImages/test11_cineplayImageStopped.png");

		panel.waitForTimePeriod(2000);

		panel.takeElementScreenShot(panel.thumbnailList.get(number-1), newImagePath+"/actualImages/test11_cineplayImageStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test11_cineplayImageStopped.png";
		actualImagePath = newImagePath+"/actualImages/test11_cineplayImageStopped.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_test11_cineplayImageStopped.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(number-1)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");
	}

	//DE1358 :Console error seen while loading user drawn findings in output panel for Imbio-texture patient
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1358","DR2280"})
	public void test12_DE1358_TC5598_TC5599_DR2280_TC9204_verifyThumbnailInOPAfterDrawingGSPSOnSC() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that thumbnail images are getting loaded in Output panel on drawing the GSPS annotation on DICOM SC series view box.<br>"+
				"Verify that all types of  GSPS annotation are getting displayed in Output panel. <br>"+
				"[Risk and Impact] US1742- TC8747");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		
		contentSelector=new ContentSelector(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );

		contentSelector.assertFalse(contentSelector.isElementPresent(contentSelector.getLossyOverlay(1)), "Checkpoint[1/3]", "Verified that Lossy is not present on viewer when zoom % is more than 100.");
		line=new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,-10,10,250,10);

		point=new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);

		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		textAnn=new TextAnnotation(driver);
		String myText ="TextAnnotation_FirstViewbox";
		textAnn.drawText(1, 10, 10, myText);

		poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		panel.enableFiltersInOutputPanel(true, true, true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify no console error while loading thumbnail images in Output panel" );
		for(int i=0;i<panel.thumbnailList.size();i++)
		{   
			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.compareElementImage(protocolName, panel.thumbnailList.get(i), "Checkpoint[2."+i+"/3]: Verifying the thumbnail for GSPS and SC data", "test12_thumbnail_Image_"+i);
			panel.assertFalse(panel.isConsoleErrorPresent(), "Checkpoint[3."+i+"/3]", "Verfied that no console error receive and Thumbnail image loaded successfully for "+ (i+1));
		}

	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2631","F1082"})
	public void test13_DE2631_TC10374_verifyConsoleErrorForPdfWhenOPOpen() throws InterruptedException, SQLException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no console error is seen when 'Imbio_Texture_CTLung' or any patient data that has PDF is loaded on viewer keeping the Output Panel open.");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		panel.openAndCloseOutputPanel(true);
		panel.assertFalse(panel.isConsoleErrorPresent(ViewerPageConstants.CONSOLE_ERROR_MESSAGE), "Checkpoint[1/2]", "Verified that no console error present when PDF loaded on viewer.");
		
		ViewerToolbar viewerpage=new ViewerToolbar(driver);
		viewerpage.click(viewerpage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel.waitForPdfToRenderInViewbox(2);
		panel.assertFalse(panel.isConsoleErrorPresent(ViewerPageConstants.CONSOLE_ERROR_MESSAGE), "Checkpoint[2/2]", "Verified that no console error present when PDF reloaded on viewer.");
		

	}
	
	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException, IOException, InterruptedException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.USERFEEDBACKTABLE); 
		db.truncateTable(Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName"),ActionLogConstant.USER_ACTION_TABLE);
		db.deleteUser(username1);

		if(db.getDefaultSlicePosition().equals(".3")) {
			db.updateDefaultSlicePosition(".5");
			db.resetIISPostDBChanges();
		}
	}

}