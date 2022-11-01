package com.trn.ns.test.viewer.synchronization;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
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
public class PanSynchronizationTest extends TestBase{

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerTextOverlays textOverlay;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;
	private ViewerToolbar tool;

	//Loading the patient on viewer
	private String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	private String filePath2 = Configurations.TEST_PROPERTIES.get("NorthStar_MR_Lspine_filepath");
	private String nsMRLspine_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	private String filePath3 = Configurations.TEST_PROPERTIES.get("NorthStar^MR^Brain^WO^Contrast_filepath");
	private String ns_mrBrain_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

//	private String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
//	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
	
	private String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	private String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath5 = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
	String mrLSPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	private HelperClass helper;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));


	}

	@Test(groups ={"IE11","Chrome","Edge", "US64","US289","US2329","US2325","DR2796","Positive","F1090","E2E"})
	public void test01_US64_TC1045_US289_TC2374_US2329_TC10165_US2325_TC10410_DR2796_TC10786_verifyPanSync() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN sync is working fine on all viewboxes when series have same orientation and same FrameReferenceUID"
				+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation.<br>"+
				"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane. <br>"+
				"[Risk and Impact]: Verify the TC10165 with quick toolbox options.<br>"+
				"[Risk and Impact]: Verify the TC10165.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		
		patientPage = new PatientListPage(driver);		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 4);
		textOverlay=new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[1] & Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		tool=new ViewerToolbar(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[2] & Checkpoint[2/3]", "Verify Pan icon is selected.");
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[3] & Checkpoint[3/3]", "Verify images should PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","US64_TC1045_Checkpoint");
	}

	@Test(groups ={"IE11","Chrome","Edge", "US64","Sanity","BVT"})
	public void test02_US64_TC1046_verifyPanSyncWhenLayoutChanged() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify PAN sync is working fine on all viewboxes when layout is changed");
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();	

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 4);
		textOverlay=new ViewerTextOverlays(driver);
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC9[1] & Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//changing layout
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC9[2] & Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC9[3] & Checkpoint[3/3]", "Verify images should PAN synchronously.");
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC9[4] & Verifying images should PAN synchronously","US64_TC1046_Checkpoint");
	}

	@Test(groups ={"IE11","Chrome","Edge","US64","US289","Sanity"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_wwSync"})
	public void test03_US64_TC1048_US289_TC2415_verifyPanSyncStoppedWhenSpaceBarPressed(String patientFilepath,String whichStudy) throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify PAN sync is stopped when space bar is pressed"
				+ "<br> Verify Sync ON/OFF for Pan.");
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		String filePath = Configurations.TEST_PROPERTIES.get(patientFilepath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);		
		helper = new HelperClass(driver);
		textOverlay=new ViewerTextOverlays(driver);
		viewerpage = helper.loadViewerDirectly(patientName, helper.convertIntoInt(whichStudy));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC10[1] & Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//performing sync off
		viewerpage.performSyncONorOFF();

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC10[2] & Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC10[3] & Checkpoint[3/3]", "Verify All series should NOT PAN synchronously.");
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying  All series should NOT PAN synchronously. Only image on which PAN was selected should PAN.","TC03_"+patientName+"_Checkpoint1");
		
		viewerpage.performSyncONorOFF();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC10[4] & Checkpoint[2/3]", "Verify Pan icon is selected.");
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC10[4] & Checkpoint[4/4]", "Verify All series should PAN synchronously.");
	    viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC10[4] & Verifying  All series should NOT PAN synchronously. Only image on which PAN was selected should PAN.","TC03_"+patientName+"_Checkpoint2");
	
	}

	@Test(groups ={"IE11","Chrome","Edge","US64","US289"})
	public void test04_US64_TC1049_US289_TC2376_verifyPanNotInSyncForDifferentOrientationAndFrameReferenceUID() throws InterruptedException, SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify PAN is NOT in sync when series have different orientation and different FrameReferenceUID"
				+ "<br> Verify scroll synchronization with series having different Orientation");

		//Loading the patient on viewer   need data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+nsMRLspine_patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(nsMRLspine_patientName, 1, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		textOverlay=new ViewerTextOverlays(driver);
		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Only single image from series(on which PAN is selected) should PAN.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Only single image from series(on which PAN is selected) should PAN. All series should not get PAN synchronously as image "
				+ "orientation and FrameReferenceUID is different.","US64_TC1049_Checkpoint");

		DatabaseMethods db = new DatabaseMethods(driver);

		HashMap<String, String> uid = db.getFrameReferenceUIDOfPatient(nsMRLspine_patientName);

		List<String> keys =  new ArrayList<>(uid.keySet());
		int seriesID = db.updateFrameReferenceUIDOfPatient(uid.get(keys.get(0)), uid.get(keys.get(1)));

		helper.browserBackAndReloadViewer(boneagePatientName, 1, 1);
		
		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Only single image from series(on which PAN is selected) should PAN.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Only single image from series(on which PAN is selected) should PAN. All series should not get PAN synchronously as image "
				+ "orientation and FrameReferenceUID is different.","US64_TC1049_Checkpoint");

		db.updateFrameReferenceUIDOfPatient(uid.get(keys.get(1)), uid.get(keys.get(0)), seriesID);

	}

