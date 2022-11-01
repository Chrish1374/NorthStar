package com.trn.ns.test.obsolete;

//import java.util.List;
//
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.NoSuchElementException;
//import org.openqa.selenium.WebElement;
//
//import com.terarecon.northstar.page.factory.NSConstants;

//package com.terarecon.northstar.test.obsolete;
//
//import java.awt.AWTException;
//import java.io.IOException;
//import java.util.List;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.TimeoutException;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.FindBy;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.terarecon.northstar.page.factory.LoginPage;
//import com.terarecon.northstar.page.factory.NSConstants;
//import com.terarecon.northstar.page.factory.PatientListPage;
//import com.terarecon.northstar.page.factory.SinglePatientStudyPage;
//import com.terarecon.northstar.page.factory.ViewerPage;
//import com.terarecon.northstar.test.base.TestBase;
//import com.terarecon.northstar.test.configs.Configurations;
//import com.terarecon.northstar.utilities.DataReader;
//import com.terarecon.northstar.utilities.ExtentManager;
//
//@Listeners(com.terarecon.northstar.test.listeners.ItestCustomListener.class)
//public class PolylineAnnotationTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//
//	String gspsFilePath1 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POLYLINE_filepath");
//	String gspsPolylinePatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath1);
//
//	String gspsFilePath2 =Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath2);
//
//	String gspsFilePath3 =Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
//	String ctNeckPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath3);
//
//	String gspsFilePath4 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^PolyLine^Closed_filepath");
//	String gspsPolylinePatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath4);
//
//	String gspsFilePath5 =Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath5);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US521","Sanity"})
//	public void test01_US521_TC1820_TC1821_TC1822_TC1823_TC1897_TC1909_verifyPolylinePresence() throws IOException, AWTException, InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("GSPS : Verify that polyline should be displayed when viewers page loaded <\br> GSPS: Verify that accept/reject context menu should be displayed just above the GSPS highlighted polyline on viewers page"
//				+ "<\br> GSPS : Verify that polyline should be rejected on clicking cross button of accept/reject radial context menu."
//				+ "<\br> GSPS : Verify that polyline should be accepted on clicking green accept button of accept/reject radial context menu."
//				+ "<\br> Verify that on clicking Previous arrow button previous finding should be highlighted and if there are no findings in current slice then on clicking previous button previous slice finding should be highlighted"
//				+ "<\br> Verify that polyline icon should be present in context menu >>Basic tab in Labeling section");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPolylinePatientName1+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(gspsPolylinePatientName1);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		// verifying the polyline rendering
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying the polyline is displayed and highlighted", "test01_checkpoint1");
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1),1,"verifying that there is already 1 polyline present","verified");
//
//		// verifying the radial menu presence
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu presence", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
//
//		// verifying the polyline is accepted and highlighted
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1,1), "Verify polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//		// rejecting the polyline and verifying its state
//		viewerpage.selectRejectfromGSPSRadialMenu();
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying the polyline is rejected", "test01_checkpoint2");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");viewerpage.assertTrue(viewerpage.gspsAccept.isDisplayed(), "Verify accept button on radial menu", "The accept button is displayed on radial menu");
//
//		// accepting the polyline and verifying its state
//		viewerpage.selectAcceptfromGSPSRadialMenu();
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying the polyline is accepted", "test01_checkpoint3");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verify polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(), "Verify reject button on radial menu", "The reject button is displayed on radial menu");
//
//		// verifying the selection of polyline on radial and verifying it on context menu 
//		viewerpage.selectPolylineFromRadialMenu(1);
//		viewerpage.assertTrue(viewerpage.isPolyLineIconDisplayedInOuterArc(viewerpage.viewboxImg1),"Verifying polyline is present in outer arc", "Verified presence of polyline annotation");
//		viewerpage.click(viewerpage.allIcon);
//		viewerpage.closeContextMenu();
//
//		//Clicking on the 3 dots icon on radial bar
//		viewerpage.openContextMenu(viewerpage.viewboxImg1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that polyline annotation is selected and enabled");
//		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(NSConstants.POLYLINE),"Verifying polyline icon is selected", "polyline Annotation icon is selected");
//		viewerpage.closeContextMenu();
//
//		// Loading the closed polyline and verifying the navigation if there is no other finding available
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//
//		patientPage.clickOnPatientRow(gspsPolylinePatientName2);
//
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu presence", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "Verify radial menu", "The radial menu is displayed");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying the closed polyline", "test01_checkpoint4");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
//
//		viewerpage.selectPreviousfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1,1), "Verify last polyline annotation is current pending Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), currentImage, "verifying on navigation image is same since there is one finding", "verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying the closed polyline on previous navigation", "test01_checkpoint5");
//
//		viewerpage.selectNextfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1,1), "Verify last polyline annotation is current pending Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), currentImage, "verifying on navigation image is same since there is one finding", "verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying the closed polyline on next navigation", "test01_checkpoint6");
//
//
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US521","Sanity"})
//	public void test02_US521_TC1824_verifyPolylineGSPSNavigation() throws IOException, AWTException, InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("	GSPS : Verify that on accepting or rejecting any finding, next GSPS finding should be highlighted and accept/reject radial context menu should display just above highlighted finding");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ctNeckPatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ctNeckPatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.inputImageNumber(13, 3);	
//
//		// on changing the slice verifying non of the annotation is highlighted
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsPendingGSPS(3,1), "Verify last polyline annotation but not highlighted", "verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		// opening the GSPS radial menu
//		viewerpage.performMouseRightClick(viewerpage.getLinesOfPolyLine(3,1).get(0));
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//		//	rejecting the poly line
//		viewerpage.selectRejectfromGSPSRadialMenu(3);
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsRejectedGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,1),"verifying on rejection another finding is selected" , "verified");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"verifying that radial menu is displayed","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		//	accepting the poly line
//		viewerpage.selectPreviousfromGSPSRadialMenu(3);
//
//		viewerpage.selectAcceptfromGSPSRadialMenu(3);
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsAcceptedGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,1),"verifying on rejection another finding is selected" , "verified");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"verifying that radial menu is displayed","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US521","Sanity"})
//	public void test03_US521_TC1896_verifyPolylineGSPSNavigation() throws IOException, AWTException, InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that if there are no GSPS finding in Current Slice then clicking on next arrow next slice GSPS finding should be highlighted");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ctNeckPatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ctNeckPatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.inputImageNumber(13, 3);	
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsPendingGSPS(3,1), "Verify last polyline annotation is non Active GSPS object", "The first point is current active GSPS object");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.performMouseRightClick(viewerpage.getLinesOfPolyLine(3,1).get(0));
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(),"verifying that radial menu is displayed","verified");
//
//		// navigating next using GSPS radial menu
//		viewerpage.selectNextfromGSPSRadialMenu(3);
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsPendingGSPS(3,1), "Verify last polyline annotation is current non Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,1),"verifying on rejection another finding is selected" , "verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//
//		// navigating previous using GSPS radial menu
//		viewerpage.selectPreviousfromGSPSRadialMenu(3);
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		// verifying that on performing the previous icon - navigate to previous slice 
//		int currentPageNumber = viewerpage.getCurrentScrollPositionOfViewbox(3);
//		viewerpage.selectPreviousfromGSPSRadialMenu(3);
//		int prevNavigatedPage = viewerpage.getCurrentScrollPositionOfViewbox(3);
//
//		viewerpage.assertTrue(prevNavigatedPage < currentPageNumber, "verifying that on previous navigation slice is changed", "verified");		
//		viewerpage.assertEquals(viewerpage.getLinesOfPolyLine(3,1).size(),0,"vrifying that slice has changed and polyline no longer present","verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu presence", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "Verify GSPS radial menu", "radial menu is displayed");
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,2),"verifying on rejection another finding is selected" , "verified");
//
//		// verifying that on performing the next icon - navigation with in slide and then navigate to next slice 
//		viewerpage.inputImageNumber(13, 3);		
//		currentPageNumber = viewerpage.getCurrentScrollPositionOfViewbox(3);
//		viewerpage.openGSPSRadialMenu(viewerpage.getLinesOfPolyLine(3,1).get(0));
//
//		viewerpage.selectNextfromGSPSRadialMenu(3);
//		viewerpage.selectNextfromGSPSRadialMenu(3);
//		viewerpage.selectNextfromGSPSRadialMenu(3);
//		viewerpage.selectNextfromGSPSRadialMenu(3);
//
//		int nextNavigatedPage = viewerpage.getCurrentScrollPositionOfViewbox(3);
//		viewerpage.assertTrue(currentPageNumber < nextNavigatedPage, "verifying that on next navigation slice is changed", "verified");		
//		viewerpage.assertTrue(viewerpage.verifyCircleAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last circle annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox","US521","Sanity"})
//	public void test04_US521_TC1902_verifyPolylineGSPSNavigationusingKeyboardArrowKeys() throws IOException, AWTException, InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("erify that user press page down from keyboard next slice GSPS finding should be highlighted");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ctNeckPatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ctNeckPatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.inputImageNumber(13, 3);	
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsPendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.performMouseRightClick(viewerpage.getLinesOfPolyLine(3,1).get(0));
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//		// verifying the keyboard right arrow navigation
//		viewerpage.navigateGSPSForwardUsingKeyboard();
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsPendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,1),"verifying on keyboard navigation another finding is selected" , "verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(2).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		// verifying the keyboard left arrow navigation
//		viewerpage.navigateGSPSBackUsingKeyboard();
//
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: color verification" ,"verified");
//		viewerpage.assertEquals(viewerpage.getPoint(3, 1).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH),NSConstants.NON_ACTIVE_GSPS_WIDTH,"Verify that accept/reject context menu should  be displayed just above the highted polyline.: width vrification","verified");
//
//		// verifying that on left key navigation slice is changed if no finding 
//		int currentPageNumber = viewerpage.getCurrentScrollPositionOfViewbox(3);
//		viewerpage.navigateGSPSBackUsingKeyboard();
//		int prevNavigatedPage = viewerpage.getCurrentScrollPositionOfViewbox(3);
//
//		viewerpage.assertTrue(prevNavigatedPage < currentPageNumber, "verifying that on previous navigation slice is changed", "verified");		
//		viewerpage.assertEquals(viewerpage.getLinesOfPolyLine(3,1).size(),0,"verifying that slice has changed and polyline no longer present","verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu presence", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verify that GSPS radial menu presence", "present");
//
//		// verifying the slice is changed on keyboard key press if it is last finding
//		viewerpage.inputImageNumber(13, 3);			
//		currentPageNumber = viewerpage.getCurrentScrollPositionOfViewbox(3);
//		viewerpage.openGSPSRadialMenu(viewerpage.getLinesOfPolyLine(3,1).get(0));
//
//		viewerpage.navigateGSPSForwardUsingKeyboard();
//		viewerpage.navigateGSPSForwardUsingKeyboard();
//		viewerpage.navigateGSPSForwardUsingKeyboard();
//		viewerpage.navigateGSPSForwardUsingKeyboard();
//
//		int nextNavigatedPage = viewerpage.getCurrentScrollPositionOfViewbox(3);
//		viewerpage.assertTrue(currentPageNumber < nextNavigatedPage, "verifying that on next navigation slice is changed", "verified");	
//		viewerpage.assertTrue(viewerpage.verifyCircleAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last circle annotation is current Active GSPS object", "The first point is current active GSPS object");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US521","Sanity"})
//	public void test05_US521_TC1903_verifyPageUpDownKeyNavigation() throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user press page up from keyboard next slice GSPS finding should be highlighted");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ctNeckPatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.inputImageNumber(13, 3);	
//		viewerpage.waitForTimePeriod(2000);
//
//		// Press Page Down key on Keyboard
//		viewerpage.scrollDownGSPSUsingKeyboard();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify on pressing page down key GSPS is automatically shifted to the next GSPS result on the slice");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3), 19, "Verify next slice having GSPS object load in Viewbox3", "The current slice in ViewBox 3 is "+viewerpage.getCurrentScrollPositionOfViewbox(1));
//		viewerpage.assertTrue(viewerpage.verifyCircleAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last circle annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verifying the radial menu is also displayed", "verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage3, "verifying the circle is highlighted on page down", "test03_checkpoint1");
//
//		// Press Page Down key on Keyboard
//		viewerpage.scrollDownGSPSUsingKeyboard();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify on pressing page down key GSPS is automatically shifted to the next GSPS result on the slice");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3), 6, "Verify next slice having GSPS object load in Viewbox3", "The current slice in ViewBox 3 is "+viewerpage.getCurrentScrollPositionOfViewbox(1));
//		viewerpage.assertTrue(viewerpage.verifyCircleAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last circle annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verifying the radial menu is also displayed", "verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage3, "verifying the circle is highlighted on page down", "test03_checkpoint2");
//
//		// Press Page Down key on Keyboard
//		viewerpage.scrollDownGSPSUsingKeyboard();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify on pressing page down key GSPS is automatically shifted to the next GSPS result on the slice");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3), 13, "Verify next slice having GSPS object load in Viewbox3", "The current slice in ViewBox 3 is "+viewerpage.getCurrentScrollPositionOfViewbox(1));
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verifying the radial menu is also displayed", "verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage3, "verifying the polyline is highlighted on page down", "test03_checkpoint3");
//
//		// Press Page up key on Keyboard		
//		viewerpage.scrollUpGSPSUsingKeyboard();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify on pressing page down key GSPS is automatically shifted to the next GSPS result on the slice");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3), 6, "Verify next slice having GSPS object load in Viewbox3", "The current slice in ViewBox 3 is "+viewerpage.getCurrentScrollPositionOfViewbox(1));
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,2), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");
//		//		viewerpage.assertTrue(viewerpage.verifyCircleAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last circle annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verifying the radial menu is also displayed", "verified");
//		//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage3, "verifying the circle is highlighted on page down", "test03_checkpoint4");
//
//		// Press Page up key on Keyboard
//		viewerpage.scrollUpGSPSUsingKeyboard();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify on pressing page down key GSPS is automatically shifted to the next GSPS result on the slice");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3), 19, "Verify next slice having GSPS object load in Viewbox3", "The current slice in ViewBox 3 is "+viewerpage.getCurrentScrollPositionOfViewbox(1));
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,2), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");
//		//		viewerpage.assertTrue(viewerpage.verifyCircleAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last circle annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verifying the radial menu is also displayed", "verified");
//		//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage3, "verifying the circle is highlighted on page down", "test03_checkpoint5");
//
//
//		// Press Page up key on Keyboard
//		viewerpage.scrollUpGSPSUsingKeyboard();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify on pressing page down key GSPS is automatically shifted to the next GSPS result on the slice");
//		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3), 13, "Verify next slice having GSPS object load in Viewbox3", "The current slice in ViewBox 3 is "+viewerpage.getCurrentScrollPositionOfViewbox(1));
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentPendingActiveGSPS(3,3), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");
//		//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(3,1), "Verify last polyline annotation is current Active GSPS object", "The first point is current active GSPS object");
//		viewerpage.assertTrue(viewerpage.verifyPendingGSPSToolbarMenu(), "verifying the radial menu is also displayed", "verified");
//		//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage3, "verifying the polyline is highlighted on page down", "test03_checkpoint6");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test06_US589_TC1910_TC1911_TC1912_TC2055_verifycreationOfPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user should be able to draw polyline and Finish (by double click or By ESC keypress) Polyline"
//				+ "<\br> Verify that polyline annotaion icon keep selected even after annoation drawn in viewbox."
//				+ "<\br> Verify that user should be able to move Polyline after selecting it."
//				+ "<\br> Verify that no handlers should be display when user finishes drawing of polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33});
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1),1,"verifying that there is already 1 polyline present","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline exit using double click", "test06_checkpoint1");
//
//		viewerpage.drawPolyLineExitUsingESCKey(2, new int[] {70,-60,33,-7,27,30,21,25,40,-28,44,-22,5,-51,47,-23,8,-45,13,-30,-40,-20,-59,11,-47,36,-63,60,-21,21});
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(2),1,"verifying that there is already 1 polyline present","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying polyline exit using double click", "test06_checkpoint2");
//
//		//Clicking on the 3 dots icon on radial bar
//		viewerpage.openContextMenu(viewerpage.viewboxImg1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that polyline annotation is selected and enabled");
//		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(NSConstants.POLYLINE),"Verifying polyline icon is selected", "polyline Annotation icon is selected");
//		viewerpage.closeContextMenu();
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.getLinesOfPolyLine(1, 1).get(1), 0, 0, 50, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying movement of polyline", "test06_checkpoint3");
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.getLinesOfPolyLine(1, 1).get(1), 0, 0, -100, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying movement of polyline", "test06_checkpoint4");
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.getLinesOfPolyLine(1, 1).get(1), 0, 0,200, -100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying movement of polyline", "test06_checkpoint5");
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.getLinesOfPolyLine(1, 1).get(1), 0, 0, 200, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying movement of polyline", "test06_checkpoint6");
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test07_US589_TC1913_TC1944_verifyZoomAndPanWithPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that on zoom the size of Polyline will be change proportionally with image"
//				+ "<\br> Verify that when user PAN the image size of annotation Polyline should not change and it should move with image");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(liver9PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {-10,10,25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36});
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1),1,"verifying that there is already 1 polyline present","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline exit using double click", "test07_checkpoint1");
//
//		viewerpage.selectZoomFromQuickToolbar(1);
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, -100, -100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline post zoom ", "test07_checkpoint2");
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, 0, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline post zoom ", "test07_checkpoint3");
//
//		viewerpage.selectPolylineFromRadialMenu(2);
//
//		viewerpage.drawPolyLine(2, new int[] {-10,10,25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36});
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(2),1,"verifying that there is already 1 polyline present","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying polyline exit using double click", "test07_checkpoint4");
//
//		viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg2);
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, -100, -100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline post pan ", "test07_checkpoint5");
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, 0, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline post pan ", "test07_checkpoint6");
//
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test08_US589_TC1946_verifycreationOfPolyLineOnJPGorPDF() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is not able to draw Polyline on PDF/JPEG/PNG.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLineExitUsingESCKey(3, new int[] {25,44,20,-32,37,-23,37,-9});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline on jpg", "test08_checkpoint1");
//
//		viewerpage.drawPolyLineExitUsingESCKey(4, new int[] {25,44,20,-32,37,-23,37,-9});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline on pdf", "test08_checkpoint2");
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test09_US589_TC1948_TC1980_TC1981_verifyResizeOfPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("	Verify polyline annotation behaviour (select, move resize) for GSPS image"
//				+ "</br> Verify that user should be able to edit (resize) polyline"
//				+ "</br> Verify that any point of the polyline can connect to any other point of polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		//		viewerpage.drawPolyLine(1, new int[] {25,30,70,70,-50,40,-30,20,30,-20, 10,30});
//		//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test09_checkpoint1");
//
//
//		//		viewerpage.drawPolyLine(1, new int[] {70,70,-70,70,-70,-70,70,-70,-35,-35,-30,30,40,40});		
//		viewerpage.drawPolyLine(1, new int[] {-70,-70,70,-70,70,70,-70,70,35,35,30,-30,-40,-40});	
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test09_checkpoint1");
//
//		viewerpage.getLinesOfPolyLine(1, 1).get(3).click();		
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getPolylineHandles(1).get(1),0,0,-10,-10);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test09_checkpoint2");
//
//		viewerpage.getLinesOfPolyLine(1, 1).get(5).click();
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getPolylineHandles(1).get(2),0,0,50,50);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test09_checkpoint3");
//
//		viewerpage.getLinesOfPolyLine(1, 1).get(3).click();		
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getPolylineHandles(1).get(5),0,0,20,20);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test09_checkpoint4");
//
//		viewerpage.getLinesOfPolyLine(1, 1).get(3).click();		
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getPolylineHandles(1).get(3),0,0,90,90);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test09_checkpoint5");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test10_US589_TC1949_TC1951_TC1952_verifyGSPSRadialMenuOnPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("GSPS: Verify that accept/reject context menu should be displayed when GSPS polyline displayed in viewers page"
//				+ "<\br> GSPS : Verify that polyline should be rejected on clicking cross button of accept/reject radial context menu."
//				+ "<\br> GSPS : Verify that polyline should be accepted on clicking green accept button of accept/reject radial context menu.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {-70,-70,70,-70,70,70,-70,70,35,35,30,-30,-40,-40});		
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test10_checkpoint1");		
//
//		viewerpage.drawPolyLine(2, new int[] {-70,-70,70,-70,70,70,-70,70,35,35,30,-30,-40,-40});		
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying polyline in viewbox 2", "test10_checkpoint2");
//
//
//		viewerpage.openGSPSRadialMenu(viewerpage.getLinesOfPolyLine(1,1).get(1));
//		viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(),"verifying the radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verifying that current polyline is accepted active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying GSPS radial menu in viewbox 1", "test10_checkpoint3");
//
//		viewerpage.selectRejectfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyAcceptGSPSRadialMenu(),"verifying the accepted radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"verifying that current polyline is rejected active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline is rejected", "test10_checkpoint4");
//
//		viewerpage.selectAcceptfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(),"verifying the rejected radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verifying that current polyline is accepted active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline is rejected", "test10_checkpoint5");
//
//		viewerpage.openGSPSRadialMenu(viewerpage.getLinesOfPolyLine(2,1).get(1));
//		viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(),"verifying the radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"verifying that current polyline is active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying GSPS radial menu in viewbox 2", "test10_checkpoint6");
//
//		viewerpage.selectRejectfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyAcceptGSPSRadialMenu(),"verifying the accepted radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(2, 1),"verifying that current polyline is rejected active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying polyline is rejected", "test10_checkpoint7");
//
//		viewerpage.selectAcceptfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(),"verifying the rejected radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"verifying that current polyline is accepted active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying polyline is rejected", "test10_checkpoint8");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test11_US589_TC1954_verifyGSPSNavigationOnPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("GSPS : Verify that on accepting or rejecting any finding, next GSPS finding should be highlighted and accept/reject radial context menu should display just above highlighted finding");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {-70,-70,70,-70,70,70,-70,70,35,35,30,-30,-40,-40});		
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test11_checkpoint1");		
//
//		viewerpage.selectPointAnnotationIconFromRadialMenu(2);
//		viewerpage.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2, "verifying point in viewbox 1", "test11_checkpoint2");
//
//		viewerpage.openGSPSRadialMenu(viewerpage.getLinesOfPolyLine(1,1).get(1));
//		viewerpage.selectRejectfromGSPSRadialMenu();
//		viewerpage.assertTrue(viewerpage.verifyRejectGSPSRadialMenu(),"verifying the radial menu presence","verified");
//		viewerpage.assertTrue(viewerpage.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"verifying that current polyline is rajected but not active","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying GSPS radial menu in viewbox 1", "test11_checkpoint3");
//
//		viewerpage.assertTrue(viewerpage.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"verifying that point is highlighted","verified");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying point and GSPS radial menu in viewbox 1", "test11_checkpoint4");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test12_US589_TC1955_TC1960_TC2021_verifyOverlappingPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that when two polylines overlaps each other than only one should get selected."
//				+ "<\br> Verify that Position of polyline should not get change on layout change"
//				+ "<\br> Verify that when user select polyline then all handlers should be displayed");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {-90,-90,90,-90,90,90,-90,90,45,45,60,-60,-80,-80});		
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test12_checkpoint1");	
//
//		viewerpage.drawPolyLine(1, new int[] {-70,-70,50,-80,80,-20,-30,60,-10,-30});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying another ovrlapping polyline", "test12_checkpoint2");	
//
//		viewerpage.click(viewerpage.getLinesOfPolyLine(1,1).get(1));
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline1 is selected", "test12_checkpoint3");	
//
//		viewerpage.click(viewerpage.getLinesOfPolyLine(1,2).get(1));
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline2 is selected", "test12_checkpoint4");
//
//		viewerpage.changeLayout();
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline post layout change", "test12_checkpoint5");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test13_US589_TC1983_TC1947_verifyPolyLineStopsWhenMouseGoesToAnotherViewebox() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that drawing of polyine finishes when user perform click in any other viewbox");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {0,-100,-100,0,-100,50,100,50,50,0,0,-50});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying polyline when another viewbox gets active", "test13_checkpoint1");
//		viewerpage.mouseHover(viewerpage.getViewPort(2));
//
//		viewerpage.click(viewerpage.getLinesOfPolyLine(1,1).get(2));
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying polyline on selection", "test13_checkpoint2");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test14_US589_TC1982_verifyClosedPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that start handler should turn into yellow color on joining starting point to end point of polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		// draw of closed polyline
//		//		viewerpage.drawClosedPolyLine(1, new int[] {0,0,-90,-90,90,-90,90,90,-86,90});	
//		viewerpage.drawClosedPolyLine(1, new int[] {500,300,100,300,100,100,500,100,500,300});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test14_checkpoint1");
//
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.getLinesOfPolyLine(1, 1).get(1), 0, 0, 10, 10);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test14_checkpoint2");
//
//		//draw open polyline and then edit it to make close polyline
//
//		viewerpage.drawPolyLine(1, new int[] {0,-100,-100,0,-50,50,50,50,100,0});
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.getPolylineHandles(1).get(5), 0, 0, 2, -101);
//		//		viewerpage.clickUsingAction(viewerpage.getLinesOfPolyLine(1,2).get(2));
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test14_checkpoint3");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test15_US589_TC2020_verifyNewPOintInPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user should be able to add new point to existing polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {0,-100,-100,0,-50,50,50,50,100,0});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test15_checkpoint1");
//
//		//		viewerpage.click(viewerpage.getLinesOfPolyLine(1,1).get(1));
//
//		int points = viewerpage.getPolylineHandles(1).size();
//
//		viewerpage.mouseHover(viewerpage.viewboxImg2);
//		viewerpage.mouseHover(viewerpage.viewboxImg1);
//
//		viewerpage.createNewPointsInPolyLine(0, -100, new int[] {20,30,70,70});								
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying new point on polyline", "test15_checkpoint2");
//
//		viewerpage.click(viewerpage.getLinesOfPolyLine(1,1).get(1));
//
//		viewerpage.assertEquals(viewerpage.getPolylineHandles(1).size(), (points+2), "verifying the new point created", "verified");
//
//		viewerpage.mouseHover(viewerpage.viewboxImg2);
//		viewerpage.mouseHover(viewerpage.viewboxImg1);
//
//		viewerpage.createNewPointsInPolyLine(0,0, new int[] {-55,-55});								
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying new point on polyline", "test15_checkpoint3");
//
//		viewerpage.click(viewerpage.getLinesOfPolyLine(1,1).get(1));
//
//		viewerpage.assertEquals(viewerpage.getPolylineHandles(1).size(), (points+3), "verifying the new point created", "verified");
//
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","US589","Sanity"})
//	public void test16_US589_TC2052_TC2053_verifyHandlersDuringCreationAndEditingPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that handler should be display (for new point ) after adding new point at time of drawing new polyline"
//				+ "Verify that all handlers should be display except resize point handler at time editing existing polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		int []coOrdinates = new int[] {0,-100,-100,0,-50,50,50,50,100,0};
//
//		// verifying the handlers for new polyline 
//		Actions action = new Actions(driver);		
//		for(int i= 0, j=0; i < coOrdinates.length; i+= 2,j++) {
//
//			action.moveByOffset(coOrdinates[i], coOrdinates[i+1]).build().perform();
//			action.clickAndHold().build().perform();
//			viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline handlers", "test16_checkpoint_"+j);
//			action.release().build().perform();
//			if(i + 2 == coOrdinates.length) {
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline before double click", "test16_checkpoint_final");
//				action.moveByOffset(coOrdinates[i], coOrdinates[i+1]).doubleClick().build().perform();
//				viewerpage.waitForTimePeriod(200);
//			}
//		}
//
//		// verifying the handlers for new point
//
//		action.moveByOffset(0, -100).build().perform();
//		viewerpage.waitForTimePeriod(2000);
//		action.clickAndHold().build().perform();
//		viewerpage.waitForTimePeriod(2000);
//		action.release().build().perform();
//
//		int []newpoint = new int[] {40,40,70,70};		
//		for(int i= 0, j=0; i < newpoint.length; i+= 2,j++) {
//
//			action.moveByOffset(newpoint[i], newpoint[i+1]).build().perform();
//			viewerpage.waitForTimePeriod(2000);
//			viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline handlers", "test16_checkpoint_newpoint_"+j);
//			action.clickAndHold().build().perform();
//			viewerpage.waitForTimePeriod(2000);
//			action.release().build().perform();
//			if(i + 2 == newpoint.length) {
//				viewerpage.waitForTimePeriod(2000);
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline before double click", "test16_cp_newpoint_final");
//				action.moveByOffset(newpoint[i], newpoint[i+1]).doubleClick().build().perform();
//				viewerpage.waitForTimePeriod(200);
//			}
//		}
//
//
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge"})
//	public void test17_DE542_TC2167_verifyDoubleClickOneUpPostPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that after drawing/resizing/editing polyline One Up scenario should work on double click when other command apart from polyline is selected");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(liver9PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {10,-100,-100,10,-50,50,50,50,100,0});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline", "test17_checkpoint1");
//
//		viewerpage.selectZoomFromQuickToolbar(1);
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg1, 0, 0, 100, 50);
//
//		viewerpage.doubleClick(viewerpage.viewboxImg1);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying double click post polyline", "test17_checkpoint2");
//
//		viewerpage.doubleClick(viewerpage.viewboxImg1);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying double click post polyline", "test17_checkpoint3");
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge"})
//	public void test18_DE542_TC2168_verifyOtherAnnotationPostPolyLineResize() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that image should get display after drawing polyline on viewer");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(liver9PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		viewerpage.drawPolyLine(1, new int[] {10,10,50,50,-30,50,-100,100});		
//
//
//		viewerpage.selectPointAnnotationIconFromRadialMenu(1);		
//		viewerpage.drawPointAnnotationMarkerOnViewbox(1, 30, 40);
//
//		// creating new point
//		viewerpage.getLinesOfPolyLine(1, 1).get(2).click();		
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getPolylineHandles(1).get(1),0,0,30,30);
//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Creating new point in polyline", "test18_checkpoint1");
//
//		// moving the polyline
//		viewerpage.getLinesOfPolyLine(1, 1).get(1).click();
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getLinesOfPolyLine(1,1).get(1),0,0,-30,-30);
//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "moving the polyline", "test18_checkpoint2");
//
//		viewerpage.drawPointAnnotationMarkerOnViewbox(1, 80, -90);
//		viewerpage.drawPointAnnotationMarkerOnViewbox(1, -100, 100);
//
//		viewerpage.drawPointAnnotationMarkerOnViewbox(2, 80, -90);
//		viewerpage.drawPointAnnotationMarkerOnViewbox(2, -100, 100);
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1),1,"verifying that there is only one polyline in viewbox 1 ","verified");
//		viewerpage.assertEquals(viewerpage.getPolylineCount(2),0,"verifying that there is none polyline present in viewbox 2","verified");
//
//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "verifying points", "test18_checkpoint3");
//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2), "verifying points", "test18_checkpoint4");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge"})
//	public void test19_DE542_TC2169_verifyRadialContextCSPostPolyline() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that radial menu and context menu should not get broken after drawing polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(liver9PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		// draw of closed polyline
//		//		viewerpage.drawClosedPolyLine(1, new int[] {0,0,50,50,-50,50,-50,-50,50,-50});	
//		viewerpage.drawClosedPolyLine(1, new int[] {500,300,100,300,100,100,500,100,500,300});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test19_checkpoint1");
//
//		//		viewerpage.getLinesOfPolyLine(1, 1).get(1).click();
//		viewerpage.assertEquals(viewerpage.getPolylineHandles(1).size(), 4, "verifying that there are only 4 handles in closed polyline", "verified");
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();		
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying polyline in viewbox 1 on rerender", "test19_checkpoint2");
//
//		viewerpage.performMouseRightClick(viewerpage.getViewPort(1));
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying radial on rendering viewbox", "test19_checkpoint3");
//
//		viewerpage.openContextMenu(viewerpage.getViewPort(1));
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying radial on rendering viewbox", "test19_checkpoint4");
//
//		viewerpage.closeContextMenu();
//		viewerpage.selectResultFromContentSelector(2, "GSPS_scan_1");
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying radial on rendering viewbox", "test19_checkpoint5");
//
//		viewerpage.openContentSelector(viewerpage.getViewbox(1), viewerpage.seriesDescriptionFirstViewBox);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewer, "verifying radial on rendering viewbox", "test19_checkpoint6");	
//
//
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge"})
//	public void test20_DE542_TC2170_verifyAnnotationonDoubleClickPolyline() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that annotation should not get deleted on double click after drawing polyline");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(liver9PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		// draw point
//		viewerpage.selectPointAnnotationIconFromRadialMenu(1);
//		viewerpage.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
//
//		// draw of closed polyline
//		viewerpage.selectPolylineFromRadialMenu(1);
//		viewerpage.drawPolyLine(1, new int[] {10,-100,-100,10,-50,50,50,50,100,0});	
//		
//		// draw point
//		viewerpage.selectCircleAnnotationIconFromRadialMenu(1);
//		viewerpage.drawCircle(1, -100, -100, 45, 45);
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1), 1, "verifying polyline", "verified");
//		viewerpage.assertEquals(viewerpage.getCircles(1).size(), 1, "verifying circle", "verified");
//		viewerpage.assertTrue(viewerpage.isPointPresent(1,1),"verifying the point#1", "point is present");
//
//		viewerpage.doubleClick();
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1), 1, "verifying polyline", "verified");
//		viewerpage.assertEquals(viewerpage.getCircles(1).size(), 1, "verifying circle", "verified");
//		viewerpage.assertTrue(viewerpage.isPointPresent(1,1),"verifying the point#1", "point is present");
//
//		viewerpage.doubleClick();
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1), 1, "verifying polyline", "verified");
//		viewerpage.assertEquals(viewerpage.getCircles(1).size(), 1, "verifying circle", "verified");
//		viewerpage.assertTrue(viewerpage.isPointPresent(1,1),"verifying the point#1", "point is present");
//
//		viewerpage.doubleClick();
//
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1), 1, "verifying polyline", "verified");
//		viewerpage.assertEquals(viewerpage.getCircles(1).size(), 1, "verifying circle", "verified");
//		viewerpage.assertTrue(viewerpage.isPointPresent(1,1),"verifying the point#1", "point is present");
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge"})
//	public void test21_DE663_TC2298_verifyPolyLineSelectionHandler() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("	Verify polyline gets selected after clicking on any line");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//
//		// draw of closed polyline
//		viewerpage.drawClosedPolyLine(1, new int[] {500,300,100,300,100,100,500,100,500,300});
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test21_checkpoint1");
//
//		int lines = viewerpage.getLinesOfPolyLine(1,1).size();
//		viewerpage.mouseHover(viewerpage.getViewPort(2));
//		for(int i =0;i<lines;i++ ){
//
//			viewerpage.clickUsingAction(viewerpage.getLinesOfPolyLine(1,1).get(i));
//			viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1, "verifying polyline in viewbox 1", "test21_checkpoint_"+i);
//			viewerpage.assertEquals(viewerpage.getPolylineHandles(1).size(),4,"verifying the handles","verified");
//			viewerpage.mouseHover(viewerpage.getViewPort(2));
//
//		}
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","DE734"})
//	public void test22_DE734_TC2925_verifyPageLoadedAfterPolyLine() throws TimeoutException, InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify page is getting loaded after drawingPolyline annotations");
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//		
//		studyPage.clickOntheFirstStudy();
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		//Select Polyline icon from radial menu
//		viewerpage.selectPolylineFromRadialMenu(1);
//		//draw Polyline
//		viewerpage.drawPolyLine(1, new int[] {0,-80,-80,0,-50,50,50,50,100,0});
//		viewerpage.drawPolyLineExitUsingESCKey(1, new int[] {0,0,85,85,-50,50,-70,-60,80,-80});
//		//Reload the study
//		viewerpage.browserBackWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]","Verifying user is navigated to single patient list screen");
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectPolylineFromRadialMenu(1);
//		//draw Polyline after reloading
//		viewerpage.drawPolyLine(1, new int[] {-70,-70,60,60,-70,-60,70,-70,-25,-25,-40,40,60,60});
//		//Reload the study again
//		viewerpage.browserBackWebPage();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]","Verifying user is navigated to single patient list screen");
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//		
//		// verifying the number of polyline
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]","Verifying polyLine count");
//		viewerpage.assertEquals(viewerpage.getPolylineCount(1), 3, "Verified PolyLine count", "Verified");
//
//	}
//	
//	
//	
////	Page Object 
//	
//	
//
//	// functions related to polyline
//
//	@FindBy(css="#viewer > ns-radialmenu > div > div > svg:nth-child(5) > svg:nth-child(1) > path")
//	public WebElement polylineIconOnRadial;
//
//	@FindBy(css="div[title='PolyLine']")
//	public WebElement polylineIconOnContextMenu;
//
//	public void selectPolylineFromRadialMenu(int whichViewbox) throws AWTException{
//
//		openRadialMenu(getViewPort(whichViewbox));
//		click(threeDotsIcon);
//		waitForElementVisibility(polylineIconOnRadial);
//		click(polylineIconOnRadial);
//
//	}
//
//	public void selectPolylineFromContextMenu(int whichViewbox) throws AWTException{
//		openRadialMenu(getViewPort(whichViewbox));
//		selectThreeDotIcon();
//		selectAllIconFromRadialBar();
//		selectPolylineIcon();
//	}
//
//	private void selectPolylineIcon() {
//
//		click(polylineIconOnContextMenu);
//		waitForElementInVisibility(radialMenuAll);
//
//	}
//
//	public boolean isPolyLineIconDisplayedInOuterArc(WebElement viewBox) throws AWTException{
//		Boolean status = false;
//		openRadialMenu(viewBox);
//		click(threeDotsIcon);
//		waitForElementVisibility(polylineIconOnRadial);
//		if(polylineIconOnRadial.isDisplayed()){
//			status = true;
//		}
//		return status;
//	}
//
//	public List<WebElement> getLinesOfPolyLine(int whichViewbox , int whichPolyline){
//
//		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g:nth-child("+whichPolyline+") > g > g > g line:only-child"));
//	}
//
//	public WebElement getPolyLineSvg(int whichViewbox , int whichPolyline){
//
//		WebElement polyline = getAllPolylines(whichViewbox).get(whichPolyline-1);
//
//		return polyline.findElement(By.cssSelector("svg > path"));
//	}
//
//	public List<WebElement> getPolylineHandles(int whichViewbox){
//
//		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+"> g > g > g > g > g:nth-child(3) > g circle + circle"));
//	}
//
//
//
//	public List<WebElement> getAllPolylines(int whichViewbox){
//
//		List<WebElement> elements = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g > g > g"));
//
//		return elements;
//
//
//	}
//
//	public int getPolylineCount(int whichViewbox){
//
//		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g > g > g svg + g +g")).size();
//
//	}
//
//	public void drawPolyLine(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{
//
//		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
//			mouseHover(PRESENCE, getViewbox(whichViewbox));
//			for(int i= 0; i < coOrdinates.length; i+= 2) {
//				clickAt(coOrdinates[i], coOrdinates[i+1]);
//
//				//represent last co-ordinate
//				if(i + 2 == coOrdinates.length) {
//					Actions action = new Actions(driver);
//					action.moveToElement(getViewPort(whichViewbox)).doubleClick().build().perform();
//					waitForTimePeriod(200);
//				}
//			}
//
//
//		}
//
//	}
//
//
//	public void drawClosedPolyLine(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{
//		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
//			mouseHover(PRESENCE, getViewbox(whichViewbox));
//			for(int i= 0; i < coOrdinates.length; i+= 2) {
//				Actions action = new Actions(driver);
//				action.moveToElement(getViewPort(whichViewbox),0,0).moveByOffset(coOrdinates[i], coOrdinates[i+1]).click().build().perform();
//				waitForTimePeriod(200);
//
//			}
//		}
//
//	}
//
//	public void editPolyLine(int whichviewbox,WebElement line, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{
//		Actions action = new Actions(driver);
//		//		getPolylineHandles(1).get(5)
//		action.moveToElement(getViewPort(whichviewbox)).clickAndHold().moveByOffset(to_xOffset, to_yOffset).release().perform();
//		//		action.dragAndDropBy(getPolylineHandles(1).get(5), to_xOffset, to_yOffset).build().perform();
//		waitForTimePeriod(100);
//
//	}
//
//	public void drawPolyLineExitUsingESCKey(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{
//
//		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
//			mouseHover(PRESENCE, getViewbox(whichViewbox));
//			for(int i= 0; i < coOrdinates.length; i+= 2) {
//				clickAt(coOrdinates[i], coOrdinates[i+1]);
//
//				//represent last co-ordinate
//				if(i + 2 == coOrdinates.length) {
//					//					pressKeys(Keys.ESCAPE);
//					Actions action = new Actions(driver);
//					action.moveToElement(getViewPort(whichViewbox)).sendKeys(Keys.ESCAPE).perform();
//					waitForTimePeriod(200);
//				}
//			}
//
//
//		}
//	}
//
//	public void createNewPointsInPolyLine(int pointx,int pointy,int[] coordinates) throws TimeoutException, InterruptedException{
//
//		clickAt(pointx, pointy);		
//		if(coordinates.length % 2 == 0) {
//
//			for(int i= 0; i < coordinates.length; i+= 2) {
//				clickAt(coordinates[i], coordinates[i+1]);		
//
//				//represent last co-ordinate
//				if(i + 2 == coordinates.length) {
//					doubleClick();
//					waitForTimePeriod(200);
//				}
//			}
//
//
//		}
//
//	}
//
//	public void createNewPointsInPolyLine(int whichviewbox, int whichPolyLine,int whichPoint,int xOfnewpoint, int yofnewpoint) throws TimeoutException, InterruptedException{
//
//		getLinesOfPolyLine(whichviewbox, whichPolyLine).get(whichPoint).click();		
//		dragAndReleaseOnViewer(getPolylineHandles(1).get(1),0,0,xOfnewpoint,yofnewpoint);
//
//	}
//
//
//}
//
//
//	public void selectPolyline(int whichViewbox, int whichPolyline){	
//		List<WebElement> elements = getLinesOfPolyLine(whichViewbox, whichPolyline);
//		elements.get(1).click();
//	}
//
//
//	public boolean isPolylineSelected(int whichViewbox){
//		boolean status = false ;
//		List<WebElement> allPolylines = getPolylineHandles(whichViewbox);
//		try{
//			if(allPolylines.size()>0){
//
//				status = true;
//			}
//		}
//		catch(NoSuchElementException e){
//			printStackTrace(e.getMessage());
//		}
//
//		return status;
//	}
//
//		public void deleteSelectedPolyline() throws InterruptedException{
//		pressKeys(Keys.DELETE); 
//	}
//	public boolean verifyPolyLineAnnotationIsRejectedGSPS(int whichViewbox, int whichPolyline){
//
//		boolean status = false;
//		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
//		boolean condition_color = polyLine.getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_COLOR);		
//		boolean condition_width = polyLine.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(NSConstants.NON_ACTIVE_GSPS_WIDTH);
//
//		if(condition_color && condition_width)
//			status=true;
//
//		return status;
//	}
//
//	public boolean verifyPolyLineAnnotationIsAcceptedGSPS(int whichViewbox, int whichPolyline){
//
//		boolean status = false;
//		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
//		boolean condition_color = polyLine.getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.ACCEPTED_COLOR);		
//		boolean condition_width = polyLine.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(NSConstants.NON_ACTIVE_GSPS_WIDTH);
//
//		if(condition_color && condition_width)
//			status=true;
//
//		return status;
//	} 
//
//	public boolean verifyPolyLineAnnotationIsPendingGSPS(int whichViewbox, int whichPolyline){
//
//		boolean status = false;
//		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
//		boolean condition_color = polyLine.getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR);		
//		boolean condition_width = polyLine.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(NSConstants.NON_ACTIVE_GSPS_WIDTH);
//
//		if(condition_color && condition_width)
//			status=true;
//
//		return status;
//	} 
//	public boolean verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, int whichPolyline){
//
//		boolean status = false;
//		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
//		boolean condition_color = polyLine.getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_COLOR);		
//		boolean condition_width = polyLine.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(NSConstants.ACTIVE_GSPS_WIDTH);
//
//		if(condition_color && condition_width)
//			status=true;
//
//		return status;
//	} 
//
//	public boolean verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(int whichViewbox, int whichPolyline){
//
//		boolean status = false;
//		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
//		boolean condition_color = polyLine.getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.ACCEPTED_COLOR);		
//		boolean condition_width = polyLine.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(NSConstants.ACTIVE_GSPS_WIDTH);
//
//		if(condition_color && condition_width)
//			status=true;
//
//		return status;
//	} 
//
//	public boolean verifyPolyLineAnnotationIsCurrentActivePendingGSPS(int whichViewbox, int whichPolyline){
//
//		boolean status = false;
//		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
//		boolean condition_color = polyLine.getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR);		
//		boolean condition_width = polyLine.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(NSConstants.ACTIVE_GSPS_WIDTH);
//
//		if(condition_color && condition_width)
//			status=true;
//
//		return status;
//	} 


