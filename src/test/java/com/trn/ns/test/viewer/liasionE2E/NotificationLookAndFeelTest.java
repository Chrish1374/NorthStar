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
public class NotificationLookAndFeelTest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ContentSelector contentSelector;
	private ViewerLayout layout;


	String filePath = Configurations.TEST_PROPERTIES.get("cpu_test_Filepath");
	String cpuPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	
	String username =Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	String machine ="CPU Test";
	private PagesTheme pagesTheme;
	String loadedTheme =ThemeConstants.EUREKA_THEME_NAME; 
	


	
	@BeforeClass(alwaysRun=true)
	public void importDataOnOrthanc() throws IOException {

		List<String> result = Files.walk(Paths.get(Configurations.TEST_PROPERTIES.get("dataFolder")+"\\Data")).map(x -> x.toString())
				.filter(f -> f.endsWith(".dcm")).collect(Collectors.toList());
		for(int i=0 ;i<result.size();i++)
			RESTUtil.importDCMOnOrthanc(OrthancAndAPIConstants.ORTHANC_BASE_URL, "/"+OrthancAndAPIConstants.INSTANCES_ORTHANC_URL, result.get(i));
	}
	
	@BeforeMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deletePatientData(cpuPatientName);
			
	}

	@Test(groups ={"US2199","US2227","US2228","Positive","Chrome","BVT","Edge","E2E","F1094","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2199_TC9506_TC9507_TC9510_US2227_TC9721_verifyBatchNotification(String theme) throws InterruptedException, ParseException, IOException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that batch notification is appearing on Patient-Study page when a study is pushed through Postman."
				+ "<br> Verify the batch notification design , location and look and feel for Eureka purple theme."
				+ "<br> Verify the batch notification design , location and look and feel for dark theme.<br>"+
				"[Risk and impact]- Verify the risk and impact of new notification design.<br>"+
				"[Risk and Impact]: Verify the TC9507, TC9508, TC9532");

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
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(cpuPatientName),"Checkpoint[1/31]","verifying no patient is present");
		
		
		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/31]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/31]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/31]","verifying the message type is info");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/31]","verifying the notification message is displayed upon sending of new study");

		PagesTheme pagesTheme = new PagesTheme(driver);
		patientPage.assertTrue(pagesTheme.verifyThemeForText(patientPage.notificationMessage, loadedTheme),"Checkpoint[5/31]","Verifying the theme for message");
		patientPage.assertTrue(pagesTheme.verifyThemeForTitle(patientPage.notificationTitle, loadedTheme),"Checkpoint[6/31]","verifying the title theme");
		patientPage.assertTrue(pagesTheme.verifyThemeForNotification(patientPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint[7/31]","verifying the theme for entire notification tile");
		patientPage.assertTrue(patientPage.verifyNotifactionDimensions(),"Checkpoint[8/31]","verifying the dimensions and position of notification tile");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertFalse(patientPage.studyRows.isEmpty(),"Checkpoint[9/31]","Verifying that studylist is not empty");
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[10/31]","Verifying the New study is displayed");
		patientPage.assertTrue(patientPage.verifyPatientIsPresent(cpuPatientName),"Checkpoint[12/31]","verifying icometrix patient is displayed");
		String studyName = db.getStudyDescription(cpuPatientName);
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(cpuPatientName, studyName),"Checkpoint[6.3/33]","Verify Patient is selected and info is displayed");
	
		patientPage.clickOnStudy(1);

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		int count = viewerpage.getNumberOfCanvasForLayout();	
		layout=new ViewerLayout(driver);
		viewerpage.assertEquals(count, layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[12/31]", "verifying the viewer page");

		contentSelector = new ContentSelector(driver);
		List<String> sourceSeries = contentSelector.getAllSeries();
		List<String> results = contentSelector.getAllResults();

		viewerpage.assertEquals(sourceSeries.size(), count, "Checkpoint[13/31]", "veriying the source are same as number of viewboxes");
		viewerpage.assertTrue(results.isEmpty(), "Checkpoint[14/31]", "verifying there is no result in content selector");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.PENDING_STATUS,"Checkpoint[15/31]","verifying the result status is displayed as pending");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[16/31]","verifying pending icon is displayed");
		String dateTIme = patientPage.getDateTimeFromToolTip();

		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchResultMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the result");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[17/31]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[18/31]","verifying that there is no response message displayed");

		//New results are available		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);

		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[19.1/31]","verifying the notification type is info");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_1+machine+PatientPageConstants.NEW_RESULT_RECIEVED_MESSAGE_2+studyName,"Checkpoint[19.2/31]","verifying the notification message is displayed upon sending of results");
		patientPage.assertTrue(pagesTheme.verifyThemeForText(patientPage.notificationMessage, loadedTheme),"Checkpoint[20/31]","Verifying the theme for message");
		patientPage.assertTrue(pagesTheme.verifyThemeForTitle(patientPage.notificationTitle, loadedTheme),"Checkpoint[21/31]","verifying the theme for info");
		patientPage.assertTrue(pagesTheme.verifyThemeForNotification(patientPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint[22/31]","verifying the entire popup for theme");
		patientPage.assertTrue(patientPage.verifyNotifactionDimensions(),"Checkpoint[23/31]","verifying the notification displayed and it position");
		
		patientPage.waitForElementInVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[24/31]","Verifying that there is no addition of study when send the result");

		patientPage.clickOntheFirstStudy();
		
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[25/31]", "verifying viewer when result is send");
		viewerpage.assertEquals(contentSelector.getAllSeries(), sourceSeries, "Checkpoint[26/31]", "verifying the same source series are displayed");
		viewerpage.assertEquals(contentSelector.getAllResults().size(), results.size()+1, "Checkpoint[27/31]", "verifying results are displayed");

		results = contentSelector.getAllResults();
		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[28/31]","verifying the result status is changed from pending to run with findings after sending the result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[29/31]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[30/31]","verifying Liaison icon is displayed");
		patientPage.assertEquals(patientPage.getDateTimeFromToolTip(), dateTIme, "Checkpoint[31/31]", "Verifying the date time is not changed after sending result");
		


	}
	
	@Test(groups ={"US2199","US2227","US2228", "Positive","Chrome","BVT","Edge","E2E","F1094","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US2199_TC9508_TC9509_TC9510_US2227_TC9721_TC9740_TC9743_US2228_TC9944_verifyMultNotifAndClosing(String theme) throws InterruptedException, ParseException, IOException{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the functionality when multiple notifications appears and its look and feel."
				+ "<br> Verify the closing behavior of batch notification."
				+ "<br> Verify the batch notification design , location and look and feel for dark theme.<br>"+
				"[Risk and impact]- Verify the risk and impact of new notification design.<br>"+
				"Verify the appearance and closing behavior of notifications when multiple notification appears simultaneously for eureka theme.<br>"+
				"Verify the appearance and closing behavior of notifications when multiple notification appears simultaneously for dark theme.<br>"+
				"[Risk and Impact]: Verify the TC9507, TC9508, TC9532");

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
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(cpuPatientName),"Checkpoint[1/10]","verifying no patient is present");
		
		
		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchMessage.json"));
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/10]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/10]","verifying the response message is empty");
		patientPage.waitForElementsVisibility(patientPage.notificationDiv);
		
		JSONParser jsonParser1 = new JSONParser();
		Object jsonObject1 = jsonParser1.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatch.json"));
		List<Object> response1 = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject1.toString());
		patientPage.assertEquals(response1.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[4/10]","verifying the response is 200 after sending the another study");
		patientPage.assertTrue(response1.get(1).toString().isEmpty(),"Checkpoint[5/10]","verifying the response message is empty");
		patientPage.waitForElementsVisibility(patientPage.notificationDiv);
		
		JSONParser jsonParser2 = new JSONParser();
		Object jsonObject2 = jsonParser2.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchResultMessage.json"));
		List<Object> response2 = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject2.toString());
		patientPage.assertEquals(response2.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[6/10]","verifying the response is 200 after sending the result");
		patientPage.assertTrue(response2.get(1).toString().isEmpty(),"Checkpoint[7/10]","verifying the response message is empty");

		patientPage.waitUntilCountChanges(patientPage.notificationDiv,2);
		patientPage.assertEquals(patientPage.notificationTiles.size(),3,"Checkpoint[8/10]","verifying the multiple notifications are displayed");

		pagesTheme = new PagesTheme(driver);
		int totalNotifications = patientPage.notificationTiles.size();		
	
		for(int i=totalNotifications;i>0;i--) {
	
			patientPage.assertTrue(pagesTheme.verifyThemeForNotification(patientPage.notificationTiles.get(i-1),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint[9/10]","verifying the theme is applied on all the notifications");
			patientPage.closeIconOnNotification.get(i-1).click();
			patientPage.assertEquals(patientPage.notificationTiles.size(),3,"Checkpoint[10/10]","verifying the closing behavior of notification on click of close icon");
				
		}	
		
		
		

	}
		
	@Test(groups ={"US2199", "Positive","Chrome","Edge","F1094"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_US2199_TC9532_TC9639_verifyResizeUpdatedMsg(String theme) throws InterruptedException, ParseException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the responsiveness of the batch notification on resizing the browser."
				+ "<br> Verify that updated batch notification is appearing on Patient-Study page when a study is pushed through Postman.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		
		int x =500;
		int y =500;
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(),"Checkpoint[1/20]","verifying no patient is present");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchMessage.json"));
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/20]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/20]","verifying the response message is empty");

		patientPage.waitForElementsVisibility(patientPage.notificationDiv);
		int height = patientPage.getHeightOfWebElement(patientPage.notificationTiles.get(0));
		int width = patientPage.getWidthOfWebElement(patientPage.notificationTiles.get(0));
		pagesTheme = new PagesTheme(driver);
		patientPage.resizeBrowserWindow(x, y);
		
		patientPage.assertEquals(height,patientPage.getHeightOfWebElement(patientPage.notificationTiles.get(0)),"Checkpoint[4/20]","verifying that on resize the height is not changed for notification");
		patientPage.assertEquals(width,patientPage.getWidthOfWebElement(patientPage.notificationTiles.get(0)),"Checkpoint[5/20]","verifying that on resize the width is not changed for notification");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.notificationDiv),"Checkpoint[6/20]","verifying Notification is present on resize");
		patientPage.assertTrue(pagesTheme.verifyThemeForText(patientPage.notificationMessage, loadedTheme),"Checkpoint[7/20]","verifying the theme on resize for message");
		patientPage.assertTrue(pagesTheme.verifyThemeForTitle(patientPage.notificationTitle, loadedTheme),"Checkpoint[8/20]","verifying the theme on resize for info");
		patientPage.assertTrue(pagesTheme.verifyThemeForNotification(patientPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint[9/20]","verifying the theme on notification");
	
		// need to wait for 5 sec to check the auto close of notification
		patientPage.waitForTimePeriod(5000);		
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.notificationDiv),"Checkpoint[10/20]","verifying the notification gets closed after 5 seconds");

		patientPage.maximizeWindow();
		
		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatch.json"));
		List<Object> response1 = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response1.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[11/20]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response1.get(1).toString().isEmpty(),"Checkpoint[12/20]","verifying the response message is empty");
		patientPage.waitForElementsVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.THE_STUDY+machine+PatientPageConstants.HAS_BEEN_UPDATED,"Checkpoint[13/20]","verifying the notification message on notification");

		
		height = patientPage.getHeightOfWebElement(patientPage.notificationTiles.get(0));
		width = patientPage.getWidthOfWebElement(patientPage.notificationTiles.get(0));
		
		patientPage.resizeBrowserWindow(x, y);
		
		patientPage.assertEquals(height,patientPage.getHeightOfWebElement(patientPage.notificationTiles.get(0)),"Checkpoint[14/20]","verifying the notification height is same for result notification on resize");
		patientPage.assertEquals(width,patientPage.getWidthOfWebElement(patientPage.notificationTiles.get(0)),"Checkpoint[15/20]","verifying the notification width is same for result notification on resize");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.notificationDiv),"Checkpoint[16/20]","verifying the notification is present");
		patientPage.assertTrue(pagesTheme.verifyThemeForText(patientPage.notificationMessage, loadedTheme),"Checkpoint[17/20]","verifying the theme for notification message");
		patientPage.assertTrue(pagesTheme.verifyThemeForTitle(patientPage.notificationTitle, loadedTheme),"Checkpoint[18/20]","verifying the message info theme");
		patientPage.assertTrue(pagesTheme.verifyThemeForNotification(patientPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint[19/20]","verifying the theme for entire notification");
	
		//this wait is to check the closing behavior of notification after 5 sec
		patientPage.waitForTimePeriod(5000);		
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.notificationDiv),"Checkpoint[20/20]","verifying the notification is closed after 5 sec");
		patientPage.maximizeWindow();
		
		

	}
	
	@Test(groups ={"US2199","Positive","Chrome","Edge","F1094"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test04_US2199_TC9647_verifyBatchNotificationOnViewer(String theme) throws InterruptedException, ParseException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that notification on viewer page for 'new result(s) available for this patient/study' is appearing  when a already sent result is sent again through Postman.'");

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
		patientPage.assertFalse(patientPage.verifyPatientIsPresent(cpuPatientName),"Checkpoint[1/16]","verifying no patient is present");

		JSONParser jsonParser = new JSONParser();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchMessage.json"));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Batch");
		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_URL, jsonObject.toString());
		patientPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[2/16]","verifying the response is 200 after sending the study");
		patientPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[3/16]","verifying the response message is empty");

		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[4.1/16]","verifying the notification type");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+machine+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[4.2/16]","verifying the notification message is displayed upon sending of new study");
		patientPage.click(patientPage.closeIconOnNotification.get(0));
		
		patientPage.clickOnStudy(1);

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
	
		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC9506/TC9506_NewBatchResultMessage.json"));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Sending the Study");
		response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.WIA_GATE_ACCESS_URL, OrthancAndAPIConstants.NEW_BATCH_RESULT_URL, jsonObject.toString());
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[5/16]","verifying the response code after sending the result");
		loginPage.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[6/16]","verifying that there is no response message displaye");

		//New results are available		
		viewerpage.waitForElementVisibility(viewerpage.notificationDiv);

		viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationTitle.get(0)),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[7.1/16]","verifying the message type is info");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationMessage.get(0)),ViewerPageConstants.NEW_RESULT_NOTIFICATION_MESSAGE,"Checkpoint[7.2/16]","verifying the notification message is displayed upon sending of results");
		pagesTheme = new PagesTheme(driver);
		viewerpage.assertTrue(pagesTheme.verifyThemeForText(viewerpage.notificationMessage.get(0), loadedTheme),"Checkpoint[8/16]","verifying the message theme");
		viewerpage.assertTrue(pagesTheme.verifyThemeForTitle(viewerpage.notificationTitle.get(0), loadedTheme),"Checkpoint[9/16]","verifying the info theme");
		viewerpage.assertTrue(pagesTheme.verifyThemeForNotification(viewerpage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme),"Checkpoint[10/16]","verifying the notification theme");
		
		viewerpage.waitForTimePeriod(10000);
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Checkpoint[11/16]","verifying the response message is empty");

		viewerpage.click(viewerpage.cancelButton.get(0));
		contentSelector = new ContentSelector(driver);
		viewerpage.assertEquals(contentSelector.getAllSeries().size(), 1, "Checkpoint[12/16]", "verifying the same source series are displayed");
		viewerpage.assertEquals(contentSelector.getAllResults().size(), 1, "Checkpoint[13/16]", "verifying results are displayed");

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();

		patientPage.assertEquals(patientPage.getResultStatus(),PatientPageConstants.RESULT_STATUS+" "+PatientPageConstants.EUREKA_RESULT_STATUS_DONE,"Checkpoint[14/16]","verifying the result status is changed from pending to run with findings after sending the result");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[15/16]","verifying pending icon is not displayed");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[16/16]","verifying Liaison icon is displayed");
		


	}
			
	@AfterClass(alwaysRun=true)
	public void deletePatientFromOrthanc() throws SQLException {

		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		sd.deleteAllPatients();
	}


}
