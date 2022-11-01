package com.trn.ns.test.viewer.basic;


import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
import java.awt.AWTException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//F118_Functional.NS.I34_AcceptRejectUILogic-CF0304ARevD -revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LogGenerationTest extends TestBase  {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private HelperClass helper;
	
	private ExtentTest extentTest;
	private RegisterUserPage registerUserPage;
	String username = "username";
	String username1="test";
	String password1="test123@";

	String filePath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);
	
	String filePathboneAge = Configurations.TEST_PROPERTIES.get("Boneage_filepath");
	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathboneAge);

	String piccLineFilePath = Configurations.TEST_PROPERTIES.get("Picline_filepath");
	String picclinePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, piccLineFilePath);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String liver9=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9);


	String uName = Configurations.TEST_PROPERTIES.get("nsUserName");
	String pwd = Configurations.TEST_PROPERTIES.get("nsPassword");
	private ViewerLayout layout;
	private ViewerARToolbox arbar;
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(uName,pwd);	

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge","US244","Sanity"})
	public void test01_US244_TC2363_verifyLogAreGettingSavedInDatabase() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verfiy log are getting saved in Database");

		DatabaseMethods db = new DatabaseMethods(driver);

		//verify all column exist in a log table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify all the column are present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.LOGID_COLUMN), "Verify LogID column exist in Log table", "LogID column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.DATE_COLUMN), "Verify Date Time column exist in Log table", "Date Time column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.SERVICEID_COLUMN), "Verify Service ID column exist in Log table", "Service ID column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.ACCOUNTID_COLUMN), "Verify Account ID column exist in Log table", "Account ID column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.WORKFLOWID_COLUMN), "Verify WorkFlow ID column exist in Log table", "Work Flow ID column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.ERRORLEVELID_COLUMN), "Verify Error level ID column exist in Log table", "Error level ID column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.MESSAGE_COLUMN), "Verify Message column exist in Log table", "Message column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.EXECPTION_COLUMN), "Verify Exception column exist in Log table", "Exception column is present in Log Table");
		db.assertTrue(db.verifyColumnExistInTable(NSDBDatabaseConstants.NS_LOG_TABLE,NSDBDatabaseConstants.STACK_TRACE_COLUMN), "Verify Stack Trace column exist in Log table", "Stack Trace column is present in Log Table");

		//verify all the logging service are present in service ID column on Log table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify all logging service are present in service ID coloumn");
		db.assertTrue(db.verifyLoggingServiceInLogTable(NSDBDatabaseConstants.LAYOUT_SERVICE), "Verify Layout Service is present in service ID cloumn", "Layout Service is present in Service ID column");
		db.assertTrue(db.verifyLoggingServiceInLogTable(NSDBDatabaseConstants.STUDY_SERVICE), "Verify Study Service is present in service ID cloumn", "Study Service is present in Service ID column");
		db.assertTrue(db.verifyLoggingServiceInLogTable(NSDBDatabaseConstants.SETTING_SERVICE), "Verify Setting Service is present in service ID cloumn", "Setting Service is present in Service ID column");
		db.assertTrue(db.verifyLoggingServiceInLogTable(NSDBDatabaseConstants.AUTHENTICATION_SERVICE), "Verify Authenication Service is present in service ID cloumn", "Authentication Service is present in Service ID column");
		db.assertTrue(db.verifyLoggingServiceInLogTable(NSDBDatabaseConstants.CONFIG_SERVICE), "Verify Configuration Service is present in service ID cloumn", "Configuration Service is present in Service ID column");
		db.assertTrue(db.verifyLoggingServiceInLogTable(NSDBDatabaseConstants.WIAGATE_SERVICE), "Verify WIA Gate Service is present in service ID cloumn", "WIA GATE Service is present in Service ID column");

		//variable to hold current count of record in log table
		int beforeCount =db.getNumberRowsInLogTable();

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameAh4);

		//variable to hold current count of record in log table
		int afterCount =db.getNumberRowsInLogTable();

		//verify log count increase on opening study list
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify log count increase in log table");
		patientPage.assertTrue(afterCount > beforeCount, "Verify log count increase in log table on opening study list page", "The log count increase from "+beforeCount+" to "+afterCount);

		beforeCount=db.getNumberRowsInLogTable();

		patientPage.clickOntheFirstStudy();
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		layout = new ViewerLayout(driver);
		
		//Change a layout to 1X2
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		afterCount =db.getNumberRowsInLogTable();

		//verify log count increase on layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify log count increase in log table");
		patientPage.assertTrue(afterCount > beforeCount, "Verify log count increase in log table on opening Viewer page", "The log count increase from "+beforeCount+" to "+afterCount);

	}

	//Feedback send back to the EnvoyAI Liaison for non-DICOM and non-GSPS result- Bone Age- Select one from multiple
