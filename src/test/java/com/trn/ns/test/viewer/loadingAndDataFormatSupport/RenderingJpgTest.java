package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)

public class RenderingJpgTest extends TestBase{

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private HelperClass helper;
	private ViewBoxToolPanel presetMenu;
	private ViewerLayout layout;
	private ViewBoxToolPanel preset;
	
	//Loading the patient on viewer
	String filePath = TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneAgeStudy = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath3 = Configurations.TEST_PROPERTIES.get("Picline_filepath");
	String piclinePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	
	String filePath4 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneAgeDE349patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymousPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE349"})
	public void test01_DE349_TC1474_ProgressiveJPGfilesareimportedCorrectly() throws InterruptedException, AWTException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Progressive JPG 1.2 files are imported incorrectly");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneAgeDE349patientName+" in viewer" );
		
		patientPage = new PatientListPage(driver);
	
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgeDE349patientName, 1);
		preset=new ViewBoxToolPanel(driver);
		ContentSelector contentSelector = new ContentSelector(driver);
		int initialZoomLevel = preset.getZoomValue(2);
		
		//Scrolling to get "atlas+1" series.
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, 0, 5);
		
		//Performing zoom and pan on "atlas+1" series.
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2), 0, 100, 0,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 01 (TC1474): Checkpoint[1/6]", "Verifying that the Zoom and Pan is implemented properly." );
		
		viewerpage.assertTrue(preset.getZoomValue(2) > initialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ initialZoomLevel + " to "+ preset.getZoomLevelValue(1));
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),0, 0, 100, -50);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN","DE349_TC1474_Checkpoint_PanForAtlas+1");

		//Scrolling to get "atlas-1" series.
//		viewerpage.selectScrollFromQuickToolbar(1);
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3), 0, 0, 0, 5);
		
		//Performing zoom and pan on "atlas-1" series.
        viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		initialZoomLevel = preset.getZoomValue(3);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3), 0, -100, 0, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 02 (TC1474): Checkpoint[2/6]", "Verifying that the Zoom and Pan is implemented properly." );
		viewerpage.assertTrue(preset.getZoomValue(3) < initialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse down decrease from "+ initialZoomLevel + " to "+ preset.getZoomLevelValue(1));
        viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(3));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3),0, 0, 100, -50);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN","DE349_TC1474_Checkpoint_PanForAtlas-1");

		//Performing 'Zoom to 100%' on "atlas-1" series.
		presetMenu=new ViewBoxToolPanel(driver);
		presetMenu.changeZoomNumber(3, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 03 (TC1474): Checkpoint[3/6]", "Verifying that the Zoom value is set to 'Zoom to 100%'." );
		viewerpage.assertEquals(preset.getZoomValue(3), 100, "Verify zoom to 100%", "Zoom value is updated to 100%"); 

		//Performing 'Zoom to fit' on "atlas-1" series.
		presetMenu.chooseZoomToFit(2); 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 04 (TC1474): Checkpoint[4/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'." );
		viewerpage.assertEquals(preset.getZoomLevelValue(2), preset.getZoomLevelValue(1), "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value"); 

		//Changing layout to 2*2.
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		
		//Obtaining name of the series and storing them in the variables.
		String firstResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY,PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath4);
		String secondResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath4);
		
	    //Changing the series in empty viewboxes through content selector.
		viewerpage.closeWaterMarkIcon(5);
		contentSelector.selectResultFromSeriesTab(5, secondResultDescription);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 05 (TC1474): Checkpoint[5/6]", "Verifying that the study is getting selected through content selector." );
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(secondResultDescription), "Verifying Checkbox appears next to newly displayed  series in viewbox 4", "Verified Checkbox appears next to newly displayed  series in viewbox 4");

		viewerpage.closeWaterMarkIcon(6);
		contentSelector.selectResultFromSeriesTab(6, firstResultDescription);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 06 (TC1474): Checkpoint[6/6]", "Verifying that the study is getting selected through content selector." );
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstResultDescription), "Verifying Checkbox appears next to newly displayed  series in viewbox 3", "Verified Checkbox appears next to newly displayed  series in viewbox 3");

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE349"})
	public void test02_DE349_TC1475_ProgressiveJPGfilesareimportedCorrectlyBoneAge() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Progressive JPG 1.2 files are imported BoneAge");
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(boneAgeDE349patientName, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneAgeDE349patientName+" in viewer" );

		//Scrolling to see if "atlas+1" is getting rendered properly.
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 5);
		viewerpage.waitForAllChangesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 01 (TC1475): Checkpoint[1/2]", "Verifying that the study is getting rendered properly." );

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Verify that Series is present after scrolling.", "DE349_TC1475_Checkpoint_ForAtlas+1" + boneAgeDE349patientName);

		//Scrolling to see if "atlas-1" is getting rendered properly.
//		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 5);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 02 (TC1475): Checkpoint[1/2]", "Verifying that the study is getting rendered properly." );
//
//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Verify that Series is present after scrolling.", "DE349_TC1475_Checkpoint_ForAtlas-1" + boneAgeDE349patientName);

	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE349"})
	public void test03_DE349_TC1477_ProgressiveJPGfilesareimportedCorrectlyPicline() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Progressive JPG 1.2 files are imported incorrectly-Picline");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+piclinePatientName+" in viewer" );
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(piclinePatientName,1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Test 01 (TC1477): Checkpoint[1/1]", "Verifying that the study is getting rendered properly." );
		viewerpage.compareElementImage("RenderingJpgTest", viewerpage.mainViewer, "Verify that Series are rendered on Viewer's page.", "DE349_TC1477Checkpoint1" + boneAgeDE349patientName);

	}


	@Test(groups ={"IE11","Chrome","Edge","US1264","Positive"})
	public void test04_US1264_TC7292_TC7294_TC7295_verifyImagesAndDICOMLoadedSuccessfully() throws InterruptedException, SQLException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the ConfigSettings table stores the value for the NSDataPath location after NS is installed and Filestream support tables are removed in the DB"
				+ "<br> Verify the NS is able to store Non-Dicom data (jpg, png,bmp)  in the NSDataPath after import"
				+ "<br> Verify the NS is able to store Dicom Data  in the NSDataPath after import");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatient+" in viewer" );
		
		patientPage = new PatientListPage(driver);		
		helper = new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(anonymousPatient, 1);
		viewerpage.clearConsoleLogs();
		layout=new ViewerLayout(driver);
		
		db = new DatabaseMethods(driver);
		
		String dataFolder = db.getValueFromConfigSettings(ViewerPageConstants.NS_IMAGE_DATA_FOLDER);
		db.assertEquals(dataFolder, Configurations.TEST_PROPERTIES.get("northstarImageFolder"), "Checkpoint[1/5]", "Verifying the image folder path");
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		for(int viewbox =1;viewbox<=viewerpage.getNumberOfCanvasForLayout();viewbox++)
			viewerpage.assertTrue(Integer.parseInt(viewerpage.getAttributeValue(viewerpage.getViewerCanvas(viewbox), NSGenericConstants.WIDTH))>0,"Checkpoint["+viewbox+"/5]","Verifying all the viewbox which contains JPG/BMP/PNG/DICOM is loaded successfully");
		
		viewerpage.closingConflictMsg();
		
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(),"Checkpoint[5/5]","Verifying no errors are reported in console");
		
		
			
		
		
	
	}
	
}