//	TC 2: Verify that user is able to pan the zoomed images (Automated) 
	@Test(groups ={"IE11","Chrome","Edge","US64"})
	public void test05_US64_TC1051_verifyPanSyncOnZoomedImages() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN sync is working fine on zoomed images");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC11[1] & Checkpoint[1/4]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> zoom option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Zoom from context Menu" );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		preset=new ViewBoxToolPanel(driver);
		int beforeZoom = preset.getZoomValue(1);
		viewerpage.dragAndReleaseOnViewer(0, 0, 0, -10);
		int afterZoom = preset.getZoomValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify images should Zoom..");
		viewerpage.assertTrue(beforeZoom < afterZoom, "Checkpoint TC2[1] & Checkpoint TC11[2]:verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint TC11[2]:Verifying images should Zoom.","US64_TC1051_Checkpoint1");
		//clicking zoom again
		//		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));

		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC11[3]:", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Checkpoint TC11[3]:Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[2] & Checkpoint TC11[4] & Checkpoint[4/4]", "Verify images should PAN synchronously on zoomed images.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously on zoomed images.","US64_TC1051_Checkpoint2");
	}

	@Test(groups ={"IE11","Chrome","Edge", "US64"})
	public void test06_US64_TC1073_verifyPanNotInSyncForDifferentOrientationAndSameFrameReferenceUID() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN is NOT in sync when series have different orientation and same FrameReferenceUID");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ns_mrBrain_patientName, 1);
		textOverlay=new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC12[1] & Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC12[2] & Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC12[3] & Checkpoint[3/3]", "Verify all series should not get PAN synchronously as image orientation is different.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying all series should not get PAN synchronously as image orientation is different.","US64_TC1073_Checkpoint");
	}

	
