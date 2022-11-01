package com.trn.ns.test.viewer.synchronization;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Regression Document - Functional.NS.I15 Synchronization-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class WindowLevelSyncTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	private ViewerOrientation orientation;
	private ViewBoxToolPanel presetMenu;

	//Get Patient Name
	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	//Get Patient Name
	String filePath1 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
	String subject60Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	//Get Patient Name
	String filePath2 = Configurations.TEST_PROPERTIES.get("CR_Thorax_Chest_filepath");
	String crThoraxPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	//Get Patient Name
	String filePath3 = Configurations.TEST_PROPERTIES.get("XA_2_filepath");
	String xapatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	//Get Patient Name
	String filePath4 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String filePath5 = Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String doeLillypatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);

	String filePath6 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);

	String filePath7 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);

	String filePath8 = Configurations.TEST_PROPERTIES.get("TEST^Modality_LUT_filepath");
	String testModalityPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath8);

	String lastSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY,filePath8);
	private ContentSelector contentSelector;
	private HelperClass helper;
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","US244","US289"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_wwSync"})
	public void test01_US244_TC740_TC742_US289_TC2374_TC2416_TC2602_verifyRelativeWWWLSync(String patientFilepath,String whichStudy) throws InterruptedException, SQLException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US244_TC740_TC742_TC627_TC635 : 1. Verify by default WW/WL synchronization is on and relative is configured in db <br> 2. Verify WW/WL synchronization is off after clicking on space bar and relative is configured in db.<br> Verify that by default sync is on. \n Verify the WW/WL behavior with sync off"
				+ "<br>Verify scroll synchronization with series having same FrameReferenceUID and same Orientation"
				+ "<br>Verify Sync ON/OFF for WWWL."
				+ "<br>Verify WL synchronization for series with same bit and Photometric Interpretation");

		String filePath = Configurations.TEST_PROPERTIES.get(patientFilepath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		LoginPage loginPage=new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientName);		
		patientPage.clickOnStudy(Integer.parseInt(whichStudy));		


		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, username, password, 1);
		

		DatabaseMethods db = new DatabaseMethods(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC23[1] & Checkpoint TC21[1] & Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//verifying the By default sync is set to Relative
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "To Check the default sync level is set to RELATIVE");
		viewerpage.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"verifying the sync level","verified - sync level is set to RELATIVE");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC21[2] & Checkpoint TC15[4]", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(2));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC21[3] & Checkpoint TC15[5]:", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling

		Integer vwWW1 = viewerpage.getValueOfWindowWidth(1);
		Integer vwWW2 = viewerpage.getValueOfWindowWidth(2);
		Integer vwWW3 = viewerpage.getValueOfWindowWidth(3);
		Integer vwWW4 = viewerpage.getValueOfWindowWidth(4);

		Integer	vwWC1 = viewerpage.getValueOfWindowCenter(1);
		Integer vwWC2 = viewerpage.getValueOfWindowCenter(2);
		Integer vwWC3 = viewerpage.getValueOfWindowCenter(3);
		Integer vwWC4 = viewerpage.getValueOfWindowCenter(4);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -100);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");


		vwWW1 = viewerpage.getValueOfWindowWidth(1);
		vwWW2 = viewerpage.getValueOfWindowWidth(2);
		vwWW3 = viewerpage.getValueOfWindowWidth(3);
		vwWW4 = viewerpage.getValueOfWindowWidth(4);

		vwWC1 = viewerpage.getValueOfWindowCenter(1);
		vwWC2 = viewerpage.getValueOfWindowCenter(2);
		vwWC3 = viewerpage.getValueOfWindowCenter(3);
		vwWC4 = viewerpage.getValueOfWindowCenter(4);

		// sync off and performing the window leveling on second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC23[2]", "Performing the sync OFF");		
		viewerpage.performSyncONorOFF();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC23[3]", "Performing the WW/WL on second viewbox");

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -100);



		int widthAfterSync = viewerpage.getValueOfWindowWidth(2);
		int windowCenterAfterSync = viewerpage.getValueOfWindowCenter(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC23[4]", "Verifying SYNC is OFF on click of spacebar and performing the window leveling");

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1), vwWW1, "Checkpoint TC23[4]:Verify the width of viewbox1", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox1="+viewerpage.getValueOfWindowWidth(1)+"- verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2), widthAfterSync,  "Checkpoint TC23[4]:Verify the width of viewbox2", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3), vwWW3, "Checkpoint TC23[4]:Verify the width of viewbox3", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4), vwWW4, "Checkpoint TC23[4]:Verify the width of viewbox4", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , vwWC1, "Checkpoint TC23[4]:Verify the window center of viewbox1", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox1="+viewerpage.getValueOfWindowCenter(1)+"- verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterAfterSync, "Checkpoint TC23[4]:Verify the window center of viewbox2", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals( viewerpage.getValueOfWindowCenter(3), vwWC3, "Checkpoint TC23[4]:Verify the window center of viewbox3", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , vwWC4, "Checkpoint TC23[4]:Verify the window center of viewbox4", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");

		// performing the sync and performing the WW/WL on second viewbox

		vwWW1 = viewerpage.getValueOfWindowWidth(1);
		vwWW2 = viewerpage.getValueOfWindowWidth(2);
		vwWW3 = viewerpage.getValueOfWindowWidth(3);
		vwWW4 = viewerpage.getValueOfWindowWidth(4);

		vwWC1 = viewerpage.getValueOfWindowCenter(1);
		vwWC2 = viewerpage.getValueOfWindowCenter(2);
		vwWC3 = viewerpage.getValueOfWindowCenter(3);
		vwWC4 = viewerpage.getValueOfWindowCenter(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is ON on click of spacebar and performing the window leveling on second viewbox for RELATIVE check");

		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -200);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");

	}

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","US244","US289"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_wwSync"})
	public void test02_US244_TC739_TC741_US289_TC2374_TC2416_TC2602_verifyAbsoluteWWWLSync(String patientFilepath,String whichStudy) throws InterruptedException, IOException, SQLException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("1. Verify that User is able to configure the sync to ABSOLUTE <BR> 2. Verify WW/WL synchronization is off after clicking on space bar and absolute is configured in db. "
				+ "<br> Verify Sync ON/OFF for WWWL."
				+ "<br>Verify WL synchronization for series with same bit and Photometric Interpretation");

		String filePath = Configurations.TEST_PROPERTIES.get(patientFilepath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		DatabaseMethods db = new DatabaseMethods(driver);

		// Checking the sync level
		//		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"Verifying the By default sync is RELATIVE","verified");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the Sync value to ABSOLUTE", "");
//		if(db.getWWWLDefaultSyncMode().equalsIgnoreCase(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE)) {
			db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);		
			db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE,"verifying the sync is now ABSOLUTE","verified");
			db.resetIISPostDBChanges();
