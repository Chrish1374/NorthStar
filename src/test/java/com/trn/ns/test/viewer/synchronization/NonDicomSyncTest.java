package com.trn.ns.test.viewer.synchronization;

import java.io.IOException;
import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class NonDicomSyncTest extends TestBase {
	
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	private HelperClass helper;
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	

	@Test(groups ={"firefox","Chrome","IE11","Edge","US674","Sanity","BVT"})
	public void test01_US674_TC2478_verifyNonDicomSync() throws InterruptedException,  SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non-Dicom Synchronization: DICOM - JPEG and DICOM- PNG");
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, username, password,2);
		
		//verify view box sync setting in configuration table
		DatabaseMethods db =new DatabaseMethods(driver);
		db.assertEquals(db.getDefaultViewboxSyncMode(),NSDBDatabaseConstants.ALL, "Verify Default Viewbox sync flag is set to All", "The Default viewbox sync is set to All");
		
		//select the layout to 3X2
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.closingConflictMsg();
		
		//variable to store zoom value
		preset=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = preset.getZoomValue(2);
		
		//select zoom from radial menu and perform zoom on Viewbox2
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2), 0,0,100,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/8]","Verify Zoom Level after Mouse Up Increase in View Box 2.");
		viewerPage.assertTrue(preset.getZoomValue(2) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/8]","Verify viewboxes with Non-Dicom images having same number of silces as the viewbox with DICOM synchronise on performing zoom");	
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox2 and viewbox5 are synchronised on performing zoom","test1_CheckPoint2");
		
		//select Pan from context menu and perform pan on Viewbox2
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2), 0,0,100,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/8]","Verify viewboxes with Non-DICOM images having same number of silces as the viewbox with DICOM synchronise on performing Pan");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox2 and viewbox5 are synchronised on performing zoom","test1_CheckPoint3");
		
		//Perform scroll in viewbox2 using keyboard
		int currentScrollPosition =viewerPage.getCurrentScrollPositionOfViewbox(2);
		viewerPage.scrollUpToSliceUsingKeyboard(2,3);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/8]","Verify scrolling is performed on viewbox2");
		viewerPage.assertTrue(currentScrollPosition<viewerPage.getCurrentScrollPositionOfViewbox(2), "Verifying forward scroll in viewbox2","Forward scroll is working fine in viewbox2");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/8]","Verify viewboxes with Non-DICOM images having same number of silces as the viewbox with DICOM synchronise on performing scroll");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox2 and viewbox5 are synchronised on performing zoom","test1_CheckPoint5");
		
		//select zoom from radial menu and perform zoom on Viewbox5 containing result JPEG image
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(4));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(4), 0,0,-50,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/8]","Verify viewboxes with Dicom images having same number of silces as the viewbox with Non-DICOM synchronise on performing zoom");	
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox5 and viewbox2 are synchronised on performing zoom","test1_CheckPoint6");
	
		//select Pan from context menu and perform pan on Viewbox5 containing result JPEG image
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(4));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(4), 0,0,-50,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]","Verify viewboxes with DICOM images having same number of silces as the viewbox with Non-DICOM synchronise on performing Pan");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox5 and viewbox2 are synchronised on performing zoom","test1_CheckPoint7");
	
		//Perform scroll in viewbox2 using keyboard
		viewerPage.scrollDownToSliceUsingKeyboard(4,3);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/8]","Verify viewboxes with Non-DICOM images having same number of silces as the viewbox with DICOM synchronise on performing scroll");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox5 and viewbox2 are synchronised on performing zoom","test1_CheckPoint8");
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US674","DE1874","Sanity"})
	public void test02_US674_TC2477_DE1874_TC7503_verifyNonDicomSync() throws InterruptedException,  SQLException, IOException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non-Dicom Synchronization with flag set as DICOM ONLY. <br>"+
		"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");
		
		//update view box sync setting in configuration table
		DatabaseMethods db =new DatabaseMethods(driver);
		db.updateDefaultViewboxSyncMode(NSDBDatabaseConstants.DICOM_ONLY);
		db.resetIISPostDBChanges();
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, username, password,2);
		
		//select the layout to 3X2
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.closingConflictMsg();
		preset=new ViewBoxToolPanel(driver);
		//variable to store zoom value
		int intialZoomLevel1 = preset.getZoomValue(2);
		
		//select zoom from radial menu and perform zoom on Viewbox2
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2), 0,0,100,-150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/8]","Verify Zoom Level after Mouse Up Increase in View Box 2.");
		viewerPage.assertTrue(preset.getZoomValue(2) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/8]","Verify viewboxes with Non-Dicom images having same number of silces as the viewbox with DICOM do not synchronise on performing zoom when sync flag is set to Dicom only");	
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox2 and viewbox5 are not synchronised on performing zoom","test2_CheckPoint2");
		
		//select Pan from context menu and perform pan on Viewbox2
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2), 0,0,50,50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/8]","Verify viewboxes with Non-DICOM images having same number of silces as the viewbox with DICOM do not synchronise on performing Pan when sync flag is set to Dicom only");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox2 and viewbox5 are synchronised on performing zoom","test2_CheckPoint3");
		
		//Perform scroll in viewbox2 using keyboard
		int currentScrollPosition =viewerPage.getCurrentScrollPositionOfViewbox(2);
		viewerPage.scrollUpToSliceUsingKeyboard(2,3);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/8]","Verify scrolling is performed on viewbox2");
		viewerPage.assertTrue(currentScrollPosition<viewerPage.getCurrentScrollPositionOfViewbox(2), "Verifying forward scroll in viewbox2","Forward scroll is working fine in viewbox2");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/8]","Verify viewboxes with Non-DICOM images having same number of silces as the viewbox with DICOM do not synchronise on performing scroll when sync flag is set to Dicom only");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox2 and viewbox5 are synchronised on performing zoom","test2_CheckPoint5");
		
		//select zoom from radial menu and perform zoom on Viewbox5 containing result JPEG image
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(4));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(4), 0,0,-50,-50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/8]","Verify viewboxes with Dicom images having same number of silces as the viewbox with Non-DICOM synchronise on performing zoom when sync flag is set to Dicom only");	
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox5 and viewbox2 are synchronised on performing zoom","test2_CheckPoint6");
	
		//select Pan from context menu and perform pan on Viewbox5 containing result JPEG image
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(4));
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(4), 0,0,-50,-50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]","Verify viewboxes with DICOM images having same number of silces as the viewbox with Non-DICOM do not synchronise on performing Pan when sync flag is set to Dicom only");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox5 and viewbox2 are synchronised on performing zoom","test2_CheckPoint7");
	
		//Perform scroll in viewbox2 using keyboard
		viewerPage.scrollDownToSliceUsingKeyboard(4,3);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/8]","Verify viewboxes with Non-DICOM images having same number of silces as the viewbox with DICOM do not synchronise on performing scroll when sync flag is set to Dicom only");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer,"Verify viewbox5 and viewbox2 are synchronised on performing zoom","test2_CheckPoint8");
	}
	
	@AfterMethod(alwaysRun=true)
	public void updateSyncMode() throws SQLException, IOException, InterruptedException {
		
		DatabaseMethods db =new DatabaseMethods(driver);
		db.updateDefaultViewboxSyncMode(NSDBDatabaseConstants.ALL);
		db.deleteUserSetLayout();
		db.resetIISPostDBChanges();
		
	}

}
