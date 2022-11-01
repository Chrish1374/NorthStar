package com.trn.ns.test.patientList;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SearchPanelThemeTest extends TestBase{


	private PatientListPage patientPage;
	private LoginPage loginPage;
	private ExtentTest extentTest;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	@Test(groups={"Chrome","IE11","Edge","US1858","Positive","E2E","F956"})
	public void test01_US1858_TC8900_verifyEurekaThemeForSearchAndClearButton() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel of the new buttons -  'Search' and 'Clear' with Eureka  theme");

		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);

		patientPage=new PatientListPage(driver);
		
		patientPage.mouseHover(patientPage.searchButton);
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.searchButton), ThemeConstants.EUREKA_BUTTON_BACKGROUND_COLOR,"Checkpoint[1/6]","Verified the Eureka theme for search button on mousehover.");
		
		patientPage.mouseHover(patientPage.clearButton);
		patientPage.assertEquals(patientPage.getColorOfRows(patientPage.clearButton),ThemeConstants.EUREKA_LABEL_FONT_COLOR,"Checkpoint[2/6]","Verified the color for Clear button on mouse hover.");
		patientPage.assertEquals(patientPage.getBorderColorOfWebElemnt(patientPage.clearButton),ThemeConstants.EUREKA_BUTTON_BORDER_COLOR,"Checkpoint[3/6]","Verified Eureka theme for border color on mouse hover on clear button.");

		patientPage.click(patientPage.searchButton);
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.searchButton), ThemeConstants.EUREKA_BUTTON_BACKGROUND_COLOR,"Checkpoint[4/6]","Verifying the Eureka theme for search button on click.");
	
		patientPage.click(patientPage.clearButton);
		patientPage.assertEquals(patientPage.getColorOfRows(patientPage.clearButton),ThemeConstants.EUREKA_LABEL_FONT_COLOR,"Checkpoint[5/6]","Verified the color for Clear button on click");
		patientPage.waitForTimePeriod(500);
		patientPage.assertTrue(patientPage.getBorderColorOfWebElemnt(patientPage.clearButton).contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR),"Checkpoint[6/6]","Verified Eureka theme for border color for clear button.");
		
	}
	
	@Test(groups={"Chrome","Edge","US1858","Positive","E2E","F956"})
	public void test02_US1858_TC8912_verifyDarkThemeForSearchAndClearButton() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel of the new buttons -  'Search' and 'Clear' with Dark theme");

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);

        patientPage=new PatientListPage(driver);
		
		patientPage.mouseHover(patientPage.searchButton);
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.searchButton), ThemeConstants.DARK_BUTTON_BACKGROUND_COLOR,"Checkpoint[1/6]","Verified the Dark theme for search button on mousehover.");
		
		patientPage.mouseHover(patientPage.clearButton);
	    patientPage.assertEquals(patientPage.getColorOfRows(patientPage.clearButton),ThemeConstants.DARK_LABEL_FONT_COLOR,"Checkpoint[2/6]","Verified the color for Clear button on mouse hover.");
		patientPage.assertEquals(patientPage.getBorderColorOfWebElemnt(patientPage.clearButton),ThemeConstants.DARK_BUTTON_BORDER_COLOR,"Checkpoint[3/6]","Verified Dark theme for border color on mouse hover on clear button.");

		patientPage.click(patientPage.searchButton);
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.searchButton), ThemeConstants.DARK_BUTTON_BACKGROUND_COLOR,"Checkpoint[4/6]","Verifying the Dark theme for search button on click.");
		
		patientPage.click(patientPage.clearButton);
		patientPage.assertEquals(patientPage.getColorOfRows(patientPage.clearButton),ThemeConstants.DARK_LABEL_FONT_COLOR,"Checkpoint[5/6]","Verified the color for Clear button on click");
		patientPage.waitForTimePeriod(500);
		patientPage.assertTrue(patientPage.getBorderColorOfWebElemnt(patientPage.clearButton).contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR),"Checkpoint[6/6]","Verified Dark theme for border color for clear button.");
	
	}

	@Test(groups={"Chrome","IE11","Edge","US1858","Positive","E2E","F956"})
	public void test03_US1858_TC8913_verifyEurekaThemeForGenderCheckbox() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel of the checkboxes for gender - Male and Female with Eureka theme.");

		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);

		patientPage=new PatientListPage(driver);
		
		PagesTheme theme=new PagesTheme(driver);
		patientPage.mouseHover(patientPage.maleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.maleLabel,false,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[1/4]","Verified that Eureka theme on male label on mousehover.");
		
		patientPage.mouseHover(patientPage.femaleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.femaleLabel,false,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[2/4]","Verified that Eureka theme on female label on mousehover.");
		
		patientPage.click(patientPage.maleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.maleLabel,true,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[3/4]","Verified that Eureka theme on male label on click.");
		
		patientPage.click(patientPage.femaleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.femaleLabel,true,ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[4/4]","Verified that Eureka theme on female label on click.");
		
	}

	@Test(groups={"Chrome","Edge","US1858","Positive","E2E","F956"})
	public void test04_US1858_TC8915_verifyDarkThemeForGenderCheckbox() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel of the checkboxes for gender - Male and Female with dark theme");

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);

		patientPage=new PatientListPage(driver);
		PagesTheme theme=new PagesTheme(driver);
		
		patientPage.mouseHover(patientPage.maleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.maleLabel,false,ThemeConstants.DARK_THEME_NAME),"Checkpoint[1/4]","Verified that Dark theme on male label on mousehover.");
		
		patientPage.mouseHover(patientPage.femaleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.femaleLabel,false,ThemeConstants.DARK_THEME_NAME),"Checkpoint[2/4]","Verified that Dark theme on female label on mousehover.");
		
		patientPage.click(patientPage.maleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.maleLabel,true,ThemeConstants.DARK_THEME_NAME),"Checkpoint[3/4]","Verified that Dark theme on male label on click.");
		
		patientPage.click(patientPage.femaleCheckbox);
		patientPage.assertTrue(theme.verifyThemeForCheckbox(patientPage.femaleLabel,true,ThemeConstants.DARK_THEME_NAME),"Checkpoint[4/4]","Verified that Dark theme on female label on click.");
		
	}
	

	@AfterMethod
	public void revertDefaultTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
		
		
	}


}

