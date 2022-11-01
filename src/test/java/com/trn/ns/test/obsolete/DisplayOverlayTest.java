//package com.trn.ns.test.obsolete;
//
//import java.awt.AWTException;
//import java.io.IOException;
//import java.util.List;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.dataProviders.DataProviderArguments;
//import com.trn.ns.dataProviders.ExcelDataProvider;
//import com.trn.ns.enums.BrowserType;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class DisplayOverlayTest extends TestBase{
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerpage;
//	
//	private SinglePatientStudyPage singlePatientStudyPage;
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("LTA_Study_filepath");
//	String ltastudyPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	String filePath1 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
//	String subject60Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//
//	String filePath2 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//	String patientNameMRLSP = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//
//	private String filePath3 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	private String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3);
//
//	String secondSeriesDescriptionLiver9 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY,filePath3);	
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws IOException, InterruptedException {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	//Move Overlay level control under TeraRecon Icon Menu - Minimum Level Coverage
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor","DE450","US334","Sanity"})
//	//Not working on IE11, FireFox 
//	public void test03_US334_TC1058_DE450_TC2038_verifyMinimumTextOverlayMinimumOnChildWindow() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Minimum Level Coverage <\br> Layout change is not working on multimonitor - Text Overlay Persistence");
//		patientPage = new PatientListPage(driver);
//
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//
//		//Enabling Child Window
//		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
//		for (String childWindow : childWinHandles) {
//
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		}
//
//		viewerpage.switchToWindow(parentWindow);
//
//		//Checking the default text overlay for the viewer's page
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Validate that the by default label 'Default' is already enabled.");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay), "Verifying TextOverlay icon on Menu 'Default' is already enabled.", "TextOverlay icon on Menu 'Default' is already enabled.");
//		viewerpage.closeTextOverlayOptions();
//
//		//Changing the overlay to minimum and verifying
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Validate 'Minimum' Text Overlay is been implemented");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.minimumTextOverlay), "Verifying TextOverlay icon 'Minimum' on Menu", "TextOverlay  icon 'Minimum' is displaying on Menu");
//		viewerpage.closeTextOverlayOptions();
//
//		//Verifying that the changed overlay is reflected on child window as well
//		viewerpage.switchToWindow(childWinHandles.get(1));
//		viewerpage.maximizeWindow();
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//		//switching back to parent window and reloading a new study
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientNameMRLSP);
//		
//		studyPage.clickOntheFirstStudy();
//
//		//verifying that the overlay is pertained for reloaded study as well
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Validate that the changed Text Overlay has pertained after change in study.");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.minimumTextOverlay), "Verifying that 'Minimum' TextOverlay  icon is still visible for reloaded study.", "'Minimum' TextOverlay  icon is still visible for reloaded study.");
//		viewerpage.closeTextOverlayOptions();
//
//		//  verifying the change of overlay from child to parent is reflecting
//		viewerpage.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientNameMRLSP);
//		
//		studyPage.clickOnStudy(1);
//
//		viewerpage.switchToWindow(childWinHandles.get(1));
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//		viewerpage.openOrCloseChildWindows(2);
//
//	}
//
//	//Move Overlay level control under TeraRecon Icon Menu - Default Level Coverage
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome","multimonitor","US334","DE450","Sanity"})
//	//Not working on IE11, FireFox 
//	public void test04_US334_TC1104_DE450_TC2038_verifyDefaultTextOverlayOnChildWindow() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Default Level Coverage <\br> Layout change is not working on multimonitor - Text Overlay Persistence");
//		patientPage = new PatientListPage(driver);
//
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//
//		//Enabling Child Window
//		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
//		for (String childWindow : childWinHandles) {
//
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}}
//
//		viewerpage.switchToWindow(parentWindow);
//
//		//Checking the default text overlay for the viewer's page	
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate that the by default label 'Default' is already enabled.");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay), "Verifying TextOverlay icon on Menu 'Default' is already enabled.", "TextOverlay icon on Menu 'Default' is already enabled.");
//		viewerpage.closeTextOverlayOptions();
//
//		//Changing the overlay to minimum and verifying
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate 'Minimum' Text Overlay is been implemented");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.minimumTextOverlay), "Verifying 'Minimum' is selected on Parent Window", "TextOverlay icon 'Minimum' is selected on Parent Window");
//		viewerpage.closeTextOverlayOptions();
//
//		//Changing the overlay to default and verifying
//		viewerpage.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate 'Default' Text Overlay is been implemented");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay), "Enabling 'Default' icon on Parent Window", "TextOverlay is displaying on Menu");
//		viewerpage.closeTextOverlayOptions();
//
//		//Verifying that the changed overlay is reflected on child window as well
//		viewerpage.switchToWindow(childWinHandles.get(1));
//		viewerpage.maximizeWindow();
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Validate that 'Default' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay), "Verifying TextOverlay icon 'Default' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Default' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//
//		//switching back to parent window and reloading a new study
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientNameMRLSP);
//		
//		studyPage.clickOntheFirstStudy();
//
//		//verifying that the overlay is pertained for reloaded study as well
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate that the changed Text Overlay has pertained after change in study.");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay),  "Verifying that 'Default' TextOverlay  icon is still visible for reloaded study.", "'Default' TextOverlay  icon is still visible for reloaded study.");
//		viewerpage.closeTextOverlayOptions();
//
//		//  verifying the change of overlay from child to parent is reflecting
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientNameMRLSP);
//		
//		studyPage.clickOnStudy(1);
//
//		viewerpage.switchToWindow(childWinHandles.get(1));
//		viewerpage.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//		viewerpage.openOrCloseChildWindows(2);
//	}
//
//	//Move Overlay level control under TeraRecon Icon Menu - Full Level Coverage
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome", "multimonitor","US334","DE450","Sanity"})
//	//Not working on IE11, FireFox 
//	public void test05_US334_TC1105_DE450_TC2038_verifyFullTextOverlayOnChildWindow() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Full Level Coverage <\br> Layout change is not working on multimonitor - Text Overlay Persistence");
//		patientPage = new PatientListPage(driver);
//
//		patientPage.clickOnPatientRow(subject60Patient);
//
//		singlePatientStudyPage = new SinglePatientStudyPage(driver);
//		singlePatientStudyPage.clickOnStudy(2);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.switchToWindow(parentWindow);
//
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.clickOnStudy(2);
//
//		//Enabling Child Window
//		List<String> childWinHandles = viewerpage.getAllOpenedWindowsIDs();
//		for (String childWindow : childWinHandles) {
//
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		}
//
//		//Checking the default text overlay for the viewer's page
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Validate that the by default label 'Default' is already enabled.");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.defaultTextOverlay), "Verifying TextOverlay icon on Menu 'Default' is already enabled.", "TextOverlay icon on Menu 'Default' is already enabled.");
//		viewerpage.closeTextOverlayOptions();
//
//		//Changing the overlay to full and verifying
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Validate 'full' Text Overlay is been implemented");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.fullTextOverlay), "Verifying TextOverlay icon 'full' on Menu", "TextOverlay  icon 'full' is displaying on Menu");
//		viewerpage.closeTextOverlayOptions();
//
//		//Verifying that the changed overlay is reflected on child window as well
//		viewerpage.switchToWindow(childWinHandles.get(1));
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Full' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.fullTextOverlay), "Verifying TextOverlay icon 'Full' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Full' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//		//switching back to parent window and reloading a new study
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientNameMRLSP);
//		
//		studyPage.clickOntheFirstStudy();
//
//		//verifying that the overlay is pertained for reloaded study as well
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Validate that the changed Text Overlay has pertained after change in study.");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.fullTextOverlay),  "Verifying that 'Full' TextOverlay  icon is still visible for reloaded study.", "'Full'TextOverlay  icon is still visible for reloaded study.");
//		viewerpage.closeTextOverlayOptions();
//
//		//  verifying the change of overlay from child to parent is reflecting
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		viewerpage.browserBackWebPage();
//		singlePatientStudyPage.waitForSingleStudyToLoad();
//		singlePatientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(patientNameMRLSP);
//		
//		studyPage.clickOnStudy(1);
//
//		viewerpage.switchToWindow(childWinHandles.get(1));
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.fullTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openTextOverlayOptions();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
//		viewerpage.assertTrue(viewerpage.isElementSelected(viewerpage.fullTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
//		viewerpage.closeTextOverlayOptions();
//		viewerpage.openOrCloseChildWindows(2);
//	}

