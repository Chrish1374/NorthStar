package com.trn.ns.test.viewer.overlays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import static com.trn.ns.test.configs.Configurations.PASS;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

//Safety.NS_F45_LoadStudyIntoViewer-CF0304ARevD - revision-0


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class OrientationTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;	
	private ViewerPage viewerPage;
	private ViewerOrientation orientation;
	private ViewerLayout layout;
	private ViewerTextOverlays textOverlay;

	// Get Patient Name
	String filePath=Configurations.TEST_PROPERTIES.get("NorthStar^MR^MultiPhase^20x6_filepath");
	String patientName_MR_MultiPhase = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	private HelperClass helper;


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		// Load and initialize the Patient List.
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" })
	public void test01_DE98_TC711_verifyOrientationPositonRetainedOnLayoutChange() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that positions are retained on layout change");
		//Get Patient Name
		String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
		String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9Patient+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerPage =helper.loadViewerDirectly(liver9Patient, 1);
		orientation=new ViewerOrientation(driver);

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(1), newImagePath+"/actualImages/beforeFlipImage.png");

		//Rotate the image present in first viewbox
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));

		viewerPage.takeElementScreenShot(viewerPage.getViewbox(1), newImagePath+"/actualImages/afterFlipImage.png");

		String beforeFlipImage = newImagePath+"/actualImages/beforeFlipImage.png";
		String afterFlipImage = newImagePath+"/actualImages/afterFlipImage.png";
		String diffImagePath = newImagePath+"/actualImages/FlipImage.png";

		boolean cpStatus =  viewerPage.compareimages(beforeFlipImage, afterFlipImage, diffImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying image should be rotated" );
		viewerPage.assertFalse(cpStatus, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying image should be rotated", "Verified image is rotated");

		//Change layout to 1X1
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying rotated position should be retained when layout changed to"+ViewerPageConstants.ONE_BY_ONE_LAYOUT );
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Verify rotated position should be retained when layout changed to "+ViewerPageConstants.ONE_BY_ONE_LAYOUT, "TC711_Checkpoint_Orientation_Retained_"+ViewerPageConstants.ONE_BY_ONE_LAYOUT);

		//Change layout ti 2X2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying rotated position should be retained when layout changed to"+ViewerPageConstants.TWO_BY_TWO_LAYOUT );
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify rotated position should be retained when layout changed to "+ViewerPageConstants.TWO_BY_TWO_LAYOUT, "TC711_Checkpoint_Orientation_Retained_"+ViewerPageConstants.TWO_BY_TWO_LAYOUT);
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US233"}, dataProvider="getDataFromExcelFile", dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test02_US233"})
	public void test02_US233_TC455_TC778_TC779_TC780_verifyImageOrientationOnSingleMonitor(String patientFilePath) throws InterruptedException {


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify theImage orientation");

		// Checkpoint 1: North Star is launched and currently on the Patient List page.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Logging in to the Viewer Page", "Proceeding to load the Patient Data into the viewer.");
		patientListPage = new PatientListPage(driver);
		patientListPage.assertTrue(patientListPage.getCurrentPageURL().contains("patient"), "North Star is launched and is currently on the Patient List page.", "The User is at page: "+ patientListPage.getCurrentPageURL());

		// Step 2: Load the Patient data with Annotation Display Level F.
		// Select the data from the Patient List
		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		helper = new HelperClass(driver);
		//Begin Viewer Actions.
		viewerPage =helper.loadViewerDirectly(patientName, 1);
		viewerPage.waitForViewerpageToLoad(2);



		// Set the Text Overlay level to full.
		textOverlay=new ViewerTextOverlays(driver);
		orientation=new ViewerOrientation(driver);
		layout=new ViewerLayout(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		//Checkpoint 2: The expected amount of Text Overlay is visible and legible.
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+dataProperty.getProperty("patientName_CT1")+"in viewer" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 2: Verify that the text overlay is properly set to Full", "US233_Checkpoint2_FullOverlay_VB1_" + patientName + "_2x2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint 2: Verify that the text overlay is properly set to Full", "US233_Checkpoint2_FullOverlay_VB2_" + patientName + "_2x2");


		// Step 3: Hover the mouse over each orientation marker.
		// Checkpoint 3: Rotation arrows display symmetrically when hovering over each orientation markers. No Flip markers or red boxes should appear.
		orientation.mouseHover(orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the top orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_TopOrientationMarker_VB1_" + patientName + "_2x2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the top orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_TopOrientationMarker_VB2_" + patientName + "_2x2");

		orientation.mouseHover(orientation.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the bottom orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_BottomOrientationMarker_VB1_" + patientName + "_2x2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the bottom orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_BottomOrientationMarker_VB2_" + patientName + "_2x2");

		orientation.mouseHover(orientation.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the left orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_LeftOrientationMarker_VB1_" + patientName + "_2x2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the left orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_LeftOrientationMarker_VB2_" + patientName + "_2x2");


		orientation.mouseHover(orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the right orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_RightOrientationMarker_VB1_" + patientName + "_2x2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the left orientation marker. No Flip markers or red boxes appear.",  "US233_Checkpoint3_RightOrientationMarker_VB2_" + patientName + "_2x2");

		//viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Verify that the rotation arrows display symmetrically when hovering over the right orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_RightOrientationMarker_" + patientName + "_2x2");
		// Then click on the clockwise and counterclockwise rotation arrow on each orientation marker one after another for each orientation marker.
		// Checkpoint 3: Each rotation arrow rotates the loaded image 90 degrees in the appropriate direction. After all rotations, image should be in default orientation
		// Top Orientation Marker
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Top Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_top_90_degrees_rotation_C_" + patientName + "_2x2");
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Top Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_top_rotation_canceled_" + patientName + "_2x2");

		// Bottom Orientation Marker
		orientation.rotateSeries(orientation.getBottomOrientationMarker(1), orientation.bottomClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Bottom Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_bottom_90_degrees_rotation_C_" + patientName + "_2x2");
		orientation.rotateSeries(orientation.getBottomOrientationMarker(1), orientation.bottomCounterClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Bottom Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_rotation_rotation_canceled_" + patientName + "_2x2");

		// Left Orientation Marker
		orientation.rotateSeries(orientation.getLeftOrientationMarker(1), orientation.leftClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Left Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_left_90_degrees_rotation_C_" + patientName + "_2x2");
		orientation.rotateSeries(orientation.getLeftOrientationMarker(1), orientation.leftCounterClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Left Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_left_rotation_canceled_" + patientName + "_2x2");

		// Right Orientation Marker
		orientation.rotateSeries(orientation.getRightOrientationMarker(1), orientation.rightClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Right Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_right_90_degrees_rotation_C_" + patientName + "_2x2");
		orientation.rotateSeries(orientation.getRightOrientationMarker(1), orientation.rightCounterClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3: Right Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_right_rotation_canceled_" + patientName + "_2x2");

		// Step 4: Hover the mouse over each orientation marker. Click on the orientation marker once and observe the result. Then click on the same orientation marker again.
		// Checkpoint 4: Clicking once on an orientation marker results in a flip along the appropriate axis. Clicking on the top or bottom orientation markers result in a flip along the horizontal axis, and clicking on the left or right orientation markers result in a flip along the vertical axis. 
		// The loaded image should be back in its default position, as the axis flips should have canceled each other out.
		// Top Orientation Marker
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Top Orientation Marker: Image flipped horizontally.", "US233_Checkpoint4_top_flip_" + patientName + "_2x2");
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		orientation.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Top Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_top_flip_canceled_" + patientName + "_2x2");

		// Bottom Orientation Marker
		orientation.flipSeries( orientation.getBottomOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Bottom Orientation Marker: Image flipped horizontally.", "US233_Checkpoint4_bottom_flip_" + patientName + "_2x2");
		orientation.flipSeries( orientation.getBottomOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Bottom Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_bottom_flip_canceled_" + patientName + "_2x2");

		// Left Orientation Marker
		orientation.flipSeries( orientation.getLeftOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Left Orientation Marker: Image flipped vertically.", "US233_Checkpoint4_left_flip_" + patientName + "_2x2");
		orientation.flipSeries( orientation.getLeftOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Left Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_left_flip_canceled_" + patientName + "_2x2");

		// Right Orientation Marker
		orientation.flipSeries( orientation.getRightOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Right Orientation Marker: Image flipped vertically.", "US233_Checkpoint4_right_flip_" + patientName + "_2x2");
		orientation.flipSeries( orientation.getRightOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4: Right Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_right_flip_canceled_" + patientName + "_2x2");

		// Step 5: Hover over the top orientation marker and click the clockwise rotation arrow. Then perform a horizontal flip on the loaded image.
		orientation.rotateSeries( orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		orientation.flipSeries( orientation.getTopOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);

		// Checkpoint 5: The image is rotated 90 degrees and flipped horizontally from the default image.
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 5: Image has been rotated 90 degrees clockwise, and is flipped horizontally.", "US233_Checkpoint5_top_clockwise_flip_" + patientName);

		// Step 6: Revert the changes made in the last step.
		orientation.flipSeries( orientation.getTopOrientationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		orientation.rotateSeries( orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);
		//Then mouse over to the second viewbox, and click on the top orientation marker�s counter-clockwise rotation arrow twice to rotate the image 180 degrees.
		orientation.mouseHover(orientation.getTopOrientationMarker(1));
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerPage.waitForTimePeriod(1000);

		// Checkpoint 6: The series in the first viewbox is reverted to default. The series in the second viewbox is rotated 180 degrees counterclockwise.
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 6: Series in first viewbox is reverted back to default.", "US233_Checkpoint6_first_series_default_" + patientName);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2), "Checkpoint 6: Series in second viewbox is rotated 180 degrees.", "US233_Checkpoint6_second_series_180_turn_" + patientName);

		// Step 7: Change the layout from the default 2x2 to 2x3.
		layout.selectLayout(layout.threeByTwoLayoutIcon);

		// Checkpoint 7: The layout is changed from the default to the 2x3 layout. Existing changes are preserved upon layout change.
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 7: Viewer is displayed in 2x3.", "US233_Checkpoint7_2x3_changes_retained_VB1_" + patientName);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2), "Checkpoint 7: Viewer is displayed in 2x3.", "US233_Checkpoint7_2x3_changes_retained_VB2_" + patientName);

		// Step 8: Hover the mouse over the orientation marker on the right side of the first viewbox. 
		//         Then move the mouse to hover over the orientation marker on the left side of the second viewbox.
		// Checkpoint 8: Rotation arrows only appear in the active viewbox.
		orientation.mouseHover(orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 8: Rotation Arrows only visible in Viewbox 1", "US233_Checkpoint8_Rotation_arrows_Right_vb1_" + patientName);
		orientation.mouseHover(orientation.getLeftOrientationMarker(2));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 8: Rotation Arrows only visible in Viewbox 2", "US233_Checkpoint8_Rotation_arrows_Left_vb2_" + patientName);
		orientation.mouseHover(orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 8: Rotation Arrows only visible in Viewbox 1", "US233_Checkpoint8_Rotation_arrows__Right_1_vb1_" + patientName);
		orientation.mouseHover(orientation.getLeftOrientationMarker(2));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2), "Checkpoint 8: Rotation Arrows only visible in Viewbox 2", "US233_Checkpoint8_Rotation_arrows_Left_1_vb2_" + patientName);

		// Repeat steps 2 through 4 in new layout.
		//Checkpoint 2 2x3: The expected amount of Text Overlay is visible and legible.
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+dataProperty.getProperty("patientName_CT1")+"in viewer" );
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 2 2x3 Repeat: Verify that the text overlay is properly set to Full", "US233_Checkpoint2_FullOverlay_VB1_" + patientName + "_2x3");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2), "Checkpoint 2 2x3 Repeat: Verify that the text overlay is properly set to Full", "US233_Checkpoint2_FullOverlay_VB2_" + patientName + "_2x3");

		// Step 3: Hover the mouse over each orientation marker.
		// Checkpoint 3: Rotation arrows display symmetrically when hovering over each orientation markers. No Flip markers or red boxes should appear.
		orientation.mouseHover(orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Verify that the rotation arrows display symmetrically when hovering over the top orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_TopOrientationMarker_" + patientName + "_2x3");
		orientation.mouseHover(orientation.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Verify that the rotation arrows display symmetrically when hovering over the bottom orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_BottomOrientationMarker_" + patientName + "_2x3");
		orientation.mouseHover(orientation.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Verify that the rotation arrows display symmetrically when hovering over the left orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_LeftOrientationMarker_" + patientName + "_2x3");
		orientation.mouseHover(orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Verify that the rotation arrows display symmetrically when hovering over the right orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_RightOrientationMarker_" + patientName + "_2x3");

		// Then click on the clockwise and counterclockwise rotation arrow on each orientation marker one after another for each orientation marker.
		// Checkpoint 3: Each rotation arrow rotates the loaded image 90 degrees in the appropriate direction. After all rotations, image should be in default orientation
		// Top Orientation Marker
		orientation.rotateSeries( orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Top Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_top_90_degrees_rotation_C_" + patientName + "_2x3");
		orientation.rotateSeries( orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Top Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_top_rotation_canceled_" + patientName + "_2x3");

		// Bottom Orientation Marker
		orientation.rotateSeries( orientation.getBottomOrientationMarker(1), orientation.bottomClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Bottom Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_bottom_90_degrees_rotation_C_" + patientName + "_2x3");
		orientation.rotateSeries( orientation.getBottomOrientationMarker(1), orientation.bottomCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Bottom Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_rotation_rotation_canceled_" + patientName + "_2x3");

		// Left Orientation Marker
		//		orientation.rotateSeries( orientation.getLeftOrientationMarker(1), orientation.leftClockwiseRotationMarker(1));
		//		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Left Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_left_90_degrees_rotation_C_" + patientName + "_2x3");
		//		orientation.rotateSeries( orientation.getLeftOrientationMarker(1), orientation.leftCounterClockwiseRotationMarker(1));
		//		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Left Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_left_rotation_canceled_" + patientName + "_2x3");

		// Right Orientation Marker
		orientation.rotateSeries( orientation.getRightOrientationMarker(1), orientation.rightClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Right Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_right_90_degrees_rotation_C_" + patientName + "_2x3");
		orientation.rotateSeries( orientation.getRightOrientationMarker(1), orientation.rightCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 2x3 Repeat: Right Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_right_rotation_canceled_" + patientName + "_2x3");

		// Step 4: Hover the mouse over each orientation marker. Click on the orientation marker once and observe the result. Then click on the same orientation marker again.
		// Checkpoint 4: Clicking once on an orientation marker results in a flip along the appropriate axis. Clicking on the top or bottom orientation markers result in a flip along the horizontal axis, and clicking on the left or right orientation markers result in a flip along the vertical axis. 
		// The loaded image should be back in its default position, as the axis flips should have canceled each other out.
		// Top Orientation Marker
		orientation.flipSeries( orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Top Orientation Marker: Image flipped horizontally.", "US233_Checkpoint4_top_flip_" + patientName + "_2x3");
		orientation.flipSeries( orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Top Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_top_flip_canceled_" + patientName + "_2x3");

		// Bottom Orientation Marker
		orientation.flipSeries( orientation.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Bottom Orientation Marker: Image flipped horizontally.", "US233_Checkpoint4_bottom_flip_" + patientName + "_2x3");
		orientation.flipSeries( orientation.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Bottom Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_bottom_flip_canceled_" + patientName + "_2x3");

		// Right Orientation Marker
		orientation.flipSeries( orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Right Orientation Marker: Image flipped vertically.", "US233_Checkpoint4_right_flip_" + patientName + "_2x3");
		orientation.flipSeries( orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Right Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_right_flip_canceled_" + patientName + "_2x3");


		// Left Orientation Marker
		orientation.flipSeries( orientation.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Left Orientation Marker: Image flipped vertically.", "US233_Checkpoint4_left_flip_" + patientName + "_2x3");
		orientation.flipSeries( orientation.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 2x3 Repeat: Left Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_left_flip_canceled_" + patientName + "_2x3");


		/*
		// Repeat steps 2 through 4 in a different browser window size.
		//Dimension resizeDimension = new Dimension(1280, 720); 
		//viewerPage.setWindowSize(viewerPage.getCurrentWindowID(), resizeDimension);

		//Checkpoint 2 2x3: The expected amount of Text Overlay is visible and legible.
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+dataProperty.getProperty("patientName_CT1")+"in viewer" );
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 2 Resize Repeat: Verify that the text overlay is properly set to Full", "US233_Checkpoint2_FullOverlay_" + patientName + "_Resize");

		// Step 3: Hover the mouse over each orientation marker.
		// Checkpoint 3: Rotation arrows display symmetrically when hovering over each orientation markers. No Flip markers or red boxes should appear.
		viewerPage.mouseHover(viewerPage.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 3 Resize Repeat: Verify that the rotation arrows display symmetrically when hovering over the top orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_TopOrientationMarker_" + patientName + "_Resize");
		viewerPage.mouseHover(viewerPage.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 3 Resize Repeat: Verify that the rotation arrows display symmetrically when hovering over the bottom orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_BottomOrientationMarker_" + patientName + "_Resize");
		viewerPage.mouseHover(viewerPage.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 3 Resize Repeat: Verify that the rotation arrows display symmetrically when hovering over the left orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_LeftOrientationMarker_" + patientName + "_Resize");
		viewerPage.mouseHover(viewerPage.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 3 Resize Repeat: Verify that the rotation arrows display symmetrically when hovering over the right orientation marker. No Flip markers or red boxes appear.", "US233_Checkpoint3_RightOrientationMarker_" + patientName + "_Resize");

		// Then click on the clockwise and counterclockwise rotation arrow on each orientation marker one after another for each orientation marker.
		// Checkpoint 3: Each rotation arrow rotates the loaded image 90 degrees in the appropriate direction. After all rotations, image should be in default orientation
		// Top Orientation Marker
		viewerPage.rotateSeries( viewerPage.getTopOrientationMarker(1), viewerPage.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Top Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_top_90_degrees_rotation_C_" + patientName + "_Resize");
		viewerPage.rotateSeries( viewerPage.getTopOrientationMarker(1), viewerPage.topCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Top Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_top_rotation_canceled_" + patientName + "_Resize");

		// Bottom Orientation Marker
		viewerPage.rotateSeries( viewerPage.getBottomOrientationMarker(1), viewerPage.bottomClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Bottom Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_bottom_90_degrees_rotation_C_" + patientName + "_Resize");
		viewerPage.rotateSeries( viewerPage.getBottomOrientationMarker(1), viewerPage.bottomCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Bottom Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_rotation_rotation_canceled_" + patientName + "_Resize");

		// Left Orientation Marker
		viewerPage.rotateSeries( viewerPage.getLeftOrientationMarker(1), viewerPage.leftClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Left Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_left_90_degrees_rotation_C_" + patientName + "_Resize");
		viewerPage.rotateSeries( viewerPage.getLeftOrientationMarker(1), viewerPage.leftCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Left Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_left_rotation_canceled_" + patientName + "_Resize");

		// Right Orientation Marker
		viewerPage.rotateSeries( viewerPage.getRightOrientationMarker(1), viewerPage.rightClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Right Orientation Marker: Image rotated 90 degrees clockwise.", "US233_Checkpoint3_right_90_degrees_rotation_C_" + patientName + "_Resize");
		viewerPage.rotateSeries( viewerPage.getRightOrientationMarker(1), viewerPage.rightCounterClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 3 Resize Repeat: Right Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US233_Checkpoint3_right_rotation_canceled_" + patientName + "_Resize");

		// Step 4: Hover the mouse over each orientation marker. Click on the orientation marker once and observe the result. Then click on the same orientation marker again.
		// Checkpoint 4: Clicking once on an orientation marker results in a flip along the appropriate axis. Clicking on the top or bottom orientation markers result in a flip along the horizontal axis, and clicking on the left or right orientation markers result in a flip along the vertical axis. 
		// The loaded image should be back in its default position, as the axis flips should have canceled each other out.
		// Top Orientation Marker
		viewerPage.flipSeries( viewerPage.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Top Orientation Marker: Image flipped horizontally.", "US233_Checkpoint4_top_flip_" + patientName + "_Resize");
		viewerPage.flipSeries( viewerPage.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Top Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_top_flip_canceled_" + patientName + "_Resize");

		// Bottom Orientation Marker
		viewerPage.flipSeries( viewerPage.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Bottom Orientation Marker: Image flipped horizontally.", "US233_Checkpoint4_bottom_flip_" + patientName + "_Resize");
		viewerPage.flipSeries( viewerPage.getBottomOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Bottom Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_bottom_flip_canceled_" + patientName + "_Resize");

		// Left Orientation Marker
		viewerPage.flipSeries( viewerPage.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Left Orientation Marker: Image flipped vertically.", "US233_Checkpoint4_left_flip_" + patientName + "_Resize");
		viewerPage.flipSeries( viewerPage.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Left Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_left_flip_canceled_" + patientName + "_Resize");

		// Right Orientation Marker
		viewerPage.flipSeries( viewerPage.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Right Orientation Marker: Image flipped vertically.", "US233_Checkpoint4_right_flip_" + patientName + "_Resize");
		viewerPage.flipSeries( viewerPage.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint 4 Resize Repeat: Right Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_right_flip_canceled_" + patientName + "_Resize");
		 */
	}

	@Test(groups ={"firefox","Chrome","Edge","IE11"}, dataProvider="getDataFromExcelFile", dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test03_DE243_TC1057"})
	public void test03_DE243_TC1057_verifyMarkersBackgrndAbsenceSingleMonitor(String patientFilePath) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("The orientation markers with back ground shadow appearing for some images");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Logging in to the Viewer Page", "Loading the Patient AH.4 into the viewer.");

		// Select the data from the Patient List
		String filePath = Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		patientListPage = new PatientListPage(driver);

		helper = new HelperClass(driver);
		//Begin Viewer Actions.
		viewerPage =helper.loadViewerDirectly(patientName, 1);
		orientation=new ViewerOrientation(driver);
		// Checkpoint 1:
		// All orientation markers display one, single character correctly. The second-drawn character is aligned properly with the first-drawn character.
		//viewerPage.compareElementImage("DE243", viewerPage.mainViewer, "Checkpoint 1: Verify that the orientation markers display correctly. Each marker is double-drawn to ensure visibility, with the doubled character in the right location.", "USDE243_Checkpoint1_" + patientName);
		viewerPage.compareElementImage(protocolName, orientation.getTopOrientationMarker(1), "Checkpoint 1: Verify that the orientation markers display correctly. Each marker is double-drawn to ensure visibility, with the doubled character in the right location.", "USDE243_Checkpoint1_TopOrientationMarker_" + patientName);
		viewerPage.compareElementImage(protocolName, orientation.getBottomOrientationMarker(1), "Checkpoint 1: Verify that the orientation markers display correctly. Each marker is double-drawn to ensure visibility, with the doubled character in the right location.", "USDE243_Checkpoint1_BottomOrientationMarker_" + patientName);
		viewerPage.compareElementImage(protocolName, orientation.getLeftOrientationMarker(1), "Checkpoint 1: Verify that the orientation markers display correctly. Each marker is double-drawn to ensure visibility, with the doubled character in the right location.", "USDE243_Checkpoint1_LeftOrientationMarker_" + patientName);
		viewerPage.compareElementImage(protocolName, orientation.getRightOrientationMarker(1), "Checkpoint 1: Verify that the orientation markers display correctly. Each marker is double-drawn to ensure visibility, with the doubled character in the right location.", "USDE243_Checkpoint1_RightOrientationMarker_" + patientName);
	}

	//	TC1: Correctness of orientation for proper visualization (Automated) 
	@Test(groups ={"Chrome"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test01_US65_TC587"})
	public void test04_US65_TC587_verifyOrientationMarkers(String fileName, String expectedOrientation1, String expectedOrientation2) throws InterruptedException {

		// Limitation in running the script in Firefox and Edge browsers.
		// Not opening child windows as behavior of application changed. Child windows remains empty until apply monitor is selected.
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate initial values for proper visualization- Orientation");

		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName, 1, 1);
		layout=new ViewerLayout(driver);

		if(!expectedOrientation1.isEmpty()) {
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[1/3]", "Validate orientation markers are correctly displayed for "+patientName);
			validateOrientationSafetyViewBox1(expectedOrientation1);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[2/3]", "Validate orientation markers are correctly displayed after changing orientation");
			layout.selectLayout(layout.oneByTwoLayoutIcon);
			validateOrientationSafetyViewBox2(expectedOrientation2);
		}else {
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[3/3]", "Validate orientation markers are not shown on any images for: "+patientName);
			validateOrientationInvisibility();
		}

	}

	@Test(groups ={"Chrome"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test02_US65_TC696"})
	public void test05_US65_TC696_verifyWindowLevelForProperVisualization(String fileName, String expectedWW, String expectedWC, String viewBox) throws InterruptedException {

		// Limitation in running the script in Firefox and Edge browsers.
		// Not opening child windows as behavior of application changed. Child windows remains empty until apply monitor is selected.
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate initial values for proper visualization- Window Level");

		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Validate that WW/WC values are correctly displayed for "+patientName);

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName, 1, 1);

		validateWWAndWC(expectedWW, expectedWC, patientName, viewBox);
		if(patientName.contains("Liver")) {
			viewerPage.mouseWheelScrollInViewer(1, "down", 10);
			validateWWAndWC(expectedWW, expectedWC, patientName, viewBox);
		}

	}

	//	TC2: Correctness of all annotation markers (A, P, H and F) on viewer default layout for MR (Automated

	@Test(groups ={"Chrome"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test03_US65_TC776"})
	public void test06_US65_TC776_verifyInitialValuesForOrientationSafety(String fileName, String expectedPattern) throws InterruptedException {

		// Limitation in running the script in Firefox and Edge browsers.
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Display study to validate initial values for proper visualization-Orientation-Safety");
		patientListPage = new PatientListPage(driver);

		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1]:Checkpoint[1/1]", "Validate the letter burned into the pixel are displayed with correct orientation"
				+ fileName);

		validatePatient(patientName, expectedPattern);
	}

	@Test(groups ={"Chrome","firefox","IE11","Sanity"})
	public void test07_DE396_TC1514_verifyMarkersPresentOnChangingLayout() throws InterruptedException{
		String testcase = "DE396_TC1514";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify all annotation markers on viewer on changing layout");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Logging in to the Viewer Page", "Loading the Patient into the viewer.");

		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerPage =helper.loadViewerDirectly(patientName_MR_MultiPhase, 1);
		

		// Checkpoint 1:
		// All orientation markers displayed on default window
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 1: Verify that the orientation markers display correctly in default window.", "Checkpoint1_OrientationMarker_" + testcase+"Checkpoint1_Default_Window_"+"patientName_MR_MultiPhase");

		//changing layout to 3x3 
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerPage.waitForAllImagesToLoad();
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 2: Verify that the orientation markers display correctly new 3x3 layout.", "Checkpoint2_OrientationMarker_" + testcase+"Checkpoint2_ChangingLayout_3x3_"+"patientName_MR_MultiPhase");
	}

	@Test(groups ={"Chrome","IE11","Edge","Negative","DE1824"})
	public void test08_DE1824_TC1514_verifyMarkersPresentOnChangingLayout() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that orientation markers on images are not changed on one-up from 1*1 layout");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Logging in to the Viewer Page", "Loading the Patient into the viewer.");

		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerPage =helper.loadViewerDirectly(liver9PatientName, 1);
		
		orientation=new ViewerOrientation(driver);

		String topOrientation = orientation.getTopOrientationMarkerText(1);
		String leftOrientation = orientation.getLeftOrientationMarkerText(1);
		String rightOrientation = orientation.getRightOrientationMarkerText(1);
		String bottomOrientation = orientation.getBottomOrientationMarkerText(1);

		viewerPage.doubleClickOnViewbox(1);
		viewerPage.waitForViewerpageToLoad();


		viewerPage.assertEquals(orientation.getTopOrientationMarkerText(1), topOrientation, "Checkpoint[1/14]", "Verifying the top orientation marker after double click");
		viewerPage.assertEquals(orientation.getLeftOrientationMarkerText(1), leftOrientation, "Checkpoint[2/14]", "Verifying the left orientation marker after double click");
		viewerPage.assertEquals(orientation.getRightOrientationMarkerText(1), rightOrientation, "Checkpoint[3/14]", "Verifying the right orientation marker after double click");
		viewerPage.assertEquals(orientation.getBottomOrientationMarkerText(1), bottomOrientation, "Checkpoint[4/14]", "Verifying the bottom orientation marker after double click");

		orientation.flipSeries(orientation.getTopOrientationMarker(1));

		viewerPage.assertEquals(orientation.getTopOrientationMarkerText(1),bottomOrientation , "Checkpoint[5/14]", "Verifying the top orientation marker after flip is changed to bottom marker");
		viewerPage.assertEquals(orientation.getBottomOrientationMarkerText(1), topOrientation, "Checkpoint[6/14]", "Verifying the bottom orientation marker after flip is changed to top marker");

		viewerPage.doubleClickOnViewbox(1);
		viewerPage.waitForViewerpageToLoad();

		viewerPage.assertEquals(orientation.getTopOrientationMarkerText(1),bottomOrientation , "Checkpoint[7/14]", "Verifying the top orientation marker after restoring to default layout");
		viewerPage.assertEquals(orientation.getBottomOrientationMarkerText(1), topOrientation, "Checkpoint[8/14]", "Verifying the bottom orientation marker after restoring to default layout");
		viewerPage.assertEquals(orientation.getLeftOrientationMarkerText(1), leftOrientation, "Checkpoint[9/14]", "Verifying the left orientation marker after restoring to default layout");
		viewerPage.assertEquals(orientation.getRightOrientationMarkerText(1), rightOrientation, "Checkpoint[10/14]", "Verifying the right orientation marker after restoring to default layout");

		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);

		viewerPage.assertEquals(orientation.getTopOrientationMarkerText(1),bottomOrientation , "Checkpoint[11/14]", "Verifying the top orientation marker after change layout to 1x1");
		viewerPage.assertEquals(orientation.getBottomOrientationMarkerText(1), topOrientation, "Checkpoint[12/14]", "Verifying the bottom orientation marker after change layout to 1x1");
		viewerPage.assertEquals(orientation.getLeftOrientationMarkerText(1), leftOrientation, "Checkpoint[13/14]", "Verifying the left orientation marker after change layout to 1x1");
		viewerPage.assertEquals(orientation.getRightOrientationMarkerText(1), rightOrientation, "Checkpoint[14/14]", "Verifying the right orientation marker after change layout to 1x1");


	}

	public void validatePatient(String patientName, String expectedPattern) throws InterruptedException {
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientName, 1, 1);
		validateOrientationSafetyViewBox1(expectedPattern);
	}


	public void validateWWAndWC(String expectedWW, String expectedWC, String PatientName, String viewBox) {
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		viewerPage.assertEquals(viewerPage.getWindowCenterText(Integer.parseInt(viewBox)), expectedWC, "Validate WC for "+PatientName, "WC value is as expected for "+PatientName);
		viewerPage.assertEquals(viewerPage.getWindowWidthValText(Integer.parseInt(viewBox)), expectedWW, "Validate WW for "+PatientName, "WW value is as expected for "+PatientName);
	}

	public void validateOrientationSafetyViewBox1(String expectedPattern) {
		orientation=new ViewerOrientation(driver);
		orientation.waitForViewerpageToLoad();
		StringBuilder actualPatternFromSample = new StringBuilder();

		actualPatternFromSample.append(orientation.getTopOrientationMarker(1).getText());
		actualPatternFromSample.append(orientation.getRightOrientationMarker(1).getText());
		actualPatternFromSample.append(orientation.getBottomOrientationMarker(1).getText());
		actualPatternFromSample.append(orientation.getLeftOrientationMarker(1).getText());

		orientation.assertEquals(actualPatternFromSample.toString(), expectedPattern, "Validating Orientaion Pattern for given Sample", "Orientation pattern for Sample equals: "+expectedPattern);
	}

	public void validateOrientationSafetyViewBox2(String expectedPattern) {
		orientation=new ViewerOrientation(driver);
		orientation.waitForViewerpageToLoad();

		StringBuilder actualPatternFromSample = new StringBuilder();

		actualPatternFromSample.append(orientation.getTopOrientationMarker(2).getText());
		actualPatternFromSample.append(orientation.getRightOrientationMarker(2).getText());
		actualPatternFromSample.append(orientation.getBottomOrientationMarker(2).getText());
		actualPatternFromSample.append(orientation.getLeftOrientationMarker(2).getText());

		orientation.assertEquals(actualPatternFromSample.toString(), expectedPattern, "Validating Orientaion Pattern for given Sample", "Orientation pattern for Sample equals: "+expectedPattern);
	}

	public void validateOrientationInvisibility() {
		orientation=new ViewerOrientation(driver);
		orientation.waitForViewerpageToLoad();
		orientation.assertTrue(orientation.getTopOrientationMarkerText(1).isEmpty(), "Validate top orientation marker", "Top orientation marker is not displayed");
		orientation.assertTrue(orientation.getBottomOrientationMarkerText(1).isEmpty(), "Validate bottom orientation marker", "Bottom orientation marker is not displayed");
		orientation.assertTrue(orientation.getLeftOrientationMarkerText(1).isEmpty(), "Validate left orientation marker", "Left orientation marker is not displayed");
		orientation.assertTrue(orientation.getRightOrientationMarkerText(1).isEmpty(), "Validate right orientation marker", "Right orientation marker is not displayed");

	}





}
