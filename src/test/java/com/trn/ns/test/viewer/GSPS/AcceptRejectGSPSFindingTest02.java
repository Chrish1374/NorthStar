package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AcceptRejectGSPSFindingTest02 extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;	
	private ExtentTest extentTest;
	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private TextAnnotation textAnno;
	

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePathGSPSCircle = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^CIRCLE_filepath");
	String GSPS_PatientName_Circle = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathGSPSCircle); 

	String filePathGSPSEllispse = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath");
	String GSPS_PatientName_Ellipse = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathGSPSEllispse); 

	String filePathLPixel = Configurations.TEST_PROPERTIES.get("LPixel_filepath");
	String LPixel_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathLPixel); 

	String filePathNullFrameReferenceID = Configurations.TEST_PROPERTIES.get("Qpqq~qt%_filepath");
	String NullFrameReferenceID_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathNullFrameReferenceID);

	String filePathAidoc = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String GSPS_PatientName_Aidoc = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidoc);
	String GSPS_Patient_Aidoc_Result1=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,filePathAidoc);
	String GSPS_Patient_Aidoc_Result2=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY,filePathAidoc);
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewerOrientation orin;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"Chrome","IE11","Edge","DE682"})
	public void test08_DE682_TC2448_verifyAnnotationDrawOnLPixelData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate that the drawn annotations is present after drawing annotations and navigating back on study for LPixel data");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(LPixel_PatientName, 1, 1);

		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		viewerPage.click(viewerPage.getViewPort(1));
		int value = viewerPage.getCurrentScrollPositionOfViewbox(1);
				
		//scroll to Fifth slice in series
		viewerPage.scrollUpToSliceUsingKeyboard(4);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify user is able to scroll on LPixel data");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), (value -4), "Verify user is able to scroll on LPixel data", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//Draw a Circle on View box 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify a circle annotation is drawn on viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle annotation is drawn on Viewbox1", "Circle annotation is drawn on Viewbox1");

		//Draw line on the View box
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,20,40,120,150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify that linear measurement can be drawn on data");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		//navigate back to study list page and select second study from list
		helper.browserBackAndReloadViewer(LPixel_PatientName, 1, 1);    

		//scroll to Fifth slice in series
		//		viewerPage.scrollDownToSliceUsingKeyboard(4);

		//verify above drawn linear measurement and circle annotation persist on study reload
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify drawn annotation presist on study reload for LPixel data");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify drawn circle annotation persist on study reload", "Circle annotation is present on Viewbox1");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify drawn Measurement annotation persist on study reload","A Single instance of Linear Measurement is present on Viewbox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "", "Verifying the linear measurement is active and accepted");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "", "Verifying the circle is active and accepted");
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US680","DE512"})
	public void test08_DE512_US680_TC2107_verifyGSPSRadialMenuDisplayedInContentSelectorScenario() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS radial menu when series selected through content selector"
				+ "</br>Validate that the new Accept/Reject toolbar is working on layout change.");
	
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Circle, 1);
		layout = new ViewerLayout(driver);
		circle = new CircleAnnotation(driver);
		cs = new ContentSelector(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName_Circle+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(circle.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1,2), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues((circle.acceptRejectToolbar.getRect().x+(circle.acceptRejectToolbar.getRect().width/2)), viewerPage.getViewPort(1).getRect().width/2, 1),"","Verifying the GSPS toolbar is center aligned");
			
		//change layout to 1x2
		layout.selectLayout(layout.twoByOneLayoutIcon);

		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES01", filePathGSPSCircle);
		cs.selectSeriesFromSeriesTab(2, firstSeriesDescription);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(firstSeriesDescription), "Verifying Checkbox appears next to newly displayed content series 1", "Verified Checkbox appears next to newly displayed content series 1");
		viewerPage.assertTrue(circle.getAllCircles(2).isEmpty(), "", "No circle present");
		viewerPage.assertFalse(circle.verifyPendingGSPSToolbarMenu(), "", "No tool bar present");
		
	}	

	@Test(groups ={"Chrome","IE11","Edge","DE512"})
	public void test09_DE512_TC2108_verifyGSPSRadialMenuDisplayedInLayoutChangeScenario() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS radial menu when series selected through content selector");

		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Circle, 1);
		layout = new ViewerLayout(driver);
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName_Circle+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(circle.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(circle.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1,2), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues((circle.acceptRejectToolbar.getRect().x+(circle.acceptRejectToolbar.getRect().width/2)), viewerPage.getViewPort(1).getRect().width/2, 1),"","Verifying the GSPS toolbar is center aligned");
		

		//change layout to 1x2
		layout.selectLayout(layout.twoByOneLayoutIcon);

		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES01", filePathGSPSCircle);
		cs.selectSeriesFromSeriesTab(2, firstSeriesDescription);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(firstSeriesDescription), "Verifying Checkbox appears next to newly displayed content series 1", "Verified Checkbox appears next to newly displayed content series 1");
		viewerPage.assertTrue(circle.getAllCircles(2).isEmpty(), "", "No circle present");
		viewerPage.assertFalse(circle.verifyPendingGSPSToolbarMenu(), "", "No tool bar present");
		viewerPage.assertEquals(circle.getNumberOfCanvasForLayout(), 2, "","Verifying the number of viewboxes");
		
		//change layout to 3x3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs.selectSeriesFromSeriesTab(3, firstSeriesDescription);
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(firstSeriesDescription), "Verifying Checkbox appears next to newly displayed content series 1", "Verified Checkbox appears next to newly displayed content series 1");	
		viewerPage.assertTrue(circle.getAllCircles(3).isEmpty(), "", "No circle present");
		viewerPage.assertFalse(circle.verifyPendingGSPSToolbarMenu(), "", "No tool bar present");
		viewerPage.assertEquals(circle.getNumberOfCanvasForLayout(), 9, "","Verifying the number of viewboxes");
		
	}	

	@Test(groups ={"Chrome","IE11","Edge","DE512"})
	public void test10_DE512_TC2109_verifyGSPSRadialMenuBrowser() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS radial menu when series selected through content selector");
	
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName_Circle+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.verifyPendingGSPSToolbarMenu(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");
		
		
		int startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		int endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
		

		//change layout to 3x3
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerPage.resizeBrowserWindow(700, 700);

		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.verifyRejectGSPSRadialMenu(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");

		viewerPage.maximizeWindow();
	}	

	//Verify GSPS radial menu while performing DICOM operations
	@Test(groups ={"Chrome","IE11","Edge","DE512"})
	public void test11_DE512_TC2110_verifyGSPSRadialMenuWithDICOMOperation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS radial menu when series selected through content selector");

		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Ellipse, 1);
		layout = new ViewerLayout(driver);
		ellipse = new EllipseAnnotation(driver);

		//Viewer renders in 1X1 layout. 
		int defaultCountFor1x1 = viewerPage.getNumberOfCanvasForLayout();
		int expectedCountFor1x1 =  layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT);
		viewerPage.waitForViewerpageToLoad();
		viewerPage.assertEquals(defaultCountFor1x1,expectedCountFor1x1, "Verifying layout", "Checkpoint : verifying 1x1 layout for GSPS ELLipse data");

		//Loading the patient on viewer and GSPS radial menu displays beside/above the GSPS object which is highlighted in that viewbox.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName_Ellipse+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(ellipse.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Verify first Ellipse annotation is current Active GSPS object", "The first point annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues((ellipse.acceptRejectToolbar.getRect().x+(ellipse.acceptRejectToolbar.getRect().width/2)), viewerPage.getViewPort(1).getRect().width/2, 1),"","Verifying the GSPS toolbar is center aligned");
		

		// Select PAN from radial menu 
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));

		//GSPS radial menu disappears when user clicks on anywhere other than GSPS object.  
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4]", "Verify GSPS radial menu  should not displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsPreviousIcn), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsRejectIcn), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsNextIcn), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");

		// Right click on GSPS object  

		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));

		//Verify GSPS radial menu should open
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5]", "Verify GSPS radial menu  should  displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(ellipse.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is  displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is  displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is  displayed on radial menu");

		// Start applying PAN to image
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, 100);

		//GSPS radial menu disappears when user clicks on anywhere other than GSPS object.  
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6]", "Verify GSPS radial menu  should not displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsPreviousIcn), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsRejectIcn), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsNextIcn), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");

		// Select zoom from radial menu
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));

		//GSPS radial menu disappears when user clicks on anywhere other than GSPS object.  
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4]", "Verify GSPS radial menu  should not displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsPreviousIcn), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsRejectIcn), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsNextIcn), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");

		// Right click on GSPS object  
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5]", "Verify GSPS radial menu  should  displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(ellipse.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is  displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is  displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is  displayed on radial menu");

		// Start applying PAN to image
		viewerPage.dragAndReleaseOnViewer(0, 0, 50, 50);

		//GSPS radial menu disappears when user clicks on anywhere other than GSPS object.  
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6]", "Verify GSPS radial menu  should not displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsPreviousIcn), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsRejectIcn), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsNextIcn), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");

		// Perform rotate/flip
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));

		//GSPS radial menu disappears when user clicks on anywhere other than GSPS object.  s
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7]", "Verify GSPS radial menu  should not displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsPreviousIcn), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsRejectIcn), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsNextIcn), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");
		// Right click on GSPS object  
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8]", "Verify GSPS radial menu  should  displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(ellipse.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is  displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is  displayed on radial menu");
		viewerPage.assertTrue(ellipse.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is  displayed on radial menu");

		// Start applying PAN to image
		viewerPage.dragAndReleaseOnViewer(0, 0, 50, 50);

		//GSPS radial menu disappears when user clicks on anywhere other than GSPS object.  
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9]", "Verify GSPS radial menu  should not displayed with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsPreviousIcn), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsRejectIcn), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertFalse(viewerPage.isElementPresent(ellipse.gspsNextIcn), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");

		int x1 = ellipse.getEllipses(1).get(0).getLocation().x;
		int y1 = ellipse.getEllipses(1).get(0).getLocation().y;
		float rx1 =Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.RX));
		float ry1 =Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.RY));
		float cy1 = Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.CY));
		
		int x2 = ellipse.getEllipses(1).get(1).getLocation().x;
		int y2 = ellipse.getEllipses(1).get(1).getLocation().y;
		
		float rx2 =Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.RX));
		float ry2 =Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.RY));
		float cy2 =Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.CY));
		orin = new ViewerOrientation(driver);
		int leftX = orin.getOrientationMarker(1,"L").getLocation().x;
		int leftY = orin.getOrientationMarker(1,"L").getLocation().y;
		
		int rightX = orin.getOrientationMarker(1,"R").getLocation().x;
		int rightY = orin.getOrientationMarker(1,"R").getLocation().y;
		
		// Image flipped horizontally Right Orientation Marker
		orin.flipSeries(orin.getLeftOrientationMarker(1));
		viewerPage.assertNotEquals(x1, ellipse.getEllipses(1).get(0).getLocation().x, "", "X coordinate is changed as series is flipped");
		viewerPage.assertEquals(y1, ellipse.getEllipses(1).get(0).getLocation().y, "", "Y is same as series is flipped horizontally");
		viewerPage.assertEquals(rx1, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.RX)), "", "Size is same");
		viewerPage.assertEquals(ry1, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.RY)), "", "Size is same");
		viewerPage.assertEquals(cy1, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.CY)), "", "center of ellipse is at same distance after flip vertically");
		
		viewerPage.assertNotEquals(x2, ellipse.getEllipses(1).get(1).getLocation().x, "", "X coordinate is changed as series is flipped");
		viewerPage.assertEquals(y2, ellipse.getEllipses(1).get(1).getLocation().y, "", "Y is same as series is flipped horizontally");
		viewerPage.assertEquals(rx2, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.RX)), "", "Size is same");
		viewerPage.assertEquals(ry2, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.RY)), "", "Size is same");
		viewerPage.assertEquals(cy2, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.CY)), "", "center of ellipse is at same distance after flip vertically");
		
		viewerPage.assertEquals(leftX, orin.getOrientationMarker(1,"R").getLocation().x, "", "Verifying the right postion of right marker is same as L marker");
		viewerPage.assertEquals(leftY, orin.getOrientationMarker(1,"L").getLocation().y, "", "Verifying the postion of Left marker is same as R marker");
		viewerPage.assertEquals(rightX,orin.getOrientationMarker(1,"L").getLocation().x, "", "Verifying the right postion of right marker is same as L marker");
		viewerPage.assertEquals(rightY,orin.getOrientationMarker(1,"R").getLocation().y, "", "Verifying the postion of Left marker is same as R marker");
	
		
		orin.flipSeries(orin.getLeftOrientationMarker(1));
		
		viewerPage.assertEquals(leftX, orin.getOrientationMarker(1,"L").getLocation().x, "", "Verifying the postion of L marker is same as previous position");
		viewerPage.assertEquals(leftY, orin.getOrientationMarker(1,"L").getLocation().y, "", "Verifying the postion of L marker is same as previous position");
		viewerPage.assertEquals(rightX,orin.getOrientationMarker(1,"R").getLocation().x, "", "Verifying the postion of R marker is same as previous position");
		viewerPage.assertEquals(rightY,orin.getOrientationMarker(1,"R").getLocation().y, "", "Verifying the postion of R marker is same as previous position");
		
		viewerPage.assertEquals(x1, ellipse.getEllipses(1).get(0).getLocation().x, "", "Verifying the first ellipse x position");
		viewerPage.assertEquals(y1, ellipse.getEllipses(1).get(0).getLocation().y, "", "Verifying the first ellipse y position");
		viewerPage.assertEquals(rx1, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.RX)), "", "verifying the radius x");
		viewerPage.assertEquals(ry1, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.RY)), "", "verifying the radius y");
		viewerPage.assertEquals(cy1, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(0), NSGenericConstants.CY)), "", "verifying the vertical position is same as it is flipped horizontally");
		
		viewerPage.assertEquals(x2, ellipse.getEllipses(1).get(1).getLocation().x, "", "Verifying the second ellipse x position");
		viewerPage.assertEquals(y2, ellipse.getEllipses(1).get(1).getLocation().y, "", "Verifying the second ellipse y position");
		viewerPage.assertEquals(rx2, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.RX)), "", "verifying the radius x");
		viewerPage.assertEquals(ry2, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.RY)), "", "verifying the radius y");
		viewerPage.assertEquals(cy2, Float.parseFloat(ellipse.getAttributeValue(ellipse.getEllipses(1).get(1), NSGenericConstants.CY)), "", "verifying the vertical position is same as it is flipped horizontally");
		


	}

	@Test(groups ={"Chrome","IE11","Edge","DE636"})
	public void test12_DE636_TC2258_verifyNavigationOfNewlyAddedAnnotation() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify newly added annotation on same slice can be navigated through smoothly");

		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
	
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		//verify viewer open to first slice in series having active GSPS data with GSPS accept/reject radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify viewer opens to first slice in series with GSPS result in first Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");	
		viewerPage.assertTrue(point.verifyPendingGSPSToolbarMenu(), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");

		//navigate to next active GSPS object
		ellipse.selectAcceptfromGSPSRadialMenu();

		//verify viewer open to first slice in series having active GSPS data with GSPS accept/reject radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify viewer move to next slice having active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify viewer move to next slice having active GSPS object", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//Draw a Circle on View box 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify a circle annotation is drawn on viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle annotation is drawn on Viewbox1", "Circle annotation is drawn on Viewbox1");

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 110,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify a ellipse annotation is drawn on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a ellipse annotation is drawn on viewbox1", "Ellipse annotation is drawn on Viewbox1");

		//click on next Button from  Accept/Reject Radial tool bar on current active GSPS object
		ellipse.selectNextfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));

		//verify circle annotation is selected and GSPS radial menu is centered above it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify above drawn circle annotation is current Active GSPS object");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify above drawn circle annotation is current Active GSPS object", "The circle annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");
		int startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		int endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
	

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();

		//verify ellipse annotation is selected and GSPS radial menu is centered above it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify above drawn ellipse annotation is current Active GSPS object");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify above drawn ellipse annotation is current Active GSPS object", "The ellipse annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");
		 startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		 endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
	

		//click on previous button to move to previous GSPS object
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify prevoius drawn GSPS object becomes active GSPS object");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify prevoius drawn circle annotation is current Active GSPS object", "The circle annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");
		startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		 endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
	
		//click on previous button to move to previous GSPS object
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify prevoius drawn GSPS object becomes active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify prevoius drawn point annotation is current Active GSPS object", "The prevoius drawn point annotation is current active GSPS object");	
		viewerPage.assertTrue(point.verifyPendingGSPSToolbarMenu(), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");
		startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		 endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
	
	}

	@Test(groups ={"Chrome","IE11","Edge","DE636","BVT"})
	public void test13_DE636_TC2259_verifyNavigationOfNewlyAddedAnnotationOnDifferentSlice() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify newly added annotation on different slice can be navigated through smoothly");

	
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);


		//verify viewer open to first slice in series having active GSPS data with GSPS accept/reject radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify viewer opens to first slice in series with GSPS result in first Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");	

		//scroll to next slice in series
		viewerPage.scrollDownToSliceUsingKeyboard(1);

		//Draw a Circle on View box 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify a circle annotation is drawn on viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle annotation is drawn on Viewbox1", "Circle annotation is drawn on Viewbox1");

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 110,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify a ellipse annotation is drawn on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a ellipse annotation is drawn on viewbox1", "Ellipse annotation is drawn on Viewbox1");

		//scroll up to previous slice in series
		viewerPage.scrollUpToSliceUsingKeyboard(1);

		//click on next Button from  Accept/Reject Radial tool bar on current active GSPS object
		ellipse.selectNextfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));

		//verify circle annotation is selected and GSPS radial menu is centered above it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify above drawn circle annotation is current Active GSPS object");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify above drawn circle annotation is current Active GSPS object", "The circle annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");
		int startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		int endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
	

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();

		//verify ellipse annotation is selected and GSPS radial menu is centered above it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify above drawn ellipse annotation is current Active GSPS object");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify above drawn ellipse annotation is current Active GSPS object", "The ellipse annotation is current active GSPS object");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");
		startDiff = (point.acceptRejectToolbar.getRect().x - viewerPage.getViewPort(1).getRect().x);
		endDiff =((viewerPage.getViewPort(1).getRect().x +viewerPage.getViewPort(1).getRect().width) -
				(point.acceptRejectToolbar.getRect().x +point.acceptRejectToolbar.getRect().width));
		
		viewerPage.assertTrue(viewerPage.verifyAccuracyOfValues(startDiff, endDiff,1),"","Verifying the GSPS toolbar is center aligned");
	
		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 8, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu(point.getHandlesOfPoint(1,2).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to previous active GSPS object
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify GSPS object on previous slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify GSPS object on previous slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

	}

	@Test(groups ={"Chrome","IE11","Edge","US950","DE636"})
	public void test14_DE636_TC2260_US950_TC4321_verifyNavigationOnDeletionOfAnnotation() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS objects navigation if any annotation from same slice is deleted"
				+ "<br> Verify that on deleting any annotation from A/R bar next annotation should highlighted with yellow shadow and annotation should not get bold /thick");

		
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);


		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		//verify viewer open to first slice in series having active GSPS data with GSPS accept/reject radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify viewer opens to first slice in series with GSPS result in first Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");	
		viewerPage.assertTrue(point.verifyPendingGSPSToolbarMenu(), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");


		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();

		//verify viewer open to first slice in series having active GSPS data with GSPS accept/reject radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify viewer move to next slice having active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify viewer move to next slice having active GSPS object", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//Draw a Circle on View box 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify a circle annotation is drawn on viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle annotation is drawn on Viewbox1", "Circle annotation is drawn on Viewbox1");

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 110,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify a ellipse annotation is drawn on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a ellipse annotation is drawn on viewbox1", "Ellipse annotation is drawn on Viewbox1");


		// Delete circle annotation drawn on view box
		circle.selectCircle(1, 1);
		
		circle.deleteSelectedCircle();
			
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify a circle annotation is deleted on viewbox");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),0, "Verify a circle annotation is deleted on Viewbox1", "Circle annotation is deleted on Viewbox1");

		//click on next Button from  Accept/Reject Radial tool bar on current active GSPS object
		//		viewerPage.selectPreviousfromGSPSRadialMenu(point.getLinesOfPoint(1,1).get(0));

		//verify ellipse annotation is selected and GSPS radial menu is centered above it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify above drawn ellipse annotation is current Active GSPS object");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify above drawn ellipse annotation is current Active GSPS object", "The ellipse annotation is current active GSPS object");
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");

		//click on previous button to move to previous GSPS object
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify prevoius drawn GSPS object becomes active GSPS object");

		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify prevoius drawn point annotation is current Active GSPS object", "The prevoius drawn point annotation is current active GSPS object");	
		viewerPage.assertTrue(point.verifyPendingGSPSToolbarMenu(), "Verify GSPS radial menu is present on viewbox", "The GSPS radial menu is present on viewbox");

	}

	@Test(groups ={"Chrome","IE11","Edge","DE682","BVT"})
	public void test15_DE636_TC2261_verifyNavigationOnDeletionOfAnnotationOnDifferentSlice() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS objects navigation if any annotation from different slice is deleted");

				helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);


		//verify viewer open to first slice in series having active GSPS data with GSPS accept/reject radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify viewer opens to first slice in series with GSPS result in first Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");	

		//scroll to next slice in series
		viewerPage.scrollDownToSliceUsingKeyboard(1);

		//Draw a Circle on View box 
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30,-100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify a circle annotation is drawn on viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle annotation is drawn on Viewbox1", "Circle annotation is drawn on Viewbox1");

		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 110,150);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify a ellipse annotation is drawn on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a ellipse annotation is drawn on viewbox1", "Ellipse annotation is drawn on Viewbox1");

		//Delete all the newly drawn slice on current slice
		ellipse.deleteAllAnnotation(1);
		viewerPage.scrollToImage(1, 2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify all the annotation is deleted on viewbox on current slice");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),0, "Verify a circle annotation is deleted on Viewbox1", "Circle annotation is deleted on Viewbox1");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(),0, "Verify a ellipse annotation is deleted on Viewbox1", "Ellipse annotation is deleted on Viewbox1");

		//scroll up to previous slice in series
		viewerPage.scrollToImage(1, 1);
		//click on next Button from  Accept/Reject Radial tool bar on current active GSPS object
		ellipse.selectNextfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify GSPS object on next slice is active now");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
//
//		//navigate to next active GSPS object
//		ellipse.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 8, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to next active GSPS object
		ellipse.selectNextfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify GSPS object on next slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify GSPS object on next slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//navigate to previous active GSPS object
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify GSPS object on previous slice is active now");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify GSPS object on previous slice is active now", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

	}

	@Test(groups ={"Chrome","IE11","Edge","DE682"})
	public void test16_DE682_TC2437_verifyLPixelDataSetGettingImportedProperly() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate that the Lpixel dataset has been displayed properly and no console error");
		
		patientPage.searchPatient(LPixel_PatientName, "", "", "");
		patientPage.waitForPatientPageToLoad();
		patientPage.clearConsoleLogs();
		if(patientPage.studyRows.size()==1)
			patientPage.clickOnPatientRow(2);
		
		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		
		textAnno = new TextAnnotation(driver);
		circle = new CircleAnnotation(driver);
		
		//verify study is loaded properly with all images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Study is getting loaded properly with all images");
		textAnno.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "", "Verifying the text annotation is displayed");
		textAnno.assertTrue(textAnno.verifyPendingGSPSToolbarMenu(), "", "Verifying the Pending GSPS Radial menu is displayed");
		textAnno.assertEquals(textAnno.getBadgeCountFromToolbar(1),7, "", "Verifying the 7 pending GSPS findings are displayed");
				
		//verify there is no console error on loading study on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify there is no console error");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Verify there is no console error on opening Lpixel study", "There is no console error logged at this point");

		//navigate back to study list page and select second study from list
		
		viewerPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnStudy(2);
		viewerPage.waitForViewerpageToLoad();
		
		//verify study is loaded properly with all images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Study is getting loaded properly with all images");
		textAnno.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "", "Verifying the text annotation is displayed");
		textAnno.assertTrue(textAnno.verifyPendingGSPSToolbarMenu(), "", "Verifying the Pending GSPS Radial menu is displayed");
		textAnno.assertEquals(textAnno.getBadgeCountFromToolbar(1),7, "", "Verifying the 7 pending GSPS findings are displayed"); 

		//verify there is no console error on loading study on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify there is no console error");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Verify there is no console error on opening Lpixel study", "There is no console error logged at this point");

		//navigate back to study list page and select third study from list
		viewerPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnStudy(2);
		viewerPage.waitForViewerpageToLoad();
	

		//verify study is loaded properly with all images
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Study is getting loaded properly with all images");
		textAnno.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "", "Verifying the text annotation is displayed");
		textAnno.assertTrue(textAnno.verifyPendingGSPSToolbarMenu(), "", "Verifying the Pending GSPS Radial menu is displayed");
		textAnno.assertEquals(textAnno.getBadgeCountFromToolbar(1),7, "", "Verifying the 7 pending GSPS findings are displayed"); 

		//verify there is no console error on loading study on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify there is no console error");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Verify there is no console error on opening Lpixel study", "There is no console error logged at this point");
	}

	//TC2295 - [Hardening] GSPS radial menu is displayed in first viewbox though annotation is present in another viewbox on re-render and on forward navigation too
	@Test(groups ={"Chrome","IE11","Edge","DE597"})
	public void test18_DE597_TC2295_verifyPositionOfGSPSRadialMenuOnReload() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Hardening] GSPS radial menu is displayed in first viewbox though annotation is present in another viewbox on re-render and on forward navigation too");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		lineWithUnit = new MeasurementWithUnit(driver);


		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(4, 60, 60, 100, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying presence of drawn annotation");
		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(4), "Verify linear measurement", "Linear measurement is present");

		// Navigate back to patient list page and reload the study
		viewerPage.browserBackWebPage();
		patientPage = new PatientListPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying user is on Single study page");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL),  "Verify that user is navigated back from viewer sceen", "User is on: "+patientPage.getCurrentPageURL());

		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		
		helper.browserBackAndReloadViewer(liver9PatientName, "", 1, 1);

		//Verifying the presence of drawn annotation with GSPS radial menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying annotation is highlighted and GSPS radial menu is within the same viewbox");

		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(1), "Verify linear measurement", "Linear measurement is present");
		viewerPage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(), "Verify GSPS radial menu presence", "GSPS radial menu is present");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify linear measurement is highlighted", "Linear measurement is highlighted");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying GSPS radial menu is not present in first viewbox and present on forth viewbox only");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify drawn Measurement annotation persist on study reload","A Single instance of Linear Measurement is present on Viewbox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "", "Verifying the linear measurement is active and accepted");
	
	}

	//TC2296 - [Hardening] GSPS radial menu is displayed in first viewbox though annotation is present in another viewbox on re-render and on forward navigation too
	@Test(groups ={"Chrome","IE11","Edge","DE597","DE1928","Positive"})
	public void test19_DE597_TC2296_DE1928_TC7792_verifyPositionOfGSPSRadialMenuOnMouseHover() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Hardening] GSPS radial menu is displayed in first viewbox though annotation is present in another viewbox on re-render and on forward navigation too. <br>"+
		"Verify the AR tool bar is displayed for dicom series.");

		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		lineWithUnit = new MeasurementWithUnit(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 90, 60);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying presence of drawn annotation");
		viewerPage.assertTrue(lineWithUnit.isLinearMeasurementPresent(1), "Verify linear measurement", "Linear measurement is present");


		//Verifying that the GSPS radial menu is present
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying presence of GSPS radial menu and highlighted measurement");

		viewerPage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(), "Verify GSPS radial menu presence", "GSPS radial menu is present");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify linear measurement is highlighted", "Linear measurement is highlighted");

		//Mouse hover on other VBs 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying presence of GSPS radial menu and highlighted measurement on mouse hover in other viewboxes");
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.assertFalse(lineWithUnit.verifyAcceptGSPSRadialMenu(), "Verify GSPS radial menu presence", "GSPS radial menu is present");
		viewerPage.assertFalse(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify linear measurement is highlighted", "Linear measurement is highlighted");

		//Mouse hover outside of NS window
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying presence of GSPS radial menu and highlighted measurement on mouse hover outside of NS window");
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		//		viewerPage.mouseMove(100,100);
		viewerPage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(), "Verify GSPS radial menu presence", "GSPS radial menu is present");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify linear measurement is highlighted", "Linear measurement is highlighted");

		//Perform mouse left click in any viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying GSPS radial menu and measurement after mouse click on other viewboxes");
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.click(viewerPage.getViewPort(3));
		viewerPage.assertFalse(lineWithUnit.verifyAcceptGSPSRadialMenu(), "Verify GSPS radial menu presence", "GSPS radial menu is not present");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Verify linear measurement is not highlighted", "Linear measurement is not highlighted");


	}

	//TC2358 - 	Cannot load GSPS when referencedFrameNumber is null
	@Test(groups ={"Chrome","IE11","Edge","DE677"})
	public void test20_DE677_TC2358_verifyGSPSSeriesHavingNullReferencedFrameNumber() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Cannot load GSPS when referencedFrameNumber is null");

	
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(NullFrameReferenceID_PatientName, 1);
		
		
		circle = new CircleAnnotation(driver);
		textAnno = new TextAnnotation(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+NullFrameReferenceID_PatientName+" in viewer" );

		//Verifying the presence of GSPS data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying GSPS loaded series in the viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify drawn circle annotation persist on study reload", "Circle annotation is present on Viewbox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "", "Verifying the circle is active and accepted");
		viewerPage.assertTrue(textAnno.verifyTextAnnotationIsCurrentPendingInactive(1, 1, false), "", "Verifying the circle is active and accepted");

		//Verifying no console error should display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying User shouldn't see any console errors like 'TypeError: Cannot read property '0' of null' ");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(ViewerPageConstants.REFERENCEFRAMENUM_ERROR), "Verify no console error should display", "Console error is not present");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE1802","Positive"})
	public void test21_DE1802_TC7383_verifyGSPSNavigationAndSync() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS navigation works in sync");

		helper= new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
	
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		
		point.scrollToImage(1, 56);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 30, 30, 70, 70);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		
		point.scrollToImage(1, 70);
		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);
		String myText ="TextAnnotation_FirstViewbox";
		textAnno.drawText(1, 10, 10, myText);		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		
		point.closingConflictMsg();
		
		cs = new ContentSelector(driver);
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		cs.openAndCloseSeriesTab(false);
		
		cs.click(cs.getViewPort(1));
		point.navigateGSPSForwardUsingKeyboard();
		
			
		for(int i =point.getAllGSPSObjects(1,false).size(),j=1 ; i>=1;i--,j++) {
			point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, i) ,"Checkpoint[1.1/15]", "Verifying the point is accepted in viewbox1");
			point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[1.2/15]", "Verifying that GSPS tool bar is present");
			point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, j) ,"Checkpoint[1.3/15]", "Verifying the point is accepted in viewbox2");
			point.assertFalse(point.isAcceptRejectToolBarPresent(2), "Checkpoint[1.4/15]", "Verifying that GSPS tool bar is absent");
			point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), point.getCurrentScrollPositionOfViewbox(2), "Checkpoint[1.5/15]", "Verifying that both viewboxes are in sync");
			point.navigateGSPSForwardUsingKeyboard();
			
		}

		point.assertEquals(point.getAllGSPSObjects(1,false).size(),point.getAllGSPSObjects(2, false).size(),"Checkpoint[2/15]","Verifying both the viewboxes are having same objects");

		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[3/15]", "Verifying on navigating the circle is highlighted");
		point.assertTrue(point.isAcceptRejectToolBarPresent(1), "Checkpoint[4/15]", "Verifying that GSPS tool bar is present");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[5/15]", "verifying that circle is also highlighted in another viewbox");
		point.assertFalse(point.isAcceptRejectToolBarPresent(2), "Checkpoint[6/15]", "Verifying that GSPS tool bar is absent");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), point.getCurrentScrollPositionOfViewbox(2), "Checkpoint[7/15]", "Verifying that both viewboxes are in sync");
		
		point.navigateGSPSForwardUsingKeyboard();
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1) ,"Checkpoint[8/15]", "Verifying the point is accepted in viewbox1");
		point.assertTrue(point.isAcceptRejectToolBarPresent(1), "Checkpoint[9/15]", "Verifying that GSPS tool bar is present");
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1) ,"Checkpoint[10/15]", "Verifying the point is accepted in viewbox2");
		point.assertFalse(point.isAcceptRejectToolBarPresent(2), "Checkpoint[11/15]", "Verifying that GSPS tool bar is absent");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), point.getCurrentScrollPositionOfViewbox(2), "Checkpoint[12/15]", "Verifying that both viewboxes are in sync");
		
		point.navigateGSPSForwardUsingKeyboard();
		
		point.assertEquals(point.getAllGSPSObjects(1,false).size(),point.getAllGSPSObjects(2, false).size(),"Checkpoint[13/15]","Verifying both the viewboxes are having same objects");

		point.selectDeletefromGSPSRadialMenu();
		
		point.assertEquals(point.getAllGSPSObjects(1,false).size(),point.getAllGSPSObjects(2, false).size(),"Checkpoint[14/15]","Verifying both the viewboxes are having same objects after deleting one object");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), point.getCurrentScrollPositionOfViewbox(2), "Checkpoint[15/15]", "Verifying that both viewboxes are in sync");
		
	}

}


