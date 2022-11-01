package com.trn.ns.test.viewer.basic;

import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;



@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ExternalCommandTest extends TestBase  {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private HelperClass helper;
	private ViewBoxToolPanel viewBoxPanel;
	
	
	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	private PointAnnotation point;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;
	private SimpleLine line;
	private PolyLineAnnotation polyline;
	private PolyLineAnnotation poly;
	private TextAnnotation textAnn;
	private ContentSelector cs;
	private ViewBoxToolPanel preset;
	private ViewerLayout layout;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test03_US476_TC1919_VerifyRadialMenuCloseOnOpeningOtherMenu() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Radial Menu close after opening other menu");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		cs = new ContentSelector(driver);
		layout = new ViewerLayout(driver);
		//verify Radial menu close on North star Menu
		viewerPage.openQuickToolbar(1);
		layout.openLayoutContainer();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify Radial Menu closes on opening Northstar logo");
		viewerPage.assertFalse(viewerPage.verifyAllIconsPresenceInQuickToolbar(),"Verify Radial Menu closes on opening Northstar logo","Radial menu is not present");

		
		//verify Radial menu close on opening Zoom overlay
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		preset = new ViewBoxToolPanel(driver);
		preset.openOrCloseViewBoxToolPanel(1, NSGenericConstants.OPEN);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Radial Menu closes on opening overlay menu");
		viewerPage.assertFalse(viewerPage.verifyAllIconsPresenceInQuickToolbar(),"Verify Radial Menu closes on opening overlay menu","Radial menu is not present");

	
		//verify Radial menu close on opening content selector
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		cs.openAndCloseSeriesTab(true);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify Radial Menu closes on opening content selector");
		viewerPage.assertFalse(viewerPage.verifyAllIconsPresenceInQuickToolbar(),"Verify Radial Menu closes on opening content selector","Radial menu is not present");

		
	}


	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test08_US476_TC1923_VerifyMenuAreClosedOnLayoutChange() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that menu are getting closed on layout change");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		
		ViewerLayout layout = new ViewerLayout(driver);

		//Select 1X2 layout from layout grid on viewbox1
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerPage.waitForAllImagesToLoad();
		//Verify all menu closes after layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify layout selector menu closes after layout change" );
		viewerPage.assertFalse(layout.layoutContainer.isDisplayed(), "Verifying layout selection menu closes on layout change", "Layout selection menu is not present");

		//Open a Context menu and double click on One-Up
		viewerPage.openQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.doubleClickJs(viewerPage.getViewPort(2));

		//Verify Context Menu closes on One-Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Context Menu closes on Double Click One-Up" );
		viewerPage.assertFalse(viewerPage.quickToolbarMenu.isDisplayed(), "Verify Context Menu closes on GSPS Navigation using Keyboard shortcut", "Context menu is not present");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test09_US476_TC1924_VerifyMenuAreClosedOnPerformingActionOnAnnotation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that menu are getting closed on performing any operation on Annotation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		circle = new CircleAnnotation(driver);
		point= new PointAnnotation(driver);

		//draw a circle annotation on viewbox1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -100, 100,100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify that circle annotation is drawn on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1, "Verify a single circle is drawn on Viewbox", "No of Circle on Viewbox1 : "+circle.getAllCircles(1).size());

		//open a tool box and select the above drawn annotation
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		circle.selectCircleWithClick(1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify Tool box Menu closes on selecting annotation" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box Menu closes on selecting annotation", "Context menu is not present");

		//draw a point annotation and select Tool box
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-150,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify that Point annotation is drawn on DICOM");
		viewerPage.assertTrue(point.isPointPresent(1,1),"verifying the point#1", "point is present");

		//open a tool box
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));

		//Select the Zoom from active overlay
		viewBoxPanel=new ViewBoxToolPanel(driver);
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify Tool box Menu closes on selecting annotation" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box Menu closes on selecting annotation", "Tool box menu is not present");

		//press left arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		preset = new ViewBoxToolPanel(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify Text overlay Menu close on using GSPS focus finding");
		viewerPage.assertFalse(preset.isElementPresent(preset.toolBox),"Verify Text overlay Menu close on pressing right arrow key","Text overlay menu is not present");

	
		//open a radial menu and click on orientation marker
		ViewerOrientation orientation = new ViewerOrientation(driver);
		viewerPage.mouseHover(viewerPage.getViewPort(1));		
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		orientation.flipSeries(orientation.getTopOrientationMarker(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify Radial Menu closes on clicking orientation marker");
		viewerPage.assertFalse(viewerPage.verifyAllIconsPresenceInQuickToolbar(),"Verify Radial Menu closes on clicking orientation marker","Radial menu is not present");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test10_US476_TC1925_VerifyMenuAreGettingClosedOnPerformingScrollOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Context Menu are getting Closed on Performing Scroll operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);

		//Open a Context Menu and Perform scroll using Mouse drag
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));

		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);

		//Performing forward scroll 
		viewerPage.dragAndReleaseOnViewerWithClick(1, 0, 0, 0, 20);
		int forwardScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify Scroll down on Viewbox1" );
		viewerPage.assertTrue(currentScrollPosition<forwardScrollPosition, "Verifying forward scroll in viewbox1", "Verified forward scroll is working fine in viewbox1");

		//Verify Context Menu closes on scroll using Mouse drag
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify Context Menu closes on scroll down using Mouse drag" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Context Menu closes on scroll down using Mouse drag", "Context menu is not present");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);

		//Open a Context Menu and Perform scroll using Mouse Wheel
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));

		//Performing down scroll using Mouse Wheel
		viewerPage.mouseWheelScrollInViewer(1, "up", 4);
		forwardScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify Scroll Up using Mouse wheel on Viewbox1" );
		viewerPage.assertTrue(currentScrollPosition>forwardScrollPosition, "Verifying scroll up in viewbox1 using Mouse wheel", "Verified scroll up is working fine in viewbox1");

		//Verify Context Menu closes on scroll using Mouse wheel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify Context Menu closes on scroll down using Mouse Wheel" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Context Menu closes on scroll down using Mouse wheel", "Context menu is not present");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);

		//Open a Context Menu and perform scroll using Keyboard shortcut
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));

		//scroll down using Arrow key
		viewerPage.scrollDownToSliceUsingKeyboard(3);
		forwardScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify cine is getting played on Viewbox1" );
		viewerPage.assertNotEquals(currentScrollPosition,forwardScrollPosition, "Verifying scroll down using keyboard shortcut", "Verified Scroll down using Keyboard shorcut");

		//Verify Context Menu closes on scroll using keyboard
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify Context Menu on scroll using Keyboard shorcut" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Context Menu closes on scroll using Keyboard shorcut", "Context menu is not present");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);

		//Open a Context Menu and perform GSPS slice scroll using Page Up key
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));

		//Press Page Up  to move to previous slice having active GSPS 
		point.scrollUpGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify active viewbox changes to previous slice having GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify active viewbox changes to previous slice having GSPS object", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//Verify Context Menu closes on GSPS scroll using keyboard
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify Context Menu closes on GSPS Navigation using Keyboard shortcut" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Context Menu closes on GSPS Navigation using Keyboard shortcut", "Context menu is not present");

		//Open a Context Menu and perform GSPS slice scroll using Page Down key
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));

		//Press Page Up  to move to previous slice having active GSPS 
		point.scrollDownGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify active viewbox changes to next slice having GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 8, "Verify active viewbox changes to next slice having GSPS object", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));

		//Verify Context Menu closes on GSPS scroll using keyboard
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify Context Menu closes on GSPS Navigation using Keyboard shortcut");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Context Menu closes on GSPS Navigation using Keyboard shortcut", "Context menu is not present");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test11_US476_TC1928_VerifyMenuAreGettingClosedOnPerformingMeasurement() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Menu are getting closed on performing measurement operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		layout = new ViewerLayout(driver);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		line = new SimpleLine(driver);


		//select a line annotation 
		line.selectLineFromQuickToolbar(1);

		//Open a Tool box and draw a line annotation on view box
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		line.drawLine(1, -50, -20, 80, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Tool box closes on drawing a line annotation");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box closes on drawing a line annotation", "Tool box is not present");

		//select point annotation from radial menu
		point.selectPointFromQuickToolbar(1);

		//open a North Star menu and draw a point annotation
		layout.click(layout.gridIcon);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 30);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Northstar Menu close on drawing a point annotation");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on drawing a point annotation","NorthStar menu is not present");

		//select point annotation from radial menu

		circle.selectCircleFromQuickToolbar(1);

		//Open a Tool box and draw a circle
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		circle.drawCircle(1, 200, -50, -100,-150);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Tool box Menu closes on drawing a circle annotation");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar),"Verify Tool box Menu closes on drawing a circle annotation","Tool box menu is not present");

		//select ellipse annotation from radial menu
		ellipse.selectEllipseFromQuickToolbar(1);

		//open a North Star menu and draw a ellipse annotation
		viewerPage.click(layout.gridIcon);;
		ellipse.drawEllipse(1, -200, -50, -100,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify Northstar Menu close on drawing a ellipse annotation");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on drawing a ellipse annotation","NorthStar menu is not present");

		//open GSPS radial menu 
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//open a North Star menu and press right arrow for GSPS navigation
		viewerPage.click(layout.gridIcon);
		ellipse.navigateGSPSForwardUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Northstar Menu close on GSPS forward navigation");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on GSPS forward navigation","NorthStar menu is not present");

		//open a North Star menu and press Left arrow for GSPS navigation
		viewerPage.click(layout.gridIcon);
		ellipse.navigateGSPSBackUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify Northstar Menu close on GSPS forward navigation");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on GSPS forward navigation","NorthStar menu is not present");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test12_US476_TC1929_VerifyMenuAreGettingClosedOnNavigatingToOtherPage() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Menu are getting Closed on navigating to other page");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//Open a Tool box and navigate back to single study page
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Tool box is present on viewbox1" );
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box is present on viewbox1", "Tool box is present");

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName,1,1);
		
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Tool box closes on navigating to Single styudy page" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box closes on navigating to Single styudy page", "Tool box is not present");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test13_US476_TC1931_VerifyMenuAreGettingClosedOnApplyingViewerOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Menu are getting closed on applying viewer operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		layout = new ViewerLayout(driver);

		cs = new ContentSelector(driver);

		//Open a Tool box and press space bar to toggle on synchronization
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Tool box is present on viewbox1" );
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box is present on viewbox1", "Tool box is present");

		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Tool box closes on pressing space bar" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box closes on pressing space bar", "Tool box is not present");

		//open a North Star menu and press W to enable WL operation
		viewerPage.click(layout.gridIcon);
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Northstar Menu close on pressing W to enable WL operation");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on pressing W to enable WL operation","NorthStar menu is not present");
/*
		//Open a Content selector and press down arrow key to change slice position
		cs.openAndCloseSeriesTab(true);
		viewerPage.mouseWheelScrollInViewer(1, "down", 2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify Content Selector Menu closes on pressing Arrow key to change slice position");
		viewerPage.assertFalse(cs.contentcontainer.isDisplayed(),"Verify Content Selector Menu closes on pressing Arrow key to change slice position","Content Selector menu is not present");
*/
		//scroll up to first image in series
		viewerPage.scrollUpToSliceUsingKeyboard(2);

		//Open a Tool box and hover to second view box
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Tool box do not close on changing active viewbox" );
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box is present on viewbox1", "Tool box is present");

		point = new PointAnnotation(driver);
		//Open GSPS radial menu close and hover to second view box
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		point.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify GSPS Radial Menu close on changing active viewbox");
		viewerPage.assertFalse(point.verifyPendingGSPSToolbarMenu(),"Verify GSPS Radial Menu close on changing active viewbox","GSPS Radial Menu is not present");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test14_US476_TC1934_VerifyMenuAreGettingClosedAfterPerformingExternalCommand() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Menu are getting closed after performing external command");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		layout = new ViewerLayout(driver);
		
		//Open a Tool box and press ALT and TAB to switch to other Window application
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify Tool box is present on viewbox1" );
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box is present on viewbox1", "Tool box is present");

		viewerPage.pressTaskSwitcher();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Tool box closes on pressing ALT and TAB together" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box closes on pressing ALT and TAB together", "Tool box is not present");

		//open a North star menu and press TAB key
		viewerPage.click(layout.gridIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify Northstar Menu open on clicking logo");
		viewerPage.assertTrue(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu is present on Viewbox","NorthStar menu is present");
		viewerPage.pressTabKey();
		viewerPage.pressTabKey();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Northstar Menu close on pressing TAB key");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on pressing TAB key","NorthStar menu is not present");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test15_US476_TC1988_VerifyMenuAreGettingClosedAfterPerformingEmptyCommand() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Menu are getting closed after performing empty command");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//Open a Tool box and perform left click
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Tool box is present on viewbox1" );
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box is present on viewbox1", "Tool box is present");

		viewerPage.click(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Tool box close on performing empty command" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box close on performing empty command", "Tool box is not present");


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test16_US476_TC1932_VerifyMenuAreGettingClosedAfterPerformingRenderingOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Menu are getting closed after performing rendering operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		preset = new ViewBoxToolPanel(driver);
		layout = new ViewerLayout(driver);
		//select WL icon from radial menu
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));

		//Open a Tool box and perform window leveling
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 20, 20, 90, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify Tool box close on performing window leveling" );
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.quickToolbar), "Verify Tool box close on performing window leveling", "Tool box is not present");

		//select Zoom icon from radial menu
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));

		//open a North star menu and perform Zoom 
		viewerPage.click(layout.gridIcon);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 20, 20, 90, 90);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Northstar Menu close on performing Zoom operation");
		viewerPage.assertFalse(viewerPage.isElementPresent(layout.gridLayoutBox),"Verify Northstar Menu close on performing Zoom","NorthStar menu is not present");

		//select pan from context menu
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		//open a text overlay menu and perform PAN operation
		preset.openOrCloseViewBoxToolPanel(1, NSGenericConstants.OPEN);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), -20, -20, -90, -90);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Text overlay Menu closes on performing PAN operation");
		viewerPage.assertFalse(preset.isElementPresent(preset.toolBox),"Verify Text overlay Menu closes on performing PAN operation","Text overlay menu is not present");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test17_US476_TC1942_VerifyScrollCommandGetStoppedAfterPerformingOtherScroll() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scoll command should get stop after performing other scroll operations");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		ViewerARToolbox artoolbar = new ViewerARToolbox(driver);
		
		//start Cine from radial menu
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//start scrolling using Keyboard and verify Cine is being stopped
		viewerPage.scrollDownToSliceUsingKeyboard(3);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify Cine is getting stopped on scrolling using Keyboard");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is getting stopped on scrolling using Keyboard","Cine is stopped on Viewbox1");

		//start Cine from keyboard
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify Cine is being played on Viewbox1 using Keyboard");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//start scrolling using Mouse wheel and verify cine is stopped
		viewerPage.mouseWheelScrollInViewer(1, "down", 5);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify Cine is getting stopped on scrolling using mouse wheel");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is getting stopped on scrolling using mouse wheel","Cine is stopped on Viewbox1");

		//start Cine from keyboard
		viewerPage.playOrStopCineUsingKeyboardCKey();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify Cine is being played on Viewbox1 using keyboard");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//perform GSPS scroll and verify cine is stopped
		artoolbar.scrollDownGSPSUsingKeyboard();
		viewerPage.waitForAllChangesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify Cine is getting stopped on GSPS scrolling using Page Down key");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine being played on Viewbox1 on GSPS scrolling using Page Down key","Cine is getting played on Viewbox1");

		//start Cine from keyboard
		viewerPage.playOrStopCineUsingKeyboardCKey();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify Cine is being played on Viewbox1 using keyboard");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is getting stopped on Viewbox1","Cine is getting stopped on Viewbox1");

		//perform GSPS scroll and verify cine is stopped
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");
		artoolbar.scrollUpGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify Cine is getting stopped on GSPS scrolling using Page Up key");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 on GSPS scrolling using Page Up key","Cine is being played on Viewbox1");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test18_US476_TC1941_VerifyScrollCommandNotStoppedOnOpeningMenu() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that scroll command is not stopped on opening menu");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		cs = new ContentSelector(driver);
		preset = new ViewBoxToolPanel(driver);
		layout = new ViewerLayout(driver);
		//start Cine from radial menu
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//open a radial menu and verify Cine is not stopped
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify Cine is being played on Viewbox1 while opening Radial menu");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while opening Radial menu","Cine is getting played on Viewbox1");

		//open a North star menu and verify cine is not stopped 
		viewerPage.click(layout.gridIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify Cine is being played on Viewbox1 while opening North Star");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while opening NorthStar menu","Cine is getting played on Viewbox1");

		//Open a Tool box and and verify cine is not stopped 
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify Cine is being played on Viewbox1 while opening Toolbox");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while opening Toolbox menu","Cine is getting played on Viewbox1");
/*
		//Open a Content selector and and verify cine is not stopped 
		cs.openContentSelector(viewerPage.getViewbox(1), viewerPage.getSeriesDescription(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify Cine is being played on Viewbox1 while opening content selector");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while opening content selector","Cine is getting played on Viewbox1");
*/
		//select zoom to 100% from overlay and verify cine is stopped
		preset.changeZoomNumber(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
		int Zoom100 = viewBoxPanel.getZoomValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verifying that the Zoom value is set to 'Zoom to 100%'" );
		viewerPage.assertEquals(Zoom100, 100, "Verify zoom to 100%", "Zoom value is updated to 100%");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify Cine is being played on Viewbox1 while selecting from overlay menu");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while selecting menu from Zoom overlay","Cine is getting played on Viewbox1");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test19_US476_TC1943_VerifyScrollCommandGetStoppedOnPerformingMeasurement() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that scroll command is not stopped on opening menu");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		line = new  SimpleLine(driver);
		point=new PointAnnotation(driver);

		//select linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);

		//start cine from keyboard and draw a line
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");
		lineWithUnit.drawLine(1, 20, 30, 100, 130);

		//verify line is drawn on view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify that Linear Measurement is drawn on Viewbox1");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		//verify Cine is stopped on drawing a linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify Cine is stopped on drawing a linear measurement");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on drawing a linear measurement","Cine is stopped on Viewbox1");

		//select Point Annotation from radial menu
		point.selectPointFromQuickToolbar(1);

		//start cine from keyboard and draw a point annotation
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);

		point.drawPointAnnotationMarkerOnViewbox(1, 60, 70);

		//verify point is drawn on view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify that Point Annotation is drawn on Viewbox1");
		viewerPage.assertTrue(point.isPointPresent(1, 1),"Verify that Point Annotation is drawn on Viewbox1", "Point Annotation is correctly drawn");

		//verify Cine is stopped on drawing a Point Annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify Cine is stopped on drawing a Point Annotation");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on drawing a Point Annotation","Cine is stopped on Viewbox1");

		//select Ellipse Annotation from radial menu
		ellipse.mouseHover(ellipse.getViewPort(1));
		ellipse.selectEllipseFromQuickToolbar(1);

		//start cine from keyboard and draw a Ellipse annotation
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);

		//verify Ellipse annotation is drawn on view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Verify that user is able to draw a Ellipse annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getEllipses(1).get(0)), "Verify a Ellipse is drawn on Viewbox1", "Ellipse is drawn on Viewbox1");

		//verify Cine is stopped on drawing a Ellipse Annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify Cine is stopped on drawing a Ellipse Annotation");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on drawing a Ellipse Annotation","Cine is stopped on Viewbox1");

		//select Circle Annotation from radial menu
		circle.selectCircleFromQuickToolbar(1);

		//start cine from keyboard and draw a circle annotation
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ellipse.drawEllipse(1, 200, -50, -100,-150);

		//verify circle annotation is drawn on view box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify that user is able to draw a Circle annotation on DICOM");
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.getAllCircles(1).get(0)), "Verify a Circle is drawn on Viewbox1", "Circle is drawn on Viewbox1");

		//verify Cine is stopped on drawing a Circle Annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify Cine is stopped on drawing a Circle Annotation");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on drawing a Circle Annotation","Circle is stopped on Viewbox1");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test22_US476_TC1945_VerifyScrollCommandNotStoppedOnChangingSliceNumber() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command should not stop while changing slice number by manual input");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//change slice number by a manual input of slice
		viewerPage.scrollToImage(1,2);

		//verify cine is not getting stopped on manual input of slice number
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Cine is not stopped on manual input of image number");
		viewerPage.waitForAllImagesToLoad();
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is not stopped on manual input of image number","Cine is getting played on Viewbox1");	

	}	

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test23_US476_TC1950_VerifyScrollCommandNotStoppedOnPerformingViewboxCommand() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command should not stop on selecting series from content selector");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		cs = new ContentSelector(driver);
		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//select series from content selector
		String secondSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES02", liver9filePath);
		cs.selectSeriesFromSeriesTab(1, secondSeriesDescription);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify series change on selecting from content selector");
		viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1),secondSeriesDescription,"Verify series change on selecting from content selector","The series on viewbox1 is: "+secondSeriesDescription);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Cine is not stopped on selecting series from content selector");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is not stopped on selecting series from content selector","Cine is getting played on Viewbox1");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test24_US476_TC2050_01_VerifyAnnotationsStopOnChangingActiveViewbox() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Active Viewbox - Verify that annotation commands should get stopped while changing active viewbox.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);
		line = new  SimpleLine(driver);
		polyline = new PolyLineAnnotation(driver);

		//Verifying with ellipse annotation
		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Ellipse" );
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -50, -50, -70,-50);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/14]", "Verify that the Ellipse is selected");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verifying the ellipse is selected", "Ellipse is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/14]", "Verify that the Ellipse is not selected");
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),  "Verifying the ellipse is not selected", "Ellipse is not selected");

		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and delete drawn Ellipse" );
		ellipse.selectEllipse(1, 1);
		ellipse.deleteSelectedEllipse();

		//Verifying with circle annotation
		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Circle" );
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -100,-100);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/14]", "Verify that the Circle is selected");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verifying the circle is selected", "Circle is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/14]", "Verify that the Circle is not selected");
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),  "Verifying the circle is not selected", "Circle is not selected");

		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and delete Circle" );
		circle.selectCircle(1, 1);
		circle.deleteSelectedCircle();

		//Verifying with linear measurement annotation
		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Measurement" );
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 50, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/14]", "Verify that the Linear measurement is selected");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1), "Verify that the linear measurement is selected", "Linear measurement is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/14]", "Verify that the Linear measurement is not selected");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1), "Verify that the linear measurement is not selected", "Linear measurement is not selected");

		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and delete measurement" );
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.deleteAllAnnotation(1);

		//Verifying with line annotation
		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Line" );
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,20,0,80,0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/14]", "Verify that the Line is selected");
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verify that the line is selected", "Line is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/14]", "Verify that the Line is not selected");
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1), "Verify that the line is not selected", "Line is not selected");


		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn line" );
		line.selectLine(1, 1);
		line.deleteSelectedLine();

		//Verifying with Point annotation
		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Point" );
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/14]", "Verify that the point is selected");
		viewerPage.assertTrue(point.isPointSelected(1,1), "Verify that the point is selected", "Point is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/14]", "Verify that the point is not selected");
		viewerPage.assertFalse(point.isPointSelected(1,1), "Verify that the point is not selected", "Point is not selected");

		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn point" );
		point.selectPoint(1, 1);
		point.deleteSelectedPoint();

		//Verifying with Polyline annotation
		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw polyline" );
		polyline.selectPolylineFromQuickToolbar(1);
		polyline.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/14]", "Verify that the polyline is selected");
		viewerPage.assertTrue(polyline.isPolylineSelected(1), "Verify that the polyline is selected", "Polyline is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/14]", "Verify that the polyline is not selected");
		viewerPage.assertFalse(polyline.isPolylineSelected(1), "Verify that the polyline is not selected", "Polyline is not selected");

		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn polyline" );
		polyline.selectClassicPolylineWithCtrlLeft(1, 1);
		polyline.deleteSelectedPolyline();

		//Verifying with text annotation annotation

		//Step 1  Draw annotation and verify that it is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw text annotation" );
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1, -50, -50, "Viewbox1_Text");
		//		textAn.getAnchorLinesOfTextAnnot(1, 1).get(0).click();;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/14]", "Verify that the text annotation is selected");
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1), "Verify that the text annotation is selected", "Text annotation is selected");

		//Change the active viewbox and verify that the annotation is not selected any more
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/14]", "Verify that the text annotation is not selected");
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1), "Verify that the text annotation is not selected", "Text annotation is not selected");

		//Deleting the drawn annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn text annotation" );
		textAn.mouseHover(textAn.getViewPort(1));
		textAn.click(textAn.getLineOfTextAnnotations(1, 1));
		textAn.deleteSelectedTextAnnotation();

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test25_US476_TC1993_VerifyCommandStopsOnOpeningMenu() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Measurement-Menu-Verify that measurement command should stop while opening menu");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		layout = new ViewerLayout(driver);
		
		textAn = new TextAnnotation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		//Create Text annotation on viewbox1 and opening radial menu
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawTextAnnotationWithNoText(1, 10, 10);
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1),"Checkpoint[1]","verifying on opening radial menu text annotation is not created if text not entered");

		//Create Text annotation on viewbox1 and opening context menu
		textAn.drawTextAnnotationWithNoText(1, 10, 10);
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1),"Checkpoint[2]","verifying on opening context menu text annotation is not created if text not entered");

		//Create Text annotation on viewbox1 and Northstar logo

		textAn.drawTextAnnotationWithNoText(1, 10, 10);
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1),"Checkpoint[3]","verifying on opening northstar logo menu text annotation is not created if text not entered");


		//Create Text annotation on viewbox1 and opening window level present menu
		textAn.drawTextAnnotationWithNoText(1, 10, 10);
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1),"Checkpoint[4]","verifying on opening windowing preset menu text annotation is not created if text not entered");

		//Create Text annotation on viewbox1 and opening window level present menu		
		textAn.drawTextAnnotationWithNoText(1, 10, 10);
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1),"Checkpoint[5]","verifying on opening windowing preset menu text annotation is not created if text not entered");


		//Create Text annotation on viewbox1 and opening zoom present menu
		textAn.drawTextAnnotationWithNoText(1, 10, 10);
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1),"Checkpoint[6]","verifying on opening windowing preset menu text annotation is not created if text not entered");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test26_US476_TC2041_01_VerifyAnnotationGetsUnSelectedOnOpeningMenu() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Menu - Verify that annotation commands should get stopped while performing menu operations.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		viewerPage.waitForViewerpageToLoad();
		textAn = new TextAnnotation(driver);
		layout = new ViewerLayout(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		// Checking for TextAnnotation
		String textAnnotation = "Automation_text";
		//Create Text annotation on viewbox1 and opening radial menu
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1, 10, -20, textAnnotation);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Text annotation and verifying the Annotation should un-select and the menu should get opened.");
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1),"Checkpoint[1/85]","verifying text annotation is selected before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(textAn.isTextAnnotationPresent(1),"Checkpoint[2/85]","verifying Text annotation is present");
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1),"Checkpoint[3/85]","verifying text annotation is not selected after opening radial menu");

		//Create Text annotation on viewbox1 and opening context menu
		textAn.selectTextAnnotation(1, 1);
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1),"Checkpoint[4/85]","verifying text annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1),"Checkpoint[5/85]","verifying text annotation is not selected after opening context menu");

		//Create Text annotation on viewbox1 and Northstar logo
		textAn.selectTextAnnotation(1, 1);
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1),"Checkpoint[6/85]","verifying text annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1),"Checkpoint[7/85]","verifying text annotation is not selected after opening Northstar logo menu");

		//Create Text annotation on viewbox1 and opening window level present menu
		textAn.selectTextAnnotation(1, 1);
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1),"Checkpoint[8/85]","verifying text annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1),"Checkpoint[9/85]","verifying text annotation is not selected after opening windowing preset menu");

		//Create Text annotation on viewbox1 and opening window level present menu		
		textAn.selectTextAnnotation(1, 1);
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1),"Checkpoint[10/85]","verifying text annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1),"Checkpoint[11/85]","verifying text annotation is not selected after opening windowing preset menu");

		//Create Text annotation on viewbox1 and opening zoom present menu
		textAn.selectTextAnnotation(1, 1);
		viewerPage.assertTrue(textAn.isTextAnnotationSelected(1,1),"Checkpoint[12/85]","verifying text annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(textAn.isTextAnnotationSelected(1,1),"Checkpoint[13/85]","verifying text annotation is not selected after opening zoom preset menu");

		/*
		 * Checking for point
		 * 
		 */

		point = new PointAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Point and verifying the Annotation should un-select and the menu should get opened.");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 20, 50);
		viewerPage.assertTrue(point.isPointSelected(1,1),"Checkpoint[14/85]","verifying point annotation is selected before opening radial menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(point.isPointSelected(1,1),"Checkpoint[15/85]","verifying point annotation is not selected after opening radial menu");

		//Create point annotation on viewbox1 and opening context menu
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[16/85]","verifying point annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[17/85]","verifying point annotation is not selected after opening context menu");

		//Create point annotation on viewbox1 and Northstar logo
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[18/85]","verifying point annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[19/85]","verifying point annotation is not selected after opening Northstar logo menu");

		//Create point annotation on viewbox1 and opening window level present menu
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[20/85]","verifying point annotation is selected before opening windowing center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[21/85]","verifying point annotation is not selected after opening windowing center preset menu");

		//Create point annotation on viewbox1 and opening window level present menu		
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[22/85]","verifying point annotation is selected before opening windowing width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[23/85]","verifying point annotation is not selected after opening windowing width preset menu");

		//Create point annotation on viewbox1 and opening zoom present menu
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[24/85]","verifying point annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[25/85]","verifying point annotation is not selected after opening zoom preset menu");

		/*
		 * Checking for circle
		 * 
		 */
		circle = new CircleAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Circle and verifying the Annotation should un-select and the menu should get opened.");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -60, -50,30,30);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[26/85]","verifying circle annotation is selected before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[27/85]","verifying circle annotation is not selected after opening radial menu");

		//Create circle annotation on viewbox1 and opening context menu
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[28/85]","verifying circle annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[29/85]","verifying circle annotation is not selected after opening context menu");

		//Create circle annotation on viewbox1 and Northstar logo
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[30/85]","verifying circle annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[31/85]","verifying circle annotation is not selected after opening Northstar logo menu");

		//Create circle annotation on viewbox1 and opening window level present menu
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[32/85]","verifying circle annotation is selected before opening windowing center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[33/85]","verifying circle annotation is not selected after opening windowing center preset menu");

		//Create circle annotation on viewbox1 and opening window level present menu		
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[34/85]","verifying circle annotation is selected before opening windowing width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[35/85]","verifying circle annotation is not selected after opening windowing width preset menu");

		//Create circle annotation on viewbox1 and opening zoom present menu
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[36/85]","verifying circle annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[37/85]","verifying circle annotation is not selected after opening zoom preset menu");

		/*
		 * Checking for ellipse
		 * 
		 */
		ellipse = new EllipseAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Ellipse and verifying the Annotation should un-select and the menu should get opened.");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -120, -100,50,60);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[38/85]","verifying ellipse annotation is selected before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[39/85]","verifying ellipse annotation is not selected after opening radial menu");

		//Create ellipse annotation on viewbox1 and opening context menu
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[40/85]","verifying ellipse annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[41/85]","verifying ellipse annotation is not selected after opening context menu");

		//Create ellipse annotation on viewbox1 and Northstar logo
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[42/85]","verifying ellipse annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[43/85]","verifying ellipse annotation is not selected after opening Northstar logo menu");

		//Create ellipse annotation on viewbox1 and opening window level present menu
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[44/85]","verifying ellipse annotation is selected before opening windowing center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[45/85]","verifying ellipse annotation is not selected after opening windowing center preset menu");

		//Create ellipse annotation on viewbox1 and opening window level present menu		
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[46/85]","verifying ellipse annotation is selected before opening windowing width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[47/85]","verifying ellipse annotation is not selected after opening windowing width preset menu");

		//Create ellipse annotation on viewbox1 and opening zoom present menu
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[48/85]","verifying ellipse annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[49/85]","verifying ellipse annotation is not selected after opening zoom preset menu");

		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		lineWithUnit = new MeasurementWithUnit(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Linear Measurement and verifying the Annotation should un-select and the menu should get opened.");
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-70, 0, 50,0);

		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[50/85]","verifying LinearMeasurement annotation is selected before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[51/85]","verifying LinearMeasurement annotation is not selected after opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[52/85]","verifying LinearMeasurement annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[53/85]","verifying LinearMeasurement annotation is not selected after opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[54/85]","verifying LinearMeasurement annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[55/85]","verifying LinearMeasurement annotation is not selected after opening Northstar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[56/85]","verifying LinearMeasurement annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[57/85]","verifying LinearMeasurement annotation is not selected after opening windowing preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu		
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[58/85]","verifying LinearMeasurement annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[59/85]","verifying LinearMeasurement annotation is not selected after opening windowing preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[60/85]","verifying LinearMeasurement annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[61/85]","verifying LinearMeasurement annotation is not selected after opening zoom preset menu");

		lineWithUnit.deleteAllAnnotation(1);

		/*
		 * Checking for Line Segment
		 * 
		 */
		line = new SimpleLine(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Line segment and verifying the Annotation should un-select and the menu should get opened.");
		line.selectLineFromQuickToolbar(1);		
		viewerPage.dragAndReleaseOnViewerWithClick(-70, 70, 70,70);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[62/85]","verifying Line Segment annotation is selected before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[63/85]","verifying Line Segment annotation is not selected after opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[64/85]","verifying Line Segment annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[65/85]","verifying Line Segment annotation is not selected after opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[66/85]","verifying Line Segment annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[67/85]","verifying Line Segment annotation is not selected after opening Northstar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[68/85]","verifying Line Segment annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[69/85]","verifying Line Segment annotation is not selected after opening windowing preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu		
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[70/85]","verifying Line Segment annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[71/85]","verifying Line Segment annotation is not selected after opening windowing preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[72/85]","verifying Line Segment annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[73/85]","verifying Line Segment annotation is not selected after opening zoom preset menu");

		/*
		 * Checking for polyline Segment
		 * 
		 */

		poly = new PolyLineAnnotation(driver);
		poly.closingConflictMsg();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing PolyLine and verifying the Annotation should un-select and the menu should get opened.");
		poly.selectPolylineFromQuickToolbar(2);		
		poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[74/85]","verifying polyline annotation is selected before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[75/85]","verifying polyline annotation is not selected after opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[76/85]","verifying polyline annotation is selected before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[77/85]","verifying polyline annotation is not selected after opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[78/85]","verifying polyline annotation is selected before opening Northstar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[79/85]","verifying polyline annotation is not selected after opening Northstar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[80/85]","verifying polyline annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(2));
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[81/85]","verifying polyline annotation is not selected after opening windowing preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu		
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[82/85]","verifying polyline annotation is selected before opening windowing preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(2));
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[83/85]","verifying polyline annotation is not selected after opening windowing preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[84/85]","verifying polyline annotation is selected before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(2));
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[85/85]","verifying polyline annotation is not selected after opening zoom preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		cs = new ContentSelector(driver);
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[86]","verifying polyline annotation is selected before opening content selector  menu");
		cs.openAndCloseSeriesTab(true);
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[87]","verifying polyline annotation is not selected after opening content selector menu");


		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.isPolylineSelected(2),"Checkpoint[88]","verifying polyline annotation is selected before opening logoff menu");
		Header header = new Header(driver);
		header.openLogoffMenu();
		viewerPage.assertFalse(poly.isPolylineSelected(2),"Checkpoint[89]","verifying polyline annotation is not selected after opening logoff menu");



	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test26_US476_TC2041_02_VerifyAnnotationGetsUnSelectedOnOpeningMenu() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Menu - Verify that annotation commands should get stopped while performing menu operations.(GSPS Focus Finding)");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		layout = new ViewerLayout(driver);
		// Checking for TextAnnotation

		viewBoxPanel=new ViewBoxToolPanel(driver);
		textAnn = new TextAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Text Annotation - GSPS Focus finding");
		String textAnnotation = "Automation_text";
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 100, 20, textAnnotation);

		//		viewerPage.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[1/84]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[2]","verifying GSPS menu absence after opening radial menu");

		//Create Text annotation on viewbox1 and opening context menu
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[4]","verifying GSPS menu absence after opening radial menu");

		//Create Text annotation on viewbox1 and Northstar logo
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[5]","verifying GSPS menu presence before opening NorthStar Logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[6]","verifying GSPS menu absence after opening Northstar Logo menu");

		//Create Text annotation on viewbox1 and opening window level present menu
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[7]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[8]","verifying GSPS menu absence after opening window center preset menu");

		//Create Text annotation on viewbox1 and opening window level present menu		
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[9]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[10]","verifying GSPS menu absence before opening window width preset menu");

		//Create Text annotation on viewbox1 and opening zoom present menu
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[11]","verifying GSPS menu presence before opening zoom label preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[12]","verifying GSPS menu absence before opening zoom label preset menu");

		/*
		 * Checking for point
		 * 
		 */
		point = new PointAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Point Annotation - GSPS Focus finding");
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -70, 50);
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[13]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[14]","verifying GSPS menu absence after opening radial menu");


		//Create point annotation on viewbox1 and opening context menu
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[15]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[16]","verifying GSPS menu absence before opening context menu");

		//Create point annotation on viewbox1 and Northstar logo
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[17]","verifying GSPS menu presence before opening NorthStar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[18]","verifying GSPS menu absence before opening NorthStar logo menu");

		//Create point annotation on viewbox1 and opening window level present menu
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[19]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[20]","verifying GSPS menu absence before opening window center preset menu");

		//Create point annotation on viewbox1 and opening window level present menu		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[21]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[22]","verifying GSPS menu absence before opening window width preset menu");

		//Create point annotation on viewbox1 and opening zoom present menu
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[23]","verifying GSPS menu presence before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[24]","verifying GSPS menu absence before opening zoom preset menu");


		/*
		 * Checking for circle
		 * 
		 */
		circle = new CircleAnnotation(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Circle Annotation - GSPS Focus finding");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -150,30,30);
		viewerPage.click(viewerPage.getViewPort(1));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[25]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[26]","verifying GSPS menu absence before opening radial menu");


		//Create circle annotation on viewbox1 and opening context menu
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[27]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[28]","verifying GSPS menu absence before opening context menu");

		//Create circle annotation on viewbox1 and Northstar logo
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[29]","verifying GSPS menu presence before opening NorthStar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[30]","verifying GSPS menu absence before opening NorthStar logo menu");

		//Create circle annotation on viewbox1 and opening window level present menu
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[31]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[32]","verifying GSPS menu absence before opening window center preset	 menu");

		//Create circle annotation on viewbox1 and opening window level present menu		
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[33]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[34]","verifying GSPS menu absence before opening window width preset menu");

		//Create circle annotation on viewbox1 and opening zoom present menu
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[35]","verifying GSPS menu presence before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[36]","verifying GSPS menu absence before opening zoom preset menu");

		/*
		 * Checking for ellipse
		 * 
		 */
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "ellipse Annotation - GSPS Focus finding");
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, -50,50,60);
		viewerPage.click(viewerPage.getViewPort(1));
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[37]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[38]","verifying GSPS menu absence before opening radial menu");


		//Create ellipse annotation on viewbox1 and opening context menu
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[39]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[40]","verifying GSPS menu absence before opening context menu");

		//Create ellipse annotation on viewbox1 and Northstar logo
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[41]","verifying GSPS menu presence before opening NorthStar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[42]","verifying GSPS menu absence before opening NorthStar logo menu");

		//Create ellipse annotation on viewbox1 and opening window level present menu
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[43]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[44]","verifying GSPS menu absence before opening window center preset menu");

		//Create ellipse annotation on viewbox1 and opening window level present menu		
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[45]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[46]","verifying GSPS menu absence before opening window width preset menu");

		//Create ellipse annotation on viewbox1 and opening zoom present menu
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[47]","verifying GSPS menu presence before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[48]","verifying GSPS menu absence before opening zoom preset menu");


		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "LinearMeasurement Annotation - GSPS Focus finding");
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-70, 0, 50,0);
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[49]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[50]","verifying GSPS menu absence before opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[51]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[52]","verifying GSPS menu absence before opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[53]","verifying GSPS menu presence before opening NorthStar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[54]","verifying GSPS menu absence before opening NorthStar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[55]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[56]","verifying GSPS menu absence before opening window center preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu		
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[57]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[58]","verifying GSPS menu absence before opening window width preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[59]","verifying GSPS menu presence before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[60]","verifying GSPS menu absence before opening zoom preset menu");

		lineWithUnit.deleteAllAnnotation(1);

		/*
		 * Checking for Line Segment
		 * 
		 */
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Line Segment Annotation - GSPS Focus finding");
		line= new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);		
		line.drawLine(1,-100, 70, 70,0);
		viewerPage.click(viewerPage.getViewPort(1));
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[61]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[62]","verifying GSPS menu absence before opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[63]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[64]","verifying GSPS menu presence before opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[65]","verifying GSPS menu presence before opening NorthStar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[66]","verifying GSPS menu presence before opening NorthStar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[67]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[68]","verifying GSPS menu presence before opening window center preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu		
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[69]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[70]","verifying GSPS menu presence before opening window width preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[71]","verifying GSPS menu presence before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[72]","verifying GSPS menu presence before opening zoom preset menu");

		/*
		 * Checking for polyline 
		 * 
		 */

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "polyline Annotation - GSPS Focus finding");
		poly = new PolyLineAnnotation(driver);
		poly.closingConflictMsg();
		poly.selectPolylineFromQuickToolbar(2);		
		poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[73]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[74]","verifying GSPS menu absence before opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[75]","verifying GSPS menu presence before opening context menu");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[76]","verifying GSPS menu absence before opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[77]","verifying GSPS menu presence before opening NorthStar logo menu");
		viewerPage.click(layout.gridIcon);
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[78]","verifying GSPS menu absence before opening NorthStar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[79]","verifying GSPS menu presence before opening window center preset menu");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(2));
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[80]","verifying GSPS menu absence before opening window center preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu		
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[81]","verifying GSPS menu presence before opening window width preset menu");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(2));
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[82]","verifying GSPS menu absence before opening window width preset menu");

		//Create LinearMeasurement annotation on viewbox1 and opening zoom present menu
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[83]","verifying GSPS menu presence before opening zoom preset menu");
		viewerPage.click(viewBoxPanel.getZoomOverlay(2));
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[84]","verifying GSPS menu absence before opening zoom preset menu");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test26_US476_TC2041_04_VerifyAnnotationGetsUnhighlightedOnOpenOfMenu() throws  InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Menu - Verify that annotation commands should get stopped while performing menu operations.GSPS Finding state");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		textAnn = new TextAnnotation(driver);
		lineWithUnit= new MeasurementWithUnit(driver);
		ellipse =new EllipseAnnotation(driver);
		point= new PointAnnotation(driver);
		circle= new CircleAnnotation(driver);
		cs = new ContentSelector(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		// Drawing  TextAnnotation
		String textAnnotation = "Automation_text";
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -30, -40, textAnnotation);

		// Drawing  Linear Distance Measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-130, 0, 50,0);

		// Drawing  ellipse
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -100,50,60);

		// Drawing  point		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, 90);

		//Drawing Circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100,90,90);

		//		viewerPage.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"checkpoint[1]","Circle is currently active and focused annotation");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1),"checkpoint[2]","Post open of radial menu, circle is accpted but not focused");


		circle.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"checkpoint[3]","point is currently active and focused annotation");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"checkpoint[4]","Post open of context menu, point is accpted but not focused");

		circle.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"checkpoint[5-1]","ellipse is currently active and focused annotation");
		viewerPage.click(viewerPage.EurekaLogo);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"checkpoint[5-2]","Post open of northstar menu, ellipse is accpted but not focused");

		circle.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"checkpoint[6-1]","Linear measurement is currently active and focused annotation");
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"checkpoint[6-2]","Post open of zoom preset menu, linear measurement is accepted but not focused");

		circle.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true),"checkpoint[7-1]" , "Verify the text annotation is accepted");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true),"checkpoint[7-2]", "Verify the text annotation is active not focused");

		circle.navigateGSPSForwardUsingKeyboard();		
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"checkpoint[8-1]","Linear measurement is currently active and focused annotation");
		cs.openAndCloseSeriesTab(true);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"checkpoint[8-2]","Post open of content selector, linear measurement is accepted but not focused");

		circle.navigateGSPSForwardUsingKeyboard();	
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"checkpoint[9-1]","ellipse is currently active and focused annotation");
		Header header = new Header(driver);
		header.openLogoffMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"checkpoint[9-2]","Post open of log off menu, ellipse is accpted but not focused");

		circle.navigateGSPSForwardUsingKeyboard();	
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"checkpoint[10-1]","point is currently active and focused annotation");
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"checkpoint[10-2]","Post open of window center preset menu, point is accpted but not focused");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test27_US476_TC1953_VerifyScrollCommandGetStopOnPerformingLayoutChange() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command should stop while performing layout change");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		layout = new ViewerLayout(driver);
		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//change the layout to 1X2
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Cine is stopped on changing a layout");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on changing a layout","Cine is stopped on Viewbox1");

		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);

		//perform Double click one-up
		viewerPage.doubleClickOnViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Cine is stopped on double click One-up");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on double click One-up","Cine is stopped on Viewbox1");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test28_US476_TC1957_VerifyScrollCommandGetStoppedOnNavigatingToOtherPage() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll command is stopped on onvigating to other pages");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName,1,1);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Cine is stopped on navigating back to other pages");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on navigating back to other pages","Cine is stopped on navigating back to other pages");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test29_US476_TC1958_VerifyScrollCommandNotStoppedOnViewerOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command should not stop after applying viewer operations");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//toggle sync off by pressing space bar and verify cine is not stopped
		viewerPage.performSyncONorOFF();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Cine does not stopped on toggling sync off");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine does not stopped on toggling sync off","Cine is getting played on viewbox1");

		//enable window leveling from keyboard and verify cine is not stopped
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify Cine does not stopped on enabling Window leveling using Keyboard shortcut");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine does not stopped on enabling Window leveling using Keyboard shortcut","Cine is getting played on viewbox1");

		//change active view box and verify cine is not stopped
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Cine does not stopped on changing active viewbox");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine does not stopped on changing active viewbox","Cine is getting played on viewbox1");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test31_US476_TC1984_VerifyScrollCommandStopOnExternalAndEmptyCommand() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command stop on external and empty command");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify Cine is being played on Viewbox1");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//press ALT+TAB to switch window task
		viewerPage.pressTaskSwitcher();
		viewerPage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Cine is being played on Viewbox1");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 on pressing Hot key for Window switch","Cine is being played on Viewbox1");
		//viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on pressing Hot key for Window switch","Cine is stopped on Viewbox1");

		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);

		//perform empty command
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		viewerPage.click(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Cine is stopped on empty command");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on empty command","Cine is stopped on Viewbox1");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test32_US476_TC2009_VerifyScrollCommandStopOnPerformingActionOnAnnotation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command get stop on performing any action on annotations");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		ViewerARToolbox arTool = new ViewerARToolbox(driver);
		ViewerOrientation orin = new ViewerOrientation(driver);
		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Cine is being played on Viewbox1");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//press arrow key to focus next GSPS object
		arTool.navigateGSPSForwardUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Cine is stopped on GSPS focus finding");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on GSPS focus finding","Cine is getting played on Viewbox1");
		//viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on GSPS focus finding","Cine is stopped on Viewbox1");

		//start cine from keyboard 
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);

		//click on active overlay and verify cine is not stopped
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify Cine is not stopped on selecting overlay");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is not stopped on selecting overlay","Cine is getting played on Viewbox1");

		//toggle off GSPS result by pressing G key
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify the 'Result applied' is set to toogle 'Off'");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by pressing Keyboard shorcut", "Result Applied is toggle Off on viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Cine is not stopped on toggling off GSPS result");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is not stopped on toggling off GSPS result","Cine is getting played on Viewbox1");

		//change orientation and verify cine is not stopped
		orin.flipSeries(orin.getTopOrientationMarker(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify Cine is not stopped on changing orientation");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is not stopped on changing orientation","Cine is getting played on Viewbox1");


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test33_US476_TC2054_VerifyAnnotationCommandStopOnViewboxOperation() throws  InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Annotation command stops on viewbox operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		
		point = new PointAnnotation(driver);
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify GSPS radial menu appear on right clicking GSPS object on Viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu appear on ViewBox1", "The GSPS radial menu appear on ViewBox");

		//toggle GSPS result off and then again On and verify GSPS object should be unfocused
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify GSPS radial menu disappear on toggling Off/On GSPS result applied");
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu disappear on toggling Off/On GSPS result applied", "The GSPS radial menu do not appear on ViewBox");

		//Verify Point annotation is no longer active GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify active GSPS object is no longer active on toggling Off/On GSPS result applied.");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Verify active GSPS object is no longer active on toggling Off/On GSPS result applied.", "The Point Annotation loses its focus on toggling GSPS result off");

		//perform right click on GSPS object to open a GSPS radial menu
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify GSPS radial menu appear on right clicking GSPS object on Viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu appear on ViewBox1", "The GSPS radial menu appear on ViewBox");

		//toggle GSPS result off and then again On and while performing GSPS scroll
		viewerPage.toggleOffandOnKeyboardGWithGSPSscroll();

		//Verify Point annotation is no longer active GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify active GSPS object is no longer active on toggling Off/On GSPS result applied while performing GSPS sroll.");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Verify active GSPS object is no longer active on toggling Off/On GSPS result applied while performing GSPS sroll.", "The Point Annotation loses its focus on toggling GSPS result off");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test35_US476_TC2051_01_VerifyAnnotationGetsUnSelectedOnEmptyCommand() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Empty command - Verify that annotation commands should get stopped while performing Empty command operations.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -30, -40, 90);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 50, 50);

		//select Point Annotation drawn on viewbox
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[1/10]","Verify Point annotation is selected");

		//Click on Viewer to perform empty command and verify annotation is un-selected
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.waitForAllChangesToLoad();
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[2/10]","verify Point annotation de-selected on performing empty command");

		//select linear measurement annotation drawn on viewbox
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[3/10]","Verify linear measurement annotation is selected");

		//Click on Viewer to perform empty command and verify annotation is un-selected
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[4/10]","verify linear measurement annotation is de-selected on performing empty command");

		//select ellipse annotation drawn on viewbox
		circle.selectCircle(1, 1);;
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[5/10]","Verify Circle annotation is selected");

		//Click on Viewer to perform empty command and verify annotation is un-selected
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[6/10]","verify Circle annotation is de-selected on performing empty command");

		//select ellipse annotation drawn on viewbox
		ellipse.selectEllipse(1, 1);;
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[7/10]","Verify Ellipse annotation is selected");

		//Click on Viewer to perform empty command and verify annotation is un-selected
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[8/10]","verify Ellipse annotation is de-selected on performing empty command");
	}
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test35_US476_TC2051_03_VerifyAnnotationGSPSFocusDisappearOnEmptyCommand() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Empty Command - Verify GSPS Foucs disappear on performing empty command");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
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
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 50, 50);

		//right click on Point Annotation drawn on viewbox
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[1/12]", "Verify GSPS radial menu appear on ViewBox1");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[2/12]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[3/10]","verify Point annotation is loses GSPS foucs on performing empty command");

		//right click on Linear measurement Annotation drawn on viewbox
		point.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[4/12]", "Verify GSPS radial menu appear on ViewBox1");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[5/12]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[6/10]","verify linear measurement annotation loses it's GSPS foucs on performing empty command");

		//right click on Ellipse Annotation drawn on viewbox
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[7/12]", "Verify GSPS radial menu appear on ViewBox1");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[8/12]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[9/10]","verify Circle annotation loses it's GSPS foucs on performing empty command");

		//right click on Circle Annotation drawn on viewbox
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[10/12]", "Verify GSPS radial menu appear on ViewBox1");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[11/12]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/12]","verify Circle annotation loses it's GSPS foucs on performing empty command");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test35_US476_TC2051_04_VerifyAnnotationGSPSFindingStateDisappearOnEmptyCommand() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Empty Command - Verify GSPS focus finding disappear on performing empty command");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

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
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 40, 50);

		//press right arrow to move to next GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[13/116]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[15/16]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[16/16]","verify linear measurement annotation loses it's GSPS foucs on performing empty command");

		//press right arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[1/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[3/16]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/16]","verify Point annotation is loses GSPS foucs on performing empty command");

		//press right arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[5/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[7/16]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/16]","verify Ellipse annotation loses it's GSPS foucs on performing empty command");

		//press right arrow to move to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		circle.waitForTimePeriod(300);
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[9/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");

		//Click on Viewer to perform empty command and verify annotation is no longer active GSPS object
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[11/16]", "Verify GSPS radial menu close on performing empty command");
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/16]","verify Circle annotation loses it's GSPS foucs on performing empty command");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test36_US476_TC2043_01_VerifyAnnotationGetsUnSelectedOnKeyPress() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Viewer- Verify that annotation commands should get stopped while performing Viewer operations.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnn = new TextAnnotation(driver);

		// Checking for TextAnnotation
		String textAnnotation = "Automation_text";
		//Create Text annotation on viewbox1 and performing the sync off
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 10, 20, textAnnotation);
		viewerPage.assertTrue(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[1]","verifying text annotation is selected before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);		
		viewerPage.assertTrue(textAnn.isTextAnnotationPresent(1),"Checkpoint[2]","verifying Text annotation is present");

		//Create Text annotation on viewbox1 and performing the GSPS toggle off
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[4]","verifying text annotation is selected before opening context menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[5]","verifying text annotation is not selected after opening context menu");

		//Create Text annotation on viewbox1 and scroll key up and key down
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[6]","verifying text annotation is selected before opening Northstar logo menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[7]","verifying text annotation is not selected after opening Northstar logo menu");

		//Create Text annotation on viewbox1 and performing the windowing using 'w'
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[8]","verifying text annotation is selected before opening windowing preset menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertFalse(textAnn.isTextAnnotationSelected(1,1),"Checkpoint[9]","verifying text annotation is not selected after opening windowing preset menu");

		/*
		 * Checking for point
		 * 
		 */
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 20, 50);
		viewerPage.assertTrue(point.isPointSelected(1,1),"Checkpoint[14]","verifyivng point annotation is selected before pressing sync off");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);	
		viewerPage.assertFalse(point.isPointSelected(1,1),"Checkpoint[15]","verifying point annotation is not selected after pressing sync off");

		//Create point annotation on viewbox1 and GSPS toggle Off
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[16]","");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[17]","verifying point annotation is not selected after opening context menu");

		//Create point annotation on viewbox1 and scroll up and down
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[18]","verifying point annotation is selected before scroll");
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[19]","verifying point annotation is not selected after scroll");

		//Create point annotation on viewbox1 and opening window level present menu
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[20]","verifying point annotation is selected before window level ");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[21]","verifying point annotation is not selected after opening windowing preset menu");

		/*
		 * Checking for circle
		 * 
		 */
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 50,30,30);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[26]","verifying circle annotation is selected before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);	
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[27]","verifying circle annotation is not selected after opening radial menu");

		//Create circle annotation on viewbox1 and opening context menu
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[28]","verifying circle annotation is selected before opening context menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[29]","verifying circle annotation is not selected after opening context menu");

		//Create circle annotation on viewbox1 and Northstar logo
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[30]","verifying circle annotation is selected before opening Northstar logo menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[31]","verifying circle annotation is not selected after opening Northstar logo menu");

		//Create circle annotation on viewbox1 and opening window level present menu
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[32]","verifying circle annotation is selected before opening windowing preset menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[33]","verifying circle annotation is not selected after opening windowing preset menu");

		/*
		 * Checking for ellipse
		 * 
		 */
		ellipse.selectEllipseFromQuickToolbar(1);

		ellipse.drawEllipse(1, -100, -100,50,60);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[38]","verifying ellipse annotation is selected before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);	
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[39]","verifying ellipse annotation is not selected after opening radial menu");

		//Create ellipse annotation on viewbox1 and opening context menu
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[40]","verifying ellipse annotation is selected before opening context menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[41]","verifying ellipse annotation is not selected after opening context menu");

		//Create ellipse annotation on viewbox1 and Northstar logo
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[42]","verifying ellipse annotation is selected before opening Northstar logo menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[43]","verifying ellipse annotation is not selected after opening Northstar logo menu");

		//Create ellipse annotation on viewbox1 and opening window level present menu
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[44]","verifying ellipse annotation is selected before opening windowing preset menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[45]","verifying ellipse annotation is not selected after opening windowing preset menu");

		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-100, 0, 20,0);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[50]","verifying LinearMeasurement annotation is selected before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);	
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[51]","verifying LinearMeasurement annotation is not selected after opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[52]","verifying LinearMeasurement annotation is selected before opening context menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[53]","verifying LinearMeasurement annotation is not selected after opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[54]","verifying LinearMeasurement annotation is selected before opening Northstar logo menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[55]","verifying LinearMeasurement annotation is not selected after opening Northstar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[56]","verifying LinearMeasurement annotation is selected before opening windowing preset menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[57]","verifying LinearMeasurement annotation is not selected after opening windowing preset menu");
		//		viewerPage.deleteAllAnnotation(1);

		/*
		 * Checking for Line Segment
		 * 
		 */

		line = new SimpleLine(driver);
		line.closingConflictMsg();
		line.selectLineFromQuickToolbar(1);		
		line.drawLine(1,-100, 70, 70,0);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[62]","verifying Line Segment annotation is selected before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);	
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[63]","verifying Line Segment annotation is not selected after opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[64]","verifying Line Segment annotation is selected before opening context menu");
		line.click(line.getViewPort(1));
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.waitForTimePeriod(300);
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[65]","verifying Line Segment annotation is not selected after opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[66]","verifying Line Segment annotation is selected before opening Northstar logo menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[67]","verifying Line Segment annotation is not selected after opening Northstar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[68]","verifying Line Segment annotation is selected before opening windowing preset menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[69]","verifying Line Segment annotation is not selected after opening windowing preset menu");


		/*
		 * Checking for polyline Segment
		 * 
		 */
		poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);		
		poly.closingConflictMsg();
		poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[74]","verifying LinearMeasurement annotation is selected before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -100);	
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[75]","verifying LinearMeasurement annotation is not selected after opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[76]","verifying LinearMeasurement annotation is selected before opening context menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[77]","verifying LinearMeasurement annotation is not selected after opening context menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[78]","verifying LinearMeasurement annotation is selected before opening Northstar logo menu");
		viewerPage.scrollUpToSliceUsingKeyboard(2,1);
		viewerPage.scrollDownToSliceUsingKeyboard(2, 1);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[79]","verifying LinearMeasurement annotation is not selected after opening Northstar logo menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[80]","verifying LinearMeasurement annotation is selected before opening windowing preset menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, 100, -50);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[81]","verifying LinearMeasurement annotation is not selected after opening windowing preset menu");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test36_US476_TC2043_02_VerifyGSPSObjectGetsUnfocusedOnKeyPress() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Viewer- Verify that annotation commands should get stopped while performing Viewer operations.(\"GSPS Focus Finding\")");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnn = new TextAnnotation(driver);

		// Checking for TextAnnotation
		String textAnnotation = "Automation_text";
		//Create Text annotation on viewbox1 and opening radial menu
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 10, 20, textAnnotation);
		//		viewerPage.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create Text annotation on viewbox1 and opening context menu
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create Text annotation on viewbox1 and Northstar logo
		point.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create Text annotation on viewbox1 and opening window level present menu
		point.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		/*
		 * Checking for point
		 * 
		 */
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, 100);
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		//Create point annotation on viewbox1 and opening context menu
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create point annotation on viewbox1 and Northstar logo
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create point annotation on viewbox1 and opening window level present menu
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		/*
		 * Checking for circle
		 * 
		 */
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 50,30,30);
		viewerPage.click(viewerPage.getViewPort(1));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		//Create circle annotation on viewbox1 and opening context menu
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create circle annotation on viewbox1 and Northstar logo
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create circle annotation on viewbox1 and opening window level present menu
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		/*
		 * Checking for ellipse
		 * 
		 */
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -100,50,60);
		viewerPage.click(viewerPage.getViewPort(1));
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		//Create ellipse annotation on viewbox1 and opening context menu
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create ellipse annotation on viewbox1 and Northstar logo
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create ellipse annotation on viewbox1 and opening window level present menu
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(ellipse.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-70, 0, 50,0);
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");

		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//		viewerPage.deleteAllAnnotation(1);

		/*
		 * Checking for Line Segment
		 * 
		 */
		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);		
		line.drawLine(1,-100, 70, 70,0);
		viewerPage.click(viewerPage.getViewPort(1));
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		viewerPage.click(viewerPage.getViewPort(1));
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(line.isAcceptRejectToolBarPresent(1),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");

		/*
		 * Checking for polyline Segment
		 * 
		 */
		poly = new PolyLineAnnotation(driver);
		poly.closingConflictMsg();
		poly.selectPolylineFromQuickToolbar(2);		
		poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -100);
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");


		//Create LinearMeasurement annotation on viewbox1 and opening context menu
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(2,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(2), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.scrollUpToSliceUsingKeyboard(2,1);
		viewerPage.scrollDownToSliceUsingKeyboard(2, 1);
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(1));
		viewerPage.assertTrue(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[1]","verifying GSPS menu presence before opening radial menu");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2), 0, 0, 100, -50);
		viewerPage.assertFalse(poly.isAcceptRejectToolBarPresent(2),"Checkpoint[3]","verifying GSPS menu absence before opening radial menu");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test36_US476_TC2043_03_VerifyAnnotationGetsUnhighlightedOnOpenOfMenu() throws  InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Viewer- Verify that annotation commands should get stopped while performing Viewer operations.(GSPS Finding state)");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnn = new TextAnnotation(driver);

		// Drawing  TextAnnotation
		String textAnnotation = "Automation_text";
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 10, 20, textAnnotation);

		// Drawing  Linear Distance Measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-130, 0, 50,0);

		// Drawing  ellipse
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -100,50,60);

		// Drawing  point		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, 90);

		//Drawing Circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100,90,90);

		//		viewerPage.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"","");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1),"","");


		point.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"","");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"","");

		ellipse.navigateGSPSBackUsingKeyboard();		
		ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1);
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"","");

		ellipse.navigateGSPSBackUsingKeyboard();		
		lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1);
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"","");

		ellipse.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true),"Verify the text annotation is accepted","accepted");
		viewerPage.performSyncONorOFF();
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true),"Verify the text annotation is accepted","accepted");

		lineWithUnit.navigateGSPSForwardUsingKeyboard();		
		lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1);
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertFalse(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.assertTrue(viewerPage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Verify the 'Result applied' is set to toogle 'Off' by clicking on text 'Result applied' in Viewbox1", "Result Applied is toggle Off on viewbox1");
		viewerPage.assertTrue(verifyGSPSObjectPresence(1), "Verify the presence of GSPS object in Viewbox1", "GSPS object is no longer visible on viewbox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"","");

		ellipse.navigateGSPSForwardUsingKeyboard();	
		ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1);
		viewerPage.scrollUpToSliceUsingKeyboard(1,1);
		viewerPage.scrollDownToSliceUsingKeyboard(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"","");

		point.navigateGSPSForwardUsingKeyboard();	
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"","");
		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"","");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test37_US476_TC2044_01_VerifyAnnotationGetsUnSelectedOnViewerOperations() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Select Annotation : Annotation-Rendering- Verify that annotation commands should get stopped while Rendering.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnn = new TextAnnotation(driver);

		// Checking for TextAnnotation for viewer operations
		String textAnnotation = "Automation_text";
		//Create Text annotation on viewbox1 and performing WW/WL
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 10, 20, textAnnotation);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1, true),"Checkpoint[1]","verifying text annotation is selected before applying WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1,true),"Checkpoint[3]","verifying text annotation is not selected after applying WW/WL");

		//Create Text annotation on viewbox1 and performing PAN
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1, true),"Checkpoint[4]","verifying text annotation is selected before pan");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1,true),"Checkpoint[5]","verifying text annotation is not selected after pan");

		//Create Text annotation on viewbox1 and ZOOM
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1, true),"Checkpoint[6]","verifying text annotation is selected before zoom");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1,true),"Checkpoint[7]","verifying text annotation is not selected after zoom");

		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1, true),"Checkpoint[8]","verifying text annotation is selected before changing the image number");
		viewerPage.scrollToImage(1,48);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1,true),"Checkpoint[9]","verifying text annotation is not selected after changing the image number");

	
		/*
		 * Checking for point
		 * 
		 */
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 20, 50);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[14]","verifyivng point annotation is selected before applying WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		point.selectPoint(1, 1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[15]","verifying point annotation is not selected after applying WW/WL");

		//Create point annotation on viewbox1 and GSPS toggle Off
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[16]","verifyivng point annotation is selected before pan");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		point.selectPoint(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[17]","verifying point annotation is not selected after pan");

		//Create point annotation on viewbox1 and scroll up and down
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[18]","verifying point annotation is selected before zoom");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		point.selectPoint(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[19]","verifying point annotation is not selected after zoom");

		/*
		 * Checking for circle
		 * 
		 */
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 50,30,30);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[26]","verifying circle annotation is selected before WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		circle.selectCircle(1, 1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[27]","verifying circle annotation is not selected after WW/WL");

		//Create circle annotation on viewbox1 and Northstar logo
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[30]","verifying circle annotation is selected before pan");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		circle.selectCircle(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[31]","verifying circle annotation is not selected after pan");

		//Create circle annotation on viewbox1 and opening window level present menu
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[32]","verifying circle annotation is selected before zoom");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		circle.selectCircle(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint[33]","verifying circle annotation is not selected after zoom");

		/*
		 * Checking for ellipse
		 * 
		 */
		ellipse.selectEllipseFromQuickToolbar(1);

		ellipse.drawEllipse(1, -100, -100,50,60);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[38]","verifying ellipse annotation is selected before WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		ellipse.selectEllipse(1, 1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[39]","verifying ellipse annotation is not selected after WW/WL");

		//Create ellipse annotation on viewbox1 and Northstar logo
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[42]","verifying ellipse annotation is selected before pan");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		ellipse.selectEllipse(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[43]","verifying ellipse annotation is not selected after pan");

		//Create ellipse annotation on viewbox1 and opening window level present menu
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[44]","verifying ellipse annotation is selected before zoom");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		ellipse.selectEllipse(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint[45]","verifying ellipse annotation is not selected after zoom");

		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-70, 0, 50,0);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[50]","verifying LinearMeasurement annotation is selected before WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[51]","verifying LinearMeasurement annotation is not selected after WW/WL");

		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[54]","verifying LinearMeasurement annotation is selected before PAN");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[55]","verifying LinearMeasurement annotation is not selected after PAN");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[56]","verifying LinearMeasurement annotation is selected before ZOOM");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1,1),"Checkpoint[57]","verifying LinearMeasurement annotation is not selected after ZOOM");
		//		viewerPage.deleteAllAnnotation(1);

		/*
		 * Checking for Line Segment
		 * 
		 */
		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);		
		line.drawLine(1,-100, 70, 70,0);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[62]","verifying Line Segment annotation is selected before WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		line.selectLine(1, 1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[63]","verifying Line Segment annotation is not selected after WW/WL");


		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[66]","verifying Line Segment annotation is selected before PAN");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		line.selectLine(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[67]","verifying Line Segment annotation is not selected after PAN");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		line.selectLine(1, 1);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[68]","verifying Line Segment annotation is selected before ZOOM");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		line.selectLine(1, 1);
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertTrue(line.verifyLineAnnotationIsAcceptedGSPS(1,1),"Checkpoint[69]","verifying Line Segment annotation is not selected after ZOOM");


		/*
		 * Checking for polyline 
		 * 
		 */
		poly= new PolyLineAnnotation(driver);
		poly.closingConflictMsg();
		poly.selectPolylineFromQuickToolbar(1);		
		poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[74]","verifying polyline annotation is selected before WW/WL");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -100);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[75]","verifying polyline annotation is not selected after WW/WL");


		//Create LinearMeasurement annotation on viewbox1 and Northstar logo
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[78]","verifying polyline annotation is selected before PAN");
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -50);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[79]","verifying polyline annotation is not selected after PAN");

		//Create LinearMeasurement annotation on viewbox1 and opening window level present menu
		poly.selectClassicPolylineWithCtrlLeft(2, 1);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2,1),"Checkpoint[80]","verifying polyline annotation is selected before ZOOM");
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2), 0, 0, 100, -50);
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(2,1),"Checkpoint[81]","verifying polyline annotation is not selected after ZOOM");

	}


	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test37_US476_TC2044_03_VerifyGSPSObjectGetsUnfocusedOnOnViewerOperations() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS Focus Finding : Annotation-Viewer- Verify that annotation commands should get stopped while performing Viewer operations.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		textAnn=new TextAnnotation(driver);

		//GSPS Focus Finding : 
		//		1. Draw any annotation (point,line,circle,ellipse,polyline). 1. Annotation should be drawn.
		//		2. Select the annotation and right click.   2. GSPS Radial menu should be opened.
		//		3. Perform any Rendering operations (windowing, windowing delta, zooming, update from edit box etc).    
		//		3. Rendering should unfocused the GSPS annotation and GSPS radial menu should also close on  performing zoom, ww and Pan from selecting through radialmenu or through mouse events. 


		// Checking for TextAnnotation

		String textAnnotation = "Automation_text";

		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 10, 20, textAnnotation);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyAcceptGSPSRadialMenu(),"Checkpoint[1-1]","verifying GSPS menu presence on text annotation before WW/WL");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(textAnn.verifyAcceptGSPSRadialMenu(),"Checkpoint[1-2]","verifying GSPS menu absence on text annotation after WW/WL");



		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyAcceptGSPSRadialMenu(),"Checkpoint[2-1]","verifying GSPS menu presence on text annotation before pan");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(textAnn.verifyAcceptGSPSRadialMenu(),"Checkpoint[2-2]","verifying GSPS menu absence on text annotation after pan");


		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyAcceptGSPSRadialMenu(),"Checkpoint[3-1]","verifying GSPS menu presence on text annotation before zoom ");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(textAnn.verifyAcceptGSPSRadialMenu(),"Checkpoint[3-2]","verifying GSPS menu absence on text annotation after zoom");

		/*
		 * Checking for point
		 * 
		 */
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, 100);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[4-1]","verifying GSPS menu presence on point annotation before WW/WL");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[4-2]","verifying GSPS menu absence on point annotation after WW/WL");

		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[5-1]","verifying GSPS menu presence on point annotation before pan");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[5-2]","verifying GSPS menu absence on point annotation after pan");

		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[6-1]","verifying GSPS menu presence on point annotation before zoom ");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[6-2]","verifying GSPS menu absence on point annotation after zoom");

		/*
		 * Checking for circle
		 * 
		 */
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 50,30,30);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[7-1]","verifying GSPS menu presence on circle annotation before WW/WL");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[7-2]","verifying GSPS menu absence on circle annotation after WW/WL");

		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[8-1]","verifying GSPS menu presence on circle annotation before pan");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[8-2]","verifying GSPS menu absence on circle annotation after pan");

		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[9-1]","verifying GSPS menu presence on circle annotation before zoom");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[9-2]","verifying GSPS menu absence on circle annotation after zoom");

		/*
		 * Checking for ellipse
		 * 
		 */
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -100,50,60);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.verifyAcceptGSPSRadialMenu(),"Checkpoint[10-1]","verifying GSPS menu presence on ellipse annotation before WW/WL");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(ellipse.verifyAcceptGSPSRadialMenu(),"Checkpoint[10-2]","verifying GSPS menu absence on ellipse annotation after WW/WL");

		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.verifyAcceptGSPSRadialMenu(),"Checkpoint[11-1]","verifying GSPS menu presence on ellipse annotation before pan");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(ellipse.verifyAcceptGSPSRadialMenu(),"Checkpoint[11-2]","verifying GSPS menu absence on ellipse annotation after pan");

		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.verifyAcceptGSPSRadialMenu(),"Checkpoint[12-1]","verifying GSPS menu presence on ellipse annotation before zoom");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(ellipse.verifyAcceptGSPSRadialMenu(),"Checkpoint[12-2]","verifying GSPS menu absence on ellipse annotation after zoom");


		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-70, 0, 50,0);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(),"Checkpoint[13-1]","verifying GSPS menu presence on LinearMeasurement annotation before WW/WL");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(lineWithUnit.verifyAcceptGSPSRadialMenu(),"Checkpoint[13-2]","verifying GSPS menu absence on LinearMeasurement annotation after WW/WL");

		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(),"Checkpoint[14-1]","verifying GSPS menu presence on LinearMeasurement annotation before pan");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(lineWithUnit.verifyAcceptGSPSRadialMenu(),"Checkpoint[14-2]","verifying GSPS menu absence on LinearMeasurement annotation after pan");


		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyAcceptGSPSRadialMenu(),"Checkpoint[15-1]","verifying GSPS menu presence on LinearMeasurement annotation before zoom");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(lineWithUnit.verifyAcceptGSPSRadialMenu(),"Checkpoint[15-2]","verifying GSPS menu absence on LinearMeasurement annotation after zoom");

		/*
		 * Checking for Line Segment
		 * 
		 */
		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);		
		line.drawLine(1,-100, 70, 70,0);
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.verifyAcceptGSPSRadialMenu(),"Checkpoint[16-1]","verifying GSPS menu presence on Line Segment annotation before WW/WL");		
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertFalse(line.verifyAcceptGSPSRadialMenu(),"Checkpoint[16-2]","verifying GSPS menu absence on Line Segment annotation after WW/WL");

		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.verifyAcceptGSPSRadialMenu(),"Checkpoint[17-1]","verifying GSPS menu presence on Line Segment annotation before pan");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.assertFalse(line.verifyAcceptGSPSRadialMenu(),"Checkpoint[17-2]","verifying GSPS menu presence on Line Segment annotation after pan");


		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.verifyAcceptGSPSRadialMenu(),"Checkpoint[18-1]","verifying GSPS menu presence on Line Segment annotation before zoom");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100, -50);
		viewerPage.assertFalse(line.verifyAcceptGSPSRadialMenu(),"Checkpoint[18-2]","verifying GSPS menu presence on Line Segment annotation after zoom");

		/*
		 * Checking for polyline Segment
		 * 
		 */
		poly = new PolyLineAnnotation(driver);
		poly.closingConflictMsg();
		poly.selectPolylineFromQuickToolbar(2);		
		poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(2));
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(0));
		viewerPage.assertTrue(poly.verifyAcceptGSPSRadialMenu(),"Checkpoint[19-1]","verifying GSPS menu presence on polyline annotation before WW/WL");
		viewerPage.performSyncONorOFF();
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -100);
		viewerPage.assertFalse(poly.verifyAcceptGSPSRadialMenu(),"Checkpoint[19-2]","verifying GSPS menu absence on polyline annotation after WW/WL");

		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(2));
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(0));
		viewerPage.assertTrue(poly.verifyAcceptGSPSRadialMenu(),"Checkpoint[20-1]","verifying GSPS menu presence on polyline annotation before pan");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -50);
		viewerPage.assertFalse(poly.verifyAcceptGSPSRadialMenu(),"Checkpoint[20-2]","verifying GSPS menu absence on polyline annotation after pan");

		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(2));
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(0));
		viewerPage.assertTrue(poly.verifyAcceptGSPSRadialMenu(),"Checkpoint[21-1]","verifying GSPS menu presence on polyline annotation before zoom");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),0, 0, 100, -50);
		viewerPage.assertFalse(poly.verifyAcceptGSPSRadialMenu(),"Checkpoint[21-2]","verifying GSPS menu absence on polyline annotation after zoom");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test37_US476_TC2044_04_VerifyAnnotationGetsUnhighlightedOnViewerOperations() throws  InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS Finding state : Annotation-Menu - Verify that annotation commands should get stopped while performing menu operations.(\"GSPS Focus Finding\")");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		// Drawing  TextAnnotation
		String textAnnotation = "Automation_text";
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Text annotation");
		textAnn = new TextAnnotation(driver);
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -80, -60, textAnnotation);

		// Drawing  Linear Distance Measurement
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing Linear measurement annotation");
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-130, 0, 50,0);

		// Drawing  ellipse
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing ellipse annotation");
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -100,50,60);

		// Drawing  point		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing point annotation");
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, 90);

		//Drawing Circle
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing circle annotation");
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70,90,90);


		// verifying GSPS should get closed while applying viewer operations
		circle.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"verifying circle is Active and focused","verified");
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"verifying circle is Active and not focused","verified");

		// verifying GSPS should get closed while applying viewer operations
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		circle.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verifying ellipse is Active and focused","verified");
		viewerPage.dragAndReleaseOnViewer(0, 0, 10, -10);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"verifying ellipse is Active and not focused","verified");

		// verifying GSPS should get closed while applying viewer operations
		circle.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"verifying Linear measurement is Active and focused","verified");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 10, -10);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"verifying Linear measurement is Active and not focused","verified");

		// verifying GSPS should get closed while applying viewer operations
		lineWithUnit.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true),"Verify the text annotation is accepted","accepted");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, -10, 10);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true),"Verify the text annotation is accepted","accepted");

		// verifying GSPS should get closed while applying viewer operations
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.navigateGSPSForwardUsingKeyboard();		
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"verifying Linear measurement is Active and focused","verified");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 10, -10);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"verifying Linear measurement is Active and not focused","verified");

		// verifying GSPS should get closed while applying viewer operations
		lineWithUnit.navigateGSPSForwardUsingKeyboard();	
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verifying ellipse is Active and focused","verified");
		viewerPage.dragAndReleaseOnViewer(0, 0, -100, 50);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"verifying ellipse is Active and not focused","verified");

		// verifying GSPS should get closed while applying viewer operations
		lineWithUnit.navigateGSPSBackUsingKeyboard();		
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"verifying Linear measurement is Active and focused","verified");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.dragAndReleaseOnViewer(0, 0, 10, -10);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"verifying Linear measurement is Active and not focused","verified");

	} 

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test38_US476_TC2047_01_VerifyAnnotationSelectedOnDoubleClickOneUp() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Layout - Verify that annotation commands should get stopped while performing double click one-up");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);
		viewerPage.waitForViewerpageToLoad();

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -30, -40, 90);

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//select Point Annotation drawn on viewbox
		point.selectPoint(1, 1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[1/10]","Verify Point annotation is selected");

		//Double click on viewer and verify annotation is un-selected
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[2/10]","verify Point annotation de-selected on performing double click oneup scenario");

		//select linear measurement annotation drawn on viewbox
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[3/10]","Verify linear measurement annotation is selected");

		//Double click on viewer and verify annotation is un-selected
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[4/10]","verify linear measurement annotation is de-selectedon performing double click oneup scenario");

		//select ellipse annotation drawn on viewbox
		circle.selectCircle(1, 1);;
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[5/10]","Verify Circle annotation is selected");

		//Double click on viewer and verify annotation is un-selected
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/10]","verify Circle annotation is de-selected on performing double click oneup scenario");

		//select ellipse annotation drawn on viewbox
		ellipse.selectEllipse(1, 1);;
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[7/10]","Verify Ellipse annotation is selected");

		//Double click on viewer and verify annotation is un-selected
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/10]","verify Ellipse annotation is de-selected on performing double click oneup scenario");
	}


	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test38_US476_TC2047_03_VerifyAnnotationGSPSFocusDisappearOnDoubleClickOneUp() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Layout - Verify GSPS Foucs disappear on performing double click one-up");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 130, 130);

		//right click on Point Annotation drawn on viewbox
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[1/12]", "Verify GSPS radial menu appear on ViewBox1");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Checkpoint[2/12]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[3/10]","verify Point annotation is loses GSPS foucs on performing double click oneup scenario");

		//right click on Linear measurement Annotation drawn on viewbox
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[4/12]", "Verify GSPS radial menu appear on ViewBox1");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[5/12]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[6/10]","verify linear measurement annotation loses it's GSPS foucs on performing double click oneup scenario");

		//right click on Ellipse Annotation drawn on viewbox
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Checkpoint[7/12]", "Verify GSPS radial menu appear on ViewBox1");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Checkpoint[8/12]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[9/10]","verify Circle annotation loses it's GSPS foucs on performing double click oneup scenario");

		//right click on Circle Annotation drawn on viewbox
		ellipse.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Checkpoint[10/12]", "Verify GSPS radial menu appear on ViewBox1");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Checkpoint[11/12]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/12]","verify Circle annotation loses it's GSPS foucs on performing double click oneup scenario");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test38_US476_TC2047_04_VerifyAnnotationGSPSFindingStateDisappearOnDoubleClickOneUp() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Layout - Verify GSPS focus finding disappear on performing double click One-up scenario");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle  = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 40, 30);

		//press right arrow to move to next GSPS object		
		lineWithUnit.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[13/116]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[15/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[16/16]","verify linear measurement annotation loses it's GSPS foucs on performing double click oneup scenario");

		//press right arrow to move to next GSPS object
		lineWithUnit.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[1/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[3/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/16]","verify Point annotation is loses GSPS foucs on performing double click oneup scenario");

		//press right arrow to move to next GSPS object
		lineWithUnit.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[5/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[7/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/16]","verify Ellipse annotation loses it's GSPS foucs on performing double click oneup scenario");

		//press right arrow to move to next GSPS object
		lineWithUnit.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[9/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");

		//double click on viewer to perform One-up and verify annotation is no longer active GSPS object
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[11/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/16]","verify Circle annotation loses it's GSPS foucs on performing double click oneup scenario");


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test39_US476_TC2046_01_VerifyAnnotationGetsDeSelectedOnMesurementOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Mesurement - Verify that annotation commands should get stopped while performing mesurement operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -30, -40, 90);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[1/13]","Verify linear measurement annotation is selected");

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		viewerPage.assertFalse(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[2/13]","Verify linear measurement annotation is de-selected on drawing Point Annotation");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[3/13]","Verify Point annotation is selected");

		//select elipse from radial menu and draw a ellipse 
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[4/13]","Verify Point annotation is de-selected on drawing a circle annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[5/13]","Verify Ellipse annotation is selected");

		//select Circle Annotation from radial menu and draw a circle
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 50, 30, 50);
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/13]","Verify Ellipse annotation is de-selected on drawing a circle annotation");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[7/13]","verify Circle annotation is selected");

		//move the line and verify line is selected
		lineWithUnit.moveLinearMeasurement(1, 1, 20, 20);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[8/13]","Verify linear measurement annotation is selected");

		//resize the line and verify one the handle disppear
		line = new SimpleLine(driver);
		lineWithUnit.selectLinearMeasurement(1, 1);
		viewerPage.assertTrue(line.verifyLineHandleDisappearOnResizing(1, 2, 70, 80),"Checkpoint[9/13]","Verify linear measurement is selected on re-sizing");

		//move point annotation and verify its is selected
		point.movePoint(1, 1, 10, 20);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[10/13]","Verify Point annotation is selected on moving");

		//move circle annotation and verify it is selected
		circle.selectCircle(1, 1);
		circle.moveSelectedCircle(1, -10, 20);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[11/13]","verify Circle annotation is selected on moving");

		//move ellipse and verify it is selected.
		ellipse.selectEllipse(1, 1);
		ellipse.moveSelectedEllipse(1, -10, 20);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/13]","Verify Ellipse annotation is selected on moving");

		//resize selected ellipse and verify it is selected.
