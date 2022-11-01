package com.trn.ns.test.obsolete;

//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.LinkedHashMap;
//
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.OrthancAndAPIConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.UsersPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class ClientConfigurationTest extends TestBase{
//
//	String protocolName;
//	String config = "teraReconTest";
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ViewerPage viewerPage;
//	private UsersPage usersPage;
//	private ExtentTest extentTest;
//	private Header header;
//	private String  patientIDAidoc, patientAidocUrl, studyUIDAidoc, viewerAidocUrl, aidocFilepath,
//	myURL;
//	LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
//
//	public ClientConfigurationTest(){
//		aidocFilepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath"); 
//		patientIDAidoc = DataReader.getPatientDetails(
//				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, aidocFilepath);
//		studyUIDAidoc = DataReader.getPatientDetails(
//				PatientXMLConstants.STUDY_UID_TEXTOVERLAY, aidocFilepath);
////		patientAidocUrl = URLConstants.SINGLE_PATIENT_LIST_URL + "/" + patientIDAidoc;
//		viewerAidocUrl = URLConstants.VIEWER_PAGE_URL + "/" + studyUIDAidoc;
//		hm.put(LoginPageConstants.PASSWORD, TEST_PROPERTIES.get("nsPassword"));
//		hm.put(LoginPageConstants.USERNAME, TEST_PROPERTIES.get("nsUserName"));
//	}
//
//	@Test(priority = 1,groups ={"Chrome","US834","DE1001","DE1276","Positive"})
//	public void test01_US834_TC3746_TC3747_DE1001_TC4087_DE1276_TC5156_VerifyClientConfig() throws AWTException, InterruptedException, IOException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Display accept-reject toolbar, radial menu, Send to PACS and Logout button in NorthStar UI based on configuration key");
//		DatabaseMethods db= new DatabaseMethods(driver);
//		//Insert 'teraReconTest' key in the ClientConfiguration table
//		db.updateClientConfiguration(config);
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		header = new Header(driver);
//
//		myURL = loginPage.getNonUILaunchURL(viewerAidocUrl + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		loginPage.navigateToURL(myURL);		
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3746][1/4]","Verify Accept/Reject tool bar is visible on bottom of screen");
//		viewerPage.mouseHover(viewerPage.getGSPSHoverContainer(1));
//		viewerPage.assertTrue(viewerPage.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is  visible on bottom of screen");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3746][2/4]","Verify radial menu is launched");
//		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
//		viewerPage.assertTrue(viewerPage.isAllRadialBarIconDisplayed(), "Verify radial menu is launched", "Verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3747][1/4]","Verify Send to PACS button is seen clickable");
//		viewerPage.assertTrue(viewerPage.checkForElementClickability(viewerPage.bySendToPacs),"Verify Send to PACS button is seen clickable ", "verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3747][2/4]","Verify Logout button is clickable");
//		header.click(header.userInfo);
//		viewerPage.assertTrue(viewerPage.isElementPresent(header.logOut),"Verify Logout button is clickable", "Verified");
//
//		//Update 'teraReconTest' key in the ClientConfiguration table
//		db.updateClientConfiguration(config);
//		db.resetIISPostDBChanges();
//
//		loginPage.openNewWindow(myURL);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3746][3/4]","Verify Accept/Reject tool bar is visible on bottom of screen");
//		viewerPage.mouseHover(viewerPage.getGSPSHoverContainer(1));
//		viewerPage.assertFalse(viewerPage.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is NOT visible on bottom of screen", "The Accept/Reject tool bar is not visible on bottom of screen");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3746][4/4]","Verify radial menu is launched");
//		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
//		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.radialMenu), "Verify user can NOT only launch radial menu", "Verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3747][3/4]","Verify radial menu is launched");
//		viewerPage.assertFalse(viewerPage.checkForElementClickability(viewerPage.bySendToPacs),"Verify Send to PACS button is seen clickable ", "verified");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3747][4/4]","Verify Logout button is clickable");
//		header.click(header.userInfo);
//		viewerPage.assertFalse(viewerPage.isElementPresent(header.logOut),"Verify Logout button is clickable", "Verified");
//		
//	}
//	// Make sure northstar.svg and terarecon.svg are present at 'C:\NorthStar\WebServer\Custom\teraReconTest' and
//	// 'teraReconTest' config key is set in ClientConfiguration table. 
//	//SVGs(northstar.svg and terarecon.svg are placed at src\test\resources\com\bat'
//	@Test(priority = 2,groups ={"Chrome","US834","Positive"})
//	public void test02_US834_TC3709_US900ReplaceIconAndProductName() throws InterruptedException, SQLException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Display Terarecon logo and icon are as per svgs shared at 'C:\\NorthStar\\WebServer\\Custom\\teraReconTest' when config key is set in ClientConfiguration table");
//
//		loginPage = new LoginPage(driver);
//		header = new Header(driver);
//
//		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		loginPage.navigateToURL(myURL);		
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Verify Terarecon logo and Northstar logo are visible on patient page");
//		patientPage.assertTrue(patientPage.containsIgnoreCase(header.northstarLogoWithConfigKey.getText(),LoginPageConstants.NORTHSTAR_LOGO_CONFIG_KEY), "Verify Northstar logo is present on patient page", "Verified");
//		patientPage.assertTrue(header.terareconLogoWithConfigKey.isDisplayed(),"Verify Terarecon logo is present on patient page", "Verified");
//
//		myURL = patientPage.getNonUILaunchURL(patientAidocUrl+ OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		patientPage.navigateToURL(myURL);
//		
//		studyPage.waitForSingleStudyToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify Terarecon logo and Northstar logo are visible on study list page");
//		studyPage.assertTrue(studyPage.containsIgnoreCase(header.northstarLogoWithConfigKey.getText(),LoginPageConstants.NORTHSTAR_LOGO_CONFIG_KEY), "Verify Northstar logo is present on study page", "Verified");
//		studyPage.assertTrue(header.terareconLogoWithConfigKey.isDisplayed(),"Verify Terarecon logo is present on study list page", "Verified");
//
//		myURL = studyPage.getNonUILaunchURL(viewerAidocUrl + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		studyPage.navigateToURL(myURL);
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify Terarecon logo and Northstar logo are visible on viewer page");
//		viewerPage.assertTrue(viewerPage.containsIgnoreCase(header.northstarLogoWithConfigKey.getText(),LoginPageConstants.NORTHSTAR_LOGO_CONFIG_KEY), "Verify Northstar logo is present on viewer page", "Verified");
//		viewerPage.assertTrue(header.terareconLogoWithConfigKey.isDisplayed(),"Verify Terarecon logo is present on viewer page", "Verified");
//
//		myURL = viewerPage.getNonUILaunchURL(URLConstants.USER_PAGE_URL + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		viewerPage.navigateToURL(myURL);
//		usersPage = new UsersPage(driver);
//		usersPage.waitForUsersPageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Verify Terarecon logo and Northstar logo are visible on users page");
//		usersPage.assertTrue(usersPage.containsIgnoreCase(header.northstarLogoWithConfigKey.getText(),LoginPageConstants.NORTHSTAR_LOGO_CONFIG_KEY), "Verify Northstar logo is present on user page", "Verified");
//		usersPage.assertTrue(header.terareconLogoWithConfigKey.isDisplayed(),"Verify Terarecon logo is present on user page", "Verified");
//		db.deleteClientConfiguration(config);
//	}
//
//	@Test(priority = 3,groups ={"Chrome","US834","Positive"})
//	public void test03_US834_TC3745_TC3794_VerifyThemeSetInClientConfig() throws InterruptedException, SQLException, IOException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify theme of patient/study/users page is as per set in client configuration");
//
//		//Setting theme to 'DARK' in config key and verifying if it is getting reflected in all pages - patient/study/user
//		db.updateClientConfiguration(config);
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		loginPage.navigateToURL(myURL);
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3745][1/6]","Verify dark theme is applied on patient page");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.DARK_THEME), "Verify Theme is seen to be dark in patient page", "verified");
//		patientPage.clickOnPatientRow(patientPage.getPatientName(0));
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3745][2/6]","Verify dark theme is applied on study page");
//		studyPage.assertTrue(studyPage.getAttributeValue(studyPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.DARK_THEME), "Verify Theme is seen to be dark in study page", "verified");
//		myURL = studyPage.getNonUILaunchURL(URLConstants.USER_PAGE_URL + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		studyPage.navigateToURL(myURL);
//		usersPage = new UsersPage(driver);
//		usersPage.waitForUsersPageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3745][3/6]","Verify dark theme is applied on user page");
//		usersPage.assertTrue(usersPage.getAttributeValue(usersPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.DARK_THEME), "Verify Theme is seen to be dark in user page", "verified");
//		
//		//Changing theme to 'LIGHT' in config key and verifying if it is getting reflected in all pages - patient/study/user
//		db.updateClientConfiguration(config);
//		db.resetIISPostDBChanges();
//
//		myURL = patientPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		patientPage.openNewWindow(myURL);
//		patientPage.waitForPatientPageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3745][4/6]","Verify light theme is applied on patient page");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.LIGHT_THEME), "Verify Theme is seen to be light in patient page", "verified");
//		patientPage.clickOnPatientRow(patientPage.getPatientName(0));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3745][5/6]","Verify light theme is applied on study page");
//		studyPage.assertTrue(studyPage.getAttributeValue(studyPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.LIGHT_THEME), "Verify Theme is seen to be light in study page", "verified");
//		myURL = studyPage.getNonUILaunchURL(URLConstants.USER_PAGE_URL + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		studyPage.navigateToURL(myURL);
//		usersPage.waitForUsersPageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3745][6/6]","Verify light theme is applied on user page");
//		usersPage.assertTrue(usersPage.getAttributeValue(usersPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.LIGHT_THEME), "Verify Theme is seen to be light in user page", "verified");
//		myURL = usersPage.getNonUILaunchURL(patientAidocUrl + OrthancAndAPIConstants.INTEGRATION_TYPE_URL + config + "&", hm);
//		usersPage.navigateToURL(myURL + OrthancAndAPIConstants.THEME_URL + PatientPageConstants.DARK_THEME);
//		patientPage.waitForPatientPageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3794][1/1]","Verify light theme is applied on patient page though dark theme is maentioned in the URL");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.body, PatientPageConstants.CLASS.replace(":", "")).contains(PatientPageConstants.LIGHT_THEME), "Verify Theme is seen to be light in patient page", "verified");
//
//		//Deleting client config key
//		db.deleteClientConfiguration(config);
//	}
//
//}
