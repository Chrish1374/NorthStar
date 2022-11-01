package com.trn.ns.test.obsolete;
//package com.terarecon.northstar.test.obsolete;
//
//import java.awt.AWTException;
//
//import java.util.Set;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
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
//@Listeners(com.terarecon.northstar.test.listeners.ItestCustomListener.class)
//public class DistanceMeasurementTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//
//	//Test data=MR_LSP
////	@Test(groups ={"Chrome","multimonitor","DE206"})
//	public void test04_DE206_TC836_verifyMeasurementOnChildWindow() throws AWTException, InterruptedException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify measurements drawn on second viewbox of parent window are displayed on child window as well when layout is changed to 1*1 from parent window "); 
//		String filePath = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(PatientName);
//		
//		studyPage.clickOntheFirstStudy();		
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon,true);		
//		viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.viewboxImg2);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Drawing the measurement on coordinates (0,0,100,0) on second viewbox" );
//		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.viewboxImage2,0,0, 100, 0);
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage2,"DE206_TC836_LinearDistanceOnSecondViewbox","TC836_LinearDistanceOnSecondViewbox");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon,true);
//		
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying linear distance displayed on child window" );
//				viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1,"DE206_TC836_LinearDistanceOnChildWindow","TC836_LinearDistanceOnChildWindow");
//			}
//	}
//	
//	//Test data=MR_LSP
////    @Test(groups ={"Chrome","multimonitor","DE203"})
//	public void test05_DE203_TC834_verifyMeasurementOnChildWindowOnLayoutChange() throws InterruptedException, AWTException{
//
//    	extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify drawn linear distance measurements are visible on the series on second window after page layout change");
//		//Loading the patient on viewer 
//		String filePath = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(PatientName);
//		
//		studyPage.clickOntheFirstStudy();		
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();	
//		childWinHandles.remove(parentWindow);
//		String childWindow = childWinHandles.iterator().next();
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon,true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the distance measurement from context Menu" );
//		viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.viewboxImg1);
//		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.viewboxImage1,0,0, 100, 0);				
//		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.viewboxImage2,0,0, 100,0);				
//		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.viewboxImage3,0,0, 100, 0);		
//		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.viewboxImage4,0,0, 100, 0);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verify linear distance is drawn on all viewboxes on parent window","TC834_parentWindowTwoByTwo");
//
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.maximizeWindow();
//		viewerpage.mouseHoverWithClick("visibility", viewerpage.viewboxImage1, 10, 10);
//		viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.viewboxImg1);
//		viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.viewboxImg1);
//		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.viewboxImage1,0,0, 100, 0);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verify linear distance is drawn on first viewbox of child window window","TC834_childWindowTwoByTwo");
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon,true);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"DE203_TC834_LinearDistanceOnParentWindow2*1","TC834_parentWindowTwoByOne");
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.maximizeWindow();
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"DE203_TC834_LinearDistanceOnChildWindow2*1","TC834_childWindowTwoByOne");
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon,true);
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"DE203_TC834_LinearDistanceOnParentWindow3*3","TC834_parentWindowThreeByThree");
//		viewerpage.switchToWindow(childWindow);
//		viewerpage.maximizeWindow();
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"DE203_TC834_LinearDistanceOnChildWindow3*3","TC834_childWindowThreeByThree");
//
//	}
//
//}
//
//
