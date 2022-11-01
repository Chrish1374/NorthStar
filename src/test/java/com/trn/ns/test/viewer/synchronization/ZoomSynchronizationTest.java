package com.trn.ns.test.viewer.synchronization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Regression Document - Functional.NS.I15 Synchronization-CF0304ARevD - revision-0
//Functional.NS.I29_Viewer-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ZoomSynchronizationTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	private ViewerTextOverlays textOverlay;
	private ViewBoxToolPanel preset;
	private ViewerToolbar tool;

	// Get Patient Name
	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	// Get Patient Name
	String filePath2 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String mrLSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);

	// Get Patient Name
	String filePath3 = Configurations.TEST_PROPERTIES.get("CR_Thorax_Chest_filepath");
	String crThoraxPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath3);
	
	String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	
	String filePath5 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);

	String filePath6 = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioTexturePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));

	}

	@Test(groups = { "Chrome", "IE11", "Edge","US63","DE266","US289","US561","US2329","US2325","DR2796","BVT","F1090","E2E"})
	public void test01_US63_DE266_TC722_TC1083_TC1781_US289_TC2374_US561_TC2360_US2329_TC10165_US2325_TC10410_DR2796_verifyZoomSyncDataWithSameDimension() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify by default zoom synchronization is on"
				+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation"
				+"<br>Manage the viewer layout and viewboxes sizes- DE441.<br>"+
				"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane. <br>"+
				"[Risk and Impact]: Verify the TC10165 with quick toolbox options. <br>"+
				"[Risk and Impact]: Verify the TC10165.");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName, 1);
	    preset=new ViewBoxToolPanel(driver);
	    tool=new ViewerToolbar(driver);
		
		// get Zoom level for Canvas 0 before Zoom
		int intialZoomLevel1 = preset.getZoomValue(1);
		// get Zoom level for Canvas 1 before Zoom
		int intialZoomLevel2 = preset.getZoomValue(2);
		// right clicking on View box 1 and Clicking on Zoom Icon;
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/7]", "Verify Zoom icon is selected.");
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -100,-100);
		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC24[2] & Checkpoint[2/7]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(preset.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		// Verify Image Zoom Out on ViewBox 2 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/7]","Verify Zoom Level after Mouse Up Increase in View Box 2.");
		viewerpage.assertTrue(preset.getZoomValue(2) > intialZoomLevel2,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel2 + " to "+ preset.getZoomLevelValue(2));
		// Verify Zoom Level remains Same on both the View Boxes on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/7]","Verify Zoom Level on both View Port is same after Mouse Up");
		viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes remain Same","The Zoom level on both View Boxes is Same");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/7]", "Verify all image in series Zoom synchronously and no one pan out of View Box  ");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(1),"All images in series should Zoom synchronously and no one pan out of View Box.","TC722_TC1083_Zoom_Sync_Same_Dimension_Checkpoint5");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(2),"All images in series should Zoom synchronously and no one pan out of View Box.","TC722_TC1083_Zoom_Sync_Same_Dimension_Checkpoint6");

		// Verify Zoom Level remains Same on both the View Boxes on dragging Left Mouse Button down
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/7]","Verify Zoom Level on both View Port is same after Mouse down");
		viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes remain Same","The Zoom level on both View Boxes is Same and is "+ preset.getZoomLevelValue(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/7]", "Verify all image in series Zoom synchronously and no one pan out of View Box ");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(1),"All images in series should Zoom synchronously and no one pan out of View Box.","TC722_TC1083_Zoom_Sync_Same_Dimension_Checkpoint7");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(2),"All images in series should Zoom synchronously and no one pan out of View Box.","TC722_TC1083_Zoom_Sync_Same_Dimension_Checkpoint8");
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"US63","DE266","US289"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_wwSync"})
	public void test02_US63_DE266_TC723_TC1084_US289_TC2413_verifyZoomSyncOffPressingSpacebarDataWithSameDimension(String patientFilepath,String whichStudy)throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify by default zoom synchronization is off by clicking spacebar"
				+ "<br> Verify Sync ON/OFF for Zoom.");
		
		
		String filePath = Configurations.TEST_PROPERTIES.get(patientFilepath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(patientName,Integer.parseInt(whichStudy), 1);
	    preset=new ViewBoxToolPanel(driver);
	    tool=new ViewerToolbar(driver);
		// get Zoom level for View Box 1 before Zoom
		int intialZoomLevel1 = preset.getZoomValue(1);
		// get Zoom level for View Box 1 before Zoom
		int intialZoomLevel2 = preset.getZoomValue(2);
		// Click on Space bar to deactivate Synchronization
		viewerpage.performSyncONorOFF();
		// right clicking on View box 1 and Clicking on Zoom Icon;
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC25[3] & Checkpoint TC25[2] & Checkpoint[1/8]", "Verify Zoom icon is selected.");
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -100,-100);
		// get Zoom level for View Box 1 after Zoom
		int finalZoomLevel1 = preset.getZoomValue(1);
		// Verify Image zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC25[4] & Checkpoint[2/8]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(preset.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		// Verify Image does not zoom Out on ViewBox 2 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/8]","Verify Zoom Level after Mouse Up remains same in View Box 2 when sync is off.");
		viewerpage.assertEquals(preset.getZoomValue(2),intialZoomLevel2,"Check Zoom Level after Mouse up remains same in View Box 2","The Zoom level after Mouse down remains same: "+ preset.getZoomLevelValue(2));
		// Verify zoom Level are different on both the View Boxes on draggingLeft Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/8]","Verify Zoom Level on both View Port is different after Mouse Up and Sync Off");
		viewerpage.assertNotEquals(preset.getZoomValue(1),preset.getZoomValue(2),"Check Zoom Level on both View Boxes does not remains Same","The Zoom level on both View Boxes is different");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/8]", "Verify all image in series do not Zoom synchronously and no one pan out of View Box as Sync is Off");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(1),"All series should not Zoom synchronously and no one pan out of View Box.","TC02_"+patientName+"_Checkpoint5");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(2),"All series should not Zoom synchronously and no one pan out of View Box.","TC02_"+patientName+"_Checkpoint6");

		// Verify zoom Levels are different on both the View Boxes on dragging Left Mouse Button down
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/8]","Verify Zoom Level on View Port1 decrease after Mouse down");
		// Verify Image zoom In on ViewBox 1 on dragging Left Mouse Button Down
		viewerpage.assertTrue(preset.getZoomValue(1) < finalZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ finalZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		// Verify Image does not zoom In on ViewBox 2 on dragging Left MouseButton Down
		viewerpage.assertEquals(preset.getZoomValue(2),intialZoomLevel2,"Check Zoom Level after Mouse down remains same in View Box 2","The Zoom level after Mouse down remains same: "+ preset.getZoomLevelValue(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/8]", "Verify all image in series do not Zoom synchronously and no one pan out of View Box as Sync is Off");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(1),"All series should not Zoom synchronously and no one pan out of View Box when sync is off.","TC02_"+patientName+"_Checkpoint7");
		viewerpage.compareElementImage(protocolName,viewerpage.getViewbox(2),"All series should not Zoom synchronously and no one pan out of View Box when sync is off.","TC02_"+patientName+"_Checkpoint8");
	
		viewerpage.performSyncONorOFF();
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -100,-100);
	
		// Verify zoom Level are different on both the View Boxes on draggingLeft Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/8]","Verify Zoom Level on both View Port is different after Mouse Up and Sync Off");
		viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes does not remains Same","The Zoom level on both View Boxes is different");

		// Verify zoom Levels are different on both the View Boxes on dragging Left Mouse Button down
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 50);
		viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level after Mouse down remains same in View Box 1 and 2","The Zoom level after Mouse down remains same: "+ preset.getZoomLevelValue(2));
		viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"All series should Zoom synchronously and no one pan out of View Box when sync is off.","TC02_"+patientName+"_Checkpoint9");
		
	
	
	}

	@Test(groups = { "IE11", "Chrome", "firefox", "Edge","US67"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls","sheetName=test01_VerifyPan" })
	public void test04_US67_TC573_verifyImageZoomToFitViewPortForDifferentModalities(String PatientName, String Modality, String Rows, String Columns)
			throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Zoom mode on Load-Zoom to Fit default on the load");
		
		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + PatientName + " in viewer");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,  1);
		preset=new ViewBoxToolPanel(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC1[1] & Checkpoint[1/3]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)),"Verifying study is loaded in viewport","Study is loaded in viewport");
		// get Zoom level for View Box 1
		int intialZoomLevel1 = preset.getZoomValue(1);
		// Change layout to 3X3 and Check if Zoom level decrease to fit to view box
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC1[2] & Checkpoint[2/3]","Verify Zoom Level decrease on changing layout to 3X3.");
		viewerpage.assertTrue(preset.getZoomValue(1) < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level decrease from " + intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		intialZoomLevel1 = preset.getZoomValue(1);
		// Change layout to 1X1 and Check if Zoom level decreases
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Verify Zoom Level increases on changing layout to 1X1.");
		viewerpage.assertTrue(preset.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
	}

	@Test(groups = { "IE11", "Chrome", "firefox", "Edge" ,"US67"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls","sheetName=test01_VerifyPan" })
	public void test05_US67_TC589_verifyZoomFunctionalityFromToolMenu(String PatientName, String Modality, String Rows, String Columns)throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Zoom Functionality from Tools menu");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,  1);
		preset=new ViewBoxToolPanel(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)),"Verifying study is loaded in viewport","Study is loaded in viewport");
		// /get Zoom level for View Box 1 before Zoom
		int intialZoomLevel1 = preset.getZoomValue(1);
		// right clicking on View box 1 and Clicking on Zoom Icon;
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		tool=new ViewerToolbar(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC2[1] & Checkpoint[1/5]", "Verify Zoom icon is selected.");
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, -50,-50);
		// Verify Image Zoom Out on ViewBox 1 on dragging Left Mouse Button Up
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(preset.getZoomValue(1) > intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));
		intialZoomLevel1 = preset.getZoomValue(1);
		// Verify Zoom Level remains Same on both the View Boxes on dragging Left Mouse Button down
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint TC2[2] & Checkpoint[5/5]","Verify Zoom Level on both View Port is same after Mouse Up");
		viewerpage.assertTrue(preset.getZoomValue(1) < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ intialZoomLevel1 + " to "+ preset.getZoomLevelValue(1));

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US289","Sanity","BVT"})
	public void test06_US289_TC2377_verifyZoomForNonDICOM() throws InterruptedException, SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Dicom and Non-Dicom are not in sync if FrameReferenceUID is made same.");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(boneagePatientName,  2);
		textOverlay=new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the zoom from radial Menu" );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should zoom synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should zoom synchronously.","test14_checkpoint1");
		
		DatabaseMethods db = new DatabaseMethods(driver);
		HashMap<String, String> uid = db.getFrameReferenceUIDOfPatient(boneagePatientName);
		List<String> keys =  new ArrayList<>(uid.keySet());
		for(int i=0;i<keys.size();i++)
			db.updateFrameReferenceUIDOfPatient(Integer.parseInt(keys.get(i)),"1");

		helper.browserBackAndReloadViewer(boneagePatientName, 1, 4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the zoom from radial Menu" );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should zoom synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","test14_checkpoint2");
		
		
		
	}

	@Test(groups ={"IE11","Chrome","Edge","DE1498","Sanity","Positive","BVT"})
	public void test07_DE1498_TC6131_verifyZoomInAsymmetricLayout() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if image is not getting PAN while zooming in an asymmetric layout");
		

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioTexturePatientName+" in viewer" );
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(imbioTexturePatientName, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the zoom from radial Menu" );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3), 0, 0, 100, -50);
		preset=new ViewBoxToolPanel(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify images should zoom synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should zoom synchronously.","test15_01");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(3),"Verifying images should zoom synchronously.","test15_02");
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(3), "Checkpoint[2/4]", "Verifying the zoom is happening correctly");
		
		helper.browserBackAndReloadViewer(imbioTexturePatientName, 1, 1);

		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3), 0, 0, 100, -50);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify images should zoom synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should zoom synchronously.","test15_01");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(3),"Verifying images should zoom synchronously.","test15_02");
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(3), "Checkpoint[4/4]", "Verifying the zoom is happening correctly");
		
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","DE1498","Sanity","Positive"})
	public void test08_DE1498_TC6133_verifyZoomInSymmetricLayout() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if image is not getting PAN while zooming in a symmetric layout");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioTexturePatientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(imbioTexturePatientName, 1, 1);
		
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		preset=new ViewBoxToolPanel(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the zoom from radial Menu" );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3), 0, 0, 100, -50);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify images should zoom synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should zoom synchronously.","test16_01");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(3),"Verifying images should zoom synchronously.","test16_02");
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(3), "Checkpoint[2/4]", "Verifying the zoom is happening correctly");
		
		helper.browserBackAndReloadViewer(imbioTexturePatientName, 1, 1);
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3), 0, 0, 100, -50);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify images should zoom synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should zoom synchronously.","test16_01");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(3),"Verifying images should zoom synchronously.","test16_02");
		viewerpage.assertEquals(preset.getZoomLevelValue(1), preset.getZoomLevelValue(3), "Checkpoint[4/4]", "Verifying the zoom is happening correctly");
		
		
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException, IOException, InterruptedException {

		DatabaseMethods db = new DatabaseMethods(driver);
		if(db.getDefaultSyncMode() == false){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Updating the default sync mode value to :"+NSGenericConstants.BOOLEAN_TRUE);
			db.updateDefaultSyncMode(NSGenericConstants.BOOLEAN_TRUE);
			db.resetIISPostDBChanges();
		}		
	}

	
}