//DE1874: [Automation]: Text overlay not visible on viewer when user access Non-UI URL and refreshes it.
//	@Test(groups ={"Chrome","Edge","IE11","DE1874","Positive"})
//	public void test08_DE1874_TC7502_verifyWWPresetAndLayoutItems() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("[Risk and Impact]: Verify the windowing presets and the layout items, when user loaded the viewer page");
//		
//		helper = new HelperClass(driver);
//		viewerpage = helper.loadViewerPageUsingSearch(ChestCT1p25mmPatient, 1, 2);
//		
//		presetMenu=new ViewerPresetMenu(driver);
//		layout=new ViewerLayout(driver);
//		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/5]", viewerpage.getCurrentPageURL()+" is displaying");
//
//		String  beforeWW=viewerpage.getWindowWidthValueOverlayText(2);
//		String beforeWC=viewerpage.getWindowCenterValueOverlayText(2);
//		presetMenu.selectPresetValue(presetMenu.getWindowWidthLabelOverlay(2), ViewerPageConstants.HEAD,2);
//		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(2), beforeWW, "Checkpoint[2/5]", "Verifying the WW after changing the value from preset menu.");
//		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(2), beforeWC, "Checkpoint[3/5]", "Verifying the WC after changing the value from preset menu.");
//		
//		//verify layout option visible on viewer
//		viewerpage.clickUsingAction(layout.gridIcon);
//		viewerpage.assertTrue(viewerpage.isElementPresent(layout.layoutContainer), "Checkpoint[4/5]", "Verified that layout container is visible on viewer.");
//		
//		viewerpage.refreshWebPage();
//		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Checkpoint[5/5]", viewerpage.getCurrentPageURL()+" is displaying");
//	}
//
//}