//		}

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );

		patientPage.clickOnPatientRow(patientName);		
		patientPage.clickOnStudy(Integer.parseInt(whichStudy));				

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Checkpoint TC22[1] & Checkpoint TC20[1]:Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC20[2]:", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC20[3]", "Performing the WW/WL on viewbox1 and checking the WW/WL is ABSOLUTE");		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 70, -40);		

		int widthBeforeSync = viewerpage.getValueOfWindowWidth(1);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeSync, "Checkpoint TC20[3]:Verifying the Window width for viewbox1", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , widthBeforeSync, "Checkpoint TC20[3]:Verifying the Window width for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , widthBeforeSync, "Checkpoint TC20[3]:Verifying the Window width for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) ,  widthBeforeSync,"Checkpoint TC20[3]:Verifying the Window width for viewbox4", "verified");


		int windowCenterBeforeSync = viewerpage.getValueOfWindowCenter(1);

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeSync, "Checkpoint TC20[3]:Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterBeforeSync, "Checkpoint TC20[3]:Verifying the Window center for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , windowCenterBeforeSync, "Checkpoint TC20[3]:Verifying the Window center for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , windowCenterBeforeSync, "Checkpoint TC20[3]:Verifying the Window center for viewbox4", "verified");


		// sync off and performing the window leveling on second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC22[2]", "Performing SYNC OFF and appying WW/WL on viewbox2 and checking the WW/WL is ABSOLUTE and only applied to viewbox2");		
		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(3),0, 0, 100, -200);


		int widthAfterSync = viewerpage.getValueOfWindowWidth(3);
		int windowCenterAfterSync = viewerpage.getValueOfWindowCenter(3);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeSync,"Checkpoint TC22[4]:Verifying the Window width for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , widthAfterSync, "Checkpoint TC22[4]:Verifying the Window width for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , widthBeforeSync,"Checkpoint TC22[4]:Verifying the Window width for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) , widthBeforeSync,"Checkpoint TC22[4]:Verifying the Window width for viewbox4", "verified");



		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeSync, "Checkpoint TC22[4]:Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , windowCenterAfterSync,  "Checkpoint TC22[4]:Verifying the Window center for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterBeforeSync, "Checkpoint TC22[4]:Verifying the Window center for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , windowCenterBeforeSync, "Checkpoint TC22[4]:Verifying the Window center for viewbox4", "verified");

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(4),0, 0, 100, -100);

		// performing the sync and performing the WW/WL on second viewbox

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing SYNC ON again and appying WW/WL on viewbox2 and checking the WW/WL is ABSOLUTE and now change is happening to all synced viewbox");		
		viewerpage.performSyncONorOFF();


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -50);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox4", "verified");	


		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox4", "verified");	


	}

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","US244","US289"})
	public void test07_US244_TC751_US289_TC2375_verifyWWWLForDifferentFrameRefUID() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(" Verify WW/WL synchronization behavior when FrameReferenceUID is different"
				+ "<br> Verify scroll synchronization with series having different FrameReferenceUID and same Orientation.");

		DatabaseMethods db = new DatabaseMethods(driver);
		String val = db.getWWWLDefaultSyncMode().trim();
		db.assertTrue(val.equals(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE),"verifying the WW/WL","verified");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		patientPage.clickOnPatientRow(ah4_patientName);
		patientPage.clickOnStudy(1);

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");


		Integer viewbox1_width = viewerpage.getValueOfWindowWidth(1);
		Integer viewbox1_windowCenter =viewerpage.getValueOfWindowCenter(1);

		Integer viewbox2_width = viewerpage.getValueOfWindowWidth(2);
		Integer viewbox2_windowCenter = viewerpage.getValueOfWindowCenter(2);	

		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the WW/WL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected.");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, 100, -50);

		int delta = viewerpage.getValueOfWindowWidth(1) - viewbox1_width;
		int centerDelta =viewerpage.getValueOfWindowCenter(1) -viewbox1_windowCenter;

		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1), viewbox1_width, "verifying the WW/WL in viewbox1", "verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(2) , viewbox2_width, "verifying the WW/WL in viewbox1", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , viewbox2_width+delta, "verifying the WW/WL in viewbox1", "verified");


		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , viewbox1_windowCenter, "verifying the WW/WL(center) in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2), viewbox2_windowCenter+centerDelta, "verifying the WW/WL(center) in viewbox1", "verified");


	}

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","US70"})
	public void test08_US70_TC147_TC148_verifyBrightnessAndContrastOnMouseMove() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC147:WW/WL for CT Series using Mouse input--Brightness <br> TC148:WW/WL for CT Series using Mouse input--Contrast");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		DatabaseMethods db = new DatabaseMethods(driver);
		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE,"verifying the WW/WL","RELATIVE");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+crThoraxPatient+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(crThoraxPatient, 1);

		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");
		viewerpage.mouseHover(viewerpage.getViewPort(1));		
		layout.selectLayout(layout.twoByOneLayoutIcon);

		int viewbox1_width = viewerpage.getValueOfWindowWidth(1);
		int viewbox1_windowCenter = viewerpage.getValueOfWindowCenter(1);

		int viewbox2_width = viewerpage.getValueOfWindowWidth(2);
		int viewbox2_windowCenter = viewerpage.getValueOfWindowCenter(2);		

		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the WW/WL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected.");
		//Increasing the brightness
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 200, 0);

		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1)  , viewbox1_width, "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(2) , viewbox2_width,"verifying the WW/WL(width) in viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , viewbox1_windowCenter, "verifying the WW/WL(center) in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , viewbox2_windowCenter, "verifying the WW/WL(center) in viewbox2", "verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , viewerpage.getValueOfWindowWidth(2) ,"verifying the since frameReference ID check is not present hence all the series should be in sync", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , viewerpage.getValueOfWindowCenter(2) ,"verifying the since frameReference ID check is not present hence all the series should be in sync", "verified");
		
		viewbox1_width=viewerpage.getValueOfWindowWidth(1);
		viewbox1_windowCenter=viewerpage.getValueOfWindowCenter(1);

		//Increasing only contrast 
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 0, 200);


		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)  , viewbox1_width, "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , viewbox1_width, "verifying the WW/WL(width) in viewbox2", "verified");


		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , viewbox1_windowCenter, "verifying the WW/WL(center) in viewbox1", "verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(2) , viewbox2_windowCenter, "verifying the WW/WL(center) in viewbox2", "verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , viewerpage.getValueOfWindowWidth(2) ,"verifying the since frameReference ID check is not present hence all the series should be in sync", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , viewerpage.getValueOfWindowCenter(2) ,"verifying the since frameReference ID check is not present hence all the series should be in sync", "verified");


	}

	//TC631: verify that WW/WL is not reset on layout change when WW/WL is synced across viewboxes
	@Test(groups ={"Chrome", "Edge", "firefox","US62","DE213","Sanity","BVT"})
	public void test09_US62_DE213_TC631_verifyWWWLNotResetOnLayoutChange() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify that WW/WL is not reset on layout change when WW/WL is synced across viewboxes");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+xapatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(xapatientName, 1);
		layout=new ViewerLayout(driver);

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC16[4]", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));				

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");

		//Capturing default values for window center and window width value
		int windowCenterValue = viewerpage.getValueOfWindowCenter(1);
		int widthBeforeValue = viewerpage.getValueOfWindowWidth(1);

		//Applying the window leveling
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterValue, "Checkpoint TC16[5]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterValue+" - verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeValue, "Checkpoint TC16[5]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+widthBeforeValue+" - verified");

		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected after changing and applying the window leveling in first viewbox");

		int widthBeforeNewLayout = viewerpage.getValueOfWindowWidth(1);

		int windowCenterBeforeNewLayout = viewerpage.getValueOfWindowCenter(1);

		//Applying the window leveling
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeNewLayout, "Checkpoint TC16[6]:Verify the width of viewbox1", "Window Width is for viewbox1 = "+widthBeforeNewLayout+" - verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeNewLayout, "Checkpoint TC16[6]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterBeforeNewLayout+" - verified");		

	}

	//TC633: Verify that user is able to perform scroll and continuous scroll on synced WW/WL viewboxes
	@Test(groups ={"Chrome", "Edge", "firefox","US62","DE213"})
	public void test11_US62_DE213_TC633_verifyScrollOnSyncWWLViewboxes() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify that user is able to perform scroll and continuous scroll on synced WW/WL viewboxes");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC17[4]", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));				

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");

		//Capturing default values for window center and window width value
		int windowCenterValue = viewerpage.getValueOfWindowCenter(1);
		int widthBeforeValue = viewerpage.getValueOfWindowWidth(1);

		//Applying the window leveling
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterValue, "Checkpoint TC17[5]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterValue+" - verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeValue, "Checkpoint TC17[5]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+widthBeforeValue+" - verified");

		String initialScrollValue =  viewerpage.getCurrentScrollPosition(1);

		viewerpage.mouseWheelScrollInViewer(1,"down", 2);
		String newScrollValue = viewerpage.getCurrentScrollPosition(1);
		viewerpage.assertNotEquals( initialScrollValue, newScrollValue, "Checkpoint TC17[6]:Verify the scroll of viewbox1", "Window scroll is for viewbox1 = "+newScrollValue+" - verified");

	}

	//TC634   Verify that user is able to play cine when viewboxes are WW/WL synced
	@Test(groups ={"Chrome", "Edge", "firefox","US62","DE213"})
	public void test12_US62_DE213_TC634_verifyScrollOnSyncWWLViewboxes() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to play cine when viewboxes are WW/WL synced");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC18[4]", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));				

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC18[5]", "Verify WWWL icon is selected and applying the window leveling in first viewbox");

		//Capturing default values for window center and window width value
		int windowCenterValue = viewerpage.getValueOfWindowCenter(1);
		int widthBeforeValue = viewerpage.getValueOfWindowWidth(1);

		//Applying the window leveling
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		int windowCenterAfterWLPerformed = viewerpage.getValueOfWindowCenter(1);
		int widthBeforeValueAfterWLPerformed = viewerpage.getValueOfWindowWidth(1);

		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterValue, "Checkpoint TC18[5]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterValue+" - verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeValue, "Checkpoint TC18[5]:Verify the window center of viewbox1", "Window window center is for viewbox1 = "+widthBeforeValue+" - verified");

		//viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		
		//playing cine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC18[6]", "Verify panned location should not be reset when cine is played.");
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));

		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/goldImages/cineplayImage.png");

		//stopping cine
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/cineplayImage.png");
		int imageAfterCine = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(currentImage, imageAfterCine, "Checkpoint TC18[6]:verifying the cine play is stopped", "cine is stopped and working fine");

		String expectedImagePath = newImagePath+"/goldImages/cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/cineplayImage.png";

		boolean cpStatus =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerpage.assertFalse(cpStatus, "The actual and Expected image are same.","");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Verifying cine is played on WWL images", "Successfully verified checkpoint with image comparison.<br>Image name is cineplayImage.png");

		//performing scroll
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 10, 50);

		//Capturing default values for window center and window width value and verifying that the WWWL retains after Cine
		for(int i = 1; i<5; i++)
		{
			viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(i) , windowCenterAfterWLPerformed, "Verify the window center of viewbox1", "Window window center is for viewbox1 = "+windowCenterValue+" - verified");		
			viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(i) , widthBeforeValueAfterWLPerformed, "Verify the window Width of viewbox1", "Window window Width is for viewbox1 = "+widthBeforeValue+" - verified");
		}

	}

	//TC632   Verify that sync WW/WL works as expected for all types of modalities
	@Test(groups ={"Chrome", "Edge", "firefox","US62","DE213"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test05_VerifySync"})
	public void test13_US62_DE213_TC632_VerifySyncOnDifferentModalities(String patientName) throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify the  function select WWL option to check sync for " + patientName + " data");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		layout=new ViewerLayout(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		
		if(viewerpage.getNumberOfCanvasForLayout()!=4) {
			
			layout.selectLayout(layout.twoByTwoLayoutIcon);
			ContentSelector cs = new ContentSelector(driver);
			cs.selectSeriesFromSeriesTab(4, cs.getAllSeries().get(1));
			viewerpage.pressKey(NSGenericConstants.DOT_KEY);
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");

		// Getting the difference from fifth viewbox since WWWL is going to get applied to 4th viewbox
		int viewbox1_widthDelta = viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(1);
		int viewbox1_windowCenterDelta=viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(1);

		int viewbox2_widthDelta = viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(2);
		int viewbox2_windowCenterDelta=viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(2);

		int viewbox3_widthDelta = viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(3);
		int viewbox3_windowCenterDelta=viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(3);

		int viewbox4_widthDelta = viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(4);
		int viewbox4_windowCenterDelta=viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the window leveling from context Menu on window" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);;


		int newWindowWidth_viewbox1 =  viewerpage.getValueOfWindowWidth(1);
		int newWindowCenter_Viewbox1 = viewerpage.getValueOfWindowCenter(1);

		int viewbox1_widthDelta_afterWWWL =newWindowWidth_viewbox1 - viewerpage.getValueOfWindowWidth(1);
		int viewbox1_windowCenterDelta_afterWWWL=newWindowCenter_Viewbox1 - viewerpage.getValueOfWindowCenter(1);

		int viewbox2_widthDelta_afterWWWL = newWindowWidth_viewbox1 - viewerpage.getValueOfWindowWidth(2);
		int viewbox2_windowCenterDelta_afterWWWL=newWindowCenter_Viewbox1- viewerpage.getValueOfWindowCenter(2);

		int viewbox3_widthDelta_afterWWWL = newWindowWidth_viewbox1 - viewerpage.getValueOfWindowWidth(3);
		int viewbox3_windowCenterDelta_afterWWWL=newWindowCenter_Viewbox1- viewerpage.getValueOfWindowCenter(3);

		int viewbox4_widthDelta_afterWWWL = newWindowWidth_viewbox1 - viewerpage.getValueOfWindowWidth(4);
		int viewbox4_windowCenterDelta_afterWWWL=newWindowCenter_Viewbox1-viewerpage.getValueOfWindowCenter(4);

		viewerpage.assertTrue((viewbox1_widthDelta_afterWWWL - viewbox1_widthDelta)<1, "Verifying the WW/WL(width) is changed based on delta on viewbox1", "verified");
		viewerpage.assertTrue((viewbox2_widthDelta_afterWWWL - viewbox2_widthDelta)<1, "Verifying the WW/WL is changed based on delta on viewbox2", "verified");
		viewerpage.assertTrue((viewbox3_widthDelta_afterWWWL - viewbox3_widthDelta)<1, "Verifying the WW/WL is changed based on delta on viewbox3", "verified");
		viewerpage.assertTrue((viewbox4_widthDelta_afterWWWL - viewbox4_widthDelta)<1, "Verifying the WW/WL is changed based on delta on viewbox4", "verified");

		viewerpage.assertTrue((viewbox1_windowCenterDelta_afterWWWL - viewbox1_windowCenterDelta)<1, "Verifying the WW/WL(Center) is changed based on delta on viewbox1", "verified");
		viewerpage.assertTrue((viewbox2_windowCenterDelta_afterWWWL - viewbox2_windowCenterDelta)<1, "Verifying the WW/WL is changed based on delta on viewbox2", "verified");
		viewerpage.assertTrue((viewbox3_windowCenterDelta_afterWWWL - viewbox3_windowCenterDelta)<1, "Verifying the WW/WL is changed based on delta on viewbox3", "verified");
		viewerpage.assertTrue((viewbox4_windowCenterDelta_afterWWWL - viewbox4_windowCenterDelta)<1, "Verifying the WW/WL is changed based on delta on viewbox4", "verified");


	}

	//TC638 Verify that rotation on synced WW/WL viewboxes

	@Test(groups ={"firefox","Chrome","Edge" ,"US62","DE213"})
	public void test14_US62_DE213_TC638_VerifyRotationWithSyncWWL() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that rotation on synced WW/WL viewboxes");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		orientation=new ViewerOrientation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC19[4]", "Performing the right click and performing the window leveling from context Menu on window" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		//Step 2

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/beforeFlipImageL.png");

		//Verify flip for A orientation
		orientation.flipSeries(orientation.getLeftOrientationMarker(1));
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/afterFlipImageL.png");

		String beforeFlipImageL = newImagePath+"/actualImages/beforeFlipImageL.png";
		String afterFlipImageL = newImagePath+"/actualImages/afterFlipImageL.png";
		String diffImagePathL = newImagePath+"/actualImages/FlipImageL.png";

		boolean cpStatusL =  viewerpage.compareimages(beforeFlipImageL, afterFlipImageL, diffImagePathL);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC19[5]", "Verifying 90 degree image flip for L orientation");
		viewerpage.assertFalse(cpStatusL, "Checkpoint TC19[6]:Verify that the actual and Expected image are not same","The actual and Expected image are not same.");

		//Capturing default values for window center and window width value
		int windowCenterValue_beforeWWL = viewerpage.getValueOfWindowCenter(1);
		int widthValue_beforeWWL = viewerpage.getValueOfWindowWidth(1);


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowWidth(1)  , widthValue_beforeWWL, "Checkpoint TC19[7]:verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertNotEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterValue_beforeWWL, "Checkpoint TC19[7]:verifying the WW/WL(center) in viewbox1", "verified");		

	}

	@Test(groups ={"firefox","Chrome" ,"DE66","US2329","US2325","DR2796","Positive","F1090","E2E"})
	public void test15_DE66_TC567_US2329_TC10165_US2325_TC10410_DR2796_TC10786_VerifyWLLChangePersistOnScroll() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Window leveling changes persist on changing image slices using Scroll.<br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane. <br>"+
		"[Risk and Impact]: Verify the TC10165 with quick toolbox options. <br>"+
		"[Risk and Impact]: Verify the TC10165.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(ah4patientName, 1);
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		//Capturing default values for window center and window width value
		int windowCenterValue_beforeWWL = viewerpage.getValueOfWindowCenter(1);
		int widthValue_beforeWWL = viewerpage.getValueOfWindowWidth(1);
		//Change window leveling on ViewBox1
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		int windowCenterValue_afterWWL = viewerpage.getValueOfWindowCenter(1);
		int widthValue_afterWWL = viewerpage.getValueOfWindowWidth(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify WWL changes on scrolling down");
		viewerpage.assertNotEquals(widthValue_afterWWL  , widthValue_beforeWWL, "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertNotEquals(windowCenterValue_afterWWL , windowCenterValue_beforeWWL, "verifying the WW/WL(center) in viewbox1", "verified");	
		//Capturing current image position in ViewBox1
		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
		//Perform Mouse Scroll to change slice on View Box
		viewerpage.mouseWheelScrollInViewer(1,"down", 5);
		viewerpage.waitForEndOfAllAjaxes();	
		int imageAfterScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]", "Verify image slice changes on scrolling down");
		viewerpage.assertNotEquals(currentImage, imageAfterScroll, "Verify image slice changes on scrolling down", "Image slice on viewbox changes from "+currentImage+" to "+imageAfterScroll);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify WWL changes persist on changing silces");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)  , widthValue_afterWWL, "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1), windowCenterValue_afterWWL, "verifying the WW/WL(center) in viewbox1", "verified");	
		////Capturing default values for window center and window width value for ViewBox2
		windowCenterValue_beforeWWL = viewerpage.getValueOfWindowCenter(2);
		widthValue_beforeWWL = viewerpage.getValueOfWindowWidth(2);
		//Capturing current image position in ViewBox2
		currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
		//Perform Mouse Scroll to change slice on View Box
		viewerpage.mouseWheelScrollInViewer(1,"down", 5);
		viewerpage.waitForEndOfAllAjaxes();	
		imageAfterScroll = viewerpage.getCurrentScrollPositionOfViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify image slice changes on scrolling down on ViewBox2");
		viewerpage.assertNotEquals(currentImage, imageAfterScroll, "Verify image slice changes on scrolling down", "Image slice on viewbox changes from "+currentImage+" to "+imageAfterScroll);
	}

	@Test(groups ={"firefox","Chrome","Edge","DE66"})
	public void test16_DE66_TC570_VerifyWLLChangePersistOnLayoutChange() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Window leveling changes persist on changing layout of Viewer");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		//Capturing default values for window center and window width value
		int windowCenterValue_beforeWWL = viewerpage.getValueOfWindowCenter(1);
		int widthValue_beforeWWL = viewerpage.getValueOfWindowWidth(1);
		//Change window leveling on ViewBox1
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -50);
		int windowCenterValue_afterWWL = viewerpage.getValueOfWindowCenter(1);
		int widthValue_afterWWL = viewerpage.getValueOfWindowWidth(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]", "Verify WWL changes on scrolling down");
		viewerpage.assertNotEquals(widthValue_afterWWL  , (widthValue_beforeWWL), "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertNotEquals((windowCenterValue_afterWWL) , (windowCenterValue_beforeWWL), "verifying the WW/WL(center) in viewbox1", "verified");	
		//Change the layout of Screen
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify WWL changes on changing layout");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)  , (widthValue_afterWWL), "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , (windowCenterValue_afterWWL), "verifying the WW/WL(center) in viewbox1", "verified");		
		//Change the layout of Screen
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify WWL changes on changing layout");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)  , (widthValue_afterWWL), "verifying the WW/WL(width) in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , (windowCenterValue_afterWWL), "verifying the WW/WL(center) in viewbox1", "verified");		
	}

	/**
	 * @author santwanab	
	 * @throws IOException 
	 * @throws SQLException 
	 */

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","DE390"})
	public void test18_DE390_TC1506_verifyRelativeWWWLSyncWhenDBConfigCaseInsensitive() throws InterruptedException, IOException, SQLException	{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify that dB configurations are case insensitive");

		DatabaseMethods db = new DatabaseMethods(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the Sync value to relative", "");
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE.toLowerCase());		
		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE.toLowerCase(),"verifying the sync is now ABSOLUTE","verified");
		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//verifying the By default sync is set to Relative
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "To Check the default sync level is set to RELATIVE");
		viewerpage.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_RELATIVE.toLowerCase(),"verifying the sync level","verified - sync level is set to RELATIVE");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(2));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling

		Integer vwWW1 = viewerpage.getValueOfWindowWidth(1);
		Integer vwWW2 = viewerpage.getValueOfWindowWidth(2);
		Integer vwWW3 = viewerpage.getValueOfWindowWidth(3);
		Integer vwWW4 = viewerpage.getValueOfWindowWidth(4);

		Integer	vwWC1 = viewerpage.getValueOfWindowCenter(1);
		Integer vwWC2 = viewerpage.getValueOfWindowCenter(2);
		Integer vwWC3 = viewerpage.getValueOfWindowCenter(3);
		Integer vwWC4 = viewerpage.getValueOfWindowCenter(4);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -100);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");


		vwWW1 = viewerpage.getValueOfWindowWidth(1);
		vwWW2 = viewerpage.getValueOfWindowWidth(2);
		vwWW3 = viewerpage.getValueOfWindowWidth(3);
		vwWW4 = viewerpage.getValueOfWindowWidth(4);

		vwWC1 = viewerpage.getValueOfWindowCenter(1);
		vwWC2 = viewerpage.getValueOfWindowCenter(2);
		vwWC3 = viewerpage.getValueOfWindowCenter(3);
		vwWC4 = viewerpage.getValueOfWindowCenter(4);

		// sync off and performing the window leveling on second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the sync OFF");		
		viewerpage.performSyncONorOFF();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the WW/WL on second viewbox");

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -100);



		int widthAfterSync = viewerpage.getValueOfWindowWidth(2);
		int windowCenterAfterSync = viewerpage.getValueOfWindowCenter(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is OFF on click of spacebar and performing the window leveling");

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1), vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox1="+viewerpage.getValueOfWindowWidth(1)+"- verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2), widthAfterSync,  "Verify the width of viewbox2", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3), vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4), vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox1="+viewerpage.getValueOfWindowCenter(1)+"- verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterAfterSync,  "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals( viewerpage.getValueOfWindowCenter(3), vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");

		// enabling the sync and performing the WW/WL on second viewbox

		int widthDelta = widthAfterSync- vwWW2;
		int windowCenterDelta=windowCenterAfterSync- vwWC2;		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is ON on click of spacebar and performing the window leveling on second viewbox for RELATIVE check");

		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -200);

		viewerpage.assertEquals((viewerpage.getValueOfWindowWidth(2))-viewerpage.getValueOfWindowWidth(1) , widthDelta, "Verifying the Window width is relatively increased in viewbox1", "verified");		
		viewerpage.assertEquals((viewerpage.getValueOfWindowWidth(2))-viewerpage.getValueOfWindowWidth(3) , widthDelta, "Verifying the Window width is relatively increased in viewbox3", "verified");	
		viewerpage.assertEquals((viewerpage.getValueOfWindowWidth(2))-viewerpage.getValueOfWindowWidth(4) , widthDelta, "Verifying the Window width is relatively increased in viewbox4", "verified");	


		viewerpage.assertEquals((viewerpage.getValueOfWindowCenter(2))-viewerpage.getValueOfWindowCenter(1) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox1", "verified");		
		viewerpage.assertEquals((viewerpage.getValueOfWindowCenter(2))-viewerpage.getValueOfWindowCenter(3) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox3", "verified");	
		viewerpage.assertEquals((viewerpage.getValueOfWindowCenter(2))-viewerpage.getValueOfWindowCenter(4) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox4", "verified");	

	}

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","DE390"})
	public void test19_DE390_TC1506_verifyAbsoluteWWWLSyncWhenDBConfigCaseInsensitive() throws InterruptedException, IOException, SQLException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify that dB configurations are case insensitive");
		DatabaseMethods db = new DatabaseMethods(driver);

		// Checking the sync level
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the Sync value to ABSOLUTE", "");
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE.toLowerCase());		
		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE.toLowerCase(),"verifying the sync is now ABSOLUTE","verified");
		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(subject60Patient, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the WW/WL on viewbox1 and checking the WW/WL is ABSOLUTE");		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 100, -200);		

		int widthBeforeSync = viewerpage.getValueOfWindowWidth(1);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeSync, "Verifying the Window width for viewbox1", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , widthBeforeSync, "Verifying the Window width for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , widthBeforeSync, "Verifying the Window width for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) ,  widthBeforeSync,"Verifying the Window width for viewbox4", "verified");


		int windowCenterBeforeSync = viewerpage.getValueOfWindowCenter(1);

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeSync, "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterBeforeSync, "Verifying the Window center for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , windowCenterBeforeSync, "Verifying the Window center for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , windowCenterBeforeSync, "Verifying the Window center for viewbox4", "verified");


		// sync off and performing the window leveling on second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing SYNC OFF and appying WW/WL on viewbox2 and checking the WW/WL is ABSOLUTE and only applied to viewbox2");		
		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(3),0, 0, 100, -200);

		int widthAfterSync = viewerpage.getValueOfWindowWidth(3);
		int windowCenterAfterSync = viewerpage.getValueOfWindowCenter(3);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeSync,"Verifying the Window width for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , widthAfterSync, "Verifying the Window width for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , widthBeforeSync,"Verifying the Window width for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) , widthBeforeSync,"Verifying the Window width for viewbox4", "verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeSync, "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , windowCenterAfterSync,  "Verifying the Window center for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterBeforeSync, "Verifying the Window center for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , windowCenterBeforeSync, "Verifying the Window center for viewbox4", "verified");

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(4),0, 0, 100, -100);

		// enabling the sync and performing the WW/WL on second viewbox

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing SYNC ON again and appying WW/WL on viewbox2 and checking the WW/WL is ABSOLUTE and now change is happening to all synced viewbox");		
		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -50);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox4", "verified");	

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox4", "verified");	

	}

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","DE390"})
	public void test20_DE390_TC1507_verifyRelativeWWWLSyncWhenDBConfigIsBlank() throws InterruptedException, IOException, SQLException	{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WW/WL is relative if no value is present in DB for column 'Value' in ConfigSettings table");

		DatabaseMethods db = new DatabaseMethods(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the Sync value to ABSOLUTE", "");
		db.updateWWWLSyncValue("");		
		db.assertEquals(db.getWWWLDefaultSyncMode(),"","verifying the sync is now blank","verified");
		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
        helper=new HelperClass(driver);	
		viewerpage = helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(2));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling

		Integer vwWW1 = viewerpage.getValueOfWindowWidth(1);
		Integer vwWW2 = viewerpage.getValueOfWindowWidth(2);
		Integer vwWW3 = viewerpage.getValueOfWindowWidth(3);
		Integer vwWW4 = viewerpage.getValueOfWindowWidth(4);

		Integer	vwWC1 = viewerpage.getValueOfWindowCenter(1);
		Integer vwWC2 = viewerpage.getValueOfWindowCenter(2);
		Integer vwWC3 = viewerpage.getValueOfWindowCenter(3);
		Integer vwWC4 = viewerpage.getValueOfWindowCenter(4);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -200);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");


		vwWW1 = viewerpage.getValueOfWindowWidth(1);
		vwWW2 = viewerpage.getValueOfWindowWidth(2);
		vwWW3 = viewerpage.getValueOfWindowWidth(3);
		vwWW4 = viewerpage.getValueOfWindowWidth(4);

		vwWC1 = viewerpage.getValueOfWindowCenter(1);
		vwWC2 = viewerpage.getValueOfWindowCenter(2);
		vwWC3 = viewerpage.getValueOfWindowCenter(3);
		vwWC4 = viewerpage.getValueOfWindowCenter(4);

		// sync off and performing the window leveling on second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the sync OFF");		
		viewerpage.performSyncONorOFF();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the WW/WL on second viewbox");

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -200);

		int widthAfterSync = viewerpage.getValueOfWindowWidth(2);
		int windowCenterAfterSync = viewerpage.getValueOfWindowCenter(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is OFF on click of spacebar and performing the window leveling");

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1), vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox1="+viewerpage.getValueOfWindowWidth(1)+"- verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2), widthAfterSync,  "Verify the width of viewbox2", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3), vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4), vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+widthAfterSync+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox1="+viewerpage.getValueOfWindowCenter(1)+"- verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterAfterSync,  "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals( viewerpage.getValueOfWindowCenter(3), vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+windowCenterAfterSync+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");

		// enabling the sync and performing the WW/WL on second viewbox

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying SYNC is ON on click of spacebar and performing the window leveling on second viewbox for RELATIVE check");
		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -200);
		int widthDelta =viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1);
		int windowCenterDelta=viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1);		

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)-viewerpage.getValueOfWindowWidth(1) , widthDelta, "Verifying the Window width is relatively increased in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)-viewerpage.getValueOfWindowWidth(3) , widthDelta, "Verifying the Window width is relatively increased in viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)-viewerpage.getValueOfWindowWidth(4) , widthDelta, "Verifying the Window width is relatively increased in viewbox4", "verified");	


		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)-viewerpage.getValueOfWindowCenter(1) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)-viewerpage.getValueOfWindowCenter(3) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)-viewerpage.getValueOfWindowCenter(4) , windowCenterDelta, "Verifying the Window center is relatively increased in viewbox4", "verified");	

	}

	//Verify the window leveling on layout change
	@Test(groups ={"IE11","Chrome","firefox", "Chrome","DE90"})
	//Not working on IE11, FireFox 
	public void test21_DE90_TC460_verifyWindowLevelingOnLayoutChange() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the window leveling on layout change");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		//takes value of WC and WW before window-leveling is performed
		int beforeWWValue = viewerpage.getValueOfWindowWidth(1);
		int beforeWCValue = viewerpage.getValueOfWindowCenter(1); 

		//Window-Leveling is performed by right clicking and performing the WWWL icon
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.windowLevelingIcon.isEnabled(),"Verifying WW/WL is performed","WW/WL is performed");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),25, 50, 50, 100);
		viewerpage.waitForViewerpageToLoad();

		//takes value of WC and WW after window-leveling is performed
		int afterWWValue = viewerpage.getValueOfWindowWidth(1);
		int afterWCValue = viewerpage.getValueOfWindowCenter(1);


		///verifying after window-leveling is performed
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that window-leveling is performed" );
		viewerpage.assertNotEquals(beforeWWValue,afterWWValue, "Verifying WW functionality and WW values are changed", "WW is working fine and WW values are changed from"+beforeWWValue+ "to" + afterWWValue);
		viewerpage.assertNotEquals(beforeWCValue,afterWCValue, "Verifying WC functionality and WC values are changed", "WC is working fine and WC values are changed from"+beforeWCValue+ "to" + afterWCValue);

		//change of layout to 1*1
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		//takes value of WC and WW after change of layout
		int afterLayoutChangeWWValue = viewerpage.getValueOfWindowWidth(1);
		int afterLayoutChangeWCValue = viewerpage.getValueOfWindowCenter(1);

		//verifying after change of layout that window leveling is retained
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that window-leveling is reatined after change in layout" );
		viewerpage.assertEquals(afterWWValue,afterLayoutChangeWWValue, "Verifying WW functionality and how it is retained when layout is changed", "WW is working fine and is retained as" + afterLayoutChangeWWValue);
		viewerpage.assertEquals(afterWCValue,afterLayoutChangeWCValue, "Verifying WC functionality and how it is retained when layout is changed", "WC is working fine and is retained as" + afterLayoutChangeWCValue);

		//verifying with the initial values and one after change in layout are not same and hence window leveling is retained
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that window-leveling is still performed wrt before performing the window leveling" );
		viewerpage.assertNotEquals(afterLayoutChangeWWValue,beforeWWValue, "Verifying WW functionality is not same as original values", "WW is working fine and is not same as");
		viewerpage.assertNotEquals(afterLayoutChangeWCValue,beforeWCValue, "Verifying WC functionality is not same as original values", "WC is working fine and is not retained");

	}

	// Refactor Windowing command- Mouse movement on annotation inside viewboxWindowing

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US552"})
	public void test28_US552_TC1611_verifyWLinsideViewbox() throws InterruptedException	{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US552_TC1611 : Refactor Windowing command- Mouse movement on annotation inside viewboxWindowing");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(2).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(2));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling

		Integer vwWW1 = viewerpage.getValueOfWindowWidth(1);
		Integer vwWW2 = viewerpage.getValueOfWindowWidth(2);
		Integer vwWW3 = viewerpage.getValueOfWindowWidth(3);
		Integer vwWW4 = viewerpage.getValueOfWindowWidth(4);

		Integer	vwWC1 = viewerpage.getValueOfWindowCenter(1);
		Integer vwWC2 = viewerpage.getValueOfWindowCenter(2);
		Integer vwWC3 = viewerpage.getValueOfWindowCenter(3);
		Integer vwWC4 = viewerpage.getValueOfWindowCenter(4);

		//viewerpag3

		//viewerpage.get(viewerpage.getZoomLabelOverlay(1));

		//		int x = viewerpage.getValueOfXCoordinate(viewerpage.getViewPort(2));
		//		int y = viewerpage.getValueOfYCoordinate(viewerpage.getViewPort(2));
		//		int width = viewerpage.getHeightOfWebElement(viewerpage.getViewPort(2));
		//		int height =  viewerpage.getWidthOfWebElement(viewerpage.getViewPort(2));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 380, 200);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2)- viewerpage.getValueOfWindowWidth(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");

	}

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US552"})
	public void test29_US552_TC1612_verifyWLAcrossViewbox() throws InterruptedException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US552_TC1612 : Refactor Windowing command- Windowing across viewbox");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getViewPort(1)), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling

		Integer vwWW1 = viewerpage.getValueOfWindowWidth(1);
		Integer vwWW2 = viewerpage.getValueOfWindowWidth(2);
		Integer vwWW3 = viewerpage.getValueOfWindowWidth(3);
		Integer vwWW4 = viewerpage.getValueOfWindowWidth(4);

		Integer	vwWC1 = viewerpage.getValueOfWindowCenter(1);
		Integer vwWC2 = viewerpage.getValueOfWindowCenter(2);
		Integer vwWC3 = viewerpage.getValueOfWindowCenter(3);
		Integer vwWC4 = viewerpage.getValueOfWindowCenter(4);


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 380, 210);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");
	}