//		ellipse.selectEllipse(1, 1);
		ellipse.resizeEllipse(1,1, 10, 10);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[13/13]","Verify Ellipse annotation is de-selected on drawing a circle annotation");
	}

	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test39_US476_TC2046_03_VerifyGSPSFoucsDisappearOnPerformingMeasurementOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Measurement - Verify GSPS focus finding disappear on performing measurement operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -30, -40, 90);

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//right click on Point Annotation drawn on viewbox
		circle.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[1/15]", "Verify GSPS radial menu appear on ViewBox1");

		//move point annotation and verify annotation is no longer active GSPS object
		point.movePoint(1, 1, 10, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[2/15]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[3/15]","verify Point annotation is loses GSPS foucs on performing double click oneup scenario");

		//right click on Linear measurement Annotation drawn on viewbox
		circle.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[4/15]", "Verify GSPS radial menu appear on ViewBox1");

		//move the line and verify line is selected
		lineWithUnit.moveLinearMeasurement(1, 1, 20, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[5/15]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[6/15]","verify linear measurement annotation loses it's GSPS foucs on performing double click oneup scenario");

		//right click on Linear measurement Annotation drawn on viewbox
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[7/15]", "Verify GSPS radial menu appear on ViewBox1");

		//resize linear measurement and verify active GSPS annotation loses it s focus
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 30, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[8/15]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[9/15]","verify linear measurement annotation loses it's GSPS foucs on performing double click oneup scenario");

		//right click on Circle Annotation drawn on viewbox
		lineWithUnit.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[10/15]", "Verify GSPS radial menu appear on ViewBox1");

		//move circle annotation and verify it is selected
		circle.selectCircle(1, 1);
		circle.moveSelectedCircle(1, -10, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[11/15]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/15]","verify Circle annotation loses it's GSPS foucs on performing double click oneup scenario");

		//right click on Ellipse Annotation drawn on viewbox
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(viewerPage.isElementPresent(lineWithUnit.gspsPrevious), "Checkpoint[13/15]", "Verify GSPS radial menu appear on ViewBox1");

		//resize selected ellipse and verify it is selected.
		ellipse.selectEllipse(1, 1);
		ellipse.resizeEllipse(1, 1, 10,10);
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.gspsPrevious), "Checkpoint[14/15]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[15/15]","verify Circle annotation loses it's GSPS foucs on performing double click oneup scenario");

	}
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test39_US476_TC2046_04_VerifyAnnotationGSPSFindingStateDisappearOnPerformingMeasurementOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Measurement - Verify GSPS focus finding disappear on performing Measurement operation");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle = new  CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//press right arrow to move to next GSPS object
		circle.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[1/16]", "Verify GSPS radial menu appear on ViewBox1");
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[2/16]","verify Point annotation is current active GSPS object");
		//move point annotation and verify annotation is no longer active GSPS object
		point.movePoint(1, 1, 10, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[3/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[4/16]","verify Point annotation is loses GSPS foucs on performing double click oneup scenario");

		//press right arrow to move to next GSPS object
		circle.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[5/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[6/16]","verify Ellipse annotation is current active GSPS object");

		//resize selected ellipse and verify it is selected.
		ellipse.selectEllipse(1, 1);
		ellipse.resizeEllipse(1, 1, 10,10);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[7/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[8/16]","verify Ellipse annotation loses it's GSPS foucs on performing double click oneup scenario");

		//press right arrow to move to next GSPS object
		circle.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[9/16]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[10/16]","verify Circle annotation is current active GSPS object");

		//move circle annotation and verify it is selected
		circle.selectCircle(1, 1);
		circle.moveSelectedCircle(1, -10, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[11/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[12/16]","verify Circle annotation loses it's GSPS foucs on performing double click oneup scenario");

		//press right arrow to move to next GSPS object
		circle.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[13/116]", "Verify GSPS radial menu appear on ViewBox1");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[14/16]","verify linear measurement annotation is current active GSPS object");   

		//resize linear measurement and verify active GSPS annotation loses it s focus
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 30, 20);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.gspsPrevious), "Checkpoint[15/16]", "Verify GSPS radial menu close on performing double click oneup scenario");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[16/16]","verify linear measurement annotation loses it's GSPS foucs on performing double click oneup scenario");
	}
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test40_US476_TC2045_VerifyAnnotationGetsUnSelectedOnAnnotationOperations() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Rendering- Verify that annotation commands should get stopped while Rendering.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		line = new  SimpleLine(driver);
		point=new PointAnnotation(driver);
		textAnn=new TextAnnotation(driver);

		// Checking for TextAnnotation
		String textAnnotation = "Automation_text";
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 10, 20, textAnnotation);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[1]","verifying text annotation is selected before enabling current scrolled image input box");

		//Should un-select the annotation for enabling edit box
		viewerPage.click(viewerPage.getSliceInfo(1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1, true),"Checkpoint[2]","verifying text annotation is not selected after enabling current scrolled image input box");

		//Should disable Show-Hide Edit Box while enabling other edit box.
		viewBoxPanel=new ViewBoxToolPanel(driver);
		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "checkpoint[3] : Verifying the input is unselected when other input box is selected", "TC40_imageName1");

		//Should disable edit box while selecting other annotation.
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "checkpoint[4] : Verifying the input is unselected when other annotation is selected", "TC40_imageName2");

		//Should unselected while right clicking on any GSPS object to perform GspsFocusFinding
		//Should disable edit box while right clicking on any GSPS object to perform Gsps Focus Finding
		textAnn.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[5]","verifying text annotation is not selected after opening GSPS radial menu");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint[7]", "TC40_imageName3");

		//Should not in focus and GSPS radial menu should also close on enabling edit box
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true),"Checkpoint[8]","Verifying that GSPS radial menu is closed and annotation is not focused upon clicking on input box");

		//Should unselected while right clicking on any GSPS object to perform GspsFocusFinding
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		textAnn.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[9]","Verifying that annotation is not selected while getting the GSPS focus state");


		/*
		 * Checking for point
		 * 
		 */
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 120,90);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[10]","verifyivng point annotation is selected");
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[11]","verifying text annotation is not selected if one annotation is selected");

		viewerPage.click(viewBoxPanel.getZoomOverlay(1));
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[12]","verifying point annotation is not selected after enabling the zoom value input box");

		//Should unselected while selecting other annotation 
