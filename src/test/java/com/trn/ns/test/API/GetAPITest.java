package com.trn.ns.test.API;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class GetAPITest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private DatabaseMethods db;
	

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String bonagePatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);
	String bonagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String filePath2= Configurations.TEST_PROPERTIES.get("AH.4_US675_filepath");
	String ah4_US675_PatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath2);
	
	String filePath3= Configurations.TEST_PROPERTIES.get("JIAJIE_filepath");
	String JIAJIEPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath3);
	String JIAJIEPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	String machineName = DataReader.getPatientDetails(NSDBDatabaseConstants.MACHINE_NAME, filePath3);
	
	String filepath4 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath4);
	String resultDescription=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filepath4);
	String covidPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filepath4);
	

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	


	
	@Test(groups ={"Chrome","Edge","IE11","DE1673", "Negative"})
    public void test01_DE1673_TC6787_TC6788_TC6789_US1521_TC8178_verifyFindingAPIResultInNonUILoginMethod() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Parameters of Findings API <br>"+
		"Verify if Finding API returns result in Non-UI login method <br>"+
		"Verify if the appropriate error message is displayed if Patient ID provided in Finding API is not Unique and if incorrect parameters are provided. <br>"+
		"Verify findings summary using non UI login in to NS for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		
		 //verify when accession number is not present in DB
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,covidPatientID);
		
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/8]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDsFromBatchTable(covidPatientName).get(0)),"Checkpoint[2/8]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[3/8]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[4/8]","Verified type after getting response from API.");
		
		//Finding API is not Unique 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message when Finding API is not unique." );
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,ah4_US675_PatientID);
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
	    loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[5/8]","Verified status code 200 after getting result from API response");
	    loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_PARAMETERS_NOT_UNIQUE),"Checkpoint[6/8]","Verified error message when finding API is not unique.");
		
		//if incorrect parameters are provided
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message when incorrect parameters are provided in API" );
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,"DP");
		keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
	    loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[7/8]","Verified status code 200 after getting result from API response"); 
	    loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_FOR_INCORRECT_PARAMETERS),"Checkpoint[8/8]","Verified error message when incorrect parameters are present in API and no matching results are found.");

	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1673", "Negative"})
	public void test02_DE1673_TC6790_verifyFindingAPIWhenParametersAreProvidedInCombination() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if Finding API returns right result if parameters are provided in combination");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify API GET response when Patient ID and Issuer ID both are provided in combination.");
        //get issuer id for AH.4 patient
		String issuerID=db.getIssuerOfPatientID(ah4PatientID);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,ah4PatientID);
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,issuerID);
		
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/2]","Verified status code 200 from API response"); 
		
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_NO_FINDING_SUMMARY_AVAILABLE),"Checkpoint[2/2]","Verified error message when no finding summary available for the particular patient");
	
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1673", "Positive"})
	public void test03_DE1673_TC6791_verifyBatchAndMachineIDForSingleAndMultipleFindingSummaryTag() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Finding Summary tag shows at result level, for a single/multiple finding summary tag shows with batchID and MachineID information");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verify batch,Machine ID and Multiple finding summary tag from API response.");

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,JIAJIEPatientID);
		
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL, keyVal,hm);
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/6]","Verified status code 200 from API response"); 
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(JIAJIEPatientName)),"Checkpoint[2/6]","Verified Batch ID from API response");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getMachineUIDFromMachineTable(machineName)),"Checkpoint[3/6]","Verified Batch ID from API response");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[4/6]","Verified finding summary from API response");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY+"1"),"Checkpoint[5/6]","Verified finding summary1 from API response");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY+"2"),"Checkpoint[6/6]","Verified finding summary2 from API response");
  
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1673","US1521", "Negative"})
	public void test04_DE1673_TC6790_US1521_TC8178_verifyErrorMessageWhenFindingSummaryDataNotAvailableInDB() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if Finding API returns right result if parameters are provided in combination. <br>"+
		"Verify findings summary using non UI login in to NS for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");

		loginPage = new LoginPage(driver);
		db=new DatabaseMethods(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,ah4PatientID);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify API response when finding summary data not available in database" );
		
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL, keyVal,hm);
	
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/2]","Verified status code 200 from API response"); 
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_NO_FINDING_SUMMARY_AVAILABLE),"Checkpoint[2/2]","Verified error message when no finding summary available for the particular patient");
	
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1619","E2E","F631"})
	public void test05_US1619_TC8455_verifyAPIResponseForPMAPData() throws SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP API is retrieving the PMAP LUT that comes along with the data.");

		db= new DatabaseMethods(driver);
		String storageID = db.getPMAPStorageId(covidPatientName,"",resultDescription);

		Response response =RESTUtil.getResponse(OrthancAndAPIConstants.PMAP_BASE_URL, OrthancAndAPIConstants.PMAP_URL+storageID);
		
		db.assertEquals(RESTUtil.getResponseValue(response, NSDBDatabaseConstants.LUT_ENTRIES_COUNT), db.getValueFromPMAPStorage(covidPatientName,"",resultDescription,NSDBDatabaseConstants.LUT_ENTRIES_COUNT), "Checkpoint[1/3]", "Verified lutEntriesCount value with json and Database.");	
		db.assertEquals(RESTUtil.getResponseValue(response,NSDBDatabaseConstants.LUT_FIRST_INPUT_VALUE), db.getValueFromPMAPStorage(covidPatientName,"",resultDescription, NSDBDatabaseConstants.LUT_FIRST_INPUT_VALUE), "Checkpoint[2/3]", "Verified lutFirstInputValue value with json and Database.");	
		db.assertNotNull(RESTUtil.getResponseValue(response,NSDBDatabaseConstants.PMAP_LOOKUP_TABLE), "Checkpoint[3/3]", "Verified pmapLookupTable value is not null in json response.");	

	
	}



}
