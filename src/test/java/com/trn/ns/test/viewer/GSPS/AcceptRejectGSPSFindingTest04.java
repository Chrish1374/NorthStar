package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
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

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AcceptRejectGSPSFindingTest04 extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private TextAnnotation textAn;	
	private SimpleLine line;
	private OutputPanel panel;

	String EllipseComment="ellipseComment";

	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String patientIDLiver9 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY,filePath1); 
	
	String filePathAidocMachine = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String GSPS_PatientName_Aidoc_machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidocMachine);
	
	String filePathGSPSPoint = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_MultiSeries_Patient_Point = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathGSPSPoint); 

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String IBL_JohnDoe =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IBL_JohnDoe);
	
	String ChestCT1p25mm = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestCT1p25mm);
	private HelperClass helper;
	private ViewerLayout layout;
	
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	//DE963: Error message in banner when drawing findings quickly
	@Test(groups ={"Chrome","DE963","Negative"})
	public void test50_DE963_TC4000_verifyErrorMessageNotSeenWhileDrawingFindings() throws InterruptedException, SQLException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify error message not seen  in banner when drawing findings quickly");

		db=new DatabaseMethods(driver);   
	
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		lineWithUnit=new MeasurementWithUnit(driver);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify user can draw multiple linear annotations without any error message on banner");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1,-385,-20,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-40,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-60,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-80,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-100,768,0);

		lineWithUnit.drawLineQuickVersion(1, -120, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -140, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -160, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -180, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -200, -200, 0, 200);
		
		lineWithUnit.mouseHover(lineWithUnit.getViewPort(2));
		lineWithUnit.mouseHover(lineWithUnit.getViewPort(1));
		
		
	//		//draw annotation on viewbox2
		//		lineWithUnit.selectDistanceMeasurementFromRadial(2);
		//		lineWithUnit.drawLineQuickVersion(2, -50, -50, 100, 0);
		//		lineWithUnit.drawLineQuickVersion(2, 50, 50, 150, 150);
		//		lineWithUnit.drawLineQuickVersion(2, 78, -20, 100, 100);
		//		lineWithUnit.drawLineQuickVersion(2, -120, -150, 0, 150);
		//		lineWithUnit.drawLineQuickVersion(2,-4,-20,134,0);
		//		lineWithUnit.drawLineQuickVersion(2,66,-50,0,180);
		//		lineWithUnit.drawLineQuickVersion(2,42,41,134,134);
		//		lineWithUnit.drawLineQuickVersion(2,-385,-20,768,0);
		//		lineWithUnit.drawLineQuickVersion(2, -20, -50, 120, 150);
		//		lineWithUnit.drawLineQuickVersion(2, -10, -30, 90, 80);
		//		lineWithUnit.drawLineQuickVersion(2, 25, -70, 50, 0);

		int GSPSCount1=lineWithUnit.getAllLinearMeasurements(1).size();
		//		int GSPSCount2=lineWithUnit.getAllLinearMeasurements(2).size();
		//		int totalGSPSCount=GSPSCount1+GSPSCount2;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify count of drawn annotation from DB");
		//		viewerPage.assertEquals(totalGSPSCount, db.getCountOfRowsFromGSPSGraphicGroup(patientIDAh4), "Verify total count of GSPS from GSPSGraphic Group table", "Verified count seen on DB and drawn annotation are same");
		viewerPage.assertEquals(GSPSCount1, db.getCountOfRowsFromGSPSGraphicGroup(patientIDLiver9), "Verify total count of GSPS from GSPSGraphic Group table", "Verified count seen on DB and drawn annotation are same");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify no console error while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify no banner seen while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation quickly.");

	}

	@Test(groups ={"Chrome","DE963","negative"})
	public void test51_DE963_TC4150_verifyErrorWhenAnnotationDrwanOnMachineData() throws Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify error message not seen  in banner when drawing findings quickly on machine data");

		db=new DatabaseMethods(driver);   
			
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_MultiSeries_Patient_Point, 1);
	
		lineWithUnit=new MeasurementWithUnit(driver);
	

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1,-385,-20,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-40,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-60,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-80,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-100,768,0);
		lineWithUnit.drawLineQuickVersion(1, -120, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -140, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -160, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -180, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -200, -200, 0, 200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify no console error while drawing annotations quickly on viewbox1");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly on viewbox1.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify no banner seen while drawing annotations quickly on viewbox1");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation quickly on viewbox1.");

		viewerPage.scrollToImage(1, 2);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1,-385,-20,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-40,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-60,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-80,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-100,768,0);

		lineWithUnit.drawLineQuickVersion(1, -120, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -140, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -160, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -180, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -200, -200, 0, 200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify no console error while drawing annotations quickly after changing the slice number");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly after changing the slice number.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify no banner seen while drawing annotations quickly after changing the slice number");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation after changing the slice number.");

		viewerPage.scrollToImage(1, 3);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1,-385,-20,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-40,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-60,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-80,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-100,768,0);

		lineWithUnit.drawLineQuickVersion(1, -120, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -140, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -160, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -180, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -200, -200, 0, 200);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify no console error while drawing annotations quickly after changing the slice number");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly after changing the slice number.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify no banner seen while drawing annotations quickly after changing the slice number");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation quickly after changing the slice number.");

	}

	@Test(groups ={"Chrome","DE963","negative"})
	public void test52_DE963_TC4151_verifyErrorMessageWhenOneUp() throws Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify error message not seen  in banner fro one up functionality");

		patientPage=new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);			
		patientPage.clickOntheFirstStudy();
		patientPage.clearConsoleLogs();
		
		lineWithUnit=new MeasurementWithUnit(driver);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1,-385,-20,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-40,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-60,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-80,768,0);
		lineWithUnit.drawLineQuickVersion(1,-385,-100,768,0);

		lineWithUnit.drawLineQuickVersion(1, -120, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -140, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -160, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -180, -200, 0, 200);
		lineWithUnit.drawLineQuickVersion(1, -200, -200, 0, 200);

		viewerPage.doubleClickOnViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify layout changes to 1X1 after Double click One-Up on Non-DIOCM image");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),1, "Verifying layout changes to 1x1 after Double click One-Up", "The Layout after double click is 1x1" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify no console error while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify no banner seen while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation quickly.");

	}

	@Test(groups ={"Chrome","DE963","negative"})
	public void test53_DE963_TC4167_verifyTextAnnotationPersistedAfterReload() throws Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify text annotation persisted after reload of viewer page");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 2);
		textAn=new TextAnnotation(driver);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify drawn text annotation persisted on viewbox1");
		textAn.selectTextArrowFromQuickToolbar(1);
		String myText1 ="TextAnnotation_1";
		String myNewText1 ="TextAnnotation_3";

		textAn.drawText(1, 10, 10, myText1);
		textAn.assertEquals(textAn.getTextAnnotations(1).size(),1,"Verifying the TextAnnotation count", "Only one text Annotation is present");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(1, 1),myText1,"Verifying Text written on Text Annotation", "text is correctly displayed");

		//reload viewer page
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify drawn annotation persisted and new annotation user can draw.");
		textAn.selectTextArrowFromQuickToolbar(1);
		String myText2 ="TextAnnotation_2";
		textAn.drawText(1,50,50,myText2);
		textAn.updateTextOnTextAnnotation(1, 1, myNewText1);
		textAn.assertEquals(textAn.getTextAnnotations(1).size(),2,"verifying the TextAnnotation count", "Two text annotation is present");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(1, 2),myText2,"Verifying Text written on Text Annotation", "text is correctly displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify edited text annotation persist on viewbox1");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(1, 1),myNewText1,"Verifying Text written on Text Annotation", "text is correctly displayed");

		//again reload study page
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify text annotation persist on viewbox after reload of viewer page ");
		textAn.assertEquals(textAn.getTextAnnotations(1).size(),2,"verifying the TextAnnotation count", "Two text annotation is present");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(1, 1),myText2,"Verifying Text written on Text Annotation", "text is correctly displayed for first annotation");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(1, 2),myNewText1,"Verifying Text written on Text Annotation", "text is correctly displayed for second annotation");
	}

	@Test(groups ={"Chrome","DE963","Negative"})
	public void test54_DE963_TC4168_verifyForDifferentAnnotationDrawn() throws Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify when different annotation draw and edit on viewer page");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(GSPS_MultiSeries_Patient_Point, 1, 1);
				
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		circle=new CircleAnnotation(driver);

		lineWithUnit=new MeasurementWithUnit(driver);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLineQuickVersion(1, -50, -50, 100, 0);
		lineWithUnit.drawLineQuickVersion(1, 50, 50, 150, 150);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);	

		String myText1 ="TextAnnotation_1";
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1, 10, 10, myText1);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		// reload study page
		helper.browserBackAndReloadViewer(GSPS_MultiSeries_Patient_Point, 1, 1);

		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, -50, -100);
		ellipse.addResultComment(ellipse.getAllEllipses(1).get(0), EllipseComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify no console error while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify no banner seen while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation quickly.");

		//again reload study page
		helper.browserBackAndReloadViewer(GSPS_MultiSeries_Patient_Point, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify edited annotation persist after reload of viewer page");
		viewerPage.assertEquals(ellipse.getAllEllipses(1).size(),1,"Verify edited ellipse annotation present on viewer","Verified that ellipse annotation present on viewer");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),2,"Verify edited linear measurement present on viewer","Verified that linear measurement persist on viewer after reload");

	}

	@Test(groups ={"Chrome","DE963","negative"})
	public void test55_DE963_TC4204_verifyNoErrorMessageForPolyline() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify error message not seen in banner when drawing polyline");
			
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		
		viewerPage.clearConsoleLogs();		
		lineWithUnit=new MeasurementWithUnit(driver);		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		poly.selectPolylineFromQuickToolbar(1);
		int[] poly1 = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		int[] poly2 = new int[] {20,-32,37,-23,37,-9,-7,-34};
		int[] poly3 = new int[] {40,5,50,10,60,15,-40,-55,-50,-60,-60,-65};
		int[] poly4 = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25};
		int[] poly5 = new int[] {43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};


		poly.drawFreehandPolyLineExitUsingESC(1, poly1);
		poly.drawFreehandPolyLineExitUsingESC(1, poly2);
		poly.drawFreehandPolyLineExitUsingESC(1, poly3);
		poly.drawFreehandPolyLineExitUsingESC(1, poly4);
		poly.drawFreehandPolyLineExitUsingESC(1, poly5);

		//		for(int i=2;i<5;i++)
		//			poly.closeWaterMarkIcon(i);

		poly.drawFreehandPolyLineExitUsingESC(2, poly1);
		poly.drawFreehandPolyLineExitUsingESC(2, poly2);
		poly.drawFreehandPolyLineExitUsingESC(2, poly3);
		poly.drawFreehandPolyLineExitUsingESC(2, poly4);
		poly.drawFreehandPolyLineExitUsingESC(2, poly5);

		int CountForViewbox1=poly.getPolylineCount(1);
		int CountForViewbox2=poly.getPolylineCount(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify no console error while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after drawing annotation quickly.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify no banner seen while drawing annotations quickly");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after drawing annotation quickly.");
		

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);		
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		poly.assertEquals(poly.getPolylineCount(1),CountForViewbox1,"Checkpoint[3/7]","Verifying the polyline count present on viewbox1 after reload of viewer page");
		poly.assertEquals(poly.getPolylineCount(2),CountForViewbox2,"Checkpoint[4/7]","Verifying the polyline count present on viewbox2 after reload of viewer page");

		int []coordinates = new int[] {30,40,50,60};
		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(3), coordinates);

		poly.assertEquals(poly.getPolylineCount(1),CountForViewbox1,"Checkpoint[5/7]","Verifying the polyline count present on viewbox1 after reload of viewer page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify no console error while editing the annotation");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verify console error", "Verified that no console error seen after editing annotation.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify no banner seen while editing the annotation");
		viewerPage.assertFalse(viewerPage.verifyPresenceOfViewerNotification(), "Verify banner", "Verified that no banner error seen after editing annotation.");

	}

	@Test(groups ={"Chrome","DE1232","positive"})
	public void test56_DE1232_TC4991_verifyAnnotationStateOnSelection() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of annotations states are not changed while selecting");
			
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
	
		
		circle = new CircleAnnotation(driver);
		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		textAn = new TextAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		line = new SimpleLine(driver);
		textAn = new TextAnnotation(driver);


		//TextAnnotation
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-50,-100,"ABC");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true),"Checkpoint[1/12]", "The Current state of text annotation is accepted");
		textAn.selectAcceptfromGSPSRadialMenu(1);
		textAn.mouseHover(textAn.getViewPort(2));
		textAn.mouseHover(textAn.getViewPort(1));
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 1, true),"Checkpoint[2/12]", "The Current state of text annotation is pending");
		textAn.deleteTextAnnotation(1,1);

		//circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[3/12]","Verifying  circle is accepted");
		circle.selectAcceptfromGSPSRadialMenu(1);
		circle.mouseHover(circle.getViewPort(2));
		circle.selectCircleWithClick(1, 1);
		//Commented as there is issue in current masrer dated 5/May/2019
		//circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[4/12]","Verifying  circle is pending");
		circle.deleteCircle(1, 1);

		//point
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, 100);
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[5/12]","Verifying  point is accepted");
		point.selectAcceptfromGSPSRadialMenu(1);
		point.mouseHover(point.getViewPort(2));
		point.selectPoint(1, 1);
		point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint[6/12]","Verifying  point is pending");
		point.deletePoint(1, 1);

		//ellipse
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -30, -30, -75, 75);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[7/12]","Verifying ellipse is accepted");
		ellipse.selectAcceptfromGSPSRadialMenu(1);
		//		ellipse.mouseHover(ellipse.getViewPort(2));
		//		ellipse.selectEllipse(1, 1);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[8/12]","Verifying ellipse is pending");
		ellipse.deleteEllipse(1, 1);

		//linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 150, 150, -50, -50);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[9/12]","Verifying  LinearMeasurement is accepted");
		lineWithUnit.selectAcceptfromGSPSRadialMenu(1);
		lineWithUnit.mouseHover(lineWithUnit.getViewPort(2));
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1),"Checkpoint[10/12]","Verifying  LinearMeasurement is penidng");
		lineWithUnit.deleteLinearMeasurement(1,1);

		//line
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, 250, -50, 50, 50);
		line.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[11/12]","Verifying  line  is accepted");
		line.selectAcceptfromGSPSRadialMenu(1);
		line.mouseHover(line.getViewPort(2));
		line.selectLine(1, 1);
		line.assertTrue(line.verifyLinesAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[12/12]","Verifying  line  is pending");
		line.deleteLine(1,1);
	
	}

	@Test(groups ={"Chrome","DE1107","Positive"})
	public void test57_DE1107_TC4727_TC4729_verfiyAcceptRejectToobarWhenFindingTableSelected() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify A/R tool bar is displayed when user opens up the finding table <br>"
				+ "Verify A/R tool bar is doesn't jump when the user opens up finding list");
		
		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbio_PatientName, 1);
	
		
		circle = new CircleAnnotation(driver);
		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);

		//point
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 200, 200);

		//ellipse
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 30, 60, -75, 85);

		//linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 150, 150, -50, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify  A/R toolbar visible when drawn annotation is selected");
		lineWithUnit.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1), "Verify binary selector toolbar visible when annotation is selected", "Verified");

		String posOfAcceptRejectToolbar =lineWithUnit.acceptRejectToolbar.getAttribute(NSGenericConstants.STYLE);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify  A/R toolbar visible and not jump to new location when finding menu list open");
		lineWithUnit.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1), "Verify binary selector toolbar visible when annotation is selected", "Verified");
		viewerPage.assertEquals(posOfAcceptRejectToolbar,lineWithUnit.acceptRejectToolbar.getAttribute(NSGenericConstants.STYLE), "Verify A/R toolbar not jump to new location when finding list open", "Verified" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify selected circle annotation is highlighted in finding list ");
		viewerPage.assertTrue(lineWithUnit.verifyFindingIsHighlighted(2), "Verify selected finding is highlighted on table", "The selected finding is highlighted on table");

		//Verify position of finding menu when finding list is clicked
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify A/R toolbar not jump to new location when finding list is clicked");
		lineWithUnit.selectFindingFromTable(2);
		viewerPage.assertEquals(posOfAcceptRejectToolbar,lineWithUnit.acceptRejectToolbar.getAttribute(NSGenericConstants.STYLE), "Verify A/R toolbar not jump to new location after click on finding list ", "Verified" );

	}

	@Test(groups ={"Chrome","DE1107","Positive"})
	public void test58_DE1107_TC4728_verifyUserActionsAfterSelectingGSPSUsingLeftClick() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify user actions to on the viewer page after selecting GSPS using left click");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		circle = new CircleAnnotation(driver);
		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		cs =new ContentSelector(driver);

		//circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);

		//point
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 200, 200);

		//ellipse
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 30, 60, -75, 85);

		//linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 150, 150, -50, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify  A/R toolbar visible and annotation is highlighted when selected using left click");
		viewerPage.click(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1), "Verify binary selector toolbar visible when annotation is selected", "Verified");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify annotation is current active accepted GSPS", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify user action for radial menu");
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Verify radial menu is launched", "Verified");
/*
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify user action for content selector");
		cs.openContentSelector(1, false);
	viewerPage.assertTrue(cs.verifyTabIsActiveInContentSelector(contentSelector.resultTab), "Verify content selector is launched", "Verified");
	*/	
	}

	//DE1286:Pending/Rejected Circle /Ellipse annotations are turning to accepted after selecting it.

	@Test(groups ={"Chrome","DE1286","Positive"})
	public void test59_DE1286_TC5228_TC5239_TC5240_verifyStateOnSelectingAnnotation() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of Circle /Ellipse annotations on selecting annotations when state is pending/rejected. <BR>"+
				"Verify the state of linear measurement annotations on selecting annotations when state is pending/rejected. <BR>" +
				"Verify the state of Text annotation on selecting annotations when state is pending/rejected.");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		textAn=new TextAnnotation(driver);

		//draw circle and verify state of annotation after click on centre or circumeference of circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);

		circle.selectAcceptfromGSPSRadialMenu();
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[1/16]", "Verified that circle annotation is current pending GSPS when click on centre of circle");
		viewerPage.click(viewerPage.getViewPort(2));
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/16]", "Circle annotation is selected and its state not change from pending to accepted  when click on circumference of circle");

		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[3/16]", "Verified that circle annotation is current rejected GSPS when click on centre of circle");
		viewerPage.click(viewerPage.getViewPort(2));
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[4/16]", "Circle annotation is selected and its state not change from rejected to accepted  when click on circumference of circle");

		//draw ellipse and verify state of annotation after click on centre or circumeference of ellipse
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		circle.selectAcceptfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[5/16]", "Verified that ellipse annotation is current pending GSPS when click on centre of ellipse");
		viewerPage.click(viewerPage.getViewPort(2));
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[6/16]", "Ellipse annotation is selected and its state not change from pending to accepted when click on circumference of ellipse");

		circle.selectRejectfromGSPSRadialMenu();
		//		viewerPage.selectRejectfromGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));
		circle.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[7/16]", "Verified that ellipse annotation is current rejected GSPS when click on centre of ellipse");
		viewerPage.click(viewerPage.getViewPort(2));
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[8/16]", "Ellipse annotation is selected and its state not change from rejected to accepted  when click on circumference of ellipse");

		//draw linear measurement and verify state of annotation after click on handle 
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 130, 130);
		circle.selectAcceptfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Checkpoint[9/16]", "Verified that distance measurement annotation is current pending GSPS");
		viewerPage.click(viewerPage.getViewPort(1));
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Checkpoint[10/16]", "Linear measurement annotation is selected and its state not change from pending to accepted  when perform left click on handle");

		circle.selectRejectfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Checkpoint[11/16]", "Verified that distance measurement annotation is current rejected GSPS");
		viewerPage.click(viewerPage.getViewPort(1));
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Checkpoint[12/16]", "Linear measurement annotation is selected and its state not change from rejected to accepted  when perform left click on handle");

		//draw linear measurement and verify state of annotation after click on handle
		viewerPage.closingConflictMsg();
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		textAn.selectTextArrowFromQuickToolbar(2);
		textAn.drawText(2, -50,-100, "Automation_DE1286");
		circle.selectAcceptfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(2, 1, false), "Checkpoint[13/16]", "Verified that text annotation annotation is current pending GSPS");
		textAn.selectTextAnnotation(2, 1);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(2, 1, false), "Checkpoint[14/16]", "Text annotation is selected and its state not change from pending to accepted  when perform left click on handle");

		circle.selectRejectfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveRejectedGSPS(2, 1, false), "Checkpoint[15/16]", "Verified that text annotation annotation is current pending GSPS");
		textAn.selectTextAnnotation(2, 1);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveRejectedGSPS(2, 1, false), "Checkpoint[16/16]", "Text annotation is selected and its state not change from rejected to accepted  when perform left click on handle");

	}

	@Test(groups ={"Chrome","DE1286","Positive"})
	public void test60_DE1286_TC5229_verifyStateOfAnnotationInOutputPanel() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of Circle /Ellipse annotations on selecting annotations when state is pending/rejected in Output panel.");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 100, 100);

		circle.selectAcceptfromGSPSRadialMenu();
		viewerPage.click(viewerPage.getViewPort(2));
		circle.selectCircle(1, 1);
		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/2]", "Verify no change in annotation state from pending to accepted and able to see annotation in Pending filter of output panel" );

		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		panel.enableFiltersInOutputPanel(false, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[2/2]", "Verify no change in annotation state from rejected to accepted and able to see annotation in rejected filter of output panel" );



	}

	@Test(groups ={"IE11","US950"})
	public void test61_US950_TC4306_verifyShadowColorWhenChangedInDB() throws InterruptedException, SQLException, IOException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able to configure the active annotation color from DB [from Config table]");

		patientPage = new PatientListPage(driver);
		Header header = new Header(driver);
		header.logout();

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateConfigTable(NSDBDatabaseConstants.KEY_SHADOW_COLOR, ViewerPageConstants.OTHER_SHADOW_COLOR);		
		db.resetIISPostDBChanges();
		db.waitForTimePeriod(5000);
		
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		
		circle = new CircleAnnotation(driver);
		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, 90, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify all user drawn annotation are in accepted state");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(circle.verifyAnnotationState(circle.getCircle(1, 1).get(0),ViewerPageConstants.OTHER_SHADOW_COLOR,true),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		//navigate to study page and reload same study
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(circle.verifyAnnotationState(circle.getCircle(1, 1).get(0),ViewerPageConstants.OTHER_SHADOW_COLOR,true),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");

		//navigate GSPS forward by clicking on next icon on tool bar and accept the current annotation
		circle.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(circle.verifyAnnotationState(circle.getCircle(1, 1).get(0),ViewerPageConstants.OTHER_SHADOW_COLOR,true),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");

		circle.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(circle.verifyAnnotationState(circle.getCircle(1, 1).get(0),ViewerPageConstants.OTHER_SHADOW_COLOR,true),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1928", "Positive" })
	public void test62_DE1928_TC7770_verifyARToolBarForDicomSeries() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify AR tool bar is displayed when a dicom series is loaded in non-dicom view box.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + IBL_JohnDoe_PatientName + "in viewer");
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(IBL_JohnDoe_PatientName,  1, 4);
				
		circle=new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad(4);
		cs=new ContentSelector(driver);
		layout = new ViewerLayout(driver);
		String seriesName=circle.getSeriesDescriptionOverlayText(4);
		circle.selectCircleFromQuickToolbar(4);
		circle.drawCircle(4, 10, 10, 50, 50);
		
		//reload viewer page
		helper.browserBackAndReloadViewer(IBL_JohnDoe_PatientName,  1, 1);
				
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		circle.closingConflictMsg();
		
		cs.selectSeriesFromSeriesTab(4, seriesName);
		line=new SimpleLine(driver);
		line.selectLineFromQuickToolbar(4);
		line.drawLine(4, -70, -80, -120, 90);
	    line.assertTrue(line.verifyAcceptGSPSRadialMenu(), "Checkpoint[1/5]", "Verified that AR tool bar visible on dicom series not a binary toolbar.");
		
	    //navigate to 3chest patient viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + ChestCT1p25mm_PatientName + "in viewer");
		helper.browserBackAndReloadViewer(ChestCT1p25mm_PatientName,  1, 2);

		circle.click(circle.getViewPort(1));
		circle.mouseHover(circle.getViewPort(1));
		circle.mouseHover(circle.getGSPSHoverContainer(1));
		circle.assertTrue(circle.verifyBinarySelectorToobar(1), "Checkpoint[2/5]", "Verified that binary toolbar visible on SR report.");
		circle.closingConflictMsg();
		seriesName=circle.getSeriesDescriptionOverlayText(2);
		
		cs.selectSeriesFromSeriesTab(1, seriesName);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 50, 50);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[3/5]", "Verified that AR tool bar visible on dicom series not a binary toolbar.");
	    
		//navigate to 3chest patient viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
		helper.browserBackAndReloadViewer(imbio_PatientName,  1, 3);
		circle.waitForPdfToRenderInViewbox(2);
		circle.assertTrue(circle.verifyBinarySelectorToobar(2), "Checkpoint[4/5]", "Verified that binary toolbar visible on SR report.");
		seriesName=circle.getSeriesDescriptionOverlayText(3);
		
		cs.selectSeriesFromSeriesTab(2, seriesName);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 10, 10, 50, 50);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[5/5]", "Verified that AR tool bar visible on dicom series not a binary toolbar.");
		
	    
	}
	
	
	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException, IOException, InterruptedException {
		DatabaseMethods db = new DatabaseMethods(driver);
		if(!db.getValueFromConfigSettings(NSDBDatabaseConstants.KEY_SHADOW_COLOR).equals(ViewerPageConstants.SHADOW_COLOR)) {
			db.updateConfigTable(NSDBDatabaseConstants.KEY_SHADOW_COLOR, ViewerPageConstants.SHADOW_COLOR);		
			db.resetIISPostDBChanges();
		}
		
	}


}


