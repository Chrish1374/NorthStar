package com.trn.ns.test.viewer.liasionE2E;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MultipleBatchV3Test extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerLayout layout;
	private ContentSelector contentSelector;

	String icometrixFilePath = Configurations.TEST_PROPERTIES.get("Icometrix");
	String icometrixPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, icometrixFilePath);
	
	String randoFilepath = Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
	String randoPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, randoFilepath);
	

	String username =Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	String machine ="icometrix";
	String rendoentMachine ="RANDO^ENT";
	
	String icoMachineName ="icometrix-icobrain-v2";
	String icoMachineName_2 ="icometrix-icobrain-v2_2";
	
	String riverPatientOrMachine1 = "Riverain ClearRead CT Detect Test 1";
	String riverPatientOrMachine2 = "Riverain ClearRead CT Detect Test 2";

	
	// Please follow below steps before running this script 
	// Change the appSetting.conf file like below -
	/*
			 "services": {
		    "authentication": "http://localhost:59021/",
		    "image": "http://localhost:63830/",
		    "layout": "http://localhost:58430/",
		    "study": "http://localhost:63820/",
		    "usermanagement": "http://localhost:63622/",
		    "settings": "http://localhost:63850/",
		    "log": "http://localhost:62449/",
		    "wiagateaccess": "http://localhost:60050/",
		    "wiagate": "http://localhost:4566/",
		    "analytics": "http://localhost:63650/",
		    "liaisonstowrs": "http://localhost:8042/dicom-web/studies",
		    "webserver": "http://localhost/"
		  },
	 */
	/*
	 * LiasonService": {
    "RegistrationRetryTimeInSeconds": 60,
    "LiaisonProtocol": "v3"
  }
	 */
	// Restart your app
	// Also, copy the data on given location as per config file or provide at runtime e.g -DliasionDataFolder="d:\data\DE2009"


	@BeforeClass(alwaysRun=true)
	public void importDataOnOrthanc() throws IOException {

		List<String> result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\icometrix-icobrain-v2")).map(x -> x.toString())
				.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());
		for(int i=0 ;i<result.size();i++)
			RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));

		result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\RANDOENT-RT_V3")).map(x -> x.toString())
				.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());
		for(int i=0 ;i<result.size();i++)
			RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));

		result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\Riverain")).map(x -> x.toString())
				.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());
		for(int i=0 ;i<result.size();i++)
			RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));



	}


	@BeforeMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.deletePatientData(icometrixPatientName);
		db.deleteMachine(icoMachineName);
		db.deleteMachine(icoMachineName_2);
		db.deletePatientData(randoPatientName);
		db.deletePatientData(riverPatientOrMachine2);
		db.deletePatientData(riverPatientOrMachine1);
		
	}
	

	@Test(groups ={"US1767", "Positive","Chrome","BVT","IE","Edge","E2E","F917"})
	public void test01_US1767_TC8641_verifyMultipleBatchResultOnStudyPage() throws InterruptedException, ParseException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify multiple 'New Batch Results' notification received in NS for same batch ID NS in V3 - ( When results sent with each notification is unique)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/33]","verifying no patient is present");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatch.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/33]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/33]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/33]","verifying the notification message is displayed upon sending of new study");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertFalse(patientPage.studyRows.isEmpty(),"Checkpoint[5.1/33]","Verifying that studylist is not empty");
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[5.2/33]","Verifying the New study is displayed");

	
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[6.1/33]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[6.2/33]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[6.3/33]","Verify Patient is selected and info is displayed");
		patientPage.clickOnStudy(1);

		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();
		

		int count = layout.getNumberOfCanvasForLayout();		
		layout.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[7/33]", "verifying that 3 viewboxes are displayed");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), count, "Checkpoint[8/33]", "veriying the source are same as number of viewboxes");
		layout.assertTrue(results.isEmpty(), "Checkpoint[9/33]", "verifying there is no result in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[10/33]","Verify Patient is selected and info is displayed");
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[11.1/33]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[11.2/33]","verifying pending icon is displayed");
		String dateTIme = patientPage.getDateTimeFromToolTip();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage_1.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[12/33]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[13/33]","verifying that there is no response message displaye");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);

		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[14.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[14.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[15/33]","Verifying that there is no addition of study when send the result");

		patientPage.clickOntheFirstStudy();
		
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[16/33]", "verifying that as result is send");
		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[17/33]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+2, "Checkpoint[18/33]", "verifying results are displayed");

		results = contentSelector.getAllResults();
		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[19/33]","verifying the result status is changed from pending to run with findings after sending the result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[20/33]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[21/33]","verifying Liaison icon is displayed");
		patientPage.assertEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[22/33]", "Verifying the date time is not changed after sending result");
		dateTIme = patientPage.getDateTimeFromToolTip();


		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[23/33]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[24/33]","verifying that there is no response message displaye");

		//New results are not available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[25.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[25.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[26/33]","verifying there is no additional study displayed");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Checkpoint[27/33]", "verifying that as result is send layout is changed to two by three");

		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[28/33]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+2, "Checkpoint[29/33]", "verifying more results are added");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[30/33]","verifying the result status is displayed as same as run with findings");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[31/33]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[32/33]","verifying the liaison icon");
		patientPage.assertNotEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[33/33]", "verifying the date is changed on sending results");




	}

	@Test(groups ={"US1767", "Positive","Chrome","BVT","IE","Edge","E2E","F917"})
	public void test02_US1767_TC8642_verifyMultipleBatchResultOnPatientStudyPage() throws InterruptedException, IOException, ParseException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify multiple 'New Batch Results' notification received in NS for same batch ID NS in V3 - ( When results sent with each notification is not  unique)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/37]","verifying no patient is present");

		
		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatch.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/37]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/37]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/33]","verifying the notification message is displayed upon sending of new study");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		int studyCount = patientPage.studyRows.size();

		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[5/37]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[6.1/37]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[6.2/37]","Verify Patient is selected and info is displayed");
		patientPage.clickOntheFirstStudy();

		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();
		
		
		int count = layout.getNumberOfCanvasForLayout();		
		layout.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[7/37]", "verifying that defaault layout is two by one ");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), count, "Checkpoint[8/37]", "veriying the source are same as number of viewboxes");
		layout.assertTrue(results.isEmpty(), "Checkpoint[9/37]", "verifying there is no result in content selector as only study is sent");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[10/37]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[11/37]","verifying Pending icon is displayed");
		String dateTIme = patientPage.getDateTimeFromToolTip();
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[12/37]", "Verifying that patient is displayed on patient page");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.navigateToBack();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[13/37]","verifying the result status is displayed as pending");
		patientPage.assertEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[14/37]", "Verifying date time is displayed and same as study page on patient study page");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[15/37]","verifying the Pending icon is displayed");

		dateTIme = patientPage.getDateTimeFromToolTip();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage_1.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[16/37]","verifying the response code after sending the result");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[17/37]","verifying that there is no response message displaye");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[14.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[14.2/33]","verifying the notification message is displayed upon sending of results");
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);		
		patientPage.assertEquals(patientPage.studyRows.size(),studyCount,"Checkpoint[19/37]","Verifying that study is same as on study page");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[20/37]", "verifying that as result is send the default layout is 1x1");

		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[21/37]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+2, "Checkpoint[22/37]", "verifying that the results are displayed");

		results = contentSelector.getAllResults();
		layout.browserBackWebPage();

		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[23/37]","verifying the result status is displayed as Run as finding");		
		patientPage.assertEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[24/37]", "verifying the date time on tooltip is same as on study page");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[25/37]","verifying no pending icon is displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[26/37]","verifying liaison icon is displayed");

		dateTIme = patientPage.getDateTimeFromToolTip();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[27/37]","verifying the response code after sending the result");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[28/37]","verifying that there is no response message displaye");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[29.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[29.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),studyCount,"Checkpoint[30/37]","verifying the same nuumber of study is displayed");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(),layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Checkpoint[31/37]", "verifying that as result is send the layout is two by three");

		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[32/37]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+2, "Checkpoint[33/37]", "verifying that more results are added");

		layout.browserBackWebPage();

		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[34/37]","verifying the result status is displayed as run with findings");		
		patientPage.assertNotEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[35/37]", "verifying date time is changed when sending the another result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[36/37]","verifying Pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[37/37]","verifying liaison icon is displayed");

	}

	@Test(groups ={"US1767", "Negative","Chrome","IE","Edge","E2E","F917"})
	public void test03_US1767_TC8643_verifyMultipleBatchAndResultDataNotUnique() throws InterruptedException, IOException, ParseException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the  NS able to import  multiple  'New Batch'  and  then associate the  'New Batch Results' notification  correctly for V3 using postman (Data in the batch is not unique)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/44]","verifying no icometrics patient is present");

		db = new DatabaseMethods(driver);

		List<String> machines = db.getAllMachinesName();
		HashMap<Integer,String> batchIDs = db.getAllBatchInfo();

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8643/TC8643_NewBatchMessage_1.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/44]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/44]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/33]","verifying the notification message is displayed upon sending of new study");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);

		int studyCount = patientPage.studyRows.size();
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[5/44]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[6.1/44]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[6.2/44]","Verify Patient is selected and info is displayed");
	

		patientPage.clickOntheFirstStudy();

		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();
		
		
		int count = layout.getNumberOfCanvasForLayout();		
		layout.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[7/44]", "verifying that study is displayed in one by one");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), count, "Checkpoint[8/44]", "veriying the source are same as number of viewboxes");
		layout.assertTrue(results.isEmpty(), "Checkpoint[9/44]", "verifying there is no result in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[10/44]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[11/44]","verifying pending icon is displayed");
		List<String> machineName = patientPage.getAllMachinesFromTooltip(1);

		db.assertEquals(db.getAllMachinesName().size(), machines.size()+1, "Checkpoint[12/44]", "verifying that new machine is added in db");
		db.assertEquals(db.getAllBatchInfo().size(), batchIDs.size()+1, "Checkpoint[13/44]", "verifying the batch id is also added in db for the study");

		machines = db.getAllMachinesName();
		batchIDs = db.getAllBatchInfo();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8643/TC8643_NewBatchMessage_2.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[14/44]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[15/44]","verifying that there is no response message displaye");

		layout.waitForElementVisibility(layout.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[16.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(layout.getText(patientPage.notificationMessage),PatientPageConstants.THE_STUDY+machine+PatientPageConstants.HAS_BEEN_UPDATED,"Checkpoint[16/44]","verifying the notification message is displayed upon sending of results");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),studyCount,"Checkpoint[17/44]","verifying that no increment in study after sending another study of same batc");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.convertIntoInt(patientPage.getText(patientPage.numberOfResults)),2,"Checkpoint[18/44]","verifying count is displayed on pending icon when another study is sent");		
		patientPage.assertEquals(patientPage.getAllMachineNameFromTooltip(1).size(),2,"Checkpoint[19/44]","verifying two machines are displayed on tooltip");		
		patientPage.assertEquals(patientPage.getAllLastUpdatedFromTooltip(1).size(),2,"Checkpoint[20/44]","verifying update dates are displayed on tooltip");		
		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).size(),2,"Checkpoint[21/44]","verifying the result status are displayed on tooltip");		

		for(int i=0;i<patientPage.getAllResultStatusFromTooltip(1).size();i++)
			patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(i),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[22/44]","verifying the result status is displayed as pending");		

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[23/44]", "verifying layout is changed after sending the another batch");

		layout.assertEquals(contentSelector.getAllSeries().size(), sourceSeries.size()+1, "Checkpoint[24/44]", "verifying source is added after sending the another batch");
		layout.assertTrue(contentSelector.getAllResults().isEmpty(), "Checkpoint[25/44]", "verifying no results aree added");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage_1.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[26/44]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[27/44]","verifying that there is no response message displaye");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[28/44]","verifying the notification message is displayed upon sending of results");
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),studyCount,"Checkpoint[29/44]","verifying that no increment in study after sending another result of same batch");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_L_AND_TWO_BY_ONE_R_LAYOUT), "Checkpoint[30/44]", "verifying that layout is changed after sending results");

		layout.assertEquals(contentSelector.getAllSeries().size(), sourceSeries.size()+1, "Checkpoint[31/44]", "verifying the source series are added");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+2, "Checkpoint[32/44]", "verifying that results start showing displayed");

		results = contentSelector.getAllResults();
		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(0),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[33/44]","verifying the result status is displayed as pending");		
		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(1),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[34/44]","verifying the result status is displayed as run with findings");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[35/44]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[36/44]","verifying liaison icon is displayed");

		db.assertEquals(db.getAllMachinesName().size(), machines.size()+1, "Checkpoint[37/44]", "verifying the another machine should be displayed");
		db.assertEquals(db.getAllBatchInfo().size(), batchIDs.size()+1, "Checkpoint[38/44]", "verifying the another batch should also be added");

		List<String> machineUIDAfterSendingAnotherBatch = db.getMachineName(machineName.get(0));		
		machines = db.getAllMachinesName();
		batchIDs = db.getAllBatchInfo();

		db.assertTrue(machines.containsAll(machineUIDAfterSendingAnotherBatch), "Checkpoint[39/44]", "verifying the same machines are present in DB");
		db.assertTrue(new ArrayList<String>(batchIDs.values()).containsAll(machineUIDAfterSendingAnotherBatch) , "Checkpoint[40/44]", "Verifying the machines are same for batch");

		List<String> ids = db.getBatchIDsFromBatchTable(icometrixPatientName);

		db.assertEquals(db.getSourceFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(0)))).size(),2,"Checkpoint[41/44]","verifying the sources are attached to batch1");
		db.assertEquals(db.getSourceFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(1)))).size(),3,"Checkpoint[42/44]","verifying the sources are attached to batch2");

		db.assertEquals(db.getResultsFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(0)))).size(),2,"Checkpoint[43/44]","verifying the results are attached to batch1");
		db.assertEquals(db.getResultsFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(1)))).size(),0,"Checkpoint[44/44]","verifying that there is no results displayed");




	}	

	@Test(groups ={"US1767", "Positive","Chrome","IE","Edge","E2E","F917"})
	public void test04_US1767_TC8649_verifyMultipleBatchAndResultDataUnique() throws InterruptedException, IOException, ParseException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the  NS able to import  multiple  'New Batch'  and  then associate the  'New Batch Results' notification  correctly for V3 using postman (Data in the batch is unique)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);	
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/41]","verifying no icometrix patient is present");

		db = new DatabaseMethods(driver);

		List<String> machines = db.getAllMachinesName();
		HashMap<Integer,String> batchIDs = db.getAllBatchInfo();

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8649/TC8649_NewBatchMessage_1_Unique.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/41]","verifying the response is 200 after sending the study");


		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[3.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[3.2/33]","verifying the notification message is displayed upon sending of new study");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		int rowCount = patientPage.studyRows.size();
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[4/41]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[5.1/41]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[5.2/41]","Verify Patient is selected and info is displayed");
	

		patientPage.clickOntheFirstStudy();

		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();
		
		
		int count = layout.getNumberOfCanvasForLayout();		
		layout.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[6/41]", "verifying that 1x1 viewbox is displayed on first study is sent");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), count, "Checkpoint[7/41]", "veriying the source are displayed in content selector");
		layout.assertTrue(results.isEmpty(), "Checkpoint[8.1/41]", "verifying there is no result in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[8.2/41]","Verify Patient is selected and info is displayed");
		

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[9/41]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[10/41]","verifying the pending icon is displayed");
		List<String> machineName = patientPage.getAllMachinesFromTooltip(1);

		db.assertEquals(db.getAllMachinesName().size(), machines.size()+1, "Checkpoint[11/41]", "verifying the machine is added in database");
		db.assertEquals(db.getAllBatchInfo().size(), batchIDs.size()+1, "Checkpoint[12/41]", "verifying the batch is also added in database");

		machines = db.getAllMachinesName();
		batchIDs = db.getAllBatchInfo();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8649/TC8649_NewBatchMessage_2_Unique.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[13/41]","verifying the response code after sending the batch");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[14.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.THE_STUDY+machine+PatientPageConstants.HAS_BEEN_UPDATED,"Checkpoint[14.2/41]","verifying the notification message is displayed upon sending of study");
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),rowCount,"Checkpoint[15/41]","verifying that another study is mapped to prev one hence no additional study is displayed");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.convertIntoInt(patientPage.getText(patientPage.numberOfResults)),2,"Checkpoint[16/41]","verifying the 2 batch study count is displayed in pending icon");		
		patientPage.assertEquals(patientPage.getAllMachineNameFromTooltip(1).size(),2,"Checkpoint[17/41]","verifying two machines are displayed in tooltip");		
		patientPage.assertEquals(patientPage.getAllLastUpdatedFromTooltip(1).size(),2,"Checkpoint[18/41]","verifying last updated date time displayed");		
		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).size(),2,"Checkpoint[19/41]","verifying run status is also displayed");		

		for(int i=0;i<patientPage.getAllResultStatusFromTooltip(1).size();i++)
			patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(i),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[20/41]","verifying the result status is displayed as pending");		

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[21/41]", "verifying  the viewer is loaded");

		layout.assertEquals(contentSelector.getAllSeries().size(), sourceSeries.size()+1, "Checkpoint[22/41]", "verifying source is also added in content selector");
		layout.assertTrue(contentSelector.getAllResults().isEmpty(), "Checkpoint[23/41]", "verifying no result is displayed");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[24/41]","verifying the response code after sending the result");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[25.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[25.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),rowCount,"Checkpoint[26/41]","verify the study row is displayed as before");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Checkpoint[27/41]", "verifying the layout is changed after sending result");

		layout.assertEquals(contentSelector.getAllSeries().size(), sourceSeries.size()+1, "Checkpoint[28/41]", "verifying the source is also added");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+4, "Checkpoint[29/41]", "verifying results are also added in content selector");

		results = contentSelector.getAllResults();
		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(0),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[30/41]","verifying the result status is displayed as pending");		
		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(1),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[31/41]","verifying the result status is displayed as run with findings");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[32/41]","verifying the pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[33/41]","verifying the EnvoyAI icon is displayed");

		db.assertEquals(db.getAllMachinesName().size(), machines.size()+1, "Checkpoint[34/41]", "verifying the machine is also added in DB");
		db.assertEquals(db.getAllBatchInfo().size(), batchIDs.size()+1, "Checkpoint[35/41]", "verifying the new batch is also added in DB");

		List<String> machineUIDAfterSendingAnotherBatch = db.getMachineName(machineName.get(0));		
		machines = db.getAllMachinesName();
		batchIDs = db.getAllBatchInfo();

		db.assertTrue(machines.containsAll(machineUIDAfterSendingAnotherBatch), "Checkpoint[36/41]", "Verifying the tooltip machines are same in DB");
		db.assertTrue(new ArrayList<String>(batchIDs.values()).containsAll(machineUIDAfterSendingAnotherBatch) , "Checkpoint[37/41]", "Verifying the tooltip machines are same in DB");

		List<String> ids = db.getBatchIDsFromBatchTable(icometrixPatientName);

		db.assertEquals(db.getSourceFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(0)))).size(),2,"Checkpoint[38/41]","verifying the source are attached to batch 1 in DB");
		db.assertEquals(db.getSourceFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(1)))).size(),2,"Checkpoint[39/41]","verifying the source are attached to batch 2 in DB");

		db.assertEquals(db.getResultsFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(0)))).size(),4,"Checkpoint[40/41]","verifying the result are associated");
		db.assertEquals(db.getResultsFromWiaTable(db.getBatchMachineID(db.convertIntoInt(ids.get(1)))).size(),0,"Checkpoint[41/41]","verifying there is no result associated to second batch");
		
		

	}	

	@Test(groups ={"US1767", "Positive","Chrome","IE","Edge","E2E","F917"})
	public void test05_US1767_TC8652_verifyMultipleBatchInParallelAndResult() throws InterruptedException, IOException, ParseException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the  NS able to import  multiple  'New Batch'  and  then associate the  'New Batch Results' notification  correctly for V3 using postman (when batch notification is sent in parallel)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/22]","verifying no patient is present");


		JSONParser jsonParser = new JSONParser();
		Object jsonObject1 = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8649/TC8649_NewBatchMessage_1_Unique.json"));
		Object jsonObject2 = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8649/TC8649_NewBatchMessage_2_Unique.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject1.toString());
		RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject2.toString());

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.THE_STUDY+machine+PatientPageConstants.HAS_BEEN_UPDATED,"Checkpoint[2/22]","verifying the notification message is displayed upon sending the multiple batch one after another");

		patientPage = new PatientListPage(driver);
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[3/22]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[4.1/22]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[4.2/22]","Verify Patient is selected and info is displayed");
	

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[4.3/22]","verifying the result status is displayed as pending");

		patientPage.assertEquals(patientPage.convertIntoInt(patientPage.getText(patientPage.numberOfResults)),2,"Checkpoint[5/22]","verifying the pending count is displayed as 2");		
		patientPage.assertEquals(patientPage.getAllMachineNameFromTooltip(1).size(),2,"Checkpoint[6/22]","verifying two machines headers are displayed on tooltip");		
		patientPage.assertEquals(patientPage.getAllLastUpdatedFromTooltip(1).size(),2,"Checkpoint[7/22]","verifying date time is displayed on tooltips");		
		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).size(),2,"Checkpoint[8/22]","verifying the result status is also displayed");		

		for(int i=0;i<patientPage.getAllResultStatusFromTooltip(1).size();i++)
			patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(i),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[9/22]","verifying the result status is displayed as pending");		

		patientPage.clickOntheFirstStudy();
		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[10/22]", "verifying layout is displayed as 1x1");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), 2, "Checkpoint[11/22]", "veriying the source are displayed in content selector");
		layout.assertTrue(results.isEmpty(), "Checkpoint[12/22]", "verifying there is no result in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[13/22]","verifying the response code after sending the result");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[14.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[14.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[15/22]","verifying that there is only one study is displayed");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Checkpoint[16/22]", "verifying that as result is send the layout is 2 x 3");

		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[17/22]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+4, "Checkpoint[18/22]", "verifying the results are added in content selector");

		results = contentSelector.getAllResults();
		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(0),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[19/22]","verifying the result status is displayed as pending");		
		patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(1),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[20/22]","verifying the result status is displayed as run with findings");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[21/22]","verifying no pending icon is displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[22/22]","verifying the envoyAI icon is displayed");

	}

	@Test(groups ={"US1767", "Positive","Chrome","IE","Edge","E2E","F917"})
	public void test06_US1767_TC8652_verifyMultipleBatchAndResultsAreSentInParallel() throws InterruptedException, IOException, ParseException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify multiple 'New Batch Results' notification received in NS for same batch ID NS in V3 - ( When results are sent  in parallel)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/33]","verifying patients are present");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatch.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/20]","verifying the response is 200 after sending the study");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[3.1/20]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[3.2/20]","verifying the notification message is displayed upon sending of new study");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		int studyCount = patientPage.studyRows.size();
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[4/20]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[5.1/20]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[5.2/20]","Verify Patient is selected and info is displayed");
	

		patientPage.clickOntheFirstStudy();
		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();

		int count = layout.getNumberOfCanvasForLayout();		
		layout.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[6/20]", "verifying that the default layout is 2x1");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), count, "Checkpoint[7/20]", "veriying the source is displayed in content selector");
		layout.assertTrue(results.isEmpty(), "Checkpoint[8/20]", "verifying there is no result in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[9/20]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[10/20]","verifying the pending icon is displayed");

		
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[11.1/20]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[11.2/20]","Verify Patient is selected and info is displayed");
	
		patientPage.clickOnPatientRow(icometrixPatientName);
		
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[12/20]","verifying the result status is displayed as pending");


		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage_1.json"));
		Object jsonObject1 = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8654/TC8654_NewBatchResultMessage_2.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject1.toString());

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[13.1/20]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[13.2/20]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.studyRows.size(),studyCount,"Checkpoint[14/20]","verifying the study count is same on sending multiple results one after another");

		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Checkpoint[15/20]", "verifying that as result is displayed in 2x3 layout");

		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[16/20]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+4, "Checkpoint[17/20]", "verifying that results are added and displayed in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[18/20]","verifying the result status is displayed as run with findings");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint/20[19]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[20/20]","verifying the envoyAI icon is displayed");

	}

	@Test(groups ={"DR2249", "Positive","Chrome","BVT","IE","Edge","E2E","F677"})
	public void test07_DR2249_TC8993_verifyBatchResultOnStudyPage() throws InterruptedException, ParseException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Re-execute TC8328: Verify importing the results first followed by source using Postman with V3 registration.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);		
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(randoPatientName),"Checkpoint[1/21]","verifying there is no patient present");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8993/TC8993_NewBatchMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/21]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/21]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/21]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+rendoentMachine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/21]","verifying the notification message is displayed upon sending of new study");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[5.1/21]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(randoPatientName),"Checkpoint[5.2/21]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(randoPatientName, ""),"Checkpoint[5.3/21]","Verify Patient is selected and info is displayed");
	
		patientPage.clickOntheFirstStudy();

		DICOMRT rt = new DICOMRT(driver);
		rt.assertTrue(rt.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[7/21]", "verifying that user is on viewerpage");
		rt.assertEquals(rt.getNumberOfCanvasForLayout(),0, "Checkpoint[8/21]", "verifying blank viewer page is displayed");
		
		
		rt.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[9/21]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[10/21]","verifying pending icon is displayed");
		String dateTIme = patientPage.getDateTimeFromToolTip();


		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8993/TC8993_NewBatchResultMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Result");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[11/21]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[12/21]","verifying that there is no response message displayed");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+rendoentMachine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[13/21]","verifying the notification message is displayed upon sending of results");
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[14/21]","Verifying that there is no addition of study when send the result");

		patientPage.clickOntheFirstStudy();
		rt.waitForDICOMRTToLoad();
		
		layout = new ViewerLayout(driver);
		rt.assertEquals(rt.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[15/21]", "verifying that as result is send");
		contentSelector = new ContentSelector(driver);
		rt.assertTrue(contentSelector.getAllSeries().isEmpty(), "Checkpoint[16/21]", "verifying the same source series are displayed");
		rt.assertFalse(contentSelector.getAllResults().isEmpty(), "Checkpoint[17/21]", "verifying results are displayed");

		rt.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[18/21]","verifying the result status is changed from pending to run with findings after sending the result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[19/21]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[20/21]","verifying Liaison icon is displayed");
		patientPage.assertEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[21/21]", "Verifying the date time is not changed after sending result");


	}

	@Test(groups ={"DR2246", "Positive","Chrome","BVT","IE","Edge","E2E","F432"})
	public void test08_DR2246_TC8944_verifyMultipleBatchesAndResultsOnStudyPage() throws InterruptedException, ParseException, IOException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that EurekaAI explorer can receive a new dataset associated with an existing machine on NSDB");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);		
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(riverPatientOrMachine2),"Checkpoint[1/33]","Verifying there is no study");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8944/TC8944_Batch1.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/33]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/33]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+riverPatientOrMachine2+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[5.0/33]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(riverPatientOrMachine2),"Checkpoint[5.1/33]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(riverPatientOrMachine2, "CT THORAX W/CONTRAST"),"Checkpoint[5.2/33]","Verify Patient is selected and info is displayed");
	
		patientPage.clickOntheFirstStudy();

		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();

		int count = layout.getNumberOfCanvasForLayout();		
		layout.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[6/33]", "verifying that 3 viewboxes are displayed");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		layout.assertEquals(sourceSeries.size(), count, "Checkpoint[7/33]", "veriying the source are same as number of viewboxes");
		layout.assertTrue(results.isEmpty(), "Checkpoint[8/33]", "verifying there is no result in content selector");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[9/33]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[10/33]","verifying pending icon is displayed");

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8944/TC8944_Batch1Result.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[11/33]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[12/33]","verifying that there is no response message displayed");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[14.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+riverPatientOrMachine2+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+patientPage.getText(patientPage.studyDescriptionList.get(0)),"Checkpoint[14.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[14.1/33]","Verifying that there is no addition of study when send the result");
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[14.2/33]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(riverPatientOrMachine2),"Checkpoint[14.3/33]","verifying icometrix patient is displayed");
		
		db = new DatabaseMethods(driver);
		String  riverStudyDetails =  db.getStudyDescription(riverPatientOrMachine2);
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(riverPatientOrMachine2, riverStudyDetails),"Checkpoint[14.4/33]","Verify Patient is selected and info is displayed");
	
		patientPage.clickOnPatientRow(riverPatientOrMachine2);
		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[15/33]", "verifying that as result is send");
		layout.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[16/33]", "verifying the same source series are displayed");
		layout.assertEquals(contentSelector.getAllResults().size(), results.size()+1, "Checkpoint[17/33]", "verifying results are displayed");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[18/33]","verifying the result status is changed from pending to run with findings after sending the result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[19/33]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[20/33]","verifying Liaison icon is displayed");


		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8944/TC8944_Batch2.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[21/33]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[22/33]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[23.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+riverPatientOrMachine1+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[23.2/33]","verifying the notification message is displayed upon sending of new study");

		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+2,"Checkpoint[24.0/33]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(riverPatientOrMachine1),"Checkpoint[24.1/33]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(riverPatientOrMachine1, riverStudyDetails),"Checkpoint[24.2/33]","Verify Patient is selected and info is displayed");
	

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8944/TC8944_Batch2Result.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[25/33]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[26/33]","verifying that there is no response message displaye");

		//New results are not available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[28.1/33]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+riverPatientOrMachine1+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+patientPage.getText(patientPage.studyDescriptionList.get(0)),"Checkpoint[28.2/33]","verifying the notification message is displayed upon sending of results");

		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+2,"Checkpoint[28.0/33]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(riverPatientOrMachine1),"Checkpoint[28.1/33]","verifying icometrix patient is displayed");

		patientPage.clickOnPatientRow(riverPatientOrMachine1);
		patientPage.clickOntheFirstStudy();
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[29/33]", "verifying that as result is send layout is changed to two by three");

		layout.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		for(int i =0 ;i<2;i++) {
			patientPage.click(patientPage.patientNamesList.get(i));
			patientPage.waitForTimePeriod(1000);
			patientPage.assertEquals(patientPage.getAllResultStatusFromTooltip(1).get(0),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[30/33]","verifying the result status is displayed as same as run with findings");
			patientPage.assertTrue(patientPage.allpendingIcon.isEmpty(),"Checkpoint[32."+i+"/33]","verifying pending icon is not displayed");
			patientPage.assertTrue(patientPage.isElementPresent(patientPage.allEurekaIcon.get(0)),"Checkpoint[33."+i+"/33]","verifying the liaison icon");
		}
	}

	@AfterClass(alwaysRun=true)
	public void deletePatientFromOrthanc() throws SQLException {

		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		List<Object> patients = RESTUtil.getJsonPath(response).getList("");
		for(Object patient : patients) {
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient.toString()).asString();
		}

	}



}
