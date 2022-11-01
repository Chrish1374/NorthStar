package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LineAnnotation extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private SimpleLine line;
	private HelperClass helper;
	private ViewBoxToolPanel presetMenu;

	String filePath=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	String filePath1=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);
	String series1=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY,filePath1);

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//Existing Defect : DE599. Need to capture base image once Defect is fixed
	//Not working in Firefox due to Defect : DE554
	@Test(groups ={"Chrome","IE11","Edge","US542","US243","US2325","F1090","E2E"})
	public void test01_US542_TC1625_US243_TC811_US2325_TC9777_verifyOnSelectingOverlappingRulerOnlyOneShouldMove() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that when two Ruler overlaps each other then on selecting one other will not move"
				+ "<br> Verify the linear distance command functionality"
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		line = new  SimpleLine(driver);

		line.selectLineFromQuickToolbar(line.getViewPort(1));		
		line.assertTrue(line.checkCurrentSelectedIcon(ViewerPageConstants.LINE),"Checkpoint[1.1/3]","Line icon is selected on quick toolbar");
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIcon(ViewerPageConstants.LINE),"Checkpoint[1.2/3]","Verifying Line icon is selected in viewebar");
	


		line.drawLine(1,-10,10,250,10);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1.3/3]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");


		//draw a one more line from context menu
		line.drawLine(1,50,50,-20,-200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),2,"Verify a no of lines drawn on Viewbox1","Number of line drawn on Viewbox1 is :"+line.getAllLines(1).size());


		//Variable to Store current coordinate of linear measurement and measurement box 
		String beforeX1=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.Y2);

		String beforeX1Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		//Move linear Measurement 
		line.moveLine(1,2,50,100);

		String afterX1Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2Line1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		String afterX1=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=line.getLine(1,2).get(0).getAttribute(ViewerPageConstants.Y2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify user is able to move line inside a Viewbox");
		line.assertNotEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		line.assertNotEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		line.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		line.assertEquals(beforeX1Line1,afterX1Line1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		line.assertEquals(beforeY1Line1,afterY1Line1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		line.assertEquals(beforeX2Line1,afterX2Line1,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertEquals(beforeY2Line1,afterY2Line1,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);


	}	

	//Not working in Firefox due to Defect : DE554
	@Test(groups ={"Chrome","IE11","Edge","US542","US36","BVT"})
	public void test02_US542_TC1626_TC1638_US36_TC243_verifyOnOneUpSizeOfRulerRemainSame() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that on changing size of viewbox(layout change) size of ruler will not change.</br>Verify that on changing the zoom percentage size of RULER not change."
				+ "<br> As a user I want to be able to measure the linear distance on the image to determine the size of an object.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);


		line = new SimpleLine(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		//Delete all Annotation on Image
		//		viewerPage.deleteAllAnnotation(1); 

		//Select Line from context menu and draw a line
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,-30,-50,50,50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

		int beforeX1=Integer.parseInt(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1));
		int beforeY1=Integer.parseInt(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1));
		int beforeX2=Integer.parseInt(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2)); 
		int beforeY2=Integer.parseInt(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2));

		//Perform  double click  Viewbox1
		line.doubleClickOnViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify that line drawn on Viewbox1 persist on One Up");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify line drawn on Veiwbox1 persist on One up scenario","A Single line appear on Viewbox1 after One Up");

		//Perform  double click  Viewbox1 to original layout
		line.doubleClickOnViewbox(1);

		int afterX1=(int) (Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1)));
		int afterY1=(int) Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1));
		int afterX2=(int) Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2)); 
		int afterY2=(int) Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2));

		line.assertEquals((afterX2-afterX1), (beforeX2-beforeX1), "Verify that line does not change its position with respect to DICOM image after One-Up", "verified");
		line.assertEquals((afterY2-afterY1), (beforeY2-beforeY1), "Verify that line does not change its position with respect to DICOM image after One-Up", "verified");

		// get Zoom level for Canvas 0 before Zoom
		int intialZoomLevel1 = presetMenu.getZoomValue(1);

		//Select a Zoom from radial bar and perform Zoom down
		line.selectZoomFromQuickToolbar(1);
		line.dragAndReleaseOnViewer(line.getViewbox(1), 0, 0, -100,-100);
		int finalZoomLevel1 = presetMenu.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/8]","Verify Zoom Level after Mouse Up decrease in View Box1.");
		line.assertTrue(finalZoomLevel1 > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up decreases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);

		//Change a layout to 3X3
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		//Perform Zoom
		line.dragAndReleaseOnViewer(line.getViewbox(1), 0, 0, 50,50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]","Verify Zoom Level after Mouse down increases in View Box1.");
		line.assertTrue(finalZoomLevel1 > presetMenu.getZoomValue(1),"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ finalZoomLevel1 + " to "+ presetMenu.getZoomLevelValue(1));


	}

	//Not working in Firefox due to Defect : DE554
	@Test(groups ={"Chrome","IE11","Edge","US542","DE902","Positive"})
	public void test03_US542_TC1633_DE902_TC3505_verifyAnnotationHandleInvisibleDuringResizing() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that on editing  line, the end of annotation handle(circle) is should not visible"
				+ "<br> Verify that line annotation position should get save after moving");

		//Loading the patient on viewer
		
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(PatientName, 1, 1);

		line = new SimpleLine(driver);

		//Delete all Annotation on Image
		//		viewerPage.deleteAllAnnotation(1); 

		//Select Line from context menu and draw a line
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,-30,-50,150,170);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

		//Variable to Store current coordinate of linear measurement and measurement box 
		String beforeX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		//resize the selected line 
		line.assertTrue(line.verifyLineHandleDisappearOnResizing(1,2,30,30),"Verify line handle disappear on resizing","Line handle disappear on resizing");

		String afterX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify user is able to move line inside a Viewbox");
		line.assertEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		line.assertEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		line.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		helper.browserBackAndReloadViewer(PatientName, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Reloading the viewer and verifying the state is saved");
		line.assertEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","Verified");
		line.assertEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","Verified");
		line.assertNotEquals(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2),afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertNotEquals(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2),afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify user is able to move line inside a Viewbox");
		line.selectLine(1, 1);

		beforeX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		beforeY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		beforeX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		beforeY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);
		line.assertTrue(line.verifyLineHandleDisappearOnResizing(1,1,-50,-50),"Verify line handle disappear on resizing","Line handle disappear on resizing");

		afterX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		afterY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		afterX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		afterY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		line.assertNotEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","Verified");
		line.assertNotEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","Verified");
		line.assertEquals(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2),afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertEquals(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2),afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		helper.browserBackAndReloadViewer(PatientName, 1, 1);
		line.assertEquals(Math.round(Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1))),Math.round(Float.parseFloat(afterX1)),"Verify X1 coordinate of line change on move","Verified");
		line.assertEquals(Math.round(Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1))),Math.round(Float.parseFloat(afterY1)),"Verify Y1 coordinate of line change on move","Verified");
		line.assertEquals(Math.round(Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2))),Math.round(Float.parseFloat(afterX2)),"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertEquals(Math.round(Float.parseFloat(line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2))),Math.round(Float.parseFloat(afterY2)),"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

	}


	@Test(groups ={"Chrome","IE11","Edge"})
	public void test04_US542_TC1639_verifyRulerPersistWithScroll() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that ruler drawn on viewer persist on scroll.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		line = new SimpleLine(driver);


		//Select Line from context menu and draw a line
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,-30,-50,150,170);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

		int currentpos = line.getCurrentScrollPositionOfViewbox(1);
		//scroll in a Viewbox1

		line.mouseHover(line.getViewPort(2));
		line.scrollDownToSliceUsingKeyboard(1,8);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify scroll position in Viewbox1 after scroll down");
		line.assertEquals(line.getCurrentScrollPositionOfViewbox(1),(currentpos+8),"Verify scroll position in Viewbox1 after scroll down","Scroll position in viewbox1 after scroll down is: "+line.getCurrentScrollPositionOfViewbox(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify that line drawn on a particular slice do not presist on scrolling to other slice in series");
		line.assertEquals(line.getAllLines(1).size(),0,"Verify line drawn on slice 1 do not appear on other slice in series","No line appear on slice No: "+line.getCurrentScrollPositionOfViewbox(1));

		//scroll up to slice on which line is drawn
		line.scrollUpToSliceUsingKeyboard(8);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify scroll position in Viewbox1 after scroll up");
		line.assertEquals(line.getCurrentScrollPositionOfViewbox(1),currentpos,"Verify scroll position in Viewbox1 after scroll up","Scroll position in viewbox1 after scroll up is: "+line.getCurrentScrollPositionOfViewbox(1));

		//verify line drawn on slice 1 persist after scrolling back and forth
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify line drawn on a particular slice persist on scrolling back to same slice");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify line drawn on slice 1 persist on scrolling back to same slice in series","No of line on current slice is: "+line.getAllLines(1).size());

		//Select scroll from radial menu and scroll using mouse drag
		line.selectScrollFromQuickToolbar(1);
		line.dragAndReleaseOnViewer(line.getViewbox(1), 0, 0, 0,50);

		//verify slice changes on viewbox1 and no line annotation is drawn.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify scroll position in Viewbox1 after scroll");
		line.assertNotEquals(line.getCurrentScrollPositionOfViewbox(1),1,"Verify scroll position in Viewbox1 after scroll up","Scroll position in viewbox1 after scroll changes from 1 to "+line.getCurrentScrollPositionOfViewbox(1));

	}

	//Not working in Firefox due to Defect : DE554
	@Test(groups ={"Chrome","IE11","Edge","BVT"})
	public void test05_US542_TC1640_verifyRulerOnImageSelectedFromContentSelector() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that user is able to draw Ruler on image selected using content selector");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName1,1);

		line = new SimpleLine(driver);
		cs = new ContentSelector(driver);
		//Delete all Annotation on Image
		line.deleteAllAnnotation(1); 

		//Select Line from context menu and draw a line
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,-30,-50,150,170);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

		line.closingConflictMsg();
		//select same series in Viewbox2 and draw a line
		cs.selectSeriesFromSeriesTab(2, series1);
		line.drawLine(2,30,60,100,160);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that user is able to draw line on same image selected using content selector");
		line.assertEquals(line.getAllLines(2).size(),1,"Verify a number of line on Viewbox2","Total number of line on Viewbox2 is: "+line.getAllLines(2).size());

		//verify line is only drawn on viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify that line drawn is not visible on image on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a number of line on Viewbox1","Total number of line on Viewbox1 is: 1");

	}

	//Not working in Firefox due to Defect : DE554
	@Test(groups ={"Chrome","IE11","Edge"})
	public void test06_DE425_TC1667_verifyOnDrawingOppositeHandleIsNotPresent() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify while drawing a line opposite handle is not present");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		line = new SimpleLine(driver);

		//Delete all Annotation on Image
		line.deleteAllAnnotation(1); 

		//Select Line from context menu and draw a line
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.assertFalse(line.verifyLineHandleDisappearOnDrawing(1,2,10,10,100,100),"Verify opposite line handle do not appear on drawing","Opposite line handle do not appear on drawing");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that line is drawn on Viewbox1");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");
	}

	@Test(groups ={"Chrome","IE11","US986","US2329","Positive","F1090","E2E"})
	public void test07_US986_TC4197_TC4207_US2329_TC10165_verifySelectionForLine() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that line annotation should not get highlighted when user perform right click on line annotation"
		+ "<br> 	Verify that A/R should get display when user perform left click on line annotation.<br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName1,1);

		line=new SimpleLine(driver);
		cs=new ContentSelector(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify drawn annotaion is current active GSPS" );
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,50,0,150,0);
		line.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify line annotation is current active GSPS object", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify A/R when user perform left click on drawn annotation ");
		line.selectLine(1, 1);
		line.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify line annotation is current active GSPS object", "Verified");
		line.assertTrue(line.isAcceptRejectToolBarPresent(1),"Verify A/R display when user perform left click on line annotation", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		line.performMouseRightClick(line.getAllLines(1).get(0));
		line.assertTrue(line.isAcceptRejectToolBarPresent(1),"Verify A/R display when user perform left click on line annotation", "Verified");
		line.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify line annotation is not active GSPS object", "Verified");
		line.assertTrue(line.isElementPresent(line.cutOption),"Verify line annotation is not active GSPS object", "Verified");

		line.click(line.getViewPort(1));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify drawn annotaion is active GSPS after editing annotation" );
		line.moveLine(1,1,50,100);
		line.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify ellipse annotation is active GSPS object", "Verified");



	}


	@Test(groups ={"Chrome","DE423"})
	public void test06_DE423_TC1692_verifyLineResizeWhenZoomSelected() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Ruler should resize when user enable zoom from inner arc or context menu using mouse click event");	
		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);
		presetMenu=new ViewBoxToolPanel(driver);
		line = new SimpleLine(driver);

		// Step 4. Open radial menu enable zoom and apply zoom
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying zoom is applied when zoom is selected through radial menu");
		int beforeZoom = presetMenu.getZoomValue(1);
		line.selectZoomFromQuickToolbar(line.getViewPort(1));
		line.dragAndReleaseOnViewer(0, 0, 0, -20);
		line.assertTrue(beforeZoom < presetMenu.getZoomValue(1), "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+presetMenu.getZoomValue(1));

		// step 5. Open radial menu and enable ruler and 6. Draw ruler
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying ruler can be drawn");
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,80,10, 80, 100);
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");
		line.closingConflictMsg();

		// step 7.  Open radial menu enable zoom and apply zoom	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying zoom is applied when zoom is selected through context menu after drawing line");
		int beforeZoom1 = presetMenu.getZoomValue(1);
		line.selectZoomFromQuickToolbar(line.getViewPort(1));
		line.dragAndReleaseOnViewer(0, 0, 0, -20);
		line.assertTrue(beforeZoom1 < presetMenu.getZoomValue(1), "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom1+". After zoom, percentage is ="+presetMenu.getZoomValue(1));

		// step 8. Select ruler to resize and resize the ruler. 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Extending the line from (10,10,100,100) to (10,10,-100,-100)" );
		String beforeX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		//resize the selected line 
		line.assertTrue(line.verifyLineHandleDisappearOnResizing(1,2,30,30),"Verify line handle disappear on resizing","Line handle disappear on resizing");

		String afterX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify user is able to move line inside a Viewbox");
		line.assertEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		line.assertEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		line.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		// step 9.  Again apply zoom and try to draw new ruler
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify zoom is applied and line is not drawn" );
		int beforeZoom2 = presetMenu.getZoomValue(1);
		line.dragAndReleaseOnViewer(10,-100, 50,-100);
		line.assertTrue(beforeZoom2 < presetMenu.getZoomValue(1), "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom2+". After zoom, percentage is ="+presetMenu.getZoomLevelValue(1));
		line.dragAndReleaseOnViewer(0, 0, 100,100);
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

	}


	@Test(groups ={"Chrome","DE423"})
	public void test07_DE423_TC1693_verifyLineResizeWhenWWWLSelected() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Ruler should resize when user enable WWWL from inner arc or context menu using mouse click event");	
		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);


		line = new SimpleLine(driver);
		// Step 4. Open radial menu enable WWWL and apply WWWL
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying WWWL is applied when WWWL is selected through context menu");

		//selecting and performing window leveling
		String viewbox1_width = line.getWindowWidthValueOverlayText(1);
		String viewbox1_windowCenter = line.getWindowCenterValueOverlayText(1);
		line.selectWindowLevelFromQuickToolbar(1);
		line.dragAndReleaseOnViewer(0, 0, 10, -20);
		line.assertNotEquals(line.getWindowWidthValueOverlayText(1)  , viewbox1_width, "verifying the WW/WL in viewbox1", "verified");		
		line.assertNotEquals(line.getWindowCenterValueOverlayText(1)  , viewbox1_windowCenter, "verifying the WW/WL(center) in viewbox1", "verified");

		// step 5. Open radial menu and enable ruler and 6. Draw ruler
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying ruler can be drawn");
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.dragAndReleaseOnViewer(50,50, 100, 100);
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");
		line.closingConflictMsg();

		// step 7.  Open radial menu enable WWWL and apply WWWL	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying WWWL is applied when WWWL is selected through context menu after drawing line");
		String viewbox1_width1 = line.getWindowWidthValueOverlayText(1);
		String viewbox1_windowCenter1 = line.getWindowCenterValueOverlayText(1);
		line.selectWindowLevelFromQuickToolbar(line.getViewPort(1));
		line.dragAndReleaseOnViewer(0, 0, 50, -80);
		line.assertNotEquals(line.getWindowWidthValueOverlayText(1)  , viewbox1_width1, "verifying the WW/WL in viewbox1", "verified");		
		line.assertNotEquals(line.getWindowCenterValueOverlayText(1)  , viewbox1_windowCenter1, "verifying the WW/WL(center) in viewbox1", "verified");

		// step 8. Select ruler to resize and resize the ruler. 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Extending the line from (10,10,100,100) to (10,10,-100,-100)" );
		String beforeX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		//resize the selected line 
		line.assertTrue(line.verifyLineHandleDisappearOnResizing(1,2,30,30),"Verify line handle disappear on resizing","Line handle disappear on resizing");

		String afterX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify user is able to move line inside a Viewbox");
		line.assertEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		line.assertEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		line.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		// step 9.  Again apply WWWL and try to draw new ruler
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify WWWL is applied and line is not drawn" );
		String viewbox1_width2 = line.getWindowWidthValueOverlayText(1);
		String viewbox1_windowCenter2 = line.getWindowCenterValueOverlayText(1);
		line.dragAndReleaseOnViewer(10,-100, 50,-100);
		line.assertNotEquals(line.getWindowWidthValueOverlayText(1)  , viewbox1_width2, "verifying the WW/WL in viewbox1", "verified");		
		line.assertNotEquals(line.getWindowCenterValueOverlayText(1)  , viewbox1_windowCenter2, "verifying the WW/WL(center) in viewbox1", "verified");
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

	}

	//Test data=Ah.4
	@Test(groups ={"Chrome","DE423"})
	public void test08_DE423_TC1694_verifyLineResizeWhenPANSelected() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Ruler should not be drawn when any dicom operation (zoom, pan..etc) is already enabled");	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);
		line= new SimpleLine(driver);
		line.doubleClickOnViewbox(1);

		// Step 4. Open radial menu enable PAN and apply PAN
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying PAN is applied when PAN is selected through context menu");	
		line.selectPanFromQuickToolbar(line.getViewPort(1));
		line.dragAndReleaseOnViewer(0, 0, 100,0);
	
		// step 5. Open radial menu and enable ruler and 6. Draw ruler
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying ruler can be drawn");
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1, 50, 50, 100, 100);
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");
		line.closingConflictMsg();

		// step 7.  Open radial menu enable PAN and apply PAN	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Resizing the line" );
		String beforeX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		//resize the selected line 
		line.assertTrue(line.verifyLineHandleDisappearOnResizing(1,2,30,30),"Verify line handle disappear on resizing","Line handle disappear on resizing");

		String afterX1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=line.getLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify user is able to move line inside a Viewbox");
		line.assertEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		line.assertEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		line.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		line.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		// step 8. Select ruler to resize and resize the ruler. 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying PAN is applied when PAN is selected through context menu after drawing line");
		line.selectPanFromQuickToolbar(line.getViewPort(1));
		line.dragAndReleaseOnViewer(100, 0, 150, 0);
	
		// step 9.  Again apply PAN and try to draw new ruler
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify PAN is applied and ruler is NOT drawn" );
		line.dragAndReleaseOnViewer(150, 0, 200, 0);		
		line.assertEquals(line.getAllLines(1).size(),1,"Verify a single instance of line is drawn on Viewbox1","A Single instance of line is present on Viewbox1");

	}

}
