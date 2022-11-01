//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.NSDBDatabaseConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.ErrorOrLogoutPage;
////import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class PatientListNonUILoginTest extends TestBase {
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//	
//	private ExtentTest extentTest;
//	private ViewerPage viewerPage;
//	//	private Header headerPage;
//	private ErrorOrLogoutPage errorPage;
//	private String actualUrl, myURL;
//	private String ah4Filepath, aidocFilepath, patientName, patientID, patientIDAidoc;
//	private LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
//	private LinkedHashMap<String, String> hmBlank = new LinkedHashMap<String, String>();
//	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
//
//	String filePath_AH4=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_AH4);
//
//	public PatientListNonUILoginTest() {
//		ah4Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//		aidocFilepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath"); 
//		patientName = DataReader.getPatientDetails(
//				PatientXMLConstants.PATIENT_NAME, ah4Filepath);
//		patientID = DataReader.getPatientDetails(
//				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, ah4Filepath);
//		patientIDAidoc = DataReader.getPatientDetails(
//				PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, aidocFilepath);
//		hm.put(LoginPageConstants.PASSWORD, password);
//		hm.put(LoginPageConstants.USERNAME, username);
//
//	}
//
//	@Test(groups = { "Chrome", "Edge", "IE11", "US726" })
//	public void test01_US726_TC2976_NavigatingURLWithPatientIDInURL()
//			throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify patient page URL with optional parameter as patient ID");
//
//		loginPage = new LoginPage(driver);
//		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		// Access patient page URL by appending patient ID
//		loginPage.navigateToChangedURL(myURL);
//		
//		// studyPage.waitForElementVisibility(patientPage.PatientIDHeader);
//
//		// Verify patient page
//
//		actualUrl = studyPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verify patient page URL with optional parameter as patient ID");
//		studyPage.assertTrue(actualUrl.contains(URLConstants.SINGLE_PATIENT_LIST_URL),"Verify patient page", "Patient page verified");
//		studyPage.assertTrue(studyPage.getText(studyPage.PatientName).contains(patientName),"Verify patient name present on patient page","Patient name present on patient page verified");
//
//	}
//
//	@Test(groups = { "Chrome", "Edge", "IE11", "US726","DR1992","Negative"})
//	public void test02_US726_TC2977_DR1992_TC8119_VerifyInvalidPatientIDInURLNavigatingToErrorPage()
//			throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify error page renders when invalid patient ID is mentioned. <br>"+
//		"[Risk and Impact]: Verify the multiple valid and invalid scenarios with batch, study and accession number.");
//
//		loginPage = new LoginPage(driver);
//		// Access patient page URL by appending invalid patient ID
//		myURL = loginPage.getNonUILaunchURL(invalidPatientAH4Url, hm);
//		loginPage.navigateToChangedURL(myURL);
//		errorPage = new ErrorOrLogoutPage(driver);
//
//		// Verify error page
//		actualUrl = errorPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verify Error Page renders when invalid patient ID is passed");
//		errorPage.assertEquals(errorPage.getText(errorPage.message),LoginPageConstants.INVALID_URL_PATIENTID_ERROR_MSG,"Verify Error Page renders when invalid patient ID is mentioned", "Error Page verified");
//
//	}
//
//	@Test(groups = { "Chrome", "Edge", "IE11", "US726" })
//	public void test03_US726_TC2978_VerifyNavigationViewerPagePatientIDInURL()
//			throws InterruptedException, AWTException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify navigation till viewer page when patient ID is mentioned in the URL");
//
//		loginPage = new LoginPage(driver);
//		String actualUrl;
//		// Access patient page URL by appending patient ID
//		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToChangedURL(myURL);
//		
//		studyPage.waitForSingleStudyToLoad();
//		// Verify patient page
//		actualUrl = studyPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify patient page renders");
//		studyPage.assertTrue(actualUrl.contains(URLConstants.SINGLE_PATIENT_LIST_URL),"Verify patient page renders", "Patient page verified");
//		studyPage.assertTrue(studyPage.getText(studyPage.PatientName).contains(patientName),"Verify patient name present on patient page","Patient name present on patient page verified");
//		studyPage.clickOntheFirstStudy();
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		// Verifying Viewer page should be rendered.
//		actualUrl = viewerPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify viewer page renders");
//		viewerPage.assertTrue(actualUrl.contains("viewer"),"Verify viewer page renders", "Viewer page verified");
//		viewerPage.assertTrue(viewerPage.getText(viewerPage.patientIDViewer).contains(patientID),"Verifying study is loaded in viewport","Study is loaded in viewport");
//	}
//
//	@Test(groups = { "Chrome", "Edge", "IE11", "US726" })
//	public void test04_US726_TC2979_VerifyPatientPagePatientIDMatchesToMultiplePatients()
//			throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify patient page URL having patient ID which matches to multiple patients");
//
//		loginPage = new LoginPage(driver);
//		myURL = loginPage.getNonUILaunchURL(multiplePatientID, hm);
//		loginPage.navigateToChangedURL(myURL);
//		PatientListPage patientpage = new PatientListPage(driver);
//
//		// Verify patient page
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verify number of patient present in the patient list table");
//		patientpage.assertEquals(patientpage.getText(patientpage.patientNameList.get(0)),"AH.4_US675","Verify patient name present on patient page","Patient name present on patient page verified");
//		patientpage.assertTrue(patientpage.patientIDList.size()>1,	"Verify number of patient present in the patient list table",	"Number of patient present in the patient list table verified : "+ patientpage.patientIDList.size());
//	}
//
//	@Test(groups = { "Chrome", "Edge", "IE11", "US726" })
//	public void test05_US726_TC2989_VerifyRedirectToLoginPageAfterUserChangesUrl()
//			throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify that after successful non ui login if user changes the url, it will redirect to the login page");
//
//		loginPage = new LoginPage(driver);
//		// Access patient page URL by appending patient ID
//		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToChangedURL(myURL);
//		
//		//		studyPage.waitForElementVisibility(studyPage.title);
//		int size = studyPage.studyDescriptionList.size();
//		// Verify patient page
//		actualUrl = studyPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Verify number of patient present in the patient list table");
//		studyPage.assertTrue(actualUrl.contains(patientID),"Verify patient present in the patient list table","Patient present in the patient list table verified");
//		studyPage.assertTrue(actualUrl.contains(patientID),	"Verify number of patient present in the patient list table", "Number of patient present in the patient list table verified : "+ size);
//		// Change the url as below without entering the authentication key and
//		// refresh the page
//		myURL = loginPage.getNonUILaunchURL(patientAidocUrl, hmBlank);
//		studyPage.navigateToChangedURL(myURL);
//		loginPage.refreshWebPage();
//		loginPage.waitForLoginPageToLoad();
//		actualUrl = studyPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify login page renders after changing the url");
//		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify login page renders","Login page verified");
//		loginPage.refreshWebPage();
//		loginPage.login(TEST_PROPERTIES.get("nsUserName"),TEST_PROPERTIES.get("nsPassword"));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify login successful renders");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		actualUrl = patientPage.getCurrentPageURL();
//		patientPage.assertTrue(actualUrl.contains(URLConstants.PATIENT_LIST_URL),"Verify login successful renders", "Login verified");
//	}
//	
//	@Test(groups = { "Chrome", "Edge", "IE11", "US726" })
//	public void test06_US726_TC3006_VerifyRedirectToLoginPageAfetrUserChangesToInvlidUrl()
//			throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Non UI Login : Verify that after successful non ui login if user changes the url to invlid url and remove credentials, it will redirect to the login page");
//
//		loginPage = new LoginPage(driver);
//		// Access patient page URL by appending patient ID
//		myURL = loginPage.getNonUILaunchURL(patientAH4Url, hm);
//		loginPage.navigateToChangedURL(myURL);
//		
//		int size = studyPage.studyDescriptionList.size();
//		// Verify patient page
//		actualUrl = studyPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Verify number of patient present in the patient list table");
//		studyPage.assertTrue(actualUrl.contains(patientID),"Verify patient present in the patient list table","Patient present in the patient list table verified");
//		studyPage.assertTrue(actualUrl.contains(patientID),	"Verify number of patient present in the patient list table", "Number of patient present in the patient list table verified : "+ size);
//		// Change the url as below without entering the authentication key and
//		// refresh the page
//		myURL = loginPage.getNonUILaunchURL(invalidPatientAH4Url, hmBlank);
//		studyPage.navigateToChangedURL(myURL);
//		loginPage.refreshWebPage();
//		loginPage.waitForLoginPageToLoad();
//		actualUrl = studyPage.getCurrentPageURL();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify User should direct to the login page on changing url with invalid patient");
//		loginPage.assertTrue(actualUrl.contains(URLConstants.LOGIN_PAGE_URL), "Verify login page renders","Login page verified");
//		loginPage.refreshWebPage();
//		loginPage.login(TEST_PROPERTIES.get("nsUserName"),TEST_PROPERTIES.get("nsPassword"));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]", "Verify login successful renders");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		actualUrl = patientPage.getCurrentPageURL();
//		patientPage.assertTrue(actualUrl.contains(URLConstants.PATIENT_LIST_URL),"Verify login successful renders", "Login verified");
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1404","Positive"})
//	public void test07_US1404_TC7590_verifyAcquisitionDateNoNUI() throws InterruptedException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the acquisition date and machine filter dropdown, when user login through Non UI url where user refresh and resize the browser");
//
//		loginPage = new LoginPage(driver);
//
//		hm.put(LoginPageConstants.PASSWORD, password);
//		hm.put(LoginPageConstants.USERNAME, username);
//
//		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
//		loginPage.navigateToChangedURL(myURL);
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.acquisitiondateDropdown), "Checkpoint[1/5]", "Verifying the acquisition date drop down presence");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.acquisitiondateDropdown, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[2/5]", "Verifying upon loading no value is selected");
//		
//		int options = patientPage.acquistionDateDropDownOptions.size();
//		patientPage.assertEquals(options,PatientPageConstants.ACQUISITIONDATE.size(),"Checkpoint[3/5]","verifying the drop down options and ordering");
//		
//		for(int i =0;i<options;i++)		
//			patientPage.assertEquals(patientPage.getText(patientPage.acquistionDateDropDownOptions.get(i)), PatientPageConstants.ACQUISITIONDATE.get(i), "Checkpoint[4/5]", "verifying the drop down options and ordering");
//						
//		for(int i =0;i<options;i++) {	
//			patientPage.click(patientPage.acquisitiondateDropdown);
//			patientPage.click(patientPage.acquistionDateDropDownOptions.get(i));
//			patientPage.assertEquals(patientPage.getAttributeValue(patientPage.acquisitiondateDropdown, NSGenericConstants.VALUE), PatientPageConstants.ACQUISITIONDATE.get(i), "Checkpoint[5/5]", "Verifying the selection of values");
//			
//					
//		}
//	}
//
//
//	@Test(groups ={"Chrome","IE11","Edge","US1404","Positive"})
//	public void test08_US1404_TC7575_TC7590_verifyMachineDropdownOrderSelection() throws InterruptedException, SQLException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the acquisition date and machine filter dropdown, when user login through Non UI url where user refresh and resize the browser");
//
//		loginPage = new LoginPage(driver);
//
//		hm.put(LoginPageConstants.PASSWORD, password);
//		hm.put(LoginPageConstants.USERNAME, username);
//
//		String myURL = loginPage.getNonUILaunchURL(URLConstants.PATIENT_LIST_URL, hm);
//		loginPage.navigateToChangedURL(myURL);
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		List<String> machinesFromDB = db.getAllMachinesName();
//		
//		machinesFromDB.remove(NSDBDatabaseConstants.USER_DEFINED_MACHINE);
//		
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineFilterDropdown), "Checkpoint[1/8]", "Verifying the machine dropdown is present");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.machineDropdownButton, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[2/8]", "Verifying there is no value selected upon loading");
//		
//		patientPage.click(patientPage.machineDropdownButton);
//		List<String> machinesFromUI = patientPage.convertWebElementToTrimmedStringList(patientPage.machineDropdownOptions);
//		patientPage.click(patientPage.machineDropdownButton);
//		
//		Collections.sort(machinesFromDB);
//		patientPage.assertEquals(machinesFromUI, machinesFromDB, "Checkpoint[3/8]", "Verifying the machines from DB and UI are same");
//		
//		String selectedMachines ="";
//		int machinesToBeSelected =3;
//		for(int i =0 ;i<machinesToBeSelected;i++) {
//			patientPage.selectMachines(machinesFromUI.get(i));
//			
//			if(i<machinesToBeSelected-1)
//				selectedMachines = selectedMachines+machinesFromUI.get(i)+"; ";
//			else
//				selectedMachines = selectedMachines+machinesFromUI.get(i);
//		}
//		
//	
//		patientPage.assertEquals(patientPage.getAttributeValue(patientPage.machineDropdownButton, NSGenericConstants.TITLE),selectedMachines,"Checkpoint[4/8]","verifying the tooltip when multiple machines are selected");
//		patientPage.assertEquals(patientPage.getText(patientPage.machineDropdownButton),selectedMachines,"Checkpoint[5/8]","Verifying the values when multiple machines are selected");
//		
//		for(int i =0 ;i<machinesToBeSelected;i++) 
//			patientPage.selectMachines(machinesFromUI.get(i));
//
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineFilterDropdown), "Checkpoint[6/8]", "Verifying the dropdown presence when multiple options are deselected");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.machineDropdownButton, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[7/8]", "verifying no value is displayed when all values are deselected");
//
//		
//		patientPage.clickOnPatientRow(patientNameAh4);
//		SinglePatientStudyPage 
//		studyPage.clickOnStudy(1);
//		
//		PointAnnotation point = new PointAnnotation(driver);
//		point.waitForViewerpageToLoad();
//		
//		point.selectPointAnnotationIconFromRadialMenu(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
//		
//		point.navigateToPatientPage();
//		
//		patientPage.click(patientPage.machineDropdownButton);
//		machinesFromUI = patientPage.convertWebElementToTrimmedStringList(patientPage.machineDropdownOptions);
//		patientPage.click(patientPage.machineDropdownButton);
//		
//		patientPage.assertFalse(machinesFromUI.contains(NSDBDatabaseConstants.USER_DEFINED_MACHINE), "Checkpoint[8/8]", "Verifying that user created machines are not displayed upon creating annotation");
//
//	}
//
//}
