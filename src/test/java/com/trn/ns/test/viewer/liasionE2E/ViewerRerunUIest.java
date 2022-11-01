package com.trn.ns.test.viewer.liasionE2E;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
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
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerRerunUIest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ContentSelector contentSelector;
	private ViewerLayout layout;


	String filePath = Configurations.TEST_PROPERTIES.get("cpu_test_Filepath");
	String cpuPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String icometrixFilePath = Configurations.TEST_PROPERTIES.get("Icometrix");
	String icometrixPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, icometrixFilePath);


	String username =Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	
	private PagesTheme pagesTheme;
	String loadedTheme =ThemeConstants.EUREKA_THEME_NAME; 
	

	
//	@BeforeClass(alwaysRun=true)
	public void importDataOnOrthanc() throws IOException {

		List<String> result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\Data")).map(x -> x.toString())
				.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());
		for(int i=0 ;i<result.size();i++)
			RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));
			
			
		result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\icometrix-icobrain-v2")).map(x -> x.toString())
			.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());
	for(int i=0 ;i<result.size();i++)
		RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));

	}

	@BeforeMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deletePatientData(cpuPatientName);
		db.deletePatientData(icometrixPatientName);
		db.deleteMachine(ViewerPageConstants.ICOMACHINENAMEV2);
			
	}
	
	@Test(groups ={"Positive","BVT","US2607","DR2708","F917","F1095","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2607_TC10391_TC10392_TC10460_TC10462_TC10547_TC10551_DR2708_TC10797_verifyViewerNotification(String theme) throws InterruptedException, ParseException, IOException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new viewer notification UI for the postman batch result push on Eureka theme."
				+ "<br> Verify the new viewer notification UI for the postman batch result push on Dark theme."
				+ "<br> Verify the result notification UI when there are successive batch results pushed through postman."
				+ "<br> Verify the cancel and refresh buttons on new UI of batch result notification."
				+ "<br> [Risk and Impact]: Verify the appearance and closing behavior of viewer notifications."
				+ "<br> Verify that there is no opacity on the viewer when viewer notification is received through postman"
				+ "<br> Verify that 'Refresh' button is not displayed on result notification pop when already there is no result loaded on viewer.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
			
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		
		sendRequestAndVerifyPatientNotification(theme,1,20);
		
		
		patientPage.clickOnStudy(1);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		int count = viewerpage.getNumberOfCanvasForLayout();	
		layout=new ViewerLayout(driver);
		viewerpage.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[2.1/20]", "Verifying that after sending the request layout is 1x1");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		viewerpage.assertEquals(sourceSeries.size(), count, "Checkpoint[2.2/20]", "veriying the source are same as number of viewboxes");
		viewerpage.assertTrue(results.isEmpty(), "Checkpoint[3/20]", "verifying there is no result in content selector");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[4/20]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[5/20]","verifying pending icon is displayed");

		patientPage.clickOnStudy(1);
		viewerpage.waitForViewerpageToLoad();

		sendRequestAndVerifyViewerNotification(0,loadedTheme,6,20);	
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.refreshIconBy), "Checkpoint[7.1/20]", "Verifying that refresh utton is not displayed when there is no result loaded and result arrives");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getViewPort(1),NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Checkpoint[7.2/20]","verifying the opacity that on arrival of result the viewboxes are not getting grayed out");
		
		sendRequestAndVerifyViewerNotification(1,loadedTheme,8,20);
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.refreshIconBy), "Checkpoint[9.1/20]", "Verifying the refresh button is not displayed");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getViewPort(1),NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Checkpoint[9.2/20]","verifying the opacity that on arrival of result the viewboxes are not getting grayed out");
		
		int totalNotifications = patientPage.notificationTiles.size();	
		for(int i=totalNotifications-1;i>=0;i--) {
			viewerpage.click(viewerpage.closeIconOnNotification.get(i));
			viewerpage.waitForElementInVisibility(viewerpage.notificationTiles.get(i));
			viewerpage.assertEquals(viewerpage.notificationTiles.size(),i,"Checkpoint[10/20]","verifying the closing behavior of notification on click of close icon");
				
		}	
		
	
		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		
		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[11/20]","verifying the result status is changed from pending to run with findings after sending the result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[12/20]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[13/20]","verifying Liaison icon is displayed");

		patientPage.clickOntheFirstStudy();
		
		viewerpage.waitForViewerpageToLoad();
		viewerpage.closeNotification();
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[14/20]", "verifying viewer when result is send");
		viewerpage.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[15/20]", "verifying the same source series are displayed");
		viewerpage.assertEquals(contentSelector.getAllResults().size(), results.size()+1, "Checkpoint[16/20]", "verifying results are displayed");

		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[17/20]", "verifying the viewer page with updated layout");

		sendRequestAndVerifyViewerNotification(0,loadedTheme,18,20);		
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getViewPort(1),NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Checkpoint[19.1/20]","verifying the opacity of viewboxes");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.refreshIconBy), "Checkpoint[19.2/20]", "Verifying the refresh button is displayed");
		viewerpage.assertTrue(pagesTheme.verifyButtonTheme(viewerpage.refreshButton.get(0),NSGenericConstants.ENABLE_TEXT,loadedTheme),"Checkpoint[20/20]","verifying the theme of refresh");
		

	}
		
	@Test(groups ={"Positive","BVT","US2607","F1095","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US2607_TC10552(String theme) throws InterruptedException, ParseException, IOException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that series tab is refreshed with the new batch result in open/close both the state.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
			
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		
		sendRequestAndVerifyPatientNotification(theme,1,6);
		
		
		patientPage.clickOnStudy(1);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		int count = viewerpage.getNumberOfCanvasForLayout();	
		layout=new ViewerLayout(driver);
		viewerpage.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[2/6]", "verifying the viewer page");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		viewerpage.assertEquals(sourceSeries.size(), count, "Checkpoint[3/6]", "veriying the source are same as number of viewboxes");
		viewerpage.assertTrue(results.isEmpty(), "Checkpoint[4/6]", "verifying there is no result in content selector");
		contentSelector.openAndCloseSeriesTab(true);

		sendRequestAndVerifyViewerNotification(0,loadedTheme,5,6);
		
		viewerpage.click(viewerpage.cancelButton.get(0));		
		viewerpage.waitForElementInVisibility(viewerpage.notificationTiles.get(0));
		viewerpage.assertTrue(viewerpage.notificationTiles.isEmpty(),"Checkpoint[6.1/6]","verifying the closing behavior of notification on click of close icon");
	
		viewerpage.assertEquals(contentSelector.getAllResults().size(),results.size()+1, "Checkpoint[6.2/6]", "verifying there is no result in content selector");
		

	}
	
	
	@Test(groups ={"DR2708", "Positive","Chrome","BVT","IE","Edge","F917","E2E"})
	public void test03_DR2708_TC10798_verifyRefreshIcon() throws InterruptedException, ParseException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that 'Refresh' button is displayed only when old result is available and new result sent which has data to swap with old result.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
//		int totalPatients = patientPage.patientNamesList.size();
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[1/23]","verifying no patient is present");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatch.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/23]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/23]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4/23]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+ViewerPageConstants.ICOMETRIXMACHINENAME+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[5/23]","verifying the notification message is displayed upon sending of new study");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		
//		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint[6/23]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(icometrixPatientName),"Checkpoint[7/23]","verifying icometrix patient is displayed");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(icometrixPatientName, ""),"Checkpoint[8/23]","Verify Patient is selected and info is displayed");
		
		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage_1.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[9/23]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[10/23]","verifying that there is no response message displaye");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[11/23]","verifying the notification message is displayed upon sending of results");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+ViewerPageConstants.ICOMETRIXMACHINENAME+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+PatientPageConstants.EMPTY_STUDY,"Checkpoint[12/23]","verifying the notification message is displayed upon sending of results");
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
	
		
		patientPage.clickOntheFirstStudy();
		layout = new ViewerLayout(driver);
		layout.waitForViewerpageToLoad();
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[13/23]", "verifying that as result is send");

		contentSelector = new ContentSelector(driver);
		List<String> series = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();
		contentSelector.openAndCloseSeriesTab(true);
		
		contentSelector.compareElementImage(protocolName, layout.getViewPort(1), "Checkpoint[14/23]", "TC03_01");
		contentSelector.compareElementImage(protocolName, layout.getViewPort(1), "Checkpoint[15/23]", "TC03_02");
		
		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC8641/TC8641_NewBatchResultMessage.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		layout.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[16/23]","verifying the response code after sending the result");
		layout.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[17/23]","verifying that there is no response message displaye");

		//New results are not available		
		layout.waitForElementVisibility(layout.notificationDiv);
		layout.assertTrue(layout.isElementPresent(layout.refreshIconBy), "Checkpoint[18/23]", "Verifying the cancel button is displayed");
		
		layout.click(layout.refreshButton.get(0));
		layout.waitForElementInVisibility(layout.notificationDiv);
		
		
		
		layout.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Checkpoint[19/23]", "verifying that as result is send");
		layout.assertEquals(contentSelector.getAllSeries().size(),series.size(), "Checkpoint[20/23]", "verifying there is no result in content selector");
		layout.assertEquals(contentSelector.getAllResults().size(),results.size()+2, "Checkpoint[21/23]", "verifying there is no result in content selector");
		
		contentSelector.compareElementImage(protocolName, layout.getViewPort(1), "Checkpoint[22/23]", "TC03_03");
		contentSelector.compareElementImage(protocolName, layout.getViewPort(1), "Checkpoint[23/23]", "TC03_04");
	

	}

			
