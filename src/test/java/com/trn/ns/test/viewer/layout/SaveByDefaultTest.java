package com.trn.ns.test.viewer.layout;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)

public class SaveByDefaultTest extends TestBase{

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private HelperClass helper;
	private ViewerLayout layout;
	private PMAP pmap;
	private ViewerSliderAndFindingMenu findingMenu;
	
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneAgePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);	
	
	String filePath2 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
	
	String filePath3 = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String AiDocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	
	String filePath4 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String MultiSeriesPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	String firstSeries= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath4);
	
	String filepath5= Configurations.TEST_PROPERTIES.get("Picline_filepath");
	String piccliineData= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath5);
	
	String filepath6= Configurations.TEST_PROPERTIES.get("Head_CT_filepath");
	String infervisonData= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath6);

	String firstSeriesDescritpionsInfervision= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filepath6);
	String secondSeriesDescritpionsInfervision= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filepath6);
	
	String filepath7 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath7);
	
	String filepath8 = Configurations.TEST_PROPERTIES.get("VIDA_LCS_COPD_Filepath");
	String VIDA_LCS_COPD_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath8);
	
	String filepath9 = Configurations.TEST_PROPERTIES.get("VIDA_Emphysema_ILD_Filepath");
	String VIDA_Emphysema_ILD_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath9);
	
	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);
	
	String pmapFilePath = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmapFilePath);
	
	String cadFilePath = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cadFilePath);
	

	private ContentSelector contentSelector;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private String loadedTheme="";
	
	String username_1 = "user_1";
	private DICOMRT drt;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password );	
	}

	@Test(groups ={"Chrome","Edge","IE11","US764"})
	public void test02_US764_TC2890_US764_TC2891_MultipleLayoutChange_FromDefaultLayout() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Refactor initial layout and change layout code in client/server-Verify layout change on different data series"+"<br>Refactor initial layout and change layout code in client/server- Verify with different loading series");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Loading the Patient "+infervisonData+" in viewer and verifying the series and default layout" );

		//Step1 Infervision data layout and series loaded on empty viewbox

		//Step 1.1 Loading the Infervision data and observing the default layout.
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerPageUsingSearch(infervisonData, 1, 1);
		contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*1");
		//Step1.2 Change to Layout to 2*2, 3*3 from 2*1

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");

		//Step1.3 Select series from content selector on empty viewbox
		contentSelector.selectSeriesFromSeriesTab(4,firstSeriesDescritpionsInfervision);
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(firstSeriesDescritpionsInfervision), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");

		//Step2  Navigating back and loading the AH.4^PDF data

		//Step2.1 Verifying that Ah.4 data is getting loaded in 1*1L-2*1R default
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(ah4PatientName, 1, 1);
	
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 1L*2*1R");

		//Step2.2 Changing layout to 1*1 and Changing layout to 2*1
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 1*1");

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*1");

		//Step2.3 OneUp and One down
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 1*1");
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*1");

		//Step3  Navigating back and loading the BoneAge data

		//Step3.1 Verifying that BoneAge data is getting loaded in 2*2 default
		helper.browserBackAndReloadViewer(boneAgePatient, 1, 1);
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");
		//Step3.2 Changing layout to 3*3
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 3*3");

		//Step4  Navigating back and loading the PicCLine data

		//Step4.1 Verifying that PicCLine data is getting loaded in 2*2 default
		helper.browserBackAndReloadViewer(piccliineData, 1, 2);
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*1");
		//Step4.2 Changing layout to 2*2 and Changing layout to 3*3
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 3*3");

		//Step5  Navigating back and loading the MultiSeries data

		//Step5.1 Verifying thatMultiSeries data is getting loaded in 2*2 default
		helper.browserBackAndReloadViewer(MultiSeriesPatientName, 1, 1);

		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*1");
		//Step5.2 Changing layout to 1*1 and Changing layout to 3*3, again Changing layout to 2*2
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 1*1");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Verifying the layout type of the viewer", "Viewer is loaded in 3*3");
	}

	//US1502: Save custom layout per machine for a user
	@Test(groups ={"Chrome","Edge","US1502","US2145","US2227","Positive","BVT","E2E","F1081","F1126"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_US1502_TC7949_TC7951_TC7952_US2145_TC9533_US2227_TC9728_verifyOptionsInLayoutSelector(String theme) throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Save layout with content, Save layout only and Reset to default layout buttons are displayed in layout selector. <br>"+
		"Verify that layout selector is closed after clicking the saveLayout button. <br>"+
		"Verify the notification message after layout is saved.<br>"+
		"Verify Layout menu display. <br>"+
		"Verify that notification 'Layout was saved for user scan for machine XXXX' appearing as per the new design of Eureka theme.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector.");
		
		patientPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username,password);
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
        
	    helper = new HelperClass(driver);
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(MultiSeriesPatientName);
	
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
				
		patientPage.clickOntheFirstStudy();				
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		layout=new ViewerLayout(driver);
				
		//verify new option in layout container 
		layout.openLayoutContainer();
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.saveLayoutWithContent), "Checkpoint[1/9]", "Verified new option to save layout with content.");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.saveLayoutOnly), "Checkpoint[2/9]", "Verified new option to save layout only.");
		viewerpage.assertTrue(viewerpage.isElementPresent(layout.resetToDefaultLayout), "Checkpoint[3/9]", "Verified new option to reset layout to default.");
		viewerpage.click(viewerpage.getViewPort(1));
		
		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[4/9]", "Verified banner message after selecting save layout with content.");
		PagesTheme pagetheme=new PagesTheme(driver);
		viewerpage.assertTrue(pagetheme.verifyThemeForNotification(viewerpage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[5/9]", "Verified theme for save layout with content");
		viewerpage.assertFalse(viewerpage.isElementPresent(layout.layoutContainer), "Checkpoint[6/9]", "Verified that layout container closed after selecting save layout with content.");
		viewerpage.waitForElementInVisibility(viewerpage.notificationDiv);

		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		//verify banner message after select of save layout only 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS,ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[7/9]", "Verified banner message after selecting save layout only.");
		viewerpage.assertTrue(pagetheme.verifyThemeForNotification(viewerpage.notificationTiles.get(0), ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[8/9]", "Verified theme for save layout with content");
		viewerpage.assertFalse(viewerpage.isElementPresent(layout.layoutContainer), "Checkpoint[9/9]", "Verified that layout container closed after selecting save layout only.");

		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1502","Negative","BVT"})
	public void test04_US1502_TC7954_US2145_TC9535_verifySaveCustomLayoutWhenDifferentBatchloaded() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that save with content is not allowed when different batch data are displayed on a viewer.<br>"+
		"Verify tool tips in layout menu.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymous_Patient+" in viewer and verifying options in layout selector." );

		helper=new HelperClass(driver);			
		viewerpage = helper.loadViewerPageUsingSearch(anonymous_Patient, 1, 2);

		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		//verify change layout so that different batch loaded on viewer
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		layout.waitForAllImagesToLoad();
	    viewerpage.assertTrue(viewerpage.verifyViewboxNotificationForConflicts(3), "Checkpoint[1/2]", "Verified that different batch machine loaded on viewer.");
	    viewerpage.assertTrue(viewerpage.verifyViewboxNotificationForConflicts(4), "Checkpoint[1/2]", "Verified that different batch machine loaded on viewer.");
	    
	    layout.openLayoutContainer();
	    ViewerToolbar tool=new ViewerToolbar(driver);
	    viewerpage.assertEquals(tool.getTooltip(layout.saveLayoutWithContent), ViewerPageConstants.TOOLTIP_WHEN_DIFFERENT_BATCH_LOADED, "Checkpoint[2/2]", "Verified tooltip on save layout option when different batch loaded."); 
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1502","US2145","Positive","BVT","E2E","F1081"})
	public void test05_US1502_TC7972_TC7980_US2145_TC9534_verifySaveLayoutWithContentFunctionality() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to save a layout with contents. <br>"+
		"Verify that user B is unable to see the layout saved by user A. <br>"+
		"Verify Layout menu functionalists.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector." );

		helper=new HelperClass(driver);			
		viewerpage = helper.loadViewerPageUsingSearch(MultiSeriesPatientName, 1, 2);
		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		int viewbox=viewerpage.getNumberOfCanvasForLayout();
		//verify change layout so that different batch loaded on viewer
		layout.selectLayout(layout.threeByThreeLayoutIcon);
	    cs=new ContentSelector(driver);
	    cs.selectSeriesFromSeriesTab(5, firstSeries);
	    viewerpage.assertNotEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[1/8]","Verified change layout on viewer.");
	    layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
	   
	    viewbox=viewerpage.getNumberOfCanvasForLayout();
	    helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(MultiSeriesPatientName, 1, 2);
        findingMenu=new ViewerSliderAndFindingMenu(driver);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[2/8]","Verified that layout remain saved after reloading of viewer page.");
		viewerpage.assertFalse(findingMenu.getAllGSPSObjects(1).isEmpty(), "Checkpoint[3/8]", "Verified that result series is loaded on viewbox1");
		viewerpage.assertFalse(findingMenu.getAllGSPSObjects(2).isEmpty(), "Checkpoint[4/8]", "Verified that result series is loaded on viewbox2");
		viewerpage.assertTrue(findingMenu.getAllGSPSObjects(3).isEmpty(), "Checkpoint[5/8]", "Verified that source series is loaded on viewbox3");
		viewerpage.assertTrue(findingMenu.getAllGSPSObjects(4).isEmpty(), "Checkpoint[6/8]", "Verified that source series is loaded on viewbox4");
		viewerpage.assertTrue(findingMenu.getAllGSPSObjects(5).isEmpty(), "Checkpoint[7/8]", "Verified that source series is loaded on viewbox5");
		
		//create new user and validate layout for that new user
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    RegisterUserPage registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying it : " + username);
		registerUserPage.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
	
		loginPage=new LoginPage(driver);
		loginPage.logout();
		loginPage.navigateToBaseURL();
		
		//verifying new user
		loginPage.login(username_1, username_1);
		patientPage=new PatientListPage(driver);
		patientPage.clickOnPatientRow(MultiSeriesPatientName);
		patientPage.clickOntheFirstStudy();			
		viewerpage.waitForViewerpageToLoad(2);
	    viewerpage.assertNotEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[8/8]","Verified that layout is not save for "+username_1);
		
		
	   
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1502","Positive","BVT"})
	public void test06_US1502_TC8003_verifySaveLayoutWithEmptyViewbox() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to save the layout with multiple empty view boxes.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(MultiSeriesPatientName);
	
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();				
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(2);
		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		int viewbox=viewerpage.getNumberOfCanvasForLayout();
		//verify change layout so that different batch loaded on viewer
		layout.selectLayout(layout.threeByThreeLayoutIcon);
	    viewerpage.assertNotEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[1/3]","Verified that viewer page loaded with new layout.");
	    layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
	    viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[2/3]", "Verified banner message after saving empty viewbox.");
	   
	    viewbox=viewerpage.getNumberOfCanvasForLayout();
	    helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(MultiSeriesPatientName, 1, 2);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[3/3]","Verified that viewer page loaded with the save layout after reload of viewer page.");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1502","US2145","Positive","BVT","E2E","F1081"})
	public void test07_US1502_TC7981_US2145_TC9534_verifySaveLayoutFunctionalityForNonEmptyViewbox() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to save the updated layout for the study.<br>"+
		"Verify Layout menu functionalists.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+MultiSeriesPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(MultiSeriesPatientName,"","","");		
		String machineName=patientPage.getAllMachinesFromTooltip(1).get(0);
		patientPage.clickOntheFirstStudy();			
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(2);
		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		int viewbox=viewerpage.getNumberOfCanvasForLayout();
		//verify change layout and the click on save layout only for non-empty viewbox
		layout.selectLayout(layout.twoByTwoLayoutIcon);
	    viewerpage.assertNotEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[1/5]","Verified that viewer page loaded with new layout.");
	    layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
	    viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[2/5]", "Verified banner message after selecting save layout only.");
	   
	    viewbox=viewerpage.getNumberOfCanvasForLayout();
	    helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(MultiSeriesPatientName, 1, 2);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[3/5]","Verified that viewer page loaded with new layout after reload");
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_RESET + machineName),"Checkpoint[4/5]", "Verified banner message after selecting reset to default layout.");
		viewerpage.waitForTimePeriod(2000);
		viewerpage.assertNotEquals(viewerpage.getNumberOfCanvasForLayout(),viewbox,"Checkpoint[5/5]","Verified that viewer loaded in default view.");
		
		
	}
	
	//US1504: Reset custom layout per machine for a user
	@Test(groups ={"Chrome","Edge","IE11","US1504","Positive"})
	public void test08_US1504_TC8120_verifyOptionsInLayoutSelector() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify layout reset for machine A, should reset layout for all studies of same machine for same user (Reset - Save layout only option)");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(VIDA_LCS_COPD_Patient);
	
		String machineName=patientPage.getText(patientPage.getAllMachineNameFromTooltip(1).get(0));
		patientPage.clickOntheFirstStudy();		
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		int defaultLayout=viewerpage.getNumberOfCanvasForLayout();
		//change layout to 3*3 
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/6]", "Verified banner message after selecting save layout only option.");
		int afterSaveLayout=viewerpage.getNumberOfCanvasForLayout();
		viewerpage.assertNotEquals(afterSaveLayout, defaultLayout, "Checkpoint[2/6]", "Verified layout is change for patient "+VIDA_LCS_COPD_Patient+" having same machine.");
		
        //navigate back and select another patient from same machine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_Emphysema_ILD_Patient+" in viewer and verifying options in layout selector." );
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(VIDA_Emphysema_ILD_Patient, 1, 1);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), afterSaveLayout, "Checkpoint[3/6]", "Verified layout is change for patient "+VIDA_Emphysema_ILD_Patient+" having same machine.");
		
		//verify banner message after select of reset layout  
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_RESET + machineName),"Checkpoint[4/6]", "Verified banner message after selecting reset layout.");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), defaultLayout, "Checkpoint[5/6]", "Verified layout is reset for patient "+VIDA_Emphysema_ILD_Patient);

		//navigate back and select another patient from same machine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer and verifying options in layout selector." );
		helper.browserBackAndReloadViewer(VIDA_LCS_COPD_Patient, 1, 1);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), defaultLayout, "Checkpoint[6/6]", "Verified layout is reset for patient "+VIDA_LCS_COPD_Patient+"having same machine.");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1504","Positive"})
	public void test09_US1504_TC8121_verifySaveLayoutForAllStudiesWithSameMachine() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify layout reset for machine A, should reset layout for all studies of same machine for same user (Reset 'Save layout with content' option)");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(VIDA_LCS_COPD_Patient,"","","");
	
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
				
		patientPage.clickOntheFirstStudy();					
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		String seriesName1=viewerpage.getSeriesDescriptionOverlayText(1);
		String seriesName2=viewerpage.getSeriesDescriptionOverlayText(2);
		String seriesName3=viewerpage.getSeriesDescriptionOverlayText(3);
	
		int defaultLayout=viewerpage.getNumberOfCanvasForLayout();
		
		//change layout
		layout.selectLayout(layout.oneByOneLayoutIcon);
		
		cs=new ContentSelector(driver);
		//select result from CS which is not already loaded on viewer
		cs.selectResultFromSeriesTab(1, seriesName2);
		
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/9]", "Verified banner message after selecting save layout with content.");
		int afterSaveLayout=viewerpage.getNumberOfCanvasForLayout();
		viewerpage.assertNotEquals(afterSaveLayout, defaultLayout, "Checkpoint[2/]", "Verified layout is change for patient "+VIDA_LCS_COPD_Patient);
		
        //navigate back and select another patient from same machine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_Emphysema_ILD_Patient+" in viewer and verifying options in layout selector." );
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(VIDA_Emphysema_ILD_Patient, 1, 1);
	
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), afterSaveLayout, "Checkpoint[3/9]", "Verified layout is change for patient "+VIDA_Emphysema_ILD_Patient+" having same machine");
		viewerpage.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName2), "Checkpoint[4/9]", "Verified that layout is save with content for "+VIDA_Emphysema_ILD_Patient+" havinge same machine");
		
		//verify banner message after select of reset layout  
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_RESET + machineName), "Checkpoint[5/9]", "Verified banner message after selecting reset layout.");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), defaultLayout, "", "");

		//navigate back and select another patient from same machine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer and verifying options in layout selector." );
		helper.browserBackAndReloadViewer(VIDA_LCS_COPD_Patient, 1, 1);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), defaultLayout, "Checkpoint[6/9]","Verified layout is reset for patient "+VIDA_LCS_COPD_Patient+"having same machine");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1), seriesName1, "Checkpoint[7/9]", "Verified series name in first viewbox after reset layout");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2), seriesName2,"Checkpoint[8/9]", "Verified series name in second viewbox after reset layout");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(3), seriesName3, "Checkpoint[9/9]", "Verified series name in third viewbox after reset layout");
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","US1504","Positive"})
	public void test10_US1504_TC8122_verifyResetLayoutForMultipleUsers() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify layout reset by user A for machine A should not reset layout for user B  for same machine.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(VIDA_LCS_COPD_Patient);
		
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
				
		patientPage.clickOntheFirstStudy();					
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(1);
		db=new DatabaseMethods(driver);
		layout=new ViewerLayout(driver);
		
		String seriesName1=viewerpage.getSeriesDescriptionOverlayText(1);
		String seriesName2=viewerpage.getSeriesDescriptionOverlayText(2);
		String seriesName3=viewerpage.getSeriesDescriptionOverlayText(3);
	
		int defaultLayout=viewerpage.getNumberOfCanvasForLayout();
		
		//verify new option in layout container 
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		//select series in empty viewbox and then save layout with content for scan user.
		cs=new ContentSelector(driver);
		cs.selectResultFromSeriesTab(4, seriesName2);
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/17]", "Verified banner message after selecting save layout with content.");
		int afterSaveLayout=viewerpage.getNumberOfCanvasForLayout();
		viewerpage.assertNotEquals(afterSaveLayout, defaultLayout, "Checkpoint[2/17]", "Verified layout is not reset for patient "+VIDA_LCS_COPD_Patient+"having same machine login with scan user.");
		
		//create new user and login with that user.
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		RegisterUserPage registerUserPage = new RegisterUserPage(driver);
		registerUserPage.waitForRegisterPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying it : " + username);
		registerUserPage.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		
		loginPage.logout();
		loginPage.navigateToBaseURL();
		
		//verifying new user
		loginPage.login(username_1, username_1);
		patientPage.waitForPatientPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer using user_1 user. " );
		patientPage.clickOnPatientRow(VIDA_Emphysema_ILD_Patient);		
		patientPage.clickOntheFirstStudy();				
		viewerpage.waitForViewerpageToLoad(1);
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), defaultLayout, "Checkpoint[3/17]", "Verified layout is not save for patient "+VIDA_Emphysema_ILD_Patient+"having same machine when login with user_1 user.");
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		int changeLayout=viewerpage.getNumberOfCanvasForLayout();
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[4/17]", "Verified banner message after selecting save layout only.");
		viewerpage.assertNotEquals(changeLayout, defaultLayout, "Checkpoint[5/17]", "Verified layout is change for patient "+VIDA_Emphysema_ILD_Patient+"having same machine when login with user_1 user.");
		viewerpage.closeNotification();
		
		//verify banner message after select of reset layout  
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.RESET_LAYOUT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_RESET + machineName), "Checkpoint[6/17]", "Verified banner message after selecting reset layout.");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), defaultLayout, "Checkpoint[7/17]", "Verified layout is reset for patient "+VIDA_Emphysema_ILD_Patient+"having same machine login with scan user.");
		viewerpage.waitForElementInVisibility(viewerpage.notificationDiv);
		loginPage.logout();
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_LCS_COPD_Patient+" in viewer using scan user." );
		loginPage.login(username, username);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(VIDA_LCS_COPD_Patient);		
		patientPage.clickOntheFirstStudy();		
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), afterSaveLayout, "Checkpoint[8/17]","Verified layout is not reset for patient "+VIDA_LCS_COPD_Patient+"having same machine login with scan user.");
	
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), afterSaveLayout, "Checkpoint[9/17]","Verified layout is reset for patient "+VIDA_LCS_COPD_Patient+"having same machine");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1), seriesName1, "Checkpoint[10/17]", "Verified series name in first viewbox after reset layout");;
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2), seriesName2, "Checkpoint[11/17]", "Verified series name in first viewbox after reset layout");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(3), seriesName3, "Checkpoint[12/17]", "Verified series name in first viewbox after reset layout");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(4), seriesName2, "Checkpoint[13/17]", "Verified series name in first viewbox after reset layout");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+VIDA_Emphysema_ILD_Patient+" in viewer using scan user." );
        helper=new HelperClass(driver);
        helper.browserBackAndReloadViewer(VIDA_Emphysema_ILD_Patient, 1, 1);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), afterSaveLayout, "Checkpoint[14/17]","Verified layout is not reset for patient "+VIDA_Emphysema_ILD_Patient+"having same machine login with scan user.");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1), seriesName1, "Checkpoint[15/17]", "Verified series name in first viewbox after reset layout");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2), seriesName2, "Checkpoint[16/17]", "Verified series name in second viewbox after reset layout");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(4), seriesName2, "Checkpoint[17/17]", "Verified series name in fourth viewbox after reset layout");
		
	}

	@Test(groups ={"Chrome","Edge","IE11","DE2026","Positive"})
	public void test11_DE2026_TC8259_verifyUpdatedLayoutForRT() throws InterruptedException, SQLException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to reload the updated layout and data for RT patient data");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameTCGA+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(patientNameTCGA,"","","");
				
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineDesc=patientPage.getText(patientPage.machineNameOnEurekaAl);
		db = new DatabaseMethods(driver);
		
		String machineName=db.getMachineName(machineDesc).get(0);
		patientPage.clickOntheFirstStudy();
		
		drt = new DICOMRT(driver);
		drt.waitForDICOMRTToLoad();
		cs = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		
		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs.selectResultFromSeriesTab(3, cs.getAllResults().get(0));
		
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
		drt.assertTrue(drt.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/5]", "Verified banner message after selecting save layout with content.");
		drt.assertFalse(drt.isElementPresent(layout.layoutContainer), "Checkpoint[2/5]", "Verified that layout container closed after selecting save layout with content.");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameTCGA, 1, 1);
		
		drt.assertEquals(drt.getNumberOfCanvasForLayout(), 4, "Checkpoint[3/5]", "Verifying layout is 2x2");
		drt.assertEquals(drt.getSeriesDescriptionOverlayText(3), drt.getSeriesDescriptionOverlayText(1), "Checkpoint[4/5]", "verifying that loaded series is displayed on viewer reload");
		drt.assertEquals(drt.getLegendOptions(1),drt.getLegendOptions(3), "Checkpoint[5/5]", "verifying the legend is displayed");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE2026","Positive"})
	public void test12_DE2026_TC8259_verifyUpdatedLayoutForPMAP() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to reload the updated layout and data for RT patient data");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientUsingID(pmapPatientName);
		
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();			
		viewerpage = new ViewerPage(driver);
		
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);	
		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);
		
		cs.closeNotification();
		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs.selectResultFromSeriesTab(3, cs.getAllResults().get(0));
		cs.closingConflictMsg();
		
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/9]", "Verified banner message after selecting save layout with content.");
		viewerpage.assertFalse(viewerpage.isElementPresent(layout.layoutContainer), "Checkpoint[2/9]", "Verified that layout container closed after selecting save layout with content.");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer("", pmapPatientName, 1, 1);
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "Checkpoint[3/9]", "Verifying layout is 2x2");
		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(3), viewerpage.getSeriesDescriptionOverlayText(1), "Checkpoint[4/9]", "verifying that loaded series is displayed on viewer reload");
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[5/9]", "verifying the LUT bar is displayed in viewbox 1");
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(2)), "Checkpoint[6/9]", "verifying the LUT bar is displayed in viewbox 2");
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(3)), "Checkpoint[7/9]", "verifying the LUT bar is displayed in viewbox 3");
		
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultAppliedAnnotation(1)), "Checkpoint[8/9]", "verifying the result appiled text is displayed in viewbox1");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultAppliedAnnotation(3)), "Checkpoint[9/9]", "verifying the result appiled text is displayed in viewbox2");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE2026","Positive"})
	public void test13_DE2026_TC8260_verifyLayoutForPMAP() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to reload saved layout for PMAP patient data");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientUsingID(pmapPatientName);
		
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.closeNotification();
	
		cs = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);
		
		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs.selectResultFromSeriesTab(3, cs.getAllResults().get(0));
		
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/8]", "Verified banner message after selecting save layout with content.");
		viewerpage.assertFalse(viewerpage.isElementPresent(layout.layoutContainer), "Checkpoint[2/8]", "Verified that layout container closed after selecting save layout with content.");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer("", pmapPatientName, 1, 1);
		viewerpage.closeNotification();
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 9, "Checkpoint[3/8]", "Verifying layout is 3x3");
		
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[4/8]", "verifying the LUT bar is displayed in viewbox 1");
		for(int i =2;i<9;i++) 
			viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(i)), "Checkpoint[5/8]", "verifying the LUT bar is not displayed in viewbox "+i);
			
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultAppliedAnnotation(1)), "Checkpoint[6/8]", "verifying the result appiled text is displayed in viewbox1");
		for(int i =2,j=3;i<9;i++,j++) {
			viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getResultAppliedElement(i)), "Checkpoint[7/8]", "verifying the result appiled text is not displayed in viewbox "+i);
			viewerpage.assertFalse(layout.verifyNoContentSpecifiedIsDisplayed(j), "Checkpoint[8/8]", "verifying the empty viewbox is displayed in viewbox-"+j);
		}
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE2026","Positive"})
	public void test14_DE2026_TC8269_verifyLayoutForPMAPWithSyncOption() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PMAP viewer sync is working fine after the layout change.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientUsingID(pmapPatientName);
		
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		String machineName=patientPage.getText(patientPage.machineNameOnEurekaAl);
		patientPage.clickOntheFirstStudy();			
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.closeNotification();
		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);
		cs = new ContentSelector(driver);
		
		List<String> results = cs.getAllResults();
		List<String> source = cs.getAllSeries();
						
		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.closingConflictMsg();
		
		cs.selectResultFromSeriesTab(2,results.get(0));
		cs.selectResultFromSeriesTab(3,results.get(0));
		
		cs.selectSeriesFromSeriesTab(1, source.get(0));
		cs.selectSeriesFromSeriesTab(4, source.get(0));
		
		cs.openAndCloseSeriesTab(false);
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/13]", "Verified banner message after selecting save layout with content.");
		viewerpage.assertFalse(viewerpage.isElementPresent(layout.layoutContainer), "Checkpoint[2/13]", "Verified that layout container closed after selecting save layout with content.");

		viewerpage.scrollDownToSliceUsingKeyboard(1, 3);
		
		for(int i =2;i<4;i++) 
			viewerpage.assertEquals(viewerpage.getCurrentScrollPosition(1),viewerpage.getCurrentScrollPosition(i), "Checkpoint[3/13]", "verifying sync is working fine");
		
		
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer("", pmapPatientName, 1, 1);
		viewerpage.closeNotification();
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "Checkpoint[4/13]", "Verifying layout is 2x2");
		
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(2)), "Checkpoint[5/13]", "verifying the LUT bar is displayed in viewbox 2");
		viewerpage.assertTrue(viewerpage.isElementPresent(pmap.getGradientBar(3)), "Checkpoint[6/13]", "verifying the LUT bar is displayed in viewbox 3");
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(1)), "Checkpoint[7/13]", "verifying the LUT bar is not displayed in viewbox 1");
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientBar(4)), "Checkpoint[8/13]", "verifying the LUT bar is not displayed in viewbox 4");
			
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultAppliedAnnotation(2)), "Checkpoint[9/13]", "verifying the result appiled text is displayed in viewbox 2");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getResultAppliedAnnotation(3)), "Checkpoint[10/13]", "verifying the result appiled text is displayed in viewbox 3");
		
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getResultAppliedElement(1)), "Checkpoint[11/13]", "verifying the result appiled text is not displayed in viewbox 1");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getResultAppliedElement(4)), "Checkpoint[12/13]", "verifying the result appiled text is not displayed in viewbox 4");
		
		viewerpage.scrollDownToSliceUsingKeyboard(1, 3);
		
		for(int i =2;i<4;i++) 
			viewerpage.assertEquals(viewerpage.getCurrentScrollPosition(1),viewerpage.getCurrentScrollPosition(i), "Checkpoint[13/13]", "verifying the sync is working on reload with content");
		
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE2026","Positive"})
	public void test15_DE2026_TC8270_verifyLayoutForCAD() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to reload saved layout for CAD patient data");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer and verifying options in layout selector." );

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(IHEMammoTestPatientName,"","","");
		
		patientPage.mouseHover(patientPage.getEurekaAIIcon(1));
		patientPage.waitForElementVisibility(patientPage.toolTip);
		db = new DatabaseMethods(driver);
		String machineDesc = patientPage.getText(patientPage.machineNameOnEurekaAl);
		String machineName=db.getMachineName(machineDesc).get(0);
		
		patientPage.clickOntheFirstStudy();;			
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		layout=new ViewerLayout(driver);
		
		//verify closing of layout selector by clicking on save .
		layout.selectLayout(layout.twoByTwoLayoutIcon);
	
		//verify banner message after select of save layout with content 
		layout.chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
		viewerpage.assertTrue(viewerpage.verifyNotificationPopUp(ViewerPageConstants.SUCCESS, ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED + machineName+" is saved"), "Checkpoint[1/3]", "Verified banner message after selecting save layout with content.");
		viewerpage.assertFalse(layout.isElementPresent(layout.layoutContainer), "Checkpoint[2/3]", "Verified that layout container closed after selecting save layout with content.");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "Checkpoint[3/3]", "Verifying layout is 2x2");
		
			
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.deleteUserSetLayout();
		db.deleteUser(username_1);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);

	}


}






