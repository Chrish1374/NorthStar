package com.trn.ns.test.viewer.overlays;

import static com.trn.ns.test.configs.Configurations.PASS;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I29_OrientationOverlay-CF0304ARevD  - revision-0
//Functional.NS.I15 ViewerOverlayInteraction-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ImageOrientationTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerOrientation orientation;
	
	private ExtentTest  extentTest;
	
	String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	private HelperClass helper;
	
	// Before method, handles the steps before loading the data for set up.

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

	}

	//Limitation: works only in Chrome , TC251, TC252 and  TC253 are same test steps repeating the different browsers.
//	Test 1: Change Image Orientation 2D images (Automated) 
//	TC5: Refactor Image orientation - Google Chrome (Automated)  http
//	TC1: Correctness of orientation markers when image is flipped or orientation changed. (Automated)
	@Test(groups ={"Chrome","US71","Sanity"})
	public void test01_US71_TC189_ChangeImageOrientation2Dimages() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Change Image Orientation 2D images");
		//Get Patient Name
		String filePath = Configurations.TEST_PROPERTIES.get("DX_THORAX_RIBS1_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[1]: Checkpoint[1/8]", "Loading the Patient "+PatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerpage =helper.loadViewerDirectly(PatientName, 1);
		

		// Step 1: Hover the mouse over each orientation marker and checking the presence. There SVG icons will be displaying on four sides of the image

		orientation=new ViewerOrientation(driver);
		orientation.mouseHover(orientation.getTopOrientationMarker(1));
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint TC1[1] : Checkpoint TC1[2] :Checkpoint TC5[2]: Checkpoint TC1[2] : Checkpoint 1/9: Verify that the rotation arrows display  when hovering over the top orientation marker. No image broken appearance.", "US71_Checkpoint1_TopOrientationMarker_" + PatientName  );
		orientation.mouseHover(orientation.getBottomOrientationMarker(1));
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint TC1[1] : Checkpoint TC1[2] :Checkpoint TC5[2]: Checkpoint TC1[2] : Checkpoint 1/9: Verify that the rotation arrows display  when hovering over the bottom orientation marker.  No image broken appearance.", "US71_Checkpoint1_BottomOrientationMarker_" + PatientName );
		orientation.mouseHover(orientation.getLeftOrientationMarker(1));
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint TC1[1] : Checkpoint TC1[2] :Checkpoint TC5[2]: Checkpoint TC1[3]:Checkpoint 1/9: Verify that the rotation arrows display when hovering over the left orientation marker.  No image broken appearance.", "US71_Checkpoint1_LeftOrientationMarker_" + PatientName );
		orientation.mouseHover(orientation.getRightOrientationMarker(1));
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint TC1[1] : Checkpoint TC1[2] :Checkpoint TC5[2]: Checkpoint TC1[3]:Checkpoint 1/9: Verify that the rotation arrows display  when hovering over the right orientation marker. No image broken appearance.", "US71_Checkpoint1_RightOrientationMarker_" + PatientName );

		// Image flipped horizontally from Top Orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[3] :Checkpoint TC5[3]:Checkpoint TC1[4]:Checkpoint 2/9: Top Orientation Marker: Image flipped horizontally.", "US71_Checkpoint2_top_flip_" + PatientName );
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[3] :Checkpoint TC5[3]:Checkpoint TC1[4]:Checkpoint 2/9: Top Orientation Marker: Image flipped horizontally. Returned to default.", "US71_Checkpoint4_Returned to default_" + PatientName );

		// Image flipped horizontally from Bottom Orientation Marker
		orientation.flipSeries(orientation.getBottomOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[4] :Checkpoint TC5[4]: Checkpoint TC1[5]:Checkpoint 3/9: Bottom Orientation Marker: Image flipped horizontally.", "US71_Checkpoint3_bottom_flip_" + PatientName );
		orientation.flipSeries(orientation.getBottomOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[4] :Checkpoint TC5[4]: Checkpoint TC1[5]:Checkpoint 3/9: Bottom Orientation Marker: Image flipped horizontally. Returned to default.", "US233_Checkpoint4_bottom_flip_canceled_" + PatientName );

		// Image flipped vertically from Left Orientation Marker
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[5] :Checkpoint TC1[6] :Checkpoint TC5[5]: Checkpoint TC1[6]:Checkpoint 4/9: Left Orientation Marker: Image flipped vertically.", "US71_Checkpoint4_left_flip_" + PatientName );
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[5] :Checkpoint TC1[6] :Checkpoint TC5[5]: Checkpoint TC1[6]:Checkpoint 4/9: Left Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_left_flip_canceled_" + PatientName );

		// Image flipped vertically Right Orientation Marker
		orientation.flipSeries(orientation.getRightOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[7]:Checkpoint 4/9: Right Orientation Marker: Image flipped vertically.", "US71_Checkpoint4_right_flip_" + PatientName );
		orientation.flipSeries(orientation.getRightOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[7]:Checkpoint 4/9: Right Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint4_right_flip_canceled_" + PatientName );


		// Image rotated 90 degrees clockwise and  90 degrees counterclockwise
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[8]: Checkpoint 5/9: Top Orientation Marker: Image rotated 90 degrees clockwise.", "US71_Checkpoint5_top_90_degrees_rotation_C_" + PatientName );
		orientation.rotateSeries(orientation.getTopOrientationMarker(1),orientation.topCounterClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint TC1[9]: Checkpoint 6/9: Top Orientation Marker: Image rotated 90 degrees counterclockwise.", "US71_Checkpoint6_top_rotation_canceled_" + PatientName );


	}

	// Rotation/Flip after Displaying different study 
	//Limitation: works only in Chrome	
//	Test 1: Change Image Orientation 2D images (Automated) 
	@Test(groups ={"Chrome","US71"})
	public void test02_US71_TC189_ChangeImageOrientation2Dimages () throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Change Image Orientation 2D images");
		//Get Patient Name
		 String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
		 String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		 
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Loading the Patient "+liver9Patient+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerpage =helper.loadViewerDirectly(liver9Patient, 1);

		//Step 2
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/beforeFlipImageA.png");

		//Verify Rotation/flip 
		orientation=new ViewerOrientation(driver);
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/afterFlipImageA.png");

		String beforeFlipImageA = newImagePath+"/actualImages/beforeFlipImageA.png";
		String afterFlipImageA = newImagePath+"/actualImages/afterFlipImageA.png";
		String diffImagePathA = newImagePath+"/actualImages/FlipImageA.png";

		boolean cpStatusA =  viewerpage.compareimages(beforeFlipImageA, afterFlipImageA, diffImagePathA);
		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint TC1[1] & Checkpoint[8/9]", "Verifying 90 degree Anti-clockwise rotation");
		viewerpage.assertFalse(cpStatusA, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");

//		viewerpage.clickOnOrientation(NSConstants.Left_ORIENTATION_TEXTOVERLAY,viewerpage.topRightOverlay,1);
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/currentFlipImageA.png");

		String currentFlipImageA = newImagePath+"/actualImages/currentFlipImageA.png";
		String diffImagePathA1 = newImagePath+"/actualImages/FlipImageA1.png";
		boolean cpStatusA1 =  viewerpage.compareimages(beforeFlipImageA, currentFlipImageA, diffImagePathA1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1] & Checkpoint[9/9]", "Verifying 90 degree clockwise rotation");
		viewerpage.assertTrue(cpStatusA1, "Verify that the actual and Expected image are same","The actual and Expected image are same.");



	}	

	//DE135: Image is truncated and shifted when flipping
	//Limitation: works only in Chrome TC661, TC662 and  TC663 are the repetitions of same steps 

	@Test(groups ={"Chrome","US71"})
	public void test03_US71_DE135_TC617_ChangeImageOrientation2Dimages () throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Image is truncated and shifted when flipping");
		//Get Patient Name
		String filePath = Configurations.TEST_PROPERTIES.get("DX_THORAX_RIBS1_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);


		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Loading the Patient "+PatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		// Horizontall Flip 
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 2/7: Top Orientation Marker: Image flipped horizontally.", "US71_DE135 Checkpoint2_top_flip_" + PatientName );
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 3/7: Top Orientation Marker: Image flipped horizontally. Returned to default.", "US71___DE135 Checkpoint3_Returned to default_" + PatientName );

		// Vertical flip 
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 4/7: Left Orientation Marker: Image flipped vertically.", "US71_DE135 Checkpoint4_left_flip_" + PatientName );
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 5/7: Left Orientation Marker: Image flipped vertically. Returned to default.", "US233_Checkpoint5_left_flip_canceled_" + PatientName );

		// Image rotated 90 degrees clockwise and  90 degrees counterclockwise
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 6/7: Top Orientation Marker: Image rotated 90 degrees clockwise.", "US71__DE135 Checkpoint6_top_90_degrees_rotation_C_" + PatientName );
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 7/7: Top Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US71_Checkpoint7_top_rotation_canceled_" + PatientName );


	}	

	//DE74: Rotating left or right with images of different dimension leaves prior rotation in viewbox
	//Limitation: works only in Chrome	
	@Test(groups ={"Chrome","US71"})
	public void test04_US71_DE74_TC332_ChangeImageOrientation2Dimages() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Rotating left or right with images of different dimension leaves prior rotation in viewbox");
		//Get Patient Name
		String filePath = Configurations.TEST_PROPERTIES.get("DX_THORAX_RIBS1_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);


		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Loading the Patient "+PatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		orientation=new ViewerOrientation(driver);
		// Image flipped horizontally from Top Orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 2/9: Top Orientation Marker: Image flipped horizontally.", "US71_DE74 Checkpoint2_top_flip_" + PatientName );
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 3/9: Top Orientation Marker: Image flipped horizontally. Returned to default.", "US71_DE74 Checkpoint3_Returned to default_" + PatientName );

		// Image flipped vertically from Left Orientation Marker
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 4/9: Left Orientation Marker: Image flipped vertically.", "US71_DE74 Checkpoint4_left_flip_" + PatientName );
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 5/9: Left Orientation Marker: Image flipped vertically. Returned to default.", "US71_DE74 Checkpoint5_left_flip_canceled_" + PatientName );

		// Image rotated 90 degrees clockwise and  90 degrees counterclockwise
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 6/9: Top Orientation Marker: Image rotated 90 degrees clockwise.", "US_71_DE135 Checkpoint6_top_90_degrees_rotation_C_" + PatientName );
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topCounterClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 7/9: Top Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US71_DE74 Checkpoint7_top_rotation_canceled_" + PatientName );

		// Image rotated 90 degrees clockwise and  90 degrees counterclockwise from different orientation marker
		orientation.rotateSeries(orientation.getLeftOrientationMarker(1), orientation.leftClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 8/9 Resize Repeat: Left Orientation Marker: Image rotated 90 degrees clockwise.", "US71_DE74 Checkpoint8_left_90_degrees_rotation_C_" + PatientName);
		orientation.rotateSeries(orientation.getLeftOrientationMarker(1), orientation.leftCounterClockwiseRotationMarker(1));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Checkpoint 9/9 Resize Repeat: Left Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "US71_DE74 Checkpoint9_left_rotation_canceled_" + PatientName );

	}

	
	@Test(groups ={"Chrome","DE584"})
	public void test05_DE584_TC2354_VerifyOrientationControl() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Missing orientation controls on some series");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Loading the Patient "+boneagePatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);		
		viewerpage =helper.loadViewerDirectly(boneagePatientName, 1);
		viewerpage.waitForViewerpageToLoad(4);
		
		viewerpage.click(viewerpage.getViewPort(4));
		
		orientation.mouseHover(orientation.getTopOrientationMarker(4));
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(4), "Checkpoint[2/10] : Verifying the no orientation marker's presence", "test05_checkpoint2");
		
		// Image flipped horizontally from Top Orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(4));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4), "Checkpoint[3/10] : flipping it vertically","test05_checkpoint3");
		
		orientation.flipSeries(orientation.getTopOrientationMarker(4));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4),  "Checkpoint[4/10] : re-flipping it ", "test05_checkpoint4");

		// Image flipped vertically from Left Orientation Marker
		orientation.click(orientation.getLeftOrientationMarker(4));
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(4), "Checkpoint[5/10] : flipping it from left", "test05_checkpoint5");
		
		orientation.getRightOrientationMarker(4).click();
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4), "Checkpoint[6/10] : flipping it from right", "test05_checkpoint6");

		// Image rotated 90 degrees clockwise and  90 degrees counterclockwise
		orientation.topCounterClockwiseRotMarker(4).click();
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4),  "Checkpoint[7/10] : Top clockwise with 90 degree", "test05_checkpoint7");
		
		orientation.topClockwiseRotMarker(4).click();
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4),  "Checkpoint[8/10] : Top counter clockwise with 90 degree", "test05_checkpoint8");
		
		// Image rotated 90 degrees clockwise and  90 degrees counterclockwise from different orientation marker
		orientation.leftClockwiseRotationMarker(4).click();
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4),  "Checkpoint[9/10] : left clockwise with 90 degree", "test05_checkpoint9");
		
		orientation.leftCounterClockwiseRotationMarker(4).click();
		viewerpage.waitForTimePeriod(1000);
		viewerpage.compareElementImage(protocolName,  viewerpage.getViewPort(4),  "Checkpoint[10/10]: left counter clockwise with 90 degree", "test05_checkpoint10");		
		
	}
}