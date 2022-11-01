package com.trn.ns.test.viewer.GSPS;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class GSPSFindingEditingTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	
	private ExtentTest extentTest;
	
	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	private HelperClass helper;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}


	@Test(groups ={"Chrome","IE11","Edge","DE1064","Positive"})
	public void test01_DE1064_TC4594_verifyCircleEditingPostCine() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify circle can be moved/edited/deleted/rejected after playing cine");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		circle = new CircleAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loaded the patient "+liver9PatientName+" and playing cine");
		viewerPage.playOrStopCineUsingKeyboardCKey();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Creating circle" );
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -70, 40, 50);
		circle.navigateGSPSBackUsingKeyboard();
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying Circle presence", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Moving and rejecting the circle");
		circle.moveElement(circle.getAllCircles(1).get(0), 20, 20);
		circle.rejectResult(1);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying circle is active and rejected", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Creating another circle");
		circle.drawCircle(1, 30, 70, 40, 50);
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1,1),  "Verifying  first circle is rejected", "verified");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,2),  "Verifying another circle is accepted and focused", "verified");
		circle.assertEquals(circle.getAllCircles(1).size(), 2, "Verifying the circle size", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Deleting the circle" );
		circle.deleteSelectedCircle();
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying the circle size", "verified");
		
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Reloading the viewer and verifying the circle presence and state" );
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying circle size", "verified");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying circle is rejected and focused", "verified");
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1064","Positive"})
	public void test02_DE1064_TC4596_verifyEllipseEditingPostCine() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify ellipse can be moved/edited/deleted/rejected after playing cine");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		ellipse = new EllipseAnnotation(driver);		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1&2/6]", "Loading the patient, playing cine and creating the patient");
		viewerPage.playOrStopCineUsingKeyboardCKey();
		ellipse.selectEllipseFromQuickToolbar(1);		
		ellipse.drawEllipse(1, -30, -70, 40, 50);
		ellipse.navigateGSPSBackUsingKeyboard();
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "verifying the ellipse prersence", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Moving and rejecting another ellipses" );
		ellipse.moveSelectedEllipse(1, 20, 20);
		ellipse.rejectResult(1);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying ellipse is now rejected and focused", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Creating another ellipse" );
		ellipse.drawEllipse(1, 30, 70, 40, 50);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1,1),  "Verifying first ellipse is rejected", "verified");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,2),  "Verifying second ellipse is accepted and focused", "verified");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 2, "Verifying the there are two ellipses", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Deleting the ellipse" );
		ellipse.deleteSelectedEllipse();
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying the ellipses size", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying the ellipse presence and state on viewer reload");
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying ellipse presence", "verified");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying ellipse is focused and rejected", "verified");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1064","Positive"})
	public void test03_DE1064_TC4597_verifyPointEditingPostCine() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify point can be moved/edited/deleted/rejected after playing cine");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		point = new PointAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Load the patient and play cine and followed by dopping point" );
		viewerPage.playOrStopCineUsingKeyboardCKey();
		point.selectPointFromQuickToolbar(1);		
		point.drawPointAnnotationMarkerOnViewbox(1, -30, -70);				
		point.navigateGSPSBackUsingKeyboard();
		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the point presence", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Moving and rejecting the point" );
		point.movePoint(1,1, 20, 20);
		point.rejectResult(1);		
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1),  "Verifying the point is rejected and focused", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Drawing another point");
		point.drawPointAnnotationMarkerOnViewbox(1, 30, 70);
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),  "First point is rejected", "verified");
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),  "second point is accepted and focused", "verified");
		point.assertEquals(point.getAllPoints(1).size(), 2, "Verifying the total points present", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Deleting the point" );
		point.deleteSelectedPoint();
		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the total points present", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the point state post reload" );
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the total points present", "verified");
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1),  "point is rejected and focused", "verified");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1064","Positive"})
	public void test04_DE1064_TC4598_verifyCircleEditingPostLinearMeasurement() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify circle can be moved/edited/deleted/rejected after drawing linear measurement");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
				
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading patient and creating measurement" );
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Creating point" );
		circle.selectCircleFromQuickToolbar(1);		
		circle.drawCircle(1, -100, -100, -80,-80);
		
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying circle presence", "verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "verifying measurement presence", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Moving and rejecting the circle" );
		circle.moveElement(circle.getAllCircles(1).get(0), 20, 20);
		circle.rejectResult(1);		
		
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1,1),  "Verifying the circle is Rejected", "verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verifying the measurement is accepted and focused","verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Creating another circle" );
		circle.drawCircle(1,-90, 140, -100,-180);
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1,1),  "Verifying the circle is Rejected", "verified");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,2),  "Verifying the circle is accepted and focused", "verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Verifying the measurement is accepted","verified");
		circle.assertEquals(circle.getAllCircles(1).size(), 2, "verifying the circles presence", "verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Deleting the circle" );
		circle.deleteSelectedCircle();
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying circle presence", "verified");
		
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Vreifying the state on viewer reload" );
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying circle presence", "verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying measurement presence", "verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verifying the measurement is accepted and focused","verified");
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1,1),  "Verifying the circle is Rejected", "verified");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1064","Positive"})
	public void test05_DE1064_TC4599_verifyEllipseeEditingPostLinearMeasurement() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify ellipse can be moved/edited/deleted/rejected after drawing linear measurement");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		ellipse = new EllipseAnnotation(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);
			
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading patient and creating measurement and creating ellipse" );
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -30, -70, 40, 50);
		
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying ellipse presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Moving the rejecting the ellipse" );
		ellipse.moveSelectedEllipse(1, 20, 20);		
		ellipse.rejectResult(1);
		
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1,1),  "Ellipse is rejected", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Measurement is active accepted","Verified");
				
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Creating another ellipse" );
		ellipse.drawEllipse(1, 30, 70, 40, 50);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1,1),  "First ellipse is rejected", "Verified");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,2),  "Second ellipse is accepted and active", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Linear measurement is accepted","Verified");
		
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 2, "Verifying the ellipses presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Deleting the ellipse" );
		ellipse.deleteSelectedEllipse();		
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying the ellipses presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");
		
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the state of annotations on reload of viewer" );
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying the ellipses presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1,1),  "First ellipse is rejected", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Linear measurement is accepted and active","Verified");


	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1064","Positive"})
	public void test06_DE1064_TC4600_verifyPointEditingPostLinearMeasurement() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify point can be moved/edited/deleted/rejected after drawing linear measurement");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		point = new PointAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the patient, creating measurement and point");
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -30, -70);
				
		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the points presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Moving the rejecting");
		point.movePoint(1,1, 20, 20);
		point.rejectResult(1);		
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),  "Verifying the Point is rejected", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Linear measurement is accepted and active","Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Creating new point" );
		point.drawPointAnnotationMarkerOnViewbox(1, 30, 70);
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),  "Verifying the Point is rejected", "Verified");
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),  "Verifying the Point is accepted and active", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Verifying the measurement is accepted","Verified");		
		
		point.assertEquals(point.getAllPoints(1).size(), 2, "Verifying the points presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Deleting the second point" );
		point.deleteSelectedPoint();
		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the points presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),  "", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Linear measurement is accepted and active","Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the state on reload of viewer" );
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the points presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verifying the measurement presence", "Verified");
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),  "Verifying the point is rejected", "Verified");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Linear measurement is accepted and active","Verified");

	}
		
	@Test(groups ={"Chrome","IE11","Edge","DE1064"})
	public void test07_DE1064_TC4601_verifyCircleEditingPostScrolling() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify circle can be moved/edited/deleted/rejected after scrolling");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		circle = new CircleAnnotation(driver);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the patient, perform scrolling and creating circle" );
		viewerPage.dragAndReleaseOnViewer(0, -150, 0, 150);
		circle.selectCircleFromQuickToolbar(1);
		
		circle.drawCircle(1, -30, -70, 40, 50);		
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying the circle presence", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Moving and rejecting" );
		circle.moveElement(circle.getAllCircles(1).get(0), 20, 20);
		circle.rejectResult(1);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying the circle is rejected and foused", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Creating new circle" );
		circle.drawCircle(1, 30, 70, 40, 50);
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1,1),  "Verifying the circle is rejected", "Verified");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,2),  "Verifying the circle is accepted and foused", "Verified");
		circle.assertEquals(circle.getAllCircles(1).size(), 2, "Verifying the circle presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Deleting the circle" );
		circle.deleteSelectedCircle();
		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying the circle presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the state on viewer reload" );
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		circle.assertEquals(circle.getAllCircles(1).size(), 1, "Verifying the circle presence", "Verified");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying the circle is rejected and foused", "Verified");


	}
		
	@Test(groups ={"Chrome","IE11","Edge","DE1064"})
	public void test08_DE1064_TC4602_verifyEllipseEditingPostScrolling() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify ellipse can be moved/edited/deleted/rejected after scrolling");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		ellipse = new EllipseAnnotation(driver);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the patient, perform scrolling and creating ellipse" );
		viewerPage.dragAndReleaseOnViewer(0, -150, 0, 150);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -30, -70, 40, 50);
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying the ellipse presence", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Moving and rejecting" );
		ellipse.moveEllipse(1,1, 20, 20);
		ellipse.rejectResult(1);		
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying the ellipse is rejected and foused", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Creating New ellipse" );
		ellipse.drawEllipse(1, 30, 70, 40, 50);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1,1),  "Verifying the ellipse is rejected", "Verified");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,2),  "Verifying the ellipse is accepted and foused", "Verified");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 2, "Verifying the ellipse presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Deleting the ellipse" );
		ellipse.deleteSelectedEllipse();
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying the ellipse presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the state on viewer reload" );
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		ellipse.assertEquals(ellipse.getAllEllipses(1).size(), 1, "Verifying the ellipse presence", "Verified");
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1,1),  "Verifying the ellipse is rejected and foused", "Verified");


	}
		
	@Test(groups ={"Chrome","IE11","Edge","DE1064"})
	public void test09_DE1064_TC4603_verifyPointEditingPostScrolling() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify point can be moved/edited/deleted/rejected after scrolling");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName,1);
	
		point = new PointAnnotation(driver);		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the patient, perform scrolling and creating point" );
		viewerPage.dragAndReleaseOnViewer(0, -150, 0, 150);
		point.selectPointFromQuickToolbar(1);		
		point.drawPointAnnotationMarkerOnViewbox(1, -30, -70);
		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the points presence", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Moving and rejecting" );
		point.movePoint(1,1, 20, 20);		
		point.rejectResult(1);		
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1),  "Verifying the point is rejected and foused", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Creating new point" );
		point.drawPointAnnotationMarkerOnViewbox(1, 30, 70);
		point.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),  "Verifying the point is rejected", "Verified");
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),  "Verifying the point is accepted and foused", "Verified");
		point.assertEquals(point.getAllPoints(1).size(), 2, "Verifying the points presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Deleting the point" );
		point.deleteSelectedPoint();
		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the points presence", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying the state on viewer reload" );
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		point.assertEquals(point.getAllPoints(1).size(), 1, "Verifying the points presence", "Verified");
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1),  "Verifying the point is rejected and foused", "Verified");


	}

	//DE1659: [Desktop][Mobile]Deleting the last GSPS annotation on a Result series does not jump to next finding on desktop and makes the ARtoolbar disappear on both desktop and mobile

	@Test(groups ={"Chrome","IE11","Edge","DE1659","Positive"})
	public void test10_DE1659_TC7034_verifyNavigationBetweenGSPSForMultislice() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the navigation through GSPS using AR tool bar and previous and next keys in keyboard.");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select Point annotation from radial menu and draw a point annotation
		point.scrollToImage(1, 20);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse.scrollToImage(1, 3);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, -50, 40, -50);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.scrollToImage(1, 25);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Circle Annotation from radial menu and draw a circle
		circle.scrollToImage(1, 6);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 80, 80);
		
		//reload viewer page.
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		point.scrollToImage(3, 1);
		ellipse.selectEllipseWithClick(1, 1);
		point.navigateGSPSBackUsingKeyboard();
		point.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[2/16]","verify linear measurement annotation is current active GSPS object");   

		//press right arrow to move to next GSPS object
		point.navigateGSPSBackUsingKeyboard();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/16]","verify Point annotation is current active GSPS object");

		//press right arrow to move to next GSPS object
		point.navigateGSPSBackUsingKeyboard();
		point.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/16]","verify Circle annotation is current active GSPS object");	
		
		//press right arrow to move to next GSPS object
		point.navigateGSPSBackUsingKeyboard();
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		
		circle.navigateGSPSForwardUsingKeyboard();
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/16]","verify Circle annotation is current active GSPS object");	

		//press right arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/16]","verify Point annotation is current active GSPS object");

		//press right arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		point.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[2/16]","verify linear measurement annotation is current active GSPS object");   

		//press right arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		point.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		
		point.toggleOnOrOffResultUsingKeyboardGKey();
		point.assertTrue(point.getAllGSPSObjects(1).isEmpty(), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		
		point.navigateGSPSForwardUsingKeyboard();
		point.assertFalse(point.getAllGSPSObjects(1).isEmpty(), "Verify the presence of GSPS object in Viewbox1", "GSPS object is present on viewbox1");
		
		
	}
	

	}


