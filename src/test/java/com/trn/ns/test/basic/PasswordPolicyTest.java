package com.trn.ns.test.basic;




import java.awt.Dimension;
import java.awt.Toolkit;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PasswordPolicyConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PasswordPolicyPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PasswordPolicyTest extends TestBase{
	
	private LoginPage loginPage;
	private PasswordPolicyPage passwordPolicyPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String passPolicyUrl =URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL;
	private Header header;
	private PagesTheme pageTheme;
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();		
		loginPage.login(username,password);
		patientPage=new PatientListPage(driver);
		patientPage.navigateToURL(passPolicyUrl);
		

	}
	
	@Test(groups ={"firefox", "Chrome", "IE11" ,"Edge",})
	public void test01_US80_TC72_verifyPasswordPolicyFields() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("CRUD operations on password policy");		
		
		passwordPolicyPage = new PasswordPolicyPage(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1", "Verify password policy page fields");
		passwordPolicyPage.waitForElementVisibility(passwordPolicyPage.checkbox);
		//Enforce Password History checkbox and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.checkbox.isDisplayed(), true, "Verify enforce Password History checkbox is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.checkboxLabel.getText(), "Enforce Password History", "Verify enforce Password History label is as expected"  , "Verified");

		//Maximum Failed Attempts Before Lock field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.maxFailedAttemptsBeforeLock.isDisplayed(), true, "Verify Maximum Failed Attempts Before Lock field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.maxFailedAttemptsBeforeLockLabel.getText(), "Maximum Failed Attempts Before Lock:", "Verify Maximum Failed Attempts Before Lock field label is as expected"  , "Verified");

		//Maximum Password Age: (days) field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.maximumPasswordAge.isDisplayed(), true, "Verify Maximum Password Age (days) field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.maximumPasswordAgeLabel.getText(), "Maximum Password Age: (days)", "Verify Maximum Password Age: (days) field label is as expected"  , "Verified");

		//Minimum Password Length: field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.minimumPasswordLength.isDisplayed(), true, "Verify Minimum Password Length field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.minimumPasswordLengthLabel.getText(), "Minimum Password Length:", "Verify Minimum Password Length field label is as expected"  , "Verified");

		//Required Lowercase Characters:field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredLowercaseCharactersCount.isDisplayed(), true, "Verify Required Lowercase Characters field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredLowercaseCharactersCountLabel.getText(), "Required Lowercase Characters:", "Verify Required Lowercase Characters field label is as expected"  , "Verified");

		//Required Uppercase Characters :field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredUppercaseCharactersCount.isDisplayed(), true, "Verify Required Uppercase Characters field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredUppercaseCharactersCountLabel.getText(), "Required Uppercase Characters:", "Verify Required Uppercase Characters field label is as expected"  , "Verified");


		//Required Digits:field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredDigitsCount.isDisplayed(), true, "Verify Required Digits field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredDigitsCountLabel.getText(), "Required Digits:", "Verify Required Digits field label is as expected"  , "Verified");


		//Required Special Characters field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredSpecialCharactersCount.isDisplayed(), true, "Verify Required Special Characters field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.requiredSpecialCharactersCountLabel.getText(), "Required Special Characters:", "Verify Required Special Characters field label is as expected"  , "Verified");

		//Allowed Special Characters: field and label
		passwordPolicyPage.verifyEquals(passwordPolicyPage.specialCharacters.isDisplayed(), true, "Verify Allowed Special Characters field is available"  , "Verified");
		passwordPolicyPage.verifyEquals(passwordPolicyPage.specialCharactersLabel.getText(), "Allowed Special Characters:", "Verify Allowed Special Characters field label is as expected"  , "Verified");


		//Allowed Update Password Policy button
		passwordPolicyPage.scrollIntoView(passwordPolicyPage.updatePolicyButton);
		passwordPolicyPage.verifyEquals(passwordPolicyPage.updatePolicyButton.isDisplayed(), true, "Verify Update Password Policy button is available", "Verified");

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","US80","US1930","F923"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test02_US80_TC73_validUpdate"})
	public void test02_US80_TC73_US1930_TC9005_validUpdate(String maxFailAtpts , String maxPwdAge , String minPwdLnth , String lowCaseChars, String uppCaseChars, String reqDigits,String reqSplChars, String allowSplChars, String status , String fieldName) throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the CRUD operations on \"<b>"+fieldName+"</b>\"" +"is validated successfully"
				+ "<br> Verify that Bootstrip Alerts are displayed on Password Policy and Active Directory pages for errors.");
		passwordPolicyPage = new PasswordPolicyPage(driver);

		passwordPolicyPage.waitForElementVisibility(passwordPolicyPage.maxFailedAttemptsBeforeLock);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1",
				"Verify CRUD operation on \"<b>"+fieldName+"</b>\"with valid values");
		
		String message = passwordPolicyPage.updatePassPolicyFieldValue(maxFailAtpts, maxPwdAge, minPwdLnth, lowCaseChars, uppCaseChars, reqDigits, reqSplChars, allowSplChars);

		if (status == "true") {
			passwordPolicyPage.assertEquals(message, PasswordPolicyConstants.SUCCESS_MESSAGE, "Checkpoint[1/2]", "Verifying the update is successful");
			passwordPolicyPage.assertTrue(passwordPolicyPage.verifySuccessMessage(), "Checkpoint[2/2]", "Verifying the update is successful");
		}
		else {
			passwordPolicyPage.assertEquals(message, PasswordPolicyConstants.FAIL_MESSAGE, "Checkpoint[1/2]", "Verifying the update is failed");
			passwordPolicyPage.assertTrue(passwordPolicyPage.verifyFailedMessage(), "Checkpoint[2/2]", "Verifying the update is successful");
		}
	
	}

	@Test(groups ={"Chrome", "IE11" ,"Edge","US1930","Positive","F923","E2E"})
	public void test03_US1930_TC8859_verifyEurekaTheme() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on Password Policy page.");
		passwordPolicyPage = new PasswordPolicyPage(driver);
		
		pageTheme = new PagesTheme(driver);
		
		passwordPolicyPage.assertTrue(pageTheme.verifyThemeOnTextbox(passwordPolicyPage.allInputBoxes,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[1/3]", "Verifying all the text boxes adhere the theme");
		passwordPolicyPage.assertTrue(pageTheme.verifyButtonTheme(passwordPolicyPage.updatePolicyButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[2/3]", "verifying the button adhere the theme");
		passwordPolicyPage.assertTrue(pageTheme.verifyThemeOnLabel(passwordPolicyPage.allLabels,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[3/3]","verifying the labels adhere the theme");
	
		
	}
	
	@Test(groups ={"Chrome","Edge","US1930","Positive","F923","E2E"})
	public void test04_US1930_TC8859_verifyDarkTheme() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on Password Policy page.");
		passwordPolicyPage = new PasswordPolicyPage(driver);
		
		header = new Header(driver);
		header.logout();
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL(passPolicyUrl);
		
		passwordPolicyPage.waitForPasswordPolicyToLoad();
		pageTheme = new PagesTheme(driver);
		
		passwordPolicyPage.assertTrue(pageTheme.verifyThemeOnTextbox(passwordPolicyPage.allInputBoxes,ThemeConstants.DARK_THEME_NAME), "Checkpoint[1/3]", "Verifying all the text boxes adhere the theme");
		passwordPolicyPage.assertTrue(pageTheme.verifyButtonTheme(passwordPolicyPage.updatePolicyButton, NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME), "Checkpoint[2/3]", "verifying the button adhere the theme");
		passwordPolicyPage.assertTrue(pageTheme.verifyThemeOnLabel(passwordPolicyPage.allLabels,ThemeConstants.DARK_THEME_NAME),"Checkpoint[3/3]","verifying the labels adhere the theme");
		
	}
	
	@Test(groups ={"Chrome", "IE11" ,"Edge","DR2269","Positive"})
	public void test05_DR2269_TC9103_verifyScrollBarForPasswordPolicyPage() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify a vertical scrollbar is displayed on Password Policy page when browser is minimized and cannot fit all fields.");		
		
		int resizeHeight=1366;
		int resizeWidth=768;
		passwordPolicyPage = new PasswordPolicyPage(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify scrollbar for password policy page fields on browser minimize.");
		passwordPolicyPage.waitForElementVisibility(passwordPolicyPage.checkbox);
		passwordPolicyPage.assertTrue(passwordPolicyPage.isElementPresent(passwordPolicyPage.checkbox), "Checkpoint[1/7]"  , "Verified checkbox is displayed");
		passwordPolicyPage.assertEquals(passwordPolicyPage.getText(passwordPolicyPage.checkboxLabel), "Enforce Password History", "Checkpoint[2/7]"  , "Verified checkbox label is displayed.");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int currentScreenWidth = (int) screenSize.getWidth(); // current screen width
		int currentScreenHeight = (int) screenSize.getHeight();
				
		if(currentScreenHeight>resizeHeight && currentScreenWidth>resizeWidth)
		passwordPolicyPage.assertFalse(passwordPolicyPage.isElementPresent(passwordPolicyPage.scrollbar), "Checkpoint[3/7]", "Verified that scrollbar is not present on Password policy page.");
		
		//resize browser and verify scrollbar
		passwordPolicyPage.resizeBrowserWindow(resizeHeight, resizeWidth);
		passwordPolicyPage.assertTrue(passwordPolicyPage.isElementPresent(passwordPolicyPage.scrollbar), "Checkpoint[4/7]", "Verified that scrollbar is visible on browser minimize.");
		passwordPolicyPage.assertFalse(passwordPolicyPage.isElementPresent(passwordPolicyPage.updatePolicyButton), "Checkpoint[5/7]", "Verified that update policy button is not visible on browser minimize.");
		passwordPolicyPage.scrollIntoView(passwordPolicyPage.updatePolicyButton);
		passwordPolicyPage.assertTrue(passwordPolicyPage.isElementPresent(passwordPolicyPage.updatePolicyButton), "Checkpoint[6/7]", "Verified that update policy button is visible after scroll.");
		passwordPolicyPage.mouseHover(passwordPolicyPage.scrollbar);
		passwordPolicyPage.assertEquals(passwordPolicyPage.getBackgroundColor(passwordPolicyPage.scrollbar), ThemeConstants.EUREKA_SCROLLBAR_BACKGROUND_COLOR, "Checkpoint[7/7]", "Verified background color of scroll after mousehover.");
		
	
	}
	
	@AfterMethod
	public void updateTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
	}

}
