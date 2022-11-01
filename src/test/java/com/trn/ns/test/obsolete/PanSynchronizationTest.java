//package com.trn.ns.test.obsolete;
//
//
//
//import java.awt.AWTException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
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
//public class PanSynchronizationTest extends TestBase{
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	//Loading the patient on viewer
//	private String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	private String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//
//	private String filePath2 = Configurations.TEST_PROPERTIES.get("NorthStar_MR_Lspine_filepath");
//	private String nsMRLspine_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//
//	private String filePath3 = Configurations.TEST_PROPERTIES.get("NorthStar^MR^Brain^WO^Contrast_filepath");
//	private String ns_mrBrain_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
//
//	private String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
//	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//	
//	private String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	private String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//	
//	String filePath5 = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
//	String mrLSPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath5);
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//
//	}
//	
//	//obselete
//		//@Test(groups ={"IE11","Chrome","firefox","Edge","US64"})
//		public void test07_US64_TC1074_US289_TC2375_verifyPanNotInSyncForSameOrientationAndDifferentFrameReferenceUID() throws InterruptedException, AWTException 
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify PAN sync is working fine on all viewboxes when series have same orientation and different FrameReferenceUID"
//					+ "<br> Verify scroll synchronization with series having different FrameReferenceUID and same Orientation.");
//			patientPage = new PatientListPage(driver);
//			patientPage.waitForPatientPageToLoad();
//
//			//Loading the patient on viewer
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4_patientName+"in viewer" );
//			patientPage.clickOnPatientRow(ah4_patientName);
//
//			
//			studyPage.waitForSingleStudyToLoad();
//			studyPage.clickOntheFirstStudy();
//
//			viewerpage = new ViewerPage(driver);
//			viewerpage.waitForViewerpageToLoad();
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//			//Selecting minimum annotation level
//			viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//			// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
//			viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
//			viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
//			viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Only single image from series(on which PAN is selected) should PAN.");
//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying Only single image from series(on which PAN is selected) should PAN. "
//					+ "All series should not get PAN synchronously as different FrameReferenceUID is different.","US64_TC1074_Checkpoint");
//		}
//	
//
//	//multimonitor
//
//
//	@Test(groups = { "firefox", "Chrome", "multimonitor" })
//	public void test08_01_US64_TC1047_verifyPanSyncOnChildWindow() throws InterruptedException, AWTException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify PAN sync is working fine on all viewboxes on child window.");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//		//selecting 2 window
//		String parentWindow = driver.getWindowHandle();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		//selecting checkbox apply to all monitor if not selected already
//		//		viewerpage.selectCheckBoxApplyToAllMonitor();
//		//Selecting minimum annotation level
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		//changing layout to 2X1
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//
//				// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu on child window" );
//				viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected on child window.");
//				viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify series should PAN synchronously.");
//				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should PAN synchronously on child window.","US64_TC1047_Child_Window_Checkpoint");
//
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should PAN synchronously on parent window.","US64_TC1047_Parent_Window_Checkpoint");
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//	@Test(groups = { "firefox", "Chrome", "multimonitor" })
//	public void test09_02_US64_TC1047_verifyPanSyncOnChildWindow() throws InterruptedException, AWTException 
//	{extentTest = ExtentManager.getTestInstance();
//	extentTest.setDescription("Verify PAN sync is working fine on all viewboxes when 4 windows are open");
//	patientPage = new PatientListPage(driver);
//	patientPage.waitForPatientPageToLoad();
//
//	//Loading the patient on viewer
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
//	patientPage.clickOnPatientRow(patientName);
//
//	
//	studyPage.waitForSingleStudyToLoad();
//	studyPage.clickOntheFirstStudy();
//
//	viewerpage = new ViewerPage(driver);
//	viewerpage.waitForViewerpageToLoad();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify user is navigating to viewer page.");
//	viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//	//selecting 4 window
//	String parentWindow = driver.getWindowHandle();
//	viewerpage.openOrCloseChildWindows(4);
//	viewerpage.switchToWindow(parentWindow);
//
//	Set<String> childWinHandles = driver.getWindowHandles();
//	System.out.println(childWinHandles);
//	List<String> childWinHandlesList = new ArrayList<String>(childWinHandles);
//
//	for (String childWindow : childWinHandlesList) 
//		if(!childWindow.equals(parentWindow)){
//			viewerpage.switchToWindow(childWindow);
//			driver.manage().window().maximize();
//		}
//	viewerpage.switchToWindow(parentWindow);
//	viewerpage.maximizeWindow();
//
//	//selecting checkbox apply to all monitor if not selected already
//	//	viewerpage.selectCheckBoxApplyToAllMonitor();
//	//Selecting minimum annotation level
//	viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//	//changing layout to 1X1
//	viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//	viewerpage.waitForAllImagesToLoad();
//
//	viewerpage.switchToWindow(childWinHandlesList.get(2));
//	viewerpage.maximizeWindow();
//
//	// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu on child window" );
//	viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Pan icon is selected.");
//	viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
//	viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//	int i = 1;
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify All series should PAN synchronously on all 4 open windows.");
//	//switching to each open window
//	for (String childWindow : childWinHandlesList) {
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.maximizeWindow();
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should PAN synchronously.","US64_TC1047_02_Checkpoint"+i);
//		i++;
//	}
//	//switching to parent window and changing layout to 2X2
//	viewerpage.switchToWindow(parentWindow);
//	viewerpage.maximizeWindow();
//	viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
//	viewerpage.waitForAllImagesToLoad();
//
//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Pan synchronization is retained when layout changed to 2X2.");
//	viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Pan synchronization is retained when layout changed.","US64_TC1047_02_Parent_Window_Checkpoint");
//	viewerpage.openOrCloseChildWindows(1);
//	}
//
//	@Test(groups = { "Chrome", "multimonitor" })
//	public void test15_US68_TC598_verifyPanPerformedonParentAndChildWindow() throws InterruptedException, AWTException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the user is able to pan the image in one viewbox and other viewboxes present on multimonitor viewport.");
//		patientPage = new PatientListPage(driver);
//		
//		viewerpage = new ViewerPage(driver);
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//
//		patientPage.clickOnPatientRow(patientName);
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify study is loaded in viewport.");
//		viewerpage.assertTrue(viewerpage.patientIDViewer.isDisplayed(), "Verifying study is loaded in viewport", "Study is loaded in viewport");
//		//selecting 2 window
//		String parentWindow = driver.getWindowHandle();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		//selecting checkbox apply to all monitor if not selected already
////		viewerpage.selectCheckBoxApplyToAllMonitor();
//		//Selecting minimum annotation level
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		//changing layout to 1X1
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		int i =1;
//		for (String childWindow : childWinHandles) {
//			viewerpage.switchToWindow(childWindow);
//			viewerpage.maximizeWindow();
//			viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying layout changed to 1X1 on winow "+i, "Verified layout changed to 1X1 on winow "+i);
//			// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//			viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Pan icon is selected on window"+i);
//			viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify images should PAN on window "+i);
//			viewerpage.dragAndReleaseOnViewer(0, 0, 300, 0);
//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","US68_TC598_Window_"+i+"_Checkpoint_Right");
//			viewerpage.dragAndReleaseOnViewer(300, 0, -600, 0);
//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","US68_TC598_Window_"+i+"_Checkpoint_left");
//			viewerpage.dragAndReleaseOnViewer(-300, 0, 300, -300);
//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","US68_TC598_Window_"+i+"_Checkpoint_Top");
//			viewerpage.dragAndReleaseOnViewer(0, -300, 0, 600);
//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","US68_TC598_Window_"+i+"_Checkpoint_Bottom");
//			viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
//			i++;
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Pan is retained when layout changed.");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Pan is retained when layout changed to 2X2.","US68_TC598_Parent_Window_Checkpoint_2X2");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Pan is retained when layout changed to 1X1.","US68_TC598_Parent_Window_Checkpoint_1X1");
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//	@Test(groups = { "firefox", "Chrome", "multimonitor" })
//	public void test16_DE316_TC1530_verifyPanSyncOffAfterClickingSpacebarOnChildWindow() throws InterruptedException, AWTException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify PAN Synchronization is off after clicking on Space bar on Child Window");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//		//selecting 2 window
//		String parentWindow = driver.getWindowHandle();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		//selecting checkbox apply to all monitor if not selected already
////		viewerpage.selectCheckBoxApplyToAllMonitor();
//		//Selecting minimum annotation level
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		//changing layout to 2X1
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify series should PAN synchronously.");
//				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should PAN synchronously on child window.","DE316_TC1530_Child_Window_Checkpoint_1");
//
//			}
//		}
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should PAN synchronously on parent window.","DE316_TC1530_Parent_Window_Checkpoint_2");
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.performSyncONorOFF();
//				viewerpage.dragAndReleaseOnViewer(100, -50, -200, 0);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify series should not PAN synchronously.");
//				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should not PAN synchronously on child window.","DE316_TC1530_Child_Window_Checkpoint_3");
//
//			}
//		}
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"All series should not PAN synchronously on parent window.","DE316_TC1530_Parent_Window_Checkpoint_4");
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//
//
//}
//
//
//
