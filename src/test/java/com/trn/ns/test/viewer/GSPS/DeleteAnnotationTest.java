package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DeleteAnnotationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	
	private ExtentTest extentTest;
	private ContentSelector cs;

	String filePath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	String filePath1=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);
	
	String filePath2=Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
	String ctNeckPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);
	
	private PointAnnotation point;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;
	private SimpleLine line;
	private HelperClass helper;
	private ViewBoxToolPanel preset;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","Sanity"}) 
	public void test01_US598_TC1974_deleteAllAnnotationByCTRLDEL() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Delete an annotation on the viewer from the user action - Multiple Deletion, User Annotation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(ah4PatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		int whichViewbox = 1;
		String myText ="test-1";
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(whichViewbox, 60, 60, 150, 150);

		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(whichViewbox, -250, -50, myText);

		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		line.drawLine(whichViewbox,20,0,60,0); 

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -200, -50, -100,-150);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, 5, 5,-80,-80);
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-2
		whichViewbox = 2;
		myText ="test-2";

		point.closingConflictMsg();
		point.selectPointFromQuickToolbar(whichViewbox);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);

		lineWithUnit.selectDistanceFromQuickToolbar((2));
		lineWithUnit.drawLine(whichViewbox, 60, 60, 150, 150);

		textAn.selectTextArrowFromQuickToolbar(whichViewbox);
		textAn.drawText(whichViewbox, -250, -50, myText);

		line.selectLineFromQuickToolbar(viewerPage.getViewPort(2));
		line.drawLine(whichViewbox,20,0,60,0); 

		ellipse.selectEllipseFromQuickToolbar(whichViewbox);
		ellipse.drawEllipse(whichViewbox, -200, -50, -100,-150);	

		circle.selectCircleFromQuickToolbar(whichViewbox);
		circle.drawCircle(whichViewbox, 5, 5,-80,-80);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify GSPS annotatios are successfully drawn on viewbox-1");
		viewerPage.assertTrue(point.isPointPresent(1,1), "Verify point is  present in first viewbox after deleting all annotations", "Point annotation is  present on first viewbox");
		viewerPage.assertTrue(circle.isCirclePresent(1), "Verify circle is  present in first viewbox after deleting all annotations", "Circle annotation is  present on first viewbox");
		viewerPage.assertTrue(ellipse.isEllipsePresent(1), "Verify ellipse is  present in first viewbox after deleting all annotations", "Ellipse annotation is  present on first viewbox");
		viewerPage.assertTrue(textAn.isTextAnnotationPresent(1), "Verify text annotation is  present in first viewbox after deleting all annotations", "Text annotation is  present on first viewbox");
		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(1), "Verify linear measurement annotation is  present in first viewbox after deleting all annotations", "Linear measurement annotation is  present on first viewbox");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify GSPS annotatios are successfully drawn on viewbox-2");
		viewerPage.assertTrue(point.isPointPresent(2,1), "Verify point is  present in second viewbox after deleting all annotations", "Point annotation is  present on second viewbox");
		viewerPage.assertTrue(circle.isCirclePresent(2), "Verify circle is  present in second viewbox after deleting all annotations", "Circle annotation is  present on second viewbox");
		viewerPage.assertTrue(ellipse.isEllipsePresent(2), "Verify ellipse is  present in second viewbox after deleting all annotations", "Ellipse annotation is  present on second viewbox");
		viewerPage.assertTrue(textAn.isTextAnnotationPresent(2), "Verify text annotation is  present in second viewbox after deleting all annotations", "Text annotation is  present on second viewbox");
		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(2), "Verify linear measurement annotation is  present in second viewbox after deleting all annotations", "Linear measurement annotation is  present on second viewbox");

		viewerPage.closingConflictMsg();
		for (int i = 1; i <=4; i++) {
			viewerPage.closeWaterMarkIcon(i);
		}
		
		//CTRL + delete on viewbox -1
		point.deleteAllAnnotation(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify NO GSPS annotatios are present on viewbox-1");
		viewerPage.assertFalse(point.isPointPresent(1,1), "Verify point is not present in first viewbox after deleting all annotations", "Point annotation is not present on first viewbox");
		viewerPage.assertFalse(circle.isCirclePresent(1), "Verify circle is not present in first viewbox after deleting all annotations", "Circle annotation is not present on first viewbox");
		viewerPage.assertFalse(ellipse.isEllipsePresent(1), "Verify ellipse is not present in first viewbox after deleting all annotations", "Ellipse annotation is not present on first viewbox");
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1), "Verify text annotation is not present in first viewbox after deleting all annotations", "Text annotation is not present on first viewbox");
		viewerPage.assertFalse(lineWithUnit.isLinearMeasurementPresent(1), "Verify linear measurement annotation is not present in first viewbox after deleting all annotations", "Linear measurement annotation is not present on first viewbox");

		point.deleteAllAnnotation(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify NO GSPS annotatios are present on viewbox-2");
		viewerPage.assertFalse(point.isPointPresent(2,1), "Verify point is not present in second viewbox after deleting all annotations", "Point annotation is not present on second viewbox");
		viewerPage.assertFalse(circle.isCirclePresent(2), "Verify circle is not present in second viewbox after deleting all annotations", "Circle annotation is not present on second viewbox");
		viewerPage.assertFalse(ellipse.isEllipsePresent(2), "Verify ellipse is not present in second viewbox after deleting all annotations", "Ellipse annotation is not present on second viewbox");
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(2), "Verify text annotation is not present in second viewbox after deleting all annotations", "Text annotation is not present on second viewbox");
		viewerPage.assertFalse(lineWithUnit.isLinearMeasurementPresent(2), "Verify linear measurement annotation is not present in second viewbox after deleting all annotations", "Linear measurement annotation is not present on second viewbox");

		// Navigate back to studylist page 
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		helper.browserBackAndReloadViewer(ah4PatientName,  1, 1);
				
		//Verify annotation after viewer reload
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify NO GSPS annotatios are present on viewbox-1 after reloading viewer");
		viewerPage.assertFalse(point.isPointPresent(1,1), "Verify point is not present in first viewbox after deleting all annotations", "Point annotation is not present on first viewbox");
		viewerPage.assertFalse(circle.isCirclePresent(1), "Verify circle is not present in first viewbox after deleting all annotations", "Circle annotation is not present on first viewbox");
		viewerPage.assertFalse(ellipse.isEllipsePresent(1), "Verify ellipse is not present in first viewbox after deleting all annotations", "Ellipse annotation is not present on first viewbox");
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1), "Verify text annotation is not present in first viewbox after deleting all annotations", "Text annotation is not present on first viewbox");
		viewerPage.assertFalse(lineWithUnit.isLinearMeasurementPresent(1), "Verify linear measurement annotation is not present in first viewbox after deleting all annotations", "Linear measurement annotation is not present on first viewbox");	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify NO GSPS annotatios are present on viewbox-2 after reloading viewer");
		viewerPage.assertFalse(point.isPointPresent(2,1), "Verify point is not present in second viewbox after deleting all annotations", "Point annotation is not present on second viewbox");
		viewerPage.assertFalse(circle.isCirclePresent(2), "Verify circle is not present in second viewbox after deleting all annotations", "Circle annotation is not present on second viewbox");
		viewerPage.assertFalse(ellipse.isEllipsePresent(2), "Verify ellipse is not present in second viewbox after deleting all annotations", "Ellipse annotation is not present on second viewbox");
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(2), "Verify text annotation is not present in second viewbox after deleting all annotations", "Text annotation is not present on second viewbox");
		viewerPage.assertFalse(lineWithUnit.isLinearMeasurementPresent(2), "Verify linear measurement annotation is not present in second viewbox after deleting all annotations", "Linear measurement annotation is not present on second viewbox");		
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","Sanity"})
	public void test02_US598_TC1975_singleDeleteAnnotation() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Delete an annotation on the viewer from the user action - Single Deletion, User Annotation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(ah4PatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-1
		int whichViewbox = 1;
		String myText ="Test1";

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);		
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-70,-70);

		lineWithUnit.selectDistanceFromQuickToolbar((1));	
		lineWithUnit.drawLine(whichViewbox, 100, 0, 200, 0);
		lineWithUnit.drawLine(whichViewbox, 100, 10, 200, 10);

		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(whichViewbox, -250, -50, myText);
		textAn.drawText(whichViewbox, -300, -50, myText);

		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		line.drawLine(whichViewbox,100,35,200,35); 
		line.drawLine(whichViewbox,100,45,200,45); 

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -50, -50, -60,-60);
		ellipse.drawEllipse(whichViewbox, 30, 40, 100,10);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, -55, -15,-30,-30);
		circle.drawCircle(whichViewbox, 75, -45,-30,-30);
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-2
		whichViewbox = 2;
		myText ="Test2";

		point.closingConflictMsg();
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-200,-100);

		lineWithUnit.selectDistanceFromQuickToolbar((2));	
		lineWithUnit.drawLine(whichViewbox, 100, 0, 200, 0);
		lineWithUnit.drawLine(whichViewbox, 100, 10, 200, 10);

		textAn.selectTextArrowFromQuickToolbar(2);
		textAn.drawText(whichViewbox, -250, -50, myText);
		textAn.drawText(whichViewbox, -300, -50, myText);

		line.selectLineFromQuickToolbar(viewerPage.getViewPort(2));
		line.drawLine(whichViewbox,100,35,200,35); 
		line.drawLine(whichViewbox,100,45,200,45); 

		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(whichViewbox, -55, -15,-30,-30);
		circle.drawCircle(whichViewbox, 75, -45,-30,-30);

		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(whichViewbox, -50, -50, -60,-60);
		ellipse.drawEllipse(whichViewbox, 30, 40, 100,10);
		//		//Press delete on viewbox-1 - updating the logic since now in order to delete we need to first select that annotation
		viewerPage.closingConflictMsg();
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		lineWithUnit.deleteSelectedMeasurement(); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify first drawn annotation is deleted from viewbox-1.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 2, "Verify none is deleted","Circle is not deleted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 2, "Verify annotation is not deleted unless selected","Both ellipses are present");
		viewerPage.assertEquals(textAn.getTextAnnotations(1).size(), 2, "Verify annotation is not deleted unless selected","Both text annotations are present");
		viewerPage.assertEquals(point.getAllPoints(1).size(), 2, "Verify annotation is not deleted unless selected","Both points are present");
		
		//Press delete on viewbox-1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify only most recently drawn annotation is deleted from viewbox-2.");
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		lineWithUnit.deleteSelectedMeasurement(); 
		lineWithUnit.deleteSelectedMeasurement();
		
		viewerPage.assertEquals(ellipse.getEllipses(2).size(), 2, "Verify none is deleted","none is deleted");
		viewerPage.assertEquals(circle.getAllCircles(2).size(), 2, "Verify annotation is not deleted unless selected","Both circles are present");
		viewerPage.assertEquals(textAn.getTextAnnotations(2).size(), 2, "Verify annotation is not deleted unless selected","Both text annotations are present");
		viewerPage.assertEquals(point.getAllPoints(2).size(), 2, "Verify annotation is not deleted unless selected","Both points are present");
		
		//Select point from second viewbox and delete it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify only selected annotation is deleted from viewbox-2 on pressing delete button");
