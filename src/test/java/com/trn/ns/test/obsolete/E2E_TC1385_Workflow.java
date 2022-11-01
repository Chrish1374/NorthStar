//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class E2E_TC1385_Workflow extends TestBase {
//	
//	
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private SinglePatientStudyPage spStudyListPage;
//	
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//	
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		
//		// Begin on the Patient List Page, select the patient Data.
//		patientListPage = new PatientListPage(driver) ;
//		patientListPage.waitForPatientPageToLoad();
//	}	
//	
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_E2E_TC1385_Workflow_Checkpoint1_SingleMonitor() throws InterruptedException, AWTException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Alternate mouse controls on the Viewer page.");
//		// Select the specific Patient Name to go to the Single Patient Study List for the Patient
//		patientListPage.clickOnPatientRow(patientName);
//		
//		// Select the specific Study Name to go to the Viewer for that Study.
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForElementClickability(spStudyListPage.dateOfBirth);
//		spStudyListPage.clickOnStudy(1);
//		
//		//Begin Viewer Actions.
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		
//		//Step 1: With study open that contains more than one series, move cursor to another viewport.
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1 of 5", "With loaded study with more than one series, move cursor to different viewports.");
//		//Hover over first viewbox
//		viewerPage.mouseHover(viewerPage.viewboxImage1);
//		viewerPage.waitForTimePeriod(1000);
//		viewerPage.compareElementImage("E2E_TC1385_Workflow", viewerPage.viewboxImage1, "Checkpoint 1: Verify that the first viewbox is marked active.", "E2E_TC1385_Checkpoint1_Viewbox1Active_" + patientName);
//		
//		//Hover over second viewbox
//		viewerPage.mouseHover(viewerPage.viewboxImage2);
//		viewerPage.waitForTimePeriod(1000);
//		viewerPage.compareElementImage("E2E_TC1385_Workflow", viewerPage.viewboxImage2, "Checkpoint 1: Verify that the second viewbox is marked active.", "E2E_TC1385_Checkpoint1_Viewbox2Active_" + patientName);
//
//		// Step 2: Rotate Center Wheel.
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 2 of 5", "Rotate the Center Mouse Wheel.");
//		String cp = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath);
//		int currentPos = Integer.parseInt(cp);
//		
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), cp, "Verify that the Scroll Position is initalized to 1.", "Scroll Position is 1 by default.");
//		viewerPage.mouseHover(viewerPage.viewboxImage1);
//		viewerPage.waitForTimePeriod(1000);
//		viewerPage.mouseWheelScrollInViewer(1,"down", 5);
//		viewerPage.waitForTimePeriod(5000);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), (currentPos+5)+"", "Verify that the Scroll Position has increased to 6.", "Scroll Position is increased to 6.");
//		//Screenshot
//		viewerPage.mouseHover(viewerPage.viewboxImage2);
//		viewerPage.waitForTimePeriod(1000);
//		viewerPage.mouseWheelScrollInViewer(1,"up", 4);
//		viewerPage.waitForTimePeriod(5000);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1), (currentPos+1)+"", "Verify that the Scroll Position has decreased to 2.", "Scroll Position is decreased to 2.");
//		
//		
//		// Step 3: Press and Hold Left and Right button and Move Up/down � Left Right.
//		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 3 of 5", "Click and Drag in all directions with both Left and Right Mouse buttons.");
//		//W/C
//		
//		// Step 4: Press and hold Mouse Wheel.
//		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 4 of 5", "Click and Drag with the Middle Mouse button (Scroll Wheel)");
//		//Zoom
//		
//		// Step 5: Press and Hold Right button/move mouse.
//		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 5 of 5", "Click and Drag with the Right Mouse button");
//		//Pan
//		
//		// Any clean up (if necessary)
//		
//	
//	}
//
//}
