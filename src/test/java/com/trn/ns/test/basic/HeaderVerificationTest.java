package com.trn.ns.test.basic;

import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.*;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class HeaderVerificationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private PasswordPolicyPage passwordPolicyPage;
	private Header header;
	private ViewerPage viewerpage;
	private DatabaseMethods databaseMethod;
	private ExtentTest extentTest ;
	private String attribute = "type";
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password= Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
	}

	// TC871 : 	Verify that header has logo, Northstar version information, logged in user's first name + last name
	// TC1877 : Remove the search field from the top bar of viewer - All pages
	@Test(groups ={ "Chrome", "IE11", "Edge","dbConfig"})
	public void test01_US187_TC871_US492_TC1877_verifyHeaderwithUsersFirstAndLastName() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that header has logo, Northstar version information, logged in user's first name "
				+ "<\br> last name and Remove the search field from the top bar of viewer - All pages");
		header = new Header(driver);
		databaseMethod = new DatabaseMethods(driver);
		databaseMethod.updateUserDetails(LoginPageConstants.FIRST_NAME,LoginPageConstants.LAST_NAME,username);	    
		loginPage.login(username, password);

		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Validate Northstar logo, Northstar hardcoded version and search box ui on Patient list screen");

		header = new Header(driver);
		//Verify  logged in user info - First name and last name
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Validate Logged in user info on Patient list screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		//patientPage.clickOnPatientRow(patientPage.getPatientName(0));
		//patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));

		//Navigate to single study page
	//	patientstudypage = new SinglePatientStudyPage(driver);
	//	patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL),"Checkpoint[3/10]", "Verifying that user is navigated to Single Patient Study list page");
	//	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Validate Northstar logo, Northstar hardcoded version and search box ui on Single Patient list screen");

		//Verify  logged in user info - First name and last name
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Validate Logged in user info on Single Patient list screen");
	//	header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();

		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Checkpoint[5/10]", "Verifying that user is navigated to Single Patient Study list page. User is on page "+ viewerpage.getCurrentPageURL());
		//Verify  logged in user info - First name and last name
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Validate Logged in user info on viewer screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to Studylist page

		//viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL); 
		//studyPage = new StudyListPage(driver);
		//studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Checkpoint[7/10]","Verifying that user is navigated to study list page , User is on page "+ studyPage.getCurrentPageURL());

		//Verify  logged in user info - First name and last name
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Validate Logged in user info on study list screen");
		//header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to password policy screen
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
		passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains("passwordPolicy"), "Checkpoint[9/10]", "Verifying that user is navigated to Password Policy page, User is on page "+ passwordPolicyPage.getCurrentPageURL());
		//Verify  logged in user info - First name and last name
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Validate Logged in user info on Password Policy screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME+" "+LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Removing added user details
		databaseMethod.updateUserDetails("","",username);	   
	}	

	// TC872 : Verify that user name is displayed as logged in user if there is no first and last name present in database
	@Test(groups ={ "Chrome", "IE11", "Edge","dbConfig"})
	public void test02_US187_TC872_verifyHeaderwithoutUsersFirstAndLastName() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user name is displayed as logged in user if there is no first and last name present in database");

		header = new Header(driver);
		databaseMethod = new DatabaseMethods(driver);
		databaseMethod.updateUserDetails("","",username);	    

		loginPage.login(username, password);

		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		//patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());

		//On Patient list screen verify  logged in user info - username 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate Logged in user info on Patient list screen");
		header.assertEquals(header.getText(header.userInfo),username, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		//patientPage.clickOnPatientRow(patientPage.getPatientName(0));
		//patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));

		//Navigate to single study page
		//patientstudypage = new SinglePatientStudyPage(driver);
		//patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());

		//Verify logged in user info - username 
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate Logged in user info on Single Patient list screen");
		//header.assertEquals(header.getText(header.userInfo),username, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();

		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());

		//Verify logged in user info - username 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate Logged in user info on viewer screen");
		header.assertEquals(header.getText(header.userInfo),username, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to Studylist page
		//viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL); 
		//studyPage = new StudyListPage(driver);
	//	studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());

		//Verify logged in user info - username 
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate Logged in user info on study list screen");
		//header.assertEquals(header.getText(header.userInfo),username, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to password policy screen
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
		passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Verifying that user is navigated to Password Policy page", "User is on page "+ passwordPolicyPage.getCurrentPageURL());

		//Verify logged in user info - username 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate Logged in user info on Password Policy screen");
		header.assertEquals(header.getText(header.userInfo),username, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
	}

	// TC873 : verify that first name is displayed as logged in user if last name is not present in DB
	@Test(groups ={ "Chrome", "IE11", "Edge","dbConfig"})
	public void test03_US187_TC873_verifyHeaderwithUsersFirstName() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify that first name is displayed as logged in user if last name is not present in DB");

		header = new Header(driver);
		databaseMethod = new DatabaseMethods(driver);
		databaseMethod.updateUserDetails(LoginPageConstants.FIRST_NAME,"",Configurations.TEST_PROPERTIES.get("nsUserName"));	    

		loginPage.login(username, password);

		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		//patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());

		//Verify logged in user info - FirstName 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate Logged in user info on Patient list screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		//patientPage.clickOnPatientRow(patientPage.getPatientName(0));
		//patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));


		//Navigate to single study page
		//patientstudypage = new SinglePatientStudyPage(driver);
		//patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());

		//Verify logged in user info - FirstName 
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate Logged in user info on Single Patient list screen");
		//header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();


		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());

		//Verify logged in user info - FirstName 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate Logged in user info on viewer screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to Studylist page

		//viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL); 
		//studyPage = new StudyListPage(driver) ;
		//studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());

		//Verify logged in user info - FirstName 
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate Logged in user info on study list screen");
		//header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to password policy screen
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
		passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Verifying that user is navigated to Password Policy page", "User is on page "+ passwordPolicyPage.getCurrentPageURL());

		//Verify logged in user info - FirstName 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate Logged in user info on Password Policy screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.FIRST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Removing added user details
		databaseMethod.updateUserDetails("","",username);	
	}

	// TC873 : verify that first name is displayed as logged in user if last name is not present in DB
	@Test(groups ={ "Chrome", "IE11", "Edge","dbConfig"})
	public void test04_US187_TC873_verifyHeaderwithUsersLastName() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify that first name is displayed as logged in user if last name is not present in DB");

		header = new Header(driver);
		databaseMethod = new DatabaseMethods(driver);
		databaseMethod.updateUserDetails("",LoginPageConstants.LAST_NAME,Configurations.TEST_PROPERTIES.get("nsUserName"));	    

		loginPage.login(username, password);

		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		//patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());

		//Verify  logged in user info - LastName 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate Logged in user info on Patient list screen");
		header.assertEquals(header.getText(header.userInfo), LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		//patientPage.clickOnPatientRow(patientPage.getPatientName(0));
		//patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));


		//Navigate to single study page
		//patientstudypage = new SinglePatientStudyPage(driver);
		//patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());

		//Verify logged in user info - LastName 
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate Logged in user info on Single Patient list screen");
		//header.assertEquals(header.getText(header.userInfo),LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();

		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());

		//Verify logged in user info - LastName 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate Logged in user info on viewer screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to Studylist page

		//viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL); 
		//studyPage = new StudyListPage(driver) ;

		//studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());

		//Verify logged in user info - LastName 
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate Logged in user info on study list screen");
		//header.assertEquals(header.getText(header.userInfo),LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Navigate to password policy screen
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
		passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Verifying that user is navigated to Password Policy page", "User is on page "+ passwordPolicyPage.getCurrentPageURL());

		//Verify logged in user info - LastName 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate Logged in user info on Password Policy screen");
		header.assertEquals(header.getText(header.userInfo),LoginPageConstants.LAST_NAME, "Verifying user Info on header as -"+header.getText(header.userInfo), "User Information is displaying properly");

		//Removing added user details
		databaseMethod.updateUserDetails("","",username);	
	}					

	//TC876 : Verify that patient name , patient ID is displayed in header on viewer page and same as mentioned in the DICOM header tags
	@Test(groups ={ "Chrome", "IE11", "Edge"})
	public void test05_US187_TC876_verifyHeaderwithUsersFirstName() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that patient name , patient ID is displayed in header on viewer page and same as mentioned in the DICOM header tags");

		header = new Header(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());

		patientPage.waitForTimePeriod(2000);
		String patientname = patientPage.getText(patientPage.patientNamesList.get(0));
		String patientid=patientPage.getText(patientPage.patientIdsList.get(0));
		//patientPage.clickOnPatientRow(patientname);

		//Navigate to single study page
		//patientstudypage = new SinglePatientStudyPage(driver);
		//patientstudypage.waitForSingleStudyToLoad();
		//patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());

		//Verify the User name and ID on header
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Validate "+patientname +"And "+patientname+" on Single Patient list screen");
		//header.assertEquals(header.getText(header.userNameID), patientname+" ID: "+patientid, "Verifying user Info on header as -"+header.getText(header.userNameID), "User Information is displaying properly");
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();

		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());

		//Verify the User name and ID on header
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Validate "+patientname +"And "+patientname+" on viewer screen");
		header.assertEquals(header.getText(header.userNameID), patientname+" ID: "+patientid, "Verifying user Info on header as -"+header.getText(header.userNameID), "User Information is displaying properly");
	}

	//TC892 : Verify that logged in user info is a link and on click of link logout happens
	@Test(groups ={ "Chrome", "IE11", "Edge","dbConfig"})
	public void test06_US187_TC892_verifyLinkLogout() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that logged in user info is a link and on click of link logout happens");

		header = new Header(driver);
		databaseMethod = new DatabaseMethods(driver);
		databaseMethod.updateUserDetails("","",username);	    

		loginPage.login(username, password);

		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Verifying that user is navigated to Patient list page", "User is on page "+ patientPage.getCurrentPageURL());

		//Click on user info link
		header.logout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate successfull logout from Patient list screen");
		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Verifying the Successful Logout", "User is on page "+ loginPage.getCurrentPageURL());
		loginPage.login(username,password);
		patientPage.waitForPatientPageToLoad();
		//patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));

		//Navigate to single study page
		//patientstudypage = new SinglePatientStudyPage(driver);
		//patientstudypage.assertTrue(patientstudypage.getCurrentPageURL().contains(URLConstants.SINGLE_PATIENT_LIST_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ patientstudypage.getCurrentPageURL());

		//Click on user info link
		header.logout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate successfull logout from Single Patient list screen");
		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Verifying the Successful Logout", "User is on page "+ loginPage.getCurrentPageURL());

		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForTimePeriod(2000);
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));
		patientPage.clickOntheFirstStudy();

		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL), "Verifying that user is navigated to Single Patient Study list page", "User is on page "+ viewerpage.getCurrentPageURL());

		//Click on user info link
		header.logout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate successfull logout from viewer screen");
		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Verifying the Successful Logout", "User is on page "+ loginPage.getCurrentPageURL());

		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();

		//Navigate to password policy screen
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
		passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Verifying that user is navigated to Password Policy page", "User is on page "+ passwordPolicyPage.getCurrentPageURL());

		//Click on user info link
		header.logout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate successfull logout from Password policy screen");
		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Verifying the Successful Logout", "User is on page "+ loginPage.getCurrentPageURL());

		//loginPage.login(username, password);
		//patientPage.waitForPatientPageToLoad();

		//Navigate to Studylist page
		//patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL); 
		//studyPage = new StudyListPage(driver) ;
		//studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());

		//Click on user info link
		//header.logout();
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate successfull logout from Study list screen");
		//header.assertTrue(header.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Verifying the Successful Logout", "User is on page "+ loginPage.getCurrentPageURL());
	}			

	@Test(groups ={"Chrome","Sanity"})
	public void test07_US966_TC4149_verifyUserManual() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify UserManual is accessible from NS Application");	
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		String parentWindow = patientPage.getCurrentWindowID();
		header = new Header(driver);
		header.click(header.userInfo);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify after clicking on user name, 'About','Help','Logout' options should be displayed");
		header.assertEquals(LoginPageConstants.USERDROPDOWNMENU, header.getText(header.userDropDown), "'About','Help','Logout' options should be displayed under user dropdown", "'About','Help','Logout' options displayed successfully under user dropdown");
		header.click(header.help);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		patientPage.switchToWindow(childWinHandles.get(0));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify after clicking on Help menu, user manual page should open in another tab.");
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.HELP_PAGE_URL), "Verify user manual page should open in another tab", "User manual opened in another tab");
		String userManualType = header.getAttributeValue(header.userManual, attribute);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify ./pdf file is displayed in new tab in the browser");
		patientPage.assertTrue(userManualType.contains(LoginPageConstants.USER_MANUAL_TYPE), "Verify ./pdf file is displayed in new tab in the browser", "pdf file opened in another tab");
	}
}

