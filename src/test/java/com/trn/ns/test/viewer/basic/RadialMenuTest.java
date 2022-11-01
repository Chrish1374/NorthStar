package com.trn.ns.test.viewer.basic;
import static com.trn.ns.test.configs.Configurations.PASS;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;

import org.openqa.selenium.TimeoutException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class RadialMenuTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel viewBoxPanel;

	// Get Patient Name
	String filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	// Get Patient Name
	String filePath2=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String PatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewerOrientation orin;
	private String loadedTheme;
	private PagesTheme pageTheme;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() 
	{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups = { "Chrome", "IE11", "Edge","US242" })
	public void test01_US242_TC580_verifyOneInstanceOfRadialMenuAppearOnViewbox() throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify a Single Instance of Radial Menu appear on ViewBox in right Click");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, 1);

		//open a Radial Menu for Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Instance of Radial Menu appear on ViewBox");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify Instance of Radial Menu appear on ViewBox");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify a Single Instance of Radial Menu appear on Viewer","TC580_Verfiy_Single_Radial_Instance_CheckPoint3");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test02_US242_TC583_verifyChangesOfRadialMenuCommandRetainOnChangingLayout() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Radial Command operation changes are retained on Viewbox on changing layout");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);
		layout = new ViewerLayout(driver);

		//get Zoom level for View Box 1 before Zoom
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = viewBoxPanel.getZoomValue(1);
		// get Zoom level for Canvas 1 before Zoom
		int intialZoomLevel2 = viewBoxPanel.getZoomValue(2);
		//open a Radial Menu for Viewbox1 and perform Zoom operation
		viewerPage.selectZoomFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, -50,-50);
		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerPage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ viewBoxPanel.getZoomValue(1));
		int finalZoomLevel = viewBoxPanel.getZoomValue(1);
		// Verify Image Zoom Out on ViewBox 2 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]","Verify Zoom Level after Mouse Up Increase in View Box 2.");
		viewerPage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel2,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel2 + " to "+ viewBoxPanel.getZoomValue(1));
		// changing layout to 3X2
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]","Verify Zoom Level on Changing Layout remain same.");
		viewerPage.assertEquals(finalZoomLevel,viewBoxPanel.getZoomValue(1),"Check Zoom Level of ViewBox1 after Changing Layout","The Zoom level after Changing Layout remain same");
	}

	@SuppressWarnings("static-access")
	@Test(groups = {"Chrome", "IE11", "Edge","US242"})
	public void test03_US242_TC581_verifyRadialMenuOptionOnCornerOfQuadrant() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Radial Menu does not appear compeletly on Corner of Quadrant");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, 1);

		//open a Radial Menu for Viewbox1
		viewerPage.mouseHoverWithContextClick(viewerPage.CLICKABILITY, viewerPage.getViewbox(1), -410, -210);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Instance of Radial Menu does not appear fully on Corner of ViewBox 1");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify Instance of Radial Menu does not appear fully on Corner of ViewBox1","TC581_Verfiy__Radial_Instance_Corner_CheckPoint1");
		//open a Radial Menu for Viewbox2 on top right corner of ViewBox
		viewerPage.mouseHoverWithContextClick(viewerPage.CLICKABILITY, viewerPage.getViewbox(2), -410, -210);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify Instance of Radial Menu does not appear fully on Corner of ViewBox 2");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify Instance of Radial Menu does not appear fully on Corner of ViewBox2","TC581_Verfiy__Radial_Instance_Corner_CheckPoint2");	
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test05_US242_TC623_verifyRadialManuOptions() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all the Icon are present on Radial Menu inner arch");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		//open a Radial Menu for Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		//Verify all 5 Ruler ,CINE ,WL,Zoom and scroll Icon appear on Radial Menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify Ruler Icon appear on Inner Arc of Radial Menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.distanceIcon),"Verify Ruler Icon appear on Inner Arc of Radial Menu", "The Ruler Icon is present on Inner Arc of Radial Menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify Cine Icon appear on Inner Arc of Radial Menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.cinePlayIcon),"Verify Cine Icon appear on Inner Arc of Radial Menu", "The Cine Icon is present on Inner Arc of Radial Menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify WL Icon appear on Inner Arc of Radial Menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.windowLevelingIcon),"Verify WL Icon appear on Inner Arc of Radial Menu", "The WL Icon is present on Inner Arc of Radial Menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify Zoom Icon appear on Inner Arc of Radial Menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.zoomIcon),"Verify Zoom Icon appear on Inner Arc of Radial Menu", "The Zoom Icon is present on Inner Arc of Radial Menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify Scroll Icon appear on Inner Arc of Radial Menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.scrollIcon),"Verify Scroll Icon appear on Inner Arc of Radial Menu", "The Scroll Icon is present on Inner Arc of Radial Menu");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test04_US242_TC584_verifyChangesOfRadialMenuCommandRetainOnNavigatingBack() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify changes done by Radial Menu is not retained on navigating back to Study Page");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
		//open a Radial Menu for Viewbox1 and Perform Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.waitForSilicesToChange(1);
		int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Cine has Started and is Working Fine");
		viewerPage.assertNotEquals(currentImage, imageAfterCine, "verifying the cine has started", "Cine is started and working fine");
		//navigate back to Study page.
		viewerPage.browserBackWebPage();
		helper.loadViewerDirectly(PatientName2, 1);
		int postImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Changes done by Cine is not retained on navigating back to Study Page");
		viewerPage.assertEquals(currentImage, postImage, "Verfiy Changes done by Cine Operation is not retained", "The Changes done by Cine operation is not retained");		
	}

	@SuppressWarnings("static-access")
	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test07_US242_TC649_verifyCinePlayButtonChangesInPauseOnPlayMode() throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Cine Play Button Changes to Pause in Play Mode");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		//open a Radial Menu for Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Cine Play Button appear on Radial Menu Arch");
		viewerPage.compareElementImage(protocolName,viewerPage.cinePlayIcon,"Verify Cine Play Button appear on Radial Menu Arch","TC649_Verfiy_Cine_Play_Button_CheckPoint1");
		//open a Radial Menu for Viewbox1 and Perform Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		//open a Radial Menu for Viewbox1 and Verify Pause Button on radial Menu Arch
		viewerPage.mouseHoverWithContextClick(viewerPage.CLICKABILITY, viewerPage.getViewbox(1), -300, -150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify Cine Pause Button appear on Radial Menu Arch");
		viewerPage.compareElementImage(protocolName,viewerPage.cinePlayIcon,"Verify Cine Pause Button appear on Radial Menu Arch","TC649_Verfiy_Cine_Play_Button_CheckPoint2");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test08_US242_TC650_verifyCinePlayStoppedOnClickingPauseButton() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Cine Play is stopped on Clicking Pause Button");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		//open a Radial Menu for Viewbox1 and Perform Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), newImagePath+"/goldImages/radialCinePlayImage.png");
		//open a Radial Menu for Viewbox1 and Close Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), newImagePath+"/actualImages/radialCinePlayImage.png");
		int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Cine Play is stopped on Viewbox");
		viewerPage.assertNotEquals(currentImage, imageAfterCine, "Verifying the Cine play is stopped", "Cine is stopped and working fine");
		String expectedImagePath = newImagePath+"/goldImages/radialCinePlayImage.png";
		String actualImagePath = newImagePath+"/actualImages/radialCinePlayImage.png";
		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, actualImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify post Cine operation images on Viewbox are different");
		viewerPage.assertFalse(cpStatus, "The Pre and Post Cine Images are different","");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying Cine operation from Radial Menu", "Successfully verified checkpoint with image comparison.<br>Image name is radialCinePlayImage.png");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test09_US242_TC651_verifyImageRotationPersistOnCine() throws TimeoutException, InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Image Rotation Persist on Cine");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);
		orin = new ViewerOrientation(driver);

		//Verify flip for A orientation
		orin.rotateSeries(orin.getTopOrientationMarker(1), orin.topCounterClockwiseRotationMarker(1));
		//open a Radial Menu for Viewbox1 and Perform Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), newImagePath+"/goldImages/radialCinePlayImage.png");
		//open a Radial Menu for Viewbox1 and Close Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), newImagePath+"/actualImages/radialCinePlayImage.png");
		int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Cine Play is stopped on Viewbox");
		viewerPage.assertNotEquals(currentImage, imageAfterCine, "Verifying the Cine play is stopped", "Cine is stopped and working fine");
		String expectedImagePath = newImagePath+"/goldImages/radialCinePlayImage.png";
		String actualImagePath = newImagePath+"/actualImages/radialCinePlayImage.png";
		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, actualImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify post Cine operation images on Viewbox are different");
		viewerPage.assertFalse(cpStatus, "The Pre and Post Cine Images are different","");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying Cine operation from Radial Menu", "Successfully verified checkpoint with image comparison.<br>Image name is radialCinePlayImage.png");	
	}

	@SuppressWarnings("static-access")
	@Test(groups = {  "Chrome", "IE11", "Edge" ,"US242"})
	public void test12_US242_TC664_verifyRadialMenuOptionShouldNotBeUpsideDownOtherThanEdgeOfViewbox() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Radial Menu option do not appear upside down on ViewBox apart from edges");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, 1);

		//open a Radial Menu at the Center of Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify Instance of Radial Menu do not appear upside down on center of ViewBox");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify Instance of Radial Menu do not appear upside down on Center of ViewBox","TC664_Verfiy__Radial_Instance_Corner_CheckPoint1");
		//open a Radial Menu for Viewbox1 to right Corner of View box1
		viewerPage.mouseHoverWithContextClick(viewerPage.CLICKABILITY, viewerPage.getViewbox(1), -400, -200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]", "Verify Instance of Radial Menu appear upside down on right Side of ViewBox1");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify Instance of Radial Menu appear upside down on Right Side of ViewBox1","TC664_Verfiy__Radial_Instance_Corner_CheckPoint2");
		//open a Radial Menu for Viewbox2 on the Left corner of ViewBox 2
		viewerPage.mouseHoverWithContextClick(viewerPage.CLICKABILITY, viewerPage.getViewbox(2), 430, -230);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify Instance of Radial Menu appear upside down on left Side of ViewBox 2");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify Instance of Radial Menu appear upside down on Right Side of ViewBox2","TC664_Verfiy__Radial_Instance_Corner_CheckPoint3");	
		//open a Radial Menu for Viewbox1 at 100,100 Pixel away form Center of ViewBox1
		viewerPage.mouseHoverWithContextClick(viewerPage.CLICKABILITY, viewerPage.getViewbox(1), 100, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify Instance of Radial Menu do not appear upside down on ViewBox 1");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify Instance of Radial Menu do not appear upside down on ViewBox","TC664_Verfiy_Radial_Instance_Corner_CheckPoint4");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test13_US242_TC665_verifyOnlyInnerArcOfRadialToolbarIsDisplayedOnRightClick() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify there should be only inner arc of radial toolbar displayed on right click on viewbox");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		//open a Radial Menu for Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Instance of Radial Menu appear on ViewBox");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
	}

	@Test(groups = { "IE11", "Chrome",  "Edge","US242" }, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls","sheetName=test01_VerifyPan" })
	public void test14_US242_TC666_verifyRadialMenuOptionForDifferentModality(String PatientName, String Modality, String Rows, String Columns)
			throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify radial toolbar should get displayed on right click for different modalities");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify study is loaded in viewport.");
		viewerPage.assertTrue(viewerPage.getPatientIDOverlay(1).isDisplayed(),"Verifying study is loaded in viewport","Study is loaded in viewport");
		//open a Radial Menu for Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify Instance of Radial Menu appear on ViewBox");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
	}

	@Test(groups ={"IE11","Chrome","DE265"})
	public void test17_DE265_TC1239_verifyRadialMenuAppearAfterLinearMeasurement() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Radial Menu appear after linear Measurement on ViewBox");
		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);		
		SimpleLine line = new SimpleLine(driver);		
		// Select Distance Measurement Icon form Radial Menu
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		// Drawing a horizontal line
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Drawing the measurement on coordinates (0,0,100,0)" );
		line.drawLine(1,50,10,100, 20);
		line.assertEquals(line.getLines(1).size(), 1, "Verify the measurement drawn @ coordinates (0,0,100,0)", "TC_1239_CheckPoint1");
		//open a Radial Menu for Viewbox1
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]", "Verify Instance of Radial Menu appear on ViewBox 1");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
		line.closingConflictMsg();
		//open a Radial Menu for Viewbox2
		viewerPage.openQuickToolbar(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]", "Verify Instance of Radial Menu appear on ViewBox 1");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox2", "The Radial menu is visble on ViewBox2");

		//Select Scroll from Radial Menu
		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));

		int currentImage = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.dragAndReleaseOnViewer(20,10, 0, 100);
		int imageAfterCine = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.waitForTimePeriod(3000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]", "Verify on de-selecting measurement icon defualt scroll operation is performed on dragging mouse");
		viewerPage.assertNotEquals(currentImage, imageAfterCine, "Verifying the Scroll is working Fine", "The Image on ViewBox 1 changes on scrolling");
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(2));

		// Drawing a horizontal line on ViewBox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Drawing the measurement on coordinates (0,0,100,0)" );
		line.drawLine(2,10,10, 100, 0);
		line.assertEquals(line.getLines(1).size(), 0,"Verify the measurement drawn @ coordinates (0,0,100,0)", "TC_1239_CheckPoint5");		
		line.assertEquals(line.getLines(2).size(), 1,"Verify the measurement drawn @ coordinates (0,0,100,0)", "TC_1239_CheckPoint6");
		viewerPage.waitForTimePeriod(1000);
		line.closingConflictMsg();
		//open a Radial Menu for Viewbox3
		viewerPage.openQuickToolbar(viewerPage.getViewPort(3));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]", "Verify Instance of Radial Menu appear on ViewBox 3");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Menu is visible on ViewBox3", "The Radial menu is visble on ViewBox3");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","US242" })
	public void test10_US242_TC653_verifyParallelCinePlayOnViewBoxes() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to play the cine parallel in other viewboxes");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		//open a Radial Menu for Viewbox1 and Perform Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		int currentImageViewBox1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.waitForSilicesToChange(1);
		//open a Radial Menu for Viewbox2 and Perform Cine operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(2));
		int currentImageViewBox2 = viewerPage.getCurrentScrollPositionOfViewbox(2);
		viewerPage.waitForSilicesToChange(2);
		//open a Radial Menu for Viewbox1 and Close Cine Operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		int imageAfterCineViewBox1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify post Cine operation images on Viewbox are different on ViewBox 1");
		viewerPage.assertNotEquals(currentImageViewBox1, imageAfterCineViewBox1, "Verifying the Cine play is stopped", "Cine is stopped and working fine on ViewBox 1");
		//open a Radial Menu for Viewbox1 and Close Cine Operation
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(2));
		int imageAfterCineViewBox2 = viewerPage.getCurrentScrollPositionOfViewbox(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify post Cine operation images on Viewbox are different on ViewBox 2");
		viewerPage.assertEquals(currentImageViewBox2, imageAfterCineViewBox2, "Verifying the Cine play is stopped", "Cine is stopped and working fine on ViewBox 2");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","DE429" })
	public void test20_DE429_TC1725_verifyPersistenaceOfRadialMenuSelectionOnLayoutChange() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Radial menu selection persist on layout change");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);
		layout = new ViewerLayout(driver);

		// Variable to store Zoom level for Canvas 0 before Zoom
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = viewBoxPanel.getZoomValue(1);

		// Select Zoom from Context Menu
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify Zoom icon is selected.");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, -100,-100);

		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerPage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ viewBoxPanel.getZoomValue(1));

		//Change layout to 1X2
		layout.selectLayout(layout.oneByTwoLayoutIcon);

		//Verify Zoom command is enable on layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify Zoom icon is selected after layout change.");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected after layout change", "Zoom icon is selected");

		//Select Window level command from Context Menu
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));

		//Change layout and verify Zoom is selected post layout change
		layout.selectLayout(layout.twoByThreeLayoutIcon);

		//Verify Zoom command is enable on layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify WWL icon is selected after layout change.");
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL),"Verifying WWL icon is selected after layout change", "WWL icon is selected");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","DE429" })
	public void test21_DE429_TC1726_verifyPersistenaceOfRadialMenuSelectionOnDoubleClickOneUp() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Radial menu selection persist on double click One-Up");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);

		// Variable to store Zoom level for Canvas 0 before Zoom
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = viewBoxPanel.getZoomValue(1);

		// Select Zoom from Context Menu
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify Zoom icon is selected.");

		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, -100,-100);

		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerPage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ viewBoxPanel.getZoomValue(1));

		//Double Click on Viewbox1 to perform One-Up
		viewerPage.doubleClickOnViewbox(1);

		//Verify Zoom command is enable on One-Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify Zoom icon is selected after layout change.");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected after layout change", "Zoom icon is selected");

		//Select Window level command from Context Menu
		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(1));

		//Change layout and verify Zoom is selected post layout change
		viewerPage.doubleClickOnViewbox(1);

		//Verify Zoom command is enable on layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify Scroll icon is selected after layout change.");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.SCROLL),"Verifying Scroll icon is selected after layout change", "Scroll icon is selected");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","DE438" })
	public void test22_DE438_TC1760_TC1761_verifyContextMenuAtExtremeEdgeOfViewbox() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the context menu should always open near the ALL button of radial menu.</br>Verify that the context menu should not disapper at extrem edges");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);


		//		//Open Context menu at edge of Viewbox1

		viewerPage.performMouseRightClick(viewerPage.getViewPort(1), -300, -200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify that the context menu should always open near the ALL button of radial menu");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify that the context menu should always open near the ALL button of radial menu","TC1760_CheckPoint1");

		//Open Context Menu at edge of Viewbox2
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2), 430, -230);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify that the context menu should not disapper at extrem edges");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify that the context menu should not disapper at extrem edges","TC1760_CheckPoint2");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge" ,"DE518"})
	public void test23_DE518_TC2068_verifyImageShouldNotPanAfterPerformingRightClick() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that image should not get PAN when user open radial menu by performing right click");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);


		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verified that image should not get PAN when user open radial menu by performing right click");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verify that image should not get PAN when user open radial menu by performing right click","TC2068_CheckPoint1");
	}

	@Test(groups = {  "Chrome", "IE11", "Edge","DE480" })
	public void test24_DE480_TC1895_verifyToolNotAvailableAndUIChange() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Tool not available and UI change");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName2, 1);


		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.mouseHover(viewerPage.scrollIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verified the icon of the 'Scroll' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the tool 'Scroll' changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint1");

		viewerPage.mouseHover(viewerPage.panIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verified the icon of the 'Pan' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the 'Pan' tool changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint2");

		viewerPage.mouseHover(viewerPage.windowLevelingIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verified the icon of the 'WindowLeveling' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the 'WindowLeveling' tool changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint3");

		viewerPage.mouseHover(viewerPage.zoomIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verified the icon of the 'Zoom' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the 'Zoom' tool changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint4");
		PointAnnotation point = new PointAnnotation(driver);

		viewerPage.mouseHover(point.pointIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verified the icon of the 'Point Annotation' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the 'Point Annotation' tool changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint5");

		MeasurementWithUnit line = new MeasurementWithUnit(driver);
		line.selectDistanceFromQuickToolbar(1);

		line.openQuickToolbar(1);
		viewerPage.mouseHover(viewerPage.distanceIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verified the icon of the 'Distance' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the 'Distance' tool changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint6");

		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		line.openQuickToolbar(1);
		viewerPage.mouseHover(viewerPage.cinePlayIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verified the icon of the 'Cine Play' tool changes to white, indicating that it is being hovered over.");
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint1 Verify the icon of the 'Cine Play' tool changes to white, indicating that it is being hovered over.","TC1895_" + "_" + "Checkpoint7");
	}

	//US2523: Show only the applicable icons for viewbox in quick toolbox which has a NON-DICOM series loaded


	@Test(groups = { "Chrome","IE11","Edge","US2523","F1090","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test25_US2523_TC10398_TC10399_TC10400_verifyToolNotAvailableAndUIChange(String theme) throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the updated quick toolbox is displayed for Non-Dicom images in Eureka theme. <br>"+
				"Verify that the updated quick toolbox is displayed for Non-Dicom images in Dark theme. <br>"+
				"Verify that when user switches from Non Dicom to Dicom viewbox, the quick toolbox gets updated to all tools options.");
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			Header header = new Header(driver);
			header.logout();

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(anonymous_Patient,1,2);

		ViewerToolbar tool=new ViewerToolbar(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify measurement icons not display for Non DICOM in Quick and Viewer toolbar.");
		tool.assertTrue(tool.verifyIconsPresenceForNonDicomInQuickToolbar(1), "Checkpoint[1/14]", "Verify that measurement icon not visible on Quick toolbar for non Dicom.");
		tool.assertTrue(tool.verifyIconsDisableForNonDicomInViewerToolbar(1), "Checkpoint[2/14]", "Verify that measurement icon not visible on Viewer toolbar for non Dicom");

		PagesTheme pageTheme=new PagesTheme(driver);
		for(int i=0;i<tool.allToolIcons.size();i++)
		{
			tool.assertEquals(tool.getCssValue(tool.allToolIcons.get(i), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[3."+(i+1)+"/14]", "Verified that measurement icon is disable on viewer toolbar");
			tool.assertTrue(pageTheme.verifyThemeForIcon(tool.allToolIcons.get(i),loadedTheme),"Checkpoint[4."+(i+1)+"/14]","Verified theme for disable measurement icon.");
		}
		//For WW icon
		tool.assertEquals(tool.getCssValue(tool.allViewerIcons.get(2), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[5/14]", "Verified that WW icon is disable on viewer toolbar.");
		tool.assertTrue(pageTheme.verifyThemeForIcon(tool.allToolIcons.get(2),loadedTheme),"Checkpoint[6/14]","Verified theme for disable WW icon.");

		tool.openQuickToolbar(1);
		for(int i=1;i<viewerPage.allViewerIcons.size();i++)
			tool.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allViewerRectIcons.get(i),loadedTheme),"Checkpoint[7."+(i+1)+"/14]","Verified that button is not selected.");


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify measurement icons display for DICOM in Quick and Viewer toolbar.");
		//on dicom viewbox
		tool.assertFalse(tool.verifyIconsPresenceForNonDicomInQuickToolbar(2), "Checkpoint[8/14]", "Verify that measurement icon visible on Quick toolbar for Dicom.");
		tool.assertFalse(tool.verifyIconsDisableForNonDicomInViewerToolbar(2), "Checkpoint[9/14]", "Verify that measurement icon visible on Quick toolbar for Dicom.");

		for(int i=0;i<tool.allToolIcons.size();i++)
		{
			tool.assertNotEquals(tool.getCssValue(tool.allToolIcons.get(i), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[10."+(i+1)+"/14]", "Verified that measurement icon is enable on viewer toolbar");
			tool.assertTrue(pageTheme.verifyThemeForIcon(tool.allToolIcons.get(i),loadedTheme),"Checkpoint[11."+(i+1)+"/14]","Verified theme for enable measurement icon on viewer toolbar.");
		}
		//For WW icon
		tool.assertTrue(pageTheme.verifyThemeForIcon(tool.allToolIcons.get(2),loadedTheme),"Checkpoint[12/14]","Verified theme for enable WW icon.");

		tool.openQuickToolbar(1);
		for(int i=1;i<viewerPage.allViewerIcons.size();i++)
			tool.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allViewerRectIcons.get(i),loadedTheme),"Checkpoint[13."+(i+1)+"/14]","Verified theme for enable viewer icon.");

		for(int i=1;i<viewerPage.allAnnotationRectIcons.size();i++)
			tool.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allAnnotationRectIcons.get(i),loadedTheme),"Checkpoint[14."+(i+1)+"/14]","Verified theme for enable measurement icon.");
	}

	@Test(groups ={"IE11","Chrome","Edge","DE2729","Positive"})
	public void test26_DE2729_TC10621_verifyRadialMenuAppearAfterBatchConflictNotificationClose() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Radial menu/Quick toolbox is displayed in conflicting results viewboxes as well when user closes the batch conflict notification");
		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(anonymous_Patient, 1);		

		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		layout.click(layout.getViewboxNotification(3));

		layout.waitForRespectedViewboxToLoad(3);
		viewerPage.openQuickToolbar(viewerPage.getViewPort(3));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Instance of Radial Menu appear on ViewBox 3");
		viewerPage.assertTrue(viewerPage.verifyIconsPresenceForNonDicomInQuickToolbar(3), "Validate Menu is visible on ViewBox3", "The Radial menu is visble on ViewBox3");

		viewerPage.openQuickToolbar(viewerPage.getViewPort(4));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify Instance of Radial Menu appear on ViewBox 4");
		viewerPage.assertTrue(viewerPage.verifyIconsPresenceForNonDicomInQuickToolbar(4), "Validate Menu is visible on ViewBox4", "The Radial menu is visble on ViewBox4");

	}

	@Test(groups={"Chrome","Edge","US2482","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test27_F1090_US2482_TC10127_TC10129_TC10130_TC10131_verifyUpdatedScrollAndPanIcon(String theme) throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the design and look and feel of the Zoom tool under the view box tool panel.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			Header header = new Header(driver);
			header.logout();

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);
			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver); 
		viewerPage=helper.loadViewerDirectly(aidocPatientName,3);

		pageTheme=new PagesTheme(driver);


		ViewerToolbar viewerTool=new ViewerToolbar(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify measurement icons not display for Non DICOM in Quick and Viewer toolbar.");

		viewerTool.assertTrue(viewerTool.isElementPresent(viewerTool.panIcon),"Checkpoint[1/16]", "Verifying pan icon is displayed in viewer toolbar");

		viewerTool.assertTrue(viewerTool.verifyViewerToolIconVisibility(viewerTool.panIcon,loadedTheme),"Checkpoint[2/16]","verifying the pan icon is visible");

		viewerTool.assertTrue(viewerTool.isElementPresent(viewerTool.scrollIcon),"Checkpoint[3/16]", "Verifying pan icon is displayed in viewer toolbar");
		viewerTool.assertTrue(viewerTool.verifyViewerToolIsSelected(viewerTool.scrollIcon,loadedTheme),"Checkpoint[4/16]","verifying that scroll icon is selected and visible");

		if(theme.equalsIgnoreCase(loadedTheme))
			viewerPage.compareElementImage(protocolName, viewerTool.panIcon, "Checkpoint [5/15]: Verify the preset menu on child window", "test27_01_"+loadedTheme+"");

		else
			viewerPage.compareElementImage(protocolName, viewerTool.panIcon, "Checkpoint [6/16]: Verify the preset menu on child window", "test27_01_"+loadedTheme+"");


		viewerPage.openQuickToolbar(1);
		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.panIcon, NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[13/16]", "Verified that pan icon is disable on radil menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.panIcon),"Checkpoint[7/16]", "Verifying pan icon is displayed in radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.scrollIcon),"Checkpoint[8/16]", "Verifying scroll icon is displayed in radial menu");
		viewerPage.assertEquals(viewerPage.getText(viewerPage.getTooltipWebElement(viewerPage.panIcon)), ViewerPageConstants.PAN, "Checkpoint[9/16]", "Verifying tool tip");

		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.panIcon), loadedTheme),"Checkpoint[10/16]","Verifying the tooltip is adhering the theme");

		viewerPage.assertEquals(viewerPage.getText(viewerPage.getTooltipWebElement(viewerPage.scrollIcon)), ViewerPageConstants.SCROLL, "Checkpoint[11/16]", "Verifying tool tip");
		viewerPage.assertTrue(pageTheme.verifyThemeOnTooltip(viewerPage.getTooltipWebElement(viewerPage.scrollIcon), loadedTheme),"Checkpoint[12/16]","Verifying the tooltip is adhering the theme");


		viewerPage.assertFalse(pageTheme.verifyButtonIsNotFilled(viewerPage.panIcon,loadedTheme),"Checkpoint[13/16]","Verified theme for disable pan icon.");
		viewerPage.assertTrue(pageTheme.verifyButtonIsNotFilled(viewerPage.allViewerRectIcons.get(1),loadedTheme),"Checkpoint[14/15]","Verified theme for enable scroll icon.");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerTool.assertTrue(viewerTool.verifyViewerToolIsSelected(viewerTool.panIcon,loadedTheme),"Checkpoint[15/16]","verifying that Pan icon is selected and visible");

		viewerPage.waitForAllChangesToLoad();

		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer,"Checkpoint[16/16] 'Verifying pan is happening'","TC27_02"+loadedTheme+"");

	}

}





