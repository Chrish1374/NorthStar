//package com.trn.ns.test.obsolete;
//
//
//import java.sql.SQLException;
//
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.PatientXMLConstants;
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
//
////Functional.NS.I15_F41_LayoutBasicCapability-CF0304ARevD - revision-0
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class BetterLayoutTest extends TestBase {
//
//	private ExtentTest extentTest;
//	private LoginPage loginPage;
//	private PatientListPage patientListPage;
////	private SinglePatientStudyPage spStudyListPage;
//	private ViewerPage viewerPage;
//	private DatabaseMethods database = new DatabaseMethods(driver);
//	
//
//	// Get Patient Name
//	String SQA_Testing = Configurations.TEST_PROPERTIES.get("SQA_Testing");
//	String patientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, SQA_Testing);
//
//	String Boneage_filepath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String patientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Boneage_filepath);
//
//	String Picline_filepath = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
//	String patientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Picline_filepath);
//
//	String Qure_ai_XRay_Patient1_filepath = Configurations.TEST_PROPERTIES.get("Qure_ai_XRay_Patient1_filepath");
//	String patientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Qure_ai_XRay_Patient1_filepath);
//
//
//	// Before method, handles the steps before loading the data for set up.
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		// Begin on the Login Page, and log in.
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//	//@Test(groups = { "firefox", "Chrome", "IE11", "Edge" ,"US609","US764","Sanity"}) Obsolete
//		public void test01_US609_TC2093_US764_TC2981_verifySaveAsDefault() throws InterruptedException  {
//
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("The 'Save as Default' checkbox is disabled if the study is not associated with a machine.json/algorithm and is enabled otherwise"
//					+ "<br> Refactor initial layout and change layout code in client/server-Verify 'Save by Default' checkbox");
//
//			//Loading the patient on viewer
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName1+"in viewer" );
//			patientListPage = new PatientListPage(driver);
//			patientListPage.clickOnPatientRow(patientName1);
//			spStudyListPage = new SinglePatientStudyPage(driver);
//			spStudyListPage.clickOntheFirstStudy();
//			viewerPage = new ViewerPage(driver);
//			viewerPage.waitForViewerpageToLoad();
//			// step 1
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1] & Checkpoint[1/2]", "The 'Save as Default' checkbox is disabled as the study is not associated with a machine.json/algorithm" );
//			viewerPage.assertTrue(viewerPage.validateSaveByDefault(), "Verifying Save By Default is enabled", "Save By Default is enabled - there is always machine data present ");
//			navigateToPatientPageAndReload(patientName2);		
//			// step 2
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[2] & Checkpoint[2/2]", "The 'Save as Default' checkbox is enabled as the study is associated with a machine.json/algorithm" );
//			viewerPage.assertTrue(viewerPage.validateSaveByDefault(), "Verifying Save By Default is enabled", "Save By Default is enabled");
//		}
//
//		//@Test(groups = { "firefox", "Chrome", "IE11", "Edge","US609" })//obsolete
//		public void test02_US609_TC2095_verifyPayloadRecommendation() throws InterruptedException, SQLException  {
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("The layout applied is based on payload recommendation(machine.json) for each of the studies");
//			//Loading Bone Age patient on viewer
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName2+"in viewer" );
//			patientListPage = new PatientListPage(driver);
//			patientListPage.clickOnPatientRow(patientName2);
//			spStudyListPage = new SinglePatientStudyPage(driver);
//			spStudyListPage.clickOntheFirstStudy();
//			viewerPage = new ViewerPage(driver);
//			viewerPage.waitForViewerpageToLoad(2);
//			//Check for BoneAge data
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Bone Age loads with 2x1 layout");
//			DatabaseMethods db = new DatabaseMethods(driver);
//			String studyUID = db.getStudyInstanceUID(patientName2);
//			viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), database.getBestFitLayoutForStudy(studyUID), "", "BoneAge loads with 2x1 layout");	
////			viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint TC1[1] : verifying the viewer for patient "+patientName2, "TC02_checkpoint1");
//			//Check for PiccLine One data
//			navigateToPatientPageAndReload(patientName3);
//			studyUID = db.getStudyInstanceUID(patientName3);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[2] & Checkpoint[3/3]", "PiccLIne loads with 2x1 layout");
//			viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), database.getBestFitLayoutForStudy(studyUID), "", "PiccLIne loads with 2x1 layout");	
////			viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "verifying the viewer for patient "+patientName3, "TC02_checkpoint2");
//			//Check for ChestXRay data
//			navigateToPatientPageAndReload(patientName4);
//			viewerPage.waitForPdfToRenderInViewbox(2);
//			studyUID = db.getStudyInstanceUID(patientName4);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[3] & Checkpoint[2/3]", "Chest-XRay loads with 2x1 layout");			
//			viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), database.getBestFitLayoutForStudy(studyUID), "", "Chest-XRay loads with 1x1L-2x1R layout");	
////			viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "verifying the viewer for patient "+patientName4, "TC02_checkpoint3");
//
//		}
//
//	
//	public void navigateToPatientPageAndReload(String patientName) throws InterruptedException{
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.browserBackWebPage();
//		spStudyListPage = new SinglePatientStudyPage(driver);
//		
//		spStudyListPage.waitForSingleStudyToLoad();
//		spStudyListPage.browserBackWebPage();
//		
//		patientListPage = new PatientListPage(driver);
//		patientListPage.waitForPatientPageToLoad();
//		
//		patientListPage.clickOnPatientRow(patientName);	
//		
//		spStudyListPage.waitForSingleStudyToLoad();
//		spStudyListPage.clickOntheFirstStudy();
//		
//		viewerPage.waitForPdfToRenderInViewbox(1);
//
//	}
//
//	@AfterMethod(alwaysRun=true)
//	public void afterMethod() throws SQLException {
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.deleteUserSetLayout();
//	}
//}
