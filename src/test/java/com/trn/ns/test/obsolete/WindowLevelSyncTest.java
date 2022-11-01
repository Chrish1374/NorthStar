//package com.trn.ns.test.obsolete;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Set;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//public class WindowLevelSyncTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private SinglePatientStudyPage singlePatientStudyPage;
//	private ExtentTest extentTest;
//
//	//Get Patient Name
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	//Get Patient Name
//	String filePath1 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
//	String subject60Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//
//	//Get Patient Name
//	String filePath2 = Configurations.TEST_PROPERTIES.get("CR_Thorax_Chest_filepath");
//	String crThoraxPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//
//	//Get Patient Name
//	String filePath3 = Configurations.TEST_PROPERTIES.get("XA_2_filepath");
//	String xapatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
//
//	//Get Patient Name
//	String filePath4 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"firefox","Chrome"})
//	public void test17_DE66_TC569_VerifyWLLChangePersistOnContinousScroll() throws InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Window leveling changes persist on Continous Scroll");
//		//Loading the patient on viewer
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(ah4patientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOntheFirstStudy();
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//		//Capturing default values for window center and window width value
//		String windowCenterValue_beforeWWL = viewerpage.getValueOfWindowCenter(1);
//		String widthValue_beforeWWL = viewerpage.getValueOfWindowWidth(1);
//		//Change window leveling on ViewBox1
//		viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.viewboxImage1,0, 0, 100, -50);
//		String windowCenterValue_afterWWL = viewerpage.getValueOfWindowCenter(1);
//		String widthValue_afterWWL = viewerpage.getValueOfWindowWidth(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify WWL changes on scrolling down");
//		viewerpage.assertNotEquals(Integer.parseInt(widthValue_afterWWL)  , Integer.parseInt(widthValue_beforeWWL), "verifying the WW/WL(width) in viewbox1", "verified");		
//		viewerpage.assertNotEquals(Integer.parseInt(windowCenterValue_afterWWL) , Integer.parseInt(windowCenterValue_beforeWWL), "verifying the WW/WL(center) in viewbox1", "verified");	
//		//Capturing current image position in ViewBox1
//		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		//Perform Mouse Scroll to change slice on View Box
//		//		viewerpage.selectRepeatIconFromContextMenuOthertab(viewerpage.viewboxImg1);
//		viewerpage.mouseWheelScrollInViewer(1,"down", 10);
//		viewerpage.waitForEndOfAllAjaxes();	
//		int imageAfterScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify image slice changes on scrolling down");
//		viewerpage.assertNotEquals(currentImage, imageAfterScroll, "Verify image slice changes on scrolling down", "Image slice on viewbox changes from "+currentImage+" to "+imageAfterScroll);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify WWL changes persist on changing silces");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))  , Integer.parseInt(widthValue_afterWWL), "verifying the WW/WL(width) in viewbox1", "verified");		
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1)) , Integer.parseInt(windowCenterValue_afterWWL), "verifying the WW/WL(center) in viewbox1", "verified");		
//		//Capturing default values for window center and window width value for ViewBox2
//		windowCenterValue_beforeWWL = viewerpage.getValueOfWindowCenter(2);
//		widthValue_beforeWWL = viewerpage.getValueOfWindowWidth(2);
//		//Capturing current image position in ViewBox2
//		currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		//Perform Mouse Scroll to change slice on View Box
//		viewerpage.mouseWheelScrollInViewer(1,"down", 5);
//		viewerpage.waitForEndOfAllAjaxes();	
//		imageAfterScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify image slice changes on scrolling down on ViewBox2");
//		viewerpage.assertNotEquals(currentImage, imageAfterScroll, "Verify image slice changes on scrolling down", "Image slice on viewbox changes from "+currentImage+" to "+imageAfterScroll);
//
//
//	}
//
//	
//	//Multimonitor 
//
//
//	@Test(groups ={"Chrome", "Edge","firefox","multimonitor","dbConfig"})
//	public void test03_US244_TC748_verifyRelativeWWWLSyncOnMultipleChildWindows() throws InterruptedException, IOException, SQLException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify by default WW/WL synchronization is on on multi-monitor and relative is configured in db.");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
//		db.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");		
//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"Verifying the By default sync is RELATIVE","verified");
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+subject60Patient+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//
//		viewerpage.switchToWindow(parentWindow);
//
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//
//		//changing layout to 2X3 and getting the Deltas w.r.t. fifth viewbox where we are gonna change the WWWL
//		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
//
//		viewerpage.waitForAllImagesToLoad();
//
//		// Getting the difference from fifth viewbox since WWWL is going to get applied to 5th viewbox
//		int viewbox1_widthDelta = Integer.parseInt(viewerpage.getValueOfWindowWidth(5))- Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int viewbox1_windowCenterDelta=Integer.parseInt(viewerpage.getValueOfWindowCenter(5))- Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		int viewbox2_widthDelta = Integer.parseInt(viewerpage.getValueOfWindowWidth(5))- Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//		int viewbox2_windowCenterDelta=Integer.parseInt(viewerpage.getValueOfWindowCenter(5))- Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//		int viewbox3_widthDelta = Integer.parseInt(viewerpage.getValueOfWindowWidth(5))- Integer.parseInt(viewerpage.getValueOfWindowWidth(3));
//		int viewbox3_windowCenterDelta=Integer.parseInt(viewerpage.getValueOfWindowCenter(5))- Integer.parseInt(viewerpage.getValueOfWindowCenter(3));
//
//		int viewbox4_widthDelta = Integer.parseInt(viewerpage.getValueOfWindowWidth(5))- Integer.parseInt(viewerpage.getValueOfWindowWidth(4));
//		int viewbox4_windowCenterDelta=Integer.parseInt(viewerpage.getValueOfWindowCenter(5))- Integer.parseInt(viewerpage.getValueOfWindowCenter(4));
//
//
//		//changing the layout from 2 by 3 to 2 by 2 and applying the WW/WL in child window
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
//
//		viewerpage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.waitForViewerpageToLoad();
//
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on child window" );
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//
//
//			}
//		}
//		// Getting the WW/WL parameters value from 5th viewbox from child window
//		int windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals((windowWidth_childviewbox1 - Integer.parseInt(viewerpage.getValueOfWindowWidth(1))), viewbox1_widthDelta, "Verifying the WW/WL(width) is changed based on delta on viewbox1", "verified");
//		viewerpage.assertEquals((windowWidth_childviewbox1 - Integer.parseInt(viewerpage.getValueOfWindowWidth(2))), viewbox2_widthDelta, "Verifying the WW/WL is changed based on delta on viewbox2", "verified");
//		viewerpage.assertEquals((windowWidth_childviewbox1 - Integer.parseInt(viewerpage.getValueOfWindowWidth(3))), viewbox3_widthDelta, "Verifying the WW/WL is changed based on delta on viewbox3", "verified");
//		viewerpage.assertEquals((windowWidth_childviewbox1 - Integer.parseInt(viewerpage.getValueOfWindowWidth(4))), viewbox4_widthDelta, "Verifying the WW/WL is changed based on delta on viewbox4", "verified");
//
//
//		viewerpage.assertEquals((windowCenter_childViewbox1 - Integer.parseInt(viewerpage.getValueOfWindowCenter(1))), viewbox1_windowCenterDelta, "Verifying the WW/WL(center) is changed based on delta on viewbox1", "verified");
//		viewerpage.assertEquals((windowCenter_childViewbox1 - Integer.parseInt(viewerpage.getValueOfWindowCenter(2))), viewbox2_windowCenterDelta, "Verifying the WW/WL(center) is changed based on delta on viewbox2", "verified");
//		viewerpage.assertEquals((windowCenter_childViewbox1 - Integer.parseInt(viewerpage.getValueOfWindowCenter(3))), viewbox3_windowCenterDelta, "Verifying the WW/WL(center) is changed based on delta on viewbox3", "verified");
//		viewerpage.assertEquals((windowCenter_childViewbox1 - Integer.parseInt(viewerpage.getValueOfWindowCenter(4))), viewbox4_windowCenterDelta, "Verifying the WW/WL(center) is changed based on delta on viewbox4", "verified");
//
//		viewerpage.openOrCloseChildWindows(1);
//
//
//
//	}
//
//	@Test(groups ={"Chrome", "Edge","firefox","multimonitor","dbConfig"})
//	public void test04_US244_TC747_verifyAbsoluteWWWLSyncOnMultipleChildWindows() throws InterruptedException, IOException, SQLException{
//
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify by default WW/WL synchronization is on on multi-monitor and absolute is configured in db.");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);
//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE,"Verifying the By default sync is ABSOLUTE","verified");
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+subject60Patient+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//		//Opening 1 more child window
//		String parentWindow = driver.getWindowHandle();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		Set<String> childWinHandles = driver.getWindowHandles();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//
//			}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.waitForViewerpageToLoad();
//
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on child window" );
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);			
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);				
//
//
//			}
//		}
//
//		int windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying the WW/WL(Width) is absolute in viewbox1", "verified");
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is absolute in viewbox2", "verified");
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(3)), "Verifying the WW/WL(Width) is absolute in viewbox3", "verified");
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(4)), "Verifying the WW/WL(Width) is absolute in viewbox4", "verified");
//
//
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying the WW/WL(center) is absolute in viewbox1", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is absolute in viewbox2", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(3)), "Verifying the WW/WL(center) is absolute in viewbox3", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(4)), "Verifying the WW/WL(center) is absolute in viewbox4", "verified");
//
//		viewerpage.openOrCloseChildWindows(1);
//
//	}
//
//	@Test(groups ={"Chrome", "Edge","firefox","multimonitor","dbConfig"})
//	public void test05_US244_TC750_verifyRelativeWWWLSyncOFFOnMultipleChildWindows() throws InterruptedException, SQLException{
//
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify WW/WL synchronization is off after clicking on space bar on multi-monitor and relative is configured in db.");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"verifying the WW/WL is RELATIVE","Verified");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+subject60Patient+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		db.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
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
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//
//		//changing layout to 2X3 Getting the Delta w.r.t. 5th viewbox
//		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
//
//		viewerpage.waitForAllImagesToLoad();
//
//		int viewbox1_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int viewbox1_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		int viewbox2_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//		int viewbox2_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//		int viewbox3_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(3));
//		int viewbox3_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(3));
//
//		int viewbox4_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(4));
//		int viewbox4_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(4));
//
//		int viewbox5_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(5));
//		int viewbox5_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(5));
//
//		// performing the sync OFF on parent window
//
//		viewerpage.performSyncONorOFF();
//
//		//changing layout to 2X1 and performing the WW/WL on 5th viewbox
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
//
//		viewerpage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerpage.switchToWindow(childWindow);
//
//
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on child window" );
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//			}
//		}
//
//		int windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//		//		viewbox5_widthBeforeOff=windowWidth_childviewbox1;
//		//		viewbox5_windowCenterBeforeOff=windowCenter_childViewbox1;
//
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), viewbox1_widthBeforeOff , "Verifying the window width on viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), viewbox2_widthBeforeOff, "Verifying the window width on viewbox2", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(3)), viewbox3_widthBeforeOff, "Verifying the window width on viewbox3", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(4)), viewbox4_widthBeforeOff, "Verifying the window width on viewbox4", "verified");
//		viewerpage.assertNotEquals(windowWidth_childviewbox1, viewbox5_widthBeforeOff, "Verifying the window width on viewbox5", "verified");
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), viewbox1_windowCenterBeforeOff, "Verifying the window center on viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), viewbox2_windowCenterBeforeOff ,"Verifying the window center on viewbox2", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(3)), viewbox3_windowCenterBeforeOff, "Verifying the window center on viewbox3", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(4)), viewbox4_windowCenterBeforeOff, "Verifying the window center on viewbox4", "verified");
//		viewerpage.assertNotEquals(windowCenter_childViewbox1, viewbox5_windowCenterBeforeOff, "Verifying the window center on viewbox5", "verified");
//
//		//performing the sync on parent 
//
//		viewerpage.performSyncONorOFF();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerpage.switchToWindow(childWindow);
//
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on child window" );
//
//				//performing the sync OFF on child window
//				viewerpage.performSyncONorOFF();
//
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//
//			}
//		}
//
//		windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		viewerpage.switchToWindow(parentWindow);		
//		viewerpage.openOrCloseChildWindows(1);
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), viewbox1_widthBeforeOff , "Verifying the window width on viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), viewbox2_widthBeforeOff, "Verifying the window width on viewbox2", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(3)), viewbox3_widthBeforeOff, "Verifying the window width on viewbox3", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(4)), viewbox4_widthBeforeOff, "Verifying the window width on viewbox4", "verified");
//		viewerpage.assertNotEquals(windowWidth_childviewbox1, viewbox5_widthBeforeOff, "Verifying the window width on viewbox5", "verified");
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), viewbox1_windowCenterBeforeOff, "Verifying the window center on viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), viewbox2_windowCenterBeforeOff ,"Verifying the window center on viewbox2", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(3)), viewbox3_windowCenterBeforeOff, "Verifying the window center on viewbox3", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(4)), viewbox4_windowCenterBeforeOff,"Verifying the window center on viewbox4", "verified");
//		viewerpage.assertNotEquals(windowCenter_childViewbox1, viewbox5_windowCenterBeforeOff, "Verifying the window center on viewbox5", "verified");
//
//	}
//
//	//IE11 has multimonitor issue
//	@Test(groups ={"Chrome", "Edge","firefox","multimonitor","dbConfig"})
//	public void test06_US244_TC749_verifyAbsoluteWWWLSyncOFFOnMultipleChildWindows() throws InterruptedException, IOException, SQLException{
//
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify WW/WL synchronization is off after clicking on space bar on multi-monitor and absolute is configured in db.");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);
//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE,"Verifying the WW/WL Level","ABSOLUTE");
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+subject60Patient+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
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
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to parent window", "");
//
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Loading the study in viewer by clicking browser back", "");
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//
//		//changing layout to 2X3 and getting the Delta w.r.t. viewbox5
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Changing the layout to "+NSConstants.TWO_BY_THREE_LAYOUT, "");
//		viewerpage.selectLayout(viewerpage.twoByThreeLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Retriving the Window width and window center value of viewbox 1-5", "");
//
//		int viewbox1_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int viewbox1_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//
//		int viewbox2_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//		int viewbox2_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//
//		int viewbox3_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(3));
//		int viewbox3_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(3));
//
//		int viewbox4_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(4));
//		int viewbox4_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(4));
//
//		int viewbox5_widthBeforeOff = Integer.parseInt(viewerpage.getValueOfWindowWidth(5));
//		int viewbox5_windowCenterBeforeOff=Integer.parseInt(viewerpage.getValueOfWindowCenter(5));
//
//		// performing the sync OFF on parent window
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Performing the sync off on Parent window", "");
//		viewerpage.performSyncONorOFF();
//
//		//changing layout to 2X1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Changing the layout to "+ViewerPageConstants.TWO_BY_TWO_LAYOUT, "");
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon);
//
//		viewerpage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerpage.switchToWindow(childWindow);
//
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Performing the right click and performing the window leveling from context Menu on child window", "" );
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Appling the window leveling on child monitor viewbox 1", "" );
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//
//
//			}
//		}
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Retriving the WW and WC of viewbox1 on child window", "" );
//
//		int windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		//		viewbox5_widthBeforeOff=windowWidth_childviewbox1;
//		//		viewbox5_windowCenterBeforeOff=windowCenter_childViewbox1;
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switiching to Parent and validating the values of WW and WC", "" );
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), viewbox1_widthBeforeOff , "Verifying the WW of viewbox1 ", "Value is not changed since sync is off");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), viewbox2_widthBeforeOff,  "Verifying the WW of viewbox2 ", "Value is not changed since sync is off");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(3)), viewbox3_widthBeforeOff,  "Verifying the WW of viewbox3 ", "Value is not changed since sync is off");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(4)), viewbox4_widthBeforeOff,  "Verifying the WW of viewbox4 ", "Value is not changed since sync is off");
//		viewerpage.assertNotEquals(windowWidth_childviewbox1, viewbox5_widthBeforeOff, "Validating the WW value has changed for viewbox1 in child window", "verified");
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), viewbox1_windowCenterBeforeOff,  "Verifying the WC of viewbox1 ", "Value is not changed since sync is off");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), viewbox2_windowCenterBeforeOff ,  "Verifying the WC of viewbox2 ", "Value is not changed since sync is off");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(3)), viewbox3_windowCenterBeforeOff,  "Verifying the WC of viewbox3 ", "Value is not changed since sync is off");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(4)), viewbox4_windowCenterBeforeOff, "Verifying the WC of viewbox4 ", "Value is not changed since sync is off");
//		viewerpage.assertNotEquals(windowCenter_childViewbox1, viewbox5_windowCenterBeforeOff, "Validating the WC value has changed for viewbox1 in child window", "verified");
//
//		//performing the sync on parent 
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Enabling the SYNC ON on Parent", "" );
//		viewerpage.performSyncONorOFF();
//
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//
//				viewerpage.switchToWindow(childWindow);
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on child window" );
//
//				//performing the sync OFF on child window
//				viewerpage.performSyncONorOFF();
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Appling the window leveling on child monitor viewbox 1", "" );
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//			}
//		}
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Retriving the WW and WC of viewbox1 on child window", "" );
//		windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switiching to Parent and validating the values of WW and WC", "" );
//		viewerpage.switchToWindow(parentWindow);
//		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
//		db.resetIISPostDBChanges();
//		viewerpage.openOrCloseChildWindows(1);
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), viewbox1_widthBeforeOff ,"verify the WW/WL(width) in viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), viewbox2_widthBeforeOff,"verify the WW/WL(width) in viewbox2", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(3)), viewbox3_widthBeforeOff,"verify the WW/WL(width) in viewbox3", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(4)), viewbox4_widthBeforeOff,"verify the WW/WL(width) in viewbox4", "verified");
//		viewerpage.assertNotEquals(windowWidth_childviewbox1, viewbox5_widthBeforeOff, "verify the WW/WL(width) in viewbox5", "verified");
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), viewbox1_windowCenterBeforeOff, "verify the WW/WL(center) in viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), viewbox2_windowCenterBeforeOff , "verify the WW/WL(center) in viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(3)), viewbox3_windowCenterBeforeOff, "verify the WW/WL(center) in viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(4)), viewbox4_windowCenterBeforeOff, "verify the WW/WL(center) in viewbox1", "verified");
//		viewerpage.assertNotEquals(windowCenter_childViewbox1, viewbox5_windowCenterBeforeOff, "verify the WW/WL(center) in viewbox1", "verified");
//	}
//
//	//TC784:Verify if parent and child window are in sync even when operated from child window.
//	@Test(groups ={"Chrome", "Edge", "firefox","multimonitor"})
//	public void test10_US62_DE213_TC784_verifyParentChildWindowAreInSyncWithWWL() throws InterruptedException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify if parent and child window are in sync even when operated from child window.");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();		
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//
//		String parentWindow = driver.getWindowHandle();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		String parentWindow1 = driver.getWindowHandle();
//		Set<String> childWinHandles = driver.getWindowHandles();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow1)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//
//		}
//
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 1x2");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//
//		viewerpage.waitForViewerpageToLoad();
//		String windowCenterValue = viewerpage.getValueOfWindowCenter(1);
//		String widthBeforeValue = viewerpage.getValueOfWindowWidth(1);
//
//		String widthBeforePerformWLOnChild = null;
//		String windowCenterBeforePerformWLOnChild = null;
//
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow1)) {
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Switching to child window");
//				viewerpage.switchToWindow(childWindow);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected after changing and applying the window leveling in first viewbox");
//
//				widthBeforePerformWLOnChild = viewerpage.getValueOfWindowWidth(1);
//				windowCenterBeforePerformWLOnChild = viewerpage.getValueOfWindowCenter(1);
//
//				// right clicking on viewbox 1
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on child window" );
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.dragAndReleaseOnViewerWithoutHop(viewerpage.viewboxImage1,0, 0, 100, -50);
//			}
//		}		
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying parent and child window are in sync when operated WWL from child window.");		
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforePerformWLOnChild, "Verify WW at viewbox1 on child ", "Window Width is for viewbox1 = "+widthBeforePerformWLOnChild+" - verified");		
//		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforePerformWLOnChild, "Verify  window center at viewbox1 on child ", "Window window center is for viewbox1= "+windowCenterBeforePerformWLOnChild+" - verified");
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeValue, "Verify the width of viewbox1", "Window Width is for viewbox1 = "+widthBeforeValue+" - verified");		
//		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterValue, "Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterValue+" - verified");
//
//	}
//
//	//Verify the cine play, scroll, window leveling retain its position on layout change when it is performed using multiple monitor setup
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor"})
//	//Not working on IE11, FireFox 
//	public void test22_DE90_TC467_DE450_TC2040_verifyIfWLRetainsOnLayoutChange() throws InterruptedException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the cine play, scroll, window leveling retain its position on layout change when it is performed using multiple monitor setup");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//
//				//taking values of initial conditions of scroll and window leveling
//
//				viewerpage.switchToWindow(parentWindow);
//				viewerpage.waitForViewerpageToLoad();
//
//				//performing window leveling by right clicking
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying WW/WL is performed","WW/WL is performed");
//				viewerpage.dragAndReleaseOnViewer(25, 50, 50, 100);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Capturing WC and WV values after window leveling is performed
//				int afterWWValue = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				int afterWCValue = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Verifying that values are retained after change in layout
//				viewerpage.assertEquals(afterWWValue,Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying WW functionality and is retained after change in layout", "WW is working fine and is retained after change in layout");
//				viewerpage.assertEquals(afterWCValue,Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying WC functionality and is retained after change in layout", "WC is working fine and is retained after change in layout");
//				viewerpage.waitForViewerpageToLoad();
//
//				//Changing control to Child Window to see if window leveling is performed
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//
//				int afterWWValueCW = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				int afterWCValueCW = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//				viewerpage.waitForViewerpageToLoad();
//
//				//Verifying that window leveling is performed in both the monitors
//				viewerpage.assertEquals(afterWWValueCW,afterWWValue, "Verifying WW functionality is reflected on Child Window", "WW is working fine and change is reflected on Child Window");
//				viewerpage.assertEquals(afterWCValueCW,afterWCValue, "Verifying WC functionality is reflected on Child Window", "WC is working fine and change is reflected on Child Window");
//
//				//Verifying that changes made on Child Window are reflected in Parent Window after change in layout 
//				//Performing window leveling on Child Window
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying WW/WL is performed","WW/WL is performed");
//				viewerpage.dragAndReleaseOnViewer(25, 50, 50, 100);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Capturing values after window leveling is performed in Child Monitor
//				int afterWWValueOnCW = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				int afterWCValueOnCW = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				//Changing the layout
//				viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//
//				//Verifying that values are retained after change in layout
//				viewerpage.assertEquals(afterWWValueOnCW,Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying WW functionality and is retained after change in layout on CW", "WW is working fine and is retained after change in layout on CW");
//				viewerpage.assertEquals(afterWCValueOnCW,Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying WC functionality and is retained after change in layout on CW", "WC is working fine and is retained after change in layout on CW");
//
//				//Changing control to Parent Window to capture window leveling values on it
//				viewerpage.switchToWindow(parentWindow);
//
//				//Verifying that the changes are reflected on Parent Window when window leveling was performed on Child Window
//				viewerpage.assertEquals(afterWWValueOnCW,Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying WW functionality on parent window wrt child window", "WW is working fine  on parent window wrt child window");
//				viewerpage.assertEquals(afterWCValueOnCW,Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying WC functionality on parent window wrt child window", "WC is working fine on parent window wrt child window");
//
//			}
//		}
//	}
//
//	//Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor"})
//	//Not working on IE11, FireFox 
//	public void test23_DE90_TC483_verifyIfWLResetsOnReloadOfNewStudy() throws InterruptedException   
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that scroll, window leveling and cine position is reset when user loads new study and then load the prior study");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		//performing child window
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {
//			if(!childWindow.equals(parentWindow)){
//
//				viewerpage.waitForViewerpageToLoad();
//
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//				viewerpage.waitForViewerpageToLoad();
//
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying that WW/WL is performed","WW/WL is performed");
//				viewerpage.dragAndReleaseOnViewer(25, 50, 50, 100);
//				viewerpage.waitForViewerpageToLoad();
//
//				int beforeWWValueCW = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				int beforeWCValueCW = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				//Reloading the same study
//				viewerpage.switchToWindow(parentWindow);
//				viewerpage.browserBackWebPage();
//				singlePatientStudyPage.waitForSingleStudyToLoad();
//				singlePatientStudyPage.clickOntheFirstStudy();
//				viewerpage.waitForViewerpageToLoad();
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "After loading the same study the changes are not retained for window leveling");
//
//				//Verifying that the changes are not reloaded once the study is reloaded
//				viewerpage.assertNotEquals(beforeWWValueCW,Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying WW functionality is not retained with reloading the same study", "WW is working fine and not retained");
//				viewerpage.assertNotEquals(beforeWCValueCW,Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying WC functionality is not retained with reloading the same study", "WC is working fine and not retainedand");
//
//			}}}
//
//	@Test(groups ={"Chrome", "Edge", "firefox", "multimonitor", "dbConfig"})
//	public void test26_DE316_TC1531_verifyAbsoluteWWWLSyncOFFOnMultipleChildWindows() throws InterruptedException, IOException, SQLException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify WW/WL synchronization is off after clicking on space bar on Child Window when absolute is configured in db.");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);
//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE,"Verifying the WW/WL Level","ABSOLUTE");
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+subject60Patient+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
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
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Loading the study in viewer by clicking browser back", "");
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//
//		//changing layout to 2X1
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Changing the layout to "+NSConstants.TWO_BY_THREE_LAYOUT, "");
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		//		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//		//		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL), "Verifying Window level icon is selected", "Window level icon is selected");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying WW/WL when sync is on");
//		int windowWidth_childviewbox1 = 0;
//		int windowCenter_childViewbox1 = 0;
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to child window", "");
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				int windowWidthCurrent_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				int windowCenterCurrent_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//
//				windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				viewerpage.assertNotEquals(windowWidth_childviewbox1, windowWidthCurrent_childviewbox1, "Verifying window width is changed on viewbox 1", "Verified window width is changed on viewbox 1");
//				viewerpage.assertNotEquals(windowCenter_childViewbox1, windowCenterCurrent_childViewbox1, "Verifying window center is changed on viewbox 1", "Verified window center is changed on viewbox 1");
//
//				viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is absolute in viewbox2", "verified");
//				viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is absolute in viewbox2", "verified");
//
//			}
//		}
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to parent window", "");
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying the WW/WL(Width) is absolute in viewbox1", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying the WW/WL(center) is absolute in viewbox1", "verified");
//
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is absolute in viewbox2", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is absolute in viewbox2", "verified");
//
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to child window", "");
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.performSyncONorOFF();
//
//				viewerpage.dragAndReleaseOnViewer(100, -50, -200, 50);
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify  WW/WL value of  image present on only that viewbox should get updated accordingly as sync is off.");
//
//				viewerpage.assertNotEquals(windowWidth_childviewbox1, Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying window width is changed in viewbox 1", "Verified window width is changed in viewbox 1");
//				viewerpage.assertNotEquals(windowCenter_childViewbox1, Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying window center is changed in viewbox 1", "Verified window center is changed in viewbox 1");
//
//				viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is not in sync in viewbox2", "verified");
//				viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is not in sync in viewbox2", "verified");
//
//			}
//		}
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to parent window", "");
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying the WW/WL(Width) is not in sync in viewbox1", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying the WW/WL(center) is not in sync in viewbox1", "verified");
//
//		viewerpage.assertEquals(windowWidth_childviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is not in sync in viewbox2", "verified");
//		viewerpage.assertEquals(windowCenter_childViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is not in sync in viewbox2", "verified");
//
//		viewerpage.openOrCloseChildWindows(1);
//
//	}
//
//	@Test(groups ={"Chrome", "Edge", "firefox", "multimonitor", "dbConfig"})
//	public void test27_DE316_TC1532_verifyRelativeWWWLSyncOFFOnMultipleChildWindows() throws InterruptedException, SQLException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(" Verify WW/WL synchronization is off after clicking on space bar on Child Window when relative is configured in db.");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+subject60Patient+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"Verifying the WW/WL Level","RELATIVE");
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
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
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to parent window", "");
//
//		viewerpage.switchToWindow(parentWindow);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Loading the study in viewer by clicking browser back", "");
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//
//		//changing layout to 2X1 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Changing the layout to "+NSConstants.TWO_BY_THREE_LAYOUT, "");
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		//		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//		//		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL), "Verifying Window level icon is selected", "Window level icon is selected");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying WW/WL when sync is on");
//
//		int windowWidth_childviewbox1 = 0;
//		int windowCenter_childViewbox1 = 0;
//
//		int windowWidth_childviewbox2 = 0;
//		int windowCenter_childViewbox2 = 0;
//
//		int windowWidthCurrent_parentviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		int windowCenterCurrent_parentViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		int windowWidthCurrent_parentviewbox2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//		int windowCenterCurrent_parentViewbox2=Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//		int windowWidthDeltaParentViewbox1 = windowWidthCurrent_parentviewbox1 - windowWidthCurrent_parentviewbox2;
//		int windowCenterDeltaParentViewbox1 = windowCenterCurrent_parentViewbox1 - windowCenterCurrent_parentViewbox2;
//
//		int windowWidthDeltaParentViewbox2 = windowWidthCurrent_parentviewbox2 - windowWidthCurrent_parentviewbox1;
//		int windowCenterDeltaParentViewbox2 = windowCenterCurrent_parentViewbox2 - windowCenterCurrent_parentViewbox1;
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to child window", "");
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//				int windowWidthCurrent_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				int windowCenterCurrent_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				int windowWidthCurrent_childviewbox2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//				int windowCenterCurrent_childViewbox2=Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//				int windowWidthDelta = windowWidthCurrent_childviewbox1 - windowWidthCurrent_childviewbox2;
//				int windowCenterDelta = windowCenterCurrent_childViewbox1 - windowCenterCurrent_childViewbox2;
//
//				viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
//
//				windowWidth_childviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//				windowCenter_childViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//				windowWidth_childviewbox2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//				windowCenter_childViewbox2 =Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//
//				viewerpage.assertNotEquals(windowWidth_childviewbox1, windowWidthCurrent_childviewbox1, "Verifying window width is changed on viewbox 1", "Verified window width is changed on viewbox 1");
//				viewerpage.assertNotEquals(windowCenter_childViewbox1, windowCenterCurrent_childViewbox1, "Verifying window center is changed on viewbox 1", "Verified window center is changed on viewbox 1");
//
//				viewerpage.assertEquals(windowWidth_childviewbox1-windowWidth_childviewbox2, windowWidthDelta, "Verifying the WW/WL(Width) is relative in viewbox2", "verified");
//				viewerpage.assertEquals(windowCenter_childViewbox1-windowCenter_childViewbox2, windowCenterDelta, "Verifying the WW/WL(center) is relative in viewbox2", "verified");
//
//			}
//		}
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to parent window", "");
//		viewerpage.switchToWindow(parentWindow);
//
//		windowWidthCurrent_parentviewbox1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
//		windowCenterCurrent_parentViewbox1=Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
//
//		windowWidthCurrent_parentviewbox2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
//		windowCenterCurrent_parentViewbox2=Integer.parseInt(viewerpage.getValueOfWindowCenter(2));
//
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))-Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), windowWidthDeltaParentViewbox1, "Verifying the WW/WL(Width) is relative in viewbox1", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))-Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), windowCenterDeltaParentViewbox1, "Verifying the WW/WL(center) is relative in viewbox1", "verified");
//
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2))-Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), windowWidthDeltaParentViewbox2, "Verifying the WW/WL(Width) is relative in viewbox2", "verified");
//		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2))-Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), windowCenterDeltaParentViewbox2, "Verifying the WW/WL(center) is relative in viewbox2", "verified");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify  WW/WL value of  image present on only that viewbox should get updated accordingly as sync is off.");
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to child window", "");
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.performSyncONorOFF();
//				viewerpage.dragAndReleaseOnViewer(100, -50, -200, 50);
//
//				viewerpage.assertNotEquals(windowWidth_childviewbox1, Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying window width is changed in viewbox 1", "Verified window width is changed in viewbox 1");
//				viewerpage.assertNotEquals(windowCenter_childViewbox1, Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying window center is changed in viewbox 1", "Verified window center is changed in viewbox 1");
//
//				viewerpage.assertEquals(windowWidth_childviewbox2 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is not in sync in viewbox2", "verified");
//				viewerpage.assertEquals(windowCenter_childViewbox2 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is not in sync in viewbox2", "verified");
//
//			}
//		}
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Switching to parent window", "");
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.assertEquals(windowWidthCurrent_parentviewbox1 , Integer.parseInt(viewerpage.getValueOfWindowWidth(1)), "Verifying the WW/WL(Width) is not in sync in viewbox1", "verified");
//		viewerpage.assertEquals(windowCenterCurrent_parentViewbox1 , Integer.parseInt(viewerpage.getValueOfWindowCenter(1)), "Verifying the WW/WL(center) is not in sync in viewbox1", "verified");
//
//
//	
//
//		viewerpage.assertEquals(windowWidthCurrent_parentviewbox2 , Integer.parseInt(viewerpage.getValueOfWindowWidth(2)), "Verifying the WW/WL(Width) is not in sync in viewbox2", "verified");
//		viewerpage.assertEquals(windowCenterCurrent_parentViewbox2 , Integer.parseInt(viewerpage.getValueOfWindowCenter(2)), "Verifying the WW/WL(center) is not in sync in viewbox2", "verified");
//
//
//		viewerpage.openOrCloseChildWindows(1);
//
//	}
//	