//		point.deletePoint(2,1);
		lineWithUnit.deleteLinearMeasurement(2, 1);
		viewerPage.assertEquals(ellipse.getEllipses(2).size(), 2, "Verify one ellipse is deleted","Ellipse is deleted");
		viewerPage.assertEquals(circle.getAllCircles(2).size(), 2, "Verify two circles are present","Both circles are present");
		viewerPage.assertEquals(textAn.getTextAnnotations(2).size(), 2, "Verify two text annotations are present","Both text annotations are present");
		viewerPage.assertEquals(point.getAllPoints(2).size(), 2, "Verify only one selected point out of two is deleted so only one point is present in viewbox-2","Only one point is present");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1, "Verify only one selected point out of two is deleted so only one point is present in viewbox-2","Only one point is present");
		viewerPage.assertEquals(line.getAllLines(2).size(),2, "Verify only one selected point out of two is deleted so only one point is present in viewbox-2","Only one point is present");

		
		//Delete all annotations from viewbox-2 one by one
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify each of the user-created GSPS annotation in the active viewbox-2 are deleted one-by-one and are no longer present in the Viewer");
		for(int i=0; i<11;i++){
			lineWithUnit.deleteSelectedMeasurement();			
		}
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 2, "Verify one circle is present","One circle is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 2, "Verify ellipses are present","Both ellipses are present");
		viewerPage.assertEquals(textAn.getTextAnnotations(1).size(), 2, "Verify two text annotations are present","Both text annotations are present");
		viewerPage.assertEquals(point.getAllPoints(1).size(), 2, "Verify two points are present","Both points are present");

		viewerPage.assertTrue(ellipse.getEllipses(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(circle.getAllCircles(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(textAn.getTextAnnotations(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(point.getAllPoints(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify GSPS state is retained on viewer reload.");
		helper.browserBackAndReloadViewer(ah4PatientName,  1, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad();
		
		//Verify annotations on viewer reload
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 2, "Verify one circle is present","One circle is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 2, "Verify ellipses are present","Both ellipses are present");
		viewerPage.assertEquals(textAn.getTextAnnotations(1).size(), 2, "Verify two text annotations are present","Both text annotations are present");
		viewerPage.assertEquals(point.getAllPoints(1).size(), 2, "Verify two points are present","Both points are present");

		viewerPage.assertTrue(ellipse.getEllipses(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(circle.getAllCircles(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(textAn.getTextAnnotations(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(point.getAllPoints(2).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");

		viewerPage.assertTrue(ellipse.getEllipses(3).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(circle.getAllCircles(3).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(textAn.getTextAnnotations(3).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");
		viewerPage.assertTrue(point.getAllPoints(3).isEmpty(), "Verify NO user created GSPS annotation is present in viewbox-2","NO user created GSPS annotation is present in viewbox-2");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","Sanity"})
	public void test03_US598_TC1976_deleteMachineGeneratedAnnotation() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Delete an annotation on the viewer from the user action - Deletion with machine-generated GSPS annotations");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		int whichViewbox = 1;
		// Verify no machine generated annotations are deleted
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify no machine-generated GSPS annotations are deleted.");
		point.deleteAllAnnotation(1);
		viewerPage.assertEquals(point.getAllPoints(1).size(), 1, "Verify one machine generated point is present on viewbox-1 on NorthStar^GSPS^POINTS^MULTISERIES", "Verify one machine generated point is present on viewbox-1 on NorthStar^GSPS^POINTS^MULTISERIES");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1), "Verify one machine generated point is present on viewbox-1 on NorthStar^GSPS^POINTS^MULTISERIES", "Verify one machine generated point is present on viewbox-1 on NorthStar^GSPS^POINTS^MULTISERIES");
		
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
					
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-2
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);
		
		lineWithUnit.selectDistanceFromQuickToolbar(1);	
		lineWithUnit.drawLine(whichViewbox, 100, 0, 200, 0);
				
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.drawLine(whichViewbox,100,35,200,35); 
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, 80, 80,80,80);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -100, 150, -100,-180);
		viewerPage.click(viewerPage.getViewPort(2));
		viewerPage.click(viewerPage.getViewPort(1));
		//Delete most recently drawn annotation on viewbox-1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify only recently user-created GSPS annotation(ellipse) is deleted.");
		circle.selectCircle(1, 1);
//		lineWithUnit.deleteSelectedMeasurement();
		circle.selectDeletefromGSPSRadialMenu();

		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(), "Verify ellipse is deleted", "Verified ellipse is deleted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify one circle is present", "Verified one circle is present");
		viewerPage.assertEquals(point.getAllPoints(1).size(), 2, "Verify one point is present", "Verified one point is present");
//		viewerPage.assertEquals(textAn.getTextAnnotations(1).size(), 1, "Verify  one text annotaion is present","One text annotation is present");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify  one text annotaion is present","One text annotation is present");
		
		//CTR + DEL on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify CTRL + DEL only deletes user drawn annotations and not machine generated annotations.");
		point.deleteAllAnnotation(1);
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(), "Verify ellipse is deleted", "Verified ellipse is deleted");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(), "Verify  circle is deleted", "Verified  circle is deleted");
		viewerPage.assertEquals(point.getAllPoints(1).size(), 1, "Verify one machine generated point is present", "Verified one machine generated point is present");
		viewerPage.assertTrue(textAn.getTextAnnotations(1).isEmpty(), "Verify text annotaion is not present","Verified text annotation is not present");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify DELETE does not delete machine generated annotations.");
		lineWithUnit.deleteSelectedMeasurement();
		viewerPage.assertEquals(point.getAllPoints(1).size(), 1, "Verify one machine generated point is present", "Verified one machine generated point is present");
		
		//Navigate to study list page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify user drawn annotations are still deleted and machine-generated GSPS results are still present after navigating to viewer page");
		helper.browserBackAndReloadViewer(gspsPatientName,  1, 1);
		
		//Verify annotations after viewer reload
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(), "Verify ellipse is not present", "Verified ellipse is not present");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(), "Verify  circle is not present", "Verified  circle is not present");
		viewerPage.assertEquals(point.getAllPoints(1).size(), 1, "Verify one machine generated point is present", "Verified one machine generated point is present");
		viewerPage.assertTrue(textAn.getTextAnnotations(1).isEmpty(), "Verify text annotaion is not present","Verified text annotation is not present");
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test04_DE666_TC2327_verifyAnnotationOnDifferentSliceDeletionOnConsquentDelete() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Annotations from different slices not getting deleted on consequent delete key press when that slice is not in focus");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);

		
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 60, 130, 130);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//verify above annotation are drawn on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Distance, Circle and Ellipse annotation are drawn on slice no:15");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//navigate to next slice in series
		viewerPage.mouseWheelScrollInViewer(1, NSGenericConstants.SCROLL_DOWN, 1);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -70, -120, -90);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 50, 130, 130);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//verify above annotation are drawn on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Distance, Circle and Ellipse annotation are drawn on slice no:16");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//navigate to other slice in series and press Delete key
		viewerPage.mouseWheelScrollInViewer(1, NSGenericConstants.SCROLL_DOWN, 2);
		lineWithUnit.deleteSelectedMeasurement();

		//scroll back to same slice
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_UP, 2);

		//verify annotation drawn on other slice are not getting on pressing delete key on other viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify annotations are not deleted on slice 16 on pressing delete key on slice 18");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//press deleted key on current slice
		circle.selectCircleWithClick(1, 1);
//		lineWithUnit.deleteSelectedMeasurement();
		circle.selectDeletefromGSPSRadialMenu();

		//verify most recent annotation is getting deleted
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify most recent annotation is getting deleted on pressing delete key");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify distance annotation is deleted", "Verified ellipse annotation is not present");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(), "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");

		//scroll back to previous slice
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_UP, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify annotations are not deleted on slice 15 on pressing delete key on slice 16");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge"})
	public void test05_DE666_TC2328_verifyHighlightedAnnotationGetDeletedOnPressingDeleteKeyAcrossSlices() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that highlighted annotation are getting deleted across different slices on pressing delete key");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);

		
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 60, 130, 130);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//verify above annotation are drawn on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Distance, Circle and Ellipse annotation are drawn on slice no:15");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//navigate to next slice in series
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_DOWN, 3);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -70, -120, -90);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 60, 130, 130);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//verify above annotation are drawn on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Distance, Circle and Ellipse annotation are drawn on slice no:16");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//navigate to next GSPS object using arrow key
		circle.navigateGSPSForwardUsingKeyboard();

		//verify linear measurement is active GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify linear measurement is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Verify GSPS radial menu appear on ViewBox1", "The GSPS radial menu appear on ViewBox");

		//press delete key and verify only linear measurement annotation is deleted
		lineWithUnit.deleteSelectedMeasurement();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify that only active linear measurement annotation is deleted");
		viewerPage.assertTrue(lineWithUnit.getAllLinearMeasurements(1).isEmpty(), "Verify distance annotation is deleted on viewbox", "Verified distance annotation is deleted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//navigate to next GSPS object using arrow key
		circle.navigateGSPSBackUsingKeyboard();

		//verify linear measurement is active GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify linear measurement is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Verify GSPS radial menu appear on ViewBox1", "The GSPS radial menu appear on ViewBox");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify Circle Annotation is current active GSPS object", "A circle annotation is current active GSPS object");

		//press delete key and verify only ellipse measurement annotation is deleted
		lineWithUnit.deleteSelectedMeasurement();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify that only active Ellipse measurement annotation is deleted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is not deleted", "Verified ellipse annotation is not deleted");
		viewerPage.assertTrue(lineWithUnit.getAllLinearMeasurements(1).isEmpty(), "Verify linear measurement is drawn on viewbox", "Verified linear measurement annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","DE666","DE1401"})
	public void test06_DE666_TC2345_DE1401_TC5705_verifyMachineGeneratedAnnotationAreNotDeletedAcrossSlices() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that highlighted annotation are getting deleted across different slices on pressing delete key");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(gspsPatientName);

		
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 130, 130);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//verify above annotation are drawn on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify Distance, Circle and Ellipse annotation are drawn on slice no:15");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify distance annotation is drawn on viewbox", "Verified distance annotation is present");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(), 1, "Verify ellipse annotation is drawn on viewbox", "Verified ellipse annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//press delete key and verify  Ellipse annotation is deleted
		lineWithUnit.deleteSelectedMeasurement();
		lineWithUnit.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify that only Ellipse measurement annotation is deleted");
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(), "Verify ellipse annotation is deleted", "Verified ellipse annotation is deleted");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify linear measurement annotation is drawn on viewbox", "Verified linear measurement annotation is present");
		viewerPage.assertEquals(circle.getAllCircles(1).size(), 1, "Verify  circle annotation is drawn on viewbox", "Verified circle annotation is drawn on viewbox");

		//verify that machine generated annotation is not deleted
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify that machine generated point annotation is not deleted");
		viewerPage.assertTrue(point.isPointPresent(1,1),"verifying the point#1", "point is present");

		//press delete key and verify most circle annotation is deleted
		lineWithUnit.deleteSelectedMeasurement();
		lineWithUnit.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify that circle measurement annotation is deleted");
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(), "Verify ellipse annotation is deleted", "Verified ellipse annotation is deleted");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(), 1, "Verify linear measurement annotation is drawn on viewbox", "Verified linear measurement annotation is present");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(), "Verify  circle annotation is deleted", "Verified circle annotation is deleted");

		//verify that machine generated annotation is not deleted
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify that machine generated point annotation is not deleted");
		viewerPage.assertTrue(point.isPointPresent(1,1),"verifying the point#1", "point is present");

		//press delete key and verify most linear measurement annotation is deleted
		lineWithUnit.deleteSelectedMeasurement();
		lineWithUnit.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify that linear measurement annotation is deleted");
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(), "Verify ellipse annotation is deleted", "Verified ellipse annotation is deleted");
		viewerPage.assertTrue(lineWithUnit.getAllLinearMeasurements(1).isEmpty(), "Verify linear annotation is deleted", "Verified linear annotation is deleted");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(), "Verify  circle annotation is deleted", "Verified circle annotation is deleted");

		//verify that machine generated annotation is not deleted
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify that machine generated point annotation is not deleted");
		viewerPage.assertTrue(point.isPointPresent(1,1),"verifying the point#1", "point is present");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify that state of machine generated annotation changes to Red and User drawn annotation gets deleted after deleting all annotation using CTRL+DEL");
		point.deleteAllAnnotation(1);
		point.assertTrue(point.verifyMarkerColorOnSlider(1,ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[8/8]","Verifying the state of markers - Red");
	}

	@Test(groups ={"firefox","Chrome","IE11","Edge"}) 
	public void test07_DE681_TC2388_verifyOrientationMarkerAreNotDeletedOnDeletingAllAnnotation() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Orientation Marker are not deleted on Pressing CTRL+DEL");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);		
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		viewerPage.closingConflictMsg();
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		ViewerSliderAndFindingMenu findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		//Verify Orientation Marker are present on Viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Orientation marker are present on View box");
		ViewerOrientation orin = new ViewerOrientation(driver);
		viewerPage.assertTrue(orin.verifyTopOrientationMarkerPresence(1), "Verify Anterior orientation marker is present on viewbox", "Verified Anterior orientation marker present on viewbox");
		viewerPage.assertTrue(orin.verifyBottomOrientationMarkerPresence(1), "Verify Posterior orientation marker is present on viewbox", "Verified Posterior orientation marker present on viewbox");
		viewerPage.assertTrue(orin.verifyRightOrientationMarkerPresence(1), "Verify Right orientation marker is present on viewbox", "Verified Right orientation marker present on viewbox");
		viewerPage.assertTrue(orin.verifyLeftOrientationMarkerPresence(1), "Verify Left orientation marker is present on viewbox", "Verified Left orientation marker present on viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify all Orentation marker are present on Viewbox","TC2388_CheckPoint1");

		//press CTRL + DEL to delete all annotation
		findingMenu.deleteAllAnnotation(1);

		//verify orientation marker are not deleted on deleting all annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Orientation marker are present on View box after deleting all annotation");
		viewerPage.assertTrue(orin.verifyTopOrientationMarkerPresence(1), "Verify Anterior orientation marker is present on viewbox", "Verified Anterior orientation marker present on viewbox");
		viewerPage.assertTrue(orin.verifyBottomOrientationMarkerPresence(1), "Verify Posterior orientation marker is present on viewbox", "Verified Posterior orientation marker present on viewbox");
		viewerPage.assertTrue(orin.verifyRightOrientationMarkerPresence(1), "Verify Right orientation marker is present on viewbox", "Verified Right orientation marker present on viewbox");
		viewerPage.assertTrue(orin.verifyLeftOrientationMarkerPresence(1), "Verify Left orientation marker is present on viewbox", "Verified Left orientation marker present on viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify all Orentation marker do not disappear on deleting Annotation","TC2388_CheckPoint2");
	}

	// US793 - Clicking on the delete button should jump to the next annotation
	
	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
	public void test08_US793_TC3054_verifyDeleteWhenAnnotationIsNotSelected() throws InterruptedException  {

		int inputImageNumber=10;
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Delete button functionality when annotations not selected/highlighted.");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ah4PatientName, 1);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -50, -50);

		viewerPage.scrollToImage(1, 10);
		
		ellipse.selectEllipseFromQuickToolbar(1);		
		ellipse.drawEllipse(1, -90, -90, 100, 90);
		
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "If no annotation is selected/highlighted then delete button should not be enabled "
				+ "and user should see  the text \"please select a finding' as a notification on top of the A/R UI and tool tip should appear on bottom of the button. "
				+ "User should see the disabled icon and notification text is disappear when move the cursor to another icon.");
		
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [2/7]", "verifying the icon is disabled - class");		
		viewerPage.assertEquals(viewerPage.getText(ellipse.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [3/7]", "verifying the tooltip text - Please select a finding");
		viewerPage.assertTrue(ellipse.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [4/7]", "verifying the icon is disabled - color");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [5/7]", "verifying the tooltip presence");
		//Pressing the DEl key without selection
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		lineWithUnit.deleteSelectedMeasurement();
		
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),inputImageNumber,"Checkpoint [6/7]", "verifying that on deletion without selection nothing happens");
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"Checkpoint [7/7]","Verifying no deletion since ellipse was not selected");
		
		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
	public void test09_US793_TC3055_verifyDeletionOnNewAnnotation() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Delete button functionality on newly created annotations");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ah4PatientName, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		int currentImageNumber= viewerPage.getCurrentScrollPositionOfViewbox(1);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 100, 100);

		viewerPage.scrollToImage(1, 10);
		
		ellipse.selectEllipseFromQuickToolbar(1);		
		ellipse.drawEllipse(1, -50, -50, 70, 90);
				
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Drawn annotations are visible on the series and all annotations are in accepting state and last annotation will be selecteded but not highlighted.\n" + 
				"Delete button should be enabled after drawing a annotation because last annotation is selected");
		
		viewerPage.mouseHover(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertTrue(ellipse.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [2/7]", "verifying the icon color");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [3/7]", "verifying the tooltip presence");
		viewerPage.assertTrue(viewerPage.getAttributeValue(ellipse.gspsDelete, "class").isEmpty(), "Checkpoint [4/7]", "verifying the icon is not disabled - class");
		
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),currentImageNumber,"Checkpoint [5/7]", "Verifying on deletion it will jump to the next annotation ");
		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint [6/7]","Verifying no deletion since ellipse was not selected");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint [7/7]","");
		
		
	}
				
	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
	public void test12_US793_TC3110_verifyDeletionOfAnnotationOnSameSlice() throws InterruptedException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify delete button functionality while (animated)annotations on same slice");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ah4PatientName, 1);
		
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/18]", "Draw annotations on same slice and different place on viewbox .");
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [2/18]","Drawn Point is in accepted stated");
		
		point.drawPointAnnotationMarkerOnViewbox(1, -200, 100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),"Checkpoint [3/18]","Drawn Point is in accepted stated");
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -50, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [4/18]","Drawn Circle is in accepted stated");
			
		ellipse.selectEllipseFromQuickToolbar(1);		
		ellipse.drawEllipse(1, -90, -90, 100, 90);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [5/18]","Drawn Ellipse is in accepted stated");
		
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/18]", "Verify GSPS is not selected/highlighted and also verify delete button on A/R toolbar."
				+ "No GSPS is selected when user do panning and delete button is disabled and user should see the text \"please select a finding' as a notification on top of the A/R UI and tool tip should appear on bottom of the button.");
		
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [7/18]", "verifying the icon is disabled - class");		
		viewerPage.assertEquals(viewerPage.getText(ellipse.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [8/18]", "verifying the tooltip text - Please select a finding");
		viewerPage.assertTrue(ellipse.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [9/18]", "verifying the icon is disabled - color");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [10/18]", "verifying the tooltip presence");
		
		ellipse.navigateGSPSForwardUsingKeyboard();
		ellipse.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),"Checkpoint [11/18]","Second point is highlighted");
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint [12/18]","Deleted and verified the point");
		
		point.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [17/18]","Verifying point is in focus and selected");
		viewerPage.click(point.getGSPSDeleteButton(1));
		viewerPage.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint [18/18]","Post Deletion checking point is absent");
		
		circle.waitForCoordinatesToGetChanged(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [13/18]","Verifying Circle is in focus and selected");
		viewerPage.click(circle.getGSPSDeleteButton(1));
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(),"Checkpoint [14/18]","Post Deletion checking circle is absent");
		
		ellipse.waitForCoordinatesToGetChanged(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [15/18]","Verifying ellipse is in focus and selected");
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(),"Checkpoint [16/18]","Post Deletion checking ellipse is absent");
		
		
		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US793","Sanity"})  
	public void test13_US793_TC3112_verifyDeletionOfAnnotationOnDifferentSlice() throws InterruptedException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify delete button functionality while (animated)annotations on different slice");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ah4PatientName, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/22]", "Draw annotations on same slice and different place on viewbox .");
		
		viewerPage.scrollToImage(1, 10);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, -100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [2/22]","Drawn Point is in accepted stated");
		
		viewerPage.scrollToImage(1, 11);
		point.drawPointAnnotationMarkerOnViewbox(1, -200, 100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [3/22]","Drawn Point is in accepted stated");
		
		viewerPage.scrollToImage(1,12);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -50, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [4/22]","Drawn Circle is in accepted stated");
			
		viewerPage.scrollToImage(1, 13);
		ellipse.selectEllipseFromQuickToolbar(1);		
		ellipse.drawEllipse(1, -90, -90, 100, 90);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [5/22]","Drawn Ellipse is in accepted stated");
		
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,200);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/22]", "Verify GSPS is not selected/highlighted and also verify delete button on A/R toolbar."
				+ "No GSPS is selected when user do panning and delete button is disabled and user should see the text \"please select a finding' as a notification on top of the A/R UI and tool tip should appear on bottom of the button.");
		
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [7/22]", "verifying the icon is disabled - class");		
		viewerPage.assertEquals(viewerPage.getText(ellipse.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [8/22]", "verifying the tooltip text - Please select a finding");
		viewerPage.assertTrue(ellipse.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [9/22]", "verifying the icon is disabled - color");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [10/22]", "verifying the tooltip presence");
		
		ellipse.navigateGSPSBackUsingKeyboard();
		
		ellipse.waitForCoordinatesToGetChanged(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [11/22]","Verifying Circle is in focus and selected");
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),13,"Checkpoint [12/22]","Verifying after deletion it is mapped to another finding");
		
		ellipse.waitForCoordinatesToGetChanged(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [13/22]","Verifying ellipse is in focus and selected");
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),10,"Checkpoint [14/22]","Verifying after deletion it is mapped to another finding");
		
		ellipse.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [15/22]","Second point is highlighted");
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),11,"Checkpoint [16/22]","Verifying after deletion it is mapped to another finding");
		
		ellipse.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [17/22]","Verifying point is in focus and selected");
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),11,"Checkpoint [18/22]","Verifying after deletion it is mapped to another finding");
		
		viewerPage.scrollToImage(1, 10);
		viewerPage.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint [19/22]","Post Deletion checking point is absent");
		viewerPage.scrollToImage(1, 11);	
		viewerPage.assertTrue(point.getAllPoints(1).isEmpty(),"Checkpoint [20/22]","Deleted and verified the point");
		viewerPage.scrollToImage(1, 12);
		viewerPage.assertTrue(ellipse.getEllipses(1).isEmpty(),"Checkpoint [21/22]","Post Deletion checking ellipse is absent");
		viewerPage.scrollToImage(1, 13);
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(),"Checkpoint [22/22]","Post Deletion checking circle is absent");
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
	public void test14_US793_TC3113_verifyDeletionForAllAnnotationAfterResize() throws InterruptedException  {

		int inputImageNumber=5;
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify delete functionality after Resize annotations");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(gspsPatientName, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/16]", "Drawn annotations are visible on the series and all annotations are in accepting state and selected but not highlighted. Delete button should be enabled after drawing an annotation because last annotation is selected");
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),"Checkpoint [2/16]","Drawn Point is in accepted stated");
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -50, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [3/16]","Drawn Circle is in accepted stated");
		
		viewerPage.scrollToImage(1, inputImageNumber);
		
		ellipse.selectEllipseFromQuickToolbar(1);		
		ellipse.drawEllipse(1, -90, -90, 100, 90);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [4/16]","Drawn Ellipse is in accepted stated");
		
		//resize the ellipse		
		ellipse.moveSelectedEllipse(1,100,120);
		
		int currentImageNumber= viewerPage.getCurrentScrollPositionOfViewbox(1);
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/16]", "Delete button should only delete the selected or highlighted annotation and it will jump to the next annotation if selected annotation is user drawn.");
		viewerPage.closingConflictMsg();
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(ellipse.getGSPSDeleteButton(1));
		viewerPage.assertTrue(ellipse.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [6/16]", "verifying the icon color");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [7/16]", "verifying the tooltip presence");
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.gspsDelete, "class"),ViewerPageConstants.ICON_DISABLE, "Checkpoint [8/16]", "verifying the icon is not disabled - class");
		
		ellipse.navigateGSPSForwardUsingKeyboard();
		viewerPage.click(ellipse.getGSPSDeleteButton(1));
		
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),currentImageNumber,"Checkpoint [9/16]", "Verifying on deletion it will jump to the next annotation ");
		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint [10/16]","Verifying ellipse is deleted and Next annotation is highlighted");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint [11/16]","Verifying that machine data is highlighted");
		
		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [12/16]", "verifying the icon color - when machine annotation is highlighted");
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [14/16]", "verifying the icon is disabled - class");		
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [15/16]", "verifying the tooltip text - Machine results cannot be deleted");
		
		viewerPage.scrollToImage(1, inputImageNumber);		
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"Checkpoint [16/16]","Verifying the ellipse is deleted");
		
		
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
	public void test15_US793_TC3116_TC3117_verifyReloadAnnotationPostDeletion() throws InterruptedException, AWTException  {

				
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Reload the study after delete few annotations"
				+ "<br> Verify deleted annotations on series selected through content selector");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(gspsPatientName,  1, 1);
		cs = new ContentSelector(driver);
		String machineName=cs.getMachineName().get(0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/18]", "Drawn annotations are visible on the series and all annotations are in accepting state and selected but not highlighted. Delete button should be enabled after drawing an annotation because last annotation is selected");
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),"Checkpoint [2/18]","Drawn Point is in accepted stated");
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -50, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [3/18]","Drawn Circle is in accepted stated");
		
		ellipse.selectEllipseFromQuickToolbar(2);		
		ellipse.drawEllipse(1, -90, -90, 100, 90);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [4/18]","Drawn Ellipse is in accepted stated");
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/18]", "Delete button should only delete the selected or highlighted annotation and it will jump to the next annotation if selected annotation is user drawn.");
				
