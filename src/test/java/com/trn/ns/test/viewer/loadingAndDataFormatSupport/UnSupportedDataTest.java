package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.HashMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class UnSupportedDataTest extends TestBase  {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	
	String filePath = Configurations.TEST_PROPERTIES.get("S10671CTSR_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
	String secondSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath);
	String thirdSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath);
	String fourthSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath);
	
	private String loadedTheme;
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	private ContentSelector contentSelector;
	private HelperClass helper;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}
	

	@Test(groups ={"IE11","Chrome","Edge","US624","US2228","E2E","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US624_TC3141_TC3142_US2228_TC9963_verifyWarningMessageForUnsupportedSOPClassUID(String theme) throws SQLException, InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning message when unsupported SOPClassUID is imported"
				+"</br>Verify error log entry is generated when unsupported SOPClassUID is imported. <br>"+
				"Verify the appearance and closing behavior of the view box notification");

		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName, 2);
		layout=new ViewerLayout(driver);
		
		DatabaseMethods db= new DatabaseMethods(driver);
		HashMap<String, Object> message= new HashMap<String,Object>();
		message =db.getErrorMessageWithIntance(patientName);
		
		//verify an entry is generated on Rejected Instance table for study
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify an entry is generated in Rejected Instance table while importing a study with unsupported SOPClassUID");
		message.forEach((key,value) -> 
		db.assertEquals(value.toString(), ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR, "Verify error message on importing study with unsupported SOPClassUID", "The error message for InstanceLevelID "+key+" is "+ value));		
	
		//verify an entry is made in log table for a unsupported data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify an entry is generated in log table while importing a study with unsupported SOPClassUID");
		db.assertTrue(db.verifyLogForErrorMeassge(ViewerPageConstants.LOG_TABLE_SOPCLASSUID_ERROR), "Verify an entry is made in log table with message for unsupported SOPClassUID", "Verifed an entry is generated in log table for Unsupported SOPClassUID");
	
		//change layout to 3 x3 
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		//verify that banner appear in each view box containing series with SOPClassUID as X-Ray Radiation Dose
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify that banner appear on top of viewbox-4 containing series with unsupported SOPClassUID");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotification(1)), "Verify banner appear on top of viewbox-4 containing series with unsupported SOPClassUID", "The banner appear on top of viewbox-4");
		viewerPage.assertTrue(viewerPage.getBannerMessageForViewbox(1).contains(ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR),"Verify warning message with SOPClassUID appear on banner", "The message on banner is "+viewerPage.getBannerMessageForViewbox(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotificationCloseIcon(1)), "Verify close button appear along with banner on viewbox-4 containing series with unsupported SOPClassUID", "The close button appear along with banner on viewbox-4");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify that banner appear on top of viewbox-6 containing series with unsupported SOPClassUID");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotification(3)), "Verify banner appear on top of viewbox-6 containing series with unsupported SOPClassUID", "The banner appear on top of viewbox-6");
		viewerPage.assertTrue(viewerPage.getBannerMessageForViewbox(3).contains(ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR),"Verify warning message with SOPClassUID appear on banner", "The message on banner is "+viewerPage.getBannerMessageForViewbox(3));
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotificationCloseIcon(3)), "Verify close button appear along with banner on viewbox-6 containing series with unsupported SOPClassUID", "The close button appear along with banner on viewbox-6");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify that banner appear on top of viewbox-7 containing series with unsupported SOPClassUID");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotification(5)), "Verify banner appear on top of viewbox-5 containing series with unsupported SOPClassUID", "The banner appear on top of viewbox-7");

		viewerPage.assertTrue(viewerPage.getBannerMessageForViewbox(5).contains(ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR),"Verify warning message with SOPClassUID appear on banner", "The message on banner is "+viewerPage.getBannerMessageForViewbox(5));
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotificationCloseIcon(5)), "Verify close button appear along with banner on viewbox-5 containing series with unsupported SOPClassUID", "The close button appear along with banner on viewbox-7");
	
	    //close the banner by clicking on close button and verify banner is no longer visible
		viewerPage.click(viewerPage.getViewboxNotificationCloseIcon(1));
		viewerPage.waitForTimePeriod(2000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify that banner closes on clicking close button");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getViewboxNotification(1)), "Verify banner close on clicking close button", "The banner is no longer visible on viewbox-3");
		
		helper.browserBackAndReloadViewer(patientName, 1, 2);
		viewerPage.click(viewerPage.getViewboxNotificationMessage(1));
		viewerPage.waitForTimePeriod(2000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify that banner closes on clicking close button");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getViewboxNotification(1)), "Verify banner close on clicking viewbox message", "The banner is no longer visible on viewbox-3");
	
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US624","Sanity"})
	public void test02_US624_TC3143_TC3144_TC3145_TC3162_verifyContentSelectorForUnsupportedSOPClassUID() throws SQLException, InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector for unsupported SOPClassUID entry"
				+"</br>Verify user is able to select unsupported SOPClassUID entry from content selector into non-empty viewbox containing valid DICOM / pdf/jpeg"
				+"</br>Verify user is able to select valid series from content selector into viewbox containing unsupported SOPClassUID entry"
				+"</br>Verify user is able to select unsupported SOPClassUID entry from content selector into empty viewbox");
		
		helper=new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName, 1, 2);
		
		viewerPage.waitForViewerpageToLoad(2);
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
	
		//verify warning icon on content selector for unsupported series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify warning icon for a series with unsupported SOPClassUID on content selector");
		viewerPage.assertTrue(contentSelector.verifyWarningIconForUnsupportedSeriesInSeriesTab(firstSeriesDescription), "Verify warning icon for a series with description "+firstSeriesDescription, "The warning icon is present on content selector for series");
		viewerPage.assertFalse(contentSelector.verifyWarningIconForUnsupportedSeriesInSeriesTab(secondSeriesDescription), "Verify warning icon for a series with description "+secondSeriesDescription, "The warning icon is present on content selector for series");
		viewerPage.assertTrue(contentSelector.verifyWarningIconForUnsupportedSeriesInSeriesTab(thirdSeriesDescription), "Verify warning icon for a series with description "+thirdSeriesDescription, "The warning icon is present on content selector for series");
		viewerPage.assertFalse(contentSelector.verifyWarningIconForUnsupportedSeriesInSeriesTab(fourthSeriesDescription), "Verify warning icon for a series with description "+fourthSeriesDescription, "The warning icon is present on content selector for series");
	
	    //verify error message on hovering over warning symbol
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify SOPClassUID error message on hovering over warning icon on content selector");
		viewerPage.assertEquals(contentSelector.getWarningMessageForUnsupportedSeriesInSeriesTab(firstSeriesDescription), ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR, "Verify warning message with SOPClassUID appear on mouse hover on warning icon on content selector", "The warning message on hover over icon"+ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR);
		viewerPage.assertEquals(contentSelector.getWarningMessageForUnsupportedSeriesInSeriesTab(thirdSeriesDescription), ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR, "Verify warning message with SOPClassUID appear on mouse hover on warning icon on content selector", "The warning message on hover over icon"+ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR);
	
		//change the layout to 3x3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
	    //select series with unsupported SOP ClassUID from content selector on viewbox1 containing a valid DICOM series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify banner appear on viewbox on selecting unsupported SOPClassUID series from content selector");
		contentSelector.selectSeriesFromSeriesTab(2, firstSeriesDescription);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotification(2)),"Verify banner appear on viewbox on selecting unsupported SOPClassUID series from content selector","The banner appear on top of viewbox");
		viewerPage.assertEquals(viewerPage.getBannerMessageForViewbox(2),ViewerPageConstants.SOPCLASSUID_ERROR,"Verify warning message with SOPClassUID appear on banner", "The message on banner is "+viewerPage.getBannerMessageForViewbox(2));
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotificationCloseIcon(2)), "Verify close button appear along with banner on viewbox-1 containing series with unsupported SOPClassUID", "The close button appear along with banner on viewbox-1");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.svgImage(1)), "Verify no image loaded on viewer", "Empty viewbox loaded with warning banner");
		
		//select series with unsupported SOP ClassUID from content selector on empty view box
		contentSelector.selectSeriesFromSeriesTab(9, firstSeriesDescription);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotification(9)),"Verify banner appear on viewbox on selecting unsupported SOPClassUID series from content selector","The banner appear on top of viewbox");
		viewerPage.assertEquals(viewerPage.getBannerMessageForViewbox(9),ViewerPageConstants.SOPCLASSUID_ERROR,"Verify warning message with SOPClassUID appear on banner", "The message on banner is "+viewerPage.getBannerMessageForViewbox(9));
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getViewboxNotificationCloseIcon(9)), "Verify close button appear along with banner on viewbox-1 containing series with unsupported SOPClassUID", "The close button appear along with banner on viewbox-9");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.svgImage(9)), "Verify no image loaded on viewer", "Empty viewbox loaded with warning banner");
		
		//change the layout to 2x2
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		//select series with valid DICOM on view box containing series with unsupported SOP ClassUID
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify valid DICOM series is rendered on UI after selection from content selector on viewbox containing unsupported SOPClassUID series");
		viewerPage.click(viewerPage.getViewboxNotification(2));
		contentSelector.selectSeriesFromSeriesTab(2, secondSeriesDescription);
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getViewboxNotification(2)),"Verify banner appear on viewbox on selecting unsupported SOPClassUID series from content selector","The banner appear on top of viewbox");
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getViewboxNotificationCloseIcon(2)), "Verify close button appear along with banner on viewbox-4 containing series with unsupported SOPClassUID", "The close button appear along with banner on viewbox-4");
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getPatientName(2)), "Verify no image loaded on viewer", "Empty viewbox loaded with warning banner");
	}
	
	@Test(groups={"firefox","Chrome","IE11","Edge","US2228","Positive","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_US2228_TC9943_TC9945_TC9995_verifyThemeForWarningPopup(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new UI for view box warning notification. <br>"+
		"Verify that the new notification UI is supported on dark theme as well.<br>"+
		"Verify that new warning notification for view boxes does not go out of the view box on window resize.");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer");
		helper = new HelperClass(driver);
		viewerPage=helper.loadViewerDirectly(patientName, 2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify notification pop up on viewbox-1.");
		
		PagesTheme pagetheme=new PagesTheme(driver);
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.getViewboxNotification(1),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[1/10]", "Verified Theme for viewbox notification for viewbox-1.");
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.WARNING_TITLE, ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR), "Checkpoint[2/10]", "Verified notification Pop up.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify notification pop up on viewbox-1 when Content selector is open.");
		ContentSelector cs=new ContentSelector(driver);
		cs.openAndCloseSeriesTab(true);
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.WARNING_TITLE, ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR), "Checkpoint[3/10]", "Verified viewbox notification for viewbox-1 when content selector is open.");
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.getViewboxNotification(3),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[4/10]", "Verified Theme for viewbox notification for viewbox-1 when content selector is open.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify notification pop up on viewbox-1 when viewbox is re-size.");
		int height = viewerPage.getHeightOfWebElement(viewerPage.getViewboxNotification(1));
		int width = viewerPage.getWidthOfWebElement(viewerPage.getViewboxNotification(1));
		
		patientPage.resizeBrowserWindow(700, 700);
		
		patientPage.assertTrue(height<viewerPage.getHeightOfWebElement(viewerPage.getViewboxNotification(1)),"Checkpoint[5/10]","verifying the notification height on resize");
		patientPage.assertTrue(width>viewerPage.getWidthOfWebElement(viewerPage.getViewboxNotification(1)),"Checkpoint[6/10]","verifying the notification width on resize");
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.WARNING_TITLE, ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR), "Checkpoint[7/10]", "Verified viewbox notification for viewbox-1 when browser is resize.");
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.getViewboxNotification(3),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[8/10]", "Verified Theme for viewbox notification for viewbox-1 when browser is resize.");
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify notification pop up on viewbox-1 when Content selector is closed.");
		cs.openAndCloseSeriesTab(false);
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.WARNING_TITLE, ViewerPageConstants.REJCTED_INSTANCE_SOPCLASSUID_ERROR), "Checkpoint[9/10]", "Verified viewbox notification for viewbox-1 when content selector is closed.");
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.getViewboxNotification(3),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[10/10]", "Verified Theme for viewbox notification for viewbox-1 when content selector is closed.");
	
	}

	@Test(groups={"firefox","Chrome","IE11","Edge","US2228","Positive","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test04_US2228_TC10000_verifyThemeForViewerAndViewboxNotification(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the viewer notification is displayed in front of the view box notification.");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;

			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);

			patientPage = new PatientListPage(driver);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage.searchPatient(patientName, "", "", "");
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();				
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Save layout and verify Viewbox and viewer notification." );
		
		PagesTheme pagetheme=new PagesTheme(driver);
		layout=new ViewerLayout(driver);
		layout.doubleClick(layout.getViewPort(1));
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/8]", "Verified notification message after selecting save layout only.");
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[2/8]", "Verified Theme for viewer notification when layout is 1*1.");  
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.getViewboxNotification(1),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[3/8]", "Verified Theme for viewbox notification for viewbox-1.");  
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Save and reser layout and verify Viewbox and viewer notification." );
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_RESET + machineName), "Checkpoint[4/8]", "Verified notification message for reset layout.");
		viewerPage.assertTrue(viewerPage.getText(viewerPage.notificationMessage.get(1)).equalsIgnoreCase(ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[5/8]", "Verified notification message for save layout.");
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[6/8]", "Verified Theme for viewer notification of Reset layout.");  
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.notificationTiles.get(1),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[7/8]", "Verified Theme for viewer notification of save layout");  
		pagetheme.assertTrue(pagetheme.verifyThemeForNotification(viewerPage.getViewboxNotification(1),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[8/8]", "Verified Theme for viewbox notification for viewbox-1.");  
		
	}

	
}
