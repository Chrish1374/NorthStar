package com.trn.ns.test.patientList;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SearchAndRecentlyViewedTest extends TestBase  {

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private DatabaseMethods db;
	private PatientListPage patientStudyPage;
	private ViewerPage viewerPage;
	private RegisterUserPage register;


	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String RANDENT_filepath = Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, RANDENT_filepath);
	String patientIdDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, RANDENT_filepath);


	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);


	String NM_L65P733139591_filepath = Configurations.TEST_PROPERTIES.get("NM_L65P733139591_WO_Result_filepath");
	String NM_L65P733139591 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, NM_L65P733139591_filepath);


	String Anonymous2_Filepath = Configurations.TEST_PROPERTIES.get("Anonymous2_Filepath");
	String Anonymous2_patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Anonymous2_Filepath);


	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	private String deleteStudyURL = "DeleteStudy/studies"; //http://localhost/api/Data/DeleteStudy/studies/{studyInstanceUid}

	String username_1 = "user_1";
	int searchCount=2;
//	private HelperClass helper;
	private Header header;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);
		patientStudyPage=new PatientListPage(driver);
	}	

	@Test(groups ={"Chrome","IE11","Edge","US2008","Positive","E2E","F994"})
	public void Test01_US2008_TC8995_verifyRecentlyViewedTable() throws SQLException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that dbo.RecentlyViewedPatient table and column is present in NSDB database");

		db = new DatabaseMethods(driver);

		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE, NSDBDatabaseConstants.ID_COLUMN),"Checkpoint[1/2]","Verifying the "+NSDBDatabaseConstants.ID_COLUMN+" column is present in table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE, NSDBDatabaseConstants.STUDY_LEVEL_ID_COLUMN),"Checkpoint[2/2]","Verifying the "+NSDBDatabaseConstants.STUDY_LEVEL_ID_COLUMN+" column is present in table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE, NSDBDatabaseConstants.ACCOUNT_ID_COLUMN),"Checkpoint[3/2]","Verifying the "+NSDBDatabaseConstants.ACCOUNT_ID_COLUMN+" column is present in table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE, NSDBDatabaseConstants.VIEWED_DATE_TIME_COLUMN),"Checkpoint[4/2]","Verifying the "+NSDBDatabaseConstants.VIEWED_DATE_TIME_COLUMN+" column is present in table");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","E2E","F994"})
	public void Test02_US1999_TC9120_TC8898_TC9110_verifyEntryInSearchAndViewedHistory() throws SQLException, InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can see an entry under 'Search and Viewed History' for searched patient."+
				"<br>+Verify that user can visit viewer page by selecting patient from 'Search and Viewed History' tab and no new entry is getting created."+
				"<br>Verify that no new entry is getting created in 'Search and Viewed History' tab on UI and in dbo.recentlyViewedpatient table on accessing viewer page from patient list for already searched patient whose entries are present dbo.recentlyViewedpatient table");


		patientStudyPage=new PatientListPage(driver);
		header = new Header(driver);	
		//TC9120 filtering patient based on 'CT' modality and selecting first patient visiting viewer page and verifying entry in Search and Viewed history tab

		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE, "","");
		List<String>patientName=new ArrayList<String>();
	

		for(int i=0; i<searchCount;i++){

			String patient = patientStudyPage.getText(patientStudyPage.patientNamesList.get(i));
			patientName.add(patient);
			patientStudyPage.clickOnPatientRow(patient);
			patientStudyPage.clickOntheFirstStudy();
			viewerPage = new ViewerPage(driver);
			viewerPage.waitForRespectedViewboxToLoad(1);
			patientStudyPage.navigateToBack();
			patientStudyPage.waitForPatientPageToLoad();
			patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patientName.get(i)), "Checkpoint[1/11]","Verifying that patient entry is present or not on Search and Viewed history tab");
			patientStudyPage.openPatientListTab();

		}

		header.logout();
		loginPage.login(username, password);
		patientStudyPage.waitForPatientPageToLoad();
		
		patientStudyPage.openSearchAndViewedHistoryTab();
		Collections.reverse(patientName);
		patientStudyPage.assertEquals(patientName,patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)), "Checkpoint[2/11]","Verifying that patient entry is present or not on Search and Viewed history tab");
		
		// TC8898 Verify that user can visit viewer page by selecting patient from 'Search and Viewed History' tab and no new entry is getting created.
		for(int i=0; i<patientName.size();i++){

			patientStudyPage.openSearchAndViewedHistoryTab();			
			patientStudyPage.clickOnPatientRow(i+1);
			patientStudyPage.clickOntheFirstStudy();
			viewerPage.waitForRespectedViewboxToLoad(1);
			viewerPage.assertTrue(viewerPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[]", "");
			viewerPage.navigateToBack();
			patientStudyPage.waitForPatientPageToLoad();
			patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patientName.get(i)), "Checkpoint[3/11]","Verifying that patient entry is present or not on Search and Viewed history tab");
		}
		patientStudyPage.assertEquals(patientName,patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)), "Checkpoint[2/11]","Verifying that patient entry is present or not on Search and Viewed history tab");
		patientStudyPage.assertEquals(patientStudyPage.patientNamesList.size(), patientName.size(), "Checkpoint[4/11]", "Verifying count in 'Search and Viewed History' tab");

		db = new DatabaseMethods(driver);
		db.assertEquals(db.VerifyEntryInRecentlyViewedPatientTable(), patientName.size(), "Checkpoint[5/11]", "Verifying count in dbo.RecentlyViewedPatient table");


		// TC9110 Selecting the patient from patient list tab, whose entry is already present patient in "Search and Viewed history tab".
		patientStudyPage.openSearchAndViewedHistoryTab();
		String patient=patientStudyPage.getText(patientStudyPage.patientNamesList.get(1));
		patientStudyPage.openPatientListTab();
		patientStudyPage.clickOnPatientRow(patient);
		patientStudyPage.clickOntheFirstStudy();
		viewerPage.waitForRespectedViewboxToLoad(1);
		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.openSearchAndViewedHistoryTab();
		
		db.assertEquals(db.VerifyEntryInRecentlyViewedPatientTable(), patientName.size(), "Checkpoint[8/11]", "Verifying count in dbo.RecentlyViewedPatient table");
		patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patient), "Checkpoint[9/11]","Verifying that patient entry is present or not on Search and Viewed history tab");
		patientStudyPage.assertEquals(patientStudyPage.patientNamesList.size(), patientName.size(), "Checkpoint[10/11]", "Verifying count in 'Search and Viewed History' tab");
		patientStudyPage.assertNotEquals(patientStudyPage.getText(patientStudyPage.patientNamesList.get(1)), patient, "Checkpoint[11/11], Verifying patient name is not same at first place", "Verifyig patient name ordering getting changed");


	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Negative","E2E","F994"})
	public void Test03_US1999_TC9121_TC9117_verifyNoEntryInSearchAndViewedHistory() throws SQLException, InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that 'Search and Viewed History' tab is empty if user has not searched any patient and visited viewer page directly by selecting any patient from patient list."+
		"<br>+Verify that no entry getting generated under 'Search and Viewed History' tab for searched patient but user did not visit the Viewer page.");


		//TC9121 Selecting patient from patient list and visiting viewer page and verifying entry in 'Search and Viewed History' tab.
		patientStudyPage = new PatientListPage(driver);
		patientStudyPage.clickOnPatientRow(imbio_PatientName);
		patientStudyPage.clickOntheFirstStudy();
		viewerPage=new ViewerPage(driver);
		viewerPage.navigateToBack();
		patientStudyPage.assertFalse(patientStudyPage.verifyPatientInSearchViewedHistoryTab(imbio_PatientName),"Checkpoint[1/4]","Verifying that patient entry is not present on Search and Viewed history tab");

		//Logout and Login and again check entry in 'Search and Viewed History' tab.

		header = new Header(driver);		
		header.logout();
		loginPage.login(username, password);
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.assertFalse(patientStudyPage.verifyPatientInSearchViewedHistoryTab(imbio_PatientName),"Checkpoint[2/4]","Verifying that patient entry is not present on Search and Viewed history tab");


		// TC9117 User is seraching patient from filter but not visiting viewer page.
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_PR_VALUE, "","");		
		for(int i=0; i<searchCount;i++){

			String patient = patientStudyPage.getText(patientStudyPage.patientNamesList.get(i));
			patientStudyPage.clickOnPatientRow(patient);
			patientStudyPage.assertFalse(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patient), "Checkpoint[3/4]","Verifying that patient entry is present or not on Search and Viewed history tab");
			patientStudyPage.openPatientListTab();

		}		
		
		//Logout and login and again checking entry in Search and Viewed History tab.
		header.logout();
		loginPage.login(username, password);
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.patientNamesList.isEmpty(), "Checkpoint[2/4]","Verifying that patient entry is present or not on Search and Viewed history tab");
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","E2E","F994"})
	public void Test04_US1999_TC9192_TC9191_verifyStudyDetailsForNoEntryInSearchAndViewedHistorTab() throws SQLException, InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Study details are empty if no entry of recently viewed patient is present in 'Search and Viewed History' tab."+
		"<br> Verify that Study details of RtStruct patient is visible on Studylist section of Patient-Study list page.");


		//TC9192 Verifying No details on studyList section'Search and Viewed History' tab.
		patientStudyPage=new PatientListPage(driver);
		patientStudyPage.openSearchAndViewedHistoryTab();		
		patientStudyPage.assertTrue(patientStudyPage.patientNamesList.isEmpty(),"Checkpoint[1/11]", "Verifying the patient list count in searchAndViewedHistoryTab is zero");
		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.patientHeader), "Checkpoint[2/11] Verifying Patient details header is present or not", "Patient header is not present if no entry in searchAndViewedHistoryTab");

		patientStudyPage.openPatientListTab();
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_PR_VALUE, "","");
		for(int i=0; i<searchCount;i++){
			String patientName=patientStudyPage.getText(patientStudyPage.patientNamesList.get(i));
			patientStudyPage.clickOnPatientRow(patientName);
			patientStudyPage.clickOntheFirstStudy();
			viewerPage=new ViewerPage(driver);
			viewerPage.waitForRespectedViewboxToLoad(1);
			viewerPage.navigateToBack();
			patientStudyPage.waitForPatientPageToLoad();
			patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patientName), "Checkpoint[3/11]","Verifying that patient entry is present or not on Search and Viewed history tab");
			patientStudyPage.openPatientListTab();

		}
		db = new DatabaseMethods(driver);
		db.assertEquals(db.VerifyEntryInRecentlyViewedPatientTable(), searchCount, "Checkpoint[4/11]", "Verifying count in dbo.RecentlyViewedPatient table");
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[5/11]","Verifying study details of first patient row is selected");
		
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE);
		patientStudyPage.openPatientListTab();
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.patientNamesList.isEmpty(), "Checkpoint[6/11]", "Verifying the patient list count in searchAndViewedHistoryTab is zero");
		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.patientHeader), "Checkpoint[7/11]", "Patient header is not present if no entry in searchAndViewedHistoryTab");

		// TC9191-Verifying study details of RT struct patient is visible on StudyDetails section.
		patientStudyPage.click(patientStudyPage.clearButton);
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.clickOnPatientRow(patientNameDICOMRT);
		patientStudyPage.assertEquals(patientNameDICOMRT, patientStudyPage.getText(patientStudyPage.patientNameHeaderValueInPatientInfo), "Checkpoint[8/11]", "Verifing patient name of "+patientNameDICOMRT+" of RT Struct patient is can be seen On Study details");
		patientStudyPage.assertEquals(patientIdDICOMRT, patientStudyPage.getText(patientStudyPage.patientIDHeaderValueInPatientInfo), "Checkpoint[9/11]", "Verifing patientID "+patientIdDICOMRT+" of RT Struct patient is can be seen On Study details");

		patientStudyPage.clickOnPatientRow(patientNameTCGA);
		patientStudyPage.assertEquals(patientNameTCGA, patientStudyPage.getText(patientStudyPage.patientNameHeaderValueInPatientInfo), "Checkpoint[10/11]", "Verifing patient name of "+patientNameTCGA+" of RT Struct patient is can be seen On Study details");
		patientStudyPage.assertEquals(patientNameTCGA, patientStudyPage.getText(patientStudyPage.patientIDHeaderValueInPatientInfo), "Checkpoint[11/11]", "Verifing patientID "+patientNameTCGA+" of RT Struct patient is can be seen On Study details");



	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","E2E","F994"})
	public void Test05_US1999_TC9190_verifyStudyDetailsForFirstSelectedPatient() throws SQLException, InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that in studylist section always study details of first patient is visible on default loading from login page , Revisiting from Viewer page, switching between Patientlist tab and after Search result on Patient-Study list page.");


		//TC9190 Verifying on default loading on patient page, study deatils of first selected patient is visible
		patientStudyPage=new PatientListPage(driver);
		patientStudyPage.assertTrue(patientStudyPage.verifyTableRowBorder(),"Checkpoint[1/7]","verifying the row strips");
		patientStudyPage.assertTrue(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[2/7]","Verifying study details of first patient row is selected");

		patientStudyPage.clickOnPatientRow(patientNameDICOMRT);
		patientStudyPage.clickOntheFirstStudy();
		viewerPage=new ViewerPage(driver);
		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.assertFalse(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[3/7]","Verifying study details of first patient row is selected");


		patientStudyPage.clickOnPatientRow(1);		
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.openPatientListTab();
		patientStudyPage.assertTrue(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[4/7]","Verifying study details of first patient row is selected");

		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_PR_VALUE, "","");
		patientStudyPage.assertTrue(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[5/7]","Verifying study details of first patient row is selected");

		patientStudyPage.searchPatient("", "", "", "", PatientPageConstants.TODAY);
		patientStudyPage.assertTrue(patientStudyPage.patientNamesList.isEmpty(), "Checkpoint[6/7]", "Verifying the patient list count in PatientListTab is zero");
		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.patientHeader), "Checkpoint[7/7] Verifying Patient details header is present or not", "Patient header is not present if no entry in PatientListTab");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","F994"})
	public void test06_US1999_TC9122_TC9186_TC9124_TC9116_verifySearchAndViewedHistoryTab() throws InterruptedException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the look and feel and components of the Header for 'Search and Viewed History' tab on Patient-Study list page"+
				"<br>Verify that in 'Search and Viewed History' tab default first patient is selected and for the same patient study details is getting displayed."+
				"<br> Verify that on performing search operation on when user is on 'Search and Viewed History'' tab, patient list tab gets highlighted."
				+"<br>Verify that entry under 'Search and Viewed History' tab is user specific only.");	

		patientStudyPage = new PatientListPage(driver);
		//TC9122 Searching patient and verifying Search And Viewed history tab.
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE, "","");

		for(int i=0; i<searchCount;i++){
			patientStudyPage.clickOnPatientRow(i+1);
			patientStudyPage.clickOntheFirstStudy();
			viewerPage=new ViewerPage(driver);
			viewerPage.navigateToBack();
			patientStudyPage.waitForPatientPageToLoad();

		}
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/14]", "Verifying the patient's url");

		patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.searchAndViewedHistoryTab), "Checkpoint[2/14]", "Verifying the SerachAndViewedHistoryTab");
		patientStudyPage.assertEquals(patientStudyPage.patientIdsList.size(), patientStudyPage.patientNamesList.size(), "Checkpoint[3/14]","verifying the data is displayed");
		patientStudyPage.assertEquals(patientStudyPage.getText(patientStudyPage.patientNameHeader), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1), "Checkpoint[4/14]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)+" is visible in SerachAndViewedHistoryTab.");
		patientStudyPage.assertEquals(patientStudyPage.getText(patientStudyPage.acqutisionHeader), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4), "Checkpoint[5/14]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5)+" is visible in SerachAndViewedHistoryTab.");
		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.sortIcon),"Checkpoint[6/14]","verifying that Sort icon is not present in SerachAndViewedHistoryTab.");


		//TC9186- Verifying default first row inside search and viewed history tab remains selected and study details for the same is displayed.
		patientStudyPage.assertTrue(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[7/14]","Verifying study details of first patient row is selected");

		//Logout and login and again checking entry in Search and Viewed History tab and Verifying study details of first selected patient is visible
		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username, password);
		
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[8/14]","Verifying study details of first patient row is selected");

		//TC9124 User is on searchAndViewedHistoryTab and on searching from filter, patient list tab get highlighted after search completion

		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE, "","");
		patientStudyPage.assertEquals(patientStudyPage.getCssValue(patientStudyPage.patientListTab, NSGenericConstants.TABLE_BORDER_BOTTOM),ThemeConstants.EUREKA_TAB_BOTTOM_BORDER_COLOR,"Checkpoint[9/14]","verifying the Tab strips");
		patientStudyPage.assertEquals(patientStudyPage.getCssValue(patientStudyPage.patientListTab, NSGenericConstants.CSS_PRO_BACKGROUND),ThemeConstants.EUREKA_TAB_SELECTED_COLOR,"Checkpoint[10/14]","verifying the default first row is highlighted");
		patientStudyPage.assertEquals(patientStudyPage.getAttributeValue(patientStudyPage.patientListTab, PatientPageConstants.ARIA_SELECTED), NSGenericConstants.BOOLEAN_TRUE, "Checkpoint[11/14]", "Verifying that patientList tab aria-selected is true");

		//TC9116 --Registering New patient

		patientStudyPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging using user A");	
		header.logout();

		// Login with  username_1 and verifying , that username_1 can not see any entry under searchAndViewedHistoryTab.
		loginPage.navigateToBaseURL();		
		loginPage.login(username_1, username_1);
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.patientNamesList.isEmpty(), "Checkpoint[12/14]", "Verifying the for "+username_1+"patient list count in searchAndViewedHistoryTab is zero");

		// Creating entry for username_1 user in searchAndViewedHistoryTab and verifying entry
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_XA_VALUE, "","");
		String patientName=patientStudyPage.getText(patientStudyPage.patientNamesList.get(0));
		patientStudyPage.clickOnPatientRow(patientName);
		patientStudyPage.clickOntheFirstStudy();
		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patientName), "Checkpoint[13/14]","Verifying that patient entry is present or not on Search and Viewed history tab");
		header.logout();

		//Logging again with scan user and verifying that scan user can not see entry of username_1 user in searchAndViewedHistoryTab because its user specific	
		loginPage.login(username, password);
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertFalse(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patientName), "Checkpoint[14/14]","Verifying that patient entry of "+username_1+"is not present on Search and Viewed history tab of "+username+" user");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","F994"})
	public void test07_US1999_TC9127_verifyPatientListRefresh() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that 'Patient list' tab gets refreshed on switching tab from 'Search and Viewed History' tab.");	

		patientStudyPage = new PatientListPage(driver);
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_NM_VALUE, "","");
		String patient=patientStudyPage.getText(patientStudyPage.patientNamesList.get(0));
		patientStudyPage.openSearchAndViewedHistoryTab();

		DatabaseMethods db = new DatabaseMethods(driver);
		db.deletePatient(patient);
		patientStudyPage.openSearchAndViewedHistoryTab();
		patientStudyPage.assertTrue(patientStudyPage.patientNamesList.isEmpty(), "Checkpoint[1/2]", "Verifying patient list is count is zero when refrshing is done by switching tab.");
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","US2008","E2E","F994"})
	public void test08_US2008_TC8897_TC9062_verifySearchAndViewedHistoryTabOnDeleteingEntryFromDB() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that entry for searched and viewer page visited patient not visible under RecentlyViewedPatient  tab if patient entry is deleted from dbo.RecentlyViewedPatient table inDatabase."+
				"<br>Verify that entry from dbo.RecentlyViewedPatient table is getting deleted on deleting patient from dbo.StudyLevel table through delete API.");	

		patientStudyPage = new PatientListPage(driver);
		patientStudyPage.waitForPatientPageToLoad();

		//TC8897- Creating entry first in searchAndViewedHistory tab and then deleting entry from dbo.recently patient and verying on viewer.
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_OT_VALUE, "","");
		patientStudyPage.waitForPatientPageToLoad();

		patientStudyPage.clickOnPatientRow(Anonymous2_patient);
		patientStudyPage.clickOntheFirstStudy();

		viewerPage=new ViewerPage(driver);
		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();

		//  Creating one more entry of RT patient in searchAndViewedHistory tab.
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_RT_VALUE, "","");
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.clickOnPatientRow(patientNameDICOMRT);
		patientStudyPage.clickOntheFirstStudy();

		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();

		// Verifying entry of both the patient Anonymous2_patient and patientNameDICOMRT
		patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(Anonymous2_patient), "Checkpoint[1/6]","Verifying that  entry of "+Anonymous2_patient+"is  present on Search and Viewed history tab");
		patientStudyPage.assertTrue(patientStudyPage.verifyPatientInSearchViewedHistoryTab(patientNameDICOMRT), "Checkpoint[2/6]","Verifying that patient entry of "+patientNameDICOMRT+"is present on Search and Viewed history tab");

		// Now deleting entry of only Anonymous2_patient from dbo.recentlyViewed table.

		DatabaseMethods db = new DatabaseMethods(driver);
		db.deletePatientFromRecentlyViewedPatientTable(Anonymous2_patient);

		patientStudyPage.refreshTabPatientOrSearchAndViewedHistoryTab();

		patientStudyPage.assertFalse(patientStudyPage.verifyPatientInSearchViewedHistoryTab(Anonymous2_patient), "Checkpoint[3/6]","Verifying that patient entry of "+Anonymous2_patient+"is not present on Search and Viewed history tab");


		//TC9062  Deleting entry from dbo.StudyLevel table through Post API and verifying entry is getting deleted from dbo.recentlyViewedTable

		patientStudyPage.refreshTabPatientOrSearchAndViewedHistoryTab();
		patientStudyPage.waitForPatientPageToLoad();

		patientStudyPage.clickOnPatientRow(Anonymous2_patient);
		patientStudyPage.clickOntheFirstStudy();

		viewerPage=new ViewerPage(driver);
		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();  

		// Deleting patient from Delete API
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		List<Object> response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.API_DATA_DELETE_BASE_URL,deleteStudyURL,db.getStudyInstanceUID(Anonymous2_patient),keyVal);
		patientStudyPage.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[4/6]Verifying status code 200 is received after successful deletion of study", "Verified status code 200 is received after successful deletion of study");



		//verifying in searchAndViewedHistoryTab and in dbo.recentlyViewed table
		patientStudyPage.assertFalse(patientStudyPage.verifyPatientInSearchViewedHistoryTab(Anonymous2_patient), "Checkpoint[5/6]","Verifying that patient entry of "+Anonymous2_patient+"is not present on Search and Viewed history tab");

		db.assertFalse(db.VerifyStudyLevlIdEntryInViewedPatientTable(Anonymous2_patient),"Checkpoint[6/6]", "Verifying count in dbo.RecentlyViewedPatient table");
	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","E2E","F994"})
	public void test09_US1999_TC9118_verifyMaxmEntryInSearchAndViewedHistoryTab() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that only top 20 recent searched patient can be seen under 'Search and Viewed History' tab");	

		patientStudyPage = new PatientListPage(driver);
		patientStudyPage.waitForPatientPageToLoad();

		//TC9118- Creating entry first in searchAndViewedHistory tab and then deleting entry from dbo.recently patient and verying on viewer.

		patientStudyPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_CT_VALUE,OrthancAndAPIConstants.ORTHANC_PR_VALUE);

		patientStudyPage.click(patientStudyPage.searchButton);
		patientStudyPage.waitForPatientPageToLoad();

		int filteredPatient=patientStudyPage.patientNamesList.size();

		for(int i=1; i<=filteredPatient;i++){
			patientStudyPage.clickOnPatientRow(i);
			patientStudyPage.clickOntheFirstStudy();
			viewerPage=new ViewerPage(driver);
			viewerPage.navigateToBack();
			patientStudyPage.waitForPatientPageToLoad();

		}
		patientStudyPage.click(patientStudyPage.searchAndViewedHistoryTab);
		patientStudyPage.waitForPatientPageToLoad();

		patientStudyPage.assertEquals(patientStudyPage.patientNamesList.size(), PatientPageConstants.MAX_ENTRY_IN_RECENTSEARCH_AND_VIEWED_TAB, "Checkpoint[1/2]", "Verifying Maxmium 20 entry in SearchAndViewedHistory tab can be present");
		db = new DatabaseMethods(driver);
		db.assertEquals(db.VerifyEntryInRecentlyViewedPatientTable(), filteredPatient, "Checkpoint[2/2]", "Verifying count in dbo.RecentlyViewedPatient table");
	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","F994"})
	public void test10_US1999_TC9123_TC9189_TC9188_verifySortingInSearchAndViewedHistoryTab() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the sorting is not supported for 'Search and Viewed History' tab on Patient-Study list page"
				+"<br>Verify that patient list is getting sorted correctly by acquisition date column when sorting is in ascending order."+"<br>Verify that patient list is getting sorted correctly by acquisition date column when sorting is in descending order.");	

		patientStudyPage = new PatientListPage(driver);

		//TC9123- Creating entry first in searchAndViewedHistory tab.

		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE, "","");

		for(int i=0; i<2;i++){
			patientStudyPage.clickOnPatientRow(i+1);
			patientStudyPage.clickOntheFirstStudy();
			viewerPage=new ViewerPage(driver);
			viewerPage.navigateToBack();
			patientStudyPage.waitForPatientPageToLoad();

		}
		// Verifying presence of Sort icon
		patientStudyPage.click(patientStudyPage.searchAndViewedHistoryTab);
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.sortIcon),"Checkpoint[1/12]","Verifying Sorting icon is present or not");

		//After clicking on Acquisition column, verifying no patient order changed

		List<String> patientNameBefore=patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1));
		patientStudyPage.click(patientStudyPage.acqutisionHeader);
		List<String> patientNameAfter=patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1));
		patientStudyPage.assertEquals(patientNameBefore, patientNameAfter, "Checkpoint[2/12]", "Verifying sorting not happening after clicking on acquistion header, Patient order is same");

		//TC9188-- Verifying that sorting is default in descending order.

		patientStudyPage.click(patientStudyPage.clearButton);
		patientStudyPage.waitForPatientPageToLoad();
		List<String> columnValues = patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[3/12]","Verifying default patient list is sorted based on descending order after click on header");
		patientStudyPage.assertEquals(patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),columnValues,"Checkpoint[4/12]","verifying the list is reversed in descending order");

		// Searching patient by modality filter and verifying that even after searching anything sorting is maintained in same order or not.
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_PR_VALUE, "","");
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[5/12]","Verifying ordering is maintained in descending in order even after search from above filter");

		// Clearing filter and verifying that still sorting order is maintained or not
		patientStudyPage.click(patientStudyPage.clearButton);
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[6/12]","Verifying ordering is maintained in descending in order even after all filtered are cleared");
		patientStudyPage.assertEquals(patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),columnValues,"Checkpoint[7/12]","verifying the list is reversed in descending order");

		//TC9189--Clicking on Acquisition column header and Verifying that sorting is happening in ascending order.
		Collections.reverse(columnValues);
		patientStudyPage.click(patientStudyPage.acqutisionHeader);
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[8/12]","Verifying default patient list is sorted based on ascending order after click on header");
		patientStudyPage.assertEquals(patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),columnValues,"Checkpoint[9/12]","verifying the list is reversed in ascending order");

		// Searching patient by modality filter and verifying that even after searching anything sorting is maintained in same order or not.
		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_PR_VALUE, "","");
		patientStudyPage.waitForPatientPageToLoad();
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[10/12]","Verifying ordering is maintained in ascending in order even after search from above filter");

		// Clearing filter and verifying that still sorting order is maintained or not
		patientStudyPage.click(patientStudyPage.clearButton);
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[11/12]","Verifying ordering is maintained in ascending in order even after all filtered are cleared");
		patientStudyPage.assertEquals(patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),columnValues,"Checkpoint[12/12]","verifying the list is reversed in ascending order");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","F994"})
	public void test11_US1999_TC9125_verifySortingPersistenceOnSwitchingTab() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that sorting is persisted as per the Acquisition column(descending order) for 'Patient list' tab when user switches from 'Search and Viewed History' or navigates back to Patient-Study list page from Viewer page");	

		patientStudyPage = new PatientListPage(driver);
		patientStudyPage.waitForPatientPageToLoad();

		//TC9125-- Filtering patient based on modality

		patientStudyPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE, "","");
		patientStudyPage.waitForPatientPageToLoad();

		// Sorting patient based on PatientID in ascending and then descending order
		patientStudyPage.click(patientStudyPage.patientNameHeader);
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[1/7]","Verifying default patient list is sorted based on ascending order after click on patient header");
		List<String> columnValues = patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1));
		Collections.reverse(columnValues);
		patientStudyPage.click(patientStudyPage.patientNameHeader);

		// Sorting patient based on PatientName in ascending and then descending order
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[2/7]","Verifying default patient list is sorted based on descending order after click on patient header");
		patientStudyPage.assertEquals(patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)),columnValues,"Checkpoint[3/7]","verifying the list is reversed in descending order");

		// Selecting any patient and creating entry in SearchAndViewedHistory tab.

		patientStudyPage.clickOnPatientRow(1);
		patientStudyPage.clickOntheFirstStudy();
		viewerPage=new ViewerPage(driver);
		viewerPage.navigateToBack();
		patientStudyPage.waitForPatientPageToLoad();

		// Verifying that Patient list tab is again sorted back to Acquisition column(descending order) on revisiting from viewer page.

		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[4/7]","Verifying default patient list is sorted based on descending order after click on header");

		// Sorting patient based on PatientID in ascending and then descending order
		patientStudyPage.click(patientStudyPage.patientIDHeader);
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[5/7]","Verifying default patient list is sorted based on ascending order after click on patient header");
		List<String> PatientIdColumnValue = patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0));
		Collections.reverse(PatientIdColumnValue);
		patientStudyPage.click(patientStudyPage.patientIDHeader);
		patientStudyPage.assertEquals(patientStudyPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0)),PatientIdColumnValue,"Checkpoint[6/7]","verifying the list is reversed in descending order");

		// Switching back to patient List tab from SearchAndViewed History tab
		patientStudyPage.refreshTabPatientOrSearchAndViewedHistoryTab();

		// Verifying that Patient list tab is again sorted back to Acquisition column(descending order) on revisiting from viewer page.
		patientStudyPage.assertTrue(patientStudyPage.verifySortIcon(patientStudyPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[7/7]","Verifying default patient list is sorted based on descending order after click on header");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1999","Positive","F994"})
	public void test12_US1999_TC9128_TC9129_verifyAddingHidingAndRearrangingColumn() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify adding, hiding columns for 'Search and Viewed History' tab for Patient-Study list page."+
				"<br>Verify rearranging  columns for 'Search and Viewed History' tab for Patient-Study list page");	

		patientStudyPage = new PatientListPage(driver);
		patientStudyPage.waitForPatientPageToLoad();

		// TC9128
		patientStudyPage.click(patientStudyPage.searchAndViewedHistoryTab);
		patientStudyPage.assertFalse(patientStudyPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0)),"Checkpoint[1/11]","Verifying that Patient ID column checkbox is uneditable");
		patientStudyPage.assertFalse(patientStudyPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)),"Checkpoint[2/11]","Verifying that Patient name column checkbox is uneditable");
		patientStudyPage.assertFalse(patientStudyPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),"Checkpoint[3/11]","Verifying that Acquistion column checkbox is uneditable");

		patientStudyPage.assertTrue(patientStudyPage.verifyCheckboxIsEnabled(patientStudyPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)),"Checkpoint[4/11]","Verifying that Sex column checkbox is editable");
		patientStudyPage.assertTrue(patientStudyPage.verifyCheckboxIsEnabled(patientStudyPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(3)),"Checkpoint[5/11]","Verifying that DOB column checkbox is editable");
		patientStudyPage.assertTrue(patientStudyPage.verifyCheckboxIsEnabled(patientStudyPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5)),"Checkpoint[6/11]","Verifying that Site column checkbox is editable");
		patientStudyPage.assertTrue(patientStudyPage.verifyCheckboxIsEnabled(patientStudyPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(6)),"Checkpoint[7/11]","Verifying that Modality column checkbox is editable");
		patientStudyPage.assertTrue(patientStudyPage.verifyCheckboxIsEnabled(patientStudyPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7)),"Checkpoint[8/11]","Verifying that Machine column checkbox is editable");


		for (Entry<String, Boolean> entry : PatientPageConstants.Patient_Header_Checkbox.entrySet()) {
			if(entry.getValue())
				patientStudyPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),entry.getKey(),PatientPageConstants.CHECK);
		}

		List<String> columnNames  = PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS;

		for(int i=0;i<columnNames.size();i++) {

			patientStudyPage.openHideUnhideDialogbox(columnNames.get(i));
			patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.selectColumnDialogue),"Checkpoin[9."+i+"/11]","Verifying that select column dialogue is present");
			patientStudyPage.assertEquals(patientStudyPage.columnNamesInSelectColDialogue.size(),columnNames.size(),"Checkpoint[9."+i+"/11]","Verifying that size of column names present on select dialogue box is same as table on patient list page");
			patientStudyPage.assertEquals(patientStudyPage.checkboxInSelectColDialogue.size(),columnNames.size(),"Checkpoint[9."+i+"/11]","Verifying that size of column checkboxes present on select dialogue box is same as columns present in table on patient list page");
			patientStudyPage.assertEquals(patientStudyPage.convertWebElementToStringList(patientStudyPage.columnNamesInSelectColDialogue),columnNames,"Checkpoint[9."+i+"/11]","Verifying that column names present on select dialogue box are same as patient page list");

			patientStudyPage.closeSelectColumnDialogBox();
		}

		for (Entry<String, Boolean> entry : PatientPageConstants.Patient_Header_Checkbox.entrySet()) {
			if(entry.getValue())
				patientStudyPage.assertTrue(patientStudyPage.verifyCheckboxIsEnabled(patientStudyPage.patientIDHeader,entry.getKey()),"Checkpoint[10/11]","Verifying that "+entry.getKey()+" column checkbox is editable");
			else
				patientStudyPage.assertFalse(patientStudyPage.verifyCheckboxIsEnabled(entry.getKey()),"Checkpoint[11/11]","Verifying that "+entry.getKey()+" column checkbox is uneditable");

		}
		//Verifying the display column selector, allows the user to show or hide the available list of columns
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.DISPLAY_TABLE_USER_PREF);

		patientStudyPage.refreshTabPatientOrSearchAndViewedHistoryTab();
		int totalColumns = patientStudyPage.displayedPatientTableHeaders.size();	

		patientStudyPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);
		patientStudyPage.assertEquals(patientStudyPage.displayedPatientTableHeaders.size(),totalColumns+1,"Checkpoint[1/4]","Verifying that size of column names present on select dialogue box is same after checking that column");
		List<String> list = patientStudyPage.convertWebElementToStringList(patientStudyPage.displayedPatientTableHeaders);
		patientStudyPage.assertTrue(list.contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[2/4]", "Verifying the Column name is same post checking the column from dailog box");

		patientStudyPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.UNCHECK);
		patientStudyPage.assertEquals(patientStudyPage.displayedPatientTableHeaders.size(),totalColumns,"Checkpoint[3/4]","Verifying that size of column names present on select dialogue box is not same as patient page");
		list = patientStudyPage.convertWebElementToStringList(patientStudyPage.displayedPatientTableHeaders);
		patientStudyPage.assertFalse(list.contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[4/4]", "Verifying the Column name is not same post uncheck of column from dailog box");


	}

	@AfterMethod(alwaysRun=true)
	public void AfterMethod() {

		DatabaseMethods db = new DatabaseMethods(driver);
		try {
			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.RECENTLY_VIEWED_PATIENT_TABLE);
			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.DISPLAY_TABLE_USER_PREF);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	
	
}


