package com.trn.ns.test.viewer.GSPS;

import java.sql.SQLException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AcceptRejectGSPSFindingTest01 extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel presetMenu;

	private PointAnnotation point ;

	
	String username = "test";
	String EllipseComment="ellipseComment";

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	private HelperClass helper;
	private ViewerLayout layout;



	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//TC1772 : Verify GSPS radial tool bar functions and navigates through the results as expected.
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US680","US563","US950","DE1799","US1411","Sanity","BVT"})
	public void test01_US563_US680_TC2773_TC1772_US950_TC4320_DE1799_TC3707_US1411_TC7760_verifyGSPSRadialToolBarNavigation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS radial tool bar functions and navigation through the results on GSPS data"
				+ "<br> Validate that the Accept, Reject, Delete ,Previous and next functionality is working"
				+ "<br> Verify that on clicking PREV - NEXT arrows from A?R bar next annotation should highlighted with yellow shadow and annotation should not get bold /thick."
				+ "<br> Verify the navigation and layouts for GSPS patient. <br>"
				+ "Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		layout = new ViewerLayout(driver);
		
		point = new PointAnnotation(driver);
		//Loading the patient on viewer
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		point.selectPoint(1, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");


		// Click on Reject Button from  Accept/Reject Radial tool bar on current active GSPS object
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify on clicking Reject button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		// Click on Previous Button from  Accept/Reject Radial tool bar on current active GSPS object
		point.selectPreviousfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify on clicking Previous button GSPS is automatically shifted to the previous GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify previous slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify Accept button on radial menu");
		viewerPage.assertTrue(point.gspsAccept.isDisplayed(), "Verify Accept button on radial menu", "The Accept button is visible on radial menu");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");
		viewerPage.assertTrue(point.verifyRejectGSPSRadialMenu(), "Verify color of Reject icon", "The color of Reject icon changes to Red");

		// Click on Accept Button from  Accept/Reject Radial tool bar on current active GSPS object
		point.selectAcceptfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verify on clicking Accept button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify Reject button on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is visible on radial menu");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");


		// Click on Previous Button from  Accept/Reject Radial tool bar on current active GSPS object
		point.selectPreviousfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		// Click on Next Button from  Accept/Reject Radial tool bar on current active GSPS object
		point.selectNextfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verify on clicking Previous button GSPS is automatically shifted to the previous GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify previous slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify Reject button on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Accept button on radial menu", "The Accept button is visible on radial menu");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");


	}

	//TC1773 : Verify GSPS radial tool bar functions and navigates through the results as expected.
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US563","US950","Sanity","BVT"})
	public void test02_US563_TC1773_US950_TC4386_verifyArrowKeyNavigationForGSPSResult() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Arrow Key navigation for GSPS result"
				+ "<br> 	Verify that on pressing left -right arrows from keyboard next annotation should highlighted with yellow shadow and annotation should not get bold /thick");


		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");


		// Press Right Arrow key on Keyboard
		point.navigateGSPSForwardUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify on pressing right arrow key GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		// Press Left Arrow key on Keyboard
		point.navigateGSPSBackUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify on pressing left arrow key GSPS is automatically shifted to the previous GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify previous slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify first point annotation is current Active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		// Press Left Arrow key on Keyboard
		point.navigateGSPSBackUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verify viewer loops around to the last slice in the series with GSPS results");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify viewer loops around to the last slice in the series with GSPS results", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify last point annotation is current Active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,2), "Verify last point annotation is current Active GSPS object", "The last point annotation is current active GSPS object");

		// Click on Next Button from  Accept/Reject Radial tool bar on current active GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verify viewer loops around to the First slice in the series with GSPS results");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify viewer loops around to the First slice in the series with GSPS results", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify first point annotation is current Active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");
	}

	//TC1774 : Verify GSPS radial tool bar functions and navigates through the results as expected.
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US563","DE1874","Sanity","BVT"})
	public void test03_US563_TC1774_DE1874_TC7503_verifyPageUpDownKeyNavigationForGSPSResult() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Page Key navigation for GSPS result. <br>"+
		"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");


		// Press Page Down key on Keyboard
		point.scrollDownGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify on pressing right arrow key GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		// Press Left Page Up key on Keyboard
		point.scrollUpGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify on pressing left arrow key GSPS is automatically shifted to the previous GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify previous slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify first point annotation is current Active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		// Press Left Page Up key on Keyboard
		point.scrollUpGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verify viewer loops around to the last slice in the series with GSPS results");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify viewer loops around to the last slice in the series with GSPS results", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify last point annotation is current Active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,2), "Verify last point annotation is current Active GSPS object", "The last point annotation is current active GSPS object");

		// Press Left Page Up key on Keyboard
		point.scrollDownGSPSUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verify viewer loops around to the first slice in the series with GSPS results");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify viewer loops around to the first slice in the series with GSPS results", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify first point annotation is current Active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

	}

	//TC1778 : Verify GSPS radial tool bar functions and navigates through the results as expected.
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US563"})
	public void test04_US563_TC1778_verifySliceLoadingForGSPSData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify viewer always loads to the first slice in a series with a GSPS result, regardless of where in the series it is.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName, 1, 1);
		point = new PointAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/14]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");

		//Navigate to Patient list page and select liver9 patient
		viewerPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		helper.loadViewerPageUsingSearch(liver9PatientName,1,1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify viewer opens to first slice in series in both Viewbox");

		int defaultSlicePosition = (int) (Integer.parseInt(viewerPage.replaceSpecialCharacterFromText(viewerPage.getImageMaxScrollPositionOverlayText(1),"/", ""))*0.5);
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), defaultSlicePosition, "Verify first slice in series opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), defaultSlicePosition, "Verify first slice in series opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));

	}

	//TC1779 : Verify GSPS radial tool bar functions and navigates through the results as expected.
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US563"})
	public void test05_US563_TC1779_US1006_TC4563_verifyOneGspsRadialToolbarAndActiveResult() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Validate that there is only one GSPS Radial Toolbar and active GSPS result in a study at a time.");


		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/14]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation in slice is current active GSPS object", "The first point annotation is current active GSPS object");

		// Press Left Page Up key on Keyboard so that both viewbox contain slices with multiple GSPS results.
		point.scrollUpGSPSUsingKeyboard();

		//Right Click on one the finding in Viewbox that does not have active GSPS result
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify last point annotation is current active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,2), "Verify last point annotation is current active GSPS object", "The last point annotation is current active GSPS object");
		//		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		// As per US1006
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,2), "Verify first point annotation is now current active GSPS object", "The first point is current active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,1), "Verify last point annotation is no longer current active GSPS object", "The last point annotation is no longer current active GSPS object");

		//Right Click on one the finding in other Viewbox that does not have active GSPS result
		viewerPage.scrollToImage(2, 3);
		point.openGSPSRadialMenu(point.getHandlesOfPoint(2,1).get(0));
		int whichPOint = point.getPointWhichIsPendingAndActive(2);
		for(int i =1;i<point.getAllPoints(2).size();i++)
		{
			if(i==whichPOint)
				viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(2,whichPOint), "Verify first point annotation on Viewbox2 is now current active GSPS object", "The first point on Viewbox2 is current active GSPS object");
			else
				viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(2,i), "Verify last point annotation on Viewbox1 is no longer current active GSPS object", "The last point annotation on Viewbox1 is no longer current active GSPS object");
		}
	}		

	//TC1780 : Verify GSPS radial tool bar functions and navigates through the results as expected.
	//Limitation : Not work in firefox because of DE456
	@Test(groups ={"Chrome","IE11","Edge","US563"})
	public void test06_US563_TC1780_DE529_TC2077_DE614_TC2237_verifyInteractionOfNormalandGSPSRadialBar() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate that the GSPS radial toolbar and the general radial toolbar interact as expected.</br>"
				+ "Validate that the Active/Inactive color of GSPS change after performing the operations.");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/17]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/17]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/17]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation in slice is current active GSPS object", "The first point annotation is current active GSPS object");

		// get Zoom level for Canvas 0 before Zoom
		presetMenu=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = presetMenu.getZoomValue(1);

		// get Zoom level for Canvas 1 before Zoom
		int intialZoomLevel2 = presetMenu.getZoomValue(2);

		//perform left click on view box and verify active GSPS object is no longer active
		viewerPage.click(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/17]", "Verify GSPS result on the loaded slice is not thicker than any other result,depicting that there is no active GSPS result on performing empty command");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,1), "Verify active point annotation is deactivated on left click on viewer", "None of the point annotation are current active GSPS object");

		// right clicking on View box 1 and Clicking on Zoom Icon;
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/17]", "Verify Zoom icon is selected.");
		viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewbox(1), 0, 0, -10,-10);

		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/17]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerPage.assertTrue(presetMenu.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ presetMenu.getZoomLevelValue(1));

		// Verify Image Zoom Out on ViewBox 2 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/17]","Verify Zoom Level after Mouse Up Increase in View Box 2.");
		viewerPage.assertTrue(presetMenu.getZoomValue(2) > intialZoomLevel2,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel2 + " to "+ presetMenu.getZoomLevelValue(2));

		// Verify Accept/ Reject Radial bar is not present on Viewbox after DICOM operation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/17]", "Verify GSPS radial menu do not appear after any DICOM operation");
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu do not appear after any DICOM operation", "The GSPS radial menu do not appear after any DICOM operation");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/17]", "Verify that last GSPS result on the loaded slice is not thicker than any other result,depicting that there is no active GSPS result");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,1), "Verify that none of the point annotations are active GSPS object after DICOM operation", "None of the point annotation are current active GSPS object");

		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/17]", "Verify general radial menu open on right click on Viewbox1 on right clicking anywhere on Viewbox1 execpt GSPS object");
		viewerPage.assertTrue(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Validate Radial Menu is visible on ViewBox1", "The Radial menu is visble on ViewBox1");
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu does not appear on right clicking anywhere on Viewbox apart from GSPS Object", "The GSPS radial menu do not appear on ViewBox");

		//Open a Accept/Reject radial menu by right clicking on GSPS object.
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/17]", "Verify GSPS radial menu appear on right clicking GSPS object on Viewbox");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu appear on ViewBox1", "The GSPS radial menu appear on ViewBox");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/17]", "Verify point annotation clicked is now active GSPS object");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify point annotation clicked is now active GSPS object", "The first annotation clicked is now active GSPS object");

		//scroll to a slice that has no GSPS result
		viewerPage.scrollDownToSliceUsingKeyboard(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/17]", "Verify there is no Point Annotation on Current Slice on Viewbox1");
		viewerPage.assertFalse(point.isPointPresent(1, 1),"Verify there is no point Annotation on Current Slice on Viewbox1","There is no GSPS Point annotation in Current slice on Viewbox1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/17]", "Verify GSPS radial menu do not appear on scrolling to slice with no GSPS object");
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu do not appear on scrolling to slice with no GSPS object", "The GSPS radial menu appear do not appear on scrolling to slice with no GSPS object");

		//scroll to a slice that has GSPS result
		viewerPage.mouseHover(10, 10);
		viewerPage.scrollUpToSliceUsingKeyboard(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[15/17]", "Verify there is a GSPS Point Annotation on current Slice on Viewbox1");
		viewerPage.assertEquals(point.getLinesOfPoint(1,1).size(),ViewerPageConstants.POINT_LINES,"Verify there is GSPS point Annotation on current slice on Viewbox1","There is GSPS point annotation in current slice on Viewbox1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[16/17]", "Verify GSPS radial menu do not appear on scrolling to slice with GSPS object");
		viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsPrevious), "Verify GSPS radial menu do not appear on scrolling to slice with GSPS object", "The GSPS radial menu appear do not appear on scrolling to slice with GSPS object");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[17/17]", "Verify no point annotation is current active GSPS object after scroll to another slice");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1,1), "Verify none of the point annotation after scrolling to new slice is active GSPS object", " None of the point annotations after scrolling to new slice is active GSPS object");
	}	

	@Test(groups ={"Chrome","IE11","Edge","DE755","US563","BVT"})
	public void test07_US563_DE755_TC2123_TC2897_TC2898_verifyGSPSRadialToolBarOnLayoutChangeAndOneUpScenario() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS radial tool bar function on layout change and One up scenario"
				+ "</br>Verify that accept/reject state should be persists after changing layout"
				+ "</br>Verify that accept/reject state should be persists in one up scenario");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		point = new PointAnnotation(driver);
		layout = new ViewerLayout(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify viewer opens to first slice in series with GSPS result in both Viewbox");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		viewerPage.assertTrue(point.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(point.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(point.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active GSPS object");
		
		//Perform One-Up Scenario
		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(1));
		viewerPage.waitForViewerpageToLoad(1);
		//verify GSPS radial disappear on One-up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow no longer appear on first GSPS object after One Up.");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsReject), "Verify Reject button on radial menu", "The Reject button is not displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is not displayed on radial menu");
		

		//select Reject from GSPS radial bar
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify on clicking Reject button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 6, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		//select Previous from GSPS radial bar
		point.selectPreviousfromGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify on clicking Reject button GSPS is automatically shifted to the next GSPS result on the slice");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");

		//Double click on Viewbox1
		viewerPage.doubleClickOnViewport(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow no longer appear on first GSPS object.");

		viewerPage.assertTrue(point.verifyRejectGSPSRadialMenu(), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify slice position in other viewbox is same as before One-Up");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));

		//change layout to 2x1
		layout.selectLayout(layout.twoByOneLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow no longer appear on first GSPS object.");

		viewerPage.assertFalse(point.verifyRejectGSPSRadialMenu(), "Verify Previous arrow on radial menu", "The Previous arrow is not displayed on radial menu");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify slice position in other viewbox is same as before One-Up");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 1, "Verify first slice in series having GSPS object opens in Viewbox1", "The current slice in ViewBox 1 is "+viewerPage.getCurrentScrollPositionOfViewbox(1));
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), 3, "Verify first slice in series having GSPS object opens in Viewbox2", "The current slice in ViewBox 2 is "+viewerPage.getCurrentScrollPositionOfViewbox(2));

	}
}


