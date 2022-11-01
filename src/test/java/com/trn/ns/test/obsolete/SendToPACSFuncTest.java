//package com.trn.ns.test.obsolete;
//
//import java.sql.SQLException;
//import java.util.List;
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
//import com.trn.ns.page.API.factory.RESTUtil;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.GSPSAnnotationHelper;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.RegisterUserPage;
//import com.trn.ns.page.factory.SendToPACS;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//import io.restassured.response.Response;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class SendToPACSFuncTest extends TestBase{
//
//	private LoginPage loginPage;
//	private ExtentTest extentTest;
//	private PatientListPage patientPage;
//	
//	private ViewerPage viewerPage;
//	private RegisterUserPage register;
//
//
//	//patient detail with multiple series data
////	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
////	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	String filePath2 = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
//	String patientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//
//	String filePath3 = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
//	String patientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
//
//	String filePath4 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String pointMultiSeriesPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4);
//
//	String aidocfilePath = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
//	String aidocMacihnePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, aidocfilePath);
//
//	String filePath6 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String boneageStudyPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath6);
//
//	String filePath7 = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath7);
//
//	String filePath8 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath8);
//
//	String filePath9 = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
//	String aidocPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath9);
//
////	private CircleAnnotation circle;
////	private EllipseAnnotation ellipse;
//
//	String username_1 = "userA";
//	String username_2 = "test123456789scan123456789";
//	String username2 = "userB";
//	String username3 = "userC";
//	static String patient_id="";
//
//	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
//	private ContentSelector cs;
////	private GSPSAnnotationHelper gsps;
////	private PointAnnotation point;
////	private MeasurementWithUnit lineWithUnit;
//	private SendToPACS sd;
////	private static String textAnnText="look here";
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(username,password);
//	}
//	// US957 Update A/R states and send findings to PACS	
// // marking this test case as obsolete becoz of TC5595
//	//@Test(groups ={"Chrome","Edge","IE11","US957","positive"})	
//	public void test06_US957_TC4492_verifySendToPACSForMachineDataWithoutModification() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Send to PACS when only Machine result are sent without any modifications");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(aidocMacihnePatient);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		//		Load the Patient Data		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient Data");
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		cs = new ContentSelector(driver);
//		OutputPanel panel = new OutputPanel(driver);
//
//		//		Loads the Original Machine result if by default it is not loaded in the viewer
//		//		Open output panel and make a note of the number of findings listed. Verify findings listed in the output panel		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open output panel and make a note of the number of findings listed. Verify findings listed in the output panel");
//
////		panel.openAndCloseOutputPanel(true);		
//		panel.enableFiltersInOutputPanel(true,true,true);
//
//		List<WebElement> allFindings = panel.findingsList;
//		panel.openAndCloseOutputPanel(false);
//
//		List<String> latestResults = cs.getAllResultDesciptionFromContentSelector(1);
//
//		//		Click  'Send to PACS'
//		//		Click the “Leave as is” option		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configure Send to PACS to send all types of findings");
//		viewerPage.openSendToPACSMenu();
//		viewerPage.enableSendToPACSFindingOptions(true, true, true);
//		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click  'Send to PACS' and Click the “Leave as is” option");
//		viewerPage.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
//		viewerPage.waitForElementVisibility(viewerPage.banner);
//
//		//		Verify in content selector under Result (both under Machine and series option) no new unified user generated clone copy is created.
//		viewerPage.assertEquals(cs.getAllResultDesciptionFromContentSelector(1),latestResults,"Checkpoint[1/2]","Verify in content selector under Result (both under Machine and series option) no new unified user generated clone copy is created.");
//		panel.openAndCloseOutputPanel(true);
//		viewerPage.assertEquals(panel.findingsList,allFindings,"Checkpoint[2/2]","Verifying the output panel that findings count are same as before");
//		panel.openAndCloseOutputPanel(false);
//		sd = new SendToPACS(driver);
//		//		Open Orthanc and Verify the PR series are not created.(Note when only machine findings are sent without modification no PR series)		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open Orthanc and Verify the PR series are not created.(Note when only machine findings are sent without modification no PR series)");
//		sd.verifyNoOfPatientsInOrthanc(1);
//
//	}
//	
//	@AfterMethod(alwaysRun=true)
//	public void afterMethod() throws SQLException{
//		DatabaseMethods db = new DatabaseMethods(driver);
//
//		db.deleteDrawnAnnotation(username_1);
//		db.deleteDrawnAnnotation(username_2);
//		db.deleteDrawnAnnotation(username2);
//		db.deleteDrawnAnnotation(username3);
//
//		db.deleteUser(username_1);
//		db.deleteUser(username_2);
//		db.deleteUser(username2);
//		db.deleteUser(username3);
//		//Getting patient id
//		if(patient_id.isEmpty()) {
//			RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
//			RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
//			Response response = RESTUtil.getResponse();		
//			
//			if(RESTUtil.getJsonPath(response).getList("").size()>0) {
//				patient_id = RESTUtil.getJsonPath(response).getList("").get(0).toString();
//				RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
//			}
//
//		}
//
//
//	}
//
////	private void usersCreation() throws InterruptedException {
////
////		patientPage = new PatientListPage(driver);
////		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
////		register = new RegisterUserPage(driver);		
////
////		register.createNewUser(username_1, username_1, LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
////		register.createNewUser("abc", "abc", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);
////
////	}
//
//	
//	@Test(groups ={"Chrome","Edge","IE11","positive"})	
//	public void test06_US957_TC4492_US996_TC4708_verifySendToPACSForMachineDataWithoutModification() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Send to PACS when only Machine result are sent without any modifications");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(aidocPatient);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		//		Load the Patient Data		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient Data");
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		cs = new ContentSelector(driver);
//		OutputPanel panel = new OutputPanel(driver);
//
//		//		Loads the Original Machine result if by default it is not loaded in the viewer
//		//		Open output panel and make a note of the number of findings listed. Verify findings listed in the output panel		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open output panel and make a note of the number of findings listed. Verify findings listed in the output panel");	
//		panel.enableFiltersInOutputPanel(true,true,true);
//
//		List<WebElement> allFindings = panel.findingsList;
//		panel.openAndCloseOutputPanel(false);
//
//		List<String> latestResults = cs.getAllResultDesciptionFromContentSelector(1);
//
//		//		Click  'Send to PACS'
//		//		Click the “Leave as is” option		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Configure Send to PACS to send all types of findings");
//		viewerPage.openSendToPACSMenu();
//		viewerPage.enableSendToPACSFindingOptions(true, true, true);
//		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Click  'Send to PACS' and Click the “Leave as is” option");
//		viewerPage.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
//		viewerPage.waitForTimePeriod(2000);
//
//		//		Verify in content selector under Result (both under Machine and series option) no new unified user generated clone copy is created.
//		viewerPage.assertEquals(cs.getAllResultDesciptionFromContentSelector(1),latestResults,"Checkpoint[1/2]","Verify in content selector under Result (both under Machine and series option) no new unified user generated clone copy is created.");
//		panel.openAndCloseOutputPanel(true);
//		viewerPage.assertEquals(panel.findingsList,allFindings,"Checkpoint[2/2]","Verifying the output panel that findings count are same as before");
//		panel.openAndCloseOutputPanel(false);
//
//		//		Open Orthanc and Verify the PR series are not created.(Note when only machine findings are sent without modification no PR series)		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open Orthanc and Verify the PR series are not created.(Note when only machine findings are sent without modification no PR series)");
//		SendToPACS sd = new SendToPACS(driver);
//		sd.verifyNoOfPatientsInOrthanc(1);
//
//	}
//	
//
//	
//
//
//
//} 
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
