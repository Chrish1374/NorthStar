

//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//import javax.xml.parsers.ParserConfigurationException;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import org.xml.sax.SAXException;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.dataProviders.DataProviderArguments;
//import com.trn.ns.dataProviders.ExcelDataProvider;
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
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class ZoomSynchronizationTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath);
//
//	// Get Patient Name
//	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//
//	// Get Patient Name
//	String filePath2 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//	String mrLSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath2);
//
//	// Get Patient Name
//	String filePath3 = Configurations.TEST_PROPERTIES.get("CR_Thorax_Chest_filepath");
//	String crThoraxPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath3);
//	
//	String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
//	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//	
//	String filePath5 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath5);
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//	//multimonitor	
//
//	@Test(groups = { "firefox", "Chrome","multimonitor"})
//	// Due to issue with browser switch, this script is not running in IE and Edge
//	public void test03_US63_TC726_verifyZoomSyncOnChildWindow()throws InterruptedException, AWTException {
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify Zoom sync is working fine on all viewboxes on Multi Mointor window.");
//			patientPage = new PatientListPage(driver);
//			
//			viewerpage = new ViewerPage(driver);
//
//			patientPage.clickOnPatientRow(liver9PatientName);
//			studyPage.clickOntheFirstStudy();
//			viewerpage.waitForViewerpageToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/8]","Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//			// selecting 2 window
//			String parentWindow = viewerpage.getCurrentWindowID();
//			viewerpage.openOrCloseChildWindows(2);
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//			// variable to store the Zoom level of Child and Parent Window
//			int ChildIntialZoomLevel, ChildFinalZoomLevel = 0;
//			// selecting check box apply to all monitor if not selected already
////			viewerpage.selectCheckBoxApplyToAllMonitor();
//			// changing layout to 2X1
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//			viewerpage.waitForAllImagesToLoad();
//			int parentIntialZoomLevel = viewerpage.getZoomLevel(1);
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					// right clicking on view box 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//					viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/8]","Verify Zoom icon is selected on child window.");
//					viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
//					// Perform Mouse up operation to increase Zoom Level
//					viewerpage.dragAndReleaseOnViewer(0, 0, -100, -100);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/8]","Verify Zoom level in Child Window increases on Mouse Up");
//					viewerpage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			int parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/8]","Verify Zoom level in Parent Window increases on Mouse Up");
//			viewerpage.assertTrue(parentFinalZoomLevel > parentIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ parentIntialZoomLevel + " to "+ parentFinalZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/8]","Verify Zoom level on both Widow remain same");
//			viewerpage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");
//			// Perform Mouse Down Operation to decrease Zoom level on Child Window and update all Variable accordingly
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					viewerpage.dragAndReleaseOnViewer(0, 0, 0, 150);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[6/8]","Verify Zoom level in Child Window decrease on Mouse down");
//					viewerpage.assertTrue(ChildFinalZoomLevel < ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from"+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]","Verify Zoom level in Parent Window decrease on Mouse down");
//			viewerpage.assertTrue(viewerpage.getZoomLevel(1) < parentIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ viewerpage.getZoomLevel(1) + " to "+ parentFinalZoomLevel);
//			parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[8/8]","Verify Zoom level on both Widow remain same");
//			viewerpage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");
//		}
//
//	}
//
//	@Test(groups = { "firefox", "Chrome","multimonitor" })
//	// Due to issue with browser switch, this script is not running in IE and Edge
//	public void test04_US63_DE266_TC727_TC1088_verifyZoomSyncOffByPressingSpaceBarOnChildWindow()
//			throws InterruptedException, AWTException, SAXException,
//			IOException, ParserConfigurationException {
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify zoom synchronization is off after clicking on space bar on multi-monitor.");
//			patientPage = new PatientListPage(driver);
//			
//			viewerpage = new ViewerPage(driver);
//
//			patientPage.clickOnPatientRow(liver9PatientName);
//			studyPage.clickOntheFirstStudy();
//			viewerpage.waitForViewerpageToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/12]","Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//			// selecting 2 window
//			String parentWindow = viewerpage.getCurrentWindowID();
//			viewerpage.openOrCloseChildWindows(2);
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//			// variable to store the Zoom level of Child and Parent Window
//			int ChildIntialZoomLevel, ChildFinalZoomLevel = 0;
//			// selecting check box apply to all monitor if not selected already
////			viewerpage.selectCheckBoxApplyToAllMonitor();
//			// changing layout to 2X1
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//			viewerpage.waitForAllImagesToLoad();
//			// Store the initial Zoom level of Parent
//			int parentIntialZoomLevel = viewerpage.getZoomLevel(1);
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					// Click on Space bar to deactivate Synchronization
//					viewerpage.performSyncONorOFF();
//					// right clicking on view box 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//					viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/12]","Verify Zoom icon is selected on child window.");
//					viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
//					// Perform Mouse up operation to increase Zoom Level
//					viewerpage.dragAndReleaseOnViewer(0, 0, -50, -50);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/12]","Verify Zoom level in Child Window increases on Mouse Up");
//					viewerpage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC727_TC1088_Zoom_Sync_Same_Dimension_Checkpoint4");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			int parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest,"Checkpoint[5/12]","Verify Zoom level in Parent Window remain unchanged on Mouse Up after Sync is Off");
//			viewerpage.assertEquals(parentFinalZoomLevel,parentIntialZoomLevel,"Check Zoom Level on View Box 1 on Parent Window remains Unchange after Sync off","The Zoom level after Mouse up on Parent Window remain same "+ parentIntialZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/12]","Verify Zoom level on both Child and Parent Window Changes");
//			viewerpage.assertNotEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up in both Window after Sync is Off","The Zoom level on Parent window is "+ parentFinalZoomLevel+ "and Child Wndow is"+ ChildFinalZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC727_TC1088_Zoom_Sync_Same_Dimension_Checkpoint7");
//
//			// Perform Mouse Down Operation to decrease Zoom level on Child Window and update all Variable accordingly
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					viewerpage.dragAndReleaseOnViewer(0, 0, 0, 150);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[8/12]","Verify Zoom level in Child Window decrease on Mouse Down");
//					viewerpage.assertTrue(ChildFinalZoomLevel < ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC727_TC1088_Zoom_Sync_Same_Dimension_Checkpoint9");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest,"Checkpoint[10/12]","Verify Zoom level in Parent Window remains unchange on Mouse down with sync OFf");
//			viewerpage.assertEquals(parentFinalZoomLevel,viewerpage.getZoomLevel(1),"Check Zoom Level on View Box 1 on Parent Window remains Unchange after Sync off","The Zoom level after Mouse down on Parent Window remain same "+ parentIntialZoomLevel);
//			parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[11/12]","Verify Zoom level on Child and Parent Window Changes");
//			viewerpage.assertNotEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check the Zoom level on Child and Parent Window is different","The Zoom level on Parent window is "+ parentFinalZoomLevel+ "and Child Wndow is"+ ChildFinalZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[12/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC727_TC1088_Zoom_Sync_Same_Dimension_Checkpoint12");
//		}
//	}
//	
//	@Test(groups = { "firefox", "Chrome","multimonitor" })
//	// Due to issue with browser switch, this script is not running in IE and Edge
//	public void test11_DE226_TC1089_verifyImageZoomSyncDataWithDifferentDimensionChildWindow()throws InterruptedException, AWTException {
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify Zoom sync is working fine on all viewboxes on Child Window window on Data With different Dimension");
//			patientPage = new PatientListPage(driver);
//			
//
//			patientPage.clickOnPatientRow(mrLSPPatientName);
//			studyPage.clickOntheFirstStudy();
//			viewerpage = new ViewerPage(driver);
//			viewerpage.waitForViewerpageToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/12]","Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//			// selecting 2 window
//			String parentWindow = viewerpage.getCurrentWindowID();
//			viewerpage.openOrCloseChildWindows(2);
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//			// variable to store the Zoom level of Child and Parent Window
//			int ChildIntialZoomLevel, ChildFinalZoomLevel = 0;
//			// selecting check box apply to all monitor if not selected already
////			viewerpage.selectCheckBoxApplyToAllMonitor();
//			// changing layout to 2X1
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//			viewerpage.waitForAllImagesToLoad();
//			int parentIntialZoomLevel = viewerpage.getZoomLevel(1);
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					// right clicking on view box 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//					viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/12]","Verify Zoom icon is selected on child window.");
//					viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
//					// Perform Mouse up operation to increase Zoom Level
//					viewerpage.dragAndReleaseOnViewer(0, 0, -20, -20);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/12]","Verify Zoom level in Child Window increases on Mouse Up");
//					viewerpage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/12]", "Verify all image in series zoom synchronously and no one pan out of View Box");
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series  zoom synchronously and no one pan out of View Box.","TC1089_Zoom_Sync_Different_Dimension_Checkpoint4");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			int parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/12]","Verify Zoom level in Parent Window increases on Mouse Up");
//			viewerpage.assertTrue(parentFinalZoomLevel > parentIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ parentIntialZoomLevel + " to "+ parentFinalZoomLevel);
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one should pan out of View Box in Parent Window .","TC1089_Zoom_Sync_Same_Dimension_Checkpoint4");
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[6/12]","Verify Zoom level on both Widow remain same");
//			viewerpage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/12]", "Verify all image in series zoom synchronously and no one pan out of View Box");
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series zoom synchronously and no one pan out of View Box.","TC1089_Zoom_Sync_Different_Dimension_Checkpoint7");
//			// Perform Mouse Down Operation to decrease Zoom level on Child Window and update all Variable accordingly
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					viewerpage.dragAndReleaseOnViewer(0, 0, 0, 150);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[8/12]","Verify Zoom level in Child Window increases on Mouse");
//					viewerpage.assertTrue(ChildFinalZoomLevel < ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/12]", "Verify all image in series zoom synchronously and no one pan out of View Box  ");
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series zoom synchronously and no one pan out of View Box.","TC1089_Zoom_Sync_Different_Dimension_Checkpoint9");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[10/12]","Verify Zoom level in Parent Window decrease on Mouse down");
//			viewerpage.assertTrue(viewerpage.getZoomLevel(1) < parentFinalZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ parentIntialZoomLevel + " to "+ viewerpage.getZoomLevel(1));
//			parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[11/12]","Verify Zoom level on both Widow remain same");
//			viewerpage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[12/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series zoom synchronously and no one pan out of View Box.","TC1089_Zoom_Sync_Different_Dimension_Checkpoint12");
//		}
//	}
//
//	@Test(groups = { "firefox", "Chrome","multimonitor" })
//	// Due to issue with browser switch, this script is not running in IE and Edge
//	public void test10_DE226_US556_TC1087_TC1627_verifyImageZoomSyncDataWithSameDimensionChildWindow()throws InterruptedException, AWTException {
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify Zoom sync is working fine on all viewboxes on Multi Mointor window on Data With Same Dimension</br> Verify Zoom Sync in multi-Mointor View");
//			patientPage = new PatientListPage(driver);
//			
//
//			patientPage.clickOnPatientRow(liver9PatientName);
//			studyPage.clickOntheFirstStudy();
//			viewerpage = new ViewerPage(driver);
//			viewerpage.waitForViewerpageToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/8]","Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//			// selecting 2 window
//			String parentWindow = viewerpage.getCurrentWindowID();
//			viewerpage.openOrCloseChildWindows(2);
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//			// variable to store the Zoom level of Child and Parent Window
//			int ChildIntialZoomLevel, ChildFinalZoomLevel = 0;
//			// selecting check box apply to all monitor if not selected already
////			viewerpage.selectCheckBoxApplyToAllMonitor();
//			// changing layout to 2X1
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//			viewerpage.waitForAllImagesToLoad();
//			int parentIntialZoomLevel = viewerpage.getZoomLevel(1);
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					// right clicking on view box 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//					viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/8]","Verify Zoom icon is selected on child window.");
//					viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
//					// Perform Mouse up operation to increase Zoom Level
//					viewerpage.dragAndReleaseOnViewer(0, 0, -100, -100);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/8]","Verify Zoom level in Child Window increases on Mouse Up");
//					viewerpage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one should pan out of View Box in Child Window .","TC1087_Zoom_Sync_Same_Dimension_Checkpoint3");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			int parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[4/8]","Verify Zoom level in Parent Window increases on Mouse Up");
//			viewerpage.assertTrue(parentFinalZoomLevel > parentIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ parentIntialZoomLevel + " to "+ parentFinalZoomLevel);
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one should pan out of View Box in Parent Window .","TC1087_Zoom_Sync_Same_Dimension_Checkpoint4");
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[5/8]","Verify Zoom level on both Widow remain same");
//			viewerpage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");
//			// Perform Mouse Down Operation to decrease Zoom level on Child Window and update all Variable accordingly
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					viewerpage.dragAndReleaseOnViewer(0, 0, 0, 150);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[6/8]","Verify Zoom level in Child Window increases on Mouse");
//					viewerpage.assertTrue(ChildFinalZoomLevel < ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one should pan out of View Box in Parent Window .","TC1087_Zoom_Sync_Same_Dimension_Checkpoint6");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]","Verify Zoom level in Parent Window decrease on Mouse down");
//			viewerpage.assertTrue(viewerpage.getZoomLevel(1) < parentFinalZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ parentIntialZoomLevel + " to "+ viewerpage.getZoomLevel(1));
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one should pan out of View Box in Parent Window .","TC1087_Zoom_Sync_Same_Dimension_Checkpoint7");
//			parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[8/8]","Verify Zoom level on both Widow remain same");
//			viewerpage.assertEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up remains same in both Window","The Zoom level after Mouse up remains same");
//		}
//	}
//	
//	@Test(groups = { "firefox", "Chrome","multimonitor" })
//	// Due to issue with browser switch, this script is not running in IE and Edge
//	public void test12_DE226_TC1090_verifyZoomSyncOffOnChildWindowDataDifferentDimension()throws InterruptedException, AWTException, SAXException,IOException, ParserConfigurationException {
//		{
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify zoom synchronization is off after clicking on space bar on multi-monitor.");
//			patientPage = new PatientListPage(driver);
//			
//			viewerpage = new ViewerPage(driver);
//
//			patientPage.clickOnPatientRow(mrLSPPatientName);
//			studyPage.clickOntheFirstStudy();
//			viewerpage.waitForViewerpageToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/12]","Verify user is navigating to viewer page.");
//			viewerpage.assertTrue(viewerpage.viewboxImg1.isDisplayed(),"Verifying user is navigating to viewer page","User is navigated to viewer page");
//			// selecting 2 window
//			String parentWindow = viewerpage.getCurrentWindowID();
//			viewerpage.openOrCloseChildWindows(2);
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//			// variable to store the Zoom level of Child and Parent Window
//			int ChildIntialZoomLevel, ChildFinalZoomLevel = 0;
//			// selecting check box apply to all monitor if not selected already
////			viewerpage.selectCheckBoxApplyToAllMonitor();
//			// changing layout to 2X1
//			viewerpage.selectLayout(viewerpage.twoByOneLayoutIcon);
//			viewerpage.waitForAllImagesToLoad();
//			// Store the initial Zoom level of Parent
//			int parentIntialZoomLevel = viewerpage.getZoomLevel(1);
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					// Click on Space bar to deactivate Synchronization
//					viewerpage.performSyncONorOFF();
//					// right clicking on view box 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
//					viewerpage.selectZoomFromQuickToolbar(viewerpage.viewboxImg1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/12]","Verify Zoom icon is selected on child window.");
//					viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
//					// Perform Mouse up operation to increase Zoom Level
//					viewerpage.dragAndReleaseOnViewer(0, 0, -50, -50);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/12]","Verify Zoom level in Child Window increases on Mouse Up");
//					viewerpage.assertTrue(ChildFinalZoomLevel > ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC1090_Zoom_Sync_Different_Dimension_Checkpoint4");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			int parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest,"Checkpoint[5/12]","Verify Zoom level in Parent Window remain unchanged on Mouse Up after Sync is Off");
//			viewerpage.assertEquals(parentFinalZoomLevel,parentIntialZoomLevel,"Check Zoom Level on View Box 1 on Parent Window remains Unchange after Sync off","The Zoom level after Mouse up on Parent Window remain same "+ parentIntialZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/12]","Verify Zoom level on both Child and Parent Window Changes");
//			viewerpage.assertNotEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check Zoom Level after Mouse up in both Window after Sync is Off","The Zoom level on Parent window is "+ parentFinalZoomLevel+ "and Child Wndow is"+ ChildFinalZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC1090_Zoom_Sync_Different_Dimension_Checkpoint7");
//
//			// Perform Mouse Down Operation to decrease Zoom level on Child Window and update all Variable accordingly
//			for (String childWindow : childWinHandles) {
//				if (!childWindow.equals(parentWindow)) {
//					viewerpage.switchToWindow(childWindow);
//					viewerpage.maximizeWindow();
//					ChildIntialZoomLevel = viewerpage.getZoomLevel(1);
//					viewerpage.dragAndReleaseOnViewer(0, 0, 0, 150);
//					ChildFinalZoomLevel = viewerpage.getZoomLevel(1);
//					ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[8/12]","Verify Zoom level in Child Window decrease on Mouse Down");
//					viewerpage.assertTrue(ChildFinalZoomLevel < ChildIntialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ ChildIntialZoomLevel + " to "+ ChildFinalZoomLevel);
//					ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//					viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC1090_Zoom_Sync_Different_Dimension_Checkpoint9");
//				}
//			}
//			viewerpage.switchToWindow(parentWindow);
//			viewerpage.maximizeWindow();
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest,"Checkpoint[10/12]","Verify Zoom level in Parent Window remains unchange on Mouse down with sync OFf");
//			viewerpage.assertEquals(parentFinalZoomLevel,viewerpage.getZoomLevel(1),"Check Zoom Level on View Box 1 on Parent Window remains Unchange after Sync off","The Zoom level after Mouse down on Parent Window remain same "+ parentIntialZoomLevel);
//			parentFinalZoomLevel = viewerpage.getZoomLevel(1);
//			ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[11/12]","Verify Zoom level on Child and Parent Window Changes");
//			viewerpage.assertNotEquals(ChildFinalZoomLevel,parentFinalZoomLevel,"Check the Zoom level on Child and Parent Window is different","The Zoom level on Parent window is "+ parentFinalZoomLevel+ "and Child Wndow is"+ ChildFinalZoomLevel);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[12/12]", "Verify all image in series do not zoom synchronously and no one pan out of View Box  ");
//			viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All images in series do not zoom synchronously and no one pan out of View Box.","TC1090_Zoom_Sync_Different_Dimension_Checkpoint12");
//		}
//	}
//

