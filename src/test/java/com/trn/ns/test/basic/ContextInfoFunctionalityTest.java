package com.trn.ns.test.basic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.ErrorOrLogoutPage;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ContextInfoFunctionalityTest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private RegisterUserPage register;
	private ViewerPage viewerpage;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String userOne="userA";
	
	String accNo1="ryantest";
	String accNo2="ACNO1";
	
	//before importing patient set ContextInfoTag value as 0079,10E1 in Config setting table.
	String ryantest_filepath = Configurations.TEST_PROPERTIES.get("ryantest_Filepath");
	String ryanTest_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ryantest_filepath);
	String ryanTest_patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, ryantest_filepath);
	String studyDescription_ryan=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, ryantest_filepath);

	String fail320_filepath =Configurations.TEST_PROPERTIES.get("320fail_Filepath");
	String fail320_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, fail320_filepath);
	String fail320_patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, fail320_filepath);
	String studyDescription_320=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, fail320_filepath);
	
	String liver9_filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9_filePath);
	String liver9_patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9_filePath);
	String studyDescription_liver9=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, liver9_filePath);
	
	String bonage_filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String bonage_patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, bonage_filePath);
	String bonage_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, bonage_filePath);
	String seriesDesc=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, bonage_filePath);
	private ViewerLayout layout;
	private ViewerTextOverlays overlays;

	//Before execution of test case please follow below steps:
	 /*set the 'ContextInfoTag' to '0079,10E1' in 'ConfigSettings' table.
	 Restart IIS.
	 Import required set of data.
	 */

	@BeforeClass(alwaysRun=true)
	public void updateValueInDB() throws IOException, SQLException, InterruptedException {
        db=new DatabaseMethods(driver);
        db.updateConfigTable(NSDBDatabaseConstants.CONTEXT_INFO_TAG, NSDBDatabaseConstants.CONTEXT_INFO_TAG_VALUE);
        db.resetIISPostDBChanges();
//        db.resetIISPostDBChanges();
        db.updateContextInfo(ryanTest_patientName, NSDBDatabaseConstants.CONTEXT_INFO);
        db.updateContextInfo(fail320_patientName, NSDBDatabaseConstants.CONTEXT_INFO);
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    register = new RegisterUserPage(driver);
	    register.waitForRegisterPageToLoad();
	    register.createNewUser(userOne, userOne, NSDBDatabaseConstants.CONTEXT_INFO, userOne, userOne, userOne);
	    loginPage.logout();
	
	}

	//Verify different scenarios when  ContextInfoEnabled flag is set to false in config setting table.
	@Test(groups ={"Chrome","Edge","IE11","US1520","US1521","Positive","BVT"})
	public void test01_01_US1520_TC8150_TC8161_US1521_TC8138_TC8158_verifyPatientAndStudyListPageForAdminUser() throws SQLException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the 'ContextInfo' field in 'PatientLevel' table for the imported data by setting the 'ContextInfoTag' value in 'ConfigSettings' table. <br>" +
		"Verify the 'ContextInfo' in PatientLevel is able to store the dicom tag value which is in byte form. <br>"+
		"Verify filtering the data on 'PatientList', 'PatientStudyList' and 'Studylist' pages for the logged in user  when 'ContextInfoEnabled' = 'False' in 'ConfigSettings' table. <br>"+
		"Verify non UI login in to NS using 'accessionNumber' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.");

		//TC8150
		DatabaseMethods db = new DatabaseMethods(driver);
		db.assertEquals(db.getContextInfo(ryanTest_patientName), NSDBDatabaseConstants.CONTEXT_INFO, "Checkpoint[1/13]", "Verified that context info value available for "+ryanTest_patientName);
		db.assertEquals(db.getContextInfo(fail320_patientName), NSDBDatabaseConstants.CONTEXT_INFO, "Checkpoint[2/13]","Verified that context info value available for "+fail320_patientName);
		db.assertTrue(db.getContextInfo(liver9_patientName).isEmpty(),  "Checkpoint[3/13]", "Verified that context info tag is empty for Liver 9 patient.");
		
		//TC8138: verify patient and study list page for Admin user
		db.updateAccessionNoInStudyTable(ryanTest_patientName, accNo1);
		db.updateAccessionNoInStudyTable(fail320_patientName, accNo2);
		db.updateAccessionNoInStudyTable(liver9_patientName, accNo2);
	
		//TC8138
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page,single study list page from UI login for Admin user.");
		patientPage = new PatientListPage(driver);
		patientPage.assertFalse(patientPage.patientNamesList.isEmpty(), "Checkpoint[4/13]", "Verified that admin user able to see patient list page after login.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[5/13]", "Verified current page URL " +patientPage.getCurrentPageURL()+" is displaying.");
		patientPage.clickOnPatientRow(ryanTest_patientName);
		loginPage.logout();
		
		//TC8158
		//Accessing viewer page url from non ui login using unique accession number for admin user.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo1);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage=new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		
		layout = new ViewerLayout(driver);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[10/13]","Verified that viewer page loaded by using accession number and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[11/13]", "Verified that viewer page loaded in 1*1 layout.");
		
		//Accessing studylist page url from non ui login using valid but not unique accession number for admin user.
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo2);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[12/13]","Verified that study list page loaded by using accession number and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertFalse(patientPage.studyRows.isEmpty(), "Checkpoint[13/13]", "Verified that study list rows when accession number is valid but not unique.");		

		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"})
	public void test01_02_US1521_TC8138_TC8158_verifyPatientAndStudyListPageForNonAdminUser() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify filtering the data on 'PatientList', 'PatientStudyList' and 'Studylist' pages for the logged in user  when 'ContextInfoEnabled' = 'False' in 'ConfigSettings' table. <br>"+
		"Verify non UI login in to NS using 'accessionNumber' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateAccessionNoInStudyTable(ryanTest_patientName, accNo1);
		db.updateAccessionNoInStudyTable(fail320_patientName, accNo2);
		db.updateAccessionNoInStudyTable(liver9_patientName, accNo2);
		
		//TC8138: verify patient and study list page for Non Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page,single study list page from UI login for Non Admin user.");
		loginPage=new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(userOne, userOne);
		patientPage=new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/8]", "Verified that non admin user can navigate to Patient list page.");
		patientPage.assertFalse(patientPage.patientNamesList.isEmpty(), "Checkpoint[2/8]", "Verified that patient list page is not empty.");
				
		patientPage.clickOnPatientRow(ryanTest_patientName);
		patientPage.assertEquals(patientPage.studyRows.size(),1, "Checkpoint[3/8]", "Verified single patient study list row after navigating to study page.");
		
		loginPage.logout();
		
		//TC8158
		//Accessing studylist page url from non ui login using non unique accession number for non-admin user.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of study list page from non-ui login using valid and non unique accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo2);
	    String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[7/8]","Verified that study list page loaded by using accession number and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertFalse(patientPage.studyRows.isEmpty(), "Checkpoint[8/8]", "Verified that study list rows when accession number is valid but not unique for non admin user.");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test02_contextInfo"})
	public void test02_01_US1521_TC8168_TC8170_verifyNonUILoginUsingPatientIDAndStudyUidForAdmin(String filepath,String viewboxNo) throws SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'patientId' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table. <br>"+
		"Verify non UI login in to NS using 'studyuid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.");
	
		//TC8168: Verify non ui login using patient ID for Admin user
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		String studyDescription=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName+ " from non-ui login .");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientID );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/4]","Verified that patient study page loaded by using Patient ID and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertEquals(patientPage.getStudyDescription(0),studyDescription, "Checkpoint[2/4]", "Verified study description visible on study list page.");
		
		//TC8170: Verify non ui login using Study UID for Admin user
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(patientName);
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/4]","Verified that viewer page loaded by using Study UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewerpage.convertIntoInt(viewboxNo), "Checkpoint[4/4]", "Verified number of viewbox loaded on viewer after non ui login using study UID.");		

		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test02_contextInfo"})
	public void test02_02_US1521_TC8168_TC8170_verifyNonUILoginUsingPatientIDAndStudyUidForNonAdmin(String filepath,String viewboxNo) throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'patientId' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table. <br>"+
		"Verify non UI login in to NS using 'studyuid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.");

		//TC8168: Verify non ui login using patient ID for Non Admin user
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		String studyDescription=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName+ " from non-ui login.");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientID );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
					
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/4]","Verified that patient study page loaded by using Patient ID and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertEquals(patientPage.getStudyDescription(0),studyDescription, "Checkpoint[2/4]","Verified study description visible on study list page.");
		
		//TC8170: Verify non ui login using Study UID  for Non Admin user
		String studyUID=db.getStudyInstanceUID(patientName);
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/4]","Verified that viewer page loaded by using Study UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewerpage.convertIntoInt(viewboxNo), "Checkpoint[4/4]", "Verified number of viewbox loaded on viewer after non ui login using study UID.");		    
		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1521","Negative"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test02_contextInfo"})
	public void test03_01_US1521_TC8173_TC8175_verifyNonUILoginUsingBatchUIDAndIssuerIDForAdmin(String filepath,String viewboxNo) throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'batchid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.. <br>"+
		"Verify non UI login in to NS using 'IssuerOfPatientID' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");
	
		//TC8173: Verify non ui login using Batch UID for Admin user
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName+ " from non-ui login.");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		String batchUID=db.getBatchUIDFromBatchTable(patientName);

		hm.put(LoginPageConstants.BATCH_UID,batchUID); 
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		overlays = new ViewerTextOverlays(driver);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/4]","Verified that viewer page loaded by using batch UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertTrue(overlays.verifyTextOverlayDetail(viewerpage.convertIntoInt(viewboxNo), ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/4]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
		
		//TC8175: Verify non ui login using Issuer ID for Admin user
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(patientID)); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login");
		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/"+patientID, hm);
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/4]","Verified single patient study list page "+patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyListRows), "Checkpoint[4/4]","Verified single study list page from non UI login");

		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Negative"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test02_contextInfo"})
	public void test03_02_US1521_TC8175_TC8173_verifyNonUILoginUsingBatchUIDAndIssuerIDForNonAdmin(String filepath,String viewboxNo) throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'batchid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.. <br>"+
		"Verify non UI login in to NS using 'IssuerOfPatientID' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");
		
		//TC8173: Verify non ui login using Batch UID for Non Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using accession number");
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName+ " from non-ui login using accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		String batchUID=db.getBatchUIDFromBatchTable(patientName);

		hm.put(LoginPageConstants.BATCH_UID,batchUID); 
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		overlays = new ViewerTextOverlays(driver);
		
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/4]","Verified that viewer page loaded by using batch UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertTrue(overlays.verifyTextOverlayDetail(viewerpage.convertIntoInt(viewboxNo), ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/4]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
		
		//TC8175: Verify non ui login using Issuer ID for Non Admin user
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(patientID)); 

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login");
		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/"+patientID, hm);
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/4]", "Verified single patient study list page "+ patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyListRows), "Checkpoint[4/4]","Verified single study list page from non UI login");

		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521", "Negative"})
    public void test04_US1521_TC8178_verifyFindingSummaryFunctForAdminUser() throws SQLException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify findings summary using non UI login in to NS for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");

		loginPage = new LoginPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message when no finding summary available for the patient in DB.");
		String studyUid=db.getStudyInstanceUID(liver9_patientName);
		LinkedHashMap<String, String>   hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,username); 
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUid); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object>response= RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
					
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_NO_FINDING_SUMMARY_AVAILABLE),"Checkpoint[2/18]","Verified type after getting response from API.");
	
		//verify finding summary result using Patient ID.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using patient ID.");
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,bonage_patientID); 
		
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
	    response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
		
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[3/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[4/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[5/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[6/18]","Verified type after getting response from API.");
		
		//verify finding summary API using Non UI login for Admin user using series UID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using series UID");
		String seriesUid=db.getSeriesInstanceUID(bonage_patientName, seriesDesc);
		hm.put(OrthancAndAPIConstants.SERIES_UID_FOR_API,seriesUid); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
	    RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
				
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[7/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[8/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[9/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[10/18]","Verified type after getting response from API.");
		
		//verify finding summary API using Non UI login for Admin user using accession no
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using accession No.");
		db.updateAccessionNoInStudyTable(bonage_patientName, accNo2);
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo2); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
	    RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
				
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[11/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[12/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[13/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[14/18]","Verified type after getting response from API.");
		
		//verify finding summary API using Non UI login for Admin user using Issuer of patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using Issuer of patient ID.");
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(bonage_patientID)); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
	    RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
				
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[15/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[16/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[17/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[18/18]","Verified type after getting response from API.");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Negative"})
    public void test05_US1521_TC8178_verifyFindingSummaryFunctForNonAdminUser() throws SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify findings summary using non UI login in to NS for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");

		loginPage = new LoginPage(driver);
		//verify finding summary API using Non UI login for non Admin user using study UID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message when no finding summary available for the patient in DB.");
		String studyUid=db.getStudyInstanceUID(liver9_patientName);
		LinkedHashMap<String, String>   hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUid); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
		List<Object>response= RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
					
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_NO_FINDING_SUMMARY_AVAILABLE),"Checkpoint[2/18]","Verified type after getting response from API.");
						
		 //verify finding summary API using Non UI login for non Admin user using patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using patient ID.");
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,bonage_patientID); 
		
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
	    response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
		
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[3/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[4/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[5/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[6/18]","Verified type after getting response from API.");
		
		
		//verify finding summary API using Non UI login for non Admin user using series UID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using series UID.");
		String seriesUid=db.getSeriesInstanceUID(bonage_patientName, seriesDesc);
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,seriesUid); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
	    RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
				
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[7/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[8/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[9/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[10/18]","Verified type after getting response from API.");
		
		//verify finding summary API using Non UI login for non Admin user using accession no
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using accession no.");
		db.updateAccessionNoInStudyTable(bonage_patientName, accNo2);
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo2); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
	    RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
				
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[11/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[12/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[13/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[14/18]","Verified type after getting response from API.");
		
		//verify finding summary API using Non UI login for non Admin user using Issuer of patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify finding summary result using Issuer of patient ID.");
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(bonage_patientID)); 
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
	    keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
	    RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);
				
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[15/18]","Verified status code 200 is received after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),db.getBatchUIDFromBatchTable(bonage_patientName)),"Checkpoint[16/18]","Verified Batch ID after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.DONE),"Checkpoint[17/18]","Verified Status after getting response from API.");
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.FINDINGS_SUMMARY),"Checkpoint[18/18]","Verified type after getting response from API.");
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521", "Negative"})
    public void test06_01_US1521_TC8178_verifyErrorMsgForIncorrectParameterForNonAdminUser() {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify findings summary using non UI login in to NS for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");

		loginPage = new LoginPage(driver);
		
		 //verify finding summary API using Non UI login for Admin user using patient ID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message when finding summary when invalid search parameter are provided for Patient ID.");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,"ap4"); 
		
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);; 
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/2]","Verified status code 200 from API response"); 
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_FOR_INCORRECT_PARAMETERS),"Checkpoint[2/2]","Verified error message when incorrect parameters are present in API and no matching results are found.");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Negative"})
    public void test06_02_US1521_TC8178_verifyErrorMsgWhenNoFindingSummaryAvailableForNonAdminUser() throws SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify findings summary using non UI login in to NS for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false'  in 'ConfigSettings' table.");

		loginPage = new LoginPage(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify error message when finding summary not available in DB for Non-admin user.");
		db=new DatabaseMethods(driver);
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,db.getStudyInstanceUID(liver9_patientName));
		
		//Generate the token and get finding API response
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify batch and and finding summary in API GET response" );
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,userOne,userOne,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal, hm);; 
		loginPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/2]","Verified status code 200 from API response"); 
		loginPage.assertTrue(loginPage.containsIgnoreCase(response.get(1).toString(),OrthancAndAPIConstants.ERROR_MESSAGE_WHEN_NO_FINDING_SUMMARY_AVAILABLE),"Checkpoint[2/2]","Verified error message when no finding summary available in DB.");
	}
	
	
	//Verify different scenarios when  ContextInfoEnabled flag is set to true in config setting table.
	@Test(groups ={"Chrome","Edge","IE11","US1520","Positive"})
	public void test07_01_US1521_TC8155_TC8165_verifyPatientAndStudyListPageForAdminUser() throws SQLException, IOException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify filtering the patient data on 'PatinetList' and 'StudyList' pages for the logged in user when 'ContextInfoEnabled' = 'True' in 'ConfigSettings' table. <br> "+
		"Verify non UI login in to NS using 'accessionNumber' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table.");

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateContextInfoEnable(NSGenericConstants.BOOLEAN_TRUE);
		db.resetIISPostDBChanges();
		db.updateAccessionNoInStudyTable(liver9_patientName, accNo2);
		
		//TC8155:Verify patient ,study list page for admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page,single study list page from UI login for Admin user.");
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		//as patient list page is taking time to load
		loginPage.waitForTimePeriod(1000);
		patientPage = new PatientListPage(driver);
		patientPage.assertFalse(patientPage.patientNamesList.isEmpty(), "Checkpoint[1/10]", "Verified that admin user able to see patient list page after login.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[2/10]", "Verified current page URL " +patientPage.getCurrentPageURL()+" is displaying.");
		patientPage.clickOnPatientRow(ryanTest_patientName);
		
		
		patientPage.assertEquals(patientPage.studyRows.size(),1, "Checkpoint[3/10]", "Verified that single patient study list page displayed after click on patient row.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[4/10]", "Verified current page URL " +patientPage.getCurrentPageURL()+" is displaying.");
		
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL);
		
		patientPage.assertFalse(patientPage.studyRows.isEmpty(), "Checkpoint[5/10]", "Verified study list page using Admin user.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Checkpoint[6/10]", "Verified current page URL " +patientPage.getCurrentPageURL()+" is displaying.");
		
		//TC8165 : verify non ui login for Admin user using Accession number
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login using valid accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo1);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage=new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		layout = new ViewerLayout(driver);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[7/10]","Verified that viewer page loaded by using accession number and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[8/10]", "Verified that viewer page loaded in 1*1 layout.");

		//Accessing studylist page url from non ui login using non unique accession number for admin user.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of study list page from non-ui login using non unique accession number");
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo2);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Checkpoint[9/10]","Verified that study list page loaded by using accession number and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertFalse(patientPage.studyRows.isEmpty(), "Checkpoint[10/10]", "Verified that study list rows when accession number is valid but not unique.");		
			

	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"})
	public void test07_02_US1521_TC8155_TC8165_verifyPatientAndStudyListPageForNonAdminUser() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify filtering the patient data on 'PatinetList' and 'StudyList' pages for the logged in user when 'ContextInfoEnabled' = 'True' in 'ConfigSettings' table. <br> "+
		"Verify non UI login in to NS using 'accessionNumber' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table.");

		//TC8155:Verify patient ,study list page for non admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient and study list  page from non-ui login for Non admin user.");	
		loginPage=new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(userOne, userOne);
		//wait is purposely  added because observed some delay while login
		loginPage.waitForTimePeriod(1000);
		patientPage=new PatientListPage(driver);
		patientPage.assertEquals(patientPage.patientNamesList.size(),2, "Checkpoint[1/8]","Verified that admin user able to see patient list page after login.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[2/8]", "Verified current page URL " +patientPage.getCurrentPageURL()+" is displaying.");
		
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.studyRows.size(),2, "Checkpoint[3/8]","Verified study list page using non Admin user.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Checkpoint[4/8]",  "Verified current page URL " +patientPage.getCurrentPageURL()+" is displaying.");
		
		//TC8165: verify non ui login using Accession number for Non admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using  valid accession number when  email id of the logged in user matches with the patients email id.");			
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo1);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage=new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		layout = new ViewerLayout(driver);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[5/8]","Verified that viewer page loaded by using accession number and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[6/8]", "Verified that viewer page loaded in 1*1 layout.");
		
		//Accessing viewer page url from non ui login using non unique accession number for non admin user.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using  valid accession number when  email id of the logged in user matches with the patients email id.");			 
		hm.put(OrthancAndAPIConstants.ACCESSION_NUMBER,accNo2);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[7/8]","Verified that viewer page loaded by using accession number and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint[8/8]", "Verified that viewer page loaded in 1*1 layout.");				

	}

	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test02_contextInfo"})
	public void test08_01_US1521_TC8169_TC8171_verifyNonUILoginUsingPatientIDAndStudyUidForAdmin(String filepath,String viewboxNo) throws SQLException   {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'patientId' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table.. <br>"+
		"Verify non UI login in to NS using 'studyuid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table.");
		
		//TC8169:Verify non ui login using patient ID for Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of Study page from non-ui login using patient ID.");
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		String studyDescription=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName+ " from non-ui login.");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientID );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/4]","Verified that patient study page loaded by using Patient ID and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertEquals(patientPage.getStudyDescription(0),studyDescription, "Checkpoint[2/4]","Verified study description visible on study list page.");
		
		//TC8171:Verify non ui login using Study UID for Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login using Study UID .");
		db=new DatabaseMethods(driver);
		String studyUID=db.getStudyInstanceUID(patientName); 
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/4]","Verified that viewer page loaded by using Study UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewerpage.convertIntoInt(viewboxNo), "Checkpoint[4/4]", "Verified number of viewbox loaded on viewer after non ui login using study UID.");		

		
	}

	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=text04_ContextInfo_NonAdmin"})
	public void test08_02_US1521_TC8169_TC8171_verifyNonUILoginUsingPatientIDAndStudyUidForNonAdmin(String filepath,String viewboxNo) throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'patientId' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table.. <br>"+
		"Verify non UI login in to NS using 'studyuid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'false' in 'ConfigSettings' table.");

		//TC8169:Verify non ui login using patient ID for Non Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of study page from non-ui login using patient ID when email id of the logged in user matches to the patient's email id");
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		String studyDescription=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName);
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,patientID );
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
					
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/4]","Verified that viewer page loaded by using accession number and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertEquals(patientPage.getStudyDescription(0),studyDescription, "Checkpoint[2/4]", "Verified study description visible on study list page.");
		
		//TC8171:Verify non ui login using Study UID for Non Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer page from non-ui login using Study UID when email id of the logged in user matches to the patient's email id");
		String studyUID=db.getStudyInstanceUID(patientName);
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUID);
	    myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
	
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[3/4]","Verified that viewer page loaded by using study UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewerpage.convertIntoInt(viewboxNo), "Checkpoint[4/4]", "Verified number of viewbox loaded on viewer after non ui login using study UID.");			    
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test02_contextInfo"})
	public void test09_01_US1521_TC8174_TC8176_verifyNonUILoginUsingBatchUIDAndIssuerIDForAdmin(String filepath,String viewboxNo) throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'batchuid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table. <br>"+
		"Verify non UI login in to NS using 'IssuerOfPatientID' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true'  in 'ConfigSettings' table.");
	
		//TC8174:Verify non ui login using batch ID for Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using batch ID.");
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName);
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		String batchUID=db.getBatchUIDFromBatchTable(patientName);

		hm.put(LoginPageConstants.BATCH_UID,batchUID); 
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		overlays = new  ViewerTextOverlays(driver) ;
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/4]","Verified that viewer page loaded by using batch UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertTrue(overlays.verifyTextOverlayDetail(viewerpage.convertIntoInt(viewboxNo), ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/4]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");
		
		//TC8176: Verify non ui login using Issuer ID for Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login using issuer ID"); 
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(patientID)); 

		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/"+patientID, hm);
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/4]", "Verified that viewer page loaded by using Issuer ID and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyListRows), "Checkpoint[4/4]","Verified single study list page from non UI login");

		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=text04_ContextInfo_NonAdmin"})
	public void test09_02_US1521_TC8174_TC8176_verifyNonUILoginUsingBatchUIDAndIssuerIDForNonAdmin(String filepath,String viewboxNo) throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'batchuid' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table. <br>"+
		"Verify non UI login in to NS using 'IssuerOfPatientID' for both Admin and Non-Admin user when 'ContextInfoEnabled' = 'true'  in 'ConfigSettings' table.");
	
		//TC8174:Verify non ui login using batch ID for Non Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of viewer from non-ui login  using batch ID when email id of the logged in user matches to the patient's email id.");
		String patientFilepath = Configurations.TEST_PROPERTIES.get(filepath);	
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilepath);
		String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, patientFilepath);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page for " +patientName+ " from non-ui login using accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		String batchUID=db.getBatchUIDFromBatchTable(patientName);

		hm.put(LoginPageConstants.BATCH_UID,batchUID); 
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
		loginPage.navigateToURL(myURL);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		overlays = new ViewerTextOverlays(driver);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/4]","Verified that viewer page loaded by using batch UID and " +viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertTrue(overlays.verifyTextOverlayDetail(viewerpage.convertIntoInt(viewboxNo), ViewerPageConstants.DEFAULT_ANNOTATION), "Checkpoint[2/4]", "Verified that viewer page  loaded for the correct patient based on Batch UID.");

		//TC8176:Verify non ui login using Issuer ID for Non Admin user
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of study  page from non-ui login  using Issuer ID when email id of the logged in user matches to the patient's email id.");
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(patientID)); 

		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL+"/"+patientID, hm);
		loginPage.navigateToURL(myURL);
		
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[3/4]","Verified that study page loaded by using Issuer ID and " +patientPage.getCurrentPageURL()+" is displaying");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyListRows), "Checkpoint[4/4]","Verified single study list page from non UI login");

		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1521","Negative"})
	public void test09_03_US1521_TC8169_TC8171_TC8174_TC8176_verifyNonUILoginWhenContextInfoFlagIsTrue() throws SQLException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify non UI login in to NS using 'patientId' for Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table.<br>"
				+ "Verify non UI login in to NS using 'studyuid' for Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table. <br>"
				+ "Verify non UI login in to NS using 'batchuid' Non-Admin user when 'ContextInfoEnabled' = 'true' in 'ConfigSettings' table. <br>"
		        + "Verify non UI login in to NS using 'IssuerOfPatientID' for Non-Admin user when 'ContextInfoEnabled' = 'true'  in 'ConfigSettings' table.");
	
		//TC8169 :Here the patient id  is valid but the user does not have permissions to access the patients data whose email id does not match with the logged in user
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify non-ui login  using patient ID when email id of the logged in user does not matches to the patient's email id.");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,liver9_patientID); 
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);

		loginPage=new LoginPage(driver);
		loginPage.navigateToURL(myURL);
		ErrorOrLogoutPage errorpage = new ErrorOrLogoutPage(driver);		
		errorpage.waitForErrorPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[1/8]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"Checkpoint[2/8]","Verifyied error message when valid patient ID provided in URL but user doesnot have the access for the patient");

		//TC8171 :Here the study UID  is valid but the user does not have permissions to access the patients data whose email id does not match with the logged in user
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify non-ui login  using Study UID when email id of the logged in user does not matches to the patient's email id.");
		String studyUid=db.getStudyInstanceUID(liver9_patientName);
	    hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,userOne);
		hm.put(LoginPageConstants.PASSWORD,userOne); 
		hm.put(OrthancAndAPIConstants.STUDY_UID_FOR_API,studyUid); 
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);

		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(1000);
		errorpage.waitForErrorPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[3/8]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.NO_PATIENT_FOUND_ERROR_MSG,"Checkpoint[4/8]","Verifyied error message when valid study UID provided in URL but user doesnot have the access for the patient");

		//TC8174 :Here the batch id  is valid but the user does not have permissions to access the patients data whose email id does not match with the logged in user
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify non-ui login  using batch ID when email id of the logged in user does not matches to the patient's email id.");
		String batchUID=db.getBatchUIDFromBatchTable(liver9_patientID);
		hm.put(LoginPageConstants.BATCH_UID,batchUID); 
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
			
	    loginPage.navigateToURL(myURL);
	    loginPage.waitForTimePeriod(1000);
		errorpage.waitForErrorPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[5/8]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.NO_PATIENT_FOUND_ERROR_MSG,"Checkpoint[6/8]","Verifyied error message when valid batch UID provided in URL but user doesnot have the access for the patient");
		
		//TC8176 :Here the Issuer id  is valid but the user does not have permissions to access the patients data whose email id does not match with the logged in user
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify non-ui login  using Issuer ID when email id of the logged in user does not matches to the patient's email id.");
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(liver9_patientID)); 
		myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);

		loginPage.navigateToURL(myURL);
		loginPage.waitForTimePeriod(1000);
		errorpage.waitForErrorPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.ERROR_PAGE_URL) , "Checkpoint[7/8]","Verified that current page URL is displaying as " +loginPage.getCurrentPageURL());
		errorpage.assertEquals(errorpage.getText(errorpage.message),LoginPageConstants.NO_PATIENT_FOUND_ERROR_MSG,"Checkpoint[8/8]","Verifyied error message when valid Issuer ID provided in URL  but user doesnot have the access for the patient");

		
	}

	
    @AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException {
	    DatabaseMethods db = new DatabaseMethods(driver);
		db.deleteUser(userOne);
		db.updateAccessionNoInStudyTable(liver9_patientName, "");
		
	}	


}