//		point.selectPoint(1, 1);
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[13]","verifying text annotation is not selected after point is selected");
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[14]","verifying point annotation is not selected after opening the GSPS radial menu using right click ");

//		point.selectPoint(1, 1);
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[15]","verifying point annotation is not selected after opening the GSPS radial menu using right click ");


		//Should not in focus and GSPS radial menu should also close while selecting other annotation 
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[16]","Verifying Point annotation is focused and selected");
		viewerPage.click(textAnn.getTextElementFromTextAnnotation(1, 1));
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[17]","verifying point annotation is not selected after selecting text annotation");
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"Checkpoint[18]","Verifying point annotation is now not focused but accepted");
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[19]","Verifying that GSPS radial menu is also closed");

		//Should not in focus and GSPS radial menu should also close while right clicking on other GSPS object to perform Gsps Focus Finding
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[20]","Point is focused and active post opening GSPS radial menu using right click");
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[21]","verifying point annotation is not selected post GSPS finding state");
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"Checkpoint[22]","Verifying point is inactive and unfocused");
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[23]","Radial GSPS menu is shifted to another annotation");

		//Should not in focus and GSPS radial menu should also close while clicking left/right key press to perform Gsps Focus Finding for previous GSPS object
//		point.selectPoint(1, 1);
		point.openGSPSRadialMenu(textAnn.getTextElementFromTextAnnotation(1, 1));
		point.navigateGSPSBackUsingKeyboard();
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true),"Checkpoint[24]","verifying that text annotation is not focused post GSPS finding state");
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(),"Checkpoint[25]","Radial GSPS menu is shifted to another annotation");

		/*
		 * Checking for circle
		 * 
		 */
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 60, 50,30,30);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[26]","verifying circle annotation is selected");
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[27]","verifying text annotation is not selected as circle is selected");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[28]","verifying point annotation is not selected as circle is selected");

		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[29]","verifying circle annotation is not selected after enabling the input edit box");

