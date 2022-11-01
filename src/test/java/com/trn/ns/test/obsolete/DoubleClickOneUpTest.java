//package com.trn.ns.test.obsolete;
//
//
//import java.awt.AWTException;
//import java.io.IOException;
//import java.util.List;
//import java.util.Set;
//
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import org.xml.sax.SAXException;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PolyLineAnnotation;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class DoubleClickOneUpTest extends TestBase {
//
//	private PatientListPage patientPage;
//	
//	private LoginPage loginPage;
//	private ViewerPage viewerpage;
//	private ExtentTest extentTest;
//
//	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);    
//
//	String filePath2 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
//	String subject_60PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);    
//
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException, SAXException, IOException, ParserConfigurationException {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//
//	}     
//	//1. Zoom to fit CW Verification
//
//	@Test(groups ={"Edge","Chrome","firefox","multimonitor","US253"})
//	public void test01_US253_TC704_verifydoubleClickZoomToFit() throws InterruptedException, AWTException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Double Click to 1-Up-Double click");
//
//		patientPage = new PatientListPage(driver);          		    		
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		List<String> windowIDs = viewerpage.getAllOpenedWindowsIDs();
//
//
//		viewerpage.switchToWindow(windowIDs.get(1));
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		for (String childWindow : windowIDs) {
//
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);                        
//			viewerpage.switchToWindow(childWindow);
//
//			int preZoom = viewerpage.getZoomLevel(1);
//
//			viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//			viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImage1, 0, 0, 100,50);
//
//			int beforeZoom = viewerpage.getZoomLevel(1);
//
//			viewerpage.doubleClickOnViewbox(1);
//			viewerpage.waitForViewerpageToLoad();  
//
//			int afterZoom = viewerpage.getZoomLevel(1);    
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
//			viewerpage.assertEquals(beforeZoom, afterZoom, "Verify zoom % is getting retained", "verified");
//
//			viewerpage.doubleClickOnViewbox(1);
//			viewerpage.waitForViewerpageToLoad();       
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
//			viewerpage.assertEquals(afterZoom, viewerpage.getZoomLevel(1), "Verify zoom % is getting retained", "verified");
//
//			viewerpage.selectZoomOverlay(1,ViewerPageConstants.ZOOM_TO_FIT);
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
//			viewerpage.assertEquals(preZoom, viewerpage.getZoomLevel(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
//
//			viewerpage.doubleClickOnViewbox(1);
//			viewerpage.assertNotEquals(preZoom, viewerpage.getZoomLevel(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
//
//			viewerpage.doubleClickOnViewbox(1);
//			viewerpage.waitForViewerpageToLoad(1);
//			viewerpage.assertEquals(preZoom, viewerpage.getZoomLevel(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
//
//			viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);    
//
//
//		}
//	}
//	//2. Linear distance Verification
//
//	@Test(groups ={"Edge","Chrome","firefox","multimonitor","US253"})
//	public void test02_US253_TC706_verifyLinearDistanceOnDoubleClick() throws InterruptedException, AWTException  
//	{
//		String testCaseName = "US253_TC706";
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Double Click to 1-Up-Double click-Multiple windows-Annotations created by the user in the session are not lost by this action");
//
//		patientPage = new PatientListPage(driver);          		    		
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		List<String> windowIDs = viewerpage.getAllOpenedWindowsIDs();
//		windowIDs.remove(parentWindow);
//
//		viewerpage.switchToWindow(windowIDs.get(0));
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//				viewerpage.switchToWindow(childWindow);
//
//				viewerpage.waitForViewerpageToLoad();
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Drawing the measurement on coordinates (0,0,100,0)" );
////				viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.viewboxImg1);
//				viewerpage.dragAndReleaseOnViewer(0,0, 100, 0);
//
//				viewerpage.doubleClickOnViewbox(1);
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1,"Verify the measurement drawn @ coordinates (0,0,100,0)", testCaseName+"_checkpoint_1");
//
//
//				viewerpage.doubleClickOnViewbox(1);
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1,"Verify the measurement drawn @ coordinates (0,0,100,0)", testCaseName+"_checkpoint_2");
//
//			}
//		}
//	}
//	//3. Window Levelling Verification
//
//	@Test(groups ={"Edge","Chrome","firefox","multimonitor","US253"})
//	public void test03_US253_TC707_verifyWWWlOnDoubleClickUp() throws InterruptedException, AWTException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Double Click to 1-Up-Double click-Multiple windows-W/L as applied to the image is not lost by this action");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );
//
//		patientPage = new PatientListPage(driver);          		    		
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		List<String> windowIDs = viewerpage.getAllOpenedWindowsIDs();
//		windowIDs.remove(parentWindow);
//
//		viewerpage.switchToWindow(windowIDs.get(0));
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//				viewerpage.switchToWindow(childWindow);
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/21]", "Performing the right click" );
//
//
//				viewerpage.waitForViewerpageToLoad();
//				int beforeWWValue = Integer.parseInt(viewerpage.getWindowWidthValueOverlay(1).getText().trim()) ;
//				int beforeWCValue = Integer.parseInt(viewerpage.getWindowCenterValueOverlay(1).getText().trim());
//
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying WW/WL is enabled","WW/WL is enabled");
//				viewerpage.dragAndReleaseOnViewer(25, 50, 50, 100);
//
//
//				int afterWWValue = Integer.parseInt(viewerpage.getWindowWidthValueOverlay(1).getText().trim()) ;
//				int afterWCValue = Integer.parseInt(viewerpage.getWindowCenterValueOverlay(1).getText().trim());
//
//				viewerpage.assertNotEquals(beforeWWValue,afterWWValue, "Verifying WW functionality", "WW is working fine");
//				viewerpage.assertNotEquals(beforeWCValue,afterWCValue, "Verifying WC functionality", "WC is working fine");
//
//
//				viewerpage.doubleClickOnViewbox(1);
//
//				viewerpage.assertEquals(afterWWValue,Integer.parseInt(viewerpage.getWindowWidthValueOverlay(1).getText().trim()), "Verifying WW functionality", "WW is working fine");
//				viewerpage.assertEquals(afterWCValue,Integer.parseInt(viewerpage.getWindowCenterValueOverlay(1).getText().trim()), "Verifying WC functionality", "WC is working fine");
//
//
//				viewerpage.doubleClickOnViewbox(1);
//
//				viewerpage.assertEquals(afterWWValue,Integer.parseInt(viewerpage.getWindowWidthValueOverlay(1).getText().trim()), "Verifying WW functionality", "WW is working fine");
//				viewerpage.assertEquals(afterWCValue,Integer.parseInt(viewerpage.getWindowCenterValueOverlay(1).getText().trim()), "Verifying WC functionality", "WC is working fine");
//
//			}
//		}                 
//	}
//	//4. Zoom Verification
//
//	@Test(groups ={"Edge","Chrome","firefox","multimonitor","US253"})
//	public void test04_US253_TC708_verifyZoomOnDoubleClickUp() throws InterruptedException, AWTException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Double Click to 1-Up-Double click-Multiple windows-Zoom is kept when Double Click 1-up/down is applied");
//		String testcaseName = "US253_TC708";
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );
//
//		patientPage = new PatientListPage(driver);          		    		
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		List<String> windowIDs = viewerpage.getAllOpenedWindowsIDs();
//		windowIDs.remove(parentWindow);
//
//		viewerpage.switchToWindow(windowIDs.get(0));
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();      
//
//				viewerpage.switchToWindow(childWindow);
////				performAndverifyZoom(viewerpage.viewboxImg1, 1,testcaseName, "For Non Dicom");
//				viewerpage.doubleClickOnViewbox(1);
//
//			}
//		}
//	}
//	//5. Pan Synchronization Verification
//
//	@Test(groups ={"Edge","Chrome","firefox","multimonitor","US253"})
//	public void test05_US253_TC777_verifyPanWithDoubleClick() throws InterruptedException, AWTException  
//	{
//
//		String testcaseName = "US253_TC777";
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription( "Double Click to 1-Up-Double click-Multiple windows-Pan is kept when Double Click 1-up/down is applied");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );
//
//		patientPage = new PatientListPage(driver);          		    		
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOnStudy(1);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		List<String> windowIDs = viewerpage.getAllOpenedWindowsIDs();
//		windowIDs.remove(parentWindow);
//
//		viewerpage.switchToWindow(windowIDs.get(0));
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOnStudy(1);
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();        
//
//				viewerpage.switchToWindow(childWindow);                              
//
//				viewerpage.selectPanFromQuickToolbar(viewerpage.viewboxImg2);
//
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg2,0, 0, 300, 0);
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg2,300, 0, -600, 0);
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImg2,-300, 0, 300, -300);
//
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2,"Verifying images should PAN towards up.","Checkpoint_PAN_BeforeDoubleClick"+ "_" +testcaseName+ "_1");
//				viewerpage.doubleClickOnViewbox(2);
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2,"Verifying images should retain PAN on double click","Checkpoint_PAN_onDoubleClick"+ "_" +testcaseName+ "_1");
//				viewerpage.doubleClickOnViewbox(2);
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2,"Verifying images should retain PAN on restore of double click","Checkpoint_PAN_afterRestoreDoubleClick"+ "_" +testcaseName+ "_2");
//
//
//			}
//		}
//	}
//	//6. Scroll Verification
//
//	
//
//
//
//}
