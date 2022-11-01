package com.trn.ns.test.viewer.layout;

import java.awt.AWTException;
import java.io.IOException;
import jxl.read.biff.BiffException;
import org.openqa.selenium.WebElement;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.enums.BrowserType;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I15_F41_LayoutBasicCapability-CF0304ARevD - revision-0
//F79_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  -revision-0
//Functional.NS.I15 ViewerOverlayInteraction-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerLayoutTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;	
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewerTextOverlays textOverlay;
	private ViewBoxToolPanel presetMenu;
	private ViewerSliderAndFindingMenu findingMenu;

	// Get Patient Name
	String filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	// Get Patient Name
	String filePath1=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	//Get Patient Name
	String filePath2 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneAgePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);	

	//Get Patient Name
	String filePath3 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	//Get Patient Name
	String filePath4 = Configurations.TEST_PROPERTIES.get("Picline_filepath");
	String piccLinePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	private String filePath5 = Configurations.TEST_PROPERTIES.get("SQA_Testing");
	String sqaTestingPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	
	private String filePath6 = Configurations.TEST_PROPERTIES.get("LiverTumor");
	String liverTumorPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);

	String filePath7 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);
		
	String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	String firstSeriesDescriptionGSPS_Multiseries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath"));
	
	String johnDoeFilepath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String johnDoePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, johnDoeFilepath);

	private ContentSelector contentSelector;
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);	
		
	}

	@Test(groups ={"firefox","Chrome","US60","DE758"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test01_VerifyAutoFillViewports"})
	public void test01_US60_TC697_DE758_TC2902_SelectPageLayout_SingleMonitor(String PatientName, String Modality,
			String Rows, String Columns) throws InterruptedException, IOException {
		
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US60_TC697_"+Modality+
				"verify the  function select page layout option to change window layout-Single Monitor for " + Modality + " data"+
				"<br> LayoutChange not working if empty viewboxes are there in viewer- Without GSPS data");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);
		viewerpage.waitForViewerpageToLoad();
		layout=new ViewerLayout(driver);
		textOverlay=new ViewerTextOverlays(driver);

		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//Select the layout and verifying that the layout is changing accordingly.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint TC4[2] & Checkpoint TC4[3] & Checkpoint 1-12", "Verify the layout changed based on the selected option and the series loaded in each view port according the series order for "+Modality+" data");
		layout.selectLayout(layout.oneByOneLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint1 Verify the layout changed to 1x1 and series loaded","TC697_" + Modality + "_" + "Checkpoint1");
		layout.selectLayout(layout.twoByOneLayoutIcon);
	
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint2 Verify the layout changed to 2x1 and series loaded","TC697_" + Modality + "_" + "Checkpoint2");
		layout.selectLayout(layout.oneByTwoLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint3 Verify the layout changed to 1x2 and series loaded","TC697_" + Modality + "_" + "Checkpoint3");
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint4 Verify the layout changed to 2x2 and series loaded","TC697_" + Modality + "_" + "Checkpoint4");
		layout.selectLayout(layout.threeByTwoLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint5 Verify the layout changed to 3x2 and series loaded","TC697_" + Modality + "_" + "Checkpoint5");
		layout.selectLayout(layout.twoByThreeLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint6 Verify the layout changed to 2x3 and series loaded","TC697_" + Modality + "_" + "Checkpoint6");
		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
	
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint7 Verify the layout changed to 1x1L-2x1R and series loaded","TC697_" + Modality + "_" + "Checkpoint7");
		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint8 Verify the layout changed to 1x1T-1x2B and series loaded","TC697_" + Modality + "_" + "Checkpoint8");
		layout.selectLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint9 Verify the layout changed to 2x1L-1x1R and series loaded","TC697_" + Modality + "_" + "Checkpoint9");
		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
	
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint10 Verify the layout changed to 3x1L-1x1R and series loaded","TC697_" + Modality + "_" + "Checkpoint10");
		layout.selectLayout(layout.threeByOneLAndOneByOneRLayoutIcon);
	
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint11 Verify the layout changed to 3x3 and series loaded","TC697_" + Modality + "_" + "Checkpoint11");
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint12 Verify the layout changed to 3x3 and series loaded","TC697_" + Modality + "_" + "Checkpoint12");

	}
		
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US60","US2145","E2E","F1081"})
	public void test04_US60_TC799_US2145_TC9533_VerfiyLayout() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify the  function select page layout option to change window layout-Single Monitor. <br>"+
        "Verify Layout menu display.");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9Patient, 1);		

		layout=new ViewerLayout(driver);
		viewerpage.clickUsingAction(layout.gridIcon);
		
		
		// Verifying both rows layout icons
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the first row is having 6 common layouts.");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.oneByOneLayoutIcon), "Verifying first layout icon in first row", "First layout icon in first row is 1x1");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.twoByOneLayoutIcon), "Verifying second layout icon in first row", "Second layout icon in first row is 2x1");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.oneByTwoLayoutIcon), "Verifying third layout icon in first row", "Third layout icon in first row is 1x2");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.twoByTwoLayoutIcon), "Verifying fourth layout icon in first row", "Fourth layout icon in first row is 2x2");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.threeByTwoLayoutIcon), "Verifying fifth layout icon in first row", "Fifth layout icon in first row is 3x2");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.twoByThreeLayoutIcon), "Verifying sixth layout icon in first row", "Sixth layout icon in first row is 2x3");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify the second row is having 6 less common layouts including Asymmetric Layouts.");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.oneByOneLAndThreeByOneRLayoutIcon), "Verifying first layout icon in second row", "First layout icon in second row is 1x1L-3x1R");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.oneByOneTAndOneByTwoBLayoutIcon), "Verifying third layout icon in second row", "Third layout icon in second row is 1x1T-1x2B");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.twoByOneLAndOneByOneRLayoutIcon), "Verifying fourth layout icon in second row", "Fourth layout icon in second row is 2x1L-1x1R");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.threeByOneLAndOneByOneRLayoutIcon), "Verifying fifth layout icon in second row", "Fifth layout icon in second row is 3x1L-1x1R");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.threeByThreeLayoutIcon), "Verifying sixth layout icon in second row", "Sixth layout icon in second row is 3x3");
	

	}

	@Test(groups ={"firefox","Chrome","Edge","US136"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test01_VerifyAutoFillViewports" })
	public void test05_US136_TC179_VerfiyDefaultLayout(String PatientName, String Modality,
			String Rows, String Columns) throws InterruptedException, BiffException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify that default layout is displayed when user relaunches the application");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);		


		//verify default layout
		int defaultCount = viewerpage.getNumberOfCanvasForLayout();
		int expectedCount = Integer.parseInt(Rows)* Integer.parseInt(Columns);
		viewerpage.assertEquals(defaultCount,expectedCount, "Verifying layout", "Checkpoint : verifying default layout for modality " + Modality);

	}


	//TC923	Load and display Bone Age Engine WIA Results in the NS Viewer 2x2 layout - Layout Coverage
	@Test(groups ={"dbConfig","US296"})
	public void test07_US296_TC923_TC1246_VerfiyLoadAndDisplayBoneageWIAResultLayoutCoverage() throws InterruptedException{
		String testcaseName = "US296_TC923_TC1246";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Load and display Bone Age Engine WIA Results in the NS Viewer 1x2 layout - Layout Coverage");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		

		layout=new ViewerLayout(driver);
		presetMenu=new ViewBoxToolPanel(driver);
		//verify default layout
		int defaultCount = viewerpage.getNumberOfCanvasForLayout();
		int expectedCount = layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT);
		viewerpage.assertEquals(defaultCount,expectedCount, "Checkpoint TC7[2]: Verifying layout", "Checkpoint : verifying default layout for Boneage");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getWindowCenterValueOverlay(4)),"Verifying patient data", "Checkpoint: verifying patientdata displayed on right viewbox (if WL/WC component is present)" );
		viewerpage.assertFalse(viewerpage.isElementPresent(presetMenu.getWindowCenterValueOverlay(1)),"Verifying Result data", "Checkpoint: verifying Result displayed on left  viewbox (if WL/WC component is not present)" );

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		int defaultCountFor3x3 = viewerpage.getNumberOfCanvasForLayout();
		int expectedCountFor3x3 =  layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT);;

		viewerpage.assertEquals(defaultCountFor3x3,expectedCountFor3x3, "Checkpoint TC7[1]: Verifying layout", "Checkpoint : verifying 3x3 layout for Boneage");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying layout 3x3 contains all results and patient data.","Checkpoint_3x3_layout"+ "_" +testcaseName);	

		layout.selectLayout(layout.oneByTwoLayoutIcon);
		int defaultCountFor2x2 = viewerpage.getNumberOfCanvasForLayout();
		int expectedCountFor2x2 =  layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT);

		viewerpage.assertEquals(defaultCountFor2x2,expectedCountFor2x2, "Verifying layout", "Checkpoint : verifying 2x2 layout for Boneage");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying layout 2x2  contains all results and patient data.","Checkpoint_2x2_layout"+ "_" +testcaseName);	
	
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","dbConfig","DE45"})
	public void test08_DE45_TC358_verifyLayoutOptionBoxLocation() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify layout options box on viewer page is aligned when user changes the browser window's size");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);

		patientPage.clickOntheFirstStudy();

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateValueInSelectorTypeTable("TRUE");

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		layout=new ViewerLayout(driver);
		layout.openLayoutContainer();

		if (!Configurations.TEST_PROPERTIES.get("browserName").equalsIgnoreCase(BrowserType.EDGE.getBrowserValue())) 
			viewerpage.assertTrue(layout.layoutContainer.isDisplayed(), "Verifying grid layout should be opened", "Grid layout container is open");

		//Getting the top left corner point of layout container box
		int xTopLeftCornerOfLayoutContainerBeforeResize = layout.layoutContainer.getLocation().getX();
		int yTopLeftCornerOfLayoutContaineBeforeResizer = layout.layoutContainer.getLocation().getY();

		//Getting height and width of layout container box
		int heightOfLayoutContainerBoxBeforeResize = layout.layoutContainer.getSize().getHeight();
		int widthOfLayoutContainerBoxBeforeResize = layout.layoutContainer.getSize().getWidth();

		//resizing browser window
		viewerpage.resizeBrowserWindow(500, 500);

		int xTopLeftCornerOfLayoutContainerAfterResize = layout.layoutContainer.getLocation().getX();
		int yTopLeftCornerOfLayoutContainerAfterResize = layout.layoutContainer.getLocation().getY();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying top left corner point of layout container box" );
		viewerpage.assertEquals(xTopLeftCornerOfLayoutContainerBeforeResize, xTopLeftCornerOfLayoutContainerAfterResize, "Verifying x-coord of top left corner point of layout container", "Verfied x-coord of top left corner point of layout container remains same on window resize");
		viewerpage.assertEquals(yTopLeftCornerOfLayoutContaineBeforeResizer, yTopLeftCornerOfLayoutContainerAfterResize, "Verifying y-coord of top left corner point of layout container", "Verfied y-coord of top left corner point of layout container remains same on window resize");

		int heightOfLayoutContainerBoxAfterResize = layout.layoutContainer.getSize().getHeight();
		int widthOfLayoutContainerBoxAfterResize = layout.layoutContainer.getSize().getWidth();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying height and width of layout container box" );
		viewerpage.assertEquals(heightOfLayoutContainerBoxBeforeResize, heightOfLayoutContainerBoxAfterResize, "Verifying height of layout container box on window resize", "Verfied height of layout container box remains same on window resize");
		viewerpage.assertEquals(widthOfLayoutContainerBoxBeforeResize, widthOfLayoutContainerBoxAfterResize, "Verifying width of layout container box on window resize", "Verfied width of layout container box remains same on window resize");
	}

	//Verify Load and display Bone Age Engine WIA Results in the NS Viewer 1x2 layout - Input Data Coverage

	@Test(groups ={"dbConfig","US296"})
	public void test09_US296_TC924_VerfiyLoadAndDisplayBoneageWIAResultInputDataCoverage() throws InterruptedException, IOException{
		String testcaseName = "US296_TC924";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Load and display Bone Age Engine WIA Results in the NS Viewer 1x2 layout - Input Data Coverage");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		

		textOverlay=new ViewerTextOverlays(driver);
		//Verify Text overlay value for full selection
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint: Overlay mode", "Validate full overlay labels on viebox");
		verifyFullOverlayDataForBoneage(filePath2, 4);

		//Verify Text overlay value for minimum selection
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint: Overlay mode", "Validate minimum overlay labels on viebox");
		verifyMinimumOverlayDataForBoneage(filePath2, 4);

		///Verify Text overlay value for default selection
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint: Overlay mode", "Validate default overlay labels on viebox");
		verifyDefaultOverlayDataForBoneage(filePath2, 4);

		//Performing and verifying zoom on DICOM image (viewbox 2)
		performAndverifyZoom(viewerpage.getViewPort(4), 4, testcaseName, "For Dicom");

		//Performing and verifying window leveling 
		performAndverifyWindowLeveling(viewerpage.getViewPort(4), 4, testcaseName, "For Dicom");

		//Performing and verifying panning on DICOM Image (viewbox 2)
		performAndverifyPanSynchronization(viewerpage.getViewPort(4), testcaseName, "For Dicom");

		//Draw and verify linear measurement
		drawAndVerifyLinearMeasurement(viewerpage.getViewPort(4), 4, testcaseName, "For Dicom");

	}

	//Verify Load and display Bone Age Engine WIA Results in the NS Viewer 1x2 layout - Result Data Coverage
	@Test(groups ={"dbConfig","US296"})
	public void test10_US296_TC925_VerfiyLoadAndDisplayBoneageWIAResultResultDataCoverage() throws InterruptedException, IOException{
		String testcaseName = "US296_TC925";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify Load and display Bone Age Engine WIA Results in the NS Viewer 1x2 layout - Result Data Coverage");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		
		layout=new ViewerLayout(driver);

		textOverlay=new ViewerTextOverlays(driver);
		///Verify Text overlay value for default selection
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint: Overlay mode", "Validate default overlay labels on viebox");
		verifyDefaultOverlayDataForBoneage(filePath2, 4);

		//Verify Text overlay value for full selection
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint: Overlay mode", "Validate full overlay labels on viebox");
		verifyFullOverlayDataForBoneage(filePath2, 4);

		//Performing and verifying zoom on non DICOM image (viewbox 1)
		performAndverifyZoom(viewerpage.getViewPort(1), 1, testcaseName, "For Non Dicom");

		//Performing and verifying panning on non DICOM Image (viewbox 1)
		performAndverifyPanSynchronization(viewerpage.getViewPort(1), testcaseName, "For Non Dicom");

		//Performing and verifying WL on non DICOM Image (viewbox 1)
		verifyWindowLevelForNonDicomImage(viewerpage.getViewPort(1), testcaseName, "For Non Dicom");


		//Performing and verifying zoom on DICOM image (viewbox 2)
		performAndverifyZoom(viewerpage.getViewPort(4), 4, testcaseName, "For Dicom");

		//Performing and verifying panning on DICOM Image (viewbox 2)
		performAndverifyPanSynchronization(viewerpage.getViewPort(4), testcaseName, "For Dicom");

		//Draw and verify linear measurement
		drawAndVerifyLinearMeasurement(viewerpage.getViewPort(4), 4, testcaseName,  "For Dicom");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		int defaultCountFor2x2 = viewerpage.getNumberOfCanvasForLayout();
		int expectedCountFor2x2 =  4;
		viewerpage.assertEquals(defaultCountFor2x2,expectedCountFor2x2, "Verifying layout", "Checkpoint : verifying 2x2 layout for Boneage");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying layout 2x2 contains all results and patient data.","Checkpoint_2x2_layout"+ "_" +testcaseName);	

	}

	//Verify zoom synchronization on DICOM + jpeg images
	@Test(groups ={"dbConfig","US296"})
	public void test11_US296_TC1255_VerfiyZoomForDICOMAndNonDICOMImages() throws InterruptedException, IOException{
		String testcaseName = "DE268_TC1255";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify zoom on DICOM and non DICOM images");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		


		//Performing and verifying zoom on non DICOM image (viewbox 1)
		performAndverifyZoom(viewerpage.getViewPort(1), 1, testcaseName, "For Non Dicom");

		//Performing and verifying zoom on DICOM Image (viewbox 2)
		performAndverifyZoom(viewerpage.getViewPort(4), 4, testcaseName, "For Dicom");

	}

	//Verify PAN synchronization on DICOM + jpeg images
	@Test(groups ={"dbConfig","US296"})
	public void test12_US296_TC1273_VerfiyPanForDICOMAndNonDICOMImages() throws InterruptedException, IOException{
		String testcaseName = "DE268_TC1273";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN on DICOM and non DICOM images");
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		


		//Performing and verifying panning on non DICOM Image (viewbox 1)
		performAndverifyPanSynchronization(viewerpage.getViewPort(1), testcaseName, "For Non Dicom");

		//Performing and verifying panning on DICOM image (viewbox 4)
		performAndverifyPanSynchronization(viewerpage.getViewPort(4), testcaseName, "For Dicom");

	}

	//Verify scroll synchronization on DICOM + jpeg images
	@Test(groups ={"DE268"})
	public void test13_DE268_TC1277_VerfiyScrollForDICOMAndNonDICOMImages() throws InterruptedException, IOException{
		String testcaseName = "DE268_TC1277";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN on DICOM and non DICOM images");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		


		String beforeAction = viewerpage.getCurrentScrollPosition(4);

		//Performing and verifying scroll on non DICOM (JPEG) image (viewbox 4)
		viewerpage.scrollDownToSliceUsingKeyboard(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying scroll should not performed on DICOM images."+ "_" +testcaseName+ "_" + "Dicom Image");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPosition(4), beforeAction, "Verifying scroll should not performed on DICOM images", "Verified scroll should not performed on DICOM image");
	}

	
	//TC1275  Verify PAN synchronization can be disabled using space bar on DICOM + jpeg images

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE268"})
	public void test17_DE268_TC1275_verifyPanSyncStoppedWhenSpaceBarPressed() throws InterruptedException, AWTException 
	{
		String testcaseName = "DE268_TC1275";
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN synchronization can be disabled using space bar on DICOM + jpeg images");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		

		contentSelector = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		layout=new ViewerLayout(driver);
		//changing layout to 3x3 
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.closingConflictMsg();
		//viewerpage.waitForAllImagesToLoad();

		//Selecting dicom image in 3rd viewbox to check sync
		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
		contentSelector.selectSeriesFromSeriesTab(3, firstSeriesDescription);

		contentSelector.selectSeriesFromSeriesTab(4, firstSeriesDescription);

		//Selecting non dicom images

		String firstResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2);

		contentSelector.selectResultFromSeriesTab(6, firstResultDescription);
		contentSelector.openAndCloseSeriesTab(false);

		//performing sync off
		viewerpage.performSyncONorOFF();

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(2));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify All series should NOT PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying  All series should NOT PAN synchronously. Only image on which PAN was selected should PAN.", testcaseName);
	}
	
	//TC1279  Verify scroll synchronization can be disabled using space bar on DICOM + jpeg images

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE268"}, enabled = false)
	public void test18_DE268_TC1279_VerfiyScrollSynDisabledForDICOMAndNonDICOMImagesOnParentChildWindow() throws InterruptedException, IOException{
		

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization can be disabled using space bar on DICOM + jpeg images");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		


		//Zoom value of dicom image
		String beforePerformActionForJPEG = viewerpage.getTextOfResultIDForViewbox(1);
		String beforePerformAction = viewerpage.getCurrentScrollPosition(4);

		//performing sync off
		viewerpage.performSyncONorOFF();

		layout=new ViewerLayout(driver);
		//changing layout to 3x3 
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		//Performing and verifying scroll on non DICOM (JPEG) image (viewbox 1)
		viewerpage.scrollDownToSliceUsingKeyboard(2);

		//Zoom value of dicom image
		String afterPerformActionForJPEG = viewerpage.getTextOfResultIDForViewbox(1);
		String afterPerformAction = viewerpage.getCurrentScrollPosition(4);

		//on performing scroll on jpeg result id should change
		viewerpage.assertEquals(beforePerformActionForJPEG,afterPerformActionForJPEG, "Verifying scroll sync on Dicom images and non dicom images", "Verified scroll sync on Dicom images and non dicom images JPEG scrolled");

		//on performing scroll on jpeg, scroll number should not change
		viewerpage.assertEquals(beforePerformAction, afterPerformAction, "Verifying scroll sync on Dicom images and non dicom images", "Verified scroll sync on Dicom images and non dicom images Dicom not scrolled");
	}

	//TC1271  Verify zoom synchronization can be disabled using space bar on DICOM + jpeg images  
	@Test(groups ={"Chrome","DE268"})
	public void test19_DE268_TC1271_VerfiyZoomSynForDICOMAndNonDICOMImagesOnLayoutChange() throws InterruptedException, IOException, AWTException{
		

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify zoom synchronization can be disabled using space bar on DICOM + jpeg images");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		

		layout=new ViewerLayout(driver);
		presetMenu=new ViewBoxToolPanel(driver);
		//changing layout to 1X3 
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.waitForViewerpageToLoad(4);
		contentSelector = new ContentSelector(driver);
		//Selecting dicom image in 3rd viewbox to check sync
		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
		contentSelector.selectSeriesFromSeriesTab(3, firstSeriesDescription);

		contentSelector.selectSeriesFromSeriesTab(4, firstSeriesDescription);

		//performing sync off
		viewerpage.performSyncONorOFF();

		//Performing and verifying zoom on non DICOM (JPEG) image (viewbox 1)
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(2));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2), 0, 0, -100,-100);


		//Zoom value of dicom image
		int afterPerformAction = presetMenu.getZoomValue(2);

		viewerpage.assertNotEquals(afterPerformAction, presetMenu.getZoomValue(3), "Verifying zoom syn on Dicom images on changing layout", "Verified zoom syn on Dicom images on changing layout");
		viewerpage.assertNotEquals(afterPerformAction, presetMenu.getZoomValue(4), "Verifying zoom syn on Dicom images on changing layout", "Verified zoom syn on Dicom images on changing layout");
		viewerpage.assertNotEquals(afterPerformAction, presetMenu.getZoomValue(1), "Verifying zoom syn on Dicom images on changing layout", "Verified zoom syn on Dicom images on changing layout");
	}
	
	//	Load and display Single WIA Results and use a Binary Selector to Confirm/Reject a Result - Layout Coverage   
