//package com.trn.ns.test.obsolete;
//
//import java.util.LinkedHashSet;
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
//public class E2E_TC1383_Workflow extends TestBase {
//	
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private SinglePatientStudyPage patientStudyPage;
//	private ExtentTest extentTest;
//	
//	String filePath = Configurations.TEST_PROPERTIES.get("SQA_Testing");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//	
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		
//					
//	}
//	
//	@Test(groups = {"Chrome", "multimonitor"})
//	public void test01_E2E_TC1383_verifyMultiMonitorOption() throws InterruptedException{
//		
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("verify the function Multi-window / Multi-monitor Option is working as expected");
//		
//		int numberOfChildWindows;
//		LinkedHashSet<String> windowHandles;
//		String parentWindow;
//		
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//		
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//		// Need to click based on Study description
//		patientStudyPage.click(patientStudyPage.rowCellValues.get(1));
//			
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.openNorthstarLogo();
//		
//		//Asserting Menu icons to be present 
//		viewerpage.assertTrue(viewerpage.textOverlayIcon.isDisplayed(), "Verify Textoverlay Icon is present", "Textoverlay Icon is present");
//		viewerpage.assertTrue(viewerpage.multiMonitorOption.isDisplayed(), "Verify Multi-monitor Icon is present", "Multi-monitor Icon is present");
//		viewerpage.assertTrue(viewerpage.gridIcon.isDisplayed(), "Verify Grid Icon is present", "Grid Icon is present");
//	
//		viewerpage.click(viewerpage.multiMonitorOption);			
//		
//		//validating 1st radio button is selected by default and others are not selected.
//		viewerpage.waitForElementVisibility(viewerpage.radioButtons.get(3));
//		viewerpage.assertTrue(viewerpage.radioButtons.get(0).isSelected(), "Verifying window 1 is selected", "Window 1 is selected as default choice");
//		viewerpage.assertFalse(viewerpage.radioButtons.get(1).isSelected(), "Verifying window 2 is not selected", "Window 2 is not selected as default choice");
//		viewerpage.assertFalse(viewerpage.radioButtons.get(2).isSelected(), "Verifying window 3 is not selected", "Window 3 is not selected as default choice");
//		viewerpage.assertFalse(viewerpage.radioButtons.get(3).isSelected(), "Verifying window 4 is not selected", "Window 4 is not selected as default choice");
//		viewerpage.closeNorthstarLogo();
//		
//		// getting parent window ID
//		parentWindow =viewerpage.getCurrentWindowID();
//		
//		// opening 2 child windows and validating child window count
//		viewerpage.openOrCloseChildWindows(3);
//		windowHandles = (LinkedHashSet<String>) driver.getWindowHandles();
//		numberOfChildWindows = windowHandles.size()-1;	// as it includes parent too
//		viewerpage.assertEquals(numberOfChildWindows, 2, "Verifying no.of child windows" , "Total no.of child windows equals 2");
//		
////		//setting child window position to available screens and maximizing child windows
////		// validate windows have been moved to specific position and maximized
////		viewerpage.setAndMaximizeChildWindowsPosition(windowHandles);
//		
//
//		//switching to Parent window
//		viewerpage.switchToWindow(parentWindow);
//		
//		//selectCheckboxApplyToAllMonitors
//		//validating grid layout box visibility
//		viewerpage.waitForElementVisibility(viewerpage.teraReconLogo);
//		viewerpage.openNorthstarLogo();
//		viewerpage.waitForElementVisibility(viewerpage.gridIcon);
//		viewerpage.click(viewerpage.gridIcon);
//		viewerpage.assertTrue(viewerpage.checkForElementVisibility(viewerpage.gridLayoutBox), "Validate Grid layout box visibility", "Grid layout box is successfully displayed");
//		viewerpage.click(viewerpage.gridIcon);
//		viewerpage.closeNorthstarLogo();
////		viewerpage.selectCheckBoxApplyToAllMonitor();
//		
//		//selecting 2x3layout
//		viewerpage.waitForElementVisibility(viewerpage.teraReconLogo);
//		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
//		
//		// validating all open windows have 6 canvas for layout
//		viewerpage.validateEqualNumOfCanvasLayoutsForAllWindows(windowHandles, 6);
//
//		//switching to Parent window
//		viewerpage.switchToWindow(parentWindow);
//		
//		// selecting radial button '2' to open '1' child window only
//		viewerpage.openOrCloseChildWindows(2);
//			
//		// validating only 2 windows(1 parent, 1 child) to be open
//		windowHandles = (LinkedHashSet<String>) viewerpage.getAllOpenedWindowsID();
//		viewerpage.assertEquals(windowHandles.size(), 2, "Validating only 2 windows to be open", "Only 2 windows are open");
//		
//		// validating all open windows have 6 canvas for layout
//		viewerpage.validateEqualNumOfCanvasLayoutsForAllWindows(windowHandles, 6);	
//	}
//
//
//}
