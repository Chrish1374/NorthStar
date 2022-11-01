package com.trn.ns.test.basic;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AboutIconTest extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	String attribute = "src";	
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();

	}

	
	@Test(groups ={"Chrome","Edge","IE11", "US888","US1835","E2E", "Positive","F930"})
	public void test01_US888_TC3498_TC3501_US1835_TC8577_TC8614_verifyAboutAndCEMarkingLinksInsideIiconMenu() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'i' icon displayed on the login page"
									+"<br>Verify About and CE marking links inside 'i' icon menu. <br>"+
				                     "Verify look and feel of Info pop up");

		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		// Verifying the i Icon button presence
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/14]", "Verify 'i' icon is present on right bottom corner of login page");
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.iIconButton),"Checkpoint[2/14]" ,"Verify 'i' icon is present on right bottom corner of login page");
		loginPage.compareElementImage(protocolName, loginPage.loginPage, "Checkpoint[3/14]", "test01_03_iIcon_Button");
		
		// verify location of iIcon logo
		loginPage.mouseHover(loginPage.iIconButton);
		loginPage.compareElementImage(protocolName, loginPage.loginPage, "Checkpoint[4/14]", "test01_06_iIcon_Button_On_MouseHover");
		
		loginPage.click(loginPage.iIconButton);
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.infoPopup), "Checkpoint[5/14]", "Verified that info pop up open after click on iIcon.");
		loginPage.click(loginPage.copyRightInfo);
		loginPage.assertFalse(loginPage.isElementPresent(loginPage.infoPopup), "Checkpoint[6/14]", "Verified that info pop up closed after click on anywhere. ");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3498_2]", "Verify About and CE marking is present in menu");
		loginPage.click(loginPage.iIconButton);
		loginPage.assertTrue(loginPage.verifyLinksPresentInsideInfoPopUp(loginPage.textFromInfoPopUp), "Checkpoint[7/14]", "Verified that 4 links are present inside info pop up.");
		loginPage.compareElementImage(protocolName, loginPage.iIconWithInfoPopUp, "Checkpoint[8/14]", "test01_13_iIcon_Info_PopUp");
		
		//verify After clicking on About menu, about page should open in another tab.
		loginPage.click(loginPage.about);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3501_2]", "Verify After clicking on About menu, about page should open in another tab.");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL),"Checkpoint[9/14]", "Verify About page should open in another tab");
		loginPage.assertTrue(loginPage.getTextForPage().contains(LoginPageConstants.HAMMERJS), "Checkpoint[10/14]", "The Hammer JS licensing information is present on page");
		loginPage.assertTrue(loginPage.getTextForPage().contains(LoginPageConstants.BOOTSTARP), "Checkpoint[11/14]","The ng-bootstrap licensing information is present on page");
		loginPage.assertTrue(loginPage.getTextForPage().contains(LoginPageConstants.NEWTONSOFT),"Checkpoint[12/14]","The Newtonsoft.Json licensing information is present on page");
	
		loginPage.switchToWindow(parentWindow);
		loginPage.refreshWebPage();
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.ceMarkingLink);
		childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3501_3]", "Verify CE marking clicking on menu, CE marking page should open in another tab.");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.CEMARKING_PAGE_URL),"Checkpoint[13/14]", "Verify CE marking page should open in another tab");
	
		loginPage.switchToWindow(parentWindow);
		loginPage.refreshWebPage();
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.buildVersionInAboutSection);
		loginPage.assertFalse(loginPage.isElementPresent(loginPage.infoPopup), "Checkpoint[14/14]", "Verified that link is not open after click on build version in about section.");
	}
		
	@Test(groups ={"Chrome","Edge","IE11", "US871", "Sanity", "Positive"})
	public void test02_US871_TC3393_verifyCEPngDisplayOnCEMarkingPage() throws InterruptedException {
		// Setting the test case Description
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on clicking CE mark CE png should get display");

		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		//verify After clicking on CE menu, CE marking page should open in another tab.
		loginPage.click(loginPage.ceMarkingLink);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		loginPage.switchToWindow(childWinHandles.get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3393_1][1/2]", "CE marking page should open in another tab.");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.CEMARKING_PAGE_URL), "CE marking page should open in another tab", "Verified");
	    
		loginPage.switchToWindow(parentWindow);
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.ceMarkingImg);
		loginPage.switchToWindow(childWinHandles.get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[TC3393_2][2/2]", "Verify that on clicking CE mark CE png should get display");
		//String pngSrc = loginPage.getAttributeValue(loginPage.ceMarkingImg, attribute);
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.CEMARKING_PAGE_URL), "Verify that on clicking CE mark CE png should get display", "Verified");
	}
	
	@Test(groups ={"Chrome","Edge","IE11", "US1373","US1835","US1985","E2E", "Positive","F930","F931"})
	public void test03_US1373_TC7342_US1835_TC8614_US1985_TC8820_verifyCEMarkingPageContent() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the CE mark of the Northstar application is updated.<br>"+
		"Verify 'About Us', 'Patent' and 'CE Marking'  links are getting open after clicking on corresponding row. <br>"+
				"Verify that the CE mark of the Eureka application is updated.");

		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.ceMarkingLink);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "CE marking page should open in another tab.");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.CEMARKING_PAGE_URL), "CE marking page should open in another tab", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "CE marking page contents");
		loginPage.compareElementImage(protocolName, loginPage.ceMarkingImg,"Verifying CE marking page contents","test03_01");

	}
	
	@Test(groups ={"Chrome","Edge","IE11", "US1165", "Sanity", "Positive"})
	public void test04_US1165_TC5721_verifyBuildNumberInAboutPage() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("The version number should not be changed. It should be  Version 1.0.0.0");

		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		
		String version = loginPage.getText(loginPage.buildVersionInAboutSection).substring(1,7);
		loginPage.click(loginPage.about);
		
		
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "About page should open in another tab");
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL), "Verify About page should open in another tab", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify version 1.0.0.0 is present on About page");
		loginPage.assertTrue(loginPage.getTextForPage().contains(LoginPageConstants.NORTHSTAR_VERSION_ABOUT_PAGE+version), "Verify version 1.0.0.0 is present on About page", "Verified");

	}

	@Test(groups ={"Chrome","Edge","IE11", "US1384","US1835", "E2E", "Positive","F930"})
	public void test05_US1384_TC7490_US1835_TC8614_TC8619_TC8667_verifyCEMarkingAndPatentPageContent() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify patent and copyright info on login screen. <br>"+
		"Verify 'About Us', 'Patent' and 'CE Marking'  links are getting open after clicking on corresponding row. <br>"+
				"Verify copyright information on login screen. <br>"+
		        "Verify copyright information on login screen on browse resize");

		loginPage = new LoginPage(driver);
		
		loginPage.assertFalse(loginPage.getText(loginPage.copyRightInfo).isEmpty(), "Checkpoint[1/12]", "Verified that copyright info visible on login page.");
		loginPage.assertEquals(loginPage.getText(loginPage.copyRightInfo), LoginPageConstants.COPYRIGHT_INFO, "Checkpoint[2/12]", "Copy right info is visible on login page." );
		loginPage.assertEquals(loginPage.getCssValue(loginPage.copyRightInfo,LoginPageConstants.TEXT_ALIGN), ViewerPageConstants.CENTER, "Checkpoint[3/12]", "Verified alignment of copyright info on login page.");
		
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		loginPage.waitForElementVisibility(loginPage.infoPopup);
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.patentLink), "Checkpoint[4/12]", "Verified patent link in About Icon.");
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.patentImg), "Checkpoint[5/12]", "Verified patent icon in About Icon.");
		loginPage.click(loginPage.patentLink);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.PATENT_PAGE_URL), "Checkpoint[6/12]", "Patent page open in another tab");

		
		//verify patent link on browser resize window
		loginPage.switchToWindow(parentWindow);
		loginPage.resizeBrowserWindow(300, 500);
		loginPage.assertFalse(loginPage.isElementPresent(loginPage.verticalScroll), "Checkpoint[7/12]", "Verified vertical scrollbar on login page after browser resize window.");
		
		loginPage.assertEquals(loginPage.getText(loginPage.copyRightInfo), LoginPageConstants.COPYRIGHT_INFO, "Checkpoint[8/12]", "Copy right info is visible on login page." );
		loginPage.assertEquals(loginPage.getCssValue(loginPage.copyRightInfo,LoginPageConstants.TEXT_ALIGN), ViewerPageConstants.CENTER, "Checkpoint[9/12]", "Verified alignment of copyright info on login page after browser resize.");
		
		parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.patentLink), "Checkpoint[10/12]", "Verified patent link in About Icon.");
		loginPage.assertTrue(loginPage.isElementPresent(loginPage.patentImg), "Checkpoint[11/12]", "Verified patent icon in About Icon.");
		loginPage.click(loginPage.patentLink);
        childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(LoginPageConstants.PATENT_PAGE_URL),"Checkpoint[12/12]","Patent page open in new tab window.");


	}

	@Test(groups ={"Chrome","Edge","IE11","US1835","DR2275","Positive","F930","E2E"})
	public void test06_US1835_TC8589_DR2275_TC9076_verifyiIconInfoPopUpOnMouseHover() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify icons present in the information pop up.<br>"+
		"Verify that the text along with Icon in Info pop up is  getting highlighted on mousehover.");

		loginPage = new LoginPage(driver);
		
		loginPage.click(loginPage.iIconButton);
	
		//mousehover on text within info pop up
		for(int i=1;i<loginPage.textFromInfoPopUp.size();i++)
		{
		loginPage.mouseHover(loginPage.textFromInfoPopUp.get(i-1));
		loginPage.assertTrue(loginPage.verifyTextIsHighlightedInInfoPopUp(loginPage.textFromInfoPopUp.get(i-1)), "Checkpoint[1."+(i-1)+"/4]", "Verified text-"+(i+1)+" is highlighted from info pop up on mousehover");
		loginPage.assertTrue(loginPage.verifyIconIsHighlightedInInfoPopUp(loginPage.iconsFromInfoPopUp.get(i-1)), "Checkpoint[2."+(i-1)+"/4]", "Verified icon-"+(i+1)+" is highlighted from info pop up on mousehover");
		}
		
		loginPage.mouseHover(loginPage.buildVersionInAboutSection);
		loginPage.assertTrue(loginPage.verifyTextIsHighlightedInInfoPopUp(loginPage.buildVersionInAboutSection), "Checkpoint[1.4/4]", "Verified build version text is highlighted from info pop up on mousehover");
		loginPage.assertTrue(loginPage.verifyIconHighlightedForBuildVersion(loginPage.versionImg), "Checkpoint[2.4/4]", "Verified build version icon is highlighted from info pop up on mousehover");

		//mousehover on icon within info pop up
		for(int i=1;i<loginPage.iconsFromInfoPopUp.size();i++)
		{
		loginPage.mouseHover(loginPage.iconsFromInfoPopUp.get(i-1));
		loginPage.assertTrue(loginPage.verifyTextIsHighlightedInInfoPopUp(loginPage.textFromInfoPopUp.get(i-1)), "Checkpoint[3."+(i-1)+"/4]", "Verified text-"+(i+1)+" is highlighted from info pop up on mousehover");
		loginPage.assertTrue(loginPage.verifyIconIsHighlightedInInfoPopUp(loginPage.iconsFromInfoPopUp.get(i-1)), "Checkpoint[4."+(i-1)+"/4]", "Verified icon-"+(i+1)+" is highlighted from info pop up on mousehover");
		}
		
		loginPage.mouseHover(loginPage.versionImg);
		loginPage.assertTrue(loginPage.verifyTextIsHighlightedInInfoPopUp(loginPage.buildVersionInAboutSection), "Checkpoint[3.4/4]", "Verified build version text is highlighted from info pop up on mousehover");
		loginPage.assertTrue(loginPage.verifyIconHighlightedForBuildVersion(loginPage.versionImg), "Checkpoint[4.4/4]", "Verified build version icon is highlighted from info pop up on mousehover");

	}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2215","Positive","F930","E2E"})
	public void test07_DR2215_TC8845_verifyBuildVersionTooltipOnMouseHover() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that correct tool tip is displayed when user mouse hovers on icon present before build version.");
		loginPage = new LoginPage(driver);
		loginPage.click(loginPage.iIconButton);
		loginPage.mouseHover(loginPage.versionIcon);
		String versionTooltip = loginPage.getAttributeValue(loginPage.versionIcon,NSGenericConstants.TITLE);
	    loginPage.assertEquals(versionTooltip,LoginPageConstants.VERSION, "Checkpoint[1/1]", "Verified 'Version' tooltip is displayed on hovering over build version icon");

	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1930","Positive","F923","E2E"})
	public void test08_US1930_TC8860_verifyEurekaThemeOnAboutPage() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on License Infos pages.");
		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.about);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertEquals(loginPage.getBackgroundColor(loginPage.aboutPage),ThemeConstants.PAGE_BACKGROUND,"Checkpoint[1/2]","Verifying the about page Theme accessed from login screen");

		loginPage.closeWindow(childWinHandles.get(0));
		loginPage.switchToWindow(parentWindow);
		loginPage.login(username, password);
		PatientListPage patientPage= new PatientListPage(driver);
		Header header = new Header(driver);
		header.viewAboutPage();
		
		childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertEquals(loginPage.getBackgroundColor(loginPage.aboutPage),ThemeConstants.PAGE_BACKGROUND,"Checkpoint[2/2]","Verifying the Eureka theme when about page accessed using header");
		
		
		
	}
	
	@Test(groups ={"Chrome","Edge","US1930","Positive","F923","E2E"})
	public void test09_US1930_TC8860_verifyDarkThemeOnAboutPage() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  theming on License Infos pages.");
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.about);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertEquals(loginPage.getBackgroundColor(loginPage.aboutPage),ThemeConstants.PAGE_BACKGROUND,"Checkpoint[1/2]","Verifying the theme is default when accessed using login screen");
		loginPage.closeWindow(childWinHandles.get(0));
		loginPage.switchToWindow(parentWindow);
		loginPage.login(username, password);
		PatientListPage patientPage= new PatientListPage(driver);
		Header header = new Header(driver);
		header.viewAboutPage();
		
		childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertEquals(loginPage.getBackgroundColor(loginPage.aboutPage),ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR,"Checkpoint[2/2]","Verifying the theme is dark when accessed using header");
		
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1930","Positive","F923","E2E"})
	public void test10_US1930_TC9012_verifyEurekaThemeOnCEPage() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify theming on CE Marking page");
		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.ceMarkingImg);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertEquals(loginPage.getBackgroundColor(loginPage.ceMarkingPage),ThemeConstants.EUREKA_POPUP_BACKGROUND,"Checkpoint[1/1]","Verifying the Eureka theme on CE page");
		
	}
	
	@Test(groups ={"Chrome","Edge","US1930","Positive","F923","E2E"})
	public void test11_US1930_TC8860_verifyDarkThemeOnCEPage() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify theming on CE Marking page");
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		loginPage = new LoginPage(driver);
		String parentWindow = loginPage.getCurrentWindowID();
		loginPage.click(loginPage.iIconButton);
		loginPage.click(loginPage.ceMarkingImg);
		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
		childWinHandles.remove(parentWindow);
		loginPage.switchToWindow(childWinHandles.get(0));
		loginPage.assertEquals(loginPage.getBackgroundColor(loginPage.ceMarkingPage),ThemeConstants.EUREKA_POPUP_BACKGROUND,"Checkpoint[1/1]","Verifying the theme remains default Eureka on CE marking page");
		
	}

	
	@AfterMethod
	public void updateTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
	}


}