//	TC1: Send to PACS-BoneAge (Automated) 
	@Test(groups ={"IE11","Chrome","firefox","US604","Sanity"})
	public void test02_US604_TC2019_verifyEnvoyAIFeedbackForBoneage() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2019 : Feedback send back to the EnvoyAI Liaison for non-DICOM and non-GSPS result- Bone Age- Select one from multiple");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneagePatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(boneagePatientName, 4);
		layout = new ViewerLayout(driver);
		arbar = new ViewerARToolbox(driver);
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerPage.closingConflictMsg();

		//Verify the Result Selector check boxes in all view boxes containing boneage result
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]:Checkpoint[1/5]", "Verify Result selector checkboxes in all viewbox containing BoneAge Result" );
		viewerPage.assertTrue(viewerPage.isElementPresent(arbar.resultSelector(5)), "Verify Result selector is present on viewbox1", "Result selector is present on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(arbar.resultSelector(6)), "Verify Result selector is present on viewbox2", "Result selector is present on viewbox2");
		viewerPage.assertTrue(viewerPage.isElementPresent(arbar.resultSelector(7)), "Verify Result selector is present on viewbox3", "Result selector is present on viewbox3");

		//select result selector on view box1 
		arbar.checkBoneage(5);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]:Checkpoint[2/5]", "Verify Result selector is checked on viewbox1");
		viewerPage.assertTrue(viewerPage.isChecked(arbar.resultSelectorCheckBox(5)),"Verify Result selector is checked on viewbox1" , "Verified Result selector is checked on viewbox1' is seen to be selected by default." );

		//Verify on reloading study result selector state persist
		performLogoutAndLoginToApplication(boneagePatientName);
		viewerPage.waitForViewerpageToLoad(4);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerPage.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[2]:Checkpoint TC1[3]:Checkpoint[3/5]", "Verify on reloading study result selector state persist" );
		viewerPage.assertTrue(viewerPage.isChecked(arbar.resultSelectorCheckBox(5)),"Verify Result selector is checked on viewbox1" , "Verified Result selector is checked on viewbox1" );

		//select result selector on view box2 
		arbar.checkBoneage(6);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[4]:Checkpoint[4/5]", "Verify Result selector is checked on viewbox2");
		viewerPage.assertTrue(viewerPage.isChecked(arbar.resultSelectorCheckBox(6)),"Verify Result selector is checked on viewbox2" , "Verified Result selector is checked on viewbox2" );

		//Change layout to 3x3 and verify result selector result persist on layout change
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.closingConflictMsg();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[5]:Checkpoint TC1[6]:Checkpoint[5/5]", "Verify result selector result persist on layout change");
		viewerPage.assertTrue(viewerPage.isChecked(arbar.resultSelectorCheckBox(6)),"Verify Result selector is checked on viewbox2" , "Verified Result selector is checked on viewbox2" );
	}


	//In initial instance of browser, upon clicking the button, the Accept button check mark is highlighted in a green color to indicate that it has been selected. After relaunching NS, the "Accept" button on the result is still seen to be persisted
