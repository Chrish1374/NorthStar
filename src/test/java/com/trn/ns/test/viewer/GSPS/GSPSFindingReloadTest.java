package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.openqa.selenium.TimeoutException;
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
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class GSPSFindingReloadTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerSliderAndFindingMenu viewerPage;
	
	private ExtentTest extentTest;
	private RegisterUserPage registerUserPage;
	String username = "test";


	String filePathAidoc = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String GSPS_PatientName_Aidoc = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidoc);
	String GSPS_Patient_Aidoc_Result1=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,filePathAidoc);
	String GSPS_Patient_Aidoc_Result2=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY,filePathAidoc);

	String filePathAidocMachine = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String GSPS_PatientName_Aidoc_machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidocMachine);

	String filePathBoneAge = Configurations.TEST_PROPERTIES.get("Boneage_filepath");
	String patientName_BoneAge = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathBoneAge);
	String patient_BoneAge1_Result1 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,filePathBoneAge);
	String patient_BoneAge1_Result2 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY,filePathBoneAge);
	String patient_BoneAge1_Result3 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY,filePathBoneAge);

	String filePathPiccline = Configurations.TEST_PROPERTIES.get("Picline_filepath");
	String patientName_Piccline = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathPiccline);

	String filePathPiccOne = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String patientName_PiccOne = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathPiccOne);


	private ContentSelector cs;
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
		db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
		db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_FALSE); 

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		
		
	}


	@Test(groups ={"Chrome","US650","Positive"})
	public void test01_US650_TC3602_verifyDifferentMachinesAtStudyLevel() throws InterruptedException, TimeoutException, java.text.ParseException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify different machines listed on study page");

		patientPage=new PatientListPage(driver);
	    
		patientPage.searchPatient(GSPS_PatientName_Aidoc, "", "", "");	
		patientPage.clickOnPatientRow(GSPS_PatientName_Aidoc);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify First machine details on study level");
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.AIDOC_MACHINE_NAME1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify Second machine details on study level");
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.AIDOC_MACHINE_NAME2);

	}

	@Test(groups ={"Chrome","US650","Positive"})
	public void test02_US650_TC3624_verifyInitialLoad() throws InterruptedException, TimeoutException, java.text.ParseException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify initial load for"+" "+GSPS_PatientName_Aidoc);

		patientPage=new PatientListPage(driver);
		patientPage.searchPatient(GSPS_PatientName_Aidoc, "", "", "");	
		patientPage.clickOnPatientRow(GSPS_PatientName_Aidoc);	

		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.AIDOC_MACHINE_NAME1);
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.AIDOC_MACHINE_NAME2);

		patientPage.clickOntheFirstStudy();
		viewerPage=new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify intial load of machine data. ");
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(GSPS_Patient_Aidoc_Result1), "Verify series loaded on viewer", "Verified that aidoc-lesion-finder series loaded on viewer.");
	}

	// obsolete
	//	@Test(groups ={"Chrome","US650","Positive"})
	public void test03_US650_TC3626_verifyAcceptRejectWithMultipleSelectorType() throws InterruptedException, TimeoutException, java.text.ParseException, SQLException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Accept/Reject functionality with multiple selector type for "+" "+patientName_BoneAge);

		patientPage=new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName_BoneAge);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify imported machin ID`s on study list");
		
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.BONEAGE_MACHINE_NAME);
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.BONEAGE_MACHINE_NAME1);

		//click on study
		patientPage.clickOntheFirstStudy();
		viewerPage=new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad(4);
		cs = new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		//reject result on 5th viewbox
		DatabaseMethods db=new DatabaseMethods(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify log fro rejected result");
		cs.selectResultFromSeriesTabWithMachineName(5, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.rejectResult(5);
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(5),"Verify Result are accepted","Verified that result is rejected and bonage is check for another machine data");

		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(false, true, false);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);

		//		viewerPage.performMouseRightClick(viewerPage.sendToPacs);
		//		viewerPage.click(viewerPage.pacsRejectedFindingSlider);
		//		viewerPage.click(viewerPage.pacsPendingFindingSlider);
		//		viewerPage.click(viewerPage.sendToPacs);
		viewerPage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.SEND_TO_PACS_LOG+"%").contains(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT),"verifying if feedback send successfully","verified");
		viewerPage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.SEND_TO_PACS_LOG+"%").contains(Configurations.TEST_PROPERTIES.get("nsUserName")),"verifying if feedback send successfully","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify log for accepted result");
		cs.selectResultFromSeriesTabWithMachineName(2, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME);
		viewerPage.checkBoneage(2);
		viewerPage.assertTrue(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(2)),"Verify Result selector is checked on viewbox1" , "Verified Result selector is checked on viewbox1' is seen to be selected by default." );
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
		viewerPage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.SEND_TO_PACS_LOG+"%").contains(NSDBDatabaseConstants.LOGENTRYSTATUSREJECT),"verifying if feedback send successfully","verified");
		viewerPage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.SEND_TO_PACS_LOG+"%").contains(Configurations.TEST_PROPERTIES.get("nsUserName")),"verifying if feedback send successfully","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify log for pending state in database");
		cs.selectResultFromSeriesTabWithMachineName(1, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.rejectResult(1);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);
		viewerPage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.SEND_TO_PACS_LOG+"%").contains(""),"verifying if feedback send successfully","verified");
		viewerPage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.SEND_TO_PACS_LOG+"%").contains(Configurations.TEST_PROPERTIES.get("nsUserName")),"verifying if feedback send successfully","verified");
	}

	@Test(groups ={"Chrome","US650","Positive"})
	public void test04_US650_TC3669_VerifySendToPACSForNonDicom() throws InterruptedException, TimeoutException, java.text.ParseException, SQLException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify send to PACS functionality for non-dicom patient "+" "+patientName_PiccOne);

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientName_PiccOne, 1, 1);		
		cs = new ContentSelector(driver);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify result are in pending state or not");
		viewerPage.assertFalse(viewerPage.verifyResultsAreAccepted(1),"Verify results are accepted","Result not accepted on viewbox-1");
		viewerPage.assertFalse(viewerPage.verifyResultsAreRejected(1),"Verify results are rejected","Result not rejected on viewbox-1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify result is rejected and red color seen on button");
		viewerPage.rejectResult(1);
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(1),"Verify reject button click.","Reject button is clicked and user can see red color on button.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify on config setting only reject is enabled");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(false, true, false);

		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when PACSAcceptedFinding slider is Disable", "when function is disable,Gray for PACSAcceptedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Green seen when pacsRejectedFinding slider is Enable", "when function is enable,Green for PACSRejectedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is disable,Gray for PACSPendingFinding seen");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify reject state is persisted after reloading study page");
		helper.browserBackAndReloadViewer(patientName_PiccOne, 1, 1);	
		
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(1),"Verify reject button click.","Reject button is clicked and user can see red color on button.");

		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify send to PACS status in database");
		//		viewerPage.click(viewerPage.sendToPacs);
		//		viewerPage.assertEquals(db.getFullMessageFromLogContainsString(NSConstants.SEND_TO_PACS_LOG+"%"),NSConstants.SEND_TO_PACS_RESPONSE_FAILED, "Verify status of send to PACS", "Verified that PACS send successfully.");

		//default to pending
		viewerPage.rejectResult(1);
	}

	@Test(groups ={"Chrome","US650","Positive"})
	public void test05_US650_TC3627_verifyMultipleMachineRuns() throws InterruptedException, SQLException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Accept/Reject functionality with multiple machine runs");

		patientPage=new PatientListPage(driver);

		helper = new HelperClass(driver);
		patientPage.searchPatient(GSPS_PatientName_Aidoc, "", "", "");	
		patientPage.clickOnPatientRow(GSPS_PatientName_Aidoc);
		patientPage.clickOntheFirstStudy();
		viewerPage=new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad();

		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		cs = new ContentSelector(driver);
		CircleAnnotation circle= new CircleAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify layout open is 2x2");

		cs.selectResultFromSeriesTabWithMachineName(1, GSPS_Patient_Aidoc_Result2,ViewerPageConstants.AIDOC_MACHINE_NAME2 );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/1]", "Verify different batch result display on viewer.");
		viewerPage.assertTrue(cs.verifyPresenceOfEyeIcon(GSPS_Patient_Aidoc_Result2), "Verify series loaded on viewer", "Verified that aidoc-lesion-finder1 series loaded on viewer.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/1]", "Verify GSPS state");
		viewerPage.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.selectAcceptfromGSPSRadialMenu(1);
		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(1),"Verify accept button click.","Accept button is clicked and user can see green color on button.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/1]", "Verify PACS send successfully message from log table");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, false, false);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, false);

//		viewerPage.assertEquals(db.getFullMessageFromLogContainsString(NSConstants.SEND_TO_PACS_LOG+"%"),NSConstants.SEND_TO_PACS_RESPONSE_OK,"verifying that feedback send successfully","verified success message for send to PACS");

	}

	@Test(groups ={"Chrome","US650","Positive"})
	public void test06_US650_TC3809_verifyWithMultipleUsers() throws InterruptedException,SQLException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify changes in accept-reject or choose-one selector with different users.");

		patientPage=new PatientListPage(driver);
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		registerUserPage = new RegisterUserPage(driver);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.createNewUser(username, username, LoginPageConstants.SUPPORT_EMAIL, username, username, username);
//		loginPage.logout();
		loginPage.navigateToChangedURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL);
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		
		patientPage.waitForPatientPageToLoad();
		patientPage.searchPatient(patientName_BoneAge,"","","");	
		
		patientPage.clickOntheFirstStudy(); 

		viewerPage=new ViewerSliderAndFindingMenu(driver);
		cs = new ContentSelector(driver);
		viewerPage.waitForViewerpageToLoad(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]","Verfiy atlas is selected and Boneage1 machine ID series is loaded and reject state applied for scan user");

		cs.selectResultFromSeriesTabWithMachineName(2, patient_BoneAge1_Result2, ViewerPageConstants.BONEAGE_MACHINE_NAME);
		viewerPage.checkBoneage(2);
		viewerPage.assertTrue(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(2)),"Verify Result selector is checked on viewbox1" , "Verified Result selector is checked on viewbox1' is seen to be selected by default." );

		cs.selectResultFromSeriesTabWithMachineName(4, patient_BoneAge1_Result2, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.closingConflictMsg();
		viewerPage.rejectResult(4);
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(4),"Verify Result are accepted","Verified that result is rejected and bonage is check for another machine data");

		//logout from application
		loginPage = new LoginPage(driver);
		loginPage.logout();

		//create test user and check
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]","Verfiy new user created for test");
		
		//navigate to application
		loginPage.navigateToBaseURL();
		loginPage.login(username,username);
		patientPage.waitForPatientPageToLoad();
        patientPage.searchPatient(patientName_BoneAge,"","","");	
		
		patientPage.clickOntheFirstStudy(); 
		viewerPage.waitForViewerpageToLoad(4);
		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]","Verfiy atlas+1 is selected and Boneage1 machine ID series is loaded and accept state applied for test user");
		//checkbox check for atlas+1
		cs.selectResultFromSeriesTabWithMachineName(2, patient_BoneAge1_Result2, ViewerPageConstants.BONEAGE_MACHINE_NAME);
		viewerPage.closingConflictMsg();
		
		cs.selectResultFromSeriesTabWithMachineName(3, patient_BoneAge1_Result3, ViewerPageConstants.BONEAGE_MACHINE_NAME);
		viewerPage.closingConflictMsg();
		
		cs.selectResultFromSeriesTabWithMachineName(4, patient_BoneAge1_Result2, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.closingConflictMsg();
		
		viewerPage.checkBoneage(3);
		viewerPage.assertTrue(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(3)),"Verify Result selector is checked on viewbox3" , "Verified Result selector is checked on viewbox3' is seen to be selected." );
		viewerPage.assertFalse(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(2)),"Verify Result selector is unchecked on viewbox2" , "Verified Result selector is checked on viewbox2' is seen to be unselected" );

		//select same result from content selector
		cs.selectResultFromSeriesTabWithMachineName(1, patient_BoneAge1_Result2, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.closingConflictMsg();		
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(1),"Verify Result are accepted","Verified that result is rejected and bonage is check for another machine data");

		//now accept the result
		viewerPage.acceptResult(1);
		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(1),"Verify accept button click.","Accept button is clicked and user can see Green color on button.");
		loginPage.logout();

		//login with scan user and check changes made by test user persisted on viewer
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientName_BoneAge);	
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]","Verfiy atlas+1 is selected and Boneage1 machine ID series is loaded and accept state applied for scan user");
		cs.selectResultFromSeriesTabWithMachineName(2, patient_BoneAge1_Result3, ViewerPageConstants.BONEAGE_MACHINE_NAME);
		viewerPage.closingConflictMsg();
		viewerPage.assertTrue(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(2)),"Verify Result selector is checked on viewbox1" , "Verified Result selector is checked on viewbox1' is seen to be selected by default." );

		cs.selectResultFromSeriesTabWithMachineName(4, patient_BoneAge1_Result2, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.closingConflictMsg();
		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(4),"Verify Result are accepted","Verified that result is rejected and bonage is check for another machine data");

		//enable all three state on Send to PACS - De1347 needs to be checked for validity if valid add DB verification else remove below part
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify all states are enable for send to PACS");
		sd.openSendToPACSMenu(true,true, true, true);
		//click on send
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify user click on send to PACS");
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);	
		String message=sd.getText(sd.notificationMessage.get(0));
		viewerPage.assertFalse(message.isEmpty(),"Verify user click on send to PACS","verified");	

		
	}

	private void getAndVerifyToolTipForMultipleMachines(String MachineName) throws TimeoutException, InterruptedException, java.text.ParseException  {
		// Mouse hover over Result Icon

		patientPage.mouseHover(patientPage.eurekaIcon);
		String str = patientPage.getEurekaAITooltip();
		
		//Verifing tooltip information
		patientPage.assertTrue(str.contains(MachineName), "Verifying "+MachineName +" is present in tooltip ", MachineName+ " is present in tooltip");
		patientPage.assertTrue(str.contains("Last updated:"), "Verifying Last updated: is present in tooltip", "Last updated: is present in tooltip");
		patientPage.assertTrue(str.contains("Result Status:"), "Verifying Result Status: Run with findings is present in tooltip", "Result Status: Run with findings is present in tooltip");
		//Verifying date format
		patientPage.assertTrue(patientPage.verifyDateFormat("MM/dd/yyyy, hh:mm aaa", patientPage.getDateTimeFromToolTip()), "Verifying date format present in tooltip","Date format is in MM/dd/yyyy, hh:mm ");

	}

	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException, IOException, InterruptedException {

		DatabaseMethods db = new DatabaseMethods(driver);
		if(!db.getValueFromConfigSettings(NSDBDatabaseConstants.KEY_SHADOW_COLOR).equals(ViewerPageConstants.SHADOW_COLOR)) {
			db.updateConfigTable(NSDBDatabaseConstants.KEY_SHADOW_COLOR, ViewerPageConstants.SHADOW_COLOR);		
			db.resetIISPostDBChanges();
		}
		db.deleteUser(username);
	}

	@Test(groups ={"Chrome","DE1079","Positive"})
	public void test07_DE1079_TC4634_verifyBothTheSelectorsAreNotDisplayed() throws InterruptedException, TimeoutException, ParseException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Boneage Specific : Both type of A/R selectors (checkbox and Binary) displayed at same time in same viewbox when user selects other machine results using content selectors- happy path");

		patientPage=new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName_BoneAge);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify imported machin ID`s on study list");
		
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.BONEAGE_MACHINE_NAME);
		getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.BONEAGE_MACHINE_NAME1);

		//click on study
		patientPage.clickOntheFirstStudy();
		viewerPage=new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad(4);
		cs = new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerPage.closingConflictMsg();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Select atlas-1 from boneage (single selection) in viewbox5. atlas-1 should be rejected in viewbox5.");
		cs.selectResultFromSeriesTabWithMachineName(5, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME1);

		viewerPage.assertTrue(viewerPage.verifyBinarySelectorToobar(5), "Verifying that binary toolbar is displayed", "verified");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.resultSelector(5)),"Verifying that no checkbox is displayed","verified");
		viewerPage.assertFalse(viewerPage.verifyResultsAreAccepted(5),"Verifying that result should not be accepted ","verified");
		viewerPage.assertFalse(viewerPage.verifyResultsAreRejected(5),"Verifying that result should not be rejected","verified");	


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Select 2nd Machine result (which contain checkbox) on 1st Machine result (which contain Binary selector for A/R) using content selector");
		cs.selectResultFromSeriesTabWithMachineName(2, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME);

		viewerPage.assertFalse(viewerPage.verifyBinarySelectorToobar(2), "User should see only single selection checkbox and it's in accepted state. No binary A/R toolbar should be visible.", "verified");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.resultSelector(2)),"User should see only single selection checkbox and it's in accepted state","verified");



	}

	@Test(groups ={"Chrome","DE1079","Positive"})
	public void test08_DE1079_TC4635_verifyBothTheSelectorsAreNotDisplayed() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification when same results on different viewbox.");

		patientPage=new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName_BoneAge);		

		//click on study
		patientPage.clickOntheFirstStudy();
		viewerPage=new ViewerSliderAndFindingMenu(driver);
		viewerPage.waitForViewerpageToLoad(4);
		cs = new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Change layout to 3*3 - Layout changed to 3*3 and both machine ID's images should be visible on viewer. First four images are from boneage machine ID and last three are from boneage-1.");
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerPage.closingConflictMsg();
		for(int i=1,j=5;i<=3 && j<=7;i++,j++) {
			viewerPage.assertTrue(viewerPage.verifyBinarySelectorToobar(i), "Verifying that binary toolbar is displayed - meaning from different machine", "verified");
			viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.resultSelector(j)),"Verifying that checkbox is displayed - meaning from different machine","verified");
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Reject atlas-1 series from boneage-1 (binary A/R toolbar) in viewbox1.");
		viewerPage.rejectResult(1);
		viewerPage.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Selecting the same result in viewebox 7 and verifying its state");
		cs.selectResultFromSeriesTabWithMachineName(7, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME1);
		viewerPage.closingConflictMsg();
		
		viewerPage.assertTrue(viewerPage.verifyBinarySelectorToobar(7), "Verifying that binary toolbar is displayed in viewbox 7", "verified");
		viewerPage.assertTrue(viewerPage.verifyBinarySelectorToobar(1), "Verifying that binary toolbar is displayed in viewbox 1", "verified");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.resultSelector(7)),"Verifying that no checkbox is displayed in viewbox 7","verified");
		viewerPage.assertFalse(viewerPage.verifyResultsAreAccepted(7),"Verifying that result should not be accepted in viewbox 7","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verifying the viewbox 7 and 1 state is same");
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(7),"Verifying that result should be rejected in viewbox 7","verified");	
		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(1),"Verifying that result should be rejected in viewbox 1","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Accepting the result in viewbox 7 and verifying it is also reflected in viewbox 1");
		viewerPage.acceptResult(7);
		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(1),"Verifying that result is accepted in viewbox 1","verified");
		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(7),"Verifying that result is accepted in viewbox 7","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Select 2nd Machine result (which contain checkbox) on 1st Machine result (which contain Binary selector for A/R) using content selector (Using content selector change the first viewbox to load the same result as shown in the 5th viewbox)");
		cs.selectResultFromSeriesTabWithMachineName(1, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME);
		viewerPage.closingConflictMsg();
		
		viewerPage.assertFalse(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(1)),"User should see only single selection checkbox and it's in rejected state in viewbox 1","verified");
		viewerPage.assertFalse(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(5)),"User should see only single selection checkbox and it's in rejecred state in viewbox 5","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Accepting the result in viewbox 1 and verifying it is also reflected in viewbox 5");
		viewerPage.checkBoneage(1);
		viewerPage.closingConflictMsg();
		
		viewerPage.assertTrue(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(1)),"User should see only single selection checkbox and it's in accepted state in viewbox 1","verified");
		viewerPage.assertTrue(viewerPage.isChecked(viewerPage.resultSelectorCheckBox(5)),"User should see only single selection checkbox and it's in accepted state in viewbox 5","verified");



	}

	@AfterMethod
	public void cleanUserFeedbackTable() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		 db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
	     db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
	     db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
	}

}



