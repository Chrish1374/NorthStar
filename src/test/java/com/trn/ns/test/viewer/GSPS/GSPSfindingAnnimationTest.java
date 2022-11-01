package com.trn.ns.test.viewer.GSPS;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class GSPSfindingAnnimationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;	
	private PointAnnotation point ;
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


	@Test(groups ={"Chrome","IE11","Edge","DE1059","Positive"})
	public void test01_DE1059_TC4731_verifyAnnotationAnnimation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Delete an annotation on the viewer from the user action - Single Deletion, User Annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		point = new PointAnnotation(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);
		
		
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-1
		int whichViewbox = 1;
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a point at center");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox, 40,50);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the coordinates of created point");
		
		List<WebElement> points = point.getAllPoints(1);
		int x = points.get(0).getLocation().getX();
		int y = points.get(0).getLocation().getY();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a point at Left botton corner");
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-390,150);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a line at right bottom corner");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(whichViewbox, 390, 150, 50, 50);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 2);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a line at right top corner");
		lineWithUnit.drawLine(whichViewbox, 400, -170, 50, -50);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 3);
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a line at Left top corner");
		lineWithUnit.drawLine(whichViewbox, -400, -170, -50, -50);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 4);

		

	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1059","Positive"})
	public void test02_DE1059_TC4731_verifyAnnotationAnnimation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Delete an annotation on the viewer from the user action - Single Deletion, User Annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
	
		point = new PointAnnotation(driver);
		
		// Draw point 
		int whichViewbox = 1;
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a point at center");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox, 40,50);
		
		List<WebElement> points = point.getAllPoints(1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting the coordinates of created point");
		int x = points.get(0).getLocation().getX();
		int y = points.get(0).getLocation().getY();
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a closed polyline at Left top corner");
		poly.selectPolylineFromQuickToolbar(whichViewbox);
//		int[] coordinates = new int[] {-390,-120,10, -30,40,-40};
		int[] coordinates = new int[] {-350,-150,50,-50,50,50,-50,50,-50,-50};
		poly.drawFreehandPolyLine(whichViewbox, coordinates);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a polyline at right top corner");
		coordinates = new int[] {390,-120,10, -30,40,-40};		
		poly.drawFreehandPolyLine(whichViewbox, coordinates);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 2);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a polyline at right bottom corner");
		coordinates = new int[] {390,190,10, -30,40,-40};		
		poly.drawFreehandPolyLine(whichViewbox, coordinates);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 3);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Creating a polyline at left bottom corner");
		coordinates = new int[] {-390,190,10, -30,40,-40};		
		poly.drawFreehandPolyLine(whichViewbox, coordinates);
		verifyPointIsNotAnimatedPostAnnotationCreation(1, 1, x, y, 4);
		

	}
	
	private void verifyPointIsNotAnimatedPostAnnotationCreation(int whichViewbox,int whichPoint,int x, int y, int checkpoint) {
		
		point = new PointAnnotation(driver);
		point.assertEquals(point.getAllPoints(whichViewbox).get(whichPoint-1).getLocation().getX(), x, "Checkpoint["+checkpoint+".a]", "Verifying the point's x coordinate is not changed - meaning no animation");
		point.assertEquals(point.getAllPoints(whichViewbox).get(whichPoint-1).getLocation().getY(), y, "Checkpoint["+checkpoint+".b]", "Verifying the point's y coordinate is not changed - meaning no animation");

		
	}
	

}