//	TC 3: Verify that window leveling on Panned image (Automated) 
	@Test(groups ={"IE11","Chrome","Edge","US68"})
	public void test10_US68_TC604_verifyWWWLAppliedOnPannedImages() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to apply WW/WL on panned images and panned location is not reset");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		textOverlay=new ViewerTextOverlays(driver);
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying study is loaded in viewport", "Study is loaded in viewport");

		//Selecting default annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		//changing layout
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2]: Checkpoint[3/5]", "Verify images should PAN .");
		viewerpage.dragAndReleaseOnViewer(0, 0, 300, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","US68_TC604_Checkpoint_Right");
		viewerpage.dragAndReleaseOnViewer(300, 0, -600, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","US68_TC604_Checkpoint_left");
		viewerpage.dragAndReleaseOnViewer(-300, 0, 300, -300);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","US68_TC604_Checkpoint_Top");
		viewerpage.dragAndReleaseOnViewer(0, -300, 0, 600);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","US68_TC604_Checkpoint_Bottom");

		//changing layout
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify panned location should not be changed and it should be relative location based on layout change.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be changed and it should be relative location based on layout change.","US68_TC604_Checkpoint_layout_Changed");

		//performing WW/WL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify panned location should not be reset when WW/WL is applied.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be reset when WW/WL is applied.","US68_TC604_Checkpoint_WW_WL");

	}

//	TC 6: Verify the text overlay on panned images (Automated) 
//	TC 7: Verify that image is not zoomed on pan (Automated) 
	@Test(groups ={"IE11","Chrome","Edge","US68"})
	public void test11_US68_TC605_verifyTextOverlayIconOnPannedImages() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the text overlay on panned images");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		textOverlay=new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying study is loaded in viewport", "Study is loaded in viewport");

		//Selecting full annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		//changing layout
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Checkpoint TC7[1] & Checkpoint TC6[1]: Verifying Pan icon is selected", "Pan icon is selected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify images should PAN .");
		viewerpage.dragAndReleaseOnViewer(0, 0, 300, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","US68_TC605_Checkpoint_Right");
		viewerpage.dragAndReleaseOnViewer(300, 0, -600, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","US68_TC605_Checkpoint_left");
		viewerpage.dragAndReleaseOnViewer(-300, 0, 300, -300);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","US68_TC605_Checkpoint_Top");
		viewerpage.dragAndReleaseOnViewer(0, -300, 0, 600);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","US68_TC605_Checkpoint_Bottom");

		//changing layout
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC6[2]:Checkpoint[4/4]", "Verify panned location should not be changed and it should be relative location based on layout change.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be changed and it should be relative when layout change to 2x2.","US68_TC605_Checkpoint_layout_Changed_2X2");

		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be changed and it should be relative when layout change to 1x1.","US68_TC605_Checkpoint_layout_Changed_1X1");
	}
//	TC 4: Verify cine play on panned images (Automated) 
	@Test(groups ={"IE11","Chrome","Edge","US68"})
	public void test12_US68_TC603_verifyCinePlayedOnPannedImages() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to play cine on panned images and panned location is not reset");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying study is loaded in viewport", "Study is loaded in viewport");

		//Selecting default annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//changing layout
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint[3/5]", "Verify images should PAN .");
		viewerpage.dragAndReleaseOnViewer(0, 0, 300, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","US68_TC603_Checkpoint_Right");
		viewerpage.dragAndReleaseOnViewer(300, 0, -600, 0);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","US68_TC603_Checkpoint_left");
		viewerpage.dragAndReleaseOnViewer(-300, 0, 300, -300);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","US68_TC603_Checkpoint_Top");
		viewerpage.dragAndReleaseOnViewer(0, -300, 0, 600);
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","US68_TC603_Checkpoint_Bottom");

		//changing layout
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify panned location should not be changed and it should be relative location based on layout change.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be changed and it should be relative location based on layout change.","US68_TC603_Checkpoint_layout_Changed");

		//playing cine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2] & Checkpoint[5/5]", "Verify panned location should not be reset when cine is played.");
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));

		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), newImagePath+"/goldImages/cineplayImage.png");
				
		viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), newImagePath+"/actualImages/cineplayImage.png");
		int imageAfterCine = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(currentImage, imageAfterCine, "verifying the cine play is stopped", "cine is stopped and working fine");

		String expectedImagePath = newImagePath+"/goldImages/cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/cineplayImage.png";
		String diffImagePath = newImagePath+"/diffImages/cineplayImage.png";
		
		//performing scroll
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, 10, 50);

		boolean cpStatus =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerpage.assertFalse(cpStatus, "Verifying cine is played on panned images","Successfully verified checkpoint with image comparison.<br>Image name is cineplayImage.png");

	}
//	TC 5: Verify that all images of the series are panned (Automated) 
	@Test(groups ={"IE11","Chrome","Edge","US68"})
	public void test13_US68_TC606_verifyScrollForwardBackwardAppliedOnPannedImages() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that all images in one series are panned when user pan one image in the series ( scroll + continuous scroll)");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying study is loaded in viewport", "Study is loaded in viewport");

		//Selecting default annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		//changing layout
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Checkpoint TC5[1]:Verifying Pan icon is selected", "Pan icon is selected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify images should PAN .");
		viewerpage.dragAndReleaseOnViewer(0, 0, 300, 0);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","US68_TC606_Checkpoint_Right");
		viewerpage.dragAndReleaseOnViewer(300, 0, -600, 0);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","US68_TC606_Checkpoint_left");
		viewerpage.dragAndReleaseOnViewer(-300, 0, 300, -300);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","US68_TC606_Checkpoint_Top");
		viewerpage.dragAndReleaseOnViewer(0, -300, 0, 600);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","US68_TC606_Checkpoint_Bottom");

		//changing layout
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify panned location should not be changed and it should be relative location based on layout change.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be changed and it should be relative location based on layout change.","US68_TC606_Checkpoint_layout_Changed");

		//performing scroll
		//		viewerpage.selectRepeatIconFromContextMenuOthertab(viewerpage.getViewPort(1));
		int scrollPos=viewerpage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[2]:Checkpoint[5/5]", "Verify panned location should not be reset when repeat scroll is performed.");
		viewerpage.mouseWheelScrollInViewer(1,"down", 1);

		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), (scrollPos+1), "Verifying backword scroll", "Backward scroll is performed.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be reset when backword scroll is performed.","US68_TC606_Checkpoint_Forward_Scroll");
		viewerpage.mouseWheelScrollInViewer(1,"up", 1);

		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), scrollPos, "Verifying forword scroll", "Forward scroll is performed.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Verifying panned location should not be reset when forward scroll is performed.", "US68_TC606_Checkpoint_Backward_Scroll");
	}