/*
 * 
 * 
 * 	//TC1066 - Active overlay manual input of zoom percentage or select from zoom options and synchronization - Context Menu Coverage
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover
	@Test(groups ={"firefox","Chrome","IE11","US340","Sanity","BVT"})
	public void test02_US340_TC1066_verifyZoomOverlayByContextMenu() throws InterruptedException {

		

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of zoom percentage or select from zoom options and synchronization - Context Menu Coverage ");
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName,username, password, 1);
		presetMenu=new ViewBoxToolPanel(driver);

		//Step1 - "Zoom to 100%" option.
		String defaultZoomValueFirstViewbox = viewerpage.getValueOfZoom(1);

		for(int i = 1 ; i<=2 ;i++){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verifying the presence of Zoom context table" );
			viewerpage.assertTrue(presetMenu.verifyPresenceOfContextMenu(i, viewerpage.getZoomLabelOverlay(1), presetMenu.OverlayMenuTable), "Verify the presence of Zoom context table", "Zoom context table is displaying");

			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[1] & Checkpoint[3/8]", "Verifying that the Zoom value is set to 'Zoom to 100%' for sync series" );
			viewerpage.assertEquals(viewerpage.getValueOfZoom(i), "100", "Verify zoom is set to 100%", "Zoom value is updated to 100%");
			compareZoomValues(i,true);

			//Step2 - "Zoom to True Size" option.
			String zoomValueFirstViewboxBeforeZoomTrueSize = viewerpage.getValueOfZoom(i);
			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_TRUE_SIZE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[2] & Checkpoint[4/8]", "Verifying that the Zoom value is set to 'Zoom to True Size' for sync series" );
			viewerpage.assertEquals(viewerpage.getValueOfZoom(i), zoomValueFirstViewboxBeforeZoomTrueSize, "Verify that Zoom value does not change ", "Zoom values are "+viewerpage.getValueOfZoom(i)+" and  "+zoomValueFirstViewboxBeforeZoomTrueSize+" ");
			compareZoomValues(i,true);

			//Step3 - "Zoom to Fit" option.
			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_FIT);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[3] & Checkpoint[5/8]", "Verifying that the Zoom value is set to 'Zoom to Fit' for sync series" );
			viewerpage.assertEquals(viewerpage.getValueOfZoom(i), defaultZoomValueFirstViewbox, "Verify zoom is set to its default zoom value upon loading the data in a viewer that size.", "Zoom value is updated to default size");
			compareZoomValues(i,true);

			//Step4 - After pressing space bar - Without sync-
			//performing sync off
			viewerpage.performSyncONorOFF();
			//Perform Zoom to 100%
			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[4] & Checkpoint[6/8]", "Verifying that the Zoom value is set to 'Zoom to 100%' for series not in sync" );
			viewerpage.assertEquals(viewerpage.getValueOfZoom(i), "100", "Verify zoom is set to 100%", "Zoom value is updated to 100%");
			compareZoomValues(i,false);

			//Step2 - "Zoom to True Size" option.
			String zoomValueFirstViewboxBeforeZoomTrueSizeForNotSyncSeries = viewerpage.getValueOfZoom(i);
			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_TRUE_SIZE);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[4] & Checkpoint[7/8]", "Verifying that the Zoom value is set to 'Zoom to True Size' for series not in sync" );
			viewerpage.assertEquals(viewerpage.getValueOfZoom(i), zoomValueFirstViewboxBeforeZoomTrueSizeForNotSyncSeries, "Verify that Zoom value does not change ", "Zoom value is not updated");
			compareZoomValues(i,false);

			//Step3 - "Zoom to Fit" option.
			//To check "zoom to Fit" not applied to sync series, change the other viewboxes zoom value to 100%
			viewerpage.performSyncONorOFF();
			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
			viewerpage.performSyncONorOFF();

			presetMenu.selectZoomOverlay(i, ViewerPageConstants.ZOOM_TO_FIT);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[4] & Checkpoint[8/8]", "Verifying that the Zoom value is set to 'Zoom to Fit' for series not in sync" );
			viewerpage.assertEquals(viewerpage.getValueOfZoom(i), defaultZoomValueFirstViewbox, "Verify zoom is set to its default zoom value upon loading the data in a viewer that size.", "Zoom value is updated to default size");
			compareZoomValues(i,false);

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			presetMenu.changeZoomValue(90, i);
		}
	}
	
	@Test(groups = { "IE11", "Chrome", "firefox", "Edge" ,"US67","Sanity","BVT"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls","sheetName=test01_VerifyPan" })
	public void test06_US67_TC592_verifyImageZoomOutLessThanFitToViewBox(String PatientName, String Modality, String Rows, String Columns)throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Image can be Zoom out to less than Fit to ViewBox");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,  1);
		preset=new ViewBoxToolPanel(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC3[1] & Checkpoint[1/3]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)),"Verifying study is loaded in viewport","Study is loaded in viewport");
		// /get Zoom level for View Box 1 before Zoom
		int intialZoomLevel1 = preset.getZoomLevelValue(1);
		// right clicking on View box 1 and Clicking on Zoom Icon;
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC3[2] & Checkpoint[2/3]", "Verify Zoom icon is selected.");
		// Verify Zoom Level decrease than Fit to window on Mouse down
		// operation
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest,"Checkpoint TC3[3] & Checkpoint[3/3]","Verify Zoom Level decrease less than Zoom to Fit for a View box on Mouse down");
		viewerpage.assertTrue(preset.getZoomLevelValue(1) < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Down decrease from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));

	}
 * 
 * 
 */
