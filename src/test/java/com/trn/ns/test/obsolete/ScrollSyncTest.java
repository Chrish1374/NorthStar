//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.sql.SQLException;
//import java.util.Set;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
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
//public class ScrollSyncTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	//Loading the patient on viewer 
//	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
//
//	String filePath2 = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
//	String PatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//		
//	String filePath3 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
//	
//	private String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
//	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//	
//	private String filePath5 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
//	private String anonymousPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath5);
//	private String secondResultDescription_anonymous = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath5);
//
//	private String firstSeriesDescriptionMR_LSP = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
//	private String thirdSeriesDescriptionMR_LSP = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath);
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientPage = new PatientListPage(driver);
//	}
//
//	
//
//	
//	
//
//	//Obselete
//	@Test(groups ={"IE11","Chrome","firefox","Edge"})
//	public void test16_US289_TC2377_verifyScrollForNonDICOM() throws InterruptedException, AWTException, SQLException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Dicom and Non-Dicom are not in sync if FrameReferenceUID is made same.");
//		
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneagePatientName+" in viewer" );
//		patientPage.clickOnPatientRow(patientName1);
//
//		
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(2);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//		//Selecting minimum annotation level
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the scroll from radial Menu" );
////		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),0, 0, 200, -50);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should scroll synchronously.");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should scroll synchronously.","test16_checkpoint1");
//		
////		DatabaseMethods db = new DatabaseMethods(driver);
////		HashMap<String, String> uid = db.getFrameReferenceUIDOfPatient(boneagePatientName);
////		List<String> keys =  new ArrayList<>(uid.keySet());
////		for(int i=0;i<keys.size();i++)
////			db.updateFrameReferenceUIDOfPatient(Integer.parseInt(keys.get(i)),"1");
//
//		viewerpage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad(2);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
//
//		//Selecting minimum annotation level
////		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);
//
//		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the scroll from radial Menu" );
//		//viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.viewboxImg1);
//
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),0, 0, 200, -50);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should scroll synchronously.");
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should scroll synchronously.","test16_checkpoint1");
//			
//	}
//
//
//	
//	// Multimonitor 
//
//	//Not working on IE and edge as Child window is blank for IE and unable to get focus of child window on edge"
//	//@Test(groups = { "firefox", "Chrome","multimonitor"})
//	public void test05_US61_TC796_verifyScrollSyncOnChildWindow()throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify by default scroll synchronization is on on multi-monitor");
//		patientPage = new PatientListPage(driver);
//		
//
//		// Get Patient Name
//		patientPage.clickOnPatientRow(patientName);
//		studyPage.clickOntheFirstStudy();
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/5]","Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//		//get current window ID
//		String parentWindow = viewerpage.getCurrentWindowID();
//		// selecting 2 window
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles)
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();	
//			}	
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		// variable to store the scroll level of Child and Parent Window
//		int ChildIntialScrollLevel, childForwardScroll = 0;
//		// changing layout to 2X1
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//		//			int parentIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				ChildIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				// Perform Mouse down operation to increase scroll Level
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImage1, 0, 0, 0, 30);
//				childForwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/5]","Verify scroll level in Child Window increases on Mouse down");
//				viewerpage.assertTrue(childForwardScroll > ChildIntialScrollLevel,"Verifying scroll level ","The scroll level after Mouse down increases from "+ ChildIntialScrollLevel + " to "+ childForwardScroll);
//				viewerpage.assertEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//				viewerpage.assertNotEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/5]","Verify Scroll level sync in Parent Window when scroll level is increased from child window");
//		viewerpage.assertEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is in sync for viewbox 1 on parent window","Verifed scroll is in sync for viewbox 1 on parent window");
//		viewerpage.assertEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is in sync for viewbox 2 on parent window","Verifed scroll is in sync for viewbox 2 on parent window");
//
//		// Perform Mouse Up Operation to decrease Scroll level on Child Window
//		int childBackwardScroll = 0;
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.dragAndReleaseOnViewer(0, 0, 0, -20);
//				childBackwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/5]","Verify Scroll level in Child Window decreases on Mouse up");
//				viewerpage.assertTrue(childBackwardScroll < childForwardScroll,"Verifying scroll level","The scroll level after Mouse up decreases from"+ childForwardScroll + " to "+ childBackwardScroll);
//				viewerpage.assertEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//				viewerpage.assertNotEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/5]","Verify Scroll level sync in Parent Window when scroll level is decreased from child window");
//		viewerpage.assertEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is in sync for viewbox 1 on parent window","Verifed scroll is in sync for viewbox 1 on parent window");
//		viewerpage.assertEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is in sync for viewbox 2 on parent window","Verifed scroll is in sync for viewbox 2 on parent window");
//	}
//
//	//Not working on IE and edge as Child window is blank for IE and unable to get focus of child window on edge"
//	//@Test(groups = { "firefox", "Chrome","multimonitor"})
//	public void test06_US61_TC797_verifyScrollSyncOffPressingSpacebarOnChildWindow()throws InterruptedException, AWTException {
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify scroll synchronization is off after clicking on space bar on multi-monitor");
//			patientPage = new PatientListPage(driver);
//			
//
//			// Get Patient Name
//			patientPage.clickOnPatientRow(patientName);
//			studyPage.clickOntheFirstStudy();
//			viewerpage = new ViewerPage(driver);
//			viewerpage.waitForViewerpageToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/5]","Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//			//get current window ID
//			String parentWindow = viewerpage.getCurrentWindowID();
//			// selecting 2 window
//			viewerpage.openOrCloseChildWindows(2);
//			viewerpage.switchToWindow(parentWindow);
//			Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//			for (String childWindow : childWinHandles)
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();	
//				}	
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			// variable to store the scroll level of Child and Parent Window
//			int ChildIntialScrollLevel, childForwardScroll = 0;
//			// changing layout to 2X1
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//			viewerpage.waitForAllImagesToLoad();
//			//				int parentIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//					//performing sync offs
//					viewerpage.performSyncONorOFF();
//					// Perform Mouse down operation to increase scroll Level
//					viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImage1, 0, 0, 0, 30);
//					childForwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/5]","Verify scroll level in Child Window increases on Mouse down");
//					viewerpage.assertTrue(childForwardScroll > ChildIntialScrollLevel,"Verifying scroll level ","The scroll level after Mouse down increases from "+ ChildIntialScrollLevel + " to "+ childForwardScroll);
//					viewerpage.assertEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//					viewerpage.assertNotEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/5]","Verify Scroll level sync in Parent Window when scroll level is increased from child window");
//			viewerpage.assertNotEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is not in sync for viewbox 1 on parent window as sync is off","Verifed scroll is not in sync for viewbox 1 on parent window as sync is off");
//			viewerpage.assertNotEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is not in sync for viewbox 2 on parent window as sync is off","Verifed scroll is not in sync for viewbox 2 on parent window as sync is off");
//
//			// Perform Mouse Up Operation to decrease Scroll level on Child Window
//			int childBackwardScroll = 0;
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					viewerpage.dragAndReleaseOnViewer(0, 0, 0, -20);
//					childBackwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/5]","Verify Scroll level in Child Window decreases on Mouse up");
//					viewerpage.assertTrue(childBackwardScroll < childForwardScroll,"Verifying scroll level","The scroll level after Mouse up decreases from"+ childForwardScroll + " to "+ childBackwardScroll);
//					viewerpage.assertEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//					viewerpage.assertNotEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/5]","Verify scroll level on both Window remain same");
//			viewerpage.assertNotEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is not in sync for viewbox 1 on parent window as sync is off","Verifed scroll is not in sync for viewbox 1 on parent window as sync is off");
//			viewerpage.assertNotEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is not in sync for viewbox 2 on parent window as sync is off","Verifed scroll is not in sync for viewbox 2 on parent window as sync is off");
//		}
//	}
//
//	//@Test(groups = { "firefox", "Chrome", "multimonitor"})
//	public void test07_DE316_TC1533_verifyScrollSyncOffPressingSpacebarOnChildWindow()throws InterruptedException, AWTException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify scroll synchronization is off after clicking on space bar on Child Window");
//		patientPage = new PatientListPage(driver);
//		
//
//		patientPage.clickOnPatientRow(patientName);
//		studyPage.clickOntheFirstStudy();
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/9]","Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//		//get current window ID
//		String parentWindow = viewerpage.getCurrentWindowID();
//		// selecting 2 window
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles)
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();	
//			}	
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		// variable to store the scroll level of Child and Parent Window
//		int ChildIntialScrollLevel, childForwardScroll = 0;
//		int childBackwardScroll = 0;
//		// changing layout to 2X1
//		viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				ChildIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				// Perform Mouse down operation to increase scroll Level
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImage1, 0, 0, 0, 30);
//				childForwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/9]","Verify scroll level in Child Window increases on Mouse down");
//				viewerpage.assertTrue(childForwardScroll > ChildIntialScrollLevel,"Verifying scroll level ","The scroll level after Mouse down increases from "+ ChildIntialScrollLevel + " to "+ childForwardScroll);
//				viewerpage.assertEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//				viewerpage.assertNotEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/9]","Verify Scroll level sync in Parent Window when scroll level is increased from child window");
//		viewerpage.assertEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is  in sync for viewbox 1 on parent window ","Verifed scroll is in sync for viewbox 1 on parent window ");
//		viewerpage.assertEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is  in sync for viewbox 2 on parent window ","Verifed scroll is in sync for viewbox 2 on parent window ");
//
//		// Perform Mouse Up Operation to decrease Scroll level on Child Window
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.dragAndReleaseOnViewer(0, 0, 0, -20);
//				childBackwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/9]","Verify Scroll level in Child Window decreases on Mouse up");
//				viewerpage.assertTrue(childBackwardScroll < childForwardScroll,"Verifying scroll level","The scroll level after Mouse up decreases from"+ childForwardScroll + " to "+ childBackwardScroll);
//				viewerpage.assertEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//				viewerpage.assertNotEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/9]","Verify scroll level on both Window remain same");
//		viewerpage.assertEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is in sync for viewbox 1 on parent window","Verifed scroll is in sync for viewbox 1 on parent window");
//		viewerpage.assertEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is in sync for viewbox 2 on parent window ","Verifed scroll is in sync for viewbox 2 on parent window");
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				ChildIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				//performing sync off
//				viewerpage.performSyncONorOFF();
//				// Perform Mouse down operation to increase scroll Level
//				viewerpage.dragAndReleaseOnViewer(viewerpage.viewboxImage1, 0, 0, 0, 30);
//				childForwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[6/9]","Verify scroll level should not be synchronously increases in Child Window on Mouse down as sync is off ");
//				viewerpage.assertTrue(childForwardScroll > ChildIntialScrollLevel,"Verifying scroll level ","The scroll level after Mouse down increases from "+ ChildIntialScrollLevel + " to "+ childForwardScroll);
//				viewerpage.assertEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//				viewerpage.assertNotEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[7/9]","Verify Scroll level sync in Parent Window when scroll level is increased from child window");
//		viewerpage.assertNotEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is not in sync for viewbox 1 on parent window as sync is off","Verifed scroll is not in sync for viewbox 1 on parent window as sync is off");
//		viewerpage.assertNotEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is not in sync for viewbox 2 on parent window as sync is off","Verifed scroll is not in sync for viewbox 2 on parent window as sync is off");
//
//		// Perform Mouse Up Operation to decrease Scroll level on Child Window
//
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.dragAndReleaseOnViewer(0, 0, 0, -20);
//				childBackwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[8/9]","Verify Scroll level in Child Window decreases on Mouse up");
//				viewerpage.assertTrue(childBackwardScroll < childForwardScroll,"Verifying scroll level","The scroll level after Mouse up decreases from"+ childForwardScroll + " to "+ childBackwardScroll);
//				viewerpage.assertEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//				viewerpage.assertNotEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2 on child window", "Verified scroll is not in sync for viewbox 2 on child window");
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[9/9]","Verify scroll level on both Window remain same");
//		viewerpage.assertNotEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is not in sync for viewbox 1 on parent window as sync is off","Verifed scroll is not in sync for viewbox 1 on parent window as sync is off");
//		viewerpage.assertNotEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(2),"Verifying scroll is not in sync for viewbox 2 on parent window as sync is off","Verifed scroll is not in sync for viewbox 2 on parent window as sync is off");
//	}
//
//	//Verify the image rendering while scrolling for multimonitor
//	//Not working on IE and edge as Child window is blank for IE and unable to get focus of child window on edge"
//	//@Test(groups = { "firefox", "Chrome","multimonitor"})
//	public void test11_US578_US533_TC1741_TC1630_verifyScrollSyncOnChildWindow()throws InterruptedException, AWTException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify by default scroll synchronization is on on multi-monitor.</br>Verify Scroll sync in multi-Mointor view");
//		patientPage = new PatientListPage(driver);
//		
//
//		// Get Patient Name
//		patientPage.clickOnPatientRow(patientName);
//		studyPage.clickOntheFirstStudy();
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/4]","Verify user is navigating to viewer page.");
//		viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//		//get current window ID
//		String parentWindow = viewerpage.getCurrentWindowID();
//		// selecting 2 child window
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles)
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();	
//			}	
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		// variable to store the scroll level of Child and Parent Window
//		int ChildIntialScrollLevel, childForwardScroll = 0;
//		// changing layout to 2X1
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//		//			int parentIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				ChildIntialScrollLevel = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				// Perform Mouse down operation to increase scroll Level
//				viewerpage.scrollDownToSliceUsingKeyboard(1,1);
//				childForwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/5]","Verify scroll level in Child Window increases on arrow keypress down");
//				viewerpage.assertTrue(childForwardScroll > ChildIntialScrollLevel,"Verifying scroll level ","The scroll level after arroe keypress down increases from "+ ChildIntialScrollLevel + " to "+ childForwardScroll);
//				viewerpage.assertEquals(childForwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//
//
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/5]","Verify Scroll level sync in Parent Window when scroll level is increased from child window");
//		viewerpage.assertEquals(childForwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is in sync for viewbox 1 on parent window","Verifed scroll is in sync for viewbox 1 on parent window");
//
//
//		// Perform Mouse Up Operation to decrease Scroll level on Child Window
//		int childBackwardScroll = 0;
//		for (String childWindow : childWinHandles) {
//			if (!childWindow.equals(parentWindow)) {
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.scrollUpToSliceUsingKeyboard(1, 1);
//				childBackwardScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
//				ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/5]","Verify Scroll level in Child Window decreases on arrow keypress up");
//				viewerpage.assertTrue(childBackwardScroll < childForwardScroll,"Verifying scroll level","The scroll level after arrow keypress up decreases from"+ childForwardScroll + " to "+ childBackwardScroll);
//				viewerpage.assertEquals(childBackwardScroll, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll is performed on viewbox 1 on child window", "Verified scroll is performed on viewbox 1 on child window");
//
//			}
//		}
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/5]","Verify Scroll level sync in Parent Window when scroll level is decreased from child window");
//		viewerpage.assertEquals(childBackwardScroll,viewerpage.getCurrentScrollPositionOfViewbox(1),"Verifying scroll is in sync for viewbox 1 on parent window","Verifed scroll is in sync for viewbox 1 on parent window");
//
//		viewerpage.openOrCloseChildWindows(2);
//	}
//	
//	
//
//}
//