//		circle.selectCircle(1, 1);
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[30]","verifying circle annotation is not selected after opening GSPS radial menu");

//		circle.selectCircle(1, 1);
		circle.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[31]","verifying circle annotation is not selected after GSPS finding state");
		
		//Should not in focus and GSPS radial menu should also close on enabling edit box
		circle.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint[32]","verifying point is active and focused post GSPS fidning state");
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"Checkpoint[34]","verifying point is also not focused");
		viewerPage.assertFalse(circle.verifyAcceptGSPSRadialMenu(),"Checkpoint[35]","verifying GSPS radial menu is closed");

		/*
		 * Checking for ellipse
		 * 
		 */
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);

		ellipse.drawEllipse(1, -100, -100,50,60);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[36]","verifying ellipse annotation is selected");
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[37]","verifying text annotation is not selected as another annotation is selected");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[38]","verifying point annotation is not selected as another annotation is selected");
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[39]","verifying circle annotation is not selected as another annotation is selected");

		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[40]","verifying ellipse annotation is not selected after enabling of edit box");

//		ellipse.selectEllipse(1, 1);
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[41]","verifying ellipse annotation is not selected as GSPS radial menu is opened");

//		ellipse.selectEllipse(1, 1);
		ellipse.navigateGSPSBackUsingKeyboard();
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[42]","verifying ellipse annotation is not selected after GSPS fidning state");


		/*
		 * Checking for LinearMeasurement
		 * 
		 */
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);		
		lineWithUnit.drawLine(1,-70, 0, 50,0);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[43]","verifying LinearMeasurement annotation is selected");
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,1,true),"Checkpoint[44]","verifying text annotation is not selected as another annotation is selected");
		viewerPage.assertFalse(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[45]","verifying point annotation is not selectedas another annotation is selected");
		viewerPage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[46]","verifying circle annotation is not selected as another annotation is selected");
		viewerPage.assertFalse(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[47]","verifying ellipse annotation is not selected as another annotation is selected");

		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.assertFalse(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[48]","verifying LinearMeasurement annotation is not selected as edit box is enabled");

//		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[49]","verifying LinearMeasurement annotation is not selected as GSPS radial menu is opened");

//		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.navigateGSPSBackUsingKeyboard();
		viewerPage.assertFalse(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1,1),"Checkpoint[50]","verifying LinearMeasurement annotation is not selected after GSPS fidning state");

		/*
		 * Checking for Line Segment
		 * 
		 */
		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);		
		line.drawLine(1, -100, 70, 70,0);
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[51]","verifying Line Segment annotation is selected");

		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.assertFalse(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[52]","verifying Line Segment annotation is not selected post edit box is enabled");

		//		line.selectLine(1, 1);
		line.openGSPSRadialMenu(line.getLine(1,1).get(0));
		viewerPage.assertTrue(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[53]","verifying Line Segment annotation is not selected as GSPS radial menu is opened");

//		line.selectLine(1, 1);
		line.navigateGSPSBackUsingKeyboard();
		viewerPage.assertFalse(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[54]","verifying Line Segment annotation is not selected after GSPS finding state");


		/*
		 * Checking for polyline Segment
		 * 
		 */
		poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);	
		poly.closingConflictMsg();
		poly.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});
		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[56]","verifying LinearMeasurement annotation is selected");

		viewerPage.click(viewerPage.getSliceInfo(1));
		viewerPage.assertFalse(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[57]","verifying LinearMeasurement annotation is not selected post edit box is enabled");

		poly.selectClassicPolylineWithCtrlLeft(1, 1);
		viewerPage.assertFalse(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[58]","verifying Line Segment annotation is not selected");

		poly.selectClassicPolylineWithCtrlLeft(1, 1);
		viewerPage.assertFalse(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[59]","verifying LinearMeasurement annotation is not selected after opening GSPS radial menu");

		poly.navigateGSPSBackUsingKeyboard();
		viewerPage.assertFalse(line.verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint[60]","verifying LinearMeasurement annotation is not selected after GSPS finding state");




	}

	public boolean verifyGSPSObjectPresence(int whichViewbox){
		int count = 0;
		boolean status = false ;

		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		TextAnnotation textAn = new TextAnnotation(driver);
		SimpleLine line = new  SimpleLine(driver); 
		poly = new PolyLineAnnotation(driver);

		count = line.getAllLines(whichViewbox).size() + circle.getAllCircles(whichViewbox).size() + ellipse.getEllipses(whichViewbox).size() 
				+ textAn.getTextAnnotations(whichViewbox).size() + point.getAllPoints(whichViewbox).size()+poly.getAllPolylines(whichViewbox).size();

		if(count>0)
			status = true ;

		return status;
	}
}
