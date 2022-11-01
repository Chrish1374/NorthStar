//package com.trn.ns.test.obsolete;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
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
//public class E2E_TC1384_Workflow extends TestBase{
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerpage;
//	
//	private ExtentTest extentTest;
//	
//	//Get Patient Name
//	String filePath = Configurations.TEST_PROPERTIES.get("LTA_Study_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//	//Limitation : As per Defect DE299, "Study date and Time" and "acquisition  Date" format is different in Chrome and FF, but to check other text overlays updated the format in xml
//	//			 : so should fail in IE11 and Edge browser
//	@Test(groups ={"firefox","Chrome","IE11","Edge","dbConfig"})
//	public void test01_E2E_TC1384_verifyTextOverlayLevelSelection() throws InterruptedException
//	{	
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Isolated Feature Testing – Non-Image Related : Text Overlay Level Selection");
//		patientPage = new PatientListPage(driver);
//
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//
//		db.updateDBTextOverlayInFullMode(patientName,
//				NSConstants.PATIENTEXTERNALID_VALUE,NSConstants.ACQUISITIONDEVICE_VALUE,NSConstants.STUDYACQUISITIONDEVICESITE_VALUE,
//				NSConstants.IMAGEACQUISITIONDEVICEDATETIME_VALUE,NSConstants.TARGET_VALUE,NSConstants.DETECTOR_VALUE);
//
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Verifying Menu appears with 3 Icons
//		viewerpage.openNorthstarLogo();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Validate Menu appers with 3 icons");
//		viewerpage.assertTrue(viewerpage.gridIcon.isDisplayed(), "Verifying Grid icon on Menu","Grid is displaying on Menu");
//		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.multiMonitorOption),"Verifying multimonitor icon on Menu","Multimonitor is displaying on Menu");
//		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.textOverlayIcon), "Verifying TextOverlay icon on Menu", "TextOverlay is displaying on Menu");
//		viewerpage.closeNorthstarLogo();
//
//		//Verifying on clicking Text overlay D – Default is selected
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Validate 'D' Default is selected on clicking text overlay icon");
//		viewerpage.openNorthstarLogo();
//		viewerpage.assertTrue(viewerpage.defaultTextOverlay.isSelected(), "Verifying that on clicking Text overlay D – Default is selected", "'Default' is selected by default on Text overlay");
//
//		//Verifying Text overlay labels for default selection
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Validate default overlay labels on viebox");
//		viewerpage.assertTrue(viewerpage.isSelectedOverlayDisplayed("Default",1), "Verifying selected overlay texts are displaying", "Overlay texts are displaying in default mode");
//		viewerpage.closeNorthstarLogo();
//
//		viewerpage.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
//		//Verifying Text overlay labels for minimum selection
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Validate Minimum overlay labels on viebox");
//		viewerpage.assertTrue(viewerpage.isSelectedOverlayDisplayed("Minimum",1), "Verifying selected overlay texts are displaying", "Overlay texts are displaying in Minimum mode");
//
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		//Verifying Text overlay labels for Full selection
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Validate full overlay labels on viebox");
//		viewerpage.assertTrue(viewerpage.isSelectedOverlayDisplayed("Full",1), "Verifying selected overlay texts are displaying", "Overlay texts are displaying in Full mode");
//
//		//Verify Text overlay value for full selection
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Validate full overlay values on viebox");
//		viewerpage.assertTrue(viewerpage.compareTextOverlayValuesInFullMode(1,filePath), "Verifying Text overlay values for full mode selection", "Values are displaying in full mode");
//
//	}
//
//	//Verifying text overlay for other study
//
//	//Limitation : As per Defect DE299, "Study date and Time" and "acquisition  Date" format is different in Chrome and FF, but to check other text overlays updated the format in xml
//	//			 : so should fail in IE11 and Edge browser
//	@Test(groups ={"firefox","Chrome","IE11","Edge"})
//	public void test02_E2E_TC1384_verifyTextOverlayLevelSelection() throws InterruptedException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Isolated Feature Testing – Non-Image Related : Text Overlay Level Selection");
//		
//		patientPage = new PatientListPage(driver);
//
//		String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Verify Text overlay value for full selection
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Validate full overlay values on viebox");
//		viewerpage.assertTrue(viewerpage.compareTextOverlayValues(1,filePath), "Verifying Text overlay values for full mode selection", "Values are displaying in full mode");
//
//	}
//
//}
