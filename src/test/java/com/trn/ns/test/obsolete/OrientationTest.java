//package com.trn.ns.test.obsolete;
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
//
//import java.util.List;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class OrientationTest extends TestBase {
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private SinglePatientStudyPage spStudyListPage;
//	private ViewerPage viewerPage;
//
//
//	// Get Patient Name
//	String filePath=Configurations.TEST_PROPERTIES.get("NorthStar^MR^MultiPhase^20x6_filepath");
//	String patientName_MR_MultiPhase = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath);
//
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		// Load and initialize the Patient List.
//	}
//
//	// IE11 Issue with multimonitor 
//		@Test(groups ={"Chrome","firefox", "multimonitor"})
//		public void test07_DE396_TC1515_verifyMarkersPresentOnMultiMonitor() throws InterruptedException{
//			String testcase = "DE396_TC1515";
//
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("verify all annotation markers on viewer on parent-child window");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Logging in to the Viewer Page", "Loading the Patient into the viewer.");
//
//			patientListPage = new PatientListPage(driver);
//			patientListPage.clickOnPatientRow(patientName_MR_MultiPhase);
//
//			// Load the study for the given Patient.
//			spStudyListPage = new SinglePatientStudyPage(driver);
//			spStudyListPage.clickOntheFirstStudy();
//
//			//Begin Viewer Actions.
//			viewerPage = new ViewerPage(driver);
//			viewerPage.waitForViewerpageToLoad(2);
//			viewerPage.waitForAllImagesToLoad();
//
//
//			String parentWindow = viewerPage.getCurrentWindowID();
//			viewerPage.openOrCloseChildWindows(2);
//			viewerPage.switchToWindow(parentWindow);
//
//			List<String> childWinHandles = viewerPage.getAllOpenedWindowsIDs();
//			//Switching to child window
//			viewerPage.switchToWindow(childWinHandles.get(1));
//			viewerPage.maximizeWindow();
//			viewerPage.waitForAllImagesToLoad();
//
//			viewerPage.switchToWindow(parentWindow);
//			viewerPage.browserBackWebPage();
//
//			spStudyListPage.clickOntheFirstStudy();
//			viewerPage.waitForAllImagesToLoad();
//
//			// Step 1: Observe the default appearance upon loading the data.
//			viewerPage.waitForViewerpageToLoad();
//
//			// Checkpoint 1:
//			// All orientation markers displayed	
//			viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 1: Verify that the orientation markers display correctly in parent window .", "Checkpoint1_OrientationMarker_" + testcase+"Checkpoint1_ParentWindow_"+"patientName_MR_MultiPhase");
//
//			//Switching to child window
//			viewerPage.switchToWindow(childWinHandles.get(1));
//			viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint 2: Verify that the orientation markers display correctly in child window.", "Checkpoint2_OrientationMarker_" + testcase+"Checkpoint1_ChildWindow_"+"patientName_MR_MultiPhase");
//		}
//
//	public void validatePatient(String patientName, String expectedPattern) {
//		navigateFromPatientListToViewerPage(patientName);
//		validateOrientationSafetyViewBox1(expectedPattern);
//	}
//
//	public void navigateFromPatientListToViewerPage(String patientName) {
//
//
//		patientListPage.waitForElementsVisibility(patientListPage.patientDataRows);
//		patientListPage.clickOnPatientRow(patientName);
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.waitForSingleStudyToLoad();
//		spStudyListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//	}
//
//	public void validateWWAndWC(String expectedWW, String expectedWC, String PatientName, String viewBox) {
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.assertEquals(viewerPage.getValueOfWindowCenter(Integer.parseInt(viewBox)), expectedWC, "Validate WC for "+PatientName, "WC value is as expected for "+PatientName);
//		viewerPage.assertEquals(viewerPage.getValueOfWindowWidth(Integer.parseInt(viewBox)), expectedWW, "Validate WW for "+PatientName, "WW value is as expected for "+PatientName);
//	}
//
//	public void validateOrientationSafetyViewBox1(String expectedPattern) {
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		StringBuilder actualPatternFromSample = new StringBuilder();
//
//		actualPatternFromSample.append(viewerPage.getTopOrientationMarker(1).getText());
//		actualPatternFromSample.append(viewerPage.getRightOrientationMarker(1).getText());
//		actualPatternFromSample.append(viewerPage.getBottomOrientationMarker(1).getText());
//		actualPatternFromSample.append(viewerPage.getLeftOrientationMarker(1).getText());
//
//		viewerPage.assertEquals(actualPatternFromSample.toString(), expectedPattern, "Validating Orientaion Pattern for given Sample", "Orientation pattern for Sample equals: "+expectedPattern);
//	}
//
//	public void validateOrientationSafetyViewBox2(String expectedPattern) {
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		StringBuilder actualPatternFromSample = new StringBuilder();
//
//		actualPatternFromSample.append(viewerPage.getTopOrientationMarker(2).getText());
//		actualPatternFromSample.append(viewerPage.getRightOrientationMarker(2).getText());
//		actualPatternFromSample.append(viewerPage.getBottomOrientationMarker(2).getText());
//		actualPatternFromSample.append(viewerPage.getLeftOrientationMarker(2).getText());
//
//		viewerPage.assertEquals(actualPatternFromSample.toString(), expectedPattern, "Validating Orientaion Pattern for given Sample", "Orientation pattern for Sample equals: "+expectedPattern);
//	}
//
//	public void validateOrientationInvisibility() {
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.assertTrue(viewerPage.getTopOrientationMarkerText(1).isEmpty(), "Validate top orientation marker", "Top orientation marker is not displayed");
//		viewerPage.assertTrue(viewerPage.getBottomOrientationMarkerText(1).isEmpty(), "Validate bottom orientation marker", "Bottom orientation marker is not displayed");
//		viewerPage.assertTrue(viewerPage.getLeftOrientationMarkerText(1).isEmpty(), "Validate left orientation marker", "Left orientation marker is not displayed");
//		viewerPage.assertTrue(viewerPage.getRightOrientationMarkerText(1).isEmpty(), "Validate right orientation marker", "Right orientation marker is not displayed");
//
//	}
//
//
//
//
//
//}
