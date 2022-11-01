package com.trn.ns.test.viewer.layout;
import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I15_F41_LayoutBasicCapability-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class BetterLayoutTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private ViewerLayout layout;


	// Get Patient Name
	String SQA_Testing = Configurations.TEST_PROPERTIES.get("SQA_Testing");
	String patientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, SQA_Testing);

	String Boneage_filepath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String patientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Boneage_filepath);

	String Picline_filepath = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String patientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Picline_filepath);

	String Qure_ai_XRay_Patient1_filepath = Configurations.TEST_PROPERTIES.get("Qure_ai_XRay_Patient1_filepath");
	String patientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Qure_ai_XRay_Patient1_filepath);


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientListPage = new PatientListPage(driver);
	}

	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"US609","Sanity"})
	public void test03_US609_TC2094_updateSaveAsDefault() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("'Save as Default' checkbox saves the new user preferred layout and applies it when study is reloaded(if there are empty view boxes, best-fit is applied to remove them");
				//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName2+"in viewer" );
	
		HelperClass helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName2, 2);
		layout=new ViewerLayout(driver);
		
		//Check for BoneAge data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1] & Checkpoint[1/4]", "Bone age loads with 2x1 layout. The checkbox 'Save as Default' is selected and new layout 1x1 is applied on viewer" );
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 4, "Verifying the Boneage default loads in 2x1", "BoneAge loads with 2x1 layout");
		// check SaveAsDefault
		// Change layout to 1x1
		layout.selectLayoutWithSaveByDefault(layout.oneByOneLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Bone age loads with new user preference layout 1x1" );
		helper.browserBackAndReloadViewer(patientName2,1, 1);
		
		// Validate layout change to 1x1
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 1, "Checkpoint TC3[2]:Verifying the after changes Boneage default loads in 1x1", "BoneAge loads with user preference 1x1 layout");
//		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint TC3[2]:verifying the viewer for patient "+patientName2, "TC03_checkpoint1");
		// Change layout to 2x1
		layout.selectLayoutWithSaveByDefault(layout.twoByOneLayoutIcon);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Bone age loads with new user preference layout 1x1" );
		helper.browserBackAndReloadViewer(patientName2,1, 1);
		
		// Validate layout change to 2x1
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 2, "Verifying the after changes Boneage default loads in 2x1", "BoneAge loads with user preference 2x1 layout");
		layout.selectLayoutWithSaveByDefault(layout.twoByTwoLayoutIcon);

		//Check for Piccline data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "PICCLine ONE loads with 2x1 layout. The checkbox 'Save as Default' is selected and new layout 2x1 is applied on viewer" );
		helper.browserBackAndReloadViewer(patientName3,1, 2);
		
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 2, "Checkpoint TC3[3]:Verifying the PICCLIne default loads in 2x1", "Picline loads with 2x1 layout");


		// check SaveAsDefault

		// Change layout to 1x1
		layout.selectLayoutWithSaveByDefault(layout.oneByOneLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "PICCLIne loads with new user preference layout 1x1" );
		helper.browserBackAndReloadViewer(patientName3,1, 1);
		

		// Validate layout change to 1x1
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 1, "Verifying the after changes PICCLIne default loads in 1x1", "PICCLIne loads with new user preference layout 1x1");
//		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "verifying the viewer for patient "+patientName3, "TC03_checkpoint2");


		// Change layout to 2x1
		layout.selectLayoutWithSaveByDefault(layout.twoByOneLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "PICCLIne loads with new user preference layout 1x1" );
		helper.browserBackAndReloadViewer(patientName3,1, 1);
		

		// Validate layout change to 2x1
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), 2, "Verifying the after changes PICCLIne default loads in 2x1", "PICCLIne loads with new user preference layout 2x1");

	}


	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.deleteUserSetLayout();
	}
}
