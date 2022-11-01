package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I15_Viewer-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DefaultSlicePositionRenderingTest extends TestBase {
	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private EllipseAnnotation ellipse;
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewerARToolbox arToolbar;

	// Get Patient Name
	String aidoc_filepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aidoc_filepath);

	String TeraRecon_BrainTDA_filepath = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String tdaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TeraRecon_BrainTDA_filepath);

	String SQA_Testing = Configurations.TEST_PROPERTIES.get("SQA_Testing");
	String sqaTestingPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, SQA_Testing);

	String DigitalReferenceObject_filepath = Configurations.TEST_PROPERTIES.get("DigitalReferenceObject_filepath");
	String dRPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, DigitalReferenceObject_filepath);

	String filePathAidocMachine = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String GSPS_PatientName_Aidoc_machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidocMachine);


	String MRLSP_filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String LSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,MRLSP_filePath);

	String Quibim_BreastPerfusion_filePath=Configurations.TEST_PROPERTIES.get("Quibim_BreastPerfusion_filepath");
	String Quibim_BreastPerfusionPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,Quibim_BreastPerfusion_filePath);

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String aH4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	// Get series description and image number
	//	private String sagittal_SeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
	private String sagittal_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));

	//	private String axial_seriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
	private String axial_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));

	//	private String coronal_SeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
	private String coronal_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));

	private String TDAColorMap_BV_18Phase2_ImageNum = DataReader.getResultDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath"));


	private String secondSeriesDescriptionDR = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("DigitalReferenceObject_filepath"));

	private String firstSeriesDescriptionLSP = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("MR_LSP_filepath"));

	private String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	private ContentSelector contentSelector;


	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	//BeforeMethod is not available as using the code for restarting the IISServer

	//TC2096 - Set slice position based on machine directive
	//Limitation : Not working in edge because of mouseHover function
	//	TC 1: Verify initial slice position of a series based on machine directive (Automated) 
	//	//	TC 4: Verify default slice position for a study based on database configuration (Automated). 
	@Test(groups ={"firefox","Chrome","IE11","US613"})
	public void test01_US613_TC2096_verifySlicePositionBasedOnMachineDirective() throws InterruptedException, AWTException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set slice position based on machine directive");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(aidocPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		ellipse = new EllipseAnnotation(driver);
		layout=new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aidocPatientName+" in viewer" );

		//Step -1
		//Verify default slice position after loading viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint TC1[1] & Checkpoint[1/5]", "Verifying slice position for images in each viewbox of Aidoc patient data");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), sagittal_ImageNum, "Verify that 'sagittal' series should load at slice number '5'", "'sagittal' series is loading at slice number '5'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), axial_ImageNum, "Verify that 'axial' series should load at slice number '101'", "'axial' series is loading at slice number '101'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(3), coronal_ImageNum, "Verify that 'coronal' series should load at slice number '15'", "'coronal' series is loading at slice number '15'");

		//Step -2 Drawing annotations on new slice
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing ellipse on 10th slice of 'sagittal' and 'coronal' series" );
		viewerPage.scrollToImage(1, 10);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -100, -100,-150);	

		viewerPage.scrollToImage(3, 10);
		ellipse.drawEllipse(3, -200, -100, -100,-150);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[2] & Checkpoint[2/5]", "Verifying presence of drawn annotation");
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"verify the ellipse is present on slice 10 of first viewbox", "Ellipse is present");
		viewerPage.assertTrue(ellipse.isEllipsePresent(3),"verify the ellipse is present on slice 10 of third viewbox", "Ellipse is present");

		//Step -3  Verifying the presence of drawn annotations
		viewerPage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[3] & Checkpoint[3/5]","Verifying user is navigated to single patient list screen");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that the user is navigated back to Single patient page", "User is on "+ patientPage.getCurrentPageURL()+" page");

		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying slice position for images in each viewbox after reloading to viewer");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify that 'sagittal' series should load at slice number '5'", "'sagittal' series is loading at slice number '5'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), axial_ImageNum, "Verify that 'axial' series should load at slice number '101'", "'axial' series is loading at slice number '101'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), 10, "Verify that 'coronal' series should load at slice number '15'", "'coronal' series is loading at slice number '15'");

		//Step- 4 Changing the layout and verifying the presence of annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 2x3" );
		layout.selectLayout(layout.twoByThreeLayoutIcon);

		//Verify the images are load as per machine directives
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2] & Checkpoint[5/5]", "Verifying slice position for images selected through content selector ");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), 10, "Verify that 'sagittal' series should load at slice number '5'", "'sagittal' series is loading at slice number '5'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), 10, "Verify that 'axial' series should load at slice number '101'", "'axial' series is loading at slice number '101'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), 10, "Verify that 'coronal' series should load at slice number '15'", "'coronal' series is loading at slice number '15'");

	}


	//TC2162 - Set slice position based on machine directive- with Synchronization
	//Limitation : Not working in edge because of mouseHover function
	//	TC 2: Verify initial slice position of a series based on machine directive and GSPS (Automated). 
	@Test(groups ={"firefox","Chrome","IE11","US613","US741"})
	public void test02_US613_TC2162_US741_TC2647_verifySlicePositionBasedOnMachineDirectiveWithSync() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set slice position based on machine directive- with Synchronization");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(tdaPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		ellipse = new EllipseAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+tdaPatientName+" in viewer" );

		//Step -1
		//Verify default slice position after loading viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1] & Checkpoint[1/5]", "Verifying slice position for images in each viewbox of TeraRecon_BrainTDA patient data");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_BV_18Phase2' series should load at slice number '15'", "'TDAColorMap_BV_18Phase2' series is loading at slice number '15'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_BF_18Phase2' series should load at slice number '20'", "'TDAColorMap_BF_18Phase2' series is loading at slice number '20'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(3), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_TOT_18Phase2' series should load at slice number '25'", "'TDAColorMap_TOT_18Phase2' series is loading at slice number '25'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(4), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_RT_18Phase2' series should load at slice number '30'", "'TDAColorMap_RT_18Phase2' series is loading at slice number '30'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(5), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_TMAX_18Phase2' series should load at slice number '15'", "'TDAColorMap_TMAX_18Phase2' series is loading at slice number '15'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(6), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_TTP_18Phase2' series should load at slice number '15'", "'TDAColorMap_TTP_18Phase2' series is loading at slice number '15'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(7), TDAColorMap_BV_18Phase2_ImageNum, "Verify that 'TDAColorMap_MAP_18Phase2' series should load at slice number '15'", "'TDAColorMap_MAP_18Phase2' series is loading at slice number '15'");

		//Step -2  Drawing annotation on new slice
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing ellipse on 13th slice of second and 17th slice of third viewboxes" );
		viewerPage.scrollToImage(2, 13);
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 60, 120, -100,-120);	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[2] & Checkpoint[2/5]", "Verifying presence of drawn annotation");
		viewerPage.assertTrue(ellipse.isEllipsePresent(2),"verify the ellipse is present on slice 13 of second viewbox", "Ellipse is present");

		viewerPage.scrollToImage(3, 17);
		ellipse.drawEllipse(3, 60, 120, -100,-120);
		viewerPage.assertTrue(ellipse.isEllipsePresent(3),"verify the ellipse is present on slice 17 of third viewbox", "Ellipse is present");

		//Step -3  Verifying the drawn annotations presence
		viewerPage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[3] & Checkpoint[3/5]","Verifying user is navigated to single patient list screen");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that the user is navigated back to Single patient page", "User is on "+ patientPage.getCurrentPageURL()+" page");

		patientPage.clickOntheFirstStudy();

		viewerPage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying slice position for images in each viewbox after reloading to viewer");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 17, "Verify that 'TDAColorMap_BV_18Phase2' series should load at slice number '15'", "'TDAColorMap_BV_18Phase2' series is loading at slice number '15'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 17, "Verify that 'TDAColorMap_BF_18Phase2' series should load at slice number '20'", "'TDAColorMap_BF_18Phase2' series is loading at slice number '20'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), 17, "Verify that 'TDAColorMap_TOT_18Phase2' series should load at slice number '25'", "'TDAColorMap_TOT_18Phase2' series is loading at slice number '25'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), 17, "Verify that 'TDAColorMap_RT_18Phase2' series should load at slice number '30'", "'TDAColorMap_RT_18Phase2' series is loading at slice number '30'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), 17, "Verify that 'TDAColorMap_TMAX_18Phase2' series should load at slice number '15'", "'TDAColorMap_TMAX_18Phase2' series is loading at slice number '15'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(6), 17, "Verify that 'TDAColorMap_TTP_18Phase2' series should load at slice number '15'", "'TDAColorMap_TTP_18Phase2' series is loading at slice number '15'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(7), 17, "Verify that 'TDAColorMap_MAP_18Phase2' series should load at slice number '15'", "'TDAColorMap_MAP_18Phase2' series is loading at slice number '15'");

	}


	//TC2101 - Set default slice position based on configuration
	//TC3742 - Verify DefaultSlice position apply when local language is English
	//Limitation : Not working in edge because of mouseHover function
	@Test(groups ={"firefox","Chrome","IE11","US614"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test03_US614_SlicePosition"})
	public void test03_US614_TC2101_DE896_TC3742_verifySlicePositionBasedOnConfiguration(String percValue) throws SQLException, InterruptedException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set default slice position based on configuration");

		//Changing the default slice position in configSetting table
		DatabaseMethods db = new DatabaseMethods(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the default slice position value to :"+percValue, "");
		db.updateDefaultSlicePosition(percValue);
		db.assertEquals(db.getDefaultSlicePosition(),percValue,"verifying the default slice position percentage is :"+percValue,"verified");

		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(sqaTestingPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+sqaTestingPatientName+" in viewer" );

		//Verify default slice position 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that all viewboxes load with "+percValue+" of total slices available in them by default");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),percValue), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(2)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(2),percValue), 
				"Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),percValue), 
				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));
	}


	//TC2102 -Set default slice position based on configuration- Draw GSPS on Non-GSPS and test Synchronization
	//Limitation : Not working in edge because of mouseHover function
	//Getting fail because of new defect #674 and #676
	//	TC 3: Verify default slice position for a study with GSPS object (Automated) 
	@Test(groups ={"firefox","Chrome","IE11","US614"})
	public void test04_US614_TC2102_verifySlicePositionForGSPSDataOnNonGSPSSeries() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set default slice position based on configuration- Draw GSPS on Non-GSPS and test Synchronization");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(dRPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		ellipse = new EllipseAnnotation(driver);
		arToolbar=new ViewerARToolbox(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+dRPatientName+" in viewer" );

		//Step -1 Drawing annotation in first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1] & Checkpoint[1/6]", "Draw new annotations and verifying presence of drawn annotation on first viewbox");
		viewerPage.scrollToImage(1,  4);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);	
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"verify the ellipse is present on slice 4 of first viewbox", "Ellipse is present");

		viewerPage.scrollToImage(1,  8);
		ellipse.drawEllipse(1, -200, -50, -100,-150);
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"verify the ellipse is present on slice 8 of first viewbox", "Ellipse is present");

		viewerPage.scrollToImage(1,  12);
		ellipse.drawEllipse(1, -200, -50, -100,-150);
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"verify the ellipse is present on slice 12 of first viewbox", "Ellipse is present");

		//Step -2 Drawing annotation in second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2] & Checkpoint[2/6]", "Draw new annotations and verifying presence of drawn annotation on second viewbox");
		viewerPage.scrollToImage(2, 3);
		ellipse.drawEllipse(2, -200, -50, -100,-150);	
		viewerPage.assertTrue(ellipse.isEllipsePresent(2),"verify the ellipse is present on slice 3 of second viewbox", "Ellipse is present");

		viewerPage.scrollToImage(2, 6);
		ellipse.drawEllipse(2, -200, -50, -100,-150);
		viewerPage.assertTrue(ellipse.isEllipsePresent(2),"verify the ellipse is present on slice 6 of second viewbox", "Ellipse is present");

		viewerPage.scrollToImage(2, 9);
		ellipse.drawEllipse(2, -200, -50, -100,-150);
		viewerPage.assertTrue(ellipse.isEllipsePresent(2),"verify the ellipse is present on slice 9 of second viewbox", "Ellipse is present");

		//Navigating to browser back
		viewerPage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[3] & Checkpoint[3/6]","Verifying user is navigated to single patient list screen");

		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that the user is navigated back to Single patient page", "User is on "+ patientPage.getCurrentPageURL()+" page");

		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		ellipse = new EllipseAnnotation(driver);

		contentSelector = new ContentSelector(driver);
		//Step - 3 Reload the viewer and verify that the viewbox loads with the first slice with GSPS in it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[4] & Checkpoint[4/6]", "Verifying that the viewbox loads with first slice having GSPS data");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), "4", "Verify that the first viewbox is loading at slice number 4", "First viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), "4", "Verify that the second viewbox is loading at slice number 4 as both viewboxs are in sync", "Second viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));

		//Step 4 -Navigating through GSPS annotations and verifying the slice number in second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[5] & Checkpoint[5/6]", "Verifying the slice number in both viewboxs while navigating through GSPS annotations");
		arToolbar.selectNextfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), "8", "Verify that the first viewbox is loading at slice number 8", "First viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), "8", "Verify that the second viewbox is loading at slice number 8 as both viewboxs are in sync", "Second viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));

		arToolbar.selectNextfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), "12", "Verify that the first viewbox is loading at slice number 12", "First viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(2), "12", "Verify that the second viewbox is loading at slice number 12 as both viewboxs are in sync", "Second viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));

		//Loading new series from content selector and verifying the slice number
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying the slice number after laoding new series");
		contentSelector.selectSeriesFromSeriesTab(3,secondSeriesDescriptionDR);
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(3), "12", "Verify that the third viewbox is loading at slice number 12 as same as the image position of first viewbox", "Third viewbox is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));

	}


	//TC2103 - Set default slice position based on configuration- With GSPS data
	//Limitation : Not working in edge because of mouseHover function
	@Test(groups ={"firefox","Chrome","IE11","US614"})
	public void test05_US614_TC2103_verifySlicePositionForGSPSSeries() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set default slice position based on configuration- With GSPS data");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(LSPPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		ellipse = new EllipseAnnotation(driver);
		layout=new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+LSPPatientName+" in viewer" );

		//Step -1
		//Verify default slice position is as 50% of total number of images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that all viewboxes load with the 50th percentile slice of total slices available in them by default");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(2)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(2),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(4)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(4),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in forth viewbox", "Forth series is loading at slice number :"+viewerPage.getCurrentScrollPosition(4));

		int inputSliceNumber = 10;
		//Draw annotation on any VB and verify the default slice position
		viewerPage.scrollToImage(3, inputSliceNumber);
		ellipse.selectEllipseFromQuickToolbar(3);
		ellipse.drawEllipse(3, -200, -50, -100,-150);	
		viewerPage.assertTrue(ellipse.isEllipsePresent(3),"verify the ellipse is present on slice 10 of third viewbox", "Ellipse is present");

		//Navigating to browser back
		viewerPage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]","Verifying user is navigated to single patient list screen");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that the user is navigated back to Single patient page", "User is on "+ patientPage.getCurrentPageURL()+" page");

		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 1, "verifying default loads in 1x1", "verified");
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		//Verifying the slice loading position for each viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that the first 3 viewboxes should load with slice containing GSPS in it. All other viewboxes with no GSPS display the slice as per 'Default Slice Position'");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), inputSliceNumber, "Verify the default slice position for series in first viewbox", "First series is loading at slice number :"
				+ ":"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), inputSliceNumber, "Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"
				+viewerPage.getCurrentScrollPosition(2));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), inputSliceNumber, "Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"
				+viewerPage.getCurrentScrollPosition(3));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(5)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(5),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in forth viewbox", "Forth series is loading at slice number :"+viewerPage.getCurrentScrollPosition(5));

	}


	//TC2152 - Set default slice position based on configuration- Multiple JPEGS/PNGS in single series
	//Limitation : Not working in edge because of mouseHover function
	@Test(groups ={"firefox","Chrome","IE11","US614"})
	public void test06_US614_TC2103_verifySlicePositionForJPEGSData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set default slice position based on configuration- Multiple JPEGS/PNGS in single series");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(Quibim_BreastPerfusionPatientName);

		patientPage.clickOntheFirstStudy();
		layout=new ViewerLayout(driver);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+Quibim_BreastPerfusionPatientName+" in viewer" );

		//Verify default slice position is as 50% of total number of images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying that viewbox with multiple jpegs is displayed as per 'Default Scroll Position' in Config settings table under NSDB");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));

		//Step - 2 On layout chnage
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 2x3" );
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying the the default image loaded in each viewbox after change in layout");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));

	}


	//TC2153 - Set default slice position based on configuration- Risk and Impact
	//Limitation : Not working in edge because of mouseHover function
	@Test(groups ={"firefox","Chrome","IE11","US614"})
	public void test07_US614_TC2153_verifySlicePositionOnLayoutChange() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set default slice position based on configuration- Risk and Impact");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(LSPPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+LSPPatientName+" in viewer" );

		//Verify default slice position is as 50% of total number of images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that all viewboxes load with the 50th percentile slice of total slices available in them by default");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(2)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(2),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(4)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(4),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in forth viewbox", "Forth series is loading at slice number :"+viewerPage.getCurrentScrollPosition(4));

		//Step - 1 On double click on up
		viewerPage.doubleClickOnViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that the slice number doesn't change after double click one up");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));

		//Step - 2 On layout chnage
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 2x3" );
		layout.selectLayout(layout.twoByThreeLayoutIcon);		

		//Verify default slice position is as 50% of total number of images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying the the default image loaded in each viewbox after change in layout");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(2)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(2),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"+viewerPage.getCurrentScrollPosition(2));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getCurrentScrollPosition(3));
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(4)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(4),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in forth viewbox", "Forth series is loading at slice number :"+viewerPage.getCurrentScrollPosition(4));
		//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(5)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(5),ViewerPageConstants.SLICE_PERCENTILE), 
		//				"Verify the default slice position for series in fifth viewbox", "Fifth series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(5));

		//Loading same series in 6th viewbox
		contentSelector.selectSeriesFromSeriesTab(6, firstSeriesDescriptionLSP);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that the the default image loaded is same as first viewbox");
		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),ViewerPageConstants.SLICE_PERCENTILE), 
				"Verify the default slice position for series in sixth viewbox", "Sixth series is loading at slice number :"+viewerPage.getCurrentScrollPosition(6)+" same as slice in first viewbox");
	}


	//TC2559 - Verify if series selected through content selector is getting loaded with the slice number same as master viewbox having GSPS annotation
	@Test(groups ={"firefox","Chrome","IE11","US674"})
	public void test08_US674_TC2559_verifySlicePositionForContentSelectorSeries() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if series selected through content selector is getting loaded with the slice number same as master viewbox having GSPS annotation");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(aH4PatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		ellipse = new EllipseAnnotation(driver);
		layout=new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aH4PatientName+" in viewer" );

		//Draw ellipse on 4th slice
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing ellipse on 4th, 8th, 12th slices of VB1 and 3rd, 6th, 9th slice of VB2" );
		viewerPage.scrollToImage(1,  4);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);	

		//Draw ellipse on 8th slice
		viewerPage.scrollToImage(1,  8);
		ellipse.drawEllipse(1, -200, -50, -100,-150);

		//Draw ellipse on 12th slice
		viewerPage.scrollToImage(1,  12);
		ellipse.drawEllipse(1, -200, -50, -100,-150);

		//Draw ellipse on 3rd slice
		viewerPage.scrollToImage(2, 3);
		ellipse.drawEllipse(2, -200, -50, -100,-150);

		//Draw ellipse on 6th slice
		viewerPage.scrollToImage(2, 6);
		ellipse.drawEllipse(2, -200, -50, -100,-150);

		//Draw ellipse on 9th slice
		viewerPage.scrollToImage(2, 9);
		ellipse.drawEllipse(2, -200, -50, -100,-150);


		//Navigating to browser back
		viewerPage.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]","Verifying user is navigated to single patient list screen");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verify that the user is navigated back to Single patient page", "User is on "+ patientPage.getCurrentPageURL()+" page");

		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		contentSelector = new ContentSelector(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that the viewboxes should load with slice number 4");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 3, "Verify the default slice position for series in first viewbox", "First series is loading at slice number :"
				+ ":"+viewerPage.getCurrentScrollPosition(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"
				+viewerPage.getCurrentScrollPosition(2));

		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), 3, "Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"
				+viewerPage.getCurrentScrollPosition(3));

		//Move to image number 12 in first VB
		viewerPage.scrollToImage(1,  12);

		//select same image in VB3 from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that the default slice position for series selected through content selector is same as master VB");
		contentSelector.selectSeriesFromSeriesTab(3, firstSeriesDescriptionAH4);
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), 12, "Verify the default slice position for series selected through content selector", "New series is loading at slice number :"
				+ ":"+viewerPage.getCurrentScrollPosition(3));

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US736"})
	public void test09_US736_TC2679_VerifyGSPSLoadOnLayoutChange() throws InterruptedException, AWTException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set default slice position based on configuration- DE712");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+sqaTestingPatientName);
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(sqaTestingPatientName);

		patientPage.clickOntheFirstStudy();
		layout=new ViewerLayout(driver);
		arToolbar=new ViewerARToolbox(driver);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerPage.waitForViewerpageToLoad(5);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the scroll on viewbox 5");
		viewerPage.scrollToImage(5, 100);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the line annotation on viewbox5");
		lineWithUnit.selectDistanceFromQuickToolbar(5);
		lineWithUnit.drawLine(5, 50, 0, 100, 0);

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(sqaTestingPatientName,1,1);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the fifth viewbox is loading on 100 slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(6), 100, "GSPS slice should be loaded", "verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 100, "GSPS slice should be loaded", "verified");
		viewerPage.click(viewerPage.getViewPort(1));
		arToolbar.deleteAllAnnotation(1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing the line annotation on viewbox5 on 400th slice");
		viewerPage.scrollToImage(6, 400);
		lineWithUnit.selectDistanceFromQuickToolbar(6);
		lineWithUnit.drawLine(6, 50, 0, 100, 0);

		layout.selectLayout(layout.threeByTwoLayoutIcon);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the fifth viewbox is loading on 400 slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(6), 400, "GSPS slice should be loaded", "verified");

	}

}
