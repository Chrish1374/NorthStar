package com.trn.ns.test.viewer.synchronization;

import static com.trn.ns.test.configs.Configurations.PASS;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class OrientationSyncTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private ViewerOrientation orientation;
	private ViewerLayout layout;

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);
	
	String iblJohnDeofilePath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String iblPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iblJohnDeofilePath);

	private String filePath1 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String MRLSP_filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String lspPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,MRLSP_filePath);
	private String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	private String secondSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	private String fifthSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	protected String protocolName;
	

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US667","US289"})
	public void test01_US667_TC2367_US289_TC2417_verifyOrientationPositonForContentSelectorSeries() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that positions are retained on layout change"
				+ "<br> Verify Rotate/Flip synchronization for series selected from Content Selector.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4PatientName, username, password,2);
	
		viewerPage.closingConflictMsg();
		orientation=new ViewerOrientation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		
		// Image rotated 90 degrees clockwise 
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Image rotated 90 degrees clockwise.","TC01_CheckPoint1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Image rotated 90 degrees clockwise.","TC01_CheckPoint2");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that new series selected through content selector should not display the rotation changes");
		//Select same series from content selector in 3rd viewbox
		contentSelector.selectSeriesFromSeriesTab(3,firstSeriesDescriptionAH4);
		viewerPage.closingConflictMsg();
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3),"Verify new selected image does not display the previous rotation","TC01_CheckPoint3");

		//Verify image in first VB again rotated 90 degrees clockwise
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Image rotated 90 degrees clockwise.","TC01_CheckPoint4");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Image rotated 90 degrees clockwise.","TC01_CheckPoint5");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3),"Verify Top Orientation Marker: Image rotated 90 degrees clockwise.","TC01_CheckPoint6");


		//Verify that image in 3rd VB is also rotated relatively
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that same series in other viewbox should rotate relatively");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3),"Verify image in 3rd VB also rotate relatively","TC01_CheckPoint7");

		//Verifying for Sync series
		viewerPage.closingConflictMsg();
		contentSelector.selectSeriesFromSeriesTab(4,secondSeriesDescriptionAH4);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(4),"Verify new selected image does not display the previous rotation","TC01_CheckPoint8");

		//Again flip the image in first VB and verify that sync series also rotated relatively
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that sync series in other viewbox should rotate relatively");
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(4),"Verify image in 3rd VB also rotate relatively","TC01_CheckPoint9");

	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US667","US289","US741","Sanity","BVT"})
	public void test02_US667_TC2389_US289_TC2404_US741_TC2626_TC2627_verifyOrientationRotationForSeriesWithSameOrientation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the image synchronization with orientation rotation and flip for series having same orientation"
				+ "<br>Verify Rotate/Flip synchronization having same FrameReferenceUID and same Orientation.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+lspPatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(lspPatientName, username, password,2);
		orientation=new ViewerOrientation(driver);
	
		//Rotate the series and verify the rotation position or other series having same orientation
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Original Image in first VB rotated 90 degrees clockwise.","TC2389_CheckPoint1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Sync Image in second VB rotated 90 degrees clockwise.","TC2389_CheckPoint2");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3),"Verify Top Orientation Marker: Sync Image in third VB rotated 90 degrees clockwise.","TC2389_CheckPoint3");

		//Flipped the series and verify the rotation position or other series having same orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Sync Image in first VB flipped horizontally","TC2389_CheckPoint4");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Original Image in second VB flipped horizontally","TC2389_CheckPoint5");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3),"Verify Top Orientation Marker: Sync Image in third VB flipped horizontally","TC2389_CheckPoint6");
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"US667","US289","Sanity","BVT"})
	public void test03_US667_TC2390_US289_TC2406_verifyOrientationRotationForSeriesWithDiffOrientation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the image synchronization with orientation rotation and flip for series having different orientation"
				+ "<br> Verify Rotate/FLip synchronization with series having different Orientation.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+lspPatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(lspPatientName, username, password,2);
		orientation=new ViewerOrientation(driver);
	
		//Rotate the series and verify the rotation position or other series having different orientation
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Original Image in first VB rotated 90 degrees clockwise.","TC2390_CheckPoint1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(4),"Verify Top Orientation Marker: Image in forth VB having different orientation does not rotated 90 degrees clockwise","TC2390_CheckPoint2");

		//Flipped the series and verify the rotation position or other series having different orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Image in first VB having different orientation does not flipped horizontally","TC2390_CheckPoint3");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Image in second VB having different orientation does not flipped horizontally","TC2390_CheckPoint4");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(3),"Verify Top Orientation Marker: Image in third VB having different orientation does not flipped horizontally","TC2390_CheckPoint5");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(4),"Verify Top Orientation Marker: Original Image in forth VB flipped horizontally","TC2390_CheckPoint6");

	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"US667","US289"})
	public void test04_US667_TC2391_US289_TC2412_verifyOrientationRenderingForNonDICOMSeries() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the orientation rendering for non DICOM images"
				+ "<br> Verify Rotae/Flip synchronization for study having Dicom as well as Non-Dicom series.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4PatientName, username, password,2);
	
		viewerPage.closingConflictMsg();
		orientation=new ViewerOrientation(driver);
		
		ContentSelector contentSelector = new ContentSelector(driver);

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(3), newImagePath+"/goldImages/nonDICOM_1.png");
		
		//Selecting non-DICOM image in VB having sync series
		contentSelector.selectSeriesFromSeriesTab(2,fifthSeriesDescriptionAH4);
		contentSelector.closingConflictMsg();
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(2), newImagePath+"/goldImages/nonDICOM_2.png");		
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(3), newImagePath+"/actualImages/currentImage_1.png");
		
		String baseNonDICOMImage = newImagePath+"/goldImages/nonDICOM_1.png";
		String diffImagePath = newImagePath+"/actualImages/FlipImage.png";
		String currentImage = newImagePath+"/actualImages/currentImage_1.png";
		String basePDFImage = newImagePath+"/goldImages/nonDICOM_2.png";
		
		boolean cpStatus =  viewerPage.compareimages(baseNonDICOMImage, currentImage, diffImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying Non-DICOM Image after selecting from content selector on Vb having sync image" );
		viewerPage.assertTrue(cpStatus, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying orientation markers for non-DICOM Images", "Orientation markers are not present for Non-DICOM images");

		//Rotate Image
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(3), newImagePath+"/actualImages/afterRotate_1.png");
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(2), newImagePath+"/actualImages/afterRotate_2.png");
		String afterRotateJPG =  newImagePath+"/actualImages/afterRotate_1.png";
		String afterRotatePDF =  newImagePath+"/actualImages/afterRotate_2.png";

		boolean cpStatus1 =  viewerPage.compareimages(baseNonDICOMImage, afterRotateJPG, diffImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying Non-DICOM Image after rotating image in other VB" );
		viewerPage.assertTrue(cpStatus1, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying non-DICOM Images", "Non-DICOM images does not get rotated");

		cpStatus1 =  viewerPage.compareimages(basePDFImage, afterRotatePDF, diffImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying Non-DICOM Image after rotating image in other VB" );
		viewerPage.assertTrue(cpStatus1, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying non-DICOM Images", "Non-DICOM images does not get rotated");

		
		//Flip Image
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(3), newImagePath+"/actualImages/afterFlip_1.png");
		String afterFlipJPG =  newImagePath+"/actualImages/afterFlip_1.png";

		boolean cpStatus2 =  viewerPage.compareimages(baseNonDICOMImage, afterFlipJPG, diffImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying Non-DICOM Image after flipped image in other VB" );
		viewerPage.assertTrue(cpStatus2, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying non-DICOM Images", "Non-DICOM images does not get flipped");
		

	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US289"})
	public void test05_US289_TC2405_verifyRotationMarkerForSeriesWithSameOrientationDiffUID() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Rotate/Flip synchronization having different FrameReferenceUID and same Orientation.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+lspPatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password,2);
		orientation=new ViewerOrientation(driver);

		//Flipped the series and verify the rotation position or other series having same orientation
		orientation.flipSeries(orientation.getTopOrientationMarker(2));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Sync Image in first VB flipped horizontally","TC05_CheckPoint1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Original Image in second VB flipped horizontally","TC05_CheckPoint2");

		//Rotate the series and verify the rotation position or other series having same orientation
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Top Orientation Marker: Original Image in first VB rotated 90 degrees clockwise.","TC05_CheckPoint3");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"Verify Top Orientation Marker: Sync Image in second VB rotated 90 degrees clockwise.","TC05_CheckPoint4");

		viewerPage.performSyncONorOFF();

		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verifying the sync off","TC05_CheckPoint5");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(2),"verifying the viewbox 2 is not getting sync off with viewbox1","TC05_CheckPoint6");
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"US621"})
	public void test06_US621_TC2485_verifyAnnotationsOnFlippedAndRotateImage() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that measurement and text annotation should not flipped/rotate while drawing on flipped/rotate image");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password,2);
		orientation=new ViewerOrientation(driver);
		MeasurementWithUnit line = new MeasurementWithUnit(driver);
		
		//Flipped the series 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the flipped Image");
		viewerPage.closingConflictMsg();
		orientation.flipSeries(orientation.getTopOrientationMarker(2));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB flipped horizontally","TC06_CheckPoint1");

		//Draw measurement and text annotation
		line.selectDistanceFromQuickToolbar(1);
		line.drawLine(1, 60, 60, 150, 150);

		TextAnnotation textAnn = new TextAnnotation(driver);
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -250, -50, "TC2485_AutomationText");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying the drawn annotations on flipped Image");
//		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify flipped Image in first VB with drawn annotations","TC06_CheckPoint2");

		//Deleting all drawn annotations 
//		viewerPage.deleteAllAnnotation(1);

		viewerPage.closingConflictMsg();
		//Rotate the series and draw the annotations
		viewerPage.click(viewerPage.getViewPort(1));
		
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the rotate Image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB roatate clockwise","TC06_CheckPoint3");

		//Draw measurement and text annotation
		line.selectDistanceFromQuickToolbar(1);
		line.drawLine(1, 60, 60, 150, 150);

		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -250, -50, "TC2485_AutomationText");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying the drawn annotations on rotate Image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify rotate Image in first VB with drawn annotations","TC06_CheckPoint4");
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US621"})
	public void test07_US621_TC2496_verifyAnnotationsSizwOnZoomInAndOut() throws  InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the width of annotation should not increased or decreased with zoom out and zoom in");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password,2);
	
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		PointAnnotation point = new PointAnnotation(driver);
		SimpleLine line = new SimpleLine(driver);
		TextAnnotation textAnn = new TextAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Double click on VB1 and draw annotations" );
		viewerPage.doubleClickOnViewbox(1);
		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 150, 150);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,100,-100);
		
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -250, -50, "TC2496_AutomationText");

		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,100,0,160,0);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -100, -100,-150);	

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -100, -100,-80,-80);

		poly.selectPolylineFromQuickToolbar(1);
		poly.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify the presence of drawn GSPS objects on VB1");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB with drawn annotations","TC07_CheckPoint1");

		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));	
		viewerPage.dragAndReleaseOnViewerWithClick(1,0, 0, 0, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify the size of drawn GSPS objects on VB1 after Zoom out");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify drawn annotations in first VB with Zoom out","TC07_CheckPoint2");

		viewerPage.dragAndReleaseOnViewerWithClick(1,0, 0, 0, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify the size of drawn GSPS objects on VB1 after Zoom in");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify drawn annotations in first VB with Zoom in","TC07_CheckPoint3");
	}

	@Test(groups = { "Chrome", "IE11", "Edge","DE726","DE1367"})
	public void test08_DE726_TC2667_DE1367_TC5663_verifyOrientationOnWindowsResize() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Orientation markers and orientation operations on viewer"
				+ "<br> verification of rotation marks appears as expected after window resizing");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_patientName, username, password,2);
	    orientation=new ViewerOrientation(driver);

		//Flipped the series 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the flipped Image");
		viewerPage.closeNotification();
		orientation.flipSeries(orientation.getTopOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB flipped horizontally","TC08_CheckPoint1");
		
		// resize browser window
		viewerPage.closeWaterMarkIcon(3);
		viewerPage.closeWaterMarkIcon(4);
		viewerPage.resizeBrowserWindow(300, 400);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Resizing the browser window");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB is not reset","TC08_CheckPoint2");

		//Rotate the series and draw the annotations
		orientation.rotateSeries(orientation.getTopOrientationMarker(1), orientation.topClockwiseRotationMarker(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the rotate Image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB roatate clockwise","TC08_CheckPoint3");

		// resize browser window
		viewerPage.resizeBrowserWindow(300, 400);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Resizing the browser window");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Verify Image in first VB is not reset","TC08_CheckPoint4");
		viewerPage.maximizeWindow();
	}

	@Test(groups = { "Chrome", "IE11", "Edge" ,"DE727"})
	public void test09_DE727_TC2893_verifyOrientationpostSeriesSelectionUsingCS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify orientation markers after layout change");

		String firstSeries= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, liver9filePath);
		String fifthSeries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, liver9filePath);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9filePath+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, username, password,2);
	
		layout=new ViewerLayout(driver);
		orientation=new ViewerOrientation(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		//Flipped the series 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the orientation post layout change");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(5),"Orientation post layout change","TC09_CheckPoint1");

		contentSelector.selectSeriesFromSeriesTab(5, firstSeries);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying the orientation post layout change");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(5),"Orientation post layout change","TC09_CheckPoint2");

		//Rotate the series and draw the annotations
		viewerPage.selectScrollFromQuickToolbar(viewerPage.getViewPort(4));
		orientation.rotateSeries(orientation.getTopOrientationMarker(4), orientation.topClockwiseRotationMarker(4));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying the rotate Image");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(4),"Verify Image in first VB roatate clockwise","TC09_CheckPoint3");

		contentSelector.selectSeriesFromSeriesTab(1, fifthSeries);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying the orientation post content selector selection");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1),"Orientation post layout change","TC09_CheckPoint4");

	}
}
