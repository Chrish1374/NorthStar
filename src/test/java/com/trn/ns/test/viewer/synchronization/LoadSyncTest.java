package com.trn.ns.test.viewer.synchronization;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LoadSyncTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	private ViewerOrientation orientation;
	private ViewBoxToolPanel preset;
	
	private ContentSelector contentSelector;

	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4atientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2 = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
	String mrLSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String aidoc_filepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aidoc_filepath);

	String sagittal_SeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
	String sagittal_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));

	String axial_seriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
	String axial_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));

	String coronal_SeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
	String coronal_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));

	String filePath9 = Configurations.TEST_PROPERTIES.get("TEST^Modality_LUT_filepath");
	String Modality_LUT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath9);

	String filePath11 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String NorthStar_MR_LSP = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath11);

	String filePath10 = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String TeraRecon_BrainTDA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath10);
	
	String filePath12 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String Anonymous_Data = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath12);
	
	String filePath13 = Configurations.TEST_PROPERTIES.get("Breast_MR_2_filepath");
	String Breast_MR_2_patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath13);
	

	private String sixthSeriesDescriptionModality_LUT = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES07_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TEST^Modality_LUT_filepath"));
	private String thirdSeriesDescriptionBrainTDA = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath"));
	private String firstSeriesDescriptionAnonmyous = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath12);
	private MeasurementWithUnit lineWithUnit;
	private HelperClass helper;
	private CircleAnnotation circle;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws IOException, InterruptedException, SQLException {


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test01_US741_TC2647_VerifymachineDirective() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content when viewer loads with series containing machine directive");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+TeraRecon_BrainTDA);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(TeraRecon_BrainTDA, 9);
		preset=new ViewBoxToolPanel(driver);
		
		int currentImageNumberVB1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentImageNumberVB8 = viewerPage.getCurrentScrollPositionOfViewbox(8);
		int currentImageNumberVB9 = viewerPage.getCurrentScrollPositionOfViewbox(9);

		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);

		// Verifying that machine directive is loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentImageNumberVB1, "Verifying the current image number of viewbox 1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentImageNumberVB1, "Verifying the current image number of viewbox 2", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentImageNumberVB1, "Verifying the current image number of viewbox 3", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), currentImageNumberVB1, "Verifying the current image number of viewbox 4", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), currentImageNumberVB1, "Verifying the current image number of viewbox 5", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(6), currentImageNumberVB1, "Verifying the current image number of viewbox 6", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(7), currentImageNumberVB1, "Verifying the current image number of viewbox 7", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(8), currentImageNumberVB8, "Verifying the current image number of viewbox 8", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(9), currentImageNumberVB9, "Verifying the current image number of viewbox 9", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "Verifying the window width of viewbox 1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "Verifying the window width of viewbox 2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "Verifying the window width of viewbox 3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "Verifying the window width of viewbox 4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(5), windowWidth, "Verifying the window width of viewbox 5", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(6), windowWidth, "Verifying the window width of viewbox 6", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(7), windowWidth, "Verifying the window width of viewbox 7", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(8), windowWidth, "Verifying the window width of viewbox 8", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(9), windowWidth, "Verifying the window width of viewbox 9", "verified");

		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "Verifying the window center of viewbox 1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "Verifying the window center of viewbox 2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "Verifying the window center of viewbox 3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "Verifying the window center of viewbox 4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(5), windowCenter, "Verifying the window center of viewbox 5", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(6), windowCenter, "Verifying the window center of viewbox 6", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(7), windowCenter, "Verifying the window center of viewbox 7", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(8), windowCenter, "Verifying the window center of viewbox 8", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(9), windowCenter, "Verifying the window center of viewbox 9", "verified");

		//verifying the zoom is default to zoom to fit
		preset=new ViewBoxToolPanel(driver);
		
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(1), "verifying the zoom to fir for viewbox1", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(2), "Verified the zoom to fit for viewbox2", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(3), "verifying the zoom to fir for viewbox3", "verified");

		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(4), "verifying the zoom to fir for viewbox4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(8), "verifying the zoom to fir for viewbox9", "verified");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test02_US741_TC2648_VerifymachineDirectiveWithGSPS() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content when viewer loads with series containing machine directive and GSPS");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+TeraRecon_BrainTDA);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPage(TeraRecon_BrainTDA, "", 1, 1);
		
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		viewerPage.scrollDownToSliceUsingKeyboard(1,3);


		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);

		lineWithUnit.selectDistanceFromQuickToolbar(3);
		lineWithUnit.drawLine(2, -20, -20, 10, 20);	
		preset=new ViewBoxToolPanel(driver);
		
		helper.browserBackAndReloadViewer(TeraRecon_BrainTDA, 1, 1);
	
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		String TDAColorMap_BF_18Phase2_ImageNum = viewerPage.getCurrentScrollPosition(2);

		// Verifying that GSPS is getting loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox2", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(3), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox3", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(4), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox4", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(5), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox5", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(6), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox6", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(7), TDAColorMap_BF_18Phase2_ImageNum, "verifying the image number of viewbox7", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "verified the window width value of viewbox1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "verified the window width value of viewbox2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "verified the window width value of viewbox3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "verified the window width value of viewbox4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(5), windowWidth, "verified the window width value of viewbox5", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(6), windowWidth, "verified the window width value of viewbox6", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(7), windowWidth, "verified the window width value of viewbox7", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(8), windowWidth, "verified the window width value of viewbox8", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(9), windowWidth, "verified the window width value of viewbox9", "verified");

		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "verified the window center value of viewbox1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "verified the window center value of viewbox2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "verified the window center value of viewbox3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "verified the window center value of viewbox4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(5), windowCenter, "verified the window center value of viewbox5", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(6), windowCenter, "verified the window center value of viewbox6", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(7), windowCenter, "verified the window center value of viewbox7", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(8), windowCenter, "verified the window center value of viewbox8", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(9), windowCenter, "verified the window center value of viewbox9", "verified");

		//verifying the zoom is default to zoom to fit

		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(1), "Verified the zoom to fit for viewbox1", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(2), "Verified the zoom to fit for viewbox2", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(3), "Verified the zoom to fit for viewbox3", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(4), "Verified the zoom to fit for viewbox4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(8), "Verified the zoom to fit for viewbox8", "verified");


	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test03_US741_TC2649_VerifySyncForNonMachineAndNonGSPS() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content when viewer loads with series having NO GSPS and machine directive");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+ah4atientName);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4atientName, 1);
		preset=new ViewBoxToolPanel(driver);

		int value = viewerPage.getMaxNumberofScrollForViewbox(1);
		int defaultVal = value /2;
		String windowWidth1 = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter1 = viewerPage.getWindowCenterValueOverlayText(1);
		String windowWidth2= viewerPage.getWindowWidthValueOverlayText(2);
		String windowCenter2 = viewerPage.getWindowCenterValueOverlayText(2);

		// Verifying that GSPS is getting loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),defaultVal , "verifying the default loading based on % for vb1" , "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),defaultVal , "verifying the default loading based on % for vb2", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth1, "Verifying the window width for viewbox1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth2, "Verifying the window width for viewbox2", "verified");

		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter1, "Verifying the window center for viewbox1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter2, "Verifying the window center for viewbox2", "verified");

		//verifying the zoom is default to zoom to fit
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(1), "verifying zoom to fir for vb1", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(2), "verifying zoom to fir for vb2", "verified");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test04_US741_TC2650_VerifyDefaultLoadingPostLayoutChange() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content after layout change when viewer loads with series containing GSPS");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+mrLSPPatientName);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(mrLSPPatientName, 1);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		layout=new ViewerLayout(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the scroll on viewbox 4");
		viewerPage.scrollDownToSliceUsingKeyboard(4,3);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the line annotation on viewbox4");
		lineWithUnit.selectDistanceFromQuickToolbar(4);
		lineWithUnit.drawLine(4, -10, -10, 30, 30);
		viewerPage.closingConflictMsg();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the zoom on viewbox1 and viewbox4");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 20, 0);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(4), 0, 0, 20, 0);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the scroll on viewbox 1");
		viewerPage.scrollDownToSliceUsingKeyboard(1,3);

		int currentImageNumberVB1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentImageNumberVB4 = viewerPage.getCurrentScrollPositionOfViewbox(4);

		String windowWidth1 = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter1 = viewerPage.getWindowCenterValueOverlayText(1);
		String windowWidth2 = viewerPage.getWindowWidthValueOverlayText(2);
		String windowCenter2 = viewerPage.getWindowCenterValueOverlayText(2);
		String windowWidth3 = viewerPage.getWindowWidthValueOverlayText(3);
		String windowCenter3 = viewerPage.getWindowCenterValueOverlayText(3);
		String windowWidth4 = viewerPage.getWindowWidthValueOverlayText(4);
		String windowCenter4= viewerPage.getWindowCenterValueOverlayText(4);

		preset=new ViewBoxToolPanel(driver);
		int zoomVb1 = preset.getZoomValue(1);
		int zoomVb4 = preset.getZoomValue(4);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing change layout to "+ViewerPageConstants.THREE_BY_TWO_LAYOUT);
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.closingConflictMsg();

		// Verifying that GSPS is getting loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentImageNumberVB1, "Verifying the current image number in vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentImageNumberVB1, "Verifying the current image number in vb2", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentImageNumberVB1, "Verifying the current image number in vb3", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), currentImageNumberVB4, "Verifying the current image number in vb4", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), currentImageNumberVB4, "Verifying the current image number in vb5", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth1, "Verifying the window width in vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth2, "Verifying the window width in vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth3, "Verifying the window width in vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth4, "Verifying the window width in vb4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(5), windowWidth4, "Verifying the window width in vb5", "verified");


		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter1, "Verifying the window center in vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter2, "Verifying the window center in vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter3, "Verifying the window center in vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter4, "Verifying the window center in vb4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(5), windowCenter4, "Verifying the window center in vb5", "verified");

		//verifying the zoom is default to zoom to fit
		viewerPage.assertEquals(preset.getZoomValue(1),zoomVb1, "verifying the zoom retained for viewbox1", "verified");
		viewerPage.assertEquals(preset.getZoomValue(2),100, "verifying the zoom retained for viewbox2", "verified");
		viewerPage.assertEquals(preset.getZoomValue(3),100, "verifying the zoom retained for viewbox3", "verified");
		viewerPage.assertEquals(preset.getZoomValue(4),zoomVb4, "verifying the zoom retained for viewbox4", "verified");
		viewerPage.assertEquals(preset.getZoomValue(5),zoomVb4, "verifying the zoom retained for viewbox5", "verified");


	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test05_US741_TC2652_VerifyMachineDirectivePostLayoutChange() throws InterruptedException, SQLException {


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content after layout change when viewer loads with series containing machine directive");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+TeraRecon_BrainTDA);

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(TeraRecon_BrainTDA, 1);
		layout=new ViewerLayout(driver);
		preset=new ViewBoxToolPanel(driver);
		
		int currentImageNumberVB1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing change layout to "+ViewerPageConstants.THREE_BY_TWO_LAYOUT);
		layout.selectLayout(layout.threeByTwoLayoutIcon);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verify viewbox content after layout change when viewer loads with series containing machine directive");

		// Verifying that GSPS is getting loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentImageNumberVB1, "Verifying all the viewboxes are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentImageNumberVB1, "Verifying all the viewboxes are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentImageNumberVB1, "Verifying all the viewboxes are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), currentImageNumberVB1,	"Verifying all the viewboxes are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), currentImageNumberVB1, "Verifying all the viewboxes are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(6), currentImageNumberVB1, "Verifying all the viewboxes are synchronized with vb1", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "Verifying all the viewboxes(WW) are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "Verifying all the viewboxes(WW) are synchronized with vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "Verifying all the viewboxes(WW) are synchronized with vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "Verifying all the viewboxes(WW) are synchronized with vb4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(5), windowWidth, "Verifying all the viewboxes(WW) are synchronized with vb5", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(6), windowWidth, "Verifying all the viewboxes(WW) are synchronized with vb6", "verified");

		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "Verifying all the viewboxes(WC) are synchronized with vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "Verifying all the viewboxes(WC) are synchronized with vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "Verifying all the viewboxes(WC) are synchronized with vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "Verifying all the viewboxes(WC) are synchronized with vb4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(5), windowCenter, "Verifying all the viewboxes(WC) are synchronized with vb5", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(6), windowCenter, "Verifying all the viewboxes(WC) are synchronized with vb6", "verified");

		//verifying the zoom is default to zoom to fit
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(1), "Verifying the zoom to fit in vb1", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(2), "Verifying the zoom to fit in vb2", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(3), "Verifying the zoom to fit in vb3", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(4), "Verifying the zoom to fit in vb4", "verified");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test06_US741_TC2654_VerifyGSPSLoadingWithScrollAndLayoutChange() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content after scroll and layout change when viewer loads with series containing GSPS");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+mrLSPPatientName);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(mrLSPPatientName, 1);
		preset=new ViewBoxToolPanel(driver);
		layout=new ViewerLayout(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the slice to 20 on fourth viewbox");
		viewerPage.scrollToImage(4, 20);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "drawing the line annotation on fourth viewbox");
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(3);
		lineWithUnit.drawLine(4, 10, 10, 40, 50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changed the slice location");
		viewerPage.scrollDownToSliceUsingKeyboard(4, 5);

		int currentImageNumberVB1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentImageNumberVB4 = viewerPage.getCurrentScrollPositionOfViewbox(4);

		String windowWidth1 = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter1 = viewerPage.getWindowCenterValueOverlayText(1);
		String windowWidth2 = viewerPage.getWindowWidthValueOverlayText(2);
		String windowCenter2 = viewerPage.getWindowCenterValueOverlayText(2);
		String windowWidth3 = viewerPage.getWindowWidthValueOverlayText(3);
		String windowCenter3 = viewerPage.getWindowCenterValueOverlayText(3);
		String windowWidth4 = viewerPage.getWindowWidthValueOverlayText(4);
		String windowCenter4= viewerPage.getWindowCenterValueOverlayText(4);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout to two by three");
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerPage.closingConflictMsg();

		// Verifying that GSPS is getting loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentImageNumberVB1, "Verifying the current scroll position of vb1 it should be the default loading", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentImageNumberVB1, "Verifying the current scroll position of vb2 it should be the default loading", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentImageNumberVB1, "Verifying the current scroll position of vb3 it should be the default loading", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), currentImageNumberVB4, "Verifying the current scroll position of vb4 it should be the GSPS annotation slice", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), currentImageNumberVB4, "Verifying the current scroll position of vb5 it should be the same as vb4 as it is synchronized with vb4", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth1, "Verifying the window width of vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth2, "Verifying the window width of vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth3, "Verifying the window width of vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth4, "Verifying the window width of vb4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(5), windowWidth4, "Verifying the window width of vb5", "verified");

		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter1, "Verifying the window center of vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter2, "Verifying the window center of vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter3, "Verifying the window center of vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter4, "Verifying the window center of vb4", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(5), windowCenter4, "Verifying the window center of vb5", "verified");

		//verifying the zoom is default to zoom to fit
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(1), "verifying the zoom if by default set to fit in viewbox4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(2), "verifying the zoom if by default set to fit in viewbox4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(3), "verifying the zoom if by default set to fit in viewbox4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(4), "verifying the zoom if by default set to fit in viewbox4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(5), "verifying the zoom if by default set to fit in viewbox4", "verified");
	
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test07_US741_TC2655_VerifyLoadingAndLayoutChange() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content after layout change when viewer loads with series containing NO GSPS and NO machine directive");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+mrLSPPatientName);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(mrLSPPatientName, 1);
		preset=new ViewBoxToolPanel(driver);
		layout=new ViewerLayout(driver);
		
		int currentImageNumberVB1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentImageNumberVB4 = viewerPage.getCurrentScrollPositionOfViewbox(4);

		String windowWidth1 = viewerPage.getWindowWidthValueOverlayText(1);
		String windowCenter1 = viewerPage.getWindowCenterValueOverlayText(1);
		String windowWidth2 = viewerPage.getWindowWidthValueOverlayText(2);
		String windowCenter2 = viewerPage.getWindowCenterValueOverlayText(2);
		String windowWidth3 = viewerPage.getWindowWidthValueOverlayText(3);
		String windowCenter3 = viewerPage.getWindowCenterValueOverlayText(3);
		String windowWidth4 = viewerPage.getWindowWidthValueOverlayText(4);
		String windowCenter4= viewerPage.getWindowCenterValueOverlayText(4);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout to two by three");
		layout.selectLayout(layout.twoByThreeLayoutIcon);

		// Verifying that GSPS is getting loaded
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentImageNumberVB1, "Verifying the current scroll position vb1", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentImageNumberVB1, "Verifying the current scroll position vb2", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentImageNumberVB1, "Verifying the current scroll position vb3", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), currentImageNumberVB4, "Verifying the current scroll position vb4", "verified");

		// verifying the WW/WL
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth1, "Verifying the window width of vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth2, "Verifying the window width of vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth3, "Verifying the window width of vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth4, "Verifying the window width of vb4", "verified");
		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(5), windowWidth4, "Verifying the window width of vb5", "verified");


		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter1, "Verifying the window center of vb1", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter2, "Verifying the window center of vb2", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter3, "Verifying the window center of vb3", "verified");
		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter4, "Verifying the window center of vb4", "verified");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(5), windowCenter4, "Verifying the window center of vb5", "verified");
		

		//verifying the zoom is default to zoom to fit
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(1), "Verifying the zoom is fit to window vb1", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(2), "Verifying the zoom is fit to window vb2", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(3), "Verifying the zoom is fit to window vb3", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(4), "Verifying the zoom is fit to window vb4", "verified");
		viewerPage.assertTrue(preset.verifyZooToFitIconDisable(5), "Verifying the zoom is fit to window vb5", "verified");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test08_US741_TC2661_TC2662_VerifyDICOMAndNonDICOMSync() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content for non-DICOM series having default config setting");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+Anonymous_Data);

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(Anonymous_Data, 1, 2);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		int currentCount = lineWithUnit.getMaxNumberofScrollForViewbox(2)/2;
		layout=new ViewerLayout(driver);
        preset=new ViewBoxToolPanel(driver);
		lineWithUnit.assertEquals(lineWithUnit.getCurrentScrollPositionOfViewbox(2), currentCount, "Verifying the current scroll position", "Verified");
		lineWithUnit.assertEquals(preset.getZoomLevelValue(2), preset.getZoomLevelValue(2), "Verifying the zoom", "verified");
		lineWithUnit.compareElementImage(protocolName, lineWithUnit.mainViewer, "verifying that first and second viewbox are synchronized", "test10_checkpoint_1");
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		lineWithUnit.scrollDownToSliceUsingKeyboard(5, 3);
		lineWithUnit.compareElementImage(protocolName, lineWithUnit.mainViewer, "verifying that first, second and third are synchronized", "test10_checkpoint_2");
		lineWithUnit.scrollDownToSliceUsingKeyboard(4, 3);
		lineWithUnit.compareElementImage(protocolName, lineWithUnit.mainViewer, "verifying that fourth and sixth are synchronized", "test10_checkpoint_3");
		lineWithUnit.scrollUpToSliceUsingKeyboard(2, 3);
		lineWithUnit.compareElementImage(protocolName, lineWithUnit.mainViewer, "verifying that first, second and fifth are synchronized", "test10_checkpoint_4");

		lineWithUnit.scrollToImage(2, 18);

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, 50, 50, 100, 50);

		helper.browserBackAndReloadViewer(Anonymous_Data, 1, 1);

		lineWithUnit.assertEquals(lineWithUnit.getCurrentScrollPositionOfViewbox(1), 18, "Verifying the current scroll position post GSPS", "verified");
		lineWithUnit.compareElementImage(protocolName, lineWithUnit.mainViewer, "verifying on load GSPS gets its preference", "test10_checkpoint_5");


	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
	public void test09_US741_TC2644_VerifyViewboxContentOnDoubleClickOneUp() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content for load time synchronization after double click one up");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+ah4atientName);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4atientName, 1);
		orientation=new ViewerOrientation(driver);
		preset=new ViewBoxToolPanel(driver);

		//Double click on viewbox1
		int beforeWW2 = Integer.parseInt(viewerPage.getWindowWidthValText(2));
		int	beforeWC2 = Integer.parseInt(viewerPage.getWindowCenterText(2));
		
		viewerPage.doubleClickOnViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying layout changes to 1X1");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 1, "Verify layout changes to 1X1", "The current layout is 1X1");

		//Select WL from radial menu and perform window leveling operation
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 50 , 50);

		//variable to store current value of W and C
		int beforeWW1 = Integer.parseInt(viewerPage.getWindowWidthValText(1));
		int	beforeWC1 = Integer.parseInt(viewerPage.getWindowCenterText(1));

		//Select Zoom from radial menu and perform Zoom operation
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 50 , 50);

		//variable to store zoom value of all view box  
		int intialZoomLevel1 = preset.getZoomValue(1);

		//Select PAN from tool box and perform PAN operation
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 50 , 50);

		//scroll to next 2 slice in series
		viewerPage.scrollDownToSliceUsingKeyboard(3);

		//variable to store current slice position
		int currentScrollPositionvw1 = viewerPage.getCurrentScrollPositionOfViewbox(1);

		//Image flipped horizontally from Top Orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(1));

		//Double click on viewbox1
		viewerPage.doubleClickOnViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying layout changes to orginal 2X2");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Verify layout changes to 2X2", "The current layout is 2X2");

		//variable to store current value of W and C
		int afterWW1 = Integer.parseInt(viewerPage.getWindowWidthValText(1));
		int afterWW2 = Integer.parseInt(viewerPage.getWindowWidthValText(2));

		int	afterWC1 = Integer.parseInt(viewerPage.getWindowCenterText(1));
		int afterWC2 = Integer.parseInt(viewerPage.getWindowCenterText(2));


		int finalZoomLevel1 = preset.getZoomValue(1);
		int finalZoomLevel2 = preset.getZoomValue(2);

		int afterScrollPositionvw1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int afterScrollPositionvw2 = viewerPage.getCurrentScrollPositionOfViewbox(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify series in viewbox1 and viewbox2 are synchronized");
		viewerPage.assertEquals(beforeWW1,afterWW1 , "Verifying window width is  changed in viewbox 1", "Verified window width is  changed in viewbox 1");
		viewerPage.assertEquals(beforeWC1,afterWC1, "Verifying window center is  changed in viewbox 1", "Verified window center is  changed in viewbox 1");
		viewerPage.assertEquals(beforeWW2,afterWW2 , "Verifying window width sync on load time in viewbox 2", "Verified window width sync in viewbox 2");
		viewerPage.assertEquals(beforeWC2,afterWC2, "Verifying window center sync on load time in viewbox 2", "Verified window center sync samein viewbox 2");
		viewerPage.assertEquals(currentScrollPositionvw1,afterScrollPositionvw1, "Verify scroll position is same on viewbox1", "Verify scroll position is same on viewbox1");
		viewerPage.assertEquals(currentScrollPositionvw1,afterScrollPositionvw2, "Verify scroll sync on load time in viewbox2", "Verify scroll sync on load time in viewbox2");
		viewerPage.assertEquals(intialZoomLevel1,finalZoomLevel1 ,"Verifying Zoom level remain same on viewbox1", "Verified Zoom level remain same on viewbox1");
		viewerPage.assertEquals(intialZoomLevel1,finalZoomLevel2 ,"Verifying Zoom level sync on viewbox2", "Verified Zoom level sync on viewbox2");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer, "Verify image position are in sync after double click one up","test10_checkPoint3" );
	}
	
	@Test(groups ={"IE11","Chrome","firefox", "Chrome","US741"})
	public void test10_US741_TC2609_verifyWWWLSynSamePriorityDifferentBitAndPhotometricInterpretation() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL sync when new series is added into first viewbox having SAME priority, bit and Photometric interpretation as existing series.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+Modality_LUT+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Modality_LUT, 1);
		
		
		contentSelector = new ContentSelector(driver);
		int beforeWW4 = Integer.parseInt(viewerPage.getWindowWidthValText(4));
		int	beforeWC4 = Integer.parseInt(viewerPage.getWindowCenterText(4));

		int beforeWW1 = Integer.parseInt(viewerPage.getWindowWidthValText(1));
		int	beforeWC1 = Integer.parseInt(viewerPage.getWindowCenterText(1));


		//changing WWWL of 4th viewbox
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(4));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(4),0, 0, 100, -50);

		//Verifying that WWWL is performed in 4th viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify Window Leveling takes place in 4th viewbox.");
		viewerPage.assertNotEquals(beforeWW4, Integer.parseInt(viewerPage.getWindowWidthValText(4)) , "Verifying window width is  changed in viewbox 4", "Verified window width is  changed in viewbox 4");
		viewerPage.assertNotEquals(beforeWC4, Integer.parseInt(viewerPage.getWindowCenterText(4)) , "Verifying window center is  changed in viewbox 4", "Verified window center is  changed in viewbox 4");

		//selecting 6th series in 1st viewbox and verifying that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that the sixth series is getting selected in 1stviewbox.");
		contentSelector.selectSeriesFromSeriesTab(1,sixthSeriesDescriptionModality_LUT);
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(sixthSeriesDescriptionModality_LUT), "Verify that the sixth series is getting selected in 1stviewbox.", "Verified that the sixth series is getting selected in 1stviewbox.");

		int afterWW1 = Integer.parseInt(viewerPage.getWindowWidthValText(1));
		int afterWW2 = Integer.parseInt(viewerPage.getWindowWidthValText(2));
		int afterWW3 = Integer.parseInt(viewerPage.getWindowWidthValText(3));
		int afterWW4 = Integer.parseInt(viewerPage.getWindowWidthValText(4));

		int	afterWC1 = Integer.parseInt(viewerPage.getWindowCenterText(1));
		int afterWC2 = Integer.parseInt(viewerPage.getWindowCenterText(2));
		int afterWC3 = Integer.parseInt(viewerPage.getWindowCenterText(3));
		int afterWC4 = Integer.parseInt(viewerPage.getWindowCenterText(4));

		//verifying that the WWWL is unaffected in first viewbox wrt to other viewboxes and first
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify Window Leveling takes place in all viewboxes.");		
		viewerPage.assertNotEquals(afterWW1,afterWW4 , "Verifying window width does not remains same in viewbox 4", "Verified window width does not remains same in viewbox 4");
		viewerPage.assertNotEquals(afterWW1,afterWW2, "Verifying window width does not remains samein viewbox 2", "Verified window width does not remains samein viewbox 2");
		viewerPage.assertNotEquals(afterWW1,afterWW3, "Verifying window width does not remains same in viewbox 3", "Verified window width does not remains same in viewbox 3");
		viewerPage.assertNotEquals(afterWC1,afterWC4, "Verifying window center does not remains same in viewbox 4", "Verifying window center does not remains same in viewbox 4");
		viewerPage.assertNotEquals(afterWC1,afterWC2, "Verifying window center does not remains same in viewbox 2", "Verified window center does not remains same in viewbox 3");
		viewerPage.assertNotEquals(afterWC1,afterWC3, "Verifying window center does not remains same in viewbox 3", "Verifying window center does not remains same in viewbox 2");

		viewerPage.assertNotEquals(beforeWW1, Integer.parseInt(viewerPage.getWindowWidthValText(1)) , "Verifying window width is not changed in viewbox 1 ", "Verified window width is not changed in viewbox 1");
		viewerPage.assertNotEquals(beforeWC1, Integer.parseInt(viewerPage.getWindowCenterText(1)) , "Verifying window center is not changed in viewbox 1", "Verified window center is not changed in viewbox 1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Window Leveling remains same in all viewboxes.");		

		viewerPage.assertEquals(afterWW2,afterWW3 , "Verifying window width remains same in viewbox 2 as viewbox 3", "Verified window width remains same in viewbox 2 as viewbox 3");
		viewerPage.assertEquals(afterWW2,afterWW4, "Verifying window width remains same in viewbox 2 as viewbox 4", "Verified window width remains same in viewbox 2 as viewbox 4");
		viewerPage.assertNotEquals(afterWC2,afterWC3, "Verifying window center not  same in viewbox 3 as viewbox 2", "Verified window center remains same in viewbox 3 as viewbox 2");
		viewerPage.assertNotEquals(afterWC2,afterWC4, "Verifying window center not same in viewbox 4 as viewbox 2", "Verifying window center remains same in viewbox 4 as viewbox 2");

	}
	
	@Test(groups = { "Chrome","firefox","US741"})
	public void test11_US741_TC2660_verifyNewSeriesSelectedFromContentSelectorWithGSPS() throws InterruptedException, AWTException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content when new series is selected through content selector having machine directive and GSPS");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(TeraRecon_BrainTDA, 1, 8);
	
		contentSelector = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);
		layout=new ViewerLayout(driver);
		preset=new ViewBoxToolPanel(driver);
		
		//Draw GSPS on 10th slice of 3rd viewbox after selecting 3rd description in 3rd viewbox
		contentSelector.selectResultFromSeriesTab(3,thirdSeriesDescriptionBrainTDA);
		circle.waitForAllImagesToLoad();
		
		circle.scrollToImage(3, 10);
		circle.selectCircleFromQuickToolbar(3);
		circle.drawCircle(3, -10, -10, 20, 20);

		////Change Layout to 1*2 with 'Save by Default'
		circle.click(circle.getViewPort(1));
		layout.selectLayoutWithSaveByDefault(layout.oneByTwoLayoutIcon);
		circle.compareElementImage(protocolName, circle.mainViewer,"Checkpoint2 Verify the layout changed to 2x1 and series loaded","TC2660_" +"_" + "Checkpoint1");

		//Go back to study list page and re-visit viewer page
		helper.browserBackAndReloadViewer(TeraRecon_BrainTDA, 1, 1);

		int beforeWW2 = Integer.parseInt(circle.getWindowWidthValText(2));
		int	beforeWC2 = Integer.parseInt(circle.getWindowCenterText(2));

		//Select 3rd series through content selector from second viewbox
		contentSelector.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");	
		
		circle.assertEquals(circle.getCurrentScrollPositionOfViewbox(2),10,"Verifying slice number is Viewbox2 is 10","The Slice number is Viewbox2 is 10");
		circle.assertEquals(beforeWW2,Integer.parseInt(circle.getWindowWidthValText(2)), "Verifying window width remains same in viewbox 2", "Verified window width remains same in viewbox 2");
		circle.assertEquals(beforeWC2,Integer.parseInt(circle.getWindowCenterText(2)), "Verifying window center remains same in viewbox 2", "Verifying window center remains same in viewbox 2");


		//verifying the zoom is default to zoom to fit
		circle.assertTrue(preset.verifyZooToFitIconDisable(1), "verifying the zoom if by default set to fit in viewbox1", "verified");
		circle.assertTrue(preset.verifyZooToFitIconDisable(2), "verifying the zoom if by default set to fit in viewbox2", "verified");

	}

	@Test(groups = { "Chrome","firefox","US741"})
	public void test12_US741_TC2663_verifyNonDicomSelectedFromContentSelectorSyncWithDicomSeries() throws InterruptedException, AWTException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewbox content when non-DICOM series is selected through content selector which is in sync with DICOM series having GSPS");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(Anonymous_Data, 1, 2);
	
		circle = new CircleAnnotation(driver);
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		preset=new ViewBoxToolPanel(driver);
		
		//Draw GSPS annotation on DICOM series present in second viewbox 
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 0, 0, 10, 20);

		//Go back to study list page and re-visit viewer page
		helper.browserBackAndReloadViewer(Anonymous_Data, 1, 1);
		
		//scroll down to next 2 slice
		circle.scrollDownToSliceUsingKeyboard(2);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		
		//Select 5th series (output.jpeg) through content selector in first viewbox
		circle.closeWaterMarkIcon(2);
		contentSelector.selectResultFromSeriesTabWithMachineName(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1", ViewerPageConstants.USER_CREATED_RESULT);
		circle.waitForAllImagesToLoad();
	
		//circle.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionAnonmyous), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");

		circle.assertEquals(circle.getCurrentScrollPositionOfViewbox(2),circle.getCurrentScrollPositionOfViewbox(1),"Verifying slice number is Viewbox 2 is same as viewbox 1","The Slice number is Viewbox 2 is same as viewbox 1");
		circle.assertEquals(circle.getCurrentScrollPositionOfViewbox(2),circle.getCurrentScrollPositionOfViewbox(1),"Verifying slice number is Viewbox 2 is same as viewbox 1","The Slice number is Viewbox 2 is same as viewbox 1");

		int ZoomLevel1 = preset.getZoomValue(1);
		int ZoomLevel2 = preset.getZoomValue(2);

		circle.assertEquals(ZoomLevel1, ZoomLevel2,"Verifying Zoom synchronization in viewbox 1 and 2","Zoom is synchronised in viewbox 1 and 2 ");

	}

	public void test13_US741_TC2678_verifySyncInFirstVBwithLowerPriority() throws InterruptedException, AWTException  {
		
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify sync when new series is added into FIRST viewbox having LOWER priority as existing series for GSPS - Machine directive combibation");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(TeraRecon_BrainTDA,  1, 2);
	
		circle = new CircleAnnotation(driver);
		contentSelector = new ContentSelector(driver);
		
		//Draw GSPS on first two series 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 20, 20);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 10, 10, 20, 20);
		layout=new ViewerLayout(driver);
		preset=new ViewBoxToolPanel(driver);

		//Change layout to 1X2 and click on 'Save as default' checkbox to save the 1X2 layout as default layout 
		layout.selectLayoutWithSaveByDefault(layout.oneByTwoLayoutIcon);

		//Go back to study list page and re-visit viewer page
		helper.browserBackAndReloadViewer(TeraRecon_BrainTDA,  1, 1);
	
		//Apply Zoom, PAN, Scroll , Rotation on viewbox 1
		circle.dragAndReleaseOnViewer(circle.getViewbox(1), 0, 0, -10,-10);

		circle.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		circle.dragAndReleaseOnViewer(circle.getViewbox(1), 0, 0, -100,-100);

		circle.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		circle.dragAndReleaseOnViewer(circle.getViewbox(1), 0, 0, -100,-10);

		//Select 3rd series into FIRST viewbox through content selector
		contentSelector.selectResultFromSeriesTabWithMachineName(1,thirdSeriesDescriptionBrainTDA,"tdamaps");
		
		circle.assertEquals(circle.getCurrentScrollPositionOfViewbox(1),circle.getCurrentScrollPositionOfViewbox(2),"Verifying slice number of viewbox 2 is same as viewbox 1","The Slice numberof viewbox 2 is same as viewbox 1");
		circle.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(2),"Check Zoom Level remains same in View Box 2 and viewbox 1","The Zoom level remains same View Box 2 and viewbox 1");

		int WW1 = Integer.parseInt(circle.getWindowWidthValText(1));
		int WW2 = Integer.parseInt(circle.getWindowWidthValText(2));
		int	WC1 = Integer.parseInt(circle.getWindowCenterText(1));
		int WC2 = Integer.parseInt(circle.getWindowCenterText(2));

		circle.assertEquals(WW1,WW2, "Verifying window width remains samein viewbox 2 as viewbox 1", "Verified window width remains same in viewbox 2 as viewbox 1");
		circle.assertEquals(WC1,WC2, "Verifying window center remains same in viewbox 2 as viewbox 1", "Verified window center remains same in viewbox 2 as viewbox 1");

	}

	@Test(groups ={"firefox","Chrome","IE11","US741"})
	public void test14_US741_TC2670_verifyGSPSHasMorePriority() throws  InterruptedException, SQLException, IOException, AWTException {

		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[US738] Verify sync when new series is added into FIRST/SECOND viewbox having HIGHER priority as existing series for GSPS and machine directive combination");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		helper = new HelperClass(driver);
		viewerPage=helper.loadViewerPageUsingSearch(TeraRecon_BrainTDA,  1, 1);
		layout=new ViewerLayout(driver);
		preset=new ViewBoxToolPanel(driver);
		
		contentSelector = new ContentSelector(driver);
		lineWithUnit =  new MeasurementWithUnit(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );

		//Perform rendering operations
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Applying windowing,zoom and pan" );
		lineWithUnit.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(3));
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewPort(3),10, 10, 50, 60);

		lineWithUnit.selectZoomFromQuickToolbar(lineWithUnit.getViewPort(3));    
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewPort(3),10, 10, 50, 60);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(3, -30, -30, 10, 10);
		
		
		lineWithUnit.selectPanFromQuickToolbar(viewerPage.getViewPort(3));
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewPort(3),0, 0, 100, -50);

		//Change layout 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing the layout" );         
		layout.selectLayoutWithSaveByDefault(layout.twoByOneLayoutIcon);

		//Reload the viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reloading the viewer" );
		
		helper.browserBackAndReloadViewer(TeraRecon_BrainTDA, 1, 1);
	
        //scroll down to few slices
		lineWithUnit.scrollDownToSliceUsingKeyboard(5);
        contentSelector.selectResultFromSeriesTab(1,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		

		//Verify W and C values for all VBs
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying Windowing values for all viewboxes");

		lineWithUnit.assertEquals(lineWithUnit.getWindowWidthValText(1), lineWithUnit.getWindowWidthValText(2), "Verify that the windowing width value for viewbox1 is same as viewbox2", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getWindowCenterText(1), lineWithUnit.getWindowCenterText(2), "Verify that the windowing center value for viewbox1 is same as viewbox2", "Verified");

		//Zoom value
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying Zoom value for viewboxes");
		lineWithUnit.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(2), "Verify zoom is set to its default zoom value after loading the series in a viewer", "Verified");

		//Image positions
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying scroll positions for viewboxes");
		lineWithUnit.assertEquals(lineWithUnit.getCurrentScrollPositionOfViewbox(1), lineWithUnit.getCurrentScrollPositionOfViewbox(2), "Image position of 1st and 2nd  viewbox should be same ", "Verified");


	}

	@Test(groups ={"firefox","Chrome","IE11","US741"})
	public void test15_US741_TC2668_verifySyncForSeriesHavingSamePriorityForMachine() throws  InterruptedException, SQLException, IOException, AWTException {
		 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[US738] -Verify sync when new series is added into SECOND viewbox having SAME priority as existing series for machine directive");

		helper = new HelperClass(driver);
		viewerPage=helper.loadViewerPageUsingSearch(TeraRecon_BrainTDA,  1, 1);
		
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		orientation=new ViewerOrientation(driver);
		preset=new ViewBoxToolPanel(driver);
		//Loading the patient on viewer
		layout.waitForTimePeriod(2000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
		layout.selectLayoutWithSaveByDefault(layout.twoByOneLayoutIcon);
		contentSelector.waitForAllImagesToLoad();

		//Reload the viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reloading the viewer" );
		helper.browserBackAndReloadViewer(TeraRecon_BrainTDA,  1, 1);

		//Perform rendering operations
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Applying windowing,zoom and pan" );
		contentSelector.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		contentSelector.dragAndReleaseOnViewer(10, 10, 50, 60);
		contentSelector.selectZoomFromQuickToolbar(contentSelector.getViewPort(1));	
		contentSelector.dragAndReleaseOnViewer(10, 10, 50, 60);
		contentSelector.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		contentSelector.dragAndReleaseOnViewer(0, 0, 100, -50);

		//Rotate image in 1st VB
		orientation.flipSeries(orientation.getTopOrientationMarker(1));	
		contentSelector.compareElementImage(protocolName, contentSelector.getViewPort(1), "Verify that Image in VB4 is flipped successfully", "TC06_Checkpoint1");

		//Select 3nd image in 2nd VB
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting 3rd image in second VB" );
		contentSelector.selectResultFromSeriesTab(2, thirdSeriesDescriptionBrainTDA);
		contentSelector.waitForViewerpageToLoad();
		//Verify W and C values for all VBs
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying Windowing values for all viewboxes");

		contentSelector.assertNotEquals(contentSelector.getWindowWidthValText(1), contentSelector.getWindowWidthValText(2), "Verify that the windowing width value for viewbox1 is same as viewbox2", "Verified");
		contentSelector.assertNotEquals(contentSelector.getWindowCenterText(1), contentSelector.getWindowCenterText(2), "Verify that the windowing center value for viewbox1 is same as viewbox2", "Verified");

		//Zoom value
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying Zoom value for viewboxes");
		contentSelector.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(2), "Verify zoom is set to its default zoom value after loading the series in a viewer", "Verified");

		//Image positions
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying scroll positions for viewboxes");
		contentSelector.assertEquals(contentSelector.getCurrentScrollPositionOfViewbox(1), contentSelector.getCurrentScrollPositionOfViewbox(2), "Image position of 1st and 2nd  viewbox should be same ", "Verified");

		//Verify the rotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying that the images are not rotated");
		contentSelector.compareElementImage(protocolName, contentSelector.getViewPort(1), "Verify that Image in VB2 is not rotate and Returned to default", "TC06_Checkpoint2");
		contentSelector.compareElementImage(protocolName, contentSelector.getViewPort(2), "Verify that Image in VB2 is not rotate and Returned to default", "TC06_Checkpoint3");
		
	}

	@Test(groups ={"firefox","Chrome","IE11"})
	public void test16_DE1041_TC4437_TC4438_verifyInitialSlicePositionSynchronization() throws  InterruptedException, SQLException, IOException {
		 
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that initial slice position is synchronized correctly.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Breast_MR_2_patient, 1);
		
		contentSelector = new ContentSelector(driver);
		
	    int defaultSlicePosition1=viewerPage.getCurrentScrollPositionOfViewbox(1);
	    int defaultSlicePosition2=viewerPage.getCurrentScrollPositionOfViewbox(2);
		
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[1/2]", "Verify initial slice positon is synchronized correctly on both viewbox" );
	    viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), defaultSlicePosition1, "Verify slice position for viewbox1 is"+" "+defaultSlicePosition1, "Verified");
	    viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), defaultSlicePosition2, "Verify slice position for viewbox2 is"+" "+defaultSlicePosition2, "Verified");
	    
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[2/2]", "Verify On one up Default slice position should not changed for viewbox-2" );
	    viewerPage.doubleClickOnViewbox(2);
	    viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), defaultSlicePosition2, "Verify on one up default slice position not changed for viewbox2", "Verified");
	    viewerPage.doubleClickOnViewbox(2);
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.deleteUserSetLayout();
	}

}