package com.trn.ns.test.viewer.PMAP;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PMAPSupportTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private OutputPanel panel;
	private ContentSelector cs;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit linewithunit;
	private PointAnnotation point;
	static String patient_id="";
	private HelperClass helper;
	private PMAP pmap;
	private ViewerSliderAndFindingMenu findingMenu;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset ;


	String filePath1 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);

	String filePath2 =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 =Configurations.TEST_PROPERTIES.get("S2008-3CTP_Filepath");
	String s2008PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filepath4 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath4);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");


	@Test(groups ={"Chrome","Edge","IE11","US1043","F141","US1620","Sanity","BVT","E2E","F631"})
	public void test01_US1043_TC5944_TC5943_TC5988_US1620_TC8389_verifyPMAPData() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of PMAPs series load  in the viewer"
				+ "<br> Verify PMAPs series with icon in the content selector."
				+ "<br> Verify PMAPs in result series in the content selector. <br>"+
				"Verify PMAP-Data  Display with  the Original LUT bar ( pre-defined LUT)");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
	
		ContentSelector cs = new ContentSelector(driver);
		cs.compareElementImage(protocolName, cs.getViewPort(1), "Checkpoint[1/6] : Verifying the result", "test01_01");
		cs.closeNotification();
		
		List<String> series = cs.getAllSeries();
		List<String> result = cs.getAllResults();

		viewerpage.assertTrue(series.contains(viewerpage.getSeriesDescriptionOverlayText(1)), "Checkpoint[2/6]", "Verifying the series description is same as viewer");
		int count =0;
		for(int i =0;i<series.size();i++)
			if(cs.verifyPresenceOfEyeIcon(series.get(i))) {
				cs.assertTrue(cs.verifyPresenceOfEyeIcon(series.get(i)), "Checkpoint[3/6]", "Verifying the series is selected");
				count=i;
			}

		//		cs.assertTrue(cs.verifyPresenceOfEyeIcon(1, result.get(0)), "Checkpoint[4/6]", "Verifying the result is selected");
		cs.openAndCloseSeriesTab(true);
		cs.assertTrue(cs.validateIconPresenceInSeriesTab(result.get(0),ViewerPageConstants.PMAP_ICON,ViewerPageConstants.PMAP), "Checkpoint[5/6]", "verifying the PMAP icon");	
		cs.selectSeriesFromSeriesTab(1, series.get(count));
		cs.compareElementImage(protocolName, cs.getViewPort(1), "Checkpoint[6/6]: Verifying the source series", "test01_02");

	}

	//US1046: Accept/Reject PMAP
	@Test(groups ={"Chrome","Edge","IE11","US1046","F141","US1411","DE1928","F350","Positive","BVT","E2E"})
	public void test02_US1046_TC6280_US1411_TC7760_DE1928_verifyAcceptRejectForPMAPData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP can be accepted/rejected using AR toolbar.<br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]. <br>"+
				"Verify the AR tool bar is displayed for dicom series.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertFalse(findingMenu.isAcceptRejectToolBarPresent(1), "Checkpoint[1/4]", "Verified that A/R toolbar not visible when PMAP result default loaded");

		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerpage.assertFalse(findingMenu.verifyResultsAreAccepted(1)&& findingMenu.verifyResultsAreRejected(1), "Checkpoint[2/4]", "Verified that PMAP data not in Accepted or Rejected state");

		//select accept from A/R toolbar
		findingMenu.selectAcceptfromGSPSRadialMenu();
		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[3/4]", "Verified that PMAP data is in Accepted state after selecting Accept icon from A/R toolbar");

		//reload viewer page and verify state of PMAP data
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer("", pmapPatientID, 1, 1);
		
		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[3.a/4]", "Verified that PMAP data is in Accepted state after reload of viewer page");
		viewerpage.assertFalse(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).isEmpty(), "Checkpoint[2.c/3]", "Verified state of PMAP result in finding menu as Green");

		//select Reject from A/R toolbar
		findingMenu.selectRejectfromGSPSRadialMenu();
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[4/4]", "Verified that PMAP data is in Rejected state after selecting Reject icon from A/R toolbar");

		//reload viewer page and verify state of PMAP data
		helper.browserBackAndReloadViewer("", pmapPatientID, 1, 1);
		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[4.a/4]", "Verified that PMAP data is in Rejected state after reload of viewer page");
		viewerpage.assertFalse(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR).isEmpty(), "Checkpoint[3.c/3]", "Verified state of PMAP result in finding menu as Red");
	}	

	@Test(groups ={"Chrome","Edge","IE11","US1046","Positive","F141"})
	public void test03_US1046_TC6282_verifyFindingListWithPMAPPlusGSPS() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding list for PMAP + GSPS");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		cs=new ContentSelector(driver);
		viewerpage.waitForViewerpageToLoad();
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		String seriesToSelect=viewerpage.getSeriesDescriptionOverlayText(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,-200, -200, -300, -200);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 150, 150,-80,-80);
		circle.selectRejectfromGSPSRadialMenu();

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, -50, 40, -50);
		ellipse.selectAcceptfromGSPSRadialMenu();

		//verify finding state for PMAP as well as GSPS finding
		viewerpage.assertEquals(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1,"Checkpoint[1/8]", "Verified state of gsps finding in finding menu as Green");
		viewerpage.assertEquals(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1,"Checkpoint[2/8]", "Verified state of gsps finding in finding menu as Red");
		viewerpage.assertEquals(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.PENDING_FINDING_COLOR).size(),2,"Checkpoint[3/8]", "Verified state of gsps finding and PMAP result in finding menu as Pending");

		//verify source and result series for PMAP data
		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.waitForAllChangesToLoad();

		cs.selectSeriesFromSeriesTab(2, seriesToSelect);
		cs.openAndCloseSeriesTab(false);
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[4/8]", "Verified that gradient bar is visible for result series only.");
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(2)), "Checkpoint[5/8]", "Verified that gradient bar is not visible for series.");
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.assertEquals(findingMenu.getBadgeCountFromToolbar(1), 2, "Checkpoint[6/8]", "Verified badge count of pending finding from finding menu.");

		//accept the PMAP data after selecting from finding menu
		findingMenu.selectAcceptfromGSPSRadialMenu();
		viewerpage.assertEquals(findingMenu.getBadgeCountFromToolbar(1), 1, "Checkpoint[7/8]", "Verified badge count of pending finding from finding menu after accepting the PMAP result");

		//reject the PMAP data after selecting from finding menu
		findingMenu.selectRejectfromGSPSRadialMenu();
		viewerpage.assertEquals(findingMenu.getBadgeCountFromToolbar(1), 1, "Checkpoint[8/8]", "Verified badge count of pending finding from finding menu after rejecting the PMAP result");







	}

	@Test(groups ={"Chrome","Edge","IE11","US1046","Positive","BVT","F141","E2E"})
	public void test04_US1046_TC6283_verifyNavigationUsingNextAndPreviousArrowFromARToolbar() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify navigation through findings - GSPS + PMAP by clicking on prev-next arrows from AR toolbar");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		cs=new ContentSelector(driver);
		viewerpage.waitForViewerpageToLoad();
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,-20, -50, 120, 150);

		viewerpage.scrollToImage(1, 12);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
		circle.selectRejectfromGSPSRadialMenu();

		//verify finding state for PMAP as well as GSPS finding using Next arrow from A/R toolbar
		findingMenu.selectFindingFromTable(1, 1);
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		findingMenu.selectNextfromGSPSRadialMenu();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[1/12]", "Verified that PAMP result is de-selected and focus jump to linear measurement annotation using arrow next");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2), "Checkpoint[2/12]", "Verified that linear measurement finding is highlighted in finding menu.");

		findingMenu.selectNextfromGSPSRadialMenu();
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[3/12]", "Verified that linear measurement is de-selected and focus jump to circle annotation using arrow next ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(3), "Checkpoint[4/12]", "Verified that circle annotation is highlighted in finding menu.");

		findingMenu.selectNextfromGSPSRadialMenu();
		viewerpage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[5/12]", "Verified that circle is de-selected and focus jump to PAMP result using arrow next ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1), "Checkpoint[6/12]", "Verified that PMAP result is highlighted in finding menu.");

		//using previous arrow from A/R toolbar
		findingMenu.selectPreviousfromGSPSRadialMenu();
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[7/12]", "Verified that PAMP result is de-selected and focus jump to circle annotation using arrow previous");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(3), "Checkpoint[8/12]", "Verified that circle annotation is highlighted in finding menu.");

		findingMenu.selectPreviousfromGSPSRadialMenu();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[9/12]", "Verified that circle annotation is de-selected and focus jump to linear measurement using arrow previous ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2), "Checkpoint[10/12]","Verified that linear measurement finding is highlighted in finding menu.");

		findingMenu.selectPreviousfromGSPSRadialMenu();
		viewerpage.assertFalse(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[11/12]",  "Verified that linear measurment annotation is de-selected and focus jump to PMAP result using arrow previous ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1), "Checkpoint[12/12]",  "Verified that PMAP result is highlighted in finding menu.");


	}

	@Test(groups ={"chrome","Edge","IE11","US1046","Positive","F141"})
	public void test05_US1046_TC6283_verifyNavigationUsingNextAndPreviousArrowFromKeyboard() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify navigation through findings - GSPS + PMAP by clicking on prev-next arrows from keyboard");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		cs=new ContentSelector(driver);
		viewerpage.waitForViewerpageToLoad();
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);

		//String seriesToSelect=viewerpage.getSeriesDescriptionOverlayText(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,-20, -50, 120, 150);

		viewerpage.scrollToImage(1, 12);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
		circle.selectRejectfromGSPSRadialMenu();
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		//verify finding state for PMAP as well as GSPS finding using Next arrow from A/R toolbar
		findingMenu.selectFindingFromTable(1, 1);
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		findingMenu.navigateGSPSForwardUsingKeyboard();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[1/12]", "Verified that PAMP result is de-selected and focus jump to linear measurement annotation using arrow next from keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2), "Checkpoint[2/12]", "Verified that linear measurement finding is highlighted in finding menu.");

		findingMenu.navigateGSPSForwardUsingKeyboard();
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[3/12]", "Verified that linear measurement is de-selected and focus jump to circle annotation using arrow next fom keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(3), "Checkpoint[4/12]", "Verified that circle annotation is highlighted in finding menu.");

		findingMenu.navigateGSPSForwardUsingKeyboard();
		viewerpage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[5/12]", "Verified that circle is de-selected and focus jump to PAMP result using arrow next from keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1), "Checkpoint[6/12]", "Verified that PMAP result is highlighted in finding menu.");

		//using previous arrow from A/R toolbar
		findingMenu.navigateGSPSBackUsingKeyboard();
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[7/12]", "Verified that PAMP result is de-selected and focus jump to circle annotation using arrow previous from keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(3), "Checkpoint[8/12]", "Verified that circle annotation is highlighted in finding menu.");

		findingMenu.navigateGSPSBackUsingKeyboard();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[9/12]", "Verified that circle annotation is de-selected and focus jump to linear measurement using arrow previous from keyboard ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2), "Checkpoint[10/12]",  "Verified that linear measurement finding is highlighted in finding menu.");

		findingMenu.navigateGSPSBackUsingKeyboard();
		viewerpage.assertFalse(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[11/12]", "Verified that linear measurment annotation is de-selected and focus jump to PMAP result using arrow previous from keyboard ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1), "Checkpoint[12/12]", "Verified that PMAP result is highlighted in finding menu.");


	}	

	@Test(groups ={"Chrome","Edge","IE11","US1046","Positive","F141"})
	public void test06_US1046_TC6283_verifyNavigationUsingPageUpAndPageDown() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify navigation through findings - GSPS + PMAP using Page Up and Page Down from keyboard");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		cs=new ContentSelector(driver);
		viewerpage.waitForViewerpageToLoad();
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,-20, -50, 120, 150);

		viewerpage.scrollToImage(1, 12);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
		circle.selectRejectfromGSPSRadialMenu();

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		//verify finding state for PMAP as well as GSPS finding using Next arrow from A/R toolbar
		findingMenu.selectFindingFromTable(1, 1);
		findingMenu.scrollDownGSPSUsingKeyboard();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[1/12]", "Verified that PAMP result is de-selected and focus jump to linear measurement annotation using page down from keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2),"Checkpoint[2/12]", "Verified that linear measurement finding is highlighted in finding menu.");

		findingMenu.scrollDownGSPSUsingKeyboard();
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Checkpoint[3/12]", "Verified that linear measurement is de-selected and focus jump to circle annotation using page down fom keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(3),"Checkpoint[4/12]", "Verified that circle annotation is highlighted in finding menu.");

		findingMenu.scrollDownGSPSUsingKeyboard();
		viewerpage.assertFalse(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Checkpoint[5/12]", "Verified that circle is de-selected and focus jump to PAMP result using page down from keyboard ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1),"Checkpoint[6/12]", "Verified that PMAP result is highlighted in finding menu.");

		//using previous arrow from A/R toolbar

		findingMenu.scrollUpGSPSUsingKeyboard();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[9/12]", "Verified that circle annotation is de-selected and focus jump to linear measurement using page up from keyboard ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2),"Checkpoint[10/12]",  "Verified that linear measurement finding is highlighted in finding menu.");

		findingMenu.scrollUpGSPSUsingKeyboard();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[11/12]", "Verified that linear measurment annotation is de-selected and focus jump to PMAP result using page up from keyboard ");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1),"Checkpoint[12/12]", "Verified that PMAP result is highlighted in finding menu.");

		findingMenu.scrollUpGSPSUsingKeyboard();
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Checkpoint[7/12]", "Verified that PAMP result is de-selected and focus jump to circle annotation using page up from keyboard");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(3),"Checkpoint[8/12]", "Verified that circle annotation is highlighted in finding menu.");


	}	

	@Test(groups ={"Chrome","Edge","IE11","US1046","US1411","Positive","F141","F350"})
	public void test07_US1046_TC6284_US1411_TC7760_verifyNavigationByAcceptingRejectingOnPMAPData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify finding list for PMAP + GSPS. <br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		cs=new ContentSelector(driver);
		viewerpage.waitForViewerpageToLoad();
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,-20, -50, 120, 150);

		viewerpage.scrollToImage(1, 12);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
		circle.selectRejectfromGSPSRadialMenu();

		viewerpage.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Checkpoint[1/4]", "Verified that circle annotation is de-selected and focus shift to PMAP result frinding.");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(1), "Checkpoint[2/4]", "Verified that  PAMP result finding is highlighted in finding table");

		circle.selectAcceptfromGSPSRadialMenu();
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Checkpoint[3/4]", "Verified that PAMAP result is  de-selected and focus shift to linear measurement frinding.");
		viewerpage.assertTrue(findingMenu.verifyFindingIsHighlighted(2), "Checkpoint[4/4]", "Verified that linear measurment finding is highlighted in finding table");

	}

	//US1044:Display Pmap in viewer as overlay

	@Test(groups ={"Chrome","Edge","IE11","US1044","US1048","DR2280","Positive","F141"})
	public void test11_US1044_TC6074_TC6076_TC6081_US1048_TC6206_TC6304_TC6285_DR2280_TC6074_verifyAcceptRejectForPMAPDataAndDefaultLoading() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP series loads  in the viewer <br>"+
				"Verify User able to toggle 'ON' or 'OFF' the PMAP overlay in the viewer using the 'G' shortcut key <br>"+
				"Verify user able to load PMAPs  from the result series in the content selector.<br>"+
				"Pmap data- Verify if Pmap data is loaded correctly on viewer with all the source and result series being displayed in content selector <br>"+
				"Pmap data: Verify if 'G' button from keyboard is pressed, toggle the GSPS annotation along with the pmap series overlayed.<br> "+
				"[Risk and Impact] US1044 - TC6074, TC6081");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();	
		cs=new ContentSelector(driver);
		point=new PointAnnotation(driver);

		String seriesToSelect=viewerpage.getSeriesDescriptionOverlayText(1);
		String resultToSelect=cs.getAllResults().get(0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),1,"Checkpoint[1/13]", "Verified that default loaded layout is 1*1");
		pmap=new PMAP(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP series and PMAP result series is highlighted in content selector after toggle off from Keyboard" );
		viewerpage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[2/13]", "Verified that Source series loaded without the PMAP overlay");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(seriesToSelect), "Checkpoint[3/13]", "Verified that source series is selected on Content selector");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(resultToSelect), "Checkpoint[4/13]", "Verified that PMAP series still be selected on content selector under result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP series and PMAP result series is highlighted in content selector after toggle on from Keyboard" );
		viewerpage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[5/13]", "Verified that Source series loaded without the PMAP overlay");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(seriesToSelect), "Checkpoint[6/13]", "Verified that source series is selected on Content selector");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(resultToSelect), "Checkpoint[7/13]", "Verified that PMAP series still be selected on content selector under result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP series is highlighted and PMAP result series is not highlighted in content selector after selecting source from content selector");
		viewerpage.click(viewerpage.getViewPort(1));
		cs.selectSeriesFromSeriesTab(1, seriesToSelect);
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[8/13]", "Verified that Source series loaded without the PMAP overlay");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(seriesToSelect), "Checkpoint[9/13]", "Verified that source series is selected on Content selector");
		viewerpage.assertFalse(cs.verifyPresenceOfEyeIcon(resultToSelect), "Checkpoint[10/13]", "Verified that PMAP series not selected on content selector under result tab");	

		cs.selectResultFromSeriesTab(1, resultToSelect);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		viewerpage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[11/13]", "Verified that point annotation is current active accepted GSPS");

		viewerpage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[12/13]", "Verified that Source series loaded without the PMAP overlay after toggle the GSPS annotation along with PMAP overlay");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(seriesToSelect), "Checkpoint[13/13]", "Verified that source series is selected on Content selector after toggle off");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1044","US1048","Positive","F141"})
	public void test12_US1044_TC6078_US1048_TC6304_verifyToggleOnOffUsingResultApplied() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User able to toggle 'ON' or 'OFF' the PMAP overlay in the viewer by clicking 'Result Applied' annotation.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		cs=new ContentSelector(driver);
		pmap=new PMAP(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result series is not-highlighted after toggle off from Keyboard" );
		viewerpage.click(viewerpage.resultApplied(1));
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[1/4]", "Verified that Source series loaded without the PMAP overlay");
		viewerpage.assertTrue(viewerpage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[2/4]", "Result Applied is de-highlighted when PMAP source series is loaded in viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP result series is highlighted after toggle on from Keyboard" );
		viewerpage.click(viewerpage.resultApplied(1));
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[3/4]", "Verified that Source series loaded with the PMAP overlay");
		viewerpage.assertTrue(viewerpage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Checkpoint[4/4]", "Result Applied is highlighted when PMAP overlay is loaded in viewbox1");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1044","US1048","US1620","Positive","F141","F631","E2E"})
	public void test13_US1044_TC6080_TC6077_US1048_TC6304_US1620_TC8389_verifyPMAPDisplayOnAllImages() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP is displayed on all images available in the series <br>"
				+ "Verify LUT bar display on the viewer when PMAP data is loaded. <br>"+
				"Verify PMAP-Data  Display with  the Original LUT bar ( pre-defined LUT)");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();		
		cs=new ContentSelector(driver);
		db=new DatabaseMethods(driver);
		int maxScroll=viewerpage.getMaxNumberofScrollForViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		cs = new ContentSelector(driver);
		pmap=new PMAP(driver);
		List<String> result = cs.getAllResults();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[1/4]","Verified maximum value of LUT bar with DB");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[2/4]","Verified minimum value of LUT bar with DB");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify PMAP series is displayed across all images in the series" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify gradient bar and result applied text across all slices." );
		for(int i=1;i<=maxScroll;i++)
		{
			viewerpage.scrollToImage(1, i);
			viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[3."+i+"/4]", "Verified that Source series loaded with the PMAP overlay and LUT bar displayed on it");
			viewerpage.assertTrue(viewerpage.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Checkpoint[4."+i+"/4]", "Result Applied is highlighted when PMAP overlay is loaded in viewbox1");

		}

	}

	@Test(groups ={"Chrome","Edge","IE11","US1044","US1048","Positive","F141"})
	public void test14_US1044_TC6141_TC6145_US1048_TC6304_TC6305_verifyToggleOnOffinMultipleViewboxes() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User able to toggle 'ON' or 'OFF' the PMAP overlay in the viewer when PMAP is loaded in multiple viewbox <br>"
				+"Verify user able load the PMAP source series thru content selector when when PMAP data loaded in more than one viewbox <br>"
				+"DE1517: Verify LUT bar is not displayed if source series is loaded in view box");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		cs=new ContentSelector(driver);

		String seriesToSelect=viewerpage.getSeriesDescriptionOverlayText(1);
		String resultToSelect=cs.getAllResults().get(0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),1,"Checkpoint[1/7]", "Verified that default loaded layout is 1*1");

		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		cs.selectResultFromSeriesTab(2, resultToSelect);
		for(int i=1;i<=cs.getNumberOfCanvasForLayout();i++)
		{
			viewerpage.click(viewerpage.getViewPort(i));		
			viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(seriesToSelect), "Checkpoint[2."+i+"/7]", "Verified that source series is selected on Content selector");
			viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(resultToSelect), "Checkpoint[3."+i+"/7]", "Verified that PMAP series still be selected on content selector under result tab");
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verified toggle ON or OFF the PMAP overlay in the viewer when PMAP is loaded in multiple viewbox" );
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.toggleOnOrOffResultUsingKeyboardGKey();
		pmap=new PMAP(driver);
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[4/7]", "Verified that Source series loaded without the PMAP overlay in active viewbox after toggle off");
		viewerpage.assertTrue(viewerpage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[5/7]", "Result Applied is de-highlighted when PMAP source series is loaded in viewbox1");

		//In inactive viewbox 
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(2)), "Checkpoint[6/7]", "Verified that Source series loaded with the PMAP overlay in non-active viewbox after toggle off in acive viewbox");
		viewerpage.assertTrue(viewerpage.verifyResultAppliedToggle(2,NSGenericConstants.ON), "Checkpoint[7/7]", "Result Applied is de-highlighted when PMAP source series is loaded in viewbox1");
	}

	// US1048: Support GSPS on Pmap

	@Test(groups ={"Chrome","Edge","IE11","US1048","Positive","F141"})
	public void test15_US1048_TC6207_TC6208_TC6210_verifyGSPSFunctionalityOnPMAPData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Pmap data- Verify if user is able to draw GSPS annotations on Pmap data from desktop <br>"
				+"Pmap data- Verify if clone copy is created with user drawn annotation and is being displayed in 'Content selector->Results' tab <br>"+
				"Pmap data- Verify if GSPS finding is displayed on finding menu from A/R tool bar on desktop");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();

		cs=new ContentSelector(driver);
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);
		point=new PointAnnotation(driver);
		circle.waitForViewerpageToLoad();
		

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		circle.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[1/13]", "Verified that point annotation is current active accepted GSPS");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[2/13]", "Verified that ellipse annotation is current active accepted GSPS");

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,5, 5,-80,-80);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[3/13]", "Verified that circle annotation is current active accepted GSPS");

		circle.click(circle.getViewPort(1));
		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,20,0,60,0);
		linewithunit.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[4/13]", "Verified that linear measurement annotation is current active accepted GSPS");

		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Checkpoint[5/13]", "Verified that circle annotation is active rejected GSPS");

		circle.selectAcceptfromGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));
		circle.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(1, 1), "Checkpoint[6/13]", "Verified that ellipse annotation is current pending GSPS");

		String resultDesc=ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1";
		circle.assertTrue(cs.verifyPresenceOfEyeIcon(resultDesc), "Checkpoint[7/13]", "Verified that new clone is created in content selector after drawing annotation");

		helper.browserBackAndReloadViewer("", pmapPatientID, 1, 1);
		linewithunit.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[8/13]", "Verified that linear measurement annotation is current active accepted GSPS");
		linewithunit.assertTrue(cs.verifyPresenceOfEyeIcon(resultDesc), "Checkpoint[9/13]", "Verified that new clone is created in content selector after drawing annotation");

		circle.selectFindingFromTable(1, 1);
		circle.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[10/13]", "Verified that linear measurement annotation is current active accepted GSPS");

		circle.selectFindingFromTable(2);
		circle.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[11/13]", "Verified that point annotation is current active accepted GSPS");

		circle.selectFindingFromTable(3);
		circle.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[12/13]", "Verified that ellipse annotation is current pending GSPS");

		circle.selectFindingFromTable(4);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[13/13]", "Verified that circle annotation is current active rejected GSPS");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1048","Positive","BVT","F141","E2E"})
	public void test16_US1048_T6209_verifyGSPSAnnotationOnFindingSliderForPMAPData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Pmap data- Verify if GSPS annotation is shown on finding slider with corresponding finding color marker on desktop");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		ellipse=new EllipseAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		List<WebElement> findingMarkersOnslider = findingMenu.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[1/6]","Verifying there is one marker present on slider as results is accepted on a slice");
		viewerpage.assertTrue(findingMenu.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[2/6]","Verifying the state of markers - Green");

		linewithunit.selectDistanceFromQuickToolbar(1);
		linewithunit.drawLine(1,20,0,60,0);
		linewithunit.selectAcceptfromGSPSRadialMenu();
		findingMarkersOnslider = findingMenu.getFindingMarkersOnSlider(1);
		viewerpage.assertEquals(findingMarkersOnslider.size(),1,"Checkpoint[3/6]","Verifying there is one marker present on slider as results is pending on a slice");

		findingMenu.getFindingMarkersOnSlider(1);
		findingMenu.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);	
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Checkpoint[4/6]", "Verified that linear measurement annotation is current active pending GSPS");

		findingMenu.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);	
		viewerpage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/6]", "Verified that ellipse annotation is current active accepted GSPS");

		findingMenu.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);
		viewerpage.assertTrue(linewithunit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Checkpoint[6/6]", "Verified that linear measurement annotation is current active pending GSPS");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1221","DR2280","Positive","BVT","F141","E2E"})
	public void test17_US1221_TC6276_TC6292_TC6368_DR2280_TC9202_verifyWindowLevelTopToBottonAndViceVersa() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse Up/Down movement in the viewer when  PMAP data is loaded  and LUT bar displayed on the viewer"
				+ "<br> Verify user able to reset to default values in the LUT bar after changing the values by moving mouse Up/Down or Left/Right"
				+ "<br> Verify Reset menu pops up the Desktop viewer when user clicks on the LUT bar and user able to reset the PMAP values. <br>"+
				"[Risk and Impact] US1221 - TC6276 + zoom + scroll");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		db=new DatabaseMethods(driver);		
		pmap=new PMAP(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);

		cs = new ContentSelector(driver);

		List<String> result = cs.getAllResults();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[1/16]","Verified maximum value of LUT bar with DB");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[2/16]","Verified minimum value of LUT bar with DB");

		String originalmiddleValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1));
		String originalmaxValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0));
		String originalminValue = viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2));

		int middleValue = Integer.parseInt(originalmiddleValue);
		float maxValue = Float.parseFloat(originalmaxValue);
		float minValue = Float.parseFloat(originalminValue);

		viewerpage.assertEquals(middleValue, Math.round((maxValue+minValue)/2), "Checkpoint[3/16]", "Verifying that middle range is round of (min + max)/2");

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowCenterValueOverlayText(1);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 200,0, -200);

		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))>maxValue, "Checkpoint[4/16]","Verifying the maximum value has changed on LUT bar");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)))>middleValue, "Checkpoint[5/16]","Verifying the middle value has changed on LUT bar");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))>minValue, "Checkpoint[6/16]","Verifying the minimum value has changed on LUT bar");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[7/16]", "Verifying that window width is not changed after performing the WWWL on PMAP");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[8/16]", "Verifying that window center is not changed after performing the WWWL on PMAP");

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.getViewPort(1),0,-200,0, 300);

		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))< maxValue, "Checkpoint[9/16]","Verifying that maximum value on bar has reduced when drag perfromed from top to bottom");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)))<middleValue, "Checkpoint[10/16]","Verifying that middle value on bar has reduced when drag perfromed from top to bottom");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))<minValue, "Checkpoint[11/16]","Verifying that minimum value on bar has reduced when drag perfromed from top to bottom");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[12/16]", "Verifying no change on WWWL parameters are not changed");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[13/16]", "Verifying no change on WWWL parameters are not changed");

		viewerpage.click(pmap.getGradientBar(1));
		viewerpage.click(pmap.resetButton);

		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)),originalmaxValue, "Checkpoint[14/16]","Verifying that maximum values has reset on click of reset on LUT bar");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)),originalmiddleValue, "Checkpoint[15/16]","Verifying that middle values has reset on click of reset on LUT bar");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)),originalminValue, "Checkpoint[16/16]","Verifying that minimum values has reset on click of reset on LUT bar");



	}

	@Test(groups ={"Chrome","Edge","IE11","US1221","Positive","F141"})
	public void test18_US1221_TC6288_verifyWindowLevelRightToLeftAndViceVersa() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse Left/Right movement in the viewer when  PMAP data is loaded  and LUT bar displayed on the viewer");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		db=new DatabaseMethods(driver);
		pmap=new PMAP(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);

		cs = new ContentSelector(driver);
		List<String> result = cs.getAllResults();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[1/13]","Verified maximum value of LUT bar with DB");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[2/13]","Verified minimum value of LUT bar with DB");

		int middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		float maxValue = Float.parseFloat(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		float minValue = Float.parseFloat(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		viewerpage.assertEquals(middleValue, Math.round((maxValue+minValue)/2), "Checkpoint[3/13]", "Verifying middle range value is max + min /2 ");

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowCenterValueOverlayText(1);

		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.getViewPort(1),-300,0, 300,0);

		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))>maxValue, "Checkpoint[4/13]","Verified maximum value of LUT bar when mouse moved from left to right");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1))),middleValue, "Checkpoint[5/13]","Verified middle value of LUT bar when mouse moved from left to right");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))< minValue, "Checkpoint[6/13]","Verified minimum value of LUT bar when mouse moved from left to right");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[7/13]", "Verifying windowing values is not changed when mouse moved from left to right");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[8/13]", "Verifying windowing values is not changed when mouse moved from left to right");

		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),200, 0, -200,0);

		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))< maxValue, "Checkpoint[9/13]","Verified minimum value of LUT bar when mouse moved from right to left");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1))), middleValue, "Checkpoint[10/13]","Verified minimum value of LUT bar when mouse moved from right to left");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))>minValue, "Checkpoint[11/13]","Verified minimum value of LUT bar when mouse moved from right to left");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[12/13]", "Verifying windowing values is not changed when mouse moved from right to left");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[13/13]", "Verifying windowing values is not changed when mouse moved from right to left");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1221","Positive","F141"})
	public void test19_US1221_TC6289_verifyWWWLOnSource() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify mouse WW/WL can be adjusted only after toggling off the display of the PMAP  display");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		db=new DatabaseMethods(driver);
		pmap=new PMAP(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);

		cs = new ContentSelector(driver);
		List<String> result = cs.getAllResults();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[1/9]","Verified maximum value of LUT bar with DB");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,"",pmapPatientID,result.get(0)), "Checkpoint[2/9]","Verified minimum value of LUT bar with DB");

		int middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		float maxValue = Float.parseFloat(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		float minValue = Float.parseFloat(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		viewerpage.assertEquals(middleValue, Math.round((maxValue+minValue)/2), "Checkpoint[3/9]", "Verifying the middle range is half of maximum and minimum value");
		viewerpage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[4/9]", "Verified that Source series loaded without the PMAP overlay in active viewbox after toggle off");
		viewerpage.assertTrue(viewerpage.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[5/9]", "Result Applied is de-highlighted when PMAP source series is loaded in viewbox1");

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowWidthValueOverlayText(1);

		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.getViewPort(1),-300,0, 300,0);

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[6/9]", "Verifying that windowing is changed once windowing is applied on source after toggle off");
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(1), windowWidth, "Checkpoint[7/9]", "Verifying that windowing is changed once windowing is applied on source after toggle off");

		windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		windowWidth = viewerpage.getWindowWidthValueOverlayText(1);

		viewerpage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),200, 0, -200,0);

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[8/9]", "Verifying that windowing is changed once windowing is applied on source after toggle");
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(1), windowWidth, "Checkpoint[9/9]", "Verifying that windowing is changed once windowing is applied on source after toggle");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1221","US1757","Negative","F884"})
	public void test20_US1221_TC6293_US1757_TC8609_verifyPMAPWhenLUTMinRangeGoesBeyondMaxRange() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  PMAP overlay display when user adjusts the LUT Min/Max range using the mouse. <br>"+
				"Re-Execute TC6293: Verify  PMAP overlay display when user adjusts the LUT Min/Max range using the mouse");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		db=new DatabaseMethods(driver);		
		pmap=new PMAP(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		int maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		int minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		int temp = minValue;
		int i =1;

		do {
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 97,0, -97);
			temp = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));		
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint["+i+"] : verifying once the minimum range is equals to maximum rane value there is no more PMAP displayed", "TC20_"+i);
			i++;
		}while(temp<=maxValue);

	}

	@Test(groups ={"Chrome","Edge","IE11","US1221","Negative","F141"})
	public void test21_US1221_TC6327_VerifyPMAPWhenBothPMAPAndSourceAreLoaded() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL value in PMAP series when sync is on and we have PMAP and source series loaded in the viewer.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		db=new DatabaseMethods(driver);
		pmap=new PMAP(driver);
		layout=new ViewerLayout(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.closeNotification();
		layout.selectLayout(layout.oneByTwoLayoutIcon);


		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		int middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		int maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		int minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowCenterValueOverlayText(1);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),-300,0, 300,0);

		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)))>maxValue, "Checkpoint[1/12]","Verified maximum value of LUT bar changed after applying the windowing");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1))),middleValue, "Checkpoint[2/12]","Verified middle value of LUT bar changed after applying the windowing");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)))< minValue, "Checkpoint[3/12]","Verified minimum value of LUT bar changed after applying the windowing");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), windowCenter, "Checkpoint[4/12]", "No More change in W value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), windowWidth, "Checkpoint[5/12]", "No More change in C value");

		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),200, 0, -200,100);

		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0))), maxValue, "Checkpoint[6/12]","Verified maximum value of LUT bar is not changed when windowing is applied on source");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1))), middleValue, "Checkpoint[7/12]","Verified middle value of LUT bar is not changed when windowing is applied on source");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2))),minValue, "Checkpoint[8/12]","Verified minimum value of LUT bar is not changed when windowing is applied on source");

		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(2), windowCenter, "Checkpoint[9/12]", "Value of W is changed");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(2), windowWidth, "Checkpoint[10/12]", "Value of C is changed");

		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[11/12]", "Value of W is changed");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[12/12]", "Value of C is changed");
	}

	@Test(groups ={"Chrome","Edge","IE11","US1221","Negative","F141"})
	public void test22_US1221_TC6329_VerifyPMAPWhenPMAPandSourceAreLoadedInMultipleViewboxes() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL value in PMAP series when sync is on and we have same PMAP series loaded in mutliple viewports in the viewer.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		db=new DatabaseMethods(driver);
		pmap=new PMAP(driver);
		layout=new ViewerLayout(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.closeNotification();
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs = new ContentSelector(driver);

		cs.selectResultFromSeriesTab(3, cs.getAllResults().get(0));		
		cs.selectSeriesFromSeriesTab(4, cs.getSeriesDescriptionOverlayText(2));


		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		int middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		int maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		int minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));

		String windowCenter = viewerpage.getWindowCenterValueOverlayText(1);
		String windowWidth = viewerpage.getWindowCenterValueOverlayText(1);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3),-300,0, 300,0);

		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0))),maxValue, "Checkpoint[1/25]","Verified maximum value of LUT bar is not changed on viewbox 1");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1))),middleValue, "Checkpoint[2/25]","Verified middle value of LUT bar is not changed on viewbox 1");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2))), minValue, "Checkpoint[3/25]","Verified minimum value of LUT bar is not changed on viewbox 1");

		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(3).get(0)))>maxValue, "Checkpoint[4/25]","Verified maximum value of LUT bar is  changed on viewbox 3");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(3).get(1))),middleValue, "Checkpoint[5/25]","Verified middle value of LUT bar is  changed on viewbox 3");
		viewerpage.assertTrue(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(3).get(2)))< minValue, "Checkpoint[6/25]","Verified minimum value of LUT bar is  changed on viewbox 3");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[7/25]", "No more W value changed on viewbox 1");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[8/25]", "No more cvalue changed on viewbox 1");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), windowCenter, "Checkpoint[9/25]", "No more W value changed on viewbox 2");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), windowWidth, "Checkpoint[10/25]", "No more c value changed on viewbox 2");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(3), windowCenter, "Checkpoint[11/25]", "No more W value changed on viewbox 3");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(3), windowWidth, "Checkpoint[12/25]", "No more c value changed on viewbox 3");

		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4), windowCenter, "Checkpoint[13/25]", "No more W value changed on viewbox 4");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4), windowWidth, "Checkpoint[14/25]", "No more c value changed on viewbox 4");

		maxValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)));
		middleValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1)));
		minValue = Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)));


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(4),200, 0, -200,100);

		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0))), maxValue, "Checkpoint[15/25]","Verified maximum value of LUT bar not changed as windowing applied on source");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(1))), middleValue, "Checkpoint[16/25]","Verified middle value of LUT bar not changed as windowing applied on source");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2))),minValue, "Checkpoint[17/25]","Verified minimum value of LUT bar not changed as windowing applied on source");

		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), windowCenter, "Checkpoint[18/25]", "Windowing has changed as windowing is applied on source in viewbox 1");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), windowWidth, "Checkpoint[19/25]", "Windowing has changed as windowing is applied on source in viewbox 1");

		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(2), windowCenter, "Checkpoint[20/25]", "Windowing has changed as windowing is applied on source in viewbox 2");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(2), windowWidth, "Checkpoint[21/25]", "Windowing has changed as windowing is applied on source in viewbox 2");

		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(3), windowCenter, "Checkpoint[22/25]", "Windowing has changed as windowing is applied on source in viewbox 3");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(3), windowWidth, "Checkpoint[23/25]", "Windowing has changed as windowing is applied on source in viewbox 3");

		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(4), windowCenter, "Checkpoint[24/25]", "Windowing has changed as windowing is applied on source in viewbox 4");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(4), windowWidth, "Checkpoint[25/25]", "Windowing has changed as windowing is applied on source in viewbox 4");
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1754","Negative","F244"})
	public void test25_DE1754_TC7302_verifyResultWhenAnnotationDrawnOnSourceSeriesForPMAP() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the added annotation on source series does not add the annotations on respective  pmap result series.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+"in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		circle=new CircleAnnotation(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		layout=new ViewerLayout(driver);

		//change layout and draw annotation
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 5, 5, -80, -80);
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[1/4]", "Verified that circle annotation is current active accepted GSPS.");
		findingMenu.mouseHover(findingMenu.getGSPSHoverContainer(2));
		findingMenu.openFindingTableOnBinarySelector(2);
		viewerpage.assertEquals(findingMenu.acceptedFindings.size(), 1, "Checkpoint[2/4]", "Verified that circle annotation visible in finding table as well.");
		viewerpage.closingConflictMsg();

		//mousehover on first viewbox i.e. result
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertFalse(circle.isCirclePresent(1), "Checkpoint[3/4]", "Verified that circle annotation is not present on result series.");
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerpage.assertTrue(findingMenu.acceptedFindings.isEmpty(), "Checkpoint[4/4]", "Verified that no accepted finding visible in finding menu dropdown.");



	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1904","Negative","F289"})
	public void test26_DE1904_TC6204_verifyNavigationUsingARToolbar() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify navigation through findings - GSPS + PMAP by clicking on next arrow from AR toolbar");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		circle=new CircleAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		point=new PointAnnotation(driver);
		linewithunit=new MeasurementWithUnit(driver);

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );

		viewerpage.scrollToImage(1, 5);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);

		viewerpage.scrollToImage(1, 7);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -150, -100,-100);	

		viewerpage.scrollToImage(1, 10);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-50,-50);

		findingMenu.selectFindingFromTable(2);
		viewerpage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[1/1]","Verified that circle annotation is currently accepted GSPS with AR toolbar next event");

		findingMenu.selectNextfromGSPSRadialMenu();
		viewerpage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[1/1]","Verified that Ellipse annotation is currently accepted GSPS with AR toolbar next event");

	}

	@Test(groups ={"Chrome","Edge","IE11","DE2002","Positive"})
	public void test29_DE2002_TC8200_verifyAcceptRejectFromARToolbarForPMAPData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP can be accepted/rejected using AR toolbar");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);
		viewerpage.closeNotification();
		
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		viewerpage.assertFalse(findingMenu.isAcceptRejectToolBarPresent(1), "Checkpoint[1/4]", "Verified that A/R toolbar not visible when PMAP result default loaded");

		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerpage.assertTrue(findingMenu.verifyPendingGSPSToolbarMenu(), "Checkpoint[2/4]", "Verified that PMAP data not in Accepted or Rejected state");

		//select accept from A/R toolbar
		findingMenu.selectAcceptfromGSPSRadialMenu();
		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[3.a/4]", "Verified that PMAP data is in Accepted state after selecting Accept icon from A/R toolbar");

		//reload viewer page and verify state of PMAP data
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer("", pmapPatientID, 1, 1);

		viewerpage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[3.b/4]", "Verified that PMAP data is in Accepted state after reload of viewer page");
		viewerpage.assertEquals(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size(),1, "Checkpoint[3.c/4]", "Verified state of PMAP result in finding menu as Green");

		//select Reject from A/R toolbar
		findingMenu.selectRejectfromGSPSRadialMenu();
		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[4.a/4]", "Verified that PMAP data is in Rejected state after selecting Reject icon from A/R toolbar");

		//reload viewer page and verify state of PMAP data
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer("", pmapPatientID, 1, 1);

		viewerpage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[4.b/4]", "Verified that PMAP data is in Rejected state after reload of viewer page");
		viewerpage.assertEquals(findingMenu.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size(),1, "Checkpoint[4.c/4]", "Verified state of PMAP result in finding menu as Red");
	}

	//US1620:Display PMAP using embedded LUT
	@Test(groups ={"Chrome","Edge","IE11","US1620","US1757","Positive","F884","E2E","F631"})
	public void test30_US1620_TC8359_US1757_TC8629_verifyPAMPDisplayWithEmbeddedLUTBar() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP-Data Display  using embedded LUT bar");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(covidPatientName,username, password, 1);
		viewerpage.closeNotification();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+covidPatientName+" in viewer" );

		pmap=new PMAP(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify Display of Embedded LUT bar on viewer." );
		viewerpage.compareElementImage(protocolName,viewerpage.getViewPort(1), "Verified Loading of PMAP LUT bar.", "test30_01_Embedded_LUT_Bar");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1620","Negative","F631"})
	public void test33_US1620_TC8542_verifyPmapDisplayOnBrowserResize() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the imported PMAP data displays the PMAP overlay and the appropriate backgroud.");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(covidPatientName,username, password, 1);
		viewerpage.closeNotification();
		
		pmap=new PMAP(driver);
		viewerpage.assertFalse(pmap.verifyPresenceOfGradientBar(1), "Checkpoint[1/9]", "Verified presence of Gradient LUT bar on viewer default load");

		viewerpage.closeNotification();
		cs = new ContentSelector(driver);
		List<String> result = cs.getAllResults();
		cs.selectResultFromSeriesTab(1, result.get(0),1);
		viewerpage.assertTrue(pmap.verifyPresenceOfGradientBar(1), "Checkpoint[1/9]", "Verified presence of Gradient LUT bar on viewer default load");

		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint[2/9]", "test30_01_Embedded_LUT_Bar");
		db=new DatabaseMethods(driver);
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,covidPatientName,"",result.get(0)), "Checkpoint[3/9]","Verified maximum value of LUT bar with DB");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,covidPatientName,"",result.get(0)), "Checkpoint[4/9]","Verified minimum value of LUT bar with DB");

		int width=pmap.getGradientBar(1).getRect().getWidth();
		int height=pmap.getGradientBar(1).getRect().getHeight();
		viewerpage.resizeBrowserWindow(700, 600);
		viewerpage.assertTrue(pmap.getGradientBar(1).getRect().getWidth()<width, "Checkpoint[5/9]", "Verified LUT bar xCoordinate value after browser resize.");
		viewerpage.assertTrue(pmap.getGradientBar(1).getRect().getHeight()< height, "Checkpoint[6/9]", "Verified LUT bar xCoordinate value after browser resize.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint[7/9]", "test33_07_Embedded_LUT_Bar_OnResize");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(0)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,covidPatientName,"",result.get(0)), "Checkpoint[8/9]","Verified maximum value of LUT bar with DB on browser resize.");
		viewerpage.assertEquals(viewerpage.getText(pmap.getMaxMinValueForLUTBar(1).get(2)), db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,covidPatientName,"",result.get(0)), "Checkpoint[9/9]","Verified minimum value of LUT bar with DB on browser resize.");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1757","Positive","BVT","F884","E2E"})
	public void test34_US1757_TC8594_verifyPMAPSynchronization() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PMAP image is applied over the correct source image");


		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(s2008PatientName,username, password, 1);
		viewerpage.closeNotification();
		
		panel=new OutputPanel(driver);
		ContentSelector cs = new ContentSelector(driver);
		pmap=new PMAP(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+s2008PatientName+" in viewer");
		List<String> series = cs.getAllSeries();
		List<String> result = cs.getAllResults();

		panel.assertTrue(series.contains(panel.getSeriesDescriptionOverlayText(1)), "Checkpoint[1/13]", "Verifying the series description is same as viewer");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(series.get(0)), "Checkpoint[2/13]", "Verifying the result is selected");	
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(result.get(0)), "Checkpoint[3/13]", "Verifying the result is selected");	
		cs.assertTrue(cs.validateIconPresenceInSeriesTab(result.get(0),ViewerPageConstants.PMAP_ICON,ViewerPageConstants.PMAP), "Checkpoint[4/13]", "verifying the PMAP icon");	

		panel.closeNotification();
		panel.scrollToImage(1, 1);
		panel.assertTrue(panel.verifyResultAppliedTextPresence(1), "Checkpoint[5/13]", "Verified presence of Result applied text.");
		panel.assertTrue(pmap.verifyPresenceOfGradientBar(1),  "Checkpoint[6/13]", "Verified presence of Gradient bar on viewer.");
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Checkpoint[7/13]", "Result Applied is toggle On on viewbox1");
		panel.compareElementImage(protocolName, panel.getViewPort(1), "Checkpoint[8/13]", "test34_08_Embedded_LUT_Bar_S2008");

		panel.toggleOnOrOffResultUsingKeyboardGKey();
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[9/13]", "Result Applied is toggle Off on viewbox1");
		panel.assertFalse(pmap.verifyPresenceOfGradientBar(1), "Checkpoint[10/13]", "Verified that Gradient bar is not present on viewer.");
		panel.compareElementImage(protocolName, panel.getViewPort(1), "Checkpoint[11/13]", "test34_11_Source");

		panel.enableFiltersInOutputPanel(true, false, true);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test34_cineplay.png");

		panel.playCineOnThumbnail(1);
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test34_cineplay.png");		
		String expectedImagePath = newImagePath+"/goldImages/test34_cineplay.png";
		String actualImagePath = newImagePath+"/actualImages/test34_cineplay.png";
		String diffImagePath = newImagePath+"/diffImages/test34_cineplay.png";

		panel.stopCineOnThumbnail(1);
		boolean cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		
		panel.assertFalse(cpStatus, "Checkpoint[12/13] The actual and Expected image should not same","After cine images are changed");	

		panel.stopCineOnThumbnail(1);		
		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/goldImages/test34_cineStopped.png");

		panel.waitForTimePeriod(2000);

		panel.takeElementScreenShot(panel.thumbnailList.get(0), newImagePath+"/actualImages/test34_cineStopped.png");		
		expectedImagePath = newImagePath+"/goldImages/test34_cineStopped.png";
		actualImagePath = newImagePath+"/actualImages/test34_cineStopped.png";
		diffImagePath = newImagePath+"/diffImages/test34_cineStopped.png";

		cpStatus =  panel.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		panel.assertTrue(cpStatus, "Checkpoint[13/13] The actual and Expected image should be same","Verifying the images are same once cine is stopped");	
	}

	@Test(groups ={"Chrome","Edge","IE11","DR2280","Positive"})
	public void test35_DR2280_TC9202_verifyZoomAndScrollForPMAPData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact] US1221 - TC6276 + zoom + scroll.");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectlyUsingID(pmapPatientID, username, password ,1);
		preset=new ViewBoxToolPanel (driver);
		viewerpage.waitForViewerpageToLoad(1);		
		pmap=new PMAP(driver);
		viewerpage.waitForElementsVisibility(pmap.lutbar);
		viewerpage.closeNotification();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		String beforeZoom=preset.getZoomLevelValue(1);
		String scrollNumber=viewerpage.getCurrentScrollPosition(1);
		
		preset.changeZoomNumber(1,180);
		viewerpage.assertNotEquals(preset.getZoomLevelValue(1), beforeZoom, "Checkpoint[1/2]", "Verified that zoom works fine on PMAP data.");
		preset.openOrCloseViewBoxToolPanel(1,NSGenericConstants.CLOSE);
		
		viewerpage.scrollDown(20,1);
		viewerpage.assertNotEquals(viewerpage.getCurrentScrollPosition(1), scrollNumber, "Checkpoint[2/2]", "Verified that scroll works fine on PMAP data.");

	}
	
	
	
	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);

	}	

}
