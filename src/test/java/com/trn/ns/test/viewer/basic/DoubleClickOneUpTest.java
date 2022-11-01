package com.trn.ns.test.viewer.basic;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.ExtentTest;
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

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DoubleClickOneUpTest extends TestBase {

	private PatientListPage patientPage;
	
	private LoginPage loginPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private CircleAnnotation circle;
	private PointAnnotation point;
	private PolyLineAnnotation poly;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private ViewBoxToolPanel viewBoxPanel;

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);    

	String filePath2 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
	String subject_60PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);    

	String filePath3=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath3);
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException, SAXException, IOException, ParserConfigurationException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

	}     
	//1. Zoom to fit CW Verification
	@Test(groups ={"Edge","Chrome","US253"})
	public void test01_US253_TC704_verifydoubleClickZoomToFit() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Double Click to 1-Up-Double click");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int preZoom = viewBoxPanel.getZoomValue(1);;

		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 100,50);

		int beforeZoom = viewBoxPanel.getZoomValue(1);

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad();  

		int afterZoom = viewBoxPanel.getZoomValue(1);    

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(beforeZoom, afterZoom, "Verify zoom % is getting retained", "verified");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad();       

		ViewBoxToolPanel preset = new ViewBoxToolPanel(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(afterZoom, viewBoxPanel.getZoomValue(1), "Verify zoom % is getting retained", "verified");

		preset.chooseZoomToFit(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(preZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertNotEquals(preZoom,viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertEquals(preZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");





	}
	//2. Linear distance Verification

	@Test(groups ={"Edge","Chrome","firefox","multimonitor","US253"})
	public void test02_US253_TC706_verifyLinearDistanceOnDoubleClick() throws InterruptedException
	{
		String testCaseName = "US253_TC706";
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Double Click to 1-Up-Double click-Multiple windows-Annotations created by the user in the session are not lost by this action");

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Drawing the measurement on coordinates (0,0,100,0)" );
		//				viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.getViewPort(1));
		lineWithUnit.drawLine(1,0,0, 100, 0);

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify the measurement drawn @ coordinates (0,0,100,0)", testCaseName+"_checkpoint_1");


		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify the measurement drawn @ coordinates (0,0,100,0)", testCaseName+"_checkpoint_2");

	}

	//3. Window Levelling Verification

	@Test(groups ={"Edge","Chrome","US253"})
	public void test03_US253_TC707_verifyWWWlOnDoubleClickUp() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Double Click to 1-Up-Double click-Multiple windows-W/L as applied to the image is not lost by this action");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		int beforeWWValue =viewerpage.getValueOfWindowWidth(1);
		int beforeWCValue =viewerpage.getValueOfWindowCenter(1);


		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying WW/WL is enabled","WW/WL is enabled");
		viewerpage.dragAndReleaseOnViewer(25, 50, 50, 100);

		int afterWWValue =viewerpage.getValueOfWindowWidth(1) ;
		int afterWCValue =viewerpage.getValueOfWindowCenter(1);


		viewerpage.assertNotEquals(beforeWWValue,afterWWValue, "Verifying WW functionality", "WW is working fine");
		viewerpage.assertNotEquals(beforeWCValue,afterWCValue, "Verifying WC functionality", "WC is working fine");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(afterWWValue,viewerpage.getValueOfWindowWidth(1), "Verifying WW functionality", "WW is working fine");
		viewerpage.assertEquals(afterWCValue,viewerpage.getValueOfWindowCenter(1), "Verifying WC functionality", "WC is working fine");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(afterWWValue,viewerpage.getValueOfWindowWidth(1), "Verifying WW functionality", "WW is working fine");
		viewerpage.assertEquals(afterWCValue,viewerpage.getValueOfWindowCenter(1), "Verifying WC functionality", "WC is working fine");

	}
	//4. Zoom Verification

	@Test(groups ={"Edge","Chrome","US253"})
	public void test04_US253_TC708_verifyZoomOnDoubleClickUp() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Double Click to 1-Up-Double click-Multiple windows-Zoom is kept when Double Click 1-up/down is applied");
		String testcaseName = "US253_TC708";
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForViewerpageToLoad();      

		performAndverifyZoom(viewerpage.getViewPort(1), 1,testcaseName, "For Non Dicom");
		viewerpage.doubleClickOnViewbox(1);

	}

	//5. Pan Synchronization Verification

	@Test(groups ={"Edge","Chrome","US253"})
	public void test05_US253_TC777_verifyPanWithDoubleClick() throws InterruptedException
	{

		String testcaseName = "US253_TC777";

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Double Click to 1-Up-Double click-Multiple windows-Pan is kept when Double Click 1-up/down is applied");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(2));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),0, 0, 300, 0);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),300, 0, -600, 0);
		//viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),-300, 0, 300, -300);

		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verifying images should PAN towards up.","Checkpoint_PAN_BeforeDoubleClick"+ "_" +testcaseName+ "_1");
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verifying images should retain PAN on double click","Checkpoint_PAN_onDoubleClick"+ "_" +testcaseName+ "_1");
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verifying images should retain PAN on restore of double click","Checkpoint_PAN_afterRestoreDoubleClick"+ "_" +testcaseName+ "_2");



	}

	//6. Scroll Verification

	@Test(groups ={"Edge","Chrome","firefox","DE271","Sanity"})
	public void test06_DE271_TC1233_verifyScrollOnDoubleClickUp() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Slice position changes after double-click one-up");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		String beforeScrollResult = viewerpage.getCurrentScrollPosition(1);

		viewerpage.mouseWheelScrollInViewer(1, "down",2);

		viewerpage.waitForViewerpageToLoad();

		String afterScrollResult = viewerpage.getCurrentScrollPosition(1);

		viewerpage.assertNotEquals(beforeScrollResult, afterScrollResult, "Verifying scroll", "scroll is happening");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Perform Double click on viewbox");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(afterScrollResult,viewerpage.getCurrentScrollPosition(1), "Verifying scroll", "scroll is getting retained");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(afterScrollResult,viewerpage.getCurrentScrollPosition(1), "Verifying scroll", "scroll is getting retained");

		viewerpage.doubleClickOnViewbox(2);

		viewerpage.mouseWheelScrollInViewer(2, "down", 2);

		beforeScrollResult = viewerpage.getCurrentScrollPosition(2);

		viewerpage.doubleClickOnViewbox(2);
		viewerpage.assertEquals(beforeScrollResult,viewerpage.getCurrentScrollPosition(2), "Verifying scroll in viewbox 2", "scroll is getting retained");


	}
	//7. Zoom to Fit PW

	@Test(groups ={"Edge","Chrome","firefox","DE246"})
	public void test07_DE246_TC1164_verifyZoomToFitOnDoubleClick() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Zoom to Fit is not honored by Double Click-1 up");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);
		ViewBoxToolPanel preset = new ViewBoxToolPanel(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int beforeZoom = viewBoxPanel.getZoomValue(1);

		preset.changeZoomNumber(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);

		viewerpage.assertEquals(viewBoxPanel.getZoomValue(1), 100, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		preset.chooseZoomToFit(1);

		int afterZoom = viewBoxPanel.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(beforeZoom, afterZoom, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad(1);

		viewerpage.assertNotEquals(beforeZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertEquals(beforeZoom, afterZoom, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		layout.selectLayout(layout.twoByOneLayoutIcon)           ;

		viewerpage.assertNotEquals(beforeZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		beforeZoom = viewBoxPanel.getZoomValue(2);

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad(1);

		viewerpage.assertEquals(beforeZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForViewerpageToLoad(1);

	}
	//8. Zoom to Fit PW

	@Test(groups ={"Edge","Chrome","firefox","DE246"})
	public void test08_DE246_TC1173_verifyZoomOverlayDoubleClickUp() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Zoom to Fit is not honored by Double Click-1 up-Edge case");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);
     
		ViewBoxToolPanel preset = new ViewBoxToolPanel(driver);

		viewBoxPanel=new ViewBoxToolPanel(driver);
		int preZoom = viewBoxPanel.getZoomValue(1);

		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 100,50);

		int beforeZoom = viewBoxPanel.getZoomValue(1);

		viewerpage.doubleClickOnViewbox(1);

		int afterZoom = viewBoxPanel.getZoomValue(1);    

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(beforeZoom, afterZoom, "Verify zoom % is getting retained", "verified");

		viewerpage.doubleClickOnViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(afterZoom, viewBoxPanel.getZoomValue(1), "Verify zoom % is getting retained", "verified");

		preset.chooseZoomToFit(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
		viewerpage.assertEquals(preZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertNotEquals(preZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");

		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(preZoom, viewBoxPanel.getZoomValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");


	}

	public void performAndverifyZoom(WebElement element, int zoomLevelViewboxNumber, String testcaseName, String imageType) throws InterruptedException{

		viewBoxPanel=new ViewBoxToolPanel(driver);
		int beforeZoom = viewBoxPanel.getZoomValue(zoomLevelViewboxNumber);
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(element, 0, 0, -10, 10);
		int afterZoom = viewBoxPanel.getZoomValue(zoomLevelViewboxNumber);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify images should Zoom.."+ "_" +testcaseName+ "_" +imageType);
		viewerpage.assertFalse(beforeZoom < afterZoom, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
		viewerpage.doubleClickOnViewbox(1);
		int afterZoom1 = viewBoxPanel.getZoomValue(zoomLevelViewboxNumber);
		viewerpage.assertFalse(beforeZoom == afterZoom1, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertFalse(beforeZoom == afterZoom1, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "US889", "Positive","Sanity"})
	public void test09_US889_TC3423_TC3424_TC3425_TC3426_TC3427_TC3428_TC3429_TC3554_TC3555_TC3556_TC3557_TC3558_TC3559_TC3560_TC3561_verifyDoubleClickforAllAnnotations() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify double click scenario for all annotations");
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(PatientName, 1);

		SimpleLine line = new SimpleLine(driver);

		//Selecting line from radial menu
		line.selectLineFromQuickToolbar(1);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[1/15]");
		line.drawLine(1, 10, 10, 100, 100);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[2/15]");
		line.deleteAllAnnotation(1);

		CircleAnnotation circle = new CircleAnnotation(driver);
		//Selecting Circle from radial menu
		circle.selectCircleFromQuickToolbar(1);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[3/15]");
		circle.drawCircle(1, 20, 20, 60, 60);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[4/15]");
		circle.deleteAllAnnotation(1);

		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		//Selecting Ellipse from radial menu
		ellipse.selectEllipseFromQuickToolbar(1);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[5/15]");
		ellipse.drawEllipse(1, 20, 30, 50, 80);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[6/15");
		ellipse.deleteAllAnnotation(1);

		PointAnnotation point = new PointAnnotation(driver);
		//Selecting Point from radial menu
		point.selectPointFromQuickToolbar(1);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[7/15]");
		point.drawPointAnnotationMarkerOnViewbox(1, 15, 15);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[8/15]");
		point.deleteAllAnnotation(1);


		TextAnnotation textAnn = new TextAnnotation(driver);
		//Selecting TextArrow from radial menu
		textAnn.selectTextArrowFromQuickToolbar(1);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[12/15]");
		textAnn.drawText(1, 20, 50, "Hello");
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[13/15]");
		textAnn.deleteAllAnnotation(1);

		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		//Selecting linear distance from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[14/15]");
		lineWithUnit.drawLine(1, 110, 100, 200, 210);
		//Performing oneup and verifying layout is 1x1
		performingAndVerifyingOneUpScenario("Checkpoint[15/15]");
		lineWithUnit.deleteAllAnnotation(1);			
	}

	//DE1329: Console Error : "Cannot read property 'render' of null" observed in console when user perform double click in viewbox after drawing ellipse
    @Test(groups ={"Chrome","IE11","Edge","DE1329","positive"})
	public void test10_DE1329_TC5521_TC5522_verifyConsoleErrorWhenDoubleClickInsideAndOutsideOfDrawnAnnotation() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify position of Ellipse annotation remains same on layout change <br>"+
		"Verify that console error Cannot read property render of null should not get display when user performs double click outside any annotation");
        
		//Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ah4PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);
		ellipse.closingConflictMsg();
		ellipse.waitForRespectedViewboxToLoad(3);
		ellipse.waitForRespectedViewboxToLoad(4);
		circle=new CircleAnnotation(driver);
		point=new PointAnnotation(driver);
		poly=new PolyLineAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		ellipse.clearConsoleLogs();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Loading patient "+ ah4PatientName +" on viewer page");
		//Draw a Ellipse on Viewer page.
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 20, 20,250,150);	
		ellipse.doubleClick(ellipse.getAllEllipses(1).get(0));
		ellipse.assertFalse(ellipse.isConsoleErrorPresent(), "Checkpoint[1/8]", "Verified that no console error when double click at the centre of ellipse");
		ellipse.doubleClick(ellipse.getViewPort(1));
		ellipse.assertFalse(ellipse.isConsoleErrorPresent(), "Checkpoint[2/8]", "Verified that no console error when double click at the outside of ellipse");

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 80, 80);
		circle.doubleClick(circle.getAllCircles(1).get(0));
		circle.assertFalse(circle.isConsoleErrorPresent(), "Checkpoint[3/8]", "Verified that no console error when double click at the centre of circle");
		circle.assertEquals(circle.getNumberOfCanvasForLayout(), 4, "Checkpoint[4/8]", "Verified that layout is 2*2 when double click at the centre of circle");
		circle.doubleClick(circle.getViewPort(1));
		circle.assertFalse(circle.isConsoleErrorPresent(), "Checkpoint[5/8]", "Verified that no console error when double click at the outside of circle");
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 60, 60);
		point.doubleClick(point.getViewPort(1));
		point.assertFalse(point.isConsoleErrorPresent(), "Checkpoint[6/8]", "Verified that no console error when double click at the outside of point");
		
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		poly.doubleClick(poly.getViewPort(1));
		poly.assertFalse(poly.isConsoleErrorPresent(), "Checkpoint[7/8]","Verified that no console error when double click at the outside of polyline");
		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,50, 0, 150, 0);
		lineWithUnit.doubleClick(lineWithUnit.getViewPort(1));
		lineWithUnit.assertFalse(lineWithUnit.isConsoleErrorPresent(), "Checkpoint[8/8]", "Verified that no console error when double click at the outside of linear measurement");
		
	}
	
	public void performingAndVerifyingOneUpScenario(String Checkpoint) throws InterruptedException{
		viewerpage.doubleClickOnViewbox(1);
		//verifying layout is 1x1
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),1, Checkpoint, "verifying layout is 1x1");
		viewerpage.waitForViewerpageToLoad(1);
		//Changing to default layout
		viewerpage.doubleClickOnViewbox(1);	
	}




}
