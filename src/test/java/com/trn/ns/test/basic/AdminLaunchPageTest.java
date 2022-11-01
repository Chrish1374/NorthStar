package com.trn.ns.test.basic;

import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.ContactAdministratorPage;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.UsersPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AdminLaunchPageTest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private Header header ;
	private ExtentTest extentTest;
	private UsersPage userPage;

	private String firstName = "firstName";
	private String lastName = "lastName";
	private String email ="abc@xyz.com";

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String userOne="userA";
	String userTwo="test";
	
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","dbconfig"})
	public void test01_US626_TC2378_TC2379_TC2380_verifyRegisterPageOrErrorPage() throws  SQLException, InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Trying to create first user from non-localhost address<br> Navigation to User registration page when accessing from localhost"
				+ "<br> After initlal user creation");

		header = new Header(driver);
		//verifying that any user is present in DB if not then delete it 

		DatabaseMethods db = new DatabaseMethods(driver);
		if(db.getUsersCount()>0) {
			db.deleteAllUsers();
			db.assertTrue(db.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL),"verifying that user is landed on default login page","verified");
			
			header.assertFalse(header.isElementPresent(header.userInfo),"Verifying no user information is present","verified");

		}
		db.refreshWebPage();
		if(Configurations.TEST_PROPERTIES.get("execution").equals(NSGenericConstants.LOCAL)) {
			//verifying the URL is now register

			RegisterUserPage rsp = new RegisterUserPage(driver);
			rsp.navigateToBaseURL();
			rsp.assertTrue(rsp.getCurrentPageURL().contains(URLConstants.REGISTER_PAGE_URL),"verified user is landed up at register page as there is no user present","verified");

			// verifying the register page

			rsp.assertEquals(rsp.createNewUser(firstName, lastName, email ,Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"),Configurations.TEST_PROPERTIES.get("nsPassword"))
					,LoginPageConstants.SUCCESS_MESSAGE,"verifying the success message is displayed","verified");

			rsp.waitForURLToChange();
			rsp.assertTrue(rsp.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL),"Verifying that upon user creation user redirects on login page","verified");

			// User is trying to login using created user credentials
			loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

			patientPage = new PatientListPage(driver);
			header.waitForElementVisibility(header.userInfo);
			header.assertTrue(header.isElementPresent(header.userInfo),"verifying user information is displayed","verified");
			header.assertEquals(header.userInfo.getText(),firstName+" "+lastName,"Verifying that first and last name is displayed","verified");

			patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);

			rsp.assertTrue(rsp.isElementPresent(header.userInfo),"verifying user information is displayed","verified");
			rsp.assertEquals(header.getText(header.userInfo),firstName+" "+lastName,"Verifying that first and last name is displayed","verified");

		}else if(Configurations.TEST_PROPERTIES.get("execution").equals(NSGenericConstants.REMOTE)) {

			ContactAdministratorPage cap = new ContactAdministratorPage(driver);
			cap.refreshWebPage();
			cap.assertTrue(cap.getCurrentPageURL().contains(URLConstants.CONTACT_ADMINISTRATOR_PAGE_URL),"verifying contact admin page is displayed when accessed remotly","verified");
			cap.assertEquals(cap.getText(cap.errorMessage),LoginPageConstants.INFO_MSG_CONTACT_ADMIN_PAGE,"verifying the infomation displayed","verified");

			// creating a user using DB way as register URL is not gonna be accessible directly
			db.addUserInDB("", Configurations.TEST_PROPERTIES.get("nsUserName"), "uPqNaW7rurnkYzKBvLwKgYh99sn6O06W", "eLaUIr+A6NoNOXMEDZ+8ImpLnvY3kiKh", "",lastName,"",firstName,email, "", "description", "2018-09-07 20:48:53.053", "dark","1","","","","");

			loginPage.navigateToBaseURL();

			loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
			patientPage = new PatientListPage(driver);
			header.waitForElementVisibility(header.userInfo);			
			header.assertTrue(header.isElementPresent(header.userInfo),"verifying user information is displayed","verified");
			header.assertEquals(header.userInfo.getText(),firstName+" "+lastName,"Verifying that first and last name is displayed","verified");

			patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
			RegisterUserPage rsp = new RegisterUserPage(driver);
			rsp.assertTrue(rsp.isElementPresent(header.userInfo),"verifying user information is displayed","verified");
			rsp.assertEquals(header.getText(header.userInfo),firstName+" "+lastName,"Verifying that first and last name is displayed","verified");


		}


	}


	//TC2922 - Active Directory Authentication - Create admin user and verification on DB.
	@Test(groups ={"Chrome","Edge","IE11","dbconfig","US792"})
	public void test02_US792_TC2922_verifyFirstUserVerification() throws SQLException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active Directory Authentication - Create admin user and verification on DB.");

		loginPage = new LoginPage(driver);

		//verifying that first user is present in DB 
		DatabaseMethods db = new DatabaseMethods(driver);
		if(db.getUsersCount()>0) {
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying the first added user");
			//verify the isAdmin field
			loginPage.assertEquals(db.getUserType(db.getFirstInsertedAccountID()), NSDBDatabaseConstants.ADMINUSER, "Verify that the first user is Admin user", "Verified");
		}

	}

	//TC2923 - Active Directory Authentication - create more users and verification on DB.
	@Test(groups ={"Chrome","Edge","IE11","dbconfig","US792"})
	public void test03_US792_TC2923_verifySecondUserCreationAndVerification() throws SQLException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active Directory Authentication - create more users and verification on DB.");

		loginPage = new LoginPage(driver);
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		//verifying that first user is present in DB 
		DatabaseMethods db = new DatabaseMethods(driver);

		//Adding one more user
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		RegisterUserPage rsp = new RegisterUserPage(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying the register page");
		rsp.assertTrue(rsp.getCurrentPageURL().contains(URLConstants.REGISTER_PAGE_URL),"verify user is redirected to register page","verified");
		rsp.assertEquals(rsp.createNewUser(firstName, lastName, email ,LoginPageConstants.NEW_USERNAME, LoginPageConstants.NEW_PASSWORD,LoginPageConstants.NEW_PASSWORD)
				,LoginPageConstants.SUCCESS_MESSAGE,"verify the success message is displayed","verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying the second added user");
		rsp.assertEquals(db.getUserType(db.getLastInsertedAccountID()), NSDBDatabaseConstants.NONADMINUSER, "Verify the isAdmin value for second user", "verified");
		header = new Header(driver);
		header.logout();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the login page");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL),"Verifying that user redirects on login page","verified");
		loginPage.login(LoginPageConstants.NEW_USERNAME, LoginPageConstants.NEW_PASSWORD);

		patientPage.waitForPatientPageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying the user details on header");
		header.assertTrue(header.isElementPresent(header.userInfo),"verifying user information is displayed","verified");
		header.assertEquals(header.userInfo.getText(),firstName+" "+lastName,"Verifying that first and last name is displayed","verified");

		//Deleting the added non admin user
		db.deleteAddedUser(db.getLastInsertedAccountID());	
	}

	//US1513:Require admin credentials to access user list or edit users and other management pages and apis.
	@Test(groups ={"Chrome","Edge","IE11","US1513","Positive","BVT"})
	public void test04_US1513_TC8112_TC8113_TC8126_verifyManagementPageAccessForAdminUser() throws SQLException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  only system administrator is able to add other users to Northstar. <br>"
				+"Verify that  only system administrator has access to Active Directory <br>"+
				"Verify that  only system administrator has access to Password Policy Page");

		loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver); 
		
		//verifying that first user is present in DB 
		DatabaseMethods db = new DatabaseMethods(driver);
		loginPage.assertTrue(db.verifyUserIsAdminUser(username), "Checkpoint[1/7]", "Verified that "+username+" user is admin user.");
		
		//Adding one  user "userA"
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    RegisterUserPage rsp = new RegisterUserPage(driver);
	    rsp.createNewUser(userOne, userOne, email ,userOne,userOne,userOne);
	    
	    //again create another user "test"
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    rsp.createNewUser(userTwo, userTwo, LoginPageConstants.SUPPORT_EMAIL ,userTwo,userTwo,userTwo);
	   
	    //navigate to user list page and verify newly created users
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.USER_PAGE_URL);
	    userPage = new UsersPage(driver);
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.USER_PAGE_URL), "Checkpoint[2/7]", "Verifed that admin user can access user page.");
	    loginPage.assertTrue(userPage.getListOfAllUsers().contains(userOne), "Checkpoint[3/7]", "Verifying newly created user (by admin user) is present on users page");	
	    loginPage.assertTrue(userPage.getListOfAllUsers().contains(userTwo), "Checkpoint[4/7]", "Verifying newly created user (by admin user) is present on users page");	
	    
	    //register page using admin user
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    rsp.waitForRegisterPageToLoad();
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.REGISTER_PAGE_URL), "Checkpoint[5/7]", "Verifed that admin user can access regisetr page.");
	    
	    //active directory page using admin user
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.ACTIVE_DIRECTORY_PAGE_URL);
	    loginPage.waitForURLToChange();
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.ACTIVE_DIRECTORY_PAGE_URL), "Checkpoint[6/7]", "Verifed that admin user can access Active directory page.");
	    
	    //navigate to password policy page using admin user
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
//	    PasswordPolicyPage passwordPolicyPage = new PasswordPolicyPage(driver);
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Checkpoint[7/7]", "Verifed that admin user can access password policy page.");
	  
	    
	}

	@Test(groups ={"Chrome","Edge","IE11","US1513","Negative","BVT"})
	public void test05_US1513_TC8112_TC8113_TC8126_verifyManagementPageAccessForNonAdminUser() throws SQLException, InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  only system administrator is able to add other users to Northstar. <br>"
				+"Verify that  only system administrator has access to Active Directory <br>"+
				"Verify that  only system administrator has access to Password Policy Page");

		loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver); 
		
		//verifying that first user is present in DB 
		DatabaseMethods db = new DatabaseMethods(driver);
		loginPage.assertTrue(db.verifyUserIsAdminUser(username), "Checkpoint[1/6]", "Verified that "+username+" user is admin user.");
		
		//Adding one  user "userA"
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    RegisterUserPage rsp = new RegisterUserPage(driver);
	    rsp.createNewUser(userOne, userOne, email ,userOne,userOne,userOne);
	    
	    //login with first user and check user,register,active directory and Password policy  page is  not accessible .
	    loginPage.navigateToBaseURL();
	    loginPage.login(userOne, userOne);
	    loginPage.assertFalse(db.verifyUserIsAdminUser(userOne), "Checkpoint[2/6]", "Verified that "+userOne+" user is not admin user.");
	    patientPage.waitForPatientPageToLoad();
	    
	    //register page
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/6]", "Verified that non admin user can not access register page");
	    //user page
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.USER_PAGE_URL);
	    loginPage.waitForURLToChange();
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[4/6]", "Verified that non admin user can not access user page");
	    //active directory page
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.ACTIVE_DIRECTORY_PAGE_URL);
	    loginPage.waitForURLToChange();
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[5/6]", "Verified that non admin user can not access Active directory page");
	    //password policy page
	    loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
	    loginPage.waitForURLToChange();
	    loginPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[6/6]", "Verified that non admin user can not access password policy page");
	    
	}

	
	
       @AfterMethod(alwaysRun=true)
	   public void updateDB() throws SQLException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.deleteUser(userOne);
		db.deleteUser(userTwo);
	}	


}