//	TC6: Layout Coverage (Automated) 
	@Test(groups ={"Chrome","US306","US352"})
	public void test20_US306_TC930_US352_TC1244_verifyLayoutChangeOnPiclineData() throws InterruptedException {
	
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify default layout structure and image rendering on layout change on Picline data");

		String filePath = Configurations.TEST_PROPERTIES.get("Picline_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 2);		
        presetMenu=new ViewBoxToolPanel(driver);
        findingMenu=new ViewerSliderAndFindingMenu(driver);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[1] : Checkpoint[1/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.getAcceptRejectToolBar(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify text overlay on orginal Patient Data for default annotation");
		verifyDefaultOverlayDataForBoneage(filePath4, 2);

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.getAcceptRejectToolBar(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay on orginal Patient Data for Minimum annotation");
		verifyMinimumOverlayDataForBoneage(filePath4, 2);

		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.getAcceptRejectToolBar(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for Full annotation");
		verifyMinimumOverlayDataForBoneage(filePath4, 2);

	}

	//	Load and display Single WIA Results and use a Binary Selector to Confirm/Reject a Result - Viewer Interaction Coverage    
	@Test(groups ={"Chrome","US306"})
	public void test21_US306_TC931_verfiyViewerInteraction() throws InterruptedException {
		
		String testcaseName = "US306_TC931";
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewer interaction (Window Leveling, Pan, Zoom and Distance Measurement) on Picline data");

		String filePath = Configurations.TEST_PROPERTIES.get("Picline_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 2);		


		//Performing and verifying windowleveling 
		performAndverifyWindowLeveling(viewerpage.getViewPort(2), 2, testcaseName, "_vb2_forDicom");

		//Performing and verifying panning on DICOM Image (viewbox 2)
		performAndverifyPanSynchronization(viewerpage.getViewPort(2), testcaseName, "_vb2_forDicom");			

		//Performing and verifying zoom on JPEG image (viewbox 1)
		performAndverifyZoom(viewerpage.getViewPort(2), 2, testcaseName, "_vb2_forNonDicom");

		//Draw and verify linear measurement
		drawAndVerifyLinearMeasurement(viewerpage.getViewPort(2), 2, testcaseName, "_vb2_forDicom");

		//Performing and verifying panning on DICOM Image (viewbox 2)
		performAndverifyPanSynchronization(viewerpage.getViewPort(1), testcaseName, "_vb1_forDicom");

		//Performing and verifying zoom on JPEG image (viewbox 1)
		performAndverifyZoom(viewerpage.getViewPort(1), 1, testcaseName, "_vb1_forNonDicom");	
	}

	@Test(groups ={"Chrome","US159"})
	public void test22_US159_TC152_verifyFontOnViewerPage() throws InterruptedException, IOException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Font on all Text overlay on Viewer page");
		
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		
		
		textOverlay=new ViewerTextOverlays(driver);
		//Verify Font on Text overlay value for full selection
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1", "Verify Font of all the text Overlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getPatientNameOverlay(4)) , "Verifying font of patient name textoverlay", "verified font of  patient name textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getSeriesDescriptionOverlay(4)),"Verifying font of series description textoverlay", "verified font of  series description textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getStudyDateTimeOverlay(4)), "Verifying font of study date time textoverlay", "verified font of study date time textoverlay");

		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getStudyDescriptionOverlay(4)),"Verifying font of study description textoverlay", "verified font of study description textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getPatientSexOverlay(4)) , "Verifying font of patient sex textoverlay", "verified font of patient sex textoverlay");
//		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getPatientClassOverlay(4)), "Verifying font of patient class textoverlay", "verified font patient class textoverlay");

		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getImageMatrixOverlay(4)),"Verifying font of image matrx textoverlay", "verified font of image matrix textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getImagePositionOverlay(4)),"Verifying font of  image position textoverlay", "verified font of image position textoverlay");

		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getSliceThicknessOverlay(4)),"Verifying font of  slice thickness textoverlay", "verified font of slice thickness textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getSliceLocationOverlay(4)), "Verifying font of  slice location textoverlay", "verified font of  slice location name textoverlay");	

		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getKvpOverlay(4)), "Verifying font of  KVP textoverlay", "verified font of  KVP textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getFOVOverlay(4)),"Verifying font FOV textoverlay", "verified font of  FOV textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getSliceInfo(4)), "Verifying font of current scroll position textoverlay", "verified font of current scroll position textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getWindowWidthValueOverlay(4)),"Verifying font of  Window width textoverlay", "verified font of Window width textoverlay");
		viewerpage.assertTrue(viewerpage.verifyFont(viewerpage.getWindowCenterValueOverlay(4)), "Verifying font of  Window center textoverlay", "verified font of  Window center textoverlay");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US352"})
	public void test23_US352_TC1242_VerfiyTextOverlayForPICCLineData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Text overlay for PICC Line data");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(piccLinePatient, 2);		
		presetMenu=new ViewBoxToolPanel(driver);


		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default Annotation mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1] & Checkpoint[2/6]", "Verify text overlay on orginal Patient Data for default annotation");
		verifyDefaultOverlayDataForBoneage(filePath4, 2);

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Minimum Annotation mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");


		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay on orginal Patient Data for Minimum annotation");
		verifyMinimumOverlayDataForBoneage(filePath4, 2);

		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2] & Checkpoint[5/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Full Annotation mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");


		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for Full annotation");
		verifyMinimumOverlayDataForBoneage(filePath4, 2);

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US352"})
	public void test24_US352_TC1243_VerfiyTextOverlayOnResutSetForBoneAge() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Text overlay on Result Set for BoneAge");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);		

		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);
		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default mode
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in default Annotation mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");


		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint[2/6]", "Verify text overlay on orginal Patient Data for default annotation");
		verifyDefaultOverlayDataForBoneage(filePath2, 4);

		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint[3/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Minimum Annotation mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");


		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay on orginal Patient Data for Minimum annotation");
		verifyMinimumOverlayDataForBoneage(filePath2, 4);

		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		//Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set for minimum annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2]: Checkpoint[5/6]", "Verify Content Selector, Zoom Overlay and Binary Selector appear on Result Set in Full Annotation mode");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying Result Overlay appear on Result set", "Result Overaly appear on Result Overaly");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");


		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for Full annotation");
		verifyMinimumOverlayDataForBoneage(filePath2, 4);
	} 

	//Validate that Bone Age Patient data loads as expected in different Layouts with the Presentation mode set to Stack.
	@Test(groups ={"IE11","Chrome","firefox","Edge","US352","DE1382","Positive"})
	public void test25_US352_TC1245_DE1382_TC5746_VerfiBoneAgeLoadForDifferent_Layout() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Text Overlay on Result Set for BoneAge for Different layout"
				+ "<br> Verify that garbage characters are not getting displayed as orientation markers for boneage data after layout change");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgePatient, 4);
		layout=new ViewerLayout(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		//Verify Default layout for Bone-Age Data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify Default layout for Boneage Data is 1X2");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC6[1] : Verifying default layout for BoneAge","TC1245_CheckPoint1");

		//Verify Atlas appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify Atlas appear on Result set on viewbox 1 by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(1)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Atlas-1 appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify Atlas+1 appear on Result set on viewbox 2 by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(2)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
				
		//Verify Atlas appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify Atlas-1 appear on Result set on viewbox 3 by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(3)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify text overlay on orginal Patient Data for default annotation");
		verifyDefaultOverlayDataForBoneage(filePath2, 4);

		//Change layout to 3X3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(4),"Verifying 3x3 layout","TC32_CheckPoint1");

		
		//Verify Atlas appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[2]: Checkpoint[6/9]", "Verify Atlas appear on Result set on viewbox 1 by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(1)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Atlas-1 appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify Atlas+1 appear on Result set on viewbox 2 by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(2)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");
				
		//Verify Atlas appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify Atlas-1 appear on Result set on viewbox 3 by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(3)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
		viewerpage.assertTrue(viewerpage.isElementPresent(presetMenu.getZoomOverlay(1)), "Verifying Zoom Overlay on Result Set", "Zoom Overlay appear on Result Set");

		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify text overlay on orginal Patient Data for default annotation");
		verifyDefaultOverlayDataForBoneage(filePath2, 4);
		
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(4),"Verifying 2x3 layout","TC32_CheckPoint2");
		
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(4),"Verifying 3x2 layout","TC32_CheckPoint3");

		
	}
	
	private void verifyWindowLevelForNonDicomImage(WebElement element, String testcaseName, String imageType) throws  InterruptedException {
		viewerpage.selectWindowLevelFromQuickToolbar(element);
		viewerpage.dragAndReleaseOnViewer(element, 0, 39, -10, -20);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify  WW/WL is applied." + "_" +testcaseName+ "_" +imageType);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WW/WL.","Checkpoint_WW_WL"+ "_" +testcaseName+ "_" +imageType);

	}

	private void drawAndVerifyLinearMeasurement(WebElement element, int viewbox,  String testcaseName, String imageType ) throws  InterruptedException {
		// Clicking on the 3 dots icon on radial bar -> allIcon -> measurement tab -> distance option
		//		viewerpage.selectLinearMeasurementFromContextMenu(element);
		SimpleLine line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(element);
		// Drawing a horizontal line
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint_Linear", "Drawing the measurement on coordinates (0,0,100,0)" );
		viewerpage.dragAndReleaseOnViewerWithClick(10,10, 100, 100);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verify the measurement drawn @ coordinates (0,0,100,0)", "Viewbox_Measurement_ContextMenu" +testcaseName +"_" +imageType);
	}

	private void performAndverifyPanSynchronization(WebElement element, String testcaseName, String imageType) throws InterruptedException {
		viewerpage.selectPanFromQuickToolbar(element);
		viewerpage.dragAndReleaseOnViewer(element, 0, 0, 300, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","Checkpoint_Right"+ "_" +testcaseName+ "_" +imageType);
		viewerpage.dragAndReleaseOnViewer(element, 300, 0, -600, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","Checkpoint_left"+ "_" +testcaseName+ "_" +imageType);
		viewerpage.dragAndReleaseOnViewer(element, -300, 0, 300, -100);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","Checkpoint_Top"+ "_" +testcaseName+ "_" +imageType);
		viewerpage.dragAndReleaseOnViewer(element, 0, -100, 0, 200);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","Checkpoint_Bottom"+ "_" +testcaseName+ "_" +imageType);	
	}

	private void performAndverifyWindowLeveling(WebElement element, int elementForWL , String testcaseName, String imageType) throws  InterruptedException {
		String valueBeforeWL = viewerpage.getWindowWidthValText(elementForWL);
		viewerpage.selectWindowLevelFromQuickToolbar(element);
		viewerpage.dragAndReleaseOnViewer(element, 0, 39, -10, -20);
		String valueAfterWL= viewerpage.getWindowWidthValText(elementForWL);
		viewerpage.assertFalse(valueAfterWL.equals(valueBeforeWL),"", "Values are same");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify panned location should not be reset when WW/WL is applied." + "_" +testcaseName+ "_" +imageType);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WW/WL.","Checkpoint_WW_WL"+ "_" +testcaseName+ "_" +imageType);
	}

	private void performAndverifyZoom(WebElement element, int zoomLevelViewboxNumber, String testcaseName, String imageType) throws  InterruptedException{
		viewerpage.selectZoomFromQuickToolbar(element);
		presetMenu=new ViewBoxToolPanel(driver);
		int beforeZoom = presetMenu.getZoomValue(zoomLevelViewboxNumber);
		viewerpage.dragAndReleaseOnViewer(element, 0, 0, -100,-100);
		int afterZoom =  presetMenu.getZoomValue(zoomLevelViewboxNumber);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify images should Zoom."+ "_" +testcaseName+ "_" +imageType);
		viewerpage.assertTrue(beforeZoom != afterZoom, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should Zoom.","Checkpoint Zoom"+ "_" +testcaseName+ "_" +imageType);
	}

	public void verifyDefaultOverlayDataForBoneage(String filePath, int viewNum) {
		presetMenu=new ViewBoxToolPanel(driver);
		viewerpage.assertTrue(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath).equalsIgnoreCase(viewerpage.getPatientNameOverlayText(viewNum)) , "Verifying patient name textoverlay", "verified patient name textoverlay");
		viewerpage.assertTrue(("ID: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath)).equalsIgnoreCase(viewerpage.getPatientIDOverlayText(viewNum)), "Verifying patient id textoverlay", "verified patient id textoverlay");	
		viewerpage.assertTrue(DataReader.getStudyDetails(PatientXMLConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDateTimeOverlayText(viewNum)), "Verifying study date time textoverlay", "verified tudy date time textoverlay");
		viewerpage.assertTrue(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_SEX_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientSexOverlayText(viewNum)) , "Verifying patient sex textoverlay", "verified patient sex textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getCurrentScrollPosition(viewNum)),"Verifying image current scroll textoverlay", "verified image current scroll textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowWidthValText(viewNum)),"Verifying window width textoverlay", "verified window width textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowCenterText(viewNum)), "Verifying window center textoverlay", "verified window center textoverlay");
		viewerpage.assertTrue(presetMenu.getZoomLevelValue(viewNum) != null, "Verifying zoom value", "verified zoom value");
	}

	public void verifyMinimumOverlayDataForBoneage(String filePath, int viewNum) {
		presetMenu=new ViewBoxToolPanel(driver);
		viewerpage.assertTrue(("ID: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath)).equalsIgnoreCase(viewerpage.getPatientIDOverlayText(viewNum)), "Verifying patient name textoverlay", "verified patient name textoverlay");
		viewerpage.assertTrue(DataReader.getStudyDetails(PatientXMLConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDateTimeOverlayText(viewNum)), "Verifying study date time textoverlay", "verified tudy date time textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getCurrentScrollPosition(viewNum)), "Verifying image current scroll textoverlay", "verified image current scroll textoverlay");
		viewerpage.assertTrue((DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMMAX_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getImageMaxScrollPositionOverlayText(viewNum)), "Verifying image max scroll textoverlay", "verified image max scroll textoverlay");
		viewerpage.assertTrue(presetMenu.getZoomLevelValue(viewNum) != null,"Verifying zoom value", "verified zoom value");
	}

	public void verifyFullOverlayDataForBoneage(String filePath, int viewNum) {
		presetMenu=new ViewBoxToolPanel(driver);
		viewerpage.assertTrue(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath).equalsIgnoreCase(viewerpage.getPatientNameOverlayText(viewNum)) , "Verifying patient name textoverlay", "verified patient name textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, "STUDY01", "STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(viewNum)),"Verifying series description textoverlay", "verified series description textoverlay");
		viewerpage.assertTrue(DataReader.getStudyDetails(PatientXMLConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDateTimeOverlayText(viewNum)), "Verifying study date time textoverlay", "verified study date time textoverlay");

		viewerpage.assertTrue(DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(viewerpage.getStudyDescriptionOverlayText(viewNum)),"Verifying study description textoverlay", "verified study description textoverlay");
		viewerpage.assertTrue(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_SEX_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientSexOverlayText(viewNum)) , "Verifying patient sex textoverlay", "verified patient sex textoverlay");
//		viewerpage.assertTrue(DataReader.getPatientDetails(NSConstants.PATIENT_CLASS_TEXTOVERLAY, filePath).equalsIgnoreCase(viewerpage.getPatientClassOverlayText(viewNum)), "Verifying patient class textoverlay", "verified patient class textoverlay");

		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_MATRIX_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImageMatrixOverlayText(viewNum)),"Verifying image matrx textoverlay", "verified image matrix textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getImagePositionOverlayText(viewNum)),"Verifying image position textoverlay", "verified image position textoverlay");

		viewerpage.assertTrue(("Thickness: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICE_THICKNESS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getSliceThicknessOverlayText(viewNum)),"Verifying slice thickness textoverlay", "verified slice thickness textoverlay");
		viewerpage.assertTrue(("Location: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICELOCATION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getSliceLocationOverlayText(viewNum)), "Verifying slice location textoverlay", "verified slice location name textoverlay");	

		viewerpage.assertTrue(("Kvp: "+DataReader.getSeriesDesc(PatientXMLConstants.KVP_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getKvpOverlayText(viewNum)), "Verifying KVP textoverlay", "verified KVP textoverlay");
		viewerpage.assertTrue(("FOV: "+DataReader.getSeriesDesc(PatientXMLConstants.FOV_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getFOVOverlayText(viewNum)),"Verifying FOV textoverlay", "verified FOV textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getCurrentScrollPosition(viewNum)), "Verifying current scroll position textoverlay", "verified current scroll position textoverlay");
		viewerpage.assertTrue((DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMMAX_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getImageMaxScrollPositionOverlayText(viewNum)), "Verifying MAX scroll postion textoverlay", "verified MAX scroll postion textoverlay");
		viewerpage.assertTrue(("mAs: "+DataReader.getSeriesDesc(PatientXMLConstants.MAS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(viewerpage.getmAsOverlayText(viewNum)),"Verifying mAS textoverlay", "verified mA textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowWidthValText(viewNum)),"Verifying Window width textoverlay", "verified Window width textoverlay");
		viewerpage.assertTrue(DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(viewerpage.getWindowCenterText(viewNum)), "Verifying Window center textoverlay", "verified Window center textoverlay");
		viewerpage.assertTrue(presetMenu.getZoomLevelValue(viewNum) != null, "Verifying zoom value", "verified zoom value");
	}

	//Verify the text overlay on layout change
	@Test(groups ={"IE11","Chrome","firefox", "Chrome","US334"})
	//Not working on IE11, FireFox 
	//TC4: Move Overlay level control under TeraRecon Icon Menu –(Automated) 
	public void test26_US334_TC1106_verifyTextOverlayOnLayoutChange() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Risk Coverage");
		
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9Patient, 4);	
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 1-12", "Verify the layout changed based on the selected option and the series loaded in each view port according the series order for data");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]: Checkpoint TC4[2]: Checkpoint 1 Verify the layout changed to 1x1 and series loaded","TC1106_Checkpoint1");

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 2 Verify the layout changed to 2x1 and series loaded","TC1106_Checkpoint2");

		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 3 Verify the layout changed to 1x2 and series loaded","TC1106_Checkpoint3");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 4 Verify the layout changed to 2x2 and series loaded","TC1106_Checkpoint4");

		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 5 Verify the layout changed to 3x2 and series loaded","TC1106_Checkpoint5");

		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]: Checkpoint TC4[2]: Checkpoint 6 Verify the layout changed to 2x3 and series loaded","TC1106_Checkpoint6");

		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 7 Verify the layout changed to 1x1L-3x1R and series loaded","TC1106_Checkpoint7");

		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]: Checkpoint TC4[2]: Checkpoint 8 Verify the layout changed to 1x1L-2x1R and series loaded","TC1106_Checkpoint8");

		layout.selectLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 9 Verify the layout changed to 1x1T-1x2B and series loaded","TC1106_Checkpoint9");

		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 10 Verify the layout changed to 2x1L-1x1R and series loaded","TC1106_Checkpoint10");

		layout.selectLayout(layout.threeByOneLAndOneByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]:Checkpoint TC4[2]:  Checkpoint 11 Verify the layout changed to 3x1L-1x1R and series loaded","TC1106_Checkpoint11");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC4[1]: Checkpoint 12 Verify the layout changed to 3x3 and series loaded","TC1106_Checkpoint12");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint 13-24", "Verify the layout changed based on the selected option and the series loaded in each view port according the series order for data");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint 13 Verify the layout changed to 1x1 and series loaded for reloaded study Mr LSP","TC1106_ Checkpoint13");

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint 14 Verify the layout changed to 2x1 and series loaded for reloaded study Mr LSP","TC1106_ Checkpoint14");

		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 15 Verify the layout changed to 1x2 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint15");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 16 Verify the layout changed to 2x2 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint16");
		
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 17 Verify the layout changed to 3x2 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint17");

		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 18 Verify the layout changed to 2x3 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint18");

		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 19 Verify the layout changed to 1x1L-3x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint19");

		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 20 Verify the layout changed to 1x1L-2x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint20");

		layout.selectLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 21 Verify the layout changed to 1x1T-1x2B and series loaded for reloaded study Mr LSP","TC1106_Checkpoint21");

		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 22 Verify the layout changed to 2x1L-1x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint22");

		layout.selectLayout(layout.threeByOneLAndOneByOneRLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 23 Verify the layout changed to 3x1L-1x1R and series loaded for reloaded study Mr LSP","TC1106_Checkpoint23");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint 24 Verify the layout changed to 3x3 and series loaded for reloaded study Mr LSP","TC1106_Checkpoint24");

	}
	
	@Test(groups ={"firefox","Chrome","Edge","IE11","US586"})
	public void test27_US586_TC1889_verifyAboutLinkFromViewer() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify licensing Information using About link from Viewer page");
	
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(ah4PatientName, 1);	
		
		Header header = new Header(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Scan drop down menu is present on top left on page");
		viewerpage.assertTrue(header.userInfo.isDisplayed(), "Verifying that Scan drop down menu is present on top left of Patient List Page", "Scan drop down menu is present on left corner of page");
		header.viewAboutPage();
		header.switchToNewWindow(2);
		header.maximizeWindow();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify About page opens in a new tab");
		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL), "Verifying the About Page is open", "User is on About page "+ loginPage.getCurrentPageURL());
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Hammer JS licensing Information is present on About page");
		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.HAMMERJS), "Verifying Hammer JS licensing information is present", "The Hammer JS licensing information is present on page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify ng-bootstrap licensing Information is present on About page");
		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.BOOTSTARP), "Verifying ng-bootstrap licensing information is present", "The ng-bootstrap licensing information is present on page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Newtonsoft.Json licensing Information is present on About page");
		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.NEWTONSOFT), "Verifying Newtonsoft.Json licensing information is present", "The Newtonsoft.Json licensing information is present on page");
				
	}
			
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US548"})
	public void test28_US548_TC1623_verifyDoubleClickOneUpForViewboxWithDicomOrPDFOrJPEG() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Double click One up for a Viewbox with PDF and JPEG image");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(johnDoePatientName);

		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(4);
		
		//variable to store default layout for 
		int defaultCount = viewerpage.getNumberOfCanvasForLayout();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify default layout for "+ah4PatientName+" Patient");
		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
		
		//Double click on Viewbox1 containing DICOM image
		viewerpage.doubleClickOnViewbox(1);
		int doubleUpCount = viewerpage.getNumberOfCanvasForLayout();
		
		//Verify layout after One-up is 1X1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify layout changes to 1X1 after Double click One-Up on DIOCM image");
		viewerpage.assertEquals(doubleUpCount,1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );
		
		//Double click on Viewbox1 for default layout
		viewerpage.doubleClickOnViewbox(1);
		
		defaultCount = viewerpage.getNumberOfCanvasForLayout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify layout changes to default on double click on viewbox with 1X1 layout");
		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
		
		//Double click on Viewbox3 containing Non-DICOM image
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(3));
		doubleUpCount = viewerpage.getNumberOfCanvasForLayout();
		
		//Verify layout after One-up is 1X1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify layout changes to 1X1 after Double click One-Up on Non-DIOCM image");
		viewerpage.assertEquals(doubleUpCount,1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );
		
		//Double click on Viewbox1 for default layout
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(3));

		defaultCount = viewerpage.getNumberOfCanvasForLayout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify layout changes to default on double click on viewbox with 1X1 layout");
		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );

		//Double click on Viewbox4 containing Non-DICOM image
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(4));
		doubleUpCount = viewerpage.getNumberOfCanvasForLayout();
		
		//Verify layout after One-up is 1X1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify layout changes to 1X1 after Double click One-Up on PDF");
		viewerpage.assertEquals(doubleUpCount,1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );
	
		//Double click on Viewbox1 for default layout
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(4));
		defaultCount = viewerpage.getNumberOfCanvasForLayout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify layout changes to default on double click on viewbox with 1X1 layout");
		viewerpage.assertEquals(defaultCount,4, "Verify default layout on Viewer", "The Default layout for AH4 patient is 2x2" );
	
		
		
		
	}
		
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE375" })
	public void test29_DE375_TC1535_verifyStudyDetailFromWIAGate() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Study detail from WIA gate services");

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(boneAgePatient,"","","");

		String studyDesciption = DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY,"STUDY01", filePath2);	

		//Verify Study description on Study page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Study desciption on Study list page for "+boneAgePatient+" Patient");
		patientPage.assertEquals(studyDesciption,patientPage.getStudyDescription(0),"Verify Study description on Study list page","Result description is : "+studyDesciption);
		
		
		//Hover mouse on EnvoyAI Icon and verify algorithm detail
		patientPage.mouseHover(patientPage.eurekaIcon);
		String str = patientPage.getEurekaAITooltip();
				
		//Verify result with finding is present on hovering on Envoy AI 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Result status on tooltip of EnvoyAI icon");
		patientPage.assertTrue(str.contains(PatientPageConstants.EUREKA_RESULT_STATUS_DONE), "Verify Result Status is present on tooltip", "Result Status: Run with findings is present in tooltip");
		
		//Verify algorithm detail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Algorithm detail is present on tooltip of EnvoyAI Icon");
		patientPage.assertTrue(str.contains(ViewerPageConstants.BONEAGE_MACHINE_NAME), "Verify algorithm detail on tooltip of EnvoyAI Icon", "Algorithm Boneage is present in tooltip");
		patientPage.assertTrue(str.contains(ViewerPageConstants.BONEAGE_MACHINE_NAME1), "Verify algorithm detail on tooltip of EnvoyAI Icon", "Algorithm Boneage is present in tooltip");
		
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(4);
		
		//Verify Default layout for Bone-Age Data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify Default layout for Boneage Data is 1X2");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying default layout for BoneAge","TC1535_CheckPoint1");
		
		//Verify Atlas appear on Result set by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Atlas appear on Result set by default");
		viewerpage.assertTrue(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath2).equalsIgnoreCase(viewerpage.getSeriesDescriptionOverlayText(1)),"Verifying Result description on text overlay", "Verified Result description on textoverlay");
	
		//Verify Text overlay for Patient Date on Viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay on orginal Patient Data for default annotation");
		verifyDefaultOverlayDataForBoneage(filePath2, 4);
	}
	
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE758" })
	public void test30_DE758_TC2901_verifyStudyDetailFromWIAGate() throws  InterruptedException, AWTException 
	{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("LayoutChange not working if empty viewboxes are there in viewer- With GSPS present data");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(GSPS_PatientName, 1);		

		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		
		//Verifying that the Default layout is displayed which is 1*2 for GSPS_Multiseries_Data
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying default layout for GSPS_Multiseries_Data","TC37_CheckPoint1");

		//Verifying the change in Layout to 1*1
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 1*1 for GSPS_Multiseries_Data","TC37_CheckPoint2");

		//Verifying the change in Layout to 2*2
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 2*2 for GSPS_Multiseries_Data","TC37_CheckPoint3");
		
		//Verifying the change in Layout to 3*3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 3*3 for GSPS_Multiseries_Data","TC37_CheckPoint4");
		
		//Selecting any series in an empty viewbox through content selector
		contentSelector.selectSeriesFromSeriesTab(4,firstSeriesDescriptionGSPS_Multiseries);	
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionGSPS_Multiseries), "Verifying that series is selected from content selector on an empty viewbox.", "Verified that series is selected from content selector on an empty viewbox.");

		//Verifying that series selected on an empty viewbox is visible
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change for GSPS_Multiseries_Data after selecting series in an empty viewbox","TC37_CheckPoint5");

	}
	
