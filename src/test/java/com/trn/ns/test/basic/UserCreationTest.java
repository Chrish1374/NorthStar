package com.trn.ns.test.basic;

import java.sql.SQLException;
import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.Tooltip;
import com.trn.ns.page.factory.UsersPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class UserCreationTest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private RegisterUserPage registerUserPage;
	private UsersPage userPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private DatabaseMethods db;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String uname = "abc";
	String email = "abc@abc.com";
	String wrongEmail = "abc.com";
	String anotherUser ="xyz";
	String registerURL =URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL;
	private UsersPage users;
	private Header header;
	private PagesTheme theme;
	
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage=new PatientListPage(driver);
		loginPage.navigateToURL(registerURL);
		
	}

	@Test(groups ={"Chrome","Edge","IE11","DE844","DE781","Positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_DE844_DE781_UserCreation"})
	public void test01_DE844_DE781_VerifyLengthOfUserNameAndPassword(String testcaseid, String firstname , String middlename,String lastname, String email,String username, String password, String confirmedpassword, String description) throws SQLException, InterruptedException 
	{

		Reporter.getCurrentTestResult().setAttribute("TEST_CASE_ID", testcaseid);
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(testcaseid + ":" + description);
		registerUserPage = new RegisterUserPage(driver);
		
		//creating new user and verifying it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying it : " + username);
		String successMessage = registerUserPage.createNewUser(firstname, middlename, lastname, email, username, password, confirmedpassword);
		
		registerUserPage.assertTrue(successMessage.contains("Success"), "Checkpoint[1/2]", "Verifying newly created user is present on users page");
	
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.USER_PAGE_URL);
		
		//verifying new user
		//loginPage.login(username, password);
		userPage = new UsersPage(driver);
		
		//DE781 verification : in DB
		userPage.assertTrue(userPage.getListOfAllUsers().contains(username), "Checkpoint[2/2]", "Verifying newly created user is present on users page");			
	}

	@Test(groups ={"Chrome","Edge","IE11","US1741","Positive","F195","E2E"})
	public void test02_US1741_TC8546_TC8615_VerifyRenderingModeOnRegisterPageForAdminUser() throws SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop] Verify default rendering mode preference options after fresh install (after dropping database) for admin user. <br>"+
		"Verify rendering mode preference options on browser resize");
		
		registerUserPage = new RegisterUserPage(driver);
		
		//creating new user and verifying it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Desktop and Mobile rendering option on Register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.desktopRenderingModeLabel), "Checkpoint[1/8]", "Verified that desktop rendering option visible on register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.mobileRenderingModeLabel), "Checkpoint[2/8]", "Verified that mobile rendering option visible on register page.");
		
		registerUserPage.assertTrue(registerUserPage.isSelected(registerUserPage.autoRenderingModeForDesktop), "Checkpoint[3/8]", "Verified that Auto option is selected for Desktop mode.");
		registerUserPage.assertTrue(registerUserPage.isSelected(registerUserPage.lossyRenderingModeForMobile), "Checkpoint[4/8]", "Verified that Lossy option is selected for Mobile mode.");
		
		db=new DatabaseMethods(driver);
		registerUserPage.assertEquals(db.getRenderingModeForUser(username,NSDBDatabaseConstants.DESKTOP_RENDERING_MODE), NSDBDatabaseConstants.NONADMINUSER, "Checkpoint[5/8]", "Verified desktop rendering mode as Auto in DB.");
		registerUserPage.assertEquals(db.getRenderingModeForUser(username,NSDBDatabaseConstants.MOBILE_RENDERING_MODE), NSDBDatabaseConstants.ADMINUSER, "Checkpoint[6/8]", "Verified mobile rendering mode as Lossy in DB.");	
	
		registerUserPage.resizeBrowserWindow(500, 800);
		registerUserPage.scrollIntoView(registerUserPage.desktopRenderingModeLabel);
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.desktopRenderingModeLabel), "Checkpoint[7/8]", "Verified that desktop rendering option visible on register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.mobileRenderingModeLabel), "Checkpoint[8/8]", "Verified that mobile rendering option visible on register page.");
		
	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1741","Positive","F195","E2E"})
	public void test03_US1741_TC8548_VerifyRenderingModeOnRegisterPageForNonAdmin() throws SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop] Verify default rendering mode preference options after installation (after dropping database) for non-admin user");
		registerUserPage = new RegisterUserPage(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Desktop and Mobile rendering option on Register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.desktopRenderingModeLabel), "Checkpoint[1/6]", "Verified that desktop rendering option visible on register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.mobileRenderingModeLabel), "Checkpoint[2/6]", "Verified that mobile rendering option visible on register page.");
		
		registerUserPage.assertTrue(registerUserPage.isSelected(registerUserPage.autoRenderingModeForDesktop), "Checkpoint[3/6]", "Verified that Auto option is selected for Desktop mode.");
		registerUserPage.assertTrue(registerUserPage.isSelected(registerUserPage.lossyRenderingModeForMobile), "Checkpoint[4/6]", "Verified that Lossy option is selected for Mobile mode.");
		
		
		//creating new user and verifying it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying it : " + username);
		registerUserPage.createNewUser(LoginPageConstants.FIRST_NAME, LoginPageConstants.LAST_NAME, LoginPageConstants.SUPPORT_EMAIL, LoginPageConstants.NEW_USERNAME, LoginPageConstants.NEW_PASSWORD, LoginPageConstants.NEW_PASSWORD);
		
		//creating new user and verifying it
		
		db=new DatabaseMethods(driver);
		registerUserPage.assertEquals(db.getRenderingModeForUser(LoginPageConstants.NEW_USERNAME,NSDBDatabaseConstants.DESKTOP_RENDERING_MODE), NSDBDatabaseConstants.NONADMINUSER, "Checkpoint[5/6]", "Verified desktop rendering mode as Auto in DB.");
		registerUserPage.assertEquals(db.getRenderingModeForUser(LoginPageConstants.NEW_USERNAME,NSDBDatabaseConstants.MOBILE_RENDERING_MODE), NSDBDatabaseConstants.ADMINUSER, "Checkpoint[6/6]", "Verified mobile rendering mode as Lossy in DB.");	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1741","Negative","F195"})
	public void test04_US1741_TC8549_VerifyRenderingModeUpdateInDBForNonAdminUser() throws SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify updated rendering mode preference options after installation (after dropping database) for non-admin user.");
		registerUserPage = new RegisterUserPage(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Desktop and Mobile rendering option on Register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.desktopRenderingModeLabel), "Checkpoint[1/6]", "Verified that desktop rendering option visible on register page.");
		registerUserPage.assertTrue(registerUserPage.isElementPresent(registerUserPage.mobileRenderingModeLabel), "Checkpoint[2/6]", "Verified that mobile rendering option visible on register page.");
		
		registerUserPage.assertTrue(registerUserPage.isSelected(registerUserPage.autoRenderingModeForDesktop), "Checkpoint[3/6]", "Verified that Auto option is selected for Desktop mode.");
		registerUserPage.assertTrue(registerUserPage.isSelected(registerUserPage.lossyRenderingModeForMobile), "Checkpoint[4/6]", "Verified that Lossy option is selected for Mobile mode.");
		
		registerUserPage.click(registerUserPage.lossyRenderingModeForDesktop);
		registerUserPage.click(registerUserPage.autoRenderingModeForMobile);
		//creating new user and verifying it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying it : " + username);
		registerUserPage.createNewUser(LoginPageConstants.FIRST_NAME, LoginPageConstants.LAST_NAME, LoginPageConstants.SUPPORT_EMAIL, LoginPageConstants.NEW_USERNAME, LoginPageConstants.NEW_PASSWORD, LoginPageConstants.NEW_PASSWORD);
		
		//creating new user and verifying it
		
		db=new DatabaseMethods(driver);
		registerUserPage.assertEquals(db.getRenderingModeForUser(LoginPageConstants.NEW_USERNAME,NSDBDatabaseConstants.DESKTOP_RENDERING_MODE), NSDBDatabaseConstants.ADMINUSER, "Checkpoint[5/6]", "Verified desktop rendering mode as Lossy in DB.");
		registerUserPage.assertEquals(db.getRenderingModeForUser(LoginPageConstants.NEW_USERNAME,NSDBDatabaseConstants.MOBILE_RENDERING_MODE), NSDBDatabaseConstants.NONADMINUSER, "Checkpoint[6/6]", "Verified mobile rendering mode as Auto in DB.");	
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive","US1930","F923","E2E"})
	public void test05_US1930_TC8858_verifyEurekaTheme() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on User management pages.");
		
		registerUserPage = new RegisterUserPage(driver);
		registerUserPage.clickOnCancelButton();
		
		users = new UsersPage(driver);
		List<String> usersList = users.getListOfAllUsers();
		users.click(users.addUserButton);
		
		theme = new PagesTheme(driver);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.assertEquals(registerUserPage.getBackgroundColor(registerUserPage.registerPageForm),ThemeConstants.PAGE_BACKGROUND,"Checkpoint[1/28]","verifying the page background");	
		
		registerUserPage.assertTrue(theme.verifyThemeOnTextbox(registerUserPage.allInputBoxes, ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[2/28]","verifying the theme on all input boxes");
		registerUserPage.assertTrue(theme.verifyThemeOnLabel(registerUserPage.allLabels, ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[3/28]","verifying all the labels");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[4/28]","verifying the submit button is disabled");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[5/28]","verifying the cancel button is enabled");
		
		registerUserPage.inputValuesToForm(uname,uname, uname, uname, uname, uname, uname);
		registerUserPage.assertTrue(theme.verifyThemeOnTooltip(registerUserPage.getTooltipWebElement(registerUserPage.submitButton),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[6/28]","verifying the submit button gets enabled after entering the values");

		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname+"123");
		registerUserPage.assertTrue(theme.verifyThemeOnTooltip(registerUserPage.getTooltipWebElement(registerUserPage.submitButton),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[7/28]","verifying the tooltip theme");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[8/28]","verifying the submit button is disabled");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[9/28]","verifying the cancel button is enabled");
		
		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname);
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[10/28]","verifying the submit button is enabled");
		registerUserPage.assertEquals(registerUserPage.clickOnSubmitButton(),LoginPageConstants.SUCCESS_MESSAGE,"Checkpoint[11/28]","verifying the message on user creation");
		
		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname);
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[12/28]","verifying the submit button");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[13/28]","verifying the cancel button");
		registerUserPage.assertEquals(registerUserPage.clickOnSubmitButton(),LoginPageConstants.ALREADY_CREATED_USR_MESSAGE,"Checkpoint[14/28]","verifying the message when same user is created");
		
		registerUserPage.clickOnCancelButton();			
		users.waitForUsersPageToLoad();
		List<String> updatedList = users.getListOfAllUsers();
		
		users.assertEquals(updatedList.size(),usersList.size()+1,"Checkpoint[15/28]","verifying user is shown on users page");				
		users.assertTrue(updatedList.contains(uname),"Checkpoint[16/28]","verifying the username is present on users list");		
		
		users.assertEquals(users.getBackgroundColor(users.userTableSection),ThemeConstants.EUREKA_TABLE_BACKGROUND,"Checkpoint[17/28]","verifying the user table background");		
		users.assertTrue(theme.verifyButtonTheme(users.addUserButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[18/28]","verifying the add user button");
		users.assertTrue(theme.verifyThemeOnTableHeader(users.columnHeadersTextOverFlow, ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[19/28]","verifying the theme on table header");
		
		users.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.assertEquals(registerUserPage.getBackgroundColor(registerUserPage.registerPageForm),ThemeConstants.PAGE_BACKGROUND,"Checkpoint[20/28]","verifying the register page background when accessed directly");	
		
		registerUserPage.assertTrue(theme.verifyThemeOnTextbox(registerUserPage.allInputBoxes, ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[21/28]","verifying the theme on all text boxes");
		registerUserPage.assertTrue(theme.verifyThemeOnLabel(registerUserPage.allLabels, ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[22/28]","verifying all the labels");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[23/28]","verifying the submit button is disabled");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[24/28]","verifying the cancel button");
		
		registerUserPage.inputValuesToForm(anotherUser,anotherUser, anotherUser, email, anotherUser, anotherUser, anotherUser);
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[25/28]","verifying the submit button gets enabled");
		registerUserPage.assertEquals(registerUserPage.clickOnSubmitButton(),LoginPageConstants.SUCCESS_MESSAGE,"Checkpoint[26/28]","verifying the success message");
	
		registerUserPage.clickOnCancelButton();			
		users.waitForUsersPageToLoad();
		users.assertEquals(users.getListOfAllUsers().size(),updatedList.size()+1,"Checkpoint[27/28]","verifying user added in user's list");				
		users.assertTrue(users.getListOfAllUsers().contains(anotherUser),"Checkpoint[28/28]","verifying the user added");	
		
		
	}
	
	@Test(groups ={"Chrome","Edge","Positive","US1930","F923","E2E"})
	public void test06_US1930_TC8858_verifyDarkTheme() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on User management pages.");
		
		header = new Header(driver);
		header.logout();
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL(registerURL);
		
		registerUserPage = new RegisterUserPage(driver);
		registerUserPage.clickOnCancelButton();
		
		users = new UsersPage(driver);
		List<String> usersList = users.getListOfAllUsers();
		users.click(users.addUserButton);
		
		theme = new PagesTheme(driver);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.assertEquals(registerUserPage.getBackgroundColor(registerUserPage.registerPageForm),ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR,"Checkpoint[1/28]","verifying the page background");	
		
		registerUserPage.assertTrue(theme.verifyThemeOnTextbox(registerUserPage.allInputBoxes, ThemeConstants.DARK_THEME_NAME),"Checkpoint[2/28]","verifying the all text boxes");
		registerUserPage.assertTrue(theme.verifyThemeOnLabel(registerUserPage.allLabels, ThemeConstants.DARK_THEME_NAME),"Checkpoint[3/28]","verifying all the labels");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[4/28]","verifying the submit button is disabled");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[5/28]","verifying the cancel button is enabled");
		
		registerUserPage.inputValuesToForm(uname,uname, uname, uname, uname, uname, uname);
		registerUserPage.assertTrue(theme.verifyThemeOnTooltip(registerUserPage.getTooltipWebElement(registerUserPage.submitButton),ThemeConstants.DARK_THEME_NAME),"Checkpoint[6/28]","verifying the submit button gets enabled after entering the values");

		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname+"123");
		registerUserPage.assertTrue(theme.verifyThemeOnTooltip(registerUserPage.getTooltipWebElement(registerUserPage.submitButton),ThemeConstants.DARK_THEME_NAME),"Checkpoint[7/28]","verifying the tooltip theme");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[8/28]","verifying the submit button is disabled");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[9/28]","verifying the cancel button is enabled");
		
		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname);
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[10/28]","verifying the submit button is enabled");
		registerUserPage.assertEquals(registerUserPage.clickOnSubmitButton(),LoginPageConstants.SUCCESS_MESSAGE,"Checkpoint[11/28]","verifying the message on user creation");
		
		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname);
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[12/28]","verifying the submit button");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[13/28]","verifying the cancel button");
		registerUserPage.assertEquals(registerUserPage.clickOnSubmitButton(),LoginPageConstants.ALREADY_CREATED_USR_MESSAGE,"Checkpoint[14/28]","verifying the message when same user is created");
		
		registerUserPage.clickOnCancelButton();			
		users.waitForUsersPageToLoad();
		List<String> updatedList = users.getListOfAllUsers();
		
		users.assertEquals(updatedList.size(),usersList.size()+1,"Checkpoint[15/28]","verifying user is shown on users page");				
		users.assertTrue(updatedList.contains(uname),"Checkpoint[16/28]","verifying the username is present on users list");		
		
		users.assertEquals(users.getBackgroundColor(users.userTableSection),ThemeConstants.DARK_TABLE_BACKGROUND,"Checkpoint[17/28]","verifying the user table background");		
		users.assertTrue(theme.verifyButtonTheme(users.addUserButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[18/28]","verifying the add user button");
		users.assertTrue(theme.verifyThemeOnTableHeader(users.columnHeadersTextOverFlow, ThemeConstants.DARK_THEME_NAME),"Checkpoint[19/28]","verifying the theme on table header");
		
		users.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		registerUserPage.waitForRegisterPageToLoad();
		registerUserPage.assertEquals(registerUserPage.getBackgroundColor(registerUserPage.registerPageForm),ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR,"Checkpoint[20/28]","verifying the register page background when accessed directly");	
		
		registerUserPage.assertTrue(theme.verifyThemeOnTextbox(registerUserPage.allInputBoxes, ThemeConstants.DARK_THEME_NAME),"Checkpoint[21/28]","verifying the theme on all text boxes");
		registerUserPage.assertTrue(theme.verifyThemeOnLabel(registerUserPage.allLabels, ThemeConstants.DARK_THEME_NAME),"Checkpoint[22/28]","verifying all the labels");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[23/28]","verifying the submit button is disabled");
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.cancelButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[24/28]","verifying the cancel button");
		
		registerUserPage.inputValuesToForm(anotherUser,anotherUser, anotherUser, email, anotherUser, anotherUser, anotherUser);
		registerUserPage.assertTrue(theme.verifyButtonTheme(registerUserPage.submitButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[25/28]","verifying the submit button gets enabled");
		registerUserPage.assertEquals(registerUserPage.clickOnSubmitButton(),LoginPageConstants.SUCCESS_MESSAGE,"Checkpoint[26/28]","verifying the success message");
	
		registerUserPage.clickOnCancelButton();			
		users.waitForUsersPageToLoad();
		users.assertEquals(users.getListOfAllUsers().size(),updatedList.size()+1,"Checkpoint[27/28]","verifying user added in user's list");				
		users.assertTrue(users.getListOfAllUsers().contains(anotherUser),"Checkpoint[28/28]","verifying the user added");	
		
	}
	
	
	//@Test(groups ={"Chrome","Edge","IE11","Positive","DR2383"})
	public void test07_DR2383_TC9358_verifyTooltipNotGettingCut() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tooltips in User List and Domain List from Active Directory page.");
		
		int x1 = 300;
		int y1 = 300;
		int x2 = 500;
		int y2 = 500;
		
		registerUserPage = new RegisterUserPage(driver);		
		registerUserPage.inputValuesToForm(uname,uname, uname, email, uname, uname, uname+123);
		Tooltip tooltipOnPage = new Tooltip(driver);
		registerUserPage.assertTrue(tooltipOnPage.verifyTooltipOnResize(registerUserPage.submitButton,x1,y1),"Checkpoint[1/2]","Verifying the tooltip is not getting cut");
	
		registerUserPage.click(registerUserPage.cancelButton);
		
		users = new UsersPage(driver);
		for(int i =0;i<5;i++)
			registerUserPage.assertTrue(tooltipOnPage.verifyTooltipOnDiffResizeLevels(users.columnHeadersTextOverFlow.get(i),x1,y1,x2,y2),"Checkpoint[2."+i+"/2]","Verifying the tooltip is not getting cut on users page table headers");
		
		
			
	}
	
	
	@AfterMethod
	public void updateTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
	}
}
