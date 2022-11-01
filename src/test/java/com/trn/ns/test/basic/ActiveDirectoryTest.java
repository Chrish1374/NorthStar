package com.trn.ns.test.basic;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PasswordPolicyConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.ActiveDirectoryPage;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.Tooltip;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ActiveDirectoryTest extends TestBase{

	String protocolName;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ActiveDirectoryPage activeDirectory;
	private String myURL = URLConstants.BASE_URL + URLConstants.ACTIVE_DIRECTORY_PAGE_URL;
	private Header header;

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	String domain = Configurations.TEST_PROPERTIES.get("domain");
	String group = Configurations.TEST_PROPERTIES.get("group");
	private PagesTheme theme;
	private Tooltip tooltipOnPage;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
		// Access activeDirectory page URL by appending activeDirectory
		patientPage.navigateToURL(myURL);
		
	}


	@Test(groups ={"Chrome","Edge","IE11","US627","Positive"})
	public void test01_US627_TC3164_AddingDomains() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Adding domains with and without authentication requirement");
		
		activeDirectory = new ActiveDirectoryPage(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verify domains added without authentication requirement");
		activeDirectory.assertTrue(activeDirectory.importDomainToActiveDirectoryDomain(domain), "Verify domains added without authentication requirement", "verified");
		
	}
		
	@Test(groups ={"Chrome","Edge","IE11","US627","Positive"})
	public void test02_US627_TC3171_VerifyInDBForGroupsSelectOnUI() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Try select/check and unchecked few groups from domain groups on UI and click on 'Update' button  and verify on Db");
		activeDirectory = new ActiveDirectoryPage(driver);
		
		activeDirectory.importDomainToActiveDirectoryDomain(domain);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Verify select/check few groups from domain groups on UI and click on 'Update' button");
		activeDirectory.assertTrue(activeDirectory.selectGroupsAndUpdate(group), "Verify select/check few groups from domain groups on UI and click on 'Update' button", "verified");
		loginPage.waitForLoginPageToLoad();
		
		DatabaseMethods db= new DatabaseMethods(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify groups on Db updated");
		db.assertTrue(db.verifyingGroupinDB(domain, group), "Verify groups on Db updated", "verified");
				
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		// Access activeDirectory page URL by appending activeDirectory
		patientPage.navigateToURL(myURL);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify deselect/uncheck few groups from domain groups on UI and click on 'Update' button");
		activeDirectory.assertTrue(activeDirectory.deselectGroupsAndUpdate(group), "Verify deselect/uncheck few groups from domain groups on UI and click on 'Update' button", "verified");
		
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		// Access activeDirectory page URL by appending activeDirectory
		patientPage.navigateToURL(myURL);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Verify groups remove from Db");
		db.assertFalse(db.verifyingGroupinDB(domain, group), "Verify groups  remove from Db", "verified");
		
	}

	@Test(groups ={"Chrome","Edge","IE11","US627","DE1421"})
	public void test03_US627_TC3172_DE1421_TC5479_VerifySearchTextOnActiveDirectory() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify search Text on Active Directory"
				+ "<br> Verify search criteria updates when searching for a group name in the search text in ActiveDirectory");
		
		activeDirectory = new ActiveDirectoryPage(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verify 'Search Text'(where user can add search text) Search and clear button  are present");
		activeDirectory.assertTrue(activeDirectory.isElementPresent(activeDirectory.searchTextfield), "Verify 'Search Text'(where user can add search text) are present.", "verified");
		activeDirectory.assertTrue(activeDirectory.isElementPresent(activeDirectory.searchButton), "Verify 'Search' button are present.", "verified");
		activeDirectory.assertTrue(activeDirectory.isElementPresent(activeDirectory.clearButton), "Verify 'clear' button are present.", "verified");
		
		String subString = "local";
		activeDirectory.enterText(activeDirectory.searchTextfield, subString);
		activeDirectory.waitForActiveDirectoryPageToLoad();
		activeDirectory.click(activeDirectory.searchButton);
		activeDirectory.waitForActiveDirectoryPageToLoad();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Verify Results matching to the substring filtered ");
		List<WebElement> weblElementList = activeDirectory.groups;
		for(WebElement webElement:weblElementList){
				activeDirectory.assertTrue(activeDirectory.containsIgnoreCase(activeDirectory.getText(webElement), subString), "Results matching to the substring filtered ", "verified");;
			}
		activeDirectory.waitForActiveDirectoryPageToLoad();
		activeDirectory.click(activeDirectory.clearButton);
		activeDirectory.waitForActiveDirectoryPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Verify Results clear after clear button clicked ");
		activeDirectory.assertTrue(activeDirectory.getText(activeDirectory.searchTextfield).isEmpty(), "Verify Results clear after clear button clicked ", "Verified");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US627","Negative"})
	public void test04_US627_TC3235_VerifyRemoveDomainFromActiveDirectory() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Remove domain on active Directory");
		activeDirectory = new ActiveDirectoryPage(driver);
		activeDirectory.waitForActiveDirectoryPageToLoad();
		
		activeDirectory.importDomainToActiveDirectoryDomain(domain);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verify Remove domain on active Directory");
		activeDirectory.assertTrue(activeDirectory.verifyDomainRemoveFromActiveDirectory(domain), "Verify Remove domain on active Directory", "Verified");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1930","Positive","F923","E2E"})
	public void test05_US1930_TC8857_verifyEurekaTheme() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on Active Directory pages.");		
		activeDirectory = new ActiveDirectoryPage(driver);
		theme = new PagesTheme(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify domains added without authentication requirement");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.addButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[1/32]","Verifying by default add button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.removeButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[2/32]","verifying remove button is disabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.editButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[3/32]","Verifying edit button is disabled");
		
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.searchButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[4/32]","Verifying the search button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.clearButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[5/32]","Verifying the clear button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.updateButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[6/32]","Verifying the update button is disabled");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify domains added without authentication requirement");
		activeDirectory.assertTrue(theme.verifyThemeOnTableHeader(activeDirectory.activeDirDomainTableHeader,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[7/32]","verifying the table header");
		activeDirectory.assertEquals(activeDirectory.getBackgroundColor(activeDirectory.activeDirPage),ThemeConstants.PAGE_BACKGROUND,"Checkpoint[8/32]","Verifying the table background");
		
		activeDirectory.assertTrue(theme.verifyThemeOnTooltip(activeDirectory.getTooltipWebElement(activeDirectory.removeButton),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[9/32]","verifying the tooltip on remove button");
		activeDirectory.assertTrue(theme.verifyThemeOnTooltip(activeDirectory.getTooltipWebElement(activeDirectory.editButton),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[10/32]","verifying the tooltip on edit button");
		activeDirectory.assertTrue(theme.verifyThemeOnTooltip(activeDirectory.getTooltipWebElement(activeDirectory.updateButton),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[11/32]","verifying the tooltip on update button");
		
		activeDirectory.assertEquals(activeDirectory.getTooltip(activeDirectory.removeButton),PasswordPolicyConstants.ACTIVE_DIRECTORY_REMOVE_TOOLTIP,"Checkpoint[12/32]","Verifying the tooltip message on remove button");
		activeDirectory.assertEquals(activeDirectory.getTooltip(activeDirectory.editButton),PasswordPolicyConstants.ACTIVE_DIRECTORY_EDIT_TOOLTIP,"Checkpoint[13/32]","Verifying the tooltip message on edit button");
		activeDirectory.assertEquals(activeDirectory.getTooltip(activeDirectory.updateButton),PasswordPolicyConstants.ACTIVE_DIRECTORY_UPDATE_TOOLTIP,"Checkpoint[14/32]","Verifying the tooltip message on update button");
		
		activeDirectory.click(activeDirectory.addButton);		
		activeDirectory.assertEquals(activeDirectory.getBackgroundColor(activeDirectory.addDomainPopup),ThemeConstants.EUREKA_POPUP_BACKGROUND,"Checkpoint[15/32]","verifying the popup background");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.crossIcon,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[16/32]","verifying the cross icon is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.importButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[17/32]","verifying import button is disabled");
	
		activeDirectory.enterText(activeDirectory.domainAddTextbox,domain);
		activeDirectory.enterText(activeDirectory.domainServerAddTextbox,domain);
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.importButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[18/32]","verifying the import button gets enabled after entering the domain");
		
		activeDirectory.click(activeDirectory.crossIcon);
		
		activeDirectory.importDomainToActiveDirectoryDomain(domain);
		activeDirectory.waitForTimePeriod(3000);
		
		for(WebElement ele :activeDirectory.addedDomainList) {			
			activeDirectory.assertTrue(activeDirectory.getText(ele).contains(domain),"Checkpoint[19/32]","verifying the domain is added and showing in list");
			activeDirectory.assertEquals(activeDirectory.getColorOfRows(ele),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[20/32]","verifying the text of rows");
		}
		
		
		activeDirectory.assertTrue(activeDirectory.availableGroupsList.size()>=1,"Checkpoint[21/32]","verifying the groups are also visible on right hand side");
		
		int i =0;
		for(WebElement ele :activeDirectory.availableGroupsList) {			
			if(i<5) {
			activeDirectory.assertEquals(activeDirectory.getColorOfRows(ele),ThemeConstants.EUREKA_LABEL_FONT_COLOR,"Checkpoint[22/32]","verifying the font color of groups");
			activeDirectory.assertEquals(activeDirectory.getBackgroundColor(ele),ThemeConstants.EUREKA_TABLE_BACKGROUND,"Checkpoint[23/32]","verifying the row background");

			
			}else break;
			i++;
		}	

		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.addButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[24/32]","verifying the add button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.removeButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[25/32]","verifying the remove button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.editButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[26/32]","verifying the edit button is enabled");
		
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.searchButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[27/32]","verifying the search button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.clearButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[28/32]","verifying the clear button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.updateButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[29/32]","verifying the update button is disabled");
		
		activeDirectory.click(activeDirectory.editButton);
		activeDirectory.assertEquals(activeDirectory.getBackgroundColor(activeDirectory.addDomainPopup),ThemeConstants.EUREKA_POPUP_BACKGROUND,"Checkpoint[30/32]","verifying the popup background on edit on domain");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.crossIcon,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[31/32]","verifying the cross button is enabled on edit of domain");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.importButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[32/32]","verifying the import button is enabled on edit of domain");
	
		activeDirectory.click(activeDirectory.crossIcon);
		
		activeDirectory.click(activeDirectory.removeButton);
		
	}
		
	@Test(groups ={"Chrome","Edge","US1930","Positive","F923","E2E"})
	public void test06_US1930_TC8857_verifyDarkTheme() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on Active Directory pages.");
		
		header = new Header(driver);
		header.logout();
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL(myURL);
		theme = new PagesTheme(driver);
		activeDirectory = new ActiveDirectoryPage(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify domains added without authentication requirement");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.addButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[1/32]","Verifying by default add button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.removeButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[2/32]","verifying remove button is disabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.editButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[3/32]","Verifying edit button is disabled");
		
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.searchButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[4/32]","Verifying the search button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.clearButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[5/32]","Verifying the clear button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.updateButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[6/32]","Verifying the update button is disabled");
			
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify domains added without authentication requirement");
		activeDirectory.assertTrue(theme.verifyThemeOnTableHeader(activeDirectory.activeDirDomainTableHeader,ThemeConstants.DARK_THEME_NAME),"Checkpoint[7/32]","verifying the table header");
		activeDirectory.assertEquals(activeDirectory.getBackgroundColor(activeDirectory.activeDirPage),ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR,"Checkpoint[8/32]","Verifying the table background");
		
		activeDirectory.assertTrue(theme.verifyThemeOnTooltip(activeDirectory.getTooltipWebElement(activeDirectory.removeButton),ThemeConstants.DARK_THEME_NAME),"Checkpoint[9/32]","verifying the tooltip on remove button");
		activeDirectory.assertTrue(theme.verifyThemeOnTooltip(activeDirectory.getTooltipWebElement(activeDirectory.editButton),ThemeConstants.DARK_THEME_NAME),"Checkpoint[10/32]","verifying the tooltip on edit button");
		activeDirectory.assertTrue(theme.verifyThemeOnTooltip(activeDirectory.getTooltipWebElement(activeDirectory.updateButton),ThemeConstants.DARK_THEME_NAME),"Checkpoint[11/32]","verifying the tooltip on update button");
		
		activeDirectory.assertEquals(activeDirectory.getTooltip(activeDirectory.removeButton),PasswordPolicyConstants.ACTIVE_DIRECTORY_REMOVE_TOOLTIP,"Checkpoint[12/32]","Verifying the tooltip message on remove button");
		activeDirectory.assertEquals(activeDirectory.getTooltip(activeDirectory.editButton),PasswordPolicyConstants.ACTIVE_DIRECTORY_EDIT_TOOLTIP,"Checkpoint[13/32]","Verifying the tooltip message on edit button");
		activeDirectory.assertEquals(activeDirectory.getTooltip(activeDirectory.updateButton),PasswordPolicyConstants.ACTIVE_DIRECTORY_UPDATE_TOOLTIP,"Checkpoint[14/32]","Verifying the tooltip message on update button");

		
		activeDirectory.click(activeDirectory.addButton);		
		activeDirectory.assertEquals(activeDirectory.getBackgroundColor(activeDirectory.addDomainPopup),ThemeConstants.DARK_POPUP_BACKGROUND,"Checkpoint[15/32]","verifying the popup background");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.crossIcon,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[16/32]","verifying the cross icon is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.importButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[17/32]","verifying import button is disabled");
			
		activeDirectory.enterText(activeDirectory.domainAddTextbox,domain);
		activeDirectory.enterText(activeDirectory.domainServerAddTextbox,domain);
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.importButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[18/32]","verifying the import button gets enabled after entering the domain");
		
		activeDirectory.click(activeDirectory.crossIcon);
		
		activeDirectory.importDomainToActiveDirectoryDomain(domain);
		activeDirectory.waitForTimePeriod(3000);
		
		for(WebElement ele :activeDirectory.addedDomainList) {			
			activeDirectory.assertTrue(activeDirectory.getText(ele).contains(domain),"Checkpoint[19/32]","verifying the domain is added and showing in list");
			activeDirectory.assertEquals(activeDirectory.getColorOfRows(ele),ThemeConstants.THEME_BUTTON_TEXT_COLOR,"Checkpoint[20/32]","verifying the text of rows");
		}
		
		activeDirectory.assertTrue(activeDirectory.availableGroupsList.size()>=1,"Checkpoint[21/32]","verifying the groups are also visible on right hand side");
		int i =0;
		for(WebElement ele :activeDirectory.availableGroupsList) {			
			if(i<5) {
			activeDirectory.assertEquals(activeDirectory.getColorOfRows(ele),ThemeConstants.DARK_LABEL_FONT_COLOR,"Checkpoint[22/32]","verifying the font color of groups");
			activeDirectory.assertEquals(activeDirectory.getBackgroundColor(ele),ThemeConstants.DARK_TABLE_BACKGROUND,"Checkpoint[23/32]","verifying the row background");

			
			}else break;
			i++;
		}	



		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.addButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[24/32]","verifying the add button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.removeButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[25/32]","verifying the remove button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.editButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[26/32]","verifying the edit button is enabled");
		
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.searchButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[27/32]","verifying the search button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.clearButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[28/32]","verifying the clear button is enabled");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.updateButton,NSGenericConstants.DISABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[29/32]","verifying the update button is disabled");
	
		activeDirectory.click(activeDirectory.editButton);
	
		activeDirectory.assertEquals(activeDirectory.getBackgroundColor(activeDirectory.addDomainPopup),ThemeConstants.DARK_POPUP_BACKGROUND,"Checkpoint[30/32]","verifying the popup background on edit on domain");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.crossIcon,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[31/32]","verifying the cross button is enabled on edit of domain");
		activeDirectory.assertTrue(theme.verifyButtonTheme(activeDirectory.importButton,NSGenericConstants.ENABLE_TEXT,ThemeConstants.DARK_THEME_NAME),"Checkpoint[32/32]","verifying the import button is enabled on edit of domain");
		activeDirectory.click(activeDirectory.crossIcon);
		
		activeDirectory.click(activeDirectory.removeButton);
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive","DR2383"})
	public void test07_DR2383_TC9358_verifyTooltipNotGettingCut() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tooltips in User List and Domain List from Active Directory page.");
		
		int x1 = 300;
		int y1 = 350;
		int x2 = 500;
		int y2 = 500;
		
		
		activeDirectory = new ActiveDirectoryPage(driver);
		tooltipOnPage = new Tooltip(driver);
		activeDirectory.assertTrue(tooltipOnPage.verifyTooltipOnResize(activeDirectory.removeButton,x1,y1),"Checkpoint[1/4]","Verifying the tooltip is not getting cut for remove button");
		activeDirectory.assertTrue(tooltipOnPage.verifyTooltipOnResize(activeDirectory.editButton,x1,y1),"Checkpoint[2/4]","Verifying the tooltip is not getting cut for edit button");
		activeDirectory.assertTrue(tooltipOnPage.verifyTooltipOnResize(activeDirectory.updateButton,x1,y1),"Checkpoint[3/4]","Verifying the tooltip is not getting cut for update button");
		
		for(int i =0;i<activeDirectory.activeDirDomainTableHeader.size();i++) {
			
			activeDirectory.assertTrue(tooltipOnPage.verifyTooltipOnDiffResizeLevels(activeDirectory.activeDirDomainTableHeader.get(i),x1,y1,x2,y2),"Checkpoint[4."+i+"/4]","Verifying the tooltip is not getting cut for table header");
			
		}
		
			
	}
	
	@AfterMethod
	public void updateTheme() {
		
		activeDirectory = new ActiveDirectoryPage(driver);
		for(int i=0;i<activeDirectory.addedDomainList.size();i++) 
			activeDirectory.click(activeDirectory.removeButton);
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
	}
	

	
}
