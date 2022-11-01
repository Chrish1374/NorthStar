//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.PASS;
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import org.openqa.selenium.TimeoutException;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.dataProviders.DataProviderArguments;
//import com.trn.ns.dataProviders.ExcelDataProvider;
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
//public class E2E_TC1389_Workflow extends TestBase{
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	//Get Patient Name
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	//Get Patient Name
//	String l9_filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String l9_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
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
//	//Not working in IE and edge because of mouse move activity 
//	@Test(groups ={"firefox","Chrome","Edge"})
//	public void test01_E2E_TC1389_verifyActiveOverlay() throws TimeoutException, InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying all active overlays");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Loading the Patient data in full overlay" );
//		viewerpage.selectTextOverlays("full");
//		viewerpage.waitForFullOverlayDisplay(1);
//
//		//Step - 1 verify the active overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Step1 - Verifying Active overlay" );
//		viewerpage.assertTrue(viewerpage.checkActiveOverlays(1), "Verify the presence of Active Overlays on viewer screen", "Active overlays are verified successfully");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test02_E2E_TC1389_verifyOrientationMarker() {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow -Verify 90 degree image flip for all orientation markers");
//
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Loading the Patient "+l9_patientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(l9_patientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		//Step 2
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/beforeFlipImageA.png");
//
//		//Verify flip for A orientation
////		viewerpage.clickOnOrientation("A",viewerpage.topLeftOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/afterFlipImageA.png");
//
//		String beforeFlipImageA = newImagePath+"/actualImages/beforeFlipImageA.png";
//		String afterFlipImageA = newImagePath+"/actualImages/afterFlipImageA.png";
//		String diffImagePathA = newImagePath+"/actualImages/FlipImageA.png";
//
//		boolean cpStatusA =  viewerpage.compareimages(beforeFlipImageA, afterFlipImageA, diffImagePathA);
//		viewerpage.assertFalse(cpStatusA, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[2/9]", "Verifying 90 degree image flip for A orientation");
//
////		viewerpage.clickOnOrientation("L",viewerpage.topRightOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/currentFlipImageA.png");
//
//		String currentFlipImageA = newImagePath+"/actualImages/currentFlipImageA.png";
//		String diffImagePathA1 = newImagePath+"/actualImages/FlipImageA1.png";
//		boolean cpStatusA1 =  viewerpage.compareimages(beforeFlipImageA, currentFlipImageA, diffImagePathA1);
//		viewerpage.assertTrue(cpStatusA1, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[3/9]", "Verifying 90 degree image flip for A orientation");
//
//		//Verify flip for L orientation
////		viewerpage.clickOnOrientation("L",viewerpage.rightLeftOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/afterFlipImageL.png");
//
//		String afterFlipImageL = newImagePath+"/actualImages/afterFlipImageL.png";
//		String diffImagePathL = newImagePath+"/actualImages/FlipImageL.png";
//
//		boolean cpStatusL =  viewerpage.compareimages(beforeFlipImageA, afterFlipImageL, diffImagePathL);
//		viewerpage.assertFalse(cpStatusL, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[4/9]", "Verifying 90 degree image flip for L orientation");
//
////		viewerpage.clickOnOrientation("A",viewerpage.rightRightOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/currentFlipImageL.png");
//
//		String currentFlipImageL = newImagePath+"/actualImages/currentFlipImageL.png";
//		String diffImagePathL1 = newImagePath+"/actualImages/FlipImageL1.png";
//		boolean cpStatusL1 =  viewerpage.compareimages(beforeFlipImageA, currentFlipImageL, diffImagePathL1);
//		viewerpage.assertTrue(cpStatusL1, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[5/9]", "Verifying 90 degree image flip for L orientation");
//
//		//Verify flip for P orientation
////		viewerpage.clickOnOrientation("P",viewerpage.bottomLeftOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/afterFlipImageP.png");
//
//		String afterFlipImageP = newImagePath+"/actualImages/afterFlipImageP.png";
//		String diffImagePathP = newImagePath+"/actualImages/FlipImageP.png";
//
//		boolean cpStatusP =  viewerpage.compareimages(beforeFlipImageA, afterFlipImageP, diffImagePathP);
//		viewerpage.assertFalse(cpStatusP, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[6/9]", "Verifying 90 degree image flip for P orientation");
//
////		viewerpage.clickOnOrientation("R",viewerpage.bottomRightOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/currentFlipImageP.png");
//
//		String currentFlipImageP = newImagePath+"/actualImages/currentFlipImageP.png";
//		String diffImagePathP1 = newImagePath+"/actualImages/FlipImageP1.png";
//		boolean cpStatusP1 =  viewerpage.compareimages(beforeFlipImageA, currentFlipImageP, diffImagePathP1);
//		viewerpage.assertTrue(cpStatusP1, "Verify that the actual and Expected image are same","The actual and Expected image are same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[7/9]", "Verifying 90 degree image flip for P orientation");
//
//		//Verify flip for R orientation
////		viewerpage.clickOnOrientation("R",viewerpage.leftLeftOverlay,1);
////		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/afterFlipImageR.png");
//
//		String afterFlipImageR = newImagePath+"/actualImages/afterFlipImageR.png";
//		String diffImagePathR = newImagePath+"/actualImages/FlipImageR.png";
//
//		boolean cpStatusR =  viewerpage.compareimages(beforeFlipImageA, afterFlipImageR, diffImagePathR);
//		viewerpage.assertFalse(cpStatusR, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[8/9]", "Verifying 90 degree image flip for R orientation");
//
////		viewerpage.clickOnOrientation("P",viewerpage.leftRightOverlay,1);
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/currentFlipImageR.png");
//
//		String currentFlipImageR = newImagePath+"/actualImages/currentFlipImageR.png";
//		String diffImagePathR1 = newImagePath+"/actualImages/FlipImageR1.png";
//		boolean cpStatusR1 =  viewerpage.compareimages(beforeFlipImageA, currentFlipImageR, diffImagePathR1);
//		viewerpage.assertTrue(cpStatusR1, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[9/9]", "Verifying 90 degree image flip for R orientation");
//
//	}
//
//	//Not work in IE and edge because of mouse move activity - 'mouseHover'
//	@Test(groups ={"firefox","Chrome"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
//	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test03_PresetForDiffModalities" })
//	public void test03_E2E_TC1389_verifyPresetValuePerModality(String PatientName, String Modality) throws InterruptedException 
//	{
//		//Step - 4
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("verify the  preset value per modality for : " + Modality + " data");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.selectTextOverlays("default");
//		viewerpage.waitForDefaultOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1-6", "Verify the PRESET value for "+Modality+" data");
//		viewerpage.assertTrue(viewerpage.isPresetTableDisplayed(viewerpage.getWindowCenterLabelOverlay(1),1), "Verifying PRESET value visibility", "PRESET values 'INVERT' and 'REST' are available ");
//	}
//
//	//Not work in IE and edge because of mouse move activity 
//	//Failing in Chrome and FF because of DE386
//	@Test(groups ={"firefox","Chrome"})
//	public void test04_E2E_TC1389_verifyInvertPreset() throws InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying Invert preset");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Loading the Patient data in full overlay" );
//		viewerpage.selectTextOverlays("full");
//		viewerpage.waitForFullOverlayDisplay(1);
//		viewerpage.assertTrue(viewerpage.isPresetTableDisplayed(viewerpage.getWindowCenterLabelOverlay(1),1), "Verify the presence of PRSET values", "Preset values are present");
//
//		//Step 5
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/OldWWWCImage.png");
//
//		viewerpage.selectPresetValue(viewerpage.getWindowCenterLabelOverlay(1), ViewerPageConstants.INVERT,1);
//		//		Since it is v low priority defect hence commenting this up
//		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying that selected label is Italic" );
//		//		viewerpage.assertTrue(viewerpage.verifyFontForPresetSelectedText(viewerpage.getWindowCenterLabelOverlay(1),viewerpage.valueINVERT,1), "Verify that the INVERT is displaying in Italic font", "INVERT is displaying in Italic font");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying that after mouse movement from selection hides the pop up menu" );
//		viewerpage.assertFalse(viewerpage.OverlayMenuTable.getCssValue("display").trim().equalsIgnoreCase("block"), "Verify that the pop up hides", "Pop up menu hides after mouse movement");
//
//		viewerpage.takeElementScreenShot(viewerpage.viewboxImage1, newImagePath+"/actualImages/NewWWWCImage.png");
//		String expectedImagePath = newImagePath+"/actualImages/OldWWWCImage.png";
//		String actualImagePath = newImagePath+"/actualImages/NewWWWCImage.png";
//		String diffImagePath = newImagePath+"/actualImages/WWWCImage.png";
//		boolean cpStatus =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		viewerpage.assertFalse(cpStatus, "Verify that the actual and Expected image are not same","The actual and Expected image are not same.");
//		ExtentManager.customExtentReportLog(PASS, extentTest, "Checkpoint[5/6]", "Verifying INVERT is applied on images");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying INVERT is applied on images from WW and WC values" );
//		viewerpage.compareElementImage(protocolName, viewerpage.viewboxImage1,"Verify that the WW values are not same after INVERT selection","test04_E2E_TC1389_verifyInvertPreset_checkpoint1" );
//
//		//		viewerpage.assertNotEquals(beforeInvertWWValue, afterInvertWWValue, "Verify that the WW values are not same after INVERT selection", "WW values are not same after INVERT selection");
//		//		viewerpage.assertNotEquals(beforeInvertWCValue, afterInvertWCValue, "Verify that the WC values are not same after INVERT selection", "WC values are not same after INVERT selection");
//	}
//
//	//Not work in IE and edge because of mouse move activity 
//	@Test(groups ={"firefox","Chrome"})
//	public void test05_E2E_TC1389_verifyResetPreset() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying RESET preset");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Loading the Patient data in default overlay" );
//		viewerpage.selectTextOverlays("default");
//		viewerpage.waitForDefaultOverlayDisplay(1);
//		viewerpage.getWindowCenterLabelOverlay(1).click();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying that selected label is Italic" );
//		viewerpage.assertTrue(viewerpage.verifyFontForPresetSelectedText(viewerpage.getWindowCenterLabelOverlay(1),viewerpage.valueRESET,1), "Verify that the RESET is displaying in Italic font", "RESET is displaying in Italic font");
//
//		//Step 5
//		String beforeResetWWValue = viewerpage.getValueOfWindowWidth(1);
//		String beforeResetWCValue = viewerpage.getValueOfWindowCenter(1);
//
//		viewerpage.inputWWNumber(500, 1);
//		viewerpage.inputWCNumber(60, 1);
//
//		viewerpage.selectPresetValue(viewerpage.getWindowCenterLabelOverlay(1), ViewerPageConstants.RESET,1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying that selected label is Italic" );
//		viewerpage.assertTrue(viewerpage.verifyFontForPresetSelectedText(viewerpage.getWindowCenterLabelOverlay(1),viewerpage.valueRESET,1), "Verify that the RESET is displaying in Italic font", "RESET is displaying in Italic font");
//
//		String afterResetWWValue = viewerpage.getValueOfWindowWidth(1);
//		String afterResetWCValue = viewerpage.getValueOfWindowCenter(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that after mouse movement from selection hides the pop up menu" );
//		viewerpage.assertFalse(viewerpage.OverlayMenuTable.getCssValue("display").trim().equalsIgnoreCase("block"), "Verify the prsesnce of Preset table", "Preset table is not present");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying RESET is applied on images from WW and WC values" );
//		//Verifying values after selecting RESET
//		viewerpage.assertEquals(beforeResetWWValue, afterResetWWValue, "Verify that the WW values are same after RESET selection", "WW values are same after RESET selection");
//		viewerpage.assertEquals(beforeResetWCValue, afterResetWCValue, "Verify that the WW values are same after RESET selection", "WC values are same after RESET selection");
//
//	}
//
//	//Not work in IE and edge because of mouse move activity 
//	@Test(groups ={"firefox","Chrome"})
//	public void test06_E2E_TC1389_verifyWCOverlayWithAndWithoutSync() throws InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying WW and WC values for images in sync and images without sync");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Loading the Patient data in full overlay" );
//		viewerpage.selectTextOverlays("full");
//		viewerpage.waitForFullOverlayDisplay(1);
//		//Step - 7 8 9
//		String beforeWWSyncValue = viewerpage.getValueOfWindowWidth(1);
//		String beforeWCSyncValue = viewerpage.getValueOfWindowCenter(1);
//
//		viewerpage.inputWWNumber(500, 1);
//
//		viewerpage.inputWCNumber(60, 1);
//		String currentWWSyncValue = viewerpage.getValueOfWindowWidth(1);
//		String currentWCSyncValue = viewerpage.getValueOfWindowCenter(1);
//
//		String currentWWSyncValue2 = viewerpage.getValueOfWindowWidth(2);
//		String currentWCSyncValue2 = viewerpage.getValueOfWindowCenter(2);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying that the WW and WC values are not same after inputing different values for WW and WC" );
//		//Verifying that the WW and WC values are changed		
//		viewerpage.assertNotEquals(beforeWWSyncValue, currentWWSyncValue, "Verify that the WW values are changes to new values", "WW values are not same as previous");
//		viewerpage.assertNotEquals(beforeWCSyncValue, currentWCSyncValue, "Verify that the WC values are changes to new values", "WC values are not same as previous");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying that the WW and WC values are same for sync series" );
//		//Verifying that Synch ON applies changes to linked series
//		viewerpage.assertEquals(currentWWSyncValue, currentWWSyncValue2, "Verify that the WW values are same for sync series", "WW values are same for sync series");
//		viewerpage.assertEquals(currentWCSyncValue, currentWCSyncValue2, "Verify that the WC values are same for sync series", "WC values are same for sync series");
//
//		//Without sync-
//		//performing sync off
//		viewerpage.performSyncONorOFF();
//		//Perform WC and WW
//		viewerpage.inputWWNumber(100, 1);
//		viewerpage.inputWCNumber(50, 1);
//		String afterWWSyncValue = viewerpage.getValueOfWindowWidth(1);;
//		String afterWCSyncValue = viewerpage.getValueOfWindowCenter(1);
//		String afterWWSyncValue2 = viewerpage.getValueOfWindowWidth(2);
//		String afterWCSyncValue2 = viewerpage.getValueOfWindowCenter(2);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the WW and WC values are not same after inputing different values for WW and WC" );
//		//Verifying that the WW and WC values are changed		
//		viewerpage.assertNotEquals(afterWWSyncValue, currentWWSyncValue, "Verify that the WW values are changes to new values", "WW values are not same as previous");
//		viewerpage.assertNotEquals(afterWCSyncValue, currentWCSyncValue, "Verify that the WC values are changes to new values", "WC values are not same as previous");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying that the WW and WC values are not change for series not in sync" );
//		//Validate the WW and WC values in both viewports
//		viewerpage.assertEquals(currentWWSyncValue2, afterWWSyncValue2, "Verify that the WW values are not same for without sync series", "WW values are not same for without sync series");
//		viewerpage.assertEquals(currentWCSyncValue2, afterWCSyncValue2, "Verify that the WC values are not same for without sync series", "WC values are not same for without sync series");
//
//	}
//
//	//Not work in IE and edge because of mouse move activity 
//	@Test(groups ={"firefox","Chrome"})
//	public void test07_E2E_TC1389_verifyWCOverlayForIndSeries() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying WW and WC values for individual series");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Loading the Patient data in full overlay" );
//		viewerpage.selectTextOverlays("full");
//		viewerpage.waitForFullOverlayDisplay(1);
//		//Step - 11
//		String beforeWWValue = viewerpage.getValueOfWindowWidth(1);
//		String beforeWCValue = viewerpage.getValueOfWindowCenter(1);
//
//		viewerpage.inputWWNumber(100, 1);
//		viewerpage.inputWCNumber(40, 1);
//		String afterWWValue = viewerpage.getValueOfWindowWidth(1);
//		String afterWCValue = viewerpage.getValueOfWindowCenter(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that the WW and WC values are not same after inputing different values for WW and WC" );
//		viewerpage.assertNotEquals(beforeWWValue, afterWWValue, "Verify that the WW values are changes to new values", "WL values are changes to new values and not same as previous");
//		viewerpage.assertNotEquals(beforeWCValue, afterWCValue, "Verify that the WC values are changes to new values", "WC values are changes to new values and not same as previous");
//
//	}
//
//	//Not work in IE and edge because of mouse move activity 
//	@Test(groups ={"firefox","Chrome"})
//	public void test08_E2E_TC1389_verifyZoomOverlay() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying zoom overlay");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Loading the Patient data in full overlay" );
//		viewerpage.selectTextOverlays("full");
//		viewerpage.waitForFullOverlayDisplay(1);
//		viewerpage.doubleClickJs(viewerpage.viewboxImg1);
//		viewerpage.waitForViewerpageToLoad();
//		//Zoom
//		int beforeZoom = viewerpage.getZoomLevel(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying the presence of Zoom context table" );
//		viewerpage.assertTrue(viewerpage.verifyPresenceOfContextMenu(1, viewerpage.getZoomLabelOverlay(1), viewerpage.OverlayMenuTable), "Verify the presence of Zoom context table", "Zoom context table is displaying");
//
//		//Step - 12
//		viewerpage.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);
//		int Zoom100 = viewerpage.getZoomLevel(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying that the Zoom value is set to 'Zoom to 100%'" );
//		viewerpage.assertEquals(Zoom100, 100, "Verify zoom to 100%", "Zoom value is updated to 100%");
//
//		//Step - 13
//		viewerpage.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_FIT);
//		int afterZoom = viewerpage.getZoomLevel(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that the Zoom value is set to 'Zoom to Fit'" );
//		viewerpage.assertEquals(beforeZoom, afterZoom, "Verify zoom to fit", "Zoom value is updated to 'zoom to fit' value");
//
//		//Step - 14
//		viewerpage.inputZoomNumber(500,1);
//		int CurrZoom = viewerpage.getZoomLevel(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying that the Zoom value is updated to new value" );
//		viewerpage.assertEquals(CurrZoom, 500, "Verify that the zoom is updated to new value", "Zoom is update to new value");
//
//	}
//
//	//Not work in IE and edge because of mouse move activity 
//	@Test(groups ={"firefox","Chrome"})
//	public void test09_E2E_TC1389_verifyImageNumOverlay() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Workflow - Verifying Image number overlay");
//
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		//1. Full Overlay
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Loading the Patient data in full overlay" );
//		viewerpage.selectTextOverlays("full");
//		viewerpage.waitForFullOverlayDisplay(1);
//		viewerpage.doubleClickJs(viewerpage.viewboxImg1);
//		viewerpage.waitForViewerpageToLoad();
//
//		//Step - 15 16
//		viewerpage.inputImageNumber(20,1);
//		int ChangeImageNum = Integer.parseInt(viewerpage.getImageCurrentScrollPositionOverlay(1).getText().trim());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying that the Image number value is updated to new valid value" );
//		viewerpage.assertEquals(ChangeImageNum, 20 ,"Verify that the image number is updated to new value", "Image number is update to new value");
//
//		viewerpage.inputImageNumber(100,1);
//		int invalidChangeNum = Integer.parseInt(viewerpage.getImageCurrentScrollPositionOverlay(1).getText().trim());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying that the Image number value is not updated to invalid value" );
//		viewerpage.assertNotEquals(100, invalidChangeNum, "Verify that the image number is not updated to invalid value", "The image number is not updated to invalid value");
//	}
//}
