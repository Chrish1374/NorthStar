package com.trn.ns.test.viewer.liasionE2E;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.AfterMethod;
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
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class NewStudyTest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ContentSelector contentSelector;
	private ViewerLayout layout;


	String filePath = Configurations.TEST_PROPERTIES.get("VIDA_LungPrint_Discovery_Filepath");
	String vidaLungPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String username =Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private String machine = "VIDA LungPrint Discovery";

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
		    "wiagate": "http://10.1.8.72:4566/",
		    "analytics": "http://localhost:63650/",
		    "liaisonstowrs": "http://localhost:8042/dicom-web/studies",
		    "webserver": "http://localhost/"
		  },
	 */
	// Restart your app
	// Also, copy the data on given location as per config file or provide at runtime e.g -DliasionDataFolder="d:\data\DE2009"

	@BeforeClass(alwaysRun=true)
	public void importDataOnOrthanc() throws IOException {

		List<String> result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\DR2009")).map(x -> x.toString())
				.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());

		for(int i=0 ;i<result.size();i++)
			RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));
		


	}
	
	@BeforeMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.deletePatientData(vidaLungPatientName);
		
	}
	
	@Test(groups ={"DE2009", "Positive","Chrome","BVT","IE","Edge"})
	public void test01_DE2009_TC8198_verifyLiasionE2EExecution() throws InterruptedException, IOException, ParseException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Northstar receives cases from Laison");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(vidaLungPatientName),"Checkpoint[1/33]","verifying no patient is present");


		
		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8198/TC8198_NewStudy.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_STUDY_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[3/19]","verifying the response is 200 after sending the study");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[4/19]","verifying the response message is empty");


		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/33]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine +PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/33]","verifying the notification message is displayed upon sending of new study");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);

		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[6.1/33]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(vidaLungPatientName),"Checkpoint[6.2/33]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(vidaLungPatientName, "Lung"),"Checkpoint[6.3/33]","Verify Patient is selected and info is displayed");
	
		//Loading the patient on viewer
		patientPage.clickOnPatientRow(vidaLungPatientName);
		
//		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
//		patientPage.waitForElementVisibility(patientPage.toolTip);
//		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[6.1/19]","verifying the result status is displayed as pending");		

		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.closeNotification();

		int count = viewerpage.getNumberOfCanvasForLayout();		
		viewerpage.assertEquals(count, 3, "Checkpoint[6.2/19]", "verifying that 3 viewboxes are displayed");

		List<Integer> currentSlices = new ArrayList<Integer>();
		for(int i =1;i<=count;i++)
			currentSlices.add(viewerpage.getMaxNumberofScrollForViewbox(i));

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		viewerpage.assertEquals(sourceSeries.size(), count, "Checkpoint[7/19]", "veriying the source are same as number of viewboxes");
		viewerpage.assertTrue(results.isEmpty(), "Checkpoint[8/19]", "verifying there is no result in content selector");

		jsonParser = new JSONParser();
		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8198/TC8198_Result.json"));
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[9/19]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[10/19]","verifying that there is no response message displaye");


		//New results are available		
		viewerpage.waitForElementVisibility(viewerpage.notificationDiv);
		viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationUI),ViewerPageConstants.NEW_RESULT_NOTIFICATION_MESSAGE,"Checkpoint[11/19]","verifying the notification message is displayed upon sending of results");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.reRunResultsDialog),"Checkpoint[12/19]","verifying the new results dialog is displayed");
		loginPage.assertEquals(viewerpage.getText(viewerpage.newResultMessage),ViewerPageConstants.NEW_RESULT_DIALOG_MESSAGE,"Checkpoint[13/19]","verifying the message displayed on new result dialog");
		viewerpage.refreshButton.get(0).click();		
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.reRunResultsDialog),"Checkpoint[14/19]","verifying after clicking on refresh button new result dialog is closed");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[15/19]","verifying the result status is now turned from pending to run with findings");		

		//There are new result(s) available for this patient/study. Please check the content selector to see the updated result(s).
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		viewerpage.closeNotification();
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Checkpoint[16/19]", "verifying that as result is send the default layout is 1x1");

		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);		
		for(int i =1;i<=count;i++)
			viewerpage.assertEquals(currentSlices.get(i), viewerpage.getMaxNumberofScrollForViewbox(i),"Checkpoint[17/19]","verifying that there is no change in slices after layout change");

		viewerpage.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[18/19]", "verifying the same source series are displayed");
		viewerpage.assertEquals(contentSelector.getAllResults(), results, "Checkpoint[19/19]", "verifying that the same results are displayed");


	}


	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {
		
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		sd.deleteAllPatients();
		
	}


}
