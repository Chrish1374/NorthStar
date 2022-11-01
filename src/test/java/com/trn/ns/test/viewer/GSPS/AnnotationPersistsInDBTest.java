package com.trn.ns.test.viewer.GSPS;
import java.awt.AWTException;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
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
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//F44_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AnnotationPersistsInDBTest extends TestBase{
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	
	private ExtentTest extentTest;

	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath2=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);

	String filePath3 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3); 

	String filePath4=Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneAge_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4); 

	String filePath5=Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
	String NorthStar_CT_Neck = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);

	String filePathAidocMachine = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String GSPS_PatientName_Aidoc_machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidocMachine);

	String myText ="Text_first";
	private String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	private PointAnnotation point;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;
	private SimpleLine line;
	private PolyLineAnnotation poly;
	private ContentSelector cs;
	private HelperClass helper;
	private ViewerLayout layout;



	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();

	}

	//TC1870 - 	Allow selection and action for user to trigger event to persist in NS DB- Draw new Annotation and Reload
	//Limitation : Not working in edge because of mouseHover function
	@Test(groups ={"firefox","Chrome","IE11","US501","Sanity","BVT"})
	public void test01_US501_TC1870_verifyAnnotationPersistsOnReload() throws   InterruptedException, SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Allow selection and action for user to trigger event to persist in NS DB- Draw new Annotation and Reload");

		loginPage = new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		helper = new HelperClass(driver);
		viewerPage=  helper.loadViewerPageUsingSearch(liver9_PatientName,  1, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		layout = new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9_PatientName+" in viewer" );

		//Step -1
		for (int vieNum =1 ; vieNum<=1 ; vieNum++){

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, ":Checkpoint TC1[1] & Checkpoint["+vieNum+"/21]", "Verifying presence of drawn GSPS objects in viewbox1 before navigating to patient list screen");
			viewerPage.scrollToImage(vieNum, 1);
			drawPointRulerTextAnnotationMeasurement(vieNum);

			//Step - 2
			viewerPage.scrollToImage(vieNum, 90);
			drawEllipseCircle(vieNum);

			//Step -3
			helper.browserBackAndReloadViewer(liver9_PatientName,  1, 1);
			
			layout.selectLayout(layout.twoByTwoLayoutIcon);
			//			viewerPage.inputZoomNumber(value, vieNum);

			//Verifying that newly drawn GSPS objects are persisted in viewbox 0
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[3] & Checkpoint["+vieNum+"/21]", "Verifying presence of drawn GSPS objects persisted in viewbox1");
			viewerPage.assertTrue(point.isPointPresent(vieNum,1),"verifying the point#1 is present on viewbox"+vieNum, "point is present");
			viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(vieNum),"verifying the linear measurenet is present on Viewbox"+vieNum, "Linear measurement is present");
			viewerPage.assertTrue(textAn.isTextAnnotationPresent(vieNum),"verifying the text annotation is present on Viewbox"+vieNum, "Text annotation is present");
			viewerPage.assertTrue(line.isLinesPresent(vieNum),"Verify the line is drawn on Viewbox"+vieNum,"Line is present");

			//Verifying with Image comparison
			//			viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(vieNum),"Verify presence of drawn GSPS objects","TC1870_CheckPoint"+vieNum+"a");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+vieNum+"/21]", "Verifying presence of drawn GSPS objects persisted in viewbox1 on slice99");
			viewerPage.scrollToImage(90,vieNum);
			viewerPage.assertTrue(ellipse.isEllipsePresent(vieNum),"verifying the ellipse is present on Viewbox"+vieNum, "Ellipse is present");
			viewerPage.assertTrue(circle.isCirclePresent(vieNum),"verifying the circle is present on Viewbox"+vieNum, "Circle is present");

			//Verifying with Image comparison
			//			viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(vieNum),"Verify presence of drawn GSPS objects","TC1870_CheckPoint"+vieNum+"b");
		}

		//Verifying the DB entry for added GSPS objects
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[21/21]", "Verifying presence of drawn GSPS objects in database");
		DatabaseMethods DB =new DatabaseMethods(driver);
		viewerPage.assertTrue(DB.verifyPresenceOfDrawnAnnotation(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify the drawn GSPS object is persist in NS DB", "GSPS object is present in DB");
	}

	//TC1871 - 	Allow selection and action for user to trigger event to persist in NS DB- Draw GSPS objects on Non-GSPS data
	//Limitation : Not working in edge getting issue with getLines function
	@Test(groups ={"firefox","Chrome","IE11","US501"})
	public void test02_US501_TC1871_verifyAnnotationPersistsOnNonGSPSData() throws  InterruptedException, SQLException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Allow selection and action for user to trigger event to persist in NS DB- Draw GSPS objects on Non-GSPS data");

		loginPage = new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(boneAge_PatientName,  1, 4);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneAge_PatientName+" in viewer" );

		//Step -1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying presence of drawn GSPS objects in viewbox2 before navigating to patient list screen");
		drawPointRulerTextAnnotationMeasurement(4);
		drawEllipseCircle(4);

		//Rejecting two annotation and verifying that the rejected annotations should be marked in red
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1] & Checkpoint[2/10]", "Verify rejected status for circle and ellipse annotation");

		//		viewerPage.performMouseRightClick(circle.getSelectedCircle(4));		
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(4).get(0));

		circle.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(4, 1).get(0));

		viewerPage.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(4, 1), "Verifying that the circle annotation is rejected", "Circle annotation in displaying as rejected");		
		viewerPage.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(4, 1), "Verifying that the point annotation is rejected", "Point annotation in displaying as rejected");		

		//Step -2
		helper.browserBackAndReloadViewer(boneAge_PatientName,  1, 1);

		//Verifying that newly drawn GSPS objects are persisted in viewbox 0
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying presence of drawn GSPS objects persisted in viewbox1");
		viewerPage.assertTrue(point.isPointPresent(1,1),"verifying the point#1", "point is present");
		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(1),"verifying the linear measurenet is present on Viewbox2", "Linear measurement is present");
		viewerPage.assertTrue(textAn.isTextAnnotationPresent(1),"verifying the text annotation is present on Viewbox2", "Text annotation is present");
		viewerPage.assertTrue(line.isLinesPresent(1),"Verify the line is drawn on Viewbox2","Line is present");
		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"verifying the ellipse is present on Viewbox2", "Ellipse is present");
		viewerPage.assertTrue(circle.isCirclePresent(1),"verifying the circle is present on Viewbox2", "Circle is present");

		//Verifying with Image comparison
		//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage2,"Verify presence of drawn GSPS objects","TC1871_CheckPoint1");

		//Verifying the DB entry for added GSPS objects
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying presence of drawn GSPS objects in database");
		DatabaseMethods DB =new DatabaseMethods(driver);
		viewerPage.assertTrue(DB.verifyPresenceOfDrawnAnnotation(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify the drawn GSPS object is persist in NS DB", "GSPS object is present in DB");

	}

	//TC1872 - 	Allow selection and action for user to trigger event to persist in NS DB - Persistence check across viewboxes
	//Limitation : Not working in edge because of mouseHover function
	@Test(groups ={"firefox","Chrome","IE11","US501"})
	public void test03_US501_TC1872_verifyAnnotationPersistsOnEmptyVB() throws   InterruptedException, SQLException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Allow selection and action for user to trigger event to persist in NS DB - Persistence check across viewboxes");

		loginPage = new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4PatientName, 1);

		
		cs = new ContentSelector(driver);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		layout = new ViewerLayout(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+" in viewer" );

		//Step -1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 3x3" );
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		//Select same series in Sixth viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting first image in sixth viewbox" );
		cs.selectSeriesFromSeriesTab(6,firstSeriesDescription);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying presence of drawn GSPS objects");
		drawPointRulerTextAnnotationMeasurement(1);

		//Step - 2
		viewerPage.scrollToImage(6, 20);
		drawEllipseCircle(6);

		//Select same series in Seventh viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting first image in seventh viewbox" );
		cs.selectResultFromSeriesTab(7,"GSPS_scan_1");

		viewerPage.assertTrue(ellipse.isEllipsePresent(7),"verifying the ellipse is present on Viewbox2", "Ellipse is present");
		viewerPage.assertTrue(circle.isCirclePresent(7),"verifying the circle is present on Viewbox2", "Circle is present");

		//Verifying with Image comparison
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(7),"Verify presence of drawn GSPS objects","TC1872_CheckPoint1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1]  & Checkpoint[3/4]", "Verifying presence of drawn GSPS objects persisted in viewbox7 on slice99");		
		viewerPage.scrollToImage(7, 15);
		//Verifying that newly drawn GSPS objects are persisted in viewbox 7
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying presence of drawn GSPS objects persisted in viewbox7");
		viewerPage.assertTrue(point.isPointPresent(7,1),"verifying the point#1", "point is present");
		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(7),"verifying the linear measurenet is present on Viewbox2", "Linear measurement is present");
		viewerPage.assertTrue(textAn.isTextAnnotationPresent(7),"verifying the text annotation is present on Viewbox2", "Text annotation is present");
		viewerPage.assertTrue(line.isLinesPresent(7),"Verify the line is drawn on Viewbox2","Line is present");

		//Verifying with Image comparison
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify presence of drawn GSPS objects","TC1872_CheckPoint2");

		//Verifying the DB entry for added GSPS objects
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying presence of drawn GSPS objects in database");
		DatabaseMethods DB =new DatabaseMethods(driver);
		viewerPage.assertTrue(DB.verifyPresenceOfDrawnAnnotation(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify the drawn GSPS object is persist in NS DB", "GSPS object is present in DB");

	}

	@Test(groups ={"firefox","Chrome","IE11"})
	public void test05_DE733_TC2744_verifyAnnotationPersistsOnBrowserBack() throws   InterruptedException, SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify draw,edit and delete functionality for all available annotations");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Login with user1" );

		loginPage = new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName_Aidoc_machine,  1, 1);
		
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		poly = new PolyLineAnnotation(driver);

		viewerPage.assertTrue(circle.getBadgeCountFromToolbar(1)>0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");

		circle.deleteAllAnnotation(1); 
		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	

		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");

//		//Draw a text annotation and navigate back to study list page and then to viewer page
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, -150);	

		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");

		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	
		
		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");


		//Draw a circle, resize and navigate back to study list page and then to viewer page
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,200, 60, 100, 200);
		circle.moveSelectedCircle(1, 100, 100);

		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");

		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	

		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");

		//Draw a ellipse, resize and navigate back to study list page and then to viewer page
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -100, 80, 30);
		ellipse.moveSelectedEllipse(1, 10, 30);

		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),1,"Checkpoint3","Verify that the Ellipse annotation is retained after going to study page and navigating back to viewer page.");

		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	
		viewerPage.waitForViewerpageToLoad();

		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2,"Checkpoint3","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),1,"Checkpoint3","Verify that the Ellipse annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");


		//Draw a line navigate back to study list page and then to viewer page
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		line.drawLine(1,150,0,150,0);

		viewerPage.assertEquals(line.getAllLines(1).size(),1,"Checkpoint6","Verify that the Linear annotation is retained after going to study page and navigating back to viewer page");

		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	
		
		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),1,"Checkpoint3","Verify that the Ellipse annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(line.getAllLines(1).size(),1,"Checkpoint6","Verify that the Linear annotation is retained after going to study page and navigating back to viewer page");

		//Draw a polyline, resize and navigate back to study list page and then to viewer page
		poly.selectPolylineFromQuickToolbar(1);
		poly.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33});

		viewerPage.assertEquals(poly.getAllPolylines(1).size(),1,"Checkpoint7","Verify that the Polyline annotation is retained after going to study page and navigating back to viewer page.");

		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	

		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),2,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),1,"Checkpoint3","Verify that the Ellipse annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(line.getAllLines(1).size(),1,"Checkpoint6","Verify that the Linear annotation is retained after going to study page and navigating back to viewer page");
		viewerPage.assertEquals(poly.getAllPolylines(1).size(),1,"Checkpoint7","Verify that the Polyline annotation is retained after going to study page and navigating back to viewer page.");

		//Deleting all annotations and navigate back to study list page and then to viewer page
		circle.deleteAllAnnotation(1); 
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),0,"Checkpoint3","Verify that the Ellipse annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),0,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(line.getAllLines(1).size(),0,"Checkpoint6","Verify that the Linear annotation is retained after going to study page and navigating back to viewer page");
		viewerPage.assertEquals(poly.getAllPolylines(1).size(),0,"Checkpoint7","Verify that the Polyline annotation is retained after going to study page and navigating back to viewer page.");

		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);	

		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getBadgeCountFromToolbar(1),0, "Checkpoint1","Verify that the annotations are retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint2","Verify that the Circle annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),0,"Checkpoint3","Verify that the Ellipse annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(point.getAllPoints(1).size(),0,"Checkpoint4","Verify that the Text annotation is retained after going to study page and navigating back to viewer page.");
		viewerPage.assertEquals(line.getAllLines(1).size(),0,"Checkpoint6","Verify that the Linear annotation is retained after going to study page and navigating back to viewer page");
		viewerPage.assertEquals(poly.getAllPolylines(1).size(),0,"Checkpoint7","Verify that the Polyline annotation is retained after going to study page and navigating back to viewer page.");


	}

	private void drawPointRulerTextAnnotationMeasurement(int whichViewbox) throws  InterruptedException{
		viewerPage = new ViewerPage(driver);

		point = new PointAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);

		point.selectPointFromQuickToolbar(whichViewbox);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,100,-100);

		lineWithUnit.selectDistanceFromQuickToolbar(whichViewbox);
		lineWithUnit.drawLine(whichViewbox, 60, 0, 100, 0);
		lineWithUnit.drawLine(whichViewbox, -80, 0, -150,0 );

		textAn.selectTextArrowFromQuickToolbar(whichViewbox);
		textAn.drawText(whichViewbox, -250, -50, myText);

		line.selectLineFromQuickToolbar(whichViewbox);
		line.drawLine(whichViewbox,50,-20,50,60);

		//Verify that annotations are drawn successfully
		viewerPage.assertTrue(point.isPointPresent(whichViewbox,1),"verifying the point is drawn on Viewbox"+whichViewbox, "point is present");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(whichViewbox).size(),2,"Verify two instances of Linear Measurement is drawn on Viewbox"+whichViewbox,"Two instances of Linear Measurement is present");
		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(whichViewbox, 1),myText,"Verifying Text written on Text Annotation on Viewbox"+whichViewbox, "text is correctly displayed");
		viewerPage.assertEquals(line.getAllLines(whichViewbox).size(),1,"Verify a single instance of line is drawn on Viewbox"+whichViewbox,"A Single instance of line is present");
	}

	private void drawEllipseCircle(int whichViewbox) throws  InterruptedException{

		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		ellipse.selectEllipseFromQuickToolbar(whichViewbox);
		ellipse.drawEllipse(whichViewbox, 150, 100, -100,-150);	

		circle.selectCircleFromQuickToolbar(whichViewbox);
		circle.drawCircle(whichViewbox, -70, -70,-80,-80);

		viewerPage.assertEquals(ellipse.getEllipses(whichViewbox).size(),1, "Verify a single Ellipse is drawn on Viewbox"+whichViewbox, "No of Ellipse on Viewbox"+whichViewbox+" : "+ellipse.getEllipses(1).size());
		viewerPage.assertEquals(circle.getAllCircles(whichViewbox).size(),1, "Verify a single circle is drawn on Viewbox"+whichViewbox, "No of Circle on Viewbox"+whichViewbox+" : "+circle.getAllCircles(1).size());
	}

	public boolean verifyGSPSObjectPresence(int whichViewbox){
		int count = 0;
		boolean status = false ;

		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		TextAnnotation textAn = new TextAnnotation(driver);
		SimpleLine line = new  SimpleLine(driver); 

		count = line.getAllLines(whichViewbox).size() + circle.getAllCircles(whichViewbox).size() + ellipse.getEllipses(whichViewbox).size() 
				+ textAn.getTextAnnotations(whichViewbox).size() + point.getAllPoints(whichViewbox).size();

		if(count>0)
			status = true ;

		return status;
	}

}
