package com.trn.ns.test.logs;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class UserActionLogsTest extends TestBase{


	//	ViewerPage viewerpage;
	DatabaseMethodsADB db;
	DatabaseMethods nsdb;
	private ExtentTest extentTest;
	private LoginPage loginPage;
	private CircleAnnotation circle ;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	String NSDBName = Configurations.TEST_PROPERTIES.get("dbName");

	private String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath9 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath9);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String mammoFilePath = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, mammoFilePath);
	String IHEMammo_result=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, mammoFilePath);

	String filePath4 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String MultiSeriesPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	String firstSeries= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath4);
	
	private PatientListPage patientPage;

	private ViewerLayout viewerLayout;
	private ViewerSliderAndFindingMenu findingMenu;
	private HelperClass helper;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test01_US999_TC4413_verifyLogsAreTracked() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that action log are getting tracked in DB");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();
		

		db = new DatabaseMethodsADB(driver);

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);

		List<String> startPayload = db.getPayload();

		viewerLayout.assertTrue(startPayload.size() > 1, "Checkpoint[1/1]", "Verifying the logs are generated in DB");


	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test02_US999_TC4436_TC4413_VerifyStudyUnloadAndLogsAreBeingstrackedUnderDB() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verfiy that Unload study action is getting logged when user navigates to Study list page from Viewer page."
				+ "<br> Verify that action log are getting tracked in DB");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);

		db = new DatabaseMethodsADB(driver);
		
		
		String studyDescription = patientPage.getStudyDescription(0);
		String studyInstanceID = db.getStudyInstanceUID(patientName);
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.waitForAllImagesToLoad();

		//Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		viewerLayout.assertEquals(startPayload.size(), 1, "Checkpoint[1/6]", "Verifying there is one entry for start study load as there is one study present for patient");
		viewerLayout.assertEquals(endPayload.size(), 1, "Checkpoint[2/6]", "Verifying there is one entry for end study load as there is one study present for patient");	

		String layout = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);
		String modality =db.getKeyValue(startPayload.get(0), ActionLogConstant.MODALITIES);

		db.assertTrue(db.verifyStudyStartAndEnd(startPayload, endPayload,studyInstanceID, ActionLogConstant.TWO_BY_TWO_LAYOUT, studyDescription), "Checkpoint[3/6]", "Verifying the properties of study start load");
		db.assertEquals(layout,ActionLogConstant.TWO_BY_TWO_LAYOUT,"Checkpoint[4/6]","Verifying the layout for the study");


		//Verfiy that Unload study action is getting logged when user navigates to Study list page from Viewer page.
		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName,1,1);
		
		

		List<String> unloadStudyPayload = db.getPayload(ActionLogConstant.UNLOAD_STUDY);
		db.assertEquals(unloadStudyPayload.size(),2, "Checkpoint[5/6]", "Verifying the study unload entry present in logs");
		db.assertTrue(db.verifyUnloadStudy(unloadStudyPayload, studyInstanceID, studyDescription, modality), "Checkpoint[6/6]", "Verifying the study unload when user navigates back to study page");


	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test03_DE1157_TC4773_verifyLoadStudyStartAndEnd() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);


		
		String studyDescription = patientPage.getStudyDescription(0);
		patientPage.clickOntheFirstStudy();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.waitForAllImagesToLoad();

		String studyInstanceID = db.getStudyInstanceUID(patientName);

		//Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		viewerLayout.assertEquals(startPayload.size(), 1, "Checkpoint[1/3]", "Verifying the start study payload size");
		viewerLayout.assertEquals(endPayload.size(), 1, "Checkpoint[2/3]", "Verifying the end study payload size");	
		String layout = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);

		db.assertTrue(db.verifyStudyStartAndEnd(startPayload, endPayload,studyInstanceID, ActionLogConstant.TWO_BY_TWO_LAYOUT, studyDescription), "Checkpoint[3/3]", "Verifying the Payload for start and end study payload");
		db.assertEquals(layout,ActionLogConstant.TWO_BY_TWO_LAYOUT,"Checkpoint[4]","Verifying the layout");



	}


	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test04_DE1157_TC4774_verifyLoadSeriesStartAndEnd() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart, LoadSeriesEnd and unloadSeries logs (when user changes series from content selector) contains loadSeriesActionID and loadStudyActionID");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();


		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.waitForAllImagesToLoad();

		int totalViewboxes = viewerLayout.getNumberOfCanvasForLayout();

		//Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		String layout = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);

		//Verify LoadSeriesStart and LoadSeriesEnd logs (when user access the viewer page) contains loadSeriesActionID and loadStudyActionID
		viewerLayout.assertEquals(db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START).size(), totalViewboxes, "Checkpoint[1/4]", "Verifying the Load series start count is equals to number of viewboxes");
		viewerLayout.assertEquals(db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END).size(), totalViewboxes, "Checkpoint[2/4]", "Verifying the Load series end count is equals to number of viewboxes");		

		List<String> ids = new ArrayList<String>();
		String seriesUID ="";
		for(int i=1;i<=totalViewboxes;i++) {			

			seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(i));
			startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);			
			endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);

			ids.add(db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID));
			ids.add(db.getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID));

			db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "true", "false", "false", ActionLogConstant.APPLICATION_DICOM, layout), "Checkpoint[3/4]", "Verifying the start and end series payload details");
		}

		viewerLayout.assertEquals(ids.stream().distinct().count(), totalViewboxes, "Checkpoint[4/4]", "Verifying the unique series action id");	


	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test05_DE1157_TC4775_verifyLoadSeriesStartAndEndOnLayoutChange() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart and LoadSeriesEnd logs (when user changes layout) contains loadSeriesActionID and loadStudyActionID");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);


		patientPage.clickOntheFirstStudy();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.waitForAllImagesToLoad();

		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);
		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);

		//		Verify LoadSeriesStart and LoadSeriesEnd logs (when user changes layout) contains loadSeriesActionID and loadStudyActionID
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		List<WebElement> layouts = new ArrayList<>(Arrays.asList(viewerLayout.twoByOneLayoutIcon,viewerLayout.twoByTwoLayoutIcon));
	
		List<String> layoutName = new ArrayList<>(Arrays.asList(ActionLogConstant.TWO_BY_ONE_LAYOUT,ActionLogConstant.TWO_BY_TWO_LAYOUT));
		System.out.println(layoutName.get(0));
		System.out.println(layoutName.get(1));
		String seriesUID ="";

		for(int j =0;j<layouts.size();j++) {

			viewerLayout.selectLayout(layouts.get(j));
			viewerLayout.waitForViewerpageToLoad(1);
			viewerLayout.waitForAllImagesToLoad();

			for(int i=1;i<=viewerLayout.getNumberOfCanvasForLayout();i++) {			

				seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(i));
				startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
				endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);
				db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "false", "true", "false", ActionLogConstant.APPLICATION_DICOM, layoutName.get(j)), "Checkpoint[1/1]", "Verifying the Series start and end load payload post layout change");

			}

			db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		}




	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test06_DE1157_TC4776_verifyLoadSeriesStartAndEndUsingContentSelector() throws SQLException, InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart, LoadSeriesEnd and unloadSeries logs (when user changes series from content selector) contains loadSeriesActionID and loadStudyActionID");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);


		patientPage.clickOntheFirstStudy();

		db = new DatabaseMethodsADB(driver);
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);

		//Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		String layout = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);
		String seriesUID ="";

		//Verify LoadSeriesStart, LoadSeriesEnd and unloadSeries logs (when user changes series from content selector) contains loadSeriesActionID and loadStudyActionID

		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(1));

		cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(1, viewerLayout.getSeriesDescriptionOverlayText(2));

		List<String> unloadSeriesPayload = db.getPayload(ActionLogConstant.UNLOAD_SERIES,seriesUID);
		db.assertTrue(db.verifyUnloadSeries(unloadSeriesPayload, seriesUID, ActionLogConstant.APPLICATION_DICOM, ActionLogConstant.ONE_BY_ONE_VIEWBOXNO), "Checkpoint[1/2]", "Verifying the unload series when series is selected using content selector");

		seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(2));
		startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
		endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);

		db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "false", "false", "true", ActionLogConstant.APPLICATION_DICOM, layout), "Checkpoint[2/2]", "Verifying that payload for new series selected using content selector for start and load payload");



	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test07_DE1157_TC4777_verifyLoadStudyStartAndEndNonUI() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadStudyStart and LoadStudyEnd logs (when viewer page is accessed through non-UI login) contains loadStudyActionID");

		patientPage = new PatientListPage(driver);
		
		Header header = new Header(driver);
		header.logout();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		String studyUId = db.getStudyInstanceUID(patientName);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		
		//Accessing patient's study page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verifying the launching of patient's study page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUId, hm);
		loginPage.navigateToURL(myURL);		
		
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.waitForAllImagesToLoad();
		viewerLayout.assertTrue(viewerLayout.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[2/7] :Verify viewerpage is displayed", viewerLayout.getCurrentPageURL()+" is displaying");

		String studyInstanceID = db.getStudyInstanceUID(patientName);		
		String studyDescription = db.getStudyDescription(patientName);

		//Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		db.assertEquals(startPayload.size(), 1, "Checkpoint[3/7]", "Verifying that study loaded started");
		db.assertEquals(endPayload.size(), 1, "Checkpoint[4/7]", "verifying the study loading ended");	

		String layout = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);

		db.assertTrue(db.verifyStudyStartAndEnd(startPayload, endPayload,studyInstanceID, ActionLogConstant.TWO_BY_TWO_LAYOUT, studyDescription), "Checkpoint[5/7]", "verifying all the details of study starta dn end payload");
		db.assertEquals(layout,ActionLogConstant.TWO_BY_TWO_LAYOUT,"Checkpoint[6/7]","Verifying the layout");
		db.assertTrue(db.getModailitiesInStudy(patientName).contains(db.getKeyValue(startPayload.get(0), ActionLogConstant.MODALITIES)),"Checkpoint[7/7]","verifying the modalities");

		


	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test08_DE1157_TC4781_verifyLoadSeriesStartAndEndNonUI() throws SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart and LoadSeriesEnd logs (when viewer page is accessed through non-UI login) contains loadStudyActionID and loadSeriesActionId");

		patientPage = new PatientListPage(driver);
		
		Header header = new Header(driver);
		header.logout();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		String studyUId = db.getStudyInstanceUID(patientName);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		
		//Accessing patient's study page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying the launching of patient's study page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUId, hm);
		loginPage.navigateToURL(myURL);		
		
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.assertTrue(viewerLayout.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[2/6] Verify viewerpage is displayed", viewerLayout.getCurrentPageURL()+" is displaying");

		int totalViewboxes = viewerLayout.getNumberOfCanvasForLayout();

		//Verify LoadStudyStart and LoadStudyEnd logs (when user selects the study on study list page) contains loadStudyActionID
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		String layout = db.getKeyValue(startPayload.get(0), ActionLogConstant.LAYOUT);

		//Verify LoadSeriesStart and LoadSeriesEnd logs (when user access the viewer page) contains loadSeriesActionID and loadStudyActionID
		viewerLayout.assertEquals(db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START).size(), totalViewboxes, "Checkpoint[3/6]", "Verifying the total number of events for load seires start ");
		viewerLayout.assertEquals(db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END).size(), totalViewboxes, "Checkpoint[4/6]", "Verifying the total number of events for load seires end ");		

		List<String> ids = new ArrayList<String>();
		String seriesUID ="";
		for(int i=1;i<=totalViewboxes;i++) {			

			seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(i));
			startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);			
			endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);

			ids.add(db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID));
			ids.add(db.getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID));

			db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "true", "false", "false", ActionLogConstant.APPLICATION_DICOM, layout), "Checkpoint[5/6]", "verifying the details of all the series loaded");
		}

		viewerLayout.assertEquals(ids.stream().distinct().count(), totalViewboxes, "Checkpoint[6/6]", "verifying the distinct series action id");	


	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test09_DE1157_TC4779_verifyLoadSeriesStartAndEndUsingContentSelectorInEmptyViewbox() throws SQLException, InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart and LoadSeriesEnd logs (when series is selected into empty viewbox) contains loadSeriesActionID and loadStudyActionId");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4patientName);
		patientPage.clickOntheFirstStudy();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);

		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);
		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		
		viewerLayout.selectLayout(viewerLayout.threeByThreeLayoutIcon);
		viewerLayout.waitForViewerpageToLoad(1);		
    	db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

		cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(6, viewerLayout.getSeriesDescriptionOverlayText(2));
		String seriesUID = db.getSeriesInstanceUID(ah4patientName,viewerLayout.getSeriesDescriptionOverlayText(2));

		startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
		endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);
		
		db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "false", "false", "true", ActionLogConstant.APPLICATION_DICOM, ActionLogConstant.THREE_BY_THREE_LAYOUT), "checkpoint[1]", "verifying the series start and end when series is loaded in empty viewbox");


	}
		
	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test10_DE1157_TC4778_verifyLoadSeriesStartAndEndUsingContentSelectorPDFInEmptyViewbox() throws SQLException, InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart and LoadSeriesEnd logs (when PDF is selected into empty viewbox) contains loadSeriesActionID and loadStudyActionID");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4patientName);
		patientPage.clickOntheFirstStudy();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_START);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_STUDY_END);

		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		
		viewerLayout.selectLayout(viewerLayout.threeByThreeLayoutIcon);
		viewerLayout.waitForViewerpageToLoad(1);
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);		
		
		cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(6, viewerLayout.getSeriesDescriptionOverlayText(5));		
		 
		String seriesUID = db.getSeriesInstanceUID(ah4patientName,viewerLayout.getSeriesDescriptionOverlayText(5));
		startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
		endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);
		
		db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "false", "false", "true", ActionLogConstant.APPLICATION_PDF, ActionLogConstant.THREE_BY_THREE_LAYOUT), "checkpoint[1]", "verifying the series start and end payload when pdf is loaded in empty viewbox");


	}

	@Test(groups ={"Chrome","IE11","Edge","US999","Positive"})
	public void test11_DE1157_TC4780_verifyLoadSeriesStartAndEndUsingContentSelectorUsinngNonUI() throws SQLException, InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify LoadSeriesStart, LoadSeriesEnd and UnloadSeries logs (when series is replaced through content selector when viewer page is accessed through non UI login URL ) contains loadSeriesActionID and loadStudyActionId");

		patientPage = new PatientListPage(driver);		
		Header header = new Header(driver);
		header.logout();

		// Need to put this in after method
		db = new DatabaseMethodsADB(driver);
		String studyUId = db.getStudyInstanceUID(patientName);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		
		//Accessing patient's study page url from non ui login
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the launching of patient's study page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.VIEWER_PAGE_URL+"/"+studyUId, hm);
		loginPage.navigateToURL(myURL);		
		
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(1);
		viewerLayout.assertTrue(viewerLayout.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[2/4] Verify viewerpage is displayed", viewerLayout.getCurrentPageURL()+" is displaying");

		db = new DatabaseMethodsADB(driver);
		String seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(1));
		List<String> startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
		String studyActionID = db.getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID);
		
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);

	    cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(1, viewerLayout.getSeriesDescriptionOverlayText(2));

		List<String> unloadSeriesPayload = db.getPayload(ActionLogConstant.UNLOAD_SERIES,seriesUID);
		db.assertTrue(db.verifyUnloadSeries(unloadSeriesPayload, seriesUID, ActionLogConstant.APPLICATION_DICOM, ActionLogConstant.ONE_BY_ONE_VIEWBOXNO), "Checkpoint[3/4]", "verifying the unload series when viewer is launch using non ui and series is loaded in non empty viewbox");

		seriesUID = db.getSeriesInstanceUID(patientName,viewerLayout.getSeriesDescriptionOverlayText(2));
		startPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_START,seriesUID);
		List<String> endPayload = db.getPayload(ActionLogConstant.USER_ACTION_LOAD_SERIES_END,seriesUID);

		db.assertTrue(db.verifySeriesStartAndEnd(startPayload, endPayload, studyActionID, "false", "false", "true", ActionLogConstant.APPLICATION_DICOM, ActionLogConstant.TWO_BY_TWO_LAYOUT), "Checkpoint[4/4]", "verifying the unload series when viewer is launch using non ui and series is loaded in non empty viewbox");
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","US1069","DE1412","Positive" })
	public void test12_US1069_TC5124_DE1412_TC7346_verifyUserActionLogWhenSRAcceptedOrRejected() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify logs when SR report is accepted or rejected. <br>"+
		"Verify in UserActionLog table, '/resultType'/ is updated in Payload column for UserActionType=resultStatusChange when the status of SR is changed.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ChestCT1p25mmPatient);
		DatabaseMethodsADB db=new DatabaseMethodsADB(driver);
		
		patientPage.clickOntheFirstStudy();

		String studyInstanceUID = db.getStudyInstanceUID(ChestCT1p25mmPatient);
	
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(2);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		int imageNumber=1;
		viewerLayout.closeNotification();
		String seriesDes=viewerLayout.getSeriesDescriptionOverlayText(1);

		//verify A/R toolbar present on SR report
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify accept reject tool bar is displayed on SR report");
		viewerLayout.click(viewerLayout.getViewPort(1));
		viewerLayout.mouseHover(viewerLayout.getViewPort(1));
		findingMenu.getGSPSHoverContainer(1);
		viewerLayout.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Verifying AR tool bar is present or not","AR tool bar is present.");
		
		findingMenu.selectAcceptfromGSPSRadialMenu();
		
		List<String> resultStatusChange = db.getPayload(ActionLogConstant.USER_ACTION_RESULT_STATUS_CHANGE);
		viewerLayout.assertEquals(resultStatusChange.size(), 1, "Checkpoint[2/3]", "Verify there is one entry for result statue change when user accept the SR report");
		
		String modality=ActionLogConstant.SR_MODALITY;
		String mimetype=db.getMimeType(seriesDes, imageNumber, modality);
		String seriesInstanceUID = db.getSeriesInstanceUID(ChestCT1p25mmPatient, seriesDes,modality);
		String batchUID=db.getBatchUIDFromBatchTable(ChestCT1p25mmPatient);
		String feedback=db.getUserFeedback(ChestCT1p25mmPatient, seriesDes);
		String sopClassUID=db.getSopClassUID(seriesDes, imageNumber, modality);
		String sopInstanceUID=db.getSopInstanceUID(seriesDes, imageNumber,modality);
	
		db.assertTrue(db.verifyResultStatusChange(resultStatusChange, sopInstanceUID, sopClassUID, studyInstanceUID, seriesInstanceUID, seriesDes, batchUID, mimetype, feedback,ActionLogConstant.SR_RESULT_TYPE), "Checkpoint[3/3]", "Verify 'resultStatusChange' UserActionType generated in 'UserActionLog' table");
	
	
	}
	
	//User action Log for Secondary capture data
	//US1075 Show regular A/R toolbar for DICOM-SC result
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US1075","Sanity","Positive"})
	public void test13_US1075_TC5343_verifyUserActionLogForGSPSFindingNavigation() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the UserActionLog for navigation when navigating over the GSPS findings.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbio_PatientName);
	
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(3);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		circle = new CircleAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		db = new DatabaseMethodsADB(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify user can draw annotation on secondary capture data" );
		
		//draw 2 annotation on secondary capture data
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);
		//verify A/R toolbar after annotation drawn
		viewerLayout.assertTrue(viewerLayout.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[2/4]","Verified accept reject tool when mousehover after drawing annotation");
		
		//select previous from A/R toolbar and verify user action log
		findingMenu.selectPreviousfromGSPSRadialMenu();
		List<String> navigationFindingForPrevious=db.getPayload(ActionLogConstant.FINDING_NAVIGATION);
		
		db.assertTrue(db.verifyFindingNavigationLogs(navigationFindingForPrevious,ActionLogConstant.GSPS_PREV, ViewerPageConstants.CIRCLE, ActionLogConstant.GSPS, ViewerPageConstants.LINEAR_MEASUREMENT, ActionLogConstant.GSPS), "Checkpoint[3/4]", "Verified user action log for navigation finding as Previous");
		
		//truncate User action log table
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		
		//select next from A/R toolbar and verify user action log
		findingMenu.selectNextfromGSPSRadialMenu();
		List<String> navigationFindingForNext=db.getPayload(ActionLogConstant.FINDING_NAVIGATION);
		db.assertTrue(db.verifyFindingNavigationLogs(navigationFindingForNext,ViewerPageConstants.GSPS_NEXT,  ViewerPageConstants.LINEAR_MEASUREMENT, ActionLogConstant.GSPS ,ViewerPageConstants.CIRCLE, ActionLogConstant.GSPS), "Checkpoint[4/4]", "Verified user action log for navigation finding as Next");
		
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US1075","Sanity","Postive"})
	public void test14_US1075_TC5344_verifyUserActionLogForNavigationFromGSPSToDICOMSC() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the UserActionLog for the DICOM SC tool bar when navigating from GSPS finding to DICOM SC result.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbio_PatientName);
	
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(3);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		circle = new CircleAnnotation(driver);
		db = new DatabaseMethodsADB(driver);
		
		String resultName=viewerLayout.getSeriesDescriptionOverlayText(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify user can draw annotation on secondary capture data" );
		lineWithUnit=new MeasurementWithUnit(driver);
 		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);
		viewerLayout.assertTrue(viewerLayout.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[2/4]","Verified accept reject tool when mousehover after drawing annotation");
		
		findingMenu.selectPreviousfromGSPSRadialMenu();
		findingMenu.selectNextfromGSPSRadialMenu();
		
		//truncate user action log table
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		
		//navigate from GSPS to Secondary capture data and verify User action log
		findingMenu.selectNextfromGSPSRadialMenu();
		viewerLayout.waitForTimePeriod(2000);
		List<String> hideGSPSResults=db.getPayload(ActionLogConstant.USER_ACTION_HIDING_GSPS_RESULT);
		List<String> navigationFindingForPrevious=db.getPayload(ActionLogConstant.FINDING_NAVIGATION);
		
		//verify user action log for Finding navigation and Hiding GSPS Results
	    db.assertTrue(db.verifyFindingNavigationLogs(navigationFindingForPrevious,ViewerPageConstants.GSPS_NEXT, ViewerPageConstants.CIRCLE, ActionLogConstant.GSPS,resultName, ActionLogConstant.SC_RESULT_TYPE), "Checkpoint[3/4]", "Verified user action log when navigate from GSPS to Secondary capture data");
	    db.assertTrue(db.verifyHidingGSPSResultActionLog(hideGSPSResults,NSGenericConstants.BOOLEAN_TRUE), "Checkpoint[4/4]", "Verified user action log for Hiding GSPS results");
		
	
		
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US1075","DE1412","Sanity","Positive"})
	public void test15_US1075_TC5346_DE1412_TC7346_verifyUserActionLogForDICOMSCAcceptReject() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the UserActionLoad for 'Accept' and 'Reject' button on DICOM SC AR Tool bar.<br>"+
		"Verify in UserActionLog table, 'resultType' is updated in Payload column for UserActionType=resultStatusChange when the status of SR is changed.");

		db = new DatabaseMethodsADB(driver);
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbio_PatientName);
	
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(3);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		circle = new CircleAnnotation(driver);
		
		String studyInstanceUID = db.getStudyInstanceUID(imbio_PatientName);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		String seriesDes=viewerLayout.getSeriesDescriptionOverlayText(1);
		int imageNumber=1;
		String modality=ActionLogConstant.SC_MODALITY;
		String mimetype=db.getMimeType(seriesDes, imageNumber, modality);
		String seriesInstanceUID = db.getSeriesInstanceUID(imbio_PatientName, seriesDes,modality);
		String batchUID=db.getBatchUIDFromBatchTable(imbio_PatientName);
		String sopClassUID=db.getSopClassUID(seriesDes, imageNumber, modality);
		String sopInstanceUID=db.getSopInstanceUID(seriesDes, imageNumber,modality);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/]",  "Verify user accept the secondary capture result");
		findingMenu.acceptResult(1);

		String newState1=db.getUserFeedback(imbio_PatientName, seriesDes);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]",  "Verify result change status when accept or reject SC data" );
		
		List<String> resultStatusChange=db.getPayload(ActionLogConstant.USER_ACTION_RESULT_STATUS_CHANGE);
		db.assertTrue(db.verifyResultStatusChange(resultStatusChange, sopInstanceUID, sopClassUID, studyInstanceUID, seriesInstanceUID, seriesDes, batchUID, mimetype, newState1,ActionLogConstant.SC_RESULT_TYPE), "Checkpoint[3/4]", "Verified user action log when SC result accepted");
		
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		db.truncateTable(NSDBName,NSDBDatabaseConstants.USERFEEDBACKTABLE);
		findingMenu.rejectResult(1);
		
		String newState2=db.getUserFeedback(imbio_PatientName, seriesDes);
		
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify drawn annotation is in accepted state from output panel" );
	    resultStatusChange=db.getPayload(ActionLogConstant.USER_ACTION_RESULT_STATUS_CHANGE);
		db.assertTrue(db.verifyResultStatusChange(resultStatusChange, sopInstanceUID, sopClassUID, studyInstanceUID, seriesInstanceUID, seriesDes, batchUID, mimetype, newState2,ActionLogConstant.SC_RESULT_TYPE), "Checkpoint[4/4]", "Verified user action log when SC result rejected");
	}
	
	@Test(groups ={"firefox","Chrome","IE11","Edge","US1075","Sanity"})
	public void test16_US1075_TC5349_verifyUserActionLogForDICOMSCFindingMenu() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the correct UserActionLog are getting captured when user selecting the DICOM SC result from finding menu.");

		db = new DatabaseMethodsADB(driver);
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbio_PatientName);
	
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(3);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		circle = new CircleAnnotation(driver);
		cs=new ContentSelector(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		String resultDes=viewerLayout.getSeriesDescriptionOverlayText(1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Draw annotation and select SC result from Finding menu" );
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);
		circle.closingConflictMsg();
		
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		viewerLayout.click(findingMenu.gspsFinding);
		String findingCount=Integer.toString(findingMenu.findings.size());
		findingMenu.selectFindingFromTable(1);
		viewerLayout.assertTrue(cs.verifyPresenceOfEyeIcon(resultDes), "Checkpoin[2/5]", "Verified result seen as selected in content selector when SC fining selected from Finding menu");
		
		List<String> findingDropdown=db.getPayload(ActionLogConstant.USER_ACTION_FINDINGS_DROPDOWN);
		List<String> hideGSPSResults=db.getPayload(ActionLogConstant.USER_ACTION_HIDING_GSPS_RESULT);
		List<String> selectFinding=db.getPayload(ActionLogConstant.USER_ACTION_SELECT_FINDING);
		
		viewerLayout.assertTrue(db.verifyFindingDropdownActionLog(findingDropdown, findingCount), "Checkpoint[3/5]", "Veriy total number of findings display in finding dropdown");
		viewerLayout.assertTrue(db.verifyHidingGSPSResultActionLog(hideGSPSResults, NSGenericConstants.BOOLEAN_TRUE), "Checkpoint[4/5]", "Verify total number of findings display in finding dropdown");
		viewerLayout.assertTrue(db.verifySelectFindingActionLog(selectFinding, ActionLogConstant.SC_RESULT_TYPE), "Checkpoint[5/5]", "Veriy total number of findings display in finding dropdown");	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","US1069","DE1412","Positive" })
	public void test17_DE1412_TC7346_verifyUserActionLogWhenCADSRAccepted() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify in UserActionLog table, '/resultType'/ is updated in Payload column for UserActionType=resultStatusChange when the status of Mammo CAD SR data is changed.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(IHEMammoTestPatientName);
		DatabaseMethodsADB db=new DatabaseMethodsADB(driver);
		
		patientPage.clickOntheFirstStudy();
		String studyInstanceUID = db.getStudyInstanceUID(IHEMammoTestPatientName);
	
		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(2);
		cs=new ContentSelector(driver);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		
		cs.selectResultFromSeriesTab(1, IHEMammo_result, 1);

		//verify A/R toolbar present on SR report
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify accept reject tool bar is displayed on CAD SR report");
		viewerLayout.assertTrue(findingMenu.verifyPendingGSPSToolbarMenu(),"Verifying AR tool bar is present or not","AR tool bar is present.");
		
		findingMenu.selectAcceptfromGSPSRadialMenu();
		
		List<String> findingStatusChange = db.getPayload(ActionLogConstant.USER_ACTION_FINDING_STATUS_CHANGE);
		viewerLayout.assertEquals(findingStatusChange.size(), 1, "Checkpoint[2/3]", "Verify there is one entry for result statue change when user accept the CAD SR report");
		
		String modality=ActionLogConstant.SR_MODALITY;
		String mimetype=db.getMimeType(IHEMammo_result,0, modality);
		String seriesInstanceUID = db.getSeriesInstanceUID(IHEMammoTestPatientName, IHEMammo_result,modality);
		String batchUID=db.getBatchUIDFromBatchTable(IHEMammoTestPatientName);
		String feedback=ViewerPageConstants.ACCEPTED_TEXT;
	
		db.assertTrue(db.verifyFindingStatusChange(findingStatusChange, studyInstanceUID, seriesInstanceUID, IHEMammo_result, batchUID, mimetype, feedback,ActionLogConstant.CAD_SR_RESULT_TYPE), "Checkpoint[3/3]", "Verify 'resultStatusChange' UserActionType generated in 'UserActionLog' table");
		
	
	}

	//US1502: Save custom layout per machine for a user
	@Test(groups = { "Chrome", "IE11", "Edge","US1502","Positive"})
	public void test18_US1502_TC7985_verifyUserActionLogForSaveLayoutAndReset() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to save the updated layout for the study.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(MultiSeriesPatientName);
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);

		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		viewerLayout.waitForViewerpageToLoad(2);
		nsdb=new DatabaseMethods(driver);
		db=new DatabaseMethodsADB(driver);
		
		String studyInstanceUID =db.getStudyInstanceUID(MultiSeriesPatientName);
		int viewbox=viewerLayout.getNumberOfCanvasForLayout();
		
		//verify change layout and the click on save layout only for non-empty viewbox
	
	    viewerLayout.selectLayout(viewerLayout.twoByTwoLayoutIcon);
	    viewerLayout.assertNotEquals(viewerLayout.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[1/5]","Verified number of canvas after layout change.");
	    viewerLayout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);

		List<String> saveLayout = db.getPayload(ActionLogConstant.USER_ACTION_FOR_SAVE_LAYOUT);
		viewerLayout.assertEquals(saveLayout.size(), 1, "Checkpoint[2/5]", "Verify there is one entry for save layout.");
		db.assertTrue(db.verifyActionLogForSaveLayout(saveLayout, ViewerPageConstants.TWO_BY_ONE_LAYOUT, ViewerPageConstants.TWO_BY_TWO_LAYOUT, username, studyInstanceUID, NSDBDatabaseConstants.SYNC_OFF), "Checkpoint[3/5]", "Verify 'saveLayout' UserActionType generated in 'UserActionLog' table.");
		
	    viewerLayout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerLayout.waitForTimePeriod(2000);
		
		List<String> resetLayout = db.getPayload(ActionLogConstant.USER_ACTION_FOR_RESET_LAYOUT);
		viewerLayout.assertEquals(resetLayout.size(), 1, "Checkpoint[4/5]", "Verify there is one entry reset layout.");
		db.assertTrue(db.verifyActionLogForResetLayout(resetLayout, ViewerPageConstants.TWO_BY_TWO_LAYOUT, ViewerPageConstants.TWO_BY_ONE_LAYOUT, username, studyInstanceUID,db.getMachineIDFromMachineTable(machineName)), "Checkpoint[5/5]", "Verify 'resetLayout' UserActionType generated in 'UserActionLog' table.");
		
		
	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","US1502","Positive"})
	public void test19_US1502_TC7985_verifyUserActionLogForSaveLayoutWithContentAndReset() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to save the updated layout for the study.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(MultiSeriesPatientName);
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		
		viewerLayout.waitForViewerpageToLoad(2);
		nsdb=new DatabaseMethods(driver);
		db=new DatabaseMethodsADB(driver);
		
		String studyInstanceUID =db.getStudyInstanceUID(MultiSeriesPatientName);
		int viewbox=viewerLayout.getNumberOfCanvasForLayout();
		
		//verify change layout so that different batch loaded on viewer
	    viewerLayout.selectLayout(viewerLayout.threeByThreeLayoutIcon);
	    cs=new ContentSelector(driver);
	    cs.selectSeriesFromSeriesTab(5, firstSeries);
	    viewerLayout.assertNotEquals(viewerLayout.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[1/5]","Verified number of canvas after layout change.");
	    viewerLayout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
	  
		List<String> saveLayout = db.getPayload(ActionLogConstant.USER_ACTION_FOR_SAVE_LAYOUT);
		viewerLayout.assertEquals(saveLayout.size(), 1, "Checkpoint[2/5]", "Verify there is one entry for save layout.");
		db.assertTrue(db.verifyActionLogForSaveLayout(saveLayout, ViewerPageConstants.TWO_BY_ONE_LAYOUT, ViewerPageConstants.THREE_BY_THREE_LAYOUT, username, studyInstanceUID, NSDBDatabaseConstants.SYNC_ON), "Checkpoint[3/5]", "Verify 'saveLayout' UserActionType generated in 'UserActionLog' table.");
		
		viewerLayout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerLayout.waitForTimePeriod(2000);
		
		List<String> resetLayout = db.getPayload(ActionLogConstant.USER_ACTION_FOR_RESET_LAYOUT);
		viewerLayout.assertEquals(resetLayout.size(), 1, "Checkpoint[4/5]", "Verify there is one entry reset layout.");
		db.assertTrue(db.verifyActionLogForResetLayout(resetLayout, ViewerPageConstants.THREE_BY_THREE_LAYOUT, ViewerPageConstants.TWO_BY_ONE_LAYOUT, username, studyInstanceUID,db.getMachineIDFromMachineTable(machineName)), "Checkpoint[5/5]", "Verify 'reset Layout' UserActionType generated in 'UserActionLog' table.");

		
	
	}
	
	//DR2220: Zoom, pan, windowing action logs are captured on every mouse move
	@Test(groups = { "Chrome", "IE11", "Edge","US1502","Positive"})
	public void test20_DR2220_TC8985_verifyUserActionLogForZoomPanAndWindowing() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that one log entry is created for each of the Zoom, pan, windowing actions.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();

		viewerLayout = new ViewerLayout(driver);
		
		viewerLayout.waitForViewerpageToLoad(2);
		db=new DatabaseMethodsADB(driver);

		viewerLayout.selectZoomFromQuickToolbar(viewerLayout.getViewPort(1));
		viewerLayout.dragAndReleaseOnViewer(0, 0, 0, -10);
		List<String> zoomActionLog = db.getPayload(ActionLogConstant.USER_ACTION_FOR_ZOOM);
		viewerLayout.assertEquals(zoomActionLog.size(), 1, "Checkpoint[1/3]", "Verify there is one log entry for ZOOM action in User Log table.");
		
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		viewerLayout.selectWindowLevelFromQuickToolbar(viewerLayout.getViewPort(1));
		viewerLayout.dragAndReleaseOnViewer(viewerLayout.getViewPort(1),100, 100, 200 , 150);
		List<String> windowingActionLog = db.getPayload(ActionLogConstant.USER_ACTION_FOR_WINDOWING);
		viewerLayout.assertEquals(windowingActionLog.size(), 1, "Checkpoint[2/3]", "Verify there is one log entry for Windowing action in User Log table.");
		
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		viewerLayout.selectPanFromQuickToolbar(viewerLayout.getViewPort(1));
		viewerLayout.dragAndReleaseOnViewer( 0, 0, -50,-50);
		
		List<String> panActionLog = db.getPayload(ActionLogConstant.USER_ACTION_FOR_PAN);
		viewerLayout.assertEquals(panActionLog.size(), 2, "Checkpoint[3/3]", "Verify there is one log entry for PAN action in User Log table.");
	

	}
	
	//DR2362: Clicking clear button in the search logs pageNavigationEnd action
	@Test(groups = { "Chrome", "IE11", "Edge","DR2362","Positive"})
	public void test21_DR2362_TC9295_verifyUserActionLogForPageNavigationEndUserActionLog() throws InterruptedException, SQLException 
	 {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verifying pageNavigationEnd log is not getting logged on clicking on \'Clear\' button on Patient-Study page.");

		db=new DatabaseMethodsADB(driver);
		patientPage = new PatientListPage(driver);
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		
		patientPage.click(patientPage.clearButton);

		List<String> pageNavigationActionLog = db.getPayload(ActionLogConstant.USER_ACTION_FOR_PAGE_NAVIGATION);
		patientPage.assertTrue(pageNavigationActionLog.isEmpty(), "Checkpoint[1/4]", "Verify there is no log entry for pageNavigationEnd  action in User Log table when click on clear button.");
		
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
	
		List<String>modalityList=patientPage.getModalities();
		patientPage.searchPatient("", modalityList.get(0), "", "");
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(modalityList.get(0)), "Checkpoint[2/4]", "Verified that modality is still selected after click on search button.");
		patientPage.click(patientPage.clearButton);
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(modalityList.get(0)), "Checkpoint[3/4]", "Verified that modality is deselected after click on clear button.");
		pageNavigationActionLog = db.getPayload(ActionLogConstant.USER_ACTION_FOR_PAGE_NAVIGATION);
		patientPage.assertTrue(pageNavigationActionLog.isEmpty(), "Checkpoint[4/4]", "Verify there is no log entry for pageNavigationEnd  action in User Log table when click on clear button.");
		
	}

	@AfterMethod(alwaysRun=true)
	public void clearUserActionTable() throws SQLException {

		db = new DatabaseMethodsADB(driver);
		db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		db.truncateTable(NSDBName, NSDBDatabaseConstants.USERFEEDBACKTABLE);
		db.deleteUserSetLayout();

	}

}