//	TC 1: Verify PAN functionality for all types of Modalities (Automated) 
	@Test(groups ={"IE11","Chrome","Edge","US68"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test01_VerifyPan" })
	public void test14_US68_TC602_verifyPanFunctionalityOnAllModalitiesOfData(String PatientName, String Modality,
			String Rows, String Columns) throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the pan functionality for "+Modality+"  modality");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify study is loaded in viewport.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying study is loaded in viewport", "Study is loaded in viewport");

		//Selecting full annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		//changing layout
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[4] & Checkpoint TC1[1]", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[4] & Checkpoint TC1[2] & Checkpoint[3/4]", "Verify images should PAN .");
		viewerpage.dragAndReleaseOnViewer(0, 0, 300, 0);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards right.","US68_TC602_"+Modality+"Checkpoint_Right");
		viewerpage.dragAndReleaseOnViewer(300, 0, -600, 0);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards left.","US68_TC602_"+Modality+"Checkpoint_left");
		viewerpage.dragAndReleaseOnViewer(-300, 0, 300, -300);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards top.","US68_TC602_"+Modality+"Checkpoint_Top");
		viewerpage.dragAndReleaseOnViewer(0, -300, 0, 600);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN towards bottom.","US68_TC602_"+Modality+"Checkpoint_Bottom");

		//changing layout
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[4] & Checkpoint TC1[3] & Checkpoint[4/4]", "Verify panned location should not be changed and it should be relative location based on layout change.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying panned location should not be changed and it should be relative location based on layout change.","US68_TC602_"+Modality+"_Checkpoint_layout_Changed");

	}

	//	[Refactoring] Refactor Pan command- Mouse movement leaving the browser
	@Test(groups ={"IE11","Chrome","Edge","US532"})
	public void test17_US532_TC1615_verifyPanOnLeavingBrowser() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Refactoring] Refactor Pan command- Mouse movement leaving the browser");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		textOverlay=new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewerWithClick(viewerpage.getViewPort(1),0, 0, 0, -300);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should PAN synchronously.","test17_US532_TC1615_Checkpoint");
	}

	// [Refactoring] Refactor Pan command- Mouse movement on text overlays inside view box
	@Test(groups ={"IE11","Chrome","Edge","US532"})
	public void test18_US532_TC1614_verifyPanOnLeavingViewbox() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Refactoring] Refactor Pan command- Mouse movement on text overlays inside view box");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");

		int x = viewerpage.getValueOfXCoordinate(viewerpage.getViewPort(2));
		int y = viewerpage.getValueOfYCoordinate(viewerpage.getViewPort(2));
		int width = viewerpage.getHeightOfWebElement(viewerpage.getViewPort(2));
		int height =  viewerpage.getWidthOfWebElement(viewerpage.getViewPort(2));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, x + 100, y + 200);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","test18_US532_TC1614_Checkpoint");
	}

//	@Test(groups ={"IE11","Chrome","Edge","US289","Sanity","BVT"})
	public void test19_US289_TC2377_verifyPanForNonDICOM() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Dicom and Non-Dicom are not in sync if FrameReferenceUID is made same.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneagePatientName+" in viewer" );
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(boneagePatientName, 1, 4);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getPatientIDOverlay(1)), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","test19_checkpoint1");
		
		DatabaseMethods db = new DatabaseMethods(driver);
		HashMap<String, String> uid = db.getFrameReferenceUIDOfPatient(boneagePatientName);
		List<String> keys =  new ArrayList<>(uid.keySet());
		for(int i=0;i<keys.size();i++)
			db.updateFrameReferenceUIDOfPatient(Integer.parseInt(keys.get(i)),"1");

		helper.browserBackAndReloadViewer(boneagePatientName, 1, 4);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","test19_checkpoint1");
	
	}
	

	@Test(groups ={"IE11","Chrome","Edge","US741","Sanity","BVT"})
	public void test20_US741_TC2625_verifyPanSyncForSeriesWithSameOrientation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PAN sync is working fine on all viewboxes when series have same orientation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(mrLSPatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// perform right click and select from tool menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the Pan from context Menu" );
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
		tool=new ViewerToolbar(driver);
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images having same orientation should PAN synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","test20_Checkpoint1");
	}

}