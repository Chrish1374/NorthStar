package com.trn.ns.test.viewer.GSPS;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CommentAdditionTest extends TestBase  {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private SimpleLine line;
	private PolyLineAnnotation polyLine;

	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String longComment = "This is a sentence which is long ";

	private final String pointComment="Point comment";
	private final String editPointComment="Edited Point comment";
	private final String rulerComment="Ruler comment";
	private final String editRulerComment="Edited Ruler comment";
	private final String lineComment="Line comment";
	private final String editLineComment="Edit Line comment";
	private final String ellipseComment="Ellipse comment";
	private final String editEllipseComment="Edit Ellipse comment";
	private final String poyLineComment="test";
	private final String editPoyLineComment="Edit Poly Line";
	private final String circleComment="Circle comment";
	private final String editCicleComment="Edited Circle comment";
	private HelperClass helper;
	private ViewerOrientation orin;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	@Test(groups ={"IE11","Chrome","Edge","US772","Sanity","DE1068","BVT", "DR2283"})
	public void test01_US772_TC3083_DE1068_TC4542_DR2283_TC9165_verifyResultCommentForFinding() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to add comment for each finding"
				+ "<br> Execute TC3083 from US772"
				+ "<br> Execute TC9165 from DR2283");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		polyLine = new PolyLineAnnotation(driver);
		line = new SimpleLine(driver);

		viewerPage.clearConsoleLogs();

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//navigate to next slice and draw an annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		polyLine.selectPolylineFromQuickToolbar(1);		
		polyLine.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, -70, -80, -120, 90);

		//navigate to slice previous slice
		viewerPage.scrollUpToSliceUsingKeyboard(4);
		viewerPage.clearConsoleLogs();

		//select point annotation and add result comment
		//Add result comment and also check TC9165 to make sure edit box is getting closed after add comment
		viewerPage.assertTrue(line.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment),"addPointComment","Verify if comment added successfully and Editbox should be closed after editing is completed for the comment.");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/15]", "Verify entered result comment text is visible on viewbox with italic format for a point annotation");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), pointComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+pointComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//edit the above drawn text annotation
		//Add result comment and also check TC9165 to make sure edit box is getting closed after edition
		viewerPage.assertTrue(line.editResultComment(line.resultComment.get(0), editPointComment),"editPointComment","Verify if comment edited successfully and Editbox should be closed after editing is completed for the comment(TC9165).");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/15]", "Verify updated text appear on result comment");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), editPointComment, "Verify updated text appear on result comment", "The entered result comment is "+editPointComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//delete above result comment
		line.deleteResultComment(line.resultComment.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/15]", "Verify result comment is deleted");
		viewerPage.assertEquals(line.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//select linear measurement annotation and add result comment
		line.addResultComment(lineWithUnit.getLinearMeasurements(1, 1).get(0),rulerComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/15]", "Verify entered result comment text is visible on viewbox with italic format for linear measurement");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), rulerComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+rulerComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");
		viewerPage.clearConsoleLogs();
		//edit the above drawn text annotation
		line.editResultComment(line.resultComment.get(0), editRulerComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/15]", "Verify updated text appear on result comment");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), editRulerComment, "Verify updated text appear on result comment", "The entered result comment is "+editRulerComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//delete above result comment
		line.deleteResultComment(line.resultComment.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/15]", "Verify result comment is deleted");
		viewerPage.assertEquals(line.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//select ellipse measurement annotation and add result comment
		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/15]", "Verify entered result comment text is visible on viewbox with italic format for eliipse annotation");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), ellipseComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+ellipseComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//edit the above drawn text annotation
		line.editResultComment(line.resultComment.get(0), editEllipseComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/15]", "Verify updated text appear on result comment");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), editEllipseComment, "Verify updated text appear on result comment", "The entered result comment is "+editEllipseComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//delete above result comment
		line.deleteResultComment(line.resultComment.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/15]", "Verify result comment is deleted");
		viewerPage.assertEquals(line.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		//scroll to next GSPS slices
		viewerPage.scrollDownToSliceUsingKeyboard(2);

		viewerPage.clearConsoleLogs();
		//select annotation and add result comment
		line.addResultComment(polyLine.getLinesOfPolyLine(1,1).get(0),poyLineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/15]", "Verify entered result comment text is visible on viewbox with italic format for poly line annotation");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), poyLineComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+poyLineComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//edit the above drawn result comment
		line.editResultComment(line.resultComment.get(0), editPoyLineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/15]", "Verify updated text appear on result comment");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), editPoyLineComment, "Verify updated text appear on result comment", "The entered result comment is "+editPoyLineComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//delete above result comment
		line.deleteResultComment(line.resultComment.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/15]", "Verify result comment is deleted");
		viewerPage.assertEquals(line.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		//scroll to next GSPS slices
		viewerPage.scrollDownToSliceUsingKeyboard(2);

		viewerPage.clearConsoleLogs();
		//select line annotation and add result comment
		line.addResultComment(line.getLine(1,1).get(0),lineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/15]", "Verify entered result comment text is visible on viewbox with italic format for line annotation");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), lineComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+lineComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//edit the above drawn line annotation
		line.editResultComment(line.resultComment.get(0), editLineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/15]", "Verify updated text appear on result comment");
		viewerPage.assertEquals(viewerPage.getText(line.resultComment.get(0)), editLineComment, "Verify updated text appear on result comment", "The entered result comment is "+editLineComment);
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");

		viewerPage.clearConsoleLogs();
		//delete above result comment
		line.deleteResultComment(line.resultComment.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[15/15]", "Verify result comment is deleted");
		viewerPage.assertEquals(line.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");
//		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Verifying the Console error absence", "verified");



	}

	@Test(groups ={"IE11","Chrome","Edge","US772"})
	public void test02_US772_TC3084_TC3191_verifyOneResultCommentForEachFinding() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to add one result for each finding"
				+ "Verify Info bar on hovering add text icon without selecting finding");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		line = new  SimpleLine(driver);
		polyLine = new PolyLineAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//navigate to next slice and draw an annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		polyLine.selectPolylineFromQuickToolbar(1);		
		polyLine.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, -70, -80, -120, 90);

		//navigate to slice previous slice
		viewerPage.scrollUpToSliceUsingKeyboard(4);

		//open a GSPS radial menu and hover over edit finding
		line.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));

		viewerPage.mouseHover(line.getAcceptRejectToolBar(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify Add comment icon is disabled");
		viewerPage.assertTrue(line.isIconDisable(line.gspsText), "Verify Add Text icon is disabled", "The Add Text icon is disabled");

		//hover over add text icon
		line.hoverOnAddTextButton(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify info bar for selecting a finding is displayed");
		viewerPage.assertTrue(viewerPage.isElementPresent(line.commentInfo),"Verify Info bar appear on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(line.commentInfo),ViewerPageConstants.NO_FINDING,"Verify text of info bar", "Verified the text of info bar");

		//select point annotation and add result comment and verify that add result button is disable
		line.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify add text comment icon is disable, if a comment already exist for a Cross Point Finding");
		viewerPage.mouseHover(line.getAcceptRejectToolBar(1));
		viewerPage.assertTrue(line.isIconDisable(line.gspsText), "Verify Add Text icon is disabled", "The Add Text icon is disabled");

		//hover over add text icon and verify Info bar appear on view box
		line.hoverOnAddTextButtonForFinding(point.getHandlesOfPoint(1, 1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify a infobar appear on hovering add text button, if a comment already exist for a Cross Point finding");
		viewerPage.assertTrue(viewerPage.isElementPresent(line.commentInfo),"Verify Info bar appear on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(line.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Verify text of info bar", "Verified the text of info bar");

		//select linear measurement and add result comment and verify that add result button is disable
		line.addResultComment(lineWithUnit.getLinearMeasurements(1,1).get(0),rulerComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify add text comment icon is disable, if a comment already exist for linear measurement finding");
		viewerPage.mouseHover(line.getAcceptRejectToolBar(1));
		viewerPage.assertTrue(line.isIconDisable(line.gspsText), "Verify Add Text icon is disabled", "The Add Text icon is disabled");

		//hover over add text icon and verify Info bar appear on view box
		line.hoverOnAddTextButtonForFinding(lineWithUnit.getLinearMeasurements(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify a infobar appear on hovering add text button, if a comment already exist for finding");
		viewerPage.assertTrue(viewerPage.isElementPresent(line.commentInfo),"Verify Info bar appear on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(line.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Verify text of info bar", "Verified the text of info bar");

		//select ellipse annotation and add result comment and verify that add result button is disable
		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verify add text comment icon is disable, if a comment already exist for ellipse finding");
		viewerPage.mouseHover(line.getAcceptRejectToolBar(1));
		viewerPage.assertTrue(line.isIconDisable(line.gspsText), "Verify Add Text icon is disabled", "The Add Text icon is disabled");

		//hover over add text icon and verify Info bar appear on view box
		ellipse.hoverOnAddTextButtonForFinding(ellipse.getEllipses(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify a infobar appear on hovering add text button, if a comment already exist for finding");
		viewerPage.assertTrue(viewerPage.isElementPresent(line.commentInfo),"Verify Info bar appear on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(line.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Verify text of info bar", "Verified the text of info bar");

		//navigate to next slice and draw a line annotation
		viewerPage.scrollDownToSliceUsingKeyboard(2);

		//select annotation and add result comment and verify that add result button is disable
		line.addResultComment(polyLine.getLinesOfPolyLine(1,1).get(1),poyLineComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verify add text comment icon is disable, if a comment already exist for Poly line finding");
		viewerPage.mouseHover(line.getAcceptRejectToolBar(1));
		viewerPage.assertTrue(line.isIconDisable(line.gspsText), "Verify Add Text icon is disabled", "The Add Text icon is disabled");

		//hover over add text icon and verify Info bar appear on view box
		line.hoverOnAddTextButtonForFinding(polyLine.getLinesOfPolyLine(1,1).get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify a infobar appear on hovering add text button, if a comment already exist for finding");
		viewerPage.assertTrue(viewerPage.isElementPresent(line.commentInfo),"Verify Info bar appear on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(line.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Verify text of info bar", "Verified the text of info bar");		
	}

	@Test(groups ={"IE11","Chrome","Edge","US772"})
	public void test03_US772_TC3087_TC3088_verifyResultCommentOnZoomAndPAN() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify result comment on zoom and pan"
				+ "Verify result comment on flip and rotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		line = new  SimpleLine(driver);
		polyLine = new PolyLineAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, -50);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 100, -50);

		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		polyLine.selectPolylineFromQuickToolbar(2);		
		polyLine.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});

		//select each finding and a add a result finding
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		line.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment);
		line.addResultComment(lineWithUnit.getLinearMeasurements(1,1).get(0),rulerComment);
		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		polyLine.addResultComment(polyLine.getLinesOfPolyLine(2,1).get(1),poyLineComment);

		//move all the finding and verify the result comment remain relative to finding
		lineWithUnit.moveLinearMeasurement(1, 1, 50, -50);
		point.movePoint(1, 1, 50, 50);
		ellipse.moveEllipse(1,1, -200, 120);
		
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		
		polyLine.movePolyLine(2, 1, -30, -30);
		
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);

	
		viewerPage.closeNotification();;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify size and position of text comment remains unchanged relative to the annotation on moving of finding");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb1_CheckPoint1");	
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(2),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb2_CheckPoint1");	

		//select zoom from radial menu and perform zoom
		viewerPage.selectZoomFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -30,-30);
		viewerPage.closeNotification();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify size and position of text comment remains unchanged relative to the annotation on Zoom");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb1_CheckPoint2");	
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(2),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb2_CheckPoint2");	

		//select zoom from radial menu and perform zoom
		viewerPage.selectPanFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -50,-50);
		viewerPage.closeNotification();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify size and position of text comment remains unchanged relative to the annotation on Pan");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb1_CheckPoint3");	
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(2),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb2_CheckPoint3");	

		viewerPage.click(viewerPage.getViewPort(1));

		//rotate the series on viewbox1
		orin = new ViewerOrientation(driver);
		orin.rotateSeries(orin.getTopOrientationMarker(1), orin.topCounterClockwiseRotationMarker(1));
		viewerPage.closeNotification();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify size and position of text comment remains unchanged relative to the annotation on orientation change");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb1_CheckPoint4");	
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(2),"Verify size and position of text comment on remains unchanged relative to the annotation on zoom ","test03_vb2_CheckPoint4");	

	}

	@Test(groups ={"IE11","Chrome","Edge","US772","BVT"})
	public void test04_US772_TC3086_verifyStateOfResultComment() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of result comment for all annotation");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		polyLine = new PolyLineAnnotation(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -150, -50);
		
		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, 50, 40, -50);

		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		polyLine.selectPolylineFromQuickToolbar(2);		
		polyLine.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});

		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		//select each finding and a add a result finding
		point.addResultComment(point.getHandlesOfPoint(1, 1).get(0),pointComment);
		lineWithUnit.addResultComment(lineWithUnit.getLinearMeasurements(1,1).get(0),rulerComment);
		ellipse.addResultComment(ellipse.getEllipses(1).get(0),ellipseComment);
		
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		polyLine.addResultComment(polyLine.getLinesOfPolyLine(2,1).get(1),poyLineComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify all the result comment are accepted by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Verify linear measurement result comment is accepted by default", "The linear measurement result comment is accepted by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Verify Point measurement result comment is accepted by default", "The Point measurement result comment is accepted by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(2), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Verify Ellipse measurement result comment is accepted by default", "The Ellipse measurement result comment is accepted by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(3), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Verify Polyline measurement result comment is accepted by default", "The Polyline measurement result comment is accepted by default");

		//reject all the finding and verify color of each finding
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		lineWithUnit.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		polyLine.selectRejectfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(2,1).get(1),false);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify all the result comment are in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "Verify Point measurement result comment is in rejected", "The Point measurement result comment is in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(2), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "Verify Ellipse measurement result comment is in rejected", "The Ellipse measurement result comment is in rejected by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.resultComment.get(3), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "Verify Polyline measurement result comment is in rejected", "The Polyline measurement result comment is in rejected by default");

		//reject all the finding and verify color of each finding
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		lineWithUnit.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		for(int i =0;i<5;i++)
			viewerPage.closeWaterMarkIcon(i);
		polyLine.selectRejectfromGSPSRadialMenu(polyLine.getLinesOfPolyLine(2,1).get(1),false);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify all the result comment are in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(lineWithUnit.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "Verify linear measurement result comment is in rejected", "The linear measurement result comment is in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(lineWithUnit.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "Verify Point measurement result comment is in rejected", "The Point measurement result comment is in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(lineWithUnit.resultComment.get(2), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "Verify Ellipse measurement result comment is in rejected", "The Ellipse measurement result comment is in rejected by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(lineWithUnit.resultComment.get(3), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "Verify Polyline measurement result comment is in rejected", "The Polyline measurement result comment is in rejected by default");

	}

	@Test(groups ={"IE11","Chrome","Edge","US735"})
	public void test05_US735_TC2992_verifyResultCommentForFindingsForCircle() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to add comment for each finding for circle annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		circle = new CircleAnnotation(driver);

		//select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -70, -80, -120, 90);
		circle.selectCircle(1, 1);
		//		viewerPage.mouseHover(viewerPage.getViewPort(2));

		//Add comment by clicking on 'Add Text' from accept reject toolbar
		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);

		//edit the above result comment
		circle.editResultComment(circle.resultComment.get(0), editCicleComment);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify updated text appear on result comment");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), editCicleComment, "Verify updated text appear on result comment", "The entered result comment is "+editCicleComment);

		//delete above result comment
		circle.deleteResultComment(circle.resultComment.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify result comment is deleted");
		viewerPage.assertEquals(circle.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");

	}

	@Test(groups ={"IE11","Chrome","Edge","US735"})
	public void test06_US735_TC2993_verifyOneResultCommentPerFindingsForCircle() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to add only one comment for each finding for circle annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		circle = new CircleAnnotation(driver);

		//select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -70, -80, -120, 90);
		circle.selectCircle(1, 1);

		//Add comment by clicking on 'Add Text' from accept reject toolbar
		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);

		//hover over add text icon
		circle.hoverOnAddTextButtonForFinding(circle.getAllCircles(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify infobar appears on hovering add text button as only one text comment is allowed per annotation");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.commentInfo),"Verify Info bar appears on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(circle.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Verify text of info bar", "Verified the text of info bar");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify add text comment icon is disabled when comment already exists for any finding");
		viewerPage.assertTrue(circle.isIconDisable(circle.gspsText), "Verify Add text icon is disabled", "The Add text icon is disabled");	
	}

	@Test(groups ={"IE11","Chrome","Edge","US735","Sanity"})
	public void test07_US735_TC2994_verifyTextCommentStateForCircle() throws  InterruptedException 
	{	
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of text comment for circle annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		circle = new CircleAnnotation(driver);

		//select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -70, -80, -120, 90);

		//Add comment by clicking on 'Add Text' from accept reject toolbar
		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify state of result comment is accepted by default");
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR, "Verify circle result comment is accepted by default", "The circle result comment is accepted by default");

		//reject all the finding and verify color of each finding
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify all the result comment are in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "Verify circle result comment is in rejected state", "The circle result comment is in rejected state");

		//reject all the finding and verify color of each finding
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//verify all result comment are accepted by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify all the result comment are in rejected state");
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.PENDING_COLOR, "Verify circle result comment is in pending state", "The circle result comment is in pending state");
	}

	@Test(groups ={"IE11","Chrome","Edge","US735"})
	public void test08_US735_TC3000_verifyTextCommentPositionOnMoveResizeForCircle() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify size and position of text comment remains unchanged relative to Circle");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);

		circle = new CircleAnnotation(driver);

		//select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -70, -80, -120, 90);

		//Add comment by clicking on 'Add Text' from accept reject toolbar
		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);

		//		viewerPage.click(circle.getAllCircles(1).get(0));
		//		circle.selectCircle(1, 1);
		circle.moveSelectedCircle(1, 50, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify size and position of text comment remains unchanged relative to the annotation on moving of finding");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewbox(1),"Verify text comment remains unchanged relative to the annotation","test08_CheckPoint2");	

		//select zoom from radial menu and perform zoom
		viewerPage.selectZoomFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -30,-30);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify size and position of text comment remains unchanged relative to the annotation on Zoom");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewbox(1),"Verify text comment remains unchanged relative to the annotation on zoom ","test08_CheckPoint3");

		//Select PAN  from radial menu and perform PAN
		viewerPage.selectPanFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -50,-50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify size and position of text comment remains unchanged relative to the annotation on PAN");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewbox(1),"Verify text comment remains unchanged relative to the annotation on PAN ","test08_CheckPoint4");	
	}

	@Test(groups ={"IE11","Chrome","Edge","US735"})
	public void test09_US735_TC3044_verifyTextCommentPositionOnRotationFlipForCircle() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify size and position of text comment remains unchanged relative to Circle on rotation and flip");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		circle = new CircleAnnotation(driver);

		//select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -70, -80, -120, 90);
		viewerPage.mouseHover(viewerPage.getViewPort(2));

		//Add comment by clicking on 'Add Text' from accept reject toolbar
		circle.addResultComment(circle.getAllCircles(1).get(0),circleComment);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);

//		viewerPage.closeNotification();
		//		viewerPage.click(viewerPage.getViewPort(1));
		//		//Rotate the series on viewbox1
		//		
		//		viewerPage.rotateSeries(viewerPage.getTopOrientationMarker(1), viewerPage.topClockwiseRotationMarker(1));;
		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify text comment remains unchanged relative to the annotation on orientation change");
		//		viewerPage.compareElementImage(protocolName,viewerPage.getViewbox(1),"Verify text comment remains unchanged relative to the annotation on orientation change ","test09_CheckPoint2");	

		//Flip the series on viewbox1
		orin = new ViewerOrientation(driver);
		orin.flipSeries(orin.getTopOrientationMarker(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify text comment remains unchanged relative to the annotation on flip");
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1),"Verify text comment remains unchanged relative to the annotation on flip ","test09_CheckPoint3");
	}


}
