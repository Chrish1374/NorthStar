package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
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
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SetFocusOnGSPSFindingTest extends TestBase  {

	private ViewerSliderAndFindingMenu viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;

	private ExtentTest extentTest;
	private ContentSelector contentSelector;
	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private ViewBoxToolPanel preset;

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String ah4FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ah4FilePath);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String liver9SeriesName = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, liver9filePath);
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US734","DE1659","Positive"})
	public void test01_US734_TC2549_TC2567_DE1659_TC7034_VerifySetFocusOnGSPSUsingKeyboard() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set focus to the selected GSPS finding within the viewbox -Draw small size annotations and validate animated GSPS navigation <br>Set focus to the selected GSPS finding within the viewbox- Draw large size annotations and validate animated GSPS navigation. <br>"
				+"Verify the navigation through GSPS using AR tool bar and previous and next keys in keyboard.");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName, 1);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 80, 80);
		preset= new 	ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,100);

		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[1/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[2/16]","verify linear measurement annotation is current active GSPS object");   

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[3/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/16]","verify Point annotation is current active GSPS object");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[5/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[7/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/16]","verify Circle annotation is current active GSPS object");

		//press left arrow to move to previous GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[9/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Ellipse annotation is current active GSPS object");

		//press left arrow to move to previous GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[11/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[12/16]","verify Point annotation is current active GSPS object");

		//press left arrow to move to previous GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[13/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   

		//press left arrow to move to previous GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[15/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[16/16]","verify Circle annotation is current active GSPS object");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US734","Sanity"})
	public void test02_US734_TC2580_VerifySetFocusOnGSPSUsingKeyboardOnDifferntSlice() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set focus to the selected GSPS finding within the viewbox- Draw annotations on different slices and validate animated GSPS navigation");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(ah4PatientName,  1, 1);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement		
		viewerPage.scrollToImage(1, 30);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);	


		//select Point annotation from radial menu and draw a point annotation
		viewerPage.scrollToImage(1, 18);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);


		//select elipse from radial menu and draw a ellipse 
		viewerPage.scrollToImage(1,  25);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);


		//select Circle Annotation from radial menu and draw a circle
		viewerPage.scrollToImage(1,  4);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,  100, -50, 40, -50);
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,25);
		//press right arrow to move to next GSPS object
		//		viewerPage.click(viewerPage.getViewPort(1));

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[1/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[2/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),30,"verifying the slice","verified");

		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[3/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),4,"verifying the slice","verified");


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US734"})
	public void test03_US734_TC2581_VerifySetFocusOnGSPSPostOrientationChange() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set focus to the selected GSPS finding within the viewbox- Verify Orientation and GSPS navigation");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(ah4PatientName,  1, 1);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		viewerPage.scrollToImage(1, 30);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);		
		viewerPage.scrollToImage(1, 18);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		viewerPage.scrollToImage(1,  25);

		//select elipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		viewerPage.scrollToImage(1,  4);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -50, 40, -50);
		viewerPage.closingConflictMsg();
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,100);

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		ViewerOrientation orin = new ViewerOrientation(driver);
		orin.flipSeries(orin.getTopOrientationMarker(1));

		//press right arrow to move to next GSPS object
		viewerPage.mouseHover(viewerPage.getViewPort(1));	

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[1/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[2/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),30,"verifying the slice","verified");


		//		//press right arrow to move to next GSPS object
		//		viewerPage.navigateGSPSBackUsingKeyboard();
		//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice","verified");

		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[3/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),4,"verifying the slice","verified");


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US734"})
	public void test04_US734_TC2592_VerifySetFocusOnGSPSPostLayoutChange() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set focus to the selected GSPS finding within the viewbox- Validate GSPS navigation after layout change");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName, 1);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);		
		viewerPage.scrollToImage(1, 18);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		viewerPage.scrollToImage(1,  25);

		//select elipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		viewerPage.scrollToImage(1,  4);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,  100, -50, 40, -50);
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,500);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		// animation to happen
		viewerPage.waitForTimePeriod(10000);
		//press right arrow to move to next GSPS object
		//		viewerPage.click(viewerPage.getViewPort(1));	
		//		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[3/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),4,"verifying the slice","verified");

		//press right arrow to move to next GSPS object added time as there is annimation happening
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.waitForTimePeriod(6000);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[1/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.waitForTimePeriod(6000);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[2/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.waitForTimePeriod(6000);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),48,"verifying the slice","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.waitForTimePeriod(6000);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice","verified");


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US734"})
	public void test05_US734_TC2623_TC2705_VerifySetFocusOnGSPSPostContentSelectorAndSync() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Set focus to the selected GSPS finding within the viewbox- Content selector selection validation <br> Set focus to the selected GSPS finding within the viewbox- Validate synchronization after GSPS animated navigation");

		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(patientName, 1);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		contentSelector = new ContentSelector(driver);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);		
		viewerPage.closingConflictMsg();
		
		viewerPage.scrollToImage(2, 18);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2, -100, -50);
		viewerPage.scrollToImage(2, 25);

		//select elipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		viewerPage.scrollToImage(2, 4);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 100, -50, 40, -50);

		viewerPage.closingConflictMsg();
		preset= new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(2,100);
		contentSelector.selectResultFromSeriesTab(1,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");


		//press right arrow to move to next GSPS object
		//		viewerPage.click(viewerPage.getViewPort(2));	
		//		viewerPage.navigateGSPSForwardUsingKeyboard();

		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[3/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");
		viewerPage.closingConflictMsg();	
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),4,"verifying the slice","verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),4,"verifying the slice in sync","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[1/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),18,"verifying the slice","verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"verifying the slice in sync","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[2/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),25,"verifying the slice","verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice in sync","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(2, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),48,"verifying the slice","verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),48,"verifying the slice in sync","verified");

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Checkpoint[4/4]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),25,"verifying the slice","verified");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"verifying the slice in sync","verified");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE1820"})
	public void test06_DE1820_TC7363_VerifyNavigationWhenNoMeasurementsOnCurrentSlice() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("when the current slice is in between two findings, forward navigation should go to the finding on the slice closest to the current slice (moving top to bottom) and backwards navigation should go to the finding on the slice closest to the current slice (moving bottom to top)");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(ah4PatientName,  1, 1);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);

		int whichViewbox = 1;
		int from_xOffset = 100;
		int from_yOffset = -50;
		int to_xOffset = 50;
		int to_yOffset = -50;

		// Draw an circle on slice 10
		viewerPage.scrollToImage(10, whichViewbox);		
		circle.drawCircle(whichViewbox,  from_xOffset, from_yOffset, to_xOffset, to_yOffset);

		// Draw an circle on slice 15
		viewerPage.scrollToImage(15, whichViewbox);
		circle.drawCircle(whichViewbox,  from_xOffset, from_yOffset, to_xOffset, to_yOffset);

		// draw a circle on slice 20
		viewerPage.scrollToImage(20, whichViewbox);
		circle.drawCircle(whichViewbox,  from_xOffset, from_yOffset, to_xOffset, to_yOffset);

		// now we have annotations on  10, 15 and 20
		// go to slice 18
		// expectation is that forward navigation goes to slice 20		
		viewerPage.scrollToImage(18, whichViewbox);

		//press right arrow to move to next GSPS object
		viewerPage.navigateGSPSForwardUsingKeyboard();
		// next measurement is on 20
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 20, "Checkpoint[1/8]", "verify next measurement is on slice 20");
		viewerPage.navigateGSPSForwardUsingKeyboard();	
		// since we were on the last measurement loop back to start
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 10, "Checkpoint[2/8]", "verify next measurement is on slice 10");
		viewerPage.navigateGSPSForwardUsingKeyboard();	
		// next measurement should be at slice 15
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 15, "Checkpoint[3/8]", "verify next measurement is on slice 15");
		viewerPage.navigateGSPSForwardUsingKeyboard();
		// finally back to slice 20
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 20, "Checkpoint[4/8]", "verify next measurement is on slice 20");

		// move back to slice 18 
		// backwards navigation should go to slice 15
		viewerPage.scrollToImage(18, whichViewbox);

		//press left arrow to move to previous GSPS object
		viewerPage.navigateGSPSBackUsingKeyboard();
		// previous measurement is at slice 15
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 15, "Checkpoint[5/8]", "verify previous measurement is on slice 15");
		viewerPage.navigateGSPSBackUsingKeyboard();
		// then slice 10
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 10, "Checkpoint[6/8]", "verify previous measurement is on slice 10");
		viewerPage.navigateGSPSBackUsingKeyboard();
		// loop around to end - slice 20
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 20, "Checkpoint[7/8]", "verify previous measurement is on slice 20");
		viewerPage.navigateGSPSBackUsingKeyboard();
		// finally back to slice 15
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(whichViewbox), 15, "Checkpoint[8/8]", "verify previous measurement is on slice 15");
	}


}
