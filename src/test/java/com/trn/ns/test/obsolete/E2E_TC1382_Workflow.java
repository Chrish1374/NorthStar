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
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class E2E_TC1382_Workflow extends TestBase {
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
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	
//				
//	}
//	
//	@Test(groups = {"firefox","Chrome","Edge","IE11"})
//	public void test01_E2E_TC1382_verifyMonitorLayoutControl() throws InterruptedException{
//		// Code is written based on current behavior. When DE282 is fixed, couple of assertions need to be added.
//		// Limitation in running the script in Firefox and Edge browsers.
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("verify Monitor Layout Control is working as expected");
//		
//		patientPage = new PatientListPage(driver);
//		//patientPage.clickOnPatientRow(dataProperty.getProperty("patientName_NS_CT"));
//		//Get Patient Name
//				
//		patientPage.clickOnPatientRow(PatientName);
//				
//		
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForElementVisibility(patientStudyPage.studyDateTime);
//		// Need to click based on Study description
//		patientStudyPage.click(patientStudyPage.rowCellValues.get(1));
//			
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.openNorthstarLogo();
//		
//		// 1. Selecting layout 2x3
//		//Asserting Menu icons to be present 
//		assertMenuIconsToBePresent();
//		
//		//Asserting GridLayout box to be present
//		viewerpage.click(viewerpage.gridIcon);
////		viewerpage.waitForElementVisibility(viewerpage.chkBoxAllMonitor);
//		viewerpage.assertTrue(viewerpage.checkForElementVisibility(viewerpage.gridLayoutBox), "Validate Grid layout box visibility", "Grid layout box is successfully displayed");
//		viewerpage.click(viewerpage.gridIcon);
//		viewerpage.closeNorthstarLogo();
//
//		//selecting 2x3layout
//		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
//		
//		//validating new layout is applied
//		viewerpage.waitForElementVisibility(viewerpage.viewboxImg5);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(NSConstants.TWO_BY_THREE_LAYOUT), "validating no.of canvas for Layout 2x3", "No.of canvas for layout 2x3 is:"+viewerpage.getExpectedNumberOfCanvasForLayout(NSConstants.TWO_BY_THREE_LAYOUT));
//		viewerpage.openNorthstarLogo();
//		
//		// 2. Selecting layout 1x2
//		//Asserting Menu icons to be present 
//		assertMenuIconsToBePresent();
//		
//		//Asserting GridLayout box to be present
//		viewerpage.click(viewerpage.gridIcon);
////		viewerpage.waitForElementVisibility(viewerpage.chkBoxAllMonitor);
//		viewerpage.assertTrue(viewerpage.checkForElementVisibility(viewerpage.gridLayoutBox), "Validate Grid layout box visibility", "Grid layout box is successfully displayed");		
//		viewerpage.click(viewerpage.gridIcon);
//		viewerpage.closeNorthstarLogo();
//		
//		//selecting layout 1x2
//		viewerpage.selectLayout(viewerpage.oneByTwoLayoutIcon);
//	
//		//validating new layout is applied
//		viewerpage.waitForElementInVisibility(viewerpage.viewboxImage3);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT), "validating no.of canvas for Layout 1x2", "No.of canvas for layout 1x2 is:"+viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT));
//		
//		// Gather details before double click
//		viewerpage.waitForElementVisibility(viewerpage.viewboxImg1);
//		viewerpage.mouseHover(viewerpage.viewboxImage1);
//		
//		
//		Integer zoomValueBeforeDoubleClick = Integer.parseInt(viewerpage.getZoomValueOverlay(1).getText());
//		String currentImageBeforeDoubleClick = viewerpage.getImageCurrentScrollPositionOverlay(1).getText();
//		
//		// Perform double click
//		viewerpage.doubleClickOnViewbox(1);
//		
//		// validating zoom to fit is applied
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Validating zoom to fit is applied after double click on any view port", "viewport expands to 1:1 and zoom to fit is applied");
//				
//		// Validating details are unchanged after zoom to fit is applied
//		String currentImageAfterDoubleClick = viewerpage.getImageCurrentScrollPositionOverlay(1).getText();
//		Integer zoomValueAfterDoubleClick = Integer.valueOf(viewerpage.getZoomValueOverlay(1).getText());
//		
//		viewerpage.assertTrue(zoomValueAfterDoubleClick > zoomValueBeforeDoubleClick, "Validating Zoom value is increased", "Zoom value is increased after fast double click using left mouse is applied on Viewport");
//		viewerpage.assertEquals(currentImageAfterDoubleClick, currentImageBeforeDoubleClick, "Validating Image # unchanged", "Image # remains unchaged after fast double click using left mouse is applied on viewport");
//		
//		// Revert to original view
//		viewerpage.doubleClickOnViewbox(1);
////		viewerpage.mouseHover(viewerpage.viewboxImage1);
//		
//		// Validating details are unchanged after revert
//		viewerpage.waitForViewerpageToLoad();
//		String currentImageAfterRevert = viewerpage.getImageCurrentScrollPositionOverlay(1).getText();
//		Integer zoomValueAfterRevert = Integer.parseInt(viewerpage.getZoomValueOverlay(1).getText());
//		
//		viewerpage.assertEquals(currentImageAfterRevert, currentImageBeforeDoubleClick, "Validating Image # unchanged after revert", "Image # unchaged after revert");
//		viewerpage.assertTrue(zoomValueAfterRevert < zoomValueAfterDoubleClick, "Validating Zoom value is decreased", "Zoom value is decreased after fast double click using left mouse is applied on Viewport");
//			
//}
//	
//	public void assertMenuIconsToBePresent() {
//		viewerpage.assertTrue(viewerpage.textOverlayIcon.isDisplayed(), "Verify Textoverlay Icon is present", "Textoverlay Icon is present");
//		viewerpage.assertTrue(viewerpage.multiMonitorOption.isDisplayed(), "Verify Multi-monitor Icon is present", "Multi-monitor Icon is present");
//		viewerpage.assertTrue(viewerpage.gridIcon.isDisplayed(), "Verify Grid Icon is present", "Grid Icon is present");	
//		
//	}
//	
//}
