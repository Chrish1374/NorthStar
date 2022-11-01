package com.trn.ns.test.viewer.basic;

import java.awt.AWTException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerBannerTest extends TestBase{


	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;

	// Get Patient Name
	String dummy16Filepath = Configurations.TEST_PROPERTIES.get("Dummy_16_filepath");
	String dummy16PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, dummy16Filepath);

	String bonadannaFilepath = Configurations.TEST_PROPERTIES.get("BONADONNA^NICHOLAS_filepath");
	String bonadannaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, bonadannaFilepath);

	String croninFilepath = Configurations.TEST_PROPERTIES.get("CRONIN^BARBARA_filepath");
	String croninPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, croninFilepath);

	String spectraDatabaseFilepath = Configurations.TEST_PROPERTIES.get("SPECTRA_DATABASE_filepath");
	String spectraDBPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, spectraDatabaseFilepath);

	String headNeckFilepath = Configurations.TEST_PROPERTIES.get("4Quan_HeadAndNeck_filepath");
	String quanHeadNeckPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, headNeckFilepath);

	String quibimFilepath = Configurations.TEST_PROPERTIES.get("Quibim_3DTrabecularBone_filepath");
	String quibimPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, quibimFilepath);

	String lunitFilepath = Configurations.TEST_PROPERTIES.get("LUNIT_CT_ChestPA_filepath");
	String lunitPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, lunitFilepath);

	String Liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Liver9filePath);

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private ViewerLayout layout;
	private ViewerTextOverlays overlays;

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);

	}

	@Test(groups= {"Chrome","US754"})
	public void test01_US754_TC2710_verifyNoBannerIsDisplayedWhenCSIsNull() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay - Content Qualification set to null");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(dummy16PatientName, 1);
		layout = new ViewerLayout(driver); 

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","Banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the text overlay in vb1","text overlay should not be present");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the text overlay in vb2","text overlay should not be present");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the scroll");

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","Banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the text overlay in vb1","text overlay should not be present");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the text overlay in vb2","text overlay should not be present");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the image number");
		viewerpage.scrollToImage(1, 1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","Banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the text overlay in vb1","text overlay should not be present");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the text overlay in vb2","text overlay should not be present");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout");
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","Banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the text overlay in vb1","text overlay should not be present");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the text overlay in vb2","text overlay should not be present");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the doubleclick");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","Banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the text overlay in vb1","text overlay should not be present");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the text overlay in vb2","text overlay should not be present");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the doubleclick once again");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","Banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the text overlay in vb1","text overlay should not be present");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the text overlay in vb2","text overlay should not be present");

	}

	@Test(groups= {"Chrome","US754"})
	public void test02_US754_TC2711_verifyNoBannerIsDisplayedWhenCSIsProduct() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay - Content Qualification set to PRODUCTION");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName, 1);	

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient");

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Verifying the banner presence","if CS id product then banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the overlay presence in vb1","if CS id product then overlay should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the overlay presence in vb2","if CS id product then overlay should not be displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the scroll using keyboard");
		viewerpage.scrollDownToSliceUsingKeyboard(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Verifying the banner presence","if CS id product then banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the overlay presence in vb1","if CS id product then overlay should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the overlay presence in vb2","if CS id product then overlay should not be displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Inputing the image number");
		viewerpage.scrollToImage(1, 1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Verifying the banner presence","if CS id product then banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the overlay presence in vb1","if CS id product then overlay should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the overlay presence in vb2","if CS id product then overlay should not be displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the change layout");
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Verifying the banner presence","if CS id product then banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the overlay presence in vb1","if CS id product then overlay should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the overlay presence in vb2","if CS id product then overlay should not be displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the doubleclick");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Verifying the banner presence","if CS id product then banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the overlay presence in vb1","if CS id product then overlay should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the overlay presence in vb2","if CS id product then overlay should not be displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the doubleclick once again");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"Verifying the banner presence","if CS id product then banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"Verifying the overlay presence in vb1","if CS id product then overlay should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"Verifying the overlay presence in vb2","if CS id product then overlay should not be displayed");

	}

	@Test(groups= {"Chrome","US754","Sanity"})
	public void test03_US754_TC2713_TC2719_verifyBannerIsDisplayedWhenCSIsResearch() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay- Content Qualification set to RESEARCH(1 SERIES) "
				+ "<br> DICOM result and research use only being displayed in a banner and overlay- Banner remains");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(croninPatientName, 1);
		overlays = new ViewerTextOverlays(driver);
		layout = new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient when CS is set to RESEARCH");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to full");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to Default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the change layout");
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click once again");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

	}

	@Test(groups= {"Chrome","US754","Sanity"})
	public void test04_US754_TC2717_TC2719_verifyBannerIsDisplayedWhenCSIsService() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay- Content Qualification set to SERVICE(1 SERIES)"
				+ "<br> DICOM result and research use only being displayed in a banner and overlay- Banner remains");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(bonadannaPatientName, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient when CS is set to SERVICE");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to full");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to Default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the change layout");
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click once again");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");




	}

	@Test(groups= {"Chrome","US754"})
	public void test05_US754_TC2718_verifyBannerClose() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay- Close banner");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(bonadannaPatientName, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient when CS is set to RESEARCH");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.closeIconOnNotification.get(0)),"verifying the close icon presence","verified");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Closing the banner");
		viewerpage.closeNotification();
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the annotation level to full ");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the annotation level to default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the annotation level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout");
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the double click");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the double click once again");
		viewerpage.doubleClickOnViewbox(1);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(1),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

	}

	@Test(groups= {"Chrome","US754"})
	public void test06_US754_TC2720_verifyBannerIsDisplayedWhenSeriesHasServiceAndResearchBoth() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay- Content Qualification set to RESEARCH(at least 3 SERIES) & SERVICE(at least 3 SERIES)");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(spectraDBPatientName, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient when series have RESEARCH and SERVICE BOTH");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence in vb2","overlay should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence in vb4","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb4","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb4","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb4","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb4","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


		layout.selectLayout(layout.twoByThreeLayoutIcon);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence in vb2","overlay should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence in vb4","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb4","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


		viewerpage.doubleClickOnViewbox(2);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence in vb2","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


		viewerpage.doubleClickOnViewbox(2);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence in vb2","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text in vb2","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");





	}

	@Test(groups= {"Chrome","US755"})
	public void test07_US755_TC2785_TC2827_verifyBannerWhenPDFIsInBackgroundOrForeground() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("PDF- foregroung"
				+ "<br> PDF- backgroung");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(quanHeadNeckPatient, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the banner when pdf is in background");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay is not displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");


		layout.selectLayout(layout.twoByThreeLayoutIcon);


		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to full");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to Default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click");
		viewerpage.doubleClick(viewerpage.getViewPort(2));
		viewerpage.waitForPdfToRenderInViewbox(2);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click once again");
		viewerpage.doubleClick(viewerpage.getViewPort(2));
		viewerpage.waitForPdfToRenderInViewbox(2);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the pdf using content Selector");
		String resultToSelect = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, headNeckFilepath);
		cs.selectResultFromSeriesTab(4, resultToSelect );

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


	}

	@Test(groups= {"Chrome","US755"})
	public void test08_US755_TC2831_TC2832_verifyBannerWhenPNGOrBMPIsInBackgroundOrForeground() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("png & bmp- foregroung"
				+ "<br>	png & bmp- backgroung");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(quibimPatientName, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);
		
		viewerpage.waitForPdfToRenderInViewbox(2);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the banner when pdf is in background");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence","overlay is not displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence","overlay is not displayed");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to full");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to Default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(1)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click");
		viewerpage.doubleClickOnViewbox(2);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click once again");
		viewerpage.doubleClickOnViewbox(2);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the image using content Selector");
		String resultToSelect = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, quibimFilepath);
		cs.selectResultFromSeriesTab(6,resultToSelect);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(2)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(2)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(2),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(6)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(6)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(6),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


	}

	@Test(groups= {"Chrome","US755"})
	public void test09_US755_TC2828_verifyBannerWhenJPEGIsInForeground() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("jpeg- foreground");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(lunitPatientName, 2);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the banner when pdf is in background");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualificationTextOverlay(1)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualificationTextOverlay(2)),"verifying the text overlay presence","overlay is not displayed");		

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");

		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to full");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to Default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click");
		viewerpage.doubleClickOnViewbox(3);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click once again");
		viewerpage.doubleClickOnViewbox(3);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the image using content Selector");
		String resultToSelect = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, lunitFilepath);
		cs.selectResultFromSeriesTab(3, resultToSelect);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


	}

	//	@Test(groups= {"Chrome","US755"})
	public void test10_US755_TC2830_TC2788_verifyBannerWhenJPEGIsInBackground() throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("jpeg- background"
				+ "<br> Banner remains");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(lunitPatientName, 1);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);
		cs = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the banner when JPEG is in background");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay is not displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");

		layout.selectLayout(layout.twoByTwoLayoutIcon);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay is not displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to full");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to Default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the overlay level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click");
		viewerpage.doubleClickOnViewbox(3);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "performing the double click once again");
		viewerpage.doubleClickOnViewbox(3);

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the image using content Selector");
		String resultToSelect = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, lunitFilepath);
		cs.selectResultFromSeriesTab(4, resultToSelect );

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(4)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(4)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(4),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","overlay should be displayed");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");

	//	viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


		viewerpage.closeNotification();		
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");


	}

	@Test(groups= {"Chrome","US755"})
	public void test11_US755_TC2787_verifyBannerCloseForNonDICOM() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Close banner");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(lunitPatientName, 2);
		layout = new ViewerLayout(driver);
		overlays = new ViewerTextOverlays(driver);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient when CS is set to RESEARCH");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getContentQualification(1)),"verifying the text overlay presence","overlay should be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.closeIconOnNotification.get(0)),"verifying the close icon presence","verified");

		viewerpage.assertTrue(viewerpage.getText(viewerpage.notificationMessage.get(0)).contains(ViewerPageConstants.BANNER_TEXT),"verifying the banner text","verified");
		//viewerpage.assertEquals(viewerpage.getBackgroundColorOfRows(viewerpage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Closing the banner");
		viewerpage.closeNotification();
		viewerpage.waitForTimePeriod(2000);
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");

		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerpage.closingConflictMsg();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the annotation level to full ");
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.closeIconOnNotification.get(0)),"verifying the close icon absence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the annotation level to default");
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.closeIconOnNotification.get(0)),"verifying the close icon absence","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the annotation level to minimum");
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.closeIconOnNotification.get(0)),"verifying the close icon absence","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Changing the layout");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.closingConflictMsg();
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");

		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the double click");
		viewerpage.doubleClickOnViewbox(3);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Performing the double click once again");
		viewerpage.doubleClickOnViewbox(3);

		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.notificationDiv),"verifying the banner presence","banner should not be displayed");
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getContentQualification(3)),"verifying the text overlay presence","verified");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.getContentQualificationTextOverlay(3)),ViewerPageConstants.TEXT_OVERLAY,"verifying the text overlay text","verified");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.getContentQualificationTextOverlay(3),NSGenericConstants.FILL),ViewerPageConstants.TEXT_OVERLAY_COLOR,"verifying the text color","verified");



	}






}