//	TC2: Send to PACS-PICCLINE (Automated) 
	@Test(groups ={"IE11","Chrome","firefox","US604","Sanity"})
	public void test03_US604_TC2034_verifyEnvoyAIFeedbackForPICCLINE() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2034: Feedback send back to the EnvoyAI Liaison for non-DICOM and non-GSPS result- PICCLINE - Binary Selector");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+picclinePatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(picclinePatientName, 2);
		layout = new ViewerLayout(driver);
		arbar = new ViewerARToolbox(driver);

		//Verify clicking the accept button, it gets highlighted in a green color to indicate that it has been selected. 
		arbar.acceptResult(1);
		viewerPage.verifyTrue(arbar.verifyResultsAreAccepted(1), "Checkpoint TC2[1]: Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		//Verify after re-loading study the "Accept" button on the result is still mark green
		performLogoutAndLoginToApplication(picclinePatientName);
		viewerPage.waitForPdfToRenderInViewbox(1);
		viewerPage.verifyTrue(arbar.verifyResultsAreAccepted(1), "Checkpoint TC2[2] : Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");	

		//Verify accept button on result is marked green on layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[3]", "Verified result selector seen in step1 is still selected after the Layout change to 1x2");
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerPage.verifyTrue(arbar.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		//Verify clicking the reject button, it gets highlighted in a red color to indicate that it has been selected. 
		arbar.rejectResult(1);
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(1), "Checkpoint TC2[4]:Verify result is rejected","Selected checkmark is highlighted in red color");

		//Verify on reloading study, the "reject" button still appear in red color
		performLogoutAndLoginToApplication(picclinePatientName);
		viewerPage.waitForPdfToRenderInViewbox(1);
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(1), "Verify result is rejected","Selected checkmark is highlighted in red color");	

		//Verify reject button appear red on layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7]", "Verified result selector seen in step1 is still selected after the Layout change to 1x2");
		layout.selectLayout(layout.oneByTwoLayoutIcon); 
		viewerPage.waitForPdfToRenderInViewbox(1);
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(1), "Verify result is rejected","Selected checkmark is highlighted in red color");	

		//Setting to default
		arbar.rejectResult(1);

	}

//	TC4:  Secondary Capture/PDF (Automated) 
	@Test(groups ={"IE11","Chrome","firefox","US604","Sanity"})
	public void test04_US604_TC2069_verifyEnvoyAIFeedbackForIMBIO() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2069: Feedback send back to the EnvoyAI Liaison for non-DICOM and non-GSPS result- Secondary Capture/PDF - Binary Selector");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatient+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatient, 3);
		layout = new ViewerLayout(driver);
		arbar = new ViewerARToolbox(driver);
		
		//Verify clicking the accept button, it gets highlighted in a green color to indicate that it has been selected. 
		arbar.acceptResult(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1]:Checkpoint[1/5]", "Verify Accept button check mark is highlighted in green color to indicate that it has been selected");
		viewerPage.verifyTrue(arbar.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		//Verify clicking the reject button, it gets highlighted in a red color to indicate that it has been selected.
		viewerPage.click(viewerPage.getViewPort(2));
		arbar.rejectResult(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Reject button check mark is highlighted in green color to indicate that it has been selected");
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(2), "Verify result is rejected","Selected checkmark is highlighted in red color");


		//Verify after reloading study on viewer, the state of binary selector button on the result is still seen to be persisted
		performLogoutAndLoginToApplication(imbioPatient);
		viewerPage.waitForPdfToRenderInViewbox(2);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2]:Checkpoint[3/5]", "Verify binary selector state persist on reloading study");
		viewerPage.verifyTrue(arbar.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");	
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(2), "Verify result is rejected","Selected checkmark is highlighted in red color");	

		//Verify result selector on "Result" / "PDF" is still selected after the layout change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[3]:Checkpoint[4/5]", "Verify binary selector state persist on layout change");
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerPage.verifyTrue(arbar.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");	
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(2), "Verify result is rejected","Selected checkmark is highlighted in red color");	

	}

