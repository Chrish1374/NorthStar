package com.trn.ns.test.viewer.envoyAI;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import org.openqa.selenium.Keys;
import org.seleniumhq.jetty9.util.security.Password;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import java.text.ParseException;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSConfigUITest extends TestBase{
	
	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	
	private ViewerPage viewerPage;
	private RegisterUserPage registerUserPage;
	private ViewerSendToPACS sd;
	private ViewerARToolbox arToolbar ;
	private String loadedTheme;

	int count=4;
	
	//patient detail with multiple series data
	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String gspsFilePath =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath);
	
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
	
	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String ChestCT1p25mm = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestCT1p25mm);
	
	String username = "sendtopacs";
	private SimpleLine line;
	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private PointAnnotation pointAnn;
	private OutputPanel panel;
	private ContentSelector cs;
	static String patient_id="";

	String user=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(user,password);
		patientPage = new PatientListPage(driver);
	}
     //  TC3471_Verification Configuration UI opens when send to PACs button enabled

	@Test(groups ={"Chrome","Edge","IE11","US831","positive"})
	public void test01_US831_TC3471_verifyConfigurationUIOfPACS() throws  InterruptedException, SQLException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(" Verify Configuration UI opens when send to PACs button enabled");
		
		patientPage = new PatientListPage(driver);

	    helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName, 1);
	
		ellipse = new EllipseAnnotation(driver);
		sd=new ViewerSendToPACS(driver);
		
        //Step 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","send to PACs button is disabled when no GSPS present.");
		sd.openAndCloseOutputPanel(true);
		viewerPage.assertFalse(viewerPage.checkForElementClickability(sd.bySendToPacs),"verify Send to PACs button is disabled", "'Send to PACs' button is disabled on UI");
		
		//Step 2 First draw 1 annotation and verify 'send to PACs' button enable
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Send to PACs button is Enable on UI when GSPS drawn");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
        viewerPage.assertTrue(viewerPage.isEnabled(sd.sendToPacs),"verify Send to PACs button is Enable", "Send to PACs button is Enable on UI");
	
		//Step 2(Note): Verify that the Configuration UI shouldn't be opened with left key.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify that the Configuration UI shouldn't be opened with left key");
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName, 1, 1);
		viewerPage.waitForViewerpageToLoad();
		sd.openAndCloseOutputPanel(true);
		viewerPage.click(sd.sendToPacs);
		viewerPage.assertFalse(viewerPage.isElementPresent(sd.pacsAcceptedFinding), "Verify that the Configuration UI shouldn't be opened with left key", "'Send to PACs' button is Enable but UI not open with left key");
	
		//Step 3  Import multiseries (AH.4) data in the viewer and verify send to PACS button enabled
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Send to PACs button is Enable on UI when multiseries data import");
		viewerPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(patientName, 1, 1);
		sd.openAndCloseOutputPanel(true);
	    viewerPage.assertTrue(sd.sendToPacs.isEnabled(),"verify Send to PACs button is Enable", "Send to PACs button is Enable on UI");
		
}
		
	 //TC3472_Verify default configuration on UI and DB
	@Test(groups ={"Chrome","Edge","IE11","US831","sanity","positive","BVT"})
	public void test02_US831_TC3472_verifyDefaultConfigurationOfSendToPACSOnDB() throws  InterruptedException, SQLException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(" Verify default configuration on UI and DB");

	    patientPage = new PatientListPage(driver);
	
	    helper=new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName, 1);
		ellipse = new EllipseAnnotation(driver);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		
		//Step 1: Verification on DB when no change in UI.
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","'Verify default configuration on DB when no change on UI");
		
		viewerPage.assertTrue(db.getSendAcceptedFindingsDefault(), "Verify SendAcceptedFindingsDefault value seen as TRUE", "SendAcceptedFindingsDefault value seen as TRUE on DB when no change in UI");
	    viewerPage.assertFalse(db.getSendRejectedFindingsDefault(), "Verify SendRejectedFindingsDefault value seen as FALSE", "SendRejectedFindingsDefault value seen as FALSE on DB when no change in UI");
        viewerPage.assertFalse(db.getSendPendingFindingsDefault(),  "Verify SendPendingFindingsDefault value seen as FALSE", "SendPendingFindingsDefault value seen as FALSE on DB when no change in UI");
       
        //Step 2 Verification on UI when change in DB.
        
       db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
       db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
       db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
       
   	   ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","'Verify configuration on UI  when change in DB");
       viewerPage.assertFalse(db.getSendAcceptedFindingsDefault(), "Verify SendAcceptedFindingsDefault value seen as FALSE on UI", "SendAcceptedFindingsDefault value seen as FALSE on DB when when change in DB");
       viewerPage.assertTrue(db.getSendRejectedFindingsDefault(), "Verify SendRejectedFindingsDefault value as TRUE", "SendSendRejectedFindingsDefault value seen as TRUE on UI when change in DB ");
       viewerPage.assertTrue(db.getSendPendingFindingsDefault(), "Verify SendPendingFindingsDefault value as TRUE", "SendPendingFindingsDefault value seen as TRUE on UI when change in DB ");
        
	}
		
	 //  TC3473_Verification on how to open and close UI component----add method to verify each element is right aligned
	@Test(groups ={"Chrome","Edge","IE11","US831","US2222","Negative","E2E","F1094"})
	public void test03_US831_TC3473_US2222_TC9898_verifyOpenAndCloseUIOfSendToPACS() throws  InterruptedException, SQLException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification on how to open and close UI component.<br>"+
		"Verify that the send to pacs pop up is closed when user clicks outside the pop up box.");

		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	 	viewerPage =helper.loadViewerDirectly(patientName, 1);
	 	
		sd=new ViewerSendToPACS(driver);
		ellipse = new EllipseAnnotation(driver);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//Step 1: Right click on  'send to PACS' button to open up the configuration UI.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","'Verify configuration  UI open and its right-aligned to the bottom of send to pacs button");
		sd.openSendToPACSMenu();
		viewerPage.assertTrue(viewerPage.isElementPresent(sd.pacsAcceptedFinding), "Verify that the Configuration UI open for PACS", "UI component of send to PACs button open");
		
		//Step 2: Verify UI component closed using Escape key or by clicking off
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","UI component of ‘send to PACs button is close using Escape key.");
		viewerPage.pressKeys(Keys.ESCAPE);
		viewerPage.assertFalse(viewerPage.isElementPresent(sd.pacsAcceptedFinding), "Verify that the Configuration UI closed by Escape key", "UI component of send to PACs button is close using Escape key.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","UI component of send to PACs button is close by clicking off it.");
		sd.openSendToPACSMenu();
		viewerPage.click(sd.sendToPacs);
		viewerPage.assertFalse(viewerPage.isElementPresent(sd.pacsAcceptedFinding), "Verify that the Configuration UI closed by clicking off", "UI component of send to PACs button is close by clicking off.");
	
	}

	// TC3528_Verification of slider component
	@Test(groups ={"Chrome","Edge","IE11","US831","DR2662","negative"})
	public void test04_US831_TC3528_DR2662_TC10406_verifyPositionOfSliderComponent() throws  InterruptedException, SQLException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of slider component - cover US840. <br>"+
		"Verify Send accepted findings' and 'Enable user action tracking' options from sync to pacs are selected by default");

        patientPage = new PatientListPage(driver);
	   
	    helper=new HelperClass(driver);
	 	viewerPage =helper.loadViewerDirectly(patientName, 1);
	 	
		sd=new ViewerSendToPACS(driver);
		ellipse = new EllipseAnnotation(driver);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		
		//Step 1: Verify slider component functionality
	
		sd.openSendToPACSMenu();
		//Step 1 and Step 2 :Verify the slider position and Component Functionality
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]","'Verfiy slider component functionality for PACSAcceptedFinding ");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Green seen when PACSAcceptedFinding slider is Enable", "when function is enable,Green for PACSAcceptedFinding seen");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]","'Verfiy slider component functionality for PACSRejectedFinding ");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is disable,Gray for PACSRejectedFinding seen");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]","'Verfiy slider component functionality for PACSPendingFinding ");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsPendingFinding slider is Disable", "when function is disable,Gray for PACSPendingFinding seen");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]","'Verfiy slider component functionality for Enable user action tracking.");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsUserActionTracking, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Gray seen when pacsPendingFinding slider is Disable", "when function is disable,Gray for PACSPendingFinding seen");
		
		
		//Step 3: Click on slider small round button and perform enabled/disabled.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]","'Verfiy two posItion of slider and check functionlity of enabled/disabled");
		viewerPage.click(sd.pacsRejectedFindingSlider);
		viewerPage.assertTrue(viewerPage.isEnabled(sd.pacsRejectedFinding), "PACSRejectedFinding function is Enable", "PACSRejectedFinding function is Enable");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Green seen when pacsRejectedFinding slider is Enable", "when function is Enable,Green for PACSRejectedFinding seen");
		viewerPage.click(sd.pacsRejectedFindingSlider);
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is disable,Gray for PACSRejectedFinding seen");
			
	}
	
	// TC3529_Verification of settings need to be saved per user
	@Test(groups ={"Chrome","Edge","IE11","US831","positive"})
	public void test05_US831_TC3529_VerificationOfSettingsSavedPerUser() throws  InterruptedException, SQLException{
		
	    extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of settings needs to be saved per user");

	    patientPage = new PatientListPage(driver);
	
	    helper=new HelperClass(driver);
	 	viewerPage =helper.loadViewerDirectly(patientName, 1);
	 	
		sd=new ViewerSendToPACS(driver);
		ellipse = new EllipseAnnotation(driver);
		
		//Step 1: Verify slider component functionality
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		sd.openSendToPACSMenu();
	
		//Change setting in configuration UI for  PACSAcceptedFinding
		viewerPage.click(sd.pacsAcceptedFindingSlider);
		viewerPage.pressKeys(Keys.ESCAPE);
		
		//Open Send to PACS button after changing the settings
		sd.openSendToPACSMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Verfiy change settings are persisted on configuration UI ");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when PACSAcceptedFinding slider is Disable", "when function is Disable,Gray for PACSAcceptedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is Disable,Gray for PACSRejectedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsPendingFinding slider is Disable", "when function is Disable,Gray for PACSPendingFinding seen");
			
		 //Step 2: change setting verifications on UserFeedbackPreferences DB
		 DatabaseMethods db=new DatabaseMethods(driver);
    
		 ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verfiy change settings are persisted in UserFeedbackPreferences DB");
         viewerPage.assertFalse(db.getSendAcceptedFindings(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify SendAcceptedFindings value seen as FALSE on UI", "SendAcceptedFindings value seen as FALSE on DB ");
         viewerPage.assertFalse(db.getSendRejectedFindings(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify SendRejectedFindings value seen as FALSE on UI", "SendRejectedFindings value seen as FALSE on DB ");
         viewerPage.assertFalse(db.getSendPendingFindings(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify SendPendingFindings value seen as FALSE on UI", "SendPendingFindings value seen as FALSE on DB ");
         loginPage.logout();
       
		 //Step 3
		 
         ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verfiy change settings are persisted on UI for 'scan' user" );
		
            loginPage.navigateToBaseURL();
		    loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		    patientPage.waitForPatientPageToLoad();
		    patientPage.clickOnPatientRow(patientName);
	
			patientPage.clickOntheFirstStudy();
		
			viewerPage.waitForViewerpageToLoad();
			sd.openSendToPACSMenu();
		
			//Verify changed settings are persisted in UI.
			 viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when PACSAcceptedFinding slider is Disable", "when function is Disable,Gray for PACSAcceptedFinding seen");
			 viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is Disable,Gray for PACSRejectedFinding seen");
			 viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsPendingFinding slider is Disable", "when function is Disable,Gray for PACSPendingFinding seen");
			 loginPage.logout();
		 
			//Step 4:
			 
		   ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Verfiy change settings are persisted on UI for 'test' user" );
	
			loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
			loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		    registerUserPage = new RegisterUserPage(driver);
		    registerUserPage.createNewUser(username, username, LoginPageConstants.SUPPORT_EMAIL, username, username, username);
		
			loginPage.logout();
			loginPage.navigateToBaseURL();
			loginPage.login(username,username);
			patientPage.waitForPatientPageToLoad();
		    patientPage.clickOnPatientRow(patientName);
			patientPage.clickOntheFirstStudy();
			viewerPage.waitForViewerpageToLoad();
			ellipse = new EllipseAnnotation(driver);
			ellipse.selectEllipseFromQuickToolbar(1);
	    	ellipse.drawEllipse(1, 100, -50, 40, -50);
	    	sd.openSendToPACSMenu();
			
			//Verify changed settings are persisted in UI.
			 viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Green seen when PACSAcceptedFinding slider is Enable", "when function is Enable,Green for PACSAcceptedFinding seen");
			 viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is Disable,Gray for PACSRejectedFinding seen");
			 viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsPendingFinding slider is Disable", "when function is Disable,Gray for PACSPendingFinding seen");
			 loginPage.logout();
			
	}

	//TC3530 : Verification when both user's settings changed
	@Test(groups ={"Chrome","Edge","IE11","US831","negative"})
	public void test06_US831_TC3530_verifyBothUsersSettingsChanged() throws  InterruptedException, SQLException{
	
		//Step 1: Open PACS UI and change settings on UI for user ="scan"
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("The settings need to be saved per user and when presented again, the saved settings are applied to the UI.");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]","Verfiy user able to change settings in configuration UI for 'scan' user");
		
	    patientPage = new PatientListPage(driver);
	   
	    helper=new HelperClass(driver);
	 	viewerPage =helper.loadViewerDirectly(patientName, 1);
	 	
		sd=new ViewerSendToPACS(driver);
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
    	ellipse.drawEllipse(1, 100, -50, 40, -50);
    	sd.openSendToPACSMenu(true,false, false, true);

		//Step 2: Open PACS UI and change settings on UI for user="test"
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/5]","Verfiy user able to change settings in configuration UI for 'test' user");
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		registerUserPage = new RegisterUserPage(driver);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.createNewUser(username, username, LoginPageConstants.SUPPORT_EMAIL, username, username, username);
		loginPage.logout();
		 
		loginPage.navigateToBaseURL();
		loginPage.login(username,username);
		patientPage.waitForPatientPageToLoad();
	    patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		
		ellipse.selectEllipseFromQuickToolbar(1);
    	ellipse.drawEllipse(1, 100, -50, 40, -50);
    	sd.openSendToPACSMenu();
    	sd.enableSendToPACSFindingOptions(false, false, false);
		loginPage.logout();
		
		//Step 3: Verify changed setting on DB for both users
		
		 DatabaseMethods db=new DatabaseMethods(driver);
		 ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/5]","Verfiy change settings in DB for both users");
	
		//changed settings verification on DB for "scan" user
	    viewerPage.assertFalse(db.getSendAcceptedFindings(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify SendAcceptedFindings value seen as FALSE on UI", "SendAcceptedFindingsDefault value seen as FALSE on DB ");
        viewerPage.assertFalse(db.getSendRejectedFindings(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify SendRejectedFindings value seen as FALSE on UI", "SendRejectedFindingsDefault value seen as FALSE on DB ");
        viewerPage.assertFalse(db.getSendPendingFindings(Configurations.TEST_PROPERTIES.get("nsUserName")), "Verify SendPendingFindings value seen as TRUE on UI", "SendPendingFindings value seen as TRUE on DB ");
		
        //changed settings verification on DB for "test" user
        viewerPage.assertFalse(db.getSendAcceptedFindings(username), "Verify SendAcceptedFindings value seen as FALSE on UI", "SendAcceptedFindingsDefault value seen as FALSE on DB ");
        viewerPage.assertFalse(db.getSendRejectedFindings(username), "Verify SendRejectedFindings value seen as FALSE on UI", "SendRejectedFindingsDefault value seen as FALSE on DB ");
        viewerPage.assertFalse(db.getSendPendingFindings(username), "Verify SendPendingFindings value seen as FALSE on UI", "SendPendingFindingsDefault value seen as FALSE on DB ");
        
		//Step 4: verify settings persisted on UI for "scan" and "test" user
		
        ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/5]","Verfiy change settings in configuration UI  for 'scan' user");
		
        loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
        patientPage.clickOnPatientRow(patientName);
	    patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		sd.openSendToPACSMenu();
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Green seen when PACSAcceptedFinding slider is Disable", "when function is Disable,Gray for PACSAcceptedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is Disable,Gray for PACSRejectedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Green seen when pacsPendingFinding slider is Enable", "when function is Enable,Green for PACSPendingFinding seen");
		
	     ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/5]","Verfiy change settings in configuration UI  for 'username'");
	   
	    loginPage.navigateToBaseURL();
	    loginPage.login(username,username);
	    patientPage.waitForPatientPageToLoad();
	    patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();
		sd.openSendToPACSMenu();
	
		//step 4:verify settings persisted on UI for "test" user
    	viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsAcceptedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when PACSAcceptedFinding slider is Disable", "when function is Disable,Gray for PACSAcceptedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsRejectedFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsRejectedFinding slider is Disable", "when function is Disable,Gray for PACSRejectedFinding seen");
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_DISABLE_COLOR,"Verify Background color Gray seen when pacsPendingFinding slider is Disable", "when function is Disable,Gray for PACSPendingFinding seen");
	
	}
	
	//US956 Display dialog when user clicks Send To Pacs when there are pending results

	@Test(groups ={"Chrome","Edge","IE11","US956","positive"})
	public void test08_US956_TC4326_verifyDialogUIWhenPendingFindingDisable() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning Pop-up is not visible when 'Send Pending findings' is disabled ");

		patientPage = new PatientListPage(driver);
	    
	    helper=new HelperClass(driver);
	 	viewerPage =helper.loadViewerDirectly(patientName, 1);
	 	
		sd=new ViewerSendToPACS(driver);
		arToolbar=new ViewerARToolbox(driver);
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);
   
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectRejectfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);		
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		arToolbar.selectRejectfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		line.drawLine(1,10,10, 100, 100);
		viewerPage.closingConflictMsg();
		
		//Open output panel
		panel.enableFiltersInOutputPanel(false, false, true);
		
		//verify pending findings on output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verfiy pending findings in output panel for 'scan' user");
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Verify pending findings in output panel", "Verified");
		
		//enable pac rejected findings
		sd.openSendToPACSMenu(true,true, true, false);
		
		//Click on send to PACS and verify UI of pending finding dialog
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","Verify UI of pending Finding dialog not visible");
	    viewerPage.click(sd.sendToPacs);
	    viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up not visible when 'Send Pending findings' is disabled.", "Verified"); 
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US956","positive"})
	public void test09_US956_TC4328_verifyCheckboxInWarningPopUp() throws  InterruptedException, SQLException, TimeoutException, ParseException {
		DatabaseMethods db = new DatabaseMethods(driver);
	     db.deleteUser(username);
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of 'Do not show again' in warning pop-up");

	    helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerPageUsingSearch(patientName, 1, 1);
	    
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);
	  
		sd=new ViewerSendToPACS(driver);
		arToolbar=new ViewerARToolbox(driver);
	
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectRejectfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		arToolbar.selectRejectfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,10,10, 100, 100);
		viewerPage.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]","Verfiy pending findings in output panel for 'scan' user");
		//enable pac rejected/pending finding slider
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		
		//set sendPendingFindingDefault=1 in DB
		db.updateShowPendingResultsDialog(Configurations.TEST_PROPERTIES.get("nsUserName"), NSGenericConstants.BOOLEAN_TRUE);
		
		//Click on send to PACS and verify UI of pending finding dialog
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]","Verify UI of pending Finding dialog when user click on send to PACS");
	    viewerPage.pressKeys(Keys.ESCAPE);
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify warning pop-up  visible when 'Send Pending findings' is enable.", "Verified"); 
	    
	    //select do not show  checkbo
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]","Verify UI  after selecting an option from pop-up");
		viewerPage.click(sd.pacsPendingResultDialogCheckbox);
		viewerPage.click(sd.pacsPendingResultNoChanges);
		viewerPage.click(sd.continueButton);
	    viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up closed after selecting an option", "Verified"); 
		
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]","Verify new series on study list page for User generated result");
	    viewerPage.browserBackWebPage();
	    viewerPage.assertTrue(getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.USER_CREATED_RESULT),"Verify new series for user generated result on study list page","Verified");
	    
	    helper.loadViewerPageUsingSearch(patientName, 1, 1);
		
		//reject 2 more findings on same viewer
	    arToolbar.selectRejectfromGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));
	    arToolbar.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]","Verify UI of pending Finding dialog not visible when checkbox check for Dont show again");
	    viewerPage.click(sd.sendToPacs);
	    viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up not visible after click on send to PACS when checkbox check for Dont show again ", "Verified"); 
	    
	    //create new user and login
	    viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    registerUserPage = new RegisterUserPage(driver);		
	    registerUserPage.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username, username, username);
	    loginPage.logout();
	    //login with new user
	    loginPage.login(username, username);
	    
	   //load viewer page and click on send to PACS
	    patientPage=new PatientListPage(driver);
	    patientPage.clickOnPatientRow(patientName);
	    patientPage.clickOntheFirstStudy();
	    viewerPage.waitForViewerpageToLoad();
	    
	    //Draw 2 annotation and mark finding as pending
	    ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, -50,-50);		
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		//set sendPendingFindingDefault=1 in DB
		db.updateShowPendingResultsDialog(Configurations.TEST_PROPERTIES.get("nsUserName"), NSGenericConstants.BOOLEAN_TRUE);
		
		//open send to PACS menu and change the setting for Pending finding dialog
		sd.openSendToPACSMenu(true,true, true, true);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]","Verify UI of pending Finding dialog when checkbox check for another user");
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify warning pop-up  visible after click on send to PACS", "Verified");   
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US956","DE1759","US2222","Positive","BVT","E2E","F1094"})
	public void test10_US956_TC4331_DE1759_TC7331_US2222_TC9898_verifyWarningPopUpClosed() throws  InterruptedException, SQLException, TimeoutException, ParseException {
	
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning pop-up closed when user click outside the pop-up. <br>"+
		"Pending result warning pop up is not getting closed after clicking anywhere on browser (outside pop-up box). <br>"+
				"Verify that the send to pacs pop up is closed when user clicks outside the pop up box.");

		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerPageUsingSearch(patientName, 1, 1);
	    
		DatabaseMethods db = new DatabaseMethods(driver);
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		arToolbar=new ViewerARToolbox(driver);
	
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectRejectfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);		
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		arToolbar.selectRejectfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,10,10, 100, 100);
		viewerPage.closingConflictMsg();
		viewerPage.closeNotification();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]","Verify PACS pending finding slider is enable or not");
		//enable pac rejected/pending finding slider
		sd.openSendToPACSMenu();
		sd.click(sd.pacsRejectedFindingSlider);
		sd.click(sd.pacsPendingFindingSlider);
		viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingFindingSlider, NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.SLIDER_ENABLE_COLOR,"Verify Background color Green seen when pacsRejectedFinding slider is Enable", "when function is Enable,Green for PACSPendingFinding seen");
		
		//set sendPendingFindingDefault=1 in DB
		db.updateShowPendingResultsDialog(Configurations.TEST_PROPERTIES.get("nsUserName"), NSGenericConstants.BOOLEAN_TRUE);
		viewerPage.pressKeys(Keys.ESCAPE);
		
		//verify UI of pending finding dialog when user click outside the popup
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]","Verify UI of pending Finding dialog when user click on send to PACS");
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify warning pop-up visible when 'Send Pending findings' is enable.", "Verified"); 
	    
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]","Verify UI of pending Finding dialog closed when user click outside the pop-up ");
	    viewerPage.click(viewerPage.getViewPort(1));
	    viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up closed when user click outside the pop-up.", "Verified"); 
	    
	    //Verify on study list page no new series is created
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]","Verify In OrthaNC under the study,  new series is created as no finding details have been sent from NS");
	    viewerPage.browserBackWebPage();
	    viewerPage.assertTrue(getAndVerifyToolTipForMultipleMachines(ViewerPageConstants.USER_CREATED_RESULT), "Verify new series created when no findings have been sent from NS.", "Verified");
		
	    //againg click on send to PACS
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]","Verify UI of pending Finding dialog closed when user click outside the popup.");
	    helper.loadViewerPageUsingSearch(patientName, 1, 1);
	    
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.click(sd.pacsPendingResultDialogCheckbox);
	    viewerPage.click(viewerPage.getViewPort(1));
	    viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up closed when user click outside the pop-up.", "Verified"); 
	    
	    //again open send to PACS and check UI
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]","Verify UI of pending Finding dialog when user click on send to PACS");
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify warning pop-up closed when user click outside the pop-up.", "Verified"); 
	  
	}

	@Test(groups ={"Chrome","Edge","IE11","US956","positive"})
	public void test12_US956_TC4340_verifyNotificationUI() throws  InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Notification UI- whats sent to PACS functionality ");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
	    
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
   
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		panel.selectRejectfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);		
		panel.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		panel.selectAcceptfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		panel.selectRejectfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(2));
		line.drawLine(2,10,10, 50, 50);
		viewerPage.closingConflictMsg();
		viewerPage.closeNotification();
		
		panel.enableFiltersInOutputPanel(true, false, false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Verify findings count for each Accpeted/rejected/Pending state");
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Verify Accepted findings in output panel", "Verified");
		
		panel.enableFiltersInOutputPanel(true, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Verify Accepted and Rejected findings in output panel", "Verified");
		
		panel.enableFiltersInOutputPanel(true, true, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Verify Accepted,Rejected and Pending findings in output panel", "Verified");
		
		panel.openAndCloseOutputPanel(false);
		
		//Click on send to PACS and verify UI of pending finding dialog
		sd.openSendToPACSMenu(true,true, true, true);
	    
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify warning pop-up visible when 'Send Pending findings' is enable.", "Verified");
	    viewerPage.click(sd.pacsPendingResultNoChanges);
	    viewerPage.click(sd.continueButton);
	    
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify warning up when 'Send Pending findings leave as'  options selected.");
    	viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up not visible when 'Send Pending findings leave as'  options selected.", "Verified");
		
	    //viewerPage.click(viewerPage.sendToPacs);
		viewerPage.waitForTimePeriod("Low");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify notification UI visible or not when user click on send to PACS");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.notificationUI), "", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Verify content seen on notification UI");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("5"+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT), "Verify total number of findings seen on notification UI", "Verified");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("1 as"+" "+ViewerPageConstants.ACCEPTED_TEXT.toLowerCase()), "Verify count of accepted findings on notification UI", "Verified");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("2 as"+" "+ViewerPageConstants.REJECTED_TEXT.toLowerCase()), "Verify count of rejected findings on  notification UI", "Verified");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("2 as"+" "+ViewerPageConstants.PENDING_TEXT.toLowerCase()), "Verify count of pending findings on notification UI", "Verified");
		
       
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US956","positive"})
	public void test13_US956_TC4341_verifyPendingFindingDialogWhenPreferenceSetFromDB() throws  InterruptedException, SQLException, TimeoutException, ParseException, IOException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of pending Finding dialog when preference set from BD instead of UI");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
	    
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		arToolbar=new ViewerARToolbox(driver);
		
		DatabaseMethods db = new DatabaseMethods(driver);
		 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectRejectfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		arToolbar.selectRejectfromGSPSRadialMenu();
		viewerPage.closingConflictMsg();
		
		line.selectLineFromQuickToolbar(2);
		line.drawLine(2,10,10, 50, 50);
		viewerPage.closingConflictMsg();
		viewerPage.closeNotification();
		
		panel.enableFiltersInOutputPanel(true, false, false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]","Verify findings count for each Accpeted/rejected/Pending state");
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Verify Accepted findings in output panel", "Verified");
		
		panel.enableFiltersInOutputPanel(true, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Verify Accepted and Rejected findings in output panel", "Verified");
		
		panel.enableFiltersInOutputPanel(true, true, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Verify Accepted,Rejected and Pending findings in output panel", "Verified");
		
		panel.openAndCloseOutputPanel(false);
		
		//enable pac rejected/pending finding slider
		viewerPage.click(viewerPage.getViewPort(2));
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
        viewerPage.pressKeys(Keys.ESCAPE);
        
        ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]","Verify user can update pending finding default value as True from DB");
        db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
		db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
		db.resetIISPostDBChanges();
	
		//Click on send to PACS and verify UI of pending finding dialog
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]","Verify UI of pending Finding dialog when user click on send to PACS");
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify warning pop-up  visible when 'Send Pending findings' is enable.", "Verified"); 
	    
	    //select LEAVE AS IS option
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]","Verify UI  after selecting an option from pop-up");
		viewerPage.click(sd.pacsPendingResultDialogCheckbox);
		viewerPage.click(sd.pacsPendingResultNoChanges);
		viewerPage.click(sd.continueButton);
	    viewerPage.assertFalse(verifyUIForPendingFindingDialog(), "Verify warning pop-up closed after selecting an option", "Verified"); 
		
	    //viewerPage.click(viewerPage.sendToPacs);
	 	viewerPage.waitForTimePeriod("Low");
	 		
	 	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]","Verify notification UI visible or not when user click on send to PACS");
	 	viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.notificationUI), "Verify notification UI when user click on send to PACS", "Verified");
	 		
	 	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]","Verify content seen on notification UI");
	 	viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("5"+" "+ViewerPageConstants.NOTIFICATION_UI_TEXT), "Verify total number of findings seen on notification UI", "Verified");
	 	viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("1 as"+" "+ViewerPageConstants.ACCEPTED_TEXT.toLowerCase()), "Verify count of accepted findings on notification UI", "Verified");
	 	viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("2 as"+" "+ViewerPageConstants.REJECTED_TEXT.toLowerCase()), "Verify count of rejected findings on  notification UI", "Verified");
	 	viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationUI).contains("2 as"+" "+ViewerPageConstants.PENDING_TEXT.toLowerCase()), "Verify count of pending findings on notification UI", "Verified");    
	}
	
	//@Test(groups ={"Chrome","Edge","IE11","US1362","Positive"})
	public void test14_US1362_TC7058_verifyNewSyncToPACSIcon() throws InterruptedException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Text of 'SendToPacs' button changed to 'Sync Icon+ToPacs'");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
	    
	    sd=new ViewerSendToPACS(driver);
		
		viewerPage.assertEquals(viewerPage.getAttributeValue(sd.sendToPacsButtonTitle.get(0), NSGenericConstants.CLASS_ATTR), ViewerPageConstants.SYNC_ICN,"Checkpoint[1/2]", "Verifying 'Send' text is replaced with 'sync' icon");
		viewerPage.assertEquals(viewerPage.getText(sd.sendToPacsButtonTitle.get(1)), ViewerPageConstants.SEND_TO_PACS_TEXT,"Checkpoint[2/2]", "Verifying 'to pacs' text is followed by sync icon");

	}

	@Test(groups ={"Chrome","Edge","IE11","DE1267","Positive"})
    public void test15_DE1267_TC5380_verifyNotificationUIAfterPerformingSendToPACS() throws  InterruptedException {
        extentTest = ExtentManager.getTestInstance();
        extentTest.setDescription("Verify slider should display correct count of Pending , accepted, rejected annotation when user perform send to PACSafter changing configruation.");

        helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
	    
        ellipse = new EllipseAnnotation(driver);
        circle = new CircleAnnotation(driver);
        line = new SimpleLine(driver);
        sd=new ViewerSendToPACS(driver);
        panel=new OutputPanel(driver);
       
        ellipse.selectEllipseFromQuickToolbar(1);
        ellipse.drawEllipse(1, 100, -50, 40, -50);
        panel.selectRejectfromGSPSRadialMenu();
    
        circle.selectCircleFromQuickToolbar(1);
        circle.drawCircle(1, 20, 20, -100,-100);        
        panel.selectAcceptfromGSPSRadialMenu();
        panel.closingConflictMsg();
        
        line.selectLineFromQuickToolbar(panel.getViewPort(1));
        line.drawLine(1,10,10, 50, 50);
        
        ellipse.selectEllipseFromQuickToolbar(1);
        ellipse.drawEllipse(1, 30, -50, 20, -30);
        panel.selectAcceptfromGSPSRadialMenu();
        panel.closingConflictMsg();

        circle.selectCircleFromQuickToolbar(1);
        circle.drawCircle(1, 0, 0, -50,-50);
        
        int acceptedCount=panel.getStateSpecificFindings(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).size();
        int rejectedCount=panel.getStateSpecificFindings(1, ViewerPageConstants.REJECTED_FINDING_COLOR).size();
        int pendingCount=panel.getStateSpecificFindings(1, ViewerPageConstants.PENDING_FINDING_COLOR).size();
        
        sd = new ViewerSendToPACS(driver);
        //2 accepted,1 rejected and 2 pending finding
        sd.openSendToPACSMenu(true,true, true, true);
        ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Verify notification UI when all 3 types of finding send to PACS");
        sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
        panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "5 findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )"), "Verify message post sending the findings to PACS","Verified");    
		panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"","Verified error notification pop-up.");
		panel.waitForElementInVisibility(panel.notificationDiv);
		panel.waitForTimePeriod(2000);
		
		sd.openSendToPACSMenu();
        sd.enableSendToPACSFindingOptions(false, true, true);
        ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Verify notification UI when all rejected and pending finding send to PACS");
        sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
        panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "3 findings are sent to PACS ( "+rejectedCount+" as rejected, "+pendingCount+" as pending )"), "Verify message post sending the findings to PACS","Verified");    
        panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"","Verified error notification pop-up.");
     	panel.waitForElementInVisibility(panel.notificationDiv);
   
     	sd.openSendToPACSMenu();
        sd.enableSendToPACSFindingOptions(false, false, true);
        ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify notification UI when all pending finding send to PACS");
        sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
        panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "2 findings are sent to PACS ( "+pendingCount+" as pending )"), "Verify message post sending the findings to PACS","Verified");    
     	panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"","Verified error notification pop-up.");
     	panel.waitForElementInVisibility(panel.notificationDiv);
        
     	sd.openSendToPACSMenu();
        sd.enableSendToPACSFindingOptions(true, true, true);
        ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verify notification UI when all 3 types of finding send to PACS again");
        sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);
        panel.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "5 findings are sent to PACS ( "+acceptedCount+" as accepted, "+rejectedCount+" as rejected, "+pendingCount+" as pending )"), "Verify message post sending the findings to PACS","Verified");    
     	panel.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS),"","Verified error notification pop-up.");
     	panel.waitForElementInVisibility(panel.notificationDiv);
    
    }

	@Test(groups ={"Chrome","Edge","US2222","US2227","Positive","US2284","F1094","F1126","E2E","F1125"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
    public void test16_US2222_TC9895_TC9896_TC9987_TC9986_US2227_TC9725_TC9741_US2284_TC9779_verifyNotificationUIAfterPerformingSendToPACS(String theme) throws  InterruptedException {
        extentTest = ExtentManager.getTestInstance();
        extentTest.setDescription("Verify the new pop up UI for send to pacs pending finding popup. <br>"+
        "Verify that no changes option is displayed under send to pacs pending pop up instead of Leave as it is.<br>"+
        "Verify that the new send to pacs pop up is supported on dark theme as well.<br>"+
        "Verify the continue and cancel button functionalities on new confirmation pop up. <br>"+
        "Verify that notification 'Error in sending results to Envoy AI' is Eureka AI' as per the new design of Eureka theme. <br>"+
        "Verify that notification 'Error in sending results to Eureka AI' is appearing as per the new design for dark theme."
        + "<br> Verify Send to PACS list on the Output Panel.");

        if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,user);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			
			
			Header header = new Header(driver);
			header.logout();
			
			loginPage.navigateToBaseURL();
			loginPage.waitForLoginPageToLoad();
			loginPage.login(user,user);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
        
        
        patientPage = new PatientListPage(driver);
      
        helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(gspsPatientName, 1);
        sd=new ViewerSendToPACS(driver);
        PagesTheme  pageTheme=new PagesTheme(driver);
       
        sd.openAndCloseOutputPanel(false);
        sd.openSendToPACSMenu(true,true, false, true);
        sd.clickUsingAction(sd.sendToPacs);
        sd.waitForPendingFindingDialoglToLoad();
        sd.assertTrue(verifyUIForPendingFindingDialog(), "Checkpoint[1/13]", "Verified pending finding dialog after click on send to pacs when pending findings are present.");
        sd.assertTrue(sd.getText(sd.pacsPendingResultDialogHeader).contains(ViewerPageConstants.DIALOG_HEADER), "Checkpoint[2/13]", "Verified dilog header text.");
        sd.assertTrue(sd.getText(sd.pacsPendingResultAcceptAll).contains(ViewerPageConstants.ACCEPT_ALL), "Checkpoint[3/13]", "Verified text of Accept all radio button.");
        sd.assertTrue(sd.getText(sd.pacsPendingResultRejectAll).contains(ViewerPageConstants.REJECT_ALL), "Checkpoint[4/13]", "Verified text of Reject all radio button.");
        sd.assertTrue(sd.getText(sd.pacsPendingResultNoChanges).contains(ViewerPageConstants.NO_CHANGES), "Checkpoint[5/13]", "Verified text of No Changes radio button.");
        sd.assertTrue(sd.getText(sd.pacsPendingResultDialogCheckbox).contains(ViewerPageConstants.DO_NOT_SHOW_CHECKBOX), "Checkpoint[6/13]", "Verified text of Do not show checkbox.");
        sd.assertTrue(sd.getText(sd.cancelButton).contains(ViewerPageConstants.CANCEL_BUTTON), "Checkpoint[7/13]", "Verified text of Cancel button.");
        sd.assertTrue(sd.getText(sd.continueButton).contains(ViewerPageConstants.CONTINUE_BUTTON), "Checkpoint[8/13]", "Verified text of Continue button.");
        sd.assertEquals(sd.getCssValue(sd.warningIconOnPopUP, NSGenericConstants.FILL),ThemeConstants.WARNING_ICON_COLOR, "Checkpoint[9/13]", "Verified color of Warning icon in theme.");
      
        sd.assertTrue(pageTheme.verifyThemeForDialogPopUP(sd.pacsPendingFindingDialog, loadedTheme), "Checkpoint[10/13]", "Verified theme for Dialog popup.");
        sd.assertTrue(pageTheme.verifyButtonIsFilled(sd.pacsAcceptAllRadioButton,loadedTheme), "Checkpoint[11/13]", "Verified theme for radio button selection.");

        sd.click(sd.cancelButton);
        sd.assertFalse(verifyUIForPendingFindingDialog(), "Checkpoint[12/13]", "Verified that Pending finding dialog closed after click on Cancel button.");
        
        sd.openSendToPACSMenu(true,true, false, true);
        sd.clickUsingAction(sd.sendToPacs);
        sd.waitForPendingFindingDialoglToLoad();
        sd.click(sd.continueButton);
        sd.assertTrue(sd.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, "18 findings are sent to PACS ( 18 as accepted )"), "Checkpoint[13/13]", "Verified that findings sent to PACS after click on continu button.");
	    sd.waitForElementVisibility(sd.errorBanner);
        sd.assertTrue(pageTheme.verifyThemeForNotification(sd.notificationTiles.get(0), ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, theme), "Checkpoint[14/]", "Verified theme for Notification UI");
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","F1125","US2290","DR2881","Positive","E2E"})
	public void test17_US2290_TC10039_DR2881_TC11108_verifySendToPacsIconWhenFindingsSentForRT() throws InterruptedException{
			
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Sent to PACS' icon is highlighted/looks bright on thumbnail tool bar in Output Panel when the findings are sent to PACS. - GSPS <br>"+
		"Verify the icons are displayed on Output Panel thumbnail tool bar on Non-UI login.");

		helper = new HelperClass(driver);
		
		DICOMRT drt= helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
			
		panel.enableFiltersInOutputPanel(false, false, true);
	    for(int i=0;i<count;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[1/8]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1));
	    
	    sd.openSendToPACSMenu(true,true, true, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
	    for(int i=0;i<count;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[2/8]","Verifying the sent to Pacs tooltip and Icon is enable for Finding"+(i+1));
	    panel.openAndCloseOutputPanel(false);
	   
	    //accept any contour and verify send to PACS icon
	    drt.navigateToFirstContourOfSegmentation(1);
	    drt.selectAcceptfromGSPSRadialMenu();
	    sd.waitForElementInVisibility(sd.notificationDiv);
	    panel.enableFiltersInOutputPanel(true, false, true);
	    for(int i=0;i<count;i++)
			panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[3/8]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1));
	    
	    //send to pacs and verify Icon is enable
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
	    for(int i=0;i<count;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[4/8]","Verifying the sent to Pacs tooltip and Icon is enable for Finding"+(i+1));
	    
	    //reject any contour and verify send to PACS icon
	    drt.navigateToFirstContourOfSegmentation(2);
	    drt.selectRejectfromGSPSRadialMenu();
	    sd.waitForElementInVisibility(sd.notificationDiv);
	    panel.enableFiltersInOutputPanel(true, true, true);
	    for(int i=0;i<count;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[5/8]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1)+" when one of the finding is rejected.");
	    
	    //browser back and verify send to pacs icon are disable
	   helper.browserBackAndReloadRTData(patientNameDICOMRT,1, 1);
	   panel.enableFiltersInOutputPanel(true, true, true);
	    for(int i=0;i<count;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[6/8]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1)+" after browser reload.");
	    
	    //TC11108
	    helper.loadRTDirectly(patientNameDICOMRT,user,password,1);
		panel.enableFiltersInOutputPanel(false, false, true);
		 for(int i=0;i<count;i++)
		 {
			 panel.mouseHover(panel.findingTileContainers.get(i));
			 panel.assertTrue(panel.isElementPresent(panel.sendToPacsIcons.get(i)), "Checkpoint[7."+(i+1)+"/8]","Verified that send to pacs Icon is visible on finding-"+(i+1));
			 panel.assertTrue(panel.verifyPendingStateInThumbnail(i+1), "Checkpoint[8."+(i+1)+"/8]", "Verified that Pending state icon is visible on finding-"+(i+1));
		 }
		 
		 
		}
	
	@Test(groups ={"Chrome","Edge","IE11","F1125","US2290","DR2881","Positive","E2E"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test18_SendtoPacsIcon"})
	public void test18_US2290_TC10126_DR2881_TC11108_verifySendToPacsIconForNonDicomImage(String filepath) throws InterruptedException{
			
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Sent to PACS' icon for non-dicom images.<br>"+
		"Verify the icons are displayed on Output Panel thumbnail tool bar on Non-UI login.");

		helper = new HelperClass(driver);
		String patientFilePath=Configurations.TEST_PROPERTIES.get(filepath);
		String patient= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, patientFilePath);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading "+patient+" on Viewer.");
	    viewerPage = helper.loadViewerDirectly(patient, 1);
		  
	    panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		
		if(viewerPage.isElementPresent(viewerPage.notificationDiv))
			viewerPage.closeNotification();
		
		panel.enableFiltersInOutputPanel(false, false, true);
	    for(int i=0;i<(count/2);i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[1/4]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1));
	    
	    sd.openSendToPACSMenu(true,true, true, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
	    for(int i=0;i<(count/2);i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[2/4]","Verifying the sent to Pacs tooltip and Icon is enable for Finding"+(i+1));
	    
	 
	    ViewerSliderAndFindingMenu findingMenu=new ViewerSliderAndFindingMenu(driver);
	    findingMenu.click(findingMenu.getViewPort(1));
	    findingMenu.mouseHover(findingMenu.getGSPSHoverContainer(1));
	    findingMenu.selectAcceptfromGSPSRadialMenu(1);
	    panel.enableFiltersInOutputPanel(true, false, true);
	    
	    // test step failing as Not sent to pacs tooltip is not visible when finding is accepted DR2893 raised
		panel.assertTrue(sd.verifyWhenFindingsSentToPACS(0,false),"Checkpoint[3/4]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding1");
	   
	    for(int i=1;i<(count/2);i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[4/4]","Verifying the sent to Pacs tooltip and Icon is enable for Finding"+(i+1));

		}
		
	@Test(groups ={"Chrome","Edge","IE11","F1125","US2290","Positive"})
	public void test19_US2290_TC10128_verifySendToPacsIconWhenNewSeriesIsLoaded() throws InterruptedException, AWTException{
			
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Sent to PACS icon on thumbnail tool bar when a new series is loaded on viewer.");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading "+patientName+" on Viewer.");
		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
		  
	    panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		
		//draw multiple annotation (Accepted,Rejected and Pending)
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 50, 50);
		
		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,-10,10,250,10);
		line.selectAcceptfromGSPSRadialMenu();
		
		helper.browserBackAndReloadViewer(patientName, 1, 1);
		cs=new ContentSelector(driver);
		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);	
		ellipse.selectAcceptfromGSPSRadialMenu();
		cs.openAndCloseSeriesTab(false);
		
		pointAnn = new PointAnnotation(driver);
		pointAnn.selectPointFromQuickToolbar(1);
		pointAnn.drawPointAnnotationMarkerOnViewbox(1,70,70);
		
		helper.browserBackAndReloadViewer(patientName, 1, 1);
		panel.enableFiltersInOutputPanel(true, false, false);
	    for(int i=0;i<panel.thumbnailList.size();i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[1/4]","Verifying the Not Sent to Pacs tooltip and Icon is enable for Finding"+(i+1));
	    
	    sd.openSendToPACSMenu(true,true, true, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
	    for(int i=0;i<2;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[2/4]","Verifying the sent to Pacs tooltip and Icon is enable for Finding"+(i+1));
	    
	    cs.closeNotification();
	    cs.waitForElementInVisibility(viewerPage.notificationDiv);
	    sd.openAndCloseOutputPanel(false);
	    cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+user+"_1");
	    cs.openAndCloseSeriesTab(false);
	    panel.enableFiltersInOutputPanel(true, false, true);
	    for(int i=0;i<2;i++)
			panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[3/4]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1));
	    
	    cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+user+"_2");
	    panel.enableFiltersInOutputPanel(true, false, true);
	    
	    //test step failing as sent to pacs tooltip is not visible for already sent finding
	    for(int i=0;i<2;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[4/4]","Verifying the sent to Pacs tooltip and Icon is enable for Finding"+(i+1));
	   
		}
	
	@Test(groups ={"Chrome","Edge","IE11","F1125","US2290","Positive"})
	public void test20_US2290_TC10132_verifySendToPacsIconForSentFindings() throws InterruptedException, AWTException{
			
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Sent to PACS icon is updated only for the findings that are sent to PACS.");

		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
		  
	    panel=new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 50, 50);
		
		line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(line.getViewPort(1));
		line.drawLine(1,-10,10,250,10);
		line.selectRejectfromGSPSRadialMenu();
		
		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);	
		ellipse.selectAcceptfromGSPSRadialMenu();
		
		panel.enableFiltersInOutputPanel(true, true, true);
	    for(int i=0;i<panel.thumbnailList.size();i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,false),"Checkpoint[1/3]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding"+(i+1));
	    
	    panel.openAndCloseOutputPanel(false);
	    panel.enableFiltersInOutputPanel(true, false, true);
	    sd.openSendToPACSMenu(true,true, false, true);
	    sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);

	    for(int i=0;i<count/2;i++)
	    	panel.assertTrue(sd.verifyWhenFindingsSentToPACS(i,true),"Checkpoint[2/3]","Verifying the Sent to Pacs tooltip and Icon is disable for Finding"+(i+1));
	   
	    panel.openAndCloseOutputPanel(false);
	    panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(sd.verifyWhenFindingsSentToPACS(0,false),"Checkpoint[3/3]","Verifying the Not Sent to Pacs tooltip and Icon is disable for Finding1.");
	   
		}
	
	public boolean verifyUIForPendingFindingDialog() {
		
	    boolean status = false;

	    boolean condition1_header=viewerPage.isElementPresent(sd.pacsPendingResultDialogHeader);
	    boolean condition2_options=viewerPage.isElementPresent(sd.pacsPendingResultAcceptAll) && viewerPage.isElementPresent(sd.pacsPendingResultRejectAll) && viewerPage.isElementPresent(sd.pacsPendingResultNoChanges);
	    boolean condition2_checkbox=viewerPage.isElementPresent(sd.pacsPendingResultDialogCheckbox);
	    boolean condition3_cancel=viewerPage.isElementPresent(sd.cancelButton);
	    boolean condition4_continue=viewerPage.isElementPresent(sd.continueButton);
	
		status=condition1_header && condition2_options && condition2_checkbox && condition3_cancel && condition4_continue;

    return status;
} 
	
	public boolean getAndVerifyToolTipForMultipleMachines(String MachineName) throws TimeoutException, InterruptedException, ParseException {
		boolean status=true;
		// Mouse hover over Result Icon
		try{

		PatientListPage patientstudypage = new PatientListPage(driver);
		patientstudypage.mouseHover("presence",patientstudypage.getEurekaAIIcon(1)); 

		String str = patientstudypage.getEurekaAITooltip();
		//Verifing tooltip information
		patientstudypage.assertTrue(str.contains(MachineName), "Verifying "+MachineName +" is present in tooltip ", MachineName+ " is present in tooltip");
		patientstudypage.assertTrue(str.contains("Last updated:"), "Verifying Last updated: is present in tooltip", "Last updated: is present in tooltip");
		patientstudypage.assertTrue(str.contains("Result Status:"), "Verifying Result Status: Run with findings is present in tooltip", "Result Status: Run with findings is present in tooltip");
		//Verifying date format
		patientstudypage.assertTrue(patientstudypage.verifyDateFormat("MM/dd/yyyy, hh:mm aaa", patientstudypage.getDateTimeFromToolTip()), "Verifying date format present in tooltip","Date format is in MM/dd/yyyy, hh:mm ");
		}
		catch(AssertionError  e)
		{
		status=false;
		}
		return status;
		
	}
		
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException{
		//DB Default configuration
		 DatabaseMethods db = new DatabaseMethods(driver);
		 db.updateSendAcceptedFindingsDefault(NSGenericConstants.BOOLEAN_TRUE);
	     db.updateSendRejectedFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
	     db.updateSendPendingFindingsDefault(NSGenericConstants.BOOLEAN_FALSE);
	     db.deleteUser(username);
	     db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);

		}

	
}

	
	
	
	
	

	
	

	

	

	


	

	

