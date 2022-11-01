//package com.trn.ns.test.obsolete;
//
//
//import java.util.LinkedHashSet;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.Header;
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
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class MultiMonitorConfigurationTest extends TestBase{
//
//
//	private ExtentTest extentTest;
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private SinglePatientStudyPage patientStudyPage;
//	private String filePath1 = Configurations.TEST_PROPERTIES.get("SQA_Testing");
//	private String filePath2 = Configurations.TEST_PROPERTIES.get("LiverTumor");
//
//	String PATIENT_NAME_TEXTOVERLAY1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//	String PATIENT_NAME_TEXTOVERLAY2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//	@Test(groups = {"Chrome","firefox","Edge","IE11","multimonitor"})
//	public void test01_US341_TC1110_verifySingleMonitorOption() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Multi Monitor Configuration in the TeraRecon Icon Menu - Single Monitor");
//
//		patientPage = new PatientListPage(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate application navigates to the Patient List page");
//		patientPage.assertTrue(patientPage.checkForElementVisibility(patientPage.patientDataRows), "validate application navigates to Patient List page", "After clicking the Log In button, the application navigated to the Patient List page");
//
//		String expectedSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate series is loaded for this patient data and Multi-Monitor Option is visible in the Viewer menu");
//
//		patientPage.clickOnPatientRow(PATIENT_NAME_TEXTOVERLAY1);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.clickOnStudy(1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(1);
//
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription , "Validate specific Patient data has been loaded", "Scout series is loaded for this Patient data:"+PATIENT_NAME_TEXTOVERLAY1);
//		viewerpage.openNorthstarLogo();
//		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.multiMonitorOption), "validate multi-monitor icon is displayed", "Multi-monitor icon is displayed in Viewer menu");
//		viewerpage.clickUsingJavaScript(viewerpage.multiMonitorOption);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate options 1,2,3 & 4 are available in Multi-Monitor menu and 1 is selected by default");
//		//validating 1st radio button is selected by default and others are not selected.
//		//		viewerpage.waitForElementVisibility(viewerpage.radioButtons.get(3));
//		viewerpage.assertTrue(viewerpage.isSelected(viewerpage.radioButtons.get(0)), "validate window 1 is selected", "Window 1 is selected as default choice");
//		viewerpage.assertFalse(viewerpage.isSelected(viewerpage.radioButtons.get(1)), "validate window 2 is not selected", "Window 2 is not selected as default choice");
//		viewerpage.assertFalse(viewerpage.isSelected(viewerpage.radioButtons.get(2)), "validate window 3 is not selected", "Window 3 is not selected as default choice");
//		viewerpage.assertFalse(viewerpage.isSelected(viewerpage.radioButtons.get(3)), "validate window 4 is not selected", "Window 4 is not selected as default choice");
//
//		viewerpage.openOrCloseChildWindows(1);
//		viewerpage.assertTrue(viewerpage.getAllOpenedWindowsID().size() == 1, "Validate single window is open after selecting option 1", "Only 1 window is open");
//
//
//	}
//
//	@Test(groups = {"Chrome","multimonitor"})
//	public void test02_US341_TC1111_verifyDoubleMonitorOption() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Multi Monitor Configuration in the TeraRecon Icon Menu - Two Monitor");
//		patientPage = new PatientListPage(driver);
//
//		String expectedSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath1);
//		String expectedSeriesDescription2 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
//		String expectedSeriesDescription3 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath2);
//
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate that second window content is emtpy by default and multi-monitor menu closes after selecting an option");	
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.waitForNumberOfWindowsToEqual(2);
//
//		// validating only 2 windows(1 parent, 1 child) to be open
//		LinkedHashSet<String> windowHandles = (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//		viewerpage.assertEquals(windowHandles.size(), 2, "Validating only 2 windows to be open", "Only 2 windows are open");
//
//		windowHandles.remove(parentWindow);		
//		String childWindow = windowHandles.iterator().next();
//
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),childWindow, "Validate switching to child window", "Successfully switched to child window");
//		viewerpage.maximizeWindow();
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 0, "Validate second window has zero canvas layouts", "Total number of canvas layouts in Second window is zero ");
//
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate that series that cannot fit in the first browser window are loaded into the additional browser window");		
//		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
//
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(), childWindow, "Validate switching to child window", "Successfully switched to child window");
//		viewerpage.waitForViewerpageToLoad();	// removed wait for seriesDescription of vewbox1
//
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription, "Validate additional series that cannot fit in First window moves to Second", expectedSeriesDescription+" series is successfully loaded in Second window");
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),parentWindow, "Validate switching back to parent window", "Successfully switched to parent window");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate that"+PATIENT_NAME_TEXTOVERLAY2 +" series is loaded in First and Second windows by default");	
//		viewerpage.browserBackWebPage();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.browserBackWebPage();
//		patientPage = new PatientListPage(driver);
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY2);
//
//		viewerpage.waitForViewerpageToLoad(4);
//		viewerpage.waitForAllImagesToLoad();
//
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription2, "Validate that"+PATIENT_NAME_TEXTOVERLAY2 +" series is loaded in First window ", expectedSeriesDescription2+" series is loaded in first window");
//
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription3, "Validate that"+PATIENT_NAME_TEXTOVERLAY2 +" seriesis loaded in Second window ",  expectedSeriesDescription3+" series is loaded in second window");
//
//	}
//
//	@Test(groups = {"Chrome","multimonitor"})
//	public void test03_US341_TC1112_verifyTripleMonitorOption() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Multi Monitor Configuration in the TeraRecon Icon Menu - Three Monitor");
//		patientPage = new PatientListPage(driver);
//
//		String expectedSeriesDescription1 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath1);
//		String expectedSeriesDescription2 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath1);
//		String expectedSeriesDescription3 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
//		String expectedSeriesDescription4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath2);
//		String expectedSeriesDescription5 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES09_TEXTOVERLAY, filePath2);
//
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//		viewerpage.waitForElementVisibility(viewerpage.totalImagesinFirstViewbox);
//		String parentWindow = viewerpage.getCurrentWindowID();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate that second and third window content is emtpy by default and multi-monitor menu closes after selecting an option");	
//		viewerpage.openOrCloseChildWindows(3);
//
//		viewerpage.waitForNumberOfWindowsToEqual(3);
//
//		// validating only 2 windows(1 parent, 1 child) to be open
//		LinkedHashSet<String> windowHandles =  (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//
//		viewerpage.assertEquals(windowHandles.size(), 3, "Validating only 3 windows to be open", "Only 3 windows are open");
//
//		windowHandles.remove(parentWindow);	
//
//		String childWindow2 = windowHandles.iterator().next();
//		windowHandles.remove(childWindow2);	
//
//		String childWindow1 = windowHandles.iterator().next();
//
//		checkNumberOfCanvasInEachChild(1, childWindow1);
//		checkNumberOfCanvasInEachChild(2, childWindow2);
//
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate that series that cannot fit in the first browser window are loaded into the additional browser window");	
//		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
//
//		validateSeriesDescriptionInEachWindow(1, childWindow1, expectedSeriesDescription1);
//		validateSeriesDescriptionInEachWindow(2, childWindow2, expectedSeriesDescription2);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),parentWindow, "Validate switching back to parent window", "Successfully switched to parent window");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate that"+PATIENT_NAME_TEXTOVERLAY2+" series is loaded in multiple windows by default");	
//		viewerpage.browserBackWebPage();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.browserBackWebPage();
//		patientPage = new PatientListPage(driver);
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY2);
//
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription3, "Validate"+ PATIENT_NAME_TEXTOVERLAY2+"series is loaded in Parent window ", expectedSeriesDescription3+" series is successfully loaded in Parent window");
//
//
//		validateSeriesDescriptionInEachWindow(1, childWindow1, expectedSeriesDescription4);
//		validateSeriesDescriptionInEachWindow(2, childWindow2, expectedSeriesDescription5);
//
//	}
//
//	@Test(groups = {"Chrome","multimonitor"})
//	public void test04_US341_TC1113_verifyFourMonitorOption() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Multi Monitor Configuration in the TeraRecon Icon Menu - Four Monitor");
//		patientPage = new PatientListPage(driver);
//
//		String expectedSeriesDescription1 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath1);
//		String expectedSeriesDescription2 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath1);
//		String expectedSeriesDescription3 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES07_TEXTOVERLAY, filePath1);
//
//		String expectedSeriesDescription4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath2);
//		String expectedSeriesDescription5 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath2);
//		String expectedSeriesDescription6 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES09_TEXTOVERLAY, filePath2);
//
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate that second and third window content is emtpy by default and multi-monitor menu closes after selecting an option");	
//
//		viewerpage.openOrCloseChildWindows(4);
//
//		viewerpage.waitForNumberOfWindowsToEqual(4);
//
//		// validating only 2 windows(1 parent, 1 child) to be open
//		LinkedHashSet<String> windowHandles = (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//		viewerpage.assertEquals(windowHandles.size(), 4, "Validating only 4 windows to be open", "Only 4 windows are open");
//
//		windowHandles.remove(parentWindow);	
//
//		String childWindow3 = windowHandles.iterator().next();
//		windowHandles.remove(childWindow3);	
//
//		String childWindow2 = windowHandles.iterator().next();
//		windowHandles.remove(childWindow2);	
//
//		String childWindow1 = windowHandles.iterator().next();
//		windowHandles.remove(childWindow1);	
//
//		checkNumberOfCanvasInEachChildToZero(1, childWindow1);
//		checkNumberOfCanvasInEachChildToZero(2, childWindow2);
//		checkNumberOfCanvasInEachChildToZero(3, childWindow3);
//
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate that series that cannot fit in the first browser window are loaded into the additional browser window");	
//		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
//
//		validateSeriesDescriptionInEachWindow(1, childWindow1, expectedSeriesDescription1);
//		validateSeriesDescriptionInEachWindow(2, childWindow2, expectedSeriesDescription2);
//		validateSeriesDescriptionInEachWindow(3, childWindow3, expectedSeriesDescription3);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),parentWindow, "Validate switching back to parent window", "Successfully switched to parent window");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate that"+PATIENT_NAME_TEXTOVERLAY2+" series is loaded in multiple windows by default");	
//		viewerpage.browserBackWebPage();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.browserBackWebPage();
//		patientPage = new PatientListPage(driver);
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY2);
//
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//
//
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription4, "Validate"+ PATIENT_NAME_TEXTOVERLAY2+"series is loaded in Parent window ", expectedSeriesDescription4+" series is successfully loaded in Parent window");
//
//		validateSeriesDescriptionInEachWindow(1, childWindow1, expectedSeriesDescription5);
//		validateSeriesDescriptionInEachWindow(2, childWindow2, expectedSeriesDescription6);
//
//	}
//
//	// Step 5 of below test skipped. See comments in that section
//	@Test(groups = {"Chrome","multimonitor"})
//	public void test05_US341_TC1124_verifyRemoveMonitors() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that extra browser windows are closed if User selects a smaller number of monitor windows than are currently enabled");
//		patientPage = new PatientListPage(driver);
//
//		String expectedSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES07_TEXTOVERLAY, filePath1);
//
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Validate that four windows are open each with atleast 1 loaded series");	
//		validateNumberOfOpenWindows(4);
//
//		LinkedHashSet<String> windowHandles = (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//		windowHandles.remove(parentWindow);
//
//		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
//
//		for(String window: windowHandles) {
//			viewerpage.switchToWindow(window);
//			viewerpage.waitForElementVisibility(viewerpage.getSeriesDescriptionOverlay(1));
//			viewerpage.assertTrue(viewerpage.getNumberOfCanvasForLayout() > 0, "Validate atleast 1 series is present in each window", "Atleast 1 series is present in current window");
//		}
//
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Validate that fourth window closes after changing the Multi-Monitor setting to 3");	
//		validateNumberOfOpenWindows(3);
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Validate that second and third window closes after changing the Multi-Monitor setting to 1");	
//		validateNumberOfOpenWindows(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Validate that Series previously loaded into the closed North Star Monitor windows load into the remaining Monitor window");	
//		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon);
//		//			viewerpage.waitForElementVisibility(viewerpage.getSeriesDescriptionOverlay(9));
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(9),expectedSeriesDescription, "Validate "+expectedSeriesDescription+" to be visibile in current window", expectedSeriesDescription+" series is successfully loaded in current window");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Validate that all Monitor window closes");	
//		// cannot be automated as closing the parent closes the driver instance.
//
//	}
//
//	@Test(groups = {"IE11","Chrome","firefox","multimonitor"})
//	public void test06_US341_TC1192_verifySyncSetting() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that the number of monitor windows is synced across the different browser windows");
//		patientPage = new PatientListPage(driver);
//
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//		viewerpage.waitForElementVisibility(viewerpage.viewboxImg4);
//		viewerpage.waitForElementVisibility(viewerpage.totalImagesinFirstViewbox);
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Validate Multi-Monitor setting in each North Star Monitor browser window is set to 4");	
//		LinkedHashSet<String> childWindowHandles = generateChildWindowHandles(4);
//		checkMultiMonitorOptionInEachWindow(childWindowHandles, 4);
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Validate that fourth window closes and Multi-Monitor setting in each North Star Monitor browser window is set to 3");	
//		childWindowHandles = generateChildWindowHandles(3);
//		checkMultiMonitorOptionInEachWindow(childWindowHandles, 3);
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that Layout is applied to all open North Star Monitor Windows");	
//		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
//		checkNumberOfCanvasInEachChild(2, childWindowHandles);		
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Validate that third North Star Monitor window closes and the Multi-Monitor setting in each North Star Monitor browser window is set to 2");	
//		childWindowHandles = generateChildWindowHandles(2);
//		checkMultiMonitorOptionInEachWindow(childWindowHandles, 2);
//		viewerpage.switchToWindow(parentWindow);
//		checkNumberOfCanvasInEachChild(2, childWindowHandles);	
//
//	}
//
//	@Test(groups = {"Chrome","multimonitor"})
//	public void test07_US536_TC1641_verifyTripleMonitorOption() throws InterruptedException {
//
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("As a NS user, I want Restore Windows commands to follow the command pattern architecture.");
//		patientPage = new PatientListPage(driver);
//
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//		Header header = new Header(driver);
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//		viewerpage.waitForElementVisibility(viewerpage.totalImagesinFirstViewbox);
//		String parentWindow = viewerpage.getCurrentWindowID();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Validate that second and third window opened and multi-monitor menu closes after selecting an option");	
//		viewerpage.openOrCloseChildWindows(3);
//
//		viewerpage.waitForNumberOfWindowsToEqual(3);
//
//		// validating only 3 windows(1 parent, 2 child) to be open
//		LinkedHashSet<String> windowHandles =  (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//
//		viewerpage.assertEquals(windowHandles.size(), 3, "Validating only 3 windows to be open", "Only 3 windows are open");
//
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),parentWindow, "Validate switching back to parent window", "Successfully switched to parent window");
//
//		//Clicking on scan 
//		header.logout();
//		
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//		//Entering credentials and clicking on Login
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientPage.waitForPatientPageToLoad();
//		navigateToViewerPage(PATIENT_NAME_TEXTOVERLAY1);
//		
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//		viewerpage.waitForElementVisibility(viewerpage.totalImagesinFirstViewbox);
//		
//			
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Validate that second and third window opened and multi-monitor menu closes after selecting an option");	
//	
//		viewerpage.waitForNumberOfWindowsToEqual(3);
//		
//		// validating only 3 windows(1 parent, 2 child) to be open
//		LinkedHashSet<String> windowHandles2 =  (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//
//		viewerpage.assertEquals(windowHandles2.size(), 3, "Validating only 3 windows to be open", "Only 3 windows are open");
//	}
//	
//	public LinkedHashSet<String> generateChildWindowHandles(int numberOfWindows) throws InterruptedException {
//		String parentWindow = viewerpage.getCurrentWindowID();
//		validateNumberOfOpenWindows(numberOfWindows);
//		LinkedHashSet<String> windowHandles = (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//		windowHandles.remove(parentWindow);
//		return windowHandles;
//	}
//
//	public void checkMultiMonitorOptionInEachWindow(LinkedHashSet<String> windowHandles, int multiMonitorOption ) throws InterruptedException {
//		int i=1;
//		for(String window: windowHandles) {
//			viewerpage.switchToWindow(window);
//			viewerpage.waitForTimePeriod("Medium");
//			viewerpage.openNorthstarLogo();
//			viewerpage.waitForElementVisibility(viewerpage.multiMonitorOption);
//			viewerpage.click(viewerpage.multiMonitorOption);
//			viewerpage.assertTrue(viewerpage.radioButtons.get(multiMonitorOption-1).isSelected(), "Validating  Multi-Monitor setting "+multiMonitorOption, "Child window "+i+" Multi-Monitor setting is "+ multiMonitorOption);
//			viewerpage.openNorthstarLogo();
//			i++;
//		}
//	}
//
//	public void checkNumberOfCanvasInEachChild(int expectedCanvas, LinkedHashSet<String> childWindows) throws InterruptedException {
//		int i=1;
//		for(String window:childWindows) {
//			viewerpage.switchToWindow(window);
//			viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), expectedCanvas, "Validate child window "+i+ " has zero canvas layouts", "Total number of canvas layouts in child window "+i+ " is zero");
//			i++;
//		}
//	}
//
//
//	public void navigateToViewerPage(String PATIENT_NAME_TEXTOVERLAY) {
//		patientPage.clickOnPatientRow(PATIENT_NAME_TEXTOVERLAY);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.clickOnStudy(1);
//
//	}
//
//	public void validateNumberOfOpenWindows(int i) throws InterruptedException {
//		if(i == 0 ) {
//			viewerpage.closeWindow(viewerpage.getCurrentWindowID());
//			viewerpage.waitForNumberOfWindowsToEqual(i);
//			viewerpage.assertEquals(viewerpage.getAllOpenedWindowsID().size(), i, "Validating "+i+ " windows to be open", "Number of open windows equals "+i);
//
//		}else {
//			viewerpage.openOrCloseChildWindows(i);
//			viewerpage.waitForNumberOfWindowsToEqual(i);
//			viewerpage.assertEquals(viewerpage.getAllOpenedWindowsID().size(), i, "Validating only "+i+ " windows to be open", "Only "+i+" windows are open");
//		}	
//	}
//
//
//	public void checkNumberOfCanvasInEachChildToZero(int i, String childWindow) throws InterruptedException {
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),childWindow, "Validate switching to child window: "+i, "Successfully switched to child window: "+i);
//		viewerpage.maximizeWindow();
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 0, "Validate child window "+i+ " has zero canvas layouts", "Total number of canvas layouts in child window "+i+ " is zero");
//
//	}
//
//	public void checkNumberOfCanvasInEachChild(int i, String childWindow) throws InterruptedException {
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),childWindow, "Validate switching to child window: "+i, "Successfully switched to child window: "+i);
//		viewerpage.maximizeWindow();
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 0, "Validate child window "+i+ " has zero canvas layouts", "Total number of canvas layouts in child window "+i+ " is zero");
//
//	}
//
//	public void validateSeriesDescriptionInEachWindow(int i, String childWindow, String expectedSeriesDescription) throws InterruptedException {
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.assertEquals(viewerpage.getCurrentWindowID(),childWindow, "Validate switching to child window: "+i, "Successfully switched to child window: "+i);
//		viewerpage.waitForElementVisibility(viewerpage.getSeriesDescriptionOverlay(1));
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),expectedSeriesDescription, "Validate additional series that cannot fit in Parent window moves to "+i+"st child window", expectedSeriesDescription+" series is successfully loaded in "+i+"st child window");
//
//	}
//	
//	
//
//
//
//}