//	@AfterClass(alwaysRun=true)
	public void deletePatientFromOrthanc() throws SQLException {

		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		sd.deleteAllPatients();

	}
	
	
	private void sendRequestAndVerifyViewerNotification(int countNotification, String loadedTheme,int startCheck,int endCheck) throws IOException, ParseException, InterruptedException {
		
		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchResultMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the result");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		
		viewerpage = new ViewerPage(driver);
		pagesTheme = new PagesTheme(driver);
		viewerpage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint["+startCheck+".1/"+endCheck+"]","verifying the response code after sending the result");
		viewerpage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint["+startCheck+"2/"+endCheck+"]","verifying that there is no response message displayed");

		//New results are available		
		viewerpage.waitUntilCountChanges(viewerpage.notificationDiv,countNotification);
			
		int totalNotifications = viewerpage.notificationTiles.size();		
		for(int i=0;i<totalNotifications;i++) {
			viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationTitle.get(i)),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint["+startCheck+".3/"+endCheck+"]","verifying the notification type is info");
			viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationMessage.get(i)),ViewerPageConstants.NEW_RESULT_NOTIFICATION_MESSAGE,"Checkpoint["+startCheck+".4/"+endCheck+"]","verifying the notification message is displayed upon sending of results");
			
			viewerpage.assertTrue(pagesTheme.verifyThemeForText(viewerpage.notificationMessage.get(i), loadedTheme),"Checkpoint["+startCheck+".5/"+endCheck+"]","Verifying the theme for message");
			viewerpage.assertTrue(pagesTheme.verifyThemeForTitle(viewerpage.notificationTitle.get(i), loadedTheme),"Checkpoint["+startCheck+".6/"+endCheck+"]","verifying the theme for info");
			viewerpage.assertTrue(pagesTheme.verifyThemeForNotification(viewerpage.notificationTiles.get(i),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint["+startCheck+".7/"+endCheck+"]","verifying the entire popup for theme");
			viewerpage.assertTrue(pagesTheme.verifyThemeOnLabel(viewerpage.cancelButton.get(i), loadedTheme), "Checkpoint["+startCheck+".8/"+endCheck+"]", "Verifying the cancel button is displayed");
			viewerpage.assertTrue(pagesTheme.verifyThemeOnLabel(viewerpage.cancelButtonParent.get(i), NSGenericConstants.BACKGROUND_COLOR,loadedTheme), "Checkpoint["+startCheck+".9/"+endCheck+"]", "Verifying the cancel button is displayed");
			
			viewerpage.waitForTimePeriod(10000); // waiting for general time where in other notification gets closed
			
			viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.INFO, ViewerPageConstants.NEW_RESULT_NOTIFICATION_MESSAGE), "Checkpoint["+startCheck+".10/"+endCheck+"]", "Verifying that notification is not auto closed");
			viewerpage.assertTrue(pagesTheme.verifyThemeOnLabel(viewerpage.cancelButton.get(i), loadedTheme), "Checkpoint["+startCheck+".11/"+endCheck+"]", "Verifying the cancel button is displayed");
			viewerpage.assertTrue(pagesTheme.verifyThemeOnLabel(viewerpage.cancelButtonParent.get(i), NSGenericConstants.BACKGROUND_COLOR,loadedTheme), "Checkpoint["+startCheck+".12/"+endCheck+"]", "Verifying the cancel button is displayed");
			
		}
		
	}
		
	private void sendRequestAndVerifyPatientNotification(String loadedTheme, int startCheckpoint, int endCheckpoint) throws IOException, ParseException, InterruptedException, SQLException {
		
		int totalPatients = patientPage.patientNamesList.size();
		
		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint["+startCheckpoint+".1/"+endCheckpoint+"]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint["+startCheckpoint+".2/"+endCheckpoint+"]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint["+startCheckpoint+".3/"+endCheckpoint+"]","verifying the message type is info");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+ViewerPageConstants.CPUMACHINENAME+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint["+startCheckpoint+".4/"+endCheckpoint+"]","verifying the notification message is displayed upon sending of new study");

		PagesTheme pagesTheme = new PagesTheme(driver);
		patientPage.assertTrue(pagesTheme.verifyThemeForText(patientPage.notificationMessage, loadedTheme),"Checkpoint["+startCheckpoint+".5/"+endCheckpoint+"]","Verifying the theme for message");
		patientPage.assertTrue(pagesTheme.verifyThemeForTitle(patientPage.notificationTitle, loadedTheme),"Checkpoint["+startCheckpoint+".6/"+endCheckpoint+"]","verifying the title theme");
		patientPage.assertTrue(pagesTheme.verifyThemeForNotification(patientPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint["+startCheckpoint+".7/"+endCheckpoint+"]","verifying the theme for entire notification tile");
		patientPage.assertTrue(patientPage.verifyNotifactionDimensions(),"Checkpoint["+startCheckpoint+".8/"+endCheckpoint+"]","verifying the dimensions and position of notification tile");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.patientNamesList.size() , totalPatients+1,"Checkpoint["+startCheckpoint+".9/"+endCheckpoint+"]","verifying new patient is added");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(cpuPatientName),"Checkpoint["+startCheckpoint+".10/"+endCheckpoint+"]","verifying icometrix patient is displayed");
		
		db = new DatabaseMethods(driver);
		String studyName = db.getStudyDescription(cpuPatientName);
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(cpuPatientName, studyName),"Checkpoint["+startCheckpoint+".11/11]","Verify Patient is selected and info is displayed");
	
		
	}


}