//	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US552"})
	public void test29_US552_TC1610_verifyWLOnLeavingBrowser() throws InterruptedException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US552_TC1612 : Refactor Windowing command- Mouse movement leaving the browser");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify WWWL icon is selected and applying the window leveling in first viewbox");
		//Applying the window leveling

		Integer vwWW1 = viewerpage.getValueOfWindowWidth(1);
		Integer vwWW2 = viewerpage.getValueOfWindowWidth(2);
		Integer vwWW3 = viewerpage.getValueOfWindowWidth(3);
		Integer vwWW4 = viewerpage.getValueOfWindowWidth(4);

		Integer	vwWC1 = viewerpage.getValueOfWindowCenter(1);
		Integer vwWC2 = viewerpage.getValueOfWindowCenter(2);
		Integer vwWC3 = viewerpage.getValueOfWindowCenter(3);
		Integer vwWC4 = viewerpage.getValueOfWindowCenter(4);


		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 0 , -300);



		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verifying by default the sync in ON");		

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(1) , vwWW2 - vwWW1, "Verify the width of viewbox1", "Window Width is for viewbox2 = "+vwWW2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(2) , vwWW2 - vwWW2, "Verify the width of viewbox2", "Window Width is for viewbox2 = "+vwWW2+" and viewbox2="+viewerpage.getValueOfWindowWidth(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(3) , vwWW2 - vwWW3, "Verify the width of viewbox3", "Window Width is for viewbox2 = "+vwWW2+" and viewbox3="+viewerpage.getValueOfWindowWidth(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1)- viewerpage.getValueOfWindowWidth(4) , vwWW2 - vwWW4, "Verify the width of viewbox4", "Window Width is for viewbox2 = "+vwWW2+" and viewbox4="+viewerpage.getValueOfWindowWidth(4)+"- verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(1) , vwWC2 - vwWC1, "Verify the window center of viewbox1", "Window window center is for viewbox2 = "+vwWC2+" - verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(2) , vwWC2 - vwWC2, "Verify the window center of viewbox2", "Window window center is for viewbox2 = "+vwWC2+" and viewbox2="+viewerpage.getValueOfWindowCenter(2)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(3) , vwWC2 - vwWC3, "Verify the window center of viewbox3", "Window window center is for viewbox2 = "+vwWC2+" and viewbox3="+viewerpage.getValueOfWindowCenter(3)+"- verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1)- viewerpage.getValueOfWindowCenter(4) , vwWC2 - vwWC4, "Verify the window center of viewbox4", "Window window center is for viewbox2 = "+vwWC2+" and viewbox4="+viewerpage.getValueOfWindowCenter(4)+"- verified");
	}

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US217","US741","Sanity","BVT"})
	public void test30_US217_TC2203_US741_TC2603_verifyWindowLevelingNotApplyingOnRGBData() throws InterruptedException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US217_TC2203 : Verify that Window level should not apply on RGB data using any events like mouse or keyboard events"
				+ "Verify WL Synchronization for series having same bit but different Photometric interpretation");
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		 helper=new HelperClass(driver);	
		viewerpage = helper.loadViewerDirectly(doeLillypatientName, 1);
		viewerpage.waitForPdfToRenderInViewbox(3);

		//variable to store current value of W and C
		int beforeWW1 = viewerpage.getValueOfWindowWidth(1);
		int beforeWW2 = viewerpage.getValueOfWindowWidth(2);
		int beforeWW5 = viewerpage.getValueOfWindowWidth(5);

		int	beforeWC1 = viewerpage.getValueOfWindowCenter(1);
		int beforeWC2 = viewerpage.getValueOfWindowCenter(2);
		int beforeWC5 = viewerpage.getValueOfWindowCenter(5);

		// Enable the Window level icon 
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 50 , -50);

		//variable to store current value of W and C
		int afterWW1 = viewerpage.getValueOfWindowWidth(1);
		int afterWW2 = viewerpage.getValueOfWindowWidth(2);
		int afterWW5 = viewerpage.getValueOfWindowWidth(5);

		int	afterWC1 = viewerpage.getValueOfWindowCenter(1);
		int afterWC2 = viewerpage.getValueOfWindowCenter(2);
		int afterWC5 = viewerpage.getValueOfWindowCenter(5);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify series in Viewbox 1 and 2 are synchronised as thay have bit and PI");
		viewerpage.assertNotEquals(beforeWW1,afterWW1 , "Verifying window width is  changed in viewbox 1", "Verified window width is  changed in viewbox 1");
		viewerpage.assertNotEquals(beforeWC1,afterWC1, "Verifying window center is  changed in viewbox 1", "Verified window center is  changed in viewbox 1");
		viewerpage.assertNotEquals(beforeWW2,afterWW2 , "Verifying window width is  changed in viewbox 2", "Verified window width is  changed in viewbox 2");
		viewerpage.assertNotEquals(beforeWC2,afterWC2, "Verifying window center is  changed in viewbox 2", "Verified window center is  changed in viewbox 2");
		viewerpage.assertEquals(afterWW1,afterWW2, "Verifying window width level is same in both viewbox", "Verified window width level is same in both viewbox");
		viewerpage.assertEquals(afterWC1,afterWC2, "Verifying window center level is same in both viewbox", "Verified window center level is same in both viewbox");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify there is no visual change on RGB image on Window leveling");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Checkpoint1 : Verified there is no visual change on RGB image on Window leveling","TC2203" + "_" + "Checkpoint1");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2), "Checkpoint1 : Verified there is no visual change on RGB image on Window leveling","TC2203" + "_" + "Checkpoint2");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify series in Viewbox 2 and 5 are not synchronised as thay have different PI");
		viewerpage.assertEquals(beforeWW5,afterWW5, "Verifying window width level remains same on viewbox5 on applying WL on viewbox1", "Verifying window width level is same in viewbox5 on applying WL in viewbox1");
		viewerpage.assertEquals(beforeWC5,afterWC5, "Verifying window center level remains same on viewbox5 on applying WL on viewbox1", "Verifying window center level is same in viewbox5 on applying WL in viewbox1");
		viewerpage.assertNotEquals(afterWW1,afterWW5, "Verifying window width level is different in both viewbox", "Verified window width level is different in both viewbox");
		viewerpage.assertNotEquals(afterWC1,afterWC5, "Verifying window center level is different in both viewbox", "Verified window center level is different in both viewbox");

	
	}

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US374"})
	public void test32_US374_TC2071_verifyValueOfWLAndWCDisplayedCorrectly() throws InterruptedException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US374_TC2071 : Verify that value of WL/WC should be displayed correctly");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		//Capturing before values for WWWL
		Integer beforeWWValue = viewerpage.getValueOfWindowWidth(1);
		Integer	beforeWCValue = viewerpage.getValueOfWindowCenter(1);


		// Enable the Window level icon 
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 50 , 50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verified image rotation and it works fine after cine/scroll");
		viewerpage.assertNotEquals(beforeWWValue, viewerpage.getValueOfWindowWidth(1), "Verifying that value of WW should be displayed correctly", "Verified window width is changed in viewbox 1");
		viewerpage.assertNotEquals(beforeWCValue, viewerpage.getValueOfWindowWidth(1), "Verifying that value of WC should be displayed correctly", "Verified window center is changed in viewbox 1");

	}

	@Test(groups ={"Chrome", "Edge","firefox","dbConfig","US289"})
	public void test33_US289_TC2376_verifyWWWLSyncForDiffOrientation() throws InterruptedException, IOException, SQLException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization with series having different Orientation");

		String filePath = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		DatabaseMethods db = new DatabaseMethods(driver);

		// Checking the sync level
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the Sync value to ABSOLUTE", "");
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);		
		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE,"verifying the sync is now ABSOLUTE","verified");
		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+patientName+"in viewer" );
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(patientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and performing the WWWL from Radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the WW/WL on viewbox1 and checking the WW/WL is ABSOLUTE");	


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 70, -40);		

		int widthBeforeSync = viewerpage.getValueOfWindowWidth(1);
		int windowCenterBeforeSync = viewerpage.getValueOfWindowCenter(1);


		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeSync, "Verifying the Window width for viewbox1", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , widthBeforeSync, "Verifying the Window width for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , widthBeforeSync, "Verifying the Window width for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) ,  widthBeforeSync,"Verifying the Window width for viewbox4", "verified");

		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeSync, "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterBeforeSync, "Verifying the Window center for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , windowCenterBeforeSync, "Verifying the Window center for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , windowCenterBeforeSync, "Verifying the Window center for viewbox4", "verified");


		// sync off and performing the window leveling on second viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing SYNC OFF and appying WW/WL on viewbox2 and checking the WW/WL is ABSOLUTE and only applied to viewbox2");		
		viewerpage.performSyncONorOFF();

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(3),0, 0, 100, -200);


		int widthAfterSync = viewerpage.getValueOfWindowWidth(3);
		int windowCenterAfterSync = viewerpage.getValueOfWindowCenter(3);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , widthBeforeSync,"Verifying the Window width for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , widthAfterSync, "Verifying the Window width for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2) , widthBeforeSync,"Verifying the Window width for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) , widthBeforeSync,"Verifying the Window width for viewbox4", "verified");



		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , windowCenterBeforeSync, "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , windowCenterAfterSync,  "Verifying the Window center for viewbox2", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2) , windowCenterBeforeSync, "Verifying the Window center for viewbox3", "verified");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , windowCenterBeforeSync, "Verifying the Window center for viewbox4", "verified");

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(4),0, 0, 100, -100);

		// performing the sync and performing the WW/WL on second viewbox

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing SYNC ON again and appying WW/WL on viewbox2 and checking the WW/WL is ABSOLUTE and now change is happening to all synced viewbox");		
		viewerpage.performSyncONorOFF();


		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -50);

		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4) , viewerpage.getValueOfWindowWidth(2), "Verifying the Window width for viewbox4", "verified");	


		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox1", "verified");		
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox3", "verified");	
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4) , viewerpage.getValueOfWindowCenter(2), "Verifying the Window center for viewbox4", "verified");	


	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US289"})
	public void test34_US289_TC2377_verifyWWWLForNonDICOM() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Dicom and Non-Dicom are not in sync if FrameReferenceUID is made same.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneagePatientName+" in viewer" );
		helper = new HelperClass(driver);		
		viewerpage = helper.loadViewerPageUsingSearch(boneagePatientName,  1, 2);	
		
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		//		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WWWL from radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),0, 0, 200, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should WWWL synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WWWL synchronously.","test34_checkpoint1");

		DatabaseMethods db = new DatabaseMethods(driver);
		HashMap<String, String> uid = db.getFrameReferenceUIDOfPatient(boneagePatientName);
		List<String> keys =  new ArrayList<>(uid.keySet());
		for(int i=0;i<keys.size();i++)
			db.updateFrameReferenceUIDOfPatient(Integer.parseInt(keys.get(i)),"1");

		helper.browserBackAndReloadViewer(boneagePatientName,  1, 2);	
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//Selecting minimum annotation level
		//		viewerpage.selectAnnotation(ViewerPageConstants.MINIMUM_ANNOTATION);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the right click and enabling the WWWL from radial Menu" );
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(2),0, 0, 200, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify images should WWWL synchronously.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should WWWL synchronously.","test34_checkpoint1");



	}

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US741","Sanity","BVT"})
	public void test35_US741_TC2604_verifyWLSyncForSeriesWithDifferentBitandSamePI() throws InterruptedException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WL Synchronization for Series having different bit but same Photometric interpretation.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+testModalityPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(testModalityPatientName, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//variable to store current value of W and C
		int beforeWW1 = viewerpage.getValueOfWindowWidth(1);
		int beforeWW2 = viewerpage.getValueOfWindowWidth(2);
		int beforeWW3 = viewerpage.getValueOfWindowWidth(3);
		int beforeWW4 = viewerpage.getValueOfWindowWidth(4);

		int	beforeWC1 = viewerpage.getValueOfWindowCenter(1);
		int beforeWC2 = viewerpage.getValueOfWindowCenter(2);
		int beforeWC3 = viewerpage.getValueOfWindowCenter(3);
		int beforeWC4 = viewerpage.getValueOfWindowCenter(4);

		// Enable the Window level icon 
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 50 , 50);

		//variable to store current value of W and C
		int afterWW1 = viewerpage.getValueOfWindowWidth(1);
		int afterWW2 = viewerpage.getValueOfWindowWidth(2);
		int afterWW3 = viewerpage.getValueOfWindowWidth(3);
		int afterWW4 = viewerpage.getValueOfWindowWidth(4);

		int	afterWC1 = viewerpage.getValueOfWindowCenter(1);
		int afterWC2 = viewerpage.getValueOfWindowCenter(2);
		int afterWC3 = viewerpage.getValueOfWindowCenter(3);
		int afterWC4 = viewerpage.getValueOfWindowCenter(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify series in Viewbox 2,3 and 4 are not synchronised with Series in viewbox1 as they have different bit used.");
		viewerpage.assertNotEquals(beforeWW1,afterWW1 , "Verifying window width is  changed in viewbox 1", "Verified window width is  changed in viewbox 1");
		viewerpage.assertNotEquals(beforeWC1,afterWC1, "Verifying window center is  changed in viewbox 1", "Verified window center is  changed in viewbox 1");
		viewerpage.assertEquals(beforeWW2,afterWW2 , "Verifying window width remains same in viewbox 2", "Verified window width remains same in viewbox 2");
		viewerpage.assertEquals(beforeWC2,afterWC2, "Verifying window center remains samein viewbox 2", "Verified window center remains samein viewbox 2");
		viewerpage.assertEquals(beforeWW3,afterWW3, "Verifying window width remains same in viewbox 3", "Verified window width remains same in viewbox 3");
		viewerpage.assertEquals(beforeWC3,afterWC3, "Verifying window center remains same in viewbox 3", "Verifying window center remains same in viewbox 3");
		viewerpage.assertEquals(beforeWW4,afterWW4, "Verifying window width remains same in viewbox 4", "Verified window width remains same in viewbox 4");
		viewerpage.assertEquals(beforeWC4,afterWC4, "Verifying window center remains same in viewbox 4", "Verifying window center remains same in viewbox 4");

	}

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US741","BVT"})
	public void test36_US741_TC2604_verifyWLSyncForSeriesWithDifferentBitandSamePIWithFlagSetAbsolute() throws InterruptedException, IOException, SQLException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WL Synchronization for Series having different bit but same Photometric interpretation when WL sync is set to Absolute.");

		//update window level sync to Absolute
		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE);		
		db.assertEquals(db.getWWWLDefaultSyncMode(),NSDBDatabaseConstants.WWWL_SYNC_ABSOLUTE,"verifying the sync is now ABSOLUTE","verified");
		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+testModalityPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(testModalityPatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//variable to store current value of W and C
		int beforeWW1 = viewerpage.getValueOfWindowWidth(1);
		int beforeWW2 = viewerpage.getValueOfWindowWidth(2);
		int beforeWW3 = viewerpage.getValueOfWindowWidth(3);
		int beforeWW4 = viewerpage.getValueOfWindowWidth(4);

		int	beforeWC1 = viewerpage.getValueOfWindowCenter(1);
		int beforeWC2 = viewerpage.getValueOfWindowCenter(2);
		int beforeWC3 = viewerpage.getValueOfWindowCenter(3);
		int beforeWC4 = viewerpage.getValueOfWindowCenter(4);

		// Enable the Window level icon 
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 50 , 50);

		//variable to store current value of W and C
		int afterWW1 = viewerpage.getValueOfWindowWidth(1);
		int afterWW2 = viewerpage.getValueOfWindowWidth(2);
		int afterWW3 = viewerpage.getValueOfWindowWidth(3);
		int afterWW4 = viewerpage.getValueOfWindowWidth(4);

		int	afterWC1 = viewerpage.getValueOfWindowCenter(1);
		int afterWC2 = viewerpage.getValueOfWindowCenter(2);
		int afterWC3 = viewerpage.getValueOfWindowCenter(3);
		int afterWC4 = viewerpage.getValueOfWindowCenter(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify series in Viewbox 2,3 and 4 are not synchronised with Series in viewbox1 as they have different bit used.");
		viewerpage.assertNotEquals(beforeWW1,afterWW1 , "Verifying window width is  changed in viewbox 1", "Verified window width is  changed in viewbox 1");
		viewerpage.assertNotEquals(beforeWC1,afterWC1, "Verifying window center is  changed in viewbox 1", "Verified window center is  changed in viewbox 1");
		viewerpage.assertEquals(beforeWW2,afterWW2 , "Verifying window width remains same in viewbox 2", "Verified window width remains same in viewbox 2");
		viewerpage.assertEquals(beforeWC2,afterWC2, "Verifying window center remains samein viewbox 2", "Verified window center remains samein viewbox 2");
		viewerpage.assertEquals(beforeWW3,afterWW3, "Verifying window width remains same in viewbox 3", "Verified window width remains same in viewbox 3");
		viewerpage.assertEquals(beforeWC3,afterWC3, "Verifying window center remains same in viewbox 3", "Verifying window center remains same in viewbox 3");
		viewerpage.assertEquals(beforeWW4,afterWW4, "Verifying window width remains same in viewbox 4", "Verified window width remains same in viewbox 4");
		viewerpage.assertEquals(beforeWC4,afterWC4, "Verifying window center remains same in viewbox 4", "Verifying window center remains same in viewbox 4");

	}

	@Test(groups ={"Chrome", "Edge","firefox", "IE11","US741"})
	public void test37_US741_TC2605_verifyWLSyncForSeriesWithDifferentBitandDifferentPI() throws InterruptedException, AWTException	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify WL Synchronization for Series having different bit but same Photometric interpretation.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+testModalityPatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);	
		viewerpage = helper.loadViewerDirectly(testModalityPatientName, 1);
		contentSelector = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		//select last series in Content selector in viewbox2s
		contentSelector.selectSeriesFromSeriesTab(2,lastSeriesDescription);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify series selected via content selector is rendered on viewbox");
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(lastSeriesDescription), "Verifying Checkbox appears next to newly selected series in viewbox2", "Verified Checkbox appears next to newly displayed series in viewbox2");

		//variable to store current value of W and C
		int beforeWW1 = viewerpage.getValueOfWindowWidth(1);
		int beforeWW2 = viewerpage.getValueOfWindowWidth(2);
		int beforeWW3 = viewerpage.getValueOfWindowWidth(3);
		int beforeWW4 = viewerpage.getValueOfWindowWidth(4);

		int	beforeWC1 = viewerpage.getValueOfWindowCenter(1);
		int beforeWC2 = viewerpage.getValueOfWindowCenter(2);
		int beforeWC3 = viewerpage.getValueOfWindowCenter(3);
		int beforeWC4 = viewerpage.getValueOfWindowCenter(4);

		// Enable the Window level icon 
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));		
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 50 , 50);

		//variable to store current value of W and C
		int afterWW1 = viewerpage.getValueOfWindowWidth(1);
		int afterWW2 = viewerpage.getValueOfWindowWidth(2);
		int afterWW3 = viewerpage.getValueOfWindowWidth(3);
		int afterWW4 = viewerpage.getValueOfWindowWidth(4);

		int	afterWC1 = viewerpage.getValueOfWindowCenter(1);
		int afterWC2 = viewerpage.getValueOfWindowCenter(2);
		int afterWC3 = viewerpage.getValueOfWindowCenter(3);
		int afterWC4 = viewerpage.getValueOfWindowCenter(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify series in Viewbox 2,3 and 4 are not synchronised with Series in viewbox1 as they have different bit and PI.");
		viewerpage.assertNotEquals(beforeWW1,afterWW1 , "Verifying window width is  changed in viewbox 1", "Verified window width is  changed in viewbox 1");
		viewerpage.assertNotEquals(beforeWC1,afterWC1, "Verifying window center is  changed in viewbox 1", "Verified window center is  changed in viewbox 1");
		viewerpage.assertEquals(beforeWW2,afterWW2 , "Verifying window width remains same in viewbox 2", "Verified window width remains same in viewbox 2");
		viewerpage.assertEquals(beforeWC2,afterWC2, "Verifying window center remains samein viewbox 2", "Verified window center remains samein viewbox 2");
		viewerpage.assertEquals(beforeWW3,afterWW3, "Verifying window width remains same in viewbox 3", "Verified window width remains same in viewbox 3");
		viewerpage.assertEquals(beforeWC3,afterWC3, "Verifying window center remains same in viewbox 3", "Verifying window center remains same in viewbox 3");
		viewerpage.assertEquals(beforeWW4,afterWW4, "Verifying window width remains same in viewbox 4", "Verified window width remains same in viewbox 4");
		viewerpage.assertEquals(beforeWC4,afterWC4, "Verifying window center remains same in viewbox 4", "Verifying window center remains same in viewbox 4");

	}

	@AfterMethod(alwaysRun=true)
	public void resetDatabase() throws IOException, InterruptedException, SQLException {
		DatabaseMethods  db = new DatabaseMethods(driver);

		if(!db.getWWWLDefaultSyncMode().equalsIgnoreCase(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE)) {
			db.updateWWWLSyncValue(NSDBDatabaseConstants.WWWL_SYNC_RELATIVE);
			db.resetIISPostDBChanges();
		}
	}


	// functions 

	public void selectAndVerifyAlloptionOfPresetWithSyncOn(List<String> wlPresetMenuOptions, WebElement windowWidthCenterLabelOverlay, int viewNum) throws TimeoutException, InterruptedException {
		//Step 1 - selecting each value in PRESET context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6] -Checkpoint TC2[1] & Checkpoint TC2[3] & Checkpoint TC2[6]", "Verify the WW and WC values after selecting each value in PRESET context menu when sync is ON");

		presetMenu=new ViewBoxToolPanel(driver);
		for(int i = 1; i < wlPresetMenuOptions.size()-1 ;i++){	
			viewerpage.assertTrue(presetMenu.verifyWWWCValuesFromPresetContextMenuOption(viewNum, wlPresetMenuOptions.get(i)),"Verify the WW and WC values with Preset menu for "+wlPresetMenuOptions.get(i)+" option","WW and WC values are same as selected");
			for(int j = 1; j <= viewerpage.getNumberOfCanvasForLayout(); j++) {
				if(j!= viewNum) {
					viewerpage.assertTrue(presetMenu.compareWindowWidthAndCenter(viewNum, viewerpage.getWindowWidthValText(j), viewerpage.getWindowCenterText(j)), "Verify the WW and WC values for sync series for "+wlPresetMenuOptions.get(i)+" option", "WW and WC values are same for sync series");		
				}

			}
		}
	}

	public void selectAndVerifyAlloptionOfPresetWithSyncOff(List<String> wlPresetMenuOptions, WebElement windowWidthCenterLabelOverlay, int viewNum) throws TimeoutException, InterruptedException {
		//Step 1 - selecting each value in PRESET context menu
		presetMenu=new ViewBoxToolPanel(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6] & Checkpoint TC2[5]:", "Verify the WW and WC values after selecting each value in PRESET context menu when sync is OFF");

		for(int i = 1; i < wlPresetMenuOptions.size()-1 ;i++){
			viewerpage.assertTrue(presetMenu.verifyWWWCValuesFromPresetContextMenuOption(viewNum,  wlPresetMenuOptions.get(i)),"Verify the WW and WC values with Preset menu for "+wlPresetMenuOptions.get(i)+" option","WW and WC values are same as selected");
			for(int j = 1; j <= viewerpage.getNumberOfCanvasForLayout(); j++) {
				if(j!= viewNum) {
					viewerpage.assertFalse(presetMenu.compareWindowWidthAndCenter(viewNum, viewerpage.getWindowWidthValText(j), viewerpage.getWindowCenterText(j)), "Verify the WW and WC values for sync series for "+wlPresetMenuOptions.get(i)+" option", "WW and WC values are same for sync series");
				}
			}
		}
	}

	public void resetAndVerifyWCvalueOFAllViewBoxes(WebElement windowWidthOrCenterLabelOverlay, int beforeWWValueFirstViewbox, int beforeWCValueFirstViewbox, int beforeWWValueSecViewbox, int beforeWCValueSecViewbox, int beforeWWValueThirdViewbox, int beforeWCValueThirdViewbox, int beforeWWValueFourthViewbox, int beforeWCValueFourthViewbox, int viewNum) throws TimeoutException, InterruptedException {
		presetMenu=new ViewBoxToolPanel(driver);
		presetMenu.selectPresetValue(viewNum, ViewerPageConstants.RESET);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1] & Checkpoint TC2[3] Checkpoint", "Verify the WW and WC values after selecting Reset in PRESET context menu" );
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1), beforeWWValueFirstViewbox, "Verifying the WW value in first viewbox", "WW value in first viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1), beforeWCValueFirstViewbox, "Verifying the WC value in first viewbox", "WC value in first viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(2), beforeWWValueSecViewbox, "Verifying the WW value in second viewbox", "WW value in second viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2), beforeWCValueSecViewbox, "Verifying the WC value in second viewbox", "WC value in second viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(3), beforeWWValueThirdViewbox, "Verifying the WW value in Third viewbox", "WW value in Third viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(3), beforeWCValueThirdViewbox, "Verifying the WC value in Third viewbox", "WC value in Third viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(4), beforeWWValueFourthViewbox, "Verifying the WW value in Fourth viewbox", "WW value in Fourth viewbox is same as previous value");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(4), beforeWCValueFourthViewbox, "Verifying the WC value in Fourth viewbox", "WC value in Fourth viewbox is same as previous value");
	}

	public void performAndVerifyInvertWindowWidthAndCenter(WebElement windowWidthOrCenterLabelOverlay, int viewNum, String checkpointNum) throws TimeoutException, InterruptedException {
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Checkpoint TC2[2] & Checkpoint TC2[4] Verify the invert option",checkpointNum);
	}
	
	

}