/*
//blocking below test case due to DR2707 
	//@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"DE266","US289","US741","Sanity","BVT"})
	public void test03_DE226_TC1085_US289_TC2376_US741_TC2606_verifyImageZoomSyncDataWithDifferentDimension()throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify zoom synchronization on data having different orientation");
		patientPage = new PatientListPage(driver);

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(mrLSPPatientName, 1);
	    preset=new ViewBoxToolPanel(driver);
		//change layout to 2X3.
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		
		//variable to store zoom value of all view box  
		int intialZoomLevel1 = preset.getZoomValue(1);
		int intialZoomLevel4 = preset.getZoomValue(4);
		int intialZoomLevel5 = preset.getZoomValue(5);
		
        //select zoom from radial menu
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -50,-50);
		
		//variable to store zoom value of all view box  
		int fnalZoomLevel1 = preset.getZoomValue(1);
		int finalZoomLevel2 = preset.getZoomValue(2);
		int finalZoomLevel3 = preset.getZoomValue(3);
		int finalZoomLevel4 = preset.getZoomValue(4);
		int finalZoomLevel5 = preset.getZoomValue(5);
		
		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertEquals(fnalZoomLevel1, finalZoomLevel2,"Verifying Zoom synchronization in viewbox 1 and 2","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		
		// Verify view box 2 and 3 are synchronized with view box 1 on performing zoom
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]","Verify viewbox 2 and 3 are synchronised with viewbox1");
		viewerpage.assertEquals(fnalZoomLevel1,finalZoomLevel2,"Verify viewbox 2 is synchronized with viewbox1","The Zoom level in both viewboxes are same");
		viewerpage.assertEquals(fnalZoomLevel1,finalZoomLevel3,"Verify viewbox 3 is synchronized with viewbox1","The Zoom level in both viewboxes are same");
		
		// Verify view box 4 and 5 are not synchronized with view box 1 on performing zoom as they have different orientation
		viewerpage.assertNotEquals(fnalZoomLevel1,finalZoomLevel4,"Verify viewbox 4 is not synchronized with viewbox1","The Zoom level in both viewboxes are different");
		viewerpage.assertNotEquals(fnalZoomLevel1,finalZoomLevel5,"Verify viewbox 3 is not synchronized with viewbox1","The Zoom level in both viewboxes are different");
		viewerpage.assertEquals(intialZoomLevel4,finalZoomLevel4,"Verify zoom level on viewbox4 remain same on performing zoom on viewbox1","The Zoom level in viewbox 4 remain same");
		viewerpage.assertEquals(intialZoomLevel5,finalZoomLevel5,"Verify zoom level on viewbox5 remain same on performing zoom on viewbox1","The Zoom level in viewbox 5 remain same");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify series should PAN synchronously.");
		viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one pan out of View Box .","TC1085_Zoom_Sync_Different_Dimension_Checkpoint4");
		
		
	}
	*/
//
//}
//