//	TC3: Envoy Persistence check (Automated) 
	@Test(groups ={"IE11","Chrome","firefox","US604","Sanity"},enabled=false)
	public void test05_US604_TC2035_verifyEnvoyAIFeedbackForPersistenceCheckOnMultipleData() throws InterruptedException, SQLException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2035: Feedback send back to the EnvoyAI Liaison for non-DICOM and non-GSPS result- Envoy Persistence check");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneagePatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(boneagePatientName, 4);
		arbar = new ViewerARToolbox(driver);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		
		//Verify the Result Selector check boxes in all view boxes containing boneage result
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1]:Checkpoint[1]", "Verify Result selector checkboxes in all viewbox containing BoneAge Result" );
		viewerPage.assertTrue(viewerPage.isElementPresent(arbar.resultSelector(1)), "Verify Result selector is present on viewbox1", "Result selector is present on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(arbar.resultSelector(2)), "Verify Result selector is present on viewbox2", "Result selector is present on viewbox2");
		viewerPage.assertTrue(viewerPage.isElementPresent(arbar.resultSelector(3)), "Verify Result selector is present on viewbox3", "Result selector is present on viewbox3");

		//Accept non DICOM (JPEG) image
		arbar.checkBoneage(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2]:Checkpoint[2]", "Truncating log table from database");
		DatabaseMethods DB = new DatabaseMethods(driver);
		//DB.truncateLogTable();

		//clicking on send to pacs button
		viewerPage.click(sd.sendToPacs);

		//fetching logline/message line from database that contain logs values like user, date, status 
		String message = DB.getFullMessageFromLogContainsString(NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT);
		
		//verifying that result status value in database
		verifyLogDataAfterSendingToPacs(message,NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT);

		//verifying status of result is "accept"
		viewerPage.assertTrue(message.contains(NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT), "Checkpoint TC3[4]:Checkpoint[4]", "Verified boneage accept message");

		//Setting to default
		arbar.checkBoneage(1);

		//deleting logs from log table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[5]:Checkpoint[5]", "Truncating log table from database");
		DB.truncateLogTable();

		//Moving to study page

		helper.browserBackAndReloadViewer(boneagePatientName, 1, 1);
		//Clicking on piccline patient
				viewerPage.waitForPdfToRenderInViewbox(1);


		//Verify clicking the reject button, it gets highlighted in a red color to indicate that it has been selected. 
				arbar.rejectResult(1);

		//Verify result is rejected
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(1), "Checkpoint[5]","Verified piccline result");	

		//clicking on send to pacs button
		viewerPage.click(sd.sendToPacs);

		//added wait to fetch the values from DB
		viewerPage.waitForTimePeriod(5000);

		//fetching logline/message line from database that contain logs values like user, date, status 
		message = DB.getFullMessageFromLogContainsString(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT);
		
		//verifying that result status value in database
		verifyLogDataAfterSendingToPacs(message, NSDBDatabaseConstants.LOGENTRYSTATUSREJECT);

		viewerPage.assertTrue(message.contains(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT), "Checkpoint[7]", "Verified piccline reject message");
		//Setting to default
		arbar.rejectResult(1);

		//deleting logs from log table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8]", "Truncating log table from database");
		DB.truncateLogTable();

		//Moving to patient page
		helper.browserBackAndReloadViewer(imbioPatient, "", 1, 2);

		//Verify clicking the reject button, it gets highlighted in a red color to indicate that it has been selected. 
		arbar.rejectResult(1);
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(1),"Checkpoint TC3[5]:Checkpoint[9]", "Verified imbioPatient result");	

		//clicking on send to pacs button
		viewerPage.click(sd.sendToPacs);

		//added wait to fetch the values from DB
		viewerPage.waitForTimePeriod(5000);

		//fetching logline/message line from database that contain logs values like user, date, status 
		message = DB.getFullMessageFromLogContainsString(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT);
		
		//verifying that result status value in database
		verifyLogDataAfterSendingToPacs(message, NSDBDatabaseConstants.LOGENTRYSTATUSREJECT);
		viewerPage.assertTrue(message.contains(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT), "Checkpoint[10]", "Verified imbioPatient reject message");

		//Setting to default
		arbar.rejectResult(1);

	}

