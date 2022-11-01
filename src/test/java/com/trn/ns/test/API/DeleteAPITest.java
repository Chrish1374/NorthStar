package com.trn.ns.test.API;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DeleteAPITest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ContentSelector cs;
	private DatabaseMethods db;

	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4DeletePatientAPI_filepath");
	String ah4DeletePatientAPI = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2 = Configurations.TEST_PROPERTIES.get("AH.4DeleteSeriesAPI_filepath");
	String ah4DeleteSeriesAPI = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 = Configurations.TEST_PROPERTIES.get("AH.4TestExpiredToken_filepath");
	String ah4TestExpiredToken = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("Subject60_DeleteStudyAPI_filepath");
	String Subject60_DeleteStudyAPI = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String filePath5 = Configurations.TEST_PROPERTIES.get("PiccOneDeleteBatchAPI_filepath");
	String PiccOneDeleteBatchAPI = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	
	String filePath6 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String bonagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);
	

	private String deletePatientURL = "DeletePatient/patients", patientName1="AH.4DeletePatientAPI/frhprod", patientName2="AH.4TestExpiredToken/frhprod"; //http://localhost/api/Data/DeletePatient/patients/{patientId}/{issuerOfPatientId}
	private String deleteStudyURL = "DeleteStudy/studies", studyID="2.16.840.1.113669.632.21.354548635.354548635.4098873744132407053"; //http://localhost/api/Data/DeleteStudy/studies/{studyInstanceUid}
	private String deleteSeriesURL = "DeleteSeries/series", seriesID="2.16.840.1.113669.632.21.1775243653.1775243653.29837438732510722"; //http://localhost/api/Data/DeleteSeries/series/{seriesInstanceUid};
	private String deleteBatchURL = "DeleteBatch/batches"; //http://localhost/api/wia/DeleteBatch/batches/{batchUid}; 
	private HelperClass helper;
	private static String batchID;

	@Test (priority = 1, groups ={"DE1067","Negative"})
	public void test01_DE1067_TC4813_verify409StatusCode() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 409 error code is received when trying to delete object(patient/study/series/batch) when they are being locked");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+ah4DeletePatientAPI+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ah4DeletePatientAPI, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying status code 409 and meaningful error message have been received when trying to delete a patient ");
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deletePatientURL,patientName1,keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.CONFLICT_API_CODE,"Verifying status code 409 is received when trying to delete a patient which is open in viewer", "Verified status code 409 is received when trying to delete a patient which is open in viewer");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_PATIENT_409_ERROR_MESSAGE),"Verifying error message when trying to delete locked patient", "Received error message after trying to delet locked patient");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(ah4DeleteSeriesAPI, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying status code 409 and meaningful error message have been received when trying to delete a series which is open in viewer");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteSeriesURL,seriesID,keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.CONFLICT_API_CODE,"Verifying status code 409 is received when trying to delete locked series", "Verified status code 409 is received when trying to delete locked series");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_SERIES_409_ERROR_MESSAGE),"Verifying error message when trying to delete locked series", "Received error message after trying to delete locked series");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(Subject60_DeleteStudyAPI, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying status code 409 and meaningful error message have been received when trying to delete a study which is open in viewer");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteStudyURL,studyID,keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.CONFLICT_API_CODE,"Verifying status code 409 is received when trying to delete locked study", "Verified status code 409 is received when trying to delete locked study");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_STUDY_409_ERROR_MESSAGE),"Verifying error message when trying to delete locked study", "Received  error message after trying to delete locked study");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(PiccOneDeleteBatchAPI, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying status code 409 and meaningful error message have been received when trying to delete a batch which is open in viewer");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_WIA_DELETE_BASE_URL,deleteBatchURL,getBatchID(PiccOneDeleteBatchAPI),keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.CONFLICT_API_CODE,"Verifying status code 409 is received when trying to delete locked batch", "Verified status code 409 is received when trying to delete locked batch");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_BATCH_409_ERROR_MESSAGE),"Verifying error message when trying to delete locked batch", "Received error message after trying to delete locked batch");
	}
	
	@Test(priority = 2,groups ={"DE1067", "Sanity", "Positive"})
	public void test02_DE1067_TC4791_TC4814_verifyDeletePatientAndVerifyDeleteNonExistingPatient() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify patient can be deleted using delete api");
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		int totalNumberOfPatientsBeforeDeletion = patientPage.patientNamesList.size();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying patient can be deleted using delete api and when token is configured in header");	

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		patientPage.assertEquals(RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deletePatientURL,patientName1,keyVal).get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Verifying status code 200 is received after successful deletion of patient", "Verified status code 200 is received after successful deletion of patient");

		patientPage.clickOnPatientRow(1);		
		patientPage.clickOntheFirstStudy();
		patientPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying patient is deleted from patient list page");
		patientPage.assertEquals(patientPage.patientNamesList.size(),totalNumberOfPatientsBeforeDeletion-1,"Verify patient does not exist in patient list","Verified patient does not exist in patient list");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying status code 404 and meaningful error message have been received when trying to delete a patient which does not exist");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deletePatientURL,patientName1,keyVal);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.NOT_FOUND_API_CODE,"Verifying status code 404 is received when trying to delete a patient which does not exist", "Verified status code 404 is received when trying to delete a patient which does not exist");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_PATIENT_404_ERROR_MESSAGE),"Verifying error message when trying to delete non-existing patient", "Received error message after trying to delet non-existing patient");

	}

	@Test(priority = 3,groups ={"DE1067", "Sanity", "Positive"})
	public void test03_DE1067_TC4793_TC4814_verifyDeleteStudyAndVerifyDeleteNonExistingStudy() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify study can be deleted using delete api"
				+ "<BR> Verify 404 error code is received when trying to delete object(patient/study/series/batch) which does not exist");
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+Subject60_DeleteStudyAPI+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(Subject60_DeleteStudyAPI,"","","");
		
		int totalNumberOfStudiesBeforeDeletion = patientPage.studyDescriptionList.size();
		patientPage.click(patientPage.clearButton);
		patientPage.waitForPatientPageToLoad();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying study can be deleted using delete api and when token is configured in header");
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		patientPage.assertEquals(RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteStudyURL,studyID,keyVal).get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Verifying status code 200 is received after successful deletion of study", "Verified status code 200 is received after successful deletion of study");

		patientPage.waitForTimePeriod(2000);
		patientPage.searchPatient(Subject60_DeleteStudyAPI,"","","");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying study is not present in the study list page");
		patientPage.assertEquals(patientPage.studyDescriptionList.size(),totalNumberOfStudiesBeforeDeletion-1,"Verify study does not exist in study list","Verified study does not exist in study list");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying status code 404 and meaningful error message have been received when trying to delete a study which does not exist");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteStudyURL,studyID,keyVal);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.NOT_FOUND_API_CODE,"Verifying status code 404 is received when trying to delete a study which does not exist", "Verified status code 404 is received when trying to delete a study which does not exist");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_STUDY_404_ERROR_MESSAGE),"Verifying error message when trying to delete non-existing study", "Received  error message after trying to delete non-existing study");

	}

	@Test(priority = 4,groups ={"DE1067", "Sanity", "Positive"})
	public void test04_DE1067_TC4796_TC4814_verifyDeleteSeriesAndVerifyDeleteNonExistingSeries() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify series can be deleted using delete api"
				+ "<BR> Verify 404 error code is received when trying to delete object(patient/study/series/batch) which does not exist");
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+ah4DeleteSeriesAPI+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(ah4DeleteSeriesAPI);
		
		patientPage.clickOntheFirstStudy();
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		
		int totalNumberOfSeriesBeforeDeletion = cs.getAllSeries().size();
		viewerpage.browserBackWebPage();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying series can be deleted using delete api and when token is configured in header");
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		patientPage.assertEquals(RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteSeriesURL,seriesID,keyVal).get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Verifying status code 200 is received after successful deletion of series", "Verified status code 200 is received after successful deletion of series");

		patientPage.clickOnPatientRow(ah4DeleteSeriesAPI);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying series is not present in the content selector");
		patientPage.assertEquals(cs.getAllSeries().size(),totalNumberOfSeriesBeforeDeletion-1,"Verify series does not exist in content selector","Verified series does not exist in content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying status code 404 and meaningful error message have been received when trying to delete a series which does not exist");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteSeriesURL,seriesID,keyVal);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.NOT_FOUND_API_CODE,"Verifying status code 404 is received when trying to delete a series which does not exist", "Verified status code 404 is received when trying to delete a series which does not exist");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_SERIES_404_ERROR_MESSAGE),"Verifying error message when trying to delete non-existing series", "Received error message after trying to delet non-existing series");

	}

	@Test(priority = 5,groups ={"DE1067", "Sanity", "Positive"})
	public void test05_DE1067_TC4798_TC4814_verifyDeleteBatchAndVerifyDeleteNonExistingBatch() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify batch can be deleted using delete api"
				+ "<BR> Verify 404 error code is received when trying to delete object(patient/study/series/batch) which does not exist");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		batchID = getBatchID(PiccOneDeleteBatchAPI);
		int totalNumberOfPatientsBeforeDeletion = patientPage.patientNamesList.size();
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientPage.getText(patientPage.patientNamesList.get(0)), 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying patient can be deleted using delete api and when token is configured in header");
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		patientPage.assertEquals(RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_WIA_DELETE_BASE_URL,deleteBatchURL,batchID,keyVal).get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Verifying status code 200 is received after successful deletion of patient", "Verified status code 200 is received after successful deletion of patient");

		patientPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying patient is deleted from patient list page");
		patientPage.assertEquals(patientPage.patientNamesList.size(),totalNumberOfPatientsBeforeDeletion-1,"Verify patient does not exist in patient list","Verified patient does not exist in patient list");


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying status code 404 and meaningful error message have been received when trying to delete a batch which does not exist");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_WIA_DELETE_BASE_URL,deleteBatchURL,batchID,keyVal);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.NOT_FOUND_API_CODE,"Verifying status code 404 is received when trying to delete a batch which does not exist", "Verified status code 404 is received when trying to delete a batch which does not exist");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_BATCH_404_ERROR_MESSAGE),"Verifying error message when trying to delete non-existing batch", "Received error message after trying to delete non-existing batch");

	}

	@Test(priority = 6,groups ={"DE1067","Negative"})
	public void test06_DE1067_TC4812_verify401StatusCode() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 401 error code is received when expired/invalid token is used to delete patient/study/series/batch");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+ah4DeletePatientAPI+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"),OrthancAndAPIConstants.HEADER_KEY);
		RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deletePatientURL,patientName1,keyVal);
		String invalidToken = "12333434343434334";		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying status code 401 and meaningful error message have been received when trying to delete a patient with expired token");
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deletePatientURL,patientName2,keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.UNAUTHORIZED_API_CODE,"Verifying status code 401 is received when trying to delete a patient with expired token", "Verified status code 401 is received when trying to delete a patient with expired token");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_401_ERROR_MESSAGE),"Verifying error message when trying to delete a patient with expired token", "Received error message when trying to delete a patient with expired token");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying status code 401 and meaningful error message have been received when trying to delete a study with expired token");
		response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteStudyURL,studyID,keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.UNAUTHORIZED_API_CODE,"Verifying status code 401 is received when trying to delete a study with expired token", "Verified status code 401 is received when trying to delete a study with expired token");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_401_ERROR_MESSAGE),"Verifying error message when trying to delete a study with expired token", "Received error message when trying to delete a study with expired token");

		keyVal = new HashMap<String,String>();		
		keyVal.put(OrthancAndAPIConstants.HEADER_KEY, invalidToken);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying status code 401 and meaningful error message have been received when trying to delete a series with invalid token");
		response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteSeriesURL,seriesID,keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.UNAUTHORIZED_API_CODE,"Verifying status code 401 is received when trying to delete a series with invalid token", "Verified status code 401 is received when trying to delete a series with invalid token");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_401_ERROR_MESSAGE),"Verifying error message when trying to delete a series with invalid token", "Received error message when trying to delete a series with invalid token");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying status code 401 and meaningful error message have been received when trying to delete a batch with invalid token");
		response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_WIA_DELETE_BASE_URL,deleteBatchURL,getBatchID(bonagePatientName),keyVal);
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.UNAUTHORIZED_API_CODE,"Verifying status code 401 is received when trying to delete a batch with invalid token", "Verified status code 401 is received when trying to delete a batch with invalid token");
		patientPage.assertTrue(patientPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DELETE_401_ERROR_MESSAGE),"Verifying error message when trying to delete a batch with invalid token", "Received error message when trying to delete a batch with inavlid token");
	}

	public String getBatchID(String patientName) {

		db = new DatabaseMethods(driver);
		String token = null;
		try {
			token= db.getBatchUIDFromBatchTable(patientName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}



	


}
