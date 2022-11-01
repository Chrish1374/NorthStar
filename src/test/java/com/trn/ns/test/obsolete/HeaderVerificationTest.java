
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.sql.SQLException;
//
//import org.testng.annotations.Test;
//
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.PasswordPolicyPage;
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////package com.trn.ns.test.obsolete;
////
////import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
////
////import java.util.List;
////import java.util.Set;
////
////import org.testng.annotations.BeforeMethod;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////
////import com.relevantcodes.extentreports.ExtentTest;
////import com.trn.ns.page.factory.*;
////import com.trn.ns.test.base.TestBase;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.ExtentManager;
////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
////public class HeaderVerificationTest extends TestBase {
////
////	private LoginPage loginPage;
////	private PatientListPage patientPage;
////	private Header header;
////	private SinglePatientStudyPage patientstudypage;
////	private ViewerPage viewerpage;
////
////	private ExtentTest extentTest ;
////
////	@BeforeMethod(alwaysRun=true)
////	public void beforeMethod(){
////
////		loginPage = new LoginPage(driver);
////		loginPage.navigateToBaseURL();
////	}
////
////	@Test(groups ={"Chrome","Edge","IE11","multimonitor"})
////	public void test10_US152_TC1489_verifyBuildVersionOnMultMonitor() throws InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Application versioning Infrastructure and display - Multi monitor");
////		loginPage = new LoginPage(driver);	
////		String buildVersion = loginPage.buildVersion.getText();
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
////		patientPage = new PatientListPage(driver);
////		header = new Header(driver);
////		patientPage.clickOnPatientRow(patientPage.getPatientName(0));
////		patientstudypage = new SinglePatientStudyPage(driver);
////		patientstudypage.clickOntheFirstStudy();
////		viewerpage = new ViewerPage(driver);
////		String parentWindow = driver.getWindowHandle();
////		viewerpage.openOrCloseChildWindows(4);
////		Set<String> childWinHandles = driver.getWindowHandles();
////		int i=1;
////		for (String childWindow : childWinHandles) 
////			if(!childWindow.equals(parentWindow)){
////				viewerpage.switchToWindow(childWindow);	
////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+"/"+childWinHandles.size()+ "]", "Validate build version displayed in the header section of child window-"+i+" is exactly same as in Login page");
////				header.assertTrue(buildVersion.contains(header.buildVersionOnHeader.getText()), "Verify build version mentioned on child window is same as mentioned on login page", "Build version mentioned on child window is same as mentioned on login pge");		
////				i++;
////			}
////		viewerpage.switchToWindow(parentWindow);
////		viewerpage.openOrCloseChildWindows(1);
////	}
////	
////	@Test(groups ={"Chrome","Edge","IE11"})
////	public void test08_US152_TC1487_verifyBuildVersionOnLoginToViewerPage() throws InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Application versioning Infrastructure and display - Login to Viewerpage");
////		loginPage = new LoginPage(driver);	
////		String parentWindow = loginPage.getCurrentWindowID();
////		loginPage.click(loginPage.about);
////		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
////		childWinHandles.remove(parentWindow);
////		loginPage.switchToWindow(childWinHandles.get(0));
////		
////		String buildVersion = loginPage.buildVersion.getText();
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
////		patientPage = new PatientListPage(driver);
////		header = new Header(driver);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate build version displayed on the header section of patient list page is exactly same as on Login page");
////		header.assertTrue(buildVersion.contains(header.buildVersionOnHeader.getText()), "Verify build version mentioned on patient page is same as mentioned on login page", "Build version mentioned on patient page is same as mentioned on login pge");
////		patientPage.clickOnPatientRow(patientPage.getPatientName(0));
////		patientstudypage = new SinglePatientStudyPage(driver);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate build version displayed on the header section of single study list page is exactly same as on Login page");
////		header.assertTrue(buildVersion.contains(header.buildVersionOnHeader.getText()), "Verify build version mentioned on single patient study page is same as mentioned on login page", "Build version mentioned on single patient study page is same as mentioned on login pge");
////		patientstudypage.clickOntheFirstStudy();
////		viewerpage = new ViewerPage(driver);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Validate build version displayed on the header section of viewer page is exactly same as on Login page");
////		header.assertTrue(buildVersion.contains(header.buildVersionOnHeader.getText()), "Verify build version mentioned on viewer page is same as mentioned on login page", "Build version mentioned on viewer page is same as mentioned on login pge");		
////	}
////
////	@Test(groups ={"Chrome","Edge","IE11"})
////	public void test09_US152_TC1488_verifyBuildVersionOnStudyList() throws InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Application versioning Infrastructure and display - StudyList & PasswordPolicy page");
////		loginPage = new LoginPage(driver);	
////		String buildVersion = loginPage.buildVersion.getText();
////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
////		patientPage = new PatientListPage(driver);		
////		patientPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL); 
//////		StudyListPage studyPage = new StudyListPage(driver) ;
////		header = new Header(driver);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Validate build version displayed on the header section of StudyList page is exactly same as in Login page");
////		header.assertTrue(buildVersion.contains(header.buildVersionOnHeader.getText()), "Verify build version mentioned on study list page is same as mentioned on login page", "Build version mentioned on study list page is same as mentioned on login pge");
////	}
////}
////
//
//
//	// TC871 : 	Verify that header has logo, Northstar version information, logged in user's first name + last name
//	// TC1877 : Remove the search field from the top bar of viewer - All pages
//	@Test(groups ={ "Chrome", "IE11", "Edge","dbConfig"})
//	public void test01_US187_TC871_US492_TC1877_verifyHeaderwithUsersFirstAndLastName() throws InterruptedException, SQLException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that header has logo, Northstar version information, logged in user's first name <\br> last name and Remove the search field from the top bar of viewer - All pages");
//		header = new Header(driver);
//		databaseMethod = new DatabaseMethods(driver);
//		databaseMethod.updateUserDetails(LoginPageConstants.FIRST_NAME,LoginPageConstants.LAST_NAME,username);	    
//		loginPage.login(username, password);
//
//		//Navigated to Patient list screen
//		patientPage = new PatientListPage(driver);
//		patientPage.assertTrue(patientPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Validate Northstar logo, Northstar hardcoded version and search box ui on Patient list screen");
//
//		header = new Header(driver);
//		header.assertTrue(header.isNorthstarLogoPresent(), "Verify Northstar logo image on screen header", "Northstar icon is present");
//		//Verify  logged in user info - First name and last name
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Validate Logged in user info on Patient list screen");
//		header.assertEquals(header.userInfo.getText().trim(),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.userInfo.getText().trim(), "User Information is displaying properly");
//		patientPage.clickOnPatientRow(patientPage.getPatientName(0));
//
//		//Navigate to single study page
//		patientstudypage = new SinglePatientStudyPage(driver);
//		patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Validate Northstar logo, Northstar hardcoded version and search box ui on Single Patient list screen");
//		header.assertTrue(header.isNorthstarLogoPresent(), "Verify Northstar logo image on screen header", "Northstar icon is present");
//
//		//Verify  logged in user info - First name and last name
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Validate Logged in user info on Single Patient list screen");
//		header.assertEquals(header.userInfo.getText().trim(),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.userInfo.getText().trim(), "User Information is displaying properly");
//		patientstudypage.clickOntheFirstStudy();
//
//		//Navigate to viewer screen
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(4);
//		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Validate Northstar logo, Northstar hardcoded version and search box ui on viewer screen");
//		header.assertTrue(header.isNorthstarLogoPresent(), "Verify Northstar logo image on screen header", "Northstar icon is present");
//
//		//Verify  logged in user info - First name and last name
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Validate Logged in user info on viewer screen");
//		header.assertEquals(header.userInfo.getText().trim(),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.userInfo.getText().trim(), "User Information is displaying properly");
//
//		//Navigate to Studylist page
//
//		viewerpage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/#/"+URLConstants.STUDY_LIST_URL); 
//		studyPage = new StudyListPage(driver);
//
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Validate Northstar logo, Northstar hardcoded version and search box ui on Study list screen");
//		header.assertTrue(header.isNorthstarLogoPresent(), "Verify Northstar logo image on screen header", "Northstar icon is present");
//
//		//Verify  logged in user info - First name and last name
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Validate Logged in user info on study list screen");
//		header.assertEquals(header.userInfo.getText().trim(),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.userInfo.getText().trim(), "User Information is displaying properly");
//
//		//Navigate to password policy screen
//		studyPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/#/"+URLConstants.PASSWORD_POLICY_URL);
//		passwordPolicyPage = new PasswordPolicyPage(driver);
//		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains("passwordPolicy"), "Verifying that user is navigated to Password Policy page", "User is on page "+ passwordPolicyPage.getCurrentPageURL());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Validate Northstar logo, Northstar hardcoded version and search box ui on Password Policy screen");
//		header.assertTrue(header.isNorthstarLogoPresent(), "Verify Northstar logo image on screen header", "Northstar icon is present");
//
//		//Verify  logged in user info - First name and last name
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Validate Logged in user info on Password Policy screen");
//		header.assertEquals(header.userInfo.getText().trim(),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.userInfo.getText().trim(), "User Information is displaying properly");
//
//		//Removing added user details
//		databaseMethod.updateUserDetails("","",username);	   
//	}	
//
//	//TC878 : Verify that on click on terarecon icon , list is displayed vertical
//		// TC877 : Verify Terarecon icon is not functional on any other page other than viewer page
//		@Test(groups ={ "Chrome", "IE11", "Edge"})
//		public void test06_US187_TC877_TC878_verifyNorthstarLogoFunctionality() throws InterruptedException, SQLException{
//
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify Terarecon icon is not functional on any other page other than viewer page <BR> Verify that on click on terarecon icon , list is displayed vertical");
//
//			header = new Header(driver);
//			loginPage.login(username,password);
//
//			//Navigated to Patient list screen
//			patientPage = new PatientListPage(driver);
//			patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
//
//			//Verify that Terarecon Icon is not functional
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate functionality of Northstar icon on Patient list screen");
//			header.northstarLogoIcon.click();
//			header.assertFalse(header.isElementPresent(header.optionMenu), "Verifying Northstar icon functionality", "Northstar icon is not functional");
//			patientPage.clickOnPatientRow(patientPage.getPatientName(0));
//
//			//Navigate to single study page
//			patientstudypage = new SinglePatientStudyPage(driver);
//			patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());
//
//			//Verify that Terarecon Icon is not functional
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate functionality of Northstar icon on Single Patient list screen");
//			header.northstarLogoIcon.click();
//			header.assertFalse(header.isElementPresent(header.optionMenu), "Verifying Northstar icon functionality", "Northstar icon is not functional");
//			patientstudypage.clickOntheFirstStudy();
//
//			//Navigate to viewer screen
//			viewerpage = new ViewerPage(driver);
//			viewerpage.waitForViewerpageToLoad(1);
//			viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());
//
//			//Verify that Terarecon Icon is  functional
//			//Verify that on click on terarecon icon , list is displayed vertical
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate functionality of Northstar icon on viewer screen. Also validating on click of logo, list is displayed in vertical");
//			header.northstarLogoIcon.click();
//			header.assertTrue(header.isElementPresent(header.optionMenu), "Verifying Northstar icon functionality", "Northstar icon is functional");
//
//			//Navigate to Studylist page
//
//			viewerpage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/#/"+URLConstants.STUDY_LIST_URL); 
//			studyPage = new StudyListPage(driver) ;
//			studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());
//
//			//Verify that Terarecon Icon is not functional
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate functionality of Northstar icon on study list screen");
//			header.northstarLogoIcon.click();
//			header.assertFalse(header.isElementPresent(header.optionMenu), "Verifying Northstar icon functionality", "Northstar icon is not functional");
//
//			//Navigate to password policy screen
//			studyPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/#/"+URLConstants.PASSWORD_POLICY_URL);
//			passwordPolicyPage = new PasswordPolicyPage(driver);
//			passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Verifying that user is navigated to Password Policy page", "User is on page "+ passwordPolicyPage.getCurrentPageURL());
//
//			//Verify that Terarecon Icon is not functional
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate functionality of Northstar icon on Password Policy screen");
//			header.northstarLogoIcon.click();
//			header.assertFalse(header.isElementPresent(header.optionMenu), "Verifying Northstar icon functionality", "Northstar icon is not functional");
//
//		}
//
//		//TC879 : verify that option grid is available on click on logo icon on viewer page	
//		@Test(groups ={ "Chrome", "IE11", "Edge"})
//		public void test07_US187_TC879_verifyOptionGridonViewer() throws InterruptedException, SQLException{
//
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("verify that option grid is available on click on logo icon on viewer page");
//
//			header = new Header(driver);
//			loginPage.login(username, password);
//
//			//Navigated to Patient list screen
//			patientPage = new PatientListPage(driver);
//			patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
//			patientPage.clickOnPatientRow(patientPage.getPatientName(0));
//
//			//Navigate to single study page
//			patientstudypage = new SinglePatientStudyPage(driver);
//			patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());
//			patientstudypage.clickOntheFirstStudy();
//
//			//Navigate to viewer screen
//			viewerpage = new ViewerPage(driver);
//			viewerpage.waitForViewerpageToLoad(1);
//			viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());
//
//			//Verify that on change of layout it is applicable to viewer page
//			header.northstarLogoIcon.click();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Validate option grid is displaying in vertical with text overlay on viewer screen");
//
//			//Verify that onclick of logo option grid is available 
//			header.assertTrue(header.isElementPresent(header.optionGrid), "Verify option grid is displaying on click on logo icon on viewer page", "Option grid is displaying on click on logo");
//
//		}
//
/*
@Test(groups ={"Chrome","Edge","IE11"})
	public void test07_US612_TC2080_verifyBETAVersionOnLoginToViewerPage() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify BETA is written on the header");	
		// Define build version in header 
		loginPage = new LoginPage(driver);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify the word 'BETA' is contained in parenthesis");
		loginPage.assertTrue(loginPage.buildVersion.getText().contains("(BETA)"), "Verify '(BETA)' is mentioned on the login page" , "Verified '(BETA)' is mentioned on the login page");

		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		header = new Header(driver);
		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify the word 'BETA' is contained in parenthesis");
		patientPage.assertTrue(header.getText(header.buildVersionOnHeader).contains(LoginPageConstants.BUILDVERSION), "Verify '(BETA)' is mentioned on the patient list page" , "Verified '(BETA)' is mentioned on the patient list page");

		//patientPage.clickOnPatientRow(patientPage.getPatientName(0));
		//patientstudypage = new SinglePatientStudyPage(driver);
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the word 'BETA' is contained in parenthesis");
		//patientstudypage.assertTrue(header.getText(header.buildVersionOnHeader).contains(LoginPageConstants.BUILDVERSION), "Verify '(BETA)' is mentioned on the single patient study page" , "Verified '(BETA)' is mentioned on the single patient study page");

		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();
		viewerpage = new ViewerPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify the word 'BETA' is contained in parenthesis");
		viewerpage.assertTrue(header.getText(header.buildVersionOnHeader).contains(LoginPageConstants.BUILDVERSION), "Verify '(BETA)' is mentioned on the viewer page" , "Verified '(BETA)' is mentioned on the viewer page");

		//Navigate to Studylist page
		//viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL); 
		//studyPage = new StudyListPage(driver) ;
		//studyPage.assertTrue(header.getText(header.buildVersionOnHeader).contains(LoginPageConstants.BUILDVERSION), "Verify '(BETA)' is mentioned on the viewer page" , "Verified '(BETA)' is mentioned on the viewer page");
	}
*/