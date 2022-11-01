package com.trn.ns.test.viewer.GSPS;

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
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
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
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import gnu.cajo.utils.BaseProxy.Panel;

//F79_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  -revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ARResultUsingBinarySelectorTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private CircleAnnotation circle;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private EllipseAnnotation ellipse;
	private SimpleLine line;
	private MeasurementWithUnit lineWithUnit;
	private HelperClass helper;
	//Get Patient Name
	String filePath2 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneAgePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);	

	//Get Patient Name
	String filePath4 = Configurations.TEST_PROPERTIES.get("Picline_filepath");
	String piccLinePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String filePath5 = Configurations.TEST_PROPERTIES.get("Head_CT_filepath");
	String headCTPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String filePath = TEST_PROPERTIES.get("AH4_pdf_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath6 = TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);

	String filePath7 = TEST_PROPERTIES.get("Corticometrix_1");
	String Corticometrix_1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);

	String CorticometrixSeries1=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath7);
	String CorticometrixResult1=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath7);

	String filePath8 = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String piccLineOnePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath8);

	String filePath9 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath9);
	String resultToSelect=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath9);
	private RegisterUserPage register;

	String username_1 = "userA";
	String username_2 = "userB";

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private ViewerLayout layout;
	private ViewerSliderAndFindingMenu findingMenu;
	private ViewerSendToPACS sd;
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	}


	//	Load and display Single WIA Results and use a Binary Selector to Confirm/Reject a Result - User Interaction Coverage    
	@Test(groups ={"Chrome","US306","US352"})
	public void test01_US306_TC933_US352_TC1240_verfiyUserInteraction() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify user interaction (accept/reject) on Picline data");

		String filePath = Configurations.TEST_PROPERTIES.get("Picline_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName, 1);
		layout = new ViewerLayout(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		findingMenu.acceptResult(1);
		viewerpage.click(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint-1a", "Verify selected checkmark is highlighted in green color");
		viewerpage.verifyTrue(findingMenu.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint-2", "Verify accept checkbox is still selected on layout change");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		findingMenu.verifyTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint TC2[2] : Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		viewerpage.browserBackWebPage();
		viewerpage = helper.loadViewerDirectly(PatientName, 1);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint-3a", "Verify selected checkmark is highlighted in green color");
		viewerpage.verifyTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint TC2[2] :Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		findingMenu.rejectResult(1);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint-4a", "Verify selected checkmark is highlighted in red color");
		viewerpage.verifyTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint TC2[3]: Verify result is rejected","Selected checkmark is highlighted in red color");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint-5", "Verify reject checkbox is still selected on layout change");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.verifyTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint TC2[3]: Verify result is rejected","Selected checkmark is highlighted in red color");

		viewerpage.browserBackWebPage();
		viewerpage = helper.loadViewerDirectly(PatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint-6a", "Verify selected checkmark is highlighted in red color");
		viewerpage.verifyTrue(findingMenu.verifyResultsAreRejected(1), "Verify result is rejected","Selected checkmark is highlighted in red color");

	}

	// Done for PICCLINE and HeadCT (INFERVISION)	
	@Test(groups ={"Chrome","US775","Sanity"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test02_userFeedback"})
	public void test02_01_US775_TC2815_TC2816_verfiyAcceptRejectWithFeedback(String patientFilePath, String whichViewbox) throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Replace binary selector buttons with GSPS A/R toolbar-Verify new binary selector A/R buttons"
				+ "<br> Replace binary selector buttons with GSPS A/R toolbar-Verify new binary selector on different binary data sets");

		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		Integer vieweboxNumber = Integer.parseInt(whichViewbox);

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		DatabaseMethods db = new DatabaseMethods(driver);		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the when result is in accepted state");
		findingMenu.acceptResult(vieweboxNumber);
		OutputPanel panel = new OutputPanel(driver);
		panel.openAndCloseOutputPanel(true);
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
		sd.closeNotification();
		viewerpage.assertEquals(db.getUserFeedback(patientName, sd.getSeriesDescriptionOverlayText(1)),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"verifying if feedback is accepted","verified");
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the when result is in rejected state");
		findingMenu.rejectResult(vieweboxNumber);
		sd.waitForElementInVisibility(sd.notificationDiv);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
		viewerpage.assertEquals(db.getUserFeedback(patientName, sd.getSeriesDescriptionOverlayText(1)),NSDBDatabaseConstants.LOGENTRYSTATUSREJECT,"verifying if feedback is rejected","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the when result is in pending state");
		findingMenu.rejectResult(vieweboxNumber);		
		panel.openAndCloseOutputPanel(false);
		

	}

	@Test(groups ={"Chrome","US775","DE909","Positive"})
	public void test02_02_US775_TC2816_DE909_TC3519_verfiyAcceptRejectWithFeedback() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Replace binary selector buttons with GSPS A/R toolbar-Verify new binary selector on different binary data sets"
				+ "<br> Verify Accept/Reject toolbar does appear on PDF viewbox.");
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(imbioPatient, 1);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		DatabaseMethods db = new DatabaseMethods(driver);		
		// DE909 - verifying that accept reject tool bar is displayed on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/6]", "verifying that accept reject tool bar is displayed on PDF");
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.getAcceptRejectToolBar(2)),"Verifying the toolbar presence","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/6]", "Verifying the when result is in accepted state");
		findingMenu.acceptResult(1);
		findingMenu.acceptResult(2);
		viewerpage.assertEquals(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(1)),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"verifying if feedback is accepted","verified");
		viewerpage.assertEquals(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(2)),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"verifying if feedback is accepted","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/6]", "Verifying the when result is in pending state");
		findingMenu.acceptResult(1);
		findingMenu.acceptResult(2);			
		viewerpage.assertTrue(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(1)).isEmpty(),"verifying if feedback is blank","verified");
		viewerpage.assertTrue(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(2)).isEmpty(),"verifying if feedback is blank","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/6]", "Verifying the when result is in rejected state");
		findingMenu.rejectResult(1);		
		findingMenu.rejectResult(2);	
		viewerpage.assertEquals(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(1)),NSDBDatabaseConstants.LOGENTRYSTATUSREJECT,"verifying if feedback is rejected","verified");
		viewerpage.assertEquals(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(2)),NSDBDatabaseConstants.LOGENTRYSTATUSREJECT,"verifying if feedback is rejected","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/6]", "Verifying the when result is in pending state");
		findingMenu.rejectResult(1);		
		findingMenu.rejectResult(2);	
		viewerpage.assertTrue(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(1)).isEmpty(),"verifying if feedback is blank","verified");
		viewerpage.assertTrue(db.getUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(2)).isEmpty(),"verifying if feedback is blank","verified");

		helper.browserBackAndReloadViewer(patientName,1,1);
		viewerpage.closingConflictMsg();
		viewerpage.click(viewerpage.getViewPort(3));
		viewerpage.mouseHover(findingMenu.getAcceptRejectToolBar(3));
		viewerpage.compareElementImage(protocolName,viewerpage.getViewPort(3),"Checkpoint[6/6]:Verifying that A/R toolbar is absent on AH4 pdf since it has no machine result associated","test02_02");

	}

	@Test(groups ={"Chrome","US775"})
	public void test02_03_US775_TC2816_verfiyAcceptRejectWithFeedback() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Replace binary selector buttons with GSPS A/R toolbar-Verify new binary selector on different binary data sets");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(boneAgePatient, 4);
		layout = new ViewerLayout(driver);
		sd = new ViewerSendToPACS(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.closingConflictMsg();
		DatabaseMethods db = new DatabaseMethods(driver);		
	
		for(int i =5 ;i< 8 ;i++) {
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the when result is in accepted state");
			findingMenu.checkBoneage(i);
			viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(i),ViewerPageConstants.BONEAGE_MACHINE_NAME),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"verifying if feedback is accepted","verified");

			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the when result is in pending state");
			findingMenu.checkBoneage(i);	
			viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(i),ViewerPageConstants.BONEAGE_MACHINE_NAME),"","verifying if feedback is blank","verified");
		}


	}

	@Test(groups ={"Chrome","US775","Sanity"})
	public void test03_US775_TC2845_TC2847_verfiyToolBarAndBinarySelector() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Replace binary selector buttons with GSPS A/R toolbar- Verify toolbar when GSPS and binary selector both on series"
				+ "<br> Replace binary selector buttons with GSPS A/R toolbar- Verify with resize annotation");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(headCTPatientName, 1);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 70, 70);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the when result is in accepted state");
		viewerpage.assertFalse(lineWithUnit.verifyBinarySelectorToobar(1),"verifying the binary selector presence","verified");

		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerpage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(),"verifying post annotation is focused binary selector toolbar is not displayed","gsps tool bar is displayed");
		viewerpage.assertFalse(lineWithUnit.verifyBinarySelectorToobar(1),"verifying that binary selector tool bar is not displayed when gsps tool bar is displaye","verified");


	}

	@Test(groups ={"Chrome","US775","DE909","Positive"})
	public void test04_DE909_TC3520_verfiyToolbarAbsenceOnDataWhichHasNoGSPSAndRT() throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify Accept/Reject toolbar does not appear when no GSPS or RTStruct results in a DICOM viewbox");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(ah4PatientName, 1);
		viewerpage.closingConflictMsg();
		findingMenu = new ViewerSliderAndFindingMenu(driver);


		// DE909 - verifying that accept reject tool bar is displayed on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "verifying that accept reject tool bar is not displayed on anyviewbox which has no GSPS and RT Struct");
		viewerpage.mouseHover(findingMenu.getAcceptRejectToolBar(1));
		viewerpage.compareElementImage(protocolName,viewerpage.getViewPort(1),"Checkpoint[2/5]:Verifying that A/R toolbar is absent on AH4 pdf since it has no machine result associated","test04_1");

		viewerpage.mouseHover(findingMenu.getAcceptRejectToolBar(2));
		viewerpage.compareElementImage(protocolName,viewerpage.getViewPort(2),"Checkpoint[3/5]:Verifying that A/R toolbar is absent on AH4 pdf since it has no machine result associated","test04_2");

		viewerpage.mouseHover(findingMenu.getAcceptRejectToolBar(3));
		viewerpage.compareElementImage(protocolName,viewerpage.getViewPort(3),"Checkpoint[4/5]:Verifying that A/R toolbar is absent on AH4 pdf since it has no machine result associated","test04_3");

		cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();

		cs.selectSeriesFromSeriesTab(4, series.get(2));
		viewerpage.mouseHover(findingMenu.getAcceptRejectToolBar(4));
		viewerpage.compareElementImage(protocolName,viewerpage.getViewPort(4),"Checkpoint[5/5]:Verifying that A/R toolbar is absent on AH4 pdf since it has no machine result associated","test04_4");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","DE910" })
	public void test05_DE910_TC3581_verifyArToolBarOnCorticometrix() throws  InterruptedException, AWTException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verifying 6th view box is empty for 'icomentrix' data");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(Corticometrix_1, 1);
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);

		// Verifying AR tool bar present on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "verifying that accept reject tool bar is displayed on first view box which has Pdf loaded");
		viewerpage.assertTrue(circle.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");
		//Click on Accept and Reject button on AR tool bar.
		circle.selectAcceptfromGSPSRadialMenu(1);
		viewerpage.assertTrue(circle.verifyResultsAreAccepted(1), "Verifying the color of accept button", "Color of accepted  button is green.");
		circle.selectRejectfromGSPSRadialMenu(1);
		viewerpage.assertTrue(circle.verifyResultsAreRejected(1), "Verifying the color of rejected button", "Color of rejected  button is red.");

		// Selecting the source series in 1st view box from content selector and verifying series in 1st and 2nd view box are same.
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "verifying that Dicom series is getting displayed on 1st and 2nd view box.");
		cs.selectSeriesFromSeriesTab(1,CorticometrixSeries1);
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(CorticometrixSeries1), "Verifying Dicom series displayed on 1st viewbox is higlighted on content selector", "Verified Dicom series displayed on 1st viewbox");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(CorticometrixSeries1), "Verifying Dicom series displayed on 2nd viewbox is higlighted on content selector", "Verified Dicom series displayed on 2nd viewbox");


		//Again open the pdf from content selector result tab and verify AR tool bar is present or not.
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "verifying that pdf loaded in 1st viewbox and accept reject tool bar is displayed on first view box which has Pdf loaded");
		cs.selectResultFromSeriesTab(1, CorticometrixResult1);
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(CorticometrixResult1), "Verifying Pdf series displayed on 1st viewbox is higlighted on content selector", "pdf series displayed on 1st viewbox");
		cs.click(cs.getViewPort(1));
		viewerpage.assertTrue(circle.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");


		//Draw GSPS on 2nd view box.
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 60, 50,30,30);

		circle.closingConflictMsg();
		String result = ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1";
		//Open the drawn circle series from result tab through content selector
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "verifying that Dicom series is getting displayed on 1st and 2nd view box.");
		cs.selectResultFromSeriesTab(1,result);
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(result), "Verifying Dicom series displayed on 1st viewbox is higlighted on content selector", "Verified Dicom series displayed on 1st viewbox");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(result), "Verifying Dicom series displayed on 2nd viewbox is higlighted on content selector", "Verified Dicom series displayed on 2nd viewbox");

		//Again open the original result pdf from content selector in result tab.
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "verifying that pdf loaded in 1st viewbox and accept reject tool bar is displayed on first view box which has Pdf loaded");
		cs.selectResultFromSeriesTab(1, CorticometrixResult1);
		cs.click(cs.getViewPort(1));
		viewerpage.assertTrue(circle.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");





	}

	@Test(groups ={"Chrome","DE1098","Positive"})
	public void test06_DE1098_TC4619_DE1079_TC4696_verfiyAcceptRejectStatePresistedOnReload() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Accept/Reject PDF throws console errors and UI icons of toolbar not updated with latest state- Happy path"
				+ "<br> Verify binary toolbar on PDF data");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerPageUsingSearch(imbioPatient,  1, 1);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		viewerpage.clearConsoleLogs();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/25]", "verifying that accept reject tool bar is displayed on PDF");
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.getAcceptRejectToolBar(2)),"Verifying the toolbar presence","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/25]", "Verifying the when result is in accepted state");
		findingMenu.acceptResult(1);
		findingMenu.acceptResult(2);
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[3/25]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(imbioPatient,  1, 1);

		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1),"Checkpoint[4/25]","Verified that result is accepted");
		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(2),"Checkpoint[5/25]","Verified that result is accepted");   
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[6/25]","Verifying there is no console error post reload"); 


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/25]", "Verifying the when result is in pending state");
		findingMenu.acceptResult(1);
		findingMenu.acceptResult(2);	
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[8/25]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(imbioPatient,  1, 1);

		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(1),"Checkpoint[9/25]","Verified that result is not accepted");
		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(2),"Checkpoint[10/25]","Verified that result is not accepted");   
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(1),"Checkpoint[11/25]","Verified that result is not rejected");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(2),"Checkpoint[12/25]","Verified that result is not rejected");   
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[13/25]","Verifying there is no console error"); 

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[14/25]", "Verifying the when result is in rejected state");
		findingMenu.rejectResult(1);		
		findingMenu.rejectResult(2);	
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[15/25]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(imbioPatient,  1, 1);

		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1),"Checkpoint[16/25]","Verified that result is rejected");
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(2),"Checkpoint[17/25]","Verified that result is rejected");   
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[18/25]","Verifying there is no console error"); 

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[19/25]", "Verifying the when result is in pending state");
		findingMenu.rejectResult(1);		
		findingMenu.rejectResult(2);	
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[20/25]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(imbioPatient,  1, 1);

		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(1),"Checkpoint[21/25]","Verified that result is not accepted");
		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(2),"Checkpoint[22/25]","Verified that result is not accepted");   
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(1),"Checkpoint[23/25]","Verified that result is not rejected");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(2),"Checkpoint[24/25]","Verified that result is not rejected");   
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[25/25]","Verifying there is no console error"); 

	}

	@Test(groups ={"Chrome","DE1098","Positive"})
	public void test07_DE1098_TC4620_verfiyAcceptRejectStatePresistedOnReloadForSC() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Accept/Reject binary A/R toolbar on secondary capture.");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerPageUsingSearch(piccLineOnePatient,  1, 1);
		findingMenu = new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/19]", "Verify binary A/R toolbar visible when mouse hover on secondary capture.");
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.getAcceptRejectToolBar(1)),"Verifying the toolbar presence","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/19]", "Verifying the when result is in accepted state");
		findingMenu.acceptResult(1);		
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[3/19]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(piccLineOnePatient,  1, 1);

		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1),"Checkpoint[4/19]","Verified that result is accepted");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[5/19]","Verifying there is no console error post reload"); 


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/19]", "Verifying the when result is in pending state");
		findingMenu.acceptResult(1);
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[7/19]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(piccLineOnePatient,  1, 1);

		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(1),"Checkpoint[8/19]","Verified that result is not accepted");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(1),"Checkpoint[9/19]","Verified that result is not rejected");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[10/19]","Verifying there is no console error"); 

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[11/19]", "Verifying the when result is in rejected state");
		findingMenu.rejectResult(1);		
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[12/19]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(piccLineOnePatient,  1, 1);

		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1),"Checkpoint[13/19]","Verified that result is rejected");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[14/19]","Verifying there is no console error"); 

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[15/19]", "Verifying the when result is in pending state");
		findingMenu.rejectResult(1);		
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[16/19]","Verifying there is no console error"); 
		helper.browserBackAndReloadViewer(piccLineOnePatient,  1, 1);

		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(1),"Checkpoint[17/19]","Verified that result is not accepted");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(1),"Checkpoint[18/19]","Verified that result is not rejected");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[19/19]","Verifying there is no console error"); 

	}

	//US1069: Implement Accept Reject for DICOM-SR result
	@Test(groups = { "Chrome", "IE11", "Edge","US1069","US1411","Positive" })
	public void test08_US1069_TC5118_TC5119_TC5123_US1411_TC7760_verifyAcceptedStatePersistedOnSR() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(" Verify binary selector is displayed on SR report <br>"+
				"Verify Accepted state of A/R bar on SR report should be persisted <br>"
				+ "Verify no clone is getting created when SR report is accepted <br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ChestCT1p25mmPatient,2);
		cs = new ContentSelector(driver);
		layout = new ViewerLayout(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		cs.closeNotification();
		
		int resultCount=cs.getAllResults().size();
		// Verifying AR tool bar present on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Verify accept reject tool bar is displayed on SR report");
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");

		//accept report and verify state of report after reload of viewer
		findingMenu.selectAcceptfromGSPSRadialMenu(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Verify Accepted state of A/R bar on SR report after reload of viewer page");
		helper.browserBackAndReloadViewer(ChestCT1p25mmPatient,1,2);
		cs.closeNotification();
		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Verifying the color of accept button", "Color of accepted  button is green.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Verify clone copy after accepting SR report");
		viewerpage.assertEquals(cs.getAllResults().size(),resultCount, "Verify no clone copy in result tab after accepting SR report", "Verified");

		//change layout and load SR report on empty viewbox
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForViewerpageToLoad(2);

		cs.waitForElementInVisibility(cs.notificationDiv);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "Verify Accepted state of A/R bar on SR report after loading SR report in empty viewbox");
		cs.selectResultFromSeriesTab(4, resultToSelect);
		cs.click(cs.getViewPort(4));
    	viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(4), "Verifying the color of accept button", "Color of accepted  button is green.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "Verify Accepted state of A/R bar on SR report after loading SR report in viewbox containing DICOM series");
		cs.selectResultFromSeriesTab(2, resultToSelect);
		cs.click(cs.getViewPort(2));
		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(2), "Verifying the color of accept button", "Color of accepted  button is green.");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","US1069","US1411","Positive" })
	public void test09_US1069_TC5120_TC5123_US1411_TC7760_verifyRejectedStatePersistedOnSR() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Rejected state of A/R bar on SR report should be persisted <br>"+
				"Verify no clone is getting created when SR report is rejected. <br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ChestCT1p25mmPatient,2);
		cs = new ContentSelector(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		layout = new ViewerLayout(driver);
		cs.closeNotification();
		int resultCount=cs.getAllResults().size();

		// Verifying AR tool bar present on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Verify accept reject tool bar is displayed on SR report");
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");

		//reject report and verify state of report after reload of viewer
		findingMenu.selectRejectfromGSPSRadialMenu(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Verify Rejected state of A/R bar on SR report after reload of viewer page");
		helper.browserBackAndReloadViewer(ChestCT1p25mmPatient,1,2);
		cs.closeNotification();
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Verifying the color of reject button", "Color of rejected  button is red.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Verify clone copy after rejecting SR report");
		viewerpage.assertEquals(cs.getAllResults().size(),resultCount, "Verify no clone copy in result tab after rejecting SR report", "Verified");

		//change layout and load SR report on empty viewbox
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForViewerpageToLoad(2);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "Verify Rejected state of A/R bar on SR report after loading SR report in empty viewbox");
		cs.selectResultFromSeriesTab(4, resultToSelect);
		cs.click(cs.getViewPort(4));
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(4), "Verifying the color of reject button", "Color of rejected  button is red.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "Verify Rejected state of A/R bar on SR report after loading SR report in viewbox containing DICOM series");
		cs.selectResultFromSeriesTab(2, resultToSelect);
		cs.click(cs.getViewPort(2));
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(2), "Verifying the color of reject button", "Color of rejected  button is red.");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","US1069","US1411","Positive" })
	public void test10_US1069_TC5121_US1411_TC7760_verifyPendingStatePersistedOnSR() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Pending state of A/R bar on SR report should be persisted.<br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ChestCT1p25mmPatient,2);
		cs = new ContentSelector(driver);
		layout = new ViewerLayout(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		cs.closeNotification();
		
		// Verifying AR tool bar present on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Verify accept reject tool bar is displayed on SR report");
		viewerpage.click(viewerpage.getViewPort(2));
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");

		// verify state of report after reload of viewer
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify Pending state of A/R bar on SR report after reload of viewer page");
		
		helper.browserBackAndReloadViewer(ChestCT1p25mmPatient,1,2);
		cs.closeNotification();
		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(1), "Verifying the color of accept button", "Color of accept  button is light blue.");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(1), "Verifying the color of reject button", "Color of reject  button is light blue.");

		//change layout and load SR report on empty viewbox
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForViewerpageToLoad(2);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify Rejected state of A/R bar on SR report after loading SR report in empty viewbox");
		cs.selectResultFromSeriesTab(4, resultToSelect);
		cs.click(cs.getViewPort(4));
		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(4), "Verifying the color of accept button", "Color of accept  button is light blue.");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(4), "Verifying the color of reject button", "Color of reject  button is light blue.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verify Rejected state of A/R bar on SR report after loading SR report in viewbox containing DICOM series");
		cs.selectResultFromSeriesTab(2, resultToSelect);
		cs.click(cs.getViewPort(2));
		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(2), "Verifying the color of accept button", "Color of accept  button is light blue.");
		viewerpage.assertFalse(findingMenu.verifyResultsAreRejected(2), "Verifying the color of reject button", "Color of reject  button is light blue.");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","US1069","Positive" })
	public void test11_US1069_TC5149_verifyBinarySelectorOnBrowserResizeForSRReport() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify binary selector on window resize");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ChestCT1p25mmPatient,1);
		cs = new ContentSelector(driver);
		layout = new ViewerLayout(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		cs.closeNotification();
		// Verifying AR tool bar present on PDF
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Verify accept reject tool bar is displayed on SR report");
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerpage.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/3]", "Verify position of binary selector when layout change is 2*2");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.resizeBrowserWindow(700,600);
		viewerpage.mouseHover(findingMenu.acceptRejectToolbar);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verify binary selector position when layout change to 2*2 ", "test11_checkpoint2_Layout_2x2");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/3]", "Verify position of binary selector when layout change is 3*3");
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.mouseHover(findingMenu.acceptRejectToolbar);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verify binary selector position when layout change to 3*3 ", "test11_checkpoint3_Layout_3x3");

	}

	//DE1305: Accept Reject toolbar is not displaying correctly
	@Test(groups = { "Chrome", "IE11", "Edge","DE1305","Positive"})
	public void test12_DE1305_TC5446_TC5548_TC5653_TC5656_verifyArToolBarWhenAnnotationDrawnResizeAndMoved() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the AR tool bar is visible when the annotation is selected/drawn/resized and moved. <br>"+
				"Verify the AR tool bar is not displayed when the user selected the annotation from Radial Menu and L.clicks on the viewer page but does not draw it.<br>"+
				"Verify Accept Reject toolbar is displaying correctly when drawing new annotation/selecting annotation - Happy path <br>"+
				"Verify AR tool visibility by clicking anywhere on the screen but not on finding.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ah4PatientName,1);

		lineWithUnit = new MeasurementWithUnit(driver);
		ellipse = new EllipseAnnotation(driver);
		line = new  SimpleLine(driver);
		cs = new ContentSelector(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying AR toolbar after user selected the annotation from Radial Menu and L.clicks on the viewer page but does not draw it");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		line.click(line.getViewPort(1));
		line.assertFalse(line.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/12]", "Verify AR toolbar not visible on viewerpage.");
		lineWithUnit.drawLine(1, 50, 10, 70, 10);
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[2/12]", "Verified AR toolbar after drawing linear measurement annotation");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify AR tool bar is visible when the annotation is drawn");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 50,30,30);
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[3/12]", "Verified AR toolbar after drawing circle annotation");

		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,20,0,60,0); 
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[4/12]", "Verified AR toolbar after drawing line annotation");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);
		line.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/12]", "Verified that ellipse annotation is selected after drawing");
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[6/12]", "Verified AR toolbar after drawing ellipse annotation");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Reload viewer page and verify AR toolbar on viewbox.");

		helper.browserBackAndReloadViewer(ah4PatientName,1,1);

		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[7/12]", "Verified AR toolbar after reload of viewer page.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify AR tool bar is visible when the annotation is selected");
		circle.selectCircle(1, 1);
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[8/12]", "Verified AR toolbar after selecting circle annotation");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify AR tool bar is visible  and annotation is selected when the annotation is moved");
		circle.moveSelectedCircle(1, -10, 20);
		line.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[9/12]", "Verified circle annotation is current active accepted GSPS on viewer after move.");
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[10/12]", "Verified AR toolbar after selecting circle annotation");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify AR tool bar is visible and annotation is selected when the annotation is resize");
		ellipse.resizeEllipse(1, 1, 100, 0);
		line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[11/12]", "Verified AR toolbar after resize of ellipse annotation");
		line.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[12/12]", "Verified ellipse annotation is current active accepted GSPS on viewer after resize.");
	}


	@Test(groups ={"Chrome","IE11","Edge","DE1729","Positive"})
	public void test13_DE1729_TC7083_verfiyFeedbackTablePostAnotherUserLogin() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify that Feedback column of UserFeedback table is not getting updated from accepted to rejected and vice versa after re-login with another user[Happy Path]");

		usersCreation();

		Header header = new Header(driver);		
		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with "+username_1);
		loginPage.login(username_1, username_1);

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(boneAgePatient,4);
		findingMenu = new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting the result in first viewbox from user 1");
		findingMenu.acceptResult(1);
		viewerpage.waitForTimePeriod(3000);
		viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(1),ViewerPageConstants.BONEAGE_MACHINE_NAME1,username_1),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"Checkpoint[1/5]","verifying if feedback is accepted for "+username_1);


		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with "+username_2);
		loginPage.login(username_2, username_2);

		patientPage.waitForPatientPageToLoad();
		viewerpage = helper.loadViewerDirectly(boneAgePatient,4);


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejecting the result in first viewbox from user 2");

		findingMenu.rejectResult(1);
		viewerpage.waitForTimePeriod(3000);
		viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(1),ViewerPageConstants.BONEAGE_MACHINE_NAME1,username_2),NSDBDatabaseConstants.LOGENTRYSTATUSREJECT,"Checkpoint[2/5]","verifying if feedback is rejected for "+username_2);
		viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(1),ViewerPageConstants.BONEAGE_MACHINE_NAME1,username_1),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"Checkpoint[3/5]","verifying if feedback is accepted for "+username_1);

		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with "+username_1);
		loginPage.login(username_1, username_1);

		patientPage.waitForPatientPageToLoad();;
		viewerpage = helper.loadViewerDirectly(boneAgePatient,4);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "On again revisiting the viewer page feedback value for User A in userfeedback table under Feedback column should not be changed to Reject from Accept.");

		viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(1),ViewerPageConstants.BONEAGE_MACHINE_NAME1,username_2),NSDBDatabaseConstants.LOGENTRYSTATUSREJECT,"Checkpoint[4/5]","verifying if feedback is rejected for "+username_2+" post relogin");
		viewerpage.assertEquals(db.getUserFeedback(boneAgePatient,viewerpage.getSeriesDescriptionOverlayText(1),ViewerPageConstants.BONEAGE_MACHINE_NAME1,username_1),NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT,"Checkpoint[5/5]","verifying if feedback is accepted for "+username_1+" post relogin");



	}



	public void cleanUserFeedbackTableForImbio() throws SQLException {

		viewerpage = new ViewerPage(driver);
		Header header = new Header(driver);
		DatabaseMethods db = new DatabaseMethods(driver);
		if(header.getText(header.patientInfo).contains(imbioPatient)) {
			db.setUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(1),"");
			db.setUserFeedback(imbioPatient,viewerpage.getSeriesDescriptionOverlayText(2),"");
		}
		if(header.getText(header.patientInfo).contains(piccLineOnePatient)) {
			db.setUserFeedback(piccLineOnePatient,viewerpage.getSeriesDescriptionOverlayText(1),"");
		}
	}

	/*private void reloadViewer(int whichResult) {

		viewerpage.navigateBackToStudyListPage();

		patientPage.clickOntheFirstStudy();
		viewerpage.waitForResultToLoad(whichResult);

	}*/

	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {


		db.deleteDrawnAnnotation(username_1);
		db.deleteDrawnAnnotation(username_2);

		db.deleteUser(username_1);
		db.deleteUser(username_2);


		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
	}

	private void usersCreation() throws InterruptedException {

		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		

		register.createNewUser(username_1, username_1, LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser(username_2, username_2, LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

	}



}