//		viewerPage.navigateGSPSForwardUsingKeyboard();
		ellipse.navigateGSPSBackUsingKeyboard();
		
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint [6/18]","Verifying that machine data is highlighted");
		
		viewerPage.click(ellipse.getGSPSDeleteButton(1));		
			
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint [7/14]","Verifying that machine data is highlighted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(),1,"Checkpoint [8/18]","Post Deletion checking ellipse is present");
		viewerPage.assertEquals(point.getAllPoints(1).size(),2,"Checkpoint [9/18]","Post Deletion checking point is present");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(),"Checkpoint [10/18]","Post Deletion checking circle is absent");
		
		helper.browserBackAndReloadViewer(gspsPatientName,  1, 2);
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 2),"Checkpoint [11/16]","Verifying that machine data is highlighted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(),1,"Checkpoint [12/18]","Post Deletion checking ellipse is absent");
		viewerPage.assertEquals(point.getAllPoints(1).size(),2,"Checkpoint [13/18]","Post Deletion checking point is present");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(),"Checkpoint [14/18]","Post Deletion checking circle is present");
		
		cs = new ContentSelector(driver);
		cs.selectResultCloneFromSeriesTabForGivenResult(1, 2, machineName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint [15/18]","Verifying that machine data is highlighted");
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 2),"Checkpoint [15/18]","Verifying that machine data is highlighted");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(),1,"Checkpoint [16/18]","Post Deletion checking ellipse is absent");
		viewerPage.assertEquals(point.getAllPoints(1).size(),2,"Checkpoint [17/18]","Post Deletion checking point is present");
		viewerPage.assertTrue(circle.getAllCircles(1).isEmpty(),"Checkpoint [18/18]","Post Deletion checking circle is present");
		
	
	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1076"}) 
	public void test16_DE1076_TC4498_verifyDeletionAfterTriggeringSelection() throws InterruptedException  {


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Selected finding is not deleted when user trigger delete action after selection.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);

		
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		point = new PointAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Draw annotations on 5 consequent slices");

		point.selectPointFromQuickToolbar(1);
		
		//drawing point on slice no. 16 (AH.4 data)
		dropPointOnNewSlice(1 , -100, -100);
		
		//drawing point on slice no. 17 (AH.4 data)
		dropPointOnNewSlice(1 , -50, -50);
		
		//drawing point on slice no. 18 (AH.4 data)
		dropPointOnNewSlice(2 , -10, -10);
		
		//drawing point on slice no. 19 (AH.4 data)
		dropPointOnNewSlice(3 , 100, 100);
		
		//drawing point on slice no. 20 (AH.4 data)
		dropPointOnNewSlice(4 , -120, -120);

		int totalFindindings = point.getBadgeCountFromToolbar(1);
		for(int i = 0; i < totalFindindings; i++)
			point.selectDeletefromGSPSRadialMenu(1);
		
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Draw annotations deleted from 5 consequent slices");
		viewerPage.assertFalse(!viewerPage.isElementPresent(point.getAcceptRejectToolBar(1)), "", "");
					
	}

	@Test(groups ={"Chrome","IE11","Edge","DE1401","Negative"}) 
	public void test17_DE1401_TC5705_verifyStateMarkerOnSliderAfterUsingCTRLDEL() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the annotation state marks in the range slider are not visible after Ctrl + Delete (Deletes all annotation) for user generated annotations.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);

		
		patientPage.clickOntheFirstStudy();

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		point.waitForViewerpageToLoad();
		
		int whichViewbox = 1;
		String myText ="test-1";
		// Draw point,ruler,line,text,ellipse and circle annotation on viewbox-1
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);

		point.scrollToImage(1, 18);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(whichViewbox, 60, 60, 150, 150);

		point.scrollToImage(1, 20);
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(whichViewbox, -250, -50, myText);

		point.scrollToImage(1, 22);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(whichViewbox,20,0,60,0); 

		point.scrollToImage(1, 24);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -200, -50, -100,-150);

		point.scrollToImage(1, 26);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, 5, 5,-80,-80);

		List<WebElement> findingMarkersOnslider = point.getFindingMarkersOnSlider(1);
		point.assertEquals(findingMarkersOnslider.size(),6,"Checkpoint[1/4]","Verifying there is multiple marker available as findings are available on different slices");
		point.assertTrue(point.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/4]","Verifying the state of markers - Green");

		//delete all annotation using ctrl+del
		point.deleteAllAnnotation(1);
		point.assertTrue(point.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[3/4]","Verifying there no annotation visible on range slider using CTRL+DEL");
		point.assertFalse(point.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[4/4]","Verified that no marker color visible on range slider after using CTRL+DEL");
		
	}

	@Test(groups ={"Chrome","IE11","Edge","DE1401","Negative"}) 
	public void test18_DE1401_TC5705_verifyStateMarkerOnSliderAfterUsingDEL() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the annotation state marks in the range slider are not visible after Ctrl + Delete (Deletes all annotation) for user generated annotations.");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ah4PatientName, 1);
		

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		line = new  SimpleLine(driver);
		point.waitForViewerpageToLoad();
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-100,-100);

		point.scrollToImage(1, 18);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 150, 0);

		point.scrollToImage(1, 22);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,20,0,60,0); 

		point.scrollToImage(1, 24);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);

		point.scrollToImage(1, 26);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
	    List <WebElement>findingMarkersOnslider = point.getFindingMarkersOnSlider(1);
		point.assertEquals(findingMarkersOnslider.size(),5,"Checkpoint[1/8]","Verifying there is one marker present on slider as results is accepted on a slice");
		point.assertTrue(point.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/8]","Verifying the state of markers - Green");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify GSPS annotatios can be deleted using delete button from keyboard");
		circle.deleteCircle(1, 1);
		circle.assertEquals(circle.getFindingMarkersOnSlider(1).size(),circle.getFindingsCountFromFindingTable(1),"Checkpoint[3/8]","Verified that actual available count from finding menu and slider range count are matched after deleting circle annotation");
		
		point.scrollToImage(1, 24);
		ellipse.deleteEllipse(1, 1);
		ellipse.assertEquals(ellipse.getFindingMarkersOnSlider(1).size(),ellipse.getFindingsCountFromFindingTable(1),"Checkpoint[4/8]","Verified that actual available count from finding menu and slider range count are matched after deleting ellipse annotation");
		
		point.scrollToImage(1, 22);
		line.deleteLine(1, 1);
		circle.click(circle.getViewPort(1));
		line.assertEquals(line.getFindingMarkersOnSlider(1).size(),line.getFindingsCountFromFindingTable(1),"Checkpoint[5/8]","Verified that actual available count from finding menu and slider range count are matched after deleting line annotation");
		
		point.scrollToImage(1,18);
		lineWithUnit.deleteLinearMeasurement(1, 1);
		circle.click(circle.getViewPort(1));
		line.assertEquals(line.getFindingMarkersOnSlider(1).size(),line.getFindingsCountFromFindingTable(1),"Checkpoint[6/8]","Verified that actual available count from finding menu and slider range count are matched after deleting distance measurment annotation");
		
		point.scrollToImage(1, 15);
		point.deletePoint(1, 1);
		circle.click(circle.getViewPort(1));
		line.assertTrue(line.getFindingMarkersOnSlider(1).isEmpty(),"Checkpoint[7/8]","Verifying there is no marker present on slider as all findings are deleted");
		line.assertTrue(line.getGSPSFindingList(1).isEmpty(),"Checkpoint[8/8]","Verifying there is no gsps object available on viewer.");
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1359", "Positive"}) 
	public void test19_DE1359_TC5672_verifyNavigationtoNextSliceAfterDeletion() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS navigation is working as expected  when user deletes the annotations across slices- Covers happy path");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ah4PatientName, 1);
		
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		point.waitForViewerpageToLoad();
		
		int whichViewbox = 1;
		lineWithUnit.selectDistanceFromQuickToolbar(whichViewbox);
		lineWithUnit.drawLine(whichViewbox, 20,0,60,0);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, 5, 5,-80,-80);
	
		// Scroll up 
		circle.scrollUpToSliceUsingKeyboard(3);
		
        //Draw ellipse and point
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -90, 140, -100,-180);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify highlighted annotation should get deleted and focus shifts to slice 15- first annotation");
		point.deleteSelectedPoint();
		point.getCurrentScrollPositionOfViewbox(1);
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), 15, "Checkpoint[1/2]", "Verify highlighted annotation should get deleted and focus shifts to slice 15- first annotation");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify on again pressing delete, highlighted annotation should get deleted and focus should move to second annotation");
		lineWithUnit.deleteSelectedMeasurement();
		lineWithUnit.assertEquals(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),true, "Checkpoint[2/2]", "Verify on again pressing delete, highlighted annotation should get deleted and focus should move to second annotation");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","DR2012", "Positive"}) 
	public void test20_DR2012_TC8249_verifyFindingsGettingDeletedFromActiveVB() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Findings are getting deleting from other view boxes upon creation of new annotation and on deletion[Happy Path]");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(ctNeckPatient, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();
		int beforeCircleCount = circle.getAllCircles(2).size();
		int beforePointCount = point.getAllPoints(2).size();
		circle.scrollToImage(1, 2);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 30, 30);
		circle.deleteSelectedCircle();
		circle.scrollToImage(1, 2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify annotations only from active viewbox are getting deleted");
		circle.assertFalse(circle.isCirclePresent(1), "Checkpoint[1/3]", "Verify circle is deleted from active viewbox");
		circle.assertEquals(circle.getAllCircles(2).size(), beforeCircleCount, "Checkpoint[2/3]", "Verify circle is not deleted which is present in non-active viewbox");
		point.assertEquals(point.getAllPoints(2).size(),beforePointCount ,"Checkpoint[3/3]", "Verify points are not deleted which are present in non-active viewbox");

	}
	
	public void dropPointOnNewSlice(int scrollNum, int pointX, int pointY) throws InterruptedException{
		point = new PointAnnotation(driver);
		point.scrollDownToSliceUsingKeyboard(1, scrollNum);
		point.drawPointAnnotationMarkerOnViewbox(1, pointX, pointY);
		point.acceptResult(1);

	}

	
}
