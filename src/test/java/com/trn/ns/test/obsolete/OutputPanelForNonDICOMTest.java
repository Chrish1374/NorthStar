//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.apache.velocity.io.VelocityWriter;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.ThemeConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.ActionLogConstant;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.DatabaseMethodsADB;
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.OutputPanel_New;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.SimpleLine;
//import com.trn.ns.page.factory.ViewerLayout;
//import com.trn.ns.page.factory.ViewerOrientation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.page.factory.ViewerTextOverlays;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class OutputPanelForNonDICOMTest extends TestBase {
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private ViewerPage viewerPage;
//	private ContentSelector contentSelector;
//	private PointAnnotation point;
//	private ViewerPage viewerpage;
//	private HelperClass helper;
//	private ViewerLayout layout;
//	private ViewerOrientation orientation;
//	private ViewerTextOverlays textOverlay;
//
//	String nsdb = Configurations.TEST_PROPERTIES.get("dbName");
//	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
//	
//	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
//	String flag = "false";
//	DatabaseMethods db;
//	DatabaseMethodsADB dba;
//	
//	// Get Patient Name
//	
//	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
//
//	String IBL_JohnDoe =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
//	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IBL_JohnDoe);
//	
//	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
//	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);
//	String resultToSelect=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY,anonymous);
//	
//	String filePath = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
//	String CT_Lung_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//	private String firstSeriesDescriptionDoeLilly = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String secondSeriesDescriptionDoeLilly = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));		
//	private String firstResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String secondResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String thirdResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String fourthResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT04_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	
//	String anonymousFilePath = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
//	String anonymousPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymousFilePath);
//	
//	private CircleAnnotation circle;
//	private OutputPanel_New panel;
//	private ContentSelector cs;
//	int panelCommentCount = 4;
//	int pointCommentCount = 4;
//	String panelComment = "panel comment ";
//	String pointComment = "point";
//	
//	
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(username,password);
//
//	}
//
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "DE1302", "Positive","Sanity" })
////	public void test01_DE1302_TC5351_VerifyCreatedByForNonDICOMInOutputPanel() throws InterruptedException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify 'Created by' in output panel for non-DICOM(jpeg/png/pdf)");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(anonymous_Patient);
////	
////		patientListPage.mouseHover(patientListPage.getEurekaAIIcon(1));
////		patientListPage.waitForTimePeriod(1000);
////	
////		String tooltipHeader =patientListPage.getText(patientListPage.machineNameOnEurekaAl);
////		patientListPage.clickOntheFirstStudy();
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForViewerpageToLoad(2);
////		panel=new OutputPanel_New(driver) ;	
////		panel.enableFiltersInOutputPanel(true, true, true);		
////		panel.assertTrue(panel.getText(panel.createdByUserList.get(0)).contains(tooltipHeader), "Checkpoint:[1/2]", "Verifying the createdBy field for jpeg/png");
////		
////		helper=new HelperClass(driver);
////		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 1);
////		patientListPage.clickOnPatientRow(imbio_PatientName);
////
////		patientListPage.mouseHover(patientListPage.getEurekaAIIcon(1));
////		patientListPage.waitForTimePeriod(1000);
////		String tooltipHeader1=patientListPage.getText(patientListPage.machineNameOnEurekaAl);
////		patientListPage.clickOntheFirstStudy();
////		viewerPage.waitForViewerpageToLoad(3);
////		panel.enableFiltersInOutputPanel(true, true, true);
////		
////		panel.assertTrue(panel.getText(panel.createdByUserList.get(0)).contains(tooltipHeader1), "Checkpoint:[2/2]", "Verifying the createdBy field for pdf");
////		
////		
////	}
////
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////    public void test02_US1064_TC5204_verifyJumpIconLookAndFeelForNonDicom() throws InterruptedException  {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that user is able to click on the JumpToIcon in thumbnail of pdf.");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(imbio_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForPdfToRenderInViewbox(2);
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.acceptResult(1);
////		
////        panel.enableFiltersInOutputPanel(false, false, true);
////        panel.mouseHoverOnJumpIcon(1);
////	    viewerPage.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[1/1]: verifying the thumbnail for all the machine generated annotation", "test02_1");
////}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064","US1159","Positive","BVT" })
////	public void test03_US1064_TC5213_US1159_TC5793_verifyJumpIconForNonDicomWhenResultNotLoaded() throws InterruptedException, AWTException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that user is able to click on the JumpToIcon in thumbnail of pdf.");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(imbio_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.waitForPdfToRenderInViewbox(2);
////		contentSelector=new ContentSelector(driver);
////	
////		String scResultName=panel.getSeriesDescriptionOverlayText(1);
////		String resultName=panel.getResultDescriptionOverlayText(2);
////		String seriesName=panel.getSeriesDescriptionOverlayText(3);
////
////		contentSelector.selectSeriesFromSeriesTab(2, seriesName);
////		panel.assertTrue(contentSelector.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[1/4]", "Verified that new series loaded on viewbox-2");
////		panel.assertFalse(contentSelector.verifyPresenceOfEyeIcon(resultName), "Checkpoint[2/4]", "Verified that PDF is getting unloaded from viewer page");
////		
////		panel.acceptResult(1);
////
////        panel.enableFiltersInOutputPanel(false, false, true);
////        panel.clickOnThumbnail(1);
////        panel.waitForPdfToRenderInViewbox(1);
////        panel.assertEquals(panel.getResultDescriptionOverlayText(1), resultName, "Checkpoint[3/4]", "Verified that pdf series on which clicked performed get opened in first view box of Viewer.");
////        panel.assertNotEquals(panel.getResultDescriptionOverlayText(1), scResultName, "Checkpoint[4/4]", "Verified that existing series get unloaded from first viewbox");
////}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064","US1047", "Positive" })
////	public void test04_US1064_TC5221_TC5224_TC5226_TC5332_TC5223_US1047_TC6414_verifyJumpIconForJpegPng() throws  InterruptedException, SQLException, AWTException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that user is able to click on the JumpToIcon in thumbnail of JPG data. <br>"+
////		"Verify that user is able to click on the JumpToIcon in thumbnail of pdf when pdf series is opened in viewer.<br>"+
////		"Verify that user is able to click on the JumpToIcon in thumbnail of JPG data when the series is opened on viewer.<br>"+
////		"Verify that warning banner is not displayed when user clicks on the thumbnail of NONDICOM series which is not loaded <br>"+
////		"Verify the user Action log for  user action type 'ThumbnailJump', when series is loaded in any of the view box.");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + IBL_JohnDoe_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForPdfToRenderInViewbox(3);
////		contentSelector=new ContentSelector(driver);
////		OutputPanel panel = new OutputPanel_New(driver);
////		db= new DatabaseMethods(driver);
////		dba= new DatabaseMethodsADB(driver);
////
////		String jpegResultName=viewerPage.getResultDescriptionOverlayText(1);
////		String pdfResultName=viewerPage.getResultDescriptionOverlayText(3);
////		String seriesName=viewerPage.getSeriesDescriptionOverlayText(4);
////		
////        panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, jpegResultName));
////        viewerPage.assertEquals(viewerPage.getResultDescriptionOverlayText(1), jpegResultName, "Checkpoint[1/18]", "Verified that jpeg result on which clicked performed get opened in first view box of Viewer.");
////        viewerPage.assertFalse(viewerPage.isElementPresent(panel.outputPanelMinimizeIcon), "Checkpoint[2/18]", "OutputPanel closed after click on Jump Icon.");
////        
////        panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pdfResultName));
////        viewerPage.assertEquals(viewerPage.getResultDescriptionOverlayText(3), pdfResultName, "Checkpoint[3/18]", "Verified that PDF result on which clicked performed get opened in third view box of Viewer as PDF is already loaded in active viewbox");
////        viewerPage.assertNotEquals(viewerPage.getResultDescriptionOverlayText(1), pdfResultName, "Checkpoint[4/18]", "Verified that jpeg result present on first viewbox as it is");
////        viewerPage.assertFalse(viewerPage.isElementPresent(panel.outputPanelMinimizeIcon), "Checkpoint[5/18]", "OutputPanel closed after click on Jump Icon for PDF");
////        
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify jump functionality along with User action payload when PDF and JPEG not loaded on any of the viewbox ");
////		contentSelector.selectSeriesFromSeriesTab(1, seriesName);
////		contentSelector.selectSeriesFromSeriesTab(2, seriesName);
////		contentSelector.selectSeriesFromSeriesTab(3, seriesName);
////		
////		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[6/18]", "Verified that new series loaded on viewbox-2");
////		viewerPage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(pdfResultName), "Checkpoint[7/18]", "Verified that PDF is getting unloaded from viewbox");
////		viewerPage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(jpegResultName), "Checkpoint[8/18]", "Verified that Jpeg is getting unloaded from viewbox");
////
////		db.truncateTable(nsAnalyticsDBName, ActionLogConstant.USER_ACTION_TABLE);
////    	
////        panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pdfResultName));
////        List<String> thumbnailPayloadForPDF = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);
////      	viewerPage.assertEquals(thumbnailPayloadForPDF.size(), 1, "Checkpoint[9/18]", "Verifying the thumbnail payload size for PDF");
////      	viewerPage.assertEquals(dba.getKeyValue(thumbnailPayloadForPDF.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[10/18]","verifying the isSeriesLoaded flag for PDF");
////        viewerPage.assertEquals(viewerPage.getResultDescriptionOverlayText(1), pdfResultName, "Checkpoint[11/18]", "Verified that pdf result on which clicked performed get opened in first view box of Viewer.");
////    	viewerPage.assertNotEquals(viewerPage.getResultDescriptionOverlayText(1), jpegResultName, "Checkpoint[12/18]", "Verified that existing series get unloaded from first viewbox");   
////    	viewerPage.assertFalse(viewerPage.isElementPresent(panel.outputPanelMinimizeIcon), "Checkpoint[13/18]", "OutputPanel closed after click on Jump Icon.");
////    	
////		db.truncateTable(nsAnalyticsDBName, ActionLogConstant.USER_ACTION_TABLE);
////		
////        panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, jpegResultName));
////        List<String> thumbnailPayloadForJpeg = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);
////		viewerPage.assertEquals(thumbnailPayloadForJpeg.size(), 1, "Checkpoint[14/18]", "Verifying the thumbnail payload size for Jpeg");
////		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayloadForJpeg.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[15/18]","verifying the isSeriesLoaded flag for Jpeg");
////		
////    	viewerPage.assertEquals(viewerPage.getResultDescriptionOverlayText(1), jpegResultName, "Checkpoint[16/18]", "Verified that jpeg result on which clicked performed get opened in first view box of Viewer.");
////    	viewerPage.assertNotEquals(viewerPage.getResultDescriptionOverlayText(1), seriesName, "Checkpoint[17/18]", "Verified that existing PDF get unloaded from first viewbox");
////    	viewerPage.assertFalse(viewerPage.isElementPresent(panel.outputPanelMinimizeIcon), "Checkpoint[18/18]", "OutputPanel closed after click on Jump Icon.");	
////    	
////}
////
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////	public void test05_US1064_TC5222_TC5225_TC5332_verifyLogWhenReportLoadedForPNG() throws  InterruptedException, SQLException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that user is able to click on the JumpToIcon in thumbnail of PNG data. <br>"+
////		"Verify that user is able to click on the JumpToIcon in thumbnail of PNG data when PNG series is opened on viewer <br>"+
////		"Verify the user Action log for  user action type 'ThumbnailJump', when series is loaded in any of the view box.");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + anonymous_Patient + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(anonymous_Patient);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForViewerpageToLoad(2);
////		contentSelector=new ContentSelector(driver);
////		OutputPanel panel = new OutputPanel_New(driver);
////		db= new DatabaseMethods(driver);
////		dba= new DatabaseMethodsADB(driver);
////
////		String pngResultName=viewerPage.getResultDescriptionOverlayText(1);
////		String seriesName=viewerPage.getSeriesDescriptionOverlayText(2);
////		
////		db.truncateTable(nsAnalyticsDBName, ActionLogConstant.USER_ACTION_TABLE);
////        panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false,false,true,pngResultName));
////        List<String> thumbnailPayloadForPNG = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);
////		viewerPage.assertEquals(thumbnailPayloadForPNG.size(), 1, "Checkpoint[1/4]", "Verifying the thumbnail payload size");
////		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayloadForPNG.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[2/4]","verifying the isSeriesLoaded flag");
////		
////    	viewerPage.assertEquals(viewerPage.getResultDescriptionOverlayText(1), pngResultName, "Checkpoint[3/4]", "Verified that pdf series on which clicked performed get opened in first view box of Viewer.");
////    	viewerPage.assertNotEquals(viewerPage.getResultDescriptionOverlayText(1), seriesName, "Checkpoint[4/4]", "Verified that existing series get unloaded from first viewbox");	
////	
////}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////	public void test06_US1064_TC5323_verifyLogWhenReportNotLoadedForPNG() throws  InterruptedException, SQLException, AWTException{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', 'UnloadSerie's, 'LoadSeriesStart', 'LoadSeriesEnd' when series is not loaded in the view box for PNG data");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + anonymous_Patient + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(anonymous_Patient);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForViewerpageToLoad(2);
////		contentSelector=new ContentSelector(driver);
////		OutputPanel panel = new OutputPanel_New(driver);
////		db= new DatabaseMethods(driver);
////		dba= new DatabaseMethodsADB(driver);
////
////		String pngResultName=viewerPage.getResultDescriptionOverlayText(1);
////		String seriesName=viewerPage.getSeriesDescriptionOverlayText(2);
////		
////		contentSelector.selectSeriesFromSeriesTab(1, seriesName);
////		db.truncateTable(nsAnalyticsDBName, ActionLogConstant.USER_ACTION_TABLE);
////		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[1/11]", "Verified that new series loaded on viewbox-2");
////		viewerPage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(pngResultName), "Checkpoint[2/11]", "Verified that png result is getting unloaded from viewer page");
////	
////	    panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pngResultName));
////		List<String> unloadSeriesPayload = dba.getPayload(ActionLogConstant.UNLOAD_SERIES);
////		List<String> startSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START);
////		List<String> endSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END);
////		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);
////		
////		viewerPage.assertEquals(unloadSeriesPayload.size(), 1, "Checkpoint[3/11]", "Verifying unload series payload");
////		viewerPage.assertEquals(startSeriesPayload.size(), 1, "Checkpoint[4/11]", "verifying the start series payload");	
////		viewerPage.assertEquals(endSeriesPayload.size(), 1, "Checkpoint[5/11]", "Verifying the end series payload");
////		viewerPage.assertEquals(thumbnailPayload.size(), 2, "Checkpoint[6/11]", "Verifying thumbnail payload for png data set");
////		
////		String mimetype = dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
////		viewerPage.assertEquals(mimetype,ActionLogConstant.IMAGE_PNG,"Checkpoint[7/11]","Verifying the MIME type in start series payload");
////		mimetype = dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
////		viewerPage.assertEquals(mimetype,ActionLogConstant.IMAGE_PNG,"Checkpoint[8/11]","Verifying the MIME type in end series payload ");
////
////		String seriesLoadType =dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
////		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[9/11]","Verifying the  output panel flag for end series payload");
////		seriesLoadType =dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
////		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[10/11]","verifying the output panel flag for start series payload");
////			
////		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[11/11]","Verifying the isSeries loaded flag");
////		
////		
////}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////	public void test07_US1064_TC5323_verifyLogWhenReportNotLoadedForJpeg() throws  InterruptedException, SQLException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', 'UnloadSerie's, 'LoadSeriesStart', 'LoadSeriesEnd' when series is not loaded in the view box for Jpeg data");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + anonymous_Patient + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(anonymous_Patient);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForViewerpageToLoad(2);
////		contentSelector=new ContentSelector(driver);
////		OutputPanel panel = new OutputPanel_New(driver);
////		db= new DatabaseMethods(driver);
////		dba= new DatabaseMethodsADB(driver);
////
////		String jpegResultName=contentSelector.getAllResults().get(1);
////		String seriesName=viewerPage.getSeriesDescriptionOverlayText(2);
////	
////		db.truncateTable(nsAnalyticsDBName, ActionLogConstant.USER_ACTION_TABLE);
////		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[1/11]", "Verified that new series loaded on viewbox-2");
////		viewerPage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(jpegResultName), "Checkpoint[2/11]", "Verified that Jpeg is getting unloaded from viewer page");
////		
////	    panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, jpegResultName));
////		List<String> unloadSeriesPayload = dba.getPayload(ActionLogConstant.UNLOAD_SERIES);
////		List<String> startSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START);
////		List<String> endSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END);
////		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);
////		
////		viewerPage.assertEquals(unloadSeriesPayload.size(), 1, "Checkpoint[3/11]", "Verifying unload series payload");
////		viewerPage.assertEquals(startSeriesPayload.size(), 1, "Checkpoint[4/11]", "verifying the start series payload");	
////		viewerPage.assertEquals(endSeriesPayload.size(), 1, "Checkpoint[5/11]", "Verifying the end series payload");
////		viewerPage.assertEquals(thumbnailPayload.size(), 2, "Checkpoint[6/11]", "Verifying thumbnail payload");
////		
////		String mimetype = dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
////		viewerPage.assertEquals(mimetype,ActionLogConstant.IMAGE_JPEG,"Checkpoint[7/11]","Verifying the MIME type for start series payload");
////		mimetype = dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
////		viewerPage.assertEquals(mimetype,ActionLogConstant.IMAGE_JPEG,"Checkpoint[8/11]","Verifying the MIME type for end series payload");
////
////		String seriesLoadType =dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
////		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[9/11]","Verifying the from output panel flag in end series payload");
////		seriesLoadType =dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
////		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[10/11]","verifying the output panel flag in start series payload");
////			
////		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[11/11]","Verifying the isSeries loaded flag");
////		
////		
////}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////	public void test08_US1064_TC5323_verifyLogWhenReportNotLoadedForPDF() throws InterruptedException, SQLException, AWTException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify the user Action log for  user action type 'ThumbnailJump', 'UnloadSerie's, 'LoadSeriesStart', 'LoadSeriesEnd' when series is not loaded in the view box for PDF data");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForPdfToRenderInViewbox(3);
////		contentSelector=new ContentSelector(driver);
////		OutputPanel panel = new OutputPanel_New(driver);
////		db= new DatabaseMethods(driver);
////		dba= new DatabaseMethodsADB(driver);
////
////		String seriesName=viewerPage.getSeriesDescriptionOverlayText(4);
////		String pdfResultName=viewerPage.getResultDescriptionOverlayText(3);
////		
////		contentSelector.selectSeriesFromSeriesTab(3, seriesName);
////		
////		db.truncateTable(nsAnalyticsDBName, ActionLogConstant.USER_ACTION_TABLE);
////        panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pdfResultName));
////		
////		List<String> unloadSeriesPayload = dba.getPayload(ActionLogConstant.UNLOAD_SERIES);
////		List<String> startSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START);
////		List<String> endSeriesPayload = dba.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END);
////		List<String> thumbnailPayload = dba.getPayload(ActionLogConstant.USER_ACTION_THUMBNAIL_JUMP);
////		
////		viewerPage.assertEquals(unloadSeriesPayload.size(), 1, "Checkpoint[1/9]", "Verifying unload series payload");
////		viewerPage.assertEquals(startSeriesPayload.size(), 1, "Checkpoint[2/9]", "verifying the start series payload");	
////		viewerPage.assertEquals(endSeriesPayload.size(), 1, "Checkpoint[3/9]", "Verifying the end series payload");
////		viewerPage.assertEquals(thumbnailPayload.size(), 1, "Checkpoint[4/9]", "Verifying thumbnail payload");
////		
////		String mimetype = dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
////		viewerPage.assertEquals(mimetype,ActionLogConstant.APPLICATION_PDF,"Checkpoint[5/9]","Verifying the MIME type in start series payload");
////		mimetype = dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_INFO,ActionLogConstant.MIME_TYPE);
////		viewerPage.assertEquals(mimetype,ActionLogConstant.APPLICATION_PDF,"Checkpoint[6/9]","Verifying the MIME type in end series payload");
////
////		String seriesLoadType =dba.getKeyValue(endSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
////		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[7/9]","Verifying the from output panel flag in end series payload");
////		seriesLoadType =dba.getKeyValue(startSeriesPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_OUTPUT_PANEL);
////		viewerPage.assertEquals(seriesLoadType,NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[8/9]","verifying the output panel flag in start series payload");
////			
////		viewerPage.assertEquals(dba.getKeyValue(thumbnailPayload.get(0), ActionLogConstant.IS_SERIES_LOADED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[9/9]","Verifying the isSeries loaded flag");
////		
////		
////}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive","BVT" })
////	public void test09_US1064_TC5292_verifyJumpFunctionalityForDICOMGSPS() throws InterruptedException, AWTException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that Jump to icon functionality is working properly for DICOM GSPS annotation when series is not laoded on viewbox");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.waitForViewerpageToLoad(4);
////		contentSelector=new ContentSelector(driver);
////		
////		circle=new CircleAnnotation(driver);
////
////		String jpegResultName=panel.getResultDescriptionOverlayText(1);
////		String seriesName=panel.getSeriesDescriptionOverlayText(4);
////		
////		contentSelector.selectSeriesFromSeriesTab(1, seriesName);
////		circle.selectCircleFromQuickToolbar(4);
////		circle.drawCircle(4, 10, 10, 50, 50);
////		
////		panel.doubleClick(panel.getViewPort(1));
////		
////		panel.assertEquals(panel.getNumberOfCanvasForLayout(), 1, "Checkpoint[1/4]", "Verified layout as 1*1 when perform double click on viewbox");
////	    panel.clickOnThumbnail(panel.getThumbnailForGivenResult(true, true, true, jpegResultName));
////	    
////	    panel.mouseHover(panel.getViewPort(1));
////	    panel.assertEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[2/4]", "Verified that Jpeg is getting loaded on first viewbox");
////	    panel.assertFalse(contentSelector.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[3/4]", "Verified that series is getting unloaded from viewer page on which annotation is drawn");
////		
////	    panel.clickOnThumbnail(panel.getThumbnailForGivenResult(true, true, true, ViewerPageConstants.CIRCLE_FINDING_NAME));
////	    panel.assertTrue(panel.getText(panel.warningMessage).contains(seriesName), "Checkpoint[4/4]", "Verified that warning message contains series name for GSPS drawn annotation in output panel");
////	
////		
////}
////
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////	public void test10_US1064_TC5315_verifyJumpFunctionalityForLayoutIsOneByTwo() throws InterruptedException, AWTException{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Implement Jump to icon functionanlity when non-dicom series is opened from output panel and layout is in 1*2  or in 2*1.");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + IBL_JohnDoe_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.waitForPdfToRenderInViewbox(3);
////		contentSelector=new ContentSelector(driver);
////		
////		layout=new ViewerLayout(driver);
////		layout.selectLayout(layout.oneByTwoLayoutIcon);
////
////		String jpegResultName=panel.getResultDescriptionOverlayText(1);
////		String pdfResultName=panel.getResultDescriptionOverlayText(3);
////		String seriesName=panel.getSeriesDescriptionOverlayText(4);
////		
////		layout.selectLayout(layout.oneByTwoLayoutIcon);
////		contentSelector.selectResultFromSeriesTab(1, pdfResultName);
////		contentSelector.selectSeriesFromSeriesTab(2, seriesName);
////		
////	    panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, jpegResultName));
////	    panel.assertEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[1/2]", "Verified that Jpeg is getting loaded on first viewbox when click on jump icon from Output panel and layout is 1*2");
////	    panel.assertNotEquals(panel.getResultDescriptionOverlayText(1),pdfResultName, "Checkpoint[2/2]", "Verified that PDF is getting unloaded from viewer page");	
////	}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1064", "Positive" })
////	public void test11_US1064_TC5293_verifyJumpFunctionalityForPDFWhenJPEGLoadedOnViewer() throws InterruptedException, SQLException, AWTException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify Jump to icon functionlaity working properly when PDF is selected from thumbnail and JPG/PNG series is already loaded on view box and vice versa");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + IBL_JohnDoe_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.waitForPdfToRenderInViewbox(3);
////		contentSelector=new ContentSelector(driver);
////		
////		String jpegResultName=panel.getResultDescriptionOverlayText(1);
////		String pdfResultName=panel.getResultDescriptionOverlayText(3);
////		
////		contentSelector.selectResultFromSeriesTab(1, jpegResultName);
////		panel.doubleClick(panel.getViewPort(1));
////		panel.assertEquals(panel.getNumberOfCanvasForLayout(), 1, "Checkpoint[1/6]", "Verified that layout loaded on viewer is 1*1");
////		panel.assertEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[2/6]", "Verified that jpeg result is loaded on first viewbox");	
////		
////		panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pdfResultName));
////		panel.mouseHover(panel.getViewPort(1));
////		panel.assertEquals(panel.getResultDescriptionOverlayText(1),pdfResultName, "Checkpoint[3/6]", "Verified that PDF is getting loaded on viewer page");
////		panel.assertNotEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[4/6]", "Verified that Jpeg series is getting unloaded from viewer page");	
////		
////		panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, jpegResultName));
////		panel.mouseHover(panel.getViewPort(1));
////		panel.assertEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[5/6]", "Verified that Jpeg is getting loaded on viewer page");
////		panel.assertNotEquals(panel.getResultDescriptionOverlayText(1),pdfResultName, "Checkpoint[6/6]", "Verified that PDF is getting unloaded from viewer page");	
////	}
////		
////	@Test(groups = { "Chrome", "IE11", "Edge", "DE1391", "Positive" })
////	public void test12_DE1391_TC5696_verifyOutputPanelWhenJumpToOnNonDICOM() throws InterruptedException, SQLException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify the 'Jump-To' functionality on JPG/PNG thumbnail in the output panel for the loaded series in the view box. - Happy Path");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + IBL_JohnDoe_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.waitForViewerpageToLoad(4);
////		contentSelector=new ContentSelector(driver);
////		
////		String jpegResultName=panel.getResultDescriptionOverlayText(1);
////		String pdfResultName=panel.getResultDescriptionOverlayText(3);
////		
////		panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, jpegResultName));
////		panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[1/10]", "Verifying that output panel is closed");
////		panel.assertTrue(panel.selectAndVerifyActiveViewbox(1), "Checkpoint[2/10]", "verifying the viewbox is highlighted");
////		panel.assertEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[3/10]", "Verified that Jpeg is getting loaded on viewer page");
////		panel.assertEquals(panel.getResultDescriptionOverlayText(3),pdfResultName, "Checkpoint[4/10]", "Verified that PDF is loaded on viewer page");	
////	
////		panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pdfResultName));
////		panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[5/10]", "Verifying that output panel is closed");
////		panel.assertEquals(panel.getResultDescriptionOverlayText(3),pdfResultName, "Checkpoint[6/10]", "Verified that PDF is getting loaded on viewer page");
////		panel.assertEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[7/10]", "Verified that Jpeg series is loaded on viewer page");	
////	
////		panel.doubleClick(panel.getViewPort(1));
////		panel.clickOnThumbnail(panel.getThumbnailForGivenResult(false, false, true, pdfResultName));
////		panel.assertFalse(panel.verifyOutputPanelIsOpened(), "Checkpoint[8/10]", "Verifying that output panel is closed");
////		panel.waitForPdfToRenderInViewbox(1);
////		panel.assertEquals(panel.getResultDescriptionOverlayText(1),pdfResultName, "Checkpoint[9/10]", "Verified that PDF is getting loaded on viewer page");
////		panel.assertNotEquals(panel.getResultDescriptionOverlayText(1),jpegResultName, "Checkpoint[10/10]", "Verified that Jpeg series is getting unloaded from viewer page");	
////	
////	}
////		
////	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1052"})
////	public void test13_US1052_TC5527_verifyCineFunctionalityAndJumpToIconPresence() throws InterruptedException{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that cine is working for PNG/JPG/BMP images when mouse hovered on the thumbnail that contains multiple images in the output panel.");
////
////		// Loading the patient on viewer
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(anonymous_Patient);
////
////		patientListPage.clickOntheFirstStudy();
////
////		panel = new OutputPanel_New(driver);
////		panel.waitForViewerpageToLoad(2);
////		
////		panel.enableFiltersInOutputPanel(false, false, true);		
////		
////		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "verifying the thumbnail images for all the legends","test13_1");	
////		
////		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
////		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test13_cineplayImage.png");
////		
////		boolean jumpToIconPresence = panel.playCineOnThumbnail(1);
////		
////		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test13_cineplayImage.png");		
////		String expectedImagePath = newImagePath+"/goldImages/test13_cineplayImage.png";
////		String actualImagePath = newImagePath+"/actualImages/test13_cineplayImage.png";
////		String diffImagePath = newImagePath+"/actualImages/diffImage_test13_cineplayImage.png";
////
////		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
////		panel.assertFalse(cpStatus, "Checkpoint[1/4] The actual and Expected image should not same","After cine images are changed");		
////		panel.assertFalse(jumpToIconPresence, "Checkpoint[2/4]", "verifying the jump to icon is not displayed when cine is working");
////		
////		panel.stopCineOnThumbnail(1);		
////		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test13_cineplayImageStopped.png");
////		
////		panel.waitForTimePeriod(2000);
////		
////		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test13_cineplayImageStopped.png");		
////		expectedImagePath = newImagePath+"/goldImages/test13_cineplayImageStopped.png";
////		actualImagePath = newImagePath+"/actualImages/test13_cineplayImageStopped.png";
////		diffImagePath = newImagePath+"/actualImages/diffImage_test13_cineplayImageStopped.png";
////
////		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
////		panel.assertTrue(cpStatus, "Checkpoint[3/4] The actual and Expected image should be same","Verifying the images are same once cine is stopped");		
////		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[4/4]", "verifying the jump to icon is displayed when cine is stopped");
////		
////
////		
////	}
////	
////	//DE1811: Non-Dicom images(jpeg, png, bmp) are not loaded on viewer and output panel.
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "DE1811", "Positive" })
////	public void test14_DE1811_TC7336_VerifyNonDicomImagesOnViewerAndOutputPanel() throws InterruptedException, AWTException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that the Non-Dicom images(jpeg, png, bmp) are loaded on viewer and output panel.");
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + anonymous_Patient + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(anonymous_Patient);
////		patientListPage.clickOntheFirstStudy();
////		panel=new OutputPanel_New(driver) ;	
////		panel.waitForViewerpageToLoad(2);
////
////		cs=new ContentSelector(driver);
////		layout=new ViewerLayout(driver);
////
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify non DICOM images on viewer page.");
////		layout.selectLayout(layout.twoByTwoLayoutIcon);
////		for(int i=1;i<=panel.getNumberOfCanvasForLayout();i++)
////		panel.assertTrue(panel.verifyNonDicomImageLoadedInViewer(i), "Checkpoint[1."+i+"/5]"," verifying images loaded on viewbox-"+(i));
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify non DICOM images on Output panel.");
////		panel.enableFiltersInOutputPanel(true, true, true);
////		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.PENDING_FINDING_COLOR,"Checkpoint[2/5]", "Verifying background color of Pending finding button");
////		
////		for(int i=0;i<panel.thumbnailList.size();i++)
////		{ 
////	    panel.scrollIntoView(panel.thumbnailList.get(i));
////		panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[3."+i+"/5]"," verifying  image loaded on thumbnail-"+(i+1));
////		}
////		
////		layout.selectLayout(layout.twoByOneLayoutIcon);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify non DICOM images after selecting result from content selector.");
////		cs.selectResultFromSeriesTab(1, resultToSelect);
////		panel.acceptResult(1);
////		panel.assertTrue(panel.verifyNonDicomImageLoadedInViewer(1), "Checkpoint[4/5]"," Verifying non-DICOM image loaded on viewbox-1 after selecting from CS.");
////		
////		panel.enableFiltersInOutputPanel(true, false, false);
////		panel.assertTrue(panel.verifyThumbnailInOutputPanel(1), "Checkpoint[5/5]","Verifying  image loaded on thumbnail-1 after selecting from Content selector.");	
////		
////	}
////	
////	@Test(groups = { "Chrome", "IE11", "Edge", "US1358", "Positive"})
////	public void test15_US1358_TC7063_verifyFilteringOfMachineAndUserFindingsForSC() throws InterruptedException{
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("SC data: Verify if comment filter is working correctly along with the A/R/P and Machine/User filters");
////
////		// Loading the patient on viewer
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(imbio_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		panel = new OutputPanel_New(driver);
////		panel.waitForViewerpageToLoad();
////		panel.waitForPdfToRenderInViewbox(2);
////			
////		createPointAndAddComment(3);
////	
////		point.closingWarningAndWaterMark();
////		
////		int whichThumbnail = panel.getThumbnailForGivenResult(false, false, true, panel.getSeriesDescriptionOverlayText(1));
////		
////		panel.mouseHover(panel.getViewPort(1));
////		
////		panel.enableFiltersInOutputPanel(false, false, true);
////		panel.addCommentFromOutputPanel(whichThumbnail, panelComment);
////		panel.openAndCloseOutputPanel(false);
////		
////		panel.enableFiltersInOutputPanel(true, true, true);		
////		int findings = panel.findingsNameTitleList.size();
////		panel.click(panel.filteringCommentIcon);
////		
////		panel.assertNotEquals(panel.findingsNameTitleList.size(), findings,"Checkpoint[1/6]","Findings are changed");
////		panel.assertEquals(panel.findingsNameTitleList.size(), 5,"Checkpoint[2/6]","Filtered output should be User/ machine findings with Accepted+rejected+pending status having comments added would get filtered out");
////		
////		
////		for(int i =1,j=5;i<=5;i++,j--)
////		{
////			panel.scrollIntoView(panel.commentsForFindings.get(i-1));
////			if(i>1)
////				panel.assertEquals(panel.getCommentsFromThumbnailinExpandMode(i), "Comment: "+pointComment+j,"Checkpoint[3."+(i+1)+"/6]", "Verifying the comment is displayed");
////			else
////				panel.assertEquals(panel.getCommentsFromThumbnailinExpandMode(i).trim(), "Comment: "+panelComment.trim(),"Checkpoint[3."+(i+1)+"/6]", "Verifying the comment is displayed");
////		}
////		
////		panel.openAndCloseOutputPanel(false);		
////		point.closeWaterMarkIcon(3);
////		
////		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejected the findings for SC");
////		point.selectPoint(3, 1);
////		for(int i =1;i<=4;i++) {			
////			point.selectRejectfromGSPSRadialMenu();
////		}
////		panel.enableFiltersInOutputPanel(false, true, true);
////		panel.click(panel.filteringCommentIcon);
////		
////		panel.assertNotEquals(panel.findingsNameTitleList.size(), findings,"Checkpoint[4/6]","Findings are changed");
////		panel.assertEquals(panel.findingsNameTitleList.size(), 4,"Checkpoint[5/6]","Filtered output should be User/ machine findings with Accepted+rejected+pending status having comments added would get filtered out");
////		
////		
////		for(int i =1,j=4;j<=1;j--)
////		{
////			panel.scrollIntoView(panel.commentsForFindings.get(i-1));
////			panel.assertEquals(panel.getCommentsFromThumbnailinExpandMode(i), "Comment: "+pointComment+j,"Checkpoint[6."+(i+1)+"/6]", "Verifying the comments");
////		}
////
////	}
////  	//DE1867: [Hardening] Dicom view box is not getting highlighted when a finding is selected from Output panel and finding panel.
////	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1867"})
////	public void test16_DE1867_TC7498_verifyViewboxBorderWhenFindingSelectedFromOutputPanel() throws InterruptedException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("[Risk and Impact]: Verify viewbox is getting highlight when a finding is selected from Output panel.");
////
////		// Loading the patient on viewer
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(imbio_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		panel=new OutputPanel_New(driver);
////		panel.waitForViewerpageToLoad(3);
////				
////		panel.enableFiltersInOutputPanel(false, false, true);
////		panel.clickOnJumpIcon(1);
////	    panel.assertFalse(panel.isElementPresent(panel.outputPanelSection), "Checkpoint[1/2]","Verified that Output panel closed after click on jump icon");
////		panel.assertTrue(panel.verifyBorderForActiveViewbox(2,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[2/2]", "Verified that viewbox-2 border in which PDF is loaded is active.");
////				
////			}
////	
////	//DE1395: [Hardening]-Output panel is not getting closed on clicking on PDF view box
////	@Test(groups ={"Chrome","IE11","Edge","DE1395","Sanity"})
////	public void test17_DE1395_TC5704_VerifyOutputPanelFunctionalityOnPDF()throws InterruptedException {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify the opened output panel is closed when clicked on the view box with the PDF <br>"+
////				"Verify pdf functionlaity on view box.");
////
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(IBL_JohnDoe_PatientName);
////		patientListPage.clickOntheFirstStudy();
////
////		OutputPanel panel = new OutputPanel_New(driver);
////		panel.waitForViewerpageToLoad(1);
////		
////		panel.mouseHover(panel.outputPanelDiv);
////
////
////		panel.enableFiltersInOutputPanel(false, false,true);
////		panel.waitForOutputPanelToLoad();
////
////		//Now click on the view box loaded with PDF report and check if output is getting closed 
////		panel.click(panel.pdfViewbox);
////		panel.assertFalse(panel.isElementPresent(panel.outputPanelMinimizeIcon),"Checkpoint[1/4]", "Verified Output panel is closed");
////
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verified one up functionality on PDF");
////		panel.doubleClick(panel.getPDFViewPort(3));
////		panel.waitForTimePeriod(3000);
////		panel.assertEquals(panel.getNumberOfCanvasForLayout(), 1, "Verify layout change to 1*1", "Verified");
////
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify restoring layout functionality on PDF");
////		panel.doubleClick((panel.getPDFViewPort(3)));
////		panel.waitForPdfToRenderInViewbox(3);
////		panel.assertEquals(panel.getNumberOfCanvasForLayout(),4, "Verify layout change to originl 2*2 layout", "Verified");
////
////		}
////		
////	//TC977-Content Selector to access other series and studies content - Edge Cases- Select Same Series
////	// Patient data = Doe Lilly
////	//Verify nothing happens when same series is selected from content selector on parent window
////	//	Test 4: Verify Content Selector with Same Series (Automated) 
////	@Test(groups = { "Chrome","firefox","US238"})
////	public void test18_US238_TC977_verifyContentSelectorBySelectingSameSeries() throws InterruptedException, AWTException  {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify content selector when same series is selected");
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+CT_Lung_PatientName+" in viewer" );
////		patientListPage = new PatientListPage(driver);
////
////		patientListPage.clickOnPatientRow(CT_Lung_PatientName);
////		patientListPage.clickOntheFirstStudy();	
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForViewerpageToLoad();
////		contentSelector = new ContentSelector(driver);
////
////		layout=new ViewerLayout(driver);
////		//change layout to 2X2
////		layout.selectLayout(layout.twoByTwoLayoutIcon);
////
////
////		//Select same series in first view box
////		contentSelector.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC4[1]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(secondSeriesDescriptionDoeLilly), "Checkpoint TC4[1]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstResultDescriptionDoeLilly), "Checkpoint TC4[1]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(secondResultDescriptionDoeLilly), "Checkpoint TC4[1]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC4[1]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(fourthResultDescriptionDoeLilly), "Checkpoint TC4[1]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////	}	
////
////	//TC978-Content Selector to access other series and studies content - Edge Cases-Double Click
////	// Patient data = Doe Lilly
////	//Verify content selector functionality after double click one up
////	//	Test 5: Verify Content Selector with Double Click (Automated) 
////	/**
////	 * @throws InterruptedException
////	 * @throws AWTException 
////	 */
////	@Test(groups = { "Chrome","firefox","US238"})
////	public void test19_US238_TC978_verifyContentSelectorOnDoubleClickOneUp() throws InterruptedException, AWTException  {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify content selector on double click one up");
////
////		//Loading the patient on viewer 
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+CT_Lung_PatientName+" in viewer" );
////
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(CT_Lung_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForPdfToRenderInViewbox(3);
////		contentSelector = new ContentSelector(driver);
////
////		layout=new ViewerLayout(driver);
////		//change layout to 2X2
////		layout.selectLayout(layout.twoByTwoLayoutIcon);
////
////
////		//Perform double click 1 up on first view box
////		viewerpage.doubleClickOnViewbox(1);
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC5[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC5[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////
////		//Again perform double click 1 up on first/same view box
////		viewerpage.doubleClickOnViewbox(1);
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectSeriesFromSeriesTab(1,secondSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(secondSeriesDescriptionDoeLilly), "Checkpoint TC5[3] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC5[3] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");					
////	}
////
////	//TC979-Content Selector to access other series and studies content - Edge Cases- Zoom/Pan
////	// Patient data = Doe Lilly
////	//Verify content selector functionality after applying zoom,PAN and distance measurement    
////	//	Test 6: Verify Content Selector with Zoom/Pan (Automated) 
////	@Test(groups = { "Chrome","firefox","US238"})
////	public void test20_US238_TC979_verifyContentSelectorAfterApplyingZOOMPanDM() throws InterruptedException, AWTException {
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription( "Verify content selector after applying zoom,PAN and distance measurement");
////
////		//Loading the patient on viewer 
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+CT_Lung_PatientName+" in viewer" );
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(CT_Lung_PatientName);
////
////		patientListPage.clickOntheFirstStudy();	
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForPdfToRenderInViewbox(3);		
////		contentSelector = new ContentSelector(driver);
////
////		// Right clicking on View box 1 and Clicking on Zoom Icon and apply zoom
////		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
////		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -50,-50);
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC6[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC6[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////
////		// Right clicking on View box 1 and Clicking on PAN Icon and apply PAN
////		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
////		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectResultFromSeriesTab(1,fourthResultDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(fourthResultDescriptionDoeLilly), "Checkpoint TC6[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC6[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
////
////		SimpleLine line = new SimpleLine(driver);
////		// Right clicking on View box 1 and Clicking on distance measurement Icon;
////		line.selectLineFromQuickToolbar(viewerpage.getViewPort(1));
////
////		//Draw linear distance
////		line.drawLine(1,0,0, 100, 0);
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC6[2]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////
////	}
////
////	//TC980-Content Selector to access other series and studies content - Edge Cases-Cine and Orientation
////	// Patient data = Doe Lilly
////	//Verify content selector functionality after applying cine and orientation change    
////	//	Test 7: Verify Content Selector with Cine and Orientation (Automated) 
////	@Test(groups = { "Chrome","firefox","US238"})
////	public void test21_US238_TC980_verifyContentSelectorAfterCineAndOrientationChange() throws InterruptedException, AWTException {
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify content selector after applying cine and orientation change ");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+CT_Lung_PatientName+" in viewer" );
////
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(CT_Lung_PatientName);
////
////		patientListPage.clickOntheFirstStudy();		
////		orientation=new ViewerOrientation(driver);
////		contentSelector = new ContentSelector(driver);
////		orientation.waitForViewerpageToLoad(1);	
////		orientation.waitForViewerpageToLoad(2);	
////		orientation.waitForPdfToRenderInViewbox(3);	
////		orientation.waitForViewerpageToLoad(5);	
////		orientation.waitForViewerpageToLoad(6);	
////		layout=new ViewerLayout(driver);
////		layout.selectLayout(layout.twoByTwoLayoutIcon);
////
////
////		//open a Radial Menu for Viewbox1 and Perform Cine operation
////		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));	
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC7[2] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC7[2] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");		
////
////		//Open a Radial Menu for Viewbox1 and stop Cine operation
////		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));
////
////		//Change orientation
////		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
////
////		//Select series other than  already displayed series and verify newly selected series is getting rendered
////		contentSelector.selectSeriesFromSeriesTab(1,secondSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(secondSeriesDescriptionDoeLilly), "Checkpoint TC7[3] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC7[3] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////	}
////
////	//TC986-Content Selector to access other series and studies content - Edge Cases-Measurement
////	// Patient data =  Doe_Lilly
////	//Verify content selector functionality after measurement is drawn   
////	//	Test 9: Verify Content Selector with Measurement (Automated) 
////	@Test(groups = { "Chrome","firefox","US238"})
////	public void test22_US238_TC986_verifyContentSelectorAfterMeasurement() throws InterruptedException, AWTException {
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify content selector functionality after measurement is drawn");
////
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+CT_Lung_PatientName+" in viewer" );
////
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(CT_Lung_PatientName);
////
////		patientListPage.clickOntheFirstStudy();
////
////		viewerpage = new ViewerPage(driver);
////		viewerpage.waitForPdfToRenderInViewbox(3);
////		contentSelector = new ContentSelector(driver);
////		SimpleLine line = new SimpleLine(driver);
////		layout=new ViewerLayout(driver);
////		layout.selectLayout(layout.twoByTwoLayoutIcon);
////		line.selectLineFromQuickToolbar(viewerpage.getViewPort(1));
////		
////		//Draw linear distance
////		viewerpage.dragAndReleaseOnViewerWithClick(0,0, 100, 0);
////		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Checkpoint TC8[1]:Verify the measurement drawn @ coordinates (0,0,100,0)","LinearDistanceMeasurementUS238_TC986");
////
////		contentSelector.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Verifying Checkbox appears next to newly displayed  series in viewbox 1", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
////		viewerpage.assertFalse(contentSelector.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Verifying Checkbox appears next to newly displayed  series in viewbox 1", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
////		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Checkpoint TC8[2]:Verify no measurement is drawn on first box","LinearDistanceMeasurementDisappearUS238_TC986");
////		}
////
////	//	Test 3: Verify Content Selector with Layout Change (Automated) 
////	@Test(groups = { "Chrome","firefox","US238"})
////	public void test23_US238_TC976_verifyContentSelectorOnLayout() throws InterruptedException, AWTException  {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription( "Verify content selector on layout change");
////		//Loading the patient on viewer 
////		String filePath = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
////		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
////
////		patientListPage = new PatientListPage(driver);
////		helper=new HelperClass(driver);
////		helper.loadViewerDirectly(PatientName, 1);
////
////		textOverlay=new ViewerTextOverlays(driver);
////
////		ContentSelector cs = new ContentSelector(driver);
////
////        layout=new ViewerLayout(driver);
////		
////		layout.selectLayout(layout.oneByOneLayoutIcon);
////		textOverlay.assertEquals(textOverlay.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying Layout change applied and result and image "+ "Checkpoint TC3[1]: content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");
////
////		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
////		cs.selectSeriesFromSeriesTab(1,firstSeriesDescriptionDoeLilly);
////		cs.assertTrue(cs.verifyPresenceOfEyeIcon(firstSeriesDescriptionDoeLilly), "Checkpoint TC3[2]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		cs.assertFalse(cs.verifyPresenceOfEyeIcon(fourthResultDescriptionDoeLilly), "Checkpoint TC3[2]: Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
////		cs.assertFalse(cs.verifyPresenceOfEyeIcon(secondSeriesDescriptionDoeLilly), "Checkpoint TC3[2]: Verifying series not displayed on any viewbox is not higlighted on content selector", "Verified series not displayed on any viewbox is not higlighted on content selector");
////		cs.assertFalse(cs.verifyPresenceOfEyeIcon(firstResultDescriptionDoeLilly), "Checkpoint TC3[2]: Verifying result not displayed on any viewbox is not higlighted on content selector", "Verified result not displayed on any viewbox is not higlighted on content selector");
////		cs.assertFalse(cs.verifyPresenceOfEyeIcon(secondResultDescriptionDoeLilly), "Checkpoint TC3[2]: Verifying result not displayed on any viewbox is not higlighted on content selector", "Verified result not displayed on any viewbox is not higlighted on content selector");
////		cs.assertFalse(cs.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC3[2]: Verifying series not displayed on any viewbox is not higlighted on content selector", "Verified series not displayed on any viewbox is not higlighted on content selector");
////
////		layout.selectLayout(layout.oneByOneLayoutIcon);
////		textOverlay.assertEquals(textOverlay.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Verifying Layout change applied and result and image "+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");
////
////				
////		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
////		cs.selectResultFromSeriesTab(1,firstResultDescriptionDoeLilly);
////		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
////		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(firstResultDescriptionDoeLilly), "Checkpoint TC3[3]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
////		viewerpage.assertFalse(cs.verifyPresenceOfEyeIcon(secondSeriesDescriptionDoeLilly), "Checkpoint TC3[3]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
////		viewerpage.assertFalse(cs.verifyPresenceOfEyeIcon(thirdResultDescriptionDoeLilly), "Checkpoint TC3[3]:Verifying Checkbox appears next to newly displayed  series in viewbox 1", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
////
////
////	}			
////	
////	@Test(groups ={"firefox","Chrome","IE11","Edge","DR2280","Positive"})
////	public void test24_DR2280_TC9195_TC9204_verifyConsoleForNonDicomData() throws InterruptedException, AWTException 
////
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("[Risk and Impact] DR2268 - TC9064");
////		
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatientName+"in viewer" );
////		patientListPage = new PatientListPage(driver);
////
////		helper=new HelperClass(driver);
////		viewerpage = helper.loadViewerDirectlyUsingID(anonymousPatientName, 2);
////		
////		panel=new OutputPanel_New(driver);
////	
////		panel.assertFalse(panel.isElementPresent(panel.getLossyOverlay(1)), "Checkpoint[1/5]", "Verified that Lossy is not present on viewer when zoom % is more than 100.");
////		
////		panel.closeBanner();
////		cs=new ContentSelector(driver);
////		List<String> result= cs.getAllResults();
////	
////		for(int i=0;i<result.size();i++)
////		{
////			cs.selectResultFromSeriesTab(1, result.get(i));
////			panel.assertTrue(panel.verifyNonDicomImageLoadedInViewer(1), "Checkpoint[2/5]"," Verifying non-DICOM image loaded on viewbox-1 after selecting from CS.");
////			panel.assertFalse(panel.isConsoleErrorPresent(),"Checkpoint[3."+(i+1)+"/5]","Verifying no console error present for resultName "+result.get(i));	
////		}
////	
////		for(int i=0;i<result.size();i++)
////		{
////		panel.enableFiltersInOutputPanel(false, false, true);
////	    panel.clickOnJumpIcon(panel.getThumbnailForGivenResult(result.get(i))+1);
////	    panel.assertTrue(panel.verifyNonDicomImageLoadedInViewer(1), "Checkpoint[4/5]"," Verifying non-DICOM image loaded on viewbox-1 after click on jump icon from Output panel.");
////	    panel.assertEquals(panel.getResultDescriptionOverlayText(1), result.get(i), "Checkpoint[5/5]", "Verified result name for non-dicom image");
////		}
////	}
////	
////	@AfterMethod(alwaysRun=true)
////	public void clearUserActionTable() throws SQLException {
////
////		db = new DatabaseMethodsADB(driver);
////	//	db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
////	//	db.truncateTable(nsdb,NSDBDatabaseConstants.USERFEEDBACKTABLE);
////
////	}
////
//    private void createPointAndAddComment(int whichviewbox) throws InterruptedException {
//		
//		point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(whichviewbox);
//		
//		point.drawPointAnnotationMarkerOnViewbox(whichviewbox, 80, -80);
//		point.drawPointAnnotationMarkerOnViewbox(whichviewbox, -80, -80);
//		point.drawPointAnnotationMarkerOnViewbox(whichviewbox, -80, 80);
//		point.drawPointAnnotationMarkerOnViewbox(whichviewbox, 80, 80);
//		
//		for(int i =1;i<=4;i++)
//			point.addResultComment(point.getAllPoints(whichviewbox).get(i-1), pointComment+i);
//		
//	}
//	
//
//}
//
