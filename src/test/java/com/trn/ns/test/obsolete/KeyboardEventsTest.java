//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.util.HashMap;
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
//public class KeyboardEventsTest extends TestBase {	
//	
//	
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
//	private SinglePatientStudyPage spStudyListPage;
//	//Loading the patient on viewer
//	private String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	private String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		
//	}	
//
//	@Test(groups ={"firefox","Chrome", "multimonitor"})
//	public void test05_DE316_TC1524_verifyKeyboardShortcutsWindowLevelOnChildWindow() throws InterruptedException{
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify Apply Keyboard shortcuts-Window Level on child window for data"+PatientName );
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientListPage = new PatientListPage(driver);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(PatientName); 
//
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.clickOnStudy(1);
//
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page");
//		viewerPage.assertTrue(viewerPage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");		
//
//		String parentWindow = viewerPage.getCurrentWindowID();
//		viewerPage.openOrCloseChildWindows(2);
//		viewerPage.switchToWindow(parentWindow);
//
//
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.maximizeWindow();
//			}
//
//		viewerPage.switchToWindow(parentWindow);
//
//		viewerPage.browserBackWebPage();
//		spStudyListPage.clickOnStudy(1);
//		viewerPage.waitForViewerpageToLoad();
//		
////		viewerPage.selectLayout(viewerPage.oneByTwoLayoutIcon, true);
//		viewerPage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.waitForViewerpageToLoad();
//
//				
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the window leveling from keyboard shortcut on child window");
//				
//				String currentWidth = viewerPage.getWindowWidthValueOverlayText(1);
//				String currentCenter = viewerPage.getWindowCenterValueOverlayText(1);
//				
//				//enable window level
//				viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
////				viewerPage.assertTrue(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL), "Verifying window level is enabled", "verfied window level is enabled"); //existing defect 
//				
//				viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, 100, -50);
//
//				String widthViewbox1 = viewerPage.getWindowWidthValueOverlayText(1);
//				String centerViewbox1 = viewerPage.getWindowCenterValueOverlayText(1);
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint [1/3]", "Verifying window level is performed on viewbox 1 on child window"); 
//				viewerPage.assertNotEquals(widthViewbox1,currentWidth ,"Verifying the WW/WL(width) performed using Keyboard W key on viewbox 1","WW/WL is working fine");
//				viewerPage.assertNotEquals(centerViewbox1,currentCenter ,"Verifying the WW/WL(center) performed using Keyboard W key on viewbox 1","WW/WL is working fine");
//				
//				viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage2, 0, 0, 100, -50);
//				
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint [2/3]", "Verifying window level is performed on viewbox 2 on child window");
//				viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(2),widthViewbox1 ,"Verifying the WW/WL(width) performed using Keyboard W key on viewbox 2","WW/WL is working fine");
//				viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(2),centerViewbox1 ,"Verifying the WW/WL(center) performed using Keyboard W key on viewbox 2","WW/WL is working fine");
//			
//				//disabling window level
//				viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
////				viewerPage.assertFalse(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL), "Verifying window level is disabled", "verfied window level is disabled");
//			
//				//enable window level again
//				viewerPage.enableOrDisableWWWLUsingKeyboardWKey();
//				String widthViewbox2 = viewerPage.getWindowWidthValueOverlayText(2);
//				String centerViewbox2 = viewerPage.getWindowCenterValueOverlayText(2);
//				
//				viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage2, 0, 0, 100, -50);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint [3/3]", "Verifying window level is performed again on viewbox 2 on child window");
//				viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(2),widthViewbox2 ,"Verifying the WW/WL(width) performed using Keyboard W key on viewbox 2","WW/WL is working fine");
//				viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(2),centerViewbox2 ,"Verifying the WW/WL(center) performed using Keyboard W key on viewbox 2","WW/WL is working fine");
//			
//			}
//		}
//		
//	}
//		
//	@Test(groups ={"firefox","Chrome","multimonitor"})
//	public void test06_DE316_TC1526_TC1527_verifyKeyboardShortcutsScrollForwardandReverseOnChildWindow() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Apply Keyboard shortcuts-Scroll Forward and reverse on child window for data "+PatientName );
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientListPage = new PatientListPage(driver);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(PatientName); 
//
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		spStudyListPage.clickOnStudy(1);
//
//		ViewerPage viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page");
//		viewerPage.assertTrue(viewerPage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");		
//
//		String parentWindow = viewerPage.getCurrentWindowID();
//		viewerPage.openOrCloseChildWindows(2);
//		viewerPage.switchToWindow(parentWindow);
//
//
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.maximizeWindow();
//			}
//
//		viewerPage.switchToWindow(parentWindow);
//
//
//		viewerPage.browserBackWebPage();
//		spStudyListPage.clickOnStudy(1);
//		viewerPage.waitForViewerpageToLoad();
//
////		viewerPage.selectLayout(viewerPage.twoByOneLayoutIcon, true);
//		viewerPage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.waitForViewerpageToLoad();
//
//				
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Performing the forward scroll using down arrow key on child window");
//				
//				viewerPage.mouseHover(viewerPage.viewboxImg1);
//				int scrollPositionViewbox1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
//				viewerPage.scrollDownToSliceUsingKeyboard(1);
//				viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)),scrollPositionViewbox1+1,"Verifying the forward scroll using down key on viewbox 1","Forward Scroll is working fine");
//				
//				viewerPage.mouseHover(viewerPage.viewboxImg2);
//				int scrollPositionViewbox2 = viewerPage.getCurrentScrollPositionOfViewbox(2);
//				viewerPage.scrollDownToSliceUsingKeyboard(1);
//				viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(2)),scrollPositionViewbox2+1,"Verifying the forward scroll using down key on viewbox 2","Forward Scroll is working fine");
//				
//				viewerPage.mouseHover(viewerPage.viewboxImg1);
//				int reverseScrollPositionViewbox1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Performing the backword scroll using up arrow key on child window");
//				viewerPage.scrollUpToSliceUsingKeyboard(1);
//				viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)),reverseScrollPositionViewbox1-1,"Verifying the backward scroll using up key on viewbox 1","Backword Scroll is working fine");
//				
//				viewerPage.mouseHover(viewerPage.viewboxImg2);
//				int reverseScrollPositionViewbox2 = viewerPage.getCurrentScrollPositionOfViewbox(2);
//				viewerPage.scrollUpToSliceUsingKeyboard(1);
//				viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(2)),reverseScrollPositionViewbox2-1,"Verifying the backword scroll using down key on viewbox 2","backword Scroll is working fine");
//				
//			}
//		}
//		
//	}
//	
//	
//	
//	
//	
//
//	
//			@Test(groups ={"Edge","Chrome","IE11","Negative","DE2226"})
//	public void test05_DE2226_TC8882_verifyImageNumberOnInvalidInput() throws  InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Slice is not getting changed when the user enters an invalid  slice number using input text box");
//
//		helper = new HelperClass(driver);
//		viewerPage = helper.loadViewerDirectly(imbio_PatientName,1);
//
//		viewerPage.inputImageNumber(100, 1);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 100, "Checkpoint[1/5]", "Verifying slice number is 100");
//
//		viewerPage.inputImageNumber(250, 1);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 100, "Checkpoint[2/5]", "Verifying slice number remains 100 as invalid input is entered");
//
//		viewerPage.inputImageNumber(0, 1);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 100, "Checkpoint[3/5]", "Verifying slice number remains 100 as invalid input is entered");
//
//		helper.browserBackAndReloadViewer(AH4_PatientName, 1, 1);
//
//		viewerPage.inputImageNumber(31, 2);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 15, "Checkpoint[4/5]", "Verifying slice number is 15 as invalid input is entered");
//
//		viewerPage.inputImageNumber(5, 1);
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 5, "Checkpoint[5/5]", "Verifying slice number is 5");
//
//	}
//	
//@Test(groups ={"Edge","Chrome","IE11","Negative","DE2226"})
//	public void test06_DE2226_TC8904_verifyPhaseNumberOnInvalidInput() throws  InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify phase number remains at its position when the user enters an invalid  number using input text box");
//		
//		helper = new HelperClass(driver);
//		
//		viewerPage = helper.loadViewerDirectly(TDAMaps_PatientName,1);
//		
//		//From view box 1, enter the Phase number greater than max value from input box. Ex:9. - [Checkpoint #1]: Phase should remain same when user inputs invalid phase number.
//		viewerPage.inputPhaseNumber(9, 1);
//		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1), 1, "Checkpoint[1/2]", "Verifying slice number is 1");
//		//From view box 1, enter the Phase number in-bounds from input box. Ex:5. - [Checkpoint #2]:  Phase number should be changed to 5.	
//		viewerPage.inputPhaseNumber(5, 1);
//		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1), 5, "Checkpoint[2/2]", "Verifying slice number is 5");
//
//	}
//
//
//}