//	TC5: Send to PACS: non-DICOM and non-GSPS result (Automated) 
	@Test(groups ={"IE11","Chrome","firefox","US604","Sanity"},enabled=false)
	public void test06_US604_TC2035_verifyEnvoyAIFeedbackForNonDicom() throws InterruptedException, SQLException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2155: Feedback send back to the EnvoyAI Liaison for non-DICOM and non-GSPS result");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatient+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatient, 2);
		arbar = new ViewerARToolbox(driver);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		
		//Verify clicking the reject button, it gets highlighted in a red color to indicate that it has been selected. 
		arbar.rejectResult(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[1]:Checkpoint[1/5]", "Verify Reject button check mark is highlighted in green color to indicate that it has been selected");
		viewerPage.verifyTrue(arbar.verifyResultsAreRejected(1), "Verify result is rejected","Selected checkmark is highlighted in red color");

		//clicking on send to pacs button
		viewerPage.click(sd.sendToPacs);

		//added wait to fetch the values from DB
		viewerPage.waitForTimePeriod(5000);

		//fetching logline/message line from database that contain logs values like user, date, status 
		DatabaseMethods DB = new DatabaseMethods(driver);
		String message = DB.getFullMessageFromLogContainsString(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT);
		
		//verifying that result status value in database
		verifyLogDataAfterSendingToPacs(message, NSDBDatabaseConstants.LOGENTRYSTATUSREJECT);
		viewerPage.assertTrue(message.contains(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT), "Checkpoint[2]", "Verified imbioPatient reject message");

		//Setting to default
		arbar.rejectResult(1);

	}

	@Test(groups ={"firefox","Chrome","Edge","DE719","DE719"})
	public void test07_DE719_TC2724_verifySendToPacsButtonEnableOrDisable() throws InterruptedException, SQLException, AWTException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2724 :Results are seen to be selected by default(single and binary selector)- Verify send to PACS button enabled/disabled");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameAh4+"in viewer");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);

		viewerPage = helper.loadViewerDirectly(liver9PatientName, 2);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);		
		CircleAnnotation circle = new CircleAnnotation(driver);
		//verifying that for AH.4 data if no gsps present than 'SendToPacs' button is disable
		sd.openAndCloseOutputPanel(true);

		viewerPage.assertFalse(viewerPage.verifyButtonEnabled(sd.sendToPacs),"Checkpoint[1]", "Verified SendToPacs button is enable or not");

		//verifying browser console errors
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Checkpoint[2]", "Verified console errors");

		//drawing annoation and check that 'send to pacs' button is enable
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, 50, 50);

		//verifying 'send to pacs' button is enable
		viewerPage.assertTrue(viewerPage.verifyButtonEnabled(sd.sendToPacs),"Checkpoint[3]", "Verified SendToPacs button is enable or not");

		//deleting the annotation
		circle.deleteAllAnnotation(1);

		//verifying that 'send to pacs' button is disable
		viewerPage.assertFalse(viewerPage.verifyButtonEnabled(sd.sendToPacs),"Checkpoint[4]", "verified SendToPacs button is enable or not");

		//verifying browser console errors
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Checkpoint[5]", "verified console errors");

	}

	@Test(groups ={"firefox","Chrome","Edge","DE719"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test07_DE719"})
	public void test08_DE719_TC2714_verifySendToPacsButtonEnableOrDisable(String fileName) throws InterruptedException, SQLException, AWTException 	{
		DatabaseMethods DB = new DatabaseMethods(driver);
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("TC2714: Results are seen to be selected by default(single and binary selector)- Verify with no binary selector/GSPS annnotations");

		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(PatientName, 1);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		
		viewerPage.clearConsoleLogs();
		//deleting logs from log table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Truncating log table from database");

		//verifying 'send to pacs' button is enable
		sd.openAndCloseOutputPanel(true);
		viewerPage.assertTrue(viewerPage.verifyButtonEnabled(sd.sendToPacs),"Checkpoint[2]", "verified  SendToPacs button is enable or not");


		//clicking on send to pacs button
		viewerPage.click(sd.sendToPacs);

		//verifying browser console errors
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Checkpoint[3]", "verified console errors");

		//verifying 'Send to pacs response is ok'
//		viewerPage.assertTrue(NSConstants.SENDTOPACSRESPONSEOK.equalsIgnoreCase(DB.getFullMessageFromLogContainsString(NSConstants.SENDTOPACSRESPONSEOK)), "Verifying send to pacs response is ok", "verified");
		String studyID = DB.getStudyInstanceUID(PatientName);
		viewerPage.assertTrue(DB.getUserFeedback(studyID).isEmpty(),"verifying if feedback is pending","verified");
		

	}

	//US948:Track studies/machines that are being viewed at the moment
	
	@Test(groups ={"IE11","Chrome","firefox","US948","positive"})
	public void test09_US948_TC4234_verifyStudyLockTableInDatabase() throws InterruptedException, SQLException, ParseException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify table in Database which indicate study is locked or unlocked");
		DatabaseMethods DB = new DatabaseMethods(driver);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Loading the Patient "+patientNameAh4+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientNameAh4, 1);
	
		
		//verify default lock time in config setting table
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify default DataLockExpireTime in database");
		viewerPage.assertEquals(DB.getDataLockExpireTimeDefault(),NSDBDatabaseConstants.DEFAULT_DATA_LOCK_EXPIRE_TIME, "Verify default DataLockExpireTime in config setting table", "Verified");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify entry is seen in StudyLockStatus table for loaded study page" );
		viewerPage.assertTrue((DB.getLockExpireTime(patientNameAh4)!=null),"Verify StudyLevelID and LockExpireTime in database for the study which is loaded in the viewer","Verified"); 
	}
	
	@Test(groups ={"IE11","Chrome","firefox","US948","positive"})
	public void test10_US948_TC4237_verifyWhenDefaultLockTimeExpired() throws InterruptedException, ParseException, Throwable 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify table in Database when default LockExpireTime expired");
		DatabaseMethods DB = new DatabaseMethods(driver);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+patientNameAh4+"in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientNameAh4, 1);
	
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify default DataLockExpireTime in database");
        viewerPage.assertEquals(DB.getDataLockExpireTimeDefault(), NSDBDatabaseConstants.DEFAULT_DATA_LOCK_EXPIRE_TIME, "Verify default DataLockExpireTime in config setting table", "Verified");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify entry is seen in StudyLockStatus table for loaded study page" );
		viewerPage.assertFalse(DB.getLockExpireTime(patientNameAh4).isEmpty(),"Verify StudyLevelID for the study which is loaded in the viewer and LockExpireTime in database","Verified");
	
		loginPage.logout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify LockExpireTime in StudyLockStatus table after logout from application" );
		viewerPage.assertTrue((DB.getLockExpireTime(patientNameAh4)).isEmpty(),"Verify data entry not seen in StudyLockStatus after logout from application","Verified");	
	}
	
	@Test(groups ={"IE11","Chrome","firefox","US948","positive"})
	public void test11_US948_TC4236_verifyTimeUpdatedInDBWhenStudyIsLocked() throws InterruptedException, SQLException, ParseException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of time updated in DB when study is locked");
		String dateOfFormat="yyyy-MM-dd HH:mm";
		DatabaseMethods DB = new DatabaseMethods(driver);
	
		DateFormat dateFormat = new SimpleDateFormat(dateOfFormat);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Loading the Patient "+patientNameAh4+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameAh4);

		patientPage.clickOntheFirstStudy();
		
		//get timestamp of loaded study page
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);  
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForAllImagesToLoad();
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify default DataLockExpireTime in database");
		viewerPage.assertEquals(DB.getDataLockExpireTimeDefault(), NSDBDatabaseConstants.DEFAULT_DATA_LOCK_EXPIRE_TIME, "Verify default DataLockExpireTime in config setting table", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify LockExpireTime in StudyLockStatus table for loaded study page" );
	    String timeStamp=DB.getLockExpireTime(patientNameAh4);
	    Date date1=new SimpleDateFormat(dateOfFormat).parse(timeStamp);
        viewerPage.assertEquals(dateFormat.format(date1), dateFormat.format(cal.getTime()), "Verify LockExpireTime in DB for loaded study page", "Verified");
	}
	
	@Test(groups ={"IE11","Chrome","firefox","US948","positive"})
	public void test12_US948_DE1196_verifyStudyLockInMultiTabScenario() throws InterruptedException, SQLException, ParseException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify study lock functionality in multi-tab scenario as well as with different user");
		String dateOfFormat="yyyy-MM-dd HH:mm";
		DatabaseMethods DB = new DatabaseMethods(driver);		
		DateFormat dateFormat = new SimpleDateFormat(dateOfFormat);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+patientNameAh4+"in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameAh4);
		patientPage.clickOntheFirstStudy();		
		//get timestamp of loaded study page
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);  
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForAllImagesToLoad();
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify default DataLockExpireTime in database");
		viewerPage.assertEquals(DB.getDataLockExpireTimeDefault(), NSDBDatabaseConstants.DEFAULT_DATA_LOCK_EXPIRE_TIME, "Verify default DataLockExpireTime in config setting table", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify LockExpireTime in StudyLockStatus table for loaded study page" );
	    String timeStamp=DB.getLockExpireTime(patientNameAh4);
	    Date date1=new SimpleDateFormat(dateOfFormat).parse(timeStamp);
        viewerPage.assertEquals(dateFormat.format(date1), dateFormat.format(cal.getTime()), "Verify LockExpireTime in DB for loaded study page", "Verified");
        viewerPage.assertEquals(db.getStudyLockStatusCount(patientNameAh4), 1, "Verify single entry in database as study page loaded in one tab window", "Verified");
        
        //check study lock status when viewer page open in new tab window 
        loginPage.openNewWindow(URLConstants.BASE_URL);
    	loginPage.login(uName,pwd);
    	patientPage.waitForPatientPageToLoad();
    	helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameAh4, 1);
    	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", " Verify study lock entry when study page loaded by same user but in different tab window" );
        viewerPage.assertEquals(db.getStudyLockStatusCount(patientNameAh4), 1, "Verify 1 entry in database as study page loaded in 2 tab window", "Verified");
        loginPage.logout();
        
        //check study lock status when viewer page loaded by two different users
        loginPage.login(uName,pwd);
        patientPage.waitForPatientPageToLoad();
    	loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
	    registerUserPage.createNewUser(username, username, LoginPageConstants.SUPPORT_EMAIL, username, username, username);
	
		loginPage.logout();
		loginPage.navigateToBaseURL();
		loginPage.login(username,username);
		patientPage.waitForPatientPageToLoad();
		viewerPage = helper.loadViewerDirectly(patientNameAh4, 1);
	   	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", " Verify study lock entry when study page loaded by two different users." );
        viewerPage.assertEquals(db.getStudyLockStatusCount(patientNameAh4), 1, "Verify 1 entry in database as study page loaded in by two different users", "Verified");
	}
	
	@Test(groups ={"IE11","Chrome","DE1053","positive"})
	public void test13_DE1053_TC4528_verifyLogFileAsCSVForNonUILogin() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		loginPage = new LoginPage(driver);
		extentTest.setDescription("Verify that Log file is getting generated for NON UI login when password has special character");

		patientPage = new PatientListPage(driver);
		//Create test user with password has special character
		patientPage = new PatientListPage(driver);
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
	    registerUserPage.createNewUser(username1, username1, LoginPageConstants.SUPPORT_EMAIL, username1, password1, password1);
		loginPage.logout();
		loginPage.navigateToBaseURL();
	
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username1);
		hm.put(LoginPageConstants.PASSWORD,password1 );
	    String nonUIURLForLog="log?fromdate=yesterday&todate=now&errorlevel=info&outputtype=csv"; 
		String myURL = loginPage.getNonUILaunchURL(nonUIURLForLog, hm);
		
		//navigate to Non UI login URL
		loginPage.navigateToChangedURL(myURL);
		loginPage.waitForTimePeriod(20000);
		
		String logFilePath=Configurations.TEST_PROPERTIES.get("logFolderPath");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify file download after accessing Non-UI login having password as special character" );
    	loginPage.assertTrue(loginPage.isFileDownloaded(logFilePath + NSDBDatabaseConstants.NS_LOG_TABLE+"."+LoginPageConstants.FILE_EXTENSION_AS_CSV), "Verify file downloaded on accessing URL", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify extension of downloaded file" );
		loginPage.assertTrue(loginPage.verifyExtensionOfFile(logFilePath, NSDBDatabaseConstants.NS_LOG_TABLE, LoginPageConstants.FILE_EXTENSION_AS_CSV),"Verify csv file downloaded on accessing URL","Verified");
 
	}
	
	@Test(groups ={"IE11","Chrome","DE1053","positive"})
	public void test14_DE1053_TC4528_verifyLogFileAsJSONForNonUILogin() throws InterruptedException
	{
		
		extentTest = ExtentManager.getTestInstance();
		loginPage = new LoginPage(driver);
		extentTest.setDescription("Verify that Log file is getting generated for NON UI login when password has special character");
		
		//Create test user with password has special character
		patientPage = new PatientListPage(driver);
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
	    registerUserPage.createNewUser(username1, username1, LoginPageConstants.SUPPORT_EMAIL, username1, password1, password1);
		loginPage.logout();
		loginPage.navigateToBaseURL();
	
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username1);
		hm.put(LoginPageConstants.PASSWORD,password1 );
	    String nonUIURLForLog="log?@&fromDate=All&toDate=Now&level=info&outputtype=json";
		String myURL = loginPage.getNonUILaunchURL(nonUIURLForLog, hm);
		
		//navigate to Non UI login URL
		loginPage.navigateToChangedURL(myURL);
		loginPage.waitForTimePeriod(20000);
		
		String logFilePath=TEST_PROPERTIES.get("logFolderPath");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify file download after accessing Non-UI login having password as special character" );
		loginPage.assertTrue(loginPage.isFileDownloaded(logFilePath + NSDBDatabaseConstants.NS_LOG_TABLE+"."+LoginPageConstants.FILE_EXTENSION_AS_JSON), "Verify file downloaded on accessing URL", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify extension of downloaded file" );
		loginPage.assertTrue(loginPage.verifyExtensionOfFile(logFilePath, NSDBDatabaseConstants.NS_LOG_TABLE, LoginPageConstants.FILE_EXTENSION_AS_JSON),"Verify json file downloaded on accessing URL","Verified");
 
	}
	
	
	
	public void performLogoutAndLoginToApplication(String patientName) throws InterruptedException {
		Header header = new Header(driver);
		header.logout();
		loginPage.login(uName,pwd);
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, 1);
	}

	private void verifyLogDataAfterSendingToPacs(String logLine, String resultStatus) {
		viewerPage.assertTrue(logLine.contains(NSDBDatabaseConstants.LOGENTRYDATE), "Checkpoint TC5[2]: Checkpoint TC3[6]:Checkpoint TC3[3]:Verifying Date is present in log file", "Verified Date");
		viewerPage.assertTrue(logLine.contains(NSDBDatabaseConstants.LOGENTRYUSER), "Checkpoint TC5[2]: Checkpoint TC3[6]:Checkpoint TC3[3]:Verifying User is present in log file", "Verified User");
		viewerPage.assertTrue(logLine.contains(NSDBDatabaseConstants.LOGENTRYARCHIVE), "Checkpoint TC5[2]: Checkpoint TC3[6]:Checkpoint TC3[3]:Verifying Archive is present in log file", "Verified Archive");
		if(resultStatus.equalsIgnoreCase(NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT)){
			viewerPage.assertTrue(logLine.contains(NSDBDatabaseConstants.LOGENTRYSTATUSACCEPT), "Checkpoint TC5[2]: Checkpoint TC3[6]:Checkpoint TC3[3]:Verifying reject is present in log file", "Verified Accept");
		}else {
			viewerPage.assertTrue(logLine.contains(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT), "Checkpoint TC5[2]: Checkpoint TC3[6]:Checkpoint TC3[3]:Verifying accept is present in log file", "Verified Reject");}
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {
		DatabaseMethods db = new DatabaseMethods(driver);	
		loginPage = new LoginPage(driver);
		db.deleteAllStudyLockStatus();
	    db.deleteUser(username);
	    db.deleteUser(username1);
	    loginPage.deleteDownloadedFile(TEST_PROPERTIES.get("logFolderPath")+NSDBDatabaseConstants.NS_LOG_TABLE+"."+LoginPageConstants.FILE_EXTENSION_AS_JSON);
	    loginPage.deleteDownloadedFile(TEST_PROPERTIES.get("logFolderPath")+NSDBDatabaseConstants.NS_LOG_TABLE+"."+LoginPageConstants.FILE_EXTENSION_AS_CSV);
	}


}