/*
 * //TC1055: Active overlay preset selection and manual input WW/WL and synchronization - Context Menu Coverage
	@Test(groups ={"firefox","Chrome","US285","BVT"})
	public void test24_US285_TC1055_verifyActiveOverlayPresetSelectionManualInputCoverage() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay preset selection and manual input WW/WL and synchronization - Context Menu Coverage");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Get Patient Name
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		textOverlay=new ViewerTextOverlays(driver);
		presetMenu=new ViewBoxToolPanel(driver);

		textOverlay.selectTextOverlays("default");
		String beforeWWValueFirstViewbox = viewerpage.getValueOfWindowWidth(1);
		String beforeWCValueFirstViewbox = viewerpage.getValueOfWindowCenter(1);
		String beforeWWValueSecViewbox = viewerpage.getValueOfWindowWidth(2);
		String beforeWCValueSecViewbox = viewerpage.getValueOfWindowCenter(2);
		String beforeWWValueThirdViewbox = viewerpage.getValueOfWindowWidth(3);
		String beforeWCValueThirdViewbox = viewerpage.getValueOfWindowCenter(3);
		String beforeWWValueFourthViewbox = viewerpage.getValueOfWindowWidth(4);
		String beforeWCValueFourthViewbox = viewerpage.getValueOfWindowCenter(4);


		//Step 1 - Click on the Window Width textual Overlay marker to open the W/L Context Menu. Select each of the options in the context menu except the INVERT entry. Then Select the RESET option.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_Checkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		List<String> wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(1,viewerpage.getWindowWidthLabelOverlay(1));
		WebElement windowWidthCenterLabelOverlay = viewerpage.getWindowWidthLabelOverlay(1);

		//Window Width and Window Center values are set to the values specified in each option in the context menu in each related series.
		//selectAndVerifyAlloptionOfPreset(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 1, false);
		selectAndVerifyAlloptionOfPresetWithSyncOn(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 1);

		//Selecting Reset and verify all values of viewboxes
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowWidthLabelOverlay(1),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 1);

		//====================================
		//Step 2
		//Click on the Window Width textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_2_first_invert_for_WW");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_2_second_invert_for_WW ");

		//======================================
		//Step 3
		//selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_Checkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(1,viewerpage.getWindowCenterLabelOverlay(1));
		windowWidthCenterLabelOverlay = viewerpage.getWindowCenterLabelOverlay(1);

		//selecting and verifying all options from preset menu
		selectAndVerifyAlloptionOfPresetWithSyncOn(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 1);
		//Selecting Reset and verify all values of viewboxes
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowCenterLabelOverlay(1),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 1);

		//=========================================
		//Step 4
		//Click on the Window Center textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_4_first_invert_for_WC");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_4_second_invert_for_WW");

		//=======================================
		//Step 5
		viewerpage.performSyncONorOFF();

		//=======================================
		//Step 5 >> Repeat steps 1- 4 (Press the Space bar, then repeat steps 1-4.)

		textOverlay.selectTextOverlays("default");
		beforeWWValueFirstViewbox = viewerpage.getValueOfWindowWidth(1);
		beforeWCValueFirstViewbox = viewerpage.getValueOfWindowCenter(1);
		beforeWWValueSecViewbox = viewerpage.getValueOfWindowWidth(2);
		beforeWCValueSecViewbox = viewerpage.getValueOfWindowCenter(2);
		beforeWWValueThirdViewbox = viewerpage.getValueOfWindowWidth(3);
		beforeWCValueThirdViewbox = viewerpage.getValueOfWindowCenter(3);
		beforeWWValueFourthViewbox = viewerpage.getValueOfWindowWidth(4);
		beforeWCValueFourthViewbox = viewerpage.getValueOfWindowCenter(4);

		//Step 5 >>Step 5.1 - Click on the Window Width textual Overlay marker to open the W/L Context Menu. Select each of the options in the context menu except the INVERT entry. Then Select the RESET option.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_Checkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(1,viewerpage.getWindowWidthLabelOverlay(1));
		windowWidthCenterLabelOverlay = viewerpage.getWindowWidthLabelOverlay(1);

		//Window Width and Window Center values are set to the values specified in each option in the context menu in each related series.
		selectAndVerifyAlloptionOfPresetWithSyncOff(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 1);

		//Selecting Reset
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowWidthLabelOverlay(1),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox,1);

		//====================================
		//Step 5 >>Step 5.2
		//Click on the Window Width textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_5.2_first_invert_for_WW");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_5.2_second_invert_for_WW");

		//======================================
		//Step 5 >>Step 5.3
		//Step 1 - selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_Checkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(1,viewerpage.getWindowCenterLabelOverlay(1));
		viewerpage.inputWCNumber(60, 1);
		windowWidthCenterLabelOverlay = viewerpage.getWindowCenterLabelOverlay(1);

		//selecting and verifying all options from preset menu
		selectAndVerifyAlloptionOfPresetWithSyncOff(wlPresetMenuOptions, windowWidthCenterLabelOverlay,1);

		//Selecting Reset and verify all values of viewboxes
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowCenterLabelOverlay(1),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 1);

		//=========================================
		//Step 5 >>Step 5.4
		//Click on the Window Center textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_5.4_first_invert_for_WC");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(1), 1, "US285_TC1055_Checkpoint_Step_5.4_second_invert_for_WC");

		//==============================================
		//Step 6: Press the Space bar again, then repeat steps 1-5 with a different series.
		viewerpage.performSyncONorOFF();
		//Repeat steps 1-5
		presetMenu=new ViewBoxToolPanel(driver);
		presetMenu.selectPresetValue(viewerpage.getWindowWidthLabelOverlay(1), ViewerPageConstants.RESET,1);
		presetMenu.selectPresetValue(viewerpage.getWindowCenterLabelOverlay(1), ViewerPageConstants.RESET,1);

		textOverlay.selectTextOverlays("default");
		beforeWWValueFirstViewbox = viewerpage.getValueOfWindowWidth(1);
		beforeWCValueFirstViewbox = viewerpage.getValueOfWindowCenter(1);
		beforeWWValueSecViewbox = viewerpage.getValueOfWindowWidth(2);
		beforeWCValueSecViewbox = viewerpage.getValueOfWindowCenter(2);
		beforeWWValueThirdViewbox = viewerpage.getValueOfWindowWidth(3);
		beforeWCValueThirdViewbox = viewerpage.getValueOfWindowCenter(3);
		beforeWWValueFourthViewbox = viewerpage.getValueOfWindowWidth(4);
		beforeWCValueFourthViewbox = viewerpage.getValueOfWindowCenter(4);

		//Step 1 - Click on the Window Width textual Overlay marker to open the W/L Context Menu. Select each of the options in the context menu except the INVERT entry. Then Select the RESET option.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_Checkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(2,viewerpage.getWindowWidthLabelOverlay(2));
		windowWidthCenterLabelOverlay = viewerpage.getWindowWidthLabelOverlay(2);

		//Window Width and Window Center values are set to the values specified in each option in the context menu in each related series.
		selectAndVerifyAlloptionOfPresetWithSyncOn(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 2);

		//Selecting Reset
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowWidthLabelOverlay(2),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 2);

		//====================================

		//Click on the Window Width textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(2), 2, "US285_TC1055_Checkpoint_Step_6_first_invert_for_WW");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(2), 2, "US285_TC1055_Checkpoint_Step 6_second_invert_for_WW");

		//======================================

		//Step 1 - selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_Checkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(2,viewerpage.getWindowCenterLabelOverlay(2));
		//viewerpage.inputWCNumber(35, 2);
		windowWidthCenterLabelOverlay = viewerpage.getWindowCenterLabelOverlay(2);

		//selecting and verifying all options from preset menu
		selectAndVerifyAlloptionOfPresetWithSyncOn(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 2); 

		//Selecting Reset and verify all values of viewboxes
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowCenterLabelOverlay(2),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 2);

		//=========================================

		//Click on the Window Center textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(2), 2, "US285_TC1055_Checkpoint_Step_6_first_invert_for_WC");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(2), 2, "US285_TC1055_Checkpoint_Step_6_second_invert_for_WC");


		//=======================================

		viewerpage.performSyncONorOFF();

		//=======================================
		//Repeat steps 1- 4

		textOverlay.selectTextOverlays("default");
		beforeWWValueFirstViewbox = viewerpage.getValueOfWindowWidth(1);
		beforeWCValueFirstViewbox = viewerpage.getValueOfWindowCenter(1);
		beforeWWValueSecViewbox = viewerpage.getValueOfWindowWidth(2);
		beforeWCValueSecViewbox = viewerpage.getValueOfWindowCenter(2);
		beforeWWValueThirdViewbox = viewerpage.getValueOfWindowWidth(3);
		beforeWCValueThirdViewbox = viewerpage.getValueOfWindowCenter(3);
		beforeWWValueFourthViewbox = viewerpage.getValueOfWindowWidth(4);
		beforeWCValueFourthViewbox = viewerpage.getValueOfWindowCenter(4);

		//Step 1 - Click on the Window Width textual Overlay marker to open the W/L Context Menu. Select each of the options in the context menu except the INVERT entry. Then Select the RESET option.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "US285_TC1055_ChecCkpoint", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(2,viewerpage.getWindowWidthLabelOverlay(2));
		windowWidthCenterLabelOverlay = viewerpage.getWindowWidthLabelOverlay(2);

		//Window Width and Window Center values are set to the values specified in each option in the context menu in each related series.
		selectAndVerifyAlloptionOfPresetWithSyncOff(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 2);

		//Selecting Reset
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowWidthLabelOverlay(2),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 2);

		//====================================

		//Click on the Window Width textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(2), 2, "US285_TC1055_Checkpoint_Step_6.1_first_invert_for_WW");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowWidthLabelOverlay(2), 2, "US285_TC1055_Checkpoint_Step_6.1_first_invert_for_WW");

		//======================================

		//Step 1 - selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify the WW and WC values after selecting each value in PRESET context menu" );

		wlPresetMenuOptions = presetMenu.getWWWLOverlayOptions(2,viewerpage.getWindowCenterLabelOverlay(2));
		windowWidthCenterLabelOverlay = viewerpage.getWindowCenterLabelOverlay(2);

		//selecting and verifying all options from preset menu
		selectAndVerifyAlloptionOfPresetWithSyncOff(wlPresetMenuOptions, windowWidthCenterLabelOverlay, 2);

		//Selecting Reset and verify all values of viewboxes
		resetAndVerifyWCvalueOFAllViewBoxes(viewerpage.getWindowCenterLabelOverlay(2),beforeWWValueFirstViewbox, beforeWCValueFirstViewbox, beforeWWValueSecViewbox, beforeWCValueSecViewbox, beforeWWValueThirdViewbox, beforeWCValueThirdViewbox, beforeWWValueFourthViewbox,  beforeWCValueFourthViewbox, 2);

		//=========================================

		//Click on the Window Center textual Overlay marker to open the W/L Context Menu. 
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(2), 2,"US285_TC1055_Checkpoint_Step_6.1_first_invert_for_WC");

		//Select the INVERT entry. Observe the result, then select the INVERT option again.
		performAndVerifyInvertWindowWidthAndCenter(viewerpage.getWindowCenterLabelOverlay(2), 2,"US285_TC1055_Checkpoint_Step_6.1_first_invert_for_WC");

	}
	
	//Active overlay preset selection and manual input WW/WL and synchronization - Context Menu Coverage
	@Test(groups ={"firefox","Chrome","US285","BVT"})
	public void test25_US285_TC1054_verifyActiveOverlayPresetSelectionManualInputCoverage() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay preset selection and manual input WW/WL and synchronization - Manual Input Coverage");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Get Patient Name

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		//changing ww value by entering new value 3 
		String wwBeforeForViewbox1 = viewerpage.getWindowWidthValueOverlayText(1);
		String wcBeforeForViewbox1 = viewerpage.getWindowCenterValueOverlayText(1);
		viewerpage.inputWWNumber(3, 1);

		//verifying wc value is not changing on changing ww value
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), wcBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WC value for viewbox1", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), wcBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WC value for viewbox2", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(3), wcBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WC value for viewbox3", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4), wcBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WC value for viewbox3", "Verified WC value not change on changing WW value");

		//verifying ww value should change on entering new value to viewbox 1 as all are in sync
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(1), wwBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WW value for viewbox1", "Verified WW value changed");
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(2), wwBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WW value for viewbox2", "Verified WW value changed");
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(3), wwBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WW value for viewbox3", "Verified WW value changed");
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(4), wwBeforeForViewbox1, "Checkpoint TC1[1]:Verifying WW value for viewbox4", "Verified WW value changed");

		//changing wc value by entering new value 6 
		wwBeforeForViewbox1 = viewerpage.getWindowWidthValueOverlayText(1);
		wcBeforeForViewbox1 = viewerpage.getWindowCenterValueOverlayText(1);

		viewerpage.inputWCNumber(20, 1);

		//verifying wc value should change on entering new value to viewbox 1 as all are in sync
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), wcBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WC value for viewbox1", "Verified WC value  change on changing WC value");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(2), wcBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WC value for viewbox2", "Verified WC value  change on changing WC value");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(3), wcBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WC value for viewbox3", "Verified WC value  change on changing WC value");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(4), wcBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WC value for viewbox3", "Verified WC value  change on changing WC value");

		//verifying ww value is not changing on changing wc value
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(1), wwBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WW value for viewbox1", "Verified WW value not changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(2), wwBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WW value for viewbox2", "Verified WW value not changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(3), wwBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WW value for viewbox3", "Verified WW value not changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(4), wwBeforeForViewbox1, "Checkpoint TC1[2]:Verifying WW value for viewbox4", "Verified WW value not changed");


		viewerpage.performSyncONorOFF();
		wwBeforeForViewbox1 = viewerpage.getWindowWidthValueOverlayText(1);
		wcBeforeForViewbox1 = viewerpage.getWindowCenterValueOverlayText(1);
		viewerpage.inputWWNumber(5, 1);

		//verifying wc value is not changing on changing ww value
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(1), wcBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WC value for viewbox1", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), wcBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WC value for viewbox2", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(3), wcBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WC value for viewbox3", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4), wcBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WC value for viewbox3", "Verified WC value not change on changing WW value");

		//changing ww value by entering new value 5 in viewbox1 will not change rest viewbox ww value as sync is off 
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(1), wwBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WW value for viewbox1", "Verified WW value changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(2), wwBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WW value for viewbox2", "Verified WW value changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(3), wwBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WW value for viewbox3", "Verified WW value changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(4), wwBeforeForViewbox1, "Checkpoint TC1[3]:Verifying WW value for viewbox4", "Verified WW value changed");

		wwBeforeForViewbox1 = viewerpage.getWindowWidthValueOverlayText(1);
		String wwBeforeForViewbox2 = viewerpage.getWindowWidthValueOverlayText(2);
		wcBeforeForViewbox1 = viewerpage.getWindowCenterValueOverlayText(1);
		viewerpage.inputWCNumber(10, 1);

		//verifying wc value is changing only for viewbox 1 
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(1), wcBeforeForViewbox1, "Checkpoint TC1[4]:Verifying WC value for viewbox1", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(2), wcBeforeForViewbox1, "Checkpoint TC1[4]:Verifying WC value for viewbox2", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(3), wcBeforeForViewbox1, "Checkpoint TC1[4]:Verifying WC value for viewbox3", "Verified WC value not change on changing WW value");
		viewerpage.assertEquals(viewerpage.getWindowCenterValueOverlayText(4), wcBeforeForViewbox1, "Checkpoint TC1[4]:Verifying WC value for viewbox3", "Verified WC value not change on changing WW value");

		//changing ww value not changing for all viewboxes
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(1), wwBeforeForViewbox1, "Checkpoint TC1[4]:Verifying WW value for viewbox1", "Verified WW value changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(2), wwBeforeForViewbox2, "Checkpoint TC1[4]:Verifying WW value for viewbox2", "Verified WW value changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(3), wwBeforeForViewbox2, "Checkpoint TC1[4]:Verifying WW value for viewbox3", "Verified WW value changed");
		viewerpage.assertEquals(viewerpage.getWindowWidthValueOverlayText(4), wwBeforeForViewbox2, "Checkpoint TC1[4]:Verifying WW value for viewbox4", "Verified WW value changed");

	}
	
	@Test(groups ={"Chrome", "Edge","firefox", "IE11","DE507"})
	public void test33_DE507_TC2091_verifyingBackgroundOfTextOverlaysIsHighlighted() throws InterruptedException, AWTException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DE507_TC2091 : Verify that the Text overlay color is changing to black on mouse hover on Parent window.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(ah4patientName);

		layout=new ViewerLayout(driver);
		patientPage.clickOnStudy(1);		

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.closingNotificationAndWaterMark();
		
		contentSelector = new ContentSelector(driver);
		//mouseHover(viewerpage.zoomIcon);
		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(1)), "Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint1 : Verifying that the background of text Overlays is Highlighted.","TC2091_" + "_" + "Checkpoint1");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(1)), "Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint2 : Verifying that the background of text Overlays is Highlighted.","TC2091_" + "_" + "Checkpoint2");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint3 : Verifying that the background of text Overlays is Highlighted.","TC2091_" + "_" + "Checkpoint3");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint4 : Verifying that the background of text Overlays is Highlighted. ","TC2091_" + "_" + "Checkpoint4");

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint5 : Verifying that the background of text Overlays is Highlighted. ","TC2091_" + "_" + "Checkpoint5");

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint6 : Verifying that the background of text Overlays is Highlighted. ","TC2091_" + "_" + "Checkpoint6");

		viewerpage.mouseHover(viewerpage.getSliceInfo(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint7 : Verifying that the background of text Overlays is Highlighted.","TC2091_" + "_" + "Checkpoint7");

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(1)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint8 : Verifying that the background of text Overlays is Highlighted. ","TC2091_" + "_" + "Checkpoint8");

		layout.selectLayout(layout.twoByOneLayoutIcon);

		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getImageCurrentScrollPositionOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		layout.selectLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);

		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(1)), "Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(1)), "Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;

		viewerpage.mouseHover(viewerpage.getImageCurrentScrollPositionOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");;

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(1)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getImageCurrentScrollPositionOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		layout.selectLayout(layout.oneByOneLayoutIcon);

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.waitForFullOverlayDisplay(1);

		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(1)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(1)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getImageCurrentScrollPositionOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(1)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(1));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(1)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY,PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath4);

		contentSelector.selectSeriesFromSeriesTab(6, firstSeriesDescription);

		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(6)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(6)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(6)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(6)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(6)) ,"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(6)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getImageCurrentScrollPositionOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(6)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(6));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(6)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		layout.selectLayout(layout.twoByTwoLayoutIcon);

		String secondSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY,PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath4);

		contentSelector.selectSeriesFromSeriesTab(2, secondSeriesDescription);

		viewerpage.mouseHover(viewerpage.getWindowWidthValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowWidthLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowWidthLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getWindowCenterValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getWindowCenterValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomLabelOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomLabelOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getZoomValueOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getZoomValueOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getImageCurrentScrollPositionOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getImageCurrentScrollPositionOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

		viewerpage.mouseHover(viewerpage.getSeriesDescriptionOverlay(2));
		viewerpage.assertTrue(textOverlay.isElementHighlighted(viewerpage.getSeriesDescriptionOverlay(2)),"Verifying that the background of text Overlays is Highlighted.","Verified that background of Text Overlays is Highlighted");

	}
 * 
 * 
 * 
 */
//}
