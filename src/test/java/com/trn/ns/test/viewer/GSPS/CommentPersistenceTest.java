package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;

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

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CommentPersistenceTest extends TestBase  {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;

	private TextAnnotation textAnn;
	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;

	private PolyLineAnnotation polyLine;
	private ContentSelector cs;

	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String liver9FilePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);
	String longComment = "This is a sentence which is long ";

	private final String pointComment="Point comment";
	private final String editPointComment="Edited Point comment";
	private final String rulerComment="Ruler comment";
	private final String editRulerComment="Edited Ruler comment";
	private final String editLineComment="Edit Line comment";
	private final String ellipseComment="Ellipse comment";
	private final String editEllipseComment="Edit Ellipse comment";
	private final String poyLineComment="test";
	private final String editPoyLineComment="Edit Poly Line";
	private final String circleComment="Circle comment";
	private final String editCicleComment="Edited Circle comment";
	private final String textComment="Text annotation";


	private String firstResultDescriptionAH4 ;
	private OutputPanel panel;
	private HelperClass helper;
	private ViewerLayout layout;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	@Test(groups ={"Chrome","US795"})
	public void test10_00_US795_TC3154_TC3156_verifyPersistenceTextComment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Persisting comments for all annotations"
				+ "<br> State of text comment- All annotations");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		lineWithUnit.addResultComment(lineWithUnit.getLinearMeasurements(1, 1).get(0),rulerComment);
		lineWithUnit.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName,  1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1.0/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		lineWithUnit.assertEquals(lineWithUnit.getText(lineWithUnit.resultComment.get(0)), rulerComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+rulerComment);
		lineWithUnit.assertEquals(lineWithUnit.getAttributeValue(lineWithUnit.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify result comment is accepted by default", "The result comment is accepted by default");


		//reject  the finding and verify color of each finding
		//viewerPage.click(viewerPage.getViewPort(1));
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1, 1).get(0));
		lineWithUnit.assertEquals(lineWithUnit.getAttributeValue(lineWithUnit.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "TC3156: Verify result comment is rejected by default", "The result comment is rejected by default");

		//pending  the finding and verify color of each finding
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1, 1).get(0));
		lineWithUnit.assertEquals(lineWithUnit.getAttributeValue(lineWithUnit.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "TC3156: Verify result comment is Pending by default", "The result comment is pending by default");	

		//edit the above drawn text annotation
		lineWithUnit.editResultComment(lineWithUnit.resultComment.get(0), editLineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2.0/3]", "Verify updated text appear on result comment");
		lineWithUnit.assertEquals(lineWithUnit.getText(lineWithUnit.resultComment.get(0)), editLineComment, "Verify updated text appear on result comment", "The entered result comment is "+editLineComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3.0/3]", "Verify all the result comment are in rejected state");
		lineWithUnit.assertEquals(lineWithUnit.getAttributeValue(lineWithUnit.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");


		//lineWithUnit.deleteResultComment(lineWithUnit.resultComment.get(0));
		lineWithUnit.deleteAllAnnotation(1);

	}

	@Test(groups ={"Chrome","US795"})
	public void test10_01_US795_TC3154_TC3156_verifyPersistenceTextComment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Persisting comments for all annotations"
				+ "<br> State of text comment- All annotations");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		circle = new CircleAnnotation(driver);

		//draw circle annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -10, -10, 10, 10);

		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);
		circle.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName,  1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1.0/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		circle.assertEquals(circle.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);
		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify result comment is accepted by default", "The result comment is accepted by default");


		//reject  the finding and verify color of each finding
		//circle.click(circle.getViewPort(1));
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "TC3156: Verify result comment is rejected by default", "The result comment is rejected by default");

		//pending  the finding and verify color of each finding
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "TC3156: Verify result comment is Pending by default", "The result comment is pending by default");	

		//edit the above drawn text annotation
		circle.editResultComment(circle.resultComment.get(0), editCicleComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2.0/3]", "Verify updated text appear on result comment");
		circle.assertEquals(circle.getText(circle.resultComment.get(0)), editCicleComment, "Verify updated text appear on result comment", "The entered result comment is "+editCicleComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3.0/3]", "Verify all the result comment are in rejected state");
		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");


		//circle.deleteResultComment(circle.resultComment.get(0));
		circle.deleteAllAnnotation(1);

	}

	@Test(groups ={"Chrome","US795"})
	public void test10_02_US795_TC3154_TC3156_verifyPersistenceTextComment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Persisting comments for all annotations"
				+ "<br> State of text comment- All annotations");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		point = new PointAnnotation(driver);
		//select Point annotation from radial menu and draw a point annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -50);

		point.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment);
		point.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName,  1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1.0/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		point.assertEquals(point.getText(point.resultComment.get(0)), pointComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+pointComment);
		point.assertEquals(point.getAttributeValue(point.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify result comment is accepted by default", "The result comment is accepted by default");


		//reject  the finding and verify color of each finding
		//point.click(point.getViewPort(1));
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.assertEquals(point.getAttributeValue(point.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "TC3156: Verify result comment is rejected by default", "The result comment is rejected by default");

		//pending  the finding and verify color of each finding
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.assertEquals(point.getAttributeValue(point.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "TC3156: Verify result comment is Pending by default", "The result comment is pending by default");	

		//edit the above drawn text annotation
		point.editResultComment(point.resultComment.get(0), editPointComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2.0/3]", "Verify updated text appear on result comment");
		point.assertEquals(point.getText(point.resultComment.get(0)), editPointComment, "Verify updated text appear on result comment", "The entered result comment is "+editPointComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3.0/3]", "Verify all the result comment are in rejected state");
		point.assertEquals(point.getAttributeValue(point.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");


		//point.deleteResultComment(point.resultComment.get(0));
		point.deleteAllAnnotation(1);
	}

	@Test(groups ={"Chrome","US795"})
	public void test10_03_US795_TC3154_TC3156_verifyPersistenceTextComment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Persisting comments for all annotations"
				+ "<br> State of text comment- All annotations");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		ellipse = new EllipseAnnotation(driver);


		//select ellipse from radial menu and draw a ellipse 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);
		ellipse.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName,  1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1.0/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		ellipse.assertEquals(ellipse.getText(ellipse.resultComment.get(0)), ellipseComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+ellipseComment);
		ellipse.assertEquals(ellipse.getAttributeValue(ellipse.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify result comment is accepted by default", "The result comment is accepted by default");


		//reject  the finding and verify color of each finding
		//ellipse.click(ellipse.getViewPort(1));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.assertEquals(ellipse.getAttributeValue(ellipse.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "TC3156: Verify result comment is rejected by default", "The result comment is rejected by default");

		//pending  the finding and verify color of each finding
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.assertEquals(ellipse.getAttributeValue(ellipse.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "TC3156: Verify result comment is Pending by default", "The result comment is pending by default");	

		//edit the above drawn text annotation
		ellipse.editResultComment(ellipse.resultComment.get(0), editEllipseComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2.0/3]", "Verify updated text appear on result comment");
		ellipse.assertEquals(ellipse.getText(ellipse.resultComment.get(0)), editEllipseComment, "Verify updated text appear on result comment", "The entered result comment is "+editEllipseComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3.0/3]", "Verify all the result comment are in rejected state");
		ellipse.assertEquals(ellipse.getAttributeValue(ellipse.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");


		//ellipse.deleteResultComment(ellipse.resultComment.get(0));
		ellipse.deleteAllAnnotation(1);
	}

	@Test(groups ={"Chrome","US795"})
	public void test10_04_US795_TC3154_TC3156_verifyPersistenceTextComment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Persisting comments for all annotations"
				+ "<br> State of text comment- All annotations");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		polyLine = new PolyLineAnnotation(driver);


		//navigate to next slice and draw an annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		polyLine.selectPolylineFromQuickToolbar(1);		
		polyLine.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		polyLine.addResultComment(polyLine.getLinesOfPolyLine(1,1).get(0),poyLineComment);
		polyLine.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName,  1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1.0/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(0)), poyLineComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+poyLineComment);
		polyLine.assertEquals(polyLine.getAttributeValue(polyLine.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify result comment is accepted by default", "The result comment is accepted by default");


		//reject  the finding and verify color of each finding
		//polyLine.click(polyLine.getViewPort(1));
		polyLine.selectRejectfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(1,1).get(0));
		polyLine.assertEquals(polyLine.getAttributeValue(polyLine.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "TC3156: Verify result comment is rejected by default", "The result comment is rejected by default");

		//pending  the finding and verify color of each finding
		polyLine.selectRejectfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(1,1).get(0));
		polyLine.assertEquals(polyLine.getAttributeValue(polyLine.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "TC3156: Verify result comment is Pending by default", "The result comment is pending by default");	

		//edit the above drawn text annotation
		polyLine.editResultComment(polyLine.resultComment.get(0), editPoyLineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2.0/3]", "Verify updated text appear on result comment");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(0)), editPoyLineComment, "Verify updated text appear on result comment", "The entered result comment is "+editPoyLineComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3.0/3]", "Verify all the result comment are in rejected state");
		polyLine.assertEquals(polyLine.getAttributeValue(polyLine.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "TC3156: Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");


		//polyLine.deleteResultComment(polyLine.resultComment.get(0));
		polyLine.deleteAllAnnotation(1);
	}

	@Test(groups ={"Chrome","US795"})
	public void test11_US795_TC3160_verifyPersistenceTextCommentOnEmptyAndNonEmptyViewbox() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Persisting comments for all annotations");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);

		cs = new ContentSelector(driver);

		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		polyLine = new PolyLineAnnotation(driver);
		circle = new CircleAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/13]", "Draw the following annotations{Circle, Ellipse, Point, Line, <b>Distance Measurement<\b>, Polyline}. ");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-150,-150,200,0);
		lineWithUnit.drawLine(1,-200,-200,0,100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/13]", "While the annotation is selected one at a time:\r\n" + 
				"launch GSPS toolbar and click on 'Add text' button\r\n" + 
				"Click within the text box and add text. Press 'Enter'");
		lineWithUnit.addResultComment(lineWithUnit.getLinearMeasurements(1, 2).get(0),rulerComment);

		//select Point annotation from radial menu and draw a point annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/13]", "Draw the following annotations{Circle, Ellipse, <b>Point<\b>, Line, Distance Measurement, Polyline}. ");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -90, 0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/13]", "While the annotation is selected one at a time:\r\n" + 
				"launch GSPS toolbar and click on 'Add text' button\r\n" + 
				"Click within the text box and add text. Press 'Enter'");
		point.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment);

		//select ellipse from radial menu and draw a ellipse 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/13]", "Draw the following annotations{Circle, <b>Ellipse<\b>, Point, Line, Distance Measurement, Polyline}. ");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -200, 70, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/13]", "While the annotation is selected one at a time:\r\n" + 
				"launch GSPS toolbar and click on 'Add text' button\r\n" + 
				"Click within the text box and add text. Press 'Enter'");
		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);

		//navigate to next slice and draw an annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/13]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, <b>Polyline<\b>}. ");
		polyLine.selectPolylineFromQuickToolbar(1);		
		polyLine.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/13]", "While the annotation is selected one at a time:\r\n" + 
				"launch GSPS toolbar and click on 'Add text' button\r\n" + 
				"Click within the text box and add text. Press 'Enter'");
		polyLine.addResultComment(polyLine.getLinesOfPolyLine(1,1).get(0),poyLineComment);

		//draw circle annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/13]", "Draw the following annotations{<b>Circle<\b>, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 70,70);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/13]", "While the annotation is selected one at a time:\r\n" + 
				"launch GSPS toolbar and click on 'Add text' button\r\n" + 
				"Click within the text box and add text. Press 'Enter'");
		circle.addResultComment(circle.getAllCircles(1).get(0), circleComment);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/13]", "Using Content Selector, load the series used in step 1 into non-empty viewbox. Select the GSPS results to be displayed from results section if needed");
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad();
		firstResultDescriptionAH4 = ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+1 ;

		//Select same series in second view box
		cs.selectResultFromSeriesTab(2,firstResultDescriptionAH4);
		cs.waitForTimePeriod(2000);
		cs.openAndCloseSeriesTab(false);
		viewerPage.click(viewerPage.getViewPort(2));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/13]", "Using same viewbox as in step 2\"\r\n" + 
				"Click on the text created in step 1 one at a time\r\n" + 
				"Edit the text using keyboard and press 'Enter' key");

		lineWithUnit.editResultComment(lineWithUnit.getTextCommentAddedForLinearMeasurement(2).get(0),editRulerComment);
		point.editResultComment(point.getTextCommentsForAllPoints(2).get(0),editPointComment);
		ellipse.editResultComment(ellipse.getTextCommentsforAllEllipses(2).get(0),editEllipseComment);
		polyLine.editResultComment(polyLine.getTextCommentsOnAllPolyLines(2).get(0),editPoyLineComment);		
		circle.editResultComment(circle.getTextCommentsforAllCircles(2).get(0), editCicleComment);

		lineWithUnit.assertEquals(lineWithUnit.getText(lineWithUnit.getTextCommentAddedForLinearMeasurement(2).get(0)),editRulerComment,"Verifying the text is edited","verified");
		point.assertEquals(lineWithUnit.getText(point.getTextCommentsForAllPoints(2).get(0)),editPointComment,"Verifying the text is edited","verified");
		ellipse.assertEquals(lineWithUnit.getText(ellipse.getTextCommentsforAllEllipses(2).get(0)),editEllipseComment,"Verifying the text is edited","verified");
		polyLine.assertEquals(lineWithUnit.getText(polyLine.getTextCommentsOnAllPolyLines(2).get(0)),editPoyLineComment,"Verifying the text is edited","verified");		
		circle.assertEquals(lineWithUnit.getText(circle.getTextCommentsforAllCircles(2).get(0)), editCicleComment,"Verifying the text is edited","verified");


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/13]", "Using Content Selector, load the series used in step 3 into empty viewbox. Select the GSPS results to be displayed from results section if needed");

		//Select same series in last empty view box
		cs.selectResultFromSeriesTab(6,firstResultDescriptionAH4);
		cs.click(cs.getViewPort(6));

		lineWithUnit.editResultComment(lineWithUnit.getTextCommentAddedForLinearMeasurement(6).get(0),rulerComment);
		point.editResultComment(point.getTextCommentsForAllPoints(6).get(0),pointComment);
		ellipse.editResultComment(ellipse.getTextCommentsforAllEllipses(6).get(0),ellipseComment);
		polyLine.editResultComment(polyLine.getTextCommentsOnAllPolyLines(6).get(0),poyLineComment);		
		circle.editResultComment(circle.getTextCommentsforAllCircles(6).get(0), circleComment);

		lineWithUnit.assertEquals(lineWithUnit.getText(lineWithUnit.getTextCommentAddedForLinearMeasurement(6).get(0)),rulerComment,"Verifying the text is edited","verified");
		point.assertEquals(lineWithUnit.getText(point.getTextCommentsForAllPoints(6).get(0)),pointComment,"Verifying the text is edited","verified");
		ellipse.assertEquals(lineWithUnit.getText(ellipse.getTextCommentsforAllEllipses(6).get(0)),ellipseComment,"Verifying the text is edited","verified");
		polyLine.assertEquals(lineWithUnit.getText(polyLine.getTextCommentsOnAllPolyLines(6).get(0)),poyLineComment,"Verifying the text is edited","verified");		
		circle.assertEquals(lineWithUnit.getText(circle.getTextCommentsforAllCircles(6).get(0)), circleComment,"Verifying the text is edited","verified");


	}

	@Test(groups ={"Chrome","DE1031"})
	public void test12_DE1031_TC4263_verifyTextStyleIsInSyncAcrossAllAnnotations() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Linear measurement text font/style is sync with Text Annotations and text comment font/style."
				+ "<br> Verify annotations text style visible on black/white background after changing states.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);

		cs = new ContentSelector(driver);

		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		polyLine = new PolyLineAnnotation(driver);
		circle = new CircleAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Draw the following annotations{Circle, Ellipse, Point, Line, <b>Distance Measurement<\b>, Polyline}. ");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-130,-150,200,0);
		lineWithUnit.addResultComment(lineWithUnit.getLinearMeasurements(1, 1).get(0),rulerComment);

		//select Point annotation from radial menu and draw a point annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Draw the following annotations{Circle, Ellipse, <b>Point<\b>, Line, Distance Measurement, Polyline}. ");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -90, 0);
		point.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment);

		//select ellipse from radial menu and draw a ellipse 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Draw the following annotations{Circle, <b>Ellipse<\b>, Point, Line, Distance Measurement, Polyline}. ");
		ellipse.mouseHover(ellipse.getViewPort(1));
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -200, 70, 100);
		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);

		//draw circle annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Draw the following annotations{<b>Circle<\b>, Ellipse, Point, Line, Distance Measurement, Polyline}. ");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -100, 70, 50,50);
		circle.addResultComment(circle.getAllCircles(1).get(0), circleComment);

		//navigate to next slice and draw an annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Draw the following annotations{Circle, Ellipse, Point, Line, Distance Measurement, <b>Polyline<\b>}. ");
		polyLine.selectPolylineFromQuickToolbar(1);		
		polyLine.drawFreehandPolyLineExitUsingESC(1, new int[] {25,44,20,-32,37,-23,37,-9});	
		polyLine.addResultComment(polyLine.getLinesOfPolyLine(1,1).get(1),poyLineComment);

		viewerPage.assertEquals(polyLine.textComment.size(), polyLine.resultComment.size(), "Checkpoint[6/7]", "Verifying the total finding are equal both in black and green");
		viewerPage.assertEquals(polyLine.textComment.size(), 5, "Checkpoint[7/7]", "verifying the total number of finding been created");

		verifyTextSize();

		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		polyLine.selectRejectfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(1,1).get(0));
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		verifyTextSize();

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		ellipse.selectAcceptfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		polyLine.selectAcceptfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(1,1).get(0));
		circle.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		verifyTextSize();

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		ellipse.selectAcceptfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		polyLine.selectAcceptfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(1,1).get(0));
		circle.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		verifyTextSize();

	}

	@Test(groups ={"Chrome","DE1031"})
	public void test13_DE1031_TC4263_verifyTextStyleAreSameInTextAnnAndMeasurement() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Linear measurement text font/style is sync with Text Annotations and text comment font/style.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		lineWithUnit = new MeasurementWithUnit(driver);
		TextAnnotation textAnn = new TextAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Drawing the text annotation");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-150,-150,200,0);

		//draw text annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Creating text annotation");
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -300, -200,textComment);

		viewerPage.assertEquals(textAnn.getTextAnnotation(1, 1).getCssValue(NSGenericConstants.FONT_SIZE_PROP),lineWithUnit.getLinearMeasurementsText(1).get(0).getCssValue(NSGenericConstants.FONT_SIZE_PROP),"Checkpoint[3/4]","Verifying the font-size is 16 px");
		//		viewerPage.assertEquals(textAnn.getBorderTextAnnotations(1).get(0).getCssValue(NSGenericConstants.FILL),lineWithUnit.getLinearMeasurementsBorderText(1).get(0).getCssValue(NSGenericConstants.FILL),"Checkpoint[4/4]","verifying both annotation text broder color is same");


	}

	//	Unable to add more text in comment box after adding certain number of characters
	@Test(groups ={"Chrome","DE1274","Positive"})
	public void test22_DE1274_TC5266_TC5319_verifyPersistenceLongTextComment() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Add/Edit comment text box is accepting as many characters as the user wants to enter."
				+ "<br> Verify the comment added for annotations with out line breaks is displayed as wrap text after the reload of the viewer page.");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientName,  1, 1);
		
		circle = new CircleAnnotation(driver);
		panel = new OutputPanel(driver);

		circle.doubleClick(circle.getViewPort(1));

		//draw circle annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Draw the annotation");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -10, -10, 40, 40);


		String circleComment ="";
		for(int i = 0;i<25;i++)
			circleComment = circleComment +longComment+i+" .";

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Adding the comment to circle");
		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);
		circle.compareElementImage(protocolName, circle.getViewPort(1), "Checkpoint[2/9]: verifying the comment is centered", "test22_1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verifying the added comment");
		circle.assertEquals(circle.getText(circle.resultComment.get(0)), circleComment, "Verifying the text","verified");
		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "The result comment is accepted by default","verified");

		circle.openFindingTableOnBinarySelector(1);
		circle.assertTrue(circle.isElementPresent(circle.iIconInfo.get(0)), "Checkpoint[4/9]", "Verifying the info icon presence");
		circle.mouseHover(circle.iIconInfo.get(0));
		circle.assertEquals(circle.getAttributeValue(circle.iIconInfo.get(0), NSGenericConstants.TITLE), circleComment, "Checkpoint[5/9]" ,"Verifying the tooltip on mouse hover on iIcon");

		panel.enableFiltersInOutputPanel(true, false, false);				
		circle.assertEquals(panel.getMultiLineCommenEntriesInOP(1).get(1), circleComment, "Checkpoint[6/9]","Verifying the multiline text in output panel");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reloading the viewer");
		helper.browserBackAndReloadViewer(patientName,  1, 1);

		circle.assertEquals(circle.getText(circle.resultComment.get(0)), circleComment, "Checkpoint[7/9]","Verifying the added comment");
		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Checkpoint[8/9]", "The result comment is accepted by default");
		circle.compareElementImage(protocolName, circle.getViewPort(1), "Checkpoint[9/9]", "test22_2");

	}

	@Test(groups ={"Chrome","DE1297","Positive","US1092"})
	public void test23_US1092_TC5024_DE1274_TC5299_TC5320_verifyPersistenceLongTextCommentWithLineBreak() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the comment added for annotations with line breaks is displayed as is even after the reload of the viewer page."
				+ "<br> Verify the multi line comment is displayed at the bottom and horizontally center of the annotation."
				+ "<br> Verify that comment added for any annotation in multilines should display in multilines in tool tip of dropdown list of A/R bar");
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientName,  1, 1);
		
		circle = new CircleAnnotation(driver);
		panel = new OutputPanel(driver);

		circle.doubleClick(circle.getViewPort(1));

		//draw circle annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw the Circle ");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -10, -10, 40, 40);

		String[] additionalComment =new String[11];
		for(int i = 0;i<11 ;i++)
			additionalComment[i] = longComment+i+".";

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Adding multi line comment");
		circle.addMultiLineResultComment(circle.getAllCircles(1).get(0),additionalComment);		
		circle.compareElementImage(protocolName, circle.getViewPort(1), "Checkpoint[1/9]: verifying the comment is centered", "test23_1");

		circle.assertEquals(circle.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Checkpoint[2/9]", "Verifying the result is accepted");
		circle.assertEquals(circle.resultMultiLineComment.size(), additionalComment.length, "Checkpoint[3/9]", "Verifying the multi lines comment size" );

		for(int i =0 ; i<additionalComment.length-1 ;i++)
			circle.assertEquals(circle.getText(circle.resultMultiLineComment.get(i)), additionalComment[i], "Checkpoint[4/9]", "Verifying the multiline comments");

		circle.openFindingTableOnBinarySelector(1);
		circle.assertTrue(circle.isElementPresent(circle.iIconInfo.get(0)), "Checkpoint[5/9]","Verifying the iIcon presence");
		circle.mouseHover(circle.iIconInfo.get(0));
		for(int i =0 ; i<additionalComment.length-1 ;i++)
			circle.assertTrue(circle.getAttributeValue(circle.iIconInfo.get(0), NSGenericConstants.TITLE).contains(additionalComment[i]), "Checkpoint[6/9]", "Verifying the multiline comments on iIcon tooltip");

		panel.enableFiltersInOutputPanel(true, false, false);
		circle.assertEquals(panel.getMultiLineCommenEntriesInOP(1).size()-1, additionalComment.length, "Checkpoint[7/9]", "Verifying the multi lines comment size");
		for(int i =1 ; i<additionalComment.length-1 ;i++)
			circle.assertEquals(panel.getMultiLineCommenEntriesInOP(1).get(i), additionalComment[i-1], "Checkpoint[8/9]", "Verifying the multiline comments");
		panel.openAndCloseOutputPanel(false);

		helper.browserBackAndReloadViewer(patientName, 1, 1);

		circle.compareElementImage(protocolName, circle.getViewPort(1), "Checkpoint[9/9]: verifying the comment is centered", "test23_2");

	}

	@Test(groups ={"Chrome","Positive","US1090"})
	public void test24_US1090_TC5007_verifyMultiLineCharIsPresentInMachineData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify text should be rendered on multiple lines when a newline character is present");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID,  1);

		textAnn = new TextAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify the background of the finding is in grey color when finding is selected on finding toolbar ");
		textAnn.openFindingTableOnBinarySelector(1);
		textAnn.click(textAnn.toggleButton.get(1));
		textAnn.assertTrue(textAnn.verifyFindingIsHighlighted(1),"Verify Background color as Gray for the first finding when it is selected","Verified");

		//verify add comment functionality for findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Selecting the machine text annotation");
		textAnn.selectFindingFromGroupOfTable(1, 1, 2);

		int lines = textAnn.getTextLinesFromTextAnnotation(1, 3).size();
		textAnn.assertTrue(lines>1, "Checkpoint[3/5]", "Verifying that machine generated text annotation has \n");

		textAnn.updateMultiLineTextOnTextAnnotation(1, 3, "testing");		
		textAnn.assertTrue(textAnn.getTextLinesFromTextAnnotation(1, 3).size()>lines, "Checkpoint[4/5]", "Verifying after adding new line text");
		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 3 ,false), "Checkpoint[5/5]", "The pointer color for accepted finding is green");


	}
	
	@Test(groups ={"Chrome","IE","Edge","DE1340","Positive"})
	public void test25_DE1340_TC5678_verifyPersistenceTextCommentAfterMovingFreehandPolyline() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comment on freehand polyline dispappears while moving polyline");

	
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,  1);

		polyLine=new PolyLineAnnotation(driver);
				
		int[] coordinates = new int[] {-60,-60,60,-60,60,60,-60,60,-60,-60};
		polyLine.selectPolylineFromQuickToolbar(1);
		polyLine.drawFreehandPolyLineExitUsingESC(1, coordinates);
		polyLine.addResultComment(1,poyLineComment);
		
		int[] abc = new int[] {-46,23,-41,37,-42,42,-25,33};
		polyLine.drawFreehandPolyLineExitUsingESC(1, abc);
		polyLine.addResultComment(1, editPoyLineComment);
		
		int totalComment=polyLine.resultComment.size();
		 polyLine.click(polyLine.getViewPort(2));
		polyLine.assertTrue(polyLine.verifyTextCommentWhileMovingPolyline(1, 1, -60, -60,editPoyLineComment,totalComment,false), "Checkpoint[1/6]", "Verified result comment for open freehand polyline while moving closed freehand polyline.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(1)), poyLineComment, "Checkpoint[2/6]", "Verified result comment for closed freehand polyline after move action perform.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(0)), editPoyLineComment, "Checkpoint[3/6]", "Verified result comment for open freehand polyline after move action perform.");
		
		polyLine.assertTrue(polyLine.verifyTextCommentWhileMovingPolyline(1, 1, 20, 20,poyLineComment,totalComment,false), "Checkpoint[4/6]", "Verified result comment for closed freehand polyline while moving open freehand polyline.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(0)), poyLineComment, "Checkpoint[5/6]", "Verified result comment for closed freehand polyline after move action perform.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(1)), editPoyLineComment, "Checkpoint[6/6]", "Verified result comment for open freehand polyline after move action perform.");

	}

	@Test(groups ={"Chrome","IE","Edge","DE1340","Positive"})
	public void test26_DE1340_TC5678_verifyPersistenceTextCommentAfterMovingFreehandPolyline() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comment on freehand polyline dispappears while moving polyline");

				
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,  1);

		polyLine=new PolyLineAnnotation(driver);
			
		
		int[] coordinates = new int[] {-50,-50,50,-50,50,50,-50,50,-50,-50};
		polyLine.selectPolylineFromQuickToolbar(1);
		polyLine.drawClosedPolyLine(1, coordinates);
		polyLine.addResultComment(1,poyLineComment);
		
		int[] abc = new int[] {37,-9,-7,-34,10,-25,43,-27,5,-36};
		polyLine.drawPolyLineExitUsingESCKey(1, abc);
		polyLine.addResultComment(1, editPoyLineComment);
	
		int totalComment=polyLine.resultComment.size();
	    polyLine.click(polyLine.getViewPort(2));
		polyLine.assertTrue(polyLine.verifyTextCommentWhileMovingPolyline(1, 1, -60, -60,editPoyLineComment,totalComment,true),"Checkpoint[1/6]", "Verified result comment for open classic polyline during move action perform for closed polyline.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(1)), poyLineComment, "Checkpoint[2/6]", "Verified result comment for closed classic polyline after move action perform.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(0)), editPoyLineComment, "Checkpoint[3/6]", "Verified result comment for open classic polyline after move action perform.");
		
		polyLine.click(polyLine.getViewPort(2));
		polyLine.assertTrue(polyLine.verifyTextCommentWhileMovingPolyline(1, 1, 20, 20,poyLineComment,totalComment,true),"Checkpoint[4/6]", "Verified result comment for closed classic polyline during move action perform for open polyline.");;
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(0)), poyLineComment, "Checkpoint[5/6]", "Verified result comment for closed classic polyline after move action perform.");
		polyLine.assertEquals(polyLine.getText(polyLine.resultComment.get(1)), editPoyLineComment, "Checkpoint[6/6]", "Verified result comment for open classic polyline after move action perform.");
	}

	private void verifyTextSize() {

		point = new PointAnnotation(driver);
		for(int i =0 ;i<point.textComment.size();i++) {

			viewerPage.assertEquals(point.textComment.get(i).getCssValue(NSGenericConstants.FONT_SIZE_PROP),ViewerPageConstants.FONT_SIZE_FOR_TEXT,"Checkpoint[a]","Verifying the font-size");
			viewerPage.assertEquals(point.textComment.get(i).getCssValue(NSGenericConstants.FILL),ViewerPageConstants.BLACK_COLOR_RGB,"Checkpoint[b]","verifying the border color is black");


		}
	}

}