//	covered in first
	//@Test(groups = { "firefox", "Chrome", "IE11", "Edge","DE758" })
	public void test31_DE758_TC2902_verifyStudyDetailFromWIAGate() throws InterruptedException, AWTException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("LayoutChange not working if empty viewboxes are there in viewer- Without GSPS data");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9Patient, 1);		

		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		
		//Verifying that the Default layout is displayed which is 1*2 for GSPS_Multiseries_Data
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying default layout for AH.4_Data","TC38_CheckPoint1");

		//Verifying the change in Layout to 1*1
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 1*1 for AH.4_Data","TC38_CheckPoint2");

		//Verifying the change in Layout to 2*2
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 2*2 for AH.4_Data","TC38_CheckPoint3");
		
		//Verifying the change in Layout to 3*3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change into 3*3 for AH.4_Data","TC38_CheckPoint4");
		
		//Selecting any series in an empty viewbox through content selector
		contentSelector.selectSeriesFromSeriesTab(8,firstSeriesDescriptionAH4);	
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescriptionAH4), "Verifying that series is selected from content selector on an empty viewbox.", "Verified that series is selected from content selector on an empty viewbox.");

		////Verifying that series selected on an empty viewbox is visible
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying the layout change for AH.4_Data after selecting series in an empty viewbox","TC38_CheckPoint5");

	}
	
	@Test(groups ={"firefox","Chrome","DE1284","positive"})
	public void test32_DE1284_TC5264_verifyConsoleErrorOnViewerLoad() throws InterruptedException, IOException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Console Errors on viewer load");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(ah4PatientName, 1);	
		layout=new ViewerLayout(driver);
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[1/2]", "Verified that no console error seen while loading viewer page in 2*2 layout");
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[2/2]", "Verified that no console error seen while loading viewer page in 3*3 layout");
		
	}

	
}
