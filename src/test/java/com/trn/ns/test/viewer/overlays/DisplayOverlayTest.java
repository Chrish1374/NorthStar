package com.trn.ns.test.viewer.overlays;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Functional.NS.I15 ViewerOverlayInteraction-CF0304ARevD - revision-0
//Safety.NS_F152_SupportOutputFromEnvoyMachinesAndSourceStudyForDICOMSCImagesCF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DisplayOverlayTest extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	private ViewerTextOverlays textOverlay;
	private ViewerOrientation orientation;

	String filePath = Configurations.TEST_PROPERTIES.get("LTA_Study_filepath");
	String ltastudyPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
	String subject60Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String subjectStudy ="MR Caput";

	String filePath2 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String patientNameMRLSP = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	private String filePath3 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	
	String filePath5 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String AH4Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	
	String secondSeriesDescriptionLiver9 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY,filePath3);
	private ContentSelector contentSelector;
	private HelperClass helper;	

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws IOException, InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	@Test(groups ={"Chrome","Edge","IE11","US100","DE681","US2253","Positive","F1083","E2E"})
	public void test01_US100_DE681_TC151_TC207_TC208_TC209_TC2401_US2253_TC10327_verifyTextOverlay() throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Display Patient, Study, Image, Series information on Overlay. <br>"+
		"Verify the Default, Minimum and Full text overlay options.");

		//Get Patient Name

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(ltastudyPatient, 1, 1);
	
		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateDBTextOverlayInFullMode(ltastudyPatient,ViewerPageConstants.PATIENTEXTERNALID_VALUE,ViewerPageConstants.ACQUISITIONDEVICE_VALUE,ViewerPageConstants.STUDYACQUISITIONDEVICESITE_VALUE,
				ViewerPageConstants.IMAGEACQUISITIONDEVICEDATETIME_VALUE,ViewerPageConstants.TARGET_VALUE,ViewerPageConstants.DETECTOR_VALUE);

		//1. Verifying Menu appears with 3 Icons
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[1/6]", "Validate Menu appers with 3 icons");
		layout=new ViewerLayout(driver);
		textOverlay=new ViewerTextOverlays(driver);
		orientation=new ViewerOrientation(driver);
		viewerpage.assertTrue(layout.gridIcon.isDisplayed(), "Verifying Grid icon on Menu","Grid is displaying on Menu");
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.textOverlayIcon), "Verifying TextOverlay icon on Menu", "TextOverlay is displaying on Menu");

		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		textOverlay.waitForDefaultOverlayDisplay(1);
		//Verifying Text overlay labels for default selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint TC2[1]: Checkpoint[3/6]", "Validate default overlay labels on viebox");
		textOverlay.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.DEFAULT_ANNOTATION,1), "Checkpoint TC1[1]: Checkpoint TC2[2]: Verifying selected overlay texts are displaying", "Overlay texts are displaying in default mode");
		
		textOverlay.assertTrue(orientation.isOrientationDisplayed(1,filePath), "Checkpoint TC1[1]: Checkpoint TC2[2]: Verifying that orientation are displaying", "Orientation are displaying in default mode");

		textOverlay.click(textOverlay.getViewPort(1));
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		textOverlay.waitForMinimumOverlayDisplay(1);
		//Verifying Text overlay labels for minimum selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint TC1[2]: Checkpoint[4/6]", "Validate Minimum overlay labels on viebox");
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed("Minimum",1), "Checkpoint TC1[1]: Verifying selected overlay texts are displaying", "Overlay texts are displaying in Minimum mode");
		viewerpage.assertTrue(orientation.isOrientationDisplayed(1,filePath), "Checkpoint TC1[1]: Verifying that orientation are displaying", "Orientation are displaying in Minimum mode");

		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.waitForFullOverlayDisplay(1);
		//Verifying Text overlay labels for Full selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[5/6]", "Validate full overlay labels on viebox");
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed("Full",1), "Checkpoint TC1[1]: Verifying selected overlay texts are displaying", "Overlay texts are displaying in Full mode");
		viewerpage.assertTrue(orientation.isOrientationDisplayed(1,filePath), "Checkpoint TC1[1]: Verifying that orientation are displaying", "Orientation are displaying in Full mode");

	}

	@Test(groups ={"IE11","Chrome","Chrome","DE90","DE681","US2144","F1081"})
	//Not working on IE11, FireFox 
	public void test02_DE90_DE681_TC466_TC2402_US2144_TC9590_verifyTextOverlayOnLayoutChange() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the text overlay on layout change</br>Verify Orientation marker on layout change.<br>"+
		"Verify selection of tools from the viewer tool bar - Left  section");
		
		helper=new HelperClass(driver);

		viewerpage = helper.loadViewerPageUsingSearch(ltastudyPatient, 1, 1);

		layout=new ViewerLayout(driver);
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.DEFAULT_ANNOTATION,1), "Verifying selected overlay texts are displaying after layout is changed", "Overlay texts are displaying in default mode after layout is changed");

		//Verifying Text overlay labels for default selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Validate default overlay labels on viewbox");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.DEFAULT_ANNOTATION,1), "Verifying selected overlay texts are displaying after layout is changed", "Overlay texts are displaying in default mode after layout is changed");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.MINIMUM_ANNOTATION,1), "Verifying selected overlay texts are displaying after layout is changed", "Overlay texts are displaying in Minimum mode after layout is changed");

		//Verifying Text overlay labels for minimum selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Validate Minimum overlay labels on viewbox");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.MINIMUM_ANNOTATION,1), "Verifying selected overlay texts are displaying after layout is changed", "Overlay texts are displaying in Minimum mode after layout is changed");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.FULL_ANNOTATION,1), "Verifying selected overlay texts are displaying after layout is changed", "Overlay texts are displaying in Full mode after layout is changed");

		//Verifying Text overlay labels for Full selection
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate full overlay labels on viewbox");
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.waitForViewerpageToLoad();

		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.FULL_ANNOTATION,1), "Verifying selected overlay texts are displaying after layout is changed", "Overlay texts are displaying in Full mode after layout is changed");
		viewerpage.click(viewerpage.patientsIcon);
		patientPage=new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[4/4]", "Verified patients page after click on Patient icon on viewer.");
	
	}

	@Test(groups ={"IE11","Chrome","Chrome","DE694"})
	//Not working on IE11, FireFox 
	public void test03_DE694_verifyTextOverlayOnOneUp() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation level reset after performing One-Up");
		
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(liver9Patient, 1, 1);

		//selecting full annotation
		layout=new ViewerLayout(driver);
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' on Menu", "TextOverlay icon is 'Full' on Menu");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode", "TextOverlays icons are present after enabling TextOverlays in 'full' mode");

		//performing double click and checking that full annotation is retained
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying TextOverlay icon is 'Full' after double clicking");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' after double clicking", "TextOverlay icon is 'Full' after double clicking");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode after double clicking", "TextOverlays icons are present after enabling TextOverlays in 'full' mode after double clicking");

		//performing double click again and checking that full annotation is retained
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying TextOverlay icon is 'Full' after double clicking again");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' after double clicking again", "TextOverlay icon is 'Full' after double clicking again");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode after double clicking again", "TextOverlays icons are present after enabling TextOverlays in 'full' mode after double clicking again");

		//performing WWWL and checking that full annotation is retained
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 0, 10, 50);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying TextOverlay icon is 'Full' after WWWL");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' after WWWL", "TextOverlay icon is 'Full' after WWWL");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode after WWWL", "TextOverlays icons are present after enabling TextOverlays in 'full' mode after WWWL");

		//performing Zoom and checking that full annotation is retained
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 0, 10, 50);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying TextOverlay icon is 'Full' after Zoom");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' after Zoom", "TextOverlay icon is 'Full' after Zoom");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode after Zoom", "TextOverlays icons are present after enabling TextOverlays in 'full' mode after Zoom");

		//performing Pan and checking that full annotation is retained
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 0, 10, 50);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying TextOverlay icon is 'Full' after Pan");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' after Pan", "TextOverlay icon is 'Full' after Pan");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode after Pan", "TextOverlays icons are present after enabling TextOverlays in 'full' mode after Pan");

		//performing Cine and checking that full annotation is retained
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying TextOverlay icon is 'Full' after Cine");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),"Verifying TextOverlay icon is 'Full' after Cine", "TextOverlay icon is 'Full' after Cine");
		textOverlay.compareTextOverlayValuesInFullMode(1, filePath3);
		viewerpage.assertTrue(true, "Verifying that all TextOverlays are present after enabling TextOverlays in 'full' mode after Cine", "TextOverlays icons are present after enabling TextOverlays in 'full' mode after Cine");

	}

	@Test(groups = {"Chrome","US756"})
	public void test04_US756_TC2696_verifyDataWrappingForTextOverlay() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  tool tips for text overlays that are truncated so that user view the full text overlay in the tool tip.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9Patient+" in viewer" );
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(liver9Patient, 1, 1);
		
		contentSelector = new ContentSelector(driver);
		//select full text overlay for Context menu
		textOverlay=new ViewerTextOverlays(driver);
		layout=new ViewerLayout(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.waitForFullOverlayDisplay(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying tool tip for study description overlay");
		viewerpage.assertTrue(textOverlay.isToolTipPresentForTextOveraly(viewerpage.getStudyDescriptionOverlay(1)), "Verifying tool tip for study description overlay on viewbox1", "The Tool tip is present for study description text overlay on viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying tool tip do not appear for series description");
		viewerpage.assertFalse(textOverlay.isToolTipPresentForTextOveraly(viewerpage.getSeriesDescriptionOverlay(1)), "Verifying tool tip for series description overlay on viewbox1", "The Tool tip is not present for series description text overlay on viewbox1");

		//change layout to 1X1
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.waitForTimePeriod(1000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying tool tip for study description overlay on changing layout");
		viewerpage.assertTrue(textOverlay.isToolTipPresentForTextOveraly(viewerpage.getStudyDescriptionOverlay(1)), "Verifying tool tip for study description overlay on viewbox1", "The Tool tip is present for study description text overlay on viewbox1");

		//select second series description form content selector
		contentSelector.selectSeriesFromSeriesTab(1, secondSeriesDescriptionLiver9);
		viewerpage.waitForTimePeriod(1000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying tool tip for study description overlay on selecting series from content selector");
		viewerpage.assertTrue(textOverlay.isToolTipPresentForTextOveraly(viewerpage.getStudyDescriptionOverlay(1)), "Verifying tool tip for study description overlay on viewbox1", "The Tool tip is present for study description text overlay on viewbox1");

		//change to default layout and resize the browser
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.resizeBrowserWindow(700, 700);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying tool tip for study description overlay on resizing browser");
		viewerpage.waitForTimePeriod(1000);
		viewerpage.assertTrue(textOverlay.isToolTipPresentForTextOveraly(viewerpage.getStudyDescriptionOverlay(1)), "Verifying tool tip for study description overlay on viewbox1", "The Tool tip is present for study description text overlay on viewbox1");


	}

	@Test(groups ={"IE11","Chrome","Chrome","DE450","US334","Sanity"})
	//Not working on IE11, FireFox 
	public void test05_US334_TC1058_DE450_TC2038_verifyMinimumTextOverlay() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Minimum Level Coverage "
				+ "<\br> Layout change is not working on multimonitor - Text Overlay Persistence");
		patientPage = new PatientListPage(driver);

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(subject60Patient, subjectStudy, 1);

		//Checking the default text overlay for the viewer's page
		textOverlay=new ViewerTextOverlays(driver);
		layout=new ViewerLayout(driver);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[1]: Checkpoint[1/4]", "Validate that the by default label 'Default' is already enabled.");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay), "Verifying TextOverlay icon on Menu 'Default' is already enabled.", "TextOverlay icon on Menu 'Default' is already enabled.");
		textOverlay.closeTextOverlayOptions();

		//Changing the overlay to minimum and verifying
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[2]: Checkpoint[2/4]", "Validate 'Minimum' Text Overlay is been implemented");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay), "Verifying TextOverlay icon 'Minimum' on Menu", "TextOverlay  icon 'Minimum' is displaying on Menu");
		textOverlay.closeTextOverlayOptions();

		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();

		//switching back to parent window and reloading a new study
		helper.browserBackAndReloadViewer(patientNameMRLSP, 1, 1);

		//verifying that the overlay is pertained for reloaded study as well
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC1[3]: Checkpoint[4/4]", "Validate that the changed Text Overlay has pertained after change in study.");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay), "Verifying that 'Minimum' TextOverlay  icon is still visible for reloaded study.", "'Minimum' TextOverlay  icon is still visible for reloaded study.");
		textOverlay.closeTextOverlayOptions();

		//  verifying the change of overlay from child to parent is reflecting
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		helper.browserBackAndReloadViewer(patientNameMRLSP, 1, 1);
		
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();

		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();


	}
	
	@Test(groups ={"IE11","Chrome", "Chrome","US334","DE450","Sanity"})
	//Not working on IE11, FireFox 
	public void test06_US334_TC1104_DE450_TC2038_verifyDefaultTextOverlay() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Default Level Coverage <\br> Layout change is not working on multimonitor - Text Overlay Persistence");
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(subject60Patient, subjectStudy, 1);
		
		//Checking the default text overlay for the viewer's page	
		textOverlay=new ViewerTextOverlays(driver);
		
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Validate that the by default label 'Default' is already enabled.");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay), "Verifying TextOverlay icon on Menu 'Default' is already enabled.", "TextOverlay icon on Menu 'Default' is already enabled.");
		textOverlay.closeTextOverlayOptions();

		//Changing the overlay to minimum and verifying
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Validate 'Minimum' Text Overlay is been implemented");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay), "Verifying 'Minimum' is selected on Parent Window", "TextOverlay icon 'Minimum' is selected on Parent Window");
		textOverlay.closeTextOverlayOptions();

		//Changing the overlay to default and verifying
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Validate 'Default' Text Overlay is been implemented");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay), "Enabling 'Default' icon on Parent Window", "TextOverlay is displaying on Menu");
		textOverlay.closeTextOverlayOptions();

		//switching back to parent window and reloading a new study
		
		helper.browserBackAndReloadViewer(patientNameMRLSP, 1, 1);

		//verifying that the overlay is pertained for reloaded study as well
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Validate that the changed Text Overlay has pertained after change in study.");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay),  "Verifying that 'Default' TextOverlay  icon is still visible for reloaded study.", "'Default' TextOverlay  icon is still visible for reloaded study.");
		textOverlay.closeTextOverlayOptions();

		//  verifying the change of overlay from child to parent is reflecting
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		helper.browserBackAndReloadViewer(patientNameMRLSP, 1, 1);

		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();

		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();
	}

	@Test(groups ={"IE11","Chrome","Chrome", "US334","DE450","Sanity"})
	public void test07_US334_TC1105_DE450_TC2038_verifyFullTextOverlay() throws InterruptedException{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Move Overlay level control under TeraRecon Icon Menu - Full Level Coverage <\br> Layout change is not working on multimonitor - Text Overlay Persistence");
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(subject60Patient, subjectStudy, 1);

		//Checking the default text overlay for the viewer's page
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1]:Checkpoint[1/4]", "Validate that the by default label 'Default' is already enabled.");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.defaultTextOverlay), "Verifying TextOverlay icon on Menu 'Default' is already enabled.", "TextOverlay icon on Menu 'Default' is already enabled.");
		textOverlay.closeTextOverlayOptions();

		//Changing the overlay to full and verifying
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2]: Checkpoint[2/4]", "Validate 'full' Text Overlay is been implemented");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay), "Verifying TextOverlay icon 'full' on Menu", "TextOverlay  icon 'full' is displaying on Menu");
		textOverlay.closeTextOverlayOptions();

		//Verifying that the changed overlay is reflected on child window as well
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Full' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay), "Verifying TextOverlay icon 'Full' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Full' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();

		//switching back to parent window and reloading a new study
		helper.browserBackAndReloadViewer(patientNameMRLSP, 1, 1);

		//verifying that the overlay is pertained for reloaded study as well
		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[3]:Checkpoint[4/4]", "Validate that the changed Text Overlay has pertained after change in study.");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.fullTextOverlay),  "Verifying that 'Full' TextOverlay  icon is still visible for reloaded study.", "'Full'TextOverlay  icon is still visible for reloaded study.");
		textOverlay.closeTextOverlayOptions();

		//  verifying the change of overlay from child to parent is reflecting
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		helper.browserBackAndReloadViewer(patientNameMRLSP, 1, 1);

		textOverlay.openTextOverlayOptions();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate that 'Minimum' Text Overlay has reflected on the Child Monitor as well");
		viewerpage.assertTrue(viewerpage.isElementSelected(textOverlay.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well", "TextOverlay icon 'Minimum' on Menu has reflected on the Child Monitor as well");
		textOverlay.closeTextOverlayOptions();
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1874"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test10_verifyTextOverlays"})
	public void test09_DE1874_TC7472_TC7501_verifyTextOverlaysWhenUserAccessUsingLogin(String fileName,String viewboxNo) throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify that text overlay's are visible on the viewer page, when user refreshes the page.");
		
		loginPage.openNewWindow();
		List<String> allWindowsID = loginPage.getAllOpenedWindowsIDs();
		
		loginPage.switchToWindow(allWindowsID.get(1));
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		
		patientPage = new PatientListPage(driver);

		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		
		int viewbox=Integer.parseInt(viewboxNo);

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(PatientName, 1, viewbox);
		
		int viewboxCount=viewerpage.getNumberOfCanvasForLayout();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Checkpoint[1/6]", viewerpage.getCurrentPageURL()+" is displaying");

		textOverlay=new ViewerTextOverlays(driver);
		viewerpage.refreshWebPage();
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL) , "Checkpoint[2/6]", viewerpage.getCurrentPageURL()+" is displaying after browser refresh");

		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		
		//Perform refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]", "Verifying text overlay on viewer after browser refresh.");
		viewerpage.waitForViewerpageToLoad(viewbox);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL) , "Verify that viewer page should display", viewerpage.getCurrentPageURL()+" is displaying");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewboxCount, "Verify layout after viewer page refresh.", "Verified layout after viewer page refreshed.");
		viewerpage.assertTrue(textOverlay.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.DEFAULT_ANNOTATION), "Verify text overlay detail for default annotation", "Verified patient name text overlay for default annotation.");
		
		//change textoverlay from default to full and verify text overlay after refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify text overlay for Full annotation.");
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerpage.waitForViewerpageToLoad(viewbox);
		viewerpage.assertTrue(textOverlay.verifyTextOverlayIsHighlighted(textOverlay.fullTextOverlay), "Verifying TextOverlay icon 'Full' on Menu", "TextOverlay  icon 'Full' is selected on Menu");
		viewerpage.assertTrue(textOverlay.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.FULL_ANNOTATION), "Verify text overlay detail for full annotation", "Verified Image matrix text overlay for full annotation.");
			
	
		//change textoverlay from full to minimum and verify text overlay after refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify text overlay for Minimum annotation.");
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		viewerpage.waitForViewerpageToLoad(viewbox);
		viewerpage.assertTrue(textOverlay.verifyTextOverlayIsHighlighted(textOverlay.minimumTextOverlay),"Verifying TextOverlay icon 'Minimum' on Menu", "TextOverlay  icon 'Minimum' is selected on Menu");
		viewerpage.assertTrue(textOverlay.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.MINIMUM_ANNOTATION), "Verify text overlay detail for minimum  annotation", "Verified patient ID text overlay for minimum annotation.");
		
		//change textoverlay from full to minimum and verify text  overlay after refresh
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify text overlay for Default annotation.");
		textOverlay.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		viewerpage.waitForViewerpageToLoad(viewbox);
		viewerpage.assertTrue(textOverlay.verifyTextOverlayIsHighlighted(textOverlay.defaultTextOverlay),"Verifying TextOverlay icon 'Default' on Menu", "TextOverlay  icon 'Default' is selected on Menu");
		viewerpage.assertTrue(textOverlay.verifyTextOverlayDetail(viewboxCount, ViewerPageConstants.DEFAULT_ANNOTATION), "Verify text overlay detail for default annotation", "Verified patient name text overlay for default annotation.");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE2229","Positive"})
	public void test10_DE2229_TC9050_verifyDigitsAfterDecimalInThickness() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the only one digit is shown after decimal for thickness displayed in Text overlay on viewer.");
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(AH4Patient, 1);
	
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		int noOfDigitsAfterDecimalInThickness = viewerpage.getText(viewerpage.getSliceThicknessOverlay(1)).substring(13, 14).length();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify only one digit should be displayed after the decimal point for the thickness displayed.");
		viewerpage.assertEquals(noOfDigitsAfterDecimalInThickness, 1, "Verify only one digit should be displayed after the decimal point for the thickness displayed", "Verifed");
		
	}
	
	
	

}
