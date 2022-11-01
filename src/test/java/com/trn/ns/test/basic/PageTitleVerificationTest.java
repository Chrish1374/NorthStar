package com.trn.ns.test.basic;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PasswordPolicyPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PageTitleVerificationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private PasswordPolicyPage passwordPolicyPage;




	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();

	}
	
	//TC1696 - Verify that page title  is not "Web" for login, patient, register pages
	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test01_US569_TC1696_verifyPageTitle() throws InterruptedException {

		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Page Title verification");


		loginPage = new LoginPage(driver);
		// Verifying title on Login Page
		loginPage.assertTrue(loginPage.getTitle().equals(LoginPageConstants.APPLICATION_TITLE),"Verifying Title of Login Page", "Title of Login Page is Eureka");
		//Login in to the application
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));



		// Patient Page Title Verification
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL),"Verifying url of PatientList Page", "Patient List page is displayed");
		patientPage.assertTrue(patientPage.getTitle().equals(LoginPageConstants.APPLICATION_TITLE),"Verifying Title of PatientList Page", "Title of Patient List Page is Eureka");


		//Open Password policy Page in new tab
		patientPage.navigateToChangedURL("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.PASSWORD_POLICY_URL);
		//verifying password policy page in new tab 
		passwordPolicyPage = new PasswordPolicyPage(driver);
		passwordPolicyPage.assertTrue(passwordPolicyPage.getCurrentPageURL().contains(URLConstants.PASSWORD_POLICY_URL), "Verifying the launching of PasswordPolicy Page", "User is on "+ passwordPolicyPage.getCurrentPageURL()+" page");			
		// Password policy Page Title Verification
		passwordPolicyPage.assertTrue(passwordPolicyPage.getTitle().equals(LoginPageConstants.APPLICATION_TITLE),"Verifying Title of PasswordPolicy Page", "Title of PasswordPolicy Page is NorthStar");
		
		//Open Register Page in new tab
		patientPage.navigateToChangedURL("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.REGISTER_PAGE_URL);
		// Register Page Title Verification
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.REGISTER_PAGE_URL),"verifying the page URL","verified");
		patientPage.assertTrue(patientPage.getTitle().contains(LoginPageConstants.APPLICATION_TITLE),"verifying the page title","verified");
		
		//Open User Page in new tab
		patientPage.navigateToChangedURL("http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.USER_PAGE_URL);
		// Register Page Title Verification
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.USER_PAGE_URL),"verifying the page URL","verified");
		patientPage.assertTrue(patientPage.getTitle().contains(LoginPageConstants.APPLICATION_TITLE),"verifying the page title","verified");
		
	}

